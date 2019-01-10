package com.iot.airswitch.util;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.CRC32;

/**
 * @Author: Xieby
 * @Date: 2018/10/16
 * @Description: *
 */
public class CRCUtil {


    private static String getCrc(byte[] data) {
        int high;
        int flag;

        // 16位寄存器，所有数位均为1
        int wcrc = 0xffff;
        for (int i = 0; i < data.length; i++) {
            // 16 位寄存器的高位字节
            high = wcrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wcrc = high ^ data[i];

            for (int j = 0; j < 8; j++) {
                flag = wcrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wcrc = wcrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wcrc ^= 0xa001;
            }
        }

        return Integer.toHexString(wcrc);
    }


    public static String crc32Cal(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        Long result = crc32.getValue();
        return Integer.toHexString(result.intValue());
    }

    public static void main(String[] args) {

        List<String> list = Lists.newArrayList("f1", "b1", "00", "00", "00", "00", "00",
                                               "01", "5b", "39", "67", "5e", "00", "00", "00", "00");

        byte[] bt = new byte[0];
        for (String st : list) {
            bt = Bytes.concat(bt, HexUtil.toBytes(st));
        }

        System.out.println(crc32Cal(bt));

        String t = "f1 b1 00 00 00 00 00 01 5b 39 67 5e 00 00 00 00";
        byte[] bt2 = new byte[0];
        for (String st : t.split(" ")) {
            bt2 = Bytes.concat(bt2, HexUtil.toBytes(st));
        }

        System.out.println(crc32Cal(bt2));

        System.out.println(LocalDateTime.now().getDayOfMonth());
    }
}
