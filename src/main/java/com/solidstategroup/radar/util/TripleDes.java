package com.solidstategroup.radar.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * TripleDES implementation copied from
 * http://stackoverflow.com/questions/20227/how-do-i-use-3des-encryption-decryption-in-java
 */
public class TripleDes {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String CIPHER_NAME = "DESede/CBC/PKCS5Padding";
    private static final String KEY_SPEC = "DESede";

    // These were copied from the old code - file TripleDES.vb
    private static final byte[] keyBytes =
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    // I think the last few values overflow and that's how they were in VB, going to try same here
    private static final byte[] ivBytes = {65, 110, 68, 26, 69, (byte) 178, (byte) 200, (byte) 219};

    public static String encrypt(String message) throws Exception {
        final Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        final byte[] plainTextBytes = message.getBytes(CHARSET_NAME);
        byte[] cipherText = cipher.doFinal(plainTextBytes);
        return new BASE64Encoder().encode(cipherText);
    }

    public static String decrypt(String message) throws Exception {
        byte[] messageBytes = new BASE64Decoder().decodeBuffer(message);

        final Cipher decipher = getCipher(Cipher.DECRYPT_MODE);
        return new String(decipher.doFinal(messageBytes), CHARSET_NAME);
    }

    private static Cipher getCipher(int mode)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException {
        final SecretKey key = new SecretKeySpec(keyBytes, KEY_SPEC);
        final IvParameterSpec iv = new IvParameterSpec(ivBytes);
        final Cipher decipher = Cipher.getInstance(CIPHER_NAME);
        decipher.init(mode, key, iv);
        return decipher;
    }

}
