package com.iot.building.device.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.iot.building.device.vo.BusinessTypeStatistic;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.common.helper.Page;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 设备-业务类型表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-05-09
 */
public interface IDeviceBusinessTypeService extends IService<DeviceBusinessTypeResp> {

	/**
	 * 获取业务类型ID
	 * 
	 * @param businessType
	 * @return
	 */
	DeviceBusinessTypeResp getBusinessTypeIdByType(Long orgId, String businessType);

	/**
	 * 获取业务类型ID
	 * 
	 * @return
	 */
	Page<DeviceBusinessTypeResp>  getBusinessTypeList(Long orgId,String name,Long tenantId, String model, int pageNumber, int pageSize);

	List<DeviceBusinessTypeResp>  getBusinessTypeList(Long orgId,Long tenantId, String model);
	
	void saveOrUpdate(DeviceBusinessTypeReq DeviceBusinessTypeReq); 
	
	void delBusinessTypeIdByType(Long id); 
	
	DeviceBusinessTypeResp findById(Long orgId,Long tenanetId,Long id);

	List<DeviceBusinessTypeResp> businessTypeWithProduct(Long orgId, Long tenantId);

    List<DeviceBusinessTypeResp> findBusinessTypeList(DeviceBusinessTypeReq deviceBusinessTypeReq);

    Boolean businessTypeDataImport(List<String[]> list,Long tenantId, Long userId);
    
	List<String> getBusinessListByDescription(String description, Long tenanetId);

	List<DeviceBusinessTypeResp> listDeviceRemoteBusinessType(Long orgId,Long tenantId,Long id);
	
	public List<BusinessTypeStatistic> findStatistic(BusinessTypeStatistic statistic);
	
	public void statisticsDeviceByBusinessType();
	
	public List<DeviceBusinessTypeResp> findByCondition(DeviceBusinessTypeReq req);
}
