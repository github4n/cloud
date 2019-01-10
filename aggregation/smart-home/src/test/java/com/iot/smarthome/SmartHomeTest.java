package com.iot.smarthome;

import com.alibaba.fastjson.JSON;
import com.iot.cloud.vo.SpaceVo;
import com.iot.scene.vo.SceneReq;
import com.iot.user.service.UserBusinessService;
import com.iot.user.vo.UserLoginParamVO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class SmartHomeTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return "";
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    private UserBusinessService userBusiness;

    @Test
    public void testSend() {
        userBusiness.sendPasswordErrorVerifyCode("qizhiyong@leedarson.com", -1L);
    }

    @Test
    public void testLogin() throws Exception {
        UserLoginParamVO vo = new UserLoginParamVO();
        vo.setPassWord("d335db7a7944182666ecc6f984ff1d52");
        vo.setUserName("qizhiyong@leedarson.com");
        mockMvc.perform(post(getBaseUrl() + "/user/login").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(vo))
        ).andDo(print());
    }

    @Test
    public void testGetRoomList() throws Exception {
        SpaceVo vo = new SpaceVo();
        vo.setCookieUserId("c232a44b8cfd477e90ddf4a286649178");
        vo.setParentId(1L);
        vo.setOffset(0);
        vo.setPageSize(100);
        mockMvc.perform(post(getBaseUrl() + "/home/getRoomList")
                .header("token", "14bd30f7-7073-4cfb-afe9-27af86a02513")
                .header("terminal", "APP")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(vo))
        );
    }

    @Test
    public void testAddScene() throws Exception {
        SceneReq req = new SceneReq();
        req.setCookieUserId("c232a44b8cfd477e90ddf4a286649178");
        req.setHomeId(1L);
        req.setIcon("default");
        req.setName("abc");
        mockMvc.perform(post(getBaseUrl() + "/scene/addScene")
                .header("token", "14bd30f7-7073-4cfb-afe9-27af86a02513")
                .header("terminal", "APP")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(req))
        ).andDo(print());
    }

    @Test
    public void testGetHomeList() throws Exception {
        SpaceVo req = new SpaceVo();
        req.setCookieUserId("cd2096de2fe54309b3b5148e205e606f");
        req.setOffset(0);
        req.setPageSize(100);

        mockMvc.perform(post(getBaseUrl() + "/home/getHomeList")
                .header("token", "3e5d7235-d8d5-43ed-935a-047b75f99b72")
                .header("terminal", "app")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(req))
        ).andDo(print());
    }
}
