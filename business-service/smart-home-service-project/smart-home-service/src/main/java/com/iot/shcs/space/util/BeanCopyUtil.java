package com.iot.shcs.space.util;

import com.iot.common.beans.BeanUtil;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;

import com.iot.shcs.helper.Constants;
import com.iot.shcs.space.vo.SpaceReqVo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：bean字段赋值工具类 创建人： chq 创建时间： 2018/6/21 16:53
 */
public class BeanCopyUtil {

	public static com.iot.shcs.space.vo.SpaceReq copyProperties(SpaceReq source) {
		if(source == null){
			return null;
		}
		com.iot.shcs.space.vo.SpaceReq target = new com.iot.shcs.space.vo.SpaceReq();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setIcon(source.getIcon());
		target.setPosition(source.getPosition());
		target.setParentId(source.getParentId());
		target.setUserId(source.getUserId());
		target.setLocationId(source.getLocationId());
		target.setCreateBy(source.getCreateBy());
		target.setUpdateBy(source.getUpdateBy());
		target.setTenantId(source.getTenantId());
		target.setType(source.getType());
		target.setSort(source.getSort());
		target.setStyle(source.getStyle());
		target.setDefaultSpace(source.getDefaultSpace());
		target.setOrgId(source.getOrgId());
		target.setModel(source.getModel());
		target.setSeq(source.getSeq());
		target.setDeployId(source.getDeployId());
		target.setSpaceIds(source.getSpaceIds());
		target.setHomeId(source.getHomeId());
		target.setOffset(source.getOffset());
		target.setPageSize(source.getPageSize());
		if (Constants.SPACE_ROOM.equalsIgnoreCase(source.getType()) && Constants.MEETING_TRUE == target.getModel()) {
			target.setType(Constants.SPACE_MEETING);
		}
		return target;
	}

	public static com.iot.control.space.vo.SpaceReqVo copyProperties(SpaceReqVo source) {
		if(source == null){
			return null;
		}
		com.iot.control.space.vo.SpaceReqVo target = new com.iot.control.space.vo.SpaceReqVo();
		com.iot.shcs.space.vo.SpaceReq setValueParam = source.getSetValueParam();
		com.iot.shcs.space.vo.SpaceReq requireParam = source.getRequstParam();
		SpaceReq targeSetParam = copyProperties(setValueParam);
		SpaceReq targeRequireParam = copyProperties(requireParam);
		target.setSetValueParam(targeSetParam);
		target.setRequstParam(targeRequireParam);

		return target;
	}


	public static SpaceReq copyProperties(com.iot.shcs.space.vo.SpaceReq source) {
		if(source == null){
			return null;
		}
		SpaceReq target = new SpaceReq();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setIcon(source.getIcon());
		target.setPosition(source.getPosition());
		target.setParentId(source.getParentId());
		target.setUserId(source.getUserId());
		target.setLocationId(source.getLocationId());
		target.setCreateBy(source.getCreateBy());
		target.setUpdateBy(source.getUpdateBy());
		target.setTenantId(source.getTenantId());
		target.setType(source.getType());
		target.setSort(source.getSort());
		target.setStyle(source.getStyle());
		target.setDefaultSpace(source.getDefaultSpace());
		target.setOrgId(source.getOrgId());
		target.setModel(source.getModel());
		target.setSeq(source.getSeq());
		target.setDeployId(source.getDeployId());
		target.setSpaceIds(source.getSpaceIds());
		target.setHomeId(source.getHomeId());
		target.setOffset(source.getOffset());
		target.setPageSize(source.getPageSize());
		if (Constants.SPACE_ROOM.equalsIgnoreCase(source.getType()) && Constants.MEETING_TRUE == target.getModel()) {
			target.setType(Constants.SPACE_MEETING);
		}
		target.setMeshName(source.getMeshName());
		target.setMeshPassword(source.getMeshPassword());
		return target;
	}

	public static com.iot.shcs.space.vo.SpaceResp copyProperties(SpaceResp spaceResp) {
		if(spaceResp == null){
			return null;
		}
		com.iot.shcs.space.vo.SpaceResp space = new com.iot.shcs.space.vo.SpaceResp();
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
		space.setOrgId(spaceResp.getOrgId());// TODO 类型不统一
		space.setModel(spaceResp.getModel());
		space.setSeq(spaceResp.getSeq());
		space.setDevNum(spaceResp.getDevNum());
		space.setPageSize(spaceResp.getPageSize());
		space.setOffset(spaceResp.getOffset());
		space.setHomeId(spaceResp.getHomeId());
		space.setDeviceId(spaceResp.getDeviceId());
		space.setDeployId(spaceResp.getDeployId());
		space.setMeshName(spaceResp.getMeshName());
		space.setMeshPassword(spaceResp.getMeshPassword());
		return space;
	}

