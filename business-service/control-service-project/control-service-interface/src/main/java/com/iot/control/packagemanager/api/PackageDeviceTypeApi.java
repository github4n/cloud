package com.iot.control.packagemanager.api;

import com.iot.control.packagemanager.vo.req.SavePackageDeviceTypeReq;
import com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "control-service")
@Api(tags = "套包支持設備類型(BOSS維護)")
@RequestMapping(value = "/packageDeviceType")
public interface PackageDeviceTypeApi {

    /**
      * @despriction：保存
      * @author  yeshiyuan
      * @created 2018/11/20 21:00
      */
    @ApiOperation(value = "保存", notes = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody SavePackageDeviceTypeReq req);

    /**
      * @despriction：通过套包id找到设备类型
      * @author  yeshiyuan
      * @created 2018/11/21 10:40
      */
    @ApiOperation(value = "通过套包id找到设备类型", notes = "通过套包id找到设备类型")
    @RequestMapping(value = "/getDeviceTypesByPackageId", method = RequestMethod.GET)
    List<Long> getDeviceTypesByPackageId(@RequestParam("packageId") Long packageId);

    /**
     *@description 根据套包id删除关联的设备类型
     *@author wucheng
     *@params [packageIds]
     *@create 2018/12/6 14:12
     *@return int
     */
    @ApiOperation(value = "根据套包id删除关联的设备类型", notes = "根据套包id删除关联的设备类型")
    @RequestMapping(value = "/batchDeleteByPackageId", method = RequestMethod.POST)
    int batchDeleteByPackageId(@RequestParam("packageIds") List<Long> packageIds);
    
    /**
     *@description 根据套包id，租户id获取套包设备类型信息
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 10:35
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp>
     */
    @ApiOperation(value="根据套包id，租户id获取套包设备类型信息", notes = "根据套包id，租户id获取套包设备类型信息")
    @RequestMapping(value = "/getPackageDeviceTypeInfo", method = RequestMethod.POST)
    List<PackageDeviceTypeInfoResp> getPackageDeviceTypeInfo(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId);

}
