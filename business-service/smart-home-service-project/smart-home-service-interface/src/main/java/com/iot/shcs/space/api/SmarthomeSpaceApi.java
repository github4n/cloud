package com.iot.shcs.space.api;

import com.iot.common.helper.Page;
import com.iot.shcs.space.api.fallback.SmarthomeSpaceApiFallbackFactory;
import com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.shcs.space.vo.SpacePageResp;
import com.iot.shcs.space.vo.SpaceReq;
import com.iot.shcs.space.vo.SpaceReqVo;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.space.vo.SpaceRespVo;
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

@Api("空间接口")
@FeignClient(value = "smart-home-service", fallbackFactory = SmarthomeSpaceApiFallbackFactory.class)
@RequestMapping("/space")
public interface SmarthomeSpaceApi {

    @ApiOperation("空间保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long save(@RequestBody @Valid SpaceReq spaceReq);

    @ApiOperation("空间修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void update(@RequestBody @Valid SpaceReq spaceReq);

    @ApiOperation("根据条件修改空间")
    @RequestMapping(value = "/updateSpaceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean updateSpaceByCondition(@RequestBody @Valid SpaceReqVo reqVo);

    @ApiOperation("根据spaceId删除空间")
    @RequestMapping(value = "/deleteSpaceBySpaceId", method = RequestMethod.GET)
    boolean deleteSpaceBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

    @ApiOperation("批量删除space")
    @RequestMapping(value = "/deleteSpaceByIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean deleteSpaceByIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req);

    @ApiOperation("通过spaceId查找space详情")
    @RequestMapping(value = "/findSpaceInfoBySpaceId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SpaceResp findSpaceInfoBySpaceId(@RequestParam("tenantId")Long tenantId, @RequestParam("spaceId") Long spaceId);

    @ApiOperation("通过spaceIds查找space详情")
    @RequestMapping(value = "/findSpaceInfoBySpaceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> findSpaceInfoBySpaceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req);

    /**
     * 根据父节点查询子节点
     *
     * @param spaceReq
     * @return
     */
    @ApiOperation("根据父节点查询子节点")
    @RequestMapping(value = "/findSpaceByParentId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> findSpaceByParentId(@RequestBody SpaceReq spaceReq);


    /**
     * @Description: 条件查询space
     *（可选择查询条件id、parent_id、name、user_id、location_id、
     * type、sort、tenant_id、default_space、org_id）
     * @param spaceReq
     * @return:
     * @author: chq
     * @date: 2018/10/11 19:52
     **/
    @ApiOperation("条件查询space")
    @RequestMapping(value = "/findSpaceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> findSpaceByCondition(@RequestBody SpaceReq spaceReq);

    /**
     * @Description: 通过条件统计space数量
     *（可选择查询条件id、parent_id、name、user_id、location_id、
     * type、sort、tenant_id、default_space、org_id）
     * @param spaceReq
     * @return:
     * @author: chq
     * @date: 2018/10/11 19:54
     **/
    @ApiOperation("通过条件统计space数量")
    @RequestMapping(value = "/countSpaceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int countSpaceByCondition(@RequestBody SpaceReq spaceReq);

    @ApiOperation("获取家列表")
    @RequestMapping(value = "/getHomePage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<SpacePageResp> getHomePage(@RequestBody SpaceReq req);

    @ApiOperation("添加空间")
    @RequestMapping(value = "/addSpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SpaceRespVo addSpace(@RequestBody @Valid SpaceReq req);

    @ApiOperation("修改空间")
    @RequestMapping(value = "/editSpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void editSpace(@RequestBody SpaceReq space);



    @ApiOperation("根据用户ID和空间ID")
    @RequestMapping(value = "/deleteSpaceBySpaceIdAndUserId", method = RequestMethod.GET)
    void deleteSpaceBySpaceIdAndUserId(@RequestParam("spaceId") Long spaceId, @RequestParam("userId") Long userId,  @RequestParam("tenantId")Long tenantId);

    //==========================================old=========================
    /**
     * 查询空间（家或房间）名称是否存在
     *
     * @param spaceReq
     * @return flag
     */
    @ApiOperation("查询空间（家或房间）名称是否存在")
    @RequestMapping(value = "/checkSpaceName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean checkSpaceName(@RequestBody SpaceReq spaceReq);

    /**
     * 查找用户的默认家
     *
     * @param userId
     * @return
     */
    @ApiOperation("查找用户的默认家")
    @RequestMapping(value = "/findUserDefaultSpace", method = RequestMethod.GET)
    SpaceResp findUserDefaultSpace(@RequestParam("userId") Long userId, @RequestParam("tenantId") Long tenantId);

    /**
     * 根据用户ID和类型查询空间
     *
     * @param spaceReq
     * @return
     * @author fenglijian
     */
    @ApiOperation("根据用户ID和类型查询空间")
    @RequestMapping(value = "/findSpaceByType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> findSpaceByType(@RequestBody SpaceReq spaceReq);


    @ApiOperation("获取房间列表")
    @RequestMapping(value = "/getRoomPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<SpacePageResp> getRoomPage(@RequestBody SpaceReq req);

    @ApiOperation("获取spaceId关联的直接设备id")
    @RequestMapping(value = "/getDirectDeviceUuidBySpaceId", method = RequestMethod.GET)
    List<String> getDirectDeviceUuidBySpaceId(@RequestParam("tenantId")Long tenantId, @RequestParam("spaceId")Long spaceId);

}
