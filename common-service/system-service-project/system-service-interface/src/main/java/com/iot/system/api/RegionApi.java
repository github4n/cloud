package com.iot.system.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.system.api.fallback.RegionApiFallbackFactory;
import com.iot.system.vo.CityInfoReq;
import com.iot.system.vo.CityInfoResp;
import com.iot.system.vo.CountryInfoResp;
import com.iot.system.vo.ProvinceInfoResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("区域信息接口")
@FeignClient(value = "system-service", fallbackFactory = RegionApiFallbackFactory.class)
@RequestMapping("/region")
public interface RegionApi {

    /**
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午3:29:21
     * @since 
     * @param lang
     * @return
     */
    @ApiOperation("获取国家信息")
    @RequestMapping(value = "/getCountryInfo", method = RequestMethod.GET)
    List<CountryInfoResp> getCountryInfo(@RequestParam("lang") String lang);

    /**
     * 
     * 描述：获取州省信息
     * @author 李帅
     * @created 2018年10月31日 下午3:29:29
     * @since 
     * @param countryCode
     * @param lang
     * @return
     */
    @ApiOperation("获取州省信息")
    @RequestMapping(value = "/getProvinceInfo", method = RequestMethod.GET)
    List<ProvinceInfoResp> getProvinceInfo(@RequestParam("countryCode") String countryCode, @RequestParam("lang") String lang);
    
    /**
     * 
     * 描述：获取城市信息
     * @author 李帅
     * @created 2018年11月19日 下午6:38:17
     * @since 
     * @param cityInfoReq
     * @return
     */
    @ApiOperation("获取城市信息")
    @RequestMapping(value = "/getCityInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<CityInfoResp> getCityInfo(@RequestBody CityInfoReq cityInfoReq);
}
