package com.iot.boss.dao.refund;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.boss.dto.OrderPriceDto;
import com.iot.boss.dto.RefundListDto;
import com.iot.boss.dto.RefundListSearch;
import com.iot.boss.entity.refund.VideoRefundRecord;

/**
 * 项目名称：cloud
 * 功能描述：视频退款记录sql
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:04
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:04
 * 修改描述：
 */
@Mapper
public interface VideoRefundRecordMapper {
    /**
     * 描述：添加退款申请
     * @author 490485964@qq.com
     * @date 2018/5/21 17:46
     * @param
     * @return
     */
    @Insert("insert into video_refund_record ("
//            + "id,"
            + "order_id,plan_id,tenant_id,refund_apply_id,refund_apply_name,refund_reason,refund_price,"
            + "refund_apply_time,audit_id,audit_name,audit_message,audit_time,audit_status,refund_status,user_id "
            + ") values ("
//            + "#{videoRefundRecord.id,jdbcType=BIGINT},"
            + "#{videoRefundRecord.orderId,jdbcType=VARCHAR},"
            + "#{videoRefundRecord.planId,jdbcType=VARCHAR},#{videoRefundRecord.tenantId,jdbcType=BIGINT},"
            + "#{videoRefundRecord.refundApplyId,jdbcType=BIGINT},#{videoRefundRecord.refundApplyName,jdbcType=VARCHAR},"
            + "#{videoRefundRecord.refundReason,jdbcType=VARCHAR}, #{videoRefundRecord.refundPrice,jdbcType=DECIMAL},"
            + "now(),#{videoRefundRecord.auditId,jdbcType=BIGINT},"
            + "#{videoRefundRecord.auditName,jdbcType=VARCHAR},#{videoRefundRecord.auditMessage,jdbcType=VARCHAR}, "
            + "#{videoRefundRecord.auditTime,jdbcType=DATE},#{videoRefundRecord.auditStatus,jdbcType=INTEGER},"
            + "#{videoRefundRecord.refundStatus,jdbcType=INTEGER},#{videoRefundRecord.userId,jdbcType=VARCHAR}"
            + ")")
    int insertVideoRefundRecord(@Param("videoRefundRecord")VideoRefundRecord videoRefundRecord);
    /**
     * 描述：校验一个订单是否已经有过申请退款
     * @author 490485964@qq.com
     * @date 2018/5/21 17:46
     * @param
     * @return
     */
    @Select("select count(1) from video_refund_record where order_id=#{orderId} and tenant_id=#{tenantId}")
    int checkRefundRecordExist(@Param("orderId") String orderId,@Param("tenantId") Long tenantId);

    /**
     * 描述：价格和退款理由修改
     * @author 490485964@qq.com
     * @date 2018/5/22 18:50
     * @param
     * @return
     */
    @Update("update video_refund_record set "
            +"refund_price=#{videoRefundRecord.refundPrice},"
            +"refund_reason=#{videoRefundRecord.refundReason},"
            +"audit_status=#{videoRefundRecord.auditStatus},"
            +"refund_apply_time=now() "
            +" where id=#{videoRefundRecord.id}")
    int editRecordPrice(@Param("videoRefundRecord") VideoRefundRecord videoRefundRecord);

    /**
     * @despriction：获取退款申请详情
     * @author  yeshiyuan
     * @created 2018/5/21 15:19
     * @param refundId 退款id
     * @return
     */
    @Select("select id," +
            "order_id as orderId," +
            "plan_id as planId," +
            "tenant_id as tenantId," +
            "refund_apply_id as refundApplyId," +
            "refund_apply_name as refundApplyName," +
            "refund_reason as refundReason," +
            "refund_price as refundPrice," +
            "refund_apply_time as refundApplyTime," +
            "audit_id as auditId," +
            "audit_name as auditName," +
            "audit_message as auditMessage," +
            "audit_time as auditTime," +
            "audit_status as auditStatus," +
            "refund_status as refundStatus," +
            "user_id as userId " +
            " from video_refund_record where id = #{refundId} limit 1")
    VideoRefundRecord getById(@Param("refundId")Long refundId);

