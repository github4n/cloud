package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.DailyElectricityStatistics;
import com.iot.device.vo.req.DailyElectricityStatisticsReq;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
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
public interface IDailyElectricityStatisticsMongoService {


	public Boolean insertOrUpdateBatch(List<DailyElectricityStatistics> dailylist);

//	public List<DailyElectricityStatistics> selectByCondition(DailyElectricityStatistics dailyElectricityStatistics);

	public Page<EnergyRsp> selectDailyElectricityRsp(EnergyReq energyReq);

	List<DailyElectricityStatisticsResp> selectListByReq(DailyElectricityStatisticsReq req);
}
