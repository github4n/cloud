package com.iot.device.business;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.device.api.GroupApi;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.comm.utils.HitCacheUtils;
import com.iot.device.core.DeviceStateCacheCoreUtils;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceState;
import com.iot.device.queue.sender.DeviceStateSender;
import com.iot.device.service.IDeviceStateMongoService;
import com.iot.device.service.IDeviceStateService;
import com.iot.device.service.IDeviceStatusService;
import com.iot.device.task.DeviceStateTask;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky @Descrpiton: @Date: 15:06 2018/9/28 @Modify by:
 */
@Slf4j
@Component
public class DeviceStateBusinessService {

  @Autowired
  private IDeviceStateMongoService deviceStateMongoService;

  @Autowired
  private IDeviceStatusService deviceStatusService;

  @Autowired
  private IDeviceStateService deviceStateService;

  @Autowired
  private DeviceBusinessService deviceBusinessService;

  @Autowired
  private GroupApi groupApi;

  @Autowired
  private DeviceStateSender deviceStateSender;

  /**
   * 获取设备属性列表
   *
   * @param tenantId
   * @param deviceIds
   * @return
   * @author lucky
   * @date 2018/9/28 15:50
   */
  public Map<String, Map<String, Object>> listDeviceStates(Long tenantId, List<String> deviceIds) {
    Map<String, Map<String, Object>> resultDataMap = Maps.newHashMap();
    if (CollectionUtils.isEmpty(deviceIds)) {
      return resultDataMap;
    }

    deviceIds.forEach(
            deviceId -> {
              resultDataMap.put(deviceId, Maps.newHashMap());
            });

    // 1.获取缓存
    Map<String, Map<String, Object>> cacheDataMap =
            DeviceStateCacheCoreUtils.getCacheDataList(tenantId, deviceIds, VersionEnum.V1);
    List<String> cacheIds = Lists.newArrayList();
    if (!CollectionUtils.isEmpty(cacheDataMap)) {
      for (String cacheId : cacheDataMap.keySet()) {

        Map<String, Object> cacheValueDataMap = cacheDataMap.get(cacheId);
        if (CollectionUtils.isEmpty(cacheValueDataMap)) {
          // 说明缓存为空 未命中 则需要拉数据库
          continue;
        }
        cacheIds.add(cacheId);
      }
    }
    // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
    List<String> noHitIds = HitCacheUtils.getNoHitCacheIds(cacheIds, deviceIds);

    // 3.db获取
    Map<String, Map<String, Object>> dbDataMap =
            this.deviceStateService.findListByDeviceIds(tenantId, noHitIds);
    // 4.缓存db数据
    DeviceStateCacheCoreUtils.cacheDataMap(tenantId, dbDataMap, VersionEnum.V1);
    // 5.组转返回
    if (!CollectionUtils.isEmpty(cacheDataMap)) {
      resultDataMap.putAll(cacheDataMap);
    }
    if (!CollectionUtils.isEmpty(dbDataMap)) {
      resultDataMap.putAll(dbDataMap);
    }
    return resultDataMap;
  }

  public Map<String, Map<String, Object>> listBatchDeviceStates(
          Long tenantId, List<String> deviceIds) {

    return listBatchDeviceStates(tenantId, deviceIds, 15);
  }

