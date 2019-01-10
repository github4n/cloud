package com.iot.boss.util;

import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.tenant.vo.req.SaveAppPackReq;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 描述：app打包配置工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/25 14:04
 */
public class AppPackUtil {
    //TODO 临时文件基本路径
    private final static String TEMP_PATH_NAME = "app-pack.temp-path";
    //logo名称
    public final static String ICON_FILE_NAME= "logo.png";
    //启动页名称
    public final static String START_IMG_NAME = "startImg.png";
    //ios证书文件 certificate
    public final static String IOS_CERTIDICATE_FILE ="certificate.p12";
    //ios授权文件
    public final static String IOS_AUTHOR_FILE ="license.profile";
    //配置文件
    public final static String CONFIG_FILE_NAME ="config.txt";
    //替换配置文件
    public final static String REPLACE_CONFIG_FILE_NAME ="replace_config.txt";
    //ios logo比例
    private final  String IOS_LOGO_RULE = "40-2,60,58-1,87-1,80-1,120,120-1,180,20,40,29,58-2,40-1,80,76,152,167,1024";
    //文件路径
    private String path;

    ////////////////////////////////////////////////////////////////////////////

    /**
     * 写入配置信息
     * @param filePath
     * @param req
     * @throws IOException
     */
    public static void writeConfig(String filePath,SaveAppPackReq req) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));
        pw.println("tenantCode="+req.getTenantCode());
        pw.println("appName="+req.getAppName());
        pw.println("lang="+req.getLang());
        pw.println("style="+req.getStyle());
        pw.println("verFlag="+req.getVerFlag());
        pw.println("androidVer="+req.getAndroidVer());
        pw.println("iosVer="+req.getIosVer());
        pw.println("productIds="+req.getProductIds());
        pw.println("iosAppKey="+req.getIosAppKey());
        pw.println("androidAppKey="+req.getAndroidAppKey());
        pw.close();
    }

    /**
     * 写入替换配置信息
     * @param filePath
     * @param req
     * @throws IOException
     */
    public static void writeReplaceConfig(String filePath,SaveAppPackReq req) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));
        //安卓
        //图片替换
        pw.println("cp sourcePath/android/mipmap-hdpi/ androidPath/app/src/main/res/mipmap-hdpi/");
        pw.println("cp sourcePath/android/mipmap-xhdpi/ androidPath/app/src/main/res/mipmap-xhdpi/");
        pw.println("cp sourcePath/android/mipmap-xxhdpi/ androidPath/app/src/main/res/mipmap-xxhdpi/");
        //buildDir 本地配置去除
        pw.println("sed buildDir:= : androidPath/build.gradle");
        //sdk路径替换
        pw.println("sed sdk.dir= sdk.dir=\\\\\\/usr\\\\\\/local\\\\\\/share\\\\\\/android-sdk androidPath/local.properties");
        //app名称替换
        pw.println("sed application_name application_name\">"+req.getAppName()+"<\\\\/string> androidPath/app/src/main/res/values-zh/strings.xml");
        pw.println("sed app_name app_name\">"+req.getAppName()+"<\\\\/string> androidPath/app/src/main/res/values-zh/strings.xml");
        //版本号替换
        pw.println("sed versionName: versionName:\""+req.getAndroidVer()+"\" androidPath/app/build.gradle");
        //bundleId替换
        pw.println("sed applicationId applicationId:\"com.iotsmarthome.lds\" androidPath/app/build.gradle");
        pw.println("sed package_name package_name\"$:\"com.iotsmarthome.lds\" androidPath/app/google-services.json");

        //ios
        //图片替换
        pw.println("cp sourcePath/ios/AppIcon.appiconset/ iosPath/Smarthome/Smarthome/Images.xcassets/AppIcon.appiconset/");
        pw.println("cp sourcePath/ios/logo-1.imageset/ iosPath/Smarthome/Smarthome/Images.xcassets/logo-1.imageset/");
        //app名称替换
        pw.println("plistBuddy CFBundleDisplayName "+req.getAppName()+" iosPath/Smarthome/Smarthome/Info.plist");
        //版本号替换
        pw.println("plistBuddy CFBundleShortVersionString "+req.getIosVer()+" iosPath/Smarthome/Smarthome/Info.plist");
        pw.println("plistBuddy CFBundleVersion "+req.getIosVer()+" iosPath/Smarthome/Smarthome/Info.plist");
        //bundleId替换
        pw.println("sed PRODUCT_BUNDLE_IDENTIFIER PRODUCT_BUNDLE_IDENTIFIER:=:com.iotsmarthome.lds2; iosPath/Smarthome/Smarthome.xcodeproj/project.pbxproj");
        //证书文件标识替换
        pw.println("sed PROVISIONING_PROFILE:= PROVISIONING_PROFILE:=:\"2730e337-d92f-4bee-a536-43c71bf67a38\"; iosPath/Smarthome/Smarthome.xcodeproj/project.pbxproj");
        //授权文件标识替换
        pw.println("sed PROVISIONING_PROFILE_SPECIFIER PROVISIONING_PROFILE_SPECIFIER:=:LdsIotSmartHomeDistributionProvisioningA023; iosPath/Smarthome/Smarthome.xcodeproj/project.pbxproj");
        //替换导出文件配置
        pw.println("plistBuddyAdd :provisioningProfiles:com.iotsmarthome.lds2 LdsIotSmartHomeDistributionProvisioningA023 iosPath/Smarthome/AutoBuild/plist/Ent.plist");

        //替换podfile
        pw.println("cp sourcePath/file/ iosPath/Smarthome/");

        //复制web到安卓
        //pw.println("cp webPath/pack/all/ androidPath/module-base/src/main/assets/web/");
        //复制web到ios
        //pw.println("cp webPath/pack/all/ iosPath/Smarthome/Smarthome/Resources/build/");

        pw.close();
    }

    /**
     * 写入podfile信息
     * @param filePath
     * @throws IOException
     */
    public static void writePodfile(String filePath) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));
        String txt = "use_frameworks!\n" +
                "\n" +
                "target 'Smarthome' do\n" +
                "    \n" +
                "#require module\n" +
                "    pod 'WebViewJavascriptBridge'\n" +
                "    pod 'CocoaLumberjack'\n" +
                "    pod 'CocoaAsyncSocket'\n" +
                "    pod 'LDSProtocol/Base', :path => '../'\n" +
                "    pod 'LDSProtocol/MQTT', :path => '../'\n" +
                "    pod 'LDSProtocol/HTTP', :path => '../'\n" +
                "    pod 'LDSProtocol/System', :path => '../'\n" +
                "    pod 'LDSProtocol/SQL', :path => '../'\n" +
                "    \n" +
                "#option module\n" +
                "#    pod 'LDSProtocol/TCP', :path => '../'\n" +
                "#    pod 'LDSProtocol/Smartlink', :path => '../'\n" +
                "#    pod 'LDSProtocol/Map', :path => '../'\n" +
                "#    pod 'LDSProtocol/UDP', :path => '../'\n" +
                "\n" +
                "#BLE module\n" +
                "    pod 'LDSProtocol/BLE', :path => '../'\n" +
                "\n" +
                "#IPC module\n" +
                "#    pod 'LDSProtocol/TUTK', :path => '../'\n" +
                "#    pod 'LDSProtocol/XPlayer', :path => '../'\n" +
                "#    pod 'LDSProtocol/IPC', :path => '../'\n" +
                "\n" +
                "end\n";
        pw.print(txt);
        pw.close();
    }

    //////////////////////////////////////////////////////////////////////////////


    public AppPackUtil(SaveAppPackReq saveAppPackReq){
        String appName = saveAppPackReq.getAppName();
        String tenantCode = saveAppPackReq.getTenantCode();
        Environment environment = ApplicationContextHelper.getBean(Environment.class);
        String temp_Path = environment.getProperty(TEMP_PATH_NAME);
        if(temp_Path==null){
            temp_Path = "F:\\SmartHome\\temp\\";
        }
        this.path = temp_Path +appName+"_"+tenantCode+"_"+System.currentTimeMillis()+"/";
    }

    public File getFile(String fileName){
        return new File(this.path+fileName);
    }

    public File getResFile(String fileName){
        return new File(getResPath()+fileName);
    }

    public String getPath(){
        return this.path;
    }

    public String getResPath(){
        return this.path+"res/";
    }

    public String[] getIosLogoRule(){
        return this.IOS_LOGO_RULE.split(",");
    }

    public void deleteDirectory() throws IOException {
        FileUtils.deleteDirectory(new File(getPath()));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 获取压缩文件
     * @param multipartRequest
     * @param saveAppPackReq
     * @return
     * @throws IOException
     */
    public File packFile(MultipartHttpServletRequest multipartRequest,SaveAppPackReq saveAppPackReq) throws IOException {
        //生成临时文件
        getTempFile(multipartRequest);
        //生成配置文件
        writeConfig(getResPath()+CONFIG_FILE_NAME,saveAppPackReq);
        //生成替换配置文件
        writeReplaceConfig(getResPath()+REPLACE_CONFIG_FILE_NAME,saveAppPackReq);
        //生成产品Podfile
        writePodfile(getResPath()+"file/Podfile");
        return getZipFile();
    }


    /**
     * 生成临时文件
     * @param multipartRequest
     * @throws IOException
     */
    public void getTempFile(MultipartHttpServletRequest multipartRequest) throws IOException {
        //先创建临时文件夹
        FileUtils.forceMkdir(new File(getPath()));
        FileUtils.forceMkdir(new File(getResPath()));
        FileUtils.forceMkdir(new File(getResPath()+"android\\mipmap-hdpi"));
        FileUtils.forceMkdir(new File(getResPath()+"android\\mipmap-xhdpi"));
        FileUtils.forceMkdir(new File(getResPath()+"android\\mipmap-xxhdpi"));
        FileUtils.forceMkdir(new File(getResPath()+"ios\\AppIcon.appiconset"));
        FileUtils.forceMkdir(new File(getResPath()+"ios\\logo-1.imageset"));
        FileUtils.forceMkdir(new File(getResPath()+"file"));

        //icon图片
        MultipartFile iconFile = multipartRequest.getFile("logo");
        iconFile.transferTo(getFile(ICON_FILE_NAME));
        //启动页图片
        MultipartFile startImgFile = multipartRequest.getFile("startImg");
        startImgFile.transferTo(getFile(START_IMG_NAME));

        /*//证书文件
        MultipartFile iosCertificateFile = multipartRequest.getFile("iosCertificate");
        File file1 = new File(getResPath()+"ios\\"+IOS_CERTIDICATE_FILE);
        iosCertificateFile.transferTo(file1);

        //授权文件
        MultipartFile iosLicenseFile = multipartRequest.getFile("iosLicense");
        File file2 = new File(getResPath()+"ios\\"+IOS_AUTHOR_FILE);
        iosLicenseFile.transferTo(file2);*/
    }

    /**
     * 压缩文件
     * @return
     * @throws IOException
     */
    public File getZipFile() throws IOException {
        long st = System.currentTimeMillis();
        //ios相关文件生成打包
        String tempPath = getPath();
        //ios
        String resPath = getResPath();

        //logo图片生成
        String[] iosLogoArray = getIosLogoRule();
        for(String name:iosLogoArray){
            String fileName = name+".png";
            String[] format = name.split("-");
            int w = Integer.parseInt(format[0]);
            //压缩复制
            FileUtil.resize(tempPath+ICON_FILE_NAME, resPath+"ios\\AppIcon.appiconset\\"+fileName, w, w);
        }

        //启动页生成
        //loading_logo@2x.png  181*221
        FileUtil.resize(tempPath+START_IMG_NAME, resPath+"ios\\logo-1.imageset\\loading_logo@2x.png", 181, 221);
        //loading_logo@3x.png 272*332
        FileUtil.resize(tempPath+START_IMG_NAME, resPath+"ios\\logo-1.imageset\\loading_logo@3x.png", 272, 332);


        //安卓相关文件生成打包
        Map<String,String> androidMap = Maps.newHashMap();
        androidMap.put("mipmap-hdpi","96&136x166");
        androidMap.put("mipmap-xhdpi","144&181x221");
        androidMap.put("mipmap-xxhdpi","192&272x332");

        String logoName = "logo2.png";
        String loadingName ="loading_logo.png";
        for(Map.Entry<String, String> entry : androidMap.entrySet()){
            String[] v = entry.getValue().split("&");
            int w = Integer.parseInt(v[0]);
            String[] v1 = v[1].split("x");
            int loading_w = Integer.parseInt(v1[0]);
            int loading_h = Integer.parseInt(v1[1]);

            //logo
            FileUtil.resize(tempPath+ICON_FILE_NAME, resPath+"android\\"+entry.getKey()+"\\"+logoName,w, w);
            //loading
            FileUtil.resize(tempPath+START_IMG_NAME, resPath+"android\\"+entry.getKey()+"\\"+loadingName,loading_w, loading_h);
        }

        //压缩
        File f = new File(resPath);
        File zipfile = new File(getPath(), f.getName() + ".zip");
        new CompactAlgorithm(zipfile).zipFiles(f);
        long et = System.currentTimeMillis();
        //System.out.println("压缩文件完成，用时："+(et-st)+"毫秒！");
        return zipfile;
    }

    public static void main(String[] args) throws IOException {
        String resPath = "F:\\SmartHome\\temp\\";

        /*List<String> resFiles = Lists.newArrayList();
        resFiles.add("F:\\SmartHome\\temp\\source2\\android\\mipmap-hdpi\\logo2.png");
        resFiles.add("F:\\SmartHome\\temp\\source2\\android\\mipmap-hdpi\\loading_logo.png");
        resFiles.add("F:\\SmartHome\\temp\\source2\\config.txt");
        resFiles.add("F:\\SmartHome\\temp\\source2\\ios\\logo-1.imageset\\loading_logo@3x.png");

        File[] srcfile=new File[resFiles.size()];
        int index = 0;
        for(String file:resFiles){
            File f = new File(file);
            srcfile[index] = f;
            index++;
        }
        File zipfile = new File(resPath + "source2.zip");
        FileUtil.zipFiles(srcfile, zipfile);*/


        /*SaveAppPackReq saveAppPackReq = new SaveAppPackReq();
        saveAppPackReq.setAppName("testName");
        saveAppPackReq.setIosVer("1.0.0.3");
        saveAppPackReq.setAndroidVer("1.0.0.6");
        try {
            writeConfig("F:\\SmartHome\\temp\\res/"+CONFIG_FILE_NAME,saveAppPackReq);
            writeReplaceConfig("F:\\SmartHome\\temp\\res/"+REPLACE_CONFIG_FILE_NAME,saveAppPackReq);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        resPath = "/usr/local/test/res";
        //String sourcePath = resPath.replaceAll("/","\\\\\\\\\\\\/");
        //System.out.println(sourcePath);
        //String iosPath = ios_path;
        //String androidPath = android_path;

        //writePodfile("F:\\SmartHome\\temp\\res/file/Podfile");
    }
}
