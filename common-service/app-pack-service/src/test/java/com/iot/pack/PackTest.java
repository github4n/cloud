package com.iot.pack;

import com.google.common.collect.Maps;
import com.iot.pack.util.HttpClientUtils;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 描述：上传app测试
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/24 19:22
 */
public class PackTest extends BaseTest {

    public static void main(String[] args) throws Exception {
        //临时路径
        String tempPath = "F:/SmartHome/temp/";
        //下载文件
        String fileUrl = "https://leedarson-bucket-00001.s3.cn-north-1.amazonaws.com.cn/0/zip/7f9c567e05fc4a0bb1feebb9446ff757.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180601T081542Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Credential=AKIAPHLS3Q5HE2XCECOQ%2F20180601%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Signature=553d10985cec8a29e2086ce9c27a17cac3dfa284dce0e23a99a3a1f92f421351";
        //HttpClientUtils.getInstance().download(fileUrl, tempPath+"source.zip");


        //需要解压缩的文件路径
        String sourcePath = "F:\\SmartHome\\temp\\res.zip";
        //解压后的目标目录
        String targetPath = "F:\\SmartHome\\temp\\res";
        //decompression(sourcePath,"utf-8",targetPath);

        //拉取svn代码
        String ios_svn_url = "";
        String android_svn_url = "";

        //复制一份
        String ios_path_source = "F:\\SmartHome\\SmartHome_ios";
        String android_path_source = "F:\\SmartHome\\SmartHome_android";
        String ios_path = "F:\\SmartHome\\SmartHome_ios_a023";
        String android_path = "F:\\SmartHome\\SmartHome_android_a023";
        //FileUtils.copyDirectory(new File(ios_path_source),new File(ios_path));
        //FileUtils.copyDirectory(new File(android_path_source),new File(android_path));

        //读取配置
        String txt = "F:\\SmartHome\\temp\\config.txt";
        //Map<String,String> configMap = getConfig(txt);
        //System.out.println("读取配置："+configMap.get("android_app_key"));

        //ios打包
        //替换文件
        //ios证书文件替换
        String certificatePath="";
        //iosFileReplace(targetPath,certificatePath);

        //ios图片替换
        //iosImgReplace(targetPath,ios_path);
        //安卓图片替换
        //androidImgReplace(targetPath,android_path);

        //TODO 替换版本号
        String path = "F:\\SmartHome\\temp\\build.gradle";
        //changeVer(path);

        //TODO 替换app名称
        String xmlPath = "F:\\SmartHome\\temp\\strings.xml";
        //changeAppName(xmlPath);

        //TODO sdk处理

        //TODO 执行打包脚本

        //上传文件
        String appPath = "F:\\SmartHome\\temp\\app-release.apk";
        //uploadApp(appPath);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        System.out.println("日期时间："+dateStr);
    }

    public static void changeVer(String path) throws IOException {
        String buildText = file2String(new File(path));
        int s = buildText.indexOf("defaultConfig {");
        int e = buildText.indexOf("testInstrumentationRunner");
        //String text = buildText.substring(s,e);
        //System.out.println("执行结果："+s+"/"+e+"==\r\n"+buildText);

        StringBuffer newStr = new StringBuffer();
        String head =buildText.substring(0,s);
        String end =  "        "+buildText.substring(e,buildText.length());
        newStr.append(head);
        //4个空格
        newStr.append("defaultConfig {\r\n");
        //8个空格
        newStr.append("        applicationId \"com.iotsmarthome.smarthome7\"\r\n");
        newStr.append("        minSdkVersion 18\r\n");
        newStr.append("        targetSdkVersion 26\r\n");
        newStr.append("        versionCode 7\r\n");
        newStr.append("        versionName \"1.0.0.7\"\r\n");
        newStr.append(end);

        //System.out.println("拼接：\r\n"+newStr.toString());
        File file = new File(path);
        PrintStream ps = new PrintStream(new FileOutputStream(file));
        ps.append(newStr.toString());// 在已有的基础上添加字符串
        ps.close();
    }

    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String file2String(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        FileInputStream fileInputStream = new FileInputStream(file); //建立数据通道
        int length = 0 ;
        byte[] buf = new byte[1024];  //建立缓存数组，缓存数组的大小一般都是1024的整数倍，理论上越大效率越好
        while((length = fileInputStream.read(buf))!=-1){
            result.append(new String(buf,0 ,length));
        }
        fileInputStream.close(); //关闭资源
        return result.toString();
    }

