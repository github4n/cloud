package com.iot.design.optical.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/17
 */
@Component
public class MathUtil {
    private Logger logger = LoggerFactory.getLogger(MathUtil.class);

    /**
     * 根据长宽计算面积
     *
     * @param length 长
     * @param width  宽
     * @return
     */
    public Float getS(Float length, Float width) {
        try {
            return length * width;
        } catch (Exception e) {
            logger.error("根据长宽计算面积错误:" + e.getMessage());
            return 0f;
        }
    }
}
