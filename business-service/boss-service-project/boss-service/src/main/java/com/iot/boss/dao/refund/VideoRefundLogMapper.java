package com.iot.boss.dao.refund;

import com.iot.boss.entity.refund.VideoRefundLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目名称：cloud
 * 功能描述：视频退款操作日志-sql
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:05
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:05
 * 修改描述：
 */
@Mapper
public interface VideoRefundLogMapper {


    @Insert("INSERT INTO video_refund_log("
//    		+ "id,"
    		+ "refund_id,operator_id,refund_price,create_time,refund_remark,refund_status) " +
            "VALUES(" +
//            "#{videoRefundLog.id}," +
            "#{videoRefundLog.refundId}," +
            "#{videoRefundLog.operatorId}," +
            "#{videoRefundLog.refundPrice,jdbcType=DECIMAL}," +
            "#{videoRefundLog.createTime}," +
            "#{videoRefundLog.refundRemark}," +
            "#{videoRefundLog.refundStatus})")
    int add(@Param("videoRefundLog") VideoRefundLog videoRefundLog);
}
