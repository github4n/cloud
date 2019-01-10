package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.ElectricityStatisticsSqlProvider;
import com.iot.device.model.DailyElectricityStatistics;
import com.iot.device.vo.req.EnergyReq;
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
 * @since 2018-05-02
 */
@Mapper
public interface DailyElectricityStatisticsMapper extends BaseMapper<DailyElectricityStatistics> {

	@SelectProvider(type = ElectricityStatisticsSqlProvider.class,method = "selectDailyElectricityByCondition")
	@Results({
		@Result(property = "userId", column = "user_id"),
        @Result(property = "deviceId", column = "device_id"),
        @Result(property = "orgId", column = "org_id"),
        @Result(property = "tenantId", column = "tenant_id"),
        @Result(property = "day", column = "day"),
        @Result(property = "electricValue", column = "electric_value")
})
	List<DailyElectricityStatistics> selectDailyElectricityByCondition(DailyElectricityStatistics dailyElectricityStatistics);


	@SelectProvider(type = ElectricityStatisticsSqlProvider.class, method = "selectDailyElectricityRsp")
	List<EnergyRsp> selectDailyElectricityRsp(EnergyReq energyReq);
}
