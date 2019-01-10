package com.iot.shcs.module.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.*;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.vo.rsp.*;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.module.api.ModuleCoreApi;
import com.iot.shcs.module.service.FileService;
import com.iot.shcs.module.service.ModuleCoreService;
import com.iot.shcs.module.vo.resp.*;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.vo.resp.AppProductResp;
import com.iot.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:21 2018/10/22
 * @Modify by:
 */
@Slf4j
@RestController
public class ModuleController implements ModuleCoreApi {
    @Autowired
    private ServiceModuleApi serviceModuleApi;

    @Autowired
    private StyleTemplateApi styleTemplateApi;

    @Autowired
    private ServiceModuleStyleApi serviceModuleStyleApi;

    @Autowired
    private ServiceActionApi serviceActionApi;

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    @Autowired
    private ProductCoreApi productApi;

    @Autowired
    private ModuleCoreService serviceModuleCoreService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AppApi appApi;

    @Autowired
    private GatewaySubDevRelationApi gatewaySubDevRelationApi;

    @Autowired
    private ProductTimerApi productTimerApi;

    private static void parseIftttInfoList(List<ListIfInfoResp> ifInfoList, List<ListThenInfoResp> thenInfoList, List<GetServiceModuleResp> serviceModules) {
        if (CollectionUtils.isEmpty(serviceModules)) {
            return;
        }
        for (GetServiceModuleResp module : serviceModules) {
            parseIftttPropertites(ifInfoList, thenInfoList, module.getPropertyList());

            parseIftttEventes(ifInfoList, thenInfoList, module.getEventList());
            //parse action event
            parseIftttActiones(ifInfoList, thenInfoList, module.getActionList());
        }
    }

    private static void parseIftttActiones(List<ListIfInfoResp> ifInfoList, List<ListThenInfoResp> thenInfoList, List<ListActionListResp> actionList) {
        if (CollectionUtils.isEmpty(actionList)) {
            return;
        }
        actionList.forEach(action -> {
            ServiceModuleActionResp commAction = action.getActionInfo();
            if (commAction != null) {
                parseCommIfttt(ifInfoList, thenInfoList, commAction.getPortalIftttType(), commAction.getId(), commAction.getName(), commAction.getCode(), "method");
            }
        });
    }

    private static void parseIftttEventes(List<ListIfInfoResp> ifInfoList, List<ListThenInfoResp> thenInfoList, List<ListEventListResp> eventList) {
        if (CollectionUtils.isEmpty(eventList)) {
            return;
        }
        eventList.forEach(event -> {

            ServiceModuleEventResp commEvent = event.getEventInfo();
            if (commEvent != null) {
                parseCommIfttt(ifInfoList, thenInfoList, commEvent.getPortalIftttType(), commEvent.getId(), commEvent.getName(), commEvent.getCode(), "event");
            }
        });
    }

    private static void parseCommIfttt(List<ListIfInfoResp> ifInfoList, List<ListThenInfoResp> thenInfoList, Integer iftttType, Long id, String name, String code, String type) {
        if (iftttType == null) {
            return;
        }
        if (iftttType.compareTo(0) == 0) {
            return;
        }
        //0:不支持 1：支持if 2:支持then 3:支持if支持then
        if (iftttType.compareTo(1) == 0) {
            ifInfoList.add(ListIfInfoResp.builder().type(type).code(code).name(name).id(id).build());
        } else if (iftttType.compareTo(2) == 0) {
            thenInfoList.add(ListThenInfoResp.builder().type(type).code(code).name(name).id(id).build());
        } else if (iftttType.compareTo(3) == 0) {
            ifInfoList.add(ListIfInfoResp.builder().type(type).code(code).name(name).id(id).build());
            thenInfoList.add(ListThenInfoResp.builder().type(type).code(code).name(name).id(id).build());
        }
    }


    private static void parseIftttPropertites(List<ListIfInfoResp> ifInfoList, List<ListThenInfoResp> thenInfoList, List<ServiceModulePropertyResp> propertyList) {
        if (CollectionUtils.isEmpty(propertyList)) {
            return;
        }
        //0:不支持 1：支持if 2:支持then 3:支持if支持then
        propertyList.forEach(property -> {
            parseCommIfttt(ifInfoList, thenInfoList, property.getPortalIftttType(), property.getId(), property.getName(), property.getCode(), "property");
        });
    }

    /**
     * 获取产品对应的功能组定义列表 [新增 弹窗列表]---列表去重
     *
     * @param productId
     * @return
     * @author lucky
     * @date 2018/7/2 9:27
     */
    public GetProductModuleResp findServiceModuleListByProductId(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        SaaSContextHolder.setCurrentTenantId(tenantId);
        GetProductModuleResp resultData = getModuleByProductId(tenantId, productId);
        return resultData;
    }

