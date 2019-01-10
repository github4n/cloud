package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.device.model.ServiceBuyRecord;
import com.iot.device.vo.req.service.ServiceBuyRecordPageReq;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.req.servicebuyrecordreq.ServiceBuyRecordReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录sql语句
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 14:57
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 14:57
 * 修改描述：
 */
@Mapper
public interface ServiceBuyRecordMapper extends BaseMapper<ServiceBuyRecord>{

    /**
     * 描述：查询虚拟服务购买记录
     * @author maochengyuan
     * @created 2018/9/13 17:49
     * @param serviceId 虚拟服务ID
     * @param goodsTypeId 商品类别ID
     * @param tenantId 租户ID
     * @update wucheng
     * @update date 2018-11-13
     * @update description: 新增 pay_status != 5 条件 去掉已退款的数据
     * @return java.util.List<com.iot.device.vo.rsp.ServiceBuyRecordResp>
     */
    @Select({"select service_id as serviceId," +
            " goods_id as goodsId," +
            " order_id as orderId," +
            " pay_status as payStatus" +
            " from service_buy_record" +
            " where goods_type_id = #{goodsTypeId}" +
            " and service_id = #{serviceId}" +
            " and tenant_id = #{tenantId}" +
            " and pay_status != 5"})
    List<ServiceBuyRecordResp> getServiceBuyRecord(@Param("serviceId") Long serviceId, @Param("goodsTypeId") Integer goodsTypeId, @Param("tenantId") Long tenantId);

    /**
     * 描述：查询虚拟服务购买记录详情
     * @author maochengyuan
     * @created 2018/9/13 17:49
     * @param serviceId 虚拟服务ID
     * @param goodsId 商品ID
     * @param tenantId 租户ID
     * @return com.iot.device.vo.rsp.ServiceBuyRecordResp
     */
    @Select({"select order_id as orderId," +
            " service_id as serviceId," +
            " pay_status as payStatus," +
            " goods_id as goodsId," +
            " goods_num as goodsNum," +
            " add_demand_desc as addDemandDesc," +
            " create_time as createTime " +
            " from service_buy_record" +
            " where goods_id = #{goodsId}" +
            " and service_id = #{serviceId}" +
            " and tenant_id = #{tenantId}" +
            " and pay_status != 5 "})
    ServiceBuyRecordResp getServiceBuyRecordDetail(@Param("serviceId") Long serviceId, @Param("goodsId") Long goodsId, @Param("tenantId") Long tenantId);


    /**
     * @despriction：校验是否创建过
     * @author  yeshiyuan
     * @created 2018/9/13 16:29
     * @return
     */
    @Select("select count(1) from service_buy_record where tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and goods_id = #{goodsId,jdbcType=BIGINT} and service_id = #{serviceId,jdbcType=BIGINT}" +
            " and pay_status !=5")
    int checkIsCreate(@Param("tenantId") Long tenantId, @Param("serviceId") Long serviceId, @Param("goodsId") Long goodsId);

    /**
     * @despriction：修改支付状态
     * @author  yeshiyuan
     * @created 2018/9/13 17:00
     * @return
     */
    @Update("<script>" +
            " update service_buy_record set pay_status = #{req.newPayStatus,jdbcType=INTEGER}, update_time = #{updateTime} " +
            " where tenant_id = #{req.tenantId, jdbcType=BIGINT} and order_id = #{req.orderId} " +
            " <if test='req.oldPayStatus!=null'>" +
            " and pay_status = #{req.oldPayStatus,jdbcType=INTEGER}" +
            " </if>" +
            "</script>")
    int updatePayStatus(@Param("req") UpdateServicePayStatusReq req, @Param("updateTime") Date updateTime);

    /**
      * @despriction：根据订单id查找
      * @author  yeshiyuan
      * @created 2018/9/14 11:33
      * @return
      */
    @Select({"select order_id as orderId," +
            " user_id as userId," +
            " service_id as serviceId," +
            " pay_status as payStatus," +
            " goods_id as goodsId," +
            " goods_num as goodsNum," +
            " add_demand_desc as addDemandDesc" +
            " from service_buy_record" +
            " where tenant_id = #{tenantId} " +
            " and order_id = #{orderId}"})
    ServiceBuyRecordResp getServiceBuyRecordByOrderId(@Param("orderId") String orderId, @Param("tenantId") Long tenantId);
    
