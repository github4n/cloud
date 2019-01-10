package com.iot.control.camera.controller;

import com.iot.common.helper.Page;
import com.iot.control.camera.service.ICameraService;
import com.iot.control.camera.vo.*;
import com.iot.control.camera.vo.api.CameraApi;
import com.iot.control.scene.service.impl.SceneServiceImpl;
import com.iot.control.scene.vo.req.LocationSceneReq;
import com.iot.control.scene.vo.rsp.LocationSceneRelationResp;
import com.iot.control.scene.vo.rsp.LocationSceneResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class CameraController implements CameraApi{
    @Autowired
    private ICameraService iCameraService;
    @Override
    public Page<CameraResp> findCameraList(@RequestBody CameraReq cameraReq, @RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "pageSize")  int pageSize) throws ParseException {
        return iCameraService.findCameraList(cameraReq,pageNumber,pageSize);
    }

    @Override
    public CameraPropertyResp count(@RequestBody CameraPropertyReq cameraPropertyReq) {
        return iCameraService.count(cameraPropertyReq);
    }

    @Override
    public List<CameraPropertyResp> countEveryHour(@RequestBody CameraPropertyReq cameraPropertyReq) {
        List<CameraPropertyResp> list = iCameraService.countEveryHour(cameraPropertyReq);
        return list;
    }

    @Override
    public CameraPropertyResp newCount() {
        CameraPropertyResp cameraPropertyResp = iCameraService.newCount();
        return cameraPropertyResp;
    }

    @Override
    public  List<CameraVo> newFace() {
        List<CameraVo> list = iCameraService.newFace();
        return list;
    }

}
