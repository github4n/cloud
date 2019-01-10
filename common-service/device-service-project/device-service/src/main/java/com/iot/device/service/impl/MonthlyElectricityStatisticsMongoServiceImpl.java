package com.iot.device.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.model.MonthlyElectricityStatistics;
import com.iot.device.repository.MonthlyElectricityStatisticsRepository;
import com.iot.device.service.IMonthlyElectricityStatisticsMongoService;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.req.MonthlyElectricityStatisticsReq;
import com.iot.device.vo.rsp.EnergyRsp;
import com.iot.device.vo.rsp.MonthlyElectricityStatisticsResp;
import com.mongodb.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
@Service
public class MonthlyElectricityStatisticsMongoServiceImpl implements IMonthlyElectricityStatisticsMongoService {

	@Autowired
	private MonthlyElectricityStatisticsRepository monthlyElectricityStatisticsRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Boolean insertOrUpdateBatch(List<MonthlyElectricityStatistics> monthlylist) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("monthly_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		monthlylist.forEach(m->{
			basicDBObject.put("tenantId",m.getTenantId());
			basicDBObject.put("deviceId",m.getDeviceId());
			basicDBObject.put("userId",m.getUserId());
			basicDBObject.put("month",m.getMonth());
			basicDBObject.put("year",m.getYear());
			BasicDBObject update = new BasicDBObject();

			update.put("tenantId",m.getTenantId());
			update.put("deviceId",m.getDeviceId());
			update.put("userId",m.getUserId());
			update.put("month",m.getMonth());
			update.put("year",m.getYear());
			update.put("orgId",m.getOrgId());
			update.put("electricValue",m.getElectricValue());
			collection.update(basicDBObject,update,true,false);
		});
		return true;
	}

	@Override
	public List<MonthlyElectricityStatistics> selectByCondition(
			MonthlyElectricityStatistics monthlyElectricityStatistics) {

//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("monthly_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		if (monthlyElectricityStatistics.getTenantId()!=null){
			basicDBObject.put("tenantId",monthlyElectricityStatistics.getTenantId());
		}
		basicDBObject.put("deviceId",monthlyElectricityStatistics.getDeviceId());
		basicDBObject.put("month",monthlyElectricityStatistics.getMonth());
		basicDBObject.put("year",monthlyElectricityStatistics.getYear());
		if (monthlyElectricityStatistics.getUserId()!=null){
			basicDBObject.put("userId",monthlyElectricityStatistics.getUserId());
		}
		DBCursor cursor = collection.find(basicDBObject).sort(new BasicDBObject("time",-1));
		List<MonthlyElectricityStatistics> result = new ArrayList<>();
		List<DBObject> list = cursor.toArray();
		list.forEach(m->{
			MonthlyElectricityStatistics monthlyElectricityStatisticsp = new MonthlyElectricityStatistics();
//			monthlyElectricityStatisticsp.setId(new Long(m.get("_id").toString()));
			monthlyElectricityStatisticsp.setDeviceId(m.get("deviceId").toString());
			monthlyElectricityStatisticsp.setElectricValue(new Double(m.get("electricValue").toString()));
//			monthlyElectricityStatisticsp.setOrgId(new Long(m.get("orgId").toString()));
			monthlyElectricityStatisticsp.setTenantId(new Long(m.get("tenantId").toString()));
			monthlyElectricityStatisticsp.setUserId(new Long(m.get("userId").toString()));
			monthlyElectricityStatisticsp.setMonth(Integer.parseInt(m.get("month").toString()));
			monthlyElectricityStatisticsp.setYear(Integer.parseInt(m.get("year").toString()));
			result.add(monthlyElectricityStatisticsp);
		});
		return result;
	}

	@Override
	public Page<EnergyRsp> selectMonthlyElectricityRsp(EnergyReq energyReq) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("monthly_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		basicDBObject.put("deviceId",energyReq.getDevId());
		if (energyReq.getTenantId()!=null){
			basicDBObject.put("tenantId",energyReq.getTenantId());
		}
		basicDBObject.put("userId",energyReq.getUserId());
		BasicDBObject sort = new BasicDBObject();
		sort.put("year",-1);
		sort.put("month",-1);
		DBCursor cursor = collection.find(basicDBObject).limit(energyReq.getPageSize()).sort(new BasicDBObject(sort));
		List<DBObject> list = cursor.toArray();
		List<EnergyRsp> resultList = Lists.newArrayList();
		list.forEach(m->{
			EnergyRsp energyRsp = new EnergyRsp();
			energyRsp.setTime(m.get("month").toString());
			energyRsp.setValue(new BigDecimal(new Double(m.get("electricValue").toString())).setScale(3,BigDecimal.ROUND_HALF_UP).toString());
			resultList.add(energyRsp);
		});
		Page returnPageData = new Page();
		returnPageData.setResult(resultList);
		returnPageData.setTotal(cursor.count());
		returnPageData.setPages(energyReq.getOffset());
		return returnPageData;
	}

	@Override
	public List<MonthlyElectricityStatisticsResp> selectListByReq(MonthlyElectricityStatisticsReq req) {
		DBCollection collection = mongoTemplate.getCollection("monthly_electricity_statistics");
		BasicDBObject queryDb  = new BasicDBObject();
		if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
			queryDb.put("deviceId", new BasicDBObject(QueryOperators.IN, req.getDeviceIds()));
		}
		if (!Strings.isNullOrEmpty(req.getDeviceId())) {
			queryDb.put("deviceId", req.getDeviceId());
		}
		if (req.getYear() != null) {
			queryDb.put("year", req.getYear());
		}
		if (req.getMonth() != null) {
			queryDb.put("month", req.getMonth());
		}
		if (req.getTenantId() != null) {
			queryDb.put("tenantId", req.getTenantId());
		}
		if (req.getUserId() != null) {
			queryDb.put("userId", req.getUserId());
		}
		DBCursor cursor = collection.find(queryDb);
		List<MonthlyElectricityStatisticsResp> list = Lists.newArrayList();
		cursor.forEach(m-> {
			MonthlyElectricityStatisticsResp statistics = new MonthlyElectricityStatisticsResp();
			statistics.setDeviceId(m.get("deviceId") != null ? m.get("deviceId").toString() : null);
			statistics.setYear(m.get("year") != null ? Integer.parseInt(m.get("year").toString()) : null);
			statistics.setMonth(m.get("month") != null ? Integer.parseInt(m.get("month").toString()) : null);
			statistics.setTenantId(m.get("tenantId") != null ? Long.parseLong(m.get("tenantId").toString()) : null);
			statistics.setUserId(m.get("userId") != null ? Long.parseLong(m.get("userId").toString()) : null);
			statistics.setOrgId(m.get("orgId") != null ? Long.parseLong(m.get("orgId").toString()) : null);
			statistics.setElectricValue(m.get("electricValue") != null ? Double.parseDouble(m.get("electricValue").toString()) : null);

			list.add(statistics);
		});
		return list;
	}
}
