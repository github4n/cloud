package com.iot.boss.controller;

import com.iot.boss.service.app.IAPPService;
import com.iot.boss.vo.review.req.AppReviewListSearchReq;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：boss聚合层
 * 功能描述：审核功能（用户审核、APP审核、产品审核、语音服务接入审核）
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 15:57
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 15:57
 * 修改描述：
 */
@Api(value = "Boss-审核管理", description = "Boss-审核管理")
@RestController
@RequestMapping("/api/reviewController")
public class AppController {

    private Logger log = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private IAPPService appService;

    @LoginRequired(Action.Normal)
    @ApiOperation(value = "APP审核列表查询", notes = "APP审核列表查询")
    @RequestMapping(value = "/getAppListByAuditStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppListByAuditStatus(@RequestBody AppReviewListSearchReq req) {
        return CommonResponse.success(appService.getAppListByAuditStatus(req));
    }

    /**
     * @return
     * @despriction：重开可编辑
     * @author yeshiyuan
     * @created 2018/10/25 17:06
     */
    @ApiOperation(value = "重开可编辑", notes = "重开可编辑")
    @RequestMapping(value = "/reOpen", method = RequestMethod.POST)
    public CommonResponse reOpen(@RequestParam("appId") Long appId) {
        appService.reOpen(appId);
        return CommonResponse.success();
    }

    @LoginRequired(Action.Normal)
    @ApiOperation(value = "修改app打包状态", notes = "修改app打包状态")
    @RequestMapping(value = "/updateAppStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateAppStatus(@RequestParam("id") Long id){
        appService.updateAppStatus(id);
        return CommonResponse.success();
    }
}