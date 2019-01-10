package com.iot.device.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.service.DeviceBusinessService;
import com.iot.device.vo.DeviceVo;
import com.iot.device.vo.rsp.DeviceMoreInfoResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetProductByDeviceRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人：
 * 创建时间：2018/3/20 17:46
 * 修改人：
 * 修改时间：2018/3/20 17:46
 * 修改描述：
 */
@Api(description = "视频云-设备接口")
@RestController
@RequestMapping("/deviceController")
public class DeviceController {

    @Autowired
    private DeviceBusinessService deviceBusiness;


    @Autowired
    private DeviceCoreApi deviceCoreApi;


    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "计划绑定设备", notes = "计划绑定设备")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/planBandingDevice", method = RequestMethod.POST)
    public CommonResponse planBandingDevice(@RequestParam("planId") String planId, @RequestParam("deviceId") String deviceId) {
        this.deviceBusiness.planBandingDevice(planId, deviceId);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取文件服务put预签名url", notes = "获取文件服务put预签名url")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "fileType", value = "文件类型", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/getFilePreSignUrls", method = RequestMethod.GET)
    public CommonResponse getFilePreSignUrls(@RequestParam("planId") String planId,@RequestParam("fileType") String fileType,
    		@RequestHeader(value="ssl_client_s_dn") String sslClientSDn){
        return ResultMsg.SUCCESS.info(this.deviceBusiness.getFilePreSignUrls(sslClientSDn, planId, fileType));
    }
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取设备列表", notes = "获取设备列表")
    @RequestMapping(value = "/getDevList", method = RequestMethod.GET)
    public CommonResponse getDevList(@RequestParam("homeId") Long homeId) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        Map<String, Object> devMap = deviceBusiness.getDevList(tenantId, userId, homeId);
        return ResultMsg.SUCCESS.info(devMap);
    }

    /**
     * 设备排序
     * @param deviceIds
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "设备排序", notes = "设备排序")
    @RequestMapping(value = "/sortDev", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse sortDev(@RequestBody List<String> deviceIds) {
        return ResultMsg.SUCCESS.info(deviceBusiness.sortDev(deviceIds));
    }


    /**
     * 描述：通过设备Id 查询p2pId
     *
     * @param deviceId
     * @return
     * @author 李帅
     * @created 2018年5月11日 下午1:57:32
     * @since
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "通过设备Id 查询p2pId", notes = "通过设备Id 查询p2pId")
    @RequestMapping(value = "/getP2pId", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name = "deviceId", value = "设备UUID", required = true, paramType = "query", dataType = "String")})
    public CommonResponse getP2pId(@RequestParam("deviceId") String deviceId) {
        String p2pId = deviceBusiness.getP2pId(deviceId);
        return ResultMsg.SUCCESS.info(p2pId);
    }

    /**
     * @param
     * @return
     * @despriction：加载未绑定计划的设备列表
     * @author yeshiyuan
     * @created 2018/4/8 15:42
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "/getUnBindPlanDeviceList", notes = "加载未绑定计划的设备列表")
    @RequestMapping(value = "/getUnBindPlanDeviceList", method = RequestMethod.POST)
    public CommonResponse getUnBindPlanDeviceList() {
        List<DeviceVo> deviceVoList = deviceBusiness.getUnBindPlanDeviceList();
        return ResultMsg.SUCCESS.info(deviceVoList);
    }

    /**
     * @param deviceId 设备id
     * @return
     * @author lucky
     * @date 2018/5/14 10:03
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取设备更多内容信息", notes = "获取设备更多内容信息")
    @RequestMapping(value = "/getDeviceMoreInfoByDeviceId", method = RequestMethod.POST)
    public CommonResponse getDeviceMoreInfoByDeviceId(@RequestParam("deviceId") String deviceId) {

        GetProductByDeviceRespVo deviceRespVo = deviceCoreApi.getProductByDeviceId(deviceId);
        if (deviceRespVo != null) {
            DeviceMoreInfoResp moreInfoResp = new DeviceMoreInfoResp();
            if (deviceRespVo.getDeviceInfo() != null) {
                moreInfoResp.setProductId(deviceRespVo.getDeviceInfo().getProductId());
                moreInfoResp.setMac(deviceRespVo.getDeviceInfo().getMac());
            }
            if (deviceRespVo.getProductInfo() != null) {
                moreInfoResp.setModelId(deviceRespVo.getProductInfo().getModel());
            }
            return ResultMsg.SUCCESS.info(moreInfoResp);
        }
        return ResultMsg.FAIL.info(DeviceExceptionEnum.DEVICE_NOT_EXIST);
    }
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取该直连设备下的子设备", notes = "获取该直连设备下的子设备")
    @RequestMapping(value = "/getDeviceListByDirectDevice", method = RequestMethod.GET)
    public CommonResponse getDeviceListByDirectDevice(@RequestParam("devId") String devId){
        List<DeviceResp> deviceList=deviceBusiness.getDeviceListByDirectDevice(devId);
        return ResultMsg.SUCCESS.info(deviceList);
    }



}
