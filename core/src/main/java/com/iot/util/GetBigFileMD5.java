package com.iot.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetBigFileMD5 {

    static MessageDigest MD5 = null;


    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ne) {
            ne.printStackTrace();
        }
    }


    /**
     * 对一个文件获取md5值
     *
     * @return md5串
     */
    public static String getMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            return getMD5(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMD5(InputStream inputStream) {
        try {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMd5FromUrl(String netUrl) {
        InputStream inputStream = null;
        try {
            URL url = new URL(netUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoInput(true);
            inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 求一个字符串的md5值
     *
     * @param target 字符串
     * @return md5 value
     */
    public static String MD5(String target) {
        return DigestUtils.md5Hex(target);
    }


    public static void main(String[] args) {

        long beginTime = System.currentTimeMillis();
        String url = "https://leedarson-bucket-00001.s3.cn-north-1.amazonaws.com.cn/0/zip/497cf836395e44f8a15fef1178971c3c.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180523T113510Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86399&X-Amz-Credential=AKIAPHLS3Q5HE2XCECOQ%2F20180523%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Signature=5bda2d210d36c2ac7dd16fe3b8a822e9d51ab69419c41a87d98ee11c6a4a30d5";
        String md5 = getMd5FromUrl(url);
//        md5 = getMD5(new File("C:\\Users\\xufuzhou\\Downloads\\8dc5bab19db64a52af52e5a0394b444c.bin"));
        long endTime = System.currentTimeMillis();
        System.out.println("MD5:" + md5 + "\n time:" + ((endTime - beginTime) / 1000) + "s");
    }
}