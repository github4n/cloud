package com.iot.control.space.api;

import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;
import com.iot.control.space.api.fallback.SpaceApiFallbackFactory;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceReqVo;
import com.iot.control.space.vo.SpaceResp;
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
@FeignClient(value = "control-service", fallbackFactory = SpaceApiFallbackFactory.class)
@RequestMapping("/space")
public interface SpaceApi {

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
    @RequestMapping(value = "/findSpaceInfoBySpaceId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    SpaceResp findSpaceInfoBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

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
    List<SpaceResp> findSpaceByCondition(@RequestBody @Valid SpaceReq spaceReq);


    @ApiOperation("分页条件查询space")
    @RequestMapping(value = "/findSpacePageByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PageInfo findSpacePageByCondition(@RequestBody @Valid SpaceReq spaceReq);
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
    int countSpaceByCondition(@RequestBody @Valid SpaceReq spaceReq);

    @ApiOperation("通过条件查询space树结构")
    @RequestMapping(value = "/findTree", method = RequestMethod.GET)
    public List<Map<String, Object>> findTree(@RequestParam("locationId") Long locationId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("通过条件查询space所有子集")
    @RequestMapping(value = "/findChild", method = RequestMethod.GET)
    public List<SpaceResp> findChild(@RequestParam("tenantId")Long tenantId,@RequestParam("spaceId") Long spaceId);
    
    //==========================================old=========================


}
