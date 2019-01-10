package com.iot.robot.transform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.SmartDataPointApi;
import com.iot.device.vo.rsp.DeviceFunResp;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import com.iot.redis.RedisCacheUtil;
import com.iot.robot.abilty.alexa.EndpointHealth;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.vo.DeviceInfo;
import com.iot.robot.vo.Endpoint;
import com.iot.robot.vo.ResponsePost;
import com.iot.robot.vo.alexa.AlexaEndpoint;
import com.iot.robot.vo.google.GoogleEndpoint;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.VoiceBoxConfigApi;
import com.iot.tenant.vo.req.VoiceBoxConfigReq;
import com.iot.tenant.vo.resp.VoiceBoxConfigResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
public abstract class AbstractTransfor implements KeyValTransfor, ResponseTransfor {
	
	private Logger log = LoggerFactory.getLogger(AbstractTransfor.class);
	private final String TYPE_NAME_PREFIX = "type.";
	public abstract Endpoint deviceHandle(DeviceInfo device);
	public abstract Endpoint sceneHandle(SceneResp device);
	public abstract String getKey();
	public abstract Integer getKeyEnum();
	public abstract String getBasePrefix();

	@Autowired
	public SmartDataPointApi smartDataPointApi;
	@Autowired
	public DeviceTypeApi deviceTypeApi;
	@Autowired
	public VoiceBoxConfigApi voiceBoxConfigApi;


	//反射功能
	protected Object reflectCap(String className) {
		if (StringUtil.isEmpty(className)) {
			throw new IllegalStateException("classname is null");
		}
		try {
			return Class.forName(className).getMethod("getInstance", null).invoke(null, null);
		} catch (Exception e) {
			throw new IllegalStateException("is not find method getInstance");
		}
		
	}

	@Override
	public ResponsePost getResponse(List<Object>[] resources) {
		return null;
	}
	
