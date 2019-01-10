package com.iot.payment.dao;

import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.vo.order.resp.VideoPlanTypeResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单商品操作sql
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:24
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:24
 * 修改描述：
 */
@Mapper
public interface OrderGoodsMapper {

    @Insert("<script>" +
            "insert into order_goods(id,order_id,tenant_id,num,goods_id,goods_name," +
            "goods_standard,goods_standard_unit,goods_price,goods_currency) values " +
            " (" +
            "   #{orderGoods.id}," +
            "   #{orderGoods.orderId}," +
            "   #{orderGoods.tenantId}," +
            "   #{orderGoods.num}," +
            "   #{orderGoods.goodsId}," +
            "   #{orderGoods.goodsName}," +
            "   #{orderGoods.goodsStandard}," +
            "   #{orderGoods.goodsStandardUnit}," +
            "   #{orderGoods.goodsPrice}," +
            "   #{orderGoods.goodsCurrency} " +
            " )" +
            "</script>")
    int add(@Param("orderGoods") OrderGoods orderGoods);

    @Insert("<script>" +
            "insert into order_goods("
//            + "id,"
            + "order_id,tenant_id,num,goods_id,goods_name," +
            "goods_standard,goods_standard_unit,goods_price,goods_currency) values " +
            " <foreach collection='list' separator=',' item='item'>" +
            " (" +
//            "   #{item.id}," +
            "   #{item.orderId}," +
            "   #{item.tenantId}," +
            "   #{item.num}," +
            "   #{item.goodsId}," +
            "   #{item.goodsName}," +
            "   #{item.goodsStandard}," +
            "   #{item.goodsStandardUnit}," +
            "   #{item.goodsPrice}," +
            "   #{item.goodsCurrency} " +
            " )" +
            "</foreach>" +
            "</script>")
    int batchAdd(@Param("list") List<OrderGoods> list);

    /**
     * 描述：修改订单UUID数量
     * @author maochengyuan
     * @created 2018/7/3 20:10
     * @param orderId 订单id
     * @param createNum  UUID数量
     * @return void
     */
    @Update("update" +
            " order_goods" +
            " set num = #{createNum,jdbcType=BIGINT}" +
            " where order_id = #{orderId,jdbcType=VARCHAR}")
    void editOrderCreateNum(@Param("orderId") String orderId, @Param("createNum")Integer createNum);

    /**
     * 描述：依据订单id获取订单商品详情
     * @author maochengyuan
     * @created 2018/7/3 20:10
     * @param tenantId 租户id
     * @param orderId 订单id
     * @return com.iot.payment.entity.order.OrderGoods
     */
    @Select("select id," +
            " order_id as orderId," +
            " tenant_id as tenantId," +
            " num," +
            " goods_id as goodsId," +
            " goods_name as goodsName," +
            " goods_standard as goodsStandard," +
            " goods_standard_unit as goodsStandardUnit," +
            " goods_price as goodsPrice," +
            " goods_currency as goodsCurrency" +
            " from order_goods" +
            " where tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and order_id = #{orderId,jdbcType=VARCHAR}")
    List<OrderGoods> getOrderGoodsByOrderId(@Param("tenantId") Long tenantId, @Param("orderId") String orderId);

    /**
     * @despriction：获取订单下的商品id
     * @author  yeshiyuan
     * @created 2018/7/4 16:09
     * @param orderId 订单id
     * @param 租户id 租户id
     * @return
     */
    @Select("select id from order_goods where tenant_id=#{tenantId} and order_id=#{orderId}")
    List<Long> getOrderGoodsIds(@Param("orderId") String orderId, @Param("tenantId") Long tenantId);

    /**
     * @despriction：查询视频计划类型、容量
     * @author  yeshiyuan
     * @created 2018/7/17 15:39
     * @param orderGoodsId 订单商品id
     * @return
     */
    @Select("select o.goods_standard as eventOrFulltimeAmount," +
            " o.goods_standard_unit as standardUnit," +
            " g.type_id as packageType " +
            " from order_goods o left join goods_info g on o.goods_id=g.id" +
            " where o.goods_id = #{oriGoodsId,jdbcType=BIGINT} and o.order_id = #{orderId}")
    VideoPlanTypeResp getVideoPlanType(@Param("orderId") String orderId,@Param("oriGoodsId") Long orderGoodsId);
}
