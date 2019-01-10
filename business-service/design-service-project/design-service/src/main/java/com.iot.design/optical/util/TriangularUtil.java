package com.iot.design.optical.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 三角函数工具类
 * 创建人:chenweida
 * 创建时间:2018/9/17
 */
@Component
public class TriangularUtil {
    private Logger logger = LoggerFactory.getLogger(TriangularUtil.class);

    /**
     * 根据cos值获取角度值
     *
     * @param cosA
     * @return
     * @throws Exception
     */
    public Float getAngleByCos(Float cosA) {
        try {
            BigDecimal angle = new BigDecimal(Math.toDegrees(Math.acos(cosA.doubleValue())));
            return angle.floatValue();
        } catch (Exception e) {
            logger.error("根据cos值获取角度值错误:" + e.getMessage());
            return 0F;
        }
    }

    /**
     * 根据cos值获取sin值
     *
     * @param cosA
     * @return
     * @throws Exception
     */
    public Float getSinByCos(Float cosA) {
        try {
            BigDecimal cosABD = new BigDecimal(cosA).pow(2);
            Double sin = Math.sqrt(new BigDecimal(1F - cosABD.floatValue()).doubleValue());
            return sin.floatValue();
        } catch (Exception e) {
            logger.error("根据cos值获取sin值错误:" + e.getMessage());
            return 0F;
        }
    }


}
