package com.iot.control.device.controller;

import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.core.IUserDeviceBusinessService;
import com.iot.control.device.entity.UserDevice;
import com.iot.control.device.service.IUserDeviceService;
import com.iot.control.device.vo.req.DelUserDeviceInfoReq;
import com.iot.control.device.vo.req.GetUserDeviceInfoReq;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import com.iot.control.device.vo.req.PageUserDeviceInfoReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.PageUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:39 2018/10/12
 * @Modify by:
 */
@Slf4j
@RestController
public class UserDeviceController implements UserDeviceCoreApi {

    @Autowired
    private IUserDeviceBusinessService userDeviceBusinessService;

    @Autowired
    private IUserDeviceService userDeviceService;


    @Override
    public UpdateUserDeviceInfoResp saveOrUpdate(@RequestBody @Validated UpdateUserDeviceInfoReq params) {

        UpdateUserDeviceInfoResp resultData = userDeviceBusinessService.saveOrUpdate(params);
        return resultData;
    }

    @Override
    public List<UpdateUserDeviceInfoResp> saveOrUpdateBatch(@RequestBody @Validated List<UpdateUserDeviceInfoReq> paramsList) {
        List<UpdateUserDeviceInfoResp> resultDataList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(paramsList)) {
            userDeviceBusinessService.saveOrUpdateBatch(paramsList);
        }
        return resultDataList;
    }

    public List<ListUserDeviceInfoRespVo> listUserDevice(@RequestBody @Validated ListUserDeviceInfoReq params) {
        List<ListUserDeviceInfoRespVo> resultDataList = Lists.newArrayList();
        List<UserDevice> userDeviceList = Lists.newArrayList();
        if (StringUtils.isEmpty(params.getUserType()) && params.getTenantId() != null && params.getOrgId() == null) {//userType 是否为空 不为空业务偏复杂 直接查走db
            if (StringUtils.isEmpty(params.getDeviceId()) && params.getUserId() != null) {//如果设备为null 则查用户下的list userDevice
                //device为空的时候 userId 不为空
                userDeviceList = userDeviceBusinessService.listBatchUserDevice(params.getTenantId(), null, params.getUserId());
            } else if ((!StringUtils.isEmpty(params.getDeviceId())) && params.getUserId() == null) {//设备不为空+用户为空
                UserDevice targetUserDevice =
                        userDeviceBusinessService.getUserDevice(params.getTenantId(), params.getDeviceId());
                if (targetUserDevice != null) {
                    userDeviceList.add(targetUserDevice);
                }
            } else if ((!StringUtils.isEmpty(params.getDeviceId())) && params.getUserId() != null) {//用户 +设备都不为空
                UserDevice targetUserDevice =
                        userDeviceBusinessService.getUserDevice(params.getTenantId(), null, params.getUserId(), params.getDeviceId());
                if (targetUserDevice != null) {
                    userDeviceList.add(targetUserDevice);
                }
            } else {
                //非业务走db
                userDeviceList = userDeviceService.getByParams(params.getTenantId(), params.getOrgId(), params.getUserId(), params.getDeviceId(), params.getUserType());
            }
        } else {
            //userType not null 或tenantId 有为空的情况  直接走db
            userDeviceList = userDeviceService.getByParams(params.getTenantId(), params.getOrgId(), params.getUserId(), params.getDeviceId(), params.getUserType());
        }
        if (CollectionUtils.isEmpty(userDeviceList)) {
            return resultDataList;
        }
        userDeviceList.forEach(userDevice -> {
            ListUserDeviceInfoRespVo target = new ListUserDeviceInfoRespVo();
            BeanUtils.copyProperties(userDevice, target);
            resultDataList.add(target);
        });
        return resultDataList;
    }

    @Override
    public List<ListUserDeviceInfoRespVo> listBatchUserDevice(@RequestBody @Validated GetUserDeviceInfoReq params) {
        List<ListUserDeviceInfoRespVo> resultDataList = Lists.newArrayList();
        //获取用户对应的设备
        List<UserDevice> userDevices = userDeviceBusinessService.listBatchUserDevice(params.getTenantId(), params.getOrgId(), params.getUserId(), params.getDeviceIds());
        if (!CollectionUtils.isEmpty(userDevices)) {
            userDevices.forEach(userDevice -> {
                ListUserDeviceInfoRespVo target = new ListUserDeviceInfoRespVo();
                BeanUtils.copyProperties(userDevice, target);
                resultDataList.add(target);
            });
        }
        return resultDataList;
    }

    @Override
    public void delUserDevice(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId
            , @RequestParam("deviceId") String deviceId) {

        userDeviceBusinessService.delUserDevice(tenantId, userId, deviceId);
    }

    @Override
    public void delBatchUserDevice(@RequestBody @Validated DelUserDeviceInfoReq params) {
        if (CollectionUtils.isEmpty(params.getSubDeviceIds())) {
            return;
        }
        //TODO 优化成批量
        Long tenantId = params.getTenantId();
        Long userId = params.getUserId();
        params.getSubDeviceIds().forEach(deviceId ->{
            userDeviceBusinessService.delUserDevice(tenantId, userId, deviceId);
        });
    }

    @Override
    public Page<PageUserDeviceInfoRespVo> pageUserDevice(@RequestBody @Validated PageUserDeviceInfoReq params) {

        Page<PageUserDeviceInfoRespVo> pageResult = new Page<>();
        pageResult.setPageSize(params.getPageSize());
        pageResult.setPageNum(params.getPageNumber());
        com.baomidou.mybatisplus.plugins.Page<UserDevice> pageUserDevice = userDeviceService.findPageByParams(params.getPageNumber(), params.getPageSize(), params.getTenantId(), params.getOrgId(), params.getUserId());
        pageResult.setPages(pageUserDevice.getPages());

        List<PageUserDeviceInfoRespVo> resultDataList = Lists.newArrayList();
        if (pageUserDevice.getSize() > 0) {
            pageUserDevice.getRecords().forEach(userDevice -> {
                PageUserDeviceInfoRespVo target = new PageUserDeviceInfoRespVo();
                BeanUtils.copyProperties(userDevice, target);
                resultDataList.add(target);
            });
        }
        pageResult.setResult(resultDataList);
        return null;
    }
}
