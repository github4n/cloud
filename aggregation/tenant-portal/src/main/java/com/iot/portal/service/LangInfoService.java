package com.iot.portal.service;

import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.common.util.ToolUtil;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.NetworkTypeApi;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.file.api.FileApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.portal.web.vo.network.NetworkStepConfigFileVo;
import com.iot.portal.web.vo.network.NetworkStepDetailVo;
import com.iot.shcs.module.api.ModuleCoreApi;
import com.iot.shcs.module.vo.resp.GetProductModuleResp;
import com.iot.system.api.LangApi;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.DeviceNetworkStepTenantApi;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.vo.resp.network.NetworkFileFormat;
import com.iot.tenant.vo.resp.network.NetworkHelpFormat;
import com.iot.tenant.vo.resp.network.NetworkStepFormat;
import com.iot.tenant.vo.resp.network.NetworkTabsFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：文案service
 * 创建人： yeshiyuan
 * 创建时间：2018/10/16 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/16 15:12
 * 修改描述：
 */
@Service
public class LangInfoService {

    private final static Logger logger = LoggerFactory.getLogger(LangInfoService.class);

    /**
     * 文案保存路径
     */
    private final static String langFileSavePath = "src"+File.separator + "projects" + File.separator +"Arnoo" + File.separator + "resources" + File.separator + "locales";

    /**
     * 配网引导图保存路径
     */
    private final static String imgSavePath = "src" + File.separator + "projects" + File.separator + "Arnoo" + File.separator + "guidImages";

    /**
     * 配网步骤文案
     */
    private final static String dataPrePath = "src" + File.separator + "projects" + File.separator + "Arnoo" + File.separator + "data";


    @Autowired
    private LangInfoTenantApi langInfoTenantApi;

    @Autowired
    private DeviceNetworkStepTenantApi deviceNetworkStepTenantApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private LangApi langApi;

    @Autowired
    private NetworkTypeApi networkTypeApi;

    @Autowired
    private DeviceTypeCoreApi deviceTypeCoreApi;

    @Autowired
    private ModuleCoreApi moduleCoreApi;

    @Autowired
    private AppApi appApi;

    /**
      * @despriction：生成app文案相关文件（app文案、配网步骤引导图、配网步骤配置文件）
      * @author  yeshiyuan
      * @created 2018/10/19 13:05
      * @return 返回保存路径
      */
    @Deprecated
    public String createAppLangFile(Long appId, List<Long> productIdList, Long tenantId){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String saveFilePath = request.getSession().getServletContext().getRealPath("/") + ToolUtil.getUUID();
        this.createAppLangFile(appId, productIdList, tenantId, saveFilePath);
        return saveFilePath;
    }

