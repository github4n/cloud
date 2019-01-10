package com.iot.portal.region.service;

import java.util.List;

import com.iot.portal.region.vo.GetCityInfoReq;
import com.iot.system.vo.CityInfoResp;
import com.iot.system.vo.CountryInfoResp;
import com.iot.system.vo.ProvinceInfoResp;


/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：区域信息
 * 功能描述：区域信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午5:49:01
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午5:49:01
 */
public interface RegionBusinessService {

    /**
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午5:48:04
     * @since 
     * @return
     */
	List<CountryInfoResp> getCountryInfo();

	/**
	 * 
	 * 描述：获取州省信息
	 * @author 李帅
	 * @created 2018年10月31日 下午5:51:47
	 * @since 
	 * @param countryCode
	 * @return
	 */
	List<ProvinceInfoResp> getProvinceInfo(String countryCode);
	
	/**
	 * 
	 * 描述：获取城市信息
	 * @author 李帅
	 * @created 2018年10月31日 下午5:52:05
	 * @since 
	 * @param countryCode
	 * @param provinceCode
	 * @return
	 */
	List<CityInfoResp> getCityInfo(GetCityInfoReq getCityInfoReq);
	
	/**
	 * 
	 * 描述：根据区号判断是否是正确的电话号码
	 * @author 李帅
	 * @created 2018年12月20日 下午7:43:24
	 * @since 
	 * @param phoneNumber
	 * @param areaCode
	 * @return
	 */
	boolean isPhoneNumberValid(String phoneNumber, String areaCode);
}
