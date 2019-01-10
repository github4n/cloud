package com.iot.mqttploy.service;

import com.iot.common.exception.BusinessException;
import com.iot.mqttploy.entity.PloyInfo;

import java.util.List;


public interface MqttPloyService {

    /**
     *
     * 描述：新增策略
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     * @param type 策略类型
     * @param clientId clientId
     * @param uuid 用户或设备UUID
     * @param password 加密后的密码
     * @throws BusinessException
     */
    public int saveAcls(String type, String clientId, String uuid, String password);

    /**
     *
     * 描述：批量新增
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     * @param ployInfos 策略
     * @throws BusinessException
     */
    public int batchSaveAcls(List<PloyInfo> ployInfos);

    /**
     *
     * 描述：附加策略
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     * @param userId 用户id
     * @param deviceId 设备id
     * @throws BusinessException
     */
    public int addAcls(String userId, String deviceId);

    /**
     *
     * 描述：ToB附加策略
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     * @param userId 用户id
     * @param deviceId 设备id
     * @throws BusinessException
     */
    public int addAclsToB(String userId, String deviceId);


    /**
     *
     * 描述：分离策略
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     * @param userId 用户
     * @param deviceId 设备id
     * @throws BusinessException
     */
    public int separationAcls(String userId, String deviceId);

    /**
     *
     * 描述：修改密码
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     * @param type 类型（0表示web,1表示app）
     * @param userId 用户ID
     * @param password 加密后的密码
     * @throws BusinessException
     */
    public int changePassword(String type, String userId, String password);

}
