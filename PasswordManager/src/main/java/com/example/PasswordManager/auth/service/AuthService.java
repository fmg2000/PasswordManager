package com.example.PasswordManager.auth.service;

import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.auth.dto.UserDTO;
import com.example.PasswordManager.user.repo.UserRepository;
import com.example.PasswordManager.user.service.UserService;
import com.example.PasswordManager.util.ValidName;
import com.example.PasswordManager.util.WeekPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;
/**
 * Serviciu care gestionează logica de autentificare și înregistrare.
 * Aici se fac validările pentru username și password și se salvează utilizatorul în baza de date.
 */
@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Înregistrează un nou utilizator în baza de date.
     * Face validarea username-ului și parolei, apoi salvează utilizatorul cu parola criptată.
     */
    public String registerUser(UserDTO userDTO) throws Exception {

        if(userService.findUserbyName(userDTO.getUsername())!=null)
            return "UserName already existed";
        if(!ValidName.verifyName(userDTO.getUsername()))
            return "Username must start with a letter";
        if(!WeekPassword.passwordVerify(userDTO.getPassword()))
            return "Password is to week";

        // cream un obiect User deoarece noi primim obiectul userDTO
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword())); //codificam parola inainte de adaugare in db
        userService.saveUser(user); //save
        return "Registration successful";
    }
}







//Un generator criptografic este o „mașină” care scoate numere
// random de așa fel încât să nu poată fi prezise sau reconstituite,
// chiar dacă cineva știe algoritmul.
// Este fundamental pentru criptografie, pentru că totul (chei, salt-uri, nonce-uri) trebuie să fie imprevizibile.
