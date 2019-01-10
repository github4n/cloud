package com.iot.building.device.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.device.mapper.BusinessTypeStatisticMapper;
import com.iot.building.device.mapper.DeviceBusinessTypeMapper;
import com.iot.building.device.service.IDeviceBusinessTypeService;
import com.iot.building.device.vo.BusinessTypeStatistic;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.helper.Constants;
import com.iot.building.space.mapper.LocationMapper;
import com.iot.building.space.vo.LocationResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.ListDeviceTypeReq;
import com.iot.device.vo.req.device.ListProductInfoReq;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;

/**
 * <p>
 * 设备-业务类型表 服务实现类
 * </p>
 * @author lucky
 * @since 2018-05-09
 */
@Service
public class DeviceBusinessTypeServiceImpl extends ServiceImpl<DeviceBusinessTypeMapper, DeviceBusinessTypeResp> implements IDeviceBusinessTypeService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DeviceBusinessTypeServiceImpl.class);
    
	@Autowired
	DeviceBusinessTypeMapper deviceBusinessTypeMapper;
	@Autowired
	BusinessTypeStatisticMapper businessTypeStatisticMapper;
	@Autowired
	ProductCoreApi productCoreApi;
	@Autowired
	private DeviceTypeCoreApi deviceTypeCoreApi;
	@Autowired
	private DeviceCoreApi deviceCoreApi;
	@Autowired
	private DeviceStatusCoreApi deviceStatusCoreApi;
	@Autowired
	LocationMapper locationMapper;
	
	@Override
	public DeviceBusinessTypeResp getBusinessTypeIdByType(Long orgId, String businessType) {
		return deviceBusinessTypeMapper.getBusinessTypeIdByType(orgId, businessType);
	}

	@Override
	public Page<DeviceBusinessTypeResp>  getBusinessTypeList(@RequestParam(value = "orgId") Long orgId,@RequestParam(value = "name") String name,@RequestParam(value = "tenantId") Long tenantId,
			@RequestParam(value = "model") String model, @RequestParam(value = "pageNumber") int pageNumber, 
			@RequestParam(value = "pageSize")  int pageSize) {
		com.github.pagehelper.Page<DeviceBusinessTypeResp> gibHubPage=com.github.pagehelper.PageHelper.startPage(pageNumber, pageSize);
		List<DeviceBusinessTypeResp> listStr = deviceBusinessTypeMapper.getBusinessTypeList(orgId, tenantId, name);
		Page<DeviceBusinessTypeResp> page = new Page<DeviceBusinessTypeResp>();
		BeanUtil.copyProperties(gibHubPage, page);
		commonSetProductInfo(listStr);
		return page;
	}

	private void commonSetProductInfo(List<DeviceBusinessTypeResp> deviceBusinessTypes) {
		List<Long> productIds=Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(deviceBusinessTypes)) {
			for (DeviceBusinessTypeResp deviceBusinessType : deviceBusinessTypes) {
				productIds.add(deviceBusinessType.getProductId());
			}
			//批量查询产品信息组装
			ListProductInfoReq params =new ListProductInfoReq();
			params.setProductIds(productIds);
			List<ListProductRespVo> productList=productCoreApi.listProducts(params);
			List<Long> deviceTypeIds=Lists.newArrayList();
			Map<Long,String> productMap=Maps.newHashMap();
			Map<Long,Long> productDeviceTypeMap=Maps.newHashMap();
			Map<Long,String> modelMap=Maps.newHashMap();
			if(CollectionUtils.isNotEmpty(productList)) {
				productList.forEach(product->{
					productMap.put(product.getId(), product.getProductName());
					productDeviceTypeMap.put(product.getId(), product.getDeviceTypeId());
					modelMap.put(product.getId(),product.getModel());
					deviceTypeIds.add(product.getDeviceTypeId());
				});
			}
			//批量查询deviceType组装信息
			Map<Long,String> deviceTypeMap=Maps.newHashMap();
 			if(CollectionUtils.isNotEmpty(deviceTypeIds)) {
 				ListDeviceTypeReq params_=new ListDeviceTypeReq();
 				params_.setDeviceTypeIds(deviceTypeIds);
 				List<ListDeviceTypeRespVo> deviceTypeList=deviceTypeCoreApi.listDeviceType(params_);
 				if(CollectionUtils.isNotEmpty(deviceTypeList)) {
 					deviceTypeList.forEach(deviceType->{
 						deviceTypeMap.put(deviceType.getId(), deviceType.getType());
 					});
 				}
 			}
 			//拼接信息
 			for (DeviceBusinessTypeResp deviceBusinessType : deviceBusinessTypes) {
 				String productName=productMap.get(deviceBusinessType.getProductId());
 				deviceBusinessType.setProductName(productName);
 				Long deviceTypeId=productDeviceTypeMap.get(deviceBusinessType.getProductId());
 				deviceBusinessType.setDeviceTypeId(deviceTypeId);
 				deviceBusinessType.setDeviceTypeName(deviceTypeMap.get(deviceTypeId));
 				deviceBusinessType.setBusinessTypeId(deviceBusinessType.getId());
 				deviceBusinessType.setModel(modelMap.get(deviceBusinessType.getProductId()));
			}
		}
	}

	@Override
	public List<DeviceBusinessTypeResp> getBusinessTypeList(Long orgId,Long tenantId, String model) {
		List<DeviceBusinessTypeResp> deviceBusinessTypes=deviceBusinessTypeMapper.getBusinessTypeList(orgId,tenantId,model);
		commonSetProductInfo(deviceBusinessTypes);
		return  deviceBusinessTypes;
	}

	@Override
	public void saveOrUpdate(DeviceBusinessTypeReq deviceBusinessTypeReq) {
//		deviceBusinessTypeReq.setIsHomeShow(null);
		if(deviceBusinessTypeReq.getId() !=null) {
			deviceBusinessTypeMapper.updateByPrimaryKeySelective(deviceBusinessTypeReq);
		}else {
			deviceBusinessTypeMapper.save(deviceBusinessTypeReq);
		}
	}
	
	@Override
	public void delBusinessTypeIdByType(Long id) {
		deviceBusinessTypeMapper.delBusinessTypeIdByType(id);
	}

	@Override
	public DeviceBusinessTypeResp findById(Long orgId,Long tenanetId,Long id) {
		return deviceBusinessTypeMapper.findById(orgId,tenanetId,id);
	}

	@Override
	public List<DeviceBusinessTypeResp> businessTypeWithProduct(Long orgId, Long tenantId) {
		return deviceBusinessTypeMapper.businessTypeWithProduct(orgId,tenantId);
	}

	@Override
	public List<DeviceBusinessTypeResp> findBusinessTypeList(DeviceBusinessTypeReq deviceBusinessTypeReq) {
		List<DeviceBusinessTypeResp> list=deviceBusinessTypeMapper.findBusinessTypeList(deviceBusinessTypeReq);
		commonSetProductInfo(list);
		return list;
	}

	@Override
	public Boolean businessTypeDataImport(@RequestParam(value = "list")List<String[]> list,@RequestParam(value = "tenantId")Long tenantId,@RequestParam(value = "userId")Long userId) {
		try {
            DeviceBusinessTypeReq deviceBusinessTypeReq = new DeviceBusinessTypeReq();
			if (list != null) {
				for(String[] str:list){
					String[] bt = str;
					String businessType = String.valueOf(bt[0]);
					String description = String.valueOf(bt[1]);
					Long productId = Long.valueOf(bt[2]);
					String a = String.valueOf(bt[3]);
					Integer isHomeShow = Integer.valueOf(a);
					deviceBusinessTypeReq.setBusinessType(businessType);
					deviceBusinessTypeReq.setDescription(description);
					deviceBusinessTypeReq.setCreateTime(new Date());
					deviceBusinessTypeReq.setUpdateTime(new Date());
					deviceBusinessTypeReq.setCreateBy(userId);
					deviceBusinessTypeReq.setTenantId(tenantId);
					deviceBusinessTypeReq.setProductId(productId);
					deviceBusinessTypeReq.setIsHomeShow(isHomeShow);
					deviceBusinessTypeMapper.save(deviceBusinessTypeReq);
					}
				}
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<String> getBusinessListByDescription(@RequestParam(value = "description")String description, @RequestParam(value = "tenantId")Long tenantId) {
		return deviceBusinessTypeMapper.getBusinessListByDescription(description,tenantId);
	}

	@Override
	public List<DeviceBusinessTypeResp> listDeviceRemoteBusinessType(Long orgId,Long tenantId,Long id) {
		return deviceBusinessTypeMapper.listDeviceRemoteBusinessType(orgId,tenantId,id);
	}
	
	public List<BusinessTypeStatistic> findStatistic(BusinessTypeStatistic statistic){
		List<BusinessTypeStatistic> statisticList=businessTypeStatisticMapper.getListByTenantAndLocation(statistic);
		if(CollectionUtils.isNotEmpty(statisticList)) {
			statisticList.forEach(vo->{
				DeviceBusinessTypeResp resp=findById(statistic.getOrgId(), statistic.getTenantId(), vo.getBusinessTypeId());
				vo.setName(resp==null?null:resp.getBusinessType());
			});
		}
		return statisticList;
	}
	
//	@Scheduled(cron = "0 0/10 * * * ?")//每十分钟执行一次
	public void statisticsDeviceByBusinessType() {
		LOGGER.info("定时统计设备数量开始... ... ... ... ... ... ...");
		//1.获取所有location 信息
		new Thread(() -> {
				List<LocationResp> locationList=locationMapper.getAll();
				if(CollectionUtils.isNotEmpty(locationList)) {
					List<DeviceBusinessTypeResp> businessList=deviceBusinessTypeMapper.getBusinessTypeList(locationList.get(0).getOrgId(),locationList.get(0).getTenantId(), null);
					if(CollectionUtils.isNotEmpty(businessList)) {
						for(LocationResp resp:locationList) {
							businessList.forEach(business->{
								Long online=0L,total=0L;
								List<ListDeviceByParamsRespVo> deviceList = getDeviceListByBusinessTypeId(resp, business);
								if(CollectionUtils.isNotEmpty(deviceList)) {
									List<String> deviceIds=Lists.newArrayList();
									total=getTotal(deviceList, deviceIds);//总数
									online=getOnlineSum(business.getTenantId(),deviceIds);//在线数
								}
								saveStatistic(resp, business, online, total);
								LOGGER.info(business.getBusinessType()+"总数："+total+" 在线数量："+online);
							});
						}
					}
				}
		} ).start();
	}

	private synchronized void saveStatistic(LocationResp resp, DeviceBusinessTypeResp business, Long online, Long total) {
		BusinessTypeStatistic statistic=new BusinessTypeStatistic();
		statistic.setBusinessTypeId(business.getId());
		statistic.setTenantId(business.getTenantId());
		statistic.setLocationId(resp.getId());
		BusinessTypeStatistic st2=businessTypeStatisticMapper.findByBusinessTypeId(statistic);
		statistic.setOnline(online);
		statistic.setTotal(total);
		statistic.setUpdateTime(new Date());
		statistic.setCreateTime(new Date());
		if(st2==null) {
			businessTypeStatisticMapper.insertSelective(statistic);
		}else {
			businessTypeStatisticMapper.updateStatistic(statistic);
		}
		
	}

	private List<ListDeviceByParamsRespVo> getDeviceListByBusinessTypeId(LocationResp resp,
			DeviceBusinessTypeResp business) {
		ListDeviceByParamsReq params=new ListDeviceByParamsReq();
		params.setDeviceBusinessTypeId(business.getId());
		params.setLocationId(resp.getId());
		params.setTenantId(business.getTenantId());
		List<ListDeviceByParamsRespVo> deviceList=deviceCoreApi.listDeviceByParams(params);
		return deviceList;
	}

	private Long getTotal(List<ListDeviceByParamsRespVo> deviceList, List<String> deviceIds) {
		Long total=0l;
		for(ListDeviceByParamsRespVo vo:deviceList) {
			deviceIds.add(vo.getUuid());
			total++;
		}
		return total;
	}

	private Long getOnlineSum(Long tenantId,  List<String> deviceIds) {
		Long online=0l;
		ListDeviceStateReq req=new ListDeviceStateReq();
		req.setTenantId(tenantId);
		req.setDeviceIds(deviceIds);
		List<ListDeviceStatusRespVo> statusList=deviceStatusCoreApi.listDeviceStatus(req);
		if(CollectionUtils.isNotEmpty(statusList)) {
			for(ListDeviceStatusRespVo vo:statusList) {
				if(vo.getOnlineStatus().equals(Constants.DEVICE_ONLINE)) {
					online++;
				}
			};
		}
		return online;
	}

	@Override
	public List<DeviceBusinessTypeResp> findByCondition(DeviceBusinessTypeReq req) {
		return deviceBusinessTypeMapper.findBusinessTypeList(req);
	}
}
