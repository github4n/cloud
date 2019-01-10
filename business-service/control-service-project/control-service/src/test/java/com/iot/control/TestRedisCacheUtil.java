package com.iot.control;

import com.iot.redis.RedisCacheUtil;
import org.junit.Test;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/8/3 16:19
 * @Modify by:
 */
public class TestRedisCacheUtil extends BaseTest {
    @Override
    public String getBaseUrl() {
        return null;
    }


    @Test
    public void test3() throws InterruptedException {
        String key = "seq:delScene:110";
        boolean result = RedisCacheUtil.setNx(key, String.valueOf(System.currentTimeMillis()), 10L);
        System.out.println("**************************");
        System.out.println("**************************");
        System.out.println("**************************");
        System.out.println("**************************");
        System.out.println(result);
        System.out.println(RedisCacheUtil.valueGet(key));

        Thread.sleep(5*1000L);
        result = RedisCacheUtil.setNx(key, String.valueOf(System.currentTimeMillis()), 10L);
        System.out.println("**************************");
        System.out.println("**************************");
        System.out.println("**************************");
        System.out.println("**************************");
        System.out.println(result);
        System.out.println(RedisCacheUtil.valueGet(key));

        Thread.sleep(5*1000L);
        System.out.println("结束");
    }
}
