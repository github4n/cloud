package com.iot.mqttploy.service.impl;


import com.iot.mqttploy.contants.ModuleContants;
import com.iot.mqttploy.exception.BusinessExceptionEnum;
import com.iot.mqttploy.service.MqttPloyService;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.mqttploy.entity.AclsInfo;
import com.iot.mqttploy.entity.PloyInfo;
import com.iot.redis.RedisCacheUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("mqttployService")
public class MqttPloyServiceImpl implements MqttPloyService {

    private static final Logger logger = LoggerFactory.getLogger(MqttPloyServiceImpl.class);

    //mqtt策略用户默认的订阅权限
    @Value("${mqttploy.user.default.subAcls}")
    private String userDefaultSub;

    //mqtt策略用户默认的发布权限
    @Value("${mqttploy.user.default.pubAcls}")
    private String userDefaultPub;

    //mqtt策略用户绑定设备的订阅权限
    @Value("${mqttploy.user.binding.subAcls}")
    private String userBindingSub;

    //mqtt策略用户绑定设备的订阅权限
    @Value("${mqttploy.user.binding.pubAcls}")
    private String userBindingPub;

    //mqtt策略设备默认的订阅权限
    @Value("${mqttploy.device.default.subAcls}")
    private String deviceDefaultSub;

    //mqtt策略设备默认的订阅权限
    @Value("${mqttploy.device.default.pubAcls}")
    private String deviceDefaultPub;

    //mqtt策略设备绑定用户的订阅权限
    @Value("${mqttploy.device.binding.subAcls}")
    private String deviceBindingSub;

    //mqtt策略设备绑定设备的订阅权限
    @Value("${mqttploy.device.binding.pubAcls}")
    private String deviceBindingPub;


    /**
     * 描述：新增策略
     *
     * @param type     策略类型
     * @param clientId clientId
     * @param uuid     用户或设备UUID
     * @param password 加密后的密码
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     */
    @Override
    public int saveAcls(String type, String clientId, String uuid, String password) {
        if (StringUtil.isBlank(type) || StringUtil.isBlank(clientId)
                || StringUtil.isBlank(uuid) || StringUtil.isBlank(password)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        if(!ModuleContants.userStr.equals(type) && !ModuleContants.deviceStr.equals(type)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "type is not 'user' or 'device'");
        }
        String key = generatePwdKey(uuid);
        if (RedisCacheUtil.hasKey(key)) {
            throw new BusinessException(BusinessExceptionEnum.USER_EXIST, "user is exist");
        }
        try {
            Map pwd = new HashMap<String, String>();
            if (type.equals(ModuleContants.userStr)) {//如果是新增用户，在redis中添加app用户和web用户两条记录
                pwd.put(ModuleContants.appStr, password);
                pwd.put(ModuleContants.webStr, password);

            } else if (type.equals(ModuleContants.deviceStr)) {//新增设备
                pwd.put(ModuleContants.devStr, password);
            }
            RedisCacheUtil.hashPutAll(key, pwd, false);
            return ModuleContants.successCode;
        } catch (BusinessException e) {
            throw e;
        }catch (Exception e) {
            String err = "save acls failed.";
            err.concat(", type="+type);
            err.concat(", uuid="+uuid);
            err.concat(", password="+password);
            logger.error(err, e);
            throw new BusinessException(BusinessExceptionEnum.SAVE_ACLS_FAILED);
        }
    }

    /**
     * 描述：批量新增
     *
     * @param ployInfos 策略
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     */
    @Override
    public int batchSaveAcls(List<PloyInfo> ployInfos) {
        return batchSaveAclsInternal("device", ployInfos, null, null);
    }

