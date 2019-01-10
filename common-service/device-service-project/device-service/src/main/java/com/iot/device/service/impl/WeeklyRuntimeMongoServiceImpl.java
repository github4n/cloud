package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.mapper.WeeklyRuntimeMapper;
import com.iot.device.model.WeeklyElectricityStatistics;
import com.iot.device.model.WeeklyRuntime;
import com.iot.device.service.IWeeklyRuntimeMongoService;
import com.iot.device.service.IWeeklyRuntimeService;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.EnergyRsp;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Service
public class WeeklyRuntimeMongoServiceImpl implements IWeeklyRuntimeMongoService {

	@Autowired
    private WeeklyRuntimeMapper weeklyRuntimeMapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Boolean insertOrUpdateBatch(List<WeeklyRuntime> weeklylist) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("weekly_runtime");
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
			update.put("runTime",m.getRuntime());
			update.put("week",m.getWeek());
			update.put("year",m.getYear());
			update.put("orgId",m.getOrgId());
			collection.update(basicDBObject,update,true,false);
		});
		return true;
	}

	@Override
	public List<WeeklyRuntime> selectByCondition(WeeklyRuntime weeklyRuntime) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("weekly_runtime");
		BasicDBObject basicDBObject  = new BasicDBObject();
		if (weeklyRuntime.getTenantId()!=null){
			basicDBObject.put("tenantId",weeklyRuntime.getTenantId());
		}
		basicDBObject.put("deviceId",weeklyRuntime.getDeviceId());
		basicDBObject.put("week",weeklyRuntime.getWeek());
		basicDBObject.put("year",weeklyRuntime.getYear());
		if (weeklyRuntime.getUserId()!=null){
			basicDBObject.put("userId",weeklyRuntime.getUserId());
		}
		DBCursor cursor = collection.find(basicDBObject).sort(new BasicDBObject("time",-1));
		List<WeeklyRuntime> result = new ArrayList<>();
		List<DBObject> list = cursor.toArray();
		list.forEach(m->{
			WeeklyRuntime weeklyRuntimep = new WeeklyRuntime();
			//weeklyRuntimep.setId(new Long(m.get("_id").toString()));
			weeklyRuntimep.setDeviceId(m.get("deviceId").toString());
			weeklyRuntimep.setRuntime(new Double(m.get("runTime").toString()).longValue());
			//weeklyRuntimep.setOrgId(new Long(m.get("orgId").toString()));
			weeklyRuntimep.setTenantId(new Long(m.get("tenantId").toString()));
			weeklyRuntimep.setUserId(new Long(m.get("userId").toString()));
			weeklyRuntimep.setWeek(Integer.parseInt(m.get("week").toString()));
			weeklyRuntimep.setYear(Integer.parseInt(m.get("year").toString()));
			result.add(weeklyRuntimep);
		});
		return result;
	}

//	@Override
//	public List<EnergyRsp> selectWeeklyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {
//		return weeklyRuntimeMapper.selectWeeklyRuntimeRsp(runtimeReq);
//	}

	@Override
	public Page<EnergyRsp> selectWeeklyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {

//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("weekly_runtime");
		BasicDBObject basicDBObject  = new BasicDBObject();
		basicDBObject.put("deviceId",runtimeReq.getDevId());
		if (runtimeReq.getTenantId()!=null){
			basicDBObject.put("tenantId",runtimeReq.getTenantId());
		}
		basicDBObject.put("userId",runtimeReq.getUserId());
		BasicDBObject sort = new BasicDBObject();
		sort.put("year",-1);
		sort.put("week",-1);
		DBCursor cursor = collection.find(basicDBObject).limit(runtimeReq.getPageSize()).sort(sort);
		List<DBObject> list = cursor.toArray();
		List<EnergyRsp> resultList = Lists.newArrayList();
		list.forEach(m->{
			EnergyRsp energyRsp = new EnergyRsp();
			energyRsp.setTime(m.get("week").toString());
			energyRsp.setValue(m.get("runTime").toString());
			resultList.add(energyRsp);
		});
		Page returnPageData = new Page();
		returnPageData.setResult(resultList);
		returnPageData.setTotal(cursor.count());
		returnPageData.setPages(runtimeReq.getOffset());
		return returnPageData;
	}
}