  /**
   * 获取设备属性列表[批量获取个数]
   *
   * @param tenantId
   * @param deviceIds
   * @param batchSize 一次获取多少个
   * @return
   * @author lucky
   * @date 2018/9/28 15:50
   */
  public Map<String, Map<String, Object>> listBatchDeviceStates(
          Long tenantId, List<String> deviceIds, int batchSize) {
    if (CollectionUtils.isEmpty(deviceIds)) {
      return Maps.newHashMap();
    }
    Map<String, Map<String, Object>> resultDataMap = Maps.newHashMap();
    deviceIds.forEach(
            deviceId -> {
              resultDataMap.put(deviceId, Maps.newHashMap());
            });
    List<String> batchIds = Lists.newArrayList();
    for (int i = 0; i < deviceIds.size(); i++) {
      batchIds.add(deviceIds.get(i));
      if (i >= 1 && i % batchSize == 0) {
        Map<String, Map<String, Object>> tempResultDataMap =
                this.listDeviceStates(tenantId, batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataMap)) {
          resultDataMap.putAll(tempResultDataMap);
        }
        // 获取一次 并清空batchIds
        batchIds.clear();
      }
    }
    Map<String, Map<String, Object>> tempResultDataMap = this.listDeviceStates(tenantId, batchIds);
    if (!CollectionUtils.isEmpty(tempResultDataMap)) {
      resultDataMap.putAll(tempResultDataMap);
    }
    return resultDataMap;
  }

  /**
   * 获取设备属性详情
   *
   * @param tenantId
   * @param deviceId
   * @return
   * @author lucky
   * @date 2018/9/28 15:50
   */
  public Map<String, Object> getDeviceState(Long tenantId, String deviceId) {
    Map<String, Object> resultDataMap = Maps.newHashMap();
    // 1.获取缓存数据
    Map<String, Object> cacheDataMap =
            DeviceStateCacheCoreUtils.getCacheData(tenantId, deviceId, VersionEnum.V1);
    if (!CollectionUtils.isEmpty(cacheDataMap)) {
      resultDataMap.putAll(cacheDataMap);
      return cacheDataMap;
    }
    // 1.1防止穿透db处理
    // 2.db获取
    Map<String, Object> dbDataMap = this.deviceStateService.getByDeviceId(tenantId, deviceId);
    // 3.缓存db数据
    if (!CollectionUtils.isEmpty(dbDataMap)) {
      DeviceStateCacheCoreUtils.cacheData(tenantId, deviceId, dbDataMap, VersionEnum.V1);
      resultDataMap.putAll(dbDataMap);
    }
    return resultDataMap;
  }

  /**
   * 更新设备状态
   *
   * @param deviceStateReq
   * @return
   * @author lucky
   * @date 2018/9/29 18:06
   */
  public void saveOrUpdate(UpdateDeviceStateReq deviceStateReq) {
    log.debug("deviceState-saveOrUpdate:{}", JSON.toJSONString(deviceStateReq));
    if (deviceStateReq == null || CollectionUtils.isEmpty(deviceStateReq.getStateList())) {
      return;
    }
    String deviceId = deviceStateReq.getDeviceId();
    Long tenantId = deviceStateReq.getTenantId();
    Device orig = deviceBusinessService.getDevice(tenantId, deviceId);
    if (orig == null) {
      log.debug("saveOrUpdate device state device not exist..{}", JSON.toJSONString(deviceStateReq));
      return;
    }
    try {
      // 异步获取数据库数据和更新数据库 统一定时更新到数据库 定时5分钟执行获取所有的cacheState 进行更新到数据库
      log.debug("saveOrUpdate-state-toTaskState-start.deviceId:{}", deviceStateReq.getDeviceId());
      DeviceStateTask.addCacheStateTask(deviceStateReq.getTenantId(), deviceStateReq.getDeviceId());
      log.debug("saveOrUpdate-state-toTaskState-end.deviceId:{}", deviceStateReq.getDeviceId());
    } catch (Exception e) {
      log.debug("saveOrUpdate-state-toTaskState-error-end.deviceId:{}", deviceStateReq.getDeviceId());
    }
    deviceStateMongoService.saveOrUpdate(deviceId, orig.getProductId(), deviceStateReq);
//    List<UpdateDeviceStateReq> deviceStateReqList = Lists.newArrayList();
//    deviceStateReqList.add(deviceStateReq);
//    deviceStateSender.send(DeviceStateMessage.builder().params(deviceStateReqList).build());

    Map<String, Object> cacheUpdateStateMap = Maps.newHashMap();
    if (DeviceStateCacheCoreUtils.checkExistKeyDeviceId(tenantId, deviceId, VersionEnum.V1)) {

      deviceStateReq
              .getStateList()
              .forEach(
                      deviceStateInfoReq -> {
                if (!StringUtils.isEmpty(deviceStateInfoReq.getPropertyName())) {
                  cacheUpdateStateMap.put(
                          deviceStateInfoReq.getPropertyName(), deviceStateInfoReq.getPropertyValue());
                }
                      });
      // 更新缓存
      DeviceStateCacheCoreUtils.cacheData(tenantId, deviceId, cacheUpdateStateMap, VersionEnum.V1);
      return;
    }

    // 获取db数据
    List<DeviceState> targetDeviceStates =
            getTargetDeviceStateList(tenantId, orig, deviceStateReq.getStateList());
    if (CollectionUtils.isEmpty(targetDeviceStates)) {
      return;
    }

    Map<String, Map<String, Object>> targetResultDataMap = Maps.newHashMap();
    if (!CollectionUtils.isEmpty(targetDeviceStates)) {
      targetDeviceStates.forEach(
              deviceState -> {
                Map<String, Object> deviceStatePropertyMap = targetResultDataMap.get(deviceId);
                if (CollectionUtils.isEmpty(deviceStatePropertyMap)) {
                  deviceStatePropertyMap = Maps.newHashMap();
                }
                String propertyName = deviceState.getPropertyName();
                String propertyValue = deviceState.getPropertyValue();
                deviceStatePropertyMap.put(propertyName, propertyValue);

                targetResultDataMap.put(deviceId, deviceStatePropertyMap);
              });
    }
    // 更新至缓存
    DeviceStateCacheCoreUtils.cacheDataMap(tenantId, targetResultDataMap, VersionEnum.V1);
  }

  /**
   * 批量更新设备状态
   *
   * @param deviceStateReqList
   * @return
   * @author lucky
   * @date 2018/9/29 18:06
   */
  public void saveOrUpdateBatch(List<UpdateDeviceStateReq> deviceStateReqList) {
    if (CollectionUtils.isEmpty(deviceStateReqList)) {
      return;
    }
    for (UpdateDeviceStateReq deviceStateReq : deviceStateReqList) {
      this.saveOrUpdate(deviceStateReq);
    }
  }

  public List<DeviceState> getTargetDeviceStateList(
          Long tenantId, Device orig, List<AddCommDeviceStateInfoReq> deviceStateList) {
    List<DeviceState> targetDeviceStates = Lists.newArrayList();
    EntityWrapper<DeviceState> wrapper = new EntityWrapper<>();
    wrapper.eq("device_id", orig.getUuid());
    wrapper.eq("tenant_id", tenantId);//优化 + tenant_id 索引
    List<DeviceState> deviceStates = deviceStateService.selectList(wrapper);
    if (CollectionUtils.isEmpty(deviceStates)) {
      deviceStateList.forEach(
              state -> {
                DeviceState destState = new DeviceState();
                destState.setDeviceId(orig.getUuid());
                destState.setLogDate(new Date());
                destState.setProductId(orig.getProductId());
                destState.setPropertyDesc(state.getPropertyDesc());
                destState.setPropertyName(state.getPropertyName());
                destState.setPropertyValue(state.getPropertyValue());
                destState.setGroupId(state.getGroupId());
                destState.setTenantId(tenantId);
                targetDeviceStates.add(destState);
              });
    } else {
      for (int j = 0; j < deviceStateList.size(); j++) {
        AddCommDeviceStateInfoReq state = deviceStateList.get(j);
        boolean isNewState = true;
        for (int i = 0; i < deviceStates.size(); i++) {
          DeviceState dbState = deviceStates.get(i);
          if (dbState.getPropertyName().equals(state.getPropertyName())) {
            dbState.setDeviceId(orig.getUuid());
            dbState.setLogDate(new Date());
            dbState.setProductId(orig.getProductId());
            dbState.setPropertyDesc(state.getPropertyDesc());
            dbState.setPropertyName(state.getPropertyName());
            dbState.setPropertyValue(state.getPropertyValue());
            dbState.setGroupId(state.getGroupId());
            dbState.setTenantId(tenantId);
            // 修改
            deviceStates.set(i, dbState);
            isNewState = false;
          }
        }
        if (isNewState) {
          DeviceState tempDestState = new DeviceState();
          tempDestState.setDeviceId(orig.getUuid());
          tempDestState.setLogDate(new Date());
          tempDestState.setProductId(orig.getProductId());
          tempDestState.setPropertyDesc(state.getPropertyDesc());
          tempDestState.setPropertyName(state.getPropertyName());
          tempDestState.setPropertyValue(state.getPropertyValue());
          tempDestState.setGroupId(state.getGroupId());
          tempDestState.setTenantId(orig.getTenantId());
          // 新增
          targetDeviceStates.add(tempDestState);
        }
      }
      // 添加原有db数据 更新
      targetDeviceStates.addAll(deviceStates);
    }

    return targetDeviceStates;
  }

  public void delByDeviceId(Long tenantId, String deviceId) {
    DeviceStateCacheCoreUtils.removeData(tenantId, deviceId, VersionEnum.V1);

    EntityWrapper wrapper = new EntityWrapper();
    wrapper.eq("tenant_id", tenantId);
    wrapper.eq("device_id", deviceId);
    deviceStateService.delete(wrapper);
  }

  @Transactional
  public void recoveryDefaultState(Long tenantId, String deviceId) {

    EntityWrapper<DeviceState> wrapper = new EntityWrapper<>();
    wrapper.eq("device_id", deviceId);
    wrapper.eq("tenant_id", tenantId);
    List<DeviceState> stateList = deviceStateService.selectList(wrapper);
    if (!CollectionUtils.isEmpty(stateList)) {
      int i = 0;
      for (DeviceState deviceState : stateList) {
        String key = deviceState.getPropertyName();
        if (key.equals("Dimming")) {
          deviceState.setPropertyValue("100");
        } else if (key.equals("RGBW")) {
          deviceState.setPropertyValue("-1375666432");
        } else if (key.equals("CCT")) {
          deviceState.setPropertyValue("4000");
        } else if (key.equals("OnOff")) {
          deviceState.setPropertyValue("1"); // 默认开
        } else if (key.equals("Occupancy")) { // 如果是Sensor_PIR
          deviceState.setPropertyValue("-1"); // 捕获状态，0: 捕获1: 没捕获  -1 未知
        } else if (key.equals("SafetyLock")) {
          deviceState.setPropertyValue("0");
        }else if(key.toLowerCase().contains("group")){
          deviceState.setPropertyValue(""); //将组属性设置为空
        }
        stateList.set(i, deviceState);
        i++;
      }
      // multi update
      deviceStateService.updateBatchById(stateList);
    }

    // 1.获取缓存数据
    Map<String, Object> cacheDataMap =
            DeviceStateCacheCoreUtils.getCacheData(tenantId, deviceId, VersionEnum.V1);
    if(!CollectionUtils.isEmpty(cacheDataMap)) {

      cacheDataMap.keySet().forEach(key ->{

        if (key.equals("Dimming")) {
          cacheDataMap.put("Dimming","100");
        } else if (key.equals("RGBW")) {
          cacheDataMap.put("RGBW","-1375666432");
        } else if (key.equals("CCT")) {
          cacheDataMap.put("CCT","4000");
        } else if (key.equals("OnOff")) {
          cacheDataMap.put("OnOff","1");
        } else if (key.equals("Occupancy")) { // 如果是Sensor_PIR
          cacheDataMap.put("Occupancy","-1");
        } else if (key.equals("SafetyLock")) {
          cacheDataMap.put("SafetyLock","0");
        }else if(key.toLowerCase().contains("group")){
          cacheDataMap.put(key,"");
        }
      });


      DeviceStateCacheCoreUtils.cacheData(tenantId, deviceId, cacheDataMap, VersionEnum.V1);
    }
  }
}
