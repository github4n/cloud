package com.iot.building.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.beans.BeanUtil;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;

/**
 * 描述：bean字段赋值工具类 创建人： chq 创建时间： 2018/6/21 16:53
 */
public class BeanCopyUtil {

	public static SpaceResp spaceToSpaceResp(SpaceReq spaceVo) {
		if(spaceVo == null){
			return null;
		}
		SpaceResp spaceResp = new SpaceResp();
		spaceResp.setId(spaceVo.getId());
		spaceResp.setName(spaceVo.getName());
		spaceResp.setIcon(spaceVo.getIcon());
		spaceResp.setPosition(spaceVo.getPosition());
		spaceResp.setParentId(spaceVo.getParentId());
		spaceResp.setUserId(spaceVo.getUserId());
		spaceResp.setLocationId(spaceVo.getLocationId());
		spaceResp.setCreateBy(spaceVo.getCreateBy());
		spaceResp.setUpdateBy(spaceVo.getUpdateBy());
		spaceResp.setTenantId(spaceVo.getTenantId());
		spaceResp.setType(spaceVo.getType());
		spaceResp.setSort(spaceVo.getSort());
		spaceResp.setStyle(spaceVo.getStyle());
		spaceResp.setDefaultSpace(spaceVo.getDefaultSpace());
		spaceResp.setOrgId(spaceVo.getOrgId());// TODO 类型不统一
		spaceResp.setModel(spaceVo.getModel());
		spaceResp.setSeq(spaceVo.getSeq());
		spaceResp.setDeployId(spaceVo.getDeployId());
		if (Constants.SPACE_ROOM.equalsIgnoreCase(spaceVo.getType()) && Constants.MEETING_TRUE == spaceVo.getModel()) {
			spaceResp.setType(Constants.SPACE_MEETING);
		}
		return spaceResp;
	}

	public static SpaceReq spaceRespToSpaceReq(SpaceResp spaceResp) {
		if(spaceResp == null){
			return null;
		}
		SpaceReq space = new SpaceReq();
		space.setId(spaceResp.getId());
		space.setName(spaceResp.getName());
		space.setIcon(spaceResp.getIcon());
		space.setPosition(spaceResp.getPosition());
		space.setParentId(spaceResp.getParentId());
		space.setUserId(spaceResp.getUserId());
		space.setLocationId(spaceResp.getLocationId());
		space.setCreateBy(spaceResp.getCreateBy());
		space.setUpdateBy(spaceResp.getUpdateBy());
		space.setTenantId(spaceResp.getTenantId());
		space.setType(spaceResp.getType());
		space.setSort(spaceResp.getSort());
		space.setStyle(spaceResp.getStyle());
		space.setDefaultSpace(spaceResp.getDefaultSpace());
		spaceResp.setOrgId(spaceResp.getOrgId());// TODO 类型不统一
		space.setModel(spaceResp.getModel());
		space.setSeq(spaceResp.getSeq());
		return space;
	}

	/**
	 * List<Space> 转 List<SpaceResp>
	 *
	 * @param spaceList
	 * @return
	 */
	public static List<SpaceResp> spaceListToSpaceRespList(List<SpaceReq> spaceList) {
		List<SpaceResp> spaceRespList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(space -> {
				spaceRespList.add(spaceToSpaceResp(space));
			});
		}
		return spaceRespList;
	}

	/**
	 * List<SpaceDevice> 转 List<SpaceDeviceResp>
	 *
	 * @param spaceDeviceList
	 * @return
	 */
	public static List<SpaceDeviceResp> spaceDeviceListToSpaceDeviceRespList(List<SpaceDeviceReq> spaceDeviceList) {
		List<SpaceDeviceResp> spaceDeviceRespList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
			spaceDeviceList.forEach(spaceDevice -> {
				spaceDeviceRespList.add(spaceDeviceReqToSpaceDeviceResp(spaceDevice));
			});
		}
		return spaceDeviceRespList;
	}

	/**
	 * List<SpaceResp> 转 List<Space>
	 *
	 * @param spaceRespList
	 * @return
	 */
	public static List<SpaceReq> spaceRespListToSpaceList(List<SpaceResp> spaceRespList) {
		List<SpaceReq> spaceList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceRespList)) {
			spaceRespList.forEach(spaceResp -> {
				spaceList.add(spaceRespToSpaceReq(spaceResp));
			});
		}
		return spaceList;
	}

	/**
	 * SpaceDevice 转 SpaceDeviceResp
	 *
	 * @param spaceDevice
	 * @return
	 */
	public static SpaceDeviceResp spaceDeviceReqToSpaceDeviceResp(SpaceDeviceReq spaceDevice) {
		SpaceDeviceResp spaceDeviceResp = new SpaceDeviceResp();
		BeanUtil.copyProperties(spaceDevice, spaceDeviceResp);
		return spaceDeviceResp;
	}
	
	/**
	 * SpaceDevice 转 SpaceDeviceResp
	 *
	 * @param spaceDevice
	 * @return
	 */
	public static SpaceDeviceReq SpaceDeviceRespToSpaceDeviceReq(SpaceDeviceResp spaceDeviceResp) {
		SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
		BeanUtil.copyProperties(spaceDeviceResp, spaceDeviceReq);
		return spaceDeviceReq;
	}


	public static SpaceDeviceVo spaceDeviceToSpaceDeviceVo(SpaceDeviceReq spaceDevice) {
		SpaceDeviceVo spaceDeviceVo = new SpaceDeviceVo();

		spaceDeviceVo.setDeviceId(spaceDevice.getDeviceId());
		spaceDeviceVo.setSpaceId(spaceDevice.getSpaceId());
		spaceDeviceVo.setOrder(spaceDevice.getOrder());

		return spaceDeviceVo;
	}

	/**
	 * List<SpaceDeviceReq> 转 List<SpaceDevice>
	 *
	 * @param reqs
	 * @return
	 */
	public static List<SpaceDeviceResp> spaceDeviceReqsToSpaceDevices(List<SpaceDeviceReq> reqs) {
		List<SpaceDeviceResp> spaceDevices = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(reqs)) {
			reqs.forEach(req -> {
				spaceDevices.add(spaceDeviceReqToSpaceDeviceResp(req));
			});
		}
		return spaceDevices;
	}

	public static WarningResp warningReqToWarningResp(WarningReq warning){
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
	public static List<WarningResp> warningListToWarningRespList(List<WarningReq> warnings){
		List<WarningResp> warningResps = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(warnings)) {
			warnings.forEach(req -> {
				warningResps.add(warningReqToWarningResp(req));
			});
		}
		return warningResps;
	}
	/**
	 * List<SpaceResp> setType
	 *
	 * @param spaceRespList
	 * @return
	 */
	public static List<SpaceResp> SpaceRespListSetType(List<SpaceResp> spaceRespList) {
		if (CollectionUtils.isNotEmpty(spaceRespList)) {
        	for(SpaceResp spaceResp:spaceRespList) {
	            if (spaceResp.getType() != null && Constants.SPACE_MEETING.equals(spaceResp.getType())) {
	            	 spaceResp.setType(Constants.SPACE_ROOM);
	            	 spaceResp.setModel(Constants.MEETING_TRUE);
	            }
        	}
        }
		return spaceRespList;
	}

}
