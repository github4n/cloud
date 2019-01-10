package com.iot.tenant.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.*;
import com.iot.device.enums.ServicePayStatusEnum;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import com.iot.file.api.FileApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.goods.GoodsCoodEnum;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.domain.AppInfo;
import com.iot.tenant.domain.AppProduct;
import com.iot.tenant.domain.AppProductInfo;
import com.iot.tenant.domain.AppReviewRecord;
import com.iot.tenant.domain.LangInfo;
import com.iot.tenant.enums.AppCanUsedEnum;
import com.iot.tenant.enums.AuditStatusEnum;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.IAppInfoService;
import com.iot.tenant.service.IAppProductInfoService;
import com.iot.tenant.service.IAppProductService;
import com.iot.tenant.service.IAppReviewRecordService;
import com.iot.tenant.service.IDeviceNetworkStepTenantService;
import com.iot.tenant.service.ILangInfoService;
import com.iot.tenant.service.ILangInfoTenantService;
import com.iot.tenant.service.ITenantService;
import com.iot.tenant.util.RedisKeyUtil;
import com.iot.tenant.vo.LangItem;
import com.iot.tenant.vo.LangKey;
import com.iot.tenant.vo.req.GetAppReq;
import com.iot.tenant.vo.req.GetLangReq;
import com.iot.tenant.vo.req.SaveAppProductReq;
import com.iot.tenant.vo.req.SaveAppReq;
import com.iot.tenant.vo.req.SaveGuideReq;
import com.iot.tenant.vo.req.SaveLangReq;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.network.CopyNetworkStepReq;
import com.iot.tenant.vo.req.review.AppReviewSearchReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.AppProductResp;
import com.iot.tenant.vo.resp.GetGuideResp;
import com.iot.tenant.vo.resp.GetLangResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import com.iot.tenant.vo.resp.review.AppReviewResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：App应用业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/5 17:27
 */
@RestController
public class AppApiController implements AppApi {
    private final Logger logger = LoggerFactory.getLogger(AppApiController.class);

    @Autowired
    private IAppInfoService appInfoService;

    @Autowired
    private ILangInfoService langInfoService;

    @Autowired
    private IAppProductService appProductService;

    @Autowired
    private IAppProductInfoService appProductInfoService;

    @Autowired
    private ILangInfoTenantService langInfoTenantService;

    @Autowired
    private IDeviceNetworkStepTenantService deviceNetworkStepTenantService;

    @Autowired
    private ITenantService tenantService;

    @Autowired
    private IAppReviewRecordService appReviewRecordService;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private GatewaySubDevRelationApi gatewaySubDevRelationApi;


    @Autowired
    private NetworkTypeApi networkTypeApi;

    @Autowired
    private ProductConfigNetmodeApi productConfigNetmodeApi;

    @Autowired
    private TechnicalRelateApi technicalRelateApi;

    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;




    @Override
    public Long saveApp(@RequestBody SaveAppReq req) {
        logger.info("save app req:" + req.toString());
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(req, appInfo);
        appInfo.setLogo(req.getLogoFileId());
        appInfo.setLoadingImg(req.getLoadingImgFileId());
        Long id = req.getId();
        if (id != null && id != 0) {
            appInfoService.updateById(appInfo);
        } else {
            appInfo.setCreateTime(new Date());
            //TODO(laiguiming) 判断应用名重复（哪些是唯一的？）
            /*EntityWrapper<AppInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("tenant_id", getTenantId());
            wrapper.eq("app_name", req.getAppName());
            List<AppInfo> list = appInfoService.selectList(wrapper);*/
            appInfoService.insert(appInfo);
        }
        return appInfo.getId();
    }

    @Override
    public List appInfoValidation(@RequestBody SaveAppReq req) {
        EntityWrapper wrapper = new EntityWrapper();
        if (req.getId() != null && req.getId() != 0){
            wrapper.notIn("id",req.getId());
        }
        if (!StringUtils.isEmpty(req.getPackName())) {
            wrapper.eq("pack_name", req.getPackName());
        }
        return appInfoService.selectList(wrapper);
    }

