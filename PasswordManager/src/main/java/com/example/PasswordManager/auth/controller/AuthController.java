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

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO){

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

            return ResponseEntity.ok(jwtService.generateToke(userDTO.getUsername()));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Username or password invalid");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) throws Exception {
        String result = authService.registerUser(userDTO);
        if (result.equals("Registration successful")) {
            return ResponseEntity.ok(result); // HTTP 200
        } else {
            return ResponseEntity.badRequest().body(result); // HTTP 400
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Void> check(){
        return ResponseEntity.ok().build();
    }

}
