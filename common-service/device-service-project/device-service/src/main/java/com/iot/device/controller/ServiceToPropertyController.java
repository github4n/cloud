package com.iot.device.controller;


import com.iot.device.api.ServiceToEventApi;
import com.iot.device.api.ServiceToPropertyApi;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.service.IServiceToPropertyService;
import com.iot.device.vo.req.ServiceToEventReq;
import com.iot.device.vo.req.ServiceToPropertyReq;
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
public class ServiceToPropertyController implements ServiceToPropertyApi {

    @Autowired
    private IServiceToPropertyService iServiceToPropertyService;

    @Override
    public void save(@RequestBody ServiceToPropertyReq serviceToPropertyReq) {
        iServiceToPropertyService.save(serviceToPropertyReq);
    }

    @Override
    public void saveMore(@RequestBody ServiceToPropertyReq serviceToPropertyReq) {
        iServiceToPropertyService.saveMore(serviceToPropertyReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iServiceToPropertyService.delete(ids);
    }

    @Override
    public void update(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        iServiceToPropertyService.update(id,status);
    }
}

