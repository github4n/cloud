package com.iot.portal.web.controller;

import com.github.pagehelper.PageInfo;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.vo.rsp.StyleTemplateResp;
import com.iot.portal.service.FileService;
import com.iot.portal.web.vo.FileResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "portal-样式管理", description = "portal-样式管理")
@RestController
@RequestMapping("/portal/style/template")
public class PortalStyleTemplateController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PortalStyleTemplateController.class);

    @Autowired
    private StyleTemplateApi styleTemplateApi;

    @Autowired
    private FileService fileService;

    @ApiOperation("获取样式模板")
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    public CommonResponse list(){
        PageInfo<StyleTemplateResp> list = styleTemplateApi.list(null);
        list.getList().forEach(m->{
            try {
                FileResp fileResp = fileService.getUrl(m.getImg());
                m.setImg(fileResp.getUrl());
            } catch (Exception e) {
                m.setImg("");
                LOGGER.warn("get style img error", e);
            }
        });

        return CommonResponse.success(list);
    }

    @ApiOperation("根据deviceTypeId获取样式")
    @RequestMapping(value = "/listByDeviceTypeId" , method = RequestMethod.GET)
    public CommonResponse listByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId){
        AssertUtils.notNull(deviceTypeId, "deviceTypeId.notnull");
        List<StyleTemplateResp> list = styleTemplateApi.listByDeviceTypeId(deviceTypeId);
        list.forEach(m->{
            if (StringUtils.isNotBlank(m.getImg())){
                try {
                    FileResp fileResp = fileService.getUrl(m.getImg());
                    m.setImg(fileResp.getUrl());
                } catch (Exception e) {
                    m.setImg("");
                    LOGGER.warn("get style img error", e);
                }
            }
        });
        return CommonResponse.success(list);
    }

}
