package com.iot.smarthome.transfor.converter;

import com.alibaba.fastjson.JSONObject;
import com.iot.smarthome.constant.SmartHomeErrorCode;
import com.iot.smarthome.exception.SmartHomeException;
import com.iot.smarthome.transfor.kv.YunKeyValue;
import com.iot.smarthome.util.AttributeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/17 19:42
 * @Modify by:
 */
public class KeyValConverter {
    private static Logger LOGGER = LoggerFactory.getLogger(KeyValConverter.class);

    // 闪烁属性
    public static final String BLINK = "Blink";
    // 色温属性, 整数
    public static final String CCT = "CCT";
    public static final Integer CCT_MIN = 2700;
    public static final Integer CCT_MAX = 6500;

    // 亮度属性, 0-100(百分比)
    public static final String DIMMING = "Dimming";
    public static final Integer DIMMING_MIN = 1;
    public static final Integer DIMMING_MAX = 100;

    // 开关属性, 0关闭 1开启
    public static final String ONOFF = "OnOff";
    // IPC设备的 开关属性( 0关闭 1开启)
    public static final String TURN_ON_OFF = "TurnOnOff";
    // 颜色属性, 整数型rgb
    public static final String RGBW = "RGBW";

    public static YunKeyValue parseKeyVal(JSONObject attr) {
        LOGGER.info("parseKeyVal, attr={}", attr);
        if (attr == null) {
            throw new SmartHomeException(SmartHomeErrorCode.NOT_SUPPORTED.getCode());
        }

        String attrName = null;
        Object attrVal = null;
        try {
            attrName = attr.getString("name");
            attrVal = attr.getInteger("value");
            LOGGER.info("parseKeyVal, attrName={}, attrVal={}", attrName, attrVal);

            switch (attrName) {
                case CCT:
                    Integer cctVal = attr.getInteger("value");
                    if (cctVal < CCT_MIN) {
                        cctVal = CCT_MIN;
                    } else if (cctVal > CCT_MAX) {
                        cctVal = CCT_MAX;
                    }
                    YunKeyValue<Integer> cctKv = new YunKeyValue<Integer>();
                    cctKv.setKey(CCT);
                    cctKv.setValue(cctVal);
                    return cctKv;
                case DIMMING:
                    Integer dimVal = attr.getInteger("value");
                    if (dimVal < DIMMING_MIN) {
                        dimVal = DIMMING_MIN;
                    } else if (dimVal > DIMMING_MAX) {
                        dimVal = DIMMING_MAX;
                    }
                    YunKeyValue<Integer> dimKv = new YunKeyValue<Integer>();
                    dimKv.setKey(DIMMING);
                    dimKv.setValue(dimVal);
                    return dimKv;
                case ONOFF:
                    Byte onOffVal = 0;
                    if (AttributeUtil.isTrue(attrVal.toString())) {
                        onOffVal = 1;
                    } else {
                        onOffVal = 0;
                    }
                    YunKeyValue<Byte> onOffKv = new YunKeyValue<Byte>();
                    onOffKv.setKey(ONOFF);
                    onOffKv.setValue(onOffVal);
                    return onOffKv;
                case TURN_ON_OFF:
                    Boolean turnOnOffVal = false;
                    if (AttributeUtil.isTrue(attrVal.toString())) {
                        turnOnOffVal = true;
                    } else {
                        turnOnOffVal = false;
                    }
                    YunKeyValue<Boolean> turnOnOffKv = new YunKeyValue<Boolean>();
                    turnOnOffKv.setKey(TURN_ON_OFF);
                    turnOnOffKv.setValue(turnOnOffVal);
                case RGBW:
                    Integer rgbVal = attr.getInteger("value");
                    YunKeyValue<Integer> rgbwKv = new YunKeyValue<Integer>();
                    rgbwKv.setKey(RGBW);
                    rgbwKv.setValue(rgbVal);
                    return rgbwKv;
                default:
                    break;
            }
        } catch (Exception e) {
            throw new SmartHomeException(SmartHomeErrorCode.PARAMETER_ERROR.getCode());
        }

        LOGGER.error("parseKeyVal, not support key/value, attrName={}, attrVal={}", attrName, attrVal);
        throw new SmartHomeException(SmartHomeErrorCode.NOT_SUPPORTED.getCode());
    }
}
