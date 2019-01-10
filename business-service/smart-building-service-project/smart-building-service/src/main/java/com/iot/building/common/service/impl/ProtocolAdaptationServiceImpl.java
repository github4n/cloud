package com.iot.building.common.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.iot.building.common.service.IProtocolAdaptationService;
import com.iot.building.common.vo.ProtocolParamVo;
import com.iot.building.gateway.MultiProtocolUDPClient;
import com.iot.building.helper.Constants;
import com.iot.building.helper.ProtocolConstants;
import com.iot.building.listener.MQTTClientListener;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceExtendsCoreApi;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;

@Service
@Transactional
public class ProtocolAdaptationServiceImpl implements IProtocolAdaptationService {
	
	@Autowired
	Environment environment;
	@Autowired
	DeviceCoreApi deviceCoreApi;
	@Autowired
	DeviceExtendsCoreApi deviceExtendsCoreApi;
	@Autowired
	IBuildingSpaceService spaceService;
	@Autowired
	SpaceApi spaceApi;
	@Autowired
	SpaceDeviceApi spaceDeviceApi;
	@Autowired
	private MqttSdkService mqttSdkService;
	
	private static final String VENDOR="vendor";//厂商
	private static final String METHOD="method";//方法
	private static final String DEFAULT_ENCODING="UTF-8";//默认编码
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ProtocolAdaptationServiceImpl.class);
	
	/**
	 * 匹配替换占位符的key
	 * @param key
	 * @param protocolParamVo
	 * @return
	 */
	private String findKeyToValue(String key,ProtocolParamVo protocolParamVo) {
		GetDeviceInfoRespVo deviceResp=deviceCoreApi.get(protocolParamVo.getDeviceId());
		switch (key) {
		case ProtocolConstants.DEVICE_ID:
			return deviceResp.getUuid();
		case ProtocolConstants.DEVICE_NAME:
			return deviceResp.getName();
		case ProtocolConstants.GATEWAY_ID:
			return deviceResp.getParentId();
		case ProtocolConstants.IP:
			return deviceResp.getIp();
		case ProtocolConstants.IS_DIRECT_DEVICE:
			return deviceResp.getIsDirectDevice().toString();
		case ProtocolConstants.DEVICE_TYPE_ID:
			return deviceResp.getDeviceTypeId().toString();
		case ProtocolConstants.PRODUCT_ID:
			return deviceResp.getProductId().toString();
		case ProtocolConstants.SPACE_NAME:
			return getSpaceName(protocolParamVo);
		case ProtocolConstants.UUID:
			return environment.getProperty(Constants.AGENT_MQTT_USERNAME);
		case ProtocolConstants.DEVICE_PASSWORD:
			//获取设备密码
	        return getDevicePwd(protocolParamVo);
		case ProtocolConstants.ATTR:
			return dataFromatAttr(protocolParamVo);
		default:
			return null;
		}
	}

	/**
	 * 协议格式
	 * @param protocolParamVo
	 * @return
	 */
	private String dataFromatAttr(ProtocolParamVo protocolParamVo) {
		String dateFormat=getEnvimentProtocolDataformat(protocolParamVo);
		if(dateFormat.equals(ProtocolConstants.JSON_FORMAT)) {
			Map<String,Object> sortMap=sortMap(protocolParamVo.getControlParams());
			LOGGER.info("()()()()()()()() new new value======"+ JSON.toJSONString(sortMap));
			return JSON.toJSONString(sortMap);
		}else if(dateFormat.equals(ProtocolConstants.STRING_FORMAT)) {
			return protocolParamVo.getControlParams().get(ProtocolConstants.ATTR).toString();
		}//新的格式 继续添加
		return null;
	}

	/**
	 * 获取设备密码
	 * @param protocolParamVo
	 * @return
	 */
	private String getDevicePwd(ProtocolParamVo protocolParamVo) {
		String password = deviceExtendsCoreApi.get(protocolParamVo.getTenantId(), protocolParamVo.getDeviceId()).getDeviceCipher();
		return password;
	}

	/**
	 * 获取空间名称
	 * @param protocolParamVo
	 * @return
	 */
	private String getSpaceName(ProtocolParamVo protocolParamVo) {
		SpaceResp resp=null;
		SpaceDeviceReq spaceDeviceReq=new SpaceDeviceReq();
		spaceDeviceReq.setDeviceId(protocolParamVo.getDeviceId());
		List<SpaceDeviceResp> mapList=spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
		if(CollectionUtils.isNotEmpty(mapList)) {
			resp=spaceApi.findSpaceInfoBySpaceId(protocolParamVo.getTenantId(),mapList.get(0).getSpaceId());
		}
		return resp==null?null:resp.getName();
	}
	
	@Override
	public String controlProtocolAdaptation(ProtocolParamVo protocolParamVo) {
		String body=getEnvimentProtocolBody(protocolParamVo);//加载协议
		return commonBusiness(protocolParamVo, body);
	}

	/**
	 * 公共处理逻辑
	 * @param protocolParamVo
	 * @param body
	 * @return
	 */
	private String commonBusiness(ProtocolParamVo protocolParamVo, String body) {
		body = placeholder(protocolParamVo, body);//占位符
		String protocolType=getEnvimentProtocolType(protocolParamVo);
		switch (protocolType) {
		case ProtocolConstants.MQTT_PROTOCOL:
			mqtt(body);
			break;
		case ProtocolConstants.TCP_PROTOCOL:
			tcp(protocolParamVo, body);
			break;
		default://如果有新的协议开发，继续添加
			break;
		}
		return body;
	}
	
	@Override
	public String queryDeviceStatus(ProtocolParamVo protocolParamVo) {
		String body=getEnvimentProtocolBody(protocolParamVo);//加载协议
		return commonBusiness(protocolParamVo, body);
	}

	/**
	 * 替换占位符
	 * @param protocolParamVo
	 * @param body
	 * @return
	 */
	private String placeholder(ProtocolParamVo protocolParamVo, String body) {
		if(StringUtils.isNotBlank(body)) {
			Matcher m = Pattern.compile(ProtocolConstants.REGULAR).matcher(body);//替换
			while (m.find()) {
				String replacekey=m.group().replace(ProtocolConstants.PREFIX, "").replace(ProtocolConstants.SUFFIX, "");
				body=body.replace(m.group(), findKeyToValue(replacekey, protocolParamVo));
			}
		}
		return body;
	}

	/**
	 * mqtt协议
	 * @param body
	 */
	private void mqtt(String body) {
		MqttMsg msg=JSON.parseObject(body, MqttMsg.class);
		Map<String,Object> newMap=(Map<String,Object>)msg.getPayload();
		if(newMap.containsKey("params")) {
			Map<String,Object> properyt=(Map<String,Object>)newMap.get("params");
			Map<String,Object> sortMap=sortMap(properyt);
			newMap.put("params", sortMap);
		}
		LOGGER.info("last last send format value===="+JSON.toJSONString(msg));
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, ProtocolConstants.QOS);
	}

	/**
	 * 排序把map
	 * @param propertyMap
	 * @return
	 */
	private Map<String, Object> sortMap(Map<String, Object> propertyMap) {
		Map<String,Object> newMap=new LinkedHashMap();
		if(propertyMap.containsKey("OnOff")) {
			newMap.put("OnOff", propertyMap.get("OnOff"));
		}
		for(String key:propertyMap.keySet()) {
			if(!key.equals("OnOff")) {
				newMap.put(key, propertyMap.get(key));
			}
		}
		return newMap;
	}
	
	/**
	 * tcp协议
	 * @param protocolParamVo
	 * @param body
	 */
	private void tcp(ProtocolParamVo protocolParamVo, String body) {
		String encoding=getEnvimentProtocolEncoding(protocolParamVo);//获取编码
		byte[] sendBuf;
		try {
			sendBuf = StringUtils.isBlank(encoding)?body.getBytes(DEFAULT_ENCODING):body.getBytes(encoding);
			MultiProtocolUDPClient.recover(sendBuf);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取该种设备的协议类型
	 * @param deviceType
	 * @return
	 */
	private String getEnvimentProtocolType(ProtocolParamVo protocolParamVo) {
		String key=ProtocolConstants.protocol_type.replace(VENDOR, protocolParamVo.getApiType().name());
		return environment.getProperty(key);
	}
	
	/**
	 * 获取该种设备的协议模板
	 * @param protocolParamVo
	 * @return
	 */
	private String getEnvimentProtocolBody(ProtocolParamVo protocolParamVo) {
		String key=ProtocolConstants.protocol_body.replace(VENDOR, protocolParamVo.getApiType().name())
				.replace(METHOD, protocolParamVo.getMethod());
		return environment.getProperty(key);
	}
	
	/**
	 * 获取该种设备的协议内容格式
	 * @param protocolParamVo
	 * @return
	 */
	private String getEnvimentProtocolDataformat(ProtocolParamVo protocolParamVo) {
		String key=ProtocolConstants.protocol_dataformat.replace(VENDOR, protocolParamVo.getApiType().name());
		return environment.getProperty(key);
	}
	
	/**
	 * 获取该种设备协议内容的编码
	 * @param protocolParamVo
	 * @return
	 */
	private String getEnvimentProtocolEncoding(ProtocolParamVo protocolParamVo) {
		String key=ProtocolConstants.protocol_encoded.replace(VENDOR, protocolParamVo.getApiType().name());
		return environment.getProperty(key);
	}
}
