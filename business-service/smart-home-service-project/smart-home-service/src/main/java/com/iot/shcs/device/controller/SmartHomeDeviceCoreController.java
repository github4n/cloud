package com.iot.shcs.device.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import com.iot.device.api.*;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateProductReq;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.mqttploy.api.MqttPloyApi;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.device.api.SmartHomeDeviceCoreApi;
import com.iot.shcs.device.enums.DeviceCoreExceptionEnum;
import com.iot.shcs.device.service.IDeviceMQTTService;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.device.service.impl.DeviceService;
import com.iot.shcs.device.vo.*;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.scene.service.SceneDetailService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.space.service.ISpaceDeviceService;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.service.SpaceCoreService;
import com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.shcs.space.vo.SpaceDeviceVo;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.user.api.UserApi;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class SmartHomeDeviceCoreController implements SmartHomeDeviceCoreApi {

    public static final String HTTP_FILE_DOWN_PATH = "local.file.download-path";

    public static final String LOCAL_FILE_UPLOAD_PATH = "local.file.upload-path";

    public static final String SERVICE = "device";
    @Autowired
    DeviceTypeApi deviceTypeApi;
    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private ProductApi productApi;
    @Autowired
    private DataPointApi dataPointApi;
    @Autowired
    private UserApi userApi;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private IDeviceMQTTService deviceMQTTService;

    @Autowired
    private MqttPloyApi mqttPloyApi;

    @Autowired
    private Environment environment;

    @Autowired
    private OTAServiceApi otaServiceApi;

    @Autowired
    private SpaceCoreService spaceCoreService;

    @Autowired
    private ISpaceDeviceService spaceDeviceService;

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private DeviceStateCoreApi deviceStateService;

    @Autowired
    private ProductCoreApi productCoreApi;

    @Autowired
    private SecurityMqttService securityMqttService;

    @Autowired
    private SceneDetailService sceneDetailService;

    @Autowired
    private IAutoService autoService;

    @Autowired
    private ActivityRecordApi activityRecordApi;

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private UserDeviceCoreApi userDeviceCoreApi;

    @Autowired
    private GroupApi groupApi;

    @Override
    public Map<String, Object> getDevList(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId, @RequestParam("homeId") Long homeId) {
        return spaceService.getHomeDevListAndCount(tenantId, userId, Long.valueOf(homeId));
    }

    @Override
    public void synchronousRemoteControl(@RequestParam("spaceId") Long spaceId) {

    }

    @Override
    public CommonResponse singleDeviceControlForAlexa(@RequestBody ControlReq controlVo) throws BusinessException {
        return null;
    }

    @Override
    public CommonResponse singleControl(@RequestBody ControlReq controlVo) throws BusinessException {
        return null;
    }

    @Override
    public Boolean cancelSos(@RequestBody ControlReq controlVo) throws BusinessException {
        return null;
    }

    @Override
    public Object getDeviceAttr(@RequestBody AttrGetReq req) throws BusinessException {
        return null;
    }


    @Override
    public Map<String, Object> getDeviceProerty(@RequestParam("deviceId") String deviceId) throws BusinessException {
        return null;
    }

    @Override
    public void initMountInfo(@RequestParam("tenantId") Long tenantId) throws BusinessException {

    }

    @Override
    public Object getDeviceAttrs(@RequestBody AttrGetReq req) throws BusinessException {
        return null;
    }


    @Override
    public JSONObject getSensorDevsAttrs(@RequestBody AttrGetReq req) {
        return null;
    }


    @Override
    public void updateOtaVersion(@RequestParam("deviceId") String deviceId, @RequestParam("fileName") String fileName, @RequestParam("tenantId") Long tenantId, @RequestParam("locationId") Long locationId) {

    }

    @Override
    public void downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq) {

    }

    @Override
    public String addBleSubDev(@RequestBody SubDevReq subDev) {
        log.info("addBleSubDev input : {}", JSON.toJSONString(subDev));
        String userUuid = subDev.getUserUuid();
        String deviceUuid = subDev.getDevId();
        String comMode = subDev.getComMode();
//        String name = subDev.getDevName();
        Long devTenantId = subDev.getTenantId();
        Integer isAppDev = subDev.getIsAppDev();
        //确保Ble设备不存在,每次重新添加ble设备上报的uuid都不一样(由app生成)
        GetDeviceInfoRespVo deviceInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
        if(deviceInfo != null){
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_IS_EXIST);
        }

        //1.获取绑定的用户信息
        FetchUserResp user = userApi.getUserByUuid(userUuid);
        if (user == null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        //检查设备名称唯一性
        Long userId = user.getId();
//        if(!StringUtil.isEmpty(name)) {
//            CheckDeviceName(devTenantId, userId, deviceUuid, name);
//        }

        Long userTenantId = user.getTenantId();
        String productId = subDev.getProductId() == null ? null : subDev.getProductId();
        //2.校验产品信息
        GetProductInfoRespVo productInfoResp = productCoreApi.getByProductModel(productId);
        if(productInfoResp == null){
            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfoResp.getTenantId().compareTo(userTenantId) != 0) {
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_TENANT_ERROR);
        }
