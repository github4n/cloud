package com.iot.report.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.DailyActivateDeviceResp;
import com.iot.report.dto.resp.DailyActivateDeviceTotalsResp;
import com.iot.report.dto.resp.DailyActiveDeviceResp;
import com.iot.report.dto.resp.DailyActiveDeviceTotalsResp;
import com.iot.report.entity.RegionDeviceActivatedNum;
import com.iot.report.entity.RegionDeviceActiveNum;
import com.iot.report.fallback.DeviceReportApiFallbackFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备报表
 * 功能描述：设备报表
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:00:44
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:00:44
 */
@Api(tags = "设备报表Api")
@FeignClient(value = "report-service", fallbackFactory = DeviceReportApiFallbackFactory.class)
@RequestMapping("/deviceReport")
public interface DeviceReportApi {
	
    /**
     * 
     * 描述：整理设备活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午7:56:58
     * @since
     */
    @ApiOperation(value = "整理设备活跃信息", notes = "整理设备活跃信息")
    @RequestMapping(value = "/arrangeDeviceData", method = RequestMethod.GET)
    void arrangeDeviceData();
    
    /**
     * 
     * 描述：统计区域设备日活量
     * @author 李帅
     * @created 2019年1月8日 下午3:58:57
     * @since 
     * @param req
     * @return
     */
    @ApiOperation(value = "统计区域设备日活量", notes = "统计区域设备日活量")
    @RequestMapping(value = "/getRegionDeviceActiveNum", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<RegionDeviceActiveNum> getRegionDeviceActiveNum(@RequestBody ActivateBaseReq req);
    
    /**
     * 
     * 描述：统计区域设备激活量
     * @author 李帅
     * @created 2019年1月8日 下午3:58:48
     * @since 
     * @param req
     * @return
     */
    @ApiOperation(value = "统计区域设备激活量", notes = "统计区域设备激活量")
    @RequestMapping(value = "/getRegionDeviceActivatedNum", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<RegionDeviceActivatedNum> getRegionDeviceActivatedNum(@RequestBody ActivateBaseReq req);
    
    /**
     * 
     * 描述：设备日活统计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:27
     * @since 
     * @param req
     * @return
     */
    @ApiOperation(value = "设备日活统计", notes = "设备日活统计")
    @RequestMapping(value = "/getDailyActiveDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DailyActiveDeviceResp> getDailyActiveDevice(@RequestBody ActivateBaseReq req);
    
    /**
     * 
     * 描述：设备激活统计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:27
     * @since 
     * @param req
     * @return
     */
    @ApiOperation(value = "设备激活统计", notes = "设备激活统计")
    @RequestMapping(value = "/getDailyActivatedDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DailyActivateDeviceResp> getDailyActivatedDevice(@RequestBody ActivateBaseReq req);
    
    /**
     * 
     * 描述：设备日活总计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:27
     * @since 
     * @param req
     * @return
     */
    @ApiOperation(value = "设备日活总计", notes = "设备日活总计")
    @RequestMapping(value = "/getDailyActiveDeviceTotals", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DailyActiveDeviceTotalsResp> getDailyActiveDeviceTotals(@RequestBody ActivateBaseReq req);
    
    /**
     * 
     * 描述：设备激活总计
     * @author 李帅
     * @created 2019年1月8日 下午3:57:27
     * @since 
     * @param req
     * @return
     */
    @ApiOperation(value = "设备激活总计", notes = "设备激活总计")
    @RequestMapping(value = "/getDailyActivatedDeviceTotals", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DailyActivateDeviceTotalsResp> getDailyActivatedDeviceTotals(@RequestBody ActivateBaseReq req);
}
