package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.mapper.DailyRuntimeMapper;
import com.iot.device.model.DailyRuntime;
import com.iot.device.service.IDailyRuntimeMongoService;
import com.iot.device.service.IDailyRuntimeService;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.EnergyRsp;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
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
public class DailyRuntimeMongoServiceImpl implements IDailyRuntimeMongoService {

    @Autowired
    DailyRuntimeMapper dailyRuntimeMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Override
//    public List<EnergyRsp> selectDailyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {
//        return dailyRuntimeMapper.selectDailyRuntimeRsp(runtimeReq);
//    }


    @Override
    public Boolean insertOrUpdateBatch(List<DailyRuntime> dailylist) {
//        MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//        DB mongoDatabase = mongoClient.getDB("iot_db_device");
        DBCollection collection = mongoTemplate.getCollection("daily_runtime");
        BasicDBObject basicDBObject  = new BasicDBObject();
        dailylist.forEach(m->{
            basicDBObject.put("tenantId",m.getTenantId());
            basicDBObject.put("deviceId",m.getDeviceId());
            basicDBObject.put("userId",m.getUserId());
            basicDBObject.put("day",new SimpleDateFormat("yyyy-MM-dd").format(m.getDay()));
            BasicDBObject update = new BasicDBObject();
            update.put("tenantId",m.getTenantId());
            update.put("deviceId",m.getDeviceId());
            update.put("userId",m.getUserId());
            update.put("runTime",m.getRuntime());
            update.put("day",new SimpleDateFormat("yyyy-MM-dd").format(m.getDay()));
            update.put("orgId",m.getOrgId());
            collection.update(basicDBObject,update,true,false);
        });
        return true;
    }

    @Override
    public Page<EnergyRsp> selectDailyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {

//        MongoClient mongoClient = new MongoClient("192.168.6.106",27017);
//        DB mongoDatabase = mongoClient.getDB("iot_db_device");
        DBCollection collection = mongoTemplate.getCollection("daily_runtime");
        BasicDBObject basicDBObject  = new BasicDBObject();
        basicDBObject.put("deviceId",runtimeReq.getDevId());
        if (runtimeReq.getTenantId()!=null){
            basicDBObject.put("tenantId",runtimeReq.getTenantId());
        }
        basicDBObject.put("userId",runtimeReq.getUserId());
        DBCursor cursor = collection.find(basicDBObject).limit(runtimeReq.getPageSize()).sort(new BasicDBObject("day",-1));
        List<DBObject> list = cursor.toArray();
        List<EnergyRsp> resultList = Lists.newArrayList();
        list.forEach(m->{
            EnergyRsp energyRsp = new EnergyRsp();
            energyRsp.setTime(m.get("day").toString());
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
