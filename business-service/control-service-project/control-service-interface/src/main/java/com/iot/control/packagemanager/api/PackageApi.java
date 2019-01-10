package com.iot.control.packagemanager.api;

import com.iot.common.helper.Page;
import com.iot.control.packagemanager.vo.req.PackageReq;
import com.iot.control.packagemanager.vo.req.PagePackageReq;
import com.iot.control.packagemanager.vo.resp.PackageBasicResp;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "套包接口")
@FeignClient(value = "control-service")
@RequestMapping("/package")
public interface PackageApi {
    /**
     *@description 新增套包,返回新增数据id
     *@author wucheng
     *@params [req]
     *@create 2018/12/13 15:34
     *@return java.lang.Long
     */
    @ApiOperation("新增套包,返回新增数据id")
    @RequestMapping(value = "/addPackage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addPackage(@RequestBody PackageReq req);

    /**
     *@description 根据id，租户id获取套包
     *@author wucheng
     *@params [id, tenantId]
     *@create 2018/12/13 15:34
     *@return com.iot.control.packagemanager.vo.resp.PackageResp
     */
    @ApiOperation("根据id获取套包")
    @RequestMapping(value = "/getPackageById", method = RequestMethod.POST)
    PackageResp getPackageById(@RequestParam("id") Long id, @RequestParam("tenantId") Long tenantId);

    /**
     *@description 根据租户id获取该租户创建的套包信息
     *@author wucheng
     *@params [tenantId]
     *@create 2018/12/13 15:37
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageResp>
     */
    @ApiOperation("根据租户id获取该租户创建的套包信息")
    @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getPackageInfo", method = RequestMethod.GET)
    List<PackageBasicResp> getPackageInfo(@RequestParam("tenantId") Long tenantId);

    /**
     *@description 分页获取套包
     *@author wucheng
     *@params [req]
     *@create 2018/12/13 15:34
     *@return com.iot.common.helper.Page<com.iot.control.packagemanager.vo.resp.PackageResp>
     */
    @ApiOperation("分页获取套包")
    @RequestMapping(value = "/getPagePackage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<PackageResp> getPagePackage(@RequestBody PagePackageReq req);

    /**
     *@description 根据id修改套包信息
     *@author wucheng
     *@params [req]
     *@create 2018/12/13 15:34
     *@return int
     */
    @ApiOperation("根据id修改套包信息")
    @RequestMapping(value = "/updatePackageById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updatePackageById(@RequestBody PackageReq req);
    
    /**
     *@description 根据id删除数据
     *@author wucheng
     *@params [ids]
     *@create 2018/12/13 15:36
     *@return int
     */
    @ApiOperation("根据id删除数据")
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    int deleteByIds(@RequestParam("ids")  List<Long> ids);

    /**
      * @despriction：删除套包相关数据（套包基本信息、场景、策略等等）
      * @author  yeshiyuan
      * @created 2018/12/5 19:58
      */
    @ApiOperation("删除套包相关数据（套包基本信息、场景、策略等等）")
    @RequestMapping(value = "/deletePackageRelateData", method = RequestMethod.POST)
    void deletePackageRelateData(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId);
}
