package com.iot.control.packagemanager.api;

import com.iot.control.packagemanager.vo.req.SceneInfoSaveReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: nongchongwei
 * @Descrpiton: 场景
 * @Date: 9:21 2018/10/23
 * @Modify by:
 */
@Api(tags = "套包接口")
@FeignClient(value = "control-service")
@RequestMapping("/sceneInfo")
public interface SceneInfoApi {
    /**
     *@description 根据id获取套包 下的场景
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/13 10:42
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoResp>
     */
    @ApiOperation("根据id获取套包下的场景")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageId", value = "套包id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id",dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getSceneInfoPage", method = RequestMethod.GET)
    List<SceneInfoResp> getSceneInfoPage(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId);

    /**
     *@description 保存场景
     *@author wucheng
     *@params [record]
     *@create 2018/12/13 10:41
     *@return int
     */
    @ApiOperation("保存场景")
    @RequestMapping(value = "/insertSceneInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int insertSceneInfo(@RequestBody SceneInfoSaveReq record);

    /**
     *@description 批量插入场景信息
     *@author wucheng
     *@params [record]
     *@create 2018/12/7 13:58
     *@return int
     */
    @ApiOperation("批量插入场景信息")
    @RequestMapping(value = "/batchInsertSceneInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int batchInsertSceneInfo(@RequestBody List<SceneInfoSaveReq> record);

    /**
     *@description 根据套包id和租户id获取当前套包绑定的场景数量
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/7 14:43
     *@return int
     */
    @ApiOperation("根据套包id和租户id获取当前套包绑定的场景数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageId", value = "套包id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id",dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/countSceneNumber", method = RequestMethod.POST)
    int countSceneNumber(@RequestParam("packageId")  Long packageId, @RequestParam("tenantId") Long tenantId);

    /**
     *@description
     *@author wucheng
     *@params [record]
     *@create 2018/12/7 14:58
     *@return int
     */
    @ApiOperation("根据场景id修改场景信息")
    @RequestMapping(value = "/updateByPrimaryKey", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateByPrimaryKey(@RequestBody SceneInfoSaveReq record);

    /**
      * @despriction：获取场景详情
      * @author  yeshiyuan
      * @created 2018/12/10 17:33
      */
    @ApiOperation("获取场景详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneId", value = "场景id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getSceneInfoById", method = RequestMethod.GET)
    SceneInfoResp getSceneInfoById(@RequestParam("sceneId") Long sceneId);
    
    /**
     *@description 根据场景id和租户id查询场景id和名称
     *@author wucheng
     *@params [sceneIds]
     *@create 2018/12/13 10:36
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp>
     */
    @ApiOperation("根据场景id获取场景详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneIds", value = "场景ids", dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getSceneInfoByIds", method = RequestMethod.GET)
    List<SceneInfoIdAndNameResp> getSceneInfoByIds(@RequestParam("sceneIds") List<Long> sceneIds, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：删除场景
      * @author  yeshiyuan
      * @created 2018/12/10 18:51
      */
    @ApiOperation("删除场景")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneId", value = "场景id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getSceneInfoById", method = RequestMethod.POST)
    int deleteByIdAndTenantId(@RequestParam("sceneId") Long sceneId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：校验场景是否存在
      * @author  yeshiyuan
      * @created 2018/12/10 20:35
      */
    @ApiOperation("校验场景是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneIds", value = "场景id列表", dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/checkExist", method = RequestMethod.GET)
    boolean checkExist(@RequestParam("sceneIds") List<Long> sceneIds, @RequestParam("tenantId") Long tenantId);
}
