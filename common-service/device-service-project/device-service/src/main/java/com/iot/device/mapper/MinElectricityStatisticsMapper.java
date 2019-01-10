package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.ElectricityStatisticsSqlProvider;
import com.iot.device.model.MinElectricityStatistics;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import org.apache.ibatis.annotations.Mapper;
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
public interface MinElectricityStatisticsMapper extends BaseMapper<MinElectricityStatistics> {

	@SelectProvider(type = ElectricityStatisticsSqlProvider.class,method = "selectElectricityRspByCondition")

	List<ElectricityStatisticsRsp> selectElectricityRspByCondition();

	@SelectProvider(type = ElectricityStatisticsSqlProvider.class,method = "selectElectricityRspByDeviceAndUser")

	ElectricityStatisticsRsp selectElectricityRspByDeviceAndUser(EnergyReq energyReq);

}
