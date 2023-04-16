package com.nghianh.security.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class TokenUtil {

	public static String encrypt(String plainText, String strSecretKey) throws Exception {
		SecretKeySpec secretKey = createKey(strSecretKey);
		Cipher cipher = Cipher.getInstance("AES");
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		return Base64.getUrlEncoder().encodeToString(encryptedByte);
	}

	public static String decrypt(String encryptedText, String strSecretKey) throws Exception {
		SecretKeySpec secretKey = createKey(strSecretKey);
		Cipher cipher = Cipher.getInstance("AES");
		byte[] encryptedTextByte = Base64.getUrlDecoder().decode(encryptedText);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		return new String(decryptedByte);
	}

	private static SecretKeySpec createKey(String myKey) {
		MessageDigest sha;
		try {
			byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			return new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
