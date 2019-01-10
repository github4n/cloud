//package com.iot.device.core;
//
//import com.alibaba.fastjson.JSON;
//import com.iot.device.BaseTest;
//import com.iot.device.model.UserDevice;
//import com.iot.redis.RedisCacheUtil;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//
///**
// * @Author: lucky
// * @Descrpiton:
// * @Date: 11:31 2018/8/1
// * @Modify by:
// */
//public class CacheCoreUtilsTest extends BaseTest {
//    String testXFZKey = "test:xfz:%s";
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Override
//    public String getBaseUrl() {
//        return null;
//    }
//
//    @Test
//    public void testAdd() {
//
//        UserDevice data = null;
//        String json = JSON.toJSONString(data);
//        String redisKey = String.format(testXFZKey, "1234567890");
////        RedisCacheUtil.delete(redisKey);
//        redisTemplate.opsForValue().set(redisKey, json, 30000L);
//    }
//
//    @Test
//    public void testGet() {
//        String redisKey = String.format(testXFZKey, "1234567890");
//        UserDevice userDevice = RedisCacheUtil.valueObjGet(redisKey, UserDevice.class);
//        if (userDevice == null) {
//            LOGGER.info("cache is null");
//            boolean hasKey = RedisCacheUtil.hasKey(redisKey);
//            if (hasKey) {
//                LOGGER.info("避免缓存穿透");
//                return;
//            }
//
//        }
//    }
//}
