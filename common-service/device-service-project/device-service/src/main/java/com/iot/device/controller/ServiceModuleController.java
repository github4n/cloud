package com.iot.device.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.model.DeviceTypeToServiceModule;
import com.iot.device.model.ProductToServiceModule;
import com.iot.device.model.ServiceModule;
import com.iot.device.service.IDeviceTypeToServiceModuleService;
import com.iot.device.service.IGenerateModuleAgreementService;
import com.iot.device.service.IGenerateModuleService;
import com.iot.device.service.IProductToServiceModuleService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.service.core.ModuleCoreService;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.GenerateModuleAgreementRsp;
import com.iot.device.vo.rsp.ServiceModuleInfoResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModuleResp;
import com.iot.device.vo.rsp.product.ParentVO;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
public class ServiceModuleController implements ServiceModuleApi {

    @Autowired
    private IServiceModuleService serviceModuleService;
    @Autowired
    private IProductToServiceModuleService productToServiceModuleService;
    @Autowired
    private IDeviceTypeToServiceModuleService deviceTypeToServiceModuleService;
    @Autowired
    private ModuleCoreService moduleCoreService;
    @Autowired
    private IGenerateModuleService iGenerateModuleService;

    @Autowired
    private IGenerateModuleAgreementService iGenerateModuleAgreementService;

