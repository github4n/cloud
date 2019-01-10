package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ServiceToEvent;
import com.iot.device.vo.req.AddEventReq;
import com.iot.device.vo.req.ServiceToEventReq;
import com.iot.device.vo.rsp.ServiceToEventRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-to-事件表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceToEventService extends IService<ServiceToEvent> {

    void addOrUpdateEventsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId);

    void delEventsByServiceModuleId(Long serviceModuleId);

    void addOrUpdateEventsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId, List<AddEventReq> eventList);

    void save(ServiceToEventReq serviceToEventReq);

    void saveMore(ServiceToEventReq serviceToEventReq);

    void delete(ArrayList<Long> ids);

    List<ServiceToEventRsp> listByModuleEventId(ArrayList<Long> ids);

    List<ServiceToEventRsp> listByServiceModuleId(ArrayList<Long> ids);

    void update(Long id, Integer status);

    List<ServiceToEvent> getServiceToEventListByServiceModuleId(ArrayList<Long> ids);

}
