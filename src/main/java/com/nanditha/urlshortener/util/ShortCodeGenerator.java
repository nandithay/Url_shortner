package com.nanditha.urlshortener.util;

import java.security.SecureRandom;

public final class ShortCodeGenerator {

    private static final String BASE62_CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private ShortCodeGenerator() {
    }

    public static String generate() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(BASE62_CHARS.charAt(SECURE_RANDOM.nextInt(BASE62_CHARS.length())));
        }
        return code.toString();
    }
}
