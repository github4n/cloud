package com.iot.shcs.scene.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.exception.SceneExceptionEnum;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.scene.service.SceneDetailService;
import com.iot.shcs.scene.service.SceneService;
import com.iot.shcs.scene.util.BeanUtils;
import com.iot.shcs.scene.vo.req.ActionVo;
import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.req.SceneDetailReqVo;
import com.iot.shcs.scene.vo.req.SceneReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;
import com.iot.shcs.scene.vo.rsp.SceneDetailVo;
import com.iot.shcs.scene.vo.rsp.SceneResp;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
import com.iot.shcs.voicebox.queue.sender.DeleteSceneSender;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.ToolUtils;
import net.bytebuddy.asm.Advice;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：IOT云平台 模块名称： 功能描述：情景service实现 创建人： wujianlong 创建时间：2017年11月9日 下午2:57:16
 * 修改人： wujianlong 修改时间：2017年11月9日 下午2:57:16
 */
@Service
public class SceneServiceImpl implements SceneService {

	private static final Logger log = LoggerFactory.getLogger(SceneServiceImpl.class);

	private static final int QOS = 1;

	@Autowired
	private SceneApi sceneApi;

	@Autowired
	private DeviceCoreService deviceCoreService;

	@Autowired
	private SpaceDeviceApi spaceDeviceApi;


	@Autowired
	private MqttSdkService mqttSdkService;

	@Autowired
	private UserApi userApi;

	@Autowired
	private ActivityRecordApi activityRecordApi;

	@Autowired
	private UserVirtualOrgApi userVirtualOrgApi;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private SceneDetailService sceneDetailService;

	@Autowired
	private ISpaceService iSpaceService;

	public SceneResp saveScene(SceneReq sceneReq) {
		int existSceneName = sceneApi.countBySceneName(sceneReq.getSceneName(), sceneReq.getCreateBy(), new Long(0));
		if (existSceneName != 0){
			throw new BusinessException(SceneExceptionEnum.SCENE_NAME_IS_EXIST);
		} else {
			com.iot.control.scene.vo.req.SceneReq reuslt = new com.iot.control.scene.vo.req.SceneReq();
			BeanUtils.toCopy(sceneReq,reuslt);
			com.iot.control.scene.vo.rsp.SceneResp sceneResp = sceneApi.insertScene(reuslt);
			SceneResp resultSceneResp = new SceneResp();
			BeanUtils.toCopy(sceneResp,resultSceneResp);
			return resultSceneResp;
		}
	}



	@Override
	public SceneResp updateScene(SceneReq sceneReq) {
		SceneResp scene = new SceneResp();
		int existSceneName = sceneApi.countBySceneName(sceneReq.getSceneName(), sceneReq.getUpdateBy(), sceneReq.getId());
		if (existSceneName == 0) {
			sceneReq.setUploadStatus(0);
			com.iot.control.scene.vo.req.SceneReq sceneReqr = new com.iot.control.scene.vo.req.SceneReq();
			BeanUtils.toCopy(sceneReq,sceneReqr);
			int updateCount = sceneApi.updateScene(sceneReqr);
			if (updateCount == 0) {
				return null;
			}
		} else {
			throw new BusinessException(SceneExceptionEnum.SCENE_NAME_IS_EXIST);
		}
		scene = getSceneById(sceneReq.getId(),sceneReq.getTenantId());
		return scene;
	}


