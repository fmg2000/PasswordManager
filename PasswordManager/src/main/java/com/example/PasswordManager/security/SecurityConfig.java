package com.example.PasswordManager.security;


import com.example.PasswordManager.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Configurația principală de securitate Spring Security.
 * - setează autentificarea (UserDetailsService + PasswordEncoder)
 * - definește regulile de autorizare / endpoints publice
 * - setează filtrul JWT și politica de sesiune (stateless)
 * */

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sha256PasswordEncoder();
    }


    ///  Provider care știe cum să autentifice utilizatorul (username + password) din DB
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /// Reguli de Securitate
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(crsfCustomize -> crsfCustomize.disable())
                .cors(cors->{})
                .authorizeHttpRequests(authorizeReq ->authorizeReq
                        .requestMatchers("/auth/register","/auth/login").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// pentru a nu vreau sa mantina aceasi sesiune. tot timpul trimit o alta sesiune
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Expune AuthenticationManager din configurația Spring Security.
     * Folosit la autentificarea în controller (login).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}