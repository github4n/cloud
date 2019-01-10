package com.iot.report.dto.resp;

import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备日活统计出参
 * 功能描述：设备日活统计出参
 * 创建人： 李帅
 * 创建时间：2019年1月8日 下午1:47:24
 * 修改人：李帅
 * 修改时间：2019年1月8日 下午1:47:24
 */
public class DailyActiveDeviceResp{

	/**
	 * 日期
	 */
//	@JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
	private String dataDate;

	/**
	 * 设备类型分类统计
	 */
    private Map<Long, Long> data;
    
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

	public Map<Long, Long> getData() {
		return data;
	}

	public void setData(Map<Long, Long> data) {
		this.data = data;
	}

	public Long getTotals() {
		return totals;
	}

	public void setTotals(Long totals) {
		this.totals = totals;
	}
    
}
