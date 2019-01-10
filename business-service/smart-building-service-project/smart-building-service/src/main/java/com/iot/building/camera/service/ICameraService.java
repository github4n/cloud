package com.iot.building.camera.service;

import com.iot.building.camera.vo.CameraVo;
import com.iot.building.camera.vo.req.CameraPropertyReq;
import com.iot.building.camera.vo.req.CameraReq;
import com.iot.building.camera.vo.resp.CameraPropertyResp;
import com.iot.building.camera.vo.resp.CameraResp;
import com.iot.common.helper.Page;
import java.text.ParseException;
import java.util.List;

public interface ICameraService {

	public String save(CameraReq cameraReq) throws Exception;
	
	public CameraResp findCameraProperty(CameraReq cameraReq);

	Page<CameraResp> findCameraList(CameraReq cameraReq, int pageNumber, int pageSize) throws ParseException;

	CameraPropertyResp count(CameraPropertyReq cameraPropertyReq);

    List<CameraPropertyResp> countEveryHour(CameraPropertyReq cameraPropertyReq);

    void saveDensity(CameraPropertyReq cameraPropertyReq);

    void saveRegionHourRecord(CameraPropertyReq cameraPropertyReq);

    CameraPropertyResp newCount();

    List<CameraVo>  newFace();
}
