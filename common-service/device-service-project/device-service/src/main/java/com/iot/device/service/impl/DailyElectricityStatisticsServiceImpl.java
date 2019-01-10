package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.mapper.DailyElectricityStatisticsMapper;
import com.iot.device.model.DailyElectricityStatistics;
import com.iot.device.service.IDailyElectricityStatisticsService;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.rsp.EnergyRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
@Service
public class DailyElectricityStatisticsServiceImpl extends ServiceImpl<DailyElectricityStatisticsMapper, DailyElectricityStatistics> implements IDailyElectricityStatisticsService {

	@Autowired
    private DailyElectricityStatisticsMapper dailyElectricityStatisticsMapper;
    
	@Override
	public List<DailyElectricityStatistics> selectByCondition(DailyElectricityStatistics dailyElectricityStatistics) {
		
		return dailyElectricityStatisticsMapper.selectDailyElectricityByCondition(dailyElectricityStatistics);

	}

//	@Override
//	public List<EnergyRsp> selectDailyElectricityRsp(EnergyReq energyReq) {
//
//		return dailyElectricityStatisticsMapper.time(energyReq);
//	}

	@Override
	public Page<EnergyRsp> selectDailyElectricityRsp(EnergyReq energyReq) {

		energyReq.setOffset(energyReq.getOffset() + 1);
		com.baomidou.mybatisplus.plugins.Page page = new com.baomidou.mybatisplus.plugins.Page(energyReq.getOffset(), energyReq.getPageSize());
		EntityWrapper ew = new EntityWrapper();

		ew.setSqlSelect(" DISTINCT day as time, electric_value as value");
		if (energyReq.getUserId() != null) {
			ew.eq("user_id", energyReq.getUserId());
		}
		if (energyReq.getDevId() != null) {
			ew.eq("device_id", energyReq.getDevId());
		}
		if (energyReq.getTenantId() != null) {
			ew.eq("tenant_id", energyReq.getTenantId());
		}
		ew.orderBy(true, "day", false);
		List<Map<String, Object>> returnDataList = dailyElectricityStatisticsMapper.selectMapsPage(page, ew);
		Page<EnergyRsp> returnPageData = new Page<>(energyReq.getOffset(), energyReq.getPageSize());
		List<EnergyRsp> resultList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(returnDataList)) {
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
			returnDataList.forEach(map -> {
				EnergyRsp target = new EnergyRsp();
				String value = map.get("value") != null ? df.format(map.get("value")) : null;
				String time = map.get("time") != null ? map.get("time").toString() : null;
				target.setValue(value);
				target.setTime(time);
				resultList.add(target);

			});
		}
		returnPageData.setResult(resultList);
		returnPageData.setTotal(page.getTotal());
		returnPageData.setPages(page.getPages());
		return returnPageData;

	}
}
