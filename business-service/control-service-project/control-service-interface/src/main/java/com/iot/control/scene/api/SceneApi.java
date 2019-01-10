package com.iot.control.scene.api;

import com.iot.control.scene.vo.req.GetSceneReq;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
@FeignClient(value = "control-service")
@RequestMapping("/scene")
public interface SceneApi {

//  sceneDetail方法

    @ApiOperation("sceneDetail通用查询")
    @RequestMapping(value = "/sceneDetailByParam", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SceneDetailResp> sceneDetailByParam(@RequestBody @Valid SceneDetailReq sceneDetailReq);

    @ApiOperation("sceneDetail通用查询(无缓存)")
    @RequestMapping(value = "/sceneDetailByParamDoing", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SceneDetailResp> sceneDetailByParamDoing(@RequestBody @Valid SceneDetailReq sceneDetailReq);

    @ApiOperation("更新")
    @RequestMapping(value = "/updateSceneDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateSceneDetail(@RequestBody @Valid SceneDetailReq sceneDetailReq);

    @ApiOperation("保存")
    @RequestMapping(value = "/insertSceneDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void insertSceneDetail(@RequestBody SceneDetailReq sceneDetailReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/deleteSceneDetail", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    int deleteSceneDetail(@RequestBody @Valid SceneDetailReq sceneDetailReq);

    @ApiOperation("根据deviceid删除")
    @RequestMapping(value = "/deleteSceneDetailByDeviceId", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    int deleteSceneDetailByDeviceId(@RequestBody SceneDetailReq sceneDetailReq);

    @ApiOperation("获取scene详情列表")
    @RequestMapping(value = "/sceneDetailList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SceneDetailResp> sceneDetailList(@RequestBody SceneDetailReq sceneDetailReq);

    @ApiOperation("统计sceneId数量")
    @RequestMapping(value = "/countChildBySceneId", method = RequestMethod.GET)
    int countChildBySceneId(@RequestParam("sceneId") Long sceneId);

    @ApiOperation("统计 每个sceneId 下的sceneDetail 数量")
    @RequestMapping(value = "/countChildBySceneIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<Long, Integer> countChildBySceneIds(@RequestBody List<Long> sceneIdList);

//  scene方法

    @ApiOperation("scene通用查询")
    @RequestMapping(value = "/sceneByParam", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SceneResp> sceneByParam(@RequestBody SceneReq sceneReq);

    @ApiOperation("scene通用查询(无缓存)")
    @RequestMapping(value = "/sceneByParamDoing", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SceneResp> sceneByParamDoing(@RequestBody SceneReq sceneReq);

    @ApiOperation("更新")
    @RequestMapping(value = "/updateScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateScene(@RequestBody @Valid SceneReq sceneReq);

    @ApiOperation("保存")
    @RequestMapping(value = "/insertScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SceneResp insertScene(@RequestBody @Valid SceneReq sceneReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/deleteScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int deleteScene(@RequestBody @Valid SceneReq sceneReq);

    @ApiOperation("查询排序最大的情景")
    @RequestMapping(value = "/maxSortSceneBySpaceId", method = RequestMethod.GET)
    SceneResp maxSortSceneBySpaceId(@RequestParam("spaceId") Long spaceId);

    @ApiOperation("保存")
    @RequestMapping(value = "/sceneById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SceneResp sceneById(@RequestBody SceneReq sceneReq);

    @ApiOperation("统计情景名称数量")
    @RequestMapping(value = "/countBySceneName", method = RequestMethod.GET)
    int countBySceneName(@RequestParam("sceneName") String sceneName, @RequestParam("userId") Long userId, @RequestParam("sceneId") Long sceneId);

    @ApiOperation("通过id批量获取scene")
    @RequestMapping(value = "/getSceneByIds",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SceneResp> getSceneByIds(@RequestBody GetSceneReq getSceneReq);

    @ApiOperation("数据迁移sceneId中的spaceId")
    @RequestMapping(value = "/moveSceneSpaceId",  method = RequestMethod.GET)
    String moveSceneSpaceId(@RequestParam("passWord") String passWord);

}