    private int batchSaveAclsInternal(String type, List<PloyInfo> ployList, List<PloyInfo> addedList, List<PloyInfo> failedList) {
        if (type == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        if (StringUtil.isBlank(type) || !type.equals(ModuleContants.deviceStr)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }

        if (ployList == null || ployList.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
        Map<String, String> map = null;
        for (PloyInfo info : ployList) {
//            try {
//                PloyInfo info = ployList.get(i);
//                if (saveAcls(type, info.getClientId(), info.getUuid(), info.getPassword()) == ModuleContants.successCode) {
//                    if (addedList != null) {//添加成功后放到成功列表中
//                        addedList.add(info);
//                    }
//                } else {
//                    logger.error("batch save ploy one of list failed, type=" + type + ", uuid=" + info.getUuid());
//                    if (failedList != null) {
//                        failedList.add(info);//添加失败后放到失败列表中
//                    }
//                }
//            } catch (Exception e) {
//                if (failedList != null) {
//                    for (; i < ployList.size(); i++) {
//                        failedList.add(ployList.get(i));
//                    }
//                }
//                throw new BusinessException(BusinessExceptionEnum.BATCH_SAVE_ACLS_FAILED);
//            }
        	map = new HashMap<String, String>();
        	map.put(ModuleContants.devStr, info.getPassword());
			maps.put(info.getClientId() + ModuleContants.splitStr + ModuleContants.pwdStr, map);
        }
        RedisCacheUtil.hashPutBatch(maps);
        return ModuleContants.successCode;
    }

    /**
     * 描述：附加策略
     *
     * @param userId   用户id
     * @param deviceId 设备id
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     */
    @Override
    public int addAcls(String userId, String deviceId) {
        if (StringUtil.isBlank(userId)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "user id is null or blank");
        }
        if(StringUtil.isBlank(deviceId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "device id is null or blank");
        }
        String userPubKey = generatePubKey(userId);
        String userSubKey = generateSubKey(userId);
        String devPubKey = generatePubKey(deviceId);
        String devSubKey = generateSubKey(deviceId);

        Set<String> userPubList = generateUserPubPloy(deviceId);
        if(userPubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate user pub ploy failed");
        }
        Set<String> userSubList = generateUserSubPloy(deviceId);
        if(userSubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate user sub ploy failed");
        }
        Set<String> devPubList = generateDevPubPloy(userId);
        if(devPubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate device pub ploy failed");
        }
        Set<String> devSubList = generateDevSubPloy(userId);
        if(devSubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate device sub ploy failed");
        }

        //这里keyArray 和 valueArray 中的元素必须一一对应
        String[] keyArray = {userPubKey, userSubKey, devPubKey, devSubKey};
        ArrayList<Set<String>> valueArray = new ArrayList<>(Arrays.asList(userPubList, userSubList, devPubList, devSubList));
        int flag = 1;

        try {
            for (int i = 0; i < keyArray.length; i++, flag++){
                Set<String> list = valueArray.get(i);
                for (String ploy : list){
                    RedisCacheUtil.setPush(keyArray[i], ploy, false);
                }
            }
            return ModuleContants.successCode;
        } catch (Exception e) {
            for (int i = 0; i < flag; i++){
                Set<String> list = valueArray.get(i);
                for (String ploy : list){
                    RedisCacheUtil.setRemove(keyArray[i], ploy, false);
                }
            }
            String err = "addAcls failed, userId=" + userId + ", deviceId="+deviceId;
            logger.error(err, e);
            throw new BusinessException(BusinessExceptionEnum.ADD_ACLS_FAILED, err);
        }
    }


    /**
     * 描述：ToB附加策略
     *
     * @param userId   用户id
     * @param deviceId 设备id
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     */
    @Override
    public int addAclsToB(String userId, String deviceId) {
        if (StringUtil.isBlank(userId)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "user id is null or blank");
        }
        if(StringUtil.isBlank(deviceId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "device id is null or blank");
        }
        String devPubKey = generatePubKey(deviceId);
        String devSubKey = generateSubKey(deviceId);

        Set<String> devPubList = new HashSet<String>();
        devPubList.add("iot/v1/s/#");
        Set<String> devSubList = new HashSet<String>();
        devSubList.add("iot/v1/c/#");

        //这里keyArray 和 valueArray 中的元素必须一一对应
        String[] keyArray = {devPubKey, devSubKey};
        ArrayList<Set<String>> valueArray = new ArrayList<>(Arrays.asList(devPubList, devSubList));
        int flag = 1;

        try {
            for (int i = 0; i < keyArray.length; i++, flag++){
                Set<String> list = valueArray.get(i);
                for (String ploy : list){
                    RedisCacheUtil.setPush(keyArray[i], ploy, false);
                }
            }
            return ModuleContants.successCode;
        } catch (Exception e) {
            for (int i = 0; i < flag; i++){
                Set<String> list = valueArray.get(i);
                for (String ploy : list){
                    RedisCacheUtil.setRemove(keyArray[i], ploy, false);
                }
            }
            String err = "addAcls failed, userId=" + userId + ", deviceId="+deviceId;
            logger.error(err, e);
            throw new BusinessException(BusinessExceptionEnum.ADD_ACLS_FAILED, err);
        }
    }

