package com.iot.device.core.utils.certencrypt;

import java.io.File;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EncryptionFiles {
	
	private static  final Logger logger = LoggerFactory.getLogger(EncryptionFiles.class);

	@SuppressWarnings("unused")
	private static String tmpPath = null;
	private static String tmpo = "k.o";
	private static String tmpa = "k.a";
	private static byte [] _$5 ={49,109,114,115,57,56,114,112,98,53,120,101,50,101,104,112,99,112,100,120,110,56,57,57,54,118,101,108,105,118,51,52,107,120,52,55,113,117,100,113,101,111,103,100,53,114,105,98,57,105,111,51,118,106,51,53,55,98,51,48,101,113,98,115};
	private static byte[]  _$6={((2<<2<<3<<2>>3<<2)-(2<<2)),((3<<2<<2<<1<<2>>2<<3>>2)-(2<<2)-(2<<3<<2)+(2>>>1)),(2<<2<<2>>1<<2<<2>>1)-(2<<3<<2<<2>>4)+(1<<1<<1)};
	private static File ftmp = null;

	private static void reset() {
		tmpo = "k.o";
		tmpa = "k.a";
	}

	@SuppressWarnings("static-access")
	public static int dencryptFile(String src, String target) {
		int rs = 0;
		reset();
		/*
		 * File tf=new File(src); target =targetFileName(tf,target);
		 */
		tmpo = systemHome() + tmpo;
		tmpa = systemHome() + tmpa;
		try {
			AES.decryptFile(AES.getSecretKey(new String(AES.getKey())), src, tmpo);
			rs = 1;
		} catch (Exception e) {
			rs = 0;
			logger.error(e.getMessage(),e);
		}
		if (rs == 0) {
			return rs;
		}
		try {
			FileEncryptAndDecrypt fb = new FileEncryptAndDecrypt();
			fb.decrypt(tmpo, tmpa, _$5.length);
			fb.decrypt(tmpa, tmpo, _$6.length);
		} catch (Exception e1) {
			rs = 0;
			logger.error(e1.getMessage(),e1);
		}
		if (rs == 0) {
			return rs;
		}
		try {
			AES.decryptFile(AES.getSecretKey(new String(AES.getKey())), tmpo, target);
		} catch (Exception e) {
			rs = 0;
			logger.error(e.getMessage(),e);
		}
		try {
			ftmp = new File(tmpo);
			if (ftmp.exists())
				ftmp.delete();

			ftmp = new File(tmpa);
			if (ftmp.exists())
				ftmp.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static String systemHome() {
		Properties props = System.getProperties();
		String home = props.getProperty("user.home");
		if (!home.substring(home.length() - 1, home.length()).equals("\\")) {
			home = home + "\\";
		}
		//return home;
		return "/tmp/";
	}


}
