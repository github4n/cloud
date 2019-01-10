package com.iot.device.controller;


import com.iot.device.api.ModuleEventToPropertyApi;
import com.iot.device.api.ServiceToActionApi;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.req.ServiceToActionReq;
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
public class ServiceToActionController implements ServiceToActionApi {

    @Autowired
    private IServiceToActionService iServiceToActionService;


    @Override
    public void save(@RequestBody ServiceToActionReq serviceToActionReq) {
        iServiceToActionService.save(serviceToActionReq);
    }

    @Override
    public void saveMore(@RequestBody ServiceToActionReq serviceToActionReq) {
        iServiceToActionService.saveMore(serviceToActionReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iServiceToActionService.delete(ids);
    }

    @Override
    public void update(@RequestParam("id") Long id,@RequestParam("status") Integer status) {
        iServiceToActionService.update(id,status);
    }
}

