package com.iot.ifttt.channel.devstatus;

import com.alibaba.fastjson.JSON;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.ifttt.channel.base.CheckVo;
import com.iot.ifttt.channel.base.ICalculator;
import com.iot.ifttt.common.IftttConstants;
import com.iot.ifttt.common.IftttSignEnum;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 描述：设备状态校验器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 10:01
 */
@Service
public class DevStatusCalculator implements ICalculator {
    private final Logger logger = LoggerFactory.getLogger(DevStatusCalculator.class);

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;

    @Override
    public boolean check(CheckVo vo) {
        boolean flag;
        if (IftttConstants.IFTTT_LOGIC_OR.equals(vo.getLogic())) {
            flag = checkOr(vo.getItems(), vo.getMsg());
        } else {
            flag = checkAnd(vo.getItems());
        }

        //判断是否重复上报
        Map<Object, Object> maps = JSON.parseObject(vo.getMsg(), Map.class);
        String recheck = (String) maps.get("recheck");

        if (recheck != null && recheck.equals("0")) {
            //为0 则不做重复满足条件判断
            return flag;
        }

        String devId = (String) maps.get("devId");
        String field = (String) maps.get("field");
        boolean finalFlag = false;

        if (vo.getItems().size() > 0) {
            AppletItem item = vo.getItems().get(0);
            Long appletId = item.getAppletId();

            //取出上一次判断结果
            String key = RedisKeyUtil.getAppletDevCheckKey(devId, appletId, field);
            String oldFlag = RedisCacheUtil.valueGet(key); //0不满足 1满足

            if (flag && (oldFlag == null || "0".equals(oldFlag))) {
                //非重复满足
                finalFlag = true;
            }

            //更新满足结果redis
            if (flag) {
                RedisCacheUtil.valueSet(key, "1");
            } else {
                RedisCacheUtil.valueSet(key, "0");
            }
        }
        return finalFlag;
    }

    /**
     * and 组校验
     *
     * @return
     */
    private boolean checkAnd(List<AppletItem> items) {
        Boolean flag = true;
        for (AppletItem vo : items) {
            Boolean flag1 = checkSingle(vo.getJson());
            flag = flag && flag1;
        }
        return flag;
    }

    /**
     * or 组校验
     *
     * @return
     */
    private boolean checkOr(List<AppletItem> items, String msg) {
        Boolean flag = false;
        //只检测对应设备
        Map<Object, Object> maps = JSON.parseObject(msg, Map.class);
        String devId = (String) maps.get("devId");
        String field = (String) maps.get("field");
        if (CollectionUtils.isNotEmpty(items)) {
            for (AppletItem vo : items) {
                String json = vo.getJson();
                Map itemMap = (Map) JSON.parse(json);
                String itemDevId = (String) itemMap.get("devId");
                String itemField = (String) itemMap.get("field");
                if (devId.equals(itemDevId) && field.equals(itemField)) {
                    flag = checkSingle(json);
                }
            }
        }

        return flag;
    }

    /**
     * 单个校验
     *
     * @return
     */
    private Boolean checkSingle(String json) {
        /*{
            "devId": "b6f7c7f3e7a14a74be155d6a0f7f38e1",
                "field": "occupancy ",
                "sign": "==",
                "value":  "1",
                "type":"0",
        }*/
        boolean flag = false;
        try {
            Map<Object, Object> sensorConfig = JSON.parseObject(json, Map.class);
            String field = (String) sensorConfig.get("field");
            Integer type = Integer.parseInt(sensorConfig.get("type").toString());
            String sign = (String) sensorConfig.get("sign");
            Object value = sensorConfig.get("value");
            String devId = (String) sensorConfig.get("devId");

            Long tenantId = getTenantId(devId);

            //缓存状态
            Map<String, Object> redisProperties = deviceStateCoreApi.get(tenantId, devId);
            if (redisProperties == null) {
                logger.warn("设备缓存状态为空，不执行！");
                return false;
            }
            Object redisValue = redisProperties.get(field);
            logger.debug("设备状态校验，redis值：" + redisValue + ",规则值：" + value);
            flag = valueJudgment(sign, redisValue, value, type);
        } catch (Exception e) {
            logger.error("IFTTT校验错误",e);
        }
        return flag;
    }

    private Long getTenantId(String subDevId) {
        Long tenantId;
        GetDeviceInfoRespVo subDeviceInfo = deviceCoreApi.get(subDevId);
        if (!StringUtils.isEmpty(subDeviceInfo.getParentId())) {
            GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(subDeviceInfo.getParentId());
            tenantId = deviceInfo.getTenantId();
        } else {
            tenantId = subDeviceInfo.getTenantId();
        }

        return tenantId;
    }

    private boolean valueJudgment(String triggerSign, Object intValue, Object intTriggerValue, int type) {
        IftttSignEnum sign = IftttSignEnum.getEnum(triggerSign);
        Boolean flag = false;
        if (type == 0) {
            int value = Integer.parseInt(intValue.toString());
            int triggerValue = Integer.parseInt(intTriggerValue.toString());
            flag = checkInt(sign, value, triggerValue);
        } else if (type == 1) {
            float value = Float.parseFloat(intValue.toString());
            float triggerValue = Float.parseFloat(intTriggerValue.toString());
            flag = checkFloat(sign, value, triggerValue);
        } else if (type == 2) {
            String value = String.valueOf(intValue);
            String triggerValue = String.valueOf(intTriggerValue);
            switch (sign) {
                case EQUAL:
                    flag = value.equals(triggerValue) ? true : false;
                    break;
                case NOT_EQUAL:
                    flag = value.equals(triggerValue) ? false : true;
                    break;
                default:
                    break;
            }
        }

        return flag;
    }

    public boolean checkInt(IftttSignEnum sign, int value, int triggerValue) {
        boolean flag = false;
        switch (sign) {
            case LARGER:
                flag = value > triggerValue ? true : false;
                break;
            case LARGER_EQUAL:
                flag = value >= triggerValue ? true : false;
                break;
            case SMALLER:
                flag = value < triggerValue ? true : false;
                break;
            case SMALLER_EQUAL:
                flag = value >= triggerValue ? true : false;
                break;
            case EQUAL:
                flag = value == triggerValue ? true : false;
                break;
            case NOT_EQUAL:
                flag = value != triggerValue ? true : false;
                break;
            default:
                break;
        }
        return flag;
    }

    public boolean checkFloat(IftttSignEnum sign, float value, float triggerValue) {
        boolean flag = false;
        switch (sign) {
            case LARGER:
                flag = value > triggerValue ? true : false;
                break;
            case LARGER_EQUAL:
                flag = value >= triggerValue ? true : false;
                break;
            case SMALLER:
                flag = value < triggerValue ? true : false;
                break;
            case SMALLER_EQUAL:
                flag = value >= triggerValue ? true : false;
                break;
            case EQUAL:
                flag = value == triggerValue ? true : false;
                break;
            case NOT_EQUAL:
                flag = value != triggerValue ? true : false;
                break;
            default:
                break;
        }
        return flag;
    }

}
