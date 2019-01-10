package com.iot.portal.region.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.portal.region.service.RegionBusinessService;
import com.iot.portal.region.vo.GetCityInfoReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Api(description = "区域信息接口")
@RestController
@RequestMapping("/region")
public class RegionController {

    /**
     * 区域信息
     */
    @Autowired
    private RegionBusinessService regionBusinessService;

    /**
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午5:48:11
     * @since 
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "获取国家信息", notes = "获取国家信息")
    @RequestMapping(value = "/getCountryInfo", method = RequestMethod.GET)
    public CommonResponse getCountryInfo() {
        return ResultMsg.SUCCESS.info(this.regionBusinessService.getCountryInfo());
    }

    /**
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午5:48:11
     * @since 
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "获取州省信息", notes = "获取州省信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "countryCode", value = "国家代码", required = false, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/getProvinceInfo", method = RequestMethod.GET)
    public CommonResponse getProvinceInfo(@RequestParam("countryCode") String countryCode) {
        return ResultMsg.SUCCESS.info(this.regionBusinessService.getProvinceInfo(countryCode));
    }
    
    /**
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午5:48:11
     * @since 
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "获取城市信息", notes = "获取城市信息")
    @RequestMapping(value = "/getCityInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCityInfo(@RequestBody GetCityInfoReq getCityInfoReq) {
        return ResultMsg.SUCCESS.info(this.regionBusinessService.getCityInfo(getCityInfoReq));
    }
    
    /**
     * 
     * 描述：根据区号判断是否是正确的电话号码
     * @author 李帅
     * @created 2018年12月20日 下午7:43:12
     * @since 
     * @param phoneNumber
     * @param areaCode
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "根据区号判断是否是正确的电话号码", notes = "根据区号判断是否是正确的电话号码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phoneNumber", value = "手机号码", required = false, paramType = "query", dataType = "String"),
    	@ApiImplicitParam(name = "areaCode", value = "手机区号", required = false, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/isPhoneNumberValid", method = RequestMethod.GET)
    public CommonResponse isPhoneNumberValid(String phoneNumber, String areaCode) {
        return ResultMsg.SUCCESS.info(this.regionBusinessService.isPhoneNumberValid(phoneNumber, areaCode));
    }
}
