package com.iot.report.restful;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.CommonUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.report.api.DeviceActivateApi;
import com.iot.report.contants.ReportConstants;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.req.DevActivateOrActiveReq;
import com.iot.report.dto.req.DevDistributionReq;
import com.iot.report.dto.resp.*;
import com.iot.report.dto.req.DevPageReq;
import com.iot.report.entity.DeviceActivatedInfo;
import com.iot.report.enums.QueryTypeEnums;
import com.iot.report.exception.ReportExceptionEnum;
import com.iot.report.service.DeviceActivateService;
import com.iot.report.utils.RedisKeyUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @Description:    立达信IOT云平台 设备报表
* @Author:         nongchongwei
* @CreateDate:     2019/1/4 15:12
* @UpdateUser:     nongchongwei
* @UpdateDate:     2019/1/4 15:12
* @UpdateRemark:
* @Version:        1.0
*/
@RestController
public class DeviceActivateRestful implements DeviceActivateApi{

    private static final Logger loger = LoggerFactory.getLogger(DeviceActivateRestful.class);

    @Autowired
    private DeviceActivateService deviceActivateService;

    /**
     * 地区库本地路径
     */
    @Value("${distribution.path}")
    private String distributionPath;

    private static File distributionInfo;

    /**
     * 描述: 查询激活信息
     * @author  nongchongwei
     * @param uuid
     * @return  com.iot.report.entity.DeviceActiveInfo
     * @exception
     * @date     2019/1/5 16:49
     */
    @Override
    public DeviceActivatedInfo getDeviceActiveInfoByUuid(@RequestParam("uuid") String uuid) {
        return deviceActivateService.getDeviceActiveInfoByUuid(uuid);
    }

    /**
     * 描述: 定时保存设备激活信息
     * @author  nongchongwei
     * @param
     * @return  void
     * @exception
     * @date     2019/1/5 16:48
     */
    @Override
    public void saveDeviceActiveInfo() {
        deviceActivateService.saveDeviceActiveInfo();
    }
    /**
     * 描述: 缓存地区信息
     * @author  nongchongwei
     * @param uuid
    * @param ip
     * @return  void
     * @exception
     * @date     2019/1/5 16:48
     */
    @Override
    public void cacheDevDistribution(@RequestParam("uuid") String uuid, @RequestParam("ip") String ip) {
        try{
            // 创建 GeoLite2 数据库
            File database = getDatabaseFile();
            // 读取数据库内容
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            // 获取查询结果
            CityResponse response = reader.city(ipAddress);
            DeviceActivateCacheInfo deviceActivateCacheInfo = new DeviceActivateCacheInfo();
            // 获取国家信息
            Country country = response.getCountry();
            deviceActivateCacheInfo.setCountryCode(country.getIsoCode());
            // 获取省份
            Subdivision subdivision = response.getMostSpecificSubdivision();
            deviceActivateCacheInfo.setProvince(subdivision.getName());
            // 获取城市
            City city = response.getCity();
            deviceActivateCacheInfo.setCity(city.getName());
            String key =  RedisKeyUtils.getDistributionKey(uuid);
            RedisCacheUtil.valueObjSet(key,deviceActivateCacheInfo);
        }catch (Exception e){
            loger.error("",e);
        }
    }

    /**
     * 描述: 缓存ip信息
     * @author  nongchongwei
     * @param uuid
    * @param ip
     * @return  void
     * @exception
     * @date     2019/1/5 16:48
     */
    @Override
    public void cacheDevIp(@RequestParam("uuid") String uuid, @RequestParam("ip") String ip) {
        String key =  RedisKeyUtils.getDevIpKey(uuid);
        RedisCacheUtil.setAdd(key,ip);
    }

    @Override
    public void syncOnlineDev() {
        Set<String> deviceIdSet = RedisCacheUtil.setGetAll(ReportConstants.DEVONLINE, String.class, false);
        RedisCacheUtil.setAdd(ReportConstants.DEVACTIVE+ CalendarUtil.getNowDate(),deviceIdSet);
    }

    @Override
    public void syncOnlineUser() {
        Set<String> userIdSet = RedisCacheUtil.setGetAll(ReportConstants.USERONLINE, String.class, false);
        RedisCacheUtil.setAdd(ReportConstants.USERACTIVE+ CalendarUtil.getNowDate(),userIdSet);
    }

