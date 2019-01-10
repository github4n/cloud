package com.iot.device.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.mapper.MinRuntimeMapper;
import com.iot.device.model.MinRuntime;
import com.iot.device.service.IMinRuntimeMongoService;
import com.iot.device.service.IMinRuntimeService;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import com.iot.device.vo.rsp.RuntimeRsp;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Service
public class MinRuntimeServiceMongoImpl implements IMinRuntimeMongoService {

	@Autowired
    private MinRuntimeMapper minRuntimeMapper;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<RuntimeRsp> selectByCondition() {

//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("min_runtime");
		BasicDBObject key  = new BasicDBObject();
		key.put("deviceId",1);
		key.put("orgId",1);
		key.put("tenantId",1);
		key.put("userId",1);
		key.put("localtimeStr",1);
		BasicDBObject cond  = new BasicDBObject();
		cond.put("localtimeStr",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		BasicDBObject initial = new BasicDBObject();
		initial.append("runTime", 0);
		initial.append("localtimeStr","");
		initial.append("deviceId","");
		initial.append("tenantId","");
		initial.append("userId","");
		String reduce = "function Reduce(doc, out) { out.runTime+=doc.runtime;out.localtimeStr=doc.localtimeStr;out.deviceId=doc.deviceId;out.tenantId=doc.tenantId;out.userId=doc.userId;}";
//		String finalize = "function(out){return out;}";
		DBObject dbObject = (DBObject) collection.group(key,cond,initial,reduce);
		List<DBObject> list = (List<DBObject>) dbObject;
		List<RuntimeRsp> result = new ArrayList<>();
		list.forEach(m->{
			RuntimeRsp runtimeRsp = new RuntimeRsp();
			try {
				runtimeRsp.setDay(new SimpleDateFormat("yyyy-MM-dd").parse(m.get("localtimeStr").toString()));
			} catch (Exception e){
				e.printStackTrace();
			}
			runtimeRsp.setDeviceId(m.get("deviceId").toString());
			//runtimeRsp.setOrgId(new Long(m.get("orgId").toString()));
			runtimeRsp.setRuntime(new Double(m.get("runTime").toString()).longValue());
			runtimeRsp.setTenantId(new Long(m.get("tenantId").toString()));
			runtimeRsp.setUserId(new Long(m.get("userId").toString()));
			result.add(runtimeRsp);
		});
		return result;
	}

	@Override
	public RuntimeRsp selectRuntimeRspByDeviceAndUser(RuntimeReq2Runtime runtimeReq) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("min_runtime");
		BasicDBObject key  = new BasicDBObject();
		BasicDBObject cond  = new BasicDBObject();
		cond.put("localtimeStr",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		cond.put("deviceId",runtimeReq.getDevId());
		//cond.put("tenantId",runtimeReq.getTenantId());
		cond.put("userId",runtimeReq.getUserId());
		BasicDBObject initial = new BasicDBObject();
		initial.append("runTime", 0);
		initial.append("localtimeStr","");
		initial.append("deviceId","");
		initial.append("tenantId","");
		initial.append("userId","");
		String reduce = "function Reduce(doc, out) { out.runTime+=doc.runtime;out.localtimeStr=doc.localtimeStr;out.deviceId=doc.deviceId;out.tenantId=doc.tenantId;out.userId=doc.userId;}";
//		String finalize = "function(out){return out;}";
		DBObject dbObject = (DBObject) collection.group(key,cond,initial,reduce);
		List<DBObject> list = (List<DBObject>) dbObject;
		List<ElectricityStatisticsRsp> result = new ArrayList<>();
		RuntimeRsp runtimeRsp = new RuntimeRsp();
		list.forEach(m->{
			try {
				runtimeRsp.setDay(new SimpleDateFormat("yyyy-MM-dd").parse(m.get("localtimeStr").toString()));
			} catch (Exception e){
				e.printStackTrace();
			}
			runtimeRsp.setDeviceId(m.get("deviceId").toString());
			//runtimeRsp.setOrgId(new Long(m.get("orgId").toString()));
			runtimeRsp.setRuntime(new Double(m.get("runTime").toString()).longValue());
			runtimeRsp.setTenantId(new Long(m.get("tenantId").toString()));
			runtimeRsp.setUserId(new Long(m.get("userId").toString()));
		});
		return runtimeRsp;

	}

	@Override
	public Boolean haveSameDataWithHourses(MinRuntime minRuntime) {
		DBCollection collection = mongoTemplate.getCollection("min_runtime");
		BasicDBObject basicDBObject  = new BasicDBObject();
		if (minRuntime.getTenantId()!=null){
			basicDBObject.put("tenantId",minRuntime.getTenantId());
		}
		if (minRuntime.getUserId()!=null){
			basicDBObject.put("userId",minRuntime.getUserId());
		}
		basicDBObject.put("deviceId",minRuntime.getDeviceId());
		basicDBObject.put("localtimeStr",minRuntime.getLocaltimeStr());
		basicDBObject.put("hourse",minRuntime.getHourse());
		DBCursor cursor = collection.find(basicDBObject).sort(new BasicDBObject("time",-1));
		List<DBObject> list = cursor.toArray();
		if (list.size()>0){
			return false;
		} else {
			return true;
		}
	}
}
