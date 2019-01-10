package com.iot.portal.region.vo;
/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：城市信息
 * 功能描述：城市信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:42:33
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:42:33
 */
public class GetCityInfoReq {

	/**
     * 国家代码
     */
    private String countryCode;

    /**
     * 州省代码
     */
    private String provinceCode;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
}