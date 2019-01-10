package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.mapper.MonthlyElectricityStatisticsMapper;
import com.iot.device.model.MonthlyElectricityStatistics;
import com.iot.device.service.IMonthlyElectricityStatisticsService;
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
public class MonthlyElectricityStatisticsServiceImpl extends ServiceImpl<MonthlyElectricityStatisticsMapper, MonthlyElectricityStatistics> implements IMonthlyElectricityStatisticsService {

	@Autowired
    private MonthlyElectricityStatisticsMapper monthlyElectricityStatisticsMapper;
	
	@Override
	public List<MonthlyElectricityStatistics> selectByCondition(
			MonthlyElectricityStatistics monthlyElectricityStatistics) {
		
		return monthlyElectricityStatisticsMapper.selectMonthlyElectricityByCondition(monthlyElectricityStatistics);
	}

	@Override
	public Page<EnergyRsp> selectMonthlyElectricityRsp(EnergyReq energyReq) {

		energyReq.setOffset(energyReq.getOffset() + 1);
		com.baomidou.mybatisplus.plugins.Page page = new com.baomidou.mybatisplus.plugins.Page(energyReq.getOffset(), energyReq.getPageSize());
		EntityWrapper ew = new EntityWrapper();

		ew.setSqlSelect("CONCAT(year,'-',month) as time, electric_value as value");
		if (energyReq.getUserId() != null) {
			ew.eq("user_id", energyReq.getUserId());
		}
		if (energyReq.getDevId() != null) {
			ew.eq("device_id", energyReq.getDevId());
		}
		if (energyReq.getTenantId() != null) {
			ew.eq("tenant_id", energyReq.getTenantId());
		}
		ew.orderBy(true, "year", false);
		ew.orderBy(true, "month", false);
		List<Map<String, Object>> returnDataList = monthlyElectricityStatisticsMapper.selectMapsPage(page, ew);
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
