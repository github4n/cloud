package com.iot.boss.controller;

import com.iot.boss.service.network.IBossDeviceNetworkStepService;
import com.iot.boss.vo.network.BossSaveNetworkStepReq;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤管理（BOSS维护）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 9:51
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 9:51
 * 修改描述：
 */
@Api(tags = "设备配网步骤管理（BOSS维护）")
@RestController
@RequestMapping(value = "/api/deviceNetworkStep")
public class BossDeviceNetworkStepController {

    @Autowired
    private IBossDeviceNetworkStepService deviceNetworkStepService;

    /**
      * @despriction：保存（先删后插）
      * @author  yeshiyuan
      * @created 2018/10/9 11:07
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "保存（先删后插）", notes = "保存（先删后插）")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse save(@RequestBody BossSaveNetworkStepReq req) {
        deviceNetworkStepService.save(req);
        return ResultMsg.SUCCESS.info();
    }

    /**
      * @despriction：查询设备类型对应的配网步骤详情
      * @author  yeshiyuan
      * @created 2018/10/9 11:09
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询设备类型对应的配网步骤详情", notes = "查询设备类型对应的配网步骤详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "networkTypeId", value = "配网类型id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/queryNetwrokStepInfo", method = RequestMethod.GET)
    public CommonResponse queryNetwrokStepInfo(Long deviceTypeId, Long networkTypeId) {
        return ResultMsg.SUCCESS.info(deviceNetworkStepService.queryNetworkStepInfo(deviceTypeId, networkTypeId));
    }

}
