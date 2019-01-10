package com.iot.robot.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/11/5 17:24
 * @Modify by:
 */

@Aspect
@Component
public class AnalysisActuatorAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisActuatorAspect.class);

    ThreadLocal<Long> beginTime = new ThreadLocal<>();

    @Pointcut("execution(* com.iot.robot.service.DeviceService.setDevAttr(..))")
    public void setDevAttrMethod() {
    };

    @Before("setDevAttrMethod()")
    public void doBeforeSetDevAttr() {
        // 记录请求到达时间
        beginTime.set(System.currentTimeMillis());
        LOGGER.info("***** doBeforeSetDevAttr, setDevAttr start");
    }

    @After("setDevAttrMethod()")
    public void doAfterSetDevAttr() {
        long endTime = System.currentTimeMillis();
        long subtract = endTime - beginTime.get();
        double second = (subtract*1.0)/(1000);
        String costTime = String.format("%.2f", second) + "s";
        LOGGER.info("***** doAfterSetDevAttr, setDevAttr costTime = {}", costTime);
    }
}
