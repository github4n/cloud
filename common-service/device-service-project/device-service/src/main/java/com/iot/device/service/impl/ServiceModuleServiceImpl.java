package com.iot.device.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.mapper.ServiceModuleMapper;
import com.iot.device.model.ProductToServiceModule;
import com.iot.device.model.ServiceModule;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.model.enums.PropertyStatusEnum;
import com.iot.device.service.IDeviceTypeToServiceModuleService;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.service.IProductToServiceModuleService;
import com.iot.device.service.IServiceModuleActionService;
import com.iot.device.service.IServiceModuleEventService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.service.IServiceModuleStyleService;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.service.IServiceToPropertyService;
import com.iot.device.vo.req.AddServiceModuleReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.ServiceModuleResp;
import com.iot.device.vo.rsp.ServiceModuleStyleResp;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 模组表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Component
public class ServiceModuleServiceImpl extends ServiceImpl<ServiceModuleMapper, ServiceModule> implements IServiceModuleService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleServiceImpl.class);
    @Autowired
    private IModuleActionToPropertyService iModuleActionToPropertyService;

    @Autowired
    private IModuleEventToPropertyService iModuleEventToPropertyService;

    @Autowired
    private IServiceToActionService iServiceToActionService;

    @Autowired
    private IServiceToEventService iServiceToEventService;

    @Autowired
    private IServiceToPropertyService iServiceToPropertyService;

    @Autowired
    private IServiceToActionService serviceToActionService;

    @Autowired
    private IServiceToEventService serviceToEventService;

    @Autowired
    private IServiceToPropertyService serviceToPropertyService;

    @Autowired
    private ServiceModuleMapper serviceModuleMapper;

    @Autowired
    private IServiceModuleStyleService iServiceModuleStyleService;

    @Autowired
    private IServiceModuleEventService iServiceModuleEventService;

    @Autowired
    private IServiceModulePropertyService iServiceModulePropertyService;

    @Autowired
    private IServiceModuleActionService iServiceModuleActionService;

    @Autowired
    private IDeviceTypeToServiceModuleService iDeviceTypeToServiceModuleService;

    @Autowired
    private IProductToServiceModuleService iProductToServiceModuleService;

    @Transactional
    public void delServicesByServiceModuleId(Long serviceModuleId) {
        ServiceModuleCacheCoreUtils.removeServiceModuleCache(serviceModuleId);
        if (serviceModuleId == null) {
            LOGGER.info("serviceModuleId not null");
            return;
        }
        ServiceModule serviceModule = super.selectById(serviceModuleId);
        if (serviceModule == null) {
            LOGGER.info("serviceModule not null");
            return;
        }

        //删除模组对应的方法
        serviceToActionService.delActionsByServiceModuleId(serviceModuleId);
        //删除模组对应的事件
        serviceToEventService.delEventsByServiceModuleId(serviceModuleId);
        //删除模组对应的属性
        serviceToPropertyService.delPropertiesByServiceModuleId(serviceModuleId);
        //删除模组
        super.deleteById(serviceModuleId);
    }

    @Transactional
    public ServiceModule addOrUpdateServiceModule(Long serviceModuleId, Long parentServiceModuleId) {
        ServiceModule serviceModule = super.selectById(serviceModuleId);
        if (serviceModule == null) {
            LOGGER.info("serviceModule not exist");
            throw new BusinessException(ServiceModuleExceptionEnum.SERVICE_MODULE_NOT_EXIST);
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        if (serviceModule.getParentId() == null) {
            //first insert 对应产品的功能需要拷贝一份存储
            ServiceModule tempServiceModule = new ServiceModule();
            BeanUtils.copyProperties(serviceModule, tempServiceModule);
            tempServiceModule.setParentId(serviceModuleId);
            tempServiceModule.setId(null);
            tempServiceModule.setCreateBy(userId);
            tempServiceModule.setCreateTime(new Date());
            tempServiceModule.setTenantId(tenantId);
            super.insert(tempServiceModule);

            Long origServiceModuleId = serviceModuleId;
            Long targetServiceModuleId = tempServiceModule.getId();
            serviceToActionService.addOrUpdateActionsByServiceModuleId(origServiceModuleId, targetServiceModuleId);
            serviceToEventService.addOrUpdateEventsByServiceModuleId(origServiceModuleId, targetServiceModuleId);
            serviceToPropertyService.addOrUpdatePropertiesByServiceModuleId(origServiceModuleId, targetServiceModuleId);
            return tempServiceModule;
        } else {
            if (serviceModule.getParentId().compareTo(parentServiceModuleId) != 0) {
                //两者对应的parentId 不匹配 说明数据异常被篡改
                LOGGER.info("parentId not null");
                throw new BusinessException(ServiceModuleExceptionEnum.SERVICE_MODULE_PARENT_ID_DISTORT);
            } else {
                LOGGER.info("已经添加----不做替换删除等");
            }
        }
        return serviceModule;
    }

    @Transactional
    public ServiceModule addOrUpdateServiceModule(AddServiceModuleReq moduleReq) {
        if (CollectionUtils.isEmpty(moduleReq.getActionList())
                && CollectionUtils.isEmpty(moduleReq.getEventList()) && CollectionUtils.isEmpty(moduleReq.getPropertyList())) {
            //忽略功能组未对应事件方法属性为空的 不做任何处理
            return null;
        }
        Long parentServiceModuleId;
        ServiceModule origServiceModule;
        if (moduleReq.getParentServiceModuleId() == null) {
            origServiceModule = super.selectById(moduleReq.getServiceModuleId());
            if (origServiceModule == null) {
                LOGGER.info("serviceModule not exist.{}", moduleReq.getServiceModuleId());
                throw new BusinessException(ServiceModuleExceptionEnum.SERVICE_MODULE_NOT_EXIST);
            }
            parentServiceModuleId = origServiceModule.getId();
        } else {
            origServiceModule = super.selectById(moduleReq.getParentServiceModuleId());
            if (origServiceModule == null) {
                LOGGER.info("serviceModule not exist.{}", moduleReq.getParentServiceModuleId());
                throw new BusinessException(ServiceModuleExceptionEnum.SERVICE_MODULE_NOT_EXIST);
            }
            parentServiceModuleId = origServiceModule.getId();
        }
//
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        //first insert 对应产品的功能需要拷贝一份存储
        ServiceModule tempServiceModule = new ServiceModule();
        BeanUtils.copyProperties(origServiceModule, tempServiceModule);

        tempServiceModule.setParentId(parentServiceModuleId);
        tempServiceModule.setId(null);
        tempServiceModule.setCreateBy(userId);
        tempServiceModule.setCreateTime(new Date());
        tempServiceModule.setTenantId(tenantId);
        super.insert(tempServiceModule);

        Long origServiceModuleId = parentServiceModuleId;
        //后面的添加对应方法事件 都copy 到 targetServiceModuleId
        Long targetServiceModuleId = tempServiceModule.getId();

        serviceToActionService.addOrUpdateActionsByServiceModuleId(origServiceModuleId, targetServiceModuleId, moduleReq.getActionList());
        serviceToEventService.addOrUpdateEventsByServiceModuleId(origServiceModuleId, targetServiceModuleId, moduleReq.getEventList());
        serviceToPropertyService.addOrUpdatePropertiesByServiceModuleId(origServiceModuleId, targetServiceModuleId, moduleReq.getPropertyList());
        return tempServiceModule;
    }

    @Transactional
    public Long copyServiceModule(Long serviceModuleId) {
        if (serviceModuleId == null) {
            LOGGER.info("copyServiceModule  serviceModuleId not exist");
            return null;
        }
        ServiceModule origServiceModule = super.selectById(serviceModuleId);
        if (origServiceModule == null) {
            LOGGER.info("serviceModule not exist");
            return null;
        }
        Long parentServiceModuleId;
        if (origServiceModule.getParentId() == null) {
            parentServiceModuleId = origServiceModule.getId();
        } else {
            parentServiceModuleId = origServiceModule.getParentId();
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        //first insert 对应产品的功能需要拷贝一份存储
        ServiceModule tempServiceModule = new ServiceModule();
        BeanUtils.copyProperties(origServiceModule, tempServiceModule);

        tempServiceModule.setParentId(parentServiceModuleId);
        tempServiceModule.setId(null);
        tempServiceModule.setCreateBy(userId);
        tempServiceModule.setCreateTime(new Date());
        tempServiceModule.setTenantId(tenantId);
        super.insert(tempServiceModule);

        Long origServiceModuleId = parentServiceModuleId;
        //后面的添加对应方法事件 都copy 到 targetServiceModuleId
        Long targetServiceModuleId = tempServiceModule.getId();

        serviceToActionService.addOrUpdateActionsByServiceModuleId(origServiceModuleId, targetServiceModuleId);
        serviceToEventService.addOrUpdateEventsByServiceModuleId(origServiceModuleId, targetServiceModuleId);
        serviceToPropertyService.addOrUpdatePropertiesByServiceModuleId(origServiceModuleId, targetServiceModuleId);
        return targetServiceModuleId;
    }

    @Override
    public Long saveOrUpdate(ServiceModuleReq serviceModuleReq) {
        ServiceModule serviceModule = null;
        if (serviceModuleReq.getId() != null && serviceModuleReq.getId() > 0) {
            //执行修改操作，删除缓存
            ServiceModuleCacheCoreUtils.removeServiceModuleCache(serviceModuleReq.getId());
            serviceModule = super.selectById(serviceModuleReq.getId());
            if (serviceModule == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            serviceModule.setUpdateTime(new Date());
            serviceModule.setUpdateBy(serviceModuleReq.getUpdateBy());
        } else {
            serviceModule = new ServiceModule();
            serviceModule.setUpdateTime(new Date());
            serviceModule.setUpdateBy(serviceModuleReq.getUpdateBy());
            serviceModule.setCreateTime(new Date());
            serviceModule.setCreateBy(serviceModuleReq.getCreateBy());
        }
        serviceModule.setParentId(null);
        serviceModule.setTenantId(serviceModuleReq.getTenantId());
        serviceModule.setVersion(serviceModuleReq.getVersion());
        serviceModule.setName(serviceModuleReq.getName());
        serviceModule.setCode(serviceModuleReq.getCode());
//        if (serviceModuleReq.getPropertyStatus()==0){
//            serviceModule.setPropertyStatus(PropertyStatusEnum.OPTIONAL);
//        } else {
//            serviceModule.setPropertyStatus(PropertyStatusEnum.MANDATORY);
//        }
        if(serviceModuleReq.getDevelopStatus() != null) {
            if (serviceModuleReq.getDevelopStatus() == 0) {
                serviceModule.setDevelopStatus(DevelopStatusEnum.IDLE);
            } else if (serviceModuleReq.getDevelopStatus() == 1) {
                serviceModule.setDevelopStatus(DevelopStatusEnum.OPERATING);
            } else {
                serviceModule.setDevelopStatus(DevelopStatusEnum.OPERATED);
            }
        }
        serviceModule.setTestCase(serviceModuleReq.getTestCase());
        serviceModule.setDescription(serviceModuleReq.getDescription());
        serviceModule.setImg(serviceModuleReq.getImg());
        serviceModule.setChangeImg(serviceModuleReq.getChangeImg());
        serviceModule.setComponentType(serviceModuleReq.getComponentType());
        super.insertOrUpdate(serviceModule);
        return serviceModule.getId();
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if(!CollectionUtils.isEmpty(ids)) {
            //检查是否被关联
            ids.forEach(serviceModuleId -> {
                EntityWrapper wrapper = new EntityWrapper<>();
                wrapper.eq("service_module_id", serviceModuleId);
                int productCount = iProductToServiceModuleService.selectCount(wrapper);
                int deviceTypeCount = iDeviceTypeToServiceModuleService.selectCount(wrapper);
                if (productCount > 0 || deviceTypeCount > 0) {
                    throw new BusinessException(ServiceModuleExceptionEnum.SERVICE_MODULE_IS_USED);
                }
            });
            /**
             * 刪除緩存 service_module,service_to_action,service_to_event,service_to_property
             */
            Long tenantId =  SaaSContextHolder.currentTenantId();
            ServiceModuleCacheCoreUtils.batchRemoveServiceModuleCache(ids);
            ServiceModuleCacheCoreUtils.batchRemoveServiceToActionByModule(ids,tenantId);
            ServiceModuleCacheCoreUtils.batchRemoveServiceToEventByModule(ids,tenantId);
            ServiceModuleCacheCoreUtils.batchRemoveServiceToPropertyByModule(ids,tenantId);
            super.deleteBatchIds(ids);
            ArrayList<Long> idsResult = new ArrayList<Long>();
            List<ServiceModuleStyleResp> list = iServiceModuleStyleService.list(ids);
            list.forEach(m -> {
                idsResult.add(m.getId());
            });
            iServiceModuleStyleService.delete(idsResult);
            idsResult.clear();

            iServiceToActionService.listByServiceModuleId(ids).forEach(n -> {
                idsResult.add(n.getId());
            });
            iServiceToActionService.delete(idsResult);
            idsResult.clear();

            iServiceToEventService.listByServiceModuleId(ids).forEach(b -> {
                idsResult.add(b.getId());
            });
            iServiceToEventService.delete(idsResult);
            idsResult.clear();

            iServiceToPropertyService.listByServiceModuleId(ids).forEach(v -> {
                idsResult.add(v.getId());
            });
            iServiceToPropertyService.delete(idsResult);
            idsResult.clear();
        }
    }

    @Override
    public PageInfo<ServiceModuleResp> list(ServiceModuleReq serviceModuleReq) {
        Page<ServiceModuleAction> page = new Page<>(CommonUtil.getPageNum(serviceModuleReq),CommonUtil.getPageSize(serviceModuleReq));
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.isNull("parent_id");
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        if (StringUtils.isNotEmpty(serviceModuleReq.getSearchValues())){
            wrapper.andNew(true, "")
                    .like("name", serviceModuleReq.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("code", serviceModuleReq.getSearchValues(), SqlLike.DEFAULT);
        }
        if (serviceModuleReq.getFilterIds()!=null || serviceModuleReq.getFilterIds().size()>0){
            wrapper.notIn("id",serviceModuleReq.getFilterIds());
        }
        wrapper.orderDesc(Arrays.asList("create_time"));
        List<ServiceModule> list = serviceModuleMapper.selectPage(page,wrapper);
        List<ServiceModuleResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModuleResp serviceModuleResp = new ServiceModuleResp();
            serviceModuleResp.setId(m.getId());
            serviceModuleResp.setParentId(m.getParentId());
            serviceModuleResp.setTenantId(m.getTenantId());
            serviceModuleResp.setVersion(m.getVersion());
            serviceModuleResp.setName(m.getName());
            serviceModuleResp.setCode(m.getCode());
            serviceModuleResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModuleResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
            serviceModuleResp.setTestCase(m.getTestCase());
            serviceModuleResp.setCreateBy(m.getCreateBy());
            serviceModuleResp.setCreateTime(m.getCreateTime());
            serviceModuleResp.setUpdateBy(m.getUpdateBy());
            serviceModuleResp.setUpdateTime(m.getUpdateTime());
            serviceModuleResp.setDescription(m.getDescription());
            serviceModuleResp.setImg(m.getImg());
            serviceModuleResp.setChangeImg(m.getChangeImg());
            serviceModuleResp.setComponentType(m.getComponentType());
            respList.add(serviceModuleResp);
        });
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(respList);
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNum(serviceModuleReq.getPageNum());
        return pageInfo;
    }

    @Override
    public List<ServiceModuleResp> listByDeviceTypeId(Long deviceTypeId,Long tenantId) {
        List<ServiceModule> list = serviceModuleMapper.listByDeviceTypeId(deviceTypeId,tenantId);
        List<ServiceModuleResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModuleResp serviceModuleResp = new ServiceModuleResp();
            serviceModuleResp.setId(m.getId());
            serviceModuleResp.setParentId(m.getParentId());
            serviceModuleResp.setTenantId(m.getTenantId());
            serviceModuleResp.setVersion(m.getVersion());
            serviceModuleResp.setName(m.getName());
            serviceModuleResp.setCode(m.getCode());
            serviceModuleResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModuleResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
            serviceModuleResp.setTestCase(m.getTestCase());
            serviceModuleResp.setCreateBy(m.getCreateBy());
            serviceModuleResp.setCreateTime(m.getCreateTime());
            serviceModuleResp.setUpdateBy(m.getUpdateBy());
            serviceModuleResp.setUpdateTime(m.getUpdateTime());
            serviceModuleResp.setDescription(m.getDescription());
            serviceModuleResp.setImg(m.getImg());
            serviceModuleResp.setChangeImg(m.getChangeImg());
            serviceModuleResp.setOtherId(m.getOtherId());
            serviceModuleResp.setComponentType(m.getComponentType());
            serviceModuleResp.setStatus(m.getStatus());
            respList.add(serviceModuleResp);
        });
        return respList;
    }

    @Override
    public List<ServiceModuleResp> listByProductId(Long productId) {
        //List<ServiceModule> list = serviceModuleMapper.listByProductId(productId);
        List<ServiceModule> list = listServiceModuleByProductId(productId);
        List<ServiceModuleResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModuleResp serviceModuleResp = new ServiceModuleResp();
            serviceModuleResp.setId(m.getId());
            serviceModuleResp.setParentId(m.getParentId());
            serviceModuleResp.setTenantId(m.getTenantId());
            serviceModuleResp.setVersion(m.getVersion());
            serviceModuleResp.setName(m.getName());
            serviceModuleResp.setCode(m.getCode());
            serviceModuleResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModuleResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
            serviceModuleResp.setTestCase(m.getTestCase());
            serviceModuleResp.setCreateBy(m.getCreateBy());
            serviceModuleResp.setCreateTime(m.getCreateTime());
            serviceModuleResp.setUpdateBy(m.getUpdateBy());
            serviceModuleResp.setUpdateTime(m.getUpdateTime());
            serviceModuleResp.setDescription(m.getDescription());
            serviceModuleResp.setImg(m.getImg());
            serviceModuleResp.setChangeImg(m.getChangeImg());
            serviceModuleResp.setOtherId(m.getOtherId());
            serviceModuleResp.setComponentType(m.getComponentType());
            respList.add(serviceModuleResp);
        });
        return respList;
    }

    public List<ServiceModule> listServiceModuleByProductId(Long productId){
        List<ServiceModule> result = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        productIds.add(productId);
        List<ProductToServiceModule> productToServiceModuleList = iProductToServiceModuleService.listByProductIds(productIds);
        if(productToServiceModuleList != null && productToServiceModuleList.size() >0){
            Map<Long,ProductToServiceModule> productToServiceModuleMap = productToServiceModuleList.stream().collect(Collectors.toMap(ProductToServiceModule::getServiceModuleId, productToServiceModule -> productToServiceModule, (k1, k2) -> k1));
            List<Long> serviceModuleIds = productToServiceModuleList.stream().map(ProductToServiceModule::getServiceModuleId).collect(Collectors.toList());
            List<ServiceModule> serviceModules = listServiceModuleByIds(serviceModuleIds);
            serviceModules.forEach(serviceModule -> {
                ProductToServiceModule productToServiceModule = productToServiceModuleMap.get(serviceModule.getId());
                if(productToServiceModule != null){
                    serviceModule.setOtherId(productToServiceModule.getId());
                }
            });
        }
        return result;
    }

    private List<ServiceModule> listServiceModuleByIds(List<Long> ids){
        List<ServiceModule> result = new ArrayList<>();
        List<Long> noCacheServiceModuleIds = new ArrayList<>();
        List<ServiceModule> cacheServiceModules = listServiceModuleByIdsFromCache(ids);
        if(cacheServiceModules != null && cacheServiceModules.size() >0){
            noCacheServiceModuleIds.addAll(getNoCacheServiceModuleIds(ids,cacheServiceModules));
            result.addAll(cacheServiceModules);
        }else{
            noCacheServiceModuleIds = ids;
        }
        if(noCacheServiceModuleIds != null && noCacheServiceModuleIds.size() >0){
            result.addAll(listServiceModuleByIdsFromSql(noCacheServiceModuleIds));
        }
        return result;
    }

    private List<ServiceModule> listServiceModuleByIdsFromCache(List<Long> ids){
        return ServiceModuleCacheCoreUtils.batchGetServiceModule(ids);
    }

    private List<ServiceModule> listServiceModuleByIdsFromSql(List<Long> ids){
        List<ServiceModule> list = new ArrayList<>();
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.in("id",ids);
        list =   super.selectList(entityWrapper);
        if(list != null && list.size()>0){
            ServiceModuleCacheCoreUtils.batchSetServiceModule(list);
        }
        return list;
    }

    private List<Long> getNoCacheServiceModuleIds(List<Long> ids,List<ServiceModule> cacheServiceModules){
        Map<Long, List<ServiceModule>> cacheServiceModuleGroup = cacheServiceModules.stream().collect(Collectors.groupingBy(ServiceModule::getId));
        Set<Long> cacheServiceModuleIds = cacheServiceModuleGroup.keySet();
        List<Long> noCacheServiceModuleIds = ids.stream().filter(id -> !cacheServiceModuleIds.contains(id)).collect(Collectors.toList());
        return noCacheServiceModuleIds;
    }
}