	public List<SceneResp> findSceneRespListByUserId(Long userId,Long tenantId) {
		List<SceneResp> sceneRespList = new ArrayList<>();
		SceneReq sceneReq = new SceneReq();
		sceneReq.setCreateBy(userId);
		sceneReq.setTenantId(tenantId);
		com.iot.control.scene.vo.req.SceneReq sceneReqr = new com.iot.control.scene.vo.req.SceneReq();
		BeanUtils.toCopy(sceneReq,sceneReqr);
		List<com.iot.control.scene.vo.rsp.SceneResp> sceneRespListResut = sceneApi.sceneByParam(sceneReqr);
		BeanUtils.toCopySceneList(sceneRespListResut,sceneRespList);
		if (CollectionUtils.isNotEmpty(sceneRespList)) {
			List<Long> sceneIdList = Lists.newArrayList();
			for (SceneResp resp : sceneRespList) {
				sceneIdList.add(resp.getId());
			}

			Map<Long, Integer> resultMap = sceneApi.countChildBySceneIds(sceneIdList);
			if (resultMap != null) {
				for (SceneResp resp : sceneRespList) {
					Integer childCount = resultMap.get(resp.getId());
					childCount = childCount == null? 0 : childCount;
					resp.setRuleCount(childCount);
				}
			}
		}
		return sceneRespList;
	}
	/**
	 * 通过spaceId获取场景信息
	 *
	 */
	public List<SceneResp> findSceneRespListBySpaceId(Long spaceId,Long tenantId) {
		List<SceneResp> sceneRespList = new ArrayList<>();
		SpaceResp spaceResp=iSpaceService.findSpaceInfoBySpaceId(tenantId,spaceId);
		if(spaceResp==null){
			log.debug("-----findSceneRespListBySpaceId_spaceResp is null");
			return sceneRespList;
		}
		Long userId=spaceResp.getUserId();
		log.debug("-----findSceneRespListBySpaceId sharedSaceId={},userId={}",spaceId,userId);
		return this.findSceneRespListByUserId(userId,tenantId);
	}


	/**
	 * 向网关发起 执行指定sceneId的场景
	 *
	 * @param sceneId
	 */
	@Override
	public CommonResponse excScene(Long sceneId, String seq,Long tenantId) {
		log.info("***** excSceneReq --> 向网关发起执行场景, sceneId={},seq={},tenantId={}", sceneId,seq,tenantId);
		CommonResponse response = new CommonResponse(ResultMsg.SUCCESS);

		SceneResp sceneResp = this.getSceneById(sceneId,tenantId);
		if (sceneResp != null) {
			// 执行结果 0=正常 1=异常
			Integer result = 0;

			try {
				List<String> directDeviceIdList = findSceneDirectDeviceUuidListBySceneId(sceneId,tenantId);
				if (CollectionUtils.isNotEmpty(directDeviceIdList)) {
					if (seq == null) {
						seq = ToolUtils.getUUID().substring(0, 10);
					}

					String userUuid = userApi.getUuid(sceneResp.getCreateBy());
					MqttMsg mqttMsg = new MqttMsg();
					mqttMsg.setService(Constants.SCENE_SERVICE);
					mqttMsg.setMethod(Constants.EXC_SCENEREQ);
					mqttMsg.setSeq(seq);
					mqttMsg.setSrcAddr("0." + userUuid);
					Map<String, Object> payload = new HashMap<>();
					payload.put(Constants.SCENE_ID, String.valueOf(sceneId));
					mqttMsg.setPayload(payload);

					for (String directDeviceId : directDeviceIdList) {
						new Thread(() -> {
							log.info("---------topic:" + Constants.TOPIC_CLIENT_PREFIX + directDeviceId + "====msg:"
									+ mqttMsg);

							mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + directDeviceId + "/scene/"
									+ Constants.EXC_SCENEREQ);
							mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);

							log.info("---------发送完成----------");
						}).start();
					}
				} else {
					response.setCode(ResultMsg.FAIL.getCode());
					response.setDesc(localeMessageSourceService.getMessage(SceneExceptionEnum.SCENE_ACTION_IS_EMPTY.getMessageKey()));
					log.info("***** excSceneReq --> 执行场景失败, 因为 directDeviceIdList is empty");
				}

			} catch (Exception e) {
				log.info("***** excSceneReq --> 执行场景失败, 因为:{}", e);
				response.setCode(ResultMsg.FAIL.getCode());
				response.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
				result = 1;
			}

