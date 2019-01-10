package com.iot.system.vo;
/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：州省信息
 * 功能描述：州省信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:40:00
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:40:00
 */
public class ProvinceInfoResp {

	/**
     * 国家代码
     */
    private String countryCode;

    /**
     * 州省代码
     */
    private String provinceCode;

    /**
     * 州省KEY
     */
    private String provinceKey;
    
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

	public String getProvinceKey() {
		return provinceKey;
	}

	public void setProvinceKey(String provinceKey) {
		this.provinceKey = provinceKey;
	}
	
}