    /**
     * 描述:查询地区激活统计信息
     * @author  nongchongwei
     * @param activateBaseReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @Override
    public List<DevDistributionResp> getActivateDistributionData(@RequestBody ActivateBaseReq activateBaseReq) {
        if (CommonUtil.isEmpty(activateBaseReq.getTenantId())) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        if (CommonUtil.isEmpty(activateBaseReq.getBeginDate())) {
            throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
        }
        if (CommonUtil.isEmpty(activateBaseReq.getEndDate())) {
            throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
        }
        DevDistributionReq devDistributionReq = new DevDistributionReq();
        BeanUtils.copyProperties(activateBaseReq, devDistributionReq);
        devDistributionReq.setActiveOrActivate(QueryTypeEnums.ACTIVATE.getCode());
        return deviceActivateService.getDistributionAmount(devDistributionReq);
    }
    /**
     * 描述:查询地区活跃统计信息
     * @author  nongchongwei
     * @param activateBaseReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @Override
    public List<DevDistributionResp> getActiveDistributionData(@RequestBody ActivateBaseReq activateBaseReq) {
        if (CommonUtil.isEmpty(activateBaseReq.getTenantId())) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        if (CommonUtil.isEmpty(activateBaseReq.getBeginDate())) {
            throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
        }
        if (CommonUtil.isEmpty(activateBaseReq.getEndDate())) {
            throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
        }
        DevDistributionReq devDistributionReq = new DevDistributionReq();
        BeanUtils.copyProperties(activateBaseReq, devDistributionReq);
        devDistributionReq.setActiveOrActivate(QueryTypeEnums.ACTIVE.getCode());
        return deviceActivateService.getDistributionAmount(devDistributionReq);
    }

    /**
     * 描述:激活设备分页查询
     * @author  nongchongwei
     * @param devPageReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @Override
    public Page<DevPageResp> getPageDeviceActivate(@RequestBody DevPageReq devPageReq) {
        if(CommonUtil.isEmpty(devPageReq.getTenantId())){
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL);
        }
        DevActivateOrActiveReq devActivateReq = new DevActivateOrActiveReq();
        BeanUtils.copyProperties(devPageReq, devActivateReq);
        devActivateReq.setQueryType(QueryTypeEnums.ACTIVATE.getCode());
        return deviceActivateService.getPageDeviceActivateOrActive(devActivateReq);
    }
    /**
     * 描述:活跃设备分页查询
     * @author  nongchongwei
     * @param devPageReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    @Override
    public Page<DevPageResp> getPageDeviceActive(@RequestBody DevPageReq devPageReq) {
        if(CommonUtil.isEmpty(devPageReq.getTenantId())){
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL);
        }
        DevActivateOrActiveReq devActivateReq = new DevActivateOrActiveReq();
        BeanUtils.copyProperties(devPageReq, devActivateReq);
        devActivateReq.setQueryType(QueryTypeEnums.ACTIVE.getCode());
        return deviceActivateService.getPageDeviceActivateOrActive(devActivateReq);
    }

    /** 
     * 描述：获取设备活跃页面数据
     * @author maochengyuan
     * @created 2019/1/8 11:42
     * @param req 参数
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    @Override
    public DevActivateResp getDeviceActive(@RequestBody ActivateBaseReq req) {
        return this.deviceActivateService.getDeviceActive(req);
    }

    /** 
     * 描述：获取设备激活页面数据
     * @author maochengyuan
     * @created 2019/1/8 11:42
     * @param req 参数
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    @Override
    public DevActivateResp getDeviceActivated(@RequestBody ActivateBaseReq req) {
        return this.deviceActivateService.getDeviceActivated(req);
    }

    /** 
     * 描述：获取设备激活/活跃数据
     * @author maochengyuan
     * @created 2019/1/8 11:42
     * @param req 参数
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    @Override
    public DevActivateResp getDeviceActiveAndActivated(@RequestBody ActivateBaseReq req) {
        return this.deviceActivateService.getDeviceActiveAndActivated(req);
    }
    
    /**
     *@description 获取设备激活总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/9 10:43
     *@return java.lang.Long
     */
    @Override
    public Long getDeviceActivatedCount(Long tenantId) {
        return deviceActivateService.getDeviceActivatedCount(tenantId);
    }


    public File getDatabaseFile() {
        if(null == distributionInfo){
            distributionInfo =  new File(distributionPath);
        }
        return distributionInfo;
    }
}
