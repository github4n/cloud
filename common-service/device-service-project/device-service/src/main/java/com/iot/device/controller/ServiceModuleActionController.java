package com.iot.device.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.device.api.ServiceActionApi;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.ServiceToAction;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IServiceModuleActionService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.service.core.ModuleCoreService;
import com.iot.device.vo.req.ServiceModuleActionReq;
import com.iot.device.vo.req.UpdateActionInfoReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.device.vo.rsp.product.ParentVO;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 模组-方法表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
public class ServiceModuleActionController implements ServiceActionApi {

    @Autowired
    private ModuleCoreService moduleCoreService;

    @Autowired
    private IServiceModuleActionService serviceModuleActionService;

    @Autowired
    private IServiceToActionService serviceToActionService;

    @Autowired
    private IModuleActionToPropertyService moduleActionToPropertyService;

    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;


    /**
     * 通过模组id获取方法列表
     *
     * @param serviceModuleId
     * @return
     * @author lucky
     * @date 2018/6/29 17:09
     */
    public List<ServiceModuleActionResp> findActionListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        EntityWrapper<ServiceToAction> saWrapper = new EntityWrapper<>();
        saWrapper.eq("service_module_id", serviceModuleId);
        List<ServiceToAction> serviceToActionList = serviceToActionService.selectList(saWrapper);
        if (CollectionUtils.isEmpty(serviceToActionList)) {
            return null;
        }
        List<Long> actionIds = Lists.newArrayList();
        Map<Long, ServiceToAction> serviceToActionMap = Maps.newHashMap();
        for (ServiceToAction sa : serviceToActionList) {
            Long actionId = sa.getModuleActionId();
            actionIds.add(actionId);
            serviceToActionMap.put(actionId, sa);
        }


        EntityWrapper<ServiceModuleAction> actionWrapper = new EntityWrapper<>();
        actionWrapper.in("id", actionIds);
        List<ServiceModuleAction> actionList = serviceModuleActionService.selectList(actionWrapper);
        if (CollectionUtils.isEmpty(actionList)) {
            return null;
        }
        List<ServiceModuleActionResp> respList = Lists.newArrayList();
        List<Long> parentActionIds = Lists.newArrayList();
        for (ServiceModuleAction action : actionList) {
            parentActionIds.add(action.getParentId());
            ServiceModuleActionResp target = new ServiceModuleActionResp();
            BeanCopyUtils.copyModuleAction(action, target);

            //可选必选
            ServiceToAction serviceToAction = serviceToActionMap.get(action.getId());
            if (serviceToAction!=null){
                //    propertyStatus 属性状态，0:可选,1:必选")
                target.setPropertyStatus(serviceToAction.getStatus());
            }
            respList.add(target);
        }
        // parentAction
        List<ServiceModuleAction> parentActionList = serviceModuleActionService.selectBatchIds(parentActionIds);
        for (ServiceModuleActionResp moduleActionResp : respList) {
            for (ServiceModuleAction parentAction : parentActionList) {
                if (moduleActionResp.getParentId() != null && moduleActionResp.getParentId().equals(parentAction.getId())) {
                    ParentVO parentVO = new ParentVO();
                    parentVO.setId(parentAction.getId());
                    parentVO.setName(parentAction.getName());
                    parentVO.setCode(parentAction.getCode());
                    moduleActionResp.setParent(parentVO);
                }
            }
        }
        return respList;
    }

    @Override
    public ServiceModuleActionResp getActionInfoByActionId(@RequestParam("actionId") Long actionId) {
        AssertUtils.notNull(actionId, "actionId.notnull");
        ServiceModuleAction action = serviceModuleActionService.selectById(actionId);
        if (action == null) {
            return null;
        }
        ServiceModuleActionResp target = new ServiceModuleActionResp();
        BeanCopyUtils.copyModuleAction(action, target);
        return target;
    }

    @Override
    public List<ServiceModuleActionResp> listActionInfoByActionIds(@RequestParam("actionIds") List<Long> actionIds) {
        List<ServiceModuleActionResp> result = Lists.newArrayList();
        List<ServiceModuleAction> actions = serviceModuleActionService.selectBatchIds(actionIds);
        if (CollectionUtils.isEmpty(actions)) {
            return result;
        }
        for (ServiceModuleAction action : actions) {
            ServiceModuleActionResp target = new ServiceModuleActionResp();
            BeanCopyUtils.copyModuleAction(action, target);
        }
        return result;
    }

    public void updateActionInfo(UpdateActionInfoReq actionInfoReq) {
        AssertUtils.notNull(actionInfoReq, "actionInfoReq.notnull");
        AssertUtils.notNull(actionInfoReq.getActionReq(), "action.notnull");
        AssertUtils.notEmpty(actionInfoReq.getPropertyReqList(), "properties.notnull");
        moduleCoreService.updateActionInfo(actionInfoReq);
    }

    @Override
    public Long saveOrUpdate(@RequestBody ServiceModuleActionReq serviceModuleActionReq) {
        return serviceModuleActionService.saveOrUpdate(serviceModuleActionReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        serviceModuleActionService.delete(ids);
    }

    @Override
    public PageInfo list(@RequestBody ServiceModuleActionReq serviceModuleActionReq) {
        return serviceModuleActionService.list(serviceModuleActionReq);
    }

    @Override
    public List<ServiceModuleActionResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        return serviceModuleActionService.listByServiceModuleId(serviceModuleId);
    }

    /**
     * @despriction：查找支持联动配置的方法
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @Override
    public ModuleSupportIftttResp findSupportIftttActions(@RequestParam("serviceModuleIds") List<Long> serviceModuleIds, @RequestParam("tenantId") Long tenantId) {
        return serviceModuleActionService.findSupportIftttActions(serviceModuleIds, tenantId);
    }

    /**
     * @despriction：修改模组事件联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    @Override
    public int updateActionSupportIfttt(@RequestBody UpdateModuleSupportIftttReq req) {
        return serviceModuleActionService.updateActionSupportIfttt(req);
    }
}

