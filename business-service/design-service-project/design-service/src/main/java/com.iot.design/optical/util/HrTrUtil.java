package com.iot.design.optical.util;

import com.iot.design.optical.constants.Constants;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/17
 */
@Component
public class HrTrUtil {
    private Map<String, String> wzzs;

    private Logger logger = LoggerFactory.getLogger(HrTrUtil.class);

    /**
     * //从redis中获取位置指数列表 并且放在内存中
     */
    public synchronized void init() {
        if (wzzs == null) {
            wzzs = RedisCacheUtil.hashGetAll(Constants.DATA_WZZS, String.class, false);
        }
    }

    /**
     * 根据位置指数获取指数值
     * 总共三种情况
     * 第一种是刚好hr tr 是表格中直接找得到
     * 第二种是hr 或者tr 其中1个是表格中接找得到的,然后另外一个需要线性计算的
     * 第三种是hr 和tr 都需要线性计算的
     *
     * @param hr
     * @param tr
     * @return
     */
    public Float getPByTrHr(Float hr, Float tr) {
        try {
            if (wzzs == null) {
                init();
            }
            if (hr >= 1.90) {
                hr = 1.90f;
            }
            if (tr >= 3.0f) {
                tr = 3.0f;
            }
            String key = hr + "-" + tr;
            Float p = new Float("0");
            List<Float> hrs = initHrs();
            List<Float> trs = initTrs();

            //第一种情况 刚好算出来的hr 和tr是完整的坐标值
            if (wzzs.containsKey(key)) {
                String temp = wzzs.get(key).toString();
                p = new Float(temp);
                return p;
            } else {
                //判断有没有hr或者tr刚好是符合情况
                if (hrs.contains(hr.floatValue())) {
                    //hr一样  tr不一样
                    //得出需要线性计算的tr的前后的值
                    Float trMin = getMin(tr, trs);
                    Float trMax = getMax(tr, trs);
                    //拼出key 并从redis中获取对应的a值
                    String keyMin = hr.floatValue() + "-" + trMin.floatValue();
                    String keyMax = hr.floatValue() + "-" + trMax.floatValue();
                    Float a1 = new Float(wzzs.get(keyMin).toString());
                    Float a2 = new Float(wzzs.get(keyMax).toString());
                    //线性计算出新的a值
                    Float value = compluteMillion(tr, trMin, a1, a2);
                    return value;
                }
                if (trs.contains(tr.floatValue())) {
                    //hr不一样  tr一样
                    //得出需要线性计算的hr的前后的值
                    Float hrMin = getMin(hr, hrs);
                    Float hrMax = getMax(hr, hrs);
                    //拼出key 并从redis中获取对应的a值
                    String keyMin = hrMin.floatValue() + "-" + tr.floatValue();
                    String keyMax = hrMax.floatValue() + "-" + tr.floatValue();
                    Float a1 = new Float(wzzs.get(keyMin).toString());
                    Float a2 = new Float(wzzs.get(keyMax).toString());
                    //线性计算出新的a值
                    Float value = compluteMillion(hr, hrMin, a1, a2);
                    return value;

                }
                // x y 都不一样
                Float trMin = getMin(tr, trs);
                Float trMax = getMax(tr, trs);
                String keyMin = hr.floatValue() + "-" + trMin.floatValue();
                String keyMax = hr.floatValue() + "-" + trMax.floatValue();
                Float a1 = new Float(wzzs.get(keyMin).toString());
                Float a2 = new Float(wzzs.get(keyMax).toString());
                //计算出中间值
                Float value1 = compluteMillion(tr, trMin, a1, a2);
                Float value2 = compluteMillion(tr, trMax, a1, a2);
                //根据上面的  value1 value2在此计算
                Float hrMin = getMin(hr, hrs);

                return compluteMillion(hr, hrMin, value1, value2);
            }
        } catch (Exception e) {
            logger.error("获取hrtr失败:hr:" + hr + ",tr:" + tr + ",错误信息 " + e.getMessage());
            return 0f;
        }
    }


    /**
     * 获取第一个大于hr的值 如果没有就取最大值
     *
     * @param hr
     * @param lists
     * @return
     */
    public Float getMax(Float hr, List<Float> lists) {
        Collections.reverse(lists);
        for (Float one : lists) {
            if (hr.floatValue() < one) {
                return new Float(one);
            }
        }
        return new Float(lists.get(lists.size() - 1));
    }

    /**
     * 获取第一个小于hr的值 如果没有就取最大值
     *
     * @param hr
     * @param lists
     * @return
     */
    public Float getMin(Float hr, List<Float> lists) {
        Collections.reverse(lists);
        for (Float one : lists) {
            if (hr.floatValue() > one) {
                return new Float(one);
            }
        }
        return new Float(0f);
    }

    /**
     * 初始化的数据 tr的坐标点集合
     *
     * @return
     */
    public static List<Float> initTrs() {
        List<Float> trs = new ArrayList<>();
        trs.add(0.0f);
        trs.add(0.1f);
        trs.add(0.2f);
        trs.add(0.3f);
        trs.add(0.4f);
        trs.add(0.5f);
        trs.add(0.6f);
        trs.add(0.7f);
        trs.add(0.8f);
        trs.add(0.9f);
        trs.add(1.0f);
        trs.add(1.1f);
        trs.add(1.2f);
        trs.add(1.3f);
        trs.add(1.4f);
        trs.add(1.5f);
        trs.add(1.6f);
        trs.add(1.7f);
        trs.add(1.8f);
        trs.add(1.9f);
        trs.add(2.0f);
        trs.add(2.1f);
        trs.add(2.2f);
        trs.add(2.3f);
        trs.add(2.4f);
        trs.add(2.5f);
        trs.add(2.6f);
        trs.add(2.7f);
        trs.add(2.8f);
        trs.add(2.9f);
        trs.add(3.0f);
        return trs;
    }

    /**
     * 初始化的数据 hr的坐标点集合
     *
     * @return
     */
    public static List<Float> initHrs() {
        List<Float> hrs = new ArrayList<>();
        hrs.add(0.0f);
        hrs.add(0.1f);
        hrs.add(0.2f);
        hrs.add(0.3f);
        hrs.add(0.4f);
        hrs.add(0.5f);
        hrs.add(0.6f);
        hrs.add(0.7f);
        hrs.add(0.8f);
        hrs.add(0.9f);
        hrs.add(1.0f);
        hrs.add(1.1f);
        hrs.add(1.2f);
        hrs.add(1.3f);
        hrs.add(1.4f);
        hrs.add(1.5f);
        hrs.add(1.6f);
        hrs.add(1.7f);
        hrs.add(1.8f);
        hrs.add(1.9f);
        return hrs;
    }

    /**
     * 计算两个 a 的中间值
     *
     * @param temp
     * @param min
     * @param value1
     * @param value2
     * @return
     */
    public Float compluteMillion(Float temp, Float min, Float value1, Float value2) {
        Float returnValue = new Float(10);
        //10(y-min) * (a3-a1)+a1
        return ((returnValue * (temp - min)) * (value2 - value1)) + value1;
    }
}