    public List<GetProductModuleResp> findServiceModuleList(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId) {
        List<GetProductModuleResp> resultDataList = Lists.newArrayList();
        List<AppProductResp> productResps = appApi.getAppProductByAppIdAndTenantId(appId, tenantId);
        if (CollectionUtils.isEmpty(productResps)) {
            return resultDataList;
        }
        SaaSContextHolder.setCurrentTenantId(tenantId);
        List<Map<String,Object>> productIdList = new ArrayList<>();
        productResps.forEach(appProductResp -> {
            Map map = new HashMap();
            map.put("productId",appProductResp.getProductId());
            List<GatewaySubDevRelationResp> relationResp = gatewaySubDevRelationApi.getGatewaySubDevByParDevId(appProductResp.getProductId(),tenantId);
            relationResp.forEach(m->{
                Map relation = new HashMap();
                relation.put("productId",m.getSubDevId());
                productIdList.add(relation);
            });
            productIdList.add(map);
        });
        Map<Long, GetProductModuleResp> temp = Maps.newHashMap();
        productIdList.forEach(n->{
            Long productId = new Long(n.get("productId").toString());
            GetProductModuleResp getProductModuleResp = getModuleByProductId(tenantId, productId);
            if (getProductModuleResp != null) {
                temp.put(productId, getProductModuleResp);
            }
        });
        this.addProductTimer(temp);
        resultDataList.addAll(temp.values());
        return resultDataList;
    }

    /**
     * 描述：查询产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 19:47
     * @param temp
     * @return void
     */
    private void addProductTimer(Map<Long, GetProductModuleResp> temp){
        if(temp.isEmpty()){
            return;
        }
        //修改处
        List productIds = new ArrayList();
        productIds.addAll(temp.keySet());
        Map<Long, List<ProductTimerResp>> maps = this.productTimerApi.getProductTimers(productIds);
        if(ObjectUtils.isEmpty(maps)){
            return;
        }
        maps.forEach((k, v)->{
            if(!ObjectUtils.isEmpty(v)){
                List<ListIfInfoResp> rest = temp.get(k).getIfList() == null ? new ArrayList<>() : temp.get(k).getIfList();
                for(ProductTimerResp p : v){
                    ListIfInfoResp resp = new ListIfInfoResp();
                    resp.setId(p.getId());
                    resp.setType(p.getTimerType());
                    rest.add(resp);
                }
                temp.get(k).setIfList(rest);
            }
        });
    }

    public List<ListProductByTenantResp> findModuleProductListByTenantId(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId) {
        List<ListProductByTenantResp> resultDataList = Lists.newArrayList();
        List<AppProductResp> productResps = appApi.getAppProductByAppIdAndTenantId(appId, tenantId);
        if (CollectionUtils.isEmpty(productResps)) {
            return resultDataList;
        }

        productResps.forEach(product -> {
            ListProductByTenantResp productByTenantResp = new ListProductByTenantResp();
            productByTenantResp.setProductId(product.getProductId());
            resultDataList.add(productByTenantResp);
        });
        return resultDataList;
    }


    private GetProductModuleResp getModuleByProductId(Long tenantId, Long productId) {
        GetProductInfoRespVo productResp = productApi.getByProductId(productId);
        if (productResp == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
//        if (tenantId.compareTo(productResp.getTenantId()) != 0) {
//            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
//        }
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);

        List<StyleTemplateResp> productStyleList = styleTemplateApi.listByProductId(productId);


        GetProductModuleResp resultData = new GetProductModuleResp();
        BeanUtils.copyProperties(productResp, resultData);

        List<ListIfInfoResp> ifInfoList = Lists.newArrayList();
        List<ListThenInfoResp> thenInfoList = Lists.newArrayList();

        List<GetServiceModuleResp> respList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(productModuleList)) {
            // 产品对应的功能组为空
            for (ServiceModuleListResp source : productModuleList) {

                // 功能组对应的样式
                ArrayList<Long> serviceModuleIds = new ArrayList<>();
                serviceModuleIds.add(source.getId());
                GetServiceModuleResp target = new GetServiceModuleResp();
                BeanUtils.copyProperties(source, target);
                List<ServiceModuleStyleResp> moduleStyleList = serviceModuleStyleApi.list(serviceModuleIds);
                if (!CollectionUtils.isEmpty(moduleStyleList)) {
                    target.setStyles(moduleStyleList);
                }
                List<ListActionListResp> actionInfoRespList =
                        serviceModuleCoreService.findActionsByServiceModuleId(source.getId());
                target.setActionList(actionInfoRespList);

                List<ListEventListResp> eventListRespList =
                        serviceModuleCoreService.findEventsByServiceModuleId(source.getId());
                target.setEventList(eventListRespList);

                List<ServiceModulePropertyResp> propertyRespList =
                        serviceModuleCoreService.findPropertiesByServiceModuleId(source.getId());
                target.setPropertyList(propertyRespList);

                respList.add(target);
            }
            StyleTemplateResp styleTemplateResp =
                    productStyleList == null || productStyleList.size() == 0 ? null : productStyleList.get(0);
            if (styleTemplateResp != null) {
                if (!StringUtils.isEmpty(styleTemplateResp.getImg())) {
                    try {
                        FileResp fileResp = fileService.getUrl(styleTemplateResp.getImg());
                        styleTemplateResp.setImg(fileResp.getUrl());
                    } catch (Exception e) {
                        log.warn("get styleTemplate img error", e);
                    }
//                    try {
//                        FileResp resourceResp = fileService.getUrl(styleTemplateResp.getResourceLink());
//                        styleTemplateResp.setResourceLink(resourceResp.getUrl());
//                    } catch (Exception e) {
//                        log.warn("get styleTemplate resourceLink error", e);
//                    }
                }
            }
            resultData.setStyle(styleTemplateResp);
            resultData.setServiceModules(respList);

            // 遍历解析内部哪些是 ifttt
            parseIftttInfoList(ifInfoList, thenInfoList, resultData.getServiceModules());

            resultData.setIfList(ifInfoList);
            resultData.setThenList(thenInfoList);
        }
        return resultData;

    }

}
