package com.iot.airswitch.util;

import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Calendar;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public class HexUtil {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 将byte[]转换为16进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    public static String bytesToUdpCmd(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        String result = new String(buf);
        if (Strings.isNullOrEmpty(result)) {
            return null;
        }
        int size = AnalysisDataUtil.getCmdLength(result);
        return StringUtils.substring(result, 0, size);
    }

    /**
     * 将16进制字符串转换为byte[]
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /**
     * 将命令转换字节
     */
    public static byte[] cmdToByte(String cmd) {
        byte[] buf = new byte[0];
        cmd = StringUtils.replacePattern(cmd, " ", "");
        for (int i=0; i<cmd.length()/2; i++) {
            if (cmd.length() < (i+1)*2) {
                continue;
            }
            String subStr = cmd.substring(i*2, (i+1)*2);
            buf = Bytes.concat(buf, HexUtil.toBytes(subStr));
        }
        return buf;
    }

    /**
     * 获取16进制的当前时间
     */
    public static String getCurrentDateToHex() {
        Long time = Calendar.getInstance().getTimeInMillis()/1000;
        return Integer.toHexString(time.intValue());
    }

    /**
     * 获取data的长度
     */
    public static String getLen(String data, int size) {
        String len = null;
        if (Strings.isNullOrEmpty(data)) {
            len = Integer.toHexString(0);
        } else {
            len = Integer.toHexString(data.length()/2);
        }

        if (len.length() == size*2) {
            return len;
        }
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<(size*2-len.length()); i++) {
            sb.append(0);
        }
        sb.append(len);

        return sb.toString();
    }

    /**
     * 获取随机的消息系列号
     */
    public static String getCmdNo() {

        String no = Integer.toHexString(RandomUtils.nextInt(1, 1000));
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<(4-no.length()); i++) {
            sb.append(0);
        }
        sb.append(no);
        return sb.toString();
    }

    /**
     * 16进制转ip
     * 格式 ：192.168.1.1
     */
    public static String hexToIp(String hex) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<(hex.length()/2); i++) {
            if (!Strings.isNullOrEmpty(sb.toString())) {
                sb.append(".");
            }
            sb.append(Integer.parseInt(hex.substring(i*2, (i+1)*2), 16));
        }
        return sb.toString();
    }

    /**
     * 16进制转2进制
     */
    public static String hexToBinary(String hex) {
        StringBuffer sb = new StringBuffer();
        for (char c : hex.toCharArray()) {
            String num = Integer.toBinaryString(Integer.parseInt(String.valueOf(c), 16));
            if (num.length() == 4) {
                sb.append(num);
            } else {
                for (int i=0; i<(4-num.length()); i++) {
                    sb.append(0);
                }
                sb.append(num);
            }
        }
        return sb.reverse().toString();
    }

    public static String intToHex(Integer value, int size) {
        String hex = Integer.toHexString(value);
        if (hex.length() == size) {
            return hex;
        }
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<size-hex.length(); i++) {
            sb.append(0);
        }
        sb.append(hex);
        return sb.toString().toUpperCase();
    }

    public static String ipToHex(String ip) {
        if (Strings.isNullOrEmpty(ip)) {
            return null;
        }
        String[] ips = StringUtils.split(ip, ".");
        StringBuffer sb = new StringBuffer();
        for (String i : ips) {
            sb.append(intToHex(Integer.parseInt(i), 2));
        }
        return sb.toString().toUpperCase();
    }

    public static String intToHexAuto(Integer i) {
        String hex = Integer.toHexString(i);
        if (hex.length() % 2 == 0) {
            return hex.toUpperCase();
        } else {
            return ("0" + hex).toUpperCase();
        }
    }

    /**
     * 转化设备名称
     * 1.16进制转字符串
     * 2.字符串解码
     */
    public static String hexToStr(String encodeName) {
        int size = StringUtils.indexOf(encodeName, "FF");
        if (size == 0) {
            return null;
        }
        if (size % 2 == 1) {
            size += 1;
        }
        String nName = StringUtils.substring(encodeName, 0, size);
        byte[] bytes = toBytes(nName);
        String hex = new String(bytes);
        return URLDecoder.decode(hex);
    }

    /**
     * 将名称转化为16进制, 不足就补F（48位）
     */
    public static String nameToHex(String name) {
        try {
            String nName = URLEncoder.encode(name, "utf-8");
            String hex = bytesToHex(nName.getBytes());
            if (hex.length() > 48) {
                return "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
            }
            int size = 48 - hex.length();
            for (int i=0; i<size; i++) {
                hex += "F";
            }
            return hex;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
    }

    public static void main(String[] args) throws UnsupportedEncodingException, ParseException {

        String o = "80011040";

        System.out.println(hexToBinary(o));

    }

}
