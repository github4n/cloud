package com.iot.user.util;

import java.security.MessageDigest;
import java.util.UUID;

public class MD5Generator {
    private static final char[] HEXCODE = "0123456789abcdef".toCharArray();

    public String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    public static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEXCODE[b >> 4 & 0xF]);
            r.append(HEXCODE[b & 0xF]);
        }
        return r.toString();
    }

    public String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes("utf-8"));
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new RuntimeException("OAuth Token cannot be generated.", e);
        }
    }
}