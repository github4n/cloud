package com.iot.tenant.api;

import com.iot.common.helper.Page;
import com.iot.tenant.api.fallback.VirtualOrgApiFallbackFactory;
import com.iot.tenant.vo.req.AddUserOrgReq;
import com.iot.tenant.vo.req.org.GetOrgByPageReq;
import com.iot.tenant.vo.req.org.SaveOrgReq;
import com.iot.tenant.vo.resp.org.OrgResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "组织服务接口")
@FeignClient(value = "tenant-service", fallbackFactory = VirtualOrgApiFallbackFactory.class)
@RequestMapping("/virtualOrg")
public interface VirtualOrgApi {

    @ApiOperation("新增组织")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean add(@RequestBody SaveOrgReq req);

    @ApiOperation("编辑组织")
    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean edit(@RequestBody List<SaveOrgReq> req);

    @ApiOperation("删除组织")
    @RequestMapping(value = "/del", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean del(@RequestBody List<Long> ids);

    @ApiOperation("分页查询组织")
    @RequestMapping(value = "/selectByPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<OrgResp> selectByPage(@RequestBody GetOrgByPageReq req);

    @ApiOperation("获取组织树")
    @RequestMapping(value = "/getChildrenTree", method = RequestMethod.GET)
    List<OrgResp> getChildrenTree(@RequestParam("parentId") Long parentId);

    @ApiOperation("获取子组织主键集")
    @RequestMapping(value = "/getChildren", method = RequestMethod.GET)
    List<Long> getChildren(@RequestParam("parentId") Long parentId);


    ///////////////////////////////////////////////// 旧接口 ////////////////////////////////////////////////////////////

    @ApiOperation("添加用户和组织信息关系")
    @RequestMapping(value = "/addUserOrg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addUserOrg(@RequestBody AddUserOrgReq req);
}
