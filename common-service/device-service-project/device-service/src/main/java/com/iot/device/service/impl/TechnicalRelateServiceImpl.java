package com.iot.device.service.impl;

import java.sql.Wrapper;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.common.util.StringUtil;
import com.iot.device.mapper.DeviceTypeMapper;
import com.iot.device.mapper.DeviceTypeTechnicalRelateMapper;
import com.iot.device.mapper.NetworkTypeMapper;
import com.iot.device.model.DeviceType;
import com.iot.device.model.DeviceTypeTechnicalRelate;
import com.iot.device.vo.req.technical.SaveDeviceTechnicalReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.common.exception.BusinessException;
import com.iot.device.dto.NetworkTypeDto;
import com.iot.device.entity.NetworkType;
import com.iot.device.mapper.TechnicalRelateMapper;
import com.iot.device.service.ITechnicalRelateService;
import com.iot.device.vo.NetworkTypeVo;

@Service("ITechnicalRelateService")
@Transactional
public class TechnicalRelateServiceImpl implements ITechnicalRelateService {

	@Autowired
	TechnicalRelateMapper technicalRelateMapper;

	@Autowired
	DeviceTypeTechnicalRelateMapper deviceTypeTechnicalRelateMapper;

	@Autowired
	DeviceTypeMapper deviceTypeMapper;

	@Autowired
	private NetworkTypeMapper networkTypeMapper;

	public NetworkTypeDto getNetworkInfo(Long networkTypeId) throws BusinessException{
		NetworkTypeDto networkTypeDto = technicalRelateMapper.getNetworkInfo(networkTypeId);
		if(networkTypeDto != null) {
			List<Long> technicalSchemeIds = technicalRelateMapper.getTechnicalSchemeIdByNetworkTypeId(networkTypeId);
			networkTypeDto.setTechnicalSchemeIds(technicalSchemeIds);
		}
		return networkTypeDto;
	}
	
	/**
	 * 
	 * 描述：更新配网模式信息
	 * @author 李帅
	 * @created 2018年10月12日 下午5:22:29
	 * @since 
	 * @param networkTypeVo
	 * @throws BusinessException
	 */
	public void updateNetworkInfo(NetworkTypeVo networkTypeVo) throws BusinessException{
		NetworkType networkType = new NetworkType();
		BeanUtils.copyProperties(networkTypeVo, networkType);
		if(networkTypeVo.getId() == null) {
			technicalRelateMapper.saveNetworkType(networkType);
			Map<String, Object> technicalNetworkRelateParam = new HashMap<String, Object>();
			technicalNetworkRelateParam.put("networkTypeId", networkType.getId());
			technicalNetworkRelateParam.put("createBy", networkTypeVo.getCreateBy());
			technicalNetworkRelateParam.put("technicalSchemeIds", networkTypeVo.getTechnicalSchemeIds());
			technicalRelateMapper.saveTechnicalNetworkRelate(technicalNetworkRelateParam);
		}else {
			technicalRelateMapper.updateNetworkType(networkType);
			technicalRelateMapper.deleteTechnicalNetworkRelateByByNetworkTypeId(networkTypeVo.getId());
			Map<String, Object> technicalNetworkRelateParam = new HashMap<String, Object>();
			technicalNetworkRelateParam.put("networkTypeId", networkTypeVo.getId());
			technicalNetworkRelateParam.put("createBy", networkTypeVo.getCreateBy());
			technicalNetworkRelateParam.put("technicalSchemeIds", networkTypeVo.getTechnicalSchemeIds());
			technicalRelateMapper.saveTechnicalNetworkRelate(technicalNetworkRelateParam);
		}
	}

	/**
	 * @despriction：保存设备支持的技术方案
	 * @author  yeshiyuan
	 * @created 2018/10/15 19:18
	 * @return
	 */
	@Transactional
	@Override
	public void saveDeviceTechnical(SaveDeviceTechnicalReq req) {
		deviceTypeTechnicalRelateMapper.deleteByDeviceTypeId(req.getDeviceTypeId());
		List<DeviceTypeTechnicalRelate> list = new ArrayList<>();
		req.getTechnicalIds().forEach(o->{
			DeviceTypeTechnicalRelate technicalRelate = new DeviceTypeTechnicalRelate(req.getDeviceTypeId(),o,req.getCreateBy(), new Date());
			list.add(technicalRelate);
		});
		if (!list.isEmpty()) {
			deviceTypeTechnicalRelateMapper.batchInsert(list);
		}
		String networkType = null;
		if (req.getNetworkTypeIds() != null) {
			networkType = StringUtils.join(req.getNetworkTypeIds().toArray(), ",");
		}
		deviceTypeMapper.updateNetworkType(req.getDeviceTypeId(), networkType);
	}

	/**
	 * @despriction：查询设备类型支持的技术方案
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:04
	 * @return
	 */
	@Override
	public List<Long> queryDeviceTechnical(Long deviceTypeId) {
		return deviceTypeTechnicalRelateMapper.findTechnicalIdsByDeviceTypeId(deviceTypeId);
	}

	/**
	 * @despriction：设备支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:31
	 * @return
	 */
	@Override
	public List<NetworkTypeResp> deviceSupportNetworkType(Long deviceTypeId) {
		String networkType = deviceTypeMapper.getNetworkType(deviceTypeId);
		if (!StringUtil.isBlank(networkType)) {
			List<Long> ids = new ArrayList<>();
			String []idArray = networkType.split(",");
			for (String id : idArray) {
				ids.add(Long.valueOf(id));
			}
			return networkTypeMapper.findByIds(ids);
		}
		return Collections.emptyList();
	}

	/**
	 * @despriction：方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:56
	 * @return
	 */
	@Override
	public List<NetworkTypeResp> technicalSupportNetworkType(Long technicalId) {
		return deviceTypeTechnicalRelateMapper.technicalSupportNetworkType(technicalId);
	}

	/**
	 * @despriction：方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/16 17:03
	 * @return
	 */
	@Override
	public List<NetworkTypeResp> findNetworkByTechnicalIds(List<Long> technicalIds) {
		return deviceTypeTechnicalRelateMapper.findNetworkByTechnicalIds(technicalIds);
	}

	/**
	 * @despriction：查看设备类型某种方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/12/11 15:39
	 */
	@Override
	public List<NetworkTypeResp> deviceSupportNetwrokByTechnicalId(Long deviceTypeId, Long technicalId) {
		//查找设备类型支持的配网模式
		String networkType = deviceTypeMapper.getNetworkType(deviceTypeId);
		if (StringUtil.isBlank(networkType)) {
			return null;
		}
		List<Long> networkTypeIds = new ArrayList<>();
		String []idArray = networkType.split(",");
		for (String id : idArray) {
			networkTypeIds.add(Long.valueOf(id));
		}
		//查找技术方案支持的配网模式
		List<NetworkTypeResp> resps = deviceTypeTechnicalRelateMapper.technicalSupportNetworkType(technicalId);
		if (resps == null || resps.isEmpty()) {
			return null;
		}
		List<NetworkTypeResp> result = resps.stream().filter(networkTypeResp -> networkTypeIds.contains(networkTypeResp.getId())).collect(Collectors.toList());
		return result;
	}
}
