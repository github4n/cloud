package com.iot.building.device.controlller;

import java.util.List;

import com.iot.common.enums.BusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.service.IDeviceBusinessTypeService;
import com.iot.building.device.vo.BusinessTypeStatistic;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.common.helper.Page;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-05-16
 */
@RestController
@Validated
public class DeviceBusinessTypeController implements DeviceBusinessTypeApi {

	public static final Logger LOGGER = LoggerFactory.getLogger(DeviceBusinessTypeController.class);
	
	@Autowired
	private IDeviceBusinessTypeService deviceBusinessTypeService;

	@Override
	public DeviceBusinessTypeResp getBusinessTypeIdByType(Long orgId, Long tenantId,String businessType) {
		return deviceBusinessTypeService.getBusinessTypeIdByType(orgId, businessType);
	}

	@Override
	public Page<DeviceBusinessTypeResp> getBusinessTypeList(@RequestParam(value = "orgId") Long orgId,@RequestParam(value = "name") String name,@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "model") String model, @RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "pageSize")  int pageSize) {
		return deviceBusinessTypeService.getBusinessTypeList(orgId,name,tenantId,model,pageNumber,pageSize);
	}

	public List<DeviceBusinessTypeResp> getBusinessTypeList(@RequestParam(value = "orgId") Long orgId, @RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "model") String model) {
		return deviceBusinessTypeService.getBusinessTypeList(orgId,tenantId,model);
	}

	@Override
	public void saveOrUpdate(@RequestBody DeviceBusinessTypeReq deviceBusinessTypeReq) {
		deviceBusinessTypeService.saveOrUpdate(deviceBusinessTypeReq);
	}

	@Override
	public void delBusinessTypeById(@RequestParam("id") Long id) {
		deviceBusinessTypeService.delBusinessTypeIdByType(id);
	}

	@Override
	public DeviceBusinessTypeResp findById(@RequestParam(value = "orgId") Long orgId,Long tenantId,@RequestParam("id") Long id) {
		return deviceBusinessTypeService.findById(orgId,tenantId,id);
	}

	@Override
	public List<DeviceBusinessTypeResp> businessTypeWithProduct(@RequestParam(value = "orgId") Long orgId,Long tenantId) {
		return deviceBusinessTypeService.businessTypeWithProduct(orgId,tenantId);
	}

	@Override
	public List<DeviceBusinessTypeResp> findBusinessTypeList(@RequestParam(value = "orgId") Long orgId,@RequestParam("tenantId") Long tenantId,@RequestParam(value = "businessType",required = false)String businessType) {
		DeviceBusinessTypeReq deviceBusinessTypeReq = new DeviceBusinessTypeReq();
		deviceBusinessTypeReq.setTenantId(tenantId);
		deviceBusinessTypeReq.setOrgId(orgId);
		deviceBusinessTypeReq.setBusinessType(businessType);
		return deviceBusinessTypeService.findBusinessTypeList(deviceBusinessTypeReq);
	}

	@Override
	public List<DeviceBusinessTypeResp> listDeviceRemoteBusinessType(Long orgId,Long tenantId,Long id) {
		return deviceBusinessTypeService.listDeviceRemoteBusinessType(orgId,tenantId,id);
	}

	@Override
	public Boolean businessTypeDataImport(@RequestParam(value = "orgId") Long orgId,@RequestParam(value = "list")List<String[]> list,@RequestParam("tenantId")Long tenantId, @RequestParam("userId")Long userId) {
		return deviceBusinessTypeService.businessTypeDataImport(list,tenantId, userId);
	}
	
	@Override
	public List<String> getBusinessListByDescription(@RequestParam(value = "orgId") Long orgId,@RequestParam(value = "description")String description, @RequestParam(value = "tenantId")Long tenantId) {
		return deviceBusinessTypeService.getBusinessListByDescription(description,tenantId);
	}
	
	@Override
	public List<BusinessTypeStatistic> findStatistic(@RequestBody BusinessTypeStatistic statistic){
		return deviceBusinessTypeService.findStatistic(statistic);
	}

	@Override
	public void initStatistic() {
		deviceBusinessTypeService.statisticsDeviceByBusinessType();
	}

	@Override
	public List<DeviceBusinessTypeResp> findByCondition(@RequestBody DeviceBusinessTypeReq req) {
		return deviceBusinessTypeService.findByCondition(req);
	}

}
