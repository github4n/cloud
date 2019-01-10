package com.iot.building.camera.controller;

import com.iot.building.camera.api.CameraApi;
import com.iot.building.camera.service.ICameraService;
import com.iot.building.camera.vo.CameraVo;
import com.iot.building.camera.vo.req.CameraPropertyReq;
import com.iot.building.camera.vo.req.CameraReq;
import com.iot.building.camera.vo.resp.CameraPropertyResp;
import com.iot.building.camera.vo.resp.CameraResp;
import com.iot.common.helper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.List;

@RestController("bulidCameraController")
public class CameraController implements CameraApi {
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
