package com.iot.portal.web.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.rsp.DeviceTypeListResp;
import com.iot.portal.service.FileService;
import com.iot.portal.web.vo.FileResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:30 2018/6/29
 * @Modify by:
 */
@Api(value = "portal-产品类别管理", description = "portal-产品类别管理")
@RestController
@RequestMapping("/portal/type")
public class PortalTypeController {

    private static Logger logger = LoggerFactory.getLogger(PortalTypeController.class);
    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @Autowired
    private FileService fileService;

    @ApiOperation("根据类目id获取所有设备类型列表")
    @RequestMapping(value = "/findDeviceTypeListByCatalogId", method = RequestMethod.GET)
    public CommonResponse<List<DeviceTypeListResp>> findDeviceTypeListByCatalogId(@RequestParam(value = "catalogId") Long catalogId) {
        AssertUtils.notNull(catalogId, "catalogId.notnull");
        List<DeviceTypeListResp> typeListRespList = deviceTypeApi.findDeviceTypeListByCatalogId(catalogId);
        if (CollectionUtils.isEmpty(typeListRespList)) {
            return CommonResponse.success();
        }
        typeListRespList.forEach(m->{
            if (StringUtils.isNotBlank(m.getImg())){
                try {
                    FileResp fileResp = fileService.getUrl(m.getImg());
                    m.setImg(fileResp.getUrl());
                } catch (Exception e) {
                    logger.info("get file error.");
                }
            } else {
                m.setImg(null);
            }
        });
        return CommonResponse.success(typeListRespList);
    }

    @ApiOperation("根据获取所有设备类型列表")
    @RequestMapping(value = "/findDeviceTypeList", method = RequestMethod.GET)
    public CommonResponse<List<DeviceTypeListResp>> findDeviceTypeList() {
        logger.info("findDeviceTypeList--begin.");
        List<DeviceTypeListResp> typeListRespList = deviceTypeApi.findAllDeviceTypeList();
        if(CollectionUtils.isEmpty(typeListRespList)){
            return CommonResponse.success();
        }
        typeListRespList.forEach(m->{
            if (StringUtils.isNotBlank(m.getImg())){
                try {
                    FileResp fileResp = fileService.getUrl(m.getImg());
                    m.setImg(fileResp.getUrl());
                } catch (Exception e) {
                    logger.info("get file error.");
                }
            } else {
                m.setImg(null);
            }
        });
        return CommonResponse.success(typeListRespList);
    }
}