//        if(!comMode.equals(productInfoResp.getConfigNetMode())){
//            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_COMMODE_IS_ERROR);
//        }

        //TODO 检查用户是否在线?
//      boolean userWhetherOnline = UserUtils.checkUserWhetherOnline(user.getState());

        //3.检查设备用户关系
        ListUserDeviceInfoRespVo userDeviceInfoRespVo = null;
        List<ListUserDeviceInfoRespVo> existRelationShip = deviceCoreService.listUserDevices(userTenantId, deviceUuid);
        if (!CollectionUtils.isEmpty(existRelationShip)) {
            userDeviceInfoRespVo = existRelationShip.get(0);
            Long existUserId = userDeviceInfoRespVo.getUserId();
            FetchUserResp existUser = userApi.getUser(existUserId);
            if (!existUser.getUuid().equals(userUuid)) {
                throw new BusinessException(DeviceExceptionEnum.DEVICE_BINDED);
            }
        }

        if (userDeviceInfoRespVo == null) {//未绑定要进行banding
            // 4.绑定用户跟直连设备的关系
            UpdateUserDeviceInfoReq bindUserDevice =
                    UpdateUserDeviceInfoReq.builder()
                            .tenantId(userTenantId)
                            .orgId(user.getOrgId())
                            .userId(userId)
                            .userType(UpdateUserDeviceInfoReq.ROOT_DEVICE)
                            .deviceId(deviceUuid)
                            .build();
            UpdateUserDeviceInfoResp response = deviceCoreService.saveOrUpdateUserDevice(bindUserDevice);

            if (response == null || response.getUserId() == null || response.getDeviceId() == null) {
                throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_BIND_ERROR);
            }
        }

        //5.检查并获取用户默认家
        SpaceResp space = spaceService.findUserDefaultSpace(userId, userTenantId);
        if(space == null){
            throw new BusinessException(DeviceCoreExceptionEnum.SPACE_DEFAULT_NOT_EXIST);
        }
        //维护space seq
        Integer seq = spaceService.getDeviceSeqByUserId(userTenantId, userId);
        String name = productInfoResp.getProductName() + "_" + seq;

        //6.添加房间跟直连设备的关系
        spaceCoreService.saveOrUpdateSpaceDevice(userTenantId, space.getId(), deviceUuid);

        //7.修改/保存设备相关信息
        UpdateDeviceExtendReq updateDeviceExtendReq = UpdateDeviceExtendReq.builder()
                .tenantId(userTenantId).deviceId(deviceUuid)
                .address(subDev.getAddress()).unbindFlag(0).resetFlag(0)
                .build();
        deviceCoreService.saveOrUpdateExtend(updateDeviceExtendReq);

        UpdateDeviceInfoReq subDeviceInfo = UpdateDeviceInfoReq.builder()
                .uuid(subDev.getDevId())
                .productId(productInfoResp.getId())
                .icon(subDev.getIcon())
                .mac(subDev.getMac())
                .name(name)
                .tenantId(userTenantId)
                .deviceTypeId(productInfoResp.getDeviceTypeId())
                .devModel(subDev.getProductId())
                .version(subDev.getVersion())
                .createBy(userId)
                .isDirectDevice(1)
                .isAppDev(isAppDev)
                .build();
        deviceCoreService.saveOrUpdateDeviceInfo(subDeviceInfo);
        return name;
    }

    @Override
    public DevInfoResp getDevInfo(@RequestBody DevInfoReq req) {
        log.debug("getDevInfo tenantId:{}, devId:{}", req.getTenantId(), req.getDevId());
        String devId = req.getDevId();
        Long tenantId = req.getTenantId();
        if(StringUtil.isEmpty(devId)){
            log.info("getDevInfo device is empty .");
            return null;
        }

        //获取设备信息
        GetDeviceInfoRespVo devInfo = deviceCoreService.getDeviceInfoByDeviceId(devId);
        //获取产品信息
        if(devInfo == null){
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_NOT_EXIST);
        }
        if(tenantId.compareTo(devInfo.getTenantId()) != 0){
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_TENANT_ERROR);
        }
        Long productId = devInfo.getProductId();
        GetProductInfoRespVo productInfo = productCoreApi.getByProductId(productId);
        if(productInfo == null){
            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_NOT_EXIST);
        }
        //获取设备拓展信息
        GetDeviceExtendInfoRespVo devExtend = deviceCoreService.getDeviceExtendByDeviceId(tenantId, devId);
        //获取设备空间信息spaceId、parentId、
        SpaceAndSpaceDeviceVo spaceDevVo = new SpaceAndSpaceDeviceVo();
        spaceDevVo.setDeviceIds(Lists.newArrayList(devId));
        spaceDevVo.setTenantId(tenantId);
        List<SpaceDeviceVo> spaceDeviceVos = spaceDeviceService.findSpaceDeviceInfoByDeviceIds(spaceDevVo);
        if(CollectionUtils.isEmpty(spaceDeviceVos)){
            throw new BusinessException(DeviceCoreExceptionEnum.USER_DEVICE_NOT_EXIST);
        }
        SpaceDeviceVo vo = spaceDeviceVos.get(0);
        Long homeId = (vo.getParentId() == -1 || vo.getParentId() == null) ? vo.getSpaceId() : vo.getParentId();
        Long roomId = (vo.getParentId() == -1 || vo.getParentId() == null) ? 0L : vo.getSpaceId();

        //TODO ota信息
        //TODO remoteGroudId
        DevInfoResp result = DevInfoResp.builder()
                .devId(devId).name(devInfo.getName())
                .icon(devInfo.getIcon()).homeId(homeId + "")
                .roomId(roomId + "").devModel(devInfo.getDevModel())
                .hwVersion(devInfo.getHwVersion()).mac(devInfo.getMac())
                .version(devInfo.getVersion()).supplier(devInfo.getSupplier())
                .address(devExtend.getAddress()).comMode(productInfo.getConfigNetMode())
                .build();

        return result;
    }

    @Override
    public void setDevInfo(@RequestBody DevInfoReq req) {
        log.debug("setDevInfo tenantId:{}, devId:{}", req.getTenantId(), req.getDevId());
        String devId = req.getDevId();
        if(StringUtil.isEmpty(devId)){
            log.info("getDevInfo device is empty .");
            return ;
        }
        Long tenantId = req.getTenantId();
        Long userId = req.getUserId();
        String name = req.getName();
        String homeId = req.getHomeId();
        String roomId = req.getRoomId();
        String icon = req.getIcon();
        Integer isAppDev = req.getIsAppDev();

        //获取设备信息
        GetDeviceInfoRespVo devInfo = deviceCoreService.getDeviceInfoByDeviceId(devId);

        if(devInfo == null){
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_NOT_EXIST);
        }
        if(tenantId.compareTo(devInfo.getTenantId()) != 0){
            log.debug("setDevInfo device tenantId is not equals to userTenantId{}",devInfo.getTenantId(), tenantId);
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_TENANT_ERROR);
        }
        //检查设备名称
        if(!StringUtil.isEmpty(name)) {
            CheckDeviceName(tenantId, userId, devId, name);
        }

        // 更新设备信息
        UpdateDeviceInfoReq updateDeviceInfoReq = UpdateDeviceInfoReq.builder()
                .tenantId(tenantId)
                .uuid(devId)
                .name(name)
                .icon(icon)
                .updateBy(userId)
                .isAppDev(isAppDev)
                .build();

        deviceCoreService.saveOrUpdateDeviceInfo(updateDeviceInfoReq);

        // 修改房间设备信息
        spaceCoreService.updateSpaceByUserId(userId, tenantId, devId, roomId, homeId);

        //设置产品信息
        String comMode = req.getComMode();
        if(!StringUtil.isEmpty(comMode)) {
            UpdateProductReq productReq = new UpdateProductReq();
            productReq.setConfigNetMode(comMode);
            productCoreApi.saveOrUpdate(productReq);
        }

        //设置设备拓展信息
        Long address = req.getAddress();
        if(address != null) {
            UpdateDeviceExtendReq devExtendReq = UpdateDeviceExtendReq.builder()
                    .tenantId(tenantId).deviceId(devId)
                    .address(address).unbindFlag(0).resetFlag(0)
                    .build();
            deviceCoreService.saveOrUpdateExtend(devExtendReq);
        }

        //TODO group

    }

    public void delBleSubDev(@RequestBody SubDevReq subDev){
        log.info("delBleSubDev input : {}", JSON.toJSONString(subDev));
        String userUuid = subDev.getUserUuid();
        String deviceUuid = subDev.getDevId();
        Long tenantId = subDev.getTenantId();
        //确保Ble设备存在,每次重新添加ble设备上报的uuid都不一样(由app生成)
        GetDeviceInfoRespVo deviceInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
        if(deviceInfo == null){
            log.debug("deleteDeviceRelationships device not exist.{}", deviceUuid);
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_NOT_EXIST);
        }

        //1.获取绑定的用户信息
        FetchUserResp user = userApi.getUserByUuid(userUuid);
        if (user == null) {
            log.debug("deleteDeviceRelationships user not exist.{}", userUuid);
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }

        Long userTenantId = user.getTenantId();
        Long productId = deviceInfo.getProductId();
        //2.校验产品信息
        GetProductInfoRespVo productInfoResp = productCoreApi.getByProductId(productId);
        if(productInfoResp == null){
            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfoResp.getTenantId().compareTo(userTenantId) != 0) {
            log.debug("deleteDeviceRelationships productTenantId is {} is not equal userTenant{}",productInfoResp.getTenantId(), userTenantId);
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_TENANT_ERROR);
        }
