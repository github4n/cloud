package com.iot.report.fallback;

import java.util.List;

import com.iot.report.api.DeviceReportApi;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.DailyActivateDeviceResp;
import com.iot.report.dto.resp.DailyActivateDeviceTotalsResp;
import com.iot.report.dto.resp.DailyActiveDeviceResp;
import com.iot.report.dto.resp.DailyActiveDeviceTotalsResp;
import com.iot.report.entity.RegionDeviceActivatedNum;
import com.iot.report.entity.RegionDeviceActiveNum;

import feign.hystrix.FallbackFactory;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备报表
 * 功能描述：设备报表
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:03:55
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:03:55
 */
public class DeviceReportApiFallbackFactory implements FallbackFactory<DeviceReportApi> {
	
	@Override
    public DeviceReportApi create(Throwable cause) {
        return new DeviceReportApi() {
        	/**
        	 * 
        	 * 描述：整理设备活跃信息
        	 * @author 李帅
        	 * @created 2019年1月3日 下午8:03:43
        	 * @since
        	 */
            @Override
            public void arrangeDeviceData() {
            	
            }
            
            /**
             * 
             * 描述：统计区域设备日活量
             * @author 李帅
             * @created 2019年1月8日 下午3:59:06
             * @since 
             * @param req
             * @return
             */
            @Override
            public List<RegionDeviceActiveNum> getRegionDeviceActiveNum(ActivateBaseReq req) {
            	return null;
            }
            
            /**
             * 
             * 描述：统计区域设备激活量
             * @author 李帅
             * @created 2019年1月8日 下午3:59:14
             * @since 
             * @param req
             * @return
             */
            @Override
            public List<RegionDeviceActivatedNum> getRegionDeviceActivatedNum(ActivateBaseReq req) {
            	return null;
            }

            /**
             * 
             * 描述：设备日活统计
             * @author 李帅
             * @created 2019年1月8日 下午7:20:59
             * @since 
             * @param req
             * @return
             */
            @Override
            public List<DailyActiveDeviceResp> getDailyActiveDevice(ActivateBaseReq req) {
                return null;
            }

            /**
             * 
             * 描述：设备激活统计
             * @author 李帅
             * @created 2019年1月8日 下午7:20:59
             * @since 
             * @param req
             * @return
             */
            @Override
            public List<DailyActivateDeviceResp> getDailyActivatedDevice(ActivateBaseReq req) {
                return null;
            }
            
            /**
             * 
             * 描述：设备日活总计
             * @author 李帅
             * @created 2019年1月8日 下午7:20:59
             * @since 
             * @param req
             * @return
             */
            @Override
            public List<DailyActiveDeviceTotalsResp> getDailyActiveDeviceTotals(ActivateBaseReq req) {
                return null;
            }

            /**
             * 
             * 描述：设备激活总计
             * @author 李帅
             * @created 2019年1月8日 下午7:20:59
             * @since 
             * @param req
             * @return
             */
            @Override
            public List<DailyActivateDeviceTotalsResp> getDailyActivatedDeviceTotals(ActivateBaseReq req) {
                return null;
            }
        };
    }
}