    /**
     * @despriction：生成app文案相关文件（app文案、配网步骤引导图、配网步骤配置文件）
     * @author  yeshiyuan
     * @created 2018/10/19 13:05
     * @return 返回保存路径
     */
    public String createAppLangFile(Long appId, List<Long> productIdList, Long tenantId, String saveFilePath){
        logger.info("saveFilePath:{}", saveFilePath);
        Map<String,Map<String, String>> langMap = new HashMap<>();
        //1、加载app文案；
        Map<String, Map<String, String>> appMap = langInfoTenantApi.getAppLangInfo(appId, tenantId);
        langMap.putAll(appMap);
        //2、加载app下的关联产品文案
        if (productIdList !=null && !productIdList.isEmpty()) {
            productIdList = productIdList.stream().distinct().collect(Collectors.toList());
            Map<String, Map<String, String>> productMap = langInfoTenantApi.getLangByProductIds(tenantId, productIdList);
            productMap.forEach((key,value) ->{
                if (langMap.containsKey(key)) {
                    Map<String, String> map = langMap.get(key);
                    map.putAll(value);
                }else {
                    langMap.put(key, value);
                }
            });
        }
        createLangFile(saveFilePath, langMap);
        Set<String> fileIdList = new HashSet<>();
        //3、加载app下的产品配网文案
        Map<String, List<NetworkFileFormat>> allNetworkFileFormat = new HashMap<>(); //key为语言类型
        productIdList.forEach(productId -> {
            Map<String, NetworkFileFormat> networkFileFormatMap = deviceNetworkStepTenantApi.getNetworkFileFormat(tenantId, appId, productId);
            for (Map.Entry<String, NetworkFileFormat> entry : networkFileFormatMap.entrySet() ) {
                List<NetworkFileFormat> formatList = allNetworkFileFormat.get(entry.getKey());
                if (formatList == null) {
                    formatList = new ArrayList<>();
                    allNetworkFileFormat.put(entry.getKey(), formatList);
                }
                NetworkFileFormat fileFormat = entry.getValue();
                formatList.add(fileFormat);
                if (fileFormat.getFileIds()!=null && !fileFormat.getFileIds().isEmpty()) {
                    fileIdList.addAll(fileFormat.getFileIds());
                }
            }
        });

        List<String> fileUrls = new ArrayList<>();
        Map<String, String> fileUrlMap = new HashMap<>();
        if (!fileIdList.isEmpty()) {
            fileUrlMap = fileApi.getGetUrl(fileIdList.stream().collect(Collectors.toList()));
            fileUrls.addAll(fileUrlMap.values());
        }
        //由于portal不支持访问外网，只能生成一个文件用来存储下载url
       // saveImg(saveFilePath, fileUrlMap);

        //5、生成配网步骤文件
        if (!allNetworkFileFormat.values().isEmpty()) {
            createNetworkStepFile(saveFilePath, fileUrlMap, allNetworkFileFormat);
        }
        //6、生成产品profile文件
        List<GetProductModuleResp> productModuleResps = moduleCoreApi.findServiceModuleList(tenantId,appId);
        if (productModuleResps != null && !productModuleResps.isEmpty()) {
            createProductInfoFile(saveFilePath, productModuleResps);
        }
        //7、生成app下产品列表文件
        Map appProductMap = appApi.appExecPackageByProduct(appId, tenantId);
        if (appProductMap != null) {
            createappProductFile(saveFilePath, appProductMap, appId);
            List<String> iconList = (List<String>) appProductMap.get("iconListResult");
            if (iconList!=null && !iconList.isEmpty()) {
                fileUrls.addAll(iconList);
            }
        }
        //8、保存图片下载url至txt文件
        if (!fileUrls.isEmpty()){
            saveUrl(saveFilePath, fileUrls);
        }
        return saveFilePath;
    }

