package com.iot.device.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.model.DailyElectricityStatistics;
import com.iot.device.repository.DailyElectricityStatisticsRepository;
import com.iot.device.service.IDailyElectricityStatisticsMongoService;
import com.iot.device.vo.req.DailyElectricityStatisticsReq;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
import com.iot.device.vo.rsp.EnergyRsp;
import com.mongodb.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
@Service
public class DailyElectricityStatisticsMongoServiceImpl implements IDailyElectricityStatisticsMongoService {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private DailyElectricityStatisticsRepository dailyElectricityStatisticsRepository;

	@Override
	public Boolean insertOrUpdateBatch(List<DailyElectricityStatistics> dailylist) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("daily_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		dailylist.forEach(m->{
			basicDBObject.put("tenantId",m.getTenantId());
			basicDBObject.put("deviceId",m.getDeviceId());
//			basicDBObject.put("userId",m.getUserId());
			basicDBObject.put("day", m.getDayStr());
			BasicDBObject update = new BasicDBObject();
			update.put("tenantId",m.getTenantId());
			update.put("deviceId",m.getDeviceId());
			update.put("userId",m.getUserId());
			update.put("electricValue",m.getElectricValue());
			update.put("day", m.getDayStr());
			update.put("orgId",m.getOrgId());
			collection.update(basicDBObject,update,true,false);
		});
		return true;
	}

//	@Override
//	public List<DailyElectricityStatistics> selectByCondition(DailyElectricityStatistics dailyElectricityStatistics) {
//
//		return dailyElectricityStatisticsMapper.selectDailyElectricityByCondition(dailyElectricityStatistics);
//
//	}
//
//	@Override
//	public List<EnergyRsp> selectDailyElectricityRsp(EnergyReq energyReq) {
//
//		return dailyElectricityStatisticsMapper.time(energyReq);
//	}

	@Override
	public Page<EnergyRsp> selectDailyElectricityRsp(EnergyReq energyReq) {
//		MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//		DB mongoDatabase = mongoClient.getDB("iot_db_device");
		DBCollection collection = mongoTemplate.getCollection("daily_electricity_statistics");
		BasicDBObject basicDBObject  = new BasicDBObject();
		basicDBObject.put("deviceId",energyReq.getDevId());
		if (energyReq.getTenantId()!=null){
			basicDBObject.put("tenantId",energyReq.getTenantId());
		}
		basicDBObject.put("userId",energyReq.getUserId());
		DBCursor cursor = collection.find(basicDBObject).limit(energyReq.getPageSize()).sort(new BasicDBObject("day",-1));
		List<DBObject> list = cursor.toArray();
		List<EnergyRsp> resultList = Lists.newArrayList();
		list.forEach(m->{
			EnergyRsp energyRsp = new EnergyRsp();
			energyRsp.setTime(m.get("day").toString());
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
	public List<DailyElectricityStatisticsResp> selectListByReq(DailyElectricityStatisticsReq req) {
		List<DailyElectricityStatisticsResp> list = Lists.newArrayList();
		DBCollection collection = mongoTemplate.getCollection("daily_electricity_statistics");
		BasicDBObject query  = new BasicDBObject();
		if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
			query.put("deviceId", new BasicDBObject(QueryOperators.IN, req.getDeviceIds()));
		}
		if (!Strings.isNullOrEmpty(req.getDeviceId())) {
			query.put("deviceId", req.getDeviceId());
		}
		if (!Strings.isNullOrEmpty(req.getDayStr())) {
			query.put("day", req.getDayStr());
		}
		if (req.getDay() != null) {
			String day = DateFormatUtils.format(req.getDay(), "yyyy-MM-dd");
			query.put("day", day);
		}
		if (req.getTenantId() != null) {
			query.put("tenantId", req.getTenantId());
		}
		if (!Strings.isNullOrEmpty(req.getMonthPrefix())) {
			Pattern pattern = Pattern.compile("^.*" + req.getMonthPrefix() + ".*$", Pattern.CASE_INSENSITIVE);
//			Pattern pattern = Pattern.compile(req.getMonthPrefix(), Pattern.CASE_INSENSITIVE);
			query.put("day", pattern);
		}
		DBCursor cursor = collection.find(query);
		cursor.forEach(m-> {
			DailyElectricityStatisticsResp daily = new DailyElectricityStatisticsResp();
			daily.setTenantId(m.get("tenantId") != null ? Long.parseLong(m.get("tenantId").toString()) : null);
			daily.setDeviceId(m.get("deviceId") != null ? m.get("deviceId").toString() : null);
			daily.setDay(m.get("day") != null ? m.get("day").toString() : null);
			daily.setUserId(m.get("userId") != null ? Long.parseLong(m.get("userId").toString()) : null);
			daily.setElectricValue(m.get("electricValue") != null ? Double.parseDouble(m.get("electricValue").toString()) : null);

			list.add(daily);
		});

		return list;
	}
}
