package com.iot.tenant.controller;

import com.github.pagehelper.PageInfo;
import com.iot.tenant.api.AppVersionLogApi;
import com.iot.tenant.service.IAppVersionLogService;
import com.iot.tenant.vo.req.AppVersionLogReq;
import com.iot.tenant.vo.resp.AppVersionLogResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppVersionLogController implements AppVersionLogApi {


    @Autowired
    private IAppVersionLogService iAppVersionLogService;

    @Override
    public Long insertOrUpdate(@RequestBody AppVersionLogReq appVersionLogReq) {
        return iAppVersionLogService.insertOrUpdate(appVersionLogReq);
    }

    @Override
    public AppVersionLogResp versionLogByKey(@RequestParam("systemInfo") String systemInfo,@RequestParam("appPackage") String appPackage,@RequestParam("version") String version) {
        return iAppVersionLogService.versionLogByKey(systemInfo,appPackage,version);
    }


    @Override
    public PageInfo page(@RequestBody AppVersionLogReq appVersionLogReq) {
        return iAppVersionLogService.page(appVersionLogReq);
    }

    @Override
    public void delete(@RequestBody List<Long> ids) {
        iAppVersionLogService.delete(ids);
    }

}