//        if(!"BLE".equals(productInfoResp.getConfigNetMode())){
//            log.debug("deleteDeviceRelationships product config_net_mode is not ble.{}", productInfoResp.getConfigNetMode());
//            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_COMMODE_IS_ERROR);
//        }

        Long userId = user.getId();

        //检查用户设备
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = deviceCoreService.listUserDevices(tenantId, userId, deviceUuid);
        if (CollectionUtils.isEmpty(userDeviceInfoList)) {
            throw new BusinessException(DeviceCoreExceptionEnum.USER_DEVICE_NOT_EXIST);
        }

        //获取设备列表属性，获取组信息
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add(deviceUuid);
        Map<String, Map<String, Object>> deviceStateMap = deviceStateService.listStates(ListDeviceStateReq.builder()
            .tenantId(tenantId).deviceIds(deviceIds).build());

        List groupIds = new ArrayList();
        deviceStateMap.forEach((k, v)->{
            v.forEach((key, value)->{
                if(key.toLowerCase().contains("group") && value != null && !StringUtil.isEmpty(value.toString())){
                    groupIds.add(Long.valueOf(value.toString()));
                }
            });
        });

        UpdateGroupDetailReq groupDetailReq = new UpdateGroupDetailReq();
        groupDetailReq.setTenantId(tenantId);
        groupDetailReq.setDeviceId(deviceUuid);

        //删除user_device关系
        userDeviceCoreApi.delUserDevice(tenantId, userId, deviceUuid);

        //删除device相关表数据，deviceExtend,deviceState,deviceStatus
        //deviceCoreApi.deleteByDeviceId(deviceUuid);
        UpdateDeviceInfoReq deviceInfoReq = UpdateDeviceInfoReq.builder()
                .tenantId(tenantId)
                .uuid(deviceUuid)
                .parentId("")
                .build();
        deviceCoreApi.saveOrUpdate(deviceInfoReq);

        //子设备只删除安防规则 “if” 中的设备
        securityMqttService.deleteSubDevSecurityInfo(tenantId, deviceUuid);

        // 解除设备与房间关系
        spaceDeviceService.deleteSpaceDeviceByDeviceId(tenantId, deviceUuid);

        // 解除设备与情景关系
        sceneDetailService.deleteSceneDetailByDeviceId(deviceUuid, tenantId);

        //删除设备与group关系
        if(!CollectionUtils.isEmpty(groupIds)) {
            UpdateGroupReq groupReq = UpdateGroupReq.builder().groupIds(groupIds)
                    .tenantId(tenantId).createBy(userId).build();
            groupApi.delGroupById(groupReq);
        }else {
            groupApi.delGroupDetial(groupDetailReq);
        }

        // 解除设备与IFTTT关系
        autoService.delByDeviceId(deviceUuid, tenantId);
        if (deviceInfo.getIsDirectDevice() != null
                && deviceInfo.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
            //删除关联的IFTTT规则
            autoService.delByDirectDeviceId(deviceInfo.getUuid(), deviceInfo.getTenantId());
        }
        //软删除活动记录
        ActivityRecordReq recordReq = new ActivityRecordReq();
        recordReq.setForeignId(deviceUuid);
        activityRecordApi.delActivityRecord(recordReq);
    }

    public void dealDevAttr(@RequestBody SetDevAttrNotifReq devAttr){
        GetDeviceInfoRespVo device = deviceCoreService.getDeviceInfoByDeviceId(devAttr.getDevId());
        Map<String, Object> attrMap = devAttr.getAttr();
        deviceService.insertDeviceStatus(device, attrMap);
    }

    private void CheckDeviceName(Long tenantId, Long userId, String devId, String name){
        List<ListUserDeviceInfoRespVo> devices = deviceCoreService.listUserDevices(tenantId, userId);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(devices)) {
            List<String> deviceIds = Lists.newArrayList();
            devices.forEach(userDevice -> {
                deviceIds.add(userDevice.getDeviceId());
            });
            List<ListDeviceInfoRespVo> deviceInfoList = deviceCoreService.listDevicesByDeviceIds(deviceIds);
            // 检查设备名字是否重复

            for (ListDeviceInfoRespVo deviceInfo : deviceInfoList) {
                // 将要被修改的 deviceUuid
                if (deviceInfo.getUuid().equals(devId)) {
                    continue;
                }
                // 只要判断 device.id != deviceId的记录
                if ((!StringUtils.isEmpty(deviceInfo.getName())) && deviceInfo.getName().equals(name)) {
                    throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_NAME_EXIST);
                }
            }
        }
    }

    @Override
    public DevBindNotifVo getDevBindNotifMqttMsg(@RequestParam("userId") String userId,
                                                 @RequestParam("devId") String devId, @RequestParam("tenantId") Long tenantId) {
        MqttMsg mqttMsg = deviceService.getDevBindNotifMqttMsgFromCache(userId, devId, tenantId);

        DevBindNotifVo vo = new DevBindNotifVo();
        vo.setService(mqttMsg.getService());
        vo.setMethod(mqttMsg.getMethod());
        vo.setSeq(mqttMsg.getSeq());
        vo.setSrcAddr(mqttMsg.getSrcAddr());
        vo.setTopic(mqttMsg.getTopic());
        vo.setPayload(mqttMsg.getPayload());
        vo.setAck(mqttMsg.getAck().getCode(), mqttMsg.getAck().getDesc());

        return vo;
    }

    @Override
    public int addAcls(@RequestParam("deviceId") String deviceId) {
        Environment environment = ApplicationContextHelper.getBean(Environment.class);
        String userId = environment.getProperty(Constants.AGENT_MQTT_USERNAME);
        return mqttPloyApi.addAcls(userId, deviceId);
    }

    @Override
    public int addAclsToB(@RequestParam("deviceId") String deviceId) {
        Environment environment = ApplicationContextHelper.getBean(Environment.class);
        String userId = environment.getProperty(Constants.AGENT_MQTT_USERNAME);
        return mqttPloyApi.addAclsToB(userId, deviceId);
    }

    @Override
    public Boolean hasChlid(@RequestParam("deviceId") String deviceId) {
        List<ListDeviceInfoRespVo> deviceInfoList = deviceCoreService.listDevicesByParentId(deviceId);
        if (!CollectionUtils.isEmpty(deviceInfoList)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean sortDev(@RequestParam("tenantId") Long tenantId, @RequestBody List<String> deviceIds) {
        if (deviceIds.size() == 0) {
            log.info("设备列表为空，不进行排序！");
            return false;
        }
        // 循环修改排序
        int index = 1;
        Long spaceId;
        Set<Long> spaceIds = Sets.newHashSet();
        for (String deviceId : deviceIds) {

            SpaceDeviceVo spaceDevice = spaceCoreService.getSpaceByDeviceId(deviceId, tenantId);
            if (spaceDevice == null) {
                //
                log.debug("sortDev tenantId:{} deviceId:{} not exist spaceDevice.", tenantId, deviceId);
                continue;
            }
            com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq = com.iot.shcs.space.vo.SpaceDeviceReq.builder()
                    .id(spaceDevice.getId())
                    .order(index)
                    .tenantId(tenantId)
                    .build();
            spaceDeviceService.updateSpaceDevice(spaceDeviceReq);
            spaceId = spaceDevice.getSpaceId();

            index++;

            spaceIds.add(spaceId);
            if (spaceDevice.getLocationId() != null) {
                spaceIds.add(Long.parseLong(spaceDevice.getLocationId()));
            }

        }

        return true;
    }


    @Override
    public void saveDevice(@RequestBody DeviceVoReq deviceVoReq) throws BusinessException {

        UpdateDeviceInfoReq deviceInfoReq = new UpdateDeviceInfoReq();
        BeanUtil.copyProperties(deviceVoReq, deviceInfoReq);
        deviceInfoReq.setUuid(deviceVoReq.getDeviceId());
        deviceCoreService.saveOrUpdateDeviceInfo(deviceInfoReq);

    }

    @Override
    public void getDeviceList(@RequestParam("deviceId") String deviceId) {
//        MultiProtocolGatewayHepler.queryReq(deviceId);
    }
}
