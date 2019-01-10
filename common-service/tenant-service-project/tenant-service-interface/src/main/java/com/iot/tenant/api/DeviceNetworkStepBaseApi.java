package com.iot.tenant.api;

import com.iot.tenant.vo.req.network.SaveNetworkStepBaseReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepBaseResp;
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
 * 项目名称：cloud
 * 功能描述：设备配网步骤模板管理（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 14:21
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 14:21
 * 修改描述：
 */
@Api(description = "设备配网步骤模板管理（BOSS使用）", tags = "设备配网步骤模板管理（BOSS使用）")
@RequestMapping(value = "/api/deviceNetworkStepBase")
@FeignClient(value = "tenant-service")
public interface DeviceNetworkStepBaseApi {

    /**
      * @despriction：保存步骤（先删后插）
      * @author  yeshiyuan
      * @created 2018/10/8 14:28
      * @return
      */
    @ApiOperation(value = "保存步骤（先删后插）", notes = "保存步骤（先删后插）")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody SaveNetworkStepBaseReq req);

    /**
      * @despriction：查询某设备类型对应的配网步骤
      * @author  yeshiyuan
      * @created 2018/10/8 17:48
      * @return
      */
    @ApiOperation(value = "查询某设备类型对应的配网步骤", notes = "查询某设备类型对应的配网步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "networkTypeId", value = "配网类型id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/queryNetworkStep", method = RequestMethod.GET)
    DeviceNetworkStepBaseResp queryNetworkStep(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam(value = "networkTypeId", required = false) Long networkTypeId);

    /**
      * @despriction：查询某设备类型支持的配网类型
      * @author  yeshiyuan
      * @created 2018/12/4 13:53
      */
    @ApiOperation(value = "查询某设备类型支持的配网类型", notes = "查询某设备类型支持的配网类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/supportNetworkType", method = RequestMethod.GET)
    List<Long> supportNetworkType(@RequestParam("deviceTypeId") Long deviceTypeId);

    /**
      * @despriction：删除设备类型的某种配网类型
      * @author  yeshiyuan
      * @created 2018/12/4 14:04
      */
    @ApiOperation(value = "删除设备类型的某种配网类型", notes = "删除设备类型的某种配网类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "networkTypeId", value = "配网类型id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/deleteByNetworkTypes", method = RequestMethod.POST)
    void deleteByNetworkTypes(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam(value = "networkTypeIds") List<Long> networkTypeIds);
}
