package com.iot.device.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.iot.device.vo.req.product.ProductServiceInfoReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ProductServiceInfoMapper {
    
    /**
     *@description 保存数据
     *@author wucheng
     *@params [req]
     *@create 2018/12/21 17:13
     *@return int
     */
    @Insert("insert into product_service_info(tenant_id, product_id, service_id, create_by, create_time, update_by, update_time, audit_status) values(" +
            " #{tenantId}, #{productId}, #{serviceId}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{auditStatus})")
    int saveProductServiceInfo(ProductServiceInfoReq req);

    /**
     *@description 根据tenantId, productId, serviceId修改其审核状态为null
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 17:16
     *@return int
     */
    @Update("update product_service_info set audit_status = null where tenant_id = #{tenantId} and product_id = #{productId} and service_id = #{serviceId}")
    int updateAuditStatusIsNull(@Param("tenantId") Long tenantId, @Param("productId") Long productId, @Param("serviceId") Long serviceId);

    /**
     *@description 根据tenantId, productId, serviceId修改其审核状态
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 17:16
     *@return int
     */
    @Update("update product_service_info set audit_status = #{auditStatus} where tenant_id = #{tenantId} and product_id = #{productId} and service_id = #{serviceId}")
    int updateAuditStatus(@Param("tenantId") Long tenantId, @Param("productId") Long productId, @Param("serviceId") Long serviceId, @Param("auditStatus") Integer auditStatus);

    /**
     *@description 根据tenantId, productId, serviceId删除产品关联的第三方信息
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 18:16
     *@return int
     */
    @Delete("<script> delete from product_service_info where tenant_id = #{tenantId} and product_id = #{productId} <if test='serviceId != null' > and service_id = #{serviceId} </if></script>")
    int deleteProductServiceInfo(@Param("tenantId") Long tenantId, @Param("productId") Long productId, @Param("serviceId") Long serviceId);

    /**
     *@description 根据tenantId, productId, serviceId获取产品关联的第三方服务信息
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/24 9:17
     *@return com.iot.device.vo.rsp.product.ProductServiceInfoResp
     */
    @Select({"select id, product_id as productId, service_id as serviceId, audit_status as auditStatus from product_service_info where tenant_id = #{tenantId} and product_id = #{productId} and service_id = #{serviceId}"})
    ProductServiceInfoResp getProductServiceInfo(@Param("tenantId") Long tenantId, @Param("productId") Long productId, @Param("serviceId") Long serviceId);

    @Select({"select id, product_id as productId, service_id as serviceId, tenant_id as tenantId, audit_status as auditStatus from product_service_info where tenant_id = #{tenantId} and product_id = #{productId}"})
    List<ProductServiceInfoResp> getServiceInfoByProductId(@Param("tenantId") Long tenantId, @Param("productId") Long productId);


    /**
     *@description 获取第三方审核信息
     *@author wucheng
     *@params [page, pageReq]
     *@create 2018/12/24 11:42
     *@return java.util.List<com.iot.device.vo.rsp.servicereview.ServiceAuditListResp>
     */
    @Select("<script>" +
            " Select p.id as productId , p.create_by as createBy, p.product_name as productName, p.tenant_id as tenantId, a.audit_status as auditStatus, p.model," +
            " d.name as deviceTypeName,  t.order_id as orderId, MAX(s.operate_time) as applyTime, a.service_id as serviceId" +
            " FROM product_service_info a  INNER JOIN product p on a.product_id = p.id " +
            " INNER JOIN device_type d on p.device_type_id = d.id " +
            " INNER JOIN service_buy_record t ON t.goods_id = a.service_id AND t.tenant_id = a.tenant_id AND t.service_id = p.id and t.pay_status != 5 " +
            " LEFT  JOIN service_review_record s ON s.service_id = a.service_id AND s.tenant_id = a.tenant_id AND s.product_id = a.product_id " +
            " <where>" +
            "  <if test='pageReq.type==0'>" +
            "    a.audit_status = 0"+
            "  </if>"+
            "  <if test='pageReq.type==1'>"+
            "   a.audit_status in (1,2)" +
            "  </if>"+
            "  <if test=\"pageReq.productParam != null and pageReq.productParam != ''\">"+
            "    and p.product_name like concat('%',#{pageReq.productParam},'%')"+
            "  </if>"+
            "  <if test=\"pageReq.tenantIds != null and pageReq.tenantIds.size != 0\">"+
                 " and p.tenant_id in "+
            "    <foreach collection='pageReq.tenantIds' open='(' close=')' separator=',' item='tenantId'>"+
            "      #{tenantId}"+
            "    </foreach>"+
            "   </if>"+
            "   <if test=\"pageReq.orderId != null and pageReq.orderId !=''\"> AND t.order_id like CONCAT('%', #{pageReq.orderId}, '%')</if>" +
            "   <if test='pageReq.createBy != null'> AND a.create_by = #{pageReq.createBy}</if>" +
            "   <if test='pageReq.serviceType != null'> AND a.service_id =#{pageReq.serviceType}</if>" +
            "  </where>" +
            "  GROUP BY orderId ORDER BY applyTime desc" +
            "</script>")
    List<ServiceAuditListResp> queryServiceAuditList(Page page, @Param("pageReq") ServiceAuditPageReq pageReq);
}
