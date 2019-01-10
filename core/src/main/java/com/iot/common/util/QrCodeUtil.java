package com.iot.common.util;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 项目名称：立达信IOT云平台
 * 功能描述：生成带字符描述的二维码图片
 * 创建人：chenxiaolin
 * 创建时间：2018年2月26日 下午5:32:46
 * 修改人： chenxiaolin
 * 修改时间：2018年2月26日 下午5:32:46
 */
public class QrCodeUtil {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final BufferedImage IMAGE_UTIL = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    private static final Graphics GUTIL = IMAGE_UTIL.getGraphics();

    static {
        GUTIL.setColor(Color.black);
        GUTIL.setFont(new Font("SimHei", Font.BOLD, 15));
    }

    public static String qrCodeToString(String qrContext, String desc) {
        String qrcodeData = "";
        try {
            qrcodeData = Base64.getEncoder().encodeToString(qrCodeToByte(qrContext, desc));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrcodeData;
    }

    public static byte[] qrCodeToByte(String qrContext, String desc) {
        ByteArrayOutputStream bo = null;
        byte[] res = new byte[0];
        try {
            int qrcodeWidth = 300;
            int qrcodeHeight = 300;
            String qrcodeFormat = "jpg";
            HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, "1");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrContext, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
            bo = new ByteArrayOutputStream();
            writeToStream(bitMatrix, qrcodeFormat, bo, desc);
            res = bo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private static void writeToStream(BitMatrix matrix, String format, OutputStream stream, String desc)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix, desc);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix, String desc) {
        List<String> rows = getSplitWords(desc);
        int wordHeight = GUTIL.getFontMetrics().getHeight();
        int size = rows.size();
        int imgH = size * wordHeight + 330;
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, imgH, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.black);
        g.setFont(new Font("SimHei", Font.BOLD, 15));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = height; y < imgH; y++) {
                image.setRGB(x, y, WHITE);
            }
        }

        int row = 0;
        for (String word : rows) {
            int wl = GUTIL.getFontMetrics().stringWidth(word);
            int offset = (300 - wl) / 2;
            g.drawString(word, offset, height + 30 + row * wordHeight);
            row++;
        }
        return image;
    }

    private static List<String> getSplitWords(String info) {
        List<String> rows = new ArrayList<>();
        String[] infos = info.split(" ");
        int[] wordLen = new int[infos.length];
        int totalLen = 0;
        for (int index = 0; index < infos.length; index++) {
            int len = GUTIL.getFontMetrics().stringWidth(infos[index] + " ");
            wordLen[index] = len;
            totalLen += len;
        }
        if (totalLen < 280) {
            rows.add(info);
        } else {
            int sum = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < wordLen.length; i++) {
                sum += wordLen[i];
                if (sum >= 280) {
                    sum = wordLen[i];
                    rows.add(sb.toString());
                    sb.setLength(0);
                }
                sb.append(infos[i] + " ");
                if ((i + 1) == wordLen.length)
                    rows.add(sb.toString());
            }
        }
        return rows;
    }
}