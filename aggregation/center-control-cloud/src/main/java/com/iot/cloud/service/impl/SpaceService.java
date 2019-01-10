package com.iot.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.cloud.helper.BusinessExceptionEnum;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.List;

@Service
public class SpaceService {

	@Autowired
	private SpaceApi spaceApi;

	public List<SpaceResp> getSpaceByCondition(SpaceReq spaceReq) {
		return spaceApi.getSpaceByCondition(spaceReq);
	}

	public SpaceResp findById(Long spaceId) {
		return spaceApi.findById(spaceId);
	}

	public void save(SpaceReq spaceReq) {
		spaceApi.save(spaceReq);
	}

	public void update(SpaceReq spaceReq) {
		spaceApi.update(spaceReq);
	}

	public void delete(Long id) {
		spaceApi.deleteSpaceById(id);
	}

	public List<SpaceResp> findSpaceByTenantId(Long tenantId) {
		return spaceApi.findSpaceByTenantId(tenantId);
	}

	public List<LocationResp> getLocationByTenantId(Long tenantId) throws BusinessException {
		LocationReq locationReq = new LocationReq();
		locationReq.setTenantId(tenantId);
		return spaceApi.findLocationByCondition(locationReq);
	}


}
