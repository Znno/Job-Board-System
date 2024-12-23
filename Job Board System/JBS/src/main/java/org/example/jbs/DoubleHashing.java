package org.example.jbs;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DoubleHashing {

    public static String doubleHash(String input) {
        try {
            // Get an instance of SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the first hash
            byte[] firstHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Perform the second hash on the result of the first hash
            byte[] secondHash = digest.digest(firstHash);

            // Convert the resulting byte array to a hexadecimal string
            return bytesToHex(secondHash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    // Helper method to convert byte array to hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
