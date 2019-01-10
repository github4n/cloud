/**
 * 
 */
package com.iot.building.helper;

import com.google.common.collect.Lists;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.template.service.TemplateService;
import com.iot.building.template.vo.req.CreateSceneFromTemplateReq;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.QueryParamReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：情景模板公用方法
 * 创建人： linjihuang@leedarson.com
 * 创建时间：2018年7月17日 上午9:16:10
 * 修改人： linjihuang@leedarson.com
 * 修改时间：2018年7月17日 上午9:16:10
 */
@Service
public class SceneTemplateHelper {
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private SpaceApi spaceApi;
	
	@Autowired
	private UserApi userApi;

	@Autowired
	private IBuildingSpaceService iBuildingSpaceService;
	
	/**
	 * 描述:情景模板生成情景微调到房间
     *
	 * @param spaceId
	 * @param templateId
	 */
	public Long addSceneToRoomByTemplate(Long spaceId, Long templateId, Long userId) {
		FetchUserResp fetchUserResp = userApi.getUser(userId);
		List<String> deviceIds = getAllSpaceDevices(spaceId, null,fetchUserResp.getOrgId(),fetchUserResp.getTenantId());
		CreateSceneFromTemplateReq req = new CreateSceneFromTemplateReq();
		req.setUserId(userId);
		req.setTemplateId(templateId);
		req.setDeviceIdList(deviceIds);
		req.setSpaceId(spaceId);
		req.setLocationId(fetchUserResp.getLocationId());
		Long sceneId = templateService.createSceneFromTemplate(req);
		return sceneId;
	} 
	
	/**
	 * 描述:删除情景微调
     *
	 * @param templateId
	 */
	public void deleteSceneToRoomByTemplate(Long templateId, Long userId) {
		FetchUserResp fetchUserResp = userApi.getUser(userId);
		SpaceTemplateReq spaceTemplateReq = new SpaceTemplateReq();
		spaceTemplateReq.setTenantId(fetchUserResp.getTenantId());
		spaceTemplateReq.setLocationId(fetchUserResp.getLocationId());
		spaceTemplateReq.setCreateBy(userId);
		spaceTemplateReq.setTemplateId(templateId);
		templateService.delSceneFromTemplate(spaceTemplateReq);
	} 

	public List<String> getAllSpaceDevices(Long spaceId, String business,Long orgId,Long tenantId) {
		List<String> deviceIds = Lists.newArrayList();
		List<String> currentDeviceIds = getDevicesBySpace(spaceId, business,orgId,tenantId);
		deviceIds.addAll(currentDeviceIds);
		List<SpaceResp> spaceList = spaceApi.findChild(tenantId, spaceId);
		if (CollectionUtils.isNotEmpty(spaceList)) {
			for (SpaceResp space : spaceList) {
				Long childSpaceId = space.getId();
				List<String> childDeviceIds = getDevicesBySpace(childSpaceId, business,orgId,tenantId);
				deviceIds.addAll(childDeviceIds);
			}
		}
		return deviceIds;
	}
	
	public List<String> getDevicesBySpace(long spaceId, String businessType,Long orgId,Long tenantId) {
		List<Map<String, Object>> deviceList = iBuildingSpaceService.findDeviceByRoom(spaceId,orgId,tenantId);
		List<String> deviceIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(deviceList)) {
			for (Map<String, Object> device : deviceList) {
				deviceIds.add(String.valueOf(device.get("deviceId")));
			}
		}

		return deviceIds;
	}
	
}
