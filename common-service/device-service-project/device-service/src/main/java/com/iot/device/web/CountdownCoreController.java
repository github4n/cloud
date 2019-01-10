package com.iot.device.web;

import com.iot.common.beans.BeanUtil;
import com.iot.device.api.CountdownCoreApi;
import com.iot.device.model.Countdown;
import com.iot.device.service.ICountdownService;
import com.iot.device.vo.req.CountDownReq;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lucky
 * @Descrpiton: 倒计时业务
 * @Date: 17:41 2018/10/18
 * @Modify by:
 */
@RestController
public class CountdownCoreController implements CountdownCoreApi {
    @Autowired
    private ICountdownService countdownService;

    public void addCountDown(@RequestBody CountDownReq countDownReq) {
        AssertUtils.notNull(countDownReq, "countDownReq.notnull");
        AssertUtils.notEmpty(countDownReq.getDeviceId(), "deviceId.notnull");
        AssertUtils.notNull(countDownReq.getUserId(), "userId.notnull");
        Countdown countdown = new Countdown();
        BeanUtil.copyProperties(countDownReq, countdown);
        countdown.setOrgId(countdown.getOrgId());
        countdown.setTenantId(countdown.getTenantId());
        countdownService.insertOrUpdate(countdown);
    }
}
