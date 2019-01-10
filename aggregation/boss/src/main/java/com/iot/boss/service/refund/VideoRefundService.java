package com.iot.boss.service.refund;

import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.refund.VideoRefundRecordDto;
import com.iot.boss.vo.refund.RecordDto;
import com.iot.video.dto.AllRecordSearchParam;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：BOSS聚合层
 * 功能描述：报障业务
 * 创建人： mao2080@sina.com
 * 创建时间：2018/5/16 9:22
 * 修改人： mao2080@sina.com
 * 修改时间：2018/5/16 9:22
 * 修改描述：
 */
public interface VideoRefundService {

    /**
     * 
     * 描述：订单列表查询
     * @author 李帅
     * @created 2018年5月22日 下午3:14:10
     * @since 
     * @param searchParam
     */
	PageInfo<RecordDto> queryPayRecordList(AllRecordSearchParam searchParam);
    /**
     * 描述： 退款申请
     * @author 490485964@qq.com
     * @date 2018/5/21 17:34
     * @param
     * @return
     */
    void applyRefund(VideoRefundRecordDto videoRefundRecordDto);

    /**
      * @despriction：查看计划的其他订单
      * @author  yeshiyuan
      * @created 2018/5/23 14:33
      * @param orderId 订单id
      * @param planId 计划id
      * @return
      */
    List<RecordDto> queryOtherPayRecord(String orderId,String planId);

    /**
      * @despriction：退款审核
      * @author  yeshiyuan
      * @created 2018/5/28 17:22
      * @param refundId 退款订单ID
      * @param auditStatus 审核状态
      * @param auditMessage 审核意见
      * @return
      */
    void refundAudit(Long refundId, Integer auditStatus,String auditMessage);
}
