package com.iot.device.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.mapper.MinElectricityStatisticsMapper;
import com.iot.device.model.MinElectricityStatistics;
import com.iot.device.service.IMinElectricityStatisticsService;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
@Service
public class MinElectricityStatisticsServiceImpl extends ServiceImpl<MinElectricityStatisticsMapper, MinElectricityStatistics> implements IMinElectricityStatisticsService {

	@Autowired
    private MinElectricityStatisticsMapper minElectricityStatisticsMapper;
	
	@Override
	public List<ElectricityStatisticsRsp> selectByCondition() {
		
		return minElectricityStatisticsMapper.selectElectricityRspByCondition();
	}

	@Override
	public ElectricityStatisticsRsp selectElectricityRspByDeviceAndUser(EnergyReq energyReq) {
		
		return minElectricityStatisticsMapper.selectElectricityRspByDeviceAndUser(energyReq);
	}

}