    /**
      * @despriction：退款审核操作
      * @author  yeshiyuan
      * @created 2018/5/21 18:12
      * @param null
      * @return
      */
    @Update("update video_refund_record set " +
            "audit_id=#{videoRefundRecord.auditId}," +
            "audit_name=#{videoRefundRecord.auditName}," +
            "audit_message=#{videoRefundRecord.auditMessage}," +
            "audit_time = #{videoRefundRecord.auditTime}," +
            "audit_status = #{videoRefundRecord.auditStatus}," +
            "refund_status = #{videoRefundRecord.refundStatus} " +
            " where id=#{videoRefundRecord.id} and audit_status = #{oldAuditStatus}")
    int updateAuditStatus(@Param("videoRefundRecord") VideoRefundRecord videoRefundRecord,@Param("oldAuditStatus") Integer oldAuditStatus);

    @Select("select pay_price as price, currency as currency from iot_db_payment.pay_transation where " +
            "order_id=#{orderId,jdbcType=VARCHAR} and user_id=#{userId,jdbcType=VARCHAR}")
    public OrderPriceDto getOrderPrice(@Param("orderId") String orderId, @Param("userId")String userId);

    @Select({"<script>",
            "SELECT " +
                    " t1.id AS refundId, " +
                    " t1.order_id AS orderId, " +
                    " t1.plan_id AS planId, " +
                    " t1.refund_status AS refundStatus, " +
                    " t2.plan_name AS planName, " +
                    " t2.plan_start_time AS planStartTime, " +
                    " t2.plan_end_time AS planEndTime, " +
                    " t3.user_name AS userName, " +
                    " t4.pay_time AS payTime " +
                    "FROM " +
                    " iot_db_boss.video_refund_record t1 " +
                    "LEFT JOIN iot_db_video.video_plan t2 ON (t1.plan_id = t2.plan_id) " +
                    "LEFT JOIN iot_db_user.user t3 ON (t2.user_id = t3.uuid) " +
                    "LEFT JOIN iot_db_payment.pay_transation t4 ON (t1.order_id= t4.order_id) " +
                    "WHERE " +
                    " t1.refund_status = #{searchParam.refundStatus,jdbcType=INTEGER} " +
                    "	 <if test=\"searchParam.orderId != null \"> and t1.order_id=#{searchParam.orderId,jdbcType=VARCHAR} </if>",
            "	 <if test=\"searchParam.tenantId != null \"> and t1.tenant_id=#{searchParam.tenantId,jdbcType=VARCHAR} </if>",
            "	 <if test=\"searchParam.userId != null \"> and t2.user_Id=#{searchParam.userId,jdbcType=VARCHAR} </if>",
            "	 <if test=\"searchParam.refundApplyTime != null \"> and DATE_FORMAT(t1.refund_apply_time, '%Y-%m-%d')=#{searchParam.refundApplyTime,jdbcType=DATE} </if>",
            "</script>" })
    public List<RefundListDto> queryRefundApplyList(@Param("searchParam")RefundListSearch searchParam);

    /**
      * @despriction：修改退款状态
      * @author  yeshiyuan
      * @created 2018/5/22 11:39
      * @param refundId 退款申请记录id
      * @param refundStatus 退款状态
      * @return
      */
    @Update("update video_refund_record set refund_status = #{refundStatus} where id=#{refundId}")
    int updateRefundStatus(@Param("refundId") Long refundId,@Param("refundStatus") Integer refundStatus);
	
	/**
	 * 
	 * 描述：获取退款记录
	 * @author 李帅
	 * @created 2018年5月22日 下午4:25:14
	 * @since 
	 * @param orderIDList
	 * @return
	 */
	@Select({ "<script>", 
		"select " + 
		" id as id," + 
		" order_id as orderId," + 
		" plan_id as planId," + 
		" tenant_id as tenantId," + 
		" refund_apply_id as refundApplyId," + 
		" refund_apply_name as refundApplyName," + 
		" refund_reason as refundReason," + 
		" refund_price as refundPrice," + 
		" refund_apply_time as refundApplyTime," + 
		" audit_id as auditId," + 
		" audit_name as auditName," + 
		" audit_message as auditMessage," + 
		" audit_time as auditTime," + 
		" audit_status as auditStatus," + 
		" refund_status as refundStatus " + 
		"from video_refund_record ",
		"where order_id in ", 
		"<foreach item='item' index='index' collection='orderIDList'",
		"	open='(' separator=',' close=')'>", 
		"	#{item}",
	    "</foreach>",
	"</script>" })
	List<VideoRefundRecord> getVideoRefundRecord(@Param("orderIDList") List<String> orderIDList);
}
