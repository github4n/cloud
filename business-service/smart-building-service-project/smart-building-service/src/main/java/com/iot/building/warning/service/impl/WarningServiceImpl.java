package com.iot.building.warning.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageInfo;
import com.iot.building.warning.domain.Warning;
import com.iot.building.warning.mapper.WarningMapper;
import com.iot.building.warning.service.IWarningService;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.exception.DeviceExceptionEnum;

@Service("warningService")
@Transactional
public class WarningServiceImpl  implements IWarningService {

	private final static Logger LOGGER = LoggerFactory.getLogger(WarningServiceImpl.class);


	@Autowired
	WarningMapper warningMapper;
	
	/**
	 * 描述：查询历史告警数据
	 *
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws BusinessException
	 * @author zhouzongwei
	 * @created 2017年11月30日 下午3:00:12
	 * @since
	 */
	@Override
	public Page<WarningResp> findHistoryWarningList(String pageNum, String pageSize,String eventType,String timeType,
			Long tenantId,Long orgId,Long locationId) throws BusinessException {
		Page<WarningResp> page = new Page<WarningResp>();
		try {
			com.github.pagehelper.PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			WarningReq warningReq = new WarningReq();
			if(StringUtil.isNotBlank(eventType) && !eventType.equals("3")){
				warningReq.setEventType(eventType);
			}
			if(StringUtil.isNotBlank(timeType)){
				if(!timeType.equals("null")){
					warningReq.setTimeType(timeType);
				}
			}
			warningReq.setTenantId(tenantId);
			warningReq.setOrgId(orgId);
			warningReq.setLocationId(locationId);
			List<WarningResp> respList = warningMapper.findHistoryWarningList(warningReq);
			PageInfo<WarningResp> info = new PageInfo(respList);
			BeanUtil.copyProperties(info, page);
			page.setResult(info.getList());
		} catch (Exception e) {
			throw new BusinessException(DeviceExceptionEnum.FIND_HISTORY_WARNINGLIST_FAILED);
		}
		return page;
	}

	/**
	 * 描述：查询未读告警数据
	 *
	 * @return
	 * @throws BusinessException
	 * @since
	 */
	@Override
	public List<WarningResp> findUnreadWarningList(Long tenantId,Long orgId,Long locationId) throws BusinessException {
		try {
			List<WarningResp> content = warningMapper.findUnreadWarningList(tenantId,orgId,locationId);
			return content;
		} catch (Exception e) {
			throw new BusinessException(DeviceExceptionEnum.FIND_UNREAD_WARNINGLIST_FAILED);
		}
	}

	/**
	 * 描述：插入告警数据
	 *
	 * @param warningReq
	 * @throws BusinessException
	 * @author zhouzongwei
	 * @created 2017年11月30日 下午3:00:00
	 * @since
	 */
	@Override
	public WarningResp addWarning(WarningReq warningReq) throws BusinessException {
		try {
			warningMapper.insertWarning(warningReq);
			WarningResp resp = new WarningResp();
			BeanUtil.copyProperties(resp, warningReq);
			return resp;
		} catch (Exception e) {
			throw new BusinessException(DeviceExceptionEnum.INSERT_WARNING_FAILED);
		}
	}

	/**
	 * 描述：更新告警消息状态
	 *
	 * @return
	 * @throws BusinessException
	 * @since
	 */
	@Override
	public int updateWarningStatus(WarningReq warningReq) throws BusinessException {
		try {
			return this.warningMapper.updateWarningStatus(warningReq);
		} catch (Exception e) {
			throw new BusinessException(DeviceExceptionEnum.UPDATE_WARNING_STATUS_FAILED);
		}
	}

	@Override
	public List<WarningResp> findHistoryWarningListNoPage(String eventType,String count,Long tenantId,Long orgId,Long locationId) {
		List<WarningResp> respList = Lists.newArrayList();
		try {
			WarningReq warningReq = new WarningReq();
			if(StringUtil.isNotBlank(eventType)){
					warningReq.setEventType(eventType);
			}
			if(StringUtil.isNotEmpty(count)){
				warningReq.setCount(count);
			}
			warningReq.setTenantId(tenantId);
			warningReq.setLocationId(locationId);
			warningReq.setOrgId(orgId);
			respList = warningMapper.findHistoryWarningListNoPage(warningReq);
		} catch (Exception e) {
			throw new BusinessException(DeviceExceptionEnum.FIND_HISTORY_WARNINGLIST_FAILED);
		}
		return respList;
	}


	/**
	 * 描述：更新告警消息状态
	 *
	 * @param id
	 * @return
	 * @throws BusinessException
	 * @since
	 */
	@Override
	public int countWarningById(Long id) throws BusinessException {
		try {
			return this.warningMapper.countWarningById(id);
		} catch (Exception e) {
			throw new BusinessException(DeviceExceptionEnum.UPDATE_WARNING_STATUS_FAILED);
		}
	}
	@Override
	public List<WarningResp> findWarningListByCondition(WarningReq warningReq){
		/*EntityWrapper<Warning> wrapper = new EntityWrapper<>();
		if(warningReq.getId() != null){
			wrapper.eq("id", warningReq.getId());
		}
		if(warningReq.getDeviceId() != null){
			wrapper.eq("device_id", warningReq.getDeviceId());
		}
		if(warningReq.getType() != null){
			wrapper.eq("type", warningReq.getType());
		}
		if(warningReq.getContent() != null){
			wrapper.like("content", warningReq.getContent());
		}
		if(warningReq.getTenantId() != null){
			wrapper.eq("tenant_id", warningReq.getTenantId());
		}
		if(warningReq.getCreateTime() != null){
			wrapper.eq("create_time", warningReq.getCreateTime());
		}
		if(warningReq.getStatus() != null){
			wrapper.eq("status", warningReq.getStatus());
		}
		if(warningReq.getLocationId() != null){
			wrapper.eq("location_id", warningReq.getLocationId());
		}
		if(warningReq.getSpaceName() != null){
			wrapper.eq("space_name", warningReq.getSpaceName());
		}
		if(warningReq.getEventName() != null){
			wrapper.eq("event_name", warningReq.getEventName());
		}
		if(warningReq.getEventType() != null){
			wrapper.eq("event_type", warningReq.getEventType());
		}
		wrapper.orderBy(true, "create_time", false);*/
		List<WarningResp> warningResp = warningMapper.findWarningListByCondition(warningReq);
		//List<Warning> warnings = warningMapper.selectList(wrapper);
		//return warningListToWarningRespList(warnings);
		return warningResp;
	}
	public static List<WarningResp> warningListToWarningRespList(List<Warning> warnings){
		List<WarningResp> warningResps = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(warnings)) {
			warnings.forEach(req -> {
				warningResps.add(warningToWarningResp(req));
			});
		}
		return warningResps;
	}
	public static WarningResp warningToWarningResp(Warning warning){
		WarningResp resp = new WarningResp();
		resp.setId(warning.getId());
		resp.setDeviceId(warning.getDeviceId());
		resp.setType(warning.getType());
		resp.setContent(warning.getContent());
		resp.setTenantId(warning.getTenantId());
		resp.setCreateTime(warning.getCreateTime());
		resp.setStatus(warning.getStatus());
		resp.setLocationId(warning.getLocationId());
		return resp;
	}
}
