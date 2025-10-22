package com.example.PasswordManager.util;

public class ValidName {

    private static final String regEx = "^[A-Za-z].*";

    public static boolean verifyName(String name){

        if(name!=null)
            return name.matches(regEx);
        return false;
    }
}
