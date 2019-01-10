package com.iot.robot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.beans.CommonResponse;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.activity.vo.rsp.ActivityRecordResp;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.robot.common.constant.AlexaErrorCodeEnum;
import com.iot.robot.common.constant.DeviceAttrConst;
import com.iot.robot.common.constant.ModuleConstants;
import com.iot.robot.common.exception.RobotException;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.transform.convertor.AbstractConvertor;
import com.iot.robot.transform.convertor.ValueConvertor;
import com.iot.robot.transform.convertor.YunKeyValue;
import com.iot.robot.utils.ErrorCodeKeys;
import com.iot.robot.utils.SecurityDescUtils;
import com.iot.robot.utils.VoiceBoxUtil;
import com.iot.robot.utils.google.ReportStateUtil;
import com.iot.robot.vo.google.GoogleHomeReportState;
import com.iot.robot.vo.google.ReportStateVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.vo.FetchUserResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonService {
	private Logger log = LoggerFactory.getLogger(CommonService.class);

	@Autowired
	private ActivityRecordApi activityRecordApi;
	
	@Autowired
	private UserApi userApi;
	@Autowired
	private SmartTokenApi smartTokenApi;
	@Autowired
	private DeviceCoreServiceApi deviceCoreServiceApi;

	@Autowired
	private DeviceTypeCoreApi deviceTypeCoreApi;

	@Autowired
	private ProductCoreApi productCoreApi;

	@Autowired
	private DeviceStateCoreApi deviceStateCoreApi;

	@Autowired
	private List<AbstractConvertor> convertor;

	@Resource(name="googleTransfor")
	private AbstractTransfor googleTransfor;

	@Autowired
	private ServiceModuleApi serviceModuleApi;
	@Autowired
	private ServicePropertyApi servicePropertyApi;


	/**
	 * 	获取 设备属性列表
	 * @param deviceId
	 */
	public List<ServiceModulePropertyResp> findPropertyListByDeviceId(String deviceId) {
		GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
		if (deviceInfoRespVo == null) {
			return null;
		}

		Long productId = deviceInfoRespVo.getProductId();
		if (StringUtils.isEmpty(productId)) {
			return null;
		}

		List<Long> serviceModuleIdList = serviceModuleApi.listServiceModuleIdsByProductId(productId);
		if (CollectionUtils.isEmpty(serviceModuleIdList)) {
			return null;
		}

		List<ServiceModulePropertyResp> serviceModulePropertyList = servicePropertyApi.findPropertyListByServiceModuleIds(serviceModuleIdList);
		return serviceModulePropertyList;
	}



	/**
	 *  把 控制属性(kv) --> YunKeyValue
	 *
	 * @param kv
	 * @param deviceId
	 * @return
	 */
	public YunKeyValue parseKeyValue2YunKeyValue(KeyValue kv, String deviceId) {
		log.info("parseKeyValue2YunKeyValue, deviceId={}, kv={}", deviceId, JSON.toJSONString(kv));

		// 设备拥有的功能点
		//List<DeviceFunResp> funRespList = dataPointApi.findDataPointListByDeviceId(deviceId);
		List<ServiceModulePropertyResp> serviceModulePropertyList = this.findPropertyListByDeviceId(deviceId);
		if (CollectionUtils.isEmpty(serviceModulePropertyList)) {
			log.info("parseKeyValue2YunKeyValue, fail, deviceId={}, serviceModulePropertyList is empty.", deviceId);
			throw new RobotException(ErrorCodeKeys.NOT_SUPPORTED);
		}

		// 功能点code
		/*List<String> funStrList = Lists.newArrayList();
		funRespList.forEach((fun) -> {
			funStrList.add(fun.getPropertyCode());
		});*/
		List<String> funStrList = Lists.newArrayList();
		serviceModulePropertyList.forEach((property) -> {
			funStrList.add(property.getCode());
		});

		log.info("parseKeyValue2YunKeyValue, deviceId={}, funStrList={}", deviceId, JSON.toJSONString(funStrList));

		List<ValueConvertor> convertorList = AbstractConvertor.selectConvertor(convertor, funStrList);
		int index = convertorList.indexOf(kv);

		if (index < 0) {
			// 无法转换为云端的功能点
			log.info("parseKeyValue2YunKeyValue, fail!, index={}, deviceId={}, kv={}", index, deviceId, JSON.toJSONString(kv));
			throw new RobotException(ErrorCodeKeys.NOT_SUPPORTED);
		}

		ValueConvertor c = convertorList.get(index);
		YunKeyValue ykv = c.valueConvert(kv, deviceId);

		log.info("parseKeyValue2YunKeyValue, deviceId={}, ykv={}", deviceId, JSON.toJSONString(ykv));
		return ykv;
	}

	/**
	 * 	设备功能点是否为空
	 *
	 * @param deviceUuid
	 * @return
	 */
	public boolean deviceFunctionIsEmpty(String deviceUuid) {
		//List<DeviceFunResp> deviceFunRespList = dataPointApi.findDataPointListByDeviceId(deviceUuid);
		List<ServiceModulePropertyResp> serviceModulePropertyList = this.findPropertyListByDeviceId(deviceUuid);
		return deviceFunctionIsEmpty(serviceModulePropertyList);
    }
	/**
	 * 	设备功能点是否为空
	 *
	 * @param serviceModulePropertyList
	 * @return
	 */
	public boolean deviceFunctionIsEmpty(List<ServiceModulePropertyResp> serviceModulePropertyList) {
		boolean isEmpty = false;
		if (CollectionUtils.isEmpty(serviceModulePropertyList)) {
			isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 *  是否 ipc设备
	 * @param deviceId
	 * @return
	 */
	public boolean isIPC(String deviceId) {
		boolean isIPC = false;
		GetDeviceTypeInfoRespVo deviceTypeInfo = deviceCoreServiceApi.getDeviceTypeByDeviceId(deviceId);
		if (deviceTypeInfo != null && ModuleConstants.IPC_TYPE_NAME.equals(deviceTypeInfo.getType())) {
			isIPC = true;
		}

		return isIPC;
	}

	/**
	 * 	获取设备属性
	 *
	 * @param deviceId
	 * @return
	 */
	public Map<String, Object> getDeviceStatus(Long tenantId, String deviceId) {
		Map<String, Object> allAttrs = deviceStateCoreApi.get(tenantId, deviceId);
		log.info("***** getDeviceStatus, deviceId={}, all_attrs={}", deviceId, allAttrs);

		return allAttrs;
	}

	/**
	 *  处理 googleHome设备状态数据上报
	 * @param reportStateVoList
	 * @return
	 */
	public void handlerGoogleHomeReportState(List<ReportStateVo> reportStateVoList, String userUuid, String requestId) {
		if (CollectionUtils.isEmpty(reportStateVoList)) {
			log.info("***** handlerGoogleHomeReportState() end. because reportStateVoList is empty.");
			return ;
		}
		if (StringUtil.isBlank(userUuid)) {
			log.info("***** handlerGoogleHomeReportState() end. because userUuid is null.");
			return ;
		}

		FetchUserResp userResp = userApi.getUserByUuid(userUuid);
		if (userResp == null) {
			log.info("***** handlerGoogleHomeReportState() end. because userResp is null.");
			return ;
		}

        GoogleHomeReportState reportState = new GoogleHomeReportState();
        reportState.setAgent_user_id(userUuid);
        if (StringUtil.isNotBlank(requestId)) {
            reportState.setRequestId(requestId);
        }

        for(ReportStateVo vo : reportStateVoList) {
			Map<String, Object> attrMap = vo.getAttrMap();
			if(attrMap == null){
				continue;
			}

			Map<String, Object> resultMap = Maps.newHashMap();
			attrMap.forEach((key, val) -> {
				YunKeyValue ykv = new YunKeyValue();
				ykv.setKey(key);
				ykv.setValue(val);
				log.info("***** handlerGoogleHomeReportState, yun_kv={}", JSON.toJSONString(ykv));

				int n = convertor.indexOf(ykv);
				log.info("***** handlerGoogleHomeReportState, n={}", n);
				if (n != -1) {
					// 云端 --> robot
					KeyValue kv = convertor.get(n).toCommonKV(ykv);
					log.info("***** handlerGoogleHomeReportState, robot_kv={}", JSON.toJSONString(kv));
					if (kv != null) {
						// robot --> 第三方
						JSONObject self = googleTransfor.getSelfKeyVal(kv);
						log.info("***** handlerGoogleHomeReportState, googleHome_kv={}", self);
						if (self != null && !self.isEmpty()) {
							resultMap.put(self.getString("key"), self.get("value"));
						}
					}
				}
			});

			if (resultMap.size() > 0) {
				reportState.addDeviceState(vo.getDeviceId(), resultMap);
			}
		}

		if (reportState.deviceStateSize() == 0) {
			log.info("***** handlerGoogleHomeReportState() end. because reportState.deviceStateSize() = 0");
			return ;
		}

		String jsonResult = JSONObject.toJSONString(reportState);
		log.info("***** handlerGoogleHomeReportState(), jsonResult={}", jsonResult);
		ReportStateUtil.callReportState(jsonResult, userResp.getTenantId());
	}

	/**
	 * 	查询传感器状态
	 *
	 * @param sensorType
	 * @return
	 */
	public String querySensorStatus(Long tenantId, Long userId, String sensorType) {
		log.info("***** querySensorStatus, userId={}, sensorType={}", userId, sensorType);

		if (StringUtil.isBlank(sensorType)) {
			return "Sorry, no such kind of sensor is found.";
		}

		// 需要匹配的传感器类型
		if (sensorType.contains(DeviceAttrConst.DEVICE_TYPE_DOOR_LOCK)) {
		} else if (sensorType.contains(DeviceAttrConst.DEVICE_TYPE_MOTION)) {
		} else {
			return "Sorry, no such kind of sensor is found.";
		}

		// 拥有的设备
		List<DeviceResp> deviceRespList = deviceCoreServiceApi.findDeviceListByUserId(tenantId, userId);
		if (CollectionUtils.isEmpty(deviceRespList)) {
			return "You have not added any sensors.";
		}

		StringBuilder sb = new StringBuilder();
		for (DeviceResp dev : deviceRespList) {
			log.info("***** querySensorStatus, current deviceId={}", dev.getDeviceId());

			if (dev.getIsDirectDevice() == null || dev.getIsDirectDevice() != 1) {
				Long productId = dev.getProductId();
				GetProductInfoRespVo productResp = productCoreApi.getByProductId(productId);
				if (productResp == null) {
					log.info("***** querySensorStatus, current dev, productResp is null");
					continue;
				}

				GetDeviceTypeInfoRespVo deviceType = deviceTypeCoreApi.get(productResp.getDeviceTypeId());
				log.info("***** querySensorStatus, deviceType.jsonStr={}", JSON.toJSONString(deviceType));
				if (deviceType == null) {
					log.info("***** querySensorStatus, current dev, deviceType is null");
					continue;
				}

				if (StringUtil.isBlank(deviceType.getType())) {
					log.info("***** querySensorStatus, current dev, deviceType.type is empty");
					continue;
				}

				Map<String, Object> stateMap = deviceStateCoreApi.get(tenantId, dev.getDeviceId());
				String type = deviceType.getType().toLowerCase();
				if (type.contains(DeviceAttrConst.DEVICE_TYPE_DOOR_LOCK)) {
					// 门磁
					sb.append(dev.getName());

					Integer valDoor = VoiceBoxUtil.getMustInteger(stateMap, DeviceAttrConst.DEVICE_ATTR_DOOR_LOCK_DOOR, 0);
					if (valDoor == 0) {
						sb.append(" is close,");
					} else {
						sb.append(" is open,");
					}
				} else if (type.contains(DeviceAttrConst.DEVICE_TYPE_MOTION)) {
					// motion
					sb.append(dev.getName());

					Integer valMotion = VoiceBoxUtil.getMustInteger(stateMap, DeviceAttrConst.DEVICE_ATTR_MOTION_MOTION, 0);
					if (valMotion == 0) {
						sb.append(" is inactivate,");
					} else {
						sb.append(" is activate,");
					}
				} else {
					log.info("***** querySensorStatus, do not math, current type={}", type);
				}
			}
		}

		if (sb.length() == 0) {
			sb.append("You have not added any sensors.");
		}

		return sb.toString();
	}

	public String getActivityLogs(Long userId) {
        log.info("***** getActivityLogs start");

		StringBuilder sb = new StringBuilder();
		ActivityRecordReq req = new ActivityRecordReq();
		req.setCreateBy(userId);
		req.setType("SECURITY");
		req.setPageNum(1);
		req.setPageSize(5);
		PageInfo<ActivityRecordResp> records = activityRecordApi.queryActivityRecord(req);
		if (records.getList() != null) {
			for (ActivityRecordResp r : records.getList()) {
				//acT时间是UTC
				long acT = r.getTime();
				long now = new Date().getTime();
				String activity = SecurityDescUtils.getModeDesc(r.getIcon());
				String desc = SecurityDescUtils.toDateString(now - acT, "days", "hours", "minutes");
				desc = (activity == null ? r.getActivity() : activity) + "," + desc;
				log.info("desc:{}", desc);
				sb.append(desc.replaceAll("\"", ""));
				sb.append(". ");
			}
		}
		if (sb.length() == 0) {
			sb.append("Recent inactive records.");
		}
		return sb.toString();
	}

	/**
	 * 	处理 alexa 提交 report后返回的数据
	 *
	 * @param userId
	 * @param commonResponse
	 * @return
	 */
	public void dealAlexaReportResult(Long userId, CommonResponse commonResponse) {
		String responseStr = null;
		if (commonResponse != null) {
			responseStr = commonResponse.toString();
		}
		log.info("***** dealAlexaReportResult, commonResponse.toString={}", responseStr);
		if (commonResponse == null) {
			return ;
		}

		if (commonResponse.getCode() > 250) {
			try {
				if (commonResponse.getData() != null) {
					String resultData = (String) commonResponse.getData();

					JSONObject jsonObject = JSON.parseObject(resultData);
					JSONObject payload = jsonObject.getJSONObject("payload");

					if (payload != null) {
						String code = payload.getString("code");
						if (AlexaErrorCodeEnum.SKILL_DISABLED_EXCEPTION.getCode().equals(code)) {
							// 用户技能未启用
							resultData = payload.getString("description");
							log.info("***** dealAlexaReportResult, userId={}, resultData={}", userId, resultData);
							smartTokenApi.deleteSmartTokenByUserIdAndSmart(userId, SmartHomeConstants.ALEXA);

							log.info("***** dealAlexaReportResult, userId={} unlink skill, will delete smartToken", userId);
						} else {
							// 其它错误
							log.info("***** dealAlexaReportResult, receive other error info");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
