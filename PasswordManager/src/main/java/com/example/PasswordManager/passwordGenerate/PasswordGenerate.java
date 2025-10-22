package com.example.PasswordManager.passwordGenerate;

import java.security.SecureRandom;

public class PasswordGenerate {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+<>?";
    private static final String ALL = LOWERCASE + UPPERCASE + DIGITS + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    public static String generate(){

        /**Random*/
        int length = random.nextInt(8,64)-4;
        StringBuilder pass = new StringBuilder();

        while(length > 0){
            pass.append(ALL.charAt(random.nextInt(ALL.length())));
            length--;
        }

        pass.insert(random.nextInt(pass.length()),LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        pass.insert(random.nextInt(pass.length()),UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        pass.insert(random.nextInt(pass.length()),DIGITS.charAt(random.nextInt(DIGITS.length())));
        pass.insert(random.nextInt(pass.length()),SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        return pass.toString();
    }

    public static String generateNITS(){

        int length = random.nextInt(8,64);
        StringBuilder pass = new StringBuilder();

        while(length > 0) {
            pass.append((char) random.nextInt(33, 127));
            length--;
        }
        return pass.toString();
    }
}
