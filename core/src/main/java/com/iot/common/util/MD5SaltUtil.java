package com.iot.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5SaltUtil {

	/**
	 * 
	 * 描述：校验加盐后是否和原文一致
	 * 
	 * @author 李帅
	 * @created 2018年6月25日 下午6:34:30
	 * @since
	 * @param password
	 * @param md5
	 * @return
	 */
	public static boolean verify(String password, String md5Salt) {
		String md5 = md5Salt.substring(0, 8) + md5Salt.substring(11, 19) + md5Salt.substring(22, 30)
				+ md5Salt.substring(33, 41);
		// System.out.println("普通MD5后:" + new String(cs1));
		String salt = md5Salt.substring(8, 11) + md5Salt.substring(19, 22) + md5Salt.substring(30, 33)
				+ md5Salt.substring(41, 44);
		// System.out.println("strA:" + salt.substring(0,8));
		return MD5(password + salt.substring(0, 8)).equals(md5);
	}

	/**
	 * 
	 * 描述：加盐MD5
	 * 
	 * @author 李帅
	 * @created 2018年6月25日 下午5:30:29
	 * @since
	 * @param password
	 * @return
	 */
	public static String generate(String password) {
		String strA = StringUtil.getRandomString(8);
		// System.out.println("strA：" + strA);
		String strB = StringUtil.getRandomString(4);
		// System.out.println("strB：" + strB);
		String strC = strA + strB;
		String md5 = MD5SaltUtil.MD5(password + strA);
		// System.out.println("普通MD5后：" + md5);
		String salt = md5.substring(0, 8) + strC.substring(0, 3) + md5.substring(8, 16) + strC.substring(3, 6)
				+ md5.substring(16, 24) + strC.substring(6, 9) + md5.substring(24, 32) + strC.substring(9, 12);
		return salt;
	}

	/**
	 * 
	 * 描述：普通MD5
	 * 
	 * @author 李帅
	 * @created 2018年6月25日 下午5:26:46
	 * @since
	 * @param input
	 * @return
	 */
	public static String MD5(String input) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "check jdk";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = input.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 测试主函数
	 */
	public static void main(String args[]) {
		// 原文
		String plaintext = "1ca765b320a4c044420c1207887fb6d2";
		System.out.println("原始：" + plaintext);
		// 获取加盐后的MD5值
		String ciphertext = MD5SaltUtil.generate(plaintext);
		System.out.println("加盐后MD5：" + ciphertext);

		System.out.println("是否是同一字符串:" + verify(plaintext, ciphertext));
	}
}
