package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.vo.req.AddActionReq;
import com.iot.device.vo.req.ServiceModuleActionReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleActionResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-方法表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceModuleActionService extends IService<ServiceModuleAction> {


    void delActionByActionId(Long actionId);

    ServiceModuleAction addOrUpdateAction(Long moduleActionId);

    ServiceModuleAction addOrUpdateAction(AddActionReq action);

    Long saveOrUpdate(ServiceModuleActionReq serviceModuleActionReq);

    void delete(ArrayList<Long> ids);

    PageInfo list(ServiceModuleActionReq serviceModuleActionReq);

    List<ServiceModuleActionResp> listByServiceModuleId(Long serviceModuleId);

    /**
      * @despriction：查找支持联动配置的方法
      * @author  yeshiyuan
      * @created 2018/10/23 13:55
      * @return
      */
    ModuleSupportIftttResp findSupportIftttActions(List<Long> serviceModuleIds, Long tenantId);

    /**
     * @despriction：修改模组方法联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
   int updateActionSupportIfttt(UpdateModuleSupportIftttReq req);

    List<ServiceModuleAction> getServiceModuleActionListByIds(List<Long> ids);

}
