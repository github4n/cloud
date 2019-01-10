package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.mapper.WeeklyElectricityStatisticsMapper;
import com.iot.device.model.WeeklyElectricityStatistics;
import com.iot.device.repository.WeeklyElectricityStatisticsRepository;
import com.iot.device.service.IWeeklyElectricityStatisticsMongoService;
import com.iot.device.service.IWeeklyElectricityStatisticsService;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import com.iot.device.vo.rsp.EnergyRsp;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
@Service
public class WeeklyElectricityStatisticsMongoServiceImpl implements IWeeklyElectricityStatisticsMongoService {

	@Autowired
	private WeeklyElectricityStatisticsRepository weeklyElectricityStatisticsRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Boolean insertOrUpdateBatch(List<WeeklyElectricityStatistics> weeklylist) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("weekly_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		weeklylist.forEach(m->{
			basicDBObject.put("tenantId",m.getTenantId());
			basicDBObject.put("deviceId",m.getDeviceId());
			basicDBObject.put("userId",m.getUserId());
			basicDBObject.put("week",m.getWeek());
			basicDBObject.put("year",m.getYear());
			BasicDBObject update = new BasicDBObject();
			update.put("tenantId",m.getTenantId());
			update.put("deviceId",m.getDeviceId());
			update.put("userId",m.getUserId());
			update.put("electricValue",m.getElectricValue());
			update.put("week",m.getWeek());
			update.put("year",m.getYear());
			update.put("orgId",m.getOrgId());
			collection.update(basicDBObject,update,true,false);
		});
		return true;
	}

	@Override
	public List<WeeklyElectricityStatistics> selectByCondition(
			WeeklyElectricityStatistics weeklyElectricityStatistics) {

//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("weekly_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		if (weeklyElectricityStatistics.getTenantId()!=null){
			basicDBObject.put("tenantId",weeklyElectricityStatistics.getTenantId());
		}
		basicDBObject.put("deviceId",weeklyElectricityStatistics.getDeviceId());
		basicDBObject.put("week",weeklyElectricityStatistics.getWeek());
		basicDBObject.put("year",weeklyElectricityStatistics.getYear());
		if (weeklyElectricityStatistics.getUserId()!=null){
			basicDBObject.put("userId",weeklyElectricityStatistics.getUserId());
		}
		DBCursor cursor = collection.find(basicDBObject).sort(new BasicDBObject("time",-1));
		List<WeeklyElectricityStatistics> result = new ArrayList<>();
		List<DBObject> list = cursor.toArray();
		list.forEach(m->{
			WeeklyElectricityStatistics weeklyElectricityStatisticsp = new WeeklyElectricityStatistics();
//			weeklyElectricityStatisticsp.setId(new Long(m.get("_id").toString()));
			weeklyElectricityStatisticsp.setDeviceId(m.get("deviceId").toString());
			weeklyElectricityStatisticsp.setElectricValue(new Double(m.get("electricValue").toString()));
//			weeklyElectricityStatisticsp.setOrgId(new Long(m.get("orgId").toString()));
			weeklyElectricityStatisticsp.setTenantId(new Long(m.get("tenantId").toString()));
			weeklyElectricityStatisticsp.setUserId(new Long(m.get("userId").toString()));
			weeklyElectricityStatisticsp.setWeek(Integer.parseInt(m.get("week").toString()));
			weeklyElectricityStatisticsp.setYear(Integer.parseInt(m.get("year").toString()));
			result.add(weeklyElectricityStatisticsp);
		});
		return result;
	}

//	@Override
//	public List<EnergyRsp> selectWeeklyElectricityRsp(EnergyReq energyReq) {
//		return weeklyElectricityStatisticsMapper.selectWeeklyElectricityRsp(energyReq);
//}

	@Override
	public Page<EnergyRsp> selectWeeklyElectricityRsp(EnergyReq energyReq) {

//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("weekly_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		basicDBObject.put("deviceId",energyReq.getDevId());
		if (energyReq.getTenantId()!=null){
			basicDBObject.put("tenantId",energyReq.getTenantId());
		}
		basicDBObject.put("userId",energyReq.getUserId());
		BasicDBObject sort = new BasicDBObject();
		sort.put("year",-1);
		sort.put("week",-1);
		DBCursor cursor = collection.find(basicDBObject).limit(energyReq.getPageSize()).sort(sort);
		List<DBObject> list = cursor.toArray();
		List<EnergyRsp> resultList = Lists.newArrayList();
		list.forEach(m->{
			EnergyRsp energyRsp = new EnergyRsp();
			energyRsp.setTime(m.get("week").toString());
			energyRsp.setValue(new BigDecimal(new Double(m.get("electricValue").toString())).setScale(3,BigDecimal.ROUND_HALF_UP).toString());
			resultList.add(energyRsp);
		});
		Page returnPageData = new Page();
		returnPageData.setResult(resultList);
		returnPageData.setTotal(cursor.count());
		returnPageData.setPages(energyReq.getOffset());
		return returnPageData;
	}
}
