package com.iot.device.controller;

import com.iot.device.api.ServiceModuleStyleApi;
import com.iot.device.service.IServiceModuleStyleService;
import com.iot.device.vo.req.ServiceModuleStyleReq;
import com.iot.device.vo.rsp.ServiceModuleStyleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ServiceModuleStyleController implements ServiceModuleStyleApi {

    @Autowired
    private IServiceModuleStyleService iServiceModuleStyleService;


    @Override
    public Long saveOrUpdate(@RequestBody ServiceModuleStyleReq serviceModuleStyleReq) {
        return iServiceModuleStyleService.saveOrUpdate(serviceModuleStyleReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iServiceModuleStyleService.delete(ids);
    }

    @Override
    public List<ServiceModuleStyleResp> list(@RequestBody ArrayList<Long> serviceModuleId) {
        return iServiceModuleStyleService.list(serviceModuleId);
    }

    @Override
    public List<ServiceModuleStyleResp> listByStyleTemplateId(@RequestParam("styleTemplateId") Long styleTemplateId) {
        return iServiceModuleStyleService.listByStyleTemplateId(styleTemplateId);
    }
}
