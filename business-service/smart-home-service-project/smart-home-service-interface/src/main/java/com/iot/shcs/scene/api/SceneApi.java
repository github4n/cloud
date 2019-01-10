package com.iot.shcs.scene.api;

import java.util.List;
import java.util.Map;

import com.iot.common.beans.CommonResponse;
import com.iot.shcs.device.vo.ControlReq;
import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.req.SceneDetailReqVo;
import com.iot.shcs.scene.vo.req.SceneReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;
import com.iot.shcs.scene.vo.rsp.SceneDetailVo;
import com.iot.shcs.scene.vo.rsp.SceneResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.common.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 项目名称: 立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/16 14:55
 * 修改人:
 * 修改时间：
 */

@Api("情景接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/scene")
public interface SceneApi {

    @ApiOperation("保存情景主表")
    @RequestMapping(value = "/saveScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SceneResp saveScene(@RequestBody SceneReq sceneReq);

    @ApiOperation("修改情景主表")
    @RequestMapping(value = "/updateScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SceneResp updateScene(@RequestBody SceneReq sceneReq);

    @ApiOperation("根据 userId 获取 SceneResp 列表")
    @RequestMapping(value = "/findSceneRespListByUserId", method = RequestMethod.GET)
    List<SceneResp> findSceneRespListByUserId(@RequestParam("userId") Long userId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("根据 spaceId 获取 SceneResp 列表")
    @RequestMapping(value = "/findSceneRespListBySpaceId", method = RequestMethod.GET)
    List<SceneResp> findSceneRespListBySpaceId(@RequestParam("spaceId") Long spaceId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("删除情景主表")
    @RequestMapping(value = "/delBleScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delBleScene(@RequestBody SceneReq sceneReq);

    @ApiOperation("查询场景项列表")
    @RequestMapping(value = "/getThen", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SceneDetailVo getThen(@RequestBody SceneReq sceneReq);

    @ApiOperation("添加场景项")
    @RequestMapping(value = "/addAction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addAction(@RequestBody SceneDetailReqVo actionReq);

    @ApiOperation("删除场景项")
    @RequestMapping(value = "/delAction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delAction(@RequestBody SceneDetailReqVo actionReq);

    @ApiOperation("修改场景项")
    @RequestMapping(value = "/editAction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void editAction(@RequestBody SceneDetailReqVo actionReq);

}
