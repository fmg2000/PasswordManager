package com.example.PasswordManager.auth.service;

import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.auth.dto.UserDTO;
import com.example.PasswordManager.user.repo.UserRepository;
import com.example.PasswordManager.user.service.UserService;
import com.example.PasswordManager.util.WeekPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private WeekPassword weekPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(UserDTO userDTO) throws Exception {


        if(userService.findUserbyName(userDTO.getUsername())!=null)
            return "UserName already existed";

        if(!weekPassword.passwordVerify(userDTO.getPassword()))
            return "Password is to week";

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        userService.saveUser(user);
        return "Registration successful";
    }
}

//Un generator criptografic este o „mașină” care scoate numere
// random de așa fel încât să nu poată fi prezise sau reconstituite,
// chiar dacă cineva știe algoritmul.
// Este fundamental pentru criptografie, pentru că totul (chei, salt-uri, nonce-uri) trebuie să fie imprevizibile.
