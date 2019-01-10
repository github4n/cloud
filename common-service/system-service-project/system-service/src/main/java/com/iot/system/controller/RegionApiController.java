package com.iot.system.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.system.api.RegionApi;
import com.iot.system.service.LangService;
import com.iot.system.service.RegionService;
import com.iot.system.vo.CityInfoReq;
import com.iot.system.vo.CityInfoResp;
import com.iot.system.vo.CountryInfoResp;
import com.iot.system.vo.ProvinceInfoResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：区域信息
 * 功能描述：区域信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:01:39
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:01:39
 */
@RestController
public class RegionApiController implements RegionApi {

//	private static String countryRedisKey = "region:country";
//	private static String provinceRedisKey = "region:province:";
//	private static String cityRedisKey = "region:city:";
	
    @Autowired
    private RegionService regionService;
    
    @Autowired
    private LangService langService;

    /**
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午3:29:11
     * @since 
     * @param lang
     * @return
     */
    @Override
    public List<CountryInfoResp> getCountryInfo(@RequestParam("lang") String lang){
    	List<CountryInfoResp> countrys = this.regionService.getCountryInfo();
    	if(countrys == null) {
    		return null;
    	}
    	Collection<String> keys = new ArrayList<String>();
    	for(CountryInfoResp country : countrys) {
    		keys.add(country.getCountryKey());
    	}
    	Map<String, String> keyMap = langService.getLangValueByKey(keys, lang);
    	for(CountryInfoResp country : countrys) {
    		country.setCountryKey(keyMap.get(country.getCountryKey()));
    	}
        return countrys;
    }
    
    /**
     * 
     * 描述：获取州省信息
     * @author 李帅
     * @created 2018年10月31日 下午3:29:02
     * @since 
     * @param countryCode
     * @param lang
     * @return
     */
    @Override
    public List<ProvinceInfoResp> getProvinceInfo(@RequestParam("countryCode") String countryCode, @RequestParam("lang") String lang){
    	List<ProvinceInfoResp> provinces = this.regionService.getProvinceInfo(countryCode);
    	Collection<String> keys = new ArrayList<String>();
    	for(ProvinceInfoResp province : provinces) {
    		keys.add(province.getProvinceKey());
    	}
    	Map<String, String> keyMap = langService.getLangValueByKey(keys, lang);
    	for(ProvinceInfoResp province : provinces) {
    		province.setProvinceKey(keyMap.get(province.getProvinceKey()));
    	}
        return provinces;
    }
    
    /**
     * 
     * 描述：获取城市信息
     * @author 李帅
     * @created 2018年11月19日 下午6:38:04
     * @since 
     * @param cityInfoReq
     * @return
     */
    @Override
    public List<CityInfoResp> getCityInfo(@RequestBody CityInfoReq cityInfoReq){
    	List<CityInfoResp> citys = this.regionService.getCityInfo(cityInfoReq.getCountryCode(), cityInfoReq.getProvinceCode());
    	Collection<String> keys = new ArrayList<String>();
    	for(CityInfoResp city : citys) {
    		keys.add(city.getCityKey());
    	}
    	Map<String, String> keyMap = langService.getLangValueByKey(keys, cityInfoReq.getLang());
    	for(CityInfoResp city : citys) {
    		city.setCityKey(keyMap.get(city.getCityKey()));
    	}
        return citys;
    }

}
