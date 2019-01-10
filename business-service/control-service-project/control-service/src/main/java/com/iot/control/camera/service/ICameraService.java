package com.iot.control.camera.service;

import com.iot.common.helper.Page;
import com.iot.control.camera.vo.*;

import java.text.ParseException;
import java.util.List;

public interface ICameraService {

	public String save(CameraReq cameraReq) throws Exception;
	
	public CameraResp findCameraProperty(CameraReq cameraReq);

	Page<CameraResp> findCameraList(CameraReq cameraReq,int pageNumber,int pageSize) throws ParseException;

	CameraPropertyResp count(CameraPropertyReq cameraPropertyReq);

    List<CameraPropertyResp> countEveryHour(CameraPropertyReq cameraPropertyReq);

    void saveDensity(CameraPropertyReq cameraPropertyReq);

    void saveRegionHourRecord(CameraPropertyReq cameraPropertyReq);

    CameraPropertyResp newCount();

    List<CameraVo>  newFace();
}
