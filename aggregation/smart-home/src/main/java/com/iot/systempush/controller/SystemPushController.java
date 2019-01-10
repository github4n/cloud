package com.iot.systempush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.systempush.service.SystemPushBusinessService;
import com.iot.systempush.vo.SystemPushControlVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "系统推送控制接口")
@RestController
@RequestMapping("/systempush")
public class SystemPushController {
	
//    private static final Logger LOGGER = LoggerFactory.getLogger(SystemPushController.class);

    /**
     * 系统推送控制服务
     */
    @Autowired
    private SystemPushBusinessService systemPushBusinessService;

    /**
     * 
     * 描述：查询系统推送控制
     * @author 李帅
     * @created 2018年11月19日 下午2:19:04
     * @since 
     * @param appId
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询用户详细信息", notes = "查询用户详细信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "appId", value = "APPID", required = true, paramType = "query", dataType = "Long")})
    @RequestMapping(value = "/getSystemPushControl", method = {RequestMethod.GET})
    public CommonResponse getSystemPushControl(@RequestParam("appId") Long appId) {
        return ResultMsg.SUCCESS.info(this.systemPushBusinessService.getSystemPushControl(appId));
    }

    /**
     * 
     * 描述：添加或修改系统推送控制
     * @author 李帅
     * @created 2018年11月19日 下午2:31:04
     * @since 
     * @param systemPushControlVo
     */
	@ApiOperation(value="添加或修改系统推送控制", notes="添加或修改系统推送控制")
    @ApiImplicitParams({@ApiImplicitParam(name = "systemPushControlVo", value = "租户邮箱信息", required = true, dataType = "SystemPushControlVo") })
	@RequestMapping(value = "/addSystemPushControl", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse addSystemPushControl(@RequestBody SystemPushControlVo systemPushControlVo) {
		this.systemPushBusinessService.addSystemPushControl(systemPushControlVo);
		return ResultMsg.SUCCESS.info();
	}
}
