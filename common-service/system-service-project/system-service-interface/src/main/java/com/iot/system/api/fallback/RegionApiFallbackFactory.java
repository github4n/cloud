package com.iot.system.api.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.iot.system.api.RegionApi;
import com.iot.system.vo.CityInfoReq;
import com.iot.system.vo.CityInfoResp;
import com.iot.system.vo.CountryInfoResp;
import com.iot.system.vo.ProvinceInfoResp;

import feign.hystrix.FallbackFactory;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：区域信息
 * 功能描述：区域信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:01:24
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:01:24
 */
@Component
public class RegionApiFallbackFactory implements FallbackFactory<RegionApi> {

    @Override
    public RegionApi create(Throwable cause) {

        return new RegionApi() {
            @Override
            public List<CountryInfoResp> getCountryInfo(String lang) {
                return null;
            }
            
            @Override
            public List<ProvinceInfoResp> getProvinceInfo(String countryCode, String lang) {
                return null;
            }
            
            @Override
            public List<CityInfoResp> getCityInfo(CityInfoReq cityInfoReq) {
                return null;
            }
        };

    }
}
