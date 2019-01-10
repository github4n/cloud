package com.iot.boss.service.onlineDebug;

import com.iot.user.api.OnlineDebugApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlineDebugService {
    @Autowired
    private OnlineDebugApi onlineDebugApi;

    public String updateOnlineDebug(String userName, int state, int tenantId){
        return onlineDebugApi.updateOnlineDebug(userName,state,tenantId);
    }
}
