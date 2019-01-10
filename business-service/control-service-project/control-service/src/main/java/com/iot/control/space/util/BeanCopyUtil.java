package com.iot.control.space.util;

import com.iot.common.beans.BeanUtil;
import com.iot.control.space.domain.Space;
import com.iot.control.space.domain.SpaceDevice;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：bean字段赋值工具类 创建人： chq 创建时间： 2018/6/21 16:53
 */
public class BeanCopyUtil {

	public static SpaceResp spaceToSpaceResp(Space spaceVo) {
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
		spaceResp.setMeshName(spaceVo.getMeshName());
		spaceResp.setMeshPassword(spaceVo.getMeshPassword());
		return spaceResp;
	}

	public static Space spaceRespToSpace(SpaceResp spaceResp) {
		if(spaceResp == null){
			return null;
		}
		Space space = new Space();
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
		space.setOrgId(spaceResp.getOrgId());
		space.setModel(spaceResp.getModel());
		space.setSeq(spaceResp.getSeq());
		space.setDeployId(spaceResp.getDeployId());
		space.setMeshName(spaceResp.getMeshName());
		space.setMeshPassword(spaceResp.getMeshPassword());
		return space;
	}

	/**
	 * List<Space> 转 List<SpaceResp>
	 *
	 * @param spaceList
	 * @return
	 */
	public static List<SpaceResp> spaceListToSpaceRespList(List<Space> spaceList) {
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
	public static List<SpaceDeviceResp> spaceDeviceListToSpaceDeviceRespList(List<SpaceDevice> spaceDeviceList) {
		List<SpaceDeviceResp> spaceDeviceRespList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
			spaceDeviceList.forEach(spaceDevice -> {
				spaceDeviceRespList.add(spaceDeviceToSpaceDeviceResp(spaceDevice));
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
	public static List<Space> spaceRespListToSpaceList(List<SpaceResp> spaceRespList) {
		List<Space> spaceList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceRespList)) {
			spaceRespList.forEach(spaceResp -> {
				spaceList.add(spaceRespToSpace(spaceResp));
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
	public static SpaceDeviceResp spaceDeviceToSpaceDeviceResp(SpaceDevice spaceDevice) {
		SpaceDeviceResp spaceDeviceResp = new SpaceDeviceResp();
		BeanUtil.copyProperties(spaceDevice, spaceDeviceResp);
		return spaceDeviceResp;
	}

	/**
	 * spaceReq 转换 Space
	 *
	 * @param spaceReq
	 * @return
	 */
	public static Space spaceReqToSpace(SpaceReq spaceReq) {
		Space space = new Space();
		space.setId(spaceReq.getId());
		space.setName(spaceReq.getName());
		space.setIcon(spaceReq.getIcon());
		space.setPosition(spaceReq.getPosition());
		space.setParentId(spaceReq.getParentId());
		space.setUserId(spaceReq.getUserId());
		space.setLocationId(spaceReq.getLocationId());
		space.setCreateBy(spaceReq.getCreateBy());
		space.setUpdateBy(spaceReq.getUpdateBy());
		space.setTenantId(spaceReq.getTenantId());
		space.setType(spaceReq.getType());
		space.setSort(spaceReq.getSort());
		space.setStyle(spaceReq.getStyle());
		space.setDefaultSpace(spaceReq.getDefaultSpace());
		space.setOrgId(spaceReq.getOrgId());
		space.setCreateTime(spaceReq.getCreateTime());
		space.setUpdateTime(spaceReq.getUpdateTime());
		space.setDeployId(spaceReq.getDeployId());
		space.setSeq(spaceReq.getSeq());
		// BeanUtil.copyProperties(spaceReq, space);
		if (space.getType() != null && Constants.SPACE_MEETING.equals(space.getType())) {
			space.setType(Constants.SPACE_ROOM);
			space.setModel(Constants.MEETING_TRUE);
		}
		space.setMeshName(spaceReq.getMeshName());
		space.setMeshPassword(spaceReq.getMeshPassword());
		return space;
	}

	public static SpaceDeviceVo spaceDeviceToSpaceDeviceVo(SpaceDevice spaceDevice) {
		SpaceDeviceVo spaceDeviceVo = new SpaceDeviceVo();

		spaceDeviceVo.setDeviceId(spaceDevice.getDeviceId());
		spaceDeviceVo.setSpaceId(spaceDevice.getSpaceId());
		spaceDeviceVo.setOrder(spaceDevice.getOrder());

		return spaceDeviceVo;
	}

	/**
	 * spaceDevoceReq 转换 SpaceDevice
	 *
	 * @param spaceDeviceReq
	 * @return
	 */
	public static SpaceDevice spaceDeviceReqToSpaceDevice(SpaceDeviceReq spaceDeviceReq) {
		SpaceDevice spaceDevice = new SpaceDevice();
		spaceDevice.setId(spaceDeviceReq.getId());
		spaceDevice.setDeviceId(spaceDeviceReq.getDeviceId());
		spaceDevice.setSpaceId(spaceDeviceReq.getSpaceId());
		spaceDevice.setPosition(spaceDeviceReq.getPosition());
		spaceDevice.setLocationId(spaceDeviceReq.getLocationId());
		spaceDevice.setDeviceCategoryId(spaceDeviceReq.getDeviceCategoryId());
		spaceDevice.setStatus(spaceDeviceReq.getStatus());
		spaceDevice.setTenantId(spaceDeviceReq.getTenantId());
		spaceDevice.setDeviceTypeId(spaceDeviceReq.getDeviceTypeId());
		spaceDevice.setOrder(spaceDeviceReq.getOrder());
		spaceDevice.setBusinessTypeId(spaceDeviceReq.getBusinessTypeId());
		spaceDevice.setProductId(spaceDeviceReq.getProductId());
		spaceDevice.setCreateTime(spaceDeviceReq.getCreateTime());
		spaceDevice.setUpdateTime(spaceDeviceReq.getUpdateTime());
		spaceDevice.setOrgId(spaceDeviceReq.getOrgId());
		return spaceDevice;
	}

	/**
	 * List<SpaceDeviceReq> 转 List<SpaceDevice>
	 *
	 * @param reqs
	 * @return
	 */
	public static List<SpaceDevice> spaceDeviceReqsToSpaceDevices(List<SpaceDeviceReq> reqs) {
		List<SpaceDevice> spaceDevices = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(reqs)) {
			reqs.forEach(req -> {
				spaceDevices.add(spaceDeviceReqToSpaceDevice(req));
			});
		}
		return spaceDevices;
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
