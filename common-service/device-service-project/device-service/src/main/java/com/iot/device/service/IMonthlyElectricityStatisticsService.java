package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.MonthlyElectricityStatistics;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.EnergyRsp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
public interface IMonthlyElectricityStatisticsService extends IService<MonthlyElectricityStatistics> {

	List<MonthlyElectricityStatistics> selectByCondition(MonthlyElectricityStatistics monthlyElectricityStatistics);

	Page<EnergyRsp> selectMonthlyElectricityRsp(EnergyReq energyReq);

}
