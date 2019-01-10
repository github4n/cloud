package com.iot.ifttt.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.redis.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述：实时API测试线程
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/22 17:10
 */
@Slf4j
public class SendUtil {
    private static long time = 0;
    private static long total = 60000;
    private static long gap = 1000;

    public static void main(String[] args) {
        //添加线程轮询
        String identity1 = "5b21e0c2ad2822350c59ff2e2c0e584cc99ff6ac";
        String identity2 = "914f541115e7ee2601d383300a9e04529d1c8b81";

        Thread t1 = new Thread() {
            @Override
            public void run() {
                log.debug("线程1启动");
                while (true) {
                    try {
                        log.info("执行任务...");
                        sendAPI(identity1);
                        sendAPI(identity2);
                        Thread.sleep(gap);
                        time = time + gap;
                    } catch (InterruptedException e) {
                        log.error("线程失败",e);
                        Thread.currentThread().interrupt();
                    }
                    log.info("已执行时长：" + time);
                    if (time >= total) {
                        break;
                    }
                }
                log.debug("线程完成任务退出");
            }

        };
        t1.setDaemon(true);
        t1.start();

        try {
            log.info("睡眠....");
            Thread.sleep(total + 5000);
        } catch (InterruptedException e) {
            log.error("线程失败",e);
            Thread.currentThread().interrupt();
        }
    }

    public static void sendAPI(String identity) {
        String url = "https://realtime.ifttt.com/v1/notifications";

        List<Map<String, Object>> dataList = Lists.newArrayList();
        Map<String, Object> item = Maps.newHashMap();
        item.put("trigger_identity", identity);
        dataList.add(item);
        Map<String, Object> data = Maps.newHashMap();
        data.put("data", dataList);
        String json = JSON.toJSONString(data);

        httpPostWithJson(url, json, "");
    }

    /**
     * 批量发送实时API
     *
     * @param identities
     */
    public static void sendNotify(List<String> identities, String iftttKey) {
        if (CollectionUtils.isEmpty(identities)) {
            return;
        }

        String url = "https://realtime.ifttt.com/v1/notifications";
        List<Map<String, Object>> dataList = Lists.newArrayList();
        Map<String, String> redisMap = Maps.newHashMap();
        String now = String.valueOf(System.currentTimeMillis());
        for (String vo : identities) {
            Map<String, Object> item = Maps.newHashMap();
            item.put("trigger_identity", vo);
            dataList.add(item);
            String key = RedisKeyUtil.getIftttCheckKey(vo);
            redisMap.put(key, now);
        }
        //添加缓存
        RedisCacheUtil.mset(redisMap, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_2);

        Map<String, Object> data = Maps.newHashMap();
        data.put("data", dataList);
        String json = JSON.toJSONString(data);
        httpPostWithJson(url, json, iftttKey);
    }

    public static String httpPostWithJson(String url, String json, String iftttKey) {
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = null;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("IFTTT-Service-Key", iftttKey);
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，必须用此方法

        } catch (Exception e) {
            log.error("调用接口失败！",e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("关闭httpClient 失败！",e);
            }
        }
        //第五步：处理返回值
        return returnValue;
    }
}