    @Override
    public Boolean copyApp(Long appId) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        //新appId
        Long id = copyAppInfo(appId);
        //复制lang_info
        CopyLangInfoReq copyLangInfoReq = new CopyLangInfoReq(tenantId, id, appId, LangInfoObjectTypeEnum.appConfig.toString(), SaaSContextHolder.getCurrentUserId());
        langInfoTenantService.copyLangInfoTenant(copyLangInfoReq);
        //复制app_product
        EntityWrapper<AppProduct> pwrapper = new EntityWrapper<>();
        pwrapper.eq("app_id", appId);
        pwrapper.eq("tenant_id", tenantId);
        List<AppProduct> appProducts = appProductService.selectList(pwrapper);
        if (CollectionUtils.isNotEmpty(appProducts)) {
            for (AppProduct vo : appProducts) {
                //复制app_product_info
                EntityWrapper<AppProductInfo> piwrapper = new EntityWrapper<>();
                piwrapper.eq("app_product_id", vo.getId());
                piwrapper.eq("tenant_id", tenantId);
                List<AppProductInfo> appProductInfos = appProductInfoService.selectList(piwrapper);
                //保存appproduct
                vo.setAppId(id);
                vo.setId(null);
                appProductService.insert(vo);
                //保存appproductinfo
                if (CollectionUtils.isNotEmpty(appProductInfos)) {
                    List<AppProductInfo> appProductInfoList = Lists.newArrayList();
                    for (AppProductInfo vi : appProductInfos) {
                        vi.setAppProductId(vo.getId());
                        appProductInfoList.add(vi);
                    }
                    appProductInfoService.insertBatch(appProductInfoList);
                }
                CopyNetworkStepReq copyNetworkStepReq = new CopyNetworkStepReq(tenantId, SaaSContextHolder.getCurrentUserId(), id, vo.getProductId(), null, null);
                deviceNetworkStepTenantService.copyNetworkStepTenant(copyNetworkStepReq, appId);
            }
        }
        return true;
    }

    private Long copyAppInfo(Long appId) {
        //查看是否已经有复制应用了
        EntityWrapper<AppInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("copy_id", appId);
        List<AppInfo> list = appInfoService.selectList(wrapper);
        int index = 0;
        if (list != null && list.size() > 0) {
            index = list.size();
        }
        index++;

        //复制app_info
        AppInfo appInfo = appInfoService.selectById(appId);
        AppInfo newApp = new AppInfo();
        BeanUtils.copyProperties(appInfo, newApp);
        newApp.setId(null);
        //应用名称
        String appName = appInfo.getAppName();
        if (index < 10) {
            appName = "复制" + appName + "+0" + index;
        } else {
            appName = "复制" + appName + "+" + index;
        }
        newApp.setAppName(appName);

        //copyId
        newApp.setCopyId(appId);
        appInfoService.insert(newApp);
        return newApp.getId();
    }

    /*private void copyLangInfo(Long appId, Long appIdnew) {
        EntityWrapper<LangInfo> langWrapper = new EntityWrapper<>();
        langWrapper.eq("app_id", appId);
        langWrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        List<LangInfo> langs = langInfoService.selectList(langWrapper);
        if (CollectionUtils.isNotEmpty(langs)) {
            List<LangInfo> langList = Lists.newArrayList();
            for (LangInfo vo : langs) {
                vo.setAppId(appIdnew);
                langList.add(vo);
            }
            langInfoService.insertBatch(langList);
        }
    }*/

    @Override
    public AppInfoResp getAppById(@RequestParam("id") Long id) {
        logger.info("get app req:id=" + id);
        AppInfo appInfo = appInfoService.selectById(id);
        AppInfoResp appInfoResp = new AppInfoResp();
        BeanUtils.copyProperties(appInfo, appInfoResp);
        appInfoResp.setLogoFileId(appInfo.getLogo());
        appInfoResp.setLoadingImgFileId(appInfo.getLoadingImg());
        //若安装路径没有填，则将蒲公英安装路径补充
        if (StringUtils.isEmpty(appInfo.getAndroidInstallUrl())) {
            appInfoResp.setAndroidInstallUrl(appInfo.getAndroidPackUrl());
        }
        if (StringUtils.isEmpty(appInfo.getIosInstallUrl())) {
            appInfoResp.setIosInstallUrl(appInfo.getIosPackUrl());
        }
        return appInfoResp;
    }

    /**
     * 描述：通过APP名称获取app信息
     *
     * @param appName
     * @return
     * @author 李帅
     * @created 2018年12月12日 上午11:45:39
     * @since
     */
    @Override
    public List<AppInfoResp> getAppByAppName(@RequestParam("appName") String appName) {
        logger.info("get app req:appName=" + appName);
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.like("app_name", appName);//.or().like("en_name",appName);
        List<AppInfo> appInfos = appInfoService.selectList(wrapper);
        List<AppInfoResp> appInfoResps = new ArrayList<AppInfoResp>();
        AppInfoResp appInfoResp = null;
        for (AppInfo appInfo : appInfos) {
            appInfoResp = new AppInfoResp();
            BeanUtils.copyProperties(appInfo, appInfoResp);
//            appInfoResp.setLogoFileId(appInfo.getLogo());
//            appInfoResp.setLoadingImgFileId(appInfo.getLoadingImg());
//            //若安装路径没有填，则将蒲公英安装路径补充
//            if (StringUtils.isEmpty(appInfo.getAndroidInstallUrl())) {
//                appInfoResp.setAndroidInstallUrl(appInfo.getAndroidPackUrl());
//            }
//            if (StringUtils.isEmpty(appInfo.getIosInstallUrl())) {
//                appInfoResp.setIosInstallUrl(appInfo.getIosPackUrl());
//            }
            appInfoResps.add(appInfoResp);
        }
        return appInfoResps;
    }

    @Override
    public Long customConfirmAppPackage(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        AppInfo appInfo = appInfoService.selectById(id);
        appInfo.setConfirmTime(new Date());
        appInfo.setConfirmStatus(status);
        appInfo.setAuditStatus(AuditStatusEnum.Passed.getAuditStatus());
        appInfoService.updateById(appInfo);
        return appInfo.getId();
    }

    @Override
    public Long updateDisplayIdentification(@RequestParam("id") Long id, @RequestParam("displayIdentification") Integer displayIdentification) {
        AppInfo appInfo = appInfoService.selectById(id);
        appInfo.setDisplayIdentification(displayIdentification);
        appInfoService.updateById(appInfo);
        return appInfo.getId();
    }

    @Override
    public Page<AppInfoResp> getAppPage(@RequestBody GetAppReq req) {
        logger.info("get app page req:" + req.toString());
        Integer pageNum = req.getPageNum();
        Integer pageSize = req.getPageSize();
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 1000;
        }
        com.baomidou.mybatisplus.plugins.Page datePage = new com.baomidou.mybatisplus.plugins.Page(pageNum, pageSize);
        Page<AppInfoResp> page = new Page<AppInfoResp>();
        EntityWrapper<AppInfo> wrapper = new EntityWrapper<>();
        Long tenantId = SaaSContextHolder.currentTenantId();
        if (tenantId == null) {
            page.setResult(null);
            page.setTotal(0);
            return page;
        }
        wrapper.eq("tenant_id", tenantId);
        wrapper.orderBy("create_time", false);
        datePage = appInfoService.selectPage(datePage, wrapper);
        List<AppInfo> list = datePage.getRecords();
        List<AppInfoResp> resList = Lists.newArrayList();
        for (AppInfo entity : list) {
            AppInfoResp appInfoResp = new AppInfoResp();
            BeanUtils.copyProperties(entity, appInfoResp);
            appInfoResp.setLogoFileId(entity.getLogo());
            appInfoResp.setLoadingImgFileId(entity.getLoadingImg());
            resList.add(appInfoResp);
        }
        page.setResult(resList);
        page.setTotal(datePage.getTotal());
        return page;
    }

    @Override
    public Boolean delApp(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            return false;
        }
        //可加上事务，错误时回滚
        for (Long id : ids) {
            //横向越权处理
            AppInfo appInfo = appInfoService.selectById(id);
            if (appInfo != null) {
                long tenantId = appInfo.getTenantId();
                long tid = SaaSContextHolder.currentTenantId();
                if (tenantId != tid) {
                    continue;
                }
            }

            //删除应用
            appInfoService.deleteById(id);
            List result = new ArrayList();
            List<AppProductResp> products = getAppProduct(id);
            if (products.size() > 0) {
                List<Long> productIdList = Lists.newArrayList();
                for (AppProductResp vo : products) {
                    productIdList.add(vo.getId());
                    //删除对应的配网文案
                    /*deviceNetworkStepTenantService.deleteNetworkStep(getTenantId(), id, vo.getProductId());*/
                }
                //删除产品信息
                appProductService.deleteBatchIds(productIdList);

                //删除app关联产品配网信息
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.in("app_product_id", productIdList);
                List<AppProductInfo> list = appProductInfoService.selectList(wrapper);
                if (CollectionUtils.isNotEmpty(list)) {
                    list.forEach(m -> {
                        result.add(m.getId());
                    });
                    appProductInfoService.deleteBatchIds(result);
                    result.clear();
                }
            }
            //删除对应多语言
           /* EntityWrapper<LangInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("tenant_id", getTenantId());
            wrapper.eq("app_id", id);
            langInfoService.delete(wrapper);*/
            langInfoTenantService.deleteByObjectTypeAndObjectIdAndTenantId(LangInfoObjectTypeEnum.appConfig.toString(), id.toString(), getTenantId());
            //删除app相关配网文案
            deviceNetworkStepTenantService.deleteAppNetwork(id, getTenantId());

            //删除APP审核记录
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.in("app_id", ids);
            List<AppReviewRecord> list = appReviewRecordService.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(m -> {
                    result.add(m.getId());
                });
                appReviewRecordService.deleteBatchIds(result);
                result.clear();
            }


        }
        return true;
    }


    @Override
    public void updateAppStatusByTime() {
        appInfoService.updateAppStatusByTime();
    }

    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    @Override
    public Boolean saveLang(@RequestBody SaveLangReq req) {
        logger.info("save lang req:" + req.toString());

        Long appId = req.getAppId();
        Long tenantId = getTenantId();
        //更新已选语言
        AppInfo appInfo = new AppInfo();
        appInfo.setId(appId);
        appInfo.setChooseLang(req.getChooseLang());
        appInfo.setTenantId(getTenantId());
        appInfoService.updateById(appInfo);

        //先删除 再保存
        EntityWrapper<LangInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", getTenantId());
        wrapper.eq("app_id", appId);
        langInfoService.delete(wrapper);

        //拼接
        List<LangKey> langKeys = req.getLangKeys();
        List<LangInfo> list = Lists.newArrayList();
        for (LangKey vo : langKeys) {
            List<LangItem> items = vo.getItems();
            String key = vo.getKey();
            for (LangItem item : items) {
                LangInfo langInfo = new LangInfo();
                langInfo.setAppId(appId);
                langInfo.setKey(key);
                langInfo.setLang(item.getLang());
                langInfo.setTenantId(tenantId);
                langInfo.setContent(item.getContent());
                list.add(langInfo);
            }
        }
        //批量保存
        return langInfoService.insertBatch(list);
    }

    @Override
    public GetLangResp getLang(@RequestBody GetLangReq req) {
        logger.info("get lang list:" + req.toString());
        GetLangResp resp = new GetLangResp();
        Long appId = req.getAppId();
        //获取已选语言
        AppInfo appInfo = appInfoService.selectById(appId);
        String lang = appInfo.getChooseLang();
        resp.setChooseLang(lang);
        //获取语言列表
        List<LangKey> resList = Lists.newArrayList();
        List<String> keys = req.getKeys();
        if (appId != null) {
            EntityWrapper<LangInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("tenant_id", getTenantId());
            wrapper.eq("app_id", appId);
            List<LangInfo> list = langInfoService.selectList(wrapper);
            Map<String, List<LangItem>> resMap = Maps.newConcurrentMap();
            for (LangInfo vo : list) {
                String key = vo.getKey();
                LangItem item = new LangItem();
                item.setLang(vo.getLang());
                item.setContent(vo.getContent());
                List<LangItem> items = resMap.get(key);
                if (items == null) {
                    items = Lists.newArrayList();
                }
                items.add(item);
                resMap.put(key, items);
            }

            //拼接
            String[] langs = lang.split(",");

            for (String vo : keys) {
                List<LangItem> items = resMap.get(vo);
                if (CollectionUtils.isEmpty(items)) {
                    items = Lists.newArrayList();
                    //为空，补充
                    for (String l : langs) {
                        LangItem item = new LangItem();
                        item.setLang(Integer.valueOf(l));
                        item.setContent("");
                        items.add(item);
                    }
                }

                LangKey langKey = new LangKey();
                langKey.setKey(vo);
                langKey.setItems(items);
                resList.add(langKey);
            }
        }
        resp.setLangKeys(resList);
        return resp;
    }

    @Override
    public Long saveAppProduct(@RequestBody SaveAppProductReq req) {
        logger.info("save app product req:" + req.toString());
        AppProduct appProduct = new AppProduct();
        appProduct.setAppId(req.getAppId());
        appProduct.setProductId(req.getProductId());
        appProduct.setTenantId(req.getTenantId());
        Long id = req.getId();
        appProduct.setId(id);
        if (req.getTenantId() == null) {
            appProduct.setTenantId(getTenantId());
        }
        if (id != null && id != 0) {
            appProductService.updateById(appProduct);
        } else {
            //添加重复产品逻辑处理
            EntityWrapper<AppProduct> wrapper = new EntityWrapper<>();
            wrapper.eq("tenant_id", getTenantId());
            wrapper.eq("app_id", req.getAppId());
            wrapper.eq("product_id", req.getProductId());
            int r = appProductService.selectCount(wrapper);
            if (r == 0) {
                appProduct.setChooseLang("1,2");
                appProduct.setCreateTime(new Date());
                appProductService.insert(appProduct);
                //拷贝配网步骤
                List<Long> networkTypeIds = req.getNetworkTypeIds();
                if (networkTypeIds != null && !networkTypeIds.isEmpty()) {
                    CopyNetworkStepReq copyNetworkStepReq = new CopyNetworkStepReq(appProduct.getTenantId(),
                            req.getUserId(), req.getAppId(), req.getProductId(), req.getDeviceTypeId(), networkTypeIds);
                    deviceNetworkStepTenantService.copyNetworkStep(copyNetworkStepReq);
                }
                //判断产品下是否有子设备，有的话也要拷贝配网步骤
                List<GatewaySubDevRelationResp> relationResps = gatewaySubDevRelationApi.getGatewaySubDevByParDevId(req.getProductId(), appProduct.getTenantId());
                if (relationResps != null && !relationResps.isEmpty()) {
                    for (GatewaySubDevRelationResp childProductResp : relationResps) {
                        if (deviceNetworkStepTenantService.checkExist(req.getAppId(), childProductResp.getSubDevId(), appProduct.getTenantId())) {
                            continue;
                        }

                        ProductResp productResp = productApi.getProductById(childProductResp.getSubDevId());
                        if (productResp != null) {
                            //查找子设备的配网模式，需要根据选择的配网模式去拷贝对应的配网引导
                            List<ProductConfigNetmodeRsp> configNetmodeRsps = productConfigNetmodeApi.listByProductId(childProductResp.getSubDevId());
                            if (configNetmodeRsps == null || configNetmodeRsps.isEmpty()) {
                                continue;
                            }
                            List<String> typeCodes = configNetmodeRsps.stream().map(ProductConfigNetmodeRsp::getName).collect(Collectors.toList());
                            List<NetworkTypeResp> networkTypeResps = networkTypeApi.findByTypeCode(typeCodes);
                            if (networkTypeResps != null && !networkTypeResps.isEmpty()) {
                                List<Long> techNetworkIds = networkTypeResps.stream().map(NetworkTypeResp::getId).collect(Collectors.toList());
                                //防止配网编码被重用，还是得在根据设备类型支持配网方式过滤
                                List<NetworkTypeResp> networkTypes = technicalRelateApi.deviceSupportNetworkType(productResp.getDeviceTypeId());
                                List<Long> subNetworkTypeIds = networkTypes.stream().map(NetworkTypeResp::getId).collect(Collectors.toList());
                                //取两者交集
                                List<Long> networkIds = subNetworkTypeIds.stream().filter(netId -> techNetworkIds.contains(netId)).collect(Collectors.toList());
                                if (networkIds != null && !networkIds.isEmpty()) {
                                    CopyNetworkStepReq copyNetworkStepReq = new CopyNetworkStepReq(appProduct.getTenantId(),
                                            req.getUserId(), req.getAppId(), childProductResp.getSubDevId(), productResp.getDeviceTypeId(), networkIds);
                                    deviceNetworkStepTenantService.copyNetworkStep(copyNetworkStepReq);
                                }
                            }
                        }
                    }
                }
            } else {
                logger.info("关联产品已存在，不能重复添加！");
            }
        }
        return appProduct.getId();
    }

    @Override
    public Boolean delAppProduct(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            logger.info("关联产品主键为空，删除失败");
            return false;
        }
        Boolean flag = false;
        //删除关联产品
        List<AppProduct> appProducts = appProductService.selectBatchIds(ids);
        flag = appProductService.deleteBatchIds(ids);
        /*for (Long id : ids) {
            EntityWrapper<AppProductInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("tenant_id", getTenantId());
            wrapper.eq("app_product_id", id);
            flag = appProductInfoService.delete(wrapper);

        }*/
        //
        boolean first = true;
        List<Long> appProductIds = null;
        //删除配网引导信息
        for (AppProduct appProduct : appProducts) {
            if (first) {
                List<AppProductResp> list = appProductService.productByAppIdAndTenantId(appProduct.getAppId(), appProduct.getTenantId());
                if (list != null && !list.isEmpty()) {
                    appProductIds = list.stream().map(AppProductResp::getProductId).collect(Collectors.toList());
                }
                first = false;
            }
            deviceNetworkStepTenantService.deleteNetworkStep(getTenantId(), appProduct.getAppId(), appProduct.getProductId());
            //查看此设备是否有子设备，有的话要删除对应文案（要先校验此子设备是否还有被此app其他产品关联，是的话不能删除）
            List<GatewaySubDevRelationResp> relationResps = gatewaySubDevRelationApi.getGatewaySubDevByParDevId(appProduct.getProductId(), appProduct.getTenantId());
            if (relationResps != null && !relationResps.isEmpty()) {
                for (GatewaySubDevRelationResp childProductResp : relationResps) {
                    if (appProductIds != null) {
                        //加载子设备被绑定的网关
                        List<Long> parentIds = gatewaySubDevRelationApi.parentProductIds(childProductResp.getSubDevId(), appProduct.getTenantId());
                        if (parentIds != null && !parentIds.isEmpty()) {
                            List<Long> otherParentIds = appProductIds.stream().filter(productId -> parentIds.contains(productId)).collect(Collectors.toList());
                            if (otherParentIds != null && !otherParentIds.isEmpty()) {
                                continue;
                            }
                        }
                    }
                    deviceNetworkStepTenantService.deleteNetworkStep(getTenantId(), appProduct.getAppId(), childProductResp.getSubDevId());
                }
            }
        }
        return flag;
    }

    @Override
    public Boolean saveGuide(@RequestBody SaveGuideReq req) {
        //更新appProduct
        AppProduct appProduct = new AppProduct();
        appProduct.setId(req.getAppProductId());
        appProduct.setSmartImg(req.getSmartImg());
        appProduct.setApImg(req.getApImg());
        appProduct.setChooseLang(req.getChooseLang());
        appProductService.updateById(appProduct);

        //先删除 再保存
        EntityWrapper<AppProductInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", getTenantId());
        wrapper.eq("app_product_id", req.getAppProductId());
        appProductInfoService.delete(wrapper);

        //拼接
        //smartConfig
        List<AppProductInfo> list = Lists.newArrayList();
        List<LangKey> smartList = req.getSmartConfig();
        if (CollectionUtils.isNotEmpty(smartList)) {
            for (LangKey vo : smartList) {
                List<LangItem> items = vo.getItems();
                String key = vo.getKey();
                for (LangItem item : items) {
                    AppProductInfo info = new AppProductInfo();
                    info.setAppProductId(req.getAppProductId());
                    info.setType(Integer.valueOf(key));
                    info.setLang(item.getLang());
                    info.setTenantId(getTenantId());
                    info.setContent(item.getContent());
                    info.setMode(0);
                    list.add(info);
                }
            }
        }
        //ap
        List<LangKey> ap = req.getAp();
        if (CollectionUtils.isNotEmpty(ap)) {
            for (LangKey vo : ap) {
                List<LangItem> items = vo.getItems();
                String key = vo.getKey();
                for (LangItem item : items) {
                    AppProductInfo info = new AppProductInfo();
                    info.setAppProductId(req.getAppProductId());
                    info.setType(Integer.valueOf(key));
                    info.setLang(item.getLang());
                    info.setTenantId(getTenantId());
                    info.setContent(item.getContent());
                    info.setMode(1);
                    list.add(info);
                }
            }
        }
        //批量保存
        return appProductInfoService.insertBatch(list);
    }

    @Override
    public GetGuideResp getGuide(@RequestParam("id") Long id) {
        logger.info("get guide req:" + id);
        //获取appProduct
        GetGuideResp resp = new GetGuideResp();
        AppProduct appProduct = appProductService.selectById(id);
        if (appProduct == null) {
            logger.info("关联产品不存在，获取配网引导失败！");
            return resp;
        }
        resp.setApImg(appProduct.getApImg());
        resp.setSmartImg(appProduct.getSmartImg());
        resp.setChooseLang(appProduct.getChooseLang());
        String lang = appProduct.getChooseLang();
        //获取langKeys
        resp.setSmartConfig(getLangKeys(id, lang, 0));
        resp.setAp(getLangKeys(id, lang, 1));
        return resp;
    }

    private List<LangKey> getLangKeys(Long id, String lang, Integer mode) {
        List<LangKey> resList = Lists.newArrayList();
        EntityWrapper<AppProductInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", getTenantId());
        wrapper.eq("app_product_id", id);
        wrapper.eq("mode", mode);
        List<AppProductInfo> list = appProductInfoService.selectList(wrapper);
        Map<String, List<LangItem>> resMap = Maps.newConcurrentMap();
        for (AppProductInfo vo : list) {
            String key = String.valueOf(vo.getType());
            LangItem item = new LangItem();
            item.setLang(vo.getLang());
            item.setContent(vo.getContent());
            List<LangItem> items = resMap.get(key);
            if (items == null) {
                items = Lists.newArrayList();
            }
            items.add(item);
            resMap.put(key, items);
        }
        //拼接
        //文案类型 0 配网文案 1 按钮文案 2 帮助文案
        String[] langs = lang.split(",");
        for (int i = 0; i < 3; i++) {
            LangKey langKey = new LangKey();
            String key = String.valueOf(i);
            langKey.setKey(key);
            List<LangItem> items = resMap.get(key);
            if (items == null) {
                items = Lists.newArrayList();
                //为空，补充
                for (String l : langs) {
                    LangItem item = new LangItem();
                    item.setLang(Integer.valueOf(l));
                    item.setContent("");
                    items.add(item);
                }
            }
            langKey.setItems(items);
            resList.add(langKey);
        }
        return resList;
    }

    @Override
    public List<Long> getAppProductIdList(@RequestParam("appId") Long appId) {
        EntityWrapper<AppProduct> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", getTenantId());
        wrapper.eq("app_id", appId);
        List<AppProduct> list = appProductService.selectList(wrapper);
        List<Long> respList = Lists.newArrayList();
        for (AppProduct vo : list) {
            respList.add(vo.getProductId());
        }
        return respList;
    }

    @Override
    public List<AppProductResp> getAppProduct(@RequestParam("appId") Long appId) {
        EntityWrapper<AppProduct> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", getTenantId());
        wrapper.eq("app_id", appId);
        List<AppProduct> list = appProductService.selectList(wrapper);
        List<AppProductResp> respList = Lists.newArrayList();
        List productIdList = new ArrayList();
        for (AppProduct vo : list) {
            AppProductResp resp = new AppProductResp();
            resp.setProductId(vo.getProductId());
            resp.setId(vo.getId());
            productIdList.add(vo.getProductId());
            respList.add(resp);
        }
        if (CollectionUtils.isNotEmpty(productIdList)) {
            List<GatewaySubDevRelationResp> gatewaySubDevRelationResp = gatewaySubDevRelationApi.getGatewaySubDevByParDevIds(productIdList, getTenantId());
            gatewaySubDevRelationResp.forEach(m -> {
                AppProductResp resp = new AppProductResp();
                resp.setProductId(m.getSubDevId());
                resp.setId(null);
                respList.add(resp);
            });
        }

        return respList;
    }

    @Override
    public Map appExecPackageByProduct(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId) {
        List<AppProductResp> list = appProductService.productByAppIdAndTenantId(appId, tenantId);
        List<TenantInfoResp> tenantList = tenantService.getTenantByIds(Arrays.asList(tenantId));
        TenantInfoResp tenantInfoResp = new TenantInfoResp();
        for (TenantInfoResp code:tenantList) {
            tenantInfoResp.setCode(code.getCode());
        }
        List fileIds = Lists.newArrayList();
        List<AppProductResp> resList = Lists.newArrayList();
        List<Map<String, Object>> productIdList = new ArrayList();
        for (AppProductResp vo : list) {
            Map map = new HashMap();
            map.put("productId", vo.getProductId());
            List<GatewaySubDevRelationResp> relationResp = gatewaySubDevRelationApi.getGatewaySubDevByParDevId(vo.getProductId(), tenantId);
            relationResp.forEach(m -> {
                Map relation = new HashMap();
                relation.put("productId", m.getSubDevId());
                productIdList.add(relation);
            });
            productIdList.add(map);
        }

        productIdList.forEach(m -> {
            AppProductResp vo = new AppProductResp();
            vo.setProductId(new Long(m.get("productId").toString()));
            ProductResp productResp = productApi.getProductById(vo.getProductId());
            if (productResp != null) {
                if (productResp.getAuditStatus() == null || !productResp.getAuditStatus().equals(2)) {
                    return;
                }
                vo.setProductName(productResp.getProductName());
                if (!StringUtils.isEmpty(productResp.getIcon())) {
                    fileIds.add(productResp.getIcon());
                }
                if (productResp.getDeviceTypeIcon() != null) {
                    fileIds.add(productResp.getDeviceTypeIcon());
                }
                //获取分类名称
                if (tenantInfoResp.getCode().toLowerCase().equals("c036")){
                    if (productResp.getCatalogName().toLowerCase().equals("Hub")){
                        vo.setCatalogName("Bridge");
                    } else {
                        vo.setCatalogName(productResp.getCatalogName());
                    }
                } else {
                    vo.setCatalogName(productResp.getCatalogName());
                }
                vo.setIcon(productResp.getIcon());
                vo.setDeviceTypeName(productResp.getDeviceTypeName());
                vo.setDeviceType(productResp.getDeviceType());
                vo.setDeviceTypeIcon(productResp.getDeviceTypeIcon());
                vo.setIsDirectDevice(productResp.getIsDirectDevice());
                vo.setCommunicationMode(productResp.getCommunicationMode());
                vo.setProtocolType(productResp.getConfigNetModes());
                vo.setProductId(productResp.getId());
                vo.setDeviceTypeId(productResp.getDeviceTypeId());
                vo.setCatalogId(productResp.getCatalogId());
                vo.setCatalogOrder(productResp.getCatalogOrder());
                resList.add(vo);
            }
        });
        List<AppProductResp> respListResult = resList.stream().sorted(Comparator.comparing(AppProductResp::getCatalogOrder)).collect(Collectors.toList());
        //转换图标url
        List iconListResult = new ArrayList();
        if (fileIds.size() > 0) {
            Map<String, String> urlMap = fileApi.getGetUrl(fileIds);
            for (AppProductResp vo : respListResult) {
                if (!StringUtils.isEmpty(vo.getIcon())) {
                    String str = urlMap.get(vo.getIcon());
                    String[] result = str.split("\\?");
                    iconListResult.add(urlMap.get(vo.getIcon()));
                    vo.setIcon(vo.getIcon() + "." + result[0].substring(result[0].length() - 3, result[0].length()));
                }

            }
            for (AppProductResp vo : respListResult) {
                if (!StringUtils.isEmpty(vo.getDeviceTypeIcon())) {
                    String str = urlMap.get(vo.getDeviceTypeIcon());
                    String[] result = str.split("\\?");
                    iconListResult.add(urlMap.get(vo.getDeviceTypeIcon()));
                    vo.setDeviceTypeIcon(vo.getDeviceTypeIcon() + "." + result[0].substring(result[0].length() - 3, result[0].length()));
                }
            }
        }
//        //转换图标url
//        if (deviceTypeFileIds.size() > 0) {
//            Map<String, String> urlMap = fileApi.getGetUrl(deviceTypeFileIds);
//            for (AppProductResp vo : resList) {
//                vo.setDeviceTypeIcon(urlMap.get(vo.getDeviceTypeIcon()));
//            }
//        }
        List<Map<String, Object>> result = Lists.newArrayList();
        for (AppProductResp productResp : respListResult) {//resList.forEach(m->{
            // catalog
            Map<String, Object> title = null;
            for (Map<String, Object> catalog : result) {
                if (catalog.get("title").equals(productResp.getCatalogName())) {
                    title = catalog;
                    break;
                }
            }
            if (title == null) {
                title = Maps.newHashMap();
                title.put("title", productResp.getCatalogName());
                title.put("catalogId", productResp.getCatalogId());
                title.put("catalogOrder",productResp.getCatalogOrder());
                title.put("children", Lists.newArrayList());
                result.add(title);
            }

            // devType
            List<Map<String, Object>> devTypeList = (List<Map<String, Object>>) title.get("children");
            List<Map<String, Object>> productVOList = Lists.newArrayList();
            boolean hasType = false;
            for (Map<String, Object> devTypeItem : devTypeList) {
                if (devTypeItem.get("type").equals(productResp.getDeviceType())) {
                    productVOList = (List<Map<String, Object>>) devTypeItem.get("list");
                    hasType = true;
                    break;
                }
            }

            if (!hasType) {
                Map<String, Object> devTypeVo = Maps.newHashMap();
                devTypeVo.put("name", productResp.getDeviceTypeName());
                devTypeVo.put("type", productResp.getDeviceType());
                devTypeVo.put("icon", productResp.getDeviceTypeIcon());
                devTypeVo.put("deviceTypeId", productResp.getDeviceTypeId());
                devTypeVo.put("list", productVOList);
                devTypeList.add(devTypeVo);
            }

            // prod
            Map<String, Object> product = new HashMap();
            product.put("name", productResp.getProductName());
            product.put("icon", productResp.getIcon());
            product.put("protocol", productResp.getProtocolType());
            product.put("communicationMode", productResp.getCommunicationMode());
            product.put("productId", productResp.getProductId());
            productVOList.add(product);
        }
        AppInfoResp appInfo = this.getAppById(appId);
        Map map = new HashMap();
        map.put("displayIdentification", appInfo.getDisplayIdentification());
        map.put("result", result);
        map.put("iconListResult", iconListResult.stream().distinct().collect(Collectors.toList()));
        return map;
    }

    @Override
    public List<AppProductResp> getAppProductByAppIdAndTenantId(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId) {
        return appProductService.productByAppIdAndTenantId(appId, tenantId);
    }


    /**
     * 描述：APP审核列表查询
     *
     * @param req 查询参数
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewResp>
     * @author maochengyuan
     * @created 2018/10/25 14:39
     */
    @Override
    public Page<AppReviewResp> getAppListByAuditStatus(@RequestBody AppReviewSearchReq req) {
        com.iot.common.helper.Page<AppReviewResp> myPage = new com.iot.common.helper.Page<>();
        List<AppReviewResp> resLst = new ArrayList<>();


        /**查询APP信息*/
        Page<AppInfo> page = this.appInfoService.getAppInfoList(req);
        if (!CommonUtil.isEmpty(page.getResult())) {
            /**存放APPid*/
            Set<Long> appIds = new HashSet<>();
            /**存放租户id*/
            Set<Long> tenIds = new HashSet<>();
            page.getResult().forEach(o -> {
                appIds.add(o.getId());
                tenIds.add(o.getTenantId());
                resLst.add(new AppReviewResp(o.getId(), o.getAppName(), o.getAuditStatus(), o.getTenantId(), o.getApplyAuditTime(), o.getCreateBy()));
            });
            /**查询企业信息*/
            Map<Long, TenantInfoResp> tenMap = this.tenantService.getTenantByIds(tenIds).stream().collect(Collectors.toMap(TenantInfoResp::getId, o -> o));
            /**查询申请信息*/
            // Map<Long, AppReviewRecordResp> applyRecords = this.appReviewRecordService.getApplyRecords(appIds);
            /**查询审核信息*/
            Map<Long, AppReviewRecordResp> auditRecords = this.appReviewRecordService.getAuditRecords(appIds);
            resLst.forEach(o -> {
                /*if (applyRecords.containsKey(o.getId())) {
                    o.setApplyTime(applyRecords.get(o.getId()).getCreateTime());
                }*/

                if (auditRecords.containsKey(o.getId())) {
                    o.setAuditOperId(auditRecords.get(o.getId()).getCreateBy());
                }
                TenantInfoResp ten = tenMap.get(o.getTenantId());
                if (ten != null) {
                    o.setEntName(ten.getName());
                    o.setEntCellphone(ten.getCellphone());
                    o.setEntContacts(ten.getContacts());
                }
            });
        }
        BeanUtils.copyProperties(page, myPage);
        myPage.setResult(resLst);
        return myPage;
    }

    /**
     * @return
     * @despriction：通过打包状态获取appId
     * @author yeshiyuan
     * @created 2018/11/14 16:38
     */
    @Override
    public List<Long> getAppIdByPackStatus(@RequestParam("packStatus") Integer packStatus) {
        return appInfoService.getAppIdByPackStatus(packStatus);
    }

    /**
     * @return
     * @despriction：批量获取app信息
     * @author yeshiyuan
     * @created 2018/11/14 17:26
     */
    @Override
    public List<AppInfoResp> getAppByIds(@RequestParam("ids") List<Long> ids) {
        List<AppInfo> appInfos = appInfoService.selectBatchIds(ids);
        List<AppInfoResp> appInfoResps = new ArrayList<>();
        appInfos.forEach(appInfo -> {
            AppInfoResp appInfoResp = new AppInfoResp();
            BeanUtils.copyProperties(appInfo, appInfoResp);
            appInfoResps.add(appInfoResp);
        });
        return appInfoResps;
    }

    /**
     * @return
     * @despriction：修改app审核状态为空
     * @author yeshiyuan
     * @created 2018/11/16 9:49
     */
    @Override
    public void updateAuditStatusToNull(Long appId) {
        appInfoService.updateAuditStatusToNull(appId);
        //清除app能否使用标志
        RedisCacheUtil.delete(RedisKeyUtil.getAppUsedKey(appId));
    }

    @Override
    public Integer countAppProductByproductId(Long productId) {
        return appProductService.countAppProductByproductId(productId);
    }

    /**
     * @return
     * @despriction：重开可编辑
     * @author yeshiyuan
     * @created 2018/10/25 17:06
     */
    @Override
    public void reOpen(Long appId) {
        AppInfo appInfo = appInfoService.selectById(appId);
        if (appInfo.getConfirmStatus() != 1) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "app haven't confirm");
        }
        appInfo.setConfirmStatus(null);
        appInfo.setAuditStatus(AuditStatusEnum.Pending.getAuditStatus());
        appInfoService.updateById(appInfo);
    }

    /**
     * @return void
     * @despriction：解绑产品关联的app,并删除相关的数据（配网文案）
     * @author yeshiyuan
     * @created 2018/12/15 16:06
     * @params [productId, tenantId]
     */
    @Override
    public void unbindProductRelateApp(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId) {
        List<Long> appIds = appProductService.findAppIdByProductId(productId, tenantId);
        if (appIds != null && !appIds.isEmpty()) {
            appProductService.deleteByProductId(productId, tenantId);
            //删除产品下的app配网文案
            for (Long appId : appIds) {
                deviceNetworkStepTenantService.deleteNetworkStep(tenantId, appId, productId);
            }
        }
    }

    /**
     * @despriction：校验app是否能够使用(付款成功的app才能使用)
     * @author  yeshiyuan
     * @created 2018/12/28 14:43
     * @params [appId, tenantId]
     * @return boolean
     */
    @Override
    public boolean checkAppCanUsed(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId) {
        String appKey = RedisKeyUtil.getAppUsedKey(appId);
        String usedFlag = RedisCacheUtil.valueGet(appKey);
        if (StringUtil.isBlank(usedFlag)) {
            GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsCode(GoodsCoodEnum.C0005.getCode());
            if (goodsInfo != null) {
                ServiceBuyRecordResp serviceBuyRecordResp = serviceBuyRecordApi.getServiceBuyRecordDetail(appId, goodsInfo.getId(), tenantId);
                if (serviceBuyRecordResp == null) {
                    RedisCacheUtil.valueSet(appKey, AppCanUsedEnum.FORBIDDEN.getValue(), 31 * 24 * 3600L);
                    return false;
                }
                Integer payStatus = serviceBuyRecordResp.getPayStatus();
                if (!ServicePayStatusEnum.checkCanUsed(payStatus)) {
                    RedisCacheUtil.valueSet(appKey, AppCanUsedEnum.FORBIDDEN.getValue(), 31 * 24 * 3600L);
                    return false;
                }
                RedisCacheUtil.valueSet(appKey, AppCanUsedEnum.USED.getValue(), 31 * 24 * 3600L);
                return true;
            }
        } else {
            if (AppCanUsedEnum.USED.getValue().equals(usedFlag)) {
                return true;
            }
        }
        return false;
    }

}
