package com.iot.ifttt.service;

import com.google.common.collect.Maps;
import com.iot.ifttt.api.IftttAccessApi;
import com.iot.ifttt.interceptor.IftttTokenInterceptor;
import com.iot.ifttt.vo.CheckTriggerReq;
import com.iot.ifttt.vo.CommonReq;
import com.iot.ifttt.vo.ActionReq;
import com.iot.ifttt.vo.GetProductReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.SmartHomeDeviceCoreApi;
import com.iot.shcs.scene.api.SceneApi;
import com.iot.shcs.scene.vo.rsp.SceneResp;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.space.vo.SpaceResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 描述：公用服务类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/12/14 11:05
 */
@Slf4j
@Service
public class CommonService {

    @Autowired
    private SmartHomeDeviceCoreApi smartHomeDeviceCoreApi;

    @Autowired
    private SmarthomeSpaceApi smarthomeSpaceApi;

    @Autowired
    private SceneApi sceneApi;

    @Autowired
    private IftttAccessApi iftttAccessApi;

    private String key;


    /**
     * 校验结果
     *
     * @param req
     * @return
     */
    public int checkApplet(CommonReq req) {
        CheckTriggerReq checkAppletReq = new CheckTriggerReq();
        checkAppletReq.setUserId(IftttTokenInterceptor.getUserId());
        checkAppletReq.setType(req.getName());
        checkAppletReq.setTriggerFields(req.getTriggerFields());
        checkAppletReq.setIdentity(req.getTrigger_identity());
        checkAppletReq.setTenantId(IftttTokenInterceptor.getTenantId());
        return iftttAccessApi.checkApplet(checkAppletReq);
    }

    /**
     * 获取设备列表
     *
     * @param type
     * @return
     */
    public Map<String, String> getDeviceMap(String type) {
        Map<String, String> deviceMap = Maps.newHashMap();
        Long tenantId = IftttTokenInterceptor.getTenantId();
        SpaceResp spaceResp = smarthomeSpaceApi.findUserDefaultSpace(IftttTokenInterceptor.getUserId(), tenantId);
        if (spaceResp != null) {
            Map<String, Object> devData = smartHomeDeviceCoreApi.getDevList(tenantId, IftttTokenInterceptor.getUserId(), spaceResp.getId());
            log.debug("用户设备信息：" + devData.toString());
            List<Map<String, Object>> devList = (List<Map<String, Object>>) devData.get("dev");

            GetProductReq getProductReq = new GetProductReq();
            getProductReq.setTenantId(tenantId);
            getProductReq.setType(type);
            log.debug("获取产品请求：" + getProductReq.toString());
            List<Long> plist = iftttAccessApi.getProductList(getProductReq);
            log.debug("获取到的产品：" + plist.toString() + "/");
            if (CollectionUtils.isNotEmpty(devList)) {
                for (Map<String, Object> dev : devList) {
                    String devId = (String) dev.get("devId");
                    String devName = (String) dev.get("name");
                    Long productId = Long.parseLong(dev.get("productId").toString());
                    if (plist.contains(productId)) {
                        deviceMap.put(devId, devName);
                    }
                }
            }
        }
        return deviceMap;
    }

    /**
     * 获取用户情景map
     *
     * @return
     */
    public Map<String, String> getSceneMap() {
        Long tenantId = IftttTokenInterceptor.getTenantId();
        SaaSContextHolder.setCurrentTenantId(tenantId);
        List<SceneResp> sceneRespList = sceneApi.findSceneRespListByUserId(IftttTokenInterceptor.getUserId(), tenantId);
        Map<String, String> sceneMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(sceneRespList)) {
            for (SceneResp vo : sceneRespList) {
                sceneMap.put(String.valueOf(vo.getId()), vo.getSceneName());
            }
        }
        return sceneMap;
    }

    /**
     * 动作逻辑
     *
     * @param req
     */
    public void action(CommonReq req) {
        ActionReq actionReq = new ActionReq();
        actionReq.setType(req.getName());
        actionReq.setTenantId(IftttTokenInterceptor.getTenantId());
        actionReq.setActionFields(req.getActionFields());
        actionReq.setUserId(IftttTokenInterceptor.getUserId());
        actionReq.setUuid(IftttTokenInterceptor.getUserUUID());
        log.debug("发送控制设备请求：" + actionReq.toString());
        iftttAccessApi.doAction(actionReq);
    }

    /**
     * 获取秘钥
     *
     * @return
     */
    public String getKey() {
        if (StringUtils.isEmpty(key)) {
            Map<String, Object> map = iftttAccessApi.getConfig();
            key = (String) map.get("key");
        }
        return key;
    }
}
