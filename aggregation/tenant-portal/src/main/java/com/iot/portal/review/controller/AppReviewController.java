package com.iot.portal.review.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.portal.review.service.IAppReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/11/6 10:09
 * 修改人： yeshiyuan
 * 修改时间：2018/11/6 10:09
 * 修改描述：
 */
@Api(tags = "app操作记录")
@RestController
@RequestMapping(value = "/appReview")
public class AppReviewController {

    @Autowired
    private IAppReviewService appReviewService;

    /**
      * @despriction：加载app操作记录
      * @author  yeshiyuan
      * @created 2018/11/6 10:21
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "加载app操作记录", notes = "加载app操作记录")
    @RequestMapping(value = "/getReviewList", method = RequestMethod.GET)
    @ApiImplicitParam(name = "appId", value = "appId", dataType = "Long", paramType = "query")
    public CommonResponse getReviewList(Long appId) {
        return CommonResponse.success(appReviewService.getReviewList(appId));
    }
}
