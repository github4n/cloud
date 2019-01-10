package com.iot.boss.controller;

import com.iot.boss.service.uuid.UUIDManegerService;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.DeviceExtendsCoreApi;
import com.iot.device.vo.req.LicenseUsageReq;
import com.iot.device.vo.req.UserLoginReq;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.UuidApplyForBoss;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "UUID管理",value = "UUID管理")
@RequestMapping("api/certGenerate")
public class UUIDManegerController {

    @Autowired
    private DeviceExtendsCoreApi deviceExtendsCoreApi;

	@Autowired
	private UUIDManegerService uuidManegerService;

    @ApiOperation(value = "UUID生成", notes = "UUID生成")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "generateUUID", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse generateUUID(@RequestBody UuidApplyForBoss uuidApplyForBoss) {
        return ResultMsg.SUCCESS.info(this.uuidManegerService.generateUUID(uuidApplyForBoss));
    }
	
	@ApiOperation(value = "UUID生成列表", notes = "UUID生成列表")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getUUIDGenerateList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse getUUIDGenerateList(@RequestBody GetUUIDOrderReq uuidOrderReq) {
		return ResultMsg.SUCCESS.info(this.uuidManegerService.getUUIDGenerateList(uuidOrderReq));
	}
	
	@ApiOperation(value = "通过批次号获取证书下载URL", notes = "通过批次号获取证书下载URL")
	@LoginRequired(value = Action.Normal)
	@ApiImplicitParams({@ApiImplicitParam(name = "batchNum", value = "批次号", required = true, paramType = "query", dataType = "Long")})
	@RequestMapping(value = "getDownloadUUID", method = RequestMethod.GET)
	public CommonResponse getDownloadUUID(@RequestParam("batchNum")  Long batchNum) {
		return ResultMsg.SUCCESS.info(this.uuidManegerService.getDownloadUUID(batchNum));
	}

    @ApiOperation(value = "管理员登陆", notes = "管理员登陆")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "userLogin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse userLogin(@RequestBody UserLoginReq userLoginReq) {
        this.uuidManegerService.userLogin(userLoginReq);
        return ResultMsg.SUCCESS.info();
    }

    @ApiOperation(value = "上报总装测试数据", notes = "上报总装测试数据")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/licenseUsage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse licenseUsage(@RequestBody LicenseUsageReq licenseUsageReq) throws BusinessException {
        this.uuidManegerService.licenseUsage(licenseUsageReq);
        return ResultMsg.SUCCESS.info();
    }

    @ApiOperation(value = "恢复首次添加套包数据", notes = "恢复首次添加套包数据")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/recoverDevFirstUploadSubDevs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse recoverDevFirstUploadSubDevs(@RequestParam("deviceUuid") String deviceUuid) throws BusinessException {
        Long tenantId = SaaSContextHolder.currentTenantId();

        deviceExtendsCoreApi.saveOrUpdate(UpdateDeviceExtendReq.builder()
                .tenantId(tenantId)
                .deviceId(deviceUuid)
                .firstUploadSubDev(1)
                .build());

        return ResultMsg.SUCCESS.info();
    }
}
