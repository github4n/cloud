package com.iot.control.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.control.device.entity.UserDevice;
import com.iot.control.device.mapper.UserDeviceMapper;
import com.iot.control.device.service.IUserDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户设备关系表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Slf4j
@Service
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements IUserDeviceService {

    private static final int batchSize = 5;

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Transactional
    public void saveOrUpdate(UserDevice entity) {
        super.insertOrUpdate(entity);
    }

    @Transactional
    public void saveUserDevice(UserDevice entity) {

    }

    @Transactional
    public void updateUserDevice(UserDevice entity) {

    }

    @Transactional
    public void saveOrUpdateBatch(List<UserDevice> entityList) {
        super.insertOrUpdateBatch(entityList);
    }

    @Override
    public Page<UserDevice> findPageByParams(Integer pageNum, Integer pageSize, Long tenantId, Long orgId, Long userId) {
        EntityWrapper ew = new EntityWrapper();
        if (tenantId != null) {
            ew.eq("tenant_id", tenantId);
        }
        if (orgId != null) {
            ew.eq("org_id", orgId);
        }
        if (userId != null) {
            ew.eq("user_id", userId);
        }

        Page<UserDevice> page = new Page<>(pageNum, pageSize);
        page = super.selectPage(page, ew);
        return page;
    }


    public List<UserDevice> getByDeviceId(Long tenantId, Long orgId, String deviceId) {
        EntityWrapper ew = new EntityWrapper();
        if (tenantId != null) {
            ew.eq("tenant_id", tenantId);
        }
        if (orgId != null) {
            ew.eq("org_id", orgId);
        }
        if (!StringUtils.isEmpty(deviceId)) {
            ew.eq("device_id", deviceId);
        }
        List<UserDevice> resultDataList = super.selectList(ew);
        return resultDataList;
    }

    public List<UserDevice> getByParams(Long tenantId, Long orgId, Long userId, String deviceId, String userType) {
        EntityWrapper ew = new EntityWrapper();
        if (tenantId != null) {
            ew.eq("tenant_id", tenantId);
        }
        if (orgId != null) {
            ew.eq("org_id", orgId);
        }
        if (userId != null) {
            ew.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(deviceId)) {
            ew.eq("device_id", deviceId);
        }
        if (!StringUtils.isEmpty(userType)) {
            ew.eq("user_type", userType);
        }
        List<UserDevice> resultDataList = super.selectList(ew);
        return resultDataList;
    }

    public List<UserDevice> getByDeviceId(String deviceId) {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("device_id", deviceId);
        List<UserDevice> resultDataList = super.selectList(ew);
        return resultDataList;
    }

    @Override
    public List<UserDevice> findListByDeviceIds(Long tenantId, Long orgId, Long userId, List<String> deviceIds) {
        List<UserDevice> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return resultDataList;
        }
        EntityWrapper ew = new EntityWrapper();
        if (tenantId != null) {
            ew.eq("tenant_id", tenantId);
        }
        if (orgId != null) {
            ew.eq("org_id", orgId);
        }
        if (userId != null) {
            ew.eq("user_id", userId);
        }
        ew.in("device_id", deviceIds);

        resultDataList = super.selectList(ew);
        return resultDataList;

    }

    @Override
    public List<UserDevice> findListByDeviceIds(Long tenantId, Long orgId, Long userId, List<String> deviceIds, int batchSize) {
        List<UserDevice> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return resultDataList;
        }
        List<String> batchIds = Lists.newArrayList();
        for (int i = 0; i < deviceIds.size(); i++) {
            batchIds.add(deviceIds.get(i));
            if (i >= 1 && i % batchSize == 0) {
                //获取一次 并清空batchIds
                List<UserDevice> tempResultDataList = this.findListByDeviceIds(tenantId, orgId, userId, batchIds);
                if (!CollectionUtils.isEmpty(tempResultDataList)) {
                    resultDataList.addAll(tempResultDataList);
                }
                batchIds.clear();
            }
        }
        List<UserDevice> tempResultDataList = this.findListByDeviceIds(tenantId, orgId, userId, batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataList)) {
            resultDataList.addAll(tempResultDataList);
        }
        return resultDataList;
    }

    @Override
    public void delUserDevice(Long tenantId, Long userId, String deviceId) {
        EntityWrapper wrapper = new EntityWrapper();
        if (tenantId != null) {
            wrapper.eq("tenant_id", tenantId);
        }
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(deviceId)) {

            wrapper.eq("device_id", deviceId);
        }
        super.delete(wrapper);
    }
}
