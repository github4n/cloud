package com.iot.device.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.device.dto.NetworkTypeDto;
import com.iot.device.entity.NetworkType;

@Mapper
public interface TechnicalRelateMapper {


	@Select("<script>"
			+"SELECT " + 
			"	id AS id, " + 
			"	network_name AS networkName, " +
			"	type_code AS typeCode, " +
			"	description AS description " + 
			"FROM " + 
			"	network_type " + 
			"WHERE " + 
			"	is_deleted = 'valid' " + 
			"AND id = #{networkTypeId}"
			+ "</script>")
	public NetworkTypeDto getNetworkInfo(@Param("networkTypeId") Long networkTypeId);

	@Select("<script>"
			+"SELECT " + 
			"	technical_scheme_id " + 
			"FROM " + 
			"	technical_network_relate " + 
			"WHERE " + 
			"	network_type_id = #{networkTypeId}"
			+ "</script>")
	public List<Long> getTechnicalSchemeIdByNetworkTypeId(@Param("networkTypeId") Long networkTypeId);
	
//	@Insert({ "<script>", 
//		"insert into network_type " + 
//		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"networkName != null\" > network_name, </if>" + 
//		"      <if test=\"description != null\" > description, </if>" + 
//		"      <if test=\"createBy != null\" > create_by, </if>" + 
//		" create_time," +
//		" is_deleted," +
//		"    </trim>" +
//		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"networkName != null\" > #{networkName,jdbcType=VARCHAR}, </if>" + 
//		"      <if test=\"description != null\" > #{description,jdbcType=VARCHAR}, </if>" + 
//		"      <if test=\"createBy != null\" > #{createBy,jdbcType=BIGINT}, </if>" + 
//		" now(), " +
//		" 'valid', " +
//		"    </trim>", 
//		"</script>" })
	@Insert("INSERT INTO network_type (network_name, description, create_by, create_time, is_deleted, type_code)"
			+ " VALUES (#{networkName,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, #{createBy,jdbcType=BIGINT}, now(), 'valid', #{typeCode,jdbcType=VARCHAR})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	public void saveNetworkType(NetworkType networkType);
	
	@Update({ "<script>", 
		"update network_type" + 
		" set " + 
		" <if test=\"networkName != null\"> network_name=#{networkName,jdbcType=VARCHAR},</if>" +
		" <if test=\"description != null\"> description=#{description,jdbcType=VARCHAR},</if>" + 
		" <if test=\"typeCode != null\"> type_code=#{typeCode,jdbcType=VARCHAR},</if>" +
		"<if test=\"createBy != null\"> update_by=#{createBy,jdbcType=BIGINT},</if>" +
		" update_time = now()" +
		" where id = #{id,jdbcType=BIGINT} and is_deleted = 'valid' ",
		"</script>" })
	public void updateNetworkType(NetworkType networkType);
	
	@Delete({
        "delete from technical_network_relate",
        "where network_type_id = #{networkTypeId,jdbcType=BIGINT}"
    })
	void deleteTechnicalNetworkRelateByByNetworkTypeId(@Param("networkTypeId") Long networkTypeId);
	
	@Insert({ "<script>",
		"INSERT INTO technical_network_relate (" +
				"			  network_type_id," +
				"			  technical_scheme_id," +
				"			  create_by," +
				"			  create_time" +
				"		) SELECT t.networkTypeId, t.technicalSchemeId, t.createBy, t.createTime FROM(" +
				"         <foreach collection=\"technicalSchemeIds\" index=\"\" item=\"technicalSchemeIds\" separator=\"union all\">" +
				"             select" +
				"               #{networkTypeId} AS networkTypeId," +
				"               #{technicalSchemeIds} AS technicalSchemeId," +
				"               #{createBy} AS createBy," +
				"               NOW() AS createTime" +
				"             from dual" +
				"         </foreach>" +
				"         ) t",
		"</script>" })
	public void saveTechnicalNetworkRelate(Map<String, Object> technicalNetworkRelateParam);
}