    public List<ServiceModuleListResp> findServiceModuleListByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId) {
        AssertUtils.notNull(deviceTypeId, "deviceTypeId.notnull");
        EntityWrapper<DeviceTypeToServiceModule> wrapper = new EntityWrapper<>();
        wrapper.eq("device_type_id", deviceTypeId);
        List<DeviceTypeToServiceModule> deviceTypeToServiceModuleList = deviceTypeToServiceModuleService.selectList(wrapper);
        if (CollectionUtils.isEmpty(deviceTypeToServiceModuleList)) {
            return null;
        }
        Map<Long, DeviceTypeToServiceModule> deviceTypeToServiceModuleMap = Maps.newHashMap();
        List<Long> serviceModuleIds = Lists.newArrayList();
        for (DeviceTypeToServiceModule ds : deviceTypeToServiceModuleList) {
            Long serviceModuleId = ds.getServiceModuleId();
            serviceModuleIds.add(serviceModuleId);
            deviceTypeToServiceModuleMap.put(serviceModuleId, ds);
        }
        List<ServiceModule> moduleList = serviceModuleService.selectBatchIds(serviceModuleIds);
        List<ServiceModuleListResp> moduleListRespList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(moduleList)) {
            for (ServiceModule module : moduleList) {
                deviceTypeToServiceModuleList.forEach(m->{
                    if (module.getId().equals(m.getServiceModuleId())){
                        ServiceModuleListResp target = new ServiceModuleListResp();
                        module.setStatus(m.getStatus());
                        BeanCopyUtils.copyServiceModel(module, target);

                        DeviceTypeToServiceModule  deviceTypeToServiceModule = deviceTypeToServiceModuleMap.get(module.getId());
                        if (deviceTypeToServiceModule!= null) {
                            //    propertyStatus 属性状态，0:可选,1:必选")
                            target.setPropertyStatus(deviceTypeToServiceModule.getStatus());
                        }
                        moduleListRespList.add(target);
                    }
                });
            }
        }
        return moduleListRespList;
    }

    public List<ServiceModuleListResp> findServiceModuleListByParentIdNull() {
        EntityWrapper<ServiceModule> wrapper = new EntityWrapper<>();
        wrapper.eq("parent_id", null);
        List<ServiceModule> moduleList = serviceModuleService.selectList(wrapper);
        List<ServiceModuleListResp> moduleListRespList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(moduleList)) {
            for (ServiceModule module : moduleList) {
                ServiceModuleListResp target = new ServiceModuleListResp();
                BeanCopyUtils.copyServiceModel(module, target);
                moduleListRespList.add(target);
            }
        }
        return moduleListRespList;
    }

    public List<ServiceModuleListResp> findServiceModuleListByProductId(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        EntityWrapper<ProductToServiceModule> productWrapper = new EntityWrapper<>();
        productWrapper.eq("product_id", productId);
        List<ProductToServiceModule> productToServiceModuleList = productToServiceModuleService.selectList(productWrapper);
        if (CollectionUtils.isEmpty(productToServiceModuleList)) {
            return null;
        }
        List<Long> moduleIdList = Lists.newArrayList();
        for (ProductToServiceModule pm : productToServiceModuleList) {
            Long moduleId = pm.getServiceModuleId();
            moduleIdList.add(moduleId);
        }
        EntityWrapper<ServiceModule> moduleWrapper = new EntityWrapper<>();
        moduleWrapper.in("id", moduleIdList);

        List<Long> parentModuleIdList = Lists.newArrayList();
        List<ServiceModuleListResp> moduleListRespList = Lists.newArrayList();
        List<ServiceModule> moduleList = serviceModuleService.selectBatchIds(moduleIdList);
        if (CollectionUtils.isEmpty(moduleList)) {
            return moduleListRespList;
        }
        for (ServiceModule module : moduleList) {
            parentModuleIdList.add(module.getParentId());
            ServiceModuleListResp target = new ServiceModuleListResp();
            BeanCopyUtils.copyServiceModel(module, target);
            moduleListRespList.add(target);
        }
        // parentModule
        List<ServiceModule> parentModuleList = serviceModuleService.selectBatchIds(parentModuleIdList);
        if (CollectionUtils.isEmpty(parentModuleIdList)) {
            return moduleListRespList;
        }
        for (ServiceModuleListResp serviceModuleListResp : moduleListRespList) {
            for (ServiceModule serviceModule : parentModuleList) {
                if (serviceModuleListResp.getParentId().equals(serviceModule.getId())) {
                    ParentVO parentVO = new ParentVO();
                    parentVO.setId(serviceModule.getId());
                    parentVO.setCode(serviceModule.getCode());
                    parentVO.setName(serviceModule.getName());
                    serviceModuleListResp.setParent(parentVO);
                    break;
                }
            }
        }
        return moduleListRespList;
    }

    public ServiceModuleInfoResp getServiceModuleInfoByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        ServiceModule serviceModule = ServiceModuleCacheCoreUtils.getServiceModuleCacheData(serviceModuleId);
        if(serviceModule == null) {
            serviceModule = serviceModuleService.selectById(serviceModuleId);
            if (serviceModule == null) {
                //TODO 防止缓存击穿
                throw new BusinessException(ServiceModuleExceptionEnum.SERVICE_MODULE_NOT_EXIST);
            } else {
                //数据库数据放入缓存
                ServiceModuleCacheCoreUtils.cacheServiceModule(serviceModule);
            }
        }
        ServiceModuleInfoResp target = new ServiceModuleInfoResp();
        BeanCopyUtils.copyServiceModel(serviceModule, target);
        return target;
    }

    public void updateServiceModuleInfo(@RequestBody AddOrUpdateServiceModuleReq serviceModuleReq) {

        moduleCoreService.updateServiceModuleInfo(serviceModuleReq);
    }

    @Override
    public Long saveOrUpdate(@RequestBody ServiceModuleReq serviceModuleReq) {
        return serviceModuleService.saveOrUpdate(serviceModuleReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        serviceModuleService.delete(ids);
    }

    @Override
    public PageInfo<ServiceModuleResp> list(@RequestBody ServiceModuleReq serviceModuleReq) {
        return serviceModuleService.list(serviceModuleReq);
    }

    @Override
    public List<ServiceModuleResp> listByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam("tenantId") Long tenantId) {
        return serviceModuleService.listByDeviceTypeId(deviceTypeId,tenantId);
    }

    @Override
    public List<ServiceModuleResp> listByProductId(@RequestParam("productId") Long productId) {
        return serviceModuleService.listByProductId(productId);
    }

    @Override
    public List<Long> listServiceModuleIdsByProductId(@RequestParam("productId") Long productId) {
        List<Long> moduleIdList = Lists.newArrayList();
        EntityWrapper<ProductToServiceModule> productWrapper = new EntityWrapper<>();
        productWrapper.eq("product_id", productId);
        List<ProductToServiceModule> productToServiceModuleList = productToServiceModuleService.selectList(productWrapper);
        if (CollectionUtils.isEmpty(productToServiceModuleList)) {
            return moduleIdList;
        }
        for (ProductToServiceModule pm : productToServiceModuleList) {
            Long moduleId = pm.getServiceModuleId();
            moduleIdList.add(moduleId);
        }
        return moduleIdList;
    }

    @Override
    public String generateModuleId(@RequestBody GenerateModuleReq generateModuleReq) {
        return iGenerateModuleService.generateModuleId(generateModuleReq);
    }

    @Override
    public PageInfo generateModuleList(@RequestBody GenerateModuleReq generateModuleReq) {
        return iGenerateModuleService.generateModuleList(generateModuleReq);
    }

    @Override
    public List<GenerateModuleAgreementRsp> generateModuleAgreementList() {
        return iGenerateModuleAgreementService.generateModuleAgreementList();
    }

    @Override
    public void copyModule(@RequestParam("sourceDeviceTypeId") Long sourceDeviceTypeId, @RequestParam("targetProductId") Long targetProductId) {
        AssertUtils.notNull(sourceDeviceTypeId, "deviceTypeId.notnull");
        AssertUtils.notNull(targetProductId, "productId.notnull");
        deviceTypeToServiceModuleService.copyService(sourceDeviceTypeId, targetProductId);
    }

    @Override
    public void delModuleByProductId(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        //先删除所有的产品对应的功能组、方法、事件、属性
        productToServiceModuleService.delServicesByProductId(productId);
        SaaSContextHolder.removeCurrentContextMap();
    }
}

