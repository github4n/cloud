package com.iot.report.dto.resp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备日活总计出参
 * 功能描述：设备日活总计出参
 * 创建人： 李帅
 * 创建时间：2019年1月8日 下午1:47:24
 * 修改人：李帅
 * 修改时间：2019年1月8日 下午1:47:24
 */
public class DailyActivateDeviceTotalsResp{

	/**
	 * 日期
	 */
	private String dataDate;

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

	public Long getTotals() {
		return totals;
	}

	public void setTotals(Long totals) {
		this.totals = totals;
	}
    
}
