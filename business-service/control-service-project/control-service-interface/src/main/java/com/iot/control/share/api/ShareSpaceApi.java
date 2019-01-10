package com.iot.control.share.api;

import com.iot.control.share.api.fallback.ShareSpaceApiFallbackFactory;
import com.iot.control.share.vo.req.AddShareSpaceReq;
import com.iot.control.share.vo.resp.ShareSpaceResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lucky
 * @InterfaceName ShareSpaceApi
 * @Description 分享家业务
 * @date 2019/1/2 14:03
 * @Version 1.0
 */
@Api("分享家业务接口")
@FeignClient(value = "control-service", fallbackFactory = ShareSpaceApiFallbackFactory.class)
@RequestMapping("/shareSpace")
public interface ShareSpaceApi {

    @ApiOperation("保存/修改分享家信息")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ShareSpaceResp saveOrUpdate(@RequestBody AddShareSpaceReq params);

    @ApiOperation("get用户具体分享家信息")
    @RequestMapping(value = "/getByShareUUID", method = RequestMethod.GET)
    ShareSpaceResp getByShareUUID(@RequestParam("tenantId") Long tenantId,
                                  @RequestParam("shareUUID") String shareUUID);

    @ApiOperation("get用户具体分享家信息")
    @RequestMapping(value = "/getByToUserId", method = RequestMethod.GET)
    ShareSpaceResp getByToUserId(@RequestParam("tenantId") Long tenantId,
                                 @RequestParam("spaceId") Long spaceId, @RequestParam("toUserId") Long toUserId);


    @ApiOperation("统计分享人所有受分享用户信息列表")
    @RequestMapping(value = "/countByFromUserId", method = RequestMethod.GET)
    int countByFromUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("fromUserId") Long fromUserId);


    @ApiOperation("list分享人所有受分享用户信息列表")
    @RequestMapping(value = "/listByFromUserId", method = RequestMethod.GET)
    List<ShareSpaceResp> listByFromUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("fromUserId") Long fromUserId);

    @ApiOperation("list分享家下的所有受分享人列表")
    @RequestMapping(value = "/listBySpaceId", method = RequestMethod.GET)
    List<ShareSpaceResp> listBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

    @ApiOperation("get用户受邀的所有分享家信息列表")
    @RequestMapping(value = "/listByToUserId", method = RequestMethod.GET)
    List<ShareSpaceResp> listByToUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("toUserId") Long toUserId);

    @ApiOperation("get用户受邀的分享家信息")
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    ShareSpaceResp getById(@RequestParam("tenantId") Long tenantId, @RequestParam("shareId") Long shareId);


    @ApiOperation("删除某人分享家信息")
    @RequestMapping(value = "/delById", method = RequestMethod.GET)
    void delById(@RequestParam("tenantId") Long tenantId, @RequestParam("shareId") Long shareId);

    @ApiOperation("删除某家分享家信息")
    @RequestMapping(value = "/delBySpaceId", method = RequestMethod.GET)
    void delBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

}
