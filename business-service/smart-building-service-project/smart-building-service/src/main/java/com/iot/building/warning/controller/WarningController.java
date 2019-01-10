package com.iot.building.warning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.warning.api.WarningApi;
import com.iot.building.warning.service.IWarningService;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;

/**
 * @Author: wl
 * @Date: 2018/10/12
 * @Description: *
 */
@RestController
public class WarningController implements WarningApi {

	@Autowired
	private IWarningService warningService;

	@Override
	public int countWarningById(Long id) throws Exception {
		return warningService.countWarningById(id);
	}

	@Override
	public Page<WarningResp> findHistoryWarningList(@RequestParam("pageNum")String pageNum, @RequestParam("pageSize") String pageSize,
			@RequestParam(value = "eventType", required = false) String eventType,
			@RequestParam(value = "timeType", required = false) String timeType,
			@RequestParam("tenantId")Long tenantId,@RequestParam("orgId")Long orgId,
			@RequestParam("locationId")Long locationId) throws BusinessException {
		return warningService.findHistoryWarningList(pageNum, pageSize,eventType,timeType,tenantId,orgId,locationId);
	}

	@Override
	public List<WarningResp> findUnreadWarningList(@RequestParam("tenantId")Long tenantId,@RequestParam("orgId")Long orgId,@RequestParam("locationId")Long locationId) throws BusinessException {
		return warningService.findUnreadWarningList(tenantId,orgId,locationId);
	}

	@Override
	public WarningResp addWarning(@RequestBody WarningReq warning) throws BusinessException {
		return warningService.addWarning(warning);
	}

	@Override
	public int updateWarningStatus(@RequestBody WarningReq warning) throws BusinessException {
		return warningService.updateWarningStatus(warning);
	}

	@Override
	public List<WarningResp> findHistoryWarningListNoPage(@RequestParam("eventType")String eventType,@RequestParam("count") String count,
			@RequestParam("tenantId")Long tenantId,@RequestParam("orgId")Long orgId,@RequestParam("locationId")Long locationId) {
		return warningService.findHistoryWarningListNoPage(eventType,count,tenantId,orgId,locationId);
	}

	@Override
	public List<WarningResp> findWarningList(@RequestBody WarningReq warning) throws BusinessException {
		return warningService.findWarningListByCondition(warning);
	}

}
