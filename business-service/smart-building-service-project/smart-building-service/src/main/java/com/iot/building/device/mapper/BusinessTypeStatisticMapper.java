package com.iot.building.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.building.device.vo.BusinessTypeStatistic;

/**
 * <p>
 * 设备-业务类型表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-05-09
 */
@Mapper
public interface BusinessTypeStatisticMapper extends BaseMapper<BusinessTypeStatistic> {

	
	@Insert({
        "insert into business_type_statistic("+
        "business_type_id,tenant_id,location_id)" +
        "values(#{businessTypeId},#{tenantId},#{locationId})"
	})
	void save(BusinessTypeStatistic statistic);
	
	 @Insert({
			"insert into business_type_statistic (id, business_type_id, ",
			"online, total, ",
			"update_time, create_time, ",
			"tenant_id, location_id)",
			"values (#{id,jdbcType=BIGINT}, #{businessTypeId,jdbcType=BIGINT}, ",
			"#{online,jdbcType=BIGINT}, #{total,jdbcType=BIGINT}, ",
			"#{updateTime,jdbcType=TIMESTAMP}, #{createTime,jdbcType=TIMESTAMP}, ",
			"#{tenantId,jdbcType=BIGINT}, #{locationId,jdbcType=BIGINT})"
	 })
	 @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	 int insertSelective(BusinessTypeStatistic statistic);
	
    @Update({
        "<script> " +
                "update business_type_statistic " +
                "		<set> " +
                "			<if test=\"updateTime != null\"> " +
                "				update_time = #{updateTime}, " +
                "			</if> " +
                "			<if test=\"online != null\"> " +
                "				online = #{online}, " +
                "			</if> " +
                "			<if test=\"total != null\"> " +
                "				total = #{total}, " +
                "			</if> " +
                "		</set> " +
                "		where location_id = #{locationId} and tenant_id=#{tenantId} and business_type_id=#{businessTypeId}" +
                "</script>  "
    })
    int updateStatistic(BusinessTypeStatistic statistic);

	@Select({
		"select "+
		"   id ," +
		"   business_type_id as businessTypeId ,location_id as locationId," +
		"   online,total,tenant_id as tenantId,create_time as createTime,",
		"   update_time as updateTime " +
		"   from business_type_statistic " +
		"	where location_id = #{locationId} and tenant_id=#{tenantId} and business_type_id=#{businessTypeId}"
	})
	BusinessTypeStatistic findByBusinessTypeId(BusinessTypeStatistic statistic);

	@Select({
		"select "+
				"   id ," +
				"   business_type_id as businessTypeId ,location_id as locationId," +
				"   online,total,tenant_id as tenantId,create_time as createTime,",
				"   update_time as updateTime " +
				"   from business_type_statistic " +
				"	where location_id = #{locationId} and tenant_id=#{tenantId}"
	})
	List<BusinessTypeStatistic> getListByTenantAndLocation(BusinessTypeStatistic statistic);

}
