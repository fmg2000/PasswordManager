package com.example.PasswordManager.user.service;

import com.example.PasswordManager.auth.dto.UserDTO;
import com.example.PasswordManager.user.dto.UserCryptDTO;
import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserbyName(String name) throws Exception {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            return null;
        }
        return userRepository.findByUsername(name);


    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public UserCryptDTO getCryptoMetaFor(Principal p) throws Exception {
        User user = userRepository.findByUsername(p.getName());
        if (user == null) {
            throw new Exception("User not found");
        }
        return new UserCryptDTO(user.getSaltMaster(), user.getVerifyCyper(), user.getVerifyIv());
    }

    public void updateEncrypt(UserCryptDTO userCryptDTO, User user) {

        user.setSaltMaster(userCryptDTO.getSaltMaster());
        user.setVerifyIv(userCryptDTO.getVerifyIv());
        user.setVerifyCyper(userCryptDTO.getVerifyCyper());
        userRepository.save(user);
    }
}
