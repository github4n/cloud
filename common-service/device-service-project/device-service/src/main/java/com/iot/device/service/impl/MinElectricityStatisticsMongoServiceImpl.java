package com.iot.device.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iot.device.mapper.MinElectricityStatisticsMapper;
import com.iot.device.model.MinElectricityStatistics;
import com.iot.device.repository.MinElectricityStatisticsRepository;
import com.iot.device.service.IMinElectricityStatisticsMongoService;
import com.iot.device.service.IMinElectricityStatisticsService;
import com.iot.device.vo.req.ElectricityStatisticsReq;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import com.mongodb.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
public class MinElectricityStatisticsMongoServiceImpl implements IMinElectricityStatisticsMongoService {

	@Autowired
	private MinElectricityStatisticsRepository minElectricityStatisticsRepository;

	@Autowired
	MongoTemplate mongoTemplate;


	@Override
	public List<ElectricityStatisticsRsp> selectByCondition() {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("min_electricity_statistics");
		BasicDBObject key  = new BasicDBObject();
		key.put("deviceId",1);
		key.put("orgId",1);
		key.put("tenantId",1);
		key.put("userId",1);
		key.put("localtimeStr",1);
		BasicDBObject cond  = new BasicDBObject();
		cond.put("localtimeStr",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		BasicDBObject initial = new BasicDBObject();
		initial.append("electricValue", 0.0);
		initial.append("localtimeStr","");
		initial.append("deviceId","");
		initial.append("tenantId","");
		initial.append("userId","");
		String reduce = "function Reduce(doc, out) { out.electricValue+=doc.electricValue;out.userId=doc.userId;out.localtimeStr=doc.localtimeStr;out.deviceId=doc.deviceId;out.tenantId=doc.tenantId;}";
//		String finalize = "function(out){return out;}";
		//String reduce = "function Reduce(doc, out) { out.electricValue+=doc.electricValue;}";
//		String finalize = "function(out){return out;}";
		DBObject dbObject = (DBObject) collection.group(key,cond,initial,reduce);
		List<DBObject> list = (List<DBObject>) dbObject;
		List<ElectricityStatisticsRsp> result = new ArrayList<>();
		list.forEach(m->{
			ElectricityStatisticsRsp electricityStatisticsRsp = new ElectricityStatisticsRsp();
			try {
				electricityStatisticsRsp.setDay(new SimpleDateFormat("yyyy-MM-dd").parse(m.get("localtimeStr").toString()));
			} catch (Exception e){
				e.printStackTrace();
			}
			electricityStatisticsRsp.setDeviceId(m.get("deviceId").toString());
			//electricityStatisticsRsp.setOrgId(new Long(m.get("orgId").toString()));
			electricityStatisticsRsp.setElectricValue(new Double(m.get("electricValue").toString()));
			electricityStatisticsRsp.setTenantId(new Long(m.get("tenantId").toString()));
			electricityStatisticsRsp.setUserId(new Long(m.get("userId").toString()));
			result.add(electricityStatisticsRsp);
		});
		return result;
	}

	@Override
	public ElectricityStatisticsRsp selectElectricityRspByDeviceAndUser(EnergyReq energyReq) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("min_electricity_statistics");
		BasicDBObject key  = new BasicDBObject();
		BasicDBObject cond  = new BasicDBObject();
		cond.put("localtimeStr",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		cond.put("deviceId",energyReq.getDevId());
		if (energyReq.getTenantId()!=null){
			cond.put("tenantId",energyReq.getTenantId());
		}
		cond.put("userId",energyReq.getUserId());
		BasicDBObject initial = new BasicDBObject();
		initial.append("electricValue", 0.0);
		initial.append("localtimeStr","");
		initial.append("deviceId","");
		initial.append("orgId","");
		initial.append("tenantId","");
		initial.append("userId","");
		String reduce = "function Reduce(doc, out) { out.electricValue+=doc.electricValue;out.userId=doc.userId;out.localtimeStr=doc.localtimeStr;out.deviceId=doc.deviceId;out.orgId=doc.orgId;out.tenantId=doc.tenantId;}";
//		String finalize = "function(out){return out;}";
		DBObject dbObject = (DBObject) collection.group(key,cond,initial,reduce);
		List<DBObject> list = (List<DBObject>) dbObject;
		List<ElectricityStatisticsRsp> result = new ArrayList<>();
		ElectricityStatisticsRsp electricityStatisticsRsp = new ElectricityStatisticsRsp();
		list.forEach(m->{
			try {
				electricityStatisticsRsp.setDay(new SimpleDateFormat("yyyy-MM-dd").parse(m.get("localtimeStr").toString()));
			} catch (Exception e){
				e.printStackTrace();
			}
			electricityStatisticsRsp.setDeviceId(m.get("deviceId").toString());
			//electricityStatisticsRsp.setOrgId(new Long(m.get("orgId").toString()));
			electricityStatisticsRsp.setElectricValue(new BigDecimal(new Double(m.get("electricValue").toString())).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue());
			electricityStatisticsRsp.setTenantId(new Long(m.get("tenantId").toString()));
			electricityStatisticsRsp.setUserId(new Long(m.get("userId").toString()));
		});
		return electricityStatisticsRsp;
	}

	@Override
	public List<ElectricityStatisticsRsp> selectByReq(ElectricityStatisticsReq req) {

		DBCollection collection = mongoTemplate.getCollection("min_electricity_statistics");
		BasicDBObject key  = new BasicDBObject();
		if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
			key.put("deviceId", new BasicDBObject(QueryOperators.IN, req.getDeviceIds()));
		}
		if (!Strings.isNullOrEmpty(req.getDeviceId())) {
			key.put("deviceId", req.getDeviceId());
		}
		if (req.getStep() != null) {
			key.put("step", req.getStep());
		}
		if (req.getOrgId() != null) {
			key.put("orgId", req.getOrgId());
		}
		if (req.getTenantId() != null) {
			key.put("tenantId", req.getTenantId());
		}
		if (!Strings.isNullOrEmpty(req.getArea())) {
			key.put("area", req.getArea());
		}
		if (!Strings.isNullOrEmpty(req.getTimeStr())) {
			key.put("timeStr", req.getTimeStr());
		}
		if (req.getIsMaster() != null) {
			key.put("isMaster", req.getIsMaster());
		}
		DBCursor cursor = collection.find(key).sort(new BasicDBObject("_id",-1));
		List<ElectricityStatisticsRsp> list = Lists.newArrayList();
		cursor.forEach(m->{
			ElectricityStatisticsRsp resp = new ElectricityStatisticsRsp();
			resp.setUserId(m.get("userId") != null ? Long.parseLong(m.get("userId").toString()) : null);
			resp.setDeviceId(m.get("deviceId") != null ? String.valueOf(m.get("deviceId")) : null);
			resp.setOrgId(m.get("orgId") != null ? Long.parseLong(m.get("orgId").toString()) : null);
			resp.setArea(m.get("area") != null ? m.get("area").toString() : null);
			resp.setTenantId(m.get("tenantId") != null ? Long.parseLong(m.get("tenantId").toString()) : null);
			resp.setElectricValue(m.get("electricValue") != null ? Double.parseDouble(m.get("electricValue").toString()) : null);

			list.add(resp);
		});
		return list;
	}

	@Override
	public boolean insertOrUpdateBatch(List<MinElectricityStatistics> minList) {
		DBCollection collection = mongoTemplate.getCollection("min_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		minList.forEach(m->{
			basicDBObject.put("tenantId",m.getTenantId());
			basicDBObject.put("deviceId",m.getDeviceId());
			basicDBObject.put("step", m.getStep());
			basicDBObject.put("area", m.getArea());
			basicDBObject.put("timeStr",m.getTimeStr());
			BasicDBObject update = new BasicDBObject();
			update.put("tenantId",m.getTenantId());
			update.put("deviceId",m.getDeviceId());
			update.put("step",m.getStep());
			update.put("area",m.getArea());
			update.put("userId",m.getUserId());
			update.put("time",m.getTime());
			update.put("timeStr",m.getTimeStr());
			update.put("isMaster", m.getIsMaster());
			update.put("localtime",m.getLocaltime());
			update.put("localtimeStr",m.getLocaltimeStr());
			update.put("electricValue",m.getElectricValue());
			collection.update(basicDBObject,update,true,false);
		});
		return true;
	}
}
