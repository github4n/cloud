package com.iot.smarthome.rabbit.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.device.queue.bean.SetDeviceAttrMessage;
import com.iot.smarthome.api.DeviceClassifyApi;
import com.iot.smarthome.service.CommonService;
import com.iot.smarthome.vo.EventDeviceStateChangeResp;
import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.vo.SmartTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: 设备属性通知 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class SetDeviceAttrNotifyProcess extends AbsMessageProcess<SetDeviceAttrMessage> {
    @Autowired
    private CommonService commonService;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DeviceClassifyApi deviceClassifyApi;
    @Autowired
    private UserDeviceCoreApi userDeviceCoreApi;

    @Override
    public void processMessage(SetDeviceAttrMessage message) {
        log.info("***** SetDeviceAttrNotifyProcess, message={}", JSON.toJSONString(message));

        try {
            String deviceId = message.getSubDeviceId();
            Map<String, Object> attrMap = message.getAttrMap();
            Integer onlineStatus = (Integer) message.getOnline();

            if (attrMap == null || attrMap.size() == 0) {
                log.info("***** SetDeviceAttrNotifyProcess, changeAttrMap is empty");
                return;
            }

            GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
            Long tenantId = deviceResp.getTenantId();
            ListUserDeviceInfoReq params = new ListUserDeviceInfoReq();
            params.setDeviceId(deviceId);
            params.setTenantId(tenantId);

            List<ListUserDeviceInfoRespVo> list = userDeviceCoreApi.listUserDevice(params);
            if (CollectionUtils.isEmpty(list)) {
                log.debug("***** SetDeviceAttrNotifyProcess, userDeviceList is empty, params.jsonString={}", JSONObject.toJSON(params));
                return;
            }

            // 获取 userId
            Long userId = list.get(0).getUserId();
            List<SmartTokenResp> smartTokenList = smartTokenApi.findThirdPartyInfoIdNotNull(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                log.debug("***** SetDeviceAttrNotifyProcess, smartTokenList is empty.");
                return;
            }

            DeviceClassifyResp deviceClassifyResp = deviceClassifyApi.getDeviceClassifyByProductId(deviceResp.getProductId());
            if (deviceClassifyResp == null) {
                log.info("SetDeviceAttrNotifyProcess, deviceId={} DeviceClassifyProductXref is null, cancel notify thirdParty", deviceId);
                return ;
            }

            // 封装基础数据
            EventDeviceStateChangeResp stateChangeResp = new EventDeviceStateChangeResp();
            stateChangeResp.setDeviceId(deviceId);
            stateChangeResp.setChangeAttr(attrMap);

            Map<String, Object> resultMap = stateChangeResp.buildMsg();
            String jsonContent = JSON.toJSONString(resultMap);

            for (SmartTokenResp smartToken : smartTokenList) {
                log.debug("***** SetDeviceAttrNotifyProcess, smartToken={}", JSON.toJSONString(smartToken));
                if (smartToken == null) {
                    log.info("***** SetDeviceAttrNotifyProcess, current smartToken is null");
                    continue;
                }
                commonService.notifyThirdParty(smartToken.getThirdPartyInfoId(), jsonContent);
            }
        } catch (Exception e) {
            log.error("***** SetDeviceAttrNotifyProcess, error.");
            e.printStackTrace();
        }
    }
}
