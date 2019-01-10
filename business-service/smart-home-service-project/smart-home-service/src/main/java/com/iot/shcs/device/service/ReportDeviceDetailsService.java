package com.iot.shcs.device.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.RemoteCallBusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.favorite.api.FavoriteApi;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.device.api.ConfigurationApi;
import com.iot.device.api.CountdownCoreApi;
import com.iot.device.api.ElectricityStatisticsApi;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.vo.req.device.*;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.file.api.FileApi;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.shcs.common.util.DeviceUtils;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.queue.sender.DeviceConnectSender;
import com.iot.shcs.device.queue.sender.DeviceDisconnectSender;
import com.iot.shcs.device.queue.sender.SetDeviceAttrNotifySender;
import com.iot.shcs.device.service.impl.*;
import com.iot.shcs.device.utils.DeviceConstants;
import com.iot.shcs.device.utils.HitCacheUtils;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.ipc.config.IPCPropertiesConfig;
import com.iot.shcs.ota.service.OTAService;
import com.iot.shcs.scene.service.SceneDetailService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.space.service.ISpaceDeviceService;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.service.SpaceCoreService;
import com.iot.shcs.space.util.BeanCopyUtil;
import com.iot.shcs.template.service.impl.PackageServiceImpl;
import com.iot.shcs.template.vo.InitPackReq;
import com.iot.shcs.user.contants.UserContants;
import com.iot.user.api.UserApi;
import com.iot.video.api.VideoManageApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:14 2018/12/17
 * @Modify by:
 */
@Slf4j
@Component
public class ReportDeviceDetailsService {

    public ReportDeviceDetailsService() {

    }

    @Autowired
    private IPCPropertiesConfig ipcPropertiesConfig;

    @Autowired
    private UserApi userApi;

    @Autowired
    private CountdownCoreApi countdownCoreApi;

    @Autowired
    private ConfigurationApi configurationApi;

    @Autowired
    private VideoManageApi videoManageApi;

    @Autowired
    private ElectricityStatisticsApi electricityStatisticsApi;

    @Autowired
    private SpaceCoreService spaceCoreService;

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MqttCoreService mqttCoreService;

    @Autowired
    private PackageServiceImpl packageService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private DeviceStateService deviceStateService;

    @Autowired
    private ActivityRecordApi activityRecordApi;

    @Autowired
    private OTAService otaService;

    @Autowired
    private OTAServiceApi otaServiceApi;

    @Autowired
    private IAutoService autoService;

    @Autowired
    private ISpaceDeviceService spaceDeviceService;

    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private SceneDetailService sceneDetailService;

    @Autowired
    private SecurityMqttService securityMqttService;

    @Autowired
    private FileApi fileApi;
    @Autowired
    private DeviceDisconnectSender deviceDisconnectSender;

    @Autowired
    private DeviceConnectSender deviceConnectSender;


    @Autowired
    private SetDeviceAttrNotifySender setDeviceAttrNotifySender;

    @Autowired
    private FavoriteApi favoriteApi;

    @Autowired
    private DeviceActivateService deviceActivateService;

    public void doUpdateDevDetails() {

    }

