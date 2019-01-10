package com.iot.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.iot.payment.entity.order.OrderGoodsExtendService;

/**
 * 项目名称：cloud
 * 功能描述：订单商品-附加服务操作sql
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:24
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:24
 * 修改描述：
 */
@Mapper
public interface OrderGoodsExtendServiceMapper {

    @Insert("<script>" +
            "insert into order_goods_extend_service(id,order_id,tenant_id,order_goods_id,goods_ex_id," +
            " goods_ex_name,goods_ex_price,goods_ex_currency) values " +
            " (" +
            "   #{goodsExService.id}," +
            "   #{goodsExService.orderId}," +
            "   #{goodsExService.tenantId}," +
            "   #{goodsExService.orderGoodsId}," +
            "   #{goodsExService.goodsExId}," +
            "   #{goodsExService.goodsExName}," +
            "   #{goodsExService.goodsExPrice}," +
            "   #{goodsExService.goodsExCurrency}" +
            " )" +
            "</script>")
    int add(@Param("goodsExService") OrderGoodsExtendService goodsExService);

    @Insert("<script>" +
            "insert into order_goods_extend_service("
//            + "id,"
            + "order_id,tenant_id,order_goods_id,goods_ex_id," +
            " goods_ex_name,goods_ex_price,goods_ex_currency) values " +
            " <foreach collection='list' separator=',' item='item'>" +
            " (" +
//            "   #{item.id}," +
            "   #{item.orderId}," +
            "   #{item.tenantId}," +
            "   #{item.orderGoodsId}," +
            "   #{item.goodsExId}," +
            "   #{item.goodsExName}," +
            "   #{item.goodsExPrice}," +
            "   #{item.goodsExCurrency}" +
            " )" +
            "</foreach>" +
            "</script>")
    int batchAdd(@Param("list") List<OrderGoodsExtendService> list);

    /**
      * @despriction：查找商品的附加服务
      * @author  yeshiyuan
      * @created 2018/7/5 11:28
      * @param tenantId 租户id
      * @param orderGoodsIds 商品id
      * @return
      */
    @Select("<script>" +
            " select id," +
            "   order_id as orderId," +
            "   tenant_id as tenantId," +
            "   order_goods_id as orderGoodsId," +
            "   goods_ex_id as goodsExId," +
            "   goods_ex_name as goodsExName," +
            "   goods_ex_price as goodsExPrice," +
            "   goods_ex_currency as goodsExCurrency" +
            " from order_goods_extend_service where tenant_id=#{tenantId} and" +
            "   order_goods_id in " +
            "   <foreach collection='goodsIds' item='goodsId' open='(' close=')' separator=','>" +
            "       #{goodsId}" +
            "   </foreach>" +
            "</script>")
    List<OrderGoodsExtendService> findByGoodsIds(@Param("tenantId") Long tenantId, @Param("goodsIds") List<Long> orderGoodsIds);
}
