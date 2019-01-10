package com.iot.shcs.ipc.controller;

import com.iot.shcs.ipc.api.IpcApi;
import com.iot.shcs.ipc.service.IpcMQTTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月24日 14:37
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月24日 14:37
 */
@RestController
public class IpcController implements IpcApi {

    @Autowired
    private IpcMQTTService ipcMQTTService;
    @Override
    public void notifyDeviceRecordConfig(@RequestParam("planId") String planId, @RequestParam("deviceId") String deviceId) {
        ipcMQTTService.notifyDeviceRecordConfig(planId,deviceId);
    }

    @Override
    public void notifyDevicePlanInfo(@RequestParam("planId") String planId) {
        ipcMQTTService.notifyDeviceRecordConfig(planId,null);
    }
    
    /**
     * 
     * 描述：获取文件服务put预签名url
     * @author 李帅
     * @created 2018年7月16日 下午8:06:40
     * @since 
     * @param deviceId
     * @param planId
     * @param fileType
     * @return
     */
    @Override
	public Map<String, Object> getFilePreSignUrls(@RequestParam("deviceId") String deviceId,
			@RequestParam("planId") String planId,@RequestParam("fileType") String fileType){
        return ipcMQTTService.getFilePreSignUrls(deviceId, planId, fileType);
    }
}
