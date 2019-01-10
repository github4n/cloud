package com.iot.payment.dao;

import com.iot.payment.entity.PayTransation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaymentMapper {

	/**
	 * 
	 * 描述：保存交易
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:53:39
	 * @since 
	 * @param transation
	 */
	@Insert("insert into pay_transation ( "
//			+ "id,"
			+ "user_id,"
			+ "pay_id,"
			+ "goods_id,"
			+ "pay_price,"
			+ "order_id,"
			+ "trade_id,"
			+ "pay_type,"
			+ "create_time,"
			+ "pay_time,"
			+ "refund_time,"
			+ "pay_status,"
			+ "refund_status,"
			+ "pay_fail_reason,"
			+ "refund_fail_reason,"
			+ "refund_sum,"
			+ "currency,"
			+ "pay_source,"
			+ "payment_id," +
			" tenant_id"
			+ ") values ( "
//			+ "#{transation.id,jdbcType=BIGINT},"
			+ "#{transation.userId,jdbcType=VARCHAR},"
			+ "#{transation.payId,jdbcType=VARCHAR},"
			+ "#{transation.goodsId,jdbcType=BIGINT},"
			+ "#{transation.payPrice,jdbcType=DOUBLE},"
			+ "#{transation.orderId,jdbcType=VARCHAR},"
			+ "#{transation.tradeId,jdbcType=VARCHAR},"
			+ "#{transation.payType,jdbcType=INTEGER},"
			+ "#{transation.createTime,jdbcType=TIMESTAMP},"
			+ "#{transation.payTime,jdbcType=TIMESTAMP},"
			+ "#{transation.refundTime,jdbcType=TIMESTAMP},"
			+ "#{transation.payStatus,jdbcType=INTEGER},"
			+ "#{transation.refundStatus,jdbcType=INTEGER},"
			+ "#{transation.payFailReason,jdbcType=VARCHAR},"
			+ "#{transation.refundFailReason,jdbcType=VARCHAR},"
			+ "#{transation.refundSum,jdbcType=VARCHAR},"
			+ "#{transation.currency,jdbcType=VARCHAR},"
			+ "#{transation.paySource,jdbcType=INTEGER},"
			+ "#{transation.paymentId,jdbcType=VARCHAR}," +
			"  #{transation.tenantId,jdbcType=BIGINT})")
	void savePayTransation(@Param("transation") PayTransation transation);
	
	/**
	 * 
	 * 描述：更新交易
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:54:00
	 * @since 
	 * @param transation
	 */
	@Update("<script> "
			+"update pay_transation "
			+ "<trim prefix=\"set\"  prefixOverrides=\",\" >"
			+ "<if test=\"transation.payId != null\"> "
			+ ",pay_id = #{transation.payId,jdbcType=VARCHAR} "
			+ "</if> "
			+ "<if test=\"transation.refundSum != null\"> "
			+ ",refund_sum = #{transation.refundSum,jdbcType=DOUBLE} "
			+ "</if> "
			+ "<if test=\"transation.tradeId != null\"> "
			+ ",trade_id = #{transation.tradeId,jdbcType=VARCHAR} "
			+ "</if> "
			+ "<if test=\"transation.payTime != null\"> "
			+ ",pay_time = #{transation.payTime,jdbcType=TIMESTAMP} "
			+ "</if> "
			+ "<if test=\"transation.refundTime != null\"> "
			+ ",refund_time = #{transation.refundTime,jdbcType=TIMESTAMP} "
			+ "</if> "
			+ "<if test=\"transation.payStatus != null\"> "
			+ ",pay_status = #{transation.payStatus,jdbcType=INTEGER} "
			+ "</if> "
			+ "<if test=\"transation.refundStatus != null\"> "
			+ ",refund_status = #{transation.refundStatus,jdbcType=INTEGER} "
			+ "</if> "
			+ "<if test=\"transation.payFailReason != null\"> "
			+ ",pay_fail_reason = #{transation.payFailReason,jdbcType=VARCHAR} "
			+ "</if> "
			+ "<if test=\"transation.refundFailReason != null\"> "
			+ ",refund_fail_reason = #{transation.refundFailReason,jdbcType=VARCHAR} "
			+ "</if> "
			+ "<if test=\"transation.refundReason != null\"> "
			+ ",refund_reason = #{transation.refundReason,jdbcType=VARCHAR} "
			+ "</if> "
			+ "</trim>"
			+ "where id = #{transation.id,jdbcType=BIGINT}"
			+ " </script>")
	void updatePayTransation(@Param("transation") PayTransation transation);
	
	/**
	 * 
	 * 描述：查询交易记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:54:12
	 * @since 
	 * @param paymentId 
	 * @param tradeId 交易id
	 * @param payTransationId 主键id
	 * @return
	 */
	@Select("select "
			+ "id,"
			+ "user_id as userId,"
			+ "pay_id as payId,"
			+ "goods_id as goodsId,"
			+ "pay_price as payPrice,"
			+ "order_id as orderId,"
			+ "trade_id as tradeId,"
			+ "pay_type as payType,"
			+ "create_time as createTime,"
			+ "pay_time as payTime,"
			+ "refund_time as refundTime,"
			+ "pay_status as payStatus,"
			+ "refund_status as refundStatus,"
			+ "pay_fail_reason as payFailReason,"
			+ "refund_fail_reason as refundFailReason,"
			+ "refund_sum as refundSum,"
			+ "currency as currency,"
			+ "pay_source as paySource,"
			+ "payment_id as paymentId "
			+ "from pay_transation "
			+ "where payment_id = #{paymentId,jdbcType=VARCHAR} "
			+ "or trade_id = #{tradeId,jdbcType=VARCHAR} "
			+ "or id = #{id,jdbcType=VARCHAR}")
	PayTransation getPayTransationById(@Param("paymentId") String paymentId, @Param("tradeId") String tradeId,
                                       @Param("id") String id);

	/**
	 *
	 * 描述：根据userId，orderId查询tradeId
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:54:45
	 * @since
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@Select("select trade_id from pay_transation "
			+ "where user_id = #{userId,jdbcType=VARCHAR} and order_id = #{orderId,jdbcType=VARCHAR}")
	String getTradeId(@Param("userId") String userId, @Param("orderId") String orderId);

	/**
	 *
	 * 描述：查询交易记录的条数
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:55:23
	 * @since
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@Select("select count(1) from pay_transation "
			+ "where user_id = #{userId,jdbcType=VARCHAR} and order_id = #{orderId,jdbcType=VARCHAR}")
	int getCount(@Param("userId") String userId, @Param("orderId") String orderId);

	/**
	 *
	 * 描述：获取支付状态
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:55:39
	 * @since
	 * @param userId
	 * @param payId
	 * @return
	 */
	@Select("select pay_status from pay_transation "
			+ "where user_id = #{userId,jdbcType=VARCHAR} and pay_id = #{payId,jdbcType=VARCHAR}")
	Integer getAppPayStatus(@Param("userId") String userId, @Param("payId") String payId);

	/**
	 * 
	 * 描述：根据payId查询交易记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:55:52
	 * @since 
	 * @param payId
	 * @return
	 */
	@Select("select "
			+ "id,"
			+ "user_id as userId,"
			+ "pay_id as payId,"
			+ "goods_id as goodsId,"
			+ "pay_price as payPrice,"
			+ "order_id as orderId,"
			+ "trade_id as tradeId,"
			+ "pay_type as payType,"
			+ "create_time as createTime,"
			+ "pay_time as payTime,"
			+ "refund_time as refundTime,"
			+ "pay_status as payStatus,"
			+ "refund_status as refundStatus,"
			+ "pay_fail_reason as payFailReason,"
			+ "refund_fail_reason as refundFailReason,"
			+ "refund_sum as refundSum,"
			+ "currency as currency,"
			+ "pay_source as paySource,"
			+ "payment_id as paymentId "
			+ "from pay_transation "
			+ "where pay_id = #{payId,jdbcType=VARCHAR}")
	PayTransation getPayTransationByPayId(@Param("payId") String payId);

	/**
	 *
	 * 描述：根据payId查询交易记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:55:52
	 * @since
	 * @param payId
	 * @return
	 */
	@Select("select "
			+ "id,"
			+ "user_id as userId,"
			+ "pay_id as payId,"
			+ "goods_id as goodsId,"
			+ "pay_price as payPrice,"
			+ "order_id as orderId,"
			+ "trade_id as tradeId,"
			+ "pay_type as payType,"
			+ "create_time as createTime,"
			+ "pay_time as payTime,"
			+ "refund_time as refundTime,"
			+ "pay_status as payStatus,"
			+ "refund_status as refundStatus,"
			+ "pay_fail_reason as payFailReason,"
			+ "refund_fail_reason as refundFailReason,"
			+ "refund_sum as refundSum,"
			+ "currency as currency,"
			+ "pay_source as paySource,"
			+ "payment_id as paymentId," +
			" tenant_id as tenantId  "
			+ "from pay_transation "
			+ "where order_id = #{orderId,jdbcType=VARCHAR}" +
			" and user_id = #{userId,jdbcType=VARCHAR} " +
			" and tenant_id =#{tenantId}")
	PayTransation getPayTransationByOrderIdAndUserId(@Param("orderId") String orderId,@Param("userId")String userId,@Param("tenantId") Long tenantId);
	
	/**
	 * 
	 * 描述：获取支付交易信息
	 * @author 李帅
	 * @created 2018年5月22日 下午4:25:14
	 * @since 
	 * @param orderIDList
	 * @return
	 */
	@Select({ "<script>", 
		"select ",
		 "id,",
		 "user_id as userId,",
		 "pay_id as payId,",
		 "goods_id as goodsId,",
		 "pay_price as payPrice,",
		 "order_id as orderId,",
		 "trade_id as tradeId,",
		 "pay_type as payType,",
		 "create_time as createTime,",
		 "pay_time as payTime,",
		 "refund_time as refundTime,",
		 "pay_status as payStatus,",
		 "refund_status as refundStatus,",
		 "pay_fail_reason as payFailReason,",
		 "refund_fail_reason as refundFailReason,",
		 "refund_sum as refundSum,",
		 "currency as currency,",
		 "pay_source as paySource,",
		 "payment_id as paymentId ",
		 "from pay_transation ",
		 "where order_id in ", 
		"<foreach item='item' index='index' collection='orderIDList'",
		"open='(' separator=',' close=')'>", 
		"#{item}",
	"</foreach>",
	"</script>" })
	List<PayTransation> getPayTransation(@Param("orderIDList") List<String> orderIDList);

	@Update("<script> "
			+"update pay_transation "
			+ "<trim prefix=\"set\"  prefixOverrides=\",\" >"
			+ "<if test=\"transation.goodsId != null\"> "
			+ ",goods_id = #{transation.goodsId,jdbcType=BIGINT} "
			+ "</if> "
			+ "<if test=\"transation.payPrice != null\"> "
			+ ",pay_price = #{transation.payPrice} "
			+ "</if> "
			+ "<if test=\"transation.payType != null\"> "
			+ ",pay_type = #{transation.payType,jdbcType=INTEGER} "
			+ "</if> "
			+ "<if test=\"transation.payTime != null\"> "
			+ ",pay_time = #{transation.payTime,jdbcType=TIMESTAMP} "
			+ "</if> "
			+ "<if test=\"transation.payStatus != null\"> "
			+ ",pay_status = #{transation.payStatus,jdbcType=INTEGER} "
			+ "</if> "
			+ "<if test=\"transation.currency != null\"> "
			+ ",currency = #{transation.currency,jdbcType=VARCHAR} "
			+ "</if> "
			+ "<if test=\"transation.paySource != null\"> "
			+ ",pay_source = #{transation.paySource,jdbcType=INTEGER} "
			+ "</if> "
			+ "<if test=\"transation.paymentId != null\"> "
			+ ",payment_id = #{transation.paymentId,jdbcType=VARCHAR} "
			+ "</if> "
			+ "</trim>"
			+ "where id = #{transation.id,jdbcType=BIGINT}"
			+ " </script>")
	void updatePayTransationInfo(@Param("transation") PayTransation transation);

}