    /**
     * 替换app名称
     * @throws IOException
     * @throws DocumentException
     */
    public static void changeAppName(String path) throws IOException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(path));
        Element root = document.getRootElement();
        boolean flag = false;
        boolean andFlag = false;
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            String value = element.attribute("name").getValue();
            if ("application_name".equals(value)) {
                element.setText("777");
                flag = true;
                if(andFlag){
                    break;
                }else{
                    andFlag = true;
                }
            }

            if ("app_name".equals(value)) {
                element.setText("888");
                flag = true;
                if(andFlag){
                    break;
                }else{
                    andFlag = true;
                }
            }
        }

        if (flag) {
            FileWriter out = new FileWriter(path);
            document.write(out);
            out.flush();
            out.close();
        }
    }

    public static void uploadApp(String appPath) throws Exception {
        Map<String,String> uploadParams = new LinkedHashMap<String, String>();
        uploadParams.put("_api_key", "8d5f16bf3331de6a8e4ac3625f4c207a");
        // 中文需转uft-8编码，不然会乱码
        HttpClientUtils.getInstance().uploadFileImpl(
                "https://www.pgyer.com/apiv2/app/upload", appPath,
                "file", uploadParams);
    }

    private static Map<String,String> getConfig(String filePath) {
        File file = new File(filePath);
        Map<String,String> maps = Maps.newHashMap();
        try {
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            String line = null;
            while ((line = in.readLine()) != null) {
                String[] strs = line.split("=");
                maps.put(strs[0],strs[1]);
            }
            in.close();
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 证书文件替换，暂不处理
     * @param source_path
     * @param certificatePath
     * @throws IOException
     */
    public static void iosFileReplace(String source_path,String certificatePath) throws IOException {
        //替换证书文件和授权文件
        String target_path1 = certificatePath+"certificate.p12";
        String target_path2 = certificatePath+"linsence.profile";

        String path1 = source_path+"certificate.p12";
        String path2 = source_path+"linsence.profile";

        File tf1 = new File(target_path1);
        File tf2 = new File(target_path2);
        if (tf1.exists()) {
            tf1.delete();
        }

        if (tf2.exists()) {
            tf2.delete();
        }
        FileUtils.copyFile(new File(path1), new File(target_path1));
        FileUtils.copyFile(new File(path2), new File(target_path2));
    }

    public static void iosImgReplace(String source_path,String ios_path) throws IOException {
        String iosPath = ios_path + "\\SmartHome\\Assets.xcassets\\";
        String logoPath = "AppIcon.appiconset";
        String loadingPath = "logo-1.imageset";
        //logo替换
        replaceFile(iosPath+logoPath,source_path+"\\ios\\"+logoPath);
        //loading替换
        replaceFile(iosPath+loadingPath,source_path+"\\ios\\"+loadingPath);
    }

    public static void replaceFile(String oldDirPath,String newFilePath) throws IOException {
        File dir = new File(newFilePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String fileName = file.getName();

            //删除
            File tf = new File(oldDirPath+"\\" + fileName);
            if (tf.exists()) {
                tf.delete();
            }
            //复制
            FileUtils.copyFile(file, tf);
        }
    }

    public static void androidImgReplace(String source_path,String android_path) throws IOException {
        //安卓替换

        String resStr = "mipmap-hdpi,mipmap-xhdpi,mipmap-xxhdpi";
        String[] resArray = resStr.split(",");

        for (String dir : resArray) {
            String androidTarget = android_path+"\\app\\src\\main\\res\\"+dir;
            String newFilePath = source_path+"\\android\\"+dir;
            replaceFile(androidTarget,newFilePath);
           /* String logoName = dir + "@logo2.png";
            String logo_path = androidTarget + dir + "\\";
            File logo = new File(logo_path + "logo2.png");
            if (logo.exists()) {
                logo.delete();
            }
            FileUtils.copyFile(new File(source_path + logoName), logo);

            //loading
            String loadingName = dir + "@loading_logo.png";
            String loading_path = androidTarget +dir + "\\";
            File loading = new File(loading_path + "loading_logo.png");
            if (loading.exists()) {
                loading.delete();
            }
            FileUtils.copyFile(new File(source_path + loadingName), loading);*/
        }
    }

    /**
     *
     * @Description (解压)
     * @param zipPath zip路径
     * @param charset 编码
     * @param outPutPath 输出路径
     */
    public static void decompression(String zipPath, String charset, String outPutPath) {
        long startTime=System.currentTimeMillis();
        try {
            ZipInputStream Zin=new ZipInputStream(new FileInputStream(zipPath), Charset.forName(charset));//输入源zip路径
            BufferedInputStream Bin=new BufferedInputStream(Zin);
            String Parent = outPutPath; //输出路径（文件夹目录）
            File Fout=null;
            ZipEntry entry;
            try {
                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){
                    Fout=new File(Parent,entry.getName());
                    if(!Fout.exists()){
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out=new FileOutputStream(Fout);
                    BufferedOutputStream Bout=new BufferedOutputStream(out);
                    int b;
                    while((b=Bin.read())!=-1){
                        Bout.write(b);
                    }
                    Bout.close();
                    out.close();
                    //System.out.println(Fout+"解压成功");
                }
                Bin.close();
                Zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long endTime=System.currentTimeMillis();
        System.out.println("解压耗费时间： "+(endTime-startTime)+" ms");
    }
}
