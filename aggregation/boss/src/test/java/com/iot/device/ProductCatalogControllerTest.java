package com.iot.device;

import com.iot.BaseTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ProductCatalogControllerTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return "/api/catalog/";
    }

    @Test
    public void testGetDeviceCatalog() throws Exception {
        MockHttpServletRequestBuilder builder = get(getBaseUrl() + "getAll")
                .header("token", "27a38595-d0d4-478d-9ad5-90a628278e9e")
                .header("terminal", "WEB")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder).andDo(print());
    }
}