    /**
     * 
     * 描述：根据服务ID和商品类别id获取订单号ID
     * @author 李帅
     * @created 2018年10月29日 下午3:42:23
     * @since 
     * @param serviceId
     * @param goodsTypeId
     * @return
     */
   @Select({"select s.order_id from service_buy_record s where s.goods_id = #{serviceId} and s.service_id = #{productId} and s.goods_type_id = #{goodsTypeId} and pay_status !=5 LIMIT 1"})
   String getOrderIdByServiceIdAndGoodsTypeId(@Param("serviceId") Long serviceId, @Param("productId") Long productId, @Param("goodsTypeId") Integer goodsTypeId);


    /**
     * @description: 根据goodsIds 获取虚拟服务购买支付成功、退款中、退款成功、退款失败
     * @author wucheng
     * @param req
     * @create 2018-11-13 20:10:33
     * @return  List<ServiceBuyRecordResp>
     */
    @Select({"<script>select user_id as userId, " +
            " order_id as orderId," +
            " service_id as serviceId," +
            " tenant_id as tenantId," +
            " pay_status as payStatus," +
            " goods_id as goodsId," +
            " goods_num as goodsNum," +
            " add_demand_desc as addDemandDesc," +
            " create_time as createTime" +
            " from service_buy_record" +
            " where " +
            "  goods_id  in "+
            " <foreach item='goodsId' index='index' collection='req.goodsIds' open='(' separator=',' close=')'>"+
            "   #{goodsId} "+
            " </foreach> "+
            " <if test='req.payStatus != null'> and pay_status = #{req.payStatus}</if>"+
            " <if test='req.payStatus == null'> and pay_status != 1</if>"+
            " <if test='req.userId != null'> and user_id = #{req.userId}</if>"+
            " <if test='req.orderId != null'> and order_id like CONCAT('%',#{req.orderId},'%') </if>"+
            " <if test=\"req.tenantIds != null and req.tenantIds.size > 0 \"> and tenant_id in " +
            "       <foreach item='tenantId' index='index' collection='req.tenantIds' open='(' separator=',' close=')'>" +
            "          #{tenantId}" +
            "       </foreach>" +
            " </if>"+
            " order by create_time desc" +
            "</script>"})
    List<ServiceBuyRecordResp> getServiceBuyRecordByGoodsIds(@Param("page") Page<ServiceBuyRecordReq> page, @Param("req") ServiceBuyRecordReq req);

    @Select("<script>" +
            " select order_id as orderId, " +
            " tenant_id as tenantId," +
            " service_id as serviceId," +
            " pay_status as payStatus," +
            " goods_id as goodsId," +
            " goods_num as goodsNum," +
            " add_demand_desc as addDemandDesc," +
            " create_time as createTime" +
            " from service_buy_record " +
            "<trim prefix=\"where\" prefixOverrides=\"and\">" +
            " and goods_type_id = #{req.goodsTypeId} " +
            " <choose>" +
            "  <when test=\"req.payStatus == null\">" +
            "   and pay_status in (2,4,5,6)" +
            "  </when>" +
            "   <otherwise>" +
            "   and pay_status = #{req.payStatus}" +
            "  </otherwise>" +
            " </choose>" +
            " <if test=\"req.serviceIds!=null and req.serviceIds.size>0\">" +
            "  and service_id in " +
            "  <foreach collection='req.serviceIds' item='serviceId' open='(' close=')' separator=','>" +
            "     #{serviceId}" +
            "  </foreach>" +
            " </if>" +
            " <if test=\"req.orderId!=null and req.orderId!=''\">" +
            "   and order_id like concat('%', #{req.orderId} ,'%')" +
            " </if>" +
            " <if test=\"req.tenantIds!=null and req.tenantIds.size>0\">" +
            "  and tenant_id in " +
            "  <foreach collection='req.tenantIds' item='tenantId' open='(' close=')' separator=','>" +
            "     #{tenantId}" +
            "  </foreach>" +
            " </if>" +
            "</trim>" +
            " order by create_time desc " +
            "</script>")
    List<ServiceBuyRecordResp> pageQuery(Page<ServiceBuyRecordResp> page, @Param("req")ServiceBuyRecordPageReq pageReq);


}
