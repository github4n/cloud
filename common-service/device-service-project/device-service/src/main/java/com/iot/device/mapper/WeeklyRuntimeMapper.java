package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.RuntimeSqlProvider;
import com.iot.device.model.WeeklyRuntime;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.EnergyRsp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Mapper
public interface WeeklyRuntimeMapper extends BaseMapper<WeeklyRuntime> {

	@SelectProvider(type = RuntimeSqlProvider.class,method = "selectWeeklyRuntimeByCondition")
	@Results({
		@Result(property = "userId", column = "user_id"),
        @Result(property = "deviceId", column = "device_id"),
        @Result(property = "orgId", column = "org_id"),
        @Result(property = "tenantId", column = "tenant_id"),
        @Result(property = "week", column = "week"),
        @Result(property = "year", column = "year"),
        @Result(property = "runtime", column = "runtime")
})
	List<WeeklyRuntime> selectWeeklyRuntimeByCondition(WeeklyRuntime weeklyRuntime);

	@SelectProvider(type = RuntimeSqlProvider.class, method = "selectWeeklyRuntimeRsp")
	List<EnergyRsp> selectWeeklyRuntimeRsp(RuntimeReq2Runtime runtimeReq);

}
