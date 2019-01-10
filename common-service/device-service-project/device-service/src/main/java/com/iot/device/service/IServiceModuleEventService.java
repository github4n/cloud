package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.iot.device.model.ServiceModuleEvent;
import com.iot.device.vo.req.AddEventReq;
import com.iot.device.vo.req.ServiceModuleEventReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-事件表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceModuleEventService extends IService<ServiceModuleEvent> {


    void delEventByEventId(Long eventId);

    ServiceModuleEvent addOrEvent(Long origEventId);

    ServiceModuleEvent addOrEvent(AddEventReq eventReq);

    Long saveOrUpdate(ServiceModuleEventReq serviceModuleEventReq);

    void delete(ArrayList<Long> ids);

    PageInfo list(ServiceModuleEventReq serviceModuleEventReq);

    List<ServiceModuleEventResp> listByServiceModuleId(Long serviceModuleId);

    /**
     * @despriction：查找支持联动配置的事件
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    ModuleSupportIftttResp findSupportIftttEvents(List<Long> serviceModuleIds, Long tenantId);

    /**
     * @despriction：修改模组事件联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    int updateEventSupportIfttt(UpdateModuleSupportIftttReq req);
}
