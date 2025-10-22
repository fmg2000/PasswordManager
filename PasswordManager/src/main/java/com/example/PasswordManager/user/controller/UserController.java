package com.example.PasswordManager.user.controller;

import com.example.PasswordManager.auth.dto.UserDTO;
import com.example.PasswordManager.user.dto.UserCryptDTO;
import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller care gestionează cererile pentru meta si
 * Expune endpoint-urile: /user/meta, /user/encript
 */

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * Returnează metadatele criptografice (salt, verifyCyper, verifyIv) asociate utilizatorului autentificat.
     *
     * @param p Principal - obiectul care conține informațiile utilizatorului autentificat
     * @return UserCryptDTO - metadatele criptografice ale utilizatorului
     * @throws Exception dacă utilizatorul nu este găsit
     */

    @GetMapping("/meta")
    public ResponseEntity<UserCryptDTO> getCryptoMetaFor(Principal p) throws Exception {
        UserCryptDTO userCryptDTO = userService.getCryptoMetaFor(p);
        return ResponseEntity.ok(userCryptDTO);
    }

    /**
     * Actualizează informațiile criptografice ale utilizatorului (saltMaster, verifyIv, verifyCyper)în baza de date, folosind datele primite din corpul cererii.
     *
     * @param userCryptDTO - obiect ce conține noile informații criptografice
     * @param p Principal - utilizatorul autentificat
     * @return UserCryptDTO - obiectul actualizat
     * @throws Exception dacă utilizatorul nu este găsit
     */

    @PutMapping("/encript")
    public ResponseEntity<UserCryptDTO> updateEncrypt(@RequestBody UserCryptDTO userCryptDTO, Principal p) throws Exception {
        User user = userService.findUserbyName(p.getName());
        userService.updateEncrypt(userCryptDTO,user);
        return ResponseEntity.ok(userCryptDTO);
    }


}
