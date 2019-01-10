package com.iot.device.controller;


import com.github.pagehelper.PageInfo;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.service.IStyleTemplateService;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.StyleTemplateResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StyleTemplateController implements StyleTemplateApi {

    @Autowired
    private IStyleTemplateService iStyleTemplateService;

    @Override
    public Long saveOrUpdate(@RequestBody StyleTemplateReq styleTemplateReq) {
        return iStyleTemplateService.saveOrUpdate(styleTemplateReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iStyleTemplateService.delete(ids);
    }


    @Override
    public PageInfo<StyleTemplateResp> list(@RequestBody StyleTemplateReq styleTemplateReq) {
        return iStyleTemplateService.list(styleTemplateReq);
    }


    @Override
    public List<StyleTemplateResp> listByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return iStyleTemplateService.listByDeviceTypeId(deviceTypeId);
    }

    @Override
    public List<StyleTemplateResp> listByModuleStyleId(@RequestParam("moduleStyleId") Long moduleStyleId) {
        return iStyleTemplateService.listByModuleStyleId(moduleStyleId);
    }

    @Override
    public List<StyleTemplateResp> listByProductId(@RequestParam("productId") Long productId) {
        return iStyleTemplateService.listByProductId(productId);
    }

    @Override
    public StyleTemplateResp infoById(@RequestParam("id") Long id) {
        return iStyleTemplateService.infoById(id);
    }
}
