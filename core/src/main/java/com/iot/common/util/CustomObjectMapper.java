package com.iot.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @Author: xfz
 * @Descrpiton: long /date 时间等格式json 字符串转化返回
 * @Date: 11:29 2018/5/17
 * @Modify by:
 */
public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 3223645203459453114L;

    /**
     * 构造函数
     */
    public CustomObjectMapper() {
        super();
        SimpleModule simpleModule = new SimpleModule();
        /**
         * 将long类型的数据转为String类型
         */
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long.class, ToStringSerializer.instance);
        registerModule(simpleModule);
    }
}