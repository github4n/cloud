package com.iot.mqttploy.restful;

import com.iot.common.exception.BusinessException;
import com.iot.mqttploy.api.MqttPloyApi;
import com.iot.mqttploy.entity.PloyInfo;
import com.iot.mqttploy.service.MqttPloyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： mao2080@sina.com
 * 创建时间：
 * 修改人： mao2080@sina.com
 * 修改时间：${date} ${time}
 * 修改描述：
 */
@RestController
public class MqttPloyRestful implements MqttPloyApi {

    @Autowired
    private MqttPloyService ployService;

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
    @Override
    public int saveAcls(@RequestParam("type")String type, @RequestParam("clientId")String clientId, @RequestParam("uuid")String uuid, @RequestParam("password")String password) throws BusinessException {
        return this.ployService.saveAcls(type, clientId, uuid, password);
    }

    /**
     *
     * 描述：批量新增
     * @author mao2080@sina.com
     * @created 2018年3月13日 下午5:09:07
     * @since
     * @param ployInfos 策略
     * @throws BusinessException
     */
    @Override
    public int batchSaveAcls(@RequestBody  List<PloyInfo> ployInfos) throws BusinessException {
        return this.ployService.batchSaveAcls(ployInfos);
    }

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
    @Override
    public int addAcls(@RequestParam("userId")String userId, @RequestParam("deviceId")String deviceId) throws BusinessException {
        return this.ployService.addAcls(userId, deviceId);
    }

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
    @Override
    public int addAclsToB(@RequestParam("userId")String userId, @RequestParam("deviceId")String deviceId) throws BusinessException {
        return this.ployService.addAclsToB(userId, deviceId);
    }

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
    @Override
    public int separationAcls(@RequestParam("userId")String userId, @RequestParam("deviceId")String deviceId) throws BusinessException {
        return this.ployService.separationAcls(userId, deviceId);
    }

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
    @Override
    public int changePassword(@RequestParam("type")String type, @RequestParam("userId")String userId, @RequestParam("password")String password) throws BusinessException {
        return this.ployService.changePassword(type, userId, password);
    }

}