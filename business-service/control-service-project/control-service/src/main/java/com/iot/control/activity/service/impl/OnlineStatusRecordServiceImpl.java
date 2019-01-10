package com.iot.control.activity.service.impl;

import com.iot.control.activity.domain.OnlineStatusRecord;
import com.iot.control.activity.mapper.OnlineStatusRecordMapper;
import com.iot.control.activity.service.OnlineStatusRecordService;
import com.iot.control.activity.vo.req.OnlineStatusRecordReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/27 14:50
 * 修改人:
 * 修改时间：
 */
@Slf4j
@Service
public class OnlineStatusRecordServiceImpl implements OnlineStatusRecordService {
    @Autowired
    private OnlineStatusRecordMapper onlineStatusRecordMapper;


    @Transactional
    public int insert(OnlineStatusRecordReq record) {
        return onlineStatusRecordMapper.insert(record);
    }

    @Transactional
    public int insertSelective(OnlineStatusRecord record) {
        return onlineStatusRecordMapper.insertSelective(record);
    }

    @Transactional
    public int updateByPrimaryKey(OnlineStatusRecord record) {
        return onlineStatusRecordMapper.updateByPrimaryKey(record);
    }

    @Override
    public OnlineStatusRecord selectById(String id) {
        return onlineStatusRecordMapper.selectById(id);
    }
}
