package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.mapper.MonthlyRuntimeMapper;
import com.iot.device.model.MonthlyElectricityStatistics;
import com.iot.device.model.MonthlyRuntime;
import com.iot.device.service.IMonthlyRuntimeMongoService;
import com.iot.device.service.IMonthlyRuntimeService;
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
public class MonthlyRuntimeMongoServiceImpl implements IMonthlyRuntimeMongoService {

	@Autowired
    private MonthlyRuntimeMapper monthlyRuntimeMapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Boolean insertOrUpdateBatch(List<MonthlyRuntime> monthlylist) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("monthly_runtime");
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
			update.put("runTime",m.getRuntime());
			collection.update(basicDBObject,update,true,false);
		});
		return true;
	}

	@Override
	public List<MonthlyRuntime> selectByCondition(MonthlyRuntime monthlyRuntime) {

//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("monthly_runtime");
		BasicDBObject basicDBObject  = new BasicDBObject();
		if (monthlyRuntime.getTenantId()!=null){
			basicDBObject.put("tenantId",monthlyRuntime.getTenantId());
		}
		basicDBObject.put("deviceId",monthlyRuntime.getDeviceId());
		basicDBObject.put("month",monthlyRuntime.getMonth());
		basicDBObject.put("year",monthlyRuntime.getYear());
		if (monthlyRuntime.getUserId()!=null){
			basicDBObject.put("userId",monthlyRuntime.getUserId());
		}
		DBCursor cursor = collection.find(basicDBObject).sort(new BasicDBObject("time",-1));
		List<MonthlyRuntime> result = new ArrayList<>();
		List<DBObject> list = cursor.toArray();
		list.forEach(m->{
			MonthlyRuntime monthlyRuntimep = new MonthlyRuntime();
			//monthlyRuntimep.setId(new Long(m.get("_id").toString()));
			monthlyRuntimep.setDeviceId(m.get("deviceId").toString());
			monthlyRuntimep.setRuntime(new Double(m.get("runTime").toString()).longValue());
			//monthlyRuntimep.setOrgId(new Long(m.get("orgId").toString()));
			monthlyRuntimep.setTenantId(new Long(m.get("tenantId").toString()));
			monthlyRuntimep.setUserId(new Long(m.get("userId").toString()));
			monthlyRuntimep.setMonth(Integer.parseInt(m.get("month").toString()));
			monthlyRuntimep.setYear(Integer.parseInt(m.get("year").toString()));
			result.add(monthlyRuntimep);
		});
		return result;
	}

//	@Override
//	public List<EnergyRsp> selectMonthlyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {
//		return monthlyRuntimeMapper.selectMonthlyRuntimeRsp(runtimeReq);
//	}

	@Override
	public Page<EnergyRsp> selectMonthlyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {

//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("monthly_runtime");
		BasicDBObject basicDBObject  = new BasicDBObject();
		basicDBObject.put("deviceId",runtimeReq.getDevId());
		if (runtimeReq.getTenantId()!=null){
			basicDBObject.put("tenantId",runtimeReq.getTenantId());
		}
		basicDBObject.put("userId",runtimeReq.getUserId());
		BasicDBObject sort = new BasicDBObject();
		sort.put("year",-1);
		sort.put("month",-1);
		DBCursor cursor = collection.find(basicDBObject).limit(runtimeReq.getPageSize()).sort(new BasicDBObject(sort));
		List<DBObject> list = cursor.toArray();
		List<EnergyRsp> resultList = Lists.newArrayList();
		list.forEach(m->{
			EnergyRsp energyRsp = new EnergyRsp();
			energyRsp.setTime(m.get("month").toString());
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
