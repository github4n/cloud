package com.iot.smarthome.service;

import com.iot.device.api.ServiceModuleApi;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.smarthome.api.ThirdPartyInfoApi;
import com.iot.smarthome.constant.EventNotifyConst;
import com.iot.smarthome.util.HttpUtils;
import com.iot.smarthome.util.SignatureUtil;
import com.iot.smarthome.vo.SignHashMap;
import com.iot.smarthome.vo.resp.ThirdPartyInfoResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 13:42
 * @Modify by:
 */

@Service("smartHomeCommonService")
public class CommonService {
    private Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private ServiceModuleApi serviceModuleApi;
    @Autowired
    private ServicePropertyApi servicePropertyApi;
    @Autowired
    private ThirdPartyInfoApi thirdPartyInfoApi;


    /**
     * 	通过 deviceId 设备属性列表
     * @param deviceId
     */
    public List<ServiceModulePropertyResp> findPropertyListByDeviceId(String deviceId) {
        GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        if (deviceInfoRespVo == null) {
            return null;
        }

        return findPropertyListByProductId(deviceInfoRespVo.getProductId());
    }

    /**
     * 	通过 productId 设备属性列表
     * @param productId
     */
    public List<ServiceModulePropertyResp> findPropertyListByProductId(Long productId) {
        if (productId == null) {
            return null;
        }

        List<Long> serviceModuleIdList = serviceModuleApi.listServiceModuleIdsByProductId(productId);
        if (CollectionUtils.isEmpty(serviceModuleIdList)) {
            return null;
        }

        List<ServiceModulePropertyResp> serviceModulePropertyList = servicePropertyApi.findPropertyListByServiceModuleIds(serviceModuleIdList);
        return serviceModulePropertyList;
    }


    public void notifyThirdParty(Long thirdPartyInfoId, String jsonContent) {
        // 第三方信息
        ThirdPartyInfoResp thirdPartyInfoResp = thirdPartyInfoApi.getById(thirdPartyInfoId);
        if (thirdPartyInfoResp == null) {
            LOGGER.info("***** notifyThirdParty, current thirdPartyInfoResp is null, thirdPartyInfoId={}", thirdPartyInfoId);
            return ;
        }

        // 事件通知地址
        String webHookUrl = thirdPartyInfoResp.getWebhookUrl();
        if (org.apache.commons.lang3.StringUtils.isBlank(webHookUrl)) {
            LOGGER.info("***** notifyThirdParty, current webHookUrl is null, thirdPartyInfoId={}", thirdPartyInfoId);
            return ;
        }

        String client_secret = thirdPartyInfoResp.getClientSecret();

        SignHashMap postParameters = new SignHashMap();
        postParameters.put("content", jsonContent);
        postParameters.put("client_id", thirdPartyInfoResp.getClientId());
        postParameters.put("version", EventNotifyConst.VERSION_1);
        postParameters.put("timestamp", System.currentTimeMillis());
        postParameters.put("sign_type", EventNotifyConst.SIGN_TYPE);

        // 生成签名
        String beforeSign = SignatureUtil.getSignatureContent(postParameters);
        beforeSign = beforeSign + "&client_secret=" + client_secret;

        String afterSign = null;
        try {
            afterSign = SignatureUtil.getSha256Sign(beforeSign, client_secret);
        } catch (Exception e) {
            LOGGER.error("notifyThirdParty, create sign error.");
            e.printStackTrace();
            return ;
        }
        afterSign = afterSign.toUpperCase();

        postParameters.put("sign", afterSign);

        // 发送给第三方
        Map<String, String> returnMap = HttpUtils.postMethod(webHookUrl, postParameters);
        LOGGER.debug("notifyThirdParty, returnMap={}", returnMap);
    }
}
