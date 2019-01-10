package com.iot.portal.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.common.util.ToolUtil;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.NetworkTypeApi;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductConfigNetmodeApi;
import com.iot.device.api.ServiceBuyRecordApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.api.TechnicalRelateApi;
import com.iot.device.vo.req.AddProductReq;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.entity.FileBean;
import com.iot.file.util.FileUtil;
import com.iot.file.vo.FileInfoResp;
import com.iot.message.api.MessageApi;
import com.iot.message.dto.TenantMailInfoDto;
import com.iot.message.entity.TenantMailInfo;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.payment.enums.goods.TechnicalSchemeEnum;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.service.FileService;
import com.iot.portal.service.LangInfoService;
import com.iot.portal.service.ServiceModuleCoreService;
import com.iot.portal.web.utils.AppPackUtil;
import com.iot.portal.web.utils.HttpClientUtils;
import com.iot.portal.web.utils.ProcessUitl;
import com.iot.portal.web.vo.CreateAppReq;
import com.iot.portal.web.vo.FileResp;
import com.iot.portal.web.vo.PackNotifyReq;
import com.iot.portal.web.vo.PortalPropertyListResp;
import com.iot.portal.web.vo.ProductListResp;
import com.iot.portal.web.vo.SaveCertificateReq;
import com.iot.portal.web.vo.SetBaseConfigReq;
import com.iot.portal.web.vo.SetIndividuationReq;
import com.iot.portal.web.vo.SetInstallUrlReq;
import com.iot.portal.web.vo.TenantMailInfoReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.vo.LangItem;
import com.iot.tenant.vo.LangKey;
import com.iot.tenant.vo.req.GetAppReq;
import com.iot.tenant.vo.req.GetLangReq;
import com.iot.tenant.vo.req.SaveAppProductReq;
import com.iot.tenant.vo.req.SaveAppReq;
import com.iot.tenant.vo.req.SaveGuideReq;
import com.iot.tenant.vo.req.SaveLangReq;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.AppProductResp;
import com.iot.tenant.vo.resp.GetGuideResp;
import com.iot.tenant.vo.resp.GetLangResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 描述：创建应用相关-控制器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 15:16
 */
@Api(value = "创建应用", description = "创建应用")
@RestController
@RequestMapping("/app")
public class AppController {
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private final Logger logger = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private AppApi appApi;
    @Autowired
    private FileApi fileApi;
    @Autowired
    private ServiceModuleApi serviceModuleApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private DeviceTypeApi deviceTypeApi;
    @Autowired
    private ServiceModuleCoreService serviceModuleCoreService;
    @Autowired
    private MessageApi messageApi;
    @Autowired
    private LangInfoTenantApi langInfoTenantApi;
    @Autowired
    private TechnicalRelateApi technicalRelateApi;
    @Autowired
    private LangInfoService langInfoService;
    @Autowired
    private TenantApi tenantApi;
    @Autowired
    private FileUploadApi fileUploadApi;
    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;
    @Autowired
    private NetworkTypeApi networkTypeApi;
    @Autowired
    private ProductConfigNetmodeApi productConfigNetmodeApi;

    @Autowired
    private UserApi userApi;
    @Value("${upload.img.str}")
    private String uploadImgStr;
    @Value("${upload.file.str}")
    private String uploadFileStr;
    @Autowired
    private FileService fileService;


    /////////////////////////////////////////// 应用配置 /////////////////////////////////////////////////////

    @ApiOperation("获取应用信息")
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public CommonResponse<AppInfoResp> getAppById(@RequestParam("id") Long id) {
        AppInfoResp resp = appApi.getAppById(id);
        Date date = new Date();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(resp.getConfirmTime());
            calendar.add(Calendar.WEEK_OF_MONTH, 2);
            Long result = calendar.getTimeInMillis();
            if (date.getTime() > result) {
                resp.setConfirmStatus(1);
                appApi.customConfirmAppPackage(resp.getId(), 1);
            }
        } catch (Exception e) {
            logger.warn("Confirm app error", e);
        }
        //图片fileId转url
        String logoFileId = resp.getLogoFileId();
        String loadImgFileId = resp.getLoadingImgFileId();
        String keystoreId = resp.getKeystoreId();
        String privacyCnLinkId = resp.getPrivacyCnLinkId();
        String privacyEnLinkId = resp.getPrivacyEnLinkId();
        String mf = resp.getMfile();
        String pf = resp.getPfile();
        String fcmFileId = resp.getFcmFileId();
        String iosFileId = resp.getIosFileId();
        List<String> fileIds = Lists.newArrayList();
        if (!StringUtils.isEmpty(mf)){
            fileIds.add(mf);
        }
        if (!StringUtils.isEmpty(pf)){
            fileIds.add(pf);
        }
        if (!StringUtils.isEmpty(fcmFileId)){
            fileIds.add(fcmFileId);
        }
        if (!StringUtils.isEmpty(iosFileId)){
            fileIds.add(iosFileId);
        }
        if (logoFileId != null) {
            fileIds.add(logoFileId);
        }
        if (loadImgFileId != null) {
            fileIds.add(loadImgFileId);
        }
        if (keystoreId != null) {
            fileIds.add(keystoreId);
        }
        if (!StringUtils.isEmpty(privacyCnLinkId)){
            fileIds.add(privacyCnLinkId);
        }
        if (!StringUtils.isEmpty(privacyEnLinkId)){
            fileIds.add(privacyEnLinkId);
        }
        if (fileIds.size() > 0) {
            Map<String, String> urlMap = fileApi.getGetUrl(fileIds);
            resp.setLogoUrl(urlMap.get(logoFileId));
            resp.setLoadingImgUrl(urlMap.get(loadImgFileId));
            resp.setKeystoreUrl(urlMap.get(keystoreId));
            resp.setPrivacyCnLinkId(urlMap.get(privacyCnLinkId));
            resp.setPrivacyEnLinkId(urlMap.get(privacyEnLinkId));
            resp.setMfile(urlMap.get(mf));
            resp.setPfile(urlMap.get(pf));
            resp.setIosFileId(urlMap.get(iosFileId));
            resp.setFcmFileId(urlMap.get(fcmFileId));
        }
        //证书文件名
        if (mf != null) {
        	FileBean fileBean = fileApi.getFileInfoByFileId(mf);
            if (fileBean != null) {
                resp.setMfileName(fileBean.getFileName());
            }
        }
        if (pf != null) {
        	FileBean fileBean = fileApi.getFileInfoByFileId(pf);
            if (fileBean != null) {
                resp.setPfileName(fileBean.getFileName());
            }
        }
        if (StringUtil.isNotEmpty(resp.getImageId())) {
            FileResp fileResp = fileService.getUrl(resp.getImageId());
            if (fileResp != null) {
                resp.setImageId(fileResp.getUrl());
            }
        }
        if (fcmFileId != null) {
        	FileBean fileBean = fileApi.getFileInfoByFileId(fcmFileId);
            if (fileBean != null) {
                resp.setFcmFileName(fileBean.getFileName());
            }
        }
        if (iosFileId != null) {
        	FileBean fileBean = fileApi.getFileInfoByFileId(iosFileId);
            if (fileBean != null) {
                resp.setIosFileName(fileBean.getFileName());
            }
        }
        // 查询支付状态 addDate: 2018-11-13 author：wucheng
        Long tenantId = SaaSContextHolder.currentTenantId();
        List<ServiceBuyRecordResp> lists = serviceBuyRecordApi.getServiceBuyRecord(id, GoodsTypeEnum.APP_PACKAGE.getCode(), tenantId);
        if (lists != null && lists.size() > 0) {
            Integer payStatus = lists.get(0).getPayStatus();
            resp.setPayStatus(payStatus);
        }
        if (resp.getConfirmStatus()!=null && resp.getConfirmStatus()==0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(resp.getConfirmTime());
            calendar.add(Calendar.WEEK_OF_MONTH,2);
            Long futureResult = calendar.getTimeInMillis();
            Long nowResult = new Date().getTime();
            Long result = (futureResult-nowResult)/86400000;
            resp.setCountDown(result);
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", resp);
    }

