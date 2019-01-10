package com.iot.report.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iot.report.api.DeviceReportApi;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.DailyActivateDeviceResp;
import com.iot.report.dto.resp.DailyActivateDeviceTotalsResp;
import com.iot.report.dto.resp.DailyActiveDeviceResp;
import com.iot.report.dto.resp.DailyActiveDeviceTotalsResp;
import com.iot.report.entity.RegionDeviceActivatedNum;
import com.iot.report.entity.RegionDeviceActiveNum;
import com.iot.report.service.DeviceReportService;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备报表
 * 功能描述：设备报表
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:04:02
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:04:02
 */
@RestController
public class DeviceReportRestful implements DeviceReportApi{

    @Autowired
    private DeviceReportService deviceActiveService;

    /**
     * 
     * 描述：整理设备活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午8:04:38
     * @since
     */
    @Override
    public void arrangeDeviceData() {
        deviceActiveService.arrangeDeviceData();
    }

    /**
     * 
     * 描述：统计区域设备日活量
     * @author 李帅
     * @created 2019年1月8日 下午3:59:41
     * @since 
     * @param req
     * @return
     */
    @Override
    public List<RegionDeviceActiveNum> getRegionDeviceActiveNum(@RequestBody ActivateBaseReq req) {
        return deviceActiveService.getRegionDeviceActiveNum(req);
    }
    
    /**
     * 
     * 描述：统计区域设备激活量
     * @author 李帅
     * @created 2019年1月8日 下午3:59:31
     * @since 
     * @param req
     * @return
     */
    @Override
    public List<RegionDeviceActivatedNum> getRegionDeviceActivatedNum(@RequestBody ActivateBaseReq req) {
        return deviceActiveService.getRegionDeviceActivatedNum(req);
    }
    
    /**
     * 
     * 描述：设备日活统计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:51
     * @since 
     * @param req
     * @return
     */
    @Override
    public List<DailyActiveDeviceResp> getDailyActiveDevice(@RequestBody ActivateBaseReq req) {
        return deviceActiveService.getDailyActiveDevice(req);
    }
    
    /**
     * 
     * 描述：设备激活统计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:51
     * @since 
     * @param req
     * @return
     */
    @Override
    public List<DailyActivateDeviceResp> getDailyActivatedDevice(@RequestBody ActivateBaseReq req) {
        return deviceActiveService.getDailyActivatedDevice(req);
    }
    
    /**
     * 
     * 描述：设备日活总计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:51
     * @since 
     * @param req
     * @return
     */
    @Override
    public List<DailyActiveDeviceTotalsResp> getDailyActiveDeviceTotals(@RequestBody ActivateBaseReq req) {
        return deviceActiveService.getDailyActiveDeviceTotals(req);
    }
    
    /**
     * 
     * 描述：设备激活总计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:51
     * @since 
     * @param req
     * @return
     */
    @Override
    public List<DailyActivateDeviceTotalsResp> getDailyActivatedDeviceTotals(@RequestBody ActivateBaseReq req) {
        return deviceActiveService.getDailyActivatedDeviceTotals(req);
    }
}
