package com.iot.device.mapper;

import com.iot.device.model.ServiceReviewRecord;
import com.iot.device.vo.req.servicereview.GetServiceReviewReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ServiceReviewMapper {


	/**
	 * 
	 * 描述：保存语音服务审核记录
	 * @author 李帅
	 * @created 2018年10月25日 下午5:12:12
	 * @since 
	 * @param serviceReviewRecord
	 */
	@Insert({ "<script>", 
		"insert into service_review_record " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
		"      <if test=\"tenantId != null\" > tenant_id, </if>" + 
		"      <if test=\"productId != null\" > product_id, </if>" + 
		"      <if test=\"serviceId != null\" > service_id, </if>" + 
		"      <if test=\"operateDesc != null\" > operate_desc, </if>" + 
		"      <if test=\"createBy != null\" > create_by, </if>" + 
		"      <if test=\"processStatus != null\" > process_status, </if>" + 
		" operate_time," +
		" create_time," +
		" is_deleted," +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"productId != null\" > #{productId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"serviceId != null\" > #{serviceId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"operateDesc != null\" > #{operateDesc,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"createBy != null\" > #{createBy,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"processStatus != null\" > #{processStatus,jdbcType=BIGINT}, </if>" + 
		" now(), " +
		" now(), " +
		" 'valid', " +
		"    </trim>",
		"</script>" })
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	public void saveServiceReviewRecord(ServiceReviewRecord serviceReviewRecord);

	/**
	 * 
	 * 描述：获取语音服务审核记录
	 * @author 李帅
	 * @created 2018年10月25日 下午5:31:24
	 * @since 
	 * @param getServiceReviewReq
	 * @return
	 */
	@Select({ "<script>", 
		"select  id            AS id," + 
		"			tenant_id          AS tenantId," + 
		"			product_id            AS productId," + 
		"			service_id          AS serviceId," + 
		"			operate_time          AS operateTime," + 
		"			operate_desc        AS operateDesc," + 
		"			create_by     AS createBy," + 
		"			create_time        AS createTime," + 
		"			update_by        AS updateBy," + 
		"			update_time        AS updateTime," + 
		"			is_deleted     AS isDeleted," + 
		"			process_status         AS processStatus" + 
		"    from service_review_record" + 
		"    where  is_deleted != 'invalid'" +
		"    <if test=\"tenantId != null\" >and tenant_id = #{tenantId,jdbcType=BIGINT}</if>" + 
		"    <if test=\"serviceId != null\" >and service_id = #{serviceId,jdbcType=BIGINT}</if>" + 
		"    <if test=\"productId != null\" >and product_id = #{productId,jdbcType=BIGINT}</if>" + 
		" order by operateTime desc", 
		"</script>" })
    List<ServiceReviewRecord> getServiceReviewRecord(GetServiceReviewReq getServiceReviewReq);
	
	
	@Select("<script>" +
            "select product_id as productId, min(operate_time) as operateTime" +
            " from service_review_record where  is_deleted != 'invalid' and product_id in " +
            "<foreach collection='productIds' open='(' close=')' item='id' separator=','>" +
            " #{id,jdbcType=BIGINT}" +
            "</foreach>" +
            " group by product_id " +
            "</script>")
    List<ServiceReviewRecord> queryApplyTimeByProductIds(@Param("productIds") List<Long> productIds);
	
	@Select("<script>" +
            "select product_id as productId, create_by as createBy,max(operate_time) as operateTime" +
            " from service_review_record where is_deleted != 'invalid' and product_id in " +
            "<foreach collection='productIds' open='(' close=')' item='id' separator=','>" +
            " #{id,jdbcType=BIGINT}" +
            "</foreach>" +
            " group by product_id " +
            "</script>")
    List<ServiceReviewRecord> queryUserIdByProductIds(@Param("productIds") List<Long> productIds);


	@Select("select tenant_id from service_review_record where id = #{id}")
	Long getTenantById(@Param("id") Long id);

	/**
	 * @despriction：记录置为失效
	 * @author  wucheng
	 * @created 2018/11/14 13:46
	 * @return
	 */
	@Update("update service_review_record set is_deleted = 'invalid', update_time = now() where product_id = #{productId} and tenant_id = #{tenantId} and service_id = #{serviceId}")
	void invalidRecord(@Param("productId") Long productId, @Param("tenantId") Long tenantId, @Param("serviceId") Long serviceId);
}