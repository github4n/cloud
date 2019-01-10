package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ProductReviewRecord;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：产品审核记录mapper
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 13:45
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 13:45
 * 修改描述：
 */
@Mapper
public interface ProductReviewRecordMapper extends BaseMapper<ProductReviewRecord>{

    @Select("<script>" +
            "select product_id as productId, create_by as createBy,max(operate_time) as operateTime" +
            " from product_review_record where product_id in " +
            "<foreach collection='productIds' open='(' close=')' item='id' separator=','>" +
            " #{id,jdbcType=BIGINT}" +
            "</foreach>" +
            " group by product_id " +
            " order by operate_time desc" +
            "</script>")
    List<ProductReviewRecord> queryUserIdByProductIds(@Param("productIds") List<Long> productIds);

    @Select("<script>" +
            "select product_id as productId, min(operate_time) as operateTime" +
            " from product_review_record where product_id in " +
            "<foreach collection='productIds' open='(' close=')' item='id' separator=','>" +
            " #{id,jdbcType=BIGINT}" +
            "</foreach>" +
            " group by product_id " +
            "</script>")
    List<ProductReviewRecord> queryApplyTimeByProductIds(@Param("productIds") List<Long> productIds);

    /**
     * 描述：依据appId获取审核记录
     * @author maochengyuan
     * @created 2018/10/23 14:58
     * @param appId
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Select(" select" +
            " create_by as createBy," +
            " process_status as processStatus," +
            " operate_desc as operateDesc," +
            " operate_time as operateTime" +
            " from" +
            " product_review_record" +
            " where product_id = #{productId} order by operateTime desc")
    List<ProductReviewRecordResp> getReviewRecord(@Param("productId") Long productId);

    @Select("select tenant_id from product_review_record where id = #{id}")
    Long getTenantById(@Param("id") Long id);
}
