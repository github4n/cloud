package com.iot.onlineDebug.service;

import com.iot.common.beans.CommonResponse;
import com.iot.user.api.OnlineDebugApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlineDebugService {
    @Autowired
    private OnlineDebugApi onlineDebugApi;

    public CommonResponse checkOnlineDebug(String uuid) {
        Boolean isDebug = onlineDebugApi.checkOnlineDebug(uuid);
        return CommonResponse.success(isDebug);
    }
}
