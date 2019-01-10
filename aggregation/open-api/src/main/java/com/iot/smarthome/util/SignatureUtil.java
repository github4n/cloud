package com.iot.smarthome.util;

import com.iot.smarthome.vo.SignHashMap;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/19 14:17
 * @Modify by:
 */
public class SignatureUtil {
    private final static String HMAC_SHA256 = "HmacSHA256";
    private final static String HMAC_SHA1 = "HmacSHA1";

    public static String getSignatureContent(SignHashMap paramMap) {
        return getSignContent(getSortedMap(paramMap));
    }

    public static Map<String, String> getSortedMap(SignHashMap signHashMap) {
        TreeMap sortedParams = new TreeMap();
        if (signHashMap != null && signHashMap.size() > 0) {
            sortedParams.putAll(signHashMap);
        }
        return sortedParams;
    }

    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        ArrayList keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;

        for (int i = 0; i < keys.size(); ++i) {
            String key = (String) keys.get(i);
            String value = sortedParams.get(key);
            if (SignHashMap.areNotEmpty(new String[]{key, value})) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }

        return content.toString();
    }

    // ************** HMAC SHA256
    public static String getSha256Sign(String content, String clientSecret) throws Exception {
        byte[] bytes = HmacSHA256Encrypt(content, clientSecret);
        //BASE64Encoder encoder = new BASE64Encoder();
        //return encoder.encode(bytes);
        return new String(Base64.encodeBase64(bytes));
    }

    public static byte[] HmacSHA256Encrypt(String encryptText, String encryptKey) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), HMAC_SHA256);
        mac.init(secretKey);
        byte[] text = encryptText.getBytes();
        byte[] bytes = mac.doFinal(text);
        return bytes;
    }

    // ************** HMAC SHA1
    public static String getSign(String content, String clientSecret) throws Exception {
        byte[] bytes = HmacSHA1Encrypt(content, clientSecret);
        //BASE64Encoder encoder = new BASE64Encoder();
        //return encoder.encode(bytes);
        return null;
    }

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), HMAC_SHA1);

        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(secretKey);
        byte[] text = encryptText.getBytes();
        byte[] bytes = mac.doFinal(text);
        return bytes;
    }
}
