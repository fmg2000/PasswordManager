package com.example.PasswordManager.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtru care rulează o singură dată pe request (OncePerRequestFilter).
 * - extrage JWT din header-ul Authorization: Bearer <token>
 * - validează token-ul
 * - dacă e valid, setează Authentication în SecurityContext
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    /**
     * 1) citește header-ul Authorization
     * 2) extrage token-ul și username-ul
     * 3) dacă nu există Authentication setat, încarcă user-ul și validează token-ul
     * 4) setează Authentication în SecurityContext și continuă lanțul
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String userName = null;

            // Header-ul trebuie să arate așa: Authorization: Bearer <JWT>
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userName = jwtService.extractUserName(token);
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(UserDetailsService.class).loadUserByUsername(userName); //userprincipal

                /****Aplicatia - STATELESS (JWT)
                 nu există sesiune server-side în care Spring să păstreze utilizatorul logat între request-uri.
                 După ce s-a terminat un request, SecurityContext se goleşte. La următorul request e din nou gol.*/
                if (jwtService.validateToken(token, userDetails)) { // if ii token valid atunci ii spunem ca user authentificat
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
