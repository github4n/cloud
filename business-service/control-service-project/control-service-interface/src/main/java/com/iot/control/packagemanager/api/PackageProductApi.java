package com.iot.control.packagemanager.api;

import com.iot.control.packagemanager.vo.req.SavePackageProductReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
  * @despriction：套包产品管理api
  * @author  yeshiyuan
  * @created 2018/11/23 17:45
  */
@Api(tags = "套包产品管理api")
@FeignClient(value = "control-service")
@RequestMapping(value = "/packageProduct")
public interface PackageProductApi {

    /**
      * @despriction：保存套包关联产品
      * @author  yeshiyuan
      * @created 2018/11/23 19:58
      */
    @ApiOperation(value = "保存套包关联产品", notes = "保存套包关联产品")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody SavePackageProductReq saveReq);

    /**
      * @despriction：获取产品id
      * @author  yeshiyuan
      * @created 2018/11/23 20:00
      */
    @ApiOperation(value = "获取产品id", notes = "获取产品id")
    @RequestMapping(value = "/getProductIds", method = RequestMethod.GET)
    List<Long> getProductIds(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：校验哪些产品已添加过套包
      * @author  yeshiyuan
      * @created 2018/11/26 11:15
      */
    @ApiOperation(value = "校验哪些产品已添加过套包", notes = "校验哪些产品已添加过套包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productIds", value = "产品id", dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/checkHadAddPackage", method = RequestMethod.GET)
    List<Long> chcekProductHadAdd(@RequestParam("productIds") List<Long> productIds, @RequestParam("tenantId") Long tenantId);

}