    /**
     * 描述：分离策略
     *
     * @param userId   用户
     * @param deviceId 设备id
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     */
    @Override
    public int separationAcls(String userId, String deviceId) {
        if (StringUtil.isBlank(userId)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "user id is null or blank");
        }if(StringUtil.isBlank(deviceId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "device id is null or blank");
        }
        String userPubKey = generatePubKey(userId);
        String userSubKey = generateSubKey(userId);
        String devPubKey = generatePubKey(deviceId);
        String devSubKey = generateSubKey(deviceId);

        Set<String> userPubList = generateUserPubPloy(deviceId);
        if(userPubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate user pub ploy failed");
        }
        Set<String> userSubList = generateUserSubPloy(deviceId);
        if(userSubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate user sub ploy failed");
        }
        Set<String> devPubList = generateDevPubPloy(userId);
        if(devPubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate device pub ploy failed");
        }
        Set<String> devSubList = generateDevSubPloy(userId);
        if(devSubList == null){
            throw new BusinessException(BusinessExceptionEnum.GENERATE_PLOY_FAILED, "generate device sub ploy failed");
        }

        //这里keyArray 和 valueArray 中的元素必须一一对应
        String[] keyArray = {userPubKey, userSubKey, devPubKey, devSubKey};
        ArrayList<Set<String>> valueArray = new ArrayList<>(Arrays.asList(userPubList, userSubList, devPubList, devSubList));
        int flag = 1;

