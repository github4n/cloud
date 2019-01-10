package com.iot.file.util;

import com.iot.common.util.StringUtil;
import com.iot.file.entity.FileBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * 项目名称：cloud
 * 功能描述：文件服务工具类
 * 创建人： yeshiyuan
 * 创建时间：2018/4/20 9:28
 * 修改人： yeshiyuan
 * 修改时间：2018/4/20 9:28
 * 修改描述：
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    /**
     * 描述：获取指定时间内的图片
     *
     * @author 李帅
     * @created 2018年1月10日 下午7:00:19
     * @since
     * @param videoFilename:视频路径
     * @param thumbFilename:图片保存路径
     * @param width:图片长
     * @param height:图片宽
     * @param hour:指定时
     * @param min:指定分
     * @param sec:指定秒
     * @throws IOException
     * @throws InterruptedException
     */
    @SuppressWarnings("unused")
    public static void getThumb(String ffmpegUrl, String videoFilename, String thumbFilename, int width, int height, int hour,
                         int min, float sec) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegUrl, "-y", "-i", videoFilename, "-vframes", "1", "-ss",
                hour + ":" + min + ":" + sec, "-f", "mjpeg", "-s", width + "*" + height, "-an", thumbFilename);

        Process process = processBuilder.start();

        InputStream stderr = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null)
            ;
        process.waitFor();

        if (br != null)
            br.close();
        if (isr != null)
            isr.close();
        if (stderr != null)
            stderr.close();
    }


    /**
     *
     * 描述：从输入流中获取字节数组
     *
     * @author 李帅
     * @created 2018年1月11日 上午10:19:25
     * @since
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     *
     * 描述：从网络Url中下载文件
     *
     * @author 李帅
     * @created 2018年1月11日 上午10:19:35
     * @since
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 获取自己数组
        byte[] getData = FileUtil.readInputStream(inputStream);

        // 文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }
    /**
     *
     * 描述：删除文件或者文件夹
     * @author 李帅
     * @created 2017年9月23日 下午4:08:48
     * @since
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }

    /**
      * @despriction：删除文件路径下的所有文件
      * @author  yeshiyuan
      * @created 2018/11/27 16:44
      */
    public static void deleteByFilePath(String filePath){
        File file = new File(filePath);
        deleteAllFilesOfDir(file);
    }

    /**
     * 描述：创建文件临时存放目录
     * @author 李帅
     * @created 2018年1月11日 上午10:28:29
     * @since
     * @return
     */
    public static String createTempDir(String fileId) {
        if (fileId == null || "".equals(fileId)){
            fileId = UUID.randomUUID().toString().replace("-","");
        }
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String tempFilePath  = request.getSession().getServletContext().getRealPath("/") + fileId;
        File dir = new File(tempFilePath);
        if(! dir.exists()) {
            dir.mkdir();
        }
        return tempFilePath;
    }

    /**
      * @despriction：根据s3Key创建fileBean
      * @author  yeshiyuan
      * @created 2018/4/20 11:37
      * @param s3key s3服务器存储文件的路径(格式：租户id/文件夹/fileId.文件后缀 )
      * @param fileType 文件类型(有些s3路径不带文件类型，所以手动传,可以传null，代码从s3获取)
      * @return
      */
    public static FileBean createFileBean(String s3key,String fileType, Long tenantId){
        FileBean fileBean = new FileBean();
        int lastIndex = s3key.length();
        if (s3key.lastIndexOf(".")!=-1){
            lastIndex = s3key.lastIndexOf(".");
        }
        fileBean.setCreateTime(new Date());
        fileBean.setFileId(s3key.substring(s3key.lastIndexOf("/")+1,lastIndex));
        if (StringUtil.isEmpty(fileType)){
            //兼容未传文件类型的处理
            if (s3key.contains(".")){
                fileBean.setFileType(s3key.substring(s3key.lastIndexOf(".")+1));
            }
        }else{
            fileBean.setFileType(fileType);
        }
        fileBean.setFilePath(s3key);
        fileBean.setTenantId(tenantId);
        return fileBean;
    }

    /**
      * @despriction：File转化为MultioartFile
      * @author  yeshiyuan
      * @created 2018/5/8 13:53
      * @param file 文件
      * @return
      */
    public static MultipartFile toMultipartFile(File file){
        MultipartFile multipartFile = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile("file",file.getName(),"", inputStream);
        }catch (Exception e){
            logger.error("file转为MultipartFile出错：",e);
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("",e);
                }
            }
        }
        return multipartFile;
    }

    public static void downloadFile(HttpServletResponse httpServletResponse, File file) {
        FileInputStream fi = null;
        BufferedInputStream  br = null;
        OutputStream os = null;
        try {
            fi = new FileInputStream(file);
            br = new BufferedInputStream (fi);
            byte []buffer = new byte[1024];
            os = new BufferedOutputStream(httpServletResponse.getOutputStream());
            //下载文件
            httpServletResponse.setContentType("application/octet-stream");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes(), "ISO8859-1") );
            int i;
            while ((i=br.read(buffer))!=-1){
                os.write(buffer,0, i);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fi!=null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os!=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