    @ApiOperation("获取安卓打包keys")
    @RequestMapping(value = "/androidKeys", method = RequestMethod.GET)
    public CommonResponse androidKeys(@RequestParam("id") Long id) {
        AppInfoResp appInfoResp = appApi.getAppById(id);
        FileResp fileResp = new FileResp();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(appInfoResp.getKeystoreId())) {
            fileResp = fileService.getUrl(appInfoResp.getKeystoreId());

        }
        return CommonResponse.success(fileResp);
    }

    @ApiOperation("用户确认打包")
    @RequestMapping(value = "/customConfirmAppPackage", method = RequestMethod.GET)
    public CommonResponse customConfirmAppPackage(@RequestParam("id") Long id) {
        Long result = appApi.customConfirmAppPackage(id, 1);
        return CommonResponse.success(result);
    }

    @ApiOperation("更新显示状态")
    @RequestMapping(value = "/updateDisplayIdentification", method = RequestMethod.GET)
    public CommonResponse updateDisplayIdentification(@RequestParam("id") Long id, @RequestParam("displayIdentification") Integer displayIdentification) {
        Long result = appApi.updateDisplayIdentification(id, displayIdentification);
        return CommonResponse.success(result);
    }

    @ApiOperation("打包前效验")
    @LoginRequired(Action.Normal)
    @RequestMapping(value = "/execPackageValidation", method = RequestMethod.GET)
    public CommonResponse<Boolean> execPackageValidation(@RequestParam("appId") Long appId){
        AppInfoResp appInfoResp = appApi.getAppById(appId);
        if (appInfoResp!=null) {
            List list = appApi.getAppProductIdList(appInfoResp.getId());
            if (CollectionUtils.isEmpty(list)){
                return new CommonResponse<>(ResultMsg.FAIL, "The product is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getMfile())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The mobileprovision certificate is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getPfile())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The P12 certificate is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getFilePassword())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The certificate password is empty.", false);
            }
            TenantMailInfo tenantMailInfo = messageApi.getTenantMailInfo(appId);
            if (tenantMailInfo == null) {
                return new CommonResponse<>(ResultMsg.FAIL, "The mail information is empty.", false);
            } else {
                if (StringUtils.isEmpty(tenantMailInfo.getMailName())) {
                    return new CommonResponse<>(ResultMsg.FAIL, "The mail address is empty.", false);
                }
                if (StringUtils.isEmpty(tenantMailInfo.getPassWord())) {
                    return new CommonResponse<>(ResultMsg.FAIL, "The mail server address is empty.", false);
                }
                if (StringUtils.isEmpty(tenantMailInfo.getMailHost())) {
                    return new CommonResponse<>(ResultMsg.FAIL, "The mail host is empty.", false);
                }
                if (StringUtils.isEmpty(tenantMailInfo.getMailPort())) {
                    return new CommonResponse<>(ResultMsg.FAIL, "The mail port number is empty.", false);
                }
            }
            if (StringUtils.isEmpty(appInfoResp.getFcmFileId())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The FCM information is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getGoogleUrl())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The Google URL address is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getGoogleKey())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The Google key is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getFilePassword())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The file password is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getBackgroundColor())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The background color value is empty.", false);
            }
            if (StringUtils.isEmpty(appInfoResp.getImageId())) {
                return new CommonResponse<>(ResultMsg.FAIL, "The background image is empty.", false);
            }
            return new CommonResponse<>(ResultMsg.SUCCESS, "success", true);
        } else {
            return new CommonResponse<>(ResultMsg.FAIL, "Cannot find the APP.", false);
        }
    }

    @ApiOperation("获取应用打包状态")
    @RequestMapping(value = "/getAppStatusById", method = RequestMethod.GET)
    public CommonResponse<Integer> getAppStatusById(@RequestParam("id") Long id) {
        Integer status = 0; //应用状态 0未打包 1打包中 2打包成功 3打包失败
        AppInfoResp resp = appApi.getAppById(id);
        if (resp != null) {
            status = resp.getStatus();
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", status);
    }

    @ApiOperation("创建应用")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResponse<Long> create(@RequestBody CreateAppReq req) {
        if (SaaSContextHolder.currentTenantId().equals(SystemConstants.BOSS_TENANT)){
            logger.debug("exception for tenantId ");
            throw new BusinessException(BusinessExceptionEnum.TENANT_ID_ERROR);
        }
        SaveAppReq saveAppReq = new SaveAppReq();
        saveAppReq.setAppName(req.getAppName());
        saveAppReq.setEnName(req.getEnName());
        saveAppReq.setPackName(req.getPackName());
        saveAppReq.setAppCode(req.getAppCode());
        saveAppReq.setTenantId(getTenantId());
        saveAppReq.setId(req.getId());
        // 获取当前登录人主账号信息，创建时以主账号为主
        List<FetchUserResp> userResps = userApi.getAdminUserByTenantId(Arrays.asList(SaaSContextHolder.currentTenantId()));
        if (userResps != null && userResps.size() > 0) {
            saveAppReq.setCreateBy(userResps.get(0).getId());
        }
        saveAppReq.setChooseLang("1,2"); //默认选中中文和英文
        //创建签名证书
        TenantInfoResp resp = tenantApi.getTenantById(getTenantId());

        //证书秘钥
        String keypass = UUID.randomUUID().toString().replace("-","");
        String storepass = UUID.randomUUID().toString().replace("-","");
        String keystoreFileId = createKeystore(req.getAppCode(), resp,keypass,storepass);

        saveAppReq.setKeypass(keypass);
        saveAppReq.setStorepass(storepass);

        saveAppReq.setKeystoreId(keystoreFileId);
        List list = appApi.appInfoValidation(saveAppReq);
        if (CollectionUtils.isNotEmpty(list)){
            return new CommonResponse(ResultMsg.FAIL,"fail","The APP package name has already existed.");
        }
        Long appId = appApi.saveApp(saveAppReq);
        //拷贝文案
        CopyLangInfoReq langInfoReq = new CopyLangInfoReq(SaaSContextHolder.currentTenantId(), appId, -1L, LangInfoObjectTypeEnum.appConfig.toString(), SaaSContextHolder.getCurrentUserId());
        langInfoTenantApi.copyLangInfo(langInfoReq);
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appId);
    }

    /**
     * 生成android签名证书
     *
     * @param code
     * @return
     */
    private String createKeystore(String code, TenantInfoResp tenantInfo,String keypass,String storepass) {
        String fileId = null;
        try {
            //生成android签名证书
            String filePath = "/tmp/" + code.replace(" ","_") + "_" + System.currentTimeMillis() + ".jks";
            String cn = tenantInfo.getName();
//            cn = cn.replace(" ","_");
            String city = tenantInfo.getCity();
            String st = tenantInfo.getProvince();
            String country = tenantInfo.getCountry();
//            String pwd = "ldsa02318";
            String alias = cn + "_" + code;
//            alias = alias.replace(" ","_");

            StringBuffer cmd = new StringBuffer();
            cmd.append("keytool -genkey -v -alias ");
            cmd.append("\""+alias+"\"");
            cmd.append(" -keyalg RSA -keysize 1024 -validity 2000 ");
            cmd.append("-keystore ");
            cmd.append(filePath);
            cmd.append(" -keypass " + keypass + " -storepass " + storepass + " ");
            cmd.append("-dname \"CN=" + cn + ",OU=" + cn + ",O=" + cn + ",L=" + city + ",ST=" + st + ",C=" + country + "\"");
            //创建脚本文件
            String shPath = "/tmp/keystore.sh";
            PrintWriter pw = null;

            File keystoreCmdFile = new File(shPath);
            // 创建文件
            if (!keystoreCmdFile.exists()) {
                keystoreCmdFile.createNewFile();
            }
            pw = new PrintWriter(new FileWriter(keystoreCmdFile));

            pw.println("#/bin/sh");
            pw.println(cmd.toString());
            pw.close();
            //添加执行权限
            execCommand("chmod 777 " + shPath);
            //执行脚本文件
            int rc = execCommand("sh " + shPath);
            if (rc == 0) {
                //上传证书
                File file = new File(filePath);
                if (file.exists()) {
                    MultipartFile multipartFile = FileUtil.toMultipartFile(file);
                    fileId = fileUploadApi.uploadFile(multipartFile, tenantInfo.getId());
                    //删除证书
                    FileUtil.deleteAllFilesOfDir(file);
                } else {
                    logger.warn("证书文件不存在！");
                }
            }
            //删除脚本文件
            FileUtils.forceDelete(new File(shPath));
        } catch (IOException e) {
            logger.error("生成签名证书失败", e);
        }
        return fileId;
    }

    /**
     * 执行shell脚本
     *
     * @param command
     */
    public int execCommand(String command) {
        logger.info("execCommand:" + command);
        int exitValue = 1;
        try {
            Process ps = Runtime.getRuntime().exec(command);
            ProcessUitl pu = new ProcessUitl(ps.getInputStream());
            ProcessUitl pu1 = new ProcessUitl(ps.getErrorStream());
            pu.start();
            pu1.start();
            ps.getOutputStream().close();
            exitValue = ps.waitFor();
            if (0 != exitValue) {
                logger.error("call shell failed. error code is :" + exitValue);
            }
            logger.debug(pu.getErrStr());
            logger.debug(pu1.getErrStr());
        } catch (Exception e) {
            logger.error("execCommand failed. ", e);
        }
        logger.info("execCommand end!");
        return exitValue;
    }

    @ApiOperation("复制应用")
    @RequestMapping(value = "/copyApp", method = RequestMethod.GET)
    public CommonResponse<Boolean> copyApp(@RequestParam("appId") Long appId) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.copyApp(appId));
    }

    @ApiOperation("分页获取应用列表")
    @RequestMapping(value = "/getAppPage", method = RequestMethod.POST)
    public CommonResponse<Page<AppInfoResp>> getAppPage(@RequestBody GetAppReq req) {
        Page<AppInfoResp> page = appApi.getAppPage(req);
        if (page.getTotal() > 0) {
            List<AppInfoResp> list = page.getResult();
            //图标url转换
            List<String> fileIds = Lists.newArrayList();
            for (AppInfoResp vo : list) {
                String logoFileId = vo.getLogoFileId();
                if (!StringUtil.isEmpty(logoFileId)) {
                    fileIds.add(logoFileId);
                }
            }

            if (CollectionUtils.isNotEmpty(fileIds)) {
                Map<String, String> resMap = fileApi.getGetUrl(fileIds);
                for (AppInfoResp vo : list) {
                    vo.setLogoUrl(resMap.get(vo.getLogoFileId()));
                }
            }
            page.setResult(list);
        }
        page.setPageNum(req.getPageNum());

        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", page);
    }

    @ApiOperation("删除应用")
    @RequestMapping(value = "/delApp", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Boolean> delApp(@RequestBody List<Long> ids) {
        List<AppInfoResp> list = appApi.getAppByIds(ids);
        List validation = new ArrayList();
        list.forEach(m->{
            if (m.getStatus()==2){
                validation.add(m.getId());
            }
        });
        if (validation.size()>0){
            return new CommonResponse<>(ResultMsg.FAIL, "Delete failed as the APP has already successfully created.",false);
        } else {
            return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.delApp(ids));
        }
    }

    @ApiOperation("删除应用")
    @RequestMapping(value = "/delAppByPost", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Boolean> delAppByPost(@RequestBody List<Long> ids) {
        List<AppInfoResp> list = appApi.getAppByIds(ids);
        List validation = new ArrayList();
        list.forEach(m->{
            if (m.getStatus()==2){
                validation.add(m.getId());
            }
        });
        if (validation.size()>0){
            return new CommonResponse<>(ResultMsg.FAIL, "Delete failed as the APP has already successfully created.",false);
        } else {
            return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.delApp(ids));
        }
    }

    @ApiOperation("配置安装链接")
    @RequestMapping(value = "/setInstallUrl", method = RequestMethod.POST)
    public CommonResponse<Boolean> setInstallUrl(@RequestBody SetInstallUrlReq req) {
        SaveAppReq saveAppReq = new SaveAppReq();
        saveAppReq.setId(req.getAppId());
        saveAppReq.setAndroidInstallUrl(req.getAndroidUrl());
        saveAppReq.setIosInstallUrl(req.getIosUrl());
        saveAppReq.setTenantId(getTenantId());
        appApi.saveApp(saveAppReq);
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", true);
    }


    @ApiOperation("保存个性化设置")
    @RequestMapping(value = "/setIndividuation", method = RequestMethod.POST)
    public CommonResponse<Boolean> setIndividuation(@RequestBody SetIndividuationReq req) {
        SaveAppReq saveAppReq = new SaveAppReq();
        saveAppReq.setId(req.getAppId());
        saveAppReq.setLogoFileId(req.getLogoFileId());
        saveAppReq.setLoadingImgFileId(req.getLoadImgFileId());
        saveAppReq.setTheme(req.getTheme());
        saveAppReq.setTenantId(getTenantId());
        appApi.saveApp(saveAppReq);
        List<String> fileIds = new ArrayList<>();
        if (!StringUtil.isBlank(req.getLoadImgFileId())) {
            fileIds.add(req.getLoadImgFileId());
        }
        if (!StringUtil.isBlank(req.getLogoFileId())) {
            fileIds.add(req.getLogoFileId());
        }
        if (!fileIds.isEmpty()) {
            fileApi.saveFileInfosToDb(fileIds);
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", true);
    }


    @ApiOperation("保存基本功能设置")
    @RequestMapping(value = "/setBaseConfig", method = RequestMethod.POST)
    public CommonResponse<Boolean> setBaseConfig(@RequestBody SetBaseConfigReq req) {
        SaveAppReq saveAppReq = new SaveAppReq();
        saveAppReq.setId(req.getAppId());
        saveAppReq.setHasSecurity(req.getHasSecurity());
        saveAppReq.setHasPrivacyPolicy(req.getHasPrivacyPolicy());
        saveAppReq.setZnCopyright(req.getZnCopyright());
        saveAppReq.setEnCopyright(req.getEnCopyright());
        saveAppReq.setTenantId(getTenantId());
        saveAppReq.setPrivacyCnLinkId(req.getPrivacyCnLinkId());
        saveAppReq.setPrivacyEnLinkId(req.getPrivacyEnLinkId());
        saveAppReq.setIsFavorite(req.getIsFavorite());
        appApi.saveApp(saveAppReq);
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", true);
    }

    //////////////////////////////////////////// 多语言管理 //////////////////////////////////////////////////////////

    @ApiOperation("保存多语言列表")
    @RequestMapping(value = "/saveLanguageList", method = RequestMethod.POST)
    public CommonResponse<Boolean> saveLanguageList(@RequestBody SaveLangReq req) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.saveLang(req));
    }

    @ApiOperation("获取多语言列表")
    @RequestMapping(value = "/getLanguageList", method = RequestMethod.GET)
    public CommonResponse<GetLangResp> getLanguageList(@RequestParam("appId") Long appId) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", getLangResp(appId));
    }

    private GetLangResp getLangResp(Long appId) {
        List<Long> appProductList = appApi.getAppProductIdList(appId);
        if (appProductList.size() > 0) {
            //根据产品获取到keys
            List<String> keys = Lists.newArrayList();
            for (Long productId : appProductList) {
                List<String> keyList = getKeys(productId);
                keys.addAll(keyList);
            }
            GetLangReq getLangReq = new GetLangReq();
            getLangReq.setAppId(appId);
            // 测试keys
            /*keys.add("dp_switch_on");
            keys.add("dp_switch_off");
            keys.add("createAppLangFile");*/
            getLangReq.setKeys(keys);
            return appApi.getLang(getLangReq);
        } else {
            logger.info("应用没有关联产品，无法获取多语言数据！");
            return new GetLangResp();
        }
    }

    private Map<String, String> getLangMap(GetLangResp resp) {
        Map<String, String> langMap = Maps.newHashMap();
        String langs = resp.getChooseLang();
        if (StringUtils.isEmpty(langs)) {
            return langMap;
        }
        String[] langArray = langs.split(",");
        List<LangKey> langKeys = resp.getLangKeys();
        StringBuffer sbf = new StringBuffer();
        for (String l : langArray) {
            Integer lang = Integer.valueOf(l);
            for (LangKey vo : langKeys) {
                String key = vo.getKey();
                List<LangItem> items = vo.getItems();
                for (LangItem item : items) {
                    if (item.getLang() == lang) {
                        String ln = "'" + key + "'$:'" + item.getContent() + "',";
                        sbf.append(ln + "&");
                    }
                }
            }
            langMap.put(getLangCode(lang), sbf.toString());
            sbf.setLength(0);
        }

        //展示
        /*for (Map.Entry<String, String> entry : langMap.entrySet()) {
            logger.info("--------");
            logger.info(entry.getKey() + ":");
            logger.info(entry.getValue());
        }*/
        return langMap;
    }

    private String getLangCode(Integer lang) {
        String code = "";
        if (lang == 1) {
            code = "en_us";
        } else if (lang == 2) {
            code = "zn_ch";
        }
        return code;
    }

    /**
     * 获取产品属性keys
     *
     * @param productId
     * @return
     */
    private List<String> getKeys(Long productId) {
        List<String> list = Lists.newArrayList();
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
        if (CollectionUtils.isNotEmpty(productModuleList)) {
            for (ServiceModuleListResp productModule : productModuleList) {
                Long serviceModuleId = productModule.getId();
                //功能组一样比较方法、事件、属性
                //1.1获取功能组方法的比较
                //List<PortalActionListResp> productActionList = serviceModuleCoreService.findActionsByServiceModuleId(serviceModuleId);
                //1.2 事件
                //List<PortalEventListResp> productEventList = serviceModuleCoreService.findEventsByServiceModuleId(serviceModuleId);
                //1.3 属性
                List<PortalPropertyListResp> productPropertyList = serviceModuleCoreService.findPropertiesByServiceModuleId(serviceModuleId);

                for (PortalPropertyListResp vo : productPropertyList) {
                    list.add(vo.getCode());
                }
            }
        }
        return list;
    }

    ///////////////////////////////////////////// 上传图片 ///////////////////////////////////////////////////////////

    @ApiOperation("上传图片")
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public CommonResponse<String> uploadImg(MultipartHttpServletRequest multipartRequest) {
        //判空
        if (multipartRequest == null || !multipartRequest.getFileNames().hasNext()) {
            return new CommonResponse<>(ResultMsg.FAIL, "The file cannot be empty.", "");
        }

        //上传文件
        MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        String fileSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        String[] imgResult = uploadImgStr.split(",");
        String[] fileResult = uploadFileStr.split(",");
        Boolean isImg = Arrays.asList(imgResult).contains(fileSuffix);
        Boolean isOk = Arrays.asList(fileResult).contains(fileSuffix);
        if (isImg || isOk) {
            String fileId = this.fileUploadApi.uploadFile(multipartFile, getTenantId());
            return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", fileId);
        } else {
            return new CommonResponse<>(ResultMsg.FAIL, "Illegal file type.", "");
        }

    }

    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    ////////////////////////////////////////// 配网信息 //////////////////////////////////////////////////////////////

    @ApiOperation("获取关联产品")
    @RequestMapping(value = "/getAppProduct", method = RequestMethod.GET)
    public CommonResponse<List<AppProductResp>> getAppProduct(@RequestParam("appId") Long appId) {
        List<AppProductResp> list = appApi.getAppProduct(appId);

        List<AppProductResp> resList = Lists.newArrayList();
        for (AppProductResp vo : list) {
            //获取产品信息
            ProductResp productResp = productApi.getProductById(vo.getProductId());
            if (productResp != null) {
                if (productResp.getAuditStatus()==null || !productResp.getAuditStatus().equals(2)){
                    continue;
                }
                vo.setProductName(productResp.getProductName());
                //获取分类名称
                vo.setCatalogName(productResp.getCatalogName());
                vo.setIcon(productResp.getDefaultIcon());
                vo.setDeviceTypeName(productResp.getDeviceTypeName());
                resList.add(vo);
            }
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", resList);
    }   

    @ApiOperation("保存关联产品")
    @RequestMapping(value = "/saveAppProduct", method = RequestMethod.POST)
    public CommonResponse<Long> saveAppProduct(@RequestBody SaveAppProductReq req) {
        //修改产品名称
        AddProductReq addProductReq = new AddProductReq();
        addProductReq.setId(req.getProductId());
        addProductReq.setIcon(req.getIconFileId());
        addProductReq.setProductName(req.getProductName());
        addProductReq.setTenantId(SaaSContextHolder.currentTenantId());
        productApi.updateProductBaseInfo(addProductReq);
        //拷贝配网步骤信息
        ProductResp productResp = productApi.getProductById(req.getProductId());
        req.setDeviceTypeId(productResp.getDeviceTypeId());
        //根据技术方案做不同的配网方式拷贝逻辑（
        List<NetworkTypeResp> networkTypeResps = new ArrayList<>();
        if (TechnicalSchemeEnum.copyNetworkByTechnical(Long.valueOf(productResp.getCommunicationMode()))) {
            //1、wifi方案、蓝牙方案、IPC方案则根据产品选择的技术方案支持的配网模式直接copy对应的配网文案，
            networkTypeResps = technicalRelateApi.technicalSupportNetworkType(Long.valueOf(productResp.getCommunicationMode()));
        } else {
            //2、如果是网关方案，还得根据创建产品选择的配网编码(product_config_mode)去拷贝对应的配网文案）
            List<ProductConfigNetmodeRsp> configNetmodeRsps = productConfigNetmodeApi.listByProductId(req.getProductId());
            if (configNetmodeRsps!=null && !configNetmodeRsps.isEmpty()) {
                List<String> typeCodes = configNetmodeRsps.stream().map(ProductConfigNetmodeRsp::getName).collect(Collectors.toList());
                networkTypeResps = networkTypeApi.findByTypeCode(typeCodes);
            }
        }
        if (networkTypeResps!=null && !networkTypeResps.isEmpty()) {
            List<Long> networkIdList = networkTypeResps.stream().map(NetworkTypeResp::getId).collect(Collectors.toList());
            //防止配网编码被重用，还是得在根据设备类型支持配网方式过滤
            List<NetworkTypeResp> networkTypes = technicalRelateApi.deviceSupportNetworkType(productResp.getDeviceTypeId());
            List<Long> dntIds = networkTypes.stream().map(NetworkTypeResp::getId).collect(Collectors.toList());
            //取两者交集
            List<Long> networkIds = dntIds.stream().filter(netId-> networkIdList.contains(netId)).collect(Collectors.toList());
            req.setNetworkTypeIds(networkIds);
        }
        req.setUserId(SaaSContextHolder.getCurrentUserId());
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.saveAppProduct(req));
    }

    @ApiOperation("删除关联产品")
    @RequestMapping(value = "/delAppProduct", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Boolean> delAppProduct(@RequestBody List<Long> ids) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.delAppProduct(ids));
    }

    @ApiOperation("删除关联产品")
    @RequestMapping(value = "/delAppProductByPost", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Boolean> delAppProductByPost(@RequestBody List<Long> ids) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.delAppProduct(ids));
    }

    @ApiOperation("获取产品列表")
    @RequestMapping(value = "/getProductList", method = RequestMethod.GET)
    public CommonResponse<List<ProductListResp>> getProductList() {
        ProductReq productReq = new ProductReq();
        productReq.setTenantId(getTenantId());
        List<ProductResp> plist = productApi.findPrimaryProductListByTenantId(productReq);
        if(CollectionUtils.isNotEmpty(plist)){
            getProductIconNotUpload(plist);
        }
        List<String> fileIds = Lists.newArrayList();
        List<ProductListResp> respList = Lists.newArrayList();
        for (ProductResp vo : plist) {
            if (vo.getAuditStatus() == null || vo.getAuditStatus() != 2) {
                //只要审核通过的产品
                continue;
            }
            if (vo.getIsDirectDevice()==0){
                continue;
            }

            ProductListResp resp = new ProductListResp();
            resp.setId(vo.getId());
            resp.setName(vo.getProductName());
            resp.setIconFileId(vo.getIcon());
            String icon = vo.getIcon();
            if (icon != null) {
                fileIds.add(icon);
            }
            respList.add(resp);
        }
        if (fileIds.size() > 0) {
            Map<String, String> fileMap = fileApi.getGetUrl(fileIds);
            for (ProductListResp vo : respList) {
                vo.setIconUrl(fileMap.get(vo.getIconFileId()));
            }
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", respList);
    }

    private void getProductIconNotUpload(List<ProductResp> productList){
        List<ProductResp> noIconProducts = productList.stream().filter(p -> StringUtil.isEmpty(p.getIcon())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(noIconProducts)){
            List<Long> deviceTypeIds = noIconProducts.stream().map(ProductResp::getDeviceTypeId).collect(Collectors.toList());
            List<DeviceTypeResp> deviceTypes = this.deviceTypeApi.getByIds(deviceTypeIds);
            if(CollectionUtils.isNotEmpty(deviceTypes)){
                Map<Long,String> imgMap = deviceTypes.stream().collect(Collectors.toMap(deviceType->deviceType.getId(),deviceType->deviceType.getImg(),(k1,k2)->k1));
                for(ProductResp productResp:productList){
                    if(StringUtil.isEmpty(productResp.getIcon())){
                        String imgId = imgMap.get(productResp.getDeviceTypeId());
                        productResp.setIcon(imgId);
                    }
                }
            }
        }
    }

    ////////////////////////////////////////// 配网引导 //////////////////////////////////////////////////////////////

    @ApiOperation("保存配网引导")
    @RequestMapping(value = "/saveGuide", method = RequestMethod.POST)
    public CommonResponse<Boolean> saveGuide(@RequestBody SaveGuideReq req) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appApi.saveGuide(req));
    }

    @ApiOperation("获取配网引导")
    @RequestMapping(value = "/getGuide", method = RequestMethod.GET)
    public CommonResponse<GetGuideResp> getGuide(@RequestParam("id") Long id) {
        GetGuideResp resp = appApi.getGuide(id);
        List<String> fileIds = Lists.newArrayList();
        String smartImg = resp.getSmartImg();
        String apImg = resp.getApImg();
        if (StringUtil.isNotEmpty(smartImg)) {
            fileIds.add(smartImg);
        }
        if (StringUtil.isNotEmpty(apImg)) {
            fileIds.add(apImg);
        }

        if (CollectionUtils.isNotEmpty(fileIds)) {
            Map<String, String> resMap = fileApi.getGetUrl(fileIds);
            resp.setSmartImg(resMap.get(smartImg));
            resp.setApImg(resMap.get(apImg));
        }

        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", resp);
    }

    /////////////////////////////////////////// 应用证书 ////////////////////////////////////////////////////////////

    @ApiOperation("保存证书文件")
    @RequestMapping(value = "/saveCertificate", method = RequestMethod.POST)
    public CommonResponse<Boolean> saveCertificate(@RequestBody SaveCertificateReq req) {
        SaveAppReq saveAppReq = new SaveAppReq();
        saveAppReq.setId(req.getAppId());
        saveAppReq.setMfile(req.getMfileId());
        saveAppReq.setPfile(req.getPfileId());
        saveAppReq.setFilePassword(req.getFilePassword());
        saveAppReq.setTenantId(getTenantId());
        appApi.saveApp(saveAppReq);
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", true);
    }

    /////////////////////////////////////////// 消息推送 /////////////////////////////////////////////////////////////

    @ApiOperation("消息推送配置")
    @RequestMapping(value = "/setMsgSend", method = RequestMethod.POST)
    public CommonResponse<Boolean> setMsgSend(MultipartHttpServletRequest multipartRequest,
                                              @RequestParam("appId") Long appId,
                                              @RequestParam("password") String password,
                                              @RequestParam("url") String url,
                                              @RequestParam("key") String key,
                                              @RequestParam("backgroundColor") String backgroundColor,
                                              @RequestParam("imageId") String imageId) {
        //配置消息推送
        MultipartFile IosMultipartFile = multipartRequest.getFile("file");
        if (IosMultipartFile != null) {
            messageApi.addAppCertInfo(IosMultipartFile, getTenantId(), appId, password, "release", url, key);
        }

        //保存安卓推送消息文件
        SaveAppReq saveAppReq = new SaveAppReq();
        MultipartFile androidMultipartFile = multipartRequest.getFile("androidFile");
        if (IosMultipartFile != null){
            String iosFileId = this.fileUploadApi.uploadFile(IosMultipartFile, getTenantId());
            saveAppReq.setIosFileId(iosFileId);
        }
        if (androidMultipartFile != null) {
            String fcmFileId = this.fileUploadApi.uploadFile(androidMultipartFile, getTenantId());
            saveAppReq.setFcmFileId(fcmFileId);
        }

        saveAppReq.setId(appId);
        saveAppReq.setTenantId(getTenantId());
        saveAppReq.setGoogleUrl(url);
        saveAppReq.setGoogleKey(key);
        saveAppReq.setIosSendKey(password);
        saveAppReq.setBackgroundColor(backgroundColor);
        saveAppReq.setImageId(imageId);
        appApi.saveApp(saveAppReq);
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", true);
    }

    /////////////////////////////////////////// 邮件配置 /////////////////////////////////////////////////////////////

    @LoginRequired(value = Action.Skip)
    @ApiOperation("设置租户邮箱信息")
    @RequestMapping(value = "/setTenantMailInfo", method = RequestMethod.POST)
    public CommonResponse<Boolean> setTenantMailInfo(@RequestBody TenantMailInfoReq req) {
        //配置消息推送
        if (req != null) {
            TenantMailInfoDto dto = new TenantMailInfoDto();
            BeanUtils.copyProperties(req, dto);
            messageApi.addTenantMailInfo(dto);
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", true);
    }

    @LoginRequired(value = Action.Skip)
    @ApiOperation("查询租户邮箱信息")
    @RequestMapping(value = "/getTenantMailInfo", method = RequestMethod.GET)
    public CommonResponse<TenantMailInfo> getTenantMailInfo(@RequestParam("appId") Long appId) {
        TenantMailInfo tenantMailInfo = messageApi.getTenantMailInfo(appId);
        if(tenantMailInfo != null) {
        	tenantMailInfo.setPassWord(null);
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", tenantMailInfo);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("测试邮件推送配置")
    @RequestMapping(value = "/testMailPushConfig", method = RequestMethod.GET)
    public CommonResponse<Boolean> testMailPushConfig(@RequestParam("host") String host, @RequestParam("port") Integer port, @RequestParam("userName") String userName, 
    		@RequestParam("passWord") String passWord, @RequestParam("message") String message) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", messageApi.testMailPushConfig(host, port, userName, passWord, message));
    }

    @LoginRequired(value = Action.Normal)
	@ApiOperation("测试Google推送配置")
	@RequestMapping(value = "/testGoogleJsonConfig", method = RequestMethod.POST)
	public CommonResponse<Boolean> testGoogleJsonConfig(MultipartHttpServletRequest multipartRequest,
			@RequestParam("appId") Long appId) throws Exception {
		if(appId == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The APP ID is empty.");
		}
		AppInfoResp resp = null;
		try {
			resp = appApi.getAppById(appId);
			if(resp == null){
				throw new BusinessException(BusinessExceptionEnum.APP_INFO_NOT_FOUND);
			}
			if(StringUtil.isEmpty(resp.getPackName())){
				throw new BusinessException(BusinessExceptionEnum.PACKNAME_IS_NULL);
			}
		} catch (Exception e1) {
			throw new BusinessException(BusinessExceptionEnum.APP_INFO_NOT_FOUND);
		}
		MultipartFile multipartFile = multipartRequest.getFile("file");
		if(multipartFile == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "Cannot find the file.");
		}
		boolean flag = false;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuffer buffer = null;
		BufferedReader bf = null;
		try {
			inputStream = multipartFile.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			buffer = new StringBuffer();
			bf = new BufferedReader(bufferedReader);
			String s = null;
			while ((s = bf.readLine()) != null) {
				buffer.append(s.trim());
			}
			JSONObject jsonobj = null;
			if(buffer != null){
				jsonobj = new JSONObject(buffer.toString());
			}
			JSONArray jsonclient = null;
			if(jsonobj != null && jsonobj.get("client") != null){
				jsonclient = new JSONArray(jsonobj.get("client").toString());
			}
			if(jsonclient != null){
			    for(int i=0; i<jsonclient.length(); i++){
                    JSONObject client0 = new JSONObject(jsonclient.get(i).toString());
                    JSONObject client_info = null;
                    if(client0 != null && client0.get("client_info") != null){
                        client_info = new JSONObject(client0.get("client_info").toString());
                    }
                    JSONObject android_client_info = null;
                    if(client_info != null && client_info.get("android_client_info") != null){
                        android_client_info = new JSONObject(client_info.get("android_client_info").toString());
                    }
                    String package_name = null;
                    if(android_client_info != null && android_client_info.get("package_name") != null){
                        package_name = android_client_info.get("package_name").toString();
                    }
                    if(package_name != null){
                        if (resp.getPackName().equals(package_name.toString())) {
                            flag = true;
                            break;
                        }
                    }
			    }
            }
		} catch (Exception e) {
			throw new BusinessException(BusinessExceptionEnum.TEST_GOOGLE_JSON_CONFIG_FAILED);
		}
		if(inputStream != null){
			inputStream.close();
		}
		if(inputStreamReader != null){
			inputStreamReader.close();
		}
		if(bufferedReader != null){
			bufferedReader.close();
		}
		if(bf != null){
			bf.close();
		}
		return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", flag);
	}

	@LoginRequired(value = Action.Normal)
	@ApiOperation("测试IOS推送P12证书配置")
	@RequestMapping(value = "/testP12PushConfig", method = RequestMethod.POST)
	public CommonResponse<Boolean> testP12PushConfig(MultipartHttpServletRequest multipartRequest, @RequestParam("appId") Long appId, @RequestParam("passWord") String passWord) {
		if(appId == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "appId is null");
		}
		AppInfoResp resp = null;
		try {
			resp = appApi.getAppById(appId);
			if(resp == null){
				throw new BusinessException(BusinessExceptionEnum.APP_INFO_NOT_FOUND);
			}
			if(StringUtil.isEmpty(resp.getPackName())){
				throw new BusinessException(BusinessExceptionEnum.PACKNAME_IS_NULL);
			}
		} catch (Exception e1) {
			throw new BusinessException(BusinessExceptionEnum.APP_INFO_NOT_FOUND);
		}
		MultipartFile multipartFile = multipartRequest.getFile("file");
		if(multipartFile == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "file is null");
		}
		boolean flag = false;
		InputStream fis = null;
		try {
			fis = multipartFile.getInputStream();
			KeyStore ks = KeyStore.getInstance("PKCS12");
			char[] nPassword = null;
			if ((passWord == null) || passWord.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = passWord.toCharArray();
			}
			ks.load(fis, nPassword);
			fis.close();
			Enumeration enum1 = ks.aliases();
			String keyAlias = null;
			if (enum1.hasMoreElements()) {
				keyAlias = (String) enum1.nextElement();
			}
			Certificate cert = ks.getCertificate(keyAlias);
			if(cert != null){
				if (cert.toString().contains(resp.getPackName())) {
					flag = true;
				}
			}
		} catch (Exception e) {
			throw new BusinessException(BusinessExceptionEnum.TEST_P12CERT_CONFIG_FAILED);
		}
		return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", flag);
	}

    @LoginRequired(value = Action.Normal)
    @ApiOperation("测试IOS打包P12证书配置")
    @RequestMapping(value = "/testP12CertConfig", method = RequestMethod.POST)
    public CommonResponse<Boolean> testP12CertConfig(MultipartHttpServletRequest multipartRequest,@RequestParam("appId") Long appId, @RequestParam("passWord") String passWord) {
        if(appId == null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "appId is null");
        }
        MultipartFile multipartFile = multipartRequest.getFile("file");
        if(multipartFile == null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "file is null");
        }
        boolean flag = false;
        InputStream fis = null;
        try {
            fis = multipartFile.getInputStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            char[] nPassword = null;
            if ((passWord == null) || passWord.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passWord.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            Enumeration enum1 = ks.aliases();
            String keyAlias = null;
            if (enum1.hasMoreElements()) {
                keyAlias = (String) enum1.nextElement();
            }
            Certificate cert = ks.getCertificate(keyAlias);
            if(cert != null){
                flag = true;
            }
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.TEST_P12CERT_CONFIG_FAILED);
        }
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", flag);
    }

	@LoginRequired(value = Action.Normal)
	@ApiOperation("测试IOS推送Mobileprovision证书配置")
	@RequestMapping(value = "/testMobileprovisionConfig", method = RequestMethod.POST)
	public CommonResponse<Boolean> testMobileprovisionConfig(MultipartHttpServletRequest multipartRequest,
			@RequestParam("appId") Long appId) throws Exception {
		if(appId == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "appId is null");
		}
		AppInfoResp resp = null;
		try {
			resp = appApi.getAppById(appId);
			if(resp == null){
				throw new BusinessException(BusinessExceptionEnum.APP_INFO_NOT_FOUND);
			}
			if(StringUtil.isEmpty(resp.getPackName())){
				throw new BusinessException(BusinessExceptionEnum.PACKNAME_IS_NULL);
			}
		} catch (Exception e1) {
			throw new BusinessException(BusinessExceptionEnum.APP_INFO_NOT_FOUND);
		}
		MultipartFile multipartFile = multipartRequest.getFile("file");
		if(multipartFile == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "file is null");
		}
		boolean flag = false;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuffer buffer = null;
		BufferedReader bf = null;
		try {
			inputStream = multipartFile.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			buffer = new StringBuffer();
			bf = new BufferedReader(bufferedReader);
			String s = null;
			while ((s = bf.readLine()) != null) {
				buffer.append(s.trim());
			}
			if(buffer != null){
				if (buffer.toString().contains(resp.getPackName())) {
					flag = true;
				}
			}
		} catch (Exception e) {
			throw new BusinessException(BusinessExceptionEnum.TEST_MOBILEPROVISION_CONFIG_FAILED);
		}
		if(inputStream != null){
			inputStream.close();
		}
		if(inputStreamReader != null){
			inputStreamReader.close();
		}
		if(bufferedReader != null){
			bufferedReader.close();
		}
		if(bf != null){
			bf.close();
		}
		return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", flag);
	}
    ////////////////////////////////////////// 打包 //////////////////////////////////////////////////////////////////

    @ApiOperation("获取打包结果")
    @RequestMapping(value = "/getPackResult", method = RequestMethod.GET)
    public CommonResponse<String> getPackResult(@RequestParam("id") Long id) {
        AppInfoResp resp = appApi.getAppById(id);
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", resp.getDesc());
    }

    @ApiOperation("执行打包")
    @LoginRequired(Action.Skip)
    @RequestMapping(value = "/execAppPack", method = RequestMethod.GET)
    public CommonResponse<Boolean> execAppPack(@RequestParam("appId") Long appId) {
        //判断是否正在打包
        AppInfoResp resp = appApi.getAppById(appId);
        Integer status = resp.getStatus();
        Boolean packFlag = true; //是否可以打包

        if (status == 1) {
            //正在打包
            long pt = 0;
            if (resp.getPackTime() != null) {
                pt = resp.getPackTime().getTime();
            }
            long now = System.currentTimeMillis();
            //打包超过两个小时，估计失败，可重新打包
            packFlag = now - pt > 1000 * 60 * 120;
        }

        if (packFlag) {
            List<String> fileIds = Lists.newArrayList();
            if (resp.getLogoFileId() != null) {
                fileIds.add(resp.getLogoFileId());
            }
            if (resp.getLoadingImgFileId() != null) {
                fileIds.add(resp.getLoadingImgFileId());
            }
            if (resp.getPfile() != null) {
                fileIds.add(resp.getPfile());
            }
            if (resp.getMfile() != null) {
                fileIds.add(resp.getMfile());
            }
            if (resp.getFcmFileId() != null) {
                fileIds.add(resp.getFcmFileId());
            }

            String keystoreId = resp.getKeystoreId();
            if (StringUtils.isEmpty(keystoreId)) {
                String keypass = UUID.randomUUID().toString().replace("-","");
                String storepass = UUID.randomUUID().toString().replace("-","");
                resp.setKeypass(keypass);
                resp.setStorepass(storepass);
                //重新创建android签名证书
                TenantInfoResp tenantInfoResp = tenantApi.getTenantById(resp.getTenantId());
                keystoreId = createKeystore(resp.getAppCode(), tenantInfoResp, keypass,storepass);
//                return new CommonResponse<>(ResultMsg.FAIL, "Configuration failed. Please check your APP certificate.", null);
                if (keystoreId == null) {
                    return new CommonResponse<>(ResultMsg.FAIL, "Configuration failed. Please check your APP certificate.", null);
                }
            }

            if (fileIds.size() < 5) {
                //文件未上传全
                return new CommonResponse<>(ResultMsg.FAIL, "Configuration failed. Please check your APP certificate.", null);
            }

            //应用设置为正在打包状态
            SaveAppReq saveAppReq = new SaveAppReq();
            saveAppReq.setId(appId);
            saveAppReq.setStatus(1);
            saveAppReq.setDesc("Applications are being packaged");
            saveAppReq.setPackTime(new Date());
            saveAppReq.setKeystoreId(keystoreId);
            saveAppReq.setKeypass(resp.getKeypass());
            saveAppReq.setStorepass(resp.getStorepass());
            appApi.saveApp(saveAppReq);
            resp.setKeystoreId(keystoreId);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String saveFilePath = request.getSession().getServletContext().getRealPath("/") + ToolUtil.getUUID();
            //这些操作做异步处理，加快响应速度
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        SaaSContextHolder.setCurrentTenantId(resp.getTenantId());
                        //异步处理
                        Map<String, String> fileMap = savePack(resp, saveFilePath);
                        //发起打包
                        doPack(fileMap);
                    } catch (Exception e) {
                        logger.error("自动化打包失败", e);
                        //修改打包状态
                        SaveAppReq saveAppReq = new SaveAppReq();
                        saveAppReq.setId(appId);
                        saveAppReq.setStatus(3);
                        saveAppReq.setDesc("app pack error." + e.getMessage());
                        saveAppReq.setPackTime(new Date());
                        appApi.saveApp(saveAppReq);
                    }
                }
            });

            appApi.customConfirmAppPackage(appId, 0);
            return new CommonResponse<>(ResultMsg.SUCCESS, "The packing has started, please wait a moment.", null);
        } else {
            logger.info("Packing… Please wait.");
            return new CommonResponse<>(ResultMsg.FAIL, "Packing… Please wait.", null);
        }
    }

    @LoginRequired(value = Action.Skip)
    @ApiOperation("执行打包完成通知")
    @RequestMapping(value = "/appPackNotify", method = RequestMethod.POST)
    public void appPackNotify(@RequestBody PackNotifyReq req) {
        logger.info("收到打包通知：" + req.toString());
        SaveAppReq saveAppReq = new SaveAppReq();
        saveAppReq.setId(Long.parseLong(req.getAppId()));
        if ("0".equals(req.getRc())) {
            saveAppReq.setStatus(2);
        } else {
            saveAppReq.setStatus(3);
        }
        saveAppReq.setDesc(req.getDesc());
        saveAppReq.setAndroidPackUrl(req.getAndroidUrl());
        saveAppReq.setIosPackUrl(req.getIosUrl());
        appApi.saveApp(saveAppReq);
    }

    /**
     * 打包应用
     *
     * @param resp
     * @return
     * @throws IOException
     */
    private Map<String, String> savePack(AppInfoResp resp, String saveFilePath) {
        try {
            //取出关联产品
            Long appId = resp.getId();
            List<AppProductResp> list = appApi.getAppProduct(appId);
            Set<Integer> protocolTypeSet = Sets.newHashSet();
            Map<String, String> productMap = Maps.newHashMap();
            int index = 1;
            List<Long> productList = Lists.newArrayList();
            for (AppProductResp vo : list) {
                //获取产品信息
                ProductResp productResp = productApi.getProductById(vo.getProductId());
                if (productResp != null) {
                    Integer mode = productResp.getCommunicationMode();
                    protocolTypeSet.add(mode);
                    String type = getProtocol(mode);
                    String model = productResp.getModel() + "." + index;

                    productMap.put(model, type);
                    index++;
                }
                productList.add(vo.getProductId());
            }

            //TODO 测试数据
            /*productMap.put("com.light1.sdf.1", "wifi");
            productMap.put("com.plug.sdf.2", "wifi");
            productMap.put("com.sirenhub.sdf.3", "wifi");
            productMap.put("com.gateway.sdf.4", "wifi");
            productMap.put("com.light.sdf.5", "zigbee");
            productMap.put("com.plug.sdf.6", "zigbee");*/

            //版本管理
            String ver = resp.getAndroidVer();
            ver = getVer(ver);
            resp.setAndroidVer(ver);
            resp.setIosVer(ver);

            //打包压缩文件
            AppPackUtil appPackUtil = new AppPackUtil(resp);
            List<String> fileIds = Lists.newArrayList();
            if (resp.getLogoFileId() != null) {
                fileIds.add(resp.getLogoFileId());
            }
            if (resp.getLoadingImgFileId() != null) {
                fileIds.add(resp.getLoadingImgFileId());
            }
            if (resp.getPfile() != null) {
                fileIds.add(resp.getPfile());
            }
            if (resp.getMfile() != null) {
                fileIds.add(resp.getMfile());
            }
            if (resp.getFcmFileId() != null) {
                fileIds.add(resp.getFcmFileId());
            }
            if (!StringUtils.isEmpty(resp.getKeystoreId())) {
                fileIds.add(resp.getKeystoreId());
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(resp.getImageId())) {
                fileIds.add(resp.getImageId());
            }
            Map<String, String> fileMap = this.fileApi.getGetUrl(fileIds);
            Map<String, String> resMap = Maps.newHashMap();
            resMap.put("logoUrl", fileMap.get(resp.getLogoFileId()));
            resMap.put("loadingUrl", fileMap.get(resp.getLoadingImgFileId()));
            resMap.put("pfileUrl", fileMap.get(resp.getPfile()));
            resMap.put("mfileUrl", fileMap.get(resp.getMfile()));
            resMap.put("fcmfileUrl", fileMap.get(resp.getFcmFileId()));
            resMap.put("keystoreUrl", fileMap.get(resp.getKeystoreId()));
            resMap.put("pushConfigurationUrl", fileMap.get(resp.getImageId()));

            //Map<String, String> langMap = getLangMap(getLangResp(appId));
            //获取多语言文件
            String langFilepath = langInfoService.createAppLangFile(resp.getId(), productList, resp.getTenantId(), saveFilePath);
            TenantInfoResp tenantInfoResp = tenantApi.getTenantById(resp.getTenantId());
            String alias = "\"" + tenantInfoResp.getName() + "_" + resp.getAppCode() + "\"";
            alias = alias.toLowerCase();
//            alias = alias.replace(" ","_");
            //生成配置文件
            File zipfile = appPackUtil.packFile(resp, protocolTypeSet, productMap, langFilepath, alias, tenantInfoResp.getEmail());
            //上传文件服务器
            MultipartFile multipartFile = FileUtil.toMultipartFile(zipfile);
            FileInfoResp fileInfoResp = this.fileUploadApi.upLoadFileAndGetUrl(multipartFile, SaaSContextHolder.currentTenantId());
            String fileId = fileInfoResp.getFileId();
            //删除临时文件
            appPackUtil.deleteDirectory();

            logger.debug("压缩文件ID:" + fileId);

            if (!StringUtils.isEmpty(resp.getFileId())) {
                fileApi.deleteObject(resp.getFileId());
            }
            //更新内容
            SaveAppReq saveAppReq = new SaveAppReq();
            saveAppReq.setId(resp.getId());
            saveAppReq.setAndroidVer(resp.getAndroidVer());
            saveAppReq.setIosVer(resp.getIosVer());
            saveAppReq.setFileId(fileId);
            /**更新时间和状态**/
            saveAppReq.setConfirmStatus(0);
            saveAppReq.setConfirmTime(new Date());
            appApi.saveApp(saveAppReq);
            resMap.put("zipUrl", fileInfoResp.getUrl());
            return resMap;
        } catch (Exception e) {
            logger.error("自动化打包，保存压缩文件失败", e);
            throw new BusinessException(BusinessExceptionEnum.SAVE_PACK_FILE_ERROE, e);
        }
    }

    public void doPack(Map<String, String> fileMap) {
        try {
            Environment environment = ApplicationContextHelper.getBean(Environment.class);
            //获取环境标识
            String packUrl = environment.getProperty("apppack.packUrl");
            packUrl = packUrl + "/pack";
            Map<String, String> params = new HashMap<String, String>();
            params.put("zipUrl", fileMap.get("zipUrl"));
            params.put("logoUrl", fileMap.get("logoUrl"));
            params.put("loadingUrl", fileMap.get("loadingUrl"));
            params.put("pfileUrl", fileMap.get("pfileUrl"));
            params.put("mfileUrl", fileMap.get("mfileUrl"));
            params.put("fcmfileUrl", fileMap.get("fcmfileUrl"));
            params.put("keystoreUrl", fileMap.get("keystoreUrl"));
            params.put("pushConfigurationUrl", fileMap.get("pushConfigurationUrl"));
            // 中文需转uft-8编码，不然会乱码
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("Content-Type", "application/json;charset=UTF-8");
            //执行打包
            logger.info("发送打包请求...");
            HttpClientUtils.getInstance().httpPost(packUrl, params, headMap);
        } catch (Exception e) {
            logger.error("自动化打包，发送打包请求失败", e);
            throw new BusinessException(BusinessExceptionEnum.SEND_PACK_REQ_ERROE, e);
        }
    }


    /**
     * 获取版本号
     *
     * @param ver
     * @return
     */
    public String getVer(String ver) {
        String resVer;
        if (StringUtil.isEmpty(ver)) {
            resVer = "1.0.0.1";
        } else {
            String verNum = ver.substring(6, ver.length());
            Integer num = Integer.valueOf(verNum);
            num++;
            resVer = "1.0.0." + num;
        }
        return resVer;
    }

    private String getProtocol(Integer type) {
        String res = null;
        if (type == 0) {
            res = "wifi";
        } else if (type == 1) {
            res = "ble";
        } else if (type == 2) {
            res = "zigbee";
        } else if (type == 3) {
            res = "ipc";
        }
        return res;
    }

}
