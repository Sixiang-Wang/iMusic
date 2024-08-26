package com.java.messagepart.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class SecurityUtil {
    private static final String ALGORITHM = "AES";
    private static final String secretKey = "114514114514114514114514";
    public static String encrypt(String password){
        Key key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = null;
        byte[] encryptedByteValue = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedByteValue = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(encryptedByteValue);

    }
    public static String decrypt(String password){
        if (password==null){
            return null;
        }
        Key key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = null;
        byte[] decryptedValue64 = null;
        byte[] decryptedByteValue = null;
        try{
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedValue64 = Base64.getDecoder().decode(password);
            decryptedByteValue = cipher.doFinal(decryptedValue64);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return new String(decryptedByteValue, StandardCharsets.UTF_8);
    }
}
