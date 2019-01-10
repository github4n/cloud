package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ServiceToAction;
import com.iot.device.vo.req.AddActionReq;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.device.vo.rsp.ServiceToActionRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-to-方法表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceToActionService extends IService<ServiceToAction> {


    void addOrUpdateActionsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId);

    void delActionsByServiceModuleId(Long serviceModuleId);

    void addOrUpdateActionsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId, List<AddActionReq> actionList);

    void save(ServiceToActionReq serviceToActionReq);

    void saveMore(ServiceToActionReq serviceToActionReq);

    void delete(ArrayList<Long> ids);

    List<ServiceToActionRsp> listByModuleActionId(ArrayList<Long> ids);

    List<ServiceToActionRsp> listByServiceModuleId(ArrayList<Long> ids);

    void update(Long id,Integer status);

    /**
     * 根据serviceModuleId获取ServiceToAction --list 走缓存
     * @param serviceModuleIds
     * @return
     */
    List<ServiceToAction> getServiceToActionByServiceModuleId(List<Long> serviceModuleIds);

}
