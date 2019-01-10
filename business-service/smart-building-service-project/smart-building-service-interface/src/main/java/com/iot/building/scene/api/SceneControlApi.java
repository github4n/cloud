package com.iot.building.scene.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.scene.vo.req.LocationSceneDetailReq;
import com.iot.building.scene.vo.req.LocationSceneRelationReq;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.req.SceneTemplateManualReq;
import com.iot.building.scene.vo.resp.LocationSceneDetailResp;
import com.iot.building.scene.vo.resp.LocationSceneRelationResp;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.common.helper.Page;
import com.iot.control.scene.vo.req.SceneAddReq;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;

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
@FeignClient(value = "building-control-service")
@RequestMapping("/scene")
public interface SceneControlApi {

    /**
     * 保存情景微调
     *
     */
	@ApiOperation("保存情景微调")
    @RequestMapping(value = "/saveSceneAndSceneDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveSceneAndSceneDetail(@RequestBody SceneAddReq sceneAddReq);


    /**
     * 获取情景详情列表
     *
     * @return
     * @author linjihuang
     * @created 2017年12月12日 下午4:22:33
     * @since
     */
    @ApiOperation("获取情景详情列表")
    @RequestMapping(value = "/findSceneDetailInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<SceneDetailResp> findSceneDetailInfo(@RequestBody SceneDetailReq sceneDetailReq);

    /**
     * 更新情景微调信息
     *
     * @return
     * @author linjihuang
     * @created 2018年3月1日 上午10:26:46
     */
    @ApiOperation("更新情景微调信息")
    @RequestMapping(value = "/updateSceneDetailInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateSceneDetailInfo(@RequestBody SceneAddReq sceneAddReq);

    /**
     * 根据ID删除情景微调
     *
     * @param sceneId
     * @return
     * @author wanglei
     */
    @ApiOperation("根据ID删除情景微调")
    @RequestMapping(value = "/deleteSceneDetail", method = RequestMethod.GET)
    public void deleteSceneDetail(@RequestParam("tenantId") Long tenantId, @RequestParam("sceneId")Long sceneId, @RequestParam("spaceId")Long spaceId, @RequestParam("userId")Long userId);

    /**
     * 情景执行
     *
     * @param sceneId
     * @return
     * @author wanglei
     */
    @ApiOperation("情景执行-中控")
    @RequestMapping(value = "/sceneExecute", method = RequestMethod.GET)
    public void sceneExecute(@RequestParam("tenantId") Long tenantId, @RequestParam("sceneId") Long sceneId);

    /**
     * 获取房间情景列表
     *
     * @param spaceId
     */
    @ApiOperation("获取房间情景列表")
    @RequestMapping(value = "/findSceneDetailList", method = RequestMethod.GET)
    public List<SceneResp> findSceneDetailList(@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

    @ApiOperation("保存/修改整校主表")
    @RequestMapping(value = "/saveOrUpdateLocationScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateLocationScene(@RequestBody LocationSceneReq locationSceneReq);

    @ApiOperation("保存/修改整校主表")
    @RequestMapping(value = "/saveLocationScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveLocationScene(@RequestBody LocationSceneReq locationSceneReq);

    @ApiOperation(" 查询整校locationScene的列表，通过tenantId查询")
    @RequestMapping(value = "/findLocationSceneList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<LocationSceneResp> findLocationSceneList(@RequestBody LocationSceneReq locationSceneReq);

    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/findLocationSceneListStr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<LocationSceneResp> findLocationSceneListStr(@RequestBody LocationSceneReq locationSceneReq);

    @ApiOperation("保存/修改整校子表")
    @RequestMapping(value = "/saveOrUpdateLocationSceneDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateLocationSceneDetail(@RequestBody LocationSceneDetailReq locationSceneDetailReq);

    @ApiOperation(" 查询整校locationSceneDetail的列表，通过tenantId和locationSceneId查询")
    @RequestMapping(value = "/findLocationSceneDetailList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<LocationSceneDetailResp> findLocationSceneDetailList(LocationSceneDetailReq locationSceneDetailReq);

    @ApiOperation("删除location_scene表中的数据,通过id")
    @RequestMapping(value = "/deleteLocationScene", method = RequestMethod.GET)
    void deleteLocationScene(@RequestParam("tenantId") Long tenantId, @RequestParam("id")Long id);

    @ApiOperation("删除location_scene_detail表中的数据,通过tenantId和locationSceneId")
    @RequestMapping(value = "/deleteLocationSceneDetail", method = RequestMethod.GET)
    void deleteLocationSceneDetail(@RequestParam("tenantId")Long tenantId,@RequestParam("locationSceneId") Long locationSceneId);

    @ApiOperation("根据用户的的根节点查询空间树结构")
    @RequestMapping(value = "/findTree", method = RequestMethod.GET)
    List<Map<String, Object>> findTree(@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId, @RequestParam("locationId") Long locationId);

    @ApiOperation("删除location_scene_detail表中的数据,通过locationSceneId")
    @RequestMapping(value = "/deleteLocationSceneDetailStr", method = RequestMethod.GET)
    void deleteLocationSceneDetailStr(@RequestParam("tenantId") Long tenantId, @RequestParam("locationSceneId") Long locationSceneId);

    @ApiOperation("修改整校父表")
    @RequestMapping(value = "/updateLocationScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateLocationScene(@RequestBody LocationSceneReq locationSceneReq);

    @ApiOperation("通过locationSceneId 查询 sceneId的集合")
    @RequestMapping(value = "/findSceneIds", method = RequestMethod.GET)
    List<Long> findSceneIds(@RequestParam("tenantId") Long tenantId, @RequestParam("locationSceneId") Long locationSceneId);

    @ApiOperation("保存整location_scene_relation表")
    @RequestMapping(value = "/saveLocationSceneRelation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveLocationSceneRelation(@RequestBody LocationSceneRelationReq locationSceneRelationReq);

    @ApiOperation("获取location_scene_list 的列表")
    @RequestMapping(value = "/findLocationSceneRelationList", method = RequestMethod.GET)
    List<LocationSceneRelationResp> findLocationSceneRelationList(@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("查询scene列表")
    @RequestMapping(value = "/querySceneList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SceneResp> querySceneList(@RequestBody SceneDetailReq req);

    @ApiOperation("获取space当前的情景ID")
    @RequestMapping(value = "/getSpaceSceneStatus", method = RequestMethod.GET)
    Long getSpaceSceneStatus(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);
    
    @ApiOperation("更新space的情景ID")
    @RequestMapping(value = "/setSpaceSceneStatus", method = RequestMethod.GET)
    Long setSpaceSceneStatus(@RequestParam("tenantId") Long tenantId, @RequestParam("sceneId") Long sceneId);
    
    @ApiOperation("情景模板手动初始化")
    @RequestMapping(value = "/sceneTemplateManualInit", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sceneTemplateManualInit(@RequestBody SceneTemplateManualReq req);
    
    @ApiOperation("情景模板手动初始化")
    @RequestMapping(value = "/templateManualInit", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public void templateManualInit(@RequestBody SceneTemplateManualReq req);
    
    @ApiOperation("情景下发")
    @RequestMapping(value = "/issueScene", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public void issueScene(@RequestBody SceneReq req);

    @RequestMapping(value = "/findLocationSceneListByName", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    List<LocationSceneResp> findLocationSceneListByName(@RequestBody LocationSceneReq req);
}
