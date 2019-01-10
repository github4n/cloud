package com.iot.pack.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.pack.vo.ExecResult;
import com.iot.pack.vo.PackReq;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 描述：app打包工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/1 11:46
 */
public class PackUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(PackUtil.class);

    private final String basePath = "/usr/local/application/app-pack-service";  //基本路径
    //logo名称
    public final static String ICON_FILE_NAME = "logo.png";
    //启动页名称
    public final static String START_IMG_NAME = "startImg.png";
    //安卓推送图标
    public final static String PUSH_CONFIGURATION = "noti_small.png";
    //ios证书文件 certificate
    public final static String IOS_CERTIDICATE_FILE = "certificate.p12";
    //ios授权文件
    public final static String IOS_AUTHOR_FILE = "license.mobileprovision";
    //安卓谷歌推送文件
    public final static String ANDROID_JSON_FILE = "google-services.json";
    //安卓谷歌推送文件
    public final static String ANDROID_KEYSTORE_FILE = "keystore.jks";

    //临时文件路径
    private String tempPath;

    private String validationTempPath;



    public PackUtil() {
        this.tempPath = basePath + "/factory/pack_" + System.currentTimeMillis() + "/";
        this.validationTempPath = basePath + "/validation/number_" + System.currentTimeMillis() + "/";
    }

    /**
     * 证书效验
     * @param map
     * @return
     */
    public Boolean validation(Map map){
        try {
            FileUtils.forceMkdir(new File(validationTempPath));
            String fileLink = validationTempPath;
            HttpClientUtils.getInstance().download(map.get("pFileLink").toString(), fileLink);
            HttpClientUtils.getInstance().download(map.get("mFileLink").toString(), fileLink);
            HttpClientUtils.getInstance().download(map.get("aFileLink").toString(), fileLink);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



    public void pack(PackReq req) {
        LOGGER.info("======== app pack start ========");
        long st = System.currentTimeMillis();
        String appId = ""; //应用ID

        String androidUrl = "";
        String iosUrl = "";
        String yunUrl = "";
        String clearFlag = "";
        Map<String, String> configMap = null;
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            //创建临时文件
            try {
                FileUtils.forceMkdir(new File(tempPath));
            } catch (Exception e) {
                throw new Exception("mkdir error", e);
            }

            /** 1.下载文件 */
            String zipFilePath = "";
            try {
                LOGGER.info("******** 1.download zip file ********");
                zipFilePath = tempPath + "source.zip";
                HttpClientUtils.getInstance().download(req.getZipUrl(), zipFilePath);
            } catch (Exception e) {
                throw new Exception("download zip file error", e);
            }

            /** 2.解压文件 */
            String resPath = "";
            try {
                LOGGER.info("******** 2.decompression zip file ********");
                resPath = tempPath + "source";
                decompression(zipFilePath, "utf-8", resPath);
            } catch (Exception e) {
                throw new Exception("decompression zip file error", e);
            }

            /** 3.读取配置 */
            Integer androidFlag = 0;
            Integer iosFlag = 0;
            String androidPackPath = "";
            String iosPackPath = "";
            String local = "";
            String pgyerUrl = "";
            String pgyerKey = "";
            try {
                LOGGER.info("******** 3.read config ********");
                //本地配置
                String txt = basePath + "/script/config/config.txt";
                configMap = getConfig(txt);
                //应用配置
                txt = resPath + "/config.txt";
                Map<String, String> tempConfigMap = getConfig(txt);
                configMap.putAll(tempConfigMap);

                //配置信息
                androidFlag = Integer.valueOf(configMap.get("androidFlag"));
                iosFlag = Integer.valueOf(configMap.get("iosFlag"));
                appId = configMap.get("appId");
                androidPackPath = configMap.get("androidPackPath");
                //替换ver date
                androidPackPath = androidPackPath.replace("[ver]", configMap.get("androidVer"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                String dateStr = sdf.format(new Date());
                androidPackPath = androidPackPath.replace("[date]", dateStr);
                androidPackPath = androidPackPath.replace("[appName]", configMap.get("appName").replace("\\ ", "_"));
                iosPackPath = configMap.get("iosPackPath");
                yunUrl = configMap.get("yunUrl");
                pgyerUrl = configMap.get("pgyerUrl");
                pgyerKey = configMap.get("pgyerKey");
                local = configMap.get("local");
                clearFlag = configMap.get("clearFlag");

                if (androidFlag == 0 && iosFlag == 0) {
                    LOGGER.info("安卓和IOS都不打包，删除临时文件！");
                    FileUtils.deleteDirectory(new File(tempPath));
                    resultMap.put("rc", 1);
                    resultMap.put("desc", "all not pack,clear temp");
                    return;
                }
            } catch (Exception e) {
                throw new Exception("deal config file error", e);
            }

            try {
                LOGGER.info("******** download files ********");
                //先创建文件夹
                creatTempPath(configMap);
                //下载资源文件
                downFile(req);
                //下载配网引导图片
                downImg(configMap.get("themeValue"), configMap.get("guidImgPath"));

                //裁剪图片
                resizeImg(configMap);
            } catch (Exception e) {
                throw new Exception("deal resource file error", e);
            }

            if ("0".equals(local)) {
                /** 4.拉取svn代码 */
                try {
                    resultMap = packStep(4, "checkout", 1, appId);
                    if (!(Boolean) resultMap.get("flag")) {
                        return;
                    }
                } catch (Exception e) {
                    throw new Exception("exec shell checkout error", e);
                }

                /** 5.复制一份代码 */
                try {
                    resultMap = packStep(5, "copy_code", 1, appId);
                    if (!(Boolean) resultMap.get("flag")) {
                        return;
                    }
                } catch (Exception e) {
                    throw new Exception("exec shell copy_code error", e);
                }
            } else {
                //本地测试打包
                /** 4.拉取svn代码 */
                try {
                    resultMap = packStep(4, "checkout_local", 1, appId);
                    if (!(Boolean) resultMap.get("flag")) {
                        return;
                    }
                } catch (Exception e) {
                    throw new Exception("exec shell checkout_local error", e);
                }

                /** 5.复制一份代码 */
                LOGGER.info("******** 5.copy code ********");
            }

            /** 6.替换内容 */
            try {
                resultMap = packStep(6, "replace_file", 1, appId);
                if (!(Boolean) resultMap.get("flag")) {
                    return;
                }
            } catch (Exception e) {
                throw new Exception("exec shell replace_file error", e);
            }

            /** 7.执行WEB打包脚本 */
            try {
                resultMap = packStep(7, "web_pack", 2, appId);
                if (!(Boolean) resultMap.get("flag")) {
                    return;
                }
            } catch (Exception e) {
                throw new Exception("exec shell web_pack error", e);
            }

            /** 8.复制web打包文件 */
            try {
                resultMap = packStep(8, "copy_web", 1, appId);
                if (!(Boolean) resultMap.get("flag")) {
                    return;
                }
            } catch (Exception e) {
                throw new Exception("exec shell copy_web error", e);
            }

            if (androidFlag == 1) {
                /** 9.执行安卓打包脚本 */
                try {
                    resultMap = packStep(9, "android_pack", 3, appId);
                    if (!(Boolean) resultMap.get("flag")) {
                        return;
                    }
                } catch (Exception e) {
                    throw new Exception("exec shell android_pack error", e);
                }

                /** 9.上传安卓包到蒲公英 */
                try {
                    LOGGER.info("******** 10.upload apk ********");
                    String android_path = tempPath + "SmartHome_android";
                    String appPath = android_path + androidPackPath;
                    String resp = uploadApp(pgyerUrl, pgyerKey, appPath);
                    androidUrl = getUrl(resp);
                } catch (Exception e) {
                    throw new Exception("upload apk error", e);
                }
            }

            if (iosFlag == 1) {
                /** 10.执行苹果打包脚本 */
                try {
                    resultMap = packStep(11, "ios_pack", 4, appId);
                    if (!(Boolean) resultMap.get("flag")) {
                        return;
                    }
                } catch (Exception e) {
                    throw new Exception("exec shell ios_pack error", e);
                }

                /** 11.上传苹果包到蒲公英 */
                try {
                    LOGGER.info("******** 12.upload ios ipa ********");
                    String ios_path = tempPath + "SmartHome_ios";
                    String ipaPath = ios_path + iosPackPath;
                    String resp = uploadApp(pgyerUrl, pgyerKey, ipaPath);
                    iosUrl = getUrl(resp);
                } catch (Exception e) {
                    throw new Exception("upload ios ipa error", e);
                }
            }


            resultMap.put("rc", 0);
            resultMap.put("desc", "Success");

            long et = System.currentTimeMillis();
            LOGGER.info("======== app pack end ========");
            LOGGER.info("pack success. execute time：" + (et - st) + "ms！");
        } catch (Exception e) {
            LOGGER.error("app pack error", e);
            resultMap.put("rc", 1);
            resultMap.put("desc", e.getMessage());
        } finally {
            /** 12.删除临时文件 */
            if (!StringUtils.isEmpty(appId) && !StringUtils.isEmpty(yunUrl)) {
                //通知服务端打包结果
                sendResult(appId, androidUrl, iosUrl, yunUrl, configMap, resultMap);
            }
            try {
                LOGGER.info("******** 13.clear temp dir.{} ********",tempPath);
//                FileUtils.deleteDirectory(new File(tempPath));
                execCleanFileShell(tempPath);
                LOGGER.info("******** 13.clear temp dir.success ********");
            } catch (Exception e) {
                // 不影响结果了
                e.printStackTrace();
            }
        }
    }

    /**
     * 打包步骤
     *
     * @param step
     * @param fun
     * @param rc
     * @return
     */
    private Map<String, Object> packStep(int step, String fun, int rc,String appId) {
        LOGGER.debug("******** " + step + "." + fun + "." + appId + " ********");
        Map<String, Object> resultMap = Maps.newHashMap();
        ExecResult result = execShell(fun);
        resultMap.put("flag", result.getFlag());
        if (!result.getFlag()) {
            LOGGER.error("exec " + fun + " failed! ");
            resultMap.put("rc", rc);
            resultMap.put("desc", "exec " + fun + " failed");
            resultMap.put("detail", result.getError());
        }
        return resultMap;
    }

    /**
     * 创建临时文件夹
     */
    private void creatTempPath(Map<String, String> configMap) throws IOException {
        //先创建临时文件夹
        String resPath = tempPath + "source/";
        String imgs = configMap.get("androidImg");
        String[] imgArray = imgs.split(",");
        for (String vo : imgArray) {
            FileUtils.forceMkdir(new File(resPath + "android/" + vo));
        }

        String iosIconRoute = configMap.get("iosIconRoute");
        String iosLoadingRoute = configMap.get("iosLoadingRoute");
        FileUtils.forceMkdir(new File(resPath + "ios/" + iosIconRoute));
        FileUtils.forceMkdir(new File(resPath + "ios/" + iosLoadingRoute));
        FileUtils.forceMkdir(new File(resPath + "img"));
    }

    /**
     * 裁剪图片
     */
    private void resizeImg(Map<String, String> configMap) throws IOException {
        //logo图片生成
        String iosLogoRule = configMap.get("iosLogoRule");
        String[] iosLogoArray = iosLogoRule.split(",");
        String resPath = tempPath + "source/";
        String imgPath = tempPath + "source/img/";
        for (String name : iosLogoArray) {
            String fileName = name + ".png";
            String[] format = name.split("-");
            int w = Integer.parseInt(format[0]);
            //压缩复制
            FileUtil.resizeProportion(imgPath + ICON_FILE_NAME, resPath + "ios/" + configMap.get("iosIconRoute") + "/" + fileName, w, w);
        }

        //启动页生成
        //loading_logo@2x.png  181*221
        String size2 = configMap.get("iosLoadingSize2x");
        if (!StringUtils.isEmpty(size2) && size2.contains("x")) {
            String[] size_array = size2.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + configMap.get("iosLoading2x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + configMap.get("iosLoading2x"), 181, 221);
        }

        //loading_logo@3x.png 272*332
        String size3 = configMap.get("iosLoadingSize3x");
        String iosLoadingRoute = configMap.get("iosLoadingRoute");
        if (!StringUtils.isEmpty(size3)) {
            String[] size_array = size3.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading3x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading3x"), 768, 1024);
        }
        String size4 = configMap.get("iosLoadingSize4x");
        if (!StringUtils.isEmpty(size4)) {
            String[] size_array = size4.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading4x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading4x"), 640, 960);
        }
        String size5 = configMap.get("iosLoadingSize5x");
        if (!StringUtils.isEmpty(size5)) {
            String[] size_array = size5.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading5x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading5x"), 1125, 2436);
        }
        String size6 = configMap.get("iosLoadingSize6x");
        if (!StringUtils.isEmpty(size6)) {
            String[] size_array = size6.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading6x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading6x"), 640, 1136);
        }
        String size7 = configMap.get("iosLoadingSize7x");
        if (!StringUtils.isEmpty(size7)) {
            String[] size_array = size7.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading7x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading7x"), 1242, 2208);
        }
        String size8 = configMap.get("iosLoadingSize8x");
        if (!StringUtils.isEmpty(size8)) {
            String[] size_array = size8.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading8x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading8x"), 1536, 2048);
        }
        String size9 = configMap.get("iosLoadingSize2x");
        if (!StringUtils.isEmpty(size9)) {
            String[] size_array = size9.split("x");
            int width = Integer.parseInt(size_array[0]);
            int height = Integer.parseInt(size_array[1]);
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading2x"), width, height);
        } else {
            FileUtil.resizeProportion(imgPath + START_IMG_NAME, resPath + "ios/" + iosLoadingRoute + "/" + configMap.get("iosLoading2x"), 750, 1334);
        }


        //安卓相关文件生成打包
        Map<String, String> androidMap = Maps.newHashMap();
        //mipmap&mipmap-hdpi
        String imgs = configMap.get("androidImg");
        String[] imgArray = imgs.split(",");
        for (String vo : imgArray) {
            if (vo.contains("-")) {
                vo = vo.replace("mipmap-", "");
            }
            androidMap.put(vo, configMap.get(vo));
        }
        for (Map.Entry<String, String> entry : androidMap.entrySet()) {
            String[] v = entry.getValue().split(",");
            int w = Integer.parseInt(v[0]);
            String[] v1 = v[1].split("x");
            int loading_w = Integer.parseInt(v1[0]);
            int loading_h = Integer.parseInt(v1[1]);
            String dir = resPath + "android/" + "mipmap-" + entry.getKey();
            FileUtils.forceMkdir(new File(dir));
            //logo
            FileUtil.resize(imgPath + ICON_FILE_NAME, dir + "/" + configMap.get("androidLogoName"), w, w);
            //loading
            FileUtil.resize(imgPath + START_IMG_NAME, dir + "/" + configMap.get("androidLoadingName"), loading_w, loading_h);
        }
    }

    /**
     * 下载资源文件
     *
     * @param theme
     */
    private void downImg(String theme, String guidImgPath) throws IOException {
        String dirPath = this.tempPath + "source/web" + guidImgPath + "guidImages_" + theme;
        FileUtils.forceMkdir(new File(dirPath));
        //取出文件
        String filePath = this.tempPath + "source/web/downloadFileUrl.txt";
        File file = new File(filePath);
        try {
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            String url = null;
            while ((url = in.readLine()) != null) {
                String name = url.substring(0, url.indexOf("?"));
                name = name.substring(name.lastIndexOf("/") + 1, name.length());
                String path = dirPath + "/" + name;
                HttpClientUtils.getInstance().download(url, path);
            }
            in.close();
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 下载资源文件
     *
     * @param req
     */
    private void downFile(PackReq req) {
        String resPath = this.tempPath + "source/";
        //logo文件
        String logoUrl = req.getLogoUrl();
        if (!StringUtils.isEmpty(logoUrl)) {
            HttpClientUtils.getInstance().download(logoUrl, resPath + "img/" + ICON_FILE_NAME);
        }
        //启动页文件
        String loadingUrl = req.getLoadingUrl();
        if (!StringUtils.isEmpty(loadingUrl)) {
            HttpClientUtils.getInstance().download(loadingUrl, resPath + "img/" + START_IMG_NAME);
        }
        //安卓推送图标
        String pushConfigurationUrl = req.getPushConfigurationUrl();
        if (!StringUtils.isEmpty(pushConfigurationUrl)) {
            HttpClientUtils.getInstance().download(pushConfigurationUrl, resPath + "img/" + PUSH_CONFIGURATION);
        }
        //证书文件
        String pfileUrl = req.getPfileUrl();
        if (!StringUtils.isEmpty(pfileUrl)) {
            HttpClientUtils.getInstance().download(pfileUrl, resPath + "ios/" + IOS_CERTIDICATE_FILE);
        }
        //授权文件
        String mfileUrl = req.getMfileUrl();
        if (!StringUtils.isEmpty(mfileUrl)) {
            HttpClientUtils.getInstance().download(mfileUrl, resPath + "ios/" + IOS_AUTHOR_FILE);
        }

        //谷歌FCM文件
        String fcmfileUrl = req.getFcmfileUrl();
        if (!StringUtils.isEmpty(fcmfileUrl)) {
            HttpClientUtils.getInstance().download(fcmfileUrl, resPath + "android/" + ANDROID_JSON_FILE);
        }

        //安卓签名证书
        String keystoreUrl = req.getKeystoreUrl();
        if (!StringUtils.isEmpty(keystoreUrl)) {
            HttpClientUtils.getInstance().download(keystoreUrl, resPath + "android/" + ANDROID_KEYSTORE_FILE);
        }
    }

    /**
     * 执行脚本
     *
     * @param fun
     * @return
     */
    public ExecResult execShell(String fun) {
        String scriptPath = basePath + "/script";
        return execCommand("sh " + scriptPath + "/pack.sh " + tempPath + " " + fun);
    }

    /**
     * 清除临时文件
     * @param url
     */
    public void execCleanFileShell(String url){
        execCommand("rm -rf + " + url + " ");
    }

    /**
     * 执行shell脚本
     *
     * @param command
     */
    public ExecResult execCommand(String command) {
        LOGGER.info("execCommand:" + command);
        Process ps = null;
        Boolean flag = true;
        ExecResult result = new ExecResult();
        BufferedReader br = null;
        BufferedReader stderrReader = null;
        StringBuilder errBuff = new StringBuilder();
        try {
            ps = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            stderrReader = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
            String line;
            while ((line = br.readLine()) != null) {
                LOGGER.debug(line);
            }
            while ((line = stderrReader.readLine()) != null) {
                errBuff.append(line).append("\n");
                LOGGER.error(line);
            }
            Integer resultWait = ps.waitFor();
            LOGGER.debug("resultWait {}", resultWait);
//            ProcessUitl pu = new ProcessUitl(ps.getInputStream());
//            ProcessUitl pu1 = new ProcessUitl(ps.getErrorStream());
//            pu.start();
//            pu1.start();
//            ps.getOutputStream().close();
//            int exitValue = ps.waitFor();
//            if (0 != resultWait) {
//                LOGGER.error("call shell failed. error code is :" + resultWait);
//                flag = false;
//                result.setError(errBuff.toString());
//            } else {
//                flag = true;
//            }
//            LOGGER.debug(pu.getErrStr());
////            String errStr = pu1.getErrStr();
//            String errStr = pu.getErrStr();
//            result.setError(errStr);
//            //LOGGER.debug(pu1.getErrStr());
        } catch (Exception e) {
            LOGGER.error("execCommand failed. ", e);
//            flag = false;
            result.setError("execCommand failed: " + e.getMessage() + "\n" + errBuff.toString());
        } finally {
            try {
                br.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                stderrReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                ps.destroy();
            }
        }
        result.setFlag(flag);
        result.setError(errBuff.toString());
        LOGGER.debug("execCommand end!");
        return result;
    }

    /**
     * 上传蒲公英
     *
     * @param appPath
     * @throws Exception
     */
    public String uploadApp(String pgyerUrl, String pgyerKey, String appPath) throws Exception {
        Map<String, String> uploadParams = new LinkedHashMap<String, String>();
        uploadParams.put("_api_key", pgyerKey);
        // 中文需转uft-8编码，不然会乱码
        return HttpClientUtils.getInstance().uploadFileImpl(pgyerUrl, appPath, "file", uploadParams);
    }

    /**
     * 发送打包结果通知
     *
     * @param appId
     * @param androidUrl
     * @param iosUrl
     * @param yunUrl
     * @param resultMap
     */
    public void sendResult(String appId, String androidUrl, String iosUrl, String yunUrl, Map<String, String> configMap, Map<String, Object> resultMap) {
        int rc = (int) resultMap.get("rc");
        String desc = (String) resultMap.get("desc");
        String detail = (String) resultMap.get("detail");

        LOGGER.info("send pack result：appId={},rc={},desc={},androidUrl={},iosUrl={},yunUrl={}", appId, rc, desc, androidUrl, iosUrl, yunUrl);
        if (StringUtils.isEmpty(appId)) {
            LOGGER.warn("打包前置步骤失败，不发送邮件！");
            return;
        }
        Map<String, String> uploadParams = new LinkedHashMap<String, String>();
        uploadParams.put("appId", appId);
        uploadParams.put("rc", String.valueOf(rc));
        uploadParams.put("desc", desc);
        uploadParams.put("androidUrl", androidUrl);
        uploadParams.put("iosUrl", iosUrl);
        // 中文需转uft-8编码，不然会乱码
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Content-Type", "application/json;charset=UTF-8");
        HttpClientUtils.getInstance().httpPost(yunUrl, uploadParams, headMap);

        //邮件通知
        //消息内容
        String result = rc == 0 ? "成功" : "失败";
        String subject = "App应用 " + configMap.get("appName") + " " + configMap.get("profile") + "环境 自动化打包通知";
        String maiBody = "打包环境：" + configMap.get("profile");
        maiBody += "<br>应用名称：" + configMap.get("appName");
        maiBody += "<br>应用ID：" + configMap.get("appId");
        maiBody += "<br>版本号：" + (StringUtils.isEmpty(configMap.get("version")) ? "" : configMap.get("version"));
        maiBody += "<br>打包结果：" + result;
        desc += "<br>" + (StringUtils.isEmpty(detail) ? "" : detail);
        maiBody += "<br>描述：" + desc;

        String emailIos = configMap.get("emailIos");
        String emailAndroid = configMap.get("emailAndroid");
        String emailWeb = configMap.get("emailWeb");
        String emailCopy = configMap.get("emailCopy");
        String tenantEmail = configMap.get("email");

        if (rc == 0) {
            maiBody += "<br>安卓应用下载地址：" + androidUrl;
            maiBody += "<br>苹果应用下载地址：" + iosUrl;
        } else if (rc == 2) {
            //web打包失败
            subject = "WEB打包失败，自动化打包通知";
            if (!StringUtils.isEmpty(emailWeb)) {
                EmailUtil.sendMsg(emailWeb, subject, maiBody);
            }
        } else if (rc == 3) {
            //安卓打包失败
            subject = "Android打包失败，自动化打包通知";
            if (!StringUtils.isEmpty(emailAndroid)) {
                String logPath = this.tempPath+"/SmartHome_android/android.log";
                EmailUtil.sendMsg(emailAndroid, subject, maiBody,logPath);
            }
        } else if (rc == 4) {
            //ios打包失败
            subject = "IOS打包失败，自动化打包通知";
            if (!StringUtils.isEmpty(emailIos)) {
                String logPath = this.tempPath+"/SmartHome_ios/ios.log";
                EmailUtil.sendMsg(emailIos, subject, maiBody,logPath);
            }
        }

        //发送抄送者
        if (!StringUtils.isEmpty(emailCopy)) {
            String[] emails = emailCopy.split(",");
            for (String email : emails) {
                EmailUtil.sendMsg(email, subject, maiBody);
            }
        }

        //租户邮箱
        if (!StringUtils.isEmpty(tenantEmail)) {
            EmailUtil.sendMsg(tenantEmail, subject, maiBody);
        }
    }

    /**
     * 获取配置数据字典
     *
     * @param filePath
     * @return
     */
    private Map<String, String> getConfig(String filePath) {
        File file = new File(filePath);
        Map<String, String> maps = Maps.newHashMap();
        try {
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.contains("=")) {
                    String[] strs = line.split("=");
                    maps.put(strs[0], strs[1]);
                }
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
     * @param zipPath    zip路径
     * @param charset    编码
     * @param outPutPath 输出路径
     * @Description (解压)
     */
    public void decompression(String zipPath, String charset, String outPutPath) {
        long startTime = System.currentTimeMillis();
        try {
            ZipInputStream Zin = new ZipInputStream(new FileInputStream(zipPath), Charset.forName(charset));//输入源zip路径
            BufferedInputStream Bin = new BufferedInputStream(Zin);
            String Parent = outPutPath; //输出路径（文件夹目录）
            File Fout = null;
            ZipEntry entry;
            try {
                while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
                    Fout = new File(Parent, entry.getName());
                    if (!Fout.exists()) {
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(Fout);
                    BufferedOutputStream Bout = new BufferedOutputStream(out);
                    int b;
                    while ((b = Bin.read()) != -1) {
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
        long endTime = System.currentTimeMillis();
        System.out.println("解压耗费时间： " + (endTime - startTime) + " ms");
    }

    /**
     * 获取蒲公英短路径
     *
     * @param res
     * @return
     */
    private String getUrl(String res) {
        String s_str = "buildShortcutUrl\":\"";
        int len = s_str.length();
        int si = res.indexOf(s_str);
        int ei = res.indexOf("\",\"buildCreated");
        String code = res.substring(si + len, ei);
        return "https://www.pgyer.com/" + code;
    }
}
