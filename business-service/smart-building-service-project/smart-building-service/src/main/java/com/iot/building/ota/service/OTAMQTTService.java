package com.iot.building.ota.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.iot.building.device.vo.DeviceRespVo;
import com.iot.building.helper.Constants;
import com.iot.building.helper.DispatcherRouteHelper;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.rsp.OtaVersionInfoResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:30 2018/4/28
 * @Modify by:
 */
@Service("ota")
public class OTAMQTTService implements CallBackProcessor {

    public static final int QOS = 1;
    private final static Logger LOGGER = LoggerFactory.getLogger(OTAMQTTService.class);
    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private Environment dnvironment;

    @Autowired
    private MqttSdkService mqttSdkService;
    
    @Autowired
	private DeviceCoreApi deviceCoreApi;

    @Override
    public void onMessage(MqttMsg mqttMsg) {
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }
    /**
     * 8.7执行OTA请求  iot/v1/s/[userId]/ota/excOtaReq
     */
    public void excOtaReq(MqttMsg msg, String reqTopic) {
    }

    /**
     * 检查执行的ota 对应的设备 是否需要下发通知升级
     *
     * @param deviceResp
     * @param infoResp
     */
    private void checkExcOtaNotif(DeviceRespVo deviceResp, OtaVersionInfoResp infoResp) {
    }

    /**
     * 检查设备是否需要升级
     *
     * @param deviceId
     * @param fwType
     * @param newVersion
     * @return
     */
    private boolean checkDeviceWhetherUpdate(String deviceId, Integer fwType, String newVersion) {
        return false;
    }

    /**
     * 8.1执行OTA通知   云--> 网关
     */
    protected void excOtaNotif(Integer fwType,
                               String directDeviceId,
                               List<String> deviceIds,
                               String version,
                               String url,
                               Integer mode,
                               String md5, String seq, String srcAddr) {
    }

    /**
     * 8.5获取版本列表请求   app --->cloud
     *
     * @param msg
     * @param reqTopic iot/v1/s/" + userId + "/ota/getVerListReq
     */
    public void getVerListReq(MqttMsg msg, String reqTopic) {
        LOGGER.info("getVerListReq({}, {})", msg, reqTopic);
    }

    /**
     * 8.6获取版本列表响应
     */
    private void getVerListResp(MqttMsg msg, String reqTopic) {
        LOGGER.info("getVerListResp({}, {})", msg, reqTopic);
    }


    /**
     * 8.4上报版本信息  直连设备上报--->云端
     * iot/v1/s/[devId]/ota/updateVerInfo
     */
    public void updateVerInfo(MqttMsg msg, String reqTopic) {
        LOGGER.info("updateVerInfo({}, {})", msg, reqTopic);
    }

    /**
     * 8.3更新版本通知   云--> APP
     */
    protected void updateVerNotif(String userId, String deviceId, String deviceName, String version) {
    }

    /**
     * 8.2上报OTA状态   网关/APP-->云  iot/v1/cb/[devId]/ota/ updateOtaStautsNotif
     */
    public void updateOtaStautsNotif(MqttMsg msg, String reqTopic) {
        LOGGER.info("updateOtaStautsNotif({}, {})", msg, reqTopic);
    }

    /**
     * 2B OTA升级
     * 6.4.1上报升级状态   
     */
    public void stateNotify(MqttMsg msg, String reqTopic) {
        LOGGER.info("updateOtaStautsNotif({}, {})", msg, reqTopic);
        String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
        String topic="iot/v1/c/"+uuid+"/center/ota";
        Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String state = (String) payload.get("state");
		if (org.apache.commons.lang.StringUtils.isNotBlank(state) && state.equals("installed")) {
			BusinessDispatchMqttHelper.sendDeviceOta(payload,topic);
		}
    }

    /**
	 * 2B OTA升级子设备 版本上报 6.4.1上报子设备升级版本
	 */
	public void otaVersionNotify(MqttMsg msg, String reqTopic) {
		LOGGER.info("otaVersionNotify({}, {})", msg, reqTopic);
		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String version = (String) payload.get("fw_ver");
		String deviceId = (String) payload.get("did");
		GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(deviceId);
		if (getDeviceInfoRespVo != null) {
			UpdateDeviceInfoReq deviceInfoReq = new UpdateDeviceInfoReq();
			deviceInfoReq.setUuid(deviceId);
			deviceInfoReq.setVersion(version);
			deviceInfoReq.setTenantId(getDeviceInfoRespVo.getTenantId());
			deviceInfoReq.setLocationId(getDeviceInfoRespVo.getLocationId());
			deviceCoreApi.saveOrUpdate(deviceInfoReq);
		}
	}

	/**
	 * 2B 获取设备的版本信息和状态
	 */
	public void queryResp(MqttMsg msg, String reqTopic) {
		LOGGER.info("queryResp({}, {})", msg, reqTopic);
		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String version = (String) payload.get("fw_ver");
		String deviceId = (String) payload.get("did");
		GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(deviceId);
		if (getDeviceInfoRespVo != null) {
			UpdateDeviceInfoReq deviceInfoReq = new UpdateDeviceInfoReq();
			deviceInfoReq.setUuid(deviceId);
			deviceInfoReq.setVersion(version);
			deviceInfoReq.setTenantId(getDeviceInfoRespVo.getTenantId());
			deviceInfoReq.setLocationId(getDeviceInfoRespVo.getLocationId());
			deviceCoreApi.saveOrUpdate(deviceInfoReq);
		}
	}
}
