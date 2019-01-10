package com.iot.user.controller;


import com.iot.user.api.OnlineDebugApi;
import com.iot.user.service.OnlineDebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineDebugController implements OnlineDebugApi {

    @Autowired
    private OnlineDebugService onlineDebugService;

    @Override
    public Boolean checkOnlineDebug(@RequestParam("uuid") String uuid) {
        return onlineDebugService.checkOnlineDebug(uuid);
    }

    @Override
    public String updateOnlineDebug(@RequestParam("userName")String userName,@RequestParam("state")int state,@RequestParam("tenantId")int tenantId) {
        return  onlineDebugService.updateOnlineDebug(userName,state,tenantId);
    }


}
