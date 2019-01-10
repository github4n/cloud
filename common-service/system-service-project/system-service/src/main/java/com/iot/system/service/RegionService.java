package com.iot.system.service;

import java.util.List;

import com.iot.system.vo.CityInfoResp;
import com.iot.system.vo.CountryInfoResp;
import com.iot.system.vo.ProvinceInfoResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：区域信息
 * 功能描述：区域信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:01:48
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:01:48
 */
public interface RegionService {

    /**
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午2:06:40
     * @since 
     * @return
     */
	List<CountryInfoResp> getCountryInfo();
	
	/**
	 * 
	 * 描述：获取州省信息
	 * @author 李帅
	 * @created 2018年10月31日 下午2:54:53
	 * @since 
	 * @param countryCode
	 * @return
	 */
	List<ProvinceInfoResp> getProvinceInfo(String countryCode);
	
	/**
	 * 
	 * 描述：获取城市信息
	 * @author 李帅
	 * @created 2018年10月31日 下午2:55:31
	 * @since 
	 * @param countryCode
	 * @param provinceCode
	 * @return
	 */
	List<CityInfoResp> getCityInfo(String countryCode, String provinceCode);

}
