package com.iot.boss.service.refund.impl;

import com.iot.boss.dao.refund.VideoRefundLogMapper;
import com.iot.boss.entity.refund.VideoRefundLog;
import com.iot.boss.service.refund.VideoRefundLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目名称：cloud
 * 功能描述：视频退款日志serviceimpl
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:13
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:13
 * 修改描述：
 */
@Service
public class VideoRefundLogServiceImpl implements VideoRefundLogService{

    @Autowired
    private VideoRefundLogMapper videoRefundLogMapper;

    /**
     * @despriction：记录退款操作日志
     * @author  yeshiyuan
     * @created 2018/5/21 11:36
     * @param videoRefundLog 退款日志实体类
     * @return
     */
    @Override
    public int add(VideoRefundLog videoRefundLog) {
        return videoRefundLogMapper.add(videoRefundLog);
    }
}