	public static List<com.iot.shcs.space.vo.SpaceResp> copyProperties(List<SpaceResp> spaceList) {
		List<com.iot.shcs.space.vo.SpaceResp> spaceRespList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(space -> {
				spaceRespList.add(copyProperties(space));
			});
		}
		return spaceRespList;
	}

	public static com.iot.shcs.space.vo.SpaceDeviceVo copyProperties(SpaceDeviceVo spaceDeviceVo){
		if(spaceDeviceVo == null){
			return null;
		}
		com.iot.shcs.space.vo.SpaceDeviceVo spaceDevice = new com.iot.shcs.space.vo.SpaceDeviceVo();
		spaceDevice.setId(spaceDeviceVo.getId());
		spaceDevice.setDeviceId(spaceDeviceVo.getDeviceId());
		spaceDevice.setSpaceId(spaceDeviceVo.getSpaceId());
		spaceDevice.setPosition(spaceDeviceVo.getPosition());
		spaceDevice.setLocationId(spaceDeviceVo.getLocationId());
		spaceDevice.setParentId(spaceDeviceVo.getParentId());
		spaceDevice.setStatus(spaceDeviceVo.getStatus());
		spaceDevice.setDeviceTypeId(spaceDeviceVo.getDeviceTypeId());
		spaceDevice.setTenantId(spaceDeviceVo.getTenantId());
		spaceDevice.setDeviceCategoryId(spaceDeviceVo.getDeviceCategoryId());
		spaceDevice.setBusinessTypeId(spaceDeviceVo.getBusinessTypeId());
		spaceDevice.setProductId(spaceDeviceVo.getProductId());
		spaceDevice.setOrgId(spaceDeviceVo.getOrgId());
		spaceDevice.setUserId(spaceDeviceVo.getUserId());
		spaceDevice.setModel(spaceDeviceVo.getModel());
		spaceDevice.setIcon(spaceDeviceVo.getIcon());
		spaceDevice.setDefaultSpace(spaceDeviceVo.getDefaultSpace());
		spaceDevice.setSeq(spaceDeviceVo.getSeq());
		spaceDevice.setDeployId(spaceDeviceVo.getDeployId());
		spaceDevice.setOrder(spaceDeviceVo.getOrder());
		spaceDevice.setName(spaceDeviceVo.getName());
		spaceDevice.setType(spaceDeviceVo.getType());
		spaceDevice.setCreateTime(spaceDeviceVo.getCreateTime());
		spaceDevice.setUpdateTime(spaceDeviceVo.getUpdateTime());
		spaceDevice.setDeviceType(spaceDeviceVo.getDeviceType());
		spaceDevice.setSort(spaceDeviceVo.getSort());

		return spaceDevice;
	}

	public static List<com.iot.shcs.space.vo.SpaceDeviceVo> copySpaceDeviceVo(List<SpaceDeviceVo> list){
		List<com.iot.shcs.space.vo.SpaceDeviceVo> spaceDeviceVos = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			list.forEach(SpaceDeviceVo -> {
				spaceDeviceVos.add(copyProperties(SpaceDeviceVo));
			});
		}
		return spaceDeviceVos;
	}


	public static SpaceDeviceReq copyProperties(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq) {
		if(spaceDeviceReq == null){
			return null;
		}
		SpaceDeviceReq spaceDevice = new SpaceDeviceReq();
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

		return spaceDevice;
	}

	public static List<SpaceDeviceReq> copySpaceDeviceReq(List<com.iot.shcs.space.vo.SpaceDeviceReq> spaceDeviceReqs) {
		List<SpaceDeviceReq> spaceDeviceReqList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceDeviceReqs)) {
			spaceDeviceReqs.forEach(space -> {
				spaceDeviceReqList.add(copyProperties(space));
			});
		}
		return spaceDeviceReqList;
	}


	public static com.iot.shcs.space.vo.SpaceDeviceResp copyProperties(SpaceDeviceResp spaceDeviceRsp) {
		if(spaceDeviceRsp == null){
			return null;
		}
		com.iot.shcs.space.vo.SpaceDeviceResp spaceDevice = new com.iot.shcs.space.vo.SpaceDeviceResp();
		spaceDevice.setId(spaceDeviceRsp.getId());
		spaceDevice.setDeviceId(spaceDeviceRsp.getDeviceId());
		spaceDevice.setSpaceId(spaceDeviceRsp.getSpaceId());
		spaceDevice.setPosition(spaceDeviceRsp.getPosition());
		spaceDevice.setLocationId(spaceDeviceRsp.getLocationId());
		spaceDevice.setDeviceCategoryId(spaceDeviceRsp.getDeviceCategoryId());
		spaceDevice.setStatus(spaceDeviceRsp.getStatus());
		spaceDevice.setTenantId(spaceDeviceRsp.getTenantId());
		spaceDevice.setDeviceTypeId(spaceDeviceRsp.getDeviceTypeId());
		spaceDevice.setOrder(spaceDeviceRsp.getOrder());
		spaceDevice.setBusinessTypeId(spaceDeviceRsp.getBusinessTypeId());
		spaceDevice.setProductId(spaceDeviceRsp.getProductId());
		spaceDevice.setCreateTime(spaceDeviceRsp.getCreateTime());
		spaceDevice.setUpdateTime(spaceDeviceRsp.getUpdateTime());


		return spaceDevice;
	}

	public static List<com.iot.shcs.space.vo.SpaceDeviceResp> copySpaceDeviceResp(List<SpaceDeviceResp> spaceDeviceResps) {
		List<com.iot.shcs.space.vo.SpaceDeviceResp> spaceDeviceRespList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceDeviceResps)) {
			spaceDeviceResps.forEach(space -> {
				spaceDeviceRespList.add(copyProperties(space));
			});
		}
		return spaceDeviceRespList;
	}

	public static SpaceDeviceReqVo copyProperties(com.iot.shcs.space.vo.SpaceDeviceReqVo reqVo){
		if(reqVo == null){
			return null;
		}
		SpaceDeviceReqVo req = new SpaceDeviceReqVo();
		com.iot.shcs.space.vo.SpaceDeviceReq sourRequst = reqVo.getRequstParam();
		SpaceDeviceReq targetRequst = copyProperties(sourRequst);
		com.iot.shcs.space.vo.SpaceDeviceReq sourSetValue = reqVo.getSetValueParam();
		SpaceDeviceReq targetSetValue = copyProperties(sourSetValue);
		req.setRequstParam(targetRequst);
		req.setSetValueParam(targetSetValue);
		return req;
	}

	public static SpaceAndSpaceDeviceVo copyProperties(com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo reqVo){
		if(reqVo == null){
			return null;
		}
		SpaceAndSpaceDeviceVo req = new SpaceAndSpaceDeviceVo();
		req.setSpaceIds(reqVo.getSpaceIds());
		req.setDeviceIds(reqVo.getDeviceIds());
		req.setTenantId(reqVo.getTenantId());
		req.setUserId(reqVo.getUserId());
		return req;
	}


	public static com.iot.shcs.space.vo.SpaceReq spaceRespToSpaceReq(com.iot.shcs.space.vo.SpaceResp spaceResp) {
		if(spaceResp == null){
			return null;
		}
		com.iot.shcs.space.vo.SpaceReq space = new com.iot.shcs.space.vo.SpaceReq();
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
		space.setOrgId(spaceResp.getOrgId());// TODO 类型不统一
		space.setModel(spaceResp.getModel());
		space.setSeq(spaceResp.getSeq());
		return space;
	}

	public static com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceRespToSpaceDeviceReq(com.iot.shcs.space.vo.SpaceDeviceResp spaceDeviceResp) {
		if(spaceDeviceResp == null){
			return null;
		}
		com.iot.shcs.space.vo.SpaceDeviceReq spaceDevice = new com.iot.shcs.space.vo.SpaceDeviceReq();
		spaceDevice.setId(spaceDeviceResp.getId());
		spaceDevice.setDeviceId(spaceDeviceResp.getDeviceId());
		spaceDevice.setSpaceId(spaceDeviceResp.getSpaceId());
		spaceDevice.setPosition(spaceDeviceResp.getPosition());
		spaceDevice.setLocationId(spaceDeviceResp.getLocationId());
		spaceDevice.setDeviceCategoryId(spaceDeviceResp.getDeviceCategoryId());
		spaceDevice.setStatus(spaceDeviceResp.getStatus());
		spaceDevice.setTenantId(spaceDeviceResp.getTenantId());
		spaceDevice.setDeviceTypeId(spaceDeviceResp.getDeviceTypeId());
		spaceDevice.setOrder(spaceDeviceResp.getOrder());
		spaceDevice.setBusinessTypeId(spaceDeviceResp.getBusinessTypeId());
		spaceDevice.setProductId(spaceDeviceResp.getProductId());
		spaceDevice.setCreateTime(spaceDeviceResp.getCreateTime());
		spaceDevice.setUpdateTime(spaceDeviceResp.getUpdateTime());

		return spaceDevice;
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
