package com.iot.user;

import com.iot.user.entity.FeedbackEntity;
import com.iot.user.service.UserFeedbackService;
import com.iot.user.vo.FeedbackReq;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class UserFeedbackTest {

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Test
    public void saveFeedbackTest() {
        FeedbackReq rep=new FeedbackReq();
        rep.setUserId(3L);
        rep.setFeedbackContent("test feedback2");
        userFeedbackService.saveFeedback(rep);
    }
}
