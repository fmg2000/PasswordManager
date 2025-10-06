package com.example.PasswordManager.auth.service;

import com.example.PasswordManager.auth.model.User;
import com.example.PasswordManager.auth.model.UserDTO;
import com.example.PasswordManager.auth.repo.UserRepository;
import com.example.PasswordManager.util.WeekPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeekPassword weekPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(UserDTO userDTO) {

        System.out.println(userDTO.toString());
        if(userRepository.findByUsername(userDTO.getUsername())!=null)
            return "UserName already existed";

        if(!weekPassword.passwordVerify(userDTO.getPassword()))
            return "Password is to week";

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        return "Registration successful";
    }
}
