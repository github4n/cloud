package com.iot.building.callback.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.building.callback.ListenerCallback;
import com.iot.building.helper.Constants;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.warning.service.IWarningService;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.enums.APIType;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

public class WarningCallback implements ListenerCallback {

    private static final Logger log = LoggerFactory.getLogger(WarningCallback.class);
    private static final String WARNING = "1";
    private IWarningService warningService = ApplicationContextHelper.getBean(IWarningService.class);
    private Environment dnvironment = ApplicationContextHelper.getBean(Environment.class);
    private SpaceDeviceApi spaceServiceApi = ApplicationContextHelper.getBean(SpaceDeviceApi.class);
    private SpaceApi spaceApi = ApplicationContextHelper.getBean(SpaceApi.class);
    
    @Override
    public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType) {
        log.info("************************进入告警****************");
        try {
            if (!(map.containsKey("Alarm")) && !(map.containsKey("Smoke"))) {
                return;
            }
            // 发送告警主题
            saveWarningLog(device, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("************************告警end****************");
    }

    /**
     * 保存告警日志--TODO 如果没有意外需要移动到warningCallback中做
     *
     * @param device
     * @param backMap
     */
    private void saveWarningLog(GetDeviceInfoRespVo device, Map<String, Object> backMap) {
        try {
            if (device != null && Constants.warningTypeMap().containsKey(device.getDeviceTypeId().toString())) {
                WarnInfo(device, backMap);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加告警数据
     *
     * @return
     */
    public void WarnInfo(GetDeviceInfoRespVo device, Map<String, Object> map) throws BusinessException {
        log.info("deviceId=" + device.getUuid() + "=====map:" + map.toString());
        String status = map.get("Alarm") != null ? String.valueOf(map.get("Alarm"))
                : String.valueOf(map.get("Smoke"));// 设备告警状态
        WarningReq warning = new WarningReq();
        try {
            if (status != null && status.equals(WARNING)) {
                List<String> spceNameList = getSpaceListByDevice(device, map);
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = dateformat.format(System.currentTimeMillis());
                String type = map.get("Alarm") != null ? "Sensor_Waterleak" : "Sensor_Smoke";
                map.put("spaceName", spceNameList);
                map.put("deviceId", device.getUuid());
                map.put("businessType", type);//
                map.put("create_time", createTime);// 告警数据创建时间
                String message = JSON.toJSONString(map);
                warning.setDeviceId(device.getUuid());
                warning.setType(type);
                warning.setContent(message);
                warning.setStatus("0");
                warning.setCreateTime(new Date());
                warning.setTenantId(device.getTenantId());
                warning.setLocationId(device.getLocationId());
                warning.setOrgId(device.getOrgId());
                WarningResp resp = warningService.addWarning(warning);
                map.put("id", resp.getId());

                String uuid = dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
                String topic = "iot/v1/c/" + uuid + "/center/warning";
                map.put("tenantId", device.getTenantId());
                BusinessDispatchMqttHelper.sendWarningTopic(map, topic);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("----------------------------告警end  end----------------------------------");
    }

    /**
     * 获取归属空间的名称
     *
     * @param device
     * @return
     */
    private List<String> getSpaceListByDevice(GetDeviceInfoRespVo device, Map<String, Object> parmeMap) {
        List<SpaceDeviceResp> spaceNameMapList = new ArrayList<SpaceDeviceResp>();
        List<String> spaceNameList = new ArrayList<String>();
        if (device != null) {
        	SpaceDeviceReq req=new SpaceDeviceReq();req.setDeviceId(device.getUuid());
            spaceNameMapList = spaceServiceApi.findSpaceDeviceByCondition(req);
        }
        if (CollectionUtils.isNotEmpty(spaceNameMapList) && spaceNameMapList.size() > 0) {
        	List<Long> spaceIds=Lists.newArrayList();
        	spaceNameMapList.forEach(resp_->{spaceIds.add(resp_.getSpaceId());});
        	SpaceReq req=new SpaceReq();req.setSpaceIds(spaceIds);
        	List<SpaceResp> respList=spaceApi.findSpaceByCondition(req);
        	String name="";
            for (SpaceResp resp : respList) {
                if (resp != null && resp.getName() != null) {
                	name=resp.getName();
                    if (resp.getParentId() != null && resp.getParentId().compareTo(-1L)!=0) {
                        Long id = resp.getParentId();
                        while (null != id && id.compareTo(-1L)!=0) {
                            SpaceResp space = spaceApi.findSpaceInfoBySpaceId(device.getTenantId(),id);
                            if (space.getType().equals(Constants.SPACE_FLOOR)) {
                                parmeMap.put("FloorId", space.getId());
                                parmeMap.put("FloorSort", space.getSort());
                            } else if (space.getType().equals(Constants.SPACE_BUILD)) {
                                parmeMap.put("BuildId", space.getId());
                            }
                            name=name+","+space.getName();
                            id = space.getParentId();
                        }
                    }
                    spaceNameList.add(name);
                }
            }
        }
        return spaceNameList;
    }
}
