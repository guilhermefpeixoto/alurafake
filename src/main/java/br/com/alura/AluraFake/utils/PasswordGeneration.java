package br.com.alura.AluraFake.utils;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGeneration {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String generateEncodedPassword() {
        String password = generatePassword();
        return passwordEncoder.encode(password);
    }

    public static String generatePassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }
}
