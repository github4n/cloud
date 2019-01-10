package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.exception.BusinessException;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.mapper.DeviceTypeToServiceModuleMapper;
import com.iot.device.mapper.ModuleActionToPropertyMapper;
import com.iot.device.mapper.ModuleEventToPropertyMapper;
import com.iot.device.mapper.ServiceModuleActionMapper;
import com.iot.device.mapper.ServiceModuleEventMapper;
import com.iot.device.mapper.ServiceModulePropertyMapper;
import com.iot.device.model.DeviceTypeToServiceModule;
import com.iot.device.model.ProductToServiceModule;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.ServiceModuleEvent;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.service.IDeviceTypeToServiceModuleService;
import com.iot.device.service.IProductToServiceModuleService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.vo.req.DeviceTypeToServiceModuleReq;
import com.iot.device.vo.rsp.DeviceTypeToServiceModuleResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型对应模组表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-07-03
 */
@Service
public class DeviceTypeToServiceModuleServiceImpl extends ServiceImpl<DeviceTypeToServiceModuleMapper, DeviceTypeToServiceModule> implements IDeviceTypeToServiceModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTypeToServiceModuleServiceImpl.class);


    @Autowired
    private IServiceModuleService serviceModuleService;

    @Autowired
    private IProductToServiceModuleService productToServiceModuleService;

    @Autowired
    private DeviceTypeToServiceModuleMapper deviceTypeToServiceModuleMapper;

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

    @Override
    public Long saveOrUpdate(DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {
        DeviceTypeToServiceModule deviceTypeToServiceModule = null;
        if (deviceTypeToServiceModuleReq.getId() != null && deviceTypeToServiceModuleReq.getId() > 0) {
            deviceTypeToServiceModule = super.selectById(deviceTypeToServiceModuleReq.getId());
            if (deviceTypeToServiceModule == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            deviceTypeToServiceModule.setUpdateTime(new Date());
            deviceTypeToServiceModule.setUpdateBy(deviceTypeToServiceModuleReq.getUpdateBy());
        } else {
            deviceTypeToServiceModule = new DeviceTypeToServiceModule();
            deviceTypeToServiceModule.setUpdateTime(new Date());
            deviceTypeToServiceModule.setUpdateBy(deviceTypeToServiceModuleReq.getUpdateBy());
            deviceTypeToServiceModule.setCreateTime(new Date());
            deviceTypeToServiceModule.setCreateBy(deviceTypeToServiceModuleReq.getCreateBy());
        }
        deviceTypeToServiceModule.setTenantId(deviceTypeToServiceModuleReq.getTenantId());
        deviceTypeToServiceModule.setDeviceTypeId(deviceTypeToServiceModuleReq.getDeviceTypeId());
        deviceTypeToServiceModule.setServiceModuleId(deviceTypeToServiceModuleReq.getServiceModuleId());
        deviceTypeToServiceModule.setStatus(deviceTypeToServiceModuleReq.getStatus());
        super.insertOrUpdate(deviceTypeToServiceModule);
        return deviceTypeToServiceModule.getId();
    }

    @Override
    public void saveMore(DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {
        List<DeviceTypeToServiceModule> list = new ArrayList();
        Map<String, String> map = deviceTypeToServiceModuleReq.getServiceModuleIds();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            DeviceTypeToServiceModule deviceTypeToServiceModule = new DeviceTypeToServiceModule();
            deviceTypeToServiceModule.setTenantId(deviceTypeToServiceModuleReq.getTenantId());
            deviceTypeToServiceModule.setDeviceTypeId(deviceTypeToServiceModuleReq.getDeviceTypeId());
            deviceTypeToServiceModule.setServiceModuleId(Long.valueOf(entry.getKey()));
            deviceTypeToServiceModule.setCreateBy(deviceTypeToServiceModuleReq.getCreateBy());
            deviceTypeToServiceModule.setCreateTime(new Date());
            deviceTypeToServiceModule.setUpdateBy(deviceTypeToServiceModuleReq.getUpdateBy());
            deviceTypeToServiceModule.setUpdateTime(new Date());
            deviceTypeToServiceModule.setStatus(Integer.parseInt(entry.getValue()));
            list.add(deviceTypeToServiceModule);
        }
        super.insertBatch(list);
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        super.deleteBatchIds(ids);
}

    @Override
    public List<DeviceTypeToServiceModuleResp> listByDeviceTypeId(Long deviceTypeId,Long tenantId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",tenantId);
        wrapper.eq("device_type_id",deviceTypeId);
        List<DeviceTypeToServiceModule> list = super.selectList(wrapper);
        List<DeviceTypeToServiceModuleResp> respList = new ArrayList<>();
        list.forEach(m->{
            DeviceTypeToServiceModuleResp deviceTypeToServiceModuleResp = new DeviceTypeToServiceModuleResp();
            deviceTypeToServiceModuleResp.setId(m.getId());
            deviceTypeToServiceModuleResp.setTenantId(m.getTenantId());
            deviceTypeToServiceModuleResp.setDeviceTypeId(m.getDeviceTypeId());
            deviceTypeToServiceModuleResp.setServiceModuleId(m.getServiceModuleId());
            deviceTypeToServiceModuleResp.setCreateBy(m.getCreateBy());
            deviceTypeToServiceModuleResp.setCreateTime(m.getCreateTime());
            deviceTypeToServiceModuleResp.setUpdateBy(m.getUpdateBy());
            deviceTypeToServiceModuleResp.setUpdateTime(m.getUpdateTime());
            deviceTypeToServiceModuleResp.setStatus(m.getStatus());
            respList.add(deviceTypeToServiceModuleResp);
        });
        return respList;
    }

    @Override
    public List<DeviceTypeToServiceModuleResp> listByServiceModuleId(Long serviceModuleId,Long tenantId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",tenantId);
        wrapper.eq("service_module_id",serviceModuleId);
        List<DeviceTypeToServiceModule> list = super.selectList(wrapper);
        List<DeviceTypeToServiceModuleResp> respList = new ArrayList<>();
        list.forEach(m->{
            DeviceTypeToServiceModuleResp deviceTypeToServiceModuleResp = new DeviceTypeToServiceModuleResp();
            deviceTypeToServiceModuleResp.setId(m.getId());
            deviceTypeToServiceModuleResp.setTenantId(m.getTenantId());
            deviceTypeToServiceModuleResp.setDeviceTypeId(m.getDeviceTypeId());
            deviceTypeToServiceModuleResp.setServiceModuleId(m.getServiceModuleId());
            deviceTypeToServiceModuleResp.setCreateBy(m.getCreateBy());
            deviceTypeToServiceModuleResp.setCreateTime(m.getCreateTime());
            deviceTypeToServiceModuleResp.setUpdateBy(m.getUpdateBy());
            deviceTypeToServiceModuleResp.setUpdateTime(m.getUpdateTime());
            deviceTypeToServiceModuleResp.setStatus(m.getStatus());
            deviceTypeToServiceModuleResp.setStatus(m.getStatus());
            respList.add(deviceTypeToServiceModuleResp);
        });
        return respList;
    }

    @Override
    public void update(Long id, Integer status) {
        DeviceTypeToServiceModule deviceTypeToServiceModule = new DeviceTypeToServiceModule();
        deviceTypeToServiceModule.setId(id);
        deviceTypeToServiceModule.setStatus(status);
        deviceTypeToServiceModule.setUpdateTime(new Date());
        super.updateById(deviceTypeToServiceModule);
    }

    @Transactional
    public void copyService(Long sourceDeviceTypeId, Long targetProductId) {
        if (sourceDeviceTypeId == null) {
            LOGGER.info("copyService.sourceDeviceTypeId null.");
            return;
        }
        if (targetProductId == null) {
            LOGGER.info("copyService.targetProductId null.");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        //先删除所有的产品对应的功能组、方法、事件、属性
        productToServiceModuleService.delServicesByProductId(targetProductId);

        //添加功能组
        EntityWrapper<DeviceTypeToServiceModule> wrapper = new EntityWrapper<>();
        wrapper.eq("device_type_id", sourceDeviceTypeId);
        List<DeviceTypeToServiceModule> serviceModuleList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceModuleList)) {
            LOGGER.info("未为产品定义相关模组功能");
            return;
        }
        for (DeviceTypeToServiceModule ps : serviceModuleList) {
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
            productToServiceModuleService.insert(productToServiceModule);
        }
    }

    /**
     * @despriction：校验设备类型是否有iftttType属性、方法、事件
     * @author  yeshiyuan
     * @created 2018/11/22 14:00
     */
    @Override
    public boolean checkDeviceTypeHadIftttType(Long deviceTypeId) {
        List<Long> serviceModuleIds = deviceTypeToServiceModuleMapper.findServiceModuleIdByDeviceTypeId(deviceTypeId);
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
        return events != null && !events.isEmpty();
    }

    /**
     * @despriction：根据ifttt类型找到对应的模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 15:53
     * @return
     */
    @Override
    public PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(Long deviceTypeId, String iftttType) {
        PackageServiceModuleDetailResp detailResp = new PackageServiceModuleDetailResp();
        List<Long> serviceModuleIds = deviceTypeToServiceModuleMapper.findServiceModuleIdByDeviceTypeId(deviceTypeId);
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
}