    protected <T> T getResponse(List<Object>[] resources, Class<T> type) {
		if (ResponsePost.class.isAssignableFrom(type))
		try {
			ResponsePost res = (ResponsePost) type.newInstance();
			for (List<Object> list : resources) {
				list.forEach((t)-> {
					Endpoint e = this.exec(t);
					if (e != null)
						res.addEndpoint(e);
				});
			}
			return (T) res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    //处理设备列表
    protected <T> T deviceHandle(DeviceInfo device, Class<T> type) {
    	StringBuilder sb = new StringBuilder();
		if (!Endpoint.class.isAssignableFrom(type)) {
			return null;
		}
		try {
			Long tenantId = SaaSContextHolder.currentTenantId();

		    // 终端设备
			Endpoint e = (Endpoint) type.newInstance();
			e.setEndpointId(device.getDeviceId());
			e.setEndpointName(device.getDeviceName());
			
			// 设备类型
			String endpointType = deviceTypeApi.getSmartCode(getKeyEnum(),device.getDeviceTypeId());
			if (endpointType == null) {
				endpointType = "";
			}
			e.addDisplayCategories(endpointType);

			String companyName = null;
			VoiceBoxConfigReq req = null;
			VoiceBoxConfigResp resp = null;
			if (AlexaEndpoint.class.isAssignableFrom(type)) {
				req = VoiceBoxConfigReq.createAlexaSmartHomeVoiceBoxConfigReq(tenantId);
				resp = voiceBoxConfigApi.getByVoiceBoxConfigReq(req);
				if (resp != null) {
					companyName = resp.getCompanyName();
				}
			} else if(GoogleEndpoint.class.isAssignableFrom(type)){
				req = VoiceBoxConfigReq.createGoogleHomeSmartHomeVoiceBoxConfigReq(tenantId);
				resp= voiceBoxConfigApi.getByVoiceBoxConfigReq(req);
				if (resp != null) {
					companyName = resp.getCompanyName();
				}
			}

			// 制造商名称
			e.addManufacturerName(companyName);

			if (StringUtil.isNotBlank(endpointType)) {
				DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(device.getDeviceTypeId());
				if (deviceTypeResp != null) {
					String deviceTypeName = deviceTypeResp.getName();
					if (StringUtil.isNotBlank(deviceTypeName)) {
						deviceTypeName = deviceTypeName.replaceAll("_", " ");
					}
					e.addDescription(deviceTypeName + " connected via " + companyName);
				} else {
					e.addDescription(endpointType + " connected via " + companyName);
				}
			}

			//List<DeviceFunResp> fs = device.getDeviceFunList();
			List<ServiceModulePropertyResp> fs = device.getServiceModulePropertyList();
			if (CollectionUtils.isNotEmpty(fs)) {
				for (ServiceModulePropertyResp f : fs) {
					if (f == null) {
						log.info("***** current ServiceModulePropertyResp is null");
						continue;
					}

					// 获取云端 功能点 对应的 第三方的功能点
					String yunPropertyCode = f.getCode();
					Long propertyId = f.getId();

					String smartCode = null;
					SmartDataPointResp smartDataPointResp = smartDataPointApi.getSmartDataPoint(tenantId, propertyId, getKeyEnum());
					log.info("***** propertyId={}, smartDataPointResp={}", propertyId, JSON.toJSONString(smartDataPointResp));
					if (smartDataPointResp != null) {
						smartCode = smartDataPointResp.getSmartCode();
					}

					if (smartCode != null) {
						Object cap = this.reflectCap(getBasePrefix() + smartCode);
						if (cap != null) {
							e.addCapabilities(cap);
						}
					} else {
						sb.append(yunPropertyCode);
						sb.append(" ");
					}
				}
			}

			// 填充 addAttributes
			addAttributes(e);

			if (e.capSize() == 0) {
				log.info("***** deviceId={}, e.capSize=0, all property are not support, unSupport={}", device.getDeviceId(), sb.toString());
				return null;
			} else {
				if (sb.length() > 0) {
					log.info("***** deviceId={}, e.capSize={}, some property not support, unSupport={}", device.getDeviceId(), e.capSize(), sb.toString());
				}
			}

			if (AlexaEndpoint.class.isAssignableFrom(type)) {
				e.addCapabilities(new EndpointHealth());
			}

			return (T) e;
		} catch (Exception e) {
			log.info("***** function mapping exception");
			e.printStackTrace();
		}
		return null;
	}
    //处理情景列表
    protected <T> T sceneHandle(SceneResp scene, Class<T> type) {
    	StringBuilder sb = new StringBuilder();
    	if (!Endpoint.class.isAssignableFrom(type)) {
    		return null;
    	}

    	try {
    	    // 终端情景
    		Endpoint e = (Endpoint) type.newInstance();
    		e.setEndpointId(String.valueOf(scene.getId()));
    		e.setEndpointName(scene.getSceneName());

    		// 类型
    		e.addDisplayCategories(getSceneCategory());

			e.addDescription("this is "+scene.getSceneName()+" scene");

    		// 能力（功能点）
			String suffix = getSceneCapabilitiesSuffix();
			Object cap = this.reflectCap(getBasePrefix() + suffix);
			if (cap != null) {
				e.addCapabilities(cap);
			}

    		return (T) e;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
	
    /**
     * 将第三方 控制属性 --> robot控制属性 
     */
	@Override
	public KeyValue toCommonKeyVal(JSONObject commond) {
		return null;
	}
	//把设备或情境转换为alexa或google的设备点
	protected Endpoint exec(Object obj) {
		Endpoint e = null;
		if (obj.getClass().isAssignableFrom(DeviceInfo.class)) {
			DeviceInfo device = (DeviceInfo) obj;
			log.info("***** exec, current deviceId={}", device.getDeviceId());

			if (device.getServiceModulePropertyList() != null && !device.getServiceModulePropertyList().isEmpty()) {
				// 抛弃没有功能点的设备
				e = deviceHandle((DeviceInfo) obj);
			} else {
				log.debug("***** device.funList is empty, device.json={}", JSON.toJSONString(device));
			}
		}else if (obj.getClass().isAssignableFrom(SceneResp.class)) {
			SceneResp scene = (SceneResp) obj;
			e = sceneHandle(scene);
		}
		return e;
	}

	// 获取设备在线状态
	public abstract JSONObject getConnectivity(String onlineStatus);

	// 填充 addAttributes
	public abstract Endpoint addAttributes(Endpoint e);

	// 获取 scene功能点名称 后缀
	public abstract String getSceneCapabilitiesSuffix();

	// 获取 scene 归属的类别
	public abstract String getSceneCategory();
}
