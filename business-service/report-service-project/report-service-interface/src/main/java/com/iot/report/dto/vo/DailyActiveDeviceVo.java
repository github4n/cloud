package com.iot.report.dto.vo;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备日活统计
 * 功能描述：设备日活统计
 * 创建人： 李帅
 * 创建时间：2019年1月8日 下午1:46:46
 * 修改人：李帅
 * 修改时间：2019年1月8日 下午1:46:46
 */
public class DailyActiveDeviceVo{

	/**
	 * 日期
	 */
	private String dataDate;

	/**
	 * 设备类型
	 */
	private Long deviceType;
    
    /**
     * 总量
     */
    private Long totals;

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public Long getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Long deviceType) {
		this.deviceType = deviceType;
	}

	public Long getTotals() {
		return totals;
	}

	public void setTotals(Long totals) {
		this.totals = totals;
	}
    
}
