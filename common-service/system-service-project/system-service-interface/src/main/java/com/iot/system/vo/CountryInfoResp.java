package com.iot.system.vo;
/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：国家信息
 * 功能描述：国家信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:16:02
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:16:02
 */
public class CountryInfoResp {

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 国家KEY
     */
    private String countryKey;
    
    /**
     * 手机区号
     */
    private String areaCode;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryKey() {
		return countryKey;
	}

	public void setCountryKey(String countryKey) {
		this.countryKey = countryKey;
	}


}