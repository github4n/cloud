package com.iot.report.api;

import com.iot.common.helper.Page;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.req.DevDistributionReq;
import com.iot.report.dto.req.DevPageReq;
import com.iot.report.dto.resp.DevActivateResp;
import com.iot.report.dto.resp.DevDistributionResp;
import com.iot.report.dto.resp.DevPageResp;
import com.iot.report.dto.resp.DistributionResp;
import com.iot.report.entity.DeviceActivatedInfo;
import com.iot.report.fallback.UserActiveApiFallbackFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
* @Description:    设备激活报表接口
* @Author:         nongchongwei
* @CreateDate:     2019/1/4 10:47
* @UpdateUser:     nongchongwei
* @UpdateDate:     2019/1/4 10:47
* @UpdateRemark:
* @Version:        1.0
*/
@Api(tags = "设备激活Api")
@FeignClient(value = "report-service", fallbackFactory = UserActiveApiFallbackFactory.class)
@RequestMapping("/deviceActivateService")
public interface DeviceActivateApi {
    /**
     * 描述: 根据设备uuid获取激活信息,激活明细表uuid唯一
     * @author  nongchongwei
     * @param uuid
     * @return  com.iot.report.entity.DeviceActiveInfo
     * @exception
     * @date     2019/1/4 11:02
     */
    @ApiOperation(value = "根据设备uuid获取激活信息", notes = "根据设备uuid获取激活信息")
    @RequestMapping(value = "/getDeviceActiveInfoByUuid", method = RequestMethod.GET)
    DeviceActivatedInfo getDeviceActiveInfoByUuid(@RequestParam("uuid") String uuid);

    /**
     * 描述: 保存设备激活信息
     * @author  nongchongwei
     * @return  int
     * @exception
     * @date     2019/1/4 11:07
     */
    @ApiOperation(value = "保存设备激活信息", notes = "保存设备激活信息")
    @RequestMapping(value = "/saveDeviceActiveInfo", method = RequestMethod.POST)
    void saveDeviceActiveInfo();

    /**
     * 描述:缓存设备地区信息
     * @author  nongchongwei
     * @param uuid
    * @param ip
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @ApiOperation(value = "缓存设备地区信息", notes = "缓存设备地区信息")
    @RequestMapping(value = "/cacheDevDistribution", method = RequestMethod.POST)
    void cacheDevDistribution(@RequestParam("uuid") String uuid, @RequestParam("ip") String ip);

    /**
     * 描述: 缓存ip信息
     * @author  nongchongwei
     * @param uuid
    * @param ip
     * @return  void
     * @exception
     * @date     2019/1/5 16:48
     */
    @ApiOperation(value = "缓存设备Ip", notes = "缓存设备地区信息")
    @RequestMapping(value = "/cacheDevIp", method = RequestMethod.POST)
    void cacheDevIp(@RequestParam("uuid") String uuid, @RequestParam("ip") String ip);

    /**
     * 描述:同步在线设备
     * @author  nongchongwei
     * @param
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @ApiOperation(value = "同步在线设备", notes = "同步在线设备")
    @RequestMapping(value = "/syncOnlineDev", method = RequestMethod.POST)
    void syncOnlineDev();

    /**
     * 描述:同步在线用户
     * @author  nongchongwei
     * @param
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @ApiOperation(value = "同步在线用户", notes = "同步在线用户")
    @RequestMapping(value = "/syncOnlineUser", method = RequestMethod.POST)
    void syncOnlineUser();

    /**
     * 描述:查询地区激活统计信息
     * @author  nongchongwei
     * @param activateBaseReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @ApiOperation(value = "查询地区激活统计信息" ,  notes="查询地区激活统计信息")
    @RequestMapping(value="/getActivateDistributionData",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    List<DevDistributionResp> getActivateDistributionData(@RequestBody ActivateBaseReq activateBaseReq);
    /**
     * 描述:查询地区活跃统计信息
     * @author  nongchongwei
     * @param activateBaseReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @ApiOperation(value = "查询地区活跃统计信息" ,  notes="查询地区活跃统计信息")
    @RequestMapping(value="/getActiveDistributionData",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    List<DevDistributionResp> getActiveDistributionData(@RequestBody ActivateBaseReq activateBaseReq);

    /**
     * 描述:激活设备分页查询
     * @author  nongchongwei
     * @param devPageReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @ApiOperation(value = "激活设备分页查询" ,  notes="激活设备分页查询")
    @RequestMapping(value="/getPageDeviceActivate",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    Page<DevPageResp> getPageDeviceActivate(@RequestBody DevPageReq devPageReq);

    /**
     * 描述:活跃设备分页查询
     * @author  nongchongwei
     * @param devPageReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @ApiOperation(value = "活跃设备分页查询" ,  notes="活跃设备分页查询")
    @RequestMapping(value="/getPageDeviceActive",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    Page<DevPageResp> getPageDeviceActive(@RequestBody DevPageReq devPageReq);


    @ApiOperation(value = "获取设备活跃页面数据" ,  notes="获取设备活跃页面数据")
    @RequestMapping(value="/getDeviceActive", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    DevActivateResp getDeviceActive(@RequestBody ActivateBaseReq req);

    @ApiOperation(value = "获取设备激活页面数据" ,  notes="获取设备激活页面数据")
    @RequestMapping(value="/getDeviceActivated", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    DevActivateResp getDeviceActivated(@RequestBody ActivateBaseReq req);

    @ApiOperation(value = "获取设备激活/活跃数据" ,  notes="获取设备激活/活跃数据")
    @RequestMapping(value="/getDeviceActiveAndActivated", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    DevActivateResp getDeviceActiveAndActivated(@RequestBody ActivateBaseReq req);
    /**
     *@description 获取设备激活总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/9 10:43
     *@return java.lang.Long
     */
    @ApiOperation(value = "获取设备激活总数" ,  notes="获取设备激活总数")
    @RequestMapping(value="/getDeviceActivatedCount", method=RequestMethod.GET)
    Long getDeviceActivatedCount(@RequestParam("tenantId") Long tenantId);
}
