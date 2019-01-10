package com.iot.tenant.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.tenant.domain.Tenant;
import com.iot.tenant.domain.TenantAddresManage;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：租户地址服务
 * 功能描述：租户地址服务
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:32:16
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:32:16
 */
@Mapper
public interface TenantAddresManageMapper extends BaseMapper<Tenant> {

	/**
	 * 
	 * 描述：保存租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:23:26
	 * @since 
	 * @param req
	 */
	@Insert({ "<script>", 
		"insert into tenant_addres_manage " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
		"      <if test=\"tenantId != null\" > tenant_id, </if>" + 
		"      <if test=\"country != null\" > country, </if>" + 
		"      <if test=\"state != null\" > state, </if>" + 
		"      <if test=\"city != null\" > city, </if>" + 
		"      <if test=\"addres != null\" > addres, </if>" + 
		"      <if test=\"remark != null\" > remark, </if>" + 
		"      <if test=\"contacterName != null\" > contacter_name, </if>" + 
		"      <if test=\"contacterTel != null\" > contacter_tel, </if>" + 
		"      <if test=\"zipCode != null\" > zip_code, </if>" + 
		"      <if test=\"createBy != null\" > create_by, </if>" + 
		" create_time," +
		" is_deleted," +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"country != null\" > #{country,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"state != null\" > #{state,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"city != null\" > #{city,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"addres != null\" > #{addres,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"remark != null\" > #{remark,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"contacterName != null\" > #{contacterName,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"contacterTel != null\" > #{contacterTel,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"zipCode != null\" > #{zipCode,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"createBy != null\" > #{createBy,jdbcType=VARCHAR}, </if>" + 
		" now(), " +
		" 'valid', " +
		"    </trim>",
		"</script>" })
	void save(TenantAddresManage TenantAddresManage);
	
	/**
	 * 
	 * 描述：更新租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:24:08
	 * @since 
	 * @param req
	 */
	@Update({ "<script>", 
		"update tenant_addres_manage" + 
		" set " + 
		" <if test=\"tenantId != null\"> tenant_id=#{tenantId,jdbcType=BIGINT},</if>" +
		" <if test=\"country != null\"> country=#{country,jdbcType=VARCHAR},</if>" + 
		" <if test=\"state != null\"> state=#{state,jdbcType=VARCHAR},</if>" + 
		" <if test=\"city != null\"> city=#{city,jdbcType=VARCHAR},</if>" + 
		" <if test=\"addres != null\"> addres=#{addres,jdbcType=VARCHAR},</if>" + 
		" <if test=\"remark != null\"> remark=#{remark,jdbcType=VARCHAR},</if>" + 
		" <if test=\"contacterName != null\"> contacter_name=#{contacterName,jdbcType=VARCHAR},</if>" + 
		" <if test=\"contacterTel != null\"> contacter_tel=#{contacterTel,jdbcType=VARCHAR},</if>" + 
		" <if test=\"zipCode != null\"> zip_code=#{zipCode,jdbcType=VARCHAR},</if>" + 
		" <if test=\"updateBy != null\"> update_by=#{updateBy,jdbcType=VARCHAR},</if>" + 
		"  update_time = now()" +
		" where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT} and is_deleted = 'valid' ",
		"</script>" })
	void update(TenantAddresManage TenantAddresManage);
	
	/**
	 * 
	 * 描述：删除租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:24:45
	 * @since 
	 * @param id
	 */
	@Update({ "<script>", 
		"update tenant_addres_manage" + 
		" set " + 
		"  is_deleted = 'invalid'" +
		" where id = #{id,jdbcType=BIGINT} and is_deleted = 'valid' and tenant_id = #{tenantId,jdbcType=BIGINT} ",
		"</script>" })
	void delete(@Param("id") Long id, @Param("tenantId") Long tenantId);
	
	/**
	 * 
	 * 描述：通过租户ID获取租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:25:20
	 * @since 
	 * @param tenantId
	 * @return
	 */
	@Select({ "<script>", 
		"SELECT " + 
		"	id AS id, " + 
		"	tenant_id AS tenantId, " + 
		"	country AS country, " + 
		"	state AS state, " + 
		"	city AS city, " + 
		"	addres AS addres, " + 
		"	remark AS remark, " + 
		"	contacter_name AS contacterName, " + 
		"	contacter_tel AS contacterTel, " + 
		"	zip_code AS zipCode, " + 
		"	create_by AS createBy, " + 
		"	create_time AS createTime, " + 
		"	update_by AS updateBy, " + 
		"	update_time AS updateTime, " + 
		"	is_deleted AS isDeleted " + 
		"FROM " + 
		"	tenant_addres_manage " + 
		"where tenant_id = #{tenantId} and is_deleted = 'valid' ", 
		"</script>" })
	List<TenantAddresManage> getAddresByTenantId(Long tenantId);
}
