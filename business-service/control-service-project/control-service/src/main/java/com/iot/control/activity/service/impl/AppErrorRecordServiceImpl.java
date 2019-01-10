package com.iot.control.activity.service.impl;

import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.domain.AppErrorRecord;
import com.iot.control.activity.exception.BusinessExceptionEnum;
import com.iot.control.activity.service.AppErrorRecordService;
import com.iot.control.activity.vo.req.AppErrorRecordReq;
import com.iot.control.activity.vo.rsp.AppErrorRecordResp;
import com.iot.saas.SaaSContextHolder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AppErrorRecordServiceImpl implements AppErrorRecordService {

    private final static Logger logger = LoggerFactory.getLogger(AppErrorRecordServiceImpl.class);


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveAppErrorRecord(AppErrorRecordReq appErrorRecordReq){
        if (appErrorRecordReq == null){
            logger.error("----saveAppErrorRecord() error! appErrorRecordReq is null");
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
        }
        try {
            AppErrorRecord ar = new AppErrorRecord();
            BeanUtils.copyProperties(appErrorRecordReq, ar);
            ar.setTenantId(SaaSContextHolder.currentTenantId());
            ar.setRecordTime(new Date());
            ar.setDelFlag(0);
            mongoTemplate.insert(ar);
        }catch (Exception e){
            logger.error("----saveAppErrorRecord() error:{}",e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION,e);
        }
    }

    @Override
    public int delAppErrorRecord(AppErrorRecordReq appErrorRecordReq) {
        if (appErrorRecordReq == null){
            logger.error("----delAppErrorRecord() error! appErrorRecordReq is null");
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
        }
        try {
            DBCollection collection = mongoTemplate.getCollection("app_error_record");
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("userId", appErrorRecordReq.getUserId());
            Long tenantId = SaaSContextHolder.currentTenantId();
            if (tenantId != null) {
                basicDBObject.put("tenantId", tenantId);
            }
            if (appErrorRecordReq.getTimeStamp() != null) {
                basicDBObject.put("timeStamp", new BasicDBObject("$lte", appErrorRecordReq.getTimeStamp()));
            }
            //删除
            //collection.remove(basicDBObject);
            //软删除
            BasicDBObject updateObject = new BasicDBObject();
            updateObject.put("delFlag", 1);
            collection.update(basicDBObject, new BasicDBObject("$set", updateObject), false, true);
            return ResultMsg.SUCCESS.getCode();
        }catch (Exception e){
            logger.error("----saveAppErrorRecord() error:{}",e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION,e);
        }
    }

    @Override
    public PageInfo queryAppErrorRecordByUser(AppErrorRecordReq appErrorRecordReq) {
        DBCollection collection = mongoTemplate.getCollection("app_error_record");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("delFlag", 0);
        basicDBObject.put("userId", appErrorRecordReq.getUserId());

        if (appErrorRecordReq.getTimeStamp()!=null){
            basicDBObject.put("timeStamp", new BasicDBObject("$gte",appErrorRecordReq.getTimeStamp()));
        }
        if (appErrorRecordReq.getCode()!=null){
            basicDBObject.put("code", appErrorRecordReq.getCode());
        }
        if (StringUtils.isNotBlank(appErrorRecordReq.getPath())){
            basicDBObject.put("path",appErrorRecordReq.getPath());
        }
        DBCursor cursor = collection.find(basicDBObject).limit(appErrorRecordReq.getPageSize()).sort(new BasicDBObject("timeStamp",-1));
        List<AppErrorRecordResp> list = new ArrayList();
        cursor.toArray().forEach(m->{
            AppErrorRecordResp resp =  new AppErrorRecordResp();

            Integer code = m.get("code") != null ? Integer.valueOf(m.get("code").toString()) : 0;
            String message = m.get("message") != null ? m.get("message").toString() : StringUtil.EMPTY;
            String path = m.get("path") != null ? m.get("path").toString() : StringUtil.EMPTY;
            Long timeStamp = m.get("timeStamp") != null ? Long.valueOf(m.get("timeStamp").toString()) : null;

            resp.setId(Long.valueOf(m.get("_id").toString()));
            resp.setCode(code);
            resp.setUserId(m.get("userId").toString());
            resp.setMessage(message);
            resp.setPath(path);
            resp.setTimeStamp(timeStamp);

            list.add(resp);
        });
        PageInfo pageInfo = new PageInfo();
        pageInfo.setStartRow(appErrorRecordReq.getPageNum()*appErrorRecordReq.getPageSize());
        pageInfo.setPageNum(appErrorRecordReq.getPageNum());
        pageInfo.setPages(appErrorRecordReq.getPageNum());
        pageInfo.setPageSize(appErrorRecordReq.getPageSize());
        pageInfo.setTotal(cursor.count());
        pageInfo.setList(list);
        return pageInfo;
    }

}
