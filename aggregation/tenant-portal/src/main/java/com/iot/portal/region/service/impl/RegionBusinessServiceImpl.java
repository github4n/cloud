package com.iot.portal.region.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.common.util.ValidateUtil;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.region.service.RegionBusinessService;
import com.iot.portal.region.vo.GetCityInfoReq;
import com.iot.system.api.RegionApi;
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
 * 创建时间：2018年10月31日 下午5:49:08
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午5:49:08
 */
@Service("regionBusiness")
public class RegionBusinessServiceImpl implements RegionBusinessService {

	/**
	 * 区域信息
	 */
	@Autowired
	private RegionApi regionApi;

	/**
	 * 
	 * 描述：获取国家信息
	 * 
	 * @author 李帅
	 * @created 2018年10月31日 下午5:47:57
	 * @since
	 * @return
	 */
	@Override
	public List<CountryInfoResp> getCountryInfo() {
		String lang = LocaleContextHolder.getLocale().toString();
		return this.regionApi.getCountryInfo(lang);
	}

	/**
	 * 
	 * 描述：获取州省信息
	 * 
	 * @author 李帅
	 * @created 2018年10月31日 下午5:51:40
	 * @since
	 * @param countryCode
	 * @return
	 */
	@Override
	public List<ProvinceInfoResp> getProvinceInfo(String countryCode) {
		String lang = LocaleContextHolder.getLocale().toString();
		return this.regionApi.getProvinceInfo(countryCode, lang);
	}

	/**
	 * 
	 * 描述：获取城市信息
	 * 
	 * @author 李帅
	 * @created 2018年10月31日 下午5:51:58
	 * @since
	 * @param countryCode
	 * @param provinceCode
	 * @return
	 */
	@Override
	public List<CityInfoResp> getCityInfo(GetCityInfoReq getCityInfoReq) {
		String lang = LocaleContextHolder.getLocale().toString();
		CityInfoReq cityInfoReq = new CityInfoReq();
		cityInfoReq.setCountryCode(getCityInfoReq.getCountryCode());
		cityInfoReq.setProvinceCode(getCityInfoReq.getProvinceCode());
		cityInfoReq.setLang(lang);
		return this.regionApi.getCityInfo(cityInfoReq);
	}
	
	/**
	 * 
	 * 描述：根据区号判断是否是正确的电话号码
	 * 
	 * @author 李帅
	 * @created 2018年12月20日 下午7:43:31
	 * @since
	 * @param phoneNumber
	 * @param areaCode
	 * @return
	 */
	@Override
	public boolean isPhoneNumberValid(String phoneNumber, String areaCode) {
		if(StringUtil.isEmpty(phoneNumber)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, phoneNumber + " is empty");
        }
		ValidateUtil.isNumeric(phoneNumber);
		if(StringUtil.isEmpty(areaCode)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, areaCode + " is empty");
        }
		if(!areaCode.startsWith("+")){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, areaCode + " must start With '+'");
        }
		ValidateUtil.isNumeric(areaCode.substring(1));
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber numberProto = phoneUtil.parse(areaCode + phoneNumber, areaCode);
			return phoneUtil.isValidNumber(numberProto);
		} catch (NumberParseException e) {
			return false;
		}
	}
}
