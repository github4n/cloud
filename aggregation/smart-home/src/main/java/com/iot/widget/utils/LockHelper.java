package com.iot.widget.utils;

import com.alibaba.fastjson.JSON;
import com.iot.common.util.StringUtil;
import com.iot.mqttsdk.common.MqttMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Descrpiton: 锁工具类
 * @Author: yuChangXing
 * @Date: 2018/10/8 17:45
 * @Modify by:
 */
public class LockHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(LockHelper.class);

    private static ConcurrentHashMap<String, Object> acks = new ConcurrentHashMap<>();

    public static void put(String key, Object obj) {
        acks.put(key, obj);
    }

    public static Object get(String key) {
        return acks.get(key);
    }

    public static Object remove(String key) {
        return acks.remove(key);
    }

    public static long getAckSize() {
        return acks.size();
    }


    public static void processMessage(MqttMsg mqttMsg) {
        if (mqttMsg == null) {
            LOGGER.debug("***** LockHelper, process end, because mqttMsg obj is null.");
            return;
        }

        try {
            String seq = mqttMsg.getSeq();
            if (seq == null) {
                LOGGER.debug("***** LockHelper, process end, because seq is null.");
                return;
            }

            // 只处理 seq 前缀带 "w_"
            if (!seq.startsWith(WidgetUtil.SEQ_PREFIX)) {
                LOGGER.debug("***** LockHelper, processMessage, seq={} do not contains 'w_'", seq);
                return;
            }

            LOGGER.debug("***** LockHelper, processMessage, mqttMsg.jsonString={}", JSON.toJSONString(mqttMsg));

            LockObject lo = (LockObject) LockHelper.get(seq);
            LOGGER.debug("***** LockHelper, process, current LockHelper.ackSize = {}", LockHelper.getAckSize());
            if (lo == null) {
                LOGGER.debug("***** LockHelper, process end, because lo is null.");
                return;
            }

            synchronized (lo) {
                LockHelper.put(seq, mqttMsg);
                lo.notifyAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
