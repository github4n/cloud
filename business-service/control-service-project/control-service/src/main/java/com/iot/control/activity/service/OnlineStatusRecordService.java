package com.iot.control.activity.service;

import com.iot.control.activity.domain.OnlineStatusRecord;
import com.iot.control.activity.vo.req.OnlineStatusRecordReq;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/27 14:50
 * 修改人:
 * 修改时间：
 */
public interface OnlineStatusRecordService {

    int insert(OnlineStatusRecordReq record);

    int insertSelective(OnlineStatusRecord record);

    int updateByPrimaryKey(OnlineStatusRecord record);

    OnlineStatusRecord selectById(String id);
}
