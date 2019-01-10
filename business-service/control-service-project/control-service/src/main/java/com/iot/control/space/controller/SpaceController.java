package com.iot.control.space.controller;

import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.service.ISpaceService;
import com.iot.control.space.vo.*;
import com.iot.file.api.FileApi;
import com.iot.user.api.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController()
public class SpaceController implements SpaceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceController.class);

	@Autowired
	private FileApi fileApi;

	@Autowired
	private UserApi userApi;

	@Autowired
	private ISpaceService spaceService;

	@Override
	public Long save(@RequestBody @Valid SpaceReq spaceReq) {
		return spaceService.save(spaceReq);
	}

	@Override
	public void update(@RequestBody @Valid SpaceReq spaceReq) {
		spaceService.update(spaceReq);
	}

	@Override
	public boolean updateSpaceByCondition(@RequestBody @Valid SpaceReqVo reqVo){
		return spaceService.updateSpaceByCondition(reqVo);
	}

	@Override
	public boolean deleteSpaceBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId) {
		return spaceService.deleteSpaceBySpaceId(tenantId, spaceId);
	}

	@Override
	public boolean deleteSpaceByIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req) {
		return spaceService.deleteSpaceByIds(req);
	}

	@Override
	public SpaceResp findSpaceInfoBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId) {
		return spaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
	}

	@Override
	public List<SpaceResp> findSpaceInfoBySpaceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req) {
		return spaceService.findSpaceInfoBySpaceIds(req);
	}

	@Override
	public List<SpaceResp> findSpaceByParentId(@RequestBody SpaceReq space) {
		return spaceService.findSpaceByParentId(space);
	}

	@Override
	public List<SpaceResp> findSpaceByCondition(@RequestBody @Valid SpaceReq spaceReq) {
		return spaceService.findSpaceByCondition(spaceReq);
	}

	@Override
	public PageInfo findSpacePageByCondition(@RequestBody @Valid SpaceReq spaceReq){
		return spaceService.findSpacePageByCondition(spaceReq);
	}

	@Override
	public int countSpaceByCondition(@RequestBody @Valid SpaceReq spaceReq) {
		return spaceService.countSpaceByCondition(spaceReq);
	}

	/**
	 * 根据用户的的根节点查询空间树结构
	 *
	 * @param locationId
	 * @return
	 * @author wanglei
	 */
	@Override
	public List<Map<String, Object>> findTree(Long locationId, Long tenantId) {
		return spaceService.findTree(locationId,tenantId);
	}
	
	@Override
	public List<SpaceResp> findChild(Long tenantId, Long spaceId){
		return spaceService.findChild(tenantId,spaceId);
	}
}
