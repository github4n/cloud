package com.iot.device.controller;


import com.iot.device.api.ServiceToActionApi;
import com.iot.device.api.ServiceToEventApi;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.device.vo.req.ServiceToEventReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author zhangyue
 * @since 2018-06-27
 */
@RestController
public class ServiceToEventController implements ServiceToEventApi {

    @Autowired
    private IServiceToEventService iServiceToEventService;


    @Override
    public void save(@RequestBody ServiceToEventReq serviceToEventReq) {
        iServiceToEventService.save(serviceToEventReq);
    }

    @Override
    public void saveMore(@RequestBody ServiceToEventReq serviceToEventReq) {
        iServiceToEventService.saveMore(serviceToEventReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iServiceToEventService.delete(ids);
    }

    @Override
    public void update(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        iServiceToEventService.update(id,status);
    }
}

