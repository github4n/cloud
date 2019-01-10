package com.iot.common.util;

import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.AESEncrypter;
import de.idyl.winzipaes.impl.AESEncrypterBC;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：通过Java的Zip输入输出流实现压缩和解压文件
 * 创建人： mao2080@sina.com
 * 创建时间：2017年8月26日 下午2:34:59
 * 修改人： mao2080@sina.com
 * 修改时间：2017年8月26日 下午2:34:59
 */
public final class ZipUtil {

    /**
     * 描述：构造函数
     *
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:37:22
     * @since
     */
    private ZipUtil() {

    }

    /**
     * 描述：压缩文件
     *
     * @param filePath 待压缩的文件路径
     * @return 压缩后的文件
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:31:27
     * @since
     */
    public static File zip(String filePath) {
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // 压缩文件名=源文件名.zip
            String zipName = source.getName() + ".zip";
            target = new File(source.getParent(), zipName);
            if (target.exists()) {
                target.delete(); //删除旧的文件
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                // 添加对应的文件Entry
                addEntry("/", source, zos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.closeQuietly(zos, fos);
            }
        }
        return target;
    }

    /**
     * 描述：
     *
     * @param base   基路径
     * @param source 源文件
     * @param zos    Zip文件输出流
     * @throws IOException
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:33:12
     * @since
     */
    private static void addEntry(String base, File source, ZipOutputStream zos) throws IOException {
        // 按目录分级，形如：/aaa/bbb.txt
        String entry = base + source.getName();
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    // 递归列出目录下的所有文件，添加文件Entry
                    addEntry(entry + "/", file, zos);
                }
            }
        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[1024 * 10];
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis, buffer.length);
                int read = bis.read(buffer, 0, buffer.length);
                zos.putNextEntry(new ZipEntry(entry));
                while (read != -1) {
                    zos.write(buffer, 0, read);
                    read = bis.read(buffer, 0, buffer.length);
                }
                zos.closeEntry();
            } finally {
                IOUtil.closeQuietly(bis, fis);
            }
        }
    }

    /**
     * 描述：使用指定密码将给定文件或文件夹压缩成指定的输出ZIP文件
     *
     * @param srcFile  需要压缩的文件或文件夹
     * @param destPath 输出路径
     * @param passwd   压缩文件使用的密码
     * @author 李帅
     * @created 2017年12月21日 下午5:27:21
     * @since
     */
    public static void zip(String srcFile, String destPath, String passwd) {
        AESEncrypter encrypter = new AESEncrypterBC();
        AesZipFileEncrypter zipFileEncrypter = null;
        try {
            zipFileEncrypter = new AesZipFileEncrypter(destPath, encrypter);
            //此方法是修改源码后添加,用以支持中文文件名
//   			zipFileEncrypter.setEncoding("utf8");
            File sFile = new File(srcFile);
            /**
             * AesZipFileEncrypter提供了重载的添加Entry的方法,其中: add(File f, String passwd)
             * 方法是将文件直接添加进压缩文件
             *
             * add(File f, String pathForEntry, String passwd) 方法是按指定路径将文件添加进压缩文件
             * pathForEntry - to be used for addition of the file (path within zip file)
             */
            doZip(sFile, zipFileEncrypter, "", passwd);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipFileEncrypter != null) {
                    zipFileEncrypter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 描述：具体压缩方法,将给定文件添加进压缩文件中,并处理压缩文件中的路径
     *
     * @param file         给定磁盘文件(是文件直接添加,是目录递归调用添加)
     * @param encrypter    AesZipFileEncrypter实例,用于输出加密ZIP文件
     * @param pathForEntry ZIP文件中的路径
     * @param passwd       压缩密码
     * @throws IOException
     * @author 李帅
     * @created 2017年12月21日 下午5:26:04
     * @since
     */

    private static void doZip(File file, AesZipFileEncrypter encrypter, String pathForEntry, String passwd)
            throws IOException {
        String path = pathForEntry;
        if (file.isFile()) {
            path += file.getName();
            encrypter.add(file, path, passwd);
            return;
        }
        path += file.getName() + File.separator;
        File[] files = file.listFiles();
        if (files != null) {
            for (File subFile : files) {
                doZip(subFile, encrypter, path, passwd);
            }
        }
    }

    /**
     * 描述：解压文件
     *
     * @param filePath 压缩文件路径
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:33:49
     * @since
     */
    public static void unzip(String filePath) {
        File source = new File(filePath);
        if (source.exists()) {
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {
                zis = new ZipInputStream(new FileInputStream(source));
                ZipEntry entry = zis.getNextEntry();
                while (entry != null && !entry.isDirectory()) {
                    File target = new File(source.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    byte[] buffer = new byte[1024 * 10];
                    int read = zis.read(buffer, 0, buffer.length);
                    while (read != -1) {
                        bos.write(buffer, 0, read);
                        read = zis.read(buffer, 0, buffer.length);
                    }
                    bos.flush();
                    entry = zis.getNextEntry();
                }
                zis.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.closeQuietly(zis, bos);
            }
        }
    }

}