package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.mapper.*;
import com.iot.device.model.*;
import com.iot.device.service.IProductToServiceModuleService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.vo.req.AddServiceModuleReq;
import com.iot.device.vo.req.ProductToServiceModuleReq;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.EventResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品对应模组表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ProductToServiceModuleServiceImpl extends ServiceImpl<ProductToServiceModuleMapper, ProductToServiceModule> implements IProductToServiceModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductToServiceModuleServiceImpl.class);

    @Autowired
    private IServiceModuleService serviceModuleService;

    @Autowired
    private ProductToServiceModuleMapper productToServiceModuleMapper;

    @Autowired
    private ServiceModuleActionMapper serviceModuleActionMapper;

    @Autowired
    private ServiceModuleEventMapper serviceModuleEventMapper;

    @Autowired
    private ServiceModulePropertyMapper serviceModulePropertyMapper;

    @Autowired
    private ModuleActionToPropertyMapper moduleActionToPropertyMapper;

    @Autowired
    private ModuleEventToPropertyMapper moduleEventToPropertyMapper;

    @Transactional
    public void addOrUpdateServicesByProductId(Long productId, Long serviceModuleId, Long parentServiceModuleId) {
        ServiceModuleCacheCoreUtils.removeProductToServiceModuleCache(productId);
        if (productId == null) {
            LOGGER.info("productId not null");
            return;
        }
        if (serviceModuleId == null) {
            LOGGER.info("serviceModuleId not null");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        EntityWrapper<ProductToServiceModule> wrapper = new EntityWrapper<>();
        wrapper.eq("product_id", productId);
        wrapper.eq("service_module_id", serviceModuleId);
        ProductToServiceModule whetherProductToServiceModule = super.selectOne(wrapper);
        if (whetherProductToServiceModule != null) {
            //说明已经添加了 产品和模组对应的关系
            LOGGER.info("serviceModuleId already exist.");
            return;
        }
        //添加模组
        ServiceModule serviceModule = serviceModuleService.addOrUpdateServiceModule(serviceModuleId, parentServiceModuleId);
        if (serviceModule == null) {
            LOGGER.info("serviceModule  add error. null");
            return;
        }

        Long targetServiceModuleId = serviceModule.getId();
        ProductToServiceModule productToServiceModule = new ProductToServiceModule();
        productToServiceModule.setProductId(productId);
        productToServiceModule.setServiceModuleId(targetServiceModuleId);
        productToServiceModule.setCreateBy(userId);
        productToServiceModule.setTenantId(tenantId);
        productToServiceModule.setCreateTime(new Date());
        super.insert(productToServiceModule);
    }

    @Transactional
    public void addOrUpdateServicesByProductId(Long productId, AddServiceModuleReq moduleReq) {
        ServiceModuleCacheCoreUtils.removeProductToServiceModuleCache(productId);
        //添加功能组
        ServiceModule serviceModule = serviceModuleService.addOrUpdateServiceModule(moduleReq);
        if (serviceModule == null) {
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        Long targetServiceModuleId = serviceModule.getId();
        ProductToServiceModule productToServiceModule = new ProductToServiceModule();
        productToServiceModule.setProductId(productId);
        productToServiceModule.setServiceModuleId(targetServiceModuleId);
        productToServiceModule.setCreateBy(userId);
        productToServiceModule.setTenantId(tenantId);
        productToServiceModule.setCreateTime(new Date());
        super.insert(productToServiceModule);
    }

    @Transactional
    public void delServicesByProductId(Long productId) {
        ServiceModuleCacheCoreUtils.removeProductToServiceModuleCache(productId);
        if (productId == null) {
            LOGGER.info("delServicesByProductId.error.productId null.");
            return;
        }
        EntityWrapper<ProductToServiceModule> wrapper = new EntityWrapper<>();
        wrapper.eq("product_id", productId);
        List<ProductToServiceModule> serviceModuleList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceModuleList)) {
            LOGGER.info("未为产品定义相关模组功能");
            return;
        }
        for (ProductToServiceModule ps : serviceModuleList) {
            Long serviceModuleId = ps.getServiceModuleId();
            //删除模组定义信息
            serviceModuleService.delServicesByServiceModuleId(serviceModuleId);
            //删除 产品和功能模组
            super.deleteById(ps.getId());
        }
    }

    @Transactional
    public void copyService(Long origProductId, Long targetProductId) {
        ServiceModuleCacheCoreUtils.removeProductToServiceModuleCache(targetProductId);
        if (origProductId == null) {
            LOGGER.info("copyService.origProductId null.");
            return;
        }
        if (targetProductId == null) {
            LOGGER.info("copyService.targetProductId null.");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        //先删除所有的产品对应的功能组、方法、事件、属性
        this.delServicesByProductId(targetProductId);

        //添加功能组
        EntityWrapper<ProductToServiceModule> wrapper = new EntityWrapper<>();
        wrapper.eq("product_id", origProductId);
        List<ProductToServiceModule> serviceModuleList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceModuleList)) {
            LOGGER.info("未为产品定义相关模组功能");
            return;
        }
        for (ProductToServiceModule ps : serviceModuleList) {
            Long serviceModuleId = ps.getServiceModuleId();
            Long targetServiceModuleId = serviceModuleService.copyServiceModule(serviceModuleId);
            if (targetServiceModuleId == null) {
                continue;
            }
            ProductToServiceModule productToServiceModule = new ProductToServiceModule();
            productToServiceModule.setProductId(targetProductId);
            productToServiceModule.setServiceModuleId(targetServiceModuleId);
            productToServiceModule.setCreateBy(userId);
            productToServiceModule.setTenantId(tenantId);
            productToServiceModule.setCreateTime(new Date());
            super.insert(productToServiceModule);
        }
    }

    @Override
    public void save(ProductToServiceModuleReq productToServiceModuleReq) {
        ServiceModuleCacheCoreUtils.removeProductToServiceModuleCache(productToServiceModuleReq.getProductId());
        ProductToServiceModule productToServiceModule = new ProductToServiceModule();
        productToServiceModule.setTenantId(productToServiceModuleReq.getTenantId());
        productToServiceModule.setProductId(productToServiceModuleReq.getProductId());
        productToServiceModule.setServiceModuleId(productToServiceModuleReq.getServiceModuleId());
        productToServiceModule.setCreateBy(productToServiceModuleReq.getCreateBy());
        productToServiceModule.setCreateTime(new Date());
        productToServiceModule.setUpdateBy(productToServiceModuleReq.getUpdateBy());
        productToServiceModule.setUpdateTime(new Date());
        super.insert(productToServiceModule);
    }

    @Override
    public void saveMore(ProductToServiceModuleReq productToServiceModuleReq) {
        ServiceModuleCacheCoreUtils.removeProductToServiceModuleCache(productToServiceModuleReq.getProductId());
        List<ProductToServiceModule> list = new ArrayList<>();
        productToServiceModuleReq.getServiceModuleIds().forEach(m->{
            ProductToServiceModule productToServiceModule = new ProductToServiceModule();
            productToServiceModule.setTenantId(productToServiceModuleReq.getTenantId());
            productToServiceModule.setProductId(productToServiceModuleReq.getProductId());
            productToServiceModule.setServiceModuleId(Long.valueOf(m.toString()));
            productToServiceModule.setCreateBy(productToServiceModuleReq.getCreateBy());
            productToServiceModule.setCreateTime(new Date());
            productToServiceModule.setUpdateBy(productToServiceModuleReq.getUpdateBy());
            productToServiceModule.setUpdateTime(new Date());
            list.add(productToServiceModule);
        });
        super.insertBatch(list);

    }

    @Override
    public void delete(ArrayList<Long> ids) {
        deleteProductToServiceModuleCacheByIds(ids);
        super.deleteBatchIds(ids);
    }

    private void deleteProductToServiceModuleCacheByIds(List<Long> ids){
        List<ProductToServiceModule> list = new ArrayList<>();
        EntityWrapper entityWrapper = new EntityWrapper<>();
        entityWrapper.in("id",ids);
        list = super.selectList(entityWrapper);
        if(list != null && list.size() >0){
            Map<Long, List<ProductToServiceModule>> productToServiceModuleGroup = list.stream().collect(Collectors.groupingBy(ProductToServiceModule::getProductId));
            List<Long> productIds = new ArrayList<>(productToServiceModuleGroup.keySet());
            ServiceModuleCacheCoreUtils.batchRemoveProductToServiceModuleCache(productIds);
        }
    }

    /**
     * @despriction：校验产品是否有支持iftttType,并有至少一个iftttType属性、方法、事件
     * @author  yeshiyuan
     * @created 2018/11/22 14:00
     */
    @Override
    public boolean checkProductHadIftttType(Long productId) {
        List<Long> serviceModuleIds = productToServiceModuleMapper.findServiceModuleIdByProductId(productId);
        if (serviceModuleIds == null || serviceModuleIds.isEmpty()) {
            return false;
        }
        List<ServiceModuleAction> actions = serviceModuleActionMapper.supportIftttAction(serviceModuleIds);
        if ( actions!=null && !actions.isEmpty()) {
            return true;
        }
        List<ServiceModuleProperty> properties = serviceModulePropertyMapper.supportIftttProperty(serviceModuleIds);
        if (properties!=null && !properties.isEmpty()){
            return true;
        }
        List<ServiceModuleEvent> events = serviceModuleEventMapper.supportIftttEvent(serviceModuleIds);
        if (events != null && !events.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(Long productId, String iftttType) {
        PackageServiceModuleDetailResp detailResp = new PackageServiceModuleDetailResp();
        List<Long> serviceModuleIds = productToServiceModuleMapper.findServiceModuleIdByProductId(productId);
        if (serviceModuleIds == null || serviceModuleIds.isEmpty()) {
            return detailResp;
        }
        if ("if".equals(iftttType)) {
            //只查询事件、属性支持if/ifthen
            List<PropertyResp> propertyResps = serviceModulePropertyMapper.queryDetailByModuleIdAndIfttt(serviceModuleIds, iftttType);
            detailResp.setProperties(propertyResps);
            List<EventResp> events = serviceModuleEventMapper.queryDetailByModuleIdAndIfttt(serviceModuleIds, iftttType);
            if (events!=null && !events.isEmpty()) {
                events.forEach(eventResp -> {
                    List<Long> propertyIds = moduleEventToPropertyMapper.getModulePropertyIds(eventResp.getId());
                    if (propertyIds!=null && !propertyIds.isEmpty()) {
                        eventResp.setProperties(serviceModulePropertyMapper.queryDetailByIdAndIfttt(propertyIds, iftttType));
                    }
                });
            }
            detailResp.setEvents(events);
        } else if ("then".equals(iftttType)){
            //只查询方法、属性支持then/ifthen
            List<PropertyResp> propertyResps = serviceModulePropertyMapper.queryDetailByModuleIdAndIfttt(serviceModuleIds, iftttType);
            detailResp.setProperties(propertyResps);
            List<ActionResp> actions = serviceModuleActionMapper.queryDetailByModuleIdAndIfttt(serviceModuleIds, iftttType);
            if (actions != null && !actions.isEmpty()) {
                actions.forEach(actionResp -> {
                    List<Long> propertyIds = moduleActionToPropertyMapper.getModulePropertyIds(actionResp.getId());
                    if (propertyIds!=null && !propertyIds.isEmpty()) {
                        actionResp.setProperties(serviceModulePropertyMapper.queryDetailByIdAndIfttt(propertyIds, iftttType));
                    }
                });
            }
            detailResp.setActions(actions);
        }
        return detailResp;
    }

    @Override
    public List<ProductToServiceModule> listByProductIds(List<Long> productIds){
        List<ProductToServiceModule> result = new ArrayList<>();
        List<Long> noCacheProductIds = new ArrayList<>();
        List<ProductToServiceModule> cacheDataList = listByProductIdsFromSqlFromCache(productIds);
        if(cacheDataList !=null&& cacheDataList.size()>0){
            noCacheProductIds = getNoCacheProductToServiceModuleProductIds(productIds,cacheDataList);
            result.addAll(cacheDataList);
        }else{
            noCacheProductIds = productIds;
        }
        if(noCacheProductIds != null && noCacheProductIds.size() >0){
            List<ProductToServiceModule> dataListFromSql = listByProductIdsFromSql(noCacheProductIds);
            result.addAll(dataListFromSql);
        }
        return result;
    }

    public List<ProductToServiceModule> listByProductIdsFromSqlFromCache(List<Long> productIds){
        List<ProductToServiceModule> result = new ArrayList<>();
        productIds.forEach(productId->{
            List<ProductToServiceModule> cacheList = ServiceModuleCacheCoreUtils.getProductToServiceModuleByProductIdCache(productId);
            if(cacheList != null && cacheList.size()>0){
                result.addAll(cacheList);
            }
        });
        return result;
    }

    public List<Long> getNoCacheProductToServiceModuleProductIds(List<Long> productIds,List<ProductToServiceModule> cacheDataList){
        Map<Long, List<ProductToServiceModule>> productToServiceModuleGroup = cacheDataList.stream().collect(Collectors.groupingBy(ProductToServiceModule::getProductId));
        Set<Long> cacheProductIds = productToServiceModuleGroup.keySet();
        List<Long> noCacheProductIds = productIds.stream().filter(id -> !cacheProductIds.contains(id)).collect(Collectors.toList());
        return noCacheProductIds;
    }

    public List<ProductToServiceModule> listByProductIdsFromSql(List<Long> productIds){
        List<ProductToServiceModule> list = new ArrayList<>();
        EntityWrapper entityWrapper = new EntityWrapper<>();
        entityWrapper.in("product_id",productIds);
        list = super.selectList(entityWrapper);
        if(list != null && list.size() >0){
            Map<Long, List<ProductToServiceModule>> productToServiceModuleGroup = list.stream().collect(Collectors.groupingBy(ProductToServiceModule::getProductId));
            productToServiceModuleGroup.forEach((k, v) -> {
                ServiceModuleCacheCoreUtils.cacheProductToServiceModuleByProductId( k, v);
            });
        }
        return list;
    }
}
