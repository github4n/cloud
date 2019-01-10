package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.MonthlyElectricityStatistics;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.req.MonthlyElectricityStatisticsReq;
import com.iot.device.vo.rsp.EnergyRsp;
import com.iot.device.vo.rsp.MonthlyElectricityStatisticsResp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
public interface IMonthlyElectricityStatisticsMongoService {

	public Boolean insertOrUpdateBatch(List<MonthlyElectricityStatistics> monthlylist);

	List<MonthlyElectricityStatistics> selectByCondition(MonthlyElectricityStatistics monthlyElectricityStatistics);

	Page<EnergyRsp> selectMonthlyElectricityRsp(EnergyReq energyReq);

    List<MonthlyElectricityStatisticsResp> selectListByReq(MonthlyElectricityStatisticsReq req);
}
