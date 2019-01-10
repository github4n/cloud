package com.iot.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.mapper.ServiceModuleActionMapper;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.ServiceToAction;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.model.enums.PropertyStatusEnum;
import com.iot.device.model.enums.ReqParamTypeEnum;
import com.iot.device.model.enums.ReturnTypeEnum;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IServiceModuleActionService;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.vo.req.AddActionReq;
import com.iot.device.vo.req.ModuleIftttReq;
import com.iot.device.vo.req.ServiceModuleActionReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleIftttDataResp;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 模组-方法表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ServiceModuleActionServiceImpl extends ServiceImpl<ServiceModuleActionMapper, ServiceModuleAction> implements IServiceModuleActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleActionServiceImpl.class);

    @Autowired
    private IModuleActionToPropertyService moduleActionToPropertyService;

    @Autowired
    private ServiceModuleActionMapper serviceModuleActionMapper;

    @Autowired
    private IServiceToActionService iServiceToActionService;

    @Transactional
    public void delActionByActionId(Long actionId) {
        ServiceModuleCacheCoreUtils.removeServiceModuleActionCache(actionId);
        if (actionId == null) {
            LOGGER.info("actionId not null");
            return;
        }
        ServiceModuleAction action = super.selectById(actionId);
        if (action == null) {
            LOGGER.info("action not exist");
            return;
        }
        //删除方法对应属性
        moduleActionToPropertyService.delPropertiesByActionId(actionId);
        //删除方法
        super.deleteById(actionId);
    }

    @Transactional
    public ServiceModuleAction addOrUpdateAction(Long actionId) {
        if (actionId == null) {
            LOGGER.info("actionId not null");
            return null;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        ServiceModuleAction action = super.selectById(actionId);
        if (action == null) {
            LOGGER.info("action not exist");
            return null;
        }
        Long parentActionId;
        if (action.getParentId() == null) {
            parentActionId = actionId;
        } else {
            parentActionId = action.getParentId();
        }
        //copy add
        ServiceModuleAction tempAction = new ServiceModuleAction();
        BeanUtils.copyProperties(action, tempAction);
        tempAction.setId(null);
        tempAction.setParentId(parentActionId);//设置parentId by actionId
        tempAction.setCreateBy(userId);
        tempAction.setTenantId(tenantId);
        tempAction.setCreateTime(new Date());
        super.insert(tempAction);

        Long origActionId = actionId;
        Long targetActionId = tempAction.getId();
        moduleActionToPropertyService.addOrUpdatePropertiesByActionId(origActionId, targetActionId);
        return tempAction;
    }

    @Transactional
    public ServiceModuleAction addOrUpdateAction(AddActionReq action) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long parentActionId;
        if (action.getActionInfo().getParentId() == null) {
            ServiceModuleAction origAction = super.selectById(action.getActionInfo().getId());
            if (origAction == null) {
                LOGGER.info("action not exist");
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_ACTION_NOT_EXIST);
            }
            parentActionId = origAction.getId();
        } else {
            ServiceModuleAction origAction = super.selectById(action.getActionInfo().getParentId());
            if (origAction == null) {
                LOGGER.info("action not exist");
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_ACTION_NOT_EXIST);
            }
            parentActionId = origAction.getId();
        }
        //copy add
        ServiceModuleAction tempAction = new ServiceModuleAction();
        BeanUtils.copyProperties(action.getActionInfo(), tempAction);
        tempAction.setId(null);
        tempAction.setParentId(parentActionId);//设置parentId by actionId
        tempAction.setDevelopStatus(DevelopStatusEnum.OPERATING);
        tempAction.setCreateBy(userId);
        tempAction.setTenantId(tenantId);
        tempAction.setCreateTime(new Date());
        super.insert(tempAction);

        Long origActionId = parentActionId;
        Long targetActionId = tempAction.getId();

        //添加方法属性 --入参参数
        moduleActionToPropertyService.addOrUpdatePropertiesByActionId(origActionId, targetActionId, action.getParamPropertyList());
        //添加方法属性 --返回参数
        moduleActionToPropertyService.addOrUpdatePropertiesByActionId(origActionId, targetActionId, action.getReturnPropertyList());

        return tempAction;
    }

    @Override
    public Long saveOrUpdate(ServiceModuleActionReq serviceModuleActionReq) {
        ServiceModuleAction serviceModuleAction = null;
        if (serviceModuleActionReq.getId() != null && serviceModuleActionReq.getId() > 0) {
            serviceModuleAction = super.selectById(serviceModuleActionReq.getId());
            if (serviceModuleAction == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            ServiceModuleCacheCoreUtils.removeServiceModuleActionCache(serviceModuleActionReq.getId());
            serviceModuleAction.setUpdateTime(new Date());
            serviceModuleAction.setUpdateBy(serviceModuleActionReq.getUpdateBy());
        } else {
            serviceModuleAction = new ServiceModuleAction();
            serviceModuleAction.setUpdateTime(new Date());
            serviceModuleAction.setUpdateBy(serviceModuleActionReq.getUpdateBy());
            serviceModuleAction.setCreateTime(new Date());
            serviceModuleAction.setCreateBy(serviceModuleActionReq.getCreateBy());
        }
        serviceModuleAction.setParentId(null);
        serviceModuleAction.setTenantId(serviceModuleActionReq.getTenantId());
        serviceModuleAction.setServiceModuleId(serviceModuleActionReq.getServiceModuleId());
        serviceModuleAction.setVersion(serviceModuleActionReq.getVersion());
        serviceModuleAction.setName(serviceModuleActionReq.getName());
        serviceModuleAction.setCode(serviceModuleActionReq.getCode());
        serviceModuleAction.setTags(serviceModuleActionReq.getTags());
        serviceModuleAction.setApiLevel(serviceModuleActionReq.getApiLevel());
        if (serviceModuleActionReq.getDevelopStatus() != null) {
            if (serviceModuleActionReq.getDevelopStatus() == 0) {
                serviceModuleAction.setDevelopStatus(DevelopStatusEnum.IDLE);
            } else if (serviceModuleActionReq.getDevelopStatus() == 1) {
                serviceModuleAction.setDevelopStatus(DevelopStatusEnum.OPERATING);
            } else {
                serviceModuleAction.setDevelopStatus(DevelopStatusEnum.OPERATED);
            }
        }
//        if (serviceModuleActionReq.getPropertyStatus() != null) {
//            if (serviceModuleActionReq.getPropertyStatus() == 0) {
//                serviceModuleAction.setPropertyStatus(PropertyStatusEnum.OPTIONAL);
//            } else {
//                serviceModuleAction.setPropertyStatus(PropertyStatusEnum.MANDATORY);
//            }
//        }
        if (serviceModuleActionReq.getReqParamType() != null) {
            if (serviceModuleActionReq.getReqParamType() == 0) {
                serviceModuleAction.setReqParamType(ReqParamTypeEnum.OPTIONAL);
            } else {
                serviceModuleAction.setReqParamType(ReqParamTypeEnum.MANDATORY);
            }
        } else {
            moduleActionToPropertyService.delete(Lists.newArrayList(serviceModuleActionReq.getId()));
        }
        if (serviceModuleActionReq.getReturnType() != null) {
            if (serviceModuleActionReq.getReturnType() == 0) {
                serviceModuleAction.setReturnType(ReturnTypeEnum.OPTIONAL);
            } else {
                serviceModuleAction.setReturnType(ReturnTypeEnum.MANDATORY);
            }
        } else {
            moduleActionToPropertyService.delete(Lists.newArrayList(serviceModuleActionReq.getId()));
        }
        serviceModuleAction.setParams(serviceModuleActionReq.getParams());
        serviceModuleAction.setReturnDesc(serviceModuleActionReq.getReturnDesc());
        serviceModuleAction.setReturns(serviceModuleActionReq.getReturns());
        serviceModuleAction.setTestCase(serviceModuleActionReq.getTestCase());
        serviceModuleAction.setDescription(serviceModuleActionReq.getDescription());
        serviceModuleAction.setIftttType(serviceModuleActionReq.getIftttType());
        super.insertOrUpdate(serviceModuleAction);
        return serviceModuleAction.getId();
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            ServiceModuleCacheCoreUtils.batchRemoveServiceModuleActionCache(ids);
            //检查是否被关联
            ids.forEach(actionId -> {
                EntityWrapper wrapper = new EntityWrapper<>();
                wrapper.eq("module_action_id", actionId);
                int count = iServiceToActionService.selectCount(wrapper);
                if (count > 0) {
                    throw new BusinessException(ServiceModuleExceptionEnum.MODULE_ACTION_IS_USED);
                }
            });
            super.deleteBatchIds(ids);
            ArrayList<Long> idsResult = new ArrayList<Long>();
            moduleActionToPropertyService.listByModuleActionId(ids).forEach(m -> {
                idsResult.add(m.getId());
            });
            moduleActionToPropertyService.delete(idsResult);
            idsResult.clear();
            iServiceToActionService.listByModuleActionId(ids).forEach(n -> {
                idsResult.add(n.getId());
            });
            iServiceToActionService.delete(idsResult);
        }
    }

    @Override
    public PageInfo list(ServiceModuleActionReq serviceModuleActionReq) {
        Page<ServiceModuleAction> page = new Page<>(CommonUtil.getPageNum(serviceModuleActionReq), CommonUtil.getPageSize(serviceModuleActionReq));
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.isNull("parent_id");
        wrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        if (StringUtils.isNotEmpty(serviceModuleActionReq.getSearchValues())) {
            wrapper.andNew(true, "")
                    .like("name", serviceModuleActionReq.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("code", serviceModuleActionReq.getSearchValues(), SqlLike.DEFAULT);
        }
        wrapper.orderDesc(Arrays.asList("create_time"));
        List<ServiceModuleAction> list = serviceModuleActionMapper.selectPage(page, wrapper);
        List<ServiceModuleActionResp> respList = new ArrayList<>();
        list.forEach(m -> {
            ServiceModuleActionResp serviceModuleActionResp = new ServiceModuleActionResp();
            serviceModuleActionResp.setId(m.getId());
            serviceModuleActionResp.setParentId(m.getParentId());
            serviceModuleActionResp.setTenantId(m.getTenantId());
            serviceModuleActionResp.setServiceModuleId(m.getServiceModuleId());
            serviceModuleActionResp.setVersion(m.getVersion());
            serviceModuleActionResp.setName(m.getName());
            serviceModuleActionResp.setCode(m.getCode());
            serviceModuleActionResp.setTags(m.getTags());
            serviceModuleActionResp.setApiLevel(m.getApiLevel());
            serviceModuleActionResp.setDevelopStatus(m.getDevelopStatus() != null ? (Integer) m.getDevelopStatus().getValue() : null);
            serviceModuleActionResp.setReqParamType(m.getReqParamType() != null ? (Integer) m.getReqParamType().getValue() : null);
            serviceModuleActionResp.setReturnType(m.getReturnType() != null ? (Integer) m.getReturnType().getValue() : null);
//            serviceModuleActionResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModuleActionResp.setParams(m.getParams());
            serviceModuleActionResp.setReturnDesc(m.getReturnDesc());
            serviceModuleActionResp.setReturns(m.getReturns());
            serviceModuleActionResp.setTestCase(m.getTestCase());
            serviceModuleActionResp.setCreateTime(m.getCreateTime());
            serviceModuleActionResp.setUpdateTime(m.getUpdateTime());
            serviceModuleActionResp.setDescription(m.getDescription());
            serviceModuleActionResp.setActionId(m.getActionId());
            serviceModuleActionResp.setIftttType(m.getIftttType());
            respList.add(serviceModuleActionResp);
        });
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(respList);
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNum(serviceModuleActionReq.getPageNum());
        return pageInfo;
    }

    @Override
    public List<ServiceModuleActionResp> listByServiceModuleId(Long serviceModuleId) {
        //List<ServiceModuleAction> list = serviceModuleActionMapper.listByServiceModuleId(serviceModuleId, SaaSContextHolder.currentTenantId());
        List<Long> serviceModuleIds = new ArrayList<>();
        serviceModuleIds.add(serviceModuleId);
        List<ServiceModuleAction> list = getServiceModuleActionListByServiceModuleId(serviceModuleIds);
        List<ServiceModuleActionResp> respList = new ArrayList<>();
        String json = JSON.toJSONString(list);
        list.forEach(m -> {
            ServiceModuleActionResp serviceModuleActionResp = new ServiceModuleActionResp();
            serviceModuleActionResp.setId(m.getId());
            serviceModuleActionResp.setParentId(m.getParentId());
            serviceModuleActionResp.setTenantId(m.getTenantId());
            serviceModuleActionResp.setServiceModuleId(m.getServiceModuleId());
            serviceModuleActionResp.setVersion(m.getVersion());
            serviceModuleActionResp.setName(m.getName());
            serviceModuleActionResp.setCode(m.getCode());
            serviceModuleActionResp.setTags(m.getTags());
            serviceModuleActionResp.setApiLevel(m.getApiLevel());
            serviceModuleActionResp.setDevelopStatus(m.getDevelopStatus() != null ? (Integer) m.getDevelopStatus().getValue() : null);
            serviceModuleActionResp.setReqParamType(m.getReqParamType() != null ? (Integer) m.getReqParamType().getValue() : null);
            serviceModuleActionResp.setReturnType(m.getReturnType() != null ? (Integer) m.getReturnType().getValue() : null);
//            serviceModuleActionResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModuleActionResp.setParams(m.getParams());
            serviceModuleActionResp.setReturnDesc(m.getReturnDesc());
            serviceModuleActionResp.setReturns(m.getReturns());
            serviceModuleActionResp.setTestCase(m.getTestCase());
            serviceModuleActionResp.setCreateTime(m.getCreateTime());
            serviceModuleActionResp.setUpdateTime(m.getUpdateTime());
            serviceModuleActionResp.setDescription(m.getDescription());
            serviceModuleActionResp.setIftttType(m.getIftttType());
            serviceModuleActionResp.setActionId(m.getActionId());
            serviceModuleActionResp.setStatus(m.getStatus());
            respList.add(serviceModuleActionResp);
        });
        return respList;
    }

    /**
     * @return
     * @despriction：查找支持联动配置的方法
     * @author yeshiyuan
     * @created 2018/10/23 13:55
     */
    @Override
    public ModuleSupportIftttResp findSupportIftttActions(List<Long> serviceModuleIds, Long tenantId) {
        ModuleSupportIftttResp resp = new ModuleSupportIftttResp();
        List<ModuleIftttDataResp> ifList = new ArrayList<>();
        List<ModuleIftttDataResp> thenList = new ArrayList<>();
        List<ServiceModuleAction> actions = serviceModuleActionMapper.supportIftttAction(serviceModuleIds);
        actions.forEach(action -> {
            if (ModuleIftttTypeEnum.IF.getValue().equals(action.getIftttType())) {
                ModuleIftttDataResp dataResp = new ModuleIftttDataResp(action.getId(), action.getName(), ModuleIftttTypeEnum.checkChooseIf(action.getPortalIftttType()));
                ifList.add(dataResp);
            } else if (ModuleIftttTypeEnum.THEN.getValue().equals(action.getIftttType())) {
                ModuleIftttDataResp dataResp = new ModuleIftttDataResp(action.getId(), action.getName(), ModuleIftttTypeEnum.checkChooseThen(action.getPortalIftttType()));
                thenList.add(dataResp);
            } else if (ModuleIftttTypeEnum.IF_TEHN.getValue().equals(action.getIftttType())) {
                ModuleIftttDataResp ifData = new ModuleIftttDataResp(action.getId(), action.getName(), ModuleIftttTypeEnum.checkChooseIf(action.getPortalIftttType()));
                ifList.add(ifData);
                ModuleIftttDataResp thenData = new ModuleIftttDataResp(action.getId(), action.getName(), ModuleIftttTypeEnum.checkChooseThen(action.getPortalIftttType()));
                thenList.add(thenData);
            }
        });
        resp.setIfList(ifList);
        resp.setThenList(thenList);
        return resp;
    }

    /**
     * @return
     * @despriction：修改模组事件联动属性（portal使用）
     * @author yeshiyuan
     * @created 2018/10/23 17:19
     */
    @Transactional
    @Override
    public int updateActionSupportIfttt(UpdateModuleSupportIftttReq req) {
        int i = 0;
        if (req.getList() != null && !req.getList().isEmpty()) {
            List<ServiceModuleAction> actions = serviceModuleActionMapper.supportIftttAction(req.getServiceModuleIds());
            Map<Long, Integer> actionMap = actions.stream().collect(Collectors.toMap(ServiceModuleAction::getId, a -> a.getIftttType()));
            for (ModuleIftttReq o : req.getList()) {
                Integer portalIftttType = 0;
                if (o.getIfData() != null && o.getIfData().intValue() == 1) {
                    portalIftttType = ModuleIftttTypeEnum.IF.getValue();
                }
                if (o.getThenData() != null && o.getThenData().intValue() == 1) {
                    portalIftttType += ModuleIftttTypeEnum.THEN.getValue();
                }
                Integer iftttType = actionMap.get(o.getId());
                if (iftttType == null) {
                    continue;
                }
                if (portalIftttType.compareTo(iftttType) == 1) {
                    throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PORTAL_IFTTTTYPE_ERROR);
                }
                i += serviceModuleActionMapper.updatePortalIftttType(o.getId(), req.getTenantId(), portalIftttType, req.getUserId(), new Date());
            }
        }
        return i;
    }

    public List<ServiceModuleAction> getServiceModuleActionListByServiceModuleId(List<Long> serviceModuleIds) {
        List<ServiceModuleAction> actionList = new ArrayList<>();
        List<ServiceToAction> serviceToActionList = iServiceToActionService.getServiceToActionByServiceModuleId(serviceModuleIds);
        if (!CollectionUtils.isEmpty(serviceToActionList)) {
            List<Long> actionIds = serviceToActionList.stream().map(ServiceToAction::getModuleActionId).collect(Collectors.toList());
            Map<Long, ServiceToAction> serviceToActionMap = serviceToActionList.stream().collect(Collectors.toMap(ServiceToAction::getModuleActionId, action -> action, (k1, k2) -> k1));
            actionList = this.getServiceModuleActionListByIds(actionIds);
            if (actionList != null && actionList.size() > 0) {
                actionList.forEach(serviceModuleAction -> {
                    ServiceToAction serviceToAction = serviceToActionMap.get(serviceModuleAction.getId());
                    serviceModuleAction.setActionId(serviceToAction.getId());
                    serviceModuleAction.setStatus(serviceToAction.getStatus());
                });
            }
        }
        return actionList;
    }

    @Override
    public List<ServiceModuleAction> getServiceModuleActionListByIds(List<Long> ids) {
        List<ServiceModuleAction> result = new ArrayList<>();
        List<Long> noCacheIds = getServiceModuleActionListByIdsFromCache(ids, result);
        if (noCacheIds != null && noCacheIds.size() > 0) {
            List<ServiceModuleAction> dataList = getServiceModuleActionListByIdsFromSql(noCacheIds);
            if (dataList != null && dataList.size() > 0) {
                result.addAll(dataList);
            }
        }
        return result;
    }

    private List<Long> getServiceModuleActionListByIdsFromCache(List<Long> ids, List<ServiceModuleAction> result) {
        List<Long> noCacheIds = new ArrayList<>();
        List<ServiceModuleAction> cacheData = ServiceModuleCacheCoreUtils.batchGetServiceMouleActionCache(ids);
        if (cacheData != null && cacheData.size() > 0) {
            List<Long> cacheIds = cacheData.stream().map(ServiceModuleAction::getId).collect(Collectors.toList());
            noCacheIds = ids.stream().filter(id -> !cacheIds.contains(id)).collect(Collectors.toList());
            result.addAll(cacheData);
        } else {
            noCacheIds = ids;
        }
        return noCacheIds;
    }

    private List<ServiceModuleAction> getServiceModuleActionListByIdsFromSql(List<Long> ids) {
        EntityWrapper<ServiceModuleAction> actionWrapper = new EntityWrapper<>();
        actionWrapper.in("id", ids);
        List<ServiceModuleAction> actionList = this.selectList(actionWrapper);
        if (actionList != null && actionList.size() > 0) {
            /**
             * 放入缓存
             */
            ServiceModuleCacheCoreUtils.batchSetServiceMouleActionCache(actionList);
        }
        return actionList;
    }
}
