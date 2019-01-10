package com.iot.pack.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 描述：文件工具类，主要用于app打包
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/22 18:49
 */
public class FileUtil {

    public static void main(String[] args) throws IOException {

        //等比压缩图片
        //resizeJpg("F:\\img\\temp\\start_img.jpg","F:\\img\\temp\\start_img1.jpg",200,200);

        //File fromFile = new File("F:\\img\\temp\\start_img.jpg");
        //File toFile = new File("F:\\img\\temp\\start_img1.jpg");
        //resize("F:\\img\\temp\\start_img.jpg", "F:\\img\\temp\\start_img1.jpg", 200, 200);

        //2个源文件
//        File f1 = new File("F:\\img\\temp\\start_img.jpg");
//        File f2 = new File("F:\\img\\temp\\log.txt");
//        File[] srcfile = {f1, f2};
//        //压缩后的文件
//        File zipfile = new File("F:\\img\\temp\\test.zip");
//        //TestZIP.zipFiles(srcfile, zipfile);
//        //需要解压缩的文件
//        File file = new File("F:\\img\\temp\\test.zip");
//        //解压后的目标目录
//        String dir = "F:\\img\\target\\";
//        //TestZIP.unZipFiles(file, dir);
//
//        //删除文件夹
//        //FileUtils.deleteDirectory(new File("F:\\img\\temp1"));
//
//        //复制文件
//        File sf = new File("F:\\img\\temp\\start_img.jpg");
//        File tf = new File("F:\\img\\target\\start_img.jpg");
//        //FileUtils.copyFile(sf,tf);
//
//        //检出svn代码
//        String targetPath = "/usr/local/test";
//        String svnUrl ="http://172.24.20.199/svn/svnrepos/Cloud/Java/branch/test/cloud/core";
//        String command="cd "+targetPath+" && svn checkout "+svnUrl;
//        //Runtime.getRuntime().exec(command);


        String ff = "C:\\Users\\zhangyue\\Desktop\\test.png";
        String tf = "C:\\Users\\zhangyue\\Desktop\\test-1.png";
        BufferedImage watermarkImage = ImageIO.read(new File("sssssssss"));
        Thumbnails.of(new File(ff)).forceSize(500,500).watermark(watermarkImage).toFile(new File(tf));

    }

    public static void resizeProportion(String fromFile, String toFile, int outputWidth, int outputHeight) throws IOException {
        long st = System.currentTimeMillis();
        String p = fromFile.substring(fromFile.indexOf(".")+1,fromFile.length());
        if(p.equals("png")){
            Thumbnails.of(new File(fromFile)).forceSize(outputWidth,outputHeight).toFile(new File(toFile));
            //resizePngProportion(fromFile, toFile, outputWidth, outputHeight);
        }else{
            Thumbnails.of(new File(fromFile)).forceSize(outputWidth,outputHeight).toFile(new File(toFile));
            //resizeJpg(fromFile, toFile, outputWidth, outputHeight);
        }
        long et = System.currentTimeMillis();
        //System.out.println("处理图片用时："+(et-st)+"毫秒！");
    }

    public static void resizePngProportion(String fromFile, String toFile, int outputWidth, int outputHeight) throws IOException {
        resizePng(new File(fromFile),new File(toFile),outputWidth,outputHeight,false);
    }

    public static void resize(String fromFile, String toFile, int outputWidth, int outputHeight) throws IOException {
        long st = System.currentTimeMillis();
        String p = fromFile.substring(fromFile.indexOf(".")+1,fromFile.length());
        if(p.equals("png")){
            resizePng(fromFile, toFile, outputWidth, outputHeight);
        }else{
            resizeJpg(fromFile, toFile, outputWidth, outputHeight);
        }
        long et = System.currentTimeMillis();
        //System.out.println("处理图片用时："+(et-st)+"毫秒！");
    }

    public static void resizePng(String fromFile, String toFile, int outputWidth, int outputHeight) throws IOException {
        resizePng(new File(fromFile),new File(toFile),outputWidth,outputHeight,true);
    }

    /**
     * 裁剪PNG图片工具类
     *
     * @param fromFile
     *            源文件
     * @param toFile
     *            裁剪后的文件
     * @param outputWidth
     *            裁剪宽度
     * @param outputHeight
     *            裁剪高度
     * @param proportion
     *            是否是等比缩放
     */
    private static void resizePng(File fromFile, File toFile, int outputWidth, int outputHeight,
                                 boolean proportion) throws IOException {
            BufferedImage bi2 = ImageIO.read(fromFile);
            int newWidth;
            int newHeight;
            // 判断是否是等比缩放
            if (proportion) {
                // 为等比缩放计算输出的图片宽度及高度
                double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth;
                double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight;
                // 根据缩放比率大的进行缩放控制
                double rate = rate1 < rate2 ? rate1 : rate2;
                newWidth = (int) (((double) bi2.getWidth(null)) / rate);
                newHeight = (int) (((double) bi2.getHeight(null)) / rate);
            } else {
                newWidth = outputWidth; // 输出的图片宽度
                newHeight = outputHeight; // 输出的图片高度
            }
            BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight,
                    Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = to.createGraphics();
            @SuppressWarnings("static-access")
            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();
            ImageIO.write(to, "png", toFile);
    }


    /**
     * 等比例缩放图片(jpg)
     * @param sourcePath
     * @param targetPath
     * @param targetW
     * @param targetH
     * @throws IOException
     */
    public static void resizeJpg(String sourcePath,String targetPath,int targetW, int targetH) throws IOException {
        File fileOne = new File(sourcePath);
        BufferedImage source = ImageIO.read(fileOne);
        BufferedImage target = resize(source,targetW,targetH);
        File outFile = new File(targetPath);
        String p = targetPath.substring(targetPath.indexOf(".")+1,targetPath.length());
        ImageIO.write(target, p, outFile);// 写图片
    }

    /**
     * 实现图像的等比缩放
     *
     * @param source  待处理的图片流
     * @param targetW 宽度
     * @param targetH 高度
     * @return
     */
    private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        int width = source.getWidth();// 图片宽度
        int height = source.getHeight();// 图片高度

        //对比比例
        double rate_w = (float)targetW/width;
        double rate_h = (float)targetH/height;

        if(rate_w<rate_h){
            //根据宽比例rate_w缩放
            targetH = (int) (height*rate_w);
        }else{
            //根据高比例rate_h缩放
            targetW = (int) (width*rate_h);
        }

        BufferedImage newImage;
        if (source.getType() == 0){
            newImage = new BufferedImage(targetW, targetH,5);
        }else{
            newImage = new BufferedImage(targetW, targetH, source.getType());
        }

        Graphics g = newImage.getGraphics();
        g.drawImage(source, 0, 0, targetW, targetH, null);
        g.dispose();
        return newImage;
    }

    /**
     * 功能:压缩多个文件成一个zip文件
     *
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static void zipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            //System.out.println("压缩完成.");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能:解压缩
     *
     * @param zipfile：需要解压缩的文件
     * @param descDir：解压后的目标目录
     */
    public static void unZipFiles(File zipfile, String descDir) {
        try {
            //创建目标文件夹
            File target = new File(descDir);
            if(!target.exists()){
                target.mkdirs();
            }

            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
                //System.out.println("解压缩完成.");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
