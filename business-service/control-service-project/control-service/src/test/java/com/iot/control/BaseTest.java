package com.iot.control;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:32 2018/4/24
 * @Modify by:
 */
//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class BaseTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    public abstract String getBaseUrl();

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

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
            mockMvc.perform(builder);
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

                for (String key : keys
                        ) {
                    builder.param(key, params.get(key));
                }
            }

            mockMvc.perform(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public MockHttpServletRequestBuilder builder(String url, Map<String, String> params) {

        MockHttpServletRequestBuilder builder = get(url).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        if (params != null && params.size() > 0) {

            Set<String> keys = params.keySet();

            for (String key : keys
                    ) {
                builder.param(key, params.get(key));
            }
        }

        return builder;

    }
}
