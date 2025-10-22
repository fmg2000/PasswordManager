package com.example.PasswordManager.auth.controller;
import com.example.PasswordManager.auth.dto.UserDTO;
import com.example.PasswordManager.auth.service.AuthService;
import com.example.PasswordManager.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

/**
 * Controller care gestionează cererile de autentificare și înregistrare.
 * Expune endpoint-urile: /auth/login, /auth/register și /auth/check.
 */

@RestController
@RequestMapping("auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Endpoint pentru autentificarea unui utilizator existent.
     *
     * @param userDTO obiect care conține username și password primite din cererea HTTP
     * @return un token JWT dacă autentificarea reușește, sau un mesaj de eroare dacă nu.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
            return ResponseEntity.ok(jwtService.generateToken(userDTO.getUsername()));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Username or password invalid");
        }
    }

    /**
     * Endpoint pentru înregistrarea unui utilizator nou.
     *
     * @param userDTO obiectul cu datele de înregistrare (username + password)
     * @return mesaj text cu rezultatul procesului de înregistrare (success/fail)
     * @throws Exception în caz de eroare internă
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) throws Exception {
        String result = authService.registerUser(userDTO);
        if (result.equals("Registration successful")) {
            return ResponseEntity.ok(result); // HTTP 200
        } else {
            return ResponseEntity.badRequest().body(result); // HTTP 400
        }
    }

    /**
     * Endpoint simplu de test pentru verificarea conexiunii/token-ului.
     * @return HTTP 200 OK dacă serviciul este accesibil.
     */
    @GetMapping("/check")
    public ResponseEntity<Void> check(){
        return ResponseEntity.ok().build();
    }

}
