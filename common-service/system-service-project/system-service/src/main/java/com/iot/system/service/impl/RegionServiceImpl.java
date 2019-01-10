package com.iot.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.redis.RedisCacheUtil;
import com.iot.system.dao.RegionMapper;
import com.iot.system.service.RegionService;
import com.iot.system.vo.CityInfoResp;
import com.iot.system.vo.CountryInfoResp;
import com.iot.system.vo.ProvinceInfoResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：区域信息
 * 功能描述：区域信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:01:57
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:01:57
 */
@Service("regionService")
public class RegionServiceImpl implements RegionService {

	private static String countryRedisKey = "region:country";
	private static String provinceRedisKey = "region:province:";
	private static String cityRedisKey = "region:city:";
	
    @Autowired
    private RegionMapper regionMapper;
    
    /** 
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午2:06:47
     * @since 
     * @return
     */
    @Override
    public List<CountryInfoResp> getCountryInfo() {
    	List<CountryInfoResp> countrys = RedisCacheUtil.listGetAll(countryRedisKey, CountryInfoResp.class);
    	if(countrys == null || countrys.size() == 0) {
    		countrys = regionMapper.getCountryInfo();
    		RedisCacheUtil.listSet(countryRedisKey, countrys);
    	}
    	return countrys;
    }
    
    /**
     * 
     * 描述：获取州省信息
     * @author 李帅
     * @created 2018年10月31日 下午2:52:24
     * @since 
     * @param countryCode
     * @return
     */
    @Override
    public List<ProvinceInfoResp> getProvinceInfo(String countryCode) {
    	List<ProvinceInfoResp> provinces = RedisCacheUtil.listGetAll(provinceRedisKey + countryCode, ProvinceInfoResp.class);
    	if(provinces == null || provinces.size() == 0) {
    		provinces = regionMapper.getProvinceInfo(countryCode);
    		RedisCacheUtil.listSet(provinceRedisKey + countryCode, provinces);
    	}
    	return provinces;
    }
    
    /**
     * 
     * 描述：获取城市信息
     * @author 李帅
     * @created 2018年10月31日 下午2:52:28
     * @since 
     * @param countryCode
     * @param provinceCode
     * @return
     */
    @Override
    public List<CityInfoResp> getCityInfo(String countryCode, String provinceCode) {
    	List<CityInfoResp> citys = RedisCacheUtil.listGetAll(cityRedisKey + countryCode + ":" + provinceCode, CityInfoResp.class);
    	if(citys == null || citys.size() == 0) {
    		citys = regionMapper.getCityInfo(countryCode, provinceCode);
    		RedisCacheUtil.listSet(cityRedisKey + countryCode + ":" + provinceCode, citys);
    	}
    	return citys;
    }
}
