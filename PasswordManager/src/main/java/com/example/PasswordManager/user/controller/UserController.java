package com.example.PasswordManager.user.controller;

import com.example.PasswordManager.auth.dto.UserDTO;
import com.example.PasswordManager.user.dto.UserCryptDTO;
import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/meta")
    public ResponseEntity<UserCryptDTO> getCryptoMetaFor(Principal p) throws Exception {
        UserCryptDTO userCryptDTO = userService.getCryptoMetaFor(p);
        return ResponseEntity.ok(userCryptDTO);
    }

    @PostMapping("/encript")
    public ResponseEntity<UserCryptDTO> updateEncrypt(@RequestBody UserCryptDTO userCryptDTO, Principal p) throws Exception {
        User user = userService.findUserbyName(p.getName());
        userService.updateEncrypt(userCryptDTO,user);
        return ResponseEntity.ok(userCryptDTO);
    }
}
