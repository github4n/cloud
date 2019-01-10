package com.iot.user.service;

import com.alibaba.fastjson.JSON;
import com.iot.message.api.MessageApi;
import com.iot.user.entity.FeedbackEntity;
import com.iot.user.mapper.UserFeedbackMapper;
import com.iot.user.vo.FeedbackFileVo;
import com.iot.user.vo.FeedbackReq;
import com.iot.user.vo.FeedbackVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserFeedbackService {



    private final Logger logger = LoggerFactory.getLogger(UserFeedbackService.class);
    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    public int saveFeedback(FeedbackReq feedbackReq) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setUserId(feedbackReq.getUserId());
//        String feedbackContent=feedbackReq.getFeedbackContent().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]","\"emoji\"");
        String feedbackContent=feedbackReq.getFeedbackContent();
        feedbackEntity.setFeedbackContent(feedbackContent);
        feedbackEntity.setCreateTime(new Date());
        return userFeedbackMapper.insertFeedbackContent(feedbackEntity);
    }

    public int saveFeedbackFileId(FeedbackFileVo feedbackFileVo) {
        FeedbackFileVo copyFeedbackFileVo=new FeedbackFileVo();
        copyFeedbackFileVo.setUpdateTime(feedbackFileVo.getUpdateTime());
        copyFeedbackFileVo.setCreateTime(feedbackFileVo.getCreateTime());
        copyFeedbackFileVo.setTenantId(feedbackFileVo.getTenantId());
        copyFeedbackFileVo.setUserId(feedbackFileVo.getUserId());
        copyFeedbackFileVo.setFileId(feedbackFileVo.getFileId());
        logger.info("****saveFeedbackFileId copyFeedbackFileVo is "+ JSON.toJSONString(copyFeedbackFileVo));
        return userFeedbackMapper.insertFeedbackFileId(feedbackFileVo);
    }


    public void save(@RequestBody FeedbackVo feedbackVo){

        String feedbackContent=feedbackVo.getFeedbackContent();
        Long tenantId=feedbackVo.getTenantId();
        Long userId=feedbackVo.getUserId();
        List<String> fileIdList=feedbackVo.getFileId();

        //两张表，一张保存反馈内容，一张保存反馈的文件fileId

        //用来存储用户反馈内容的表
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setFeedbackContent(feedbackContent);
        feedbackEntity.setUserId(userId);
        feedbackEntity.setCreateTime(new Date());
        userFeedbackMapper.insertFeedbackContent(feedbackEntity);

        logger.info("********feedbackFileList",JSON.toJSONString(fileIdList));
        //用来存储文件的表
       List<FeedbackFileVo> feedbackFileList=new ArrayList<>();
       if(CollectionUtils.isNotEmpty(fileIdList)){
           for(String fileId:fileIdList){
               FeedbackFileVo feedbackFileVo=new FeedbackFileVo();
               feedbackFileVo.setUpdateTime(new Date());
               feedbackFileVo.setCreateTime(new Date());
               feedbackFileVo.setTenantId(tenantId);
               feedbackFileVo.setUserId(userId);
               feedbackFileVo.setFileId(fileId);
               feedbackFileList.add(feedbackFileVo);
           }
           userFeedbackMapper.insertFeedbackFileIdByList(feedbackFileList);
       }

    }
}
