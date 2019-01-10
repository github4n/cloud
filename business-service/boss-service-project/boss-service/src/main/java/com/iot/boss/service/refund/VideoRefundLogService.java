package com.iot.boss.service.refund;

import com.iot.boss.entity.refund.VideoRefundLog;

/**
 * 项目名称：cloud
 * 功能描述：视频退款日志service
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:13
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:13
 * 修改描述：
 */
public interface VideoRefundLogService {

    /**
      * @despriction：记录退款操作日志
      * @author  yeshiyuan
      * @created 2018/5/21 11:36
      * @param videoRefundLog 退款日志实体类
      * @return
      */
    int add(VideoRefundLog videoRefundLog);
}
