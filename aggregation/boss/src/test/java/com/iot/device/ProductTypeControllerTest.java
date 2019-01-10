package com.iot.device;

import com.iot.BaseTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ProductTypeControllerTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return "/api/deviceType/";
    }

    @Test
    public void testGetDeviceType() throws Exception {
        MockHttpServletRequestBuilder builder = get(getBaseUrl() + "getAll")
                .header("token", "3226dae8-f911-4e4a-bb26-ae20daa27671")
                .header("terminal", "WEB")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder).andDo(print());
    }
}
