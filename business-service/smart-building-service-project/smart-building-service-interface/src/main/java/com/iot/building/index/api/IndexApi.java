package com.iot.building.index.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.index.api.callback.IndexApiFallbackFactory;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexPageVo;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;
import com.iot.common.helper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
@Api("首页配置接口")
@FeignClient(value = "building-control-service" , fallbackFactory = IndexApiFallbackFactory.class)
@RequestMapping("/index")
public interface IndexApi {

    @ApiOperation("保存首页信息")
    @RequestMapping(value = "/saveIndexContent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveIndexContent(@RequestBody IndexReq indexReq);

    @ApiOperation("更新首页信息")
    @RequestMapping(value = "/updateIndexContent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateIndexContent(@RequestBody IndexReq indexReq);

    @ApiOperation("保存首页详情")
    @RequestMapping(value = "/saveIndexDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveIndexDetail(@RequestBody IndexDetailReq indexDetailReq);

    @ApiOperation("更新首页详情")
    @RequestMapping(value = "/updateIndexDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateIndexDetail(@RequestBody IndexDetailReq indexDetailReq);

  /*  @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/findCustomPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<IndexResp> findCustomPage(@RequestBody IndexPageVo indexPageVo);
*/
    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/findIndexDetailByIndexId", method = RequestMethod.GET)
    List<IndexDetailResp> findIndexDetailByIndexId(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("indexContentIdStr") Long indexContentIdStr);

    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/deleteIndexDatailByIndexId", method = RequestMethod.GET)
    void deleteIndexDatailByIndexId(@RequestParam("tenantId") Long tenantId, @RequestParam("orgId") Long orgId,@RequestParam("indexContentId") Long indexContentId);

    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/deleteIndexContent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteIndexContent(@RequestBody IndexReq indexReq);

    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/findIndexContentById", method = RequestMethod.GET)
    IndexResp findIndexContentById(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId,  @RequestParam("indexContentIdStr") Long indexContentIdStr);

    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/setAllEnableOff", method = RequestMethod.GET)
    void setAllEnableOff(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("locationId") Long locationId, @RequestParam("enable") int enable);

    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/getEnableIndex", method = RequestMethod.GET)
    IndexResp getEnableIndex(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("locationId") Long locationId);
    
    @ApiOperation("查询配置名称信息")
    @RequestMapping(value = "/getInexInfoByLocationId", method = RequestMethod.GET)
    public List<IndexResp> getInexInfoByLocationId(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("locationId") Long locationId);
    
    @ApiOperation("查询首页")
    @RequestMapping(value = "/findCustomIndex", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<IndexResp> findCustomIndex(@RequestBody IndexReq indexReq);
}
