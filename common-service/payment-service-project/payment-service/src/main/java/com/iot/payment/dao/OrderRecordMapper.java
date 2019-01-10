package com.iot.payment.dao;

import com.iot.payment.entity.order.OrderRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单记录操作sql
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:23
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:23
 * 修改描述：
 */
@Mapper
public interface OrderRecordMapper {

    @Insert("insert into order_record values(" +
            " #{orderRecord.id}," +
            " #{orderRecord.tenantId}," +
            " #{orderRecord.userId}," +
            " #{orderRecord.totalPrice}," +
            " #{orderRecord.currency}," +
            " #{orderRecord.orderType}," +
            " #{orderRecord.orderStatus}," +
            " #{orderRecord.createTime}," +
            " #{orderRecord.updateTime}" +
            ")")
    int add(@Param("orderRecord") OrderRecord orderRecord);

    /**
     * 描述：依据订单id获取订单信息
     * @author maochengyuan
     * @created 2018/7/3 20:22
     * @param tenantId 租户id
     * @param orderId 订单id
     * @return com.iot.payment.entity.order.OrderRecord
     */
    @Select("select id," +
            " tenant_id as tenantId," +
            " user_id as userId," +
            " total_price as totalPrice," +
            " currency, order_type as orderType," +
            " order_status as orderStatus," +
            " create_time as createTime," +
            " update_time as updateTime" +
            " from order_record" +
            " where id = #{orderId,jdbcType=VARCHAR}" +
            " and tenant_id = #{tenantId,jdbcType=BIGINT}")
    OrderRecord getOrderRecordByOrderId(@Param("tenantId") Long tenantId, @Param("orderId") String orderId);

    /**
     * 
     * 描述：依据订单id获取订单信息
     * @author 李帅
     * @created 2018年11月14日 上午9:52:28
     * @since 
     * @param orderIds
     * @return
     */
    @Select({"<script>",
    	"select id," +
            " tenant_id as tenantId," +
            " user_id as userId," +
            " total_price as totalPrice," +
            " currency, order_type as orderType," +
            " order_status as orderStatus," +
            " create_time as createTime," +
            " update_time as updateTime" +
            " from order_record" +
            " where 1= 1 and id in " +
            "<foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'>",
            "#{orderId}",
            "</foreach>",
            "</script>"
    })
    List<OrderRecord> getOrderRecordByOrderIds(@Param("orderIds") List<String> orderIds);
    
    /**
     * 描述：修改订单总价
     * @author maochengyuan
     * @created 2018/7/3 20:22
     * @param orderId 订单id
     * @param totalPirce 订单总价
     * @return void
     */
    @Update("update" +
            " order_record" +
            " set total_price = #{totalPirce,jdbcType=NUMERIC},update_time=#{updateTime}" +
            " where id = #{orderId,jdbcType=VARCHAR}")
    int editOrderTotalPrice(@Param("orderId") String orderId, @Param("totalPirce") BigDecimal totalPirce,@Param("updateTime") Date updateTime);

    /**
     * @despriction：修改订单状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param 租户id 租户id
     * @param orderStatus 订单状态
     * @return
     */
    @Update("<script>" +
            "update order_record set order_status=#{orderStatus},update_time=#{updateTime} " +
            "where id = #{orderId,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}" +
            "<if test='oldOrderStatus!=null'> " +
            "   and order_status=#{oldOrderStatus,jdbcType=TINYINT} " +
            "</if>" +
            "</script>")
    int updateOrderRecordStatus(@Param("orderId") String orderId,@Param("tenantId") Long tenantId,@Param("orderStatus") Integer orderStatus,
                                @Param("updateTime") Date updateTime,@Param("oldOrderStatus") Integer oldOrderStatus);
}
