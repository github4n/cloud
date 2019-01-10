package com.iot;

import com.alibaba.fastjson.JSON;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/21 16:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//启动契约服务，模拟produer提供服务
@AutoConfigureStubRunner(ids = {"com.iot:device-service:0.0.1-SNAPSHOT:stubs:8180"}, workOffline = true)
public abstract class BaseTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    @Autowired
    protected MockMvc mockMvc;

    public abstract String getBaseUrl();

    public void mockPost(String url, Object content) {
        try {
            mockMvc.perform(post(getBaseUrl() + url).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(content))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mockGet(String url, Map<String, String> params) {
        try {
            MockHttpServletRequestBuilder builder = builder(getBaseUrl() + url, params);
            mockMvc.perform(builder).andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mockDelete(String url, Map<String, String> params) {
        try {
            MockHttpServletRequestBuilder builder = delete(getBaseUrl() + url).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
            if (params != null && params.size() > 0) {
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    builder.param(key, params.get(key));
                }
            }
            mockMvc.perform(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public MockHttpServletRequestBuilder builder(String url, Map<String, String> params) {
        MockHttpServletRequestBuilder builder = get(url).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder.param(key, params.get(key));
            }
        }
        return builder;
    }
}
