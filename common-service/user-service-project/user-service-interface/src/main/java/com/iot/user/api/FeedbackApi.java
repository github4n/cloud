package com.iot.user.api;

import com.iot.user.api.fallback.FeedbackApiFallbackFactory;
import com.iot.user.vo.FeedbackFileVo;
import com.iot.user.vo.FeedbackReq;
import com.iot.user.vo.FeedbackVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api("用户反馈接口")
@FeignClient(value = "user-service", fallbackFactory = FeedbackApiFallbackFactory.class)
@RequestMapping("/api/feedback")
public interface FeedbackApi {

    @ApiOperation("提交用户反馈")
    @RequestMapping(value = "/saveFeedback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int saveFeedback(@RequestBody FeedbackReq feedbackReq);

    @ApiOperation("提交用户反馈视频或图片fileId")
    @RequestMapping(value = "/saveFeedbackFile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int saveFeedbackFile(@RequestBody FeedbackFileVo feedbackFileVo);

    @ApiOperation("提交用户反馈")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody FeedbackVo feedbackVo);

}
