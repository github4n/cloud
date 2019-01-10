package com.iot.portal.web.controller;

import com.google.common.collect.Lists;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.*;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.TimerConfigReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.*;
import com.iot.file.api.FileApi;
import com.iot.portal.service.FileService;
import com.iot.portal.service.ServiceModuleCoreService;
import com.iot.portal.web.utils.ModuleUtils;
import com.iot.portal.web.vo.*;
import com.iot.portal.web.vo.req.SaveModuleIftttReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xfz
 * @Descrpiton: 产品定义-功能定义模组
 * @Date: 9:06 2018/7/2
 * @Modify by:
 */
@Api(value = "portal-功能定义模组管理", description = "portal-功能定义模组管理")
@RestController
@RequestMapping("/portal/serviceModule")
public class PortalServiceModuleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalServiceModuleController.class);

    @Autowired
    private ServiceModuleApi serviceModuleApi;

    @Autowired
    private ServiceActionApi serviceActionApi;

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ServiceModuleCoreService serviceModuleCoreService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private ProductTimerApi productTimerApi;

    @Autowired
    private ServiceEventApi serviceEventApi;

    /**
     * 根据设备类型id获取所有的功模组
     *
     * @param
     * @return
     * @author lucky
     * @date 2018/7/3 10:00
     */
    @ApiOperation("根据设备类型id获取所有的功能模组")
    @RequestMapping(value = "/findServiceModuleListByDeviceTypeId", method = RequestMethod.GET)
    public CommonResponse<List<PortalServiceModuleResp>> findServiceModuleListByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId) {
        AssertUtils.notNull(deviceTypeId, "deviceTypeId.notnull");
        List<PortalServiceModuleResp> respList = Lists.newArrayList();
        List<ServiceModuleListResp> parentModuleList = serviceModuleApi.findServiceModuleListByDeviceTypeId(deviceTypeId);
        if (CollectionUtils.isEmpty(parentModuleList)) {
            LOGGER.info("boss-未定义功能模组");
            return CommonResponse.success(respList);
        }
        parentModuleList.forEach(m -> {
            try {
                if (StringUtils.isNotBlank(m.getImg())) {
                    FileResp fileResp = fileService.getUrl(m.getImg());
                    m.setImg(fileResp.getUrl());
                }
                if (StringUtils.isNotBlank(m.getChangeImg())) {
                    FileResp fileResp = fileService.getUrl(m.getChangeImg());
                    m.setChangeImg(fileResp.getUrl());
                }
            } catch (Exception e) {
                LOGGER.warn("serviceModule {} img error", m.getName(), e);
            }
        });

        for (ServiceModuleListResp parentModule : parentModuleList) {
            PortalServiceModuleResp target = new PortalServiceModuleResp();
            BeanUtils.copyProperties(parentModule, target);
            target.setWhetherCheck(false);

            List<PortalActionListResp> parentActionList = serviceModuleCoreService.findActionsByServiceModuleId(parentModule.getId());
            List<PortalEventListResp> parentEventList = serviceModuleCoreService.findEventsByServiceModuleId(parentModule.getId());
            List<PortalPropertyListResp> parentPropertyList = serviceModuleCoreService.findPropertiesByServiceModuleId(parentModule.getId());

            target.setActionList(parentActionList);
            target.setEventList(parentEventList);
            target.setPropertyList(parentPropertyList);
            respList.add(target);
        }
        return CommonResponse.success(respList);
    }

    /**
     * 获取所有的功模组
     *
     * @param
     * @return
     * @author lucky
     * @date 2018/7/2 19:13
     */
    @ApiOperation("获取所有的功模组")
    @RequestMapping(value = "/findServiceModuleListAll", method = RequestMethod.GET)
    public CommonResponse<List<PortalServiceModuleResp>> findServiceModuleListAll() {
        List<PortalServiceModuleResp> respList = Lists.newArrayList();
        List<ServiceModuleListResp> parentModuleList = serviceModuleApi.findServiceModuleListByParentIdNull();
        if (CollectionUtils.isEmpty(parentModuleList)) {
            LOGGER.info("boss-未定义功能模组");
            return CommonResponse.success(respList);
        }
        for (ServiceModuleListResp parentModule : parentModuleList) {
            PortalServiceModuleResp target = new PortalServiceModuleResp();
            BeanUtils.copyProperties(parentModule, target);
            target.setWhetherCheck(false);
            respList.add(target);
        }
        return CommonResponse.success(respList);
    }

    /**
     * 获取所有的功模组
     *
     * @param
     * @return
     * @author lucky
     * @date 2018/7/2 19:13
     */
    @ApiOperation("获取指定产品对应的的功模组【不包括事件方法属性】")
    @RequestMapping(value = "/findServiceModuleInfoByProductId", method = RequestMethod.GET)
    public CommonResponse<List<PortalServiceModuleResp>> findServiceModuleInfoByProductId(@RequestParam("productId") Long productId) {
        List<PortalServiceModuleResp> respList = Lists.newArrayList();
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
        if (CollectionUtils.isEmpty(productModuleList)) {
            LOGGER.info("boss-未定义功能模组");
            return CommonResponse.success(respList);
        }
        for (ServiceModuleListResp parentModule : productModuleList) {
            PortalServiceModuleResp target = new PortalServiceModuleResp();
            BeanUtils.copyProperties(parentModule, target);
            target.setWhetherCheck(true);
            respList.add(target);
        }
        return CommonResponse.success(respList);
    }

    /**
     * 获取产品对应的功能组定义列表
     *
     * @param productId
     * @return
     * @author lucky
     * @date 2018/7/2 9:27
     */
    @ApiOperation("获取产品对应的功能组定义列表【事件方法属性】")
    @RequestMapping(value = "/getServiceModuleListByProductId", method = RequestMethod.GET)
    public CommonResponse<List<PortalServiceModuleResp>> getServiceModuleListByProductId(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        List<PortalServiceModuleResp> respList = Lists.newArrayList();
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
        if (!CollectionUtils.isEmpty(productModuleList)) {
            //产品对应的功能组为空
            for (ServiceModuleListResp source : productModuleList) {
                PortalServiceModuleResp target = new PortalServiceModuleResp();
                BeanUtils.copyProperties(source, target);

                List<PortalActionListResp> actionInfoRespList = serviceModuleCoreService.findActionsByServiceModuleId(source.getId());
                target.setActionList(actionInfoRespList);

                List<PortalEventListResp> eventListRespList = serviceModuleCoreService.findEventsByServiceModuleId(source.getId());
                target.setEventList(eventListRespList);

                List<PortalPropertyListResp> propertyRespList = serviceModuleCoreService.findPropertiesByServiceModuleId(source.getId());
                target.setPropertyList(propertyRespList);
                target.setWhetherCheck(true);
                respList.add(target);
            }
        }
        //获取所有的 boss 定义功能模组给产品
        return CommonResponse.success(respList);
    }

    /**
     * 获取产品对应的功能组定义列表 [新增 弹窗列表]-
     *
     * @param productId
     * @return
     * @author lucky
     * @date 2018/7/2 9:27
     */
    @ApiOperation("获取产品对应的功能组定义列表【事件方法属性【包括是否选中问题】】")
    @RequestMapping(value = "/findServiceModuleListByProductId", method = RequestMethod.GET)
    public CommonResponse<List<PortalServiceModuleResp>> findServiceModuleListByProductId(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        List<PortalServiceModuleResp> respList = Lists.newArrayList();
        Long deviceTypeId = productResp.getDeviceTypeId();
        List<ServiceModuleListResp> parentModuleList = serviceModuleApi.findServiceModuleListByDeviceTypeId(deviceTypeId);
        if (parentModuleList != null) {
            List<String> fileIds = Lists.newArrayList();
            parentModuleList.forEach(m -> {
                    if (StringUtils.isNotBlank(m.getImg())) {
                        fileIds.add(m.getImg());
                    }
                    if (StringUtils.isNotBlank(m.getChangeImg())) {
                        fileIds.add(m.getChangeImg());
                    }
            });
            if (!CollectionUtils.isEmpty(fileIds)){
                Map<String, String> fileUrls = fileApi.getGetUrl(fileIds);
                parentModuleList.forEach(m -> {
                    if (StringUtils.isNotBlank(m.getImg())) {
                        m.setImg(fileUrls.get(m.getImg()));
                    }
                    if (StringUtils.isNotBlank(m.getChangeImg())) {
                        m.setChangeImg(fileUrls.get(m.getChangeImg()));
                    }
                });
            }
        }
        if (CollectionUtils.isEmpty(parentModuleList)) {
            LOGGER.info("boss-未定义功能模组");
            return CommonResponse.success(respList);
        }
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
        if (CollectionUtils.isEmpty(productModuleList)) {
            //产品对应的功能组为空
            for (ServiceModuleListResp source : parentModuleList) {
                PortalServiceModuleResp target = new PortalServiceModuleResp();
                BeanUtils.copyProperties(source, target);

                List<PortalActionListResp> actionInfoRespList = serviceModuleCoreService.findActionsByServiceModuleId(source.getId());
                target.setActionList(actionInfoRespList);

                List<PortalEventListResp> eventListRespList = serviceModuleCoreService.findEventsByServiceModuleId(source.getId());
                target.setEventList(eventListRespList);

                List<PortalPropertyListResp> propertyRespList = serviceModuleCoreService.findPropertiesByServiceModuleId(source.getId());
                target.setPropertyList(propertyRespList);

                respList.add(target);
            }
            //获取所有的 boss 定义功能模组给产品
            return CommonResponse.success(respList);
        }
        for (ServiceModuleListResp parentModule : parentModuleList) {
            boolean whetherCheck = false;
            ServiceModuleListResp tempProductModule = null;

            if (!CollectionUtils.isEmpty(productModuleList)) {
                for (ServiceModuleListResp productModule : productModuleList) {
                    if (parentModule.getId().compareTo(productModule.getParentId()) != 0) {
                        continue;
                    }
                    whetherCheck = true;
                    tempProductModule = productModule;
                    break;
                }
            }
            List<PortalActionListResp> parentActionList = serviceModuleCoreService.findActionsByServiceModuleId(parentModule.getId());
            List<PortalEventListResp> parentEventList = serviceModuleCoreService.findEventsByServiceModuleId(parentModule.getId());
            List<PortalPropertyListResp> parentPropertyList = serviceModuleCoreService.findPropertiesByServiceModuleId(parentModule.getId());

            PortalServiceModuleResp target = new PortalServiceModuleResp();
            if (whetherCheck) {
                BeanUtils.copyProperties(tempProductModule, target);
                //功能组一样比较方法、事件、属性
                //1.1获取功能组方法的比较
                List<PortalActionListResp> productActionList = serviceModuleCoreService.findActionsByServiceModuleId(tempProductModule.getId());
                target.setActionList(ModuleUtils.parseActions(parentActionList, productActionList));
                //1.2 事件
                List<PortalEventListResp> productEventList = serviceModuleCoreService.findEventsByServiceModuleId(tempProductModule.getId());
                target.setEventList(ModuleUtils.parseEvents(parentEventList, productEventList));
                //1.3 属性
                List<PortalPropertyListResp> productPropertyList = serviceModuleCoreService.findPropertiesByServiceModuleId(tempProductModule.getId());
                target.setPropertyList(ModuleUtils.parseProperties(parentPropertyList, productPropertyList));
            } else {
                BeanUtils.copyProperties(parentModule, target);
                target.setActionList(parentActionList);
                target.setEventList(parentEventList);
                target.setPropertyList(parentPropertyList);
            }
            //聚合两者是否被选中
            target.setWhetherCheck(whetherCheck);
            respList.add(target);
        }
        return CommonResponse.success(respList);
    }


    /**
     * 获取模组明细列表【包括方法、事件、属性】
     *
     * @param serviceModuleReq
     * @return
     * @author lucky
     * @date 2018/7/2 19:15
     */
    @Deprecated
    @ApiOperation("获取模组明细列表【包括方法、事件、属性】")
    @RequestMapping(value = "/findServiceModuleListByServiceModuleIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<List<PortalServiceModuleResp>> findServiceModuleListByServiceModuleIds(@RequestBody PortalServiceModuleReq serviceModuleReq) {
        AssertUtils.notNull(serviceModuleReq, "serviceModuleReq.notnull");
        AssertUtils.notNull(serviceModuleReq.getProductId(), "productId.notnull");
        AssertUtils.notEmpty(serviceModuleReq.getServiceModuleIds(), "serviceModuleId.notnull");
        List<PortalServiceModuleResp> respList = Lists.newArrayList();
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(serviceModuleReq.getProductId());
        if (!CollectionUtils.isEmpty(productModuleList)) {
            for (ServiceModuleListResp productModule : productModuleList) {
                if (!serviceModuleReq.getServiceModuleIds().contains(productModule.getId())) {
                    continue;
                }
                PortalServiceModuleResp target = new PortalServiceModuleResp();
                List<PortalActionListResp> parentActionList =
                        serviceModuleCoreService.findActionsByServiceModuleId(productModule.getId());
                List<PortalEventListResp> parentEventList =
                        serviceModuleCoreService.findEventsByServiceModuleId(productModule.getId());
                List<PortalPropertyListResp> parentPropertyList =
                        serviceModuleCoreService.findPropertiesByServiceModuleId(productModule.getId());

                BeanUtils.copyProperties(productModule, target);
                target.setActionList(parentActionList);
                target.setEventList(parentEventList);
                target.setPropertyList(parentPropertyList);
                target.setWhetherCheck(true);
                respList.add(target);
            }
            return CommonResponse.success(respList);
        }
        //获取所有对应的所有选中的功能组
        for (Long serviceModuleId : serviceModuleReq.getServiceModuleIds()) {
            ServiceModuleInfoResp moduleInfo = serviceModuleApi.getServiceModuleInfoByServiceModuleId(serviceModuleId);
            if (moduleInfo == null) {
                continue;
            }
            serviceModuleCoreService.findActionsByServiceModuleId(serviceModuleId);
            List<PortalActionListResp> parentActionList =
                    serviceModuleCoreService.findActionsByServiceModuleId(serviceModuleId);
            List<PortalEventListResp> parentEventList =
                    serviceModuleCoreService.findEventsByServiceModuleId(serviceModuleId);
            List<PortalPropertyListResp> parentPropertyList =
                    serviceModuleCoreService.findPropertiesByServiceModuleId(serviceModuleId);

            PortalServiceModuleResp target = new PortalServiceModuleResp();
            BeanUtils.copyProperties(moduleInfo, target);
            target.setActionList(parentActionList);
            target.setEventList(parentEventList);
            target.setPropertyList(parentPropertyList);
            target.setWhetherCheck(true);
            respList.add(target);
        }
        return CommonResponse.success(respList);
    }

    @ApiOperation("修改模组信息")
    @RequestMapping(value = "/updateServiceModuleInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<AddOrUpdateServiceModuleReq> updateServiceModuleInfo(@RequestBody AddOrUpdateServiceModuleReq serviceModuleReq) {
        LOGGER.info("updateServiceModuleInfo : {}", serviceModuleReq);
        AssertUtils.notNull(serviceModuleReq, "serviceModuleReq.notnull");
        AssertUtils.notNull(serviceModuleReq.getProductId(), "productId.notnull");
        AssertUtils.notEmpty(serviceModuleReq.getServiceModuleList(), "serviceModule.notnull");
        serviceModuleApi.updateServiceModuleInfo(serviceModuleReq);
        return CommonResponse.success();
    }

    /**
      * @despriction：查询支持联动配置的模组
      * @author  yeshiyuan
      * @created 2018/10/23 15:10
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询支持联动配置的模组", notes = "查询支持联动配置的模组")
    @RequestMapping(value = "/queryIftttModule", method = RequestMethod.GET)
    public CommonResponse queryIftttModule(Long productId) {
        PortalModuleIftttResp resp = new PortalModuleIftttResp();
        List<ProductTimerResp> list = this.productTimerApi.getProductTimer(productId);
        if(!ObjectUtils.isEmpty(list)){
            List<String> timers = list.stream().map(ProductTimerResp::getTimerType).collect(Collectors.toList());
            resp.setTimerTypes(timers);
        }
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
        if (productModuleList == null) {
            return CommonResponse.success(resp);
        }
        List<Long> serviceModuleIds = productModuleList.stream().map(ServiceModuleListResp::getId).collect(Collectors.toList());
        if (!serviceModuleIds.isEmpty()) {
            ModuleSupportIftttResp actionResp = serviceActionApi.findSupportIftttActions(serviceModuleIds, SaaSContextHolder.currentTenantId());
            resp.setAction(actionResp);
            ModuleSupportIftttResp eventResp = serviceEventApi.findSupportIftttEvents(serviceModuleIds, SaaSContextHolder.currentTenantId());
            resp.setEvent(eventResp);
            ModuleSupportIftttResp propertyResp = servicePropertyApi.findSupportIftttProperties(serviceModuleIds, SaaSContextHolder.currentTenantId());
            resp.setProperty(propertyResp);
        }
        return CommonResponse.success(resp);
    }

    /**
      * @despriction：联动配置管理保存
      * @author  yeshiyuan
      * @created 2018/10/23 18:58
      * @param req
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "联动配置管理保存", notes = "联动配置管理保存")
    @RequestMapping(value = "/saveModuleIfttt", method = RequestMethod.POST)
    public CommonResponse saveModuleIfttt(@RequestBody SaveModuleIftttReq req) {
        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(req.getProductId());
        List<Long> serviceModuleIds = new ArrayList<>();
        if (productModuleList != null && productModuleList.size() > 0) {
            serviceModuleIds = productModuleList.stream().map(ServiceModuleListResp::getId).collect(Collectors.toList());
        }
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        if (req.getActions()!=null) {
            UpdateModuleSupportIftttReq actionReq = new UpdateModuleSupportIftttReq(tenantId, userId, serviceModuleIds,req.getActions());
            serviceActionApi.updateActionSupportIfttt(actionReq);
        }
        if (req.getEvents()!=null) {
            UpdateModuleSupportIftttReq evevntReq = new UpdateModuleSupportIftttReq(tenantId, userId, serviceModuleIds, req.getEvents());
            serviceEventApi.updateEventSupportIfttt(evevntReq);
        }
        if (req.getProperties()!=null) {
            UpdateModuleSupportIftttReq propertyReq = new UpdateModuleSupportIftttReq(tenantId, userId, serviceModuleIds, req.getProperties());
            servicePropertyApi.updatePropertySupportIfttt(propertyReq);
        }
        if(!ObjectUtils.isEmpty(req.getTimerTypes())){
            TimerConfigReq reqs = new TimerConfigReq(req.getTimerTypes(), req.getProductId(), tenantId, userId);
            this.productTimerApi.uptProductTimer(reqs);
        }else{
            this.productTimerApi.delProductTimer(req.getProductId());
        }
        return CommonResponse.success();
    }

}