			saveSceneActivityRecord(sceneResp, result);
		} else {
			response.setCode(ResultMsg.FAIL.getCode());
			response.setDesc(localeMessageSourceService.getMessage(SceneExceptionEnum.SCENE_NOT_EXIST.getMessageKey()));
			log.info("***** excSceneReq --> 执行场景失败, 因为sceneId={}的记录不存在", sceneId);
		}

		return response;
	}

	/**
	 * 保存 情景执行日志
	 *
	 * @param scene
	 * @param result
	 */
	private void saveSceneActivityRecord(SceneResp scene, Integer result) {
		if (scene != null) {
			// 保存情景日志
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("“" + scene.getSceneName() + "”").append(" has been operated");

			ActivityRecordReq activityRecordReq = new ActivityRecordReq();
			activityRecordReq.setActivity(stringBuilder.toString());
			activityRecordReq.setIcon(scene.getIcon());
			activityRecordReq.setForeignId(scene.getId().toString());
			activityRecordReq.setType(Constants.ACTIVITY_RECORD_SCENE);
			activityRecordReq.setCreateBy(scene.getCreateBy());
			activityRecordReq.setResult(result);

			UserDefaultOrgInfoResp userOrgInfoResp = userVirtualOrgApi
					.getDefaultUsedOrgInfoByUserId(scene.getCreateBy());
			if (userOrgInfoResp != null) {
				activityRecordReq.setTenantId(userOrgInfoResp.getTenantId());
				activityRecordReq.setOrgId(userOrgInfoResp.getId());
			}
			List<ActivityRecordReq> list = new ArrayList<>();
			list.add(activityRecordReq);
			activityRecordApi.saveActivityRecord(list);
		}
	}

	/**
	 * 查找sceneId下关联的 直连设备uuid
	 *
	 * @param sceneId
	 * @return @
	 */
	public List<String> findSceneDirectDeviceUuidListBySceneId(Long sceneId,Long tenantId) {
		log.debug("findSceneDirectDeviceUuidListBySceneId({},{})", sceneId,tenantId);
		// 可以去掉重复
		Map<String, String> directDeviceIdMap = Maps.newHashMap();

		// 可能包含 普通灯、wifi plug、wifi灯
		SceneDetailReq sceneDetailReq = new SceneDetailReq();
		sceneDetailReq.setSceneId(sceneId);
		sceneDetailReq.setTenantId(tenantId);
		com.iot.control.scene.vo.req.SceneDetailReq sceneDetailReqr = new com.iot.control.scene.vo.req.SceneDetailReq();
		BeanUtils.toCopy(sceneDetailReq,sceneDetailReqr);
		List<com.iot.control.scene.vo.rsp.SceneDetailResp> sceneDetailList = sceneApi.sceneDetailByParam(sceneDetailReqr);
		if (CollectionUtils.isNotEmpty(sceneDetailList)) {
			// scene存在子规则循环找出直连设备
			sceneDetailList.forEach(detail -> {
				String directDeviceId = getClientTopicId(detail.getDeviceId() + "");
				directDeviceIdMap.put(directDeviceId, directDeviceId);
			});
		}

		return new ArrayList(directDeviceIdMap.values());
	}

	/**
	 * 获取设备的clientTopicId
	 *
	 * @param deviceId
	 */
	public String getClientTopicId(String deviceId) {
		String topicClientId = deviceId;
		GetDeviceInfoRespVo device = deviceCoreService.getDeviceInfoByDeviceId(topicClientId);

		// 判断是否是直连设备，如果非直连需要找到直连设备的ID
		if (device.getIsDirectDevice() == null || device.getIsDirectDevice() != Constants.IS_DIRECT_DEVICE) {
			topicClientId = device.getParentId();
		}

		return topicClientId;
	}

	@Override
	public void delSceneBySceneId(Long sceneId,Long tenantId,Long userId) {
		SceneResp sceneResp = this.getSceneById(sceneId,tenantId);
		if (sceneResp != null) {
			SceneReq sceneReq = new SceneReq();
			sceneReq.setId(sceneId);
			sceneReq.setTenantId(tenantId);
			sceneReq.setCreateBy(userId);
			com.iot.control.scene.vo.req.SceneReq sceneReqr = new com.iot.control.scene.vo.req.SceneReq();
			BeanUtils.toCopy(sceneReq,sceneReqr);
			sceneApi.deleteScene(sceneReqr);
		}
	}


	@Override
	public SceneResp getSceneById(Long sceneId,Long tenantId) {
		SceneReq sceneReq = new SceneReq();
		sceneReq.setId(sceneId);
		sceneReq.setTenantId(tenantId);
		com.iot.control.scene.vo.req.SceneReq sceneReqr = new com.iot.control.scene.vo.req.SceneReq();
		BeanUtils.toCopy(sceneReq,sceneReqr);
		com.iot.control.scene.vo.rsp.SceneResp sceneResp = sceneApi.sceneById(sceneReqr);
		SceneResp sceneRespr = new SceneResp();
		BeanUtils.toCopy(sceneResp,sceneRespr);
		return sceneRespr;
	}

	@Override
	public List<SceneDetailResp> sceneDetailAndSpaceDeviceList(SceneDetailReq sceneDetailReq) {
		com.iot.control.scene.vo.req.SceneDetailReq sceneDetailReqr = new com.iot.control.scene.vo.req.SceneDetailReq();
		BeanUtils.toCopy(sceneDetailReq,sceneDetailReqr);
		List<com.iot.control.scene.vo.rsp.SceneDetailResp> list = sceneApi.sceneDetailList(sceneDetailReqr);
		List<SceneDetailResp> result = new ArrayList<>();
		list.forEach(m->{
			SceneDetailResp sceneDetailResp = new SceneDetailResp();
			SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
			spaceDeviceReq.setSpaceId(m.getSpaceId());
			spaceDeviceReq.setDeviceId(m.getDeviceId());
			spaceDeviceReq.setLocationId(sceneDetailReq.getLocationId());
			spaceDeviceReq.setTenantId(sceneDetailReq.getTenantId());
			List<SpaceDeviceResp> spaceDeviceResps = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
			spaceDeviceResps.forEach(s->{
				sceneDetailResp.setDeviceId(s.getDeviceId());
			});
			sceneDetailResp.setSceneName(m.getSceneName());
			sceneDetailResp.setSpaceId(m.getSpaceId());
			sceneDetailResp.setTargetValue(m.getTargetValue());
			result.add(sceneDetailResp);
		});
		return result;
	}


	@Override
	public void delBleScene(SceneReq sceneReq){
		Long userId = sceneReq.getCreateBy();
		Long sceneId = sceneReq.getSceneId();
		Long tenantId = sceneReq.getTenantId();
		List<String> directDeviceUuidList = this.findSceneDirectDeviceUuidListBySceneId(sceneId, tenantId);
		FetchUserResp user = userApi.getUser(userId);
		if (user == null) {
			log.error("delSceneReq-system-error, user is null.");
			throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
		}
		if(CollectionUtils.isEmpty(directDeviceUuidList)){
			throw new BusinessException(SceneExceptionEnum.PARAM_SCENE_DETAIL_DEVICE_ID_IS_NULL);
		}
		this.deleteSceneAndSceneDetail(tenantId, sceneId, userId);

	}

	/**
	 *  删除 scene 和 sceneDetail
	 * @param sceneId
	 */
	private void deleteSceneAndSceneDetail(Long tenantId, Long sceneId ,Long userId) {
		if (sceneId != null) {
			SceneResp sceneResp = this.getSceneById(sceneId,tenantId);
			if (userId.compareTo(sceneResp.getCreateBy()) != 0){
				log.debug("deleteSceneAndSceneDetail is error,userId is: {},scene.createBy is {}", userId, sceneResp.getCreateBy());
				throw new BusinessException(SceneExceptionEnum.SCENE_NOT_EXIST);
			}
			sceneDetailService.delSceneDetailBySceneId(sceneId, tenantId,userId);
			this.delSceneBySceneId(sceneId,tenantId,userId);
			//ble设备不支持ifttt控制scene
//			iAutoService.delBySceneId(sceneId,tenantId);
		}
	}

	@Override
	public SceneDetailVo getSceneThen(SceneReq sceneReq){
		Long userId = sceneReq.getCreateBy();
		Long sceneId = sceneReq.getSceneId();
		Long tenantId = sceneReq.getTenantId();
		//检查scene是否存在
		SceneResp sceneResp = this.getSceneById(sceneId,tenantId);
		if(sceneResp == null){
			log.debug("getSceneThen is not exist,sceneId is {}", sceneId);
			throw new BusinessException(SceneExceptionEnum.SCENE_NOT_EXIST);
		}
		//检查用户是否拥有scene
		if (userId.compareTo(sceneResp.getCreateBy()) != 0){
			log.debug("deleteSceneAndSceneDetail is error,userId is: {},scene.createBy is {}", userId, sceneResp.getCreateBy());
			throw new BusinessException(SceneExceptionEnum.SCENE_NOT_EXIST);
		}
		List<Map<String, Object>> thenList = Lists.newArrayList();
		List<SceneDetailResp> sceneDetailList = sceneDetailService.findSceneDetailsBySceneId(sceneId, tenantId);
		if (CollectionUtils.isNotEmpty(sceneDetailList)) {
			sceneDetailList.forEach(sceneDetail -> {
				Map<String, Object> attrMap = null;
				if (StringUtils.isNotBlank(sceneDetail.getTargetValue())) {
					attrMap = JSON.parseObject(sceneDetail.getTargetValue(), Map.class);
				}
				Map<String, Object> thenMap = Maps.newHashMap();
				thenMap.put(Constants.SCENE_THEN_TYPE, "device");
				thenMap.put(Constants.SCENE_THEN_UUID, sceneDetail.getDeviceId());
				thenMap.put(Constants.SCENE_THEN_METHOD, sceneDetail.getMethod());
				thenMap.put(Constants.SCENE_THEN_PARAMS, attrMap);
				thenList.add(thenMap);
			});
		}
		SceneDetailVo result = new SceneDetailVo();
		result.setSceneId(sceneId);
		result.setThen(thenList);
		return result;
	}

	@Override
	public void addOrEditAction(SceneDetailReqVo actionReq){
		Long userId = actionReq.getUserId();
		Long tenantId = actionReq.getTenantId();
		Long sceneId = actionReq.getSceneId();
		ActionVo action = actionReq.getAction();
		String devUuid = action.getUuid();
		String method = action.getMethod();
		String params = JSON.toJSONString(action.getParams());
		String directUuid = null;
		SceneResp scene = this.getSceneById(sceneId, tenantId);
		if (scene == null || userId.compareTo(scene.getCreateBy()) != 0) {
			throw new BusinessException(SceneExceptionEnum.SCENE_NOT_EXIST);
		}
		GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(devUuid);
		if(deviceResp == null){
			throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
		}
		if ((deviceResp.getIsDirectDevice() != null && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE)
                ||  Constants.IS_APP_DEV == deviceResp.getIsAppDev()) {
			directUuid = deviceResp.getUuid();
		}else{
			directUuid = deviceResp.getParentId();
		}
		if (StringUtil.isEmpty(directUuid)) {
			throw new BusinessException(SceneExceptionEnum.SCENE_UNBOUND_DIRECT_DEVICE);
		}

		SceneDetailReq sceneDetailReq = new SceneDetailReq();
		sceneDetailReq.setSceneId(sceneId);
		sceneDetailReq.setDeviceId(devUuid);
		sceneDetailReq.setSpaceId(scene.getSpaceId());
		sceneDetailReq.setMethod(method);
		sceneDetailReq.setTargetValue(params);
		sceneDetailReq.setCreateBy(userId);
		sceneDetailReq.setUpdateBy(userId);
		sceneDetailReq.setTenantId(tenantId);
		log.info("insertOrUpdateSceneDetail {}", JSONObject.toJSONString(sceneDetailReq));
		sceneDetailService.insertOrUpdateSceneDetail(sceneDetailReq);

	}

	public void delAction(SceneDetailReqVo actionReq){
		Long userId = actionReq.getUserId();
		Long tenantId = actionReq.getTenantId();
		Long sceneId = actionReq.getSceneId();
		ActionVo action = actionReq.getAction();
		String devUuid = action.getUuid();
		String directUuid = null;
		SceneResp scene = this.getSceneById(sceneId, tenantId);
		if (scene == null || userId.compareTo(scene.getCreateBy()) != 0 ) {
			throw new BusinessException(SceneExceptionEnum.SCENE_NOT_EXIST);
		}
		GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(devUuid);
		if(deviceResp == null){
			throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
		}
		if (deviceResp.getIsDirectDevice() != null && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
			directUuid = deviceResp.getUuid();
		}else{
			directUuid = deviceResp.getParentId();
		}
		if (StringUtil.isEmpty(directUuid)) {
			throw new BusinessException(SceneExceptionEnum.SCENE_UNBOUND_DIRECT_DEVICE);
		}
		sceneDetailService.delSceneDetailBySceneIdAndDeviceId(sceneId, devUuid, tenantId);
	}


}
