package com.iot.user.controller;

import com.iot.user.api.FeedbackApi;
import com.iot.user.service.UserFeedbackService;
import com.iot.user.vo.FeedbackFileVo;
import com.iot.user.vo.FeedbackReq;
import com.iot.user.vo.FeedbackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackApiController implements FeedbackApi {


    @Autowired
    private UserFeedbackService userFeedbackService;

    @Override
    public int saveFeedback(@RequestBody FeedbackReq feedbackReq) {
        return userFeedbackService.saveFeedback(feedbackReq);
    }

    @Override
    public int saveFeedbackFile(@RequestBody FeedbackFileVo feedbackFileVo) {
        return userFeedbackService.saveFeedbackFileId(feedbackFileVo);
    }

    @Override
    public void save(@RequestBody FeedbackVo feedbackVo) {
         userFeedbackService.save(feedbackVo);
    }

}
