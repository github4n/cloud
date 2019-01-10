package com.iot.common.util;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：IO工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2017年8月26日 下午2:35:56
 * 修改人： mao2080@sina.com
 * 修改时间：2017年8月26日 下午2:35:56
 */
public final class IOUtil {

    /**
     * 描述：关闭一个或多个流对象-抛出异常
     *
     * @param closeables 可关闭的流对象列表
     * @throws IOException
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:36:19
     * @since
     */
    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }

    /**
     * 描述：关闭一个或多个流对象-不会抛出异常
     *
     * @param closeables 可关闭的流对象列表
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:36:38
     * @since
     */
    public static void closeQuietly(Closeable... closeables) {
        try {
            close(closeables);
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * 描述：文件转换
     *
     * @param multfile MultipartFile类型文件
     * @return java.io.File
     * @author mao2080@sina.com
     * @created 2018/4/16 14:19
     */
    public static File multipartToFile(MultipartFile multfile) throws IOException {
        CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File file = fi.getStoreLocation();
        if (file.length() < 2048) {
            File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getName());
            multfile.transferTo(tmpFile);
            return tmpFile;
        }
        return file;
    }

    /**
     * @param inputStream
     * @param newFile     目标文件
     * @return
     * @despriction：描述 输入流存储至文件
     * @author yeshiyuan
     * @created 2018/4/19 18:40
     */
    public static File copyInputToFile(InputStream inputStream, File newFile) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(newFile);
            int i;
            byte[] bytes = new byte[1024];
            i = inputStream.read(bytes);
            while (i != -1) {
                out.write(bytes, 0, i);
                i = inputStream.read(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return newFile;
    }

}