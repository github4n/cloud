package com.iot.ifttt.channel.timerange;

import com.alibaba.fastjson.JSON;
import com.iot.ifttt.channel.base.CheckVo;
import com.iot.ifttt.channel.base.ICalculator;
import com.iot.ifttt.common.IftttConstants;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.util.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 描述：时间范围校验器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 8:47
 */

/**
 * 校验当前时间是否在规定范围内
 */
@Service
public class TimerRangeCalculator implements ICalculator {

    @Override
    public boolean check(CheckVo vo) {
        if (IftttConstants.IFTTT_LOGIC_AND.equals(vo.getLogic())) {
            //同时在两个时间段内，非常理，不做判断，直接返回false
            return false;
        } else {
            //or 可以在多个时间段内
            return checkOr(vo.getItems());
        }
    }

    /**
     * or 组校验
     *
     * @return
     */
    private boolean checkOr(List<AppletItem> items) {
        Boolean flag = false;
        for (AppletItem vo : items) {
            flag = checkSingle(vo.getJson());
            if (flag) {
                break;
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
        //校验当前时间是否在规定范围内
        Boolean isTrigger = false;
        Map<Object, Object> timeMap = JSON.parseObject(json, Map.class);
        String beginStr = (String) timeMap.get("begin");
        String endStr = (String) timeMap.get("end");
        List<Integer> repeat = (List<Integer>) timeMap.get("repeat");
        String area = String.valueOf(timeMap.get("area"));
        ZoneId zoneId = ZoneId.of("+00:00");
        if (!StringUtils.isEmpty(area) && !"null".equals(area)) {
            zoneId = TimeUtil.getZoneId(area);
        }
        // 当前0时区时间
        LocalDateTime areaLocalDateTime = LocalDateTime.now(zoneId);
        LocalTime areaLocalTime = areaLocalDateTime.toLocalTime();
        int w = areaLocalDateTime.getDayOfWeek().getValue();

        // 设定的 开始、结束时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime begin = LocalTime.parse(beginStr, formatter);
        LocalTime end = LocalTime.parse(endStr, formatter);

        for (int i : repeat) {
            if (i == w && areaLocalTime.isAfter(begin) && areaLocalTime.isBefore(end)) {
                isTrigger = true;
                break;
            }
        }
        return isTrigger;
    }
}
