package com.iot.report.entity;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备活跃明细
 * 功能描述：设备活跃明细
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:05:15
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:05:15
 */
public class RegionDeviceActiveNum {
	
	/**
	 * 国家
	 */
	private String countryCode;
	
	private Long counts;
	
	public Long getCounts() {
		return counts;
	}
	public void setCounts(Long counts) {
		this.counts = counts;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
