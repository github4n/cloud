package com.iot.device.controller;

import com.iot.device.api.ServiceStyleToTemplateApi;
import com.iot.device.service.IServiceStyleToTemplateService;
import com.iot.device.vo.req.ServiceStyleToTemplateReq;
import com.iot.device.vo.rsp.ServiceStyleToTemplateResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ServiceStyleToTemplateController implements ServiceStyleToTemplateApi {

    @Autowired
    private IServiceStyleToTemplateService iServiceStyleToTemplateService;


    @Override
    public Long saveOrUpdate(@RequestBody ServiceStyleToTemplateReq serviceStyleToTemplateReq) {
        return iServiceStyleToTemplateService.saveOrUpdate(serviceStyleToTemplateReq);
    }

    @Override
    public void saveMore(@RequestBody ServiceStyleToTemplateReq serviceStyleToTemplateReq) {
        iServiceStyleToTemplateService.saveMore(serviceStyleToTemplateReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iServiceStyleToTemplateService.delete(ids);
    }

    @Override
    public List<ServiceStyleToTemplateResp> list(@RequestBody ArrayList<Long> moduleStyleIds) {
        return iServiceStyleToTemplateService.list(moduleStyleIds);
    }
}
