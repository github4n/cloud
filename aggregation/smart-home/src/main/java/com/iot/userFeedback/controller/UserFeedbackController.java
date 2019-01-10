package com.iot.userFeedback.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.userFeedback.service.UserFeedbackService;
import com.iot.userFeedback.vo.FeedbackContentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Api(description = "用户反馈接口")
@RestController
@RequestMapping("/feedback")
public class UserFeedbackController {

    @Autowired
    private UserFeedbackService userFeedbackService;
    private static Logger LOGGER = LoggerFactory.getLogger(UserFeedbackService.class);

    @LoginRequired(Action.Normal)
    @ApiOperation(value = "反馈内容", response = void.class)
    @RequestMapping(value = "/content", method = RequestMethod.POST)
    public CommonResponse saveFeedback(@RequestBody FeedbackContentVo feedbackContentVo) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        return userFeedbackService.saveFeedback(userId, feedbackContentVo.getContent());
    }

    @LoginRequired(Action.Normal)
    @ApiOperation(value = "反馈视频和图片", response = void.class)
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public CommonResponse saveFeedbackFile(MultipartHttpServletRequest multipartRequest) {
        Long currentTenantId = SaaSContextHolder.currentTenantId();
        Long userId=SaaSContextHolder.getCurrentUserId();
        userFeedbackService.saveFeedbackFile(multipartRequest,currentTenantId,userId);
        return new CommonResponse(ResultMsg.SUCCESS, "Success.");
    }

    @LoginRequired(Action.Normal)
    @ApiOperation(value = "保存反馈内容和视频", response = void.class)
    @RequestMapping(value = "/uploadFeedback", method = RequestMethod.POST)
    public CommonResponse uploadFeedback(MultipartHttpServletRequest multipartRequest, @RequestParam(value = "content") String content) {
        Long currentTenantId = SaaSContextHolder.currentTenantId();
        Long userId=SaaSContextHolder.getCurrentUserId();
        userFeedbackService.uploadFeedback(multipartRequest,currentTenantId,userId,content);
        return new CommonResponse(ResultMsg.SUCCESS, "");
    }
}
