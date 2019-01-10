package com.iot.device.api;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.fallback.ServiceModuleApiFallbackFactory;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.GenerateModuleAgreementRsp;
import com.iot.device.vo.rsp.ServiceModuleInfoResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModuleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 8:58 2018/7/2
 * @Modify by:
 */
@Api(tags = "功能定义管理接口")
@FeignClient(value = "device-service", fallbackFactory = ServiceModuleApiFallbackFactory.class)
@RequestMapping("/serviceModule")
public interface ServiceModuleApi {

    @ApiOperation("根据类型获取所有系统功能定义")
    @RequestMapping(value = "/findServiceModuleListByDeviceTypeId", method = RequestMethod.GET)
    List<ServiceModuleListResp> findServiceModuleListByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId);

    @ApiOperation("获取所有系统功能定义")
    @RequestMapping(value = "/findServiceModuleListByParentIdNull", method = RequestMethod.GET)
    List<ServiceModuleListResp> findServiceModuleListByParentIdNull();

    @ApiOperation("获取产品下对应的所有功能定义")
    @RequestMapping(value = "/findServiceModuleListByProductId", method = RequestMethod.GET)
    List<ServiceModuleListResp> findServiceModuleListByProductId(@RequestParam("productId") Long productId);

    @ApiOperation("获取功能定义详细")
    @RequestMapping(value = "/getServiceModuleInfoByServiceModuleId", method = RequestMethod.GET)
    ServiceModuleInfoResp getServiceModuleInfoByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId);

    @ApiOperation("更新已有的功能组信息")
    @RequestMapping(value = "/updateServiceModuleInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateServiceModuleInfo(@RequestBody AddOrUpdateServiceModuleReq serviceModuleReq);

    @ApiOperation("保存或更新")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody ServiceModuleReq serviceModuleReq);


    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("获取全部")
    @RequestMapping(value = "/list" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo<ServiceModuleResp> list(@RequestBody ServiceModuleReq serviceModuleReq);

    @ApiOperation("根据device_type_id获取")
    @RequestMapping(value = "/listByDeviceTypeId" , method = RequestMethod.GET)
    List<ServiceModuleResp> listByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("根据productId获取")
    @RequestMapping(value = "/listByProductId" , method = RequestMethod.GET)
    List<ServiceModuleResp> listByProductId(@RequestParam("productId") Long productId);

    @ApiOperation("根据productId获取功能组ids")
    @RequestMapping(value = "/listServiceModuleIdsByProductId" , method = RequestMethod.GET)
    List<Long> listServiceModuleIdsByProductId(@RequestParam("productId") Long productId);

    @ApiOperation("生成Moduleid")
    @RequestMapping(value = "/generateModuleId" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    String generateModuleId(@RequestBody GenerateModuleReq generateModuleReq);


    @ApiOperation("分页获取moduleId")
    @RequestMapping(value = "/generateModuleList" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo generateModuleList(@RequestBody GenerateModuleReq generateModuleReq);

    @ApiOperation("获取协议")
    @RequestMapping(value = "/generateModuleAgreementList" , method = RequestMethod.GET)
    List<GenerateModuleAgreementRsp> generateModuleAgreementList();


    @ApiOperation("拷贝deviceType的功能组信息给产品id")
    @RequestMapping(value = "/copyModule", method = RequestMethod.GET)
    void copyModule(@RequestParam("sourceDeviceTypeId") Long sourceDeviceTypeId, @RequestParam("targetProductId") Long targetProductId);

    @ApiOperation("删除产品下的功能组")
    @RequestMapping(value = "/delModuleByProductId", method = RequestMethod.GET)
    void delModuleByProductId(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId);



}
