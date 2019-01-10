package com.iot.shcs.template.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.security.domain.Security;
import com.iot.shcs.security.service.SecurityRuleService;
import com.iot.shcs.security.service.SecurityService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.security.vo.SecurityRule;
import com.iot.shcs.template.entity.Package;
import com.iot.shcs.template.entity.TemplateRule;
import com.iot.shcs.template.mapper.PackageMapper;
import com.iot.shcs.template.service.IPackageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.shcs.template.service.ITemplateRuleService;
import com.iot.shcs.template.vo.InitPackReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * <p>
 * 套包表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-12
 */
@Slf4j
@Service
public class PackageServiceImpl extends ServiceImpl<PackageMapper, Package> implements IPackageService {

    @Autowired
    private ITemplateRuleService templateRuleService;

    @Autowired
    private DeviceApi deviceApi;

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SecurityRuleService securityRuleService;

    @Autowired
    private SecurityMqttService securityMqttService;

   /* {
        "securityType": "stay",
        "enabled": 0,
        "defer": 30,
        "delay": 20,
        "if": {
            "trigger": [
             {
                "idx":  0,
                "trigType": "dev",
                "devId": "b6f7c7f3e7a14a74be155d6a0f7f38e1",
                "attr": "occupancy ",
                "compOp": "== ",
                "value":  "1",
              },
            ]
        },
        "then": [
           {
                "idx":  0,
                "thenType": "dev",
                "id": "b6f7c7f3e7a14a74be155d6a0f7f38e1",
                "attr": {
                    "OnOff": 1,
                    "Dimming": 87
                }
            },
        ]
    }*/

    @Override
    public void initPack(InitPackReq req) {
        log.debug("init pack request : {}", JSON.toJSON(req));
        if (CollectionUtils.isEmpty(req.getDevIds()) || req.getTenantId() == null
                || req.getProductId() == null || req.getSpaceId() == null) {
            log.error("init pack error, request: {}", JSON.toJSON(req));
            return;
        }

        Package pack = getByProductId(req.getProductId(), req.getTenantId());
        if (pack == null) {
            return;
        }

        List<String> deviceIdList = new ArrayList<>();
        deviceIdList.addAll(req.getDevIds());
        deviceIdList.add(req.getDirectDeviceId());

        //获取套包模板
        List<TemplateRule> templateRules = templateRuleService.getTemplateByPackId(pack.getId());
        if (CollectionUtils.isNotEmpty(templateRules)) {
            for (TemplateRule vo : templateRules) {
                //创建规则
                Map<String, Queue<String>> map = getProductDeviceMap(deviceIdList);
                generateRule(req, vo.getType(), vo.getJson(), map);
            }
        }
    }

    private Map<String, Queue<String>> getProductDeviceMap(List<String> deviceIdList) {
        Map<String, Queue<String>> map = new HashMap<>();

        ListDeviceInfoReq req = ListDeviceInfoReq.builder().deviceIds(deviceIdList).build();
        List<ListDeviceInfoRespVo> deviceList = deviceCoreApi.listDevices(req);
        for (ListDeviceInfoRespVo device : deviceList) {
            String productId = device.getProductId().toString();
            String deviceId = device.getUuid();
            if (productId == null || StringUtil.isEmpty(deviceId)) {
                continue;
            }

            if (!map.containsKey(productId)) {
                map.put(productId, new LinkedList<>());
            }

            map.get(productId).add(deviceId);
        }

        return map;
    }

    public void generateRule(InitPackReq req, int type, String json, Map<String, Queue<String>> productDeviceQueueMap) {
        //判断类型 0安防 1scene 2 ifttt
        if (type == 0) {
            Map<String, Object> map = JSON.parseObject(json, Map.class);
            //if
            Map<String, Object> ifMap = processIfMap((Map<String, Object>) map.get("if"), productDeviceQueueMap);
            map.put("if", ifMap);
            //then
            List<Map<String, Object>> thenList = processThenList((List<Map<String, Object>>) map.get("then"), productDeviceQueueMap);
            map.put("then", thenList);

            Long securityId = saveSecurityRule(req, map);
            map.put("securityId", securityId.toString());

            sendSecurityRuleToDevice(req.getDirectDeviceId(), req.getUserUuId(), map);

        }
        //TODO 目前只有安防
    }

