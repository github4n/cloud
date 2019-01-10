package com.iot.center;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.iot.center.interceptor.AccessTokenInterceptor;
import com.iot.center.interceptor.EcmSysemLogInterceptor;
import com.iot.center.interceptor.PermissionInterceptor;
import com.iot.common.config.AbstractWebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.math.BigInteger;
import java.util.List;

@Configuration
public class WebConfig extends AbstractWebConfig {

    @Override
    protected void addCustomInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessTokenInterceptor());
        registry.addInterceptor(new EcmSysemLogInterceptor());
        registry.addInterceptor(new PermissionInterceptor());
    }

    /*@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        *//**
         * 1.需要定义一个convert转换消息的对象 2.创建配置信息，加入配置信息：比如是否需要格式化返回的json 3.converter中添加配置信息
         * 4.convert添加到converters当中
         *//*
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();


        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,// map 转化为 null
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty,// 字符串null返回空字符串
                SerializerFeature.WriteNullListAsEmpty,// list 为[]
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNonStringKeyAsString,
                SerializerFeature.BrowserCompatible
                //SerializerFeature.WriteDateUseDateFormat // date 为指定时间格式
        );

        //解决Long转json精度丢失的问题
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);

      *//*  //处理中文乱码问题
        List<MediaType> fastMediaTypes = Lists.newArrayList();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);*//*


        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastJsonHttpMessageConverter);
    }*/
}
