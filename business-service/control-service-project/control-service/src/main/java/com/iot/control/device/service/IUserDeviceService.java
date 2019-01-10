package com.iot.control.device.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.iot.control.device.entity.UserDevice;

import java.util.List;

/**
 * <p>
 * 用户设备关系表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IUserDeviceService extends IService<UserDevice> {
    /**
     * 保存或修改用户跟设备的信息
     *
     * @param entity
     */
    void saveOrUpdate(UserDevice entity);

    void saveUserDevice(UserDevice entity);

    void updateUserDevice(UserDevice entity);

    /**
     * 保存或修改用户跟设备的信息
     *
     * @param entityList
     */
    void saveOrUpdateBatch(List<UserDevice> entityList);

    /**
     * 获取用户对应的设备列表
     *
     * @param pageNum
     * @param pageSize
     * @param tenantId
     * @param orgId
     * @param userId
     * @return
     * @author lucky
     * @date 2018/9/17 14:07
     */
    Page<UserDevice> findPageByParams(Integer pageNum, Integer pageSize, Long tenantId, Long orgId, Long userId);


    /**
     * 获取用户对应的设备
     *
     * @param tenantId
     * @param orgId    不必填
     * @param userId   不必填
     * @param deviceId 不必填
     *                 @param userType 不必填
     * @return
     * @author lucky
     * @date 2018/9/18 16:59
     */
    List<UserDevice> getByParams(Long tenantId, Long orgId, Long userId, String deviceId, String userType);


    List<UserDevice> findListByDeviceIds(Long tenantId, Long orgId, Long userId, List<String> deviceIds);

    List<UserDevice> findListByDeviceIds(Long tenantId, Long orgId, Long userId, List<String> deviceIds, int batchSize);

    void delUserDevice(Long tenantId, Long userId, String deviceId);
}
