package com.iot.device.mapper;

import com.iot.device.vo.req.ProductTimerReq;
import com.iot.device.vo.rsp.ProductTimerResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：产品定时配置
 * 创建人： maochengyuan
 * 创建时间：2018/11/23 10:42
 * 修改人： maochengyuan
 * 修改时间：2018/11/23 10:42
 * 修改描述：
 */
@Mapper
public interface ProductTimerMapper {

	/**
	 * 描述：保存产品定时配置
	 * @author maochengyuan
	 * @created 2018/11/23 10:35
	 * @param timers 配置信息
	 * @return int
	 */
	@Insert({ "<script>",
			"insert into product_to_timer (tenant_id, product_id, timer_type, create_by, create_time, update_by, update_time) values ",
			"<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">",
			"(#{item.tenantId,jdbcType=BIGINT}" +
					", #{item.productId,jdbcType=BIGINT}" +
					",#{item.timerType,jdbcType=VARCHAR}" +
					",#{item.createBy,jdbcType=BIGINT}" +
					",#{item.createTime,jdbcType=TIMESTAMP}" +
					",#{item.updateBy,jdbcType=BIGINT}" +
					",#{item.updateTime,jdbcType=TIMESTAMP})",
			"</foreach>",
			"</script>" })
	int addProductTimer(List<ProductTimerReq> timers);

	/**
	 * 描述：删除产品定时配置
	 * @author maochengyuan
	 * @created 2018/11/23 10:08
	 * @param productId 产品ID
	 * @return int
	 */
	@Delete("delete from product_to_timer where product_id = #{productId,jdbcType=BIGINT}")
	int delProductTimer(@Param("productId") Long productId);

	/**
	 * 描述：查询产品定时配置-单个
	 * @author maochengyuan
	 * @created 2018/11/23 10:13
	 * @param productId 产品ID
	 * @return java.util.List<com.iot.device.vo.rsp.ProductTimerResp>
	 */
	@Select("select timer_type as timerType, product_id as productId from product_to_timer where product_id = #{productId,jdbcType=BIGINT}")
	List<ProductTimerResp> getProductTimer(@Param("productId") Long productId);

	/**
	 * 描述：查询产品定时配置-批量
	 * @author maochengyuan
	 * @created 2018/11/23 10:13
	 * @param productIds 产品ID
	 * @return java.util.List<com.iot.device.vo.rsp.ProductTimerResp>
	 */
	@Select({"<script>" +
			"select id, timer_type as timerType, product_id as productId from product_to_timer where product_id in " +
			"<foreach collection='productIds' item='productId' open='(' close=')' separator=','>",
			" #{productId,jdbcType=BIGINT}" ,
			"</foreach>",
			"</script>"})
	List<ProductTimerResp> getProductTimers(@Param("productIds") List<Long> productIds);

}