    /**
      * @despriction：生成文案文件
      * @author  yeshiyuan
      * @created 2018/10/17 16:50
      * @return
      */
    private void createLangFile(String saveFilePath, Map<String, Map<String, String>> langMap) {
        //创建临时目录
        String langFileDirectoryPath = saveFilePath + File.separator + langFileSavePath;
        File directory = new File(langFileDirectoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        langMap.forEach((key, map) -> {
            FileWriter writer = null;
            BufferedWriter bw = null;
            try {
                key = key.toLowerCase();
                File file = new File(langFileDirectoryPath + File.separator + key + ".js");
                if (!file.exists()) {
                    file.createNewFile();
                }
                writer = new FileWriter(file);
                bw = new BufferedWriter(writer);
                bw.write("export default {");
                int size = map.entrySet().size();
                int index = 1;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    bw.write(addTab(1) + addQuotes(entry.getKey())+ ":" + addQuotes(entry.getValue()));
                    if (index < size) {
                        bw.write(",");
                    }
                    index++;
                }
                bw.write(addTab(0) + "};");
                bw.flush();
            } catch (IOException e) {
                logger.error("create lang file error", e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
      * @despriction：保存图片
      * @author  yeshiyuan
      * @created 2018/10/18 9:59
      * @return
      */
    private void saveImg(String saveFilePath, Map<String, String> fileUrlMap) {
        //创建临时目录
        String imgDirectoryPath = saveFilePath + File.separator + imgSavePath;
        File directory = new File(imgDirectoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //下载图片
        fileUrlMap.forEach((fileId,fileUrl) -> {
            URL url = null;
            DataInputStream dataInputStream = null;
            FileOutputStream fo = null;
            ByteArrayOutputStream bo = null;
            try {
                url = new URL(fileUrl);
                dataInputStream = new DataInputStream(url.openStream());
                String imgName = fileUrl.substring(fileUrl.indexOf(fileId), fileUrl.indexOf("?"));
                fo = new FileOutputStream(new File(imgDirectoryPath + File.separator + imgName));
                bo = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length=dataInputStream.read(buffer))>0) {
                    bo.write(buffer,0, length);
                }
                fo.write(bo.toByteArray());
            } catch (IOException e) {
                logger.error("download network step img error", e);
            } finally {
                try {
                    if (dataInputStream != null) {
                        dataInputStream.close();
                    }
                    if (fo != null) {
                        fo.close();
                    }
                    if (bo != null) {
                        bo.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * @despriction：保存下载url
     * @author  yeshiyuan
     * @created 2018/10/18 9:59
     * @return
     */
    private void saveUrl(String saveFilePath, List<String> fileUrls) {
        //创建临时目录
        File directory = new File(saveFilePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            File file = new File(saveFilePath + File.separator + "downloadFileUrl.txt");
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            for (String url : fileUrls) {
                bw.write(url);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
      * @despriction：生成产品信息的配置文件
      * @author  yeshiyuan
      * @created 2018/11/12 16:17
      * @return
      */
    private void createProductInfoFile(String saveFilePath, List<GetProductModuleResp> list) {
        //创建临时目录
        String directoryPath = saveFilePath + File.separator + dataPrePath;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        list.forEach(getProductModuleResp -> {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(new File(directoryPath + File.separator + "profile_" + getProductModuleResp.getId() + ".json"));
                bw = new BufferedWriter(fw);
                bw.write(JsonUtil.toJson(getProductModuleResp));
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
      * @despriction：生成app下的产品列表文件
      * @author  yeshiyuan
      * @created 2018/11/14 10:57
      * @return
      */
    private void createappProductFile(String saveFilePath, Map appProductMap, Long appId) {
        //创建临时目录
        String directoryPath = saveFilePath + File.separator + dataPrePath;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(new File(directory + File.separator + "add_entry_" + appId + ".json"));
            bw = new BufferedWriter(fw);
            bw.write(JsonUtil.toJson(appProductMap));
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw!=null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw!=null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
      * @despriction：加双引号
      * @author  yeshiyuan
      * @created 2018/10/18 17:56
      * @return
      */
    private String addQuotes(String text) {
        text = text==null? "" : text.replace("\"", "\\\"");
        return "\"" + text + "\"";
    }

    /**
      * @despriction：换行并加Tab
      * @author  yeshiyuan
      * @created 2018/10/18 20:05
      * @return
      */
    private String addTab(Integer num) {
        String tab = "\n";
        for (int i = 0; i< num; i++) {
            tab += "\t";
        }
        return tab;
    }



    /**
     * @despriction：生成配网步骤文件
     * @author  yeshiyuan
     * @created 2018/10/18 11:45
     * @return
     */
    private void createNetworkStepFile(String saveFilePath, Map<String, String> fileUrlMap, Map<String, List<NetworkFileFormat>> allNetworkFileFormat) {
        //创建临时目录
        String directoryPath = saveFilePath + File.separator + dataPrePath;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //先技术方案名称
        List<GoodsInfo> goodsInfos = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
        Set<String> goodsName = new HashSet<>();
        goodsInfos.forEach(o ->{
            goodsName.add(o.getGoodsName());
        });
        Map<String, String> nameMap =  this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        Map<Long, String> technicalNameMap = new HashMap<>();
        goodsInfos.forEach(o ->{
            technicalNameMap.put(o.getId(), nameMap.get(o.getGoodsName()));
        });
        //key：语言类型, value: 配网步骤信息
        Map<String, Map<String, NetworkStepConfigFileVo>> netwrokConfigMap = new HashMap<>();
        //Map<String, List<NetworkStepConfigFileVo>> netwrokConfigMap = new HashMap<>();

        //存储配网名称
        Map<Long, NetworkTypeResp> networkTypeMap = new HashMap<>();
        //key: productId  value: technicalName
        Map<Long, String> productTechnicalNameMap = new HashMap<>();
        allNetworkFileFormat.forEach((key, list) -> {
            Map<String, NetworkStepConfigFileVo> configVos = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                NetworkStepConfigFileVo configFileVo=  new NetworkStepConfigFileVo();
                NetworkFileFormat fileFormat = list.get(i);
                Long productId = Long.valueOf(fileFormat.getName());
                String technicalName = productTechnicalNameMap.get(productId);
                if (StringUtil.isBlank(technicalName)) {
                    ProductResp productResp = productApi.getProductById(Long.valueOf(fileFormat.getName()));
                    if (productResp == null) {
                        continue;
                    }
                    technicalName = technicalNameMap.get(Long.valueOf(productResp.getCommunicationMode()));
                    productTechnicalNameMap.put(productId, technicalName);
                }
                configFileVo.setProtocol(technicalName);
                List<NetworkStepDetailVo> tabs = new ArrayList<>();
                List<NetworkTabsFormat> tabsFormats = fileFormat.getTabs();
                if (tabsFormats!=null && !tabsFormats.isEmpty()) {
                    for(NetworkTabsFormat tabsFormat : tabsFormats) {
                        NetworkStepDetailVo tab = new NetworkStepDetailVo();
                        //配网名称、编码
                        NetworkTypeResp networkTypeResp = networkTypeMap.get(tabsFormat.getNetworkTypeId());
                        if (networkTypeResp == null) {
                            networkTypeResp = networkTypeApi.getNetworkType(tabsFormat.getNetworkTypeId());
                            if (networkTypeResp != null){
                                networkTypeMap.put(tabsFormat.getNetworkTypeId(), networkTypeResp);
                            }else {
                                networkTypeResp = new NetworkTypeResp();
                            }
                        }
                        tab.setTitle(networkTypeResp.getNetworkName());
                        tab.setMode(networkTypeResp.getTypeCode());
                        //步骤
                        List<NetworkStepFormat> steps = tabsFormat.getSteps();
                        if (steps!=null && !steps.isEmpty()) {
                            for (NetworkStepFormat stepFormat : steps) {
                                String fileUrl = fileUrlMap.get(stepFormat.getIcon());
                                String icon = fileUrl.substring(fileUrl.indexOf(stepFormat.getIcon()), fileUrl.indexOf("?"));
                                stepFormat.setIcon(icon);
                            }
                        }
                        //帮助
                        List<NetworkHelpFormat> helps = tabsFormat.getHelps();
                        if (helps!=null && !helps.isEmpty()) {
                            for (NetworkHelpFormat helpFormat : helps) {
                                String fileUrl = fileUrlMap.get(helpFormat.getIcon());
                                String icon = fileUrl.substring(fileUrl.indexOf(helpFormat.getIcon()), fileUrl.indexOf("?"));
                                helpFormat.setIcon(icon);
                            }
                        }
                        tab.setHelps(tabsFormat.getHelps());
                        tab.setSteps(tabsFormat.getSteps());
                        tabs.add(tab);
                    }
                }
                configFileVo.setTabs(tabs);
                configVos.put(productId.toString(), configFileVo);
            }
            netwrokConfigMap.put(key, configVos);
        });
        netwrokConfigMap.forEach((langKey, networkConfig) -> {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                langKey = "deviceGuidConfig_" + langKey.toLowerCase();
                fw = new FileWriter(new File(directoryPath + File.separator + langKey + ".json"));
                bw = new BufferedWriter(fw);
                bw.write(JsonUtil.toJson(networkConfig));
                bw.flush();
            } catch (IOException e) {
                logger.error("createNetworkStepFile error", e);
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
