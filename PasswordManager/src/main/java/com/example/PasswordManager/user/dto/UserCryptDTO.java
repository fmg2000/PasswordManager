package com.example.PasswordManager.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCryptDTO {
    private String saltMaster;
    private String verifyCyper;
    private String verifyIv;
}
