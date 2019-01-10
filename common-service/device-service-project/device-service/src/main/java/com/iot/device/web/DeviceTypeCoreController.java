package com.iot.device.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.business.DeviceTypeBusinessService;
import com.iot.device.model.DeviceType;
import com.iot.device.service.IDeviceTypeService;
import com.iot.device.vo.req.device.ListDeviceTypeReq;
import com.iot.device.vo.req.device.PageDeviceTypeByParamsReq;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:03 2018/9/25
 * @Modify by:
 */
@RestController
public class DeviceTypeCoreController implements DeviceTypeCoreApi {

    @Autowired
    private DeviceTypeBusinessService deviceTypeBusinessService;

    @Autowired
    private IDeviceTypeService deviceTypeService;

    @Override
    public GetDeviceTypeInfoRespVo get(@RequestParam("deviceTypeId") Long deviceTypeId) {
        GetDeviceTypeInfoRespVo resultData = null;
        DeviceType deviceType = deviceTypeBusinessService.getDeviceType(deviceTypeId);
        if (deviceType != null) {
            resultData = new GetDeviceTypeInfoRespVo();
            BeanUtils.copyProperties(deviceType, resultData);
        }
        return resultData;
    }

    @Override
    public List<ListDeviceTypeRespVo> listDeviceType(@RequestBody @Validated ListDeviceTypeReq params) {
        List<ListDeviceTypeRespVo> resultDataList = Lists.newArrayList();
        List<DeviceType> deviceTypeList = deviceTypeBusinessService.listBatchDeviceTypes(params.getDeviceTypeIds());
        if (!CollectionUtils.isEmpty(deviceTypeList)) {
            deviceTypeList.forEach(deviceType -> {
                ListDeviceTypeRespVo target = new ListDeviceTypeRespVo();
                BeanUtils.copyProperties(deviceType, target);
                resultDataList.add(target);
            });
        }
        return resultDataList;
    }

    @Override
    public Page<ListDeviceTypeRespVo> pageDeviceType(@RequestBody PageDeviceTypeByParamsReq params) {

        EntityWrapper wrapper = new EntityWrapper();
        if (params.getTenantId() != null) {
            wrapper.eq("tenant_id", params.getTenantId());
        }
        com.baomidou.mybatisplus.plugins.Page<DeviceType> deviceTypePage = deviceTypeService.selectPage(new com.baomidou.mybatisplus.plugins.Page<>(params.getPageNumber(), params.getPageSize()), wrapper);
        List<ListDeviceTypeRespVo> resultDataList = Lists.newArrayList();
        if (deviceTypePage.getTotal() > 0) {
            deviceTypePage.getRecords().forEach(deviceType -> {
                ListDeviceTypeRespVo target = new ListDeviceTypeRespVo();
                BeanUtils.copyProperties(deviceType, target);
                resultDataList.add(target);

            });
        }
        Page<ListDeviceTypeRespVo> resultDataPage = new Page<>(params.getPageNumber(), params.getPageSize());
        resultDataPage.setResult(resultDataList);
        resultDataPage.setTotal(deviceTypePage.getTotal());
        resultDataPage.setPages(deviceTypePage.getPages());

        return resultDataPage;
    }
}
