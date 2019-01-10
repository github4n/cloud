package com.iot.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.beans.BeanUtil;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.mapper.ServiceModulePropertyMapper;
import com.iot.device.model.*;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.model.enums.ParamTypeEnum;
import com.iot.device.model.enums.PropertyStatusEnum;
import com.iot.device.model.enums.RWStatusEnum;
import com.iot.device.model.enums.ReqParamTypeEnum;
import com.iot.device.model.enums.ReturnTypeEnum;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.IServiceToPropertyService;
import com.iot.device.service.ISmartDataPointService;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ModuleIftttReq;
import com.iot.device.vo.req.ServiceModulePropertyReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleIftttDataResp;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.LangInfoBaseApi;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.enums.LangTypeEnum;
import com.iot.tenant.vo.req.lang.DelLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.LangInfoReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoTenantReq;
import com.iot.tenant.vo.resp.lang.LangInfoBaseResp;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 模组-属性表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ServiceModulePropertyServiceImpl extends ServiceImpl<ServiceModulePropertyMapper, ServiceModuleProperty> implements IServiceModulePropertyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModulePropertyServiceImpl.class);

    @Autowired
    private ServiceModulePropertyMapper serviceModulePropertyMapper;

    @Autowired
    private IServiceToPropertyService iServiceToPropertyService;

    @Autowired
    private ISmartDataPointService iSmartDataPointService;

    @Autowired
    private IModuleActionToPropertyService iModuleActionToPropertyService;

    @Autowired
    private IModuleEventToPropertyService iModuleEventToPropertyService;

    @Autowired
    private LangInfoBaseApi langInfoBaseApi;

    @Autowired
    private LangInfoTenantApi langInfoTenantApi;

    @Transactional
    public void delPropertyByPropertyId(Long propertyId) {
        ServiceModuleCacheCoreUtils.removeServiceModulePropertyCache(propertyId);
        if (propertyId == null) {
            LOGGER.info("propertyId not null");
            return;
        }
        ServiceModuleProperty property = super.selectById(propertyId);
        if (property == null) {
            LOGGER.info("property not exist");
            return;
        }
        if (property.getTenantId().equals(SystemConstants.BOSS_TENANT)) {
            this.deletePropertyLangInfo(property);
        } else {
            langInfoTenantApi.deleteLangInfo(LangInfoObjectTypeEnum.property.name(), propertyId.toString(), property.getTenantId());
        }
        super.deleteById(propertyId);

        // 删除音箱关联的功能点
        iSmartDataPointService.delByPropertyIdAndTenantId(propertyId, property.getTenantId());
    }

    @Transactional
    public ServiceModuleProperty addOrUpdateProperty(Long origPropertyId) {
        if (origPropertyId == null) {
            LOGGER.info("origPropertyId not null");
            return null;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        ServiceModuleProperty property = super.selectById(origPropertyId);
        if (property == null) {
            LOGGER.info("property not exist");
            return null;
        }
        Long parentPropertyId;
        if (property.getParentId() == null) {
            parentPropertyId = origPropertyId;
        } else {
            parentPropertyId = property.getParentId();
        }
        //copy add
        ServiceModuleProperty tempProperty = new ServiceModuleProperty();
        BeanUtils.copyProperties(property, tempProperty);
        tempProperty.setId(null);
        tempProperty.setParentId(parentPropertyId);//设置parentId by origPropertyId
        tempProperty.setCreateBy(userId);
        tempProperty.setTenantId(tenantId);
        tempProperty.setCreateTime(new Date());
        super.insert(tempProperty);

        String allowedValues = this.addPropertyLangInfo(tempProperty.getAllowedValues(), tempProperty);
        tempProperty.setAllowedValues(allowedValues);
        super.insertOrUpdate(tempProperty);

        return tempProperty;
    }

    @Transactional
    public ServiceModuleProperty addOrUpdateProperty(AddServiceModulePropertyReq property) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        Long parentPropertyId;
        Integer inHomePage;
        if (property.getParentId() != null) {
            ServiceModuleProperty origProperty = super.selectById(property.getParentId());
            if (origProperty == null) {
                LOGGER.info("property not exist");
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PROPERTY_NOT_EXIST);
            }
            parentPropertyId = origProperty.getId();
            inHomePage = origProperty.getInHomePage();
        } else {
            ServiceModuleProperty origProperty = super.selectById(property.getId());
            if (origProperty == null) {
                LOGGER.info("property not exist");
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PROPERTY_NOT_EXIST);
            }
            parentPropertyId = origProperty.getId();
            inHomePage = origProperty.getInHomePage();
        }
        // copy add
        ServiceModuleProperty tempProperty = new ServiceModuleProperty();
        BeanUtils.copyProperties(property, tempProperty);
        tempProperty.setDevelopStatus(DevelopStatusEnum.getByValue(property.getDevelopStatus()));
        tempProperty.setParamType(ParamTypeEnum.getByValue(property.getParamType()));
        tempProperty.setReqParamType(ReqParamTypeEnum.getByValue(property.getReqParamType()));
        tempProperty.setReturnType(ReturnTypeEnum.getByValue(property.getReturnType()));
        tempProperty.setPropertyStatus(PropertyStatusEnum.getByValue(property.getPropertyStatus()));
        tempProperty.setRwStatus(RWStatusEnum.getByValue(property.getRwStatus()));
        tempProperty.setDevelopStatus(DevelopStatusEnum.OPERATING);
        tempProperty.setId(null);
        tempProperty.setParentId(parentPropertyId);
        tempProperty.setCreateBy(userId);
        tempProperty.setTenantId(tenantId);
        tempProperty.setCreateTime(new Date());
        tempProperty.setInHomePage(inHomePage);
        super.insert(tempProperty);

        String allowedValues = this.addPropertyLangInfo(tempProperty.getAllowedValues(), tempProperty);
        tempProperty.setAllowedValues(allowedValues);
        super.insertOrUpdate(tempProperty);

        // copy音箱关联的功能点
        iSmartDataPointService.copySmartDataPoint(parentPropertyId, tempProperty.getId());

        return tempProperty;
    }

    @Override
    public Long saveOrUpdate(ServiceModulePropertyReq serviceModulePropertyReq) {
        LOGGER.info("***** saveOrUpdate, serviceModulePropertyReq.json={}", JSON.toJSONString(serviceModulePropertyReq));

        ServiceModuleProperty serviceModuleProperty = null;
        if (serviceModulePropertyReq.getId() != null && serviceModulePropertyReq.getId() > 0) {
            serviceModuleProperty = super.selectById(serviceModulePropertyReq.getId());
            if (serviceModuleProperty == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            ServiceModuleCacheCoreUtils.removeServiceModulePropertyCache(serviceModulePropertyReq.getId());
            serviceModuleProperty.setUpdateTime(new Date());
            serviceModuleProperty.setUpdateBy(serviceModulePropertyReq.getUpdateBy());
        } else {
            serviceModuleProperty = new ServiceModuleProperty();
            serviceModuleProperty.setUpdateTime(new Date());
            serviceModuleProperty.setUpdateBy(serviceModulePropertyReq.getUpdateBy());
            serviceModuleProperty.setCreateTime(new Date());
            serviceModuleProperty.setCreateBy(serviceModulePropertyReq.getCreateBy());
        }
        serviceModuleProperty.setParentId(null);
        serviceModuleProperty.setTenantId(serviceModulePropertyReq.getTenantId());
        serviceModuleProperty.setServiceModuleId(serviceModulePropertyReq.getServiceModuleId());
        serviceModuleProperty.setVersion(serviceModulePropertyReq.getVersion());
        serviceModuleProperty.setName(serviceModulePropertyReq.getName());
        serviceModuleProperty.setCode(serviceModulePropertyReq.getCode());
        serviceModuleProperty.setTags(serviceModulePropertyReq.getTags());
        serviceModuleProperty.setApiLevel(serviceModulePropertyReq.getApiLevel());
        if(serviceModulePropertyReq.getDevelopStatus() != null) {
            if (serviceModulePropertyReq.getDevelopStatus() == 0) {
                serviceModuleProperty.setDevelopStatus(DevelopStatusEnum.IDLE);
            } else if (serviceModulePropertyReq.getDevelopStatus() == 1) {
                serviceModuleProperty.setDevelopStatus(DevelopStatusEnum.OPERATING);
            } else {
                serviceModuleProperty.setDevelopStatus(DevelopStatusEnum.OPERATED);
            }
        }
        if(serviceModulePropertyReq.getReqParamType() != null) {
            if (serviceModulePropertyReq.getReqParamType() == 0) {
                serviceModuleProperty.setReqParamType(ReqParamTypeEnum.OPTIONAL);
            } else {
                serviceModuleProperty.setReqParamType(ReqParamTypeEnum.MANDATORY);
            }
        }
        if(serviceModulePropertyReq.getReturnType() != null) {
            if (serviceModulePropertyReq.getReturnType() == 0) {
                serviceModuleProperty.setReturnType(ReturnTypeEnum.OPTIONAL);
            } else {
                serviceModuleProperty.setReturnType(ReturnTypeEnum.MANDATORY);
            }
        }
//        if(serviceModulePropertyReq.getPropertyStatus() != null) {
//            if (serviceModulePropertyReq.getPropertyStatus() == 0) {
//                serviceModuleProperty.setPropertyStatus(PropertyStatusEnum.OPTIONAL);
//            } else {
//                serviceModuleProperty.setPropertyStatus(PropertyStatusEnum.MANDATORY);
//            }
//        }
        if(serviceModulePropertyReq.getRwStatus() != null) {
            if (serviceModulePropertyReq.getRwStatus() == 0) {
                serviceModuleProperty.setRwStatus(RWStatusEnum.ENABLE_RW);
            } else if (serviceModulePropertyReq.getRwStatus() == 1) {
                serviceModuleProperty.setRwStatus(RWStatusEnum.DISABLE_RW);
            } else {
                serviceModuleProperty.setRwStatus(RWStatusEnum.DISABLE_W);
            }
        }
        if(serviceModulePropertyReq.getParamType() != null) {
            if (serviceModulePropertyReq.getParamType() == 0) {
                serviceModuleProperty.setParamType(ParamTypeEnum.INT);
            } else if (serviceModulePropertyReq.getParamType() == 1) {
                serviceModuleProperty.setParamType(ParamTypeEnum.FLOAT);
            } else if (serviceModulePropertyReq.getParamType() == 2) {
                serviceModuleProperty.setParamType(ParamTypeEnum.BOOL);
            } else if (serviceModulePropertyReq.getParamType() == 3) {
                serviceModuleProperty.setParamType(ParamTypeEnum.ENUM);
            } else {
                serviceModuleProperty.setParamType(ParamTypeEnum.STRING);
            }
        }
        serviceModuleProperty.setMaxValue(serviceModulePropertyReq.getMaxValue());
        serviceModuleProperty.setMinValue(serviceModulePropertyReq.getMinValue());
        serviceModuleProperty.setAllowedValues(serviceModulePropertyReq.getAllowedValues());
        serviceModuleProperty.setTestCase(serviceModulePropertyReq.getTestCase());
        serviceModuleProperty.setDescription(serviceModulePropertyReq.getDescription());
        serviceModuleProperty.setIftttType(serviceModulePropertyReq.getIftttType());
        serviceModuleProperty.setPropertyType(serviceModulePropertyReq.getPropertyType());
        serviceModuleProperty.setInHomePage(serviceModulePropertyReq.getInHomePage());
        super.insertOrUpdate(serviceModuleProperty);

        // 多语言处理
        String allowedValues = this.addPropertyLangInfo(serviceModulePropertyReq.getAllowedValues(), serviceModuleProperty);
        serviceModuleProperty.setAllowedValues(allowedValues);
        super.insertOrUpdate(serviceModuleProperty);

        // 保存 关联的音箱功能点
        iSmartDataPointService.createSmartDataPoint(serviceModulePropertyReq.getSmart(), serviceModuleProperty.getTenantId(),
                serviceModuleProperty.getId(), serviceModuleProperty.getCreateBy());

        return serviceModuleProperty.getId();
    }

    @Override
    public List<Long> saveOrUpdateBatch(List<ServiceModulePropertyReq> serviceModulePropertyReqs){
        if(CollectionUtils.isEmpty(serviceModulePropertyReqs)){
            return null;
        }
        List<ServiceModuleProperty> list = new ArrayList<>();
        List<Long> result = new ArrayList<>();
        serviceModulePropertyReqs.forEach(serviceModulePropertyReq->{
            ServiceModuleProperty serviceModuleProperty = null;
            if (serviceModulePropertyReq.getId() != null && serviceModulePropertyReq.getId() > 0) {
                serviceModuleProperty = super.selectById(serviceModulePropertyReq.getId());
                if (serviceModuleProperty == null) {
                    throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
                }
                ServiceModuleCacheCoreUtils.removeServiceModulePropertyCache(serviceModulePropertyReq.getId());
                serviceModuleProperty.setUpdateTime(new Date());
                serviceModuleProperty.setUpdateBy(SaaSContextHolder.getCurrentUserId());
            } else {
                serviceModuleProperty = new ServiceModuleProperty();
                serviceModuleProperty.setUpdateTime(new Date());
                serviceModuleProperty.setUpdateBy(SaaSContextHolder.getCurrentUserId());
                serviceModuleProperty.setCreateTime(new Date());
                serviceModuleProperty.setCreateBy(SaaSContextHolder.getCurrentUserId());
            }
            serviceModuleProperty.setParentId(null);
            serviceModuleProperty.setTenantId(SaaSContextHolder.currentTenantId());
            serviceModuleProperty.setServiceModuleId(serviceModulePropertyReq.getServiceModuleId());
            serviceModuleProperty.setVersion(serviceModulePropertyReq.getVersion());
            serviceModuleProperty.setName(serviceModulePropertyReq.getName());
            serviceModuleProperty.setCode(serviceModulePropertyReq.getCode());
            serviceModuleProperty.setTags(serviceModulePropertyReq.getTags());
            serviceModuleProperty.setApiLevel(serviceModulePropertyReq.getApiLevel());
            if(serviceModulePropertyReq.getDevelopStatus() != null) {
                if (serviceModulePropertyReq.getDevelopStatus() == 0) {
                    serviceModuleProperty.setDevelopStatus(DevelopStatusEnum.IDLE);
                } else if (serviceModulePropertyReq.getDevelopStatus() == 1) {
                    serviceModuleProperty.setDevelopStatus(DevelopStatusEnum.OPERATING);
                } else {
                    serviceModuleProperty.setDevelopStatus(DevelopStatusEnum.OPERATED);
                }
            }
            if(serviceModulePropertyReq.getReqParamType() != null) {
                if (serviceModulePropertyReq.getReqParamType() == 0) {
                    serviceModuleProperty.setReqParamType(ReqParamTypeEnum.OPTIONAL);
                } else {
                    serviceModuleProperty.setReqParamType(ReqParamTypeEnum.MANDATORY);
                }
            }
            if(serviceModulePropertyReq.getReturnType()!= null) {
                if (serviceModulePropertyReq.getReturnType() == 0) {
                    serviceModuleProperty.setReturnType(ReturnTypeEnum.OPTIONAL);
                } else {
                    serviceModuleProperty.setReturnType(ReturnTypeEnum.MANDATORY);
                }
            }
//            if(serviceModulePropertyReq.getPropertyStatus() != null) {
//                if (serviceModulePropertyReq.getPropertyStatus() == 0) {
//                    serviceModuleProperty.setPropertyStatus(PropertyStatusEnum.OPTIONAL);
//                } else {
//                    serviceModuleProperty.setPropertyStatus(PropertyStatusEnum.MANDATORY);
//                }
//            }
            if(serviceModulePropertyReq.getRwStatus() != null) {
                if (serviceModulePropertyReq.getRwStatus() == 0) {
                    serviceModuleProperty.setRwStatus(RWStatusEnum.ENABLE_RW);
                } else if (serviceModulePropertyReq.getRwStatus() == 1) {
                    serviceModuleProperty.setRwStatus(RWStatusEnum.DISABLE_RW);
                } else {
                    serviceModuleProperty.setRwStatus(RWStatusEnum.DISABLE_W);
                }
            }
            if(serviceModulePropertyReq.getParamType() != null) {
                if (serviceModulePropertyReq.getParamType() == 0) {
                    serviceModuleProperty.setParamType(ParamTypeEnum.INT);
                } else if (serviceModulePropertyReq.getParamType() == 1) {
                    serviceModuleProperty.setParamType(ParamTypeEnum.FLOAT);
                } else if (serviceModulePropertyReq.getParamType() == 2) {
                    serviceModuleProperty.setParamType(ParamTypeEnum.BOOL);
                } else if (serviceModulePropertyReq.getParamType() == 3) {
                    serviceModuleProperty.setParamType(ParamTypeEnum.ENUM);
                } else {
                    serviceModuleProperty.setParamType(ParamTypeEnum.STRING);
                }
            }
            serviceModuleProperty.setMaxValue(serviceModulePropertyReq.getMaxValue());
            serviceModuleProperty.setMinValue(serviceModulePropertyReq.getMinValue());
            serviceModuleProperty.setAllowedValues(serviceModulePropertyReq.getAllowedValues());
            serviceModuleProperty.setTestCase(serviceModulePropertyReq.getTestCase());
            serviceModuleProperty.setDescription(serviceModulePropertyReq.getDescription());
            serviceModuleProperty.setIftttType(serviceModulePropertyReq.getIftttType());
            serviceModuleProperty.setPropertyType(serviceModulePropertyReq.getPropertyType());
            serviceModuleProperty.setInHomePage(serviceModulePropertyReq.getInHomePage());
            list.add(serviceModuleProperty);
        });
        super.insertOrUpdateBatch(list);
        List<ServiceModuleProperty> serviceModuleProperties = Lists.newArrayList();
        list.forEach(property->{
            // 多语言处理
            String allowedValues = this.addPropertyLangInfo(property.getAllowedValues(), property);
            property.setAllowedValues(allowedValues);
            serviceModuleProperties.add(property);
        });
        super.insertOrUpdateBatch(serviceModuleProperties);
        serviceModuleProperties.forEach(property -> {
            result.add(property.getId());
        });
        return result;
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (ids != null && ids.size() > 0) {
            ServiceModuleCacheCoreUtils.batchRemoveServiceModulePropertyCache(ids);
            //检查是否被关联
            ids.forEach(propertyId->{
                EntityWrapper wrapper = new EntityWrapper<>();
                wrapper.eq("module_property_id", propertyId);
                int moduleCount = iServiceToPropertyService.selectCount(wrapper);
                int actionCount = iModuleActionToPropertyService.selectCount(wrapper);
                wrapper = new EntityWrapper();
                wrapper.eq("event_property_id", propertyId);
                int eventCount = iModuleEventToPropertyService.selectCount(wrapper);
                if (moduleCount > 0 || actionCount > 0 || eventCount > 0) {
                    throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PROPERTY_IS_USED);
                }
            });
            // 删除多语言信息
            List<ServiceModuleProperty> serviceModuleProperties = super.selectBatchIds(ids);
            for (ServiceModuleProperty serviceModuleProperty : serviceModuleProperties) {
                if (serviceModuleProperty.getTenantId().equals(SystemConstants.BOSS_TENANT)) {
                    this.deletePropertyLangInfo(serviceModuleProperty);
                } else {
                    langInfoTenantApi.deleteLangInfo(LangInfoObjectTypeEnum.property.name(), serviceModuleProperty.getId().toString(), serviceModuleProperty.getTenantId());
                }
            }
            super.deleteBatchIds(ids);
            // 删除音箱关联的功能点
            iSmartDataPointService.delByPropertyIds(ids);

            ArrayList<Long> idsResult = new ArrayList<Long>();
            iServiceToPropertyService.listByModulePropertyId(ids).forEach(m->{
                idsResult.add(m.getId());
            });
            iServiceToPropertyService.delete(idsResult);
            idsResult.clear();
            iModuleEventToPropertyService.listByModuleEventId(ids).forEach(m -> {
                idsResult.add(m.getId());
            });
            iModuleEventToPropertyService.delete(idsResult);
            idsResult.clear();
            iModuleActionToPropertyService.listByModuleActionId(ids).forEach(m -> {
                idsResult.add(m.getId());
            });
            iModuleActionToPropertyService.delete(idsResult);
        }
    }

    @Override
    public PageInfo list(ServiceModulePropertyReq serviceModulePropertyReq) {
        Page page = new Page<>(CommonUtil.getPageNum(serviceModulePropertyReq),CommonUtil.getPageSize(serviceModulePropertyReq));
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.isNull("parent_id");
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        if(serviceModulePropertyReq.getPropertyType() != null){
            wrapper.eq("property_type", serviceModulePropertyReq.getPropertyType());
        }
        if(serviceModulePropertyReq.getReqParamType() != null){
            wrapper.eq("req_param_type", serviceModulePropertyReq.getReqParamType());
        }
        if(serviceModulePropertyReq.getReturnType() != null){
            wrapper.eq("return_type", serviceModulePropertyReq.getReturnType());
        }
        if (StringUtils.isNotEmpty(serviceModulePropertyReq.getSearchValues())){
            wrapper.andNew(true, "")
                    .like("name", serviceModulePropertyReq.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("code", serviceModulePropertyReq.getSearchValues(), SqlLike.DEFAULT);
        }

        wrapper.orderDesc(Arrays.asList("create_time"));
        if (serviceModulePropertyReq.getFilterIds().size()>0 && serviceModulePropertyReq.getFilterIds()!=null){
            wrapper.notIn("id",serviceModulePropertyReq.getFilterIds());
        }
        List<ServiceModuleProperty> list = serviceModulePropertyMapper.selectPage(page,wrapper);
        List<ServiceModulePropertyResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModulePropertyResp serviceModulePropertyResp = new ServiceModulePropertyResp();
            serviceModulePropertyResp.setId(m.getId());
            serviceModulePropertyResp.setParentId(m.getParentId());
            serviceModulePropertyResp.setTenantId(m.getTenantId());
            serviceModulePropertyResp.setServiceModuleId(m.getServiceModuleId());
            serviceModulePropertyResp.setVersion(m.getVersion());
            serviceModulePropertyResp.setName(m.getName());
            serviceModulePropertyResp.setCode(m.getCode());
            serviceModulePropertyResp.setTags(m.getTags());
            serviceModulePropertyResp.setApiLevel(m.getApiLevel());
            serviceModulePropertyResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
            serviceModulePropertyResp.setReqParamType(m.getReqParamType()!=null?(Integer)m.getReqParamType().getValue():null);
            serviceModulePropertyResp.setReturnType(m.getReturnType()!=null?(Integer)m.getReturnType().getValue():null);
//            serviceModulePropertyResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModulePropertyResp.setRwStatus(m.getRwStatus()!=null?(Integer)m.getRwStatus().getValue():null);
            serviceModulePropertyResp.setParamType(m.getParamType()!=null?(Integer)m.getParamType().getValue():null);
            serviceModulePropertyResp.setMaxValue(m.getMaxValue());
            serviceModulePropertyResp.setMinValue(m.getMinValue());
            String allowedValues = this.getPropertyLangInfo(m);
            serviceModulePropertyResp.setAllowedValues(allowedValues);
            serviceModulePropertyResp.setTestCase(m.getTestCase());
            serviceModulePropertyResp.setCreateBy(m.getCreateBy());
            serviceModulePropertyResp.setCreateTime(m.getCreateTime());
            serviceModulePropertyResp.setUpdateBy(m.getUpdateBy());
            serviceModulePropertyResp.setUpdateTime(m.getUpdateTime());
            serviceModulePropertyResp.setDescription(m.getDescription());
            serviceModulePropertyResp.setPropertyId(m.getPropertyId());
            serviceModulePropertyResp.setIftttType(m.getIftttType());
            serviceModulePropertyResp.setPropertyType(m.getPropertyType());
            serviceModulePropertyResp.setInHomePage(m.getInHomePage());

            // 填充 关联的音箱功能点
            List<SmartDataPoint> smartDataPointList = iSmartDataPointService.findSmartDataPointList(m.getTenantId(), m.getId());
            if (!CollectionUtils.isEmpty(smartDataPointList)) {
                List<SmartDataPointResp> smartRespList = Lists.newArrayList();
                smartDataPointList.forEach(smartDataPoint ->{
                    SmartDataPointResp resp = new SmartDataPointResp();
                    resp.setSmartCode(smartDataPoint.getSmartCode());
                    resp.setId(smartDataPoint.getId());
                    resp.setSmart(smartDataPoint.getSmart());
                    smartRespList.add(resp);
                });
                serviceModulePropertyResp.setSmart(smartRespList);
            }

            respList.add(serviceModulePropertyResp);
        });
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(respList);
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNum(serviceModulePropertyReq.getPageNum());
        return pageInfo;
    }

    @Override
    public List<ServiceModulePropertyResp> listByServiceModuleId(Long serviceModuleId) {
       // List<ServiceModuleProperty> list = serviceModulePropertyMapper.listByServiceModuleId(serviceModuleId,SaaSContextHolder.currentTenantId());
        List<ServiceModuleProperty> list = getServiceModulePropertyListByServiceModuleId(serviceModuleId);
        List<ServiceModulePropertyResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModulePropertyResp serviceModulePropertyResp = new ServiceModulePropertyResp();
            serviceModulePropertyResp.setId(m.getId());
            serviceModulePropertyResp.setParentId(m.getParentId());
            serviceModulePropertyResp.setTenantId(m.getTenantId());
            serviceModulePropertyResp.setServiceModuleId(m.getServiceModuleId());
            serviceModulePropertyResp.setVersion(m.getVersion());
            serviceModulePropertyResp.setName(m.getName());
            serviceModulePropertyResp.setCode(m.getCode());
            serviceModulePropertyResp.setTags(m.getTags());
            serviceModulePropertyResp.setApiLevel(m.getApiLevel());
            serviceModulePropertyResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
            serviceModulePropertyResp.setReqParamType(m.getReqParamType()!=null?(Integer)m.getReqParamType().getValue():null);
            serviceModulePropertyResp.setReturnType(m.getReturnType()!=null?(Integer)m.getReturnType().getValue():null);
//            serviceModulePropertyResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModulePropertyResp.setRwStatus(m.getRwStatus()!=null?(Integer)m.getRwStatus().getValue():null);
            serviceModulePropertyResp.setParamType(m.getParamType()!=null?(Integer)m.getParamType().getValue():null);
            serviceModulePropertyResp.setMaxValue(m.getMaxValue());
            serviceModulePropertyResp.setMinValue(m.getMinValue());
            String allowedValues = this.getPropertyLangInfo(m);
            serviceModulePropertyResp.setAllowedValues(allowedValues);
            serviceModulePropertyResp.setTestCase(m.getTestCase());
            serviceModulePropertyResp.setCreateBy(m.getCreateBy());
            serviceModulePropertyResp.setCreateTime(m.getCreateTime());
            serviceModulePropertyResp.setUpdateBy(m.getUpdateBy());
            serviceModulePropertyResp.setUpdateTime(m.getUpdateTime());
            serviceModulePropertyResp.setDescription(m.getDescription());
            serviceModulePropertyResp.setPropertyId(m.getPropertyId());
            serviceModulePropertyResp.setIftttType(m.getIftttType());
            serviceModulePropertyResp.setPropertyType(m.getPropertyType());
            serviceModulePropertyResp.setInHomePage(m.getInHomePage());
            serviceModulePropertyResp.setStatus(m.getStatus());
            respList.add(serviceModulePropertyResp);
        });
        return respList;
    }

    public List<ServiceModuleProperty> getServiceModulePropertyListByServiceModuleId(Long serviceModuleId){
        List<ServiceModuleProperty> result = new ArrayList<>();
        /**
         * 根据serviceModuleId查询关系表
         */
        List<Long> serviceModuleIds = new ArrayList<>();
        serviceModuleIds.add(serviceModuleId);
        List<ServiceToProperty> serviceToPropertyList = this.iServiceToPropertyService.getServiceToPropertyListByServiceModuleId(serviceModuleIds);
        /**
         * 根据关系表种的propertiyId查询serviceModuleProperty信息(缓存+表)
         */
        if(serviceToPropertyList != null && serviceToPropertyList.size() >0){
            Map<Long, ServiceToProperty> serviceToPropertyMap = serviceToPropertyList.stream()
                    .collect(Collectors.toMap(ServiceToProperty::getModulePropertyId, serviceToProperty -> serviceToProperty, (k1, k2) -> k1));
            List<Long> propertyIds = serviceToPropertyList.stream().map(ServiceToProperty::getModulePropertyId).collect(Collectors.toList());
            result  =  getServiceModulePropertyListByIds(propertyIds);
            result.forEach(serviceModuleProperty -> {
                ServiceToProperty serviceToProperty = serviceToPropertyMap.get(serviceModuleProperty.getId());
                if(serviceToProperty != null){
                    serviceModuleProperty.setStatus(serviceToProperty.getStatus());
                    serviceModuleProperty.setPropertyId(serviceToProperty.getId());
                }
            });
        }
        return result;
    }

    public List<ServiceModuleProperty> getServiceModulePropertyListByIds(List<Long> ids) {
        List<ServiceModuleProperty> result = new ArrayList<>();
        List<Long> noCacheIds = getServiceModulePropertyListByIdsFromCache(ids, result);
        if (noCacheIds != null && noCacheIds.size() > 0) {
            List<ServiceModuleProperty> dataList = getServiceModulePropertyListByIdsFromSql(noCacheIds);
            if (dataList != null && dataList.size() > 0) {
                result.addAll(dataList);
            }
        }
        return result;
    }

    private List<Long> getServiceModulePropertyListByIdsFromCache(List<Long> ids, List<ServiceModuleProperty> result) {
        List<Long> noCacheIds = new ArrayList<>();
        List<ServiceModuleProperty> cacheData = ServiceModuleCacheCoreUtils.batchGetServiceMoulePropertyCache(ids);
        if (cacheData != null && cacheData.size() > 0) {
            List<Long> cacheIds = cacheData.stream().map(ServiceModuleProperty::getId).collect(Collectors.toList());
            noCacheIds = ids.stream().filter(id -> !cacheIds.contains(id)).collect(Collectors.toList());
            result.addAll(cacheData);
        } else {
            noCacheIds = ids;
        }
        return noCacheIds;
    }

    private List<ServiceModuleProperty> getServiceModulePropertyListByIdsFromSql(List<Long> ids) {
        EntityWrapper<ServiceModuleProperty> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.in("id", ids);
        List<ServiceModuleProperty> propertyList = this.selectList(propertyWrapper);
        if (propertyList != null && propertyList.size() > 0) {
            /**
             * 放入缓存
             */
            ServiceModuleCacheCoreUtils.batchSetServiceModulePropertyCache(propertyList);
        }
        return propertyList;
    }

    @Override
    public List<ServiceModulePropertyResp> listByActionModuleId(Long actionModuleId) {
        List<ServiceModuleProperty> list = serviceModulePropertyMapper.listByActionModuleId(actionModuleId,SaaSContextHolder.currentTenantId());
        List<ServiceModulePropertyResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModulePropertyResp serviceModulePropertyResp = new ServiceModulePropertyResp();
            serviceModulePropertyResp.setId(m.getId());
            serviceModulePropertyResp.setParentId(m.getParentId());
            serviceModulePropertyResp.setTenantId(m.getTenantId());
            serviceModulePropertyResp.setServiceModuleId(m.getServiceModuleId());
            serviceModulePropertyResp.setVersion(m.getVersion());
            serviceModulePropertyResp.setName(m.getName());
            serviceModulePropertyResp.setCode(m.getCode());
            serviceModulePropertyResp.setTags(m.getTags());
            serviceModulePropertyResp.setApiLevel(m.getApiLevel());
            serviceModulePropertyResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
            serviceModulePropertyResp.setReqParamType(m.getReqParamType()!=null?(Integer)m.getReqParamType().getValue():null);
            serviceModulePropertyResp.setReturnType(m.getReturnType()!=null?(Integer)m.getReturnType().getValue():null);
            serviceModulePropertyResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModulePropertyResp.setRwStatus(m.getRwStatus()!=null?(Integer)m.getRwStatus().getValue():null);
            serviceModulePropertyResp.setParamType(m.getParamType()!=null?(Integer)m.getParamType().getValue():null);
            serviceModulePropertyResp.setMaxValue(m.getMaxValue());
            serviceModulePropertyResp.setMinValue(m.getMinValue());
            String allowedValues = this.getPropertyLangInfo(m);
            serviceModulePropertyResp.setAllowedValues(allowedValues);
            serviceModulePropertyResp.setTestCase(m.getTestCase());
            serviceModulePropertyResp.setCreateBy(m.getCreateBy());
            serviceModulePropertyResp.setCreateTime(m.getCreateTime());
            serviceModulePropertyResp.setUpdateBy(m.getUpdateBy());
            serviceModulePropertyResp.setUpdateTime(m.getUpdateTime());
            serviceModulePropertyResp.setDescription(m.getDescription());
            serviceModulePropertyResp.setPropertyId(m.getPropertyId());
            serviceModulePropertyResp.setIftttType(m.getIftttType());
            serviceModulePropertyResp.setPropertyType(m.getPropertyType());
            serviceModulePropertyResp.setPropertyParamType(m.getPropertyParamType());
            serviceModulePropertyResp.setInHomePage(m.getInHomePage());
            respList.add(serviceModulePropertyResp);
        });
        return respList;
    }

    @Override
    public List<ServiceModulePropertyResp> listByEventModuleId(Long eventModueId) {
        List<ServiceModuleProperty> list = serviceModulePropertyMapper.listByEventModuleId(eventModueId,SaaSContextHolder.currentTenantId());
        List<ServiceModulePropertyResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModulePropertyResp serviceModulePropertyResp = new ServiceModulePropertyResp();
            serviceModulePropertyResp.setId(m.getId());
            serviceModulePropertyResp.setParentId(m.getParentId());
            serviceModulePropertyResp.setTenantId(m.getTenantId());
            serviceModulePropertyResp.setServiceModuleId(m.getServiceModuleId());
            serviceModulePropertyResp.setVersion(m.getVersion());
            serviceModulePropertyResp.setName(m.getName());
            serviceModulePropertyResp.setCode(m.getCode());
            serviceModulePropertyResp.setTags(m.getTags());
            serviceModulePropertyResp.setApiLevel(m.getApiLevel());
            serviceModulePropertyResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
            serviceModulePropertyResp.setReqParamType(m.getReqParamType()!=null?(Integer)m.getReqParamType().getValue():null);
            serviceModulePropertyResp.setReturnType(m.getReturnType()!=null?(Integer)m.getReturnType().getValue():null);
            serviceModulePropertyResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModulePropertyResp.setRwStatus(m.getRwStatus()!=null?(Integer)m.getRwStatus().getValue():null);
            serviceModulePropertyResp.setParamType(m.getParamType()!=null?(Integer)m.getParamType().getValue():null);
            serviceModulePropertyResp.setMaxValue(m.getMaxValue());
            serviceModulePropertyResp.setMinValue(m.getMinValue());
            String allowedValues = this.getPropertyLangInfo(m);
            serviceModulePropertyResp.setAllowedValues(allowedValues);
            serviceModulePropertyResp.setTestCase(m.getTestCase());
            serviceModulePropertyResp.setCreateBy(m.getCreateBy());
            serviceModulePropertyResp.setCreateTime(m.getCreateTime());
            serviceModulePropertyResp.setUpdateBy(m.getUpdateBy());
            serviceModulePropertyResp.setUpdateTime(m.getUpdateTime());
            serviceModulePropertyResp.setDescription(m.getDescription());
            serviceModulePropertyResp.setPropertyId(m.getPropertyId());
            serviceModulePropertyResp.setIftttType(m.getIftttType());
            serviceModulePropertyResp.setPropertyType(m.getPropertyType());
            serviceModulePropertyResp.setInHomePage(m.getInHomePage());
            respList.add(serviceModulePropertyResp);
        });
        return respList;
    }

    /**
     * @despriction：查找支持联动配置的属性
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @Override
    public ModuleSupportIftttResp findSupportIftttPropertys(List<Long> serviceModuleIds, Long tenantId) {
        ModuleSupportIftttResp resp = new ModuleSupportIftttResp();
        List<ModuleIftttDataResp> ifList = new ArrayList<>();
        List<ModuleIftttDataResp> thenList = new ArrayList<>();
        List<ServiceModuleProperty> properties = serviceModulePropertyMapper.supportIftttProperty(serviceModuleIds);
        properties.forEach(event -> {
            if (ModuleIftttTypeEnum.IF.getValue().equals(event.getIftttType())) {
                ModuleIftttDataResp dataResp = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseIf(event.getPortalIftttType()));
                ifList.add(dataResp);
            } else if (ModuleIftttTypeEnum.THEN.getValue().equals(event.getIftttType())) {
                ModuleIftttDataResp dataResp = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseThen(event.getPortalIftttType()));
                thenList.add(dataResp);
            } else if (ModuleIftttTypeEnum.IF_TEHN.getValue().equals(event.getIftttType())) {
                ModuleIftttDataResp ifData = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseIf(event.getPortalIftttType()));
                ifList.add(ifData);
                ModuleIftttDataResp thenData = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseThen(event.getPortalIftttType()));
                thenList.add(thenData);
            }
        });
        resp.setIfList(ifList);
        resp.setThenList(thenList);
        return resp;
    }

    /**
     * @despriction：修改模组属性联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    @Transactional
    @Override
    public int updatePropertySupportIfttt(UpdateModuleSupportIftttReq req) {
        int i = 0;
        if (req.getList() != null && !req.getList().isEmpty()) {
            List<ServiceModuleProperty> properties = serviceModulePropertyMapper.supportIftttProperty(req.getServiceModuleIds());
            Map<Long, Integer> propertyMap = properties.stream().collect(Collectors.toMap(ServiceModuleProperty::getId, a-> a.getIftttType()));
            for (ModuleIftttReq o : req.getList()) {
                Integer portalIftttType = 0;
                if (o.getIfData()!=null && o.getIfData().intValue() == 1 ) {
                    portalIftttType = ModuleIftttTypeEnum.IF.getValue();
                }
                if (o.getThenData()!=null && o.getThenData().intValue() == 1) {
                    portalIftttType+=ModuleIftttTypeEnum.THEN.getValue();
                }
                Integer iftttType = propertyMap.get(o.getId());
                if (iftttType == null) {
                    continue;
                }
                if (portalIftttType.compareTo(iftttType) == 1 ) {
                    throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PORTAL_IFTTTTYPE_ERROR);
                }
                i += serviceModulePropertyMapper.updatePortalIftttType(o.getId(), req.getTenantId(), portalIftttType, req.getUserId(), new Date());
            }
        }
        return i;
    }

    /**
     * 根据 主键id 获取记录
     * @param serviceModulePropertyId
     * @return
     */
    @Override
    public ServiceModuleProperty get(Long serviceModulePropertyId) {
        if (serviceModulePropertyId == null) {
            return null;
        }
        ServiceModuleProperty prop = super.selectById(serviceModulePropertyId);
        if (prop != null) {
            String allowedValues = this.getPropertyLangInfo(prop);
            prop.setAllowedValues(allowedValues);
        }
        return prop;
    }

    private String addPropertyLangInfo(String reqAllowedValues, ServiceModuleProperty serviceModuleProperty) {
        LOGGER.debug("addPropertyLangInfo, reqAllowedValues={}, propertyId={}", reqAllowedValues, serviceModuleProperty.getId());
        String result = new JSONArray().toJSONString();
        try {
            if (StringUtils.isBlank(reqAllowedValues)) {
                LOGGER.warn("addPropertyLangInfo, reqAllowedValues is blank");
                return result;
            }
            JSONArray jsonArray = JSON.parseArray(reqAllowedValues);
            SaveLangInfoBaseReq saveLangInfoBaseReq = new SaveLangInfoBaseReq();
            saveLangInfoBaseReq.setBelongModule(LangInfoObjectTypeEnum.property.name());
            saveLangInfoBaseReq.setObjectId(serviceModuleProperty.getId());
            saveLangInfoBaseReq.setObjectType(LangInfoObjectTypeEnum.property.name());
            saveLangInfoBaseReq.setTenantId(serviceModuleProperty.getTenantId());
            saveLangInfoBaseReq.setUserId(serviceModuleProperty.getCreateBy());
            List<LangInfoReq> langInfoReqs = Lists.newArrayList();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LOGGER.debug("addPropertyLangInfo, jsonObject_{}={}", i, jsonObject.toJSONString());
                String name = jsonObject.getString("name");
                String value = jsonObject.getString("value");
                String description = jsonObject.getString("description");
                LangInfoReq nameLangInfoReq = new LangInfoReq();
                String nameLangKey = "paramsName_" + serviceModuleProperty.getId() + "_" + i;
                nameLangInfoReq.setKey(nameLangKey);
                jsonObject.put("name", nameLangKey);
                Map<String, String> nameVal = Maps.newHashMap();
                nameVal.put(LangTypeEnum.EN_US.getLangType(), name);
                nameVal.put(LangTypeEnum.ZH_CN.getLangType(), name);
                nameLangInfoReq.setVal(nameVal);
                LangInfoReq valueLangInfoReq = new LangInfoReq();
                String valueLangKey = "paramsValue_" + serviceModuleProperty.getId() + "_" + i;
                valueLangInfoReq.setKey(valueLangKey);
                jsonObject.put("value", valueLangKey);
                Map<String, String> valueVal = Maps.newHashMap();
                valueVal.put(LangTypeEnum.EN_US.getLangType(), value);
                valueVal.put(LangTypeEnum.ZH_CN.getLangType(), value);
                valueLangInfoReq.setVal(valueVal);
                LangInfoReq descLangInfoReq = new LangInfoReq();
                String descLangKey = "paramsDesc_" + serviceModuleProperty.getId() + "_" + i;
                descLangInfoReq.setKey(descLangKey);
                jsonObject.put("description", descLangKey);
                Map<String, String> descVal = Maps.newHashMap();
                descVal.put(LangTypeEnum.EN_US.getLangType(), description);
                descVal.put(LangTypeEnum.ZH_CN.getLangType(), description);
                descLangInfoReq.setVal(descVal);
                langInfoReqs.add(nameLangInfoReq);
                langInfoReqs.add(valueLangInfoReq);
                langInfoReqs.add(descLangInfoReq);
            }
            saveLangInfoBaseReq.setLangInfos(langInfoReqs);
            if (serviceModuleProperty.getTenantId().equals(SystemConstants.BOSS_TENANT)) {
                langInfoBaseApi.saveLangInfoBase(saveLangInfoBaseReq);
            } else {
                SaveLangInfoTenantReq saveLangInfoTenantReq = new SaveLangInfoTenantReq();
                BeanUtil.copyProperties(saveLangInfoBaseReq, saveLangInfoTenantReq);
                saveLangInfoTenantReq.setObjectId(saveLangInfoBaseReq.getObjectId().toString());
                langInfoTenantApi.addLangInfo(saveLangInfoTenantReq);
            }
            LOGGER.debug("langInfoBaseApi.addLangInfoBase({})", JSONObject.toJSONString(saveLangInfoBaseReq));
            result = jsonArray.toJSONString();
        } catch (Exception e) {
            LOGGER.warn("add allowedValues, parse error", e);
        }
        return result;
    }

    @Override
    public String getPropertyLangInfo(ServiceModuleProperty prop) {
        LOGGER.debug("getPropertyLangInfo, property={}", JSONObject.toJSONString(prop));
        String result = new JSONArray().toJSONString();
        try {
            String allowedValues = prop.getAllowedValues();
            if (StringUtils.isBlank(allowedValues)) {
                LOGGER.warn("getPropertyLangInfo, allowedValues is blank");
                return result;
            }
            JSONArray jsonArray = JSON.parseArray(allowedValues);
            QueryLangInfoBaseReq queryLangInfoBaseReq = new QueryLangInfoBaseReq();
            queryLangInfoBaseReq.setObjectId(prop.getId());
            queryLangInfoBaseReq.setObjectType(LangInfoObjectTypeEnum.property.name());
            LOGGER.debug("langInfoBaseApi.queryLangInfoBase req={}", JSONObject.toJSONString(queryLangInfoBaseReq));
            LangInfoBaseResp langInfoBaseResp = null;
            if (prop.getTenantId().equals(SystemConstants.BOSS_TENANT)) {
                langInfoBaseResp = langInfoBaseApi.queryLangInfoBase(queryLangInfoBaseReq);
            } else {
                QueryLangInfoTenantReq queryLangInfoTenantReq = new QueryLangInfoTenantReq();
                BeanUtil.copyProperties(queryLangInfoBaseReq, queryLangInfoTenantReq);
                queryLangInfoTenantReq.setObjectId(queryLangInfoBaseReq.getObjectId().toString());
                queryLangInfoTenantReq.setTenantId(prop.getTenantId());
                LangInfoTenantResp langInfoTenantResp = langInfoTenantApi.queryLangInfo(queryLangInfoTenantReq);
                langInfoBaseResp = new LangInfoBaseResp();
                BeanUtil.copyProperties(langInfoTenantResp, langInfoBaseResp);
            }
            LOGGER.debug("langInfoBaseApi.queryLangInfoBase resp={}", JSONObject.toJSONString(langInfoBaseResp));
            List<LangInfoReq> langInfos = langInfoBaseResp.getLangInfos();
            for (LangInfoReq langInfoReq : langInfos) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String nameKey = jsonObject.getString("name");
                    String valueKey = jsonObject.getString("value");
                    String descriptionKey = jsonObject.getString("description");
                    Map<String, String> values = langInfoReq.getVal();
                    if (langInfoReq.getKey().equals(nameKey)) {
                        jsonObject.put("name", values.get(LocaleContextHolder.getLocale().toString()));
                        break;
                    }
                    if (langInfoReq.getKey().equals(valueKey)) {
                        jsonObject.put("value", values.get(LocaleContextHolder.getLocale().toString()));
                        break;
                    }
                    if (langInfoReq.getKey().equals(descriptionKey)) {
                        jsonObject.put("description", values.get(LocaleContextHolder.getLocale().toString()));
                        break;
                    }
                }
            }
            result = jsonArray.toJSONString();
        } catch (Exception e) {
            LOGGER.warn("get allowedValues, parse error", e);
        }
        return result;
    }

    private void deletePropertyLangInfo(ServiceModuleProperty serviceModuleProperty) {
        try {
            DelLangInfoBaseReq delLangInfoBaseReq = new DelLangInfoBaseReq();
            delLangInfoBaseReq.setObjectId(serviceModuleProperty.getId());
            delLangInfoBaseReq.setObjectType(LangInfoObjectTypeEnum.property.name());
            List<String> keys = Lists.newArrayList();
            JSONArray jsonArray = JSON.parseArray(serviceModuleProperty.getAllowedValues());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nameKey = jsonObject.getString("name");
                String valueKey = jsonObject.getString("value");
                String descKey = jsonObject.getString("description");
                keys.add(nameKey);
                keys.add(valueKey);
                keys.add(descKey);
            }
            delLangInfoBaseReq.setKeys(keys);
            langInfoBaseApi.delLangInfoBase(delLangInfoBaseReq);
        } catch (Exception e) {
            LOGGER.warn("deletePropertyLangInfo error", e);
        }
    }
}