        try {
            for (int i = 0; i < keyArray.length; i++, flag++){
                Set<String> list = valueArray.get(i);
                for (String ploy : list){
                    RedisCacheUtil.setRemove(keyArray[i], ploy, false);
                }
            }
            return ModuleContants.successCode;
        } catch (Exception e) {
            for (int i = 0; i < flag; i++){
                Set<String> list = valueArray.get(i);
                for (String ploy : list){
                    RedisCacheUtil.setPush(keyArray[i], ploy, false);
                }
            }
            String err = "separationAcls failed, user id=" + userId + ", deviceId="+deviceId;
            logger.error(err, e);
            throw new BusinessException(BusinessExceptionEnum.SEPARATION_ACLS_FAILED, err);
        }
    }

    /**
     * 描述：修改密码
     *
     * @param type     类型（app/web）
     * @param uuid   用户ID
     * @param password 加密后的密码
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     */
    @Override
    public int changePassword(String type, String uuid, String password) {
        if (StringUtil.isBlank(type)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "type is null or blank");
        }if( StringUtil.isBlank(uuid)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "user id is null or blank");
        }if( StringUtil.isBlank(password)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "password id is null or blank");
        }
        if (!type.equals(ModuleContants.appStr) && !type.equals(ModuleContants.webStr)
                && !type.equals(ModuleContants.alexaStr) && !type.equals(ModuleContants.googleHomeStr)) {
            String err = new StringBuilder().append("type is not in '").append(ModuleContants.appStr).append("','").append(ModuleContants.webStr)
                    .append("','").append(ModuleContants.alexaStr).append("','").append(ModuleContants.googleHomeStr).append("'").toString();
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, err);
        }
        String key = generatePwdKey(uuid);
        if (!RedisCacheUtil.hasKey(key)) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_EXIST, "user not exist");
        }
        try {
            RedisCacheUtil.hashPut(key, type, password, false);
            return ModuleContants.successCode;
        } catch (Exception e) {
            String err = "changePassword failed, type="+type+",uuid id=" + uuid + ", password="+password;
            logger.error(err, e);
            throw new BusinessException(BusinessExceptionEnum.CHANGE_PASSWD_FAILED, err);
        }
    }


    /*******************private method**********************************************/
    /**
     * 描述：将字符串数组中每个元素中含有关键字key替换成另一个字符串
     *
     * @param list 需要替换的数组
     *
     * @param keyWord 被替换的字符串
     *
     * @param replace 新的字符串
     *
     * @author ouyangjie
     * @created 2018年3月19日 下午5:09:07
     * @since
     */
    private Set<String> replaceKeyWord(String[] list, String keyWord, String replace)
    {
        Set<String> result = null;
        if (list != null) {
            result = new HashSet<String>();
            for (int i = 0; i < list.length; i++) {
                result.add(list[i].replace(keyWord, replace));
            }
        }
        return result;
    }

    /**
     * 描述：根据设备uuid生成该设备对应的发布权限字符串数组
     *
     * @param deviceId 设备的uuid
     *
     * @author ouyangjie
     * @created 2018年3月19日 下午5:09:07
     * @since
     */
    private Set<String> generateUserPubPloy(String deviceId) {
        String[] list = userBindingPub.split(",");
        return replaceKeyWord(list, ModuleContants.devFlag, deviceId);
    }

    /**
     * 描述：根据设备uuid生成该设备对应的订阅权限字符串数组
     *
     * @param deviceId 设备的uuid
     *
     * @author ouyangjie
     * @created 2018年3月19日 下午5:09:07
     * @since
     */
    private Set<String> generateUserSubPloy(String deviceId) {
        String[] list = userBindingSub.split(",");
        return replaceKeyWord(list, ModuleContants.devFlag, deviceId);
    }

    /**
     * 描述：根据用户uuid生成该用户对应的发布权限字符串数组
     *
     * @param  userId 用户的uuid
     *
     * @author ouyangjie
     * @created 2018年3月19日 下午5:09:07
     * @since
     */
    private Set<String> generateDevPubPloy(String userId) {
        String[] list = deviceBindingPub.split(",");
        return replaceKeyWord(list, ModuleContants.userFlag, userId);
    }

    /**
     * 描述：根据用户uuid生成该用户对应的订阅权限字符串数组
     *
     * @param userId 用户的uuid
     *
     * @author ouyangjie
     * @created 2018年3月19日 下午5:09:07
     * @since
     */
    private Set<String> generateDevSubPloy(String userId) {
        String[] list = deviceBindingSub.split(",");
        return replaceKeyWord(list, ModuleContants.userFlag, userId);
    }

    private String generatePwdKey(String uuid)
    {
        return new StringBuilder().append(uuid).append(ModuleContants.splitStr).append(ModuleContants.pwdStr).toString();
    }

    private String generatePubKey(String uuid)
    {
        return new StringBuilder().append(uuid).append(ModuleContants.splitStr).append(ModuleContants.pubStr).toString();
    }

    private String generateSubKey(String uuid)
    {
        return new StringBuilder().append(uuid).append(ModuleContants.splitStr).append(ModuleContants.subStr).toString();
    }
}
