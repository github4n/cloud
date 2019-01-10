package com.iot.control.camera.vo.api;

import com.iot.common.helper.Page;
import com.iot.control.camera.vo.*;
import com.iot.control.scene.vo.req.*;
import com.iot.control.scene.vo.rsp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

@Api("人脸接口")
@FeignClient(value = "control-service")
@RequestMapping("/camera")
public interface CameraApi {




 /*   @ApiOperation(value = "分页查找camera_recode")
    @RequestMapping(value = "/findLocationSceneListStr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<LocationSceneResp> findLocationSceneListStr(@RequestBody LocationSceneReq locationSceneReq);

    @ApiOperation("获取location_scene_list 的列表")
    @RequestMapping(value = "/findLocationSceneRelationList", method = RequestMethod.GET)
    List<LocationSceneRelationResp> findLocationSceneRelationList();*/

    @ApiOperation(value = "分页查找camera_recode")
    @RequestMapping(value = "/findCameraList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<CameraResp> findCameraList(@RequestBody CameraReq cameraReq,@RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "pageSize")  int pageSize) throws ParseException;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CameraPropertyResp count(@RequestBody CameraPropertyReq cameraPropertyReq);

    @RequestMapping(value = "/countEveryHour", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<CameraPropertyResp> countEveryHour(@RequestBody CameraPropertyReq cameraPropertyReq);

    @RequestMapping(value = "/newCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CameraPropertyResp newCount();

    @RequestMapping(value = "/newFace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<CameraVo> newFace();
}
