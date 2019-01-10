package com.iot.shcs.scene.service.impl;

import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.scene.vo.req.ActionVo;
import com.iot.shcs.scene.vo.req.SceneDetailReqVo;
import com.iot.shcs.scene.vo.req.SceneReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SceneServiceImplTest {

    @Autowired
    private SceneServiceImpl sceneService;

    @Test
    public void saveScene() {
    }

    @Test
    public void updateScene() {
    }

    @Test
    public void findSceneRespListByUserId() {
    }

    @Test
    public void excScene() {
    }

    @Test
    public void findSceneDirectDeviceUuidListBySceneId() {
    }

    @Test
    public void getClientTopicId() {
    }

    @Test
    public void delSceneBySceneId() {
    }

    @Test
    public void getSceneById() {
    }

    @Test
    public void sceneDetailAndSpaceDeviceList() {
    }

    @Test
    public void delBleScene() {

        SceneReq req = new SceneReq();
        req.setCreateBy(1197l);
        req.setTenantId(2l);
        req.setId(6197l);
        req.setSceneId(6197l);

        sceneService.delBleScene(req);
    }

    @Test
    public void getSceneThen() {
        SceneReq req = new SceneReq();
        req.setCreateBy(1197l);
        req.setTenantId(2l);
        req.setId(6197l);
        req.setSceneId(6197l);
        SceneDetailVo resp = sceneService.getSceneThen(req);
        System.out.println(resp);
    }

    @Test
    public void addOrEditAction() {
        SceneDetailReqVo req = new SceneDetailReqVo();
        req.setUserUuid("96e21b9bfc6747f39ab425b7be6fedf2");
        req.setUserId(1197l);
        req.setTenantId(2l);
        req.setSceneId(6197l);
        ActionVo vo = new ActionVo();
        vo.setMethod("set_prop");
        vo.setUuid("5e534fa0f3484f0c9e24a51993f5662f");
        Map map = new HashMap();
        map.put("Dimming", 100);
        map.put("OnOff", 1);
        vo.setParams(map);
        req.setAction(vo);
        sceneService.addOrEditAction(req);
    }

    @Test
    public void delAction() {
        SceneDetailReqVo req = new SceneDetailReqVo();
        req.setUserUuid("96e21b9bfc6747f39ab425b7be6fedf2");
        req.setUserId(1197l);
        req.setTenantId(2l);
        req.setSceneId(6197l);
        ActionVo vo = new ActionVo();
        vo.setUuid("5e534fa0f3484f0c9e24a51993f5662f");
        vo.setType("device");

        req.setAction(vo);
        sceneService.delAction(req);
    }
}