package com.iot.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.file.api.FileApi;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.vo.resp.AppInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("appinfo")
@RestController
@RequestMapping("/app/info")
public class AppInfoController {

    @Autowired
    private AppApi appApi;

    @Autowired
    private FileApi fileApi;



    @LoginRequired(Action.Skip)
    @ApiOperation("隐私文件")
    @RequestMapping(value = "/privacyFile", method = RequestMethod.GET)
    public CommonResponse privacyFile(@RequestParam("appId") Long appId){
        AppInfoResp appInfoRes = appApi.getAppById(appId);
        List list = new ArrayList();
        Map result = new HashMap();
        if (!StringUtils.isEmpty(appInfoRes.getPrivacyCnLinkId())){
            list.add(appInfoRes.getPrivacyCnLinkId());
        }
        if (!StringUtils.isEmpty(appInfoRes.getPrivacyEnLinkId())){
            list.add(appInfoRes.getPrivacyEnLinkId());
        }
        if (!CollectionUtils.isEmpty(list)){
            Map map = fileApi.getGetUrl(list);
            result.put("cnLink",map.get(appInfoRes.getPrivacyCnLinkId()));
            result.put("enLink",map.get(appInfoRes.getPrivacyEnLinkId()));
        }
        return CommonResponse.success(result);
    }

}
