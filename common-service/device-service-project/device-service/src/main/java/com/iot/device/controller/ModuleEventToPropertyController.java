package com.iot.device.controller;


import com.iot.device.api.ModuleActionToPropertyApi;
import com.iot.device.api.ModuleEventToPropertyApi;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author zhangyue
 * @since 2018-06-27
 */
@RestController
public class ModuleEventToPropertyController implements ModuleEventToPropertyApi {

    @Autowired
    private IModuleEventToPropertyService iModuleEventToPropertyService;


    @Override
    public void save(@RequestBody ModuleEventToPropertyReq moduleEventToPropertyReq) {
        iModuleEventToPropertyService.save(moduleEventToPropertyReq);
    }

    @Override
    public void saveMore(@RequestBody ModuleEventToPropertyReq moduleEventToPropertyReq) {
        iModuleEventToPropertyService.saveMore(moduleEventToPropertyReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iModuleEventToPropertyService.delete(ids);
    }
}

