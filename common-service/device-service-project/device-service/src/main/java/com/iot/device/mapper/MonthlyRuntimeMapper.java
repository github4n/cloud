package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.RuntimeSqlProvider;
import com.iot.device.model.MonthlyRuntime;
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
public interface MonthlyRuntimeMapper extends BaseMapper<MonthlyRuntime> {

	@SelectProvider(type = RuntimeSqlProvider.class,method = "selectMonthlyRuntimeByCondition")
	@Results({
		@Result(property = "userId", column = "user_id"),
        @Result(property = "deviceId", column = "device_id"),
        @Result(property = "orgId", column = "org_id"),
        @Result(property = "tenantId", column = "tenant_id"),
        @Result(property = "month", column = "month"),
        @Result(property = "year", column = "year"),
        @Result(property = "runtime", column = "runtime")
})
	List<MonthlyRuntime> selectMonthlyRuntimeByCondition(MonthlyRuntime monthlyRuntime);

	@SelectProvider(type = RuntimeSqlProvider.class, method = "selectMonthlyRuntimeRsp")
	List<EnergyRsp> selectMonthlyRuntimeRsp(RuntimeReq2Runtime runtimeReq);
}
