package com.example.PasswordManager.util;

import org.springframework.stereotype.Component;

@Component
public class WeekPassword {

    private String regEx = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{6,}$";

    public boolean passwordVerify(String pass){
        return pass.matches(regEx);
    }
}
