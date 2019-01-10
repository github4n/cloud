package com.iot.device.service.core;

import com.iot.common.exception.BusinessException;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.service.IProductService;
import com.iot.device.service.IProductToServiceModuleService;
import com.iot.device.service.IServiceModuleActionService;
import com.iot.device.service.IServiceModuleEventService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.service.IServiceToPropertyService;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.AddServiceModuleActionReq;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.AddServiceModuleReq;
import com.iot.device.vo.req.ServiceModuleInfoReq;
import com.iot.device.vo.req.UpdateActionInfoReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:36 2018/7/2
 * @Modify by:
 */
@Component
public class ModuleCoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCoreService.class);

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductToServiceModuleService productToServiceModuleService;

    @Autowired
    private IServiceModuleService serviceModuleService;

    @Autowired
    private IServiceModuleActionService serviceModuleActionService;

    @Autowired
    private IServiceModuleEventService serviceModuleEventService;

    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;

    @Autowired
    private IServiceToActionService serviceToActionService;

    @Autowired
    private IServiceToEventService serviceToEventService;

    @Autowired
    private IServiceToPropertyService serviceToPropertyService;

    @Autowired
    private IModuleEventToPropertyService moduleEventToPropertyService;

    @Autowired
    private IModuleActionToPropertyService moduleActionToPropertyService;


    @Transactional
    public void copyModule(Long origProductId, Long targetProductId) {

        productToServiceModuleService.copyService(origProductId, targetProductId);
    }
    @Transactional
    public void addOrUpdateModule(Long productId, List<ServiceModuleInfoReq> moduleInfoReqList) {
        if (productId == null) {
            LOGGER.info("productId not null");
            return;
        }
        if (CollectionUtils.isEmpty(moduleInfoReqList)) {
            //清空所有产品对应相关的定义
            productToServiceModuleService.delServicesByProductId(productId);
            return;
        }
        for (ServiceModuleInfoReq moduleInfoReq : moduleInfoReqList) {
            Long parentServiceModuleId = moduleInfoReq.getParentId();
            Long serviceModuleId = moduleInfoReq.getId();
            //添加产品对应的所有功能组+方法+事件+属性
            productToServiceModuleService.addOrUpdateServicesByProductId(productId, serviceModuleId, parentServiceModuleId);

        }
    }

    @Deprecated
    public void updateActionInfo(UpdateActionInfoReq actionInfoReq) {
        AddServiceModuleActionReq actionInfo = actionInfoReq.getActionReq();
        //获取action
        ServiceModuleAction action = serviceModuleActionService.selectById(actionInfo.getId());


        List<AddServiceModulePropertyReq> propertyList = actionInfoReq.getPropertyReqList();

        for (AddServiceModulePropertyReq propertyReq : propertyList) {

            if (propertyReq.getId() == null || propertyReq.getId() <= 0) {
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PROPERTY_NOT_EXIST);
            }
            ServiceModuleProperty property = serviceModulePropertyService.selectById(propertyReq.getId());
            if (property == null) {
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PROPERTY_NOT_EXIST);
            }
            property.setUpdateTime(new Date());
            property.setUpdateBy(propertyReq.getUpdateBy());
            property.setApiLevel(action.getApiLevel());
        }

        /**
         * TODO 修改action
         */
//        serviceModuleActionService.updateAction(actionInfo);
    }

    @Transactional
    public void updateServiceModuleInfo(AddOrUpdateServiceModuleReq serviceModuleReq) {

        Long productId = serviceModuleReq.getProductId();
        if (CollectionUtils.isEmpty(serviceModuleReq.getServiceModuleList())) {
            return;
        }
        //先删除所有的产品对应的功能组、方法、事件、属性
        productToServiceModuleService.delServicesByProductId(productId);

        for (AddServiceModuleReq moduleInfoReq : serviceModuleReq.getServiceModuleList()) {
            //添加产品对应的所有功能组+方法+事件+属性
            productToServiceModuleService.addOrUpdateServicesByProductId(productId, moduleInfoReq);
        }
    }
}
