package com.example.demo.utils;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;

public class EncryptionUtil {

    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
