package com.iot.portal.web.utils;

import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.portal.web.controller.AppController;
import com.iot.tenant.vo.resp.AppInfoResp;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：app打包配置工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/25 14:04
 */
public class AppPackUtil {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AppController.class);

    //配置文件
    public final static String CONFIG_FILE_NAME = "config.txt";
    //替换配置文件
    public final static String REPLACE_CONFIG_FILE_NAME = "replace_config.txt";
    //文件路径
    private String path;

    private Map<String, Object> params;

    public AppPackUtil(AppInfoResp res) {
        String appName = res.getAppName();
        String tenantCode = res.getAppCode();
        String temp_Path = "/tmp/app-pack/";
        this.path = temp_Path + appName + "_" + tenantCode + "_" + System.currentTimeMillis() + "/";
        this.params = null;
    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * 获取压缩文件
     *
     * @param res
     * @return
     * @throws IOException
     */
    public File packFile(AppInfoResp res, Set<Integer> protocolTypeSet, Map<String, String> productMap, String langFilepath, String alias, String email) throws IOException {
        //生成临时文件
        getTempFile();
        //生成配置文件
        writeConfig(getResPath() + CONFIG_FILE_NAME, res, protocolTypeSet, productMap, alias, email);
        //生成替换配置文件
        //writeReplaceConfig(getResPath() + REPLACE_CONFIG_FILE_NAME, langMap);
        //复制多语言文件
        logger.info("多语言生成路径：" + langFilepath);
        File sourceDir = new File(langFilepath);
        File targetDir = new File(getResPath() + "web");
        FileUtils.copyDirectory(sourceDir, targetDir);
        //删除原始文件
        FileUtils.deleteDirectory(sourceDir);
        return getZipFile();
    }

    ///////////////////////////////////////////////////////////////////////////////

    /**
     * 生成临时文件
     *
     * @throws IOException
     */
    public void getTempFile() throws IOException {
        logger.debug("自动化打包，生成临时文件..." + getResPath());
        //先创建临时文件夹
        FileUtils.forceMkdir(new File(getPath()));
        FileUtils.forceMkdir(new File(getResPath()));
        FileUtils.forceMkdir(new File(getResPath() + "web/"));
    }

    /**
     * 写入配置信息
     *
     * @param filePath
     * @param req
     * @throws IOException
     */
    public void writeConfig(String filePath, AppInfoResp req, Set<Integer> protocolTypeSet, Map<String, String> productMap, String alias,String email) throws IOException {
        logger.debug("自动化打包，写入配置信息...");
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));

        Environment environment = ApplicationContextHelper.getBean(Environment.class);
        //获取环境标识
        String profile = environment.getProperty("spring.profiles.active");
        //获取svn路径
        String androidSvnPath = (String) getConfigValue("androidSvnUrl");
        String iosSvnPath = (String) getConfigValue("iosSvnUrl");
        String webSvnPath = (String) getConfigValue("webSvnUrl");
        String local = "0";
        if (getConfigValue("local") != null) {
            local = "1";
        }
        if (StringUtils.isEmpty(androidSvnPath)) {
            androidSvnPath = environment.getProperty("apppack.url.android");
        }
        if (StringUtils.isEmpty(iosSvnPath)) {
            iosSvnPath = environment.getProperty("apppack.url.ios");
        }
        if (StringUtils.isEmpty(webSvnPath)) {
            webSvnPath = environment.getProperty("apppack.url.web");
        }
        String notifyUrl = environment.getProperty("apppack.notify");

        pw.println("androidSvnPath=" + androidSvnPath);
        pw.println("iosSvnPath=" + iosSvnPath);
        pw.println("webSvnPath=" + webSvnPath);
        pw.println("profile=" + profile);
        pw.println("appId=" + req.getId());
        pw.println("androidFlag=" + req.getAndroidPack()); //是否打包
        pw.println("iosFlag=" + req.getIosPack());
        pw.println("local=" + local); //是否本地测试 1是 0否
        pw.println("yunUrl=" + notifyUrl); //打包结果通知地址
        pw.println("password=" + req.getFilePassword()); //p12文件密码
        String appName = req.getAppName();
        appName = appName.replace(" ", "\\ ");
        pw.println("appName=" + appName); //应用名称
        pw.println("androidVer=" + req.getAndroidVer());
        pw.println("iosVer=" + req.getIosVer());
        pw.println("packName=" + req.getPackName());
        pw.println("enCopyright=" + req.getEnCopyright());
        pw.println("znCopyright=" + req.getZnCopyright());
        pw.println("tenantId=" + req.getTenantId());
        pw.println("alias=" + alias);
        pw.println("email=" + email);
        //主题颜色 0 默认 1 蓝色
        Integer theme = req.getTheme();
        String themeValue = "default";
        if (theme != null && theme == 1) {
            themeValue = "blueSky";
        }
        pw.println("themeValue=" + themeValue);
        //隐私政策
        String policy = "false";
        if (!StringUtils.isEmpty(req.getHasPrivacyPolicy())){
            if (req.getHasPrivacyPolicy() == 1 ) {
                policy = "true";
            }
        }
        pw.println("policy=" + policy);

        //是否支持安防 0否 1是
        if (StringUtils.isEmpty(req.getHasSecurity())){
            req.setHasSecurity(0);
        }
        pw.println("security=" + req.getHasSecurity());

        //去除ble
        String bleFlag = "true";
        if (!protocolTypeSet.contains(1)) {
            //不需要 BLE
            bleFlag = "false";
        }
        //去除ipc
        String ipcFlag = "true";
        if (!protocolTypeSet.contains(3)) {
            //不需要IPC
            ipcFlag = "false";
        }
        pw.println("bleFlag=" + bleFlag);
        pw.println("ipcFlag=" + ipcFlag);
        //http/mqtt配置
        String http = environment.getProperty("apppack.http");
        pw.println("http=" + http);
        String mqtt = environment.getProperty("apppack.mqtt");
        pw.println("mqtt=" + mqtt);
        String bleName = environment.getProperty("apppack.ble.name");
        pw.println("bleName=" + bleName);
        String blePwd = environment.getProperty("apppack.ble.password");
        pw.println("blePwd=" + blePwd);
        String bleOurName = environment.getProperty("apppack.ble.ourName");
        bleOurName = bleOurName.replace(" ", "\\ ");
        pw.println("bleOurName=" + bleOurName);
        //产品配置
        Map<String, String> products = getProductStr(productMap);
        pw.println("wifiStr=" + products.get("wifiStr"));
        pw.println("zigbeeStr=" + products.get("zigbeeStr"));
        if (!StringUtils.isEmpty(req.getBackgroundColor())){
            pw.println("notiBgColor=" + req.getBackgroundColor());
        }
        if (!StringUtils.isEmpty(req.getKeypass())){
            pw.println("keypass=" + req.getKeypass());
        }
        if (!StringUtils.isEmpty(req.getStorepass())){
            pw.println("storepass=" + req.getStorepass());
        }
        pw.close();
    }

    /**
     * 写入替换配置信息
     *
     * @param filePath
     * @throws IOException
     */
    public void writeReplaceConfig(String filePath, Map<String, String> langMap) throws IOException {
        logger.debug("自动化打包，写入替换配置信息...");
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));

        //配置多语言
        //writeLanguage(pw, langMap);
        pw.close();
    }

    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取产品配置
     *
     * @param productMap
     */
    public Map<String, String> getProductStr(Map<String, String> productMap) {
        //wifi ble zigbee ipc
        String wifiStr = "[";
        String zigbeeStr = "[";
        int wifiLen = wifiStr.length();
        int zigbeeLen = zigbeeStr.length();

        for (Map.Entry<String, String> entry : productMap.entrySet()) {
            String key = entry.getKey();
            String[] values = key.split("\\.");
            if (values.length == 4) {
                key = values[1];
            }

            String value = entry.getValue();
            if ("wifi".equals(value)) {
                wifiStr += "\\\"" + key + "\\\",";
            }
            if ("zigbee".equals(value)) {
                zigbeeStr += "\\\"" + key + "\\\",";
            }
        }
        int wifiLen2 = wifiStr.length();
        if (wifiLen2 > wifiLen) {
            wifiStr = wifiStr.substring(0, wifiStr.length() - 1);
        }
        wifiStr += "]";
        int zigbeeLen2 = zigbeeStr.length();
        if (zigbeeLen2 > zigbeeLen) {
            zigbeeStr = zigbeeStr.substring(0, zigbeeStr.length() - 1);
        }
        zigbeeStr += "]";

        Map<String, String> maps = Maps.newHashMap();
        maps.put("wifiStr", wifiStr);
        maps.put("zigbeeStr", zigbeeStr);
        return maps;
    }

    /**
     * 写入多语言配置
     *
     * @param pw
     * @param langMap
     * @return
     */
    public void writeLanguage(PrintWriter pw, Map<String, String> langMap) {
        String langs = "\"language\": [";
        for (Map.Entry<String, String> entry : langMap.entrySet()) {
            String lang = entry.getKey();
            String value = entry.getValue();
            if (value.length() > 0) {
                value = value.substring(0, value.length() - 1);
                pw.println("sedAdd }; " + value + " webPath/src/resources/locales/" + lang + ".js");
            }
            langs += "\"" + lang + "\",";
        }
        if (langs.contains(",")) {
            langs = langs.substring(0, langs.length() - 1);
        }
        langs += "],";
        langs = langs.replace(":", "$");
        langs = langs.replace(" ", ":");
        pw.println("sed \"language " + langs + " webPath/config/app.config.json");
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 压缩文件
     *
     * @return
     */
    public File getZipFile() {
        logger.debug("自动化打包，压缩文件...");
        long st = System.currentTimeMillis();
        //压缩
        File f = new File(getResPath());
        File zipfile = new File(getPath(), f.getName() + ".zip");
        new CompactAlgorithm(zipfile).zipFiles(f);
        long et = System.currentTimeMillis();
        //logger.debug("压缩文件完成，用时："+(et-st)+"毫秒！");
        return zipfile;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Object getConfigValue(String key) {
        if (this.params != null) {
            return this.params.get(key);
        } else {
            return null;
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getResPath() {
        return this.path + "res/";
    }

    public void deleteDirectory() throws IOException {
        FileUtils.deleteDirectory(new File(getPath()));
    }

    //////////////////////////////////////////////////////////////////////////////////////////
}
