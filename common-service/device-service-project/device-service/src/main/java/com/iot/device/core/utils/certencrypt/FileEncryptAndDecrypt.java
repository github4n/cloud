package com.iot.device.core.utils.certencrypt;

import java.io.*;

public class FileEncryptAndDecrypt {

	

	/**
	 * @param fileName
	 * @param content
	 */

	public static void appendMethodA(String fileName, String content) {

		try {
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String decrypt(String fileUrl, String tempUrl, int keyLength) throws Exception {
		File file = new File(fileUrl);
		if (!file.exists()) {
			return null;
		}
		File dest = new File(tempUrl);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		InputStream is = new FileInputStream(fileUrl);
		OutputStream out = new FileOutputStream(tempUrl);
		byte[] buffer = new byte[1024];
		byte[] buffer2 = new byte[1024];
		byte bMax = (byte) 255;
		long size = file.length() - keyLength;
		int mod = (int) (size % 1024);
		int div = (int) (size >> 10);
		int count = mod == 0 ? div : (div + 1);
		int k = 1, r;
		while ((k <= count && (r = is.read(buffer)) > 0)) {
			if (mod != 0 && k == count) {
				r = mod;
			}
			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 0 ? bMax : --b;
			}
			out.write(buffer2, 0, r);
			k++;
		}
		out.close();
		is.close();
		return tempUrl;

	}

	public static String readFileLastByte(String fileName, int keyLength) {
		File file = new File(fileName);
		if (!file.exists())
			return null;
		StringBuffer str = new StringBuffer();
		try {
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");
			long fileLength = randomFile.length();
			for (int i = keyLength; i >= 1; i--) {
				randomFile.seek(fileLength - i);
				str.append((char) randomFile.read());
			}
			randomFile.close();
			return str.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
