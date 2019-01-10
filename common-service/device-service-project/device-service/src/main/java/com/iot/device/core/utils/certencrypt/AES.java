package com.iot.device.core.utils.certencrypt;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.SecureRandom;

public class AES {

	private static final String ALGORITHM = "AES";
	private static final int KEY_SIZE = 128;
	private static final int CACHE_SIZE = 1024;

	public static byte[] getKey() {
		byte [] cc={((2<<2<<3<<2>>3<<2)-(2<<2)),((3<<2<<2<<1<<2>>2<<3>>2)-(2<<2)-(2<<3<<2)+(2>>>1)),(2<<2<<2>>1<<2<<2>>1)-(2<<3<<2<<2>>4)+(1<<1<<1)};
		return cc;
	}

	public static Key getKey(String key) {
		// Key k = toKey(Base64Utils.decode(key));
		Key k = null;
		try {
			// k = toKey(key.getBytes());
			k = toKey(Base64Utils.decode(key));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return k;
	}

	public static String getSecretKey(String seed) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		SecureRandom secureRandom;

		secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(seed.getBytes());

		keyGenerator.init(KEY_SIZE, secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		return Base64Utils.encode(secretKey.getEncoded());
	}



	
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		// Key k = toKey(Base64Utils.decode(key));
		Key k = getKey(key);
		byte[] raw = k.getEncoded();
		SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		return cipher.doFinal(data);
	}

	public static void decryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		if (sourceFile.exists() && sourceFile.isFile()) {
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			destFile.createNewFile();
			FileInputStream in = new FileInputStream(sourceFile);
			FileOutputStream out = new FileOutputStream(destFile);
			// Key k = toKey(Base64Utils.decode(key));
			Key k = getKey(key);
			byte[] raw = k.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			CipherOutputStream cout = new CipherOutputStream(out, cipher);
			byte[] cache = new byte[CACHE_SIZE];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				cout.write(cache, 0, nRead);
				cout.flush();
			}
			cout.close();
			out.close();
			in.close();
		}
	}

	private static Key toKey(byte[] key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return secretKey;
	}

}
