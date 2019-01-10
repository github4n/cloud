package com.iot.center.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.iot.center.annotation.PermissionAnnotation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.device.vo.DeviceRemoteTemplatePageReq;
import com.iot.building.device.vo.DeviceRemoteTemplateReq;
import com.iot.building.device.vo.DeviceRemoteTemplateResp;
import com.iot.building.device.vo.DeviceRemoteTemplateSimpleResp;
import com.iot.center.enums.DeviceRemoteControlParamEnum;
import com.iot.center.enums.DeviceRemoteControlTypeEnum;
import com.iot.center.service.DeviceRemoteService;
import com.iot.center.vo.DeviceRemoteControlParamVO;
import com.iot.center.vo.DeviceRemoteControlTypeVO;
import com.iot.center.vo.DictVO;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.user.vo.LoginResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板相关
 */
@Api(tags = "遥控器相关接口")
@RestController
@RequestMapping(value = "/deviceRemote")
public class DeviceRemoteTemplateController {

    @Autowired
    private DeviceRemoteService deviceRemoteService;

    @ApiOperation("新增遥控器模板")
    @RequestMapping(value = "/addDeviceRemoteTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addDeviceRemoteTemplate(@RequestBody DeviceRemoteTemplateReq deviceRemoteTemplateReq) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            if(user!=null){
                deviceRemoteTemplateReq.setCreateBy(user.getUserId().toString());
                deviceRemoteTemplateReq.setUpdateBy(user.getUserId().toString());
            }
            deviceRemoteService.addDeviceRemoteTemplate(deviceRemoteTemplateReq);
            return new CommonResponse(ResultMsg.SUCCESS.getCode(), ResultMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("更新遥控器模板")
    @RequestMapping(value = "/updateDeviceRemoteTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateDeviceRemoteTemplate(@RequestBody DeviceRemoteTemplateReq deviceRemoteTemplateReq) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            if(user!=null){
                deviceRemoteTemplateReq.setUpdateBy(user.getUserId().toString());
            }
            deviceRemoteService.updateDeviceRemoteTemplate(deviceRemoteTemplateReq);
            return new CommonResponse(ResultMsg.SUCCESS);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("删除遥控器模板")
    @RequestMapping(value = "/deleteDeviceRemoteTemplate", method = RequestMethod.POST)
    public CommonResponse deleteDeviceRemoteTemplate(@RequestParam(value = "id",required = true) Long id) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            Long userId=0L;
            if(user!=null){
                userId=user.getUserId();
            }
            deviceRemoteService.deleteDeviceRemoteTemplate(user.getTenantId(), id, userId);
            return new CommonResponse(ResultMsg.SUCCESS);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("获取遥控器模板详情")
    @RequestMapping(value = "/getDeviceRemoteTemplateById", method = RequestMethod.GET)
    public CommonResponse getDeviceRemoteTemplateById(@RequestParam(value = "id",required = true) Long id) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            return new CommonResponse(ResultMsg.SUCCESS).data(deviceRemoteService.getDeviceRemoteTemplateById(user.getTenantId(), id));
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @PermissionAnnotation(value = "REMOTE_CONTROL")
    @ApiOperation("获取遥控器分页")
    @RequestMapping(value = "/pageDeviceRemoteTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<DeviceRemoteTemplateResp> pageDeviceRemoteTemplatePage(@RequestBody DeviceRemoteTemplatePageReq pageReq) {
        try {
            Page<DeviceRemoteTemplateSimpleResp> page = deviceRemoteService.pageDeviceRemoteTemplatePage(pageReq);
            return new CommonResponse(ResultMsg.SUCCESS).data(page);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("获取详情页的全部下拉款")
    @RequestMapping(value = "/getAllComboboxs", method = RequestMethod.GET)
    public CommonResponse getAllComboboxs(
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) Long remoteId) {
        try {
//            if (tenantId == null) {
//                tenantId = SaaSContextHolder.currentTenantId();
//            }
            Map<String,Object> datas=new HashMap<>();
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            //获取遥控器类型
            datas.put("listDeviceRemoteType",deviceRemoteService.listDeviceRemoteType(tenantId).stream().map(one -> new DictVO(one.getId(), one.getName())).collect(Collectors.toList()));
            //获取遥控器的用途
            datas.put("listDeviceRemoteBusinessType",deviceRemoteService.listDeviceRemoteBusinessType(user.getOrgId(),tenantId,remoteId));
            //获取遥控器按键功能
            List<DeviceRemoteControlTypeVO> deviceRemoteControlTypeVOS = new ArrayList<>();
            deviceRemoteControlTypeVOS.add(new DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum.OFF));
            deviceRemoteControlTypeVOS.add(new DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum.ON));
            deviceRemoteControlTypeVOS.add(new DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum.SCENE_SITHCH));
            deviceRemoteControlTypeVOS.add(new DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum.METTING));
            deviceRemoteControlTypeVOS.add(new DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum.DIMMING_ADD));
            deviceRemoteControlTypeVOS.add(new DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum.DIMMING_SUB));
            deviceRemoteControlTypeVOS.add(new DeviceRemoteControlTypeVO(DeviceRemoteControlTypeEnum.ONOFF));
            datas.put("listDeviceRemoteControlType",deviceRemoteControlTypeVOS);
            //获取遥控器按键参数
            List<DeviceRemoteControlParamVO> deviceRemoteControlTypeVoS = new ArrayList<>();
            deviceRemoteControlTypeVoS.add(new DeviceRemoteControlParamVO(DeviceRemoteControlParamEnum.GROUP));
            deviceRemoteControlTypeVoS.add(new DeviceRemoteControlParamVO(DeviceRemoteControlParamEnum.SCENE));
            deviceRemoteControlTypeVoS.add(new DeviceRemoteControlParamVO(DeviceRemoteControlParamEnum.NULL));
            datas.put("listDeviceRemoteControlParams",deviceRemoteControlTypeVoS);
            //获取遥控器键的Scene列表
            //datas.put("listDeviceRemoteControlSceneTemplate",deviceRemoteService.listDeviceRemoteControlSceneTemplate(tenantId).stream().map(one -> new DictVO(one.getId(), one.getName())).collect(Collectors.toList()));
            return new CommonResponse(ResultMsg.SUCCESS).data(datas);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @ApiOperation("遥控器下发")
    @RequestMapping(value = "/synchronousRemoteControl", method = RequestMethod.POST)
    public CommonResponse<DeviceRemoteTemplateResp> synchronousRemoteControl(  @RequestParam(value ="spaceId" )  Long spaceId) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            deviceRemoteService.synchronousRemoteControl(user.getTenantId(), spaceId);
            return new CommonResponse(ResultMsg.SUCCESS);
        } catch (Exception e) {
            return new CommonResponse(ResultMsg.EXCEPTION.getCode(), e.getMessage());
        }
    }
}
