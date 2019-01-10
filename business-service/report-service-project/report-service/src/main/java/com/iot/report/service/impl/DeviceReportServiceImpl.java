package com.iot.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.CommonUtil;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.report.contants.ReportConstants;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.DailyActivateDeviceResp;
import com.iot.report.dto.resp.DailyActivateDeviceTotalsResp;
import com.iot.report.dto.resp.DailyActiveDeviceResp;
import com.iot.report.dto.resp.DailyActiveDeviceTotalsResp;
import com.iot.report.dto.vo.DailyActiveDeviceVo;
import com.iot.report.entity.DeviceActivatedInfo;
import com.iot.report.entity.DeviceActiveInfo;
import com.iot.report.entity.RegionDeviceActivatedNum;
import com.iot.report.entity.RegionDeviceActiveNum;
import com.iot.report.exception.ReportExceptionEnum;
import com.iot.report.service.DeviceReportService;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：设备报表
 * 功能描述：设备报表
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:04:23
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:04:23
 */
@Service
public class DeviceReportServiceImpl implements DeviceReportService{

	@Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private DeviceCoreApi deviceCoreApi;
    /**
     * 
     * 描述：整理设备活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午8:04:51
     * @since
     */
    @Override
    public void arrangeDeviceData() {
//    	Set<String> value = new HashSet<String>();
//    	value.add("070a90742d2f26360ccdd124aef54650");
//    	RedisCacheUtil.setAdd(ReportConstants.DEVACTIVE + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD), value, false);
    	Set<String> deviceUUIDList = RedisCacheUtil.setGetAll(ReportConstants.DEVACTIVE + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD), String.class, false);
    	if(deviceUUIDList == null || deviceUUIDList.size() == 0){
    		throw new BusinessException(ReportExceptionEnum.USER_ACTIVE_CACHE_IS_NOT_EXIST);
    	}
    	DeviceActiveInfo deviceActiveInfo = null;
    	for(String uuid : deviceUUIDList){
    		deviceActiveInfo = new DeviceActiveInfo();
    		org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(Criteria.where("uuid").is(uuid));
    		DeviceActivatedInfo activeInfo = this.mongoTemplate.findOne(query, DeviceActivatedInfo.class, "device_activated_info");
    	  	if(activeInfo != null){
    	  		deviceActiveInfo.setActiveDate(new Date());
    	  		deviceActiveInfo.setCity(activeInfo.getCity());
    	  		deviceActiveInfo.setCountryCode(activeInfo.getCountryCode());
    	  		deviceActiveInfo.setDeviceType(activeInfo.getDeviceType());
    	  		deviceActiveInfo.setProvince(activeInfo.getProvince());
    	  		deviceActiveInfo.setTenantId(activeInfo.getTenantId());
    	  		deviceActiveInfo.setUuid(uuid);
    	  		
    	  	}else{
    	  		GetDeviceTypeByDeviceRespVo respVo = deviceCoreApi.getDeviceTypeByDeviceId(uuid);
    	  		if(respVo != null){
    	  			if(respVo.getDeviceTypeInfo() != null){
    	  				deviceActiveInfo.setDeviceType(respVo.getDeviceTypeInfo().getDeviceCatalogId());
    	  			}
    	  			if(respVo.getDeviceInfo() != null){
    	  				deviceActiveInfo.setTenantId(respVo.getDeviceInfo().getTenantId());
    	  			}
    	  		}
    	  		deviceActiveInfo.setActiveDate(new Date());
    	  		deviceActiveInfo.setUuid(uuid);
    	  	}
    	  	this.mongoTemplate.insert(deviceActiveInfo);
    		RedisCacheUtil.setRemove(ReportConstants.DEVACTIVE + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD), uuid, false);
    	}
    }
    
    /**
     * 
     * 描述：统计区域设备日活量
     * @author 李帅
     * @created 2019年1月8日 下午4:00:16
     * @since 
     * @param req
     * @return
     */
    @Override
	public List<RegionDeviceActiveNum> getRegionDeviceActiveNum(ActivateBaseReq req) {

		if (req.getTenantId() == null) {
			throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
		}
		if (req.getBeginDate() == null) {
			throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
		}
		if (req.getEndDate() == null) {
			throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
		}
		// 组合查询条件
		TypedAggregation agg = Aggregation.newAggregation(
				DeviceActiveInfo.class,
				Aggregation.match(
						Criteria.where("tenantId").is(req.getTenantId())
						.and("activeDate").lte(req.getEndDate()).gte(req.getBeginDate())
						),
				Aggregation.project("countryCode"), Aggregation.group("countryCode").count().as("counts"),
				Aggregation.project("countryCode", "counts").and("countryCode").previousOperation()
				);
		// 获取查询结果
        AggregationResults<RegionDeviceActiveNum> activeDatas = mongoTemplate.aggregate(agg, DeviceActiveInfo.class, RegionDeviceActiveNum.class);
        List<RegionDeviceActiveNum> result = activeDatas.getMappedResults();
		return result;
	}
    
    /**
     * 
     * 描述：统计区域设备激活量
     * @author 李帅
     * @created 2019年1月8日 下午4:00:05
     * @since 
     * @param req
     * @return
     */
    @Override
	public List<RegionDeviceActivatedNum> getRegionDeviceActivatedNum(ActivateBaseReq req) {

		if (req.getTenantId() == null) {
			throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
		}
		if (req.getBeginDate() == null) {
			throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
		}
		if (req.getEndDate() == null) {
			throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
		}
		// 组合查询条件
		TypedAggregation agg = Aggregation.newAggregation(
				DeviceActivatedInfo.class,
				Aggregation.match(
						Criteria.where("tenantId").is(req.getTenantId())
						.and("activatedDate").lte(req.getEndDate()).gte(req.getBeginDate())
						),
				Aggregation.project("countryCode"), Aggregation.group("countryCode").count().as("counts"),
				Aggregation.project("countryCode", "counts").and("countryCode").previousOperation()
				);
		// 获取查询结果
        AggregationResults<RegionDeviceActivatedNum> activeDatas = mongoTemplate.aggregate(agg, DeviceActivatedInfo.class, RegionDeviceActivatedNum.class);
        List<RegionDeviceActivatedNum> result = activeDatas.getMappedResults();
		return result;
	}
    
    /**
     * 
     * 描述：设备日活统计
     * @author 李帅
     * @created 2019年1月8日 下午3:58:07
     * @since 
     * @param req
     * @return
     */
    @Override
	public List<DailyActiveDeviceResp> getDailyActiveDevice(ActivateBaseReq req) {
		if (CommonUtil.isEmpty(req.getTenantId())) {
			throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
		}
		if (CommonUtil.isEmpty(req.getBeginDate())) {
			throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
		}
		if (CommonUtil.isEmpty(req.getEndDate())) {
			throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
		}
		// 组合查询条件
		TypedAggregation agg = Aggregation.newAggregation(
				DeviceActiveInfo.class,
				Aggregation.match(
						Criteria.where("tenantId").is(req.getTenantId())
						.and("activeDate").lte(req.getEndDate()).gte(req.getBeginDate())
						),
				Aggregation.project("deviceType").and(DateOperators.DateToString.dateOf("activeDate").toString("%Y-%m-%d")).as("dataDate"),
				Aggregation.group("dataDate", "deviceType").count().as("totals"),
				Aggregation.project("dataDate","deviceType", "totals"),
				Aggregation.sort(Sort.Direction.DESC, "dataDate")
				);
		// 获取查询结果
		AggregationResults<DailyActiveDeviceVo> activeDatas = mongoTemplate.aggregate(agg, DeviceActiveInfo.class, DailyActiveDeviceVo.class);
		List<DailyActiveDeviceVo> result = activeDatas.getMappedResults();
		List<DailyActiveDeviceResp> dailyActiveDeviceList = null;
		Map<String, DailyActiveDeviceResp> respMap = null;
		if (result != null && result.size() > 0) {
			dailyActiveDeviceList = new ArrayList<DailyActiveDeviceResp>();
			respMap = new HashMap<String, DailyActiveDeviceResp>();
			DailyActiveDeviceResp resp = null;
			Map<Long, Long> data = null;
			for (DailyActiveDeviceVo dailyActiveDeviceVo : result) {
				if (respMap.get(dailyActiveDeviceVo.getDataDate()) == null) {
					resp = new DailyActiveDeviceResp();
					resp.setDataDate(dailyActiveDeviceVo.getDataDate());
					data = new HashMap<Long, Long>();
					if(dailyActiveDeviceVo.getDeviceType() == null){
						dailyActiveDeviceVo.setDeviceType(0L);
					}
					data.put(dailyActiveDeviceVo.getDeviceType(), dailyActiveDeviceVo.getTotals());
					resp.setData(data);
					resp.setTotals(dailyActiveDeviceVo.getTotals());
					respMap.put(dailyActiveDeviceVo.getDataDate(), resp);
				} else {
					resp = respMap.get(dailyActiveDeviceVo.getDataDate());
					data = resp.getData();
					if(dailyActiveDeviceVo.getDeviceType() == null){
						dailyActiveDeviceVo.setDeviceType(0L);
					}
					data.put(dailyActiveDeviceVo.getDeviceType(), dailyActiveDeviceVo.getTotals());
					resp.setData(data);
					resp.setTotals(resp.getTotals() + dailyActiveDeviceVo.getTotals());
				}
			}
			Iterator<Map.Entry<String, DailyActiveDeviceResp>> entries = respMap.entrySet().iterator();
			Map.Entry<String, DailyActiveDeviceResp> entry = null;
			while (entries.hasNext()) {
				entry = entries.next();
				dailyActiveDeviceList.add(entry.getValue());
			}
		}
		return dailyActiveDeviceList;
	}
    
    /**
     * 
     * 描述：设备激活统计
     * @author 李帅
     * @created 2019年1月8日 下午3:58:07
     * @since 
     * @param req
     * @return
     */
    @Override
	public List<DailyActivateDeviceResp> getDailyActivatedDevice(ActivateBaseReq req) {
		if (CommonUtil.isEmpty(req.getTenantId())) {
			throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
		}
		if (CommonUtil.isEmpty(req.getBeginDate())) {
			throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
		}
		if (CommonUtil.isEmpty(req.getEndDate())) {
			throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
		}
		// 组合查询条件
		TypedAggregation agg = Aggregation.newAggregation(
				DeviceActivatedInfo.class,
				Aggregation.match(
						Criteria.where("tenantId").is(req.getTenantId())
						.and("activateDate").lte(req.getEndDate()).gte(req.getBeginDate())
						),
				Aggregation.project("deviceType").and(DateOperators.DateToString.dateOf("activateDate").toString("%Y-%m-%d")).as("dataDate"),
				Aggregation.group("dataDate", "deviceType").count().as("totals"),
				Aggregation.project("dataDate","deviceType", "totals"),
				Aggregation.sort(Sort.Direction.DESC, "dataDate")
				);
		// 获取查询结果
		AggregationResults<DailyActiveDeviceVo> activeDatas = mongoTemplate.aggregate(agg, DeviceActivatedInfo.class, DailyActiveDeviceVo.class);
		List<DailyActiveDeviceVo> result = activeDatas.getMappedResults();
//		System.out.println(JsonUtil.toJson(result));
		List<DailyActivateDeviceResp> dailyActiveDeviceList = null;
		Map<String, DailyActivateDeviceResp> respMap = null;
		if (result != null && result.size() > 0) {
			dailyActiveDeviceList = new ArrayList<DailyActivateDeviceResp>();
			respMap = new HashMap<String, DailyActivateDeviceResp>();
			DailyActivateDeviceResp resp = null;
			Map<Long, Long> data = null;
			for (DailyActiveDeviceVo dailyActiveDeviceVo : result) {
				if (respMap.get(dailyActiveDeviceVo.getDataDate()) == null) {
					resp = new DailyActivateDeviceResp();
					resp.setDataDate(dailyActiveDeviceVo.getDataDate());
					data = new HashMap<Long, Long>();
					if(dailyActiveDeviceVo.getDeviceType() == null){
						dailyActiveDeviceVo.setDeviceType(0L);
					}
					data.put(dailyActiveDeviceVo.getDeviceType(), dailyActiveDeviceVo.getTotals());
					resp.setData(data);
					resp.setTotals(dailyActiveDeviceVo.getTotals());
					respMap.put(dailyActiveDeviceVo.getDataDate(), resp);
				} else {
					resp = respMap.get(dailyActiveDeviceVo.getDataDate());
					data = resp.getData();
					if(dailyActiveDeviceVo.getDeviceType() == null){
						dailyActiveDeviceVo.setDeviceType(0L);
					}
					data.put(dailyActiveDeviceVo.getDeviceType(), dailyActiveDeviceVo.getTotals());
					resp.setData(data);
					resp.setTotals(resp.getTotals() + dailyActiveDeviceVo.getTotals());
				}
			}
			Iterator<Map.Entry<String, DailyActivateDeviceResp>> entries = respMap.entrySet().iterator();
			Map.Entry<String, DailyActivateDeviceResp> entry = null;
			while (entries.hasNext()) {
				entry = entries.next();
				dailyActiveDeviceList.add(entry.getValue());
			}
		}
		return dailyActiveDeviceList;
	}
    
    /**
     * 
     * 描述：设备日活总计
     * @author 李帅
     * @created 2019年1月8日 下午3:58:07
     * @since 
     * @param req
     * @return
     */
    @Override
	public List<DailyActiveDeviceTotalsResp> getDailyActiveDeviceTotals(ActivateBaseReq req) {
		if (CommonUtil.isEmpty(req.getTenantId())) {
			throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
		}
		if (CommonUtil.isEmpty(req.getBeginDate())) {
			throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
		}
		if (CommonUtil.isEmpty(req.getEndDate())) {
			throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
		}
		// 组合查询条件
		TypedAggregation agg = Aggregation.newAggregation(
				DeviceActiveInfo.class,
				Aggregation.match(
						Criteria.where("tenantId").is(req.getTenantId())
						.and("activeDate").lte(req.getEndDate()).gte(req.getBeginDate())
						),
				Aggregation.project().and(DateOperators.DateToString.dateOf("activeDate").toString("%Y-%m-%d")).as("dataDate"),
				Aggregation.group("dataDate").count().as("totals"),
				Aggregation.project("dataDate", "totals").and("dataDate").previousOperation(),
				Aggregation.sort(Sort.Direction.DESC, "dataDate")
				);
		// 获取查询结果
		AggregationResults<DailyActiveDeviceTotalsResp> activeDatas = mongoTemplate.aggregate(agg, DeviceActiveInfo.class, DailyActiveDeviceTotalsResp.class);
		List<DailyActiveDeviceTotalsResp> result = activeDatas.getMappedResults();
		return result;
	}
    
    /**
     * 
     * 描述：设备激活总计
     * @author 李帅
     * @created 2019年1月8日 下午3:58:07
     * @since 
     * @param req
     * @return
     */
    @Override
	public List<DailyActivateDeviceTotalsResp> getDailyActivatedDeviceTotals(ActivateBaseReq req) {
		if (CommonUtil.isEmpty(req.getTenantId())) {
			throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
		}
		if (CommonUtil.isEmpty(req.getBeginDate())) {
			throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
		}
		if (CommonUtil.isEmpty(req.getEndDate())) {
			throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
		}
		// 组合查询条件
		TypedAggregation agg = Aggregation.newAggregation(
				DeviceActivatedInfo.class,
				Aggregation.match(
						Criteria.where("tenantId").is(req.getTenantId())
						.and("activateDate").lte(req.getEndDate()).gte(req.getBeginDate())
						),
				Aggregation.project().and(DateOperators.DateToString.dateOf("activateDate").toString("%Y-%m-%d")).as("dataDate"),
				Aggregation.group("dataDate").count().as("totals"),
				Aggregation.project("dataDate", "totals").and("dataDate").previousOperation(),
				Aggregation.sort(Sort.Direction.DESC, "dataDate")
				);
		// 获取查询结果
		AggregationResults<DailyActivateDeviceTotalsResp> activeDatas = mongoTemplate.aggregate(agg, DeviceActivatedInfo.class, DailyActivateDeviceTotalsResp.class);
		List<DailyActivateDeviceTotalsResp> result = activeDatas.getMappedResults();
		return result;
	}
}
