package com.example.PasswordManager.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class Sha256PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        ///  Cream hash cu MessageDigest, rezultatul din byte il transformam cu hexa
        /// hexString.append(String.format("%02x", b)); %x transformare hexa,2 - minim 2 caractere , 0 - se pune zero daca trebuie in fata
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        /// Cream din nou hash-ul si il comparam cu cel din DB
        String hashedInput = encode(rawPassword);
        return hashedInput.equals(encodedPassword);
    }
}