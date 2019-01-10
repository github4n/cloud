package com.iot.version;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.tenant.api.AppVersionLogApi;
import com.iot.tenant.vo.resp.AppVersionLogResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "应用版本记录接口",value = "应用版本记录接口")
@RestController
@RequestMapping("/app/version/log")
public class AppVersionLogController {


    @Autowired
    private AppVersionLogApi appVersionLogApi;


    @LoginRequired(value = Action.Skip)
    @ApiOperation("获取版本信息")
    @RequestMapping(value = "/versionLogByKey", method = RequestMethod.GET)
    public CommonResponse versionLogByKey(@RequestParam("systemInfo") String systemInfo,@RequestParam("appPackage") String appPackage,@RequestParam("version") String version) {
        AppVersionLogResp appVersionLogResp = appVersionLogApi.versionLogByKey(systemInfo,appPackage,version);
        return CommonResponse.success(appVersionLogResp);
    }


}
