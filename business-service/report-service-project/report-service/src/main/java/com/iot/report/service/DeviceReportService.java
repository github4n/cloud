package com.iot.report.service;

import java.util.List;

import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.DailyActivateDeviceResp;
import com.iot.report.dto.resp.DailyActivateDeviceTotalsResp;
import com.iot.report.dto.resp.DailyActiveDeviceResp;
import com.iot.report.dto.resp.DailyActiveDeviceTotalsResp;
import com.iot.report.entity.RegionDeviceActivatedNum;
import com.iot.report.entity.RegionDeviceActiveNum;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备报表
 * 功能描述：设备报表
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:04:15
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:04:15
 */
public interface DeviceReportService {
	
    /**
     * 
     * 描述：整理设备活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午8:04:45
     * @since
     */
	void arrangeDeviceData();

	/**
	 * 
	 * 描述：统计区域设备日活量
	 * @author 李帅
	 * @created 2019年1月8日 下午3:59:49
	 * @since 
	 * @param req
	 * @return
	 */
	List<RegionDeviceActiveNum> getRegionDeviceActiveNum(ActivateBaseReq req);
	
	/**
	 * 
	 * 描述：统计区域设备激活量
	 * @author 李帅
	 * @created 2019年1月8日 下午3:59:56
	 * @since 
	 * @param req
	 * @return
	 */
	List<RegionDeviceActivatedNum> getRegionDeviceActivatedNum(ActivateBaseReq req);
	
	/**
	 * 
	 * 描述：设备日活统计
	 * @author 李帅
	 * @created 2019年1月8日 下午3:57:58
	 * @since 
	 * @param req
	 * @return
	 */
	List<DailyActiveDeviceResp> getDailyActiveDevice(ActivateBaseReq req);
	
	/**
	 * 
	 * 描述：设备激活统计
	 * @author 李帅
	 * @created 2019年1月8日 下午3:57:58
	 * @since 
	 * @param req
	 * @return
	 */
	List<DailyActivateDeviceResp> getDailyActivatedDevice(ActivateBaseReq req);
	
	/**
	 * 
	 * 描述：设备日活总计
	 * @author 李帅
	 * @created 2019年1月8日 下午3:57:58
	 * @since 
	 * @param req
	 * @return
	 */
	List<DailyActiveDeviceTotalsResp> getDailyActiveDeviceTotals(ActivateBaseReq req);
	
	/**
	 * 
	 * 描述：设备激活总计
	 * @author 李帅
	 * @created 2019年1月8日 下午3:57:58
	 * @since 
	 * @param req
	 * @return
	 */
	List<DailyActivateDeviceTotalsResp> getDailyActivatedDeviceTotals(ActivateBaseReq req);
}