    private Map<String, Object> processIfMap(Map<String, Object> ifMap, Map<String, Queue<String>> productDeviceQueueMap) {
        List<Map<String, Object>> triggerList = (List<Map<String, Object>>) ifMap.get("trigger");
        List<Map<String, Object>> newTriggerList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(triggerList)) {
            for (Map<String, Object> trigger : triggerList) {
                // 模板中DevId存的是productId
                String productId = String.valueOf(trigger.get("devId"));
                if (!productDeviceQueueMap.containsKey(productId)) {
                    continue;
                }

                Queue<String> deviceQueue = productDeviceQueueMap.get(productId);
                String deviceId = deviceQueue.poll();
                if (deviceId == null) {
                    continue;
                }

                trigger.put("devId", deviceId);
                newTriggerList.add(trigger);
            }

            ifMap.put("trigger", newTriggerList);
        }

        return ifMap;
    }

    private List<Map<String, Object>> processThenList(List<Map<String, Object>> thenList, Map<String, Queue<String>> productDeviceQueueMap) {
        List<Map<String, Object>> newThenList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(thenList)) {
            for (Map<String, Object> vo : thenList) {
                String productId = String.valueOf(vo.get("id").toString());
                if (!productDeviceQueueMap.containsKey(productId)) {
                    continue;
                }

                String deviceId = productDeviceQueueMap.get(productId).poll();
                if (deviceId == null) {
                    continue;
                }

                vo.put("id", deviceId);
                newThenList.add(vo);
            }
        }
        return newThenList;
    }

    public Package getByProductId(Long productId, Long tenantId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("product_id", productId);
        params.put("tenant_id", tenantId);
        List<Package> list = selectByMap(params);
        Package pack = null;
        if (CollectionUtils.isNotEmpty(list)) {
            pack = list.get(0);
        }
        return pack;
    }

    private Long saveSecurityRule(InitPackReq req, Map<String, Object> map) {
        Security security = securityService.getBySpaceId(req.getSpaceId(),req.getTenantId());
        if (security == null) {
            log.error("you have not set security yet");
            return -1L;
        }

        SecurityRule securityRule = new SecurityRule();
        securityRule.setEnabled(1);
        securityRule.setTenantId(req.getTenantId());
        securityRule.setSecurityId(security.getId());
        securityRule.setDefer(Integer.parseInt(map.get("defer").toString()));
        securityRule.setDelay(Integer.parseInt(map.get("delay").toString()));
        securityRule.setType(map.get("securityType").toString());
        securityRule.setIfCondition(map.get("if").toString());
        securityRule.setThenCondition(map.get("then").toString());
        securityRuleService.saveSecurityRule(securityRule);
        return security.getId();
    }

    private void sendSecurityRuleToDevice(String directDeviceId, String userUuid, Map<String, Object> payloadMap) {
        String service = "security";
        String method = "setSecurityRuleReq";

        MqttMsg mqttMsg = new MqttMsg();
        mqttMsg.setService(service);
        mqttMsg.setMethod(method);
        String topic = Constants.TOPIC_CLIENT_PREFIX + directDeviceId + "/" + service + "/" + method;
        mqttMsg.setTopic(topic);
        mqttMsg.setPayload(payloadMap);
        mqttMsg.setSrcAddr("0." + userUuid);
        mqttMsg.setSeq(String.valueOf(System.currentTimeMillis()));

        securityMqttService.setSecurityRuleReq(mqttMsg, topic);
    }
}
