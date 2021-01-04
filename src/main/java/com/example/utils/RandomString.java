package com.example.utils;

import java.security.SecureRandom;

public class RandomString {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHA_NUMS = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String get(long size) {
        return SECURE_RANDOM.ints(size, 0, ALPHA_NUMS.length())
                .mapToObj(ALPHA_NUMS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
