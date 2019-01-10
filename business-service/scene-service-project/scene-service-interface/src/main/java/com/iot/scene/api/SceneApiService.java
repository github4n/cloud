package com.iot.scene.api;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.scene.domain.DeviceTarValue;
import com.iot.scene.domain.RoomScene;
import com.iot.scene.domain.SceneExec;
import com.iot.scene.domain.SceneTemplate;
import com.iot.scene.vo.DeviceTarValueVO;
import com.iot.scene.vo.RoomSceneTO;
import com.iot.scene.vo.RoomTemplateTO;
import com.iot.scene.vo.SceneDetailVO;
import com.iot.scene.vo.SceneTemplateTO;
import com.iot.scene.vo.SceneTemplateVO;
import com.iot.scene.vo.TemplateDetailTO;
import com.iot.scene.vo.TemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "情景微服务接口")
public interface SceneApiService {

    @ApiOperation("保存情景模板")
    @RequestMapping(value = "/scene/saveSceneTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveSceneTemplate(@RequestBody SceneTemplate sceneTemplate) throws BusinessException;

    @ApiOperation("分页查询情景模板")
    @RequestMapping(value = "/scene/getSceneTemplateList", method = RequestMethod.GET)
    Page<SceneTemplateVO> getSceneTemplateList(@RequestParam("pageNum") String pageNum, @RequestParam("pageSize") String pageSize, @RequestParam("name") String name) throws BusinessException;

    @ApiOperation("获取情景模板下的设备")
    @RequestMapping(value = "/scene/getDeviceTarValueList", method = RequestMethod.GET)
    List<DeviceTarValue> getDeviceTarValueList(@RequestParam("templateId") String templateId) throws BusinessException;

    @ApiOperation("获取情景模板")
    @RequestMapping(value = "/scene/getSceneTemplate", method = RequestMethod.GET)
    SceneTemplateTO getSceneTemplate(@RequestParam("templateId") String templateId) throws BusinessException;

    @ApiOperation("更新情景模板")
    @RequestMapping(value = "/scene/updateSceneTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateSceneTemplate(@RequestBody TemplateDetailTO templateDetail) throws BusinessException;

    @ApiOperation("删除情景模板")
    @RequestMapping(value = "/scene/deleteSceneTemplate", method = RequestMethod.DELETE)
    void deleteSceneTemplate(@RequestParam("templateId") String templateId) throws BusinessException;

    @ApiOperation("根据名称查询情景模板")
    @RequestMapping(value = "/scene/getTemplateList", method = RequestMethod.GET)
    List<TemplateVO> getTemplateList(@RequestParam("name") String name) throws BusinessException;

    @ApiOperation("保存房间情景模板")
    @RequestMapping(value = "/saveRoomTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveRoomTemplate(@RequestBody RoomTemplateTO roomTemplate) throws BusinessException;

    @ApiOperation("根据空间id和模板id获取设备信息")
    @RequestMapping(value = "/getDeviceTarValueVOList", method = RequestMethod.GET)
    List<DeviceTarValueVO> getDeviceTarValueVOList(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) throws BusinessException;

    @ApiOperation("保存情景模板详情")
    @RequestMapping(value = "/scene/saveSceneDetail", method = RequestMethod.GET)
    void saveSceneDetail(@RequestParam("deviceTarValues") String deviceTarValues, @RequestParam("username") String username) throws BusinessException;

    @ApiOperation("根据空间id获取四个情景模板")
    @RequestMapping(value = "/scene/getTemplateListGroupByFour", method = RequestMethod.GET)
    List<List<TemplateVO>> getTemplateListGroupByFour(@RequestParam("spaceId") String spaceId) throws BusinessException;

    @ApiOperation("获取情景执行信息")
    @RequestMapping(value = "/scene/getDiffDeviceCategorySceneExecList", method = RequestMethod.GET)
    List<SceneExec> getDiffDeviceCategorySceneExecList(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) throws BusinessException;

    @ApiOperation("获取空间情景模板列表")
    @RequestMapping(value = "/scene/getTemplateBySpaceId", method = RequestMethod.GET)
    List<TemplateVO> getTemplateBySpaceId(@RequestParam("spaceId") String spaceId) throws BusinessException;

    @ApiOperation("根据空间id和模板id获取情景详情")
    @RequestMapping(value = "/scene/getSceneDetailList", method = RequestMethod.GET)
    List<SceneDetailVO> getSceneDetailList(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) throws BusinessException;

    @ApiOperation("根据房间id获取房间情景")
    @RequestMapping(value = "/scene/getRoomScene", method = RequestMethod.GET)
    List<RoomSceneTO> getRoomScene(@RequestParam("realityId") String realityId);

    @ApiOperation("根据情景id获取房间情景")
    @RequestMapping(value = "/scene/getRoomSceneBySceneId", method = RequestMethod.GET)
    List<RoomScene> getRoomSceneBySceneId(@RequestParam("sceneId") String sceneId);


    @ApiOperation("执行情景")
    @RequestMapping(value = "/scene/sceneExecute", method = RequestMethod.POST)
    void sceneExecute(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) throws Exception;


    @ApiOperation("保存楼层svg")
    @RequestMapping(value = "/scene/saveSceneSvg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveSceneSvg(@RequestParam("spaceId") String spaceId, @RequestParam("svg") String svg) throws BusinessException;

    @ApiOperation("查询楼层svg")
    @RequestMapping(value = "/scene/getSceneSvg", method = RequestMethod.GET)
    String getSceneSvg(@RequestParam("spaceId") String spaceId) throws BusinessException;


}
