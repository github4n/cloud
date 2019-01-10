package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.MinElectricityStatistics;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
public interface IMinElectricityStatisticsService extends IService<MinElectricityStatistics> {

	public List<ElectricityStatisticsRsp> selectByCondition();
	
	public ElectricityStatisticsRsp selectElectricityRspByDeviceAndUser(EnergyReq energyReq);

}
