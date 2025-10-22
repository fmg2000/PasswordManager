package com.example.PasswordManager.util;

import org.springframework.stereotype.Component;

public class WeekPassword {
    /// (?= ...) se asigura ca este
    private static final String regEx = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}$";

    public static boolean passwordVerify(String pass){
        return pass.matches(regEx);
    }
}