    public void doUpdateDevDetails(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("updateDevDetails({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> setDevAttrReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        List<Map<String, Object>> subDevs = (List<Map<String, Object>>) setDevAttrReqPayload.get("subDev");

        String deviceId = MQTTUtils.parseReqTopic(reqTopic);//直连设备id
        try {
            //1.check device available
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
            if (deviceResp == null) {
                log.error("---- updateDevDetails() error! deviceId not in database. deviceId={} ---- ", deviceId);
                return;
            }
            Long productId = deviceResp.getProductId();
            Long tenantId = deviceResp.getTenantId();
            Long userId = deviceCoreService.getRootUserIdByDeviceId(tenantId, deviceId);
            if (userId == null) {
                log.error("---- updateDevDetails() error! gateWay({}) unbound user! ---- ", deviceId);
                return;
            }
            Long userDefaultSpaceId;// user默认家id
            com.iot.shcs.space.vo.SpaceResp userDefaultSpace = spaceService.findUserDefaultSpace(userId, tenantId);
            if (userDefaultSpace == null) {
                log.error("---- updateDevDetails() error! userDefaultSpace is null. deviceId={}, userId={} ---- ", deviceId, userId);
                return;
            } else {
                userDefaultSpaceId = userDefaultSpace.getId();
            }

            //预备动作： 获取子设备对应的信息
            Map<String, String> deviceToDeviceInfoMap = Maps.newHashMap();
            //预备动作： 获取子设备对应上线状态
            Map<String, Integer> deviceToOnlineMap = Maps.newHashMap();
            //预备动作： 获取子设备对应上报属性
            Map<String, Map<String, Object>> deviceToAttrMap = Maps.newHashMap();
            //预备动作： 获取所有的productId 对应的 deviceIds
            Map<String ,Set<String>> productIdModelSetMap = Maps.newHashMap();
            //预备动作： 获取所有base
            Set<String> productBaseModelSet = Sets.newHashSet();
            for (Map<String, Object> subDev : subDevs) {
                String subDeviceId = (String) subDev.get("devId");
                String subProductModel = (String) subDev.get("productId");
                String baseModel = (String) subDev.get("base");
                Object onlineObj = subDev.get("online");
                Integer online = (onlineObj != null && onlineObj instanceof Integer) ? (Integer) onlineObj : 0;
                //device to Info
                deviceToDeviceInfoMap.put(subDeviceId, subDeviceId);
                //device TO ONLINE
                deviceToOnlineMap.put(subDeviceId, online);
                //device to Attr
                Map<String, Object> attrMap = (Map<String, Object>) subDev.get("attr");
                deviceToAttrMap.put(subDeviceId, attrMap);
                // product ID model
                Set<String> subDeviceIdSet =  productIdModelSetMap.get(subProductModel);
                if (CollectionUtils.isEmpty(subDeviceIdSet)) {
                    subDeviceIdSet = Sets.newHashSet();
                    subDeviceIdSet.add(subDeviceId);
                } else {
                    if (!subDeviceIdSet.contains(subDeviceId)) {
                        subDeviceIdSet.add(subDeviceId);
                    }
                }
                productIdModelSetMap.put(subProductModel, subDeviceIdSet);

                if(!StringUtils.isEmpty(baseModel)) {
                    //product base model
                    productBaseModelSet.add(subDeviceId);
                }
            }
            //预备动作： 获取子设备和对应产品model相关信息
            Map<String, GetProductInfoRespVo> deviceToProductMap = buildProductByProductModel1(productIdModelSetMap, productBaseModelSet, subDevs);

            //预备动作： 批量获取 子设备信息[只有条件允许的集合在算数]
            List<String> subDeviceIdList = Lists.newArrayList();
            deviceToProductMap.keySet().forEach(subDevId ->{
                subDeviceIdList.add(subDevId);
            });

            //2.process multi sub device list relation
            List<UpdateDeviceInfoReq> allUpdateDeviceList = this.processMultiSubDevRelation(tenantId, userId, userDefaultSpaceId,
                    deviceId, deviceToProductMap, subDeviceIdList, deviceToDeviceInfoMap);

            //3.process multi sub device status
            this.processMultiSubDevStatus(tenantId, subDeviceIdList, deviceToOnlineMap);

            //4.process multi sub device state
            this.processMultiSubDevState(allUpdateDeviceList, deviceToAttrMap);

            String userUuid = userApi.getUuid(userId);
            //5.process kit product
            this.processKitProduct(tenantId, deviceId, productId, userUuid, userDefaultSpaceId, subDeviceIdList);

            //send refresh
            this.sendUserRefreshMQTTNotify(userUuid);

            deviceActivateService.recordActiveInfo(tenantId,subDeviceIdList,deviceId,deviceToProductMap);
        } catch (RemoteCallBusinessException e) {
            log.debug("updateDevDetails-remote-error.:{}", e);
        } catch (BusinessException e) {
            log.debug("updateDevDetails-local-error.:{}", e);
        } catch (Exception e) {
            log.error("updateDevDetails-error.:{}", e);
        }
    }

    private Map<String, GetProductInfoRespVo> buildProductByProductModel1(Map<String, Set<String>> productIdModelSetMap, Set<String> productBaseModelSet, List<Map<String, Object>> subDevs) {
        Map<String, GetProductInfoRespVo> deviceToProductMap = Maps.newHashMap();

        Map<String, GetProductInfoRespVo> productInfoRespVoMap = Maps.newHashMap();

        Set<String> productSet = new HashSet<>();
        productSet.addAll(productIdModelSetMap.keySet());
        productSet.addAll(productBaseModelSet);
        List<GetProductInfoRespVo> productInfoRespVoList = deviceCoreService.listBatchByProductModel(productSet);//增加【不必要的base model】的查询 by cong

        productInfoRespVoList.forEach(product -> {
            productInfoRespVoMap.put(product.getModel(), product);
        });

        for (Map<String, Object> subDev : subDevs) {
            String subDeviceId = (String) subDev.get("devId");
            String subProductModel = (String) subDev.get("productId");
            String baseModel = (String) subDev.get("base");

            GetProductInfoRespVo subProductResp = productInfoRespVoMap.get(subProductModel);

            if (subProductResp != null) {
                //device to product
                deviceToProductMap.put(subDeviceId, subProductResp);
                //cong造的bug 产品会被篡改成 baseProduct  解决：需加 continue
                continue;
            }

            subProductResp = productInfoRespVoMap.get(baseModel);

            if (subProductResp != null) {
                //device to product
                deviceToProductMap.put(subDeviceId, subProductResp);
            }
        }
        return deviceToProductMap;
    }

    private Map<String, GetProductInfoRespVo> buildProductByProductModel(Map<String, Set<String>> productIdModelSetMap, Set<String> productBaseModelSet, List<Map<String, Object>> subDevs) {
        Map<String, GetProductInfoRespVo> deviceToProductMap = Maps.newHashMap();

        Map<String, GetProductInfoRespVo> productInfoRespVoMap = Maps.newHashMap();
        List<GetProductInfoRespVo> productInfoRespVoList = deviceCoreService.listBatchByProductModel(productIdModelSetMap.keySet());
        if (!CollectionUtils.isEmpty(productInfoRespVoList)) {

            productInfoRespVoList.forEach(product ->{
                productInfoRespVoMap.put(product.getModel(), product);
            });
            List<String> baseModelList = Lists.newArrayList();
            for (Map<String, Object> subDev : subDevs) {
                String subDeviceId = (String) subDev.get("devId");
                String subProductModel = (String) subDev.get("productId");
                String baseModel = (String) subDev.get("base");
                GetProductInfoRespVo subProductResp =  productInfoRespVoMap.get(subProductModel);

                if (subProductResp != null) {
                    //device to product
                    deviceToProductMap.put(subDeviceId, subProductResp);
                } else {
                    if(StringUtils.isEmpty(baseModel)) {
                        continue;
                    }
                    //need select base model
                    baseModelList.add(baseModel);
                }
            }
            if (!CollectionUtils.isEmpty(baseModelList)) {
                buildBaseProductModels(subDevs, deviceToProductMap, baseModelList);
            }
            return deviceToProductMap;
        }

        buildBaseProductModels(subDevs, deviceToProductMap, productBaseModelSet);
        return deviceToProductMap;
    }

    private void buildBaseProductModels(List<Map<String, Object>> subDevs, Map<String, GetProductInfoRespVo> deviceToProductMap, Collection<String> baseModelList) {
        List<GetProductInfoRespVo> productInfoRespVoList;
        productInfoRespVoList = deviceCoreService.listBatchByProductModel(baseModelList);
        if (!CollectionUtils.isEmpty(productInfoRespVoList)) {
            Map<String, GetProductInfoRespVo> productBaseRespVoMap = Maps.newHashMap();

            productInfoRespVoList.forEach(baseProduct -> {
                productBaseRespVoMap.put(baseProduct.getModel(), baseProduct);
            });
            for (Map<String, Object> subDev : subDevs) {
                String subDeviceId = (String) subDev.get("devId");
                String baseModel = (String) subDev.get("base");
                if (StringUtils.isEmpty(baseModel)) {
                    continue;
                }
                GetProductInfoRespVo subProductResp = productBaseRespVoMap.get(baseModel);
                if (subProductResp != null) {
                    //device to product
                    deviceToProductMap.put(subDeviceId, subProductResp);
                }
            }
        }
    }

    private List<UpdateDeviceInfoReq> processMultiSubDevRelation(Long tenantId,
                                                                 Long userId,
                                                                 Long userDefaultSpaceId,
                                                                 String deviceId,
                                                                 Map<String, GetProductInfoRespVo> deviceToProductMap,
                                                                 List<String> subDeviceIdList,
                                                                 Map<String, String> deviceToDeviceInfoMap) {
        //1.构建
        List<UpdateDeviceInfoReq> allUpdateDeviceList = Lists.newArrayList();
        //预备动作： 获取每个设备对应的seq 方便后面拼接 设备名称
        Map<String, Integer> spaceSeqMap = this.processSpaceSeq(tenantId, userId, subDeviceIdList);
        //预备动作： 过滤出已经存在db里面的 子设备信息
        List<String> cacheExistSubIds = Lists.newArrayList();
        //1.1 获取设备信息集合
        List<ListDeviceInfoRespVo>  cacheExistSubDeviceList = deviceCoreService.listDevicesByDeviceIds(subDeviceIdList);
        if (!CollectionUtils.isEmpty(cacheExistSubDeviceList)) {
            cacheExistSubDeviceList.forEach(subDeviceInfo ->{
                cacheExistSubIds.add(subDeviceInfo.getUuid());
            });
        }
        //1.2构建批量保存子设备
        List<UpdateDeviceInfoReq> insertBatchSubDevList = buildSaveMultiSubDevice(tenantId, userId, deviceId, cacheExistSubIds, subDeviceIdList, deviceToProductMap, spaceSeqMap);
        if (!CollectionUtils.isEmpty(insertBatchSubDevList)) {
            allUpdateDeviceList.addAll(insertBatchSubDevList);
        }

        //1.3.构建 存在的 进行判断parentId 进行 更新
        List<UpdateDeviceInfoReq> existUpdateBatchSubDevList = buildUpdateMultiSubDevice(tenantId, userId, deviceId, cacheExistSubIds, cacheExistSubDeviceList, deviceToProductMap, spaceSeqMap);
        if(!CollectionUtils.isEmpty(existUpdateBatchSubDevList)) {
            allUpdateDeviceList.addAll(existUpdateBatchSubDevList);
        }

        //2.建立关系
        if (!CollectionUtils.isEmpty(allUpdateDeviceList)) {
            //2.1批量保存 修改设备信息
            deviceCoreService.saveOrUpdateBatchDeviceInfo(allUpdateDeviceList);
            //2.2批量建立用户与子设备的关系(user_device)
            deviceCoreService.saveOrUpdateBatchUserDevice(tenantId, userId, UpdateUserDeviceInfoReq.ROOT_DEVICE, subDeviceIdList);
            //2.3批量保存space 跟设备的关系
            spaceCoreService.saveOrUpdateBatchSpaceDevice(tenantId, userDefaultSpaceId, subDeviceIdList);
        }
        //3. 去重 网关下面的设备用户关系
        Map<String, ListDeviceInfoRespVo> deviceRespMap = this.processDuplicateRemovalParentDeviceId(tenantId, userId, deviceId, deviceToDeviceInfoMap);

        //4. 去重 房间下的设备关系
        this.processDuplicateRemovalSpaceDevice(tenantId, deviceId, userDefaultSpaceId, deviceToDeviceInfoMap, deviceRespMap);
        return allUpdateDeviceList;
    }

    private List<UpdateDeviceInfoReq> buildUpdateMultiSubDevice(Long tenantId, Long userId, String deviceId, List<String> cacheExistSubIds, List<ListDeviceInfoRespVo> cacheExistSubDeviceList, Map<String, GetProductInfoRespVo> deviceToProductMap, Map<String, Integer> spaceSeqMap) {
        List<UpdateDeviceInfoReq> existUpdateBatchSubDevList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cacheExistSubDeviceList)) {
            return existUpdateBatchSubDevList;
        }
        //T批量获取存在的设备 对应是否有用户关系 user_device 根据 deviceIds + tenantId
        Map<String, ListUserDeviceInfoRespVo> deviceToUserMap = deviceCoreService.listBatchUserDeviceMap(tenantId, cacheExistSubIds);
        for (ListDeviceInfoRespVo subDeviceInfo : cacheExistSubDeviceList) {
            String subDeviceId = subDeviceInfo.getUuid();
            if (!StringUtils.isEmpty(subDeviceInfo.getParentId())) {
                // 2.2 parentId存在 && 不等于当前网关，直接 跳过循环（避免互抢)
                if (!subDeviceInfo.getParentId().equals(deviceId)) {
                    continue;
                }
            }
            // 2.1 parentId不存在 且未被绑定， 设置subDev的parent为当前网关、并绑定到当前用户
            ListUserDeviceInfoRespVo subUserDevice = deviceToUserMap.get(subDeviceId);
            if (subUserDevice != null) {
                continue;
            }
            // 子设备 在同一个租户下 未被人绑定
            GetProductInfoRespVo subProductResp = deviceToProductMap.get(subDeviceId);
            String deviceName = subProductResp.getProductName() + "_" + spaceSeqMap.get(subDeviceId);
            UpdateDeviceInfoReq deviceInfoParam =
                    UpdateDeviceInfoReq.builder()
                            .deviceTypeId(subProductResp.getDeviceTypeId())
                            .productId(subProductResp.getId())
                            .name(deviceName)
                            .tenantId(tenantId)
                            .parentId(deviceId)
                            .isDirectDevice(0)
                            .uuid(subDeviceId)
                            .updateBy(userId)
                            .build();
            existUpdateBatchSubDevList.add(deviceInfoParam);
        }
        return existUpdateBatchSubDevList;
    }

    private List<UpdateDeviceInfoReq> buildSaveMultiSubDevice(Long tenantId, Long userId, String deviceId, List<String> cacheExistSubIds, List<String> subDeviceIdList, Map<String, GetProductInfoRespVo> deviceToProductMap, Map<String, Integer> spaceSeqMap) {
        //1. 不存在则先存储到数据库
        List<UpdateDeviceInfoReq> insertBatchSubDevList = Lists.newArrayList();
        //预备动作： 去重 检验出需要新增的子设备信息
        List<String> noHitSubDeviceIds = HitCacheUtils.getNoHitCacheIds(cacheExistSubIds, subDeviceIdList);
        if (!CollectionUtils.isEmpty(noHitSubDeviceIds)) {
            noHitSubDeviceIds.forEach(subDeviceId ->{
                GetProductInfoRespVo subProductResp = deviceToProductMap.get(subDeviceId);

                String deviceName = subProductResp.getProductName() + "_" + spaceSeqMap.get(subDeviceId);
                // 获取设备图标
                String icon = getIcon(null, subProductResp);
                UpdateDeviceInfoReq deviceInfoParam = UpdateDeviceInfoReq.builder()
                        .deviceTypeId(subProductResp.getDeviceTypeId()).productId(subProductResp.getId())
                        .name(deviceName).icon(icon).tenantId(tenantId).parentId(deviceId).isDirectDevice(0)
                        .uuid(subDeviceId).createBy(userId).build();
                //所有要保存的
                insertBatchSubDevList.add(deviceInfoParam);

            });
        }
        return insertBatchSubDevList;
    }

    private Map<String, ListDeviceInfoRespVo> processDuplicateRemovalParentDeviceId(Long tenantId, Long userId, String deviceId, Map<String, String> deviceToDeviceInfoMap){
        Map<String, ListDeviceInfoRespVo> deviceRespMap = Maps.newHashMap();

        //上报有可能少了 几个设备 但有用户绑定了这个设备  要删除这个子设备与用户的关系 并删除与网关的关系【userDevice的关系】
        List<ListDeviceInfoRespVo> deviceList = deviceCoreService.listDevicesByParentId(deviceId);

        if(CollectionUtils.isEmpty(deviceList)) {
            return deviceRespMap;
        }
        //过滤 删除不存在当前网关对应的子设备
        List<String> getChildDevicesNotInThisParentDevList = Lists.newArrayList();
        deviceList.forEach(m -> {
            deviceRespMap.put(m.getUuid(), m);
        });
        for (String existSubDeviceId : deviceRespMap.keySet()) {
            //
            ListDeviceInfoRespVo existSubDevice = deviceRespMap.get(existSubDeviceId);
            boolean isExist = false;
            if (deviceToDeviceInfoMap.containsKey(existSubDeviceId)) {
                isExist = true;
            }

            if (!isExist) {
                getChildDevicesNotInThisParentDevList.add(existSubDevice.getUuid());
            }
        }
        if (!CollectionUtils.isEmpty(getChildDevicesNotInThisParentDevList)) {
            log.info("----delSubDevice----  parentDeviceId:{} ,childDeviceId:{}", deviceId, JSON.toJSONString(getChildDevicesNotInThisParentDevList));
            this.deleteMultiDeviceRelation(tenantId, getChildDevicesNotInThisParentDevList,userId);// 先删所有的关系后 再删设备
            deviceCoreService.delBatchSubDeviceId(tenantId, userId, getChildDevicesNotInThisParentDevList);
        }
        return deviceRespMap;
    }
    private void processDuplicateRemovalSpaceDevice(Long tenantId, String deviceId, Long userDefaultSpaceId,
                                                    Map<String, String> deviceToDeviceInfoMap, Map<String, ListDeviceInfoRespVo> deviceRespMap){
        try {
            log.debug("checkSpaceDevice-begin");
            if (CollectionUtils.isEmpty(deviceRespMap)) {
                return;
            }
            //上报有可能少了/多了 几个设备，维护处理好 spaceDevice 的关系
            List<com.iot.shcs.space.vo.SpaceDeviceVo> homeDeviceList = spaceDeviceService.findSpaceDeviceVOBySpaceId(userDefaultSpaceId);
            if (CollectionUtils.isEmpty(homeDeviceList)) {
                return;
            }
            List<String> needRemoveSpaceSubDeviceList = Lists.newArrayList();
            for (com.iot.shcs.space.vo.SpaceDeviceVo spaceDevice : homeDeviceList) {
                boolean isExist = false;
                String tempDeviceId =  deviceToDeviceInfoMap.get(spaceDevice.getDeviceId());
                if (tempDeviceId != null) {
                    isExist = true;
                }
                // 不存在，则删除云端子设备，但直连设备不能删除
                if (!isExist && !deviceId.equals(spaceDevice.getDeviceId())) {
                    ListDeviceInfoRespVo deviceTemp = deviceRespMap.get(spaceDevice.getDeviceId()); // 是否在子设备列表
                    if (deviceTemp != null) {
                        log.debug("delSpaceDevice-spaceId:{},deviceId:{}", userDefaultSpaceId, spaceDevice.getDeviceId());
                        needRemoveSpaceSubDeviceList.add(spaceDevice.getDeviceId());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(needRemoveSpaceSubDeviceList)) {
                spaceDeviceService.deleteBatchSpaceDeviceByDevIds(tenantId, needRemoveSpaceSubDeviceList);
            }
            log.debug("checkSpaceDevice-end");
        } catch (Exception e) {
            log.debug("delSpaceDevice-去重异常.{}", e);
        }
    }

    public Map<String, Integer> processSpaceSeq(Long tenantId, Long userId, List<String> subDeviceIdList) {
        Map<String, Integer> spaceSeqMap = Maps.newHashMap();
        com.iot.shcs.space.vo.SpaceResp spaceResp = spaceService.findUserDefaultSpace(userId, tenantId);
        Integer seq = 0;
        for (String  subDeviceId : subDeviceIdList) {
            if (seq == 0) {
                seq = (spaceResp != null && spaceResp.getSeq() != null) ? (spaceResp.getSeq().intValue()) + 1 : 1;
            } else {
                seq = seq + 1;
            }
            spaceSeqMap.put(subDeviceId, seq);
        }
        try {
            com.iot.shcs.space.vo.SpaceReq space = BeanCopyUtil.spaceRespToSpaceReq(spaceResp);
            space.setSeq(seq);
            spaceService.update(space);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("查找设备序列号异常", e);
        }

        return spaceSeqMap;
    }

    private void processMultiSubDevStatus(Long tenantId, List<String> subDeviceIdList, Map<String, Integer> deviceToOnlineMap) {
        List<UpdateDeviceStatusReq> updateDeviceStatusBatchList = Lists.newArrayList();
        subDeviceIdList.forEach(subDeviceId ->{
            Integer online = deviceToOnlineMap.get(subDeviceId);
            // 更新 subDevice在线状态
            UpdateDeviceStatusReq updateDeviceStatusParam = UpdateDeviceStatusReq.builder()
                    .onlineStatus(OnlineStatusEnum.valueOf(online).getCode())
                    .tenantId(tenantId)
                    .deviceId(subDeviceId)
                    .build();
            updateDeviceStatusBatchList.add(updateDeviceStatusParam);
        });
        if(!CollectionUtils.isEmpty(updateDeviceStatusBatchList)) {
            deviceCoreService.saveOrUpdateBatchDeviceStatus(updateDeviceStatusBatchList);
        }
    }

    private void processMultiSubDevState(List<UpdateDeviceInfoReq> allUpdateDeviceList, Map<String, Map<String, Object>> deviceToAttrMap) {
        List<UpdateDeviceStateReq> updateDeviceStateBatchList = Lists.newArrayList();
        for (UpdateDeviceInfoReq deviceInfo : allUpdateDeviceList) {
            String subDeviceId = deviceInfo.getUuid();
            Map<String, Object> attrMap = deviceToAttrMap.get(subDeviceId);
            if(CollectionUtils.isEmpty(attrMap)) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            List<AddCommDeviceStateInfoReq> deviceStatusInfoList = Lists.newArrayList();
            for (Map.Entry<String, Object> entry : attrMap.entrySet()) {
                AddCommDeviceStateInfoReq deviceStatusInfo = new AddCommDeviceStateInfoReq();
                deviceStatusInfo.setPropertyName(entry.getKey());
                deviceStatusInfo.setPropertyValue(entry.getValue().toString());
                deviceStatusInfo.setPropertyDesc(null);
                deviceStatusInfoList.add(deviceStatusInfo);
                if (StringUtil.isNotBlank(sb.toString())) {
                    sb = sb.append("\n");
                }
                String value =
                        DeviceUtils.changeValue(deviceInfo.getName(), entry.getKey(), entry.getValue().toString());
                sb = sb.append(value);
            }
            UpdateDeviceStateReq deviceStateReq = UpdateDeviceStateReq.builder()
                    .tenantId(deviceInfo.getTenantId())
                    .deviceId(deviceInfo.getUuid())
                    .stateList(deviceStatusInfoList)
                    .build();
            updateDeviceStateBatchList.add(deviceStateReq);
        }
        if (!CollectionUtils.isEmpty(updateDeviceStateBatchList)) {
            deviceCoreService.saveOrUpdateDeviceStates(updateDeviceStateBatchList);
        }
    }
    private void processKitProduct(Long tenantId, String deviceId, Long productId,
                                   String userUuId, Long userDefaultSpaceId,
                                   List<String> deviceIds) {
        boolean isKitProduct = deviceService.isKitProduct(productId);
        log.debug("-----isKitProduct={}", isKitProduct);
        if (isKitProduct) {
            // 套包产品(网关), 首次上传子设备(向云端)时, 云端是没有存它的子设备, 此时必须 把subDevs 存起来
            GetDeviceExtendInfoRespVo deviceExtendResp = deviceCoreService.getDeviceExtendByDeviceId(tenantId, deviceId);
            log.debug("-----deviceExtendResp jsonString={}", JSON.toJSON(deviceExtendResp));
            if (deviceExtendResp != null && deviceExtendResp.getFirstUploadSubDev() != null
                    && deviceExtendResp.getFirstUploadSubDev() == 1) {

                InitPackReq req = InitPackReq.builder().tenantId(tenantId).productId(productId).spaceId(userDefaultSpaceId)
                        .userUuId(userUuId).directDeviceId(deviceId).devIds(deviceIds).build();
                packageService.initPack(req);

                UpdateDeviceExtendReq updateReq = UpdateDeviceExtendReq.builder().tenantId(tenantId).deviceId(deviceId).firstUploadSubDev(0).build();
                deviceCoreService.saveOrUpdateExtend(updateReq);
            }


        }
    }


    private void sendUserRefreshMQTTNotify(String userUuid) {
        MqttMsg refreshMsg = new MqttMsg();
        Map<String, Object> payload = Maps.newHashMap();
        payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
        refreshMsg.setService("user");
        refreshMsg.setMethod("refresh");
        String refreshTopic = UserContants.buildTopic(userUuid, "refresh");
        refreshMsg.setTopic(refreshTopic);
        refreshMsg.setSrcAddr(userUuid);
        refreshMsg.setSeq(StringUtil.getRandomString(10));
        refreshMsg.setPayload(payload);
        log.info("sendUserRefreshMQTTNotify-topic:{} msg:{} ,", refreshTopic, refreshMsg);
        mqttCoreService.sendMessage(refreshMsg);
    }
    /**
     * 获取设备图标
     *
     * @return
     */
    private String getIcon(GetDeviceInfoRespVo device, GetProductInfoRespVo productResp) {
        String icon = null;
        if (device == null) {
            if (productResp.getModel() != null && productResp.getModel().contains(".")) {
                String deviceIcon = productResp.getModel().split("\\.")[1];
                icon = deviceIcon;
            }
        } else {
            if (StringUtils.isEmpty(device.getIcon())) {
                if (productResp.getModel() != null && productResp.getModel().contains(".")) {
                    String deviceIcon = productResp.getModel().split("\\.")[1];
                    icon = deviceIcon;
                }
            } else {
                icon = device.getIcon();
            }
        }

        return icon;
    }

    public void deleteMultiDeviceRelation(Long tenantId, List<String> deviceIds,Long userId) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        List<ListDeviceInfoRespVo> deviceInfoRespVoList = deviceCoreService.listDevicesByDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(deviceInfoRespVoList)) {
            return;
        }
        List<ListDeviceInfoRespVo> directDeviceInfoList = Lists.newArrayList();
        List<String> directDeviceIds = Lists.newArrayList();
        deviceInfoRespVoList.forEach(deviceInfo ->{
            if (deviceInfo.getIsDirectDevice() != null
                    && deviceInfo.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
                directDeviceInfoList.add(deviceInfo);
                directDeviceIds.add(deviceInfo.getUuid());
            }
        });
        //1.delete multi security
        deleteMultiSecurity(tenantId, deviceInfoRespVoList);
        //2.delete multi space device
        spaceDeviceService.deleteBatchSpaceDeviceByDevIds(tenantId, deviceIds);
        //3.delete multi multi scene
        sceneDetailService.deleteSceneDetailByBatchDeviceIds(tenantId, deviceIds);
        //4.delete multi auto
        autoService.delByBatchDeviceIds(tenantId, deviceIds);
        if (!CollectionUtils.isEmpty(directDeviceIds)) {
            autoService.delByBatchDirectDeviceIds(tenantId, directDeviceIds);
        }
        //5.delete multi activity
        //6.批量删除设备的favorite
        deleteDevFavorite(tenantId, deviceInfoRespVoList,userId);

    }
    private void deleteDevFavorite(Long tenantId,List<ListDeviceInfoRespVo> deviceInfoRespVoList,Long userId){
        log.info("deleteDevFavorite={},userId={},tenantId={}",JSON.toJSONString(deviceInfoRespVoList),userId,tenantId);
        if (CollectionUtils.isEmpty(deviceInfoRespVoList)) {
            return;
        }
        for (ListDeviceInfoRespVo deviceResp : deviceInfoRespVoList) {
            try {
                FavoriteReq favoriteReq=new FavoriteReq();
                favoriteReq.setUserId(userId);
                favoriteReq.setTenantId(tenantId);
                favoriteReq.setDevScene(deviceResp.getUuid());
                favoriteReq.setTypeId(1);
                favoriteApi.delSingleFavorite(favoriteReq);
            }catch (Exception e){
                e.printStackTrace();
                log.error("deleteDevFavorite erro={}", e);
            }
        }
        log.info("deleteDevFavorite ok");
    }
    private void deleteMultiSecurity(Long tenantId, List<ListDeviceInfoRespVo> deviceInfoRespVoList) {
        if (CollectionUtils.isEmpty(deviceInfoRespVoList)) {
            return;
        }
        for (ListDeviceInfoRespVo deviceResp : deviceInfoRespVoList) {
            boolean isDirectDevice = false;
            if (deviceResp.getIsDirectDevice() != null
                    && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
                isDirectDevice = true;
            }
            if (isDirectDevice) {
                // 网关删除所有安防信息
                securityMqttService.deleteSecurityInfo(tenantId, deviceResp.getUuid(), deviceResp.getProductId());
            } else {
                //子设备只删除安防规则 “if” 中的设备
                securityMqttService.deleteSubDevSecurityInfo(tenantId, deviceResp.getUuid());
            }
        }
    }
    /**
     * 删除 与设备关联的数据
     *
     * @param deviceUuid 设备uuid
     * @throws BusinessException
     */
    private void deleteDeviceRelationships(String deviceUuid) {
        try {
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
            if (deviceResp == null) {
                log.debug("deleteDeviceRelationships device not exist.{}", deviceUuid);
                return;
            }
            Long tenantId = deviceResp.getTenantId();
            boolean isDirectDevice = false;
            if (deviceResp.getIsDirectDevice() != null
                    && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
                isDirectDevice = true;
            }

            if (isDirectDevice) {
                // 网关删除所有安防信息
                securityMqttService.deleteSecurityInfo(tenantId, deviceUuid, deviceResp.getProductId());
            } else {
                //子设备只删除安防规则 “if” 中的设备
                securityMqttService.deleteSubDevSecurityInfo(tenantId, deviceUuid);
            }

            // 解除设备与房间关系
            spaceDeviceService.deleteSpaceDeviceByDeviceId(tenantId, deviceResp.getUuid());

            // 解除设备与情景关系
            sceneDetailService.deleteSceneDetailByDeviceId(deviceResp.getUuid(), deviceResp.getTenantId());


            log.debug("delete-ifttt-deviceId:{}", deviceResp.getId());
            // 解除设备与IFTTT关系
            autoService.delByDeviceId(deviceUuid, deviceResp.getTenantId());
            if (isDirectDevice) {
                autoService.delByDirectDeviceId(deviceResp.getUuid(), deviceResp.getTenantId());
            }
            //软删除活动记录
            ActivityRecordReq recordReq = new ActivityRecordReq();
            recordReq.setForeignId(deviceUuid);
            activityRecordApi.delActivityRecord(recordReq);
        } catch (Exception e) {
            log.error("deleteDeviceRelationships-error--->", e);
        }
    }

}
