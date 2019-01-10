package com.iot.user.api.fallback;

import com.iot.user.api.FeedbackApi;
import com.iot.user.vo.FeedbackFileVo;
import com.iot.user.vo.FeedbackReq;
import com.iot.user.vo.FeedbackVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class FeedbackApiFallbackFactory implements FallbackFactory<FeedbackApi> {
    @Override
    public FeedbackApi create(Throwable cause) {
        return new FeedbackApi() {
            @Override
            public int saveFeedback(@RequestBody FeedbackReq feedbackReq) {
                return 0;
            }

            @Override
            public int saveFeedbackFile(FeedbackFileVo feedbackFileVo) {
                return 0;
            }

            @Override
            public void save(FeedbackVo feedbackVo) {

            }
        };
    }
}
