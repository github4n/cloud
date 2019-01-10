package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.exception.BusinessException;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.mapper.DeviceTypeToStyleMapper;
import com.iot.device.model.DeviceTypeToStyle;
import com.iot.device.service.IDeviceTypeToStyleService;
import com.iot.device.vo.req.DeviceTypeToStyleReq;
import com.iot.device.vo.rsp.DeviceTypeToStyleResp;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DeviceTypeToStyleServiceImpl extends ServiceImpl<DeviceTypeToStyleMapper,DeviceTypeToStyle> implements IDeviceTypeToStyleService {


    @Autowired
    private DeviceTypeToStyleMapper deviceTypeToStyleMapper;

    @Override
    public Long saveOrUpdate(DeviceTypeToStyleReq deviceTypeToStyleReq) {
        DeviceTypeToStyle deviceTypeToStyle = null;
        if (deviceTypeToStyleReq.getId() != null && deviceTypeToStyleReq.getId() > 0) {
            deviceTypeToStyle = super.selectById(deviceTypeToStyleReq.getId());
            if (deviceTypeToStyle == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            deviceTypeToStyle.setUpdateTime(new Date());
            deviceTypeToStyle.setUpdateBy(deviceTypeToStyleReq.getUpdateBy());
        } else {
            deviceTypeToStyle = new DeviceTypeToStyle();
            deviceTypeToStyle.setUpdateTime(new Date());
            deviceTypeToStyle.setUpdateBy(deviceTypeToStyleReq.getUpdateBy());
            deviceTypeToStyle.setCreateTime(new Date());
            deviceTypeToStyle.setCreateBy(deviceTypeToStyleReq.getCreateBy());
        }
        deviceTypeToStyle.setDeviceTypeId(deviceTypeToStyleReq.getDeviceTypeId());
        deviceTypeToStyle.setStyleTemplateId(deviceTypeToStyleReq.getStyleTemplateId());
        deviceTypeToStyle.setDescription(deviceTypeToStyleReq.getDescription());
        deviceTypeToStyle.setTenantId(deviceTypeToStyleReq.getTenantId());
        super.insertOrUpdate(deviceTypeToStyle);
        return deviceTypeToStyle.getId();
    }


    @Override
    public void saveMore(DeviceTypeToStyleReq deviceTypeToStyleReq) {
        List<DeviceTypeToStyle> list = new ArrayList<>();
        deviceTypeToStyleReq.getStyleTemplateIds().forEach(m->{
            DeviceTypeToStyle deviceTypeToStyle = new DeviceTypeToStyle();
            deviceTypeToStyle.setDeviceTypeId(deviceTypeToStyleReq.getDeviceTypeId());
            deviceTypeToStyle.setStyleTemplateId(Long.valueOf(m.toString()));
            deviceTypeToStyle.setCreateBy(deviceTypeToStyleReq.getCreateBy());
            deviceTypeToStyle.setCreateTime(new Date());
            deviceTypeToStyle.setUpdateBy(deviceTypeToStyleReq.getUpdateBy());
            deviceTypeToStyle.setUpdateTime(new Date());
            deviceTypeToStyle.setDescription(deviceTypeToStyleReq.getDescription());
            deviceTypeToStyle.setTenantId(SaaSContextHolder.currentTenantId());
            list.add(deviceTypeToStyle);
        });
        super.insertBatch(list);
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        super.deleteBatchIds(ids);
    }

    @Override
    public List<DeviceTypeToStyleResp> listDeviceTypeStyleByDeviceTypeId(Long deviceTypeId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("device_type_id",deviceTypeId);
        List<DeviceTypeToStyle> list = super.selectList(wrapper);
        List<DeviceTypeToStyleResp> rspList = new ArrayList<>();
        list.forEach(m->{
            DeviceTypeToStyleResp deviceTypeToStyleResp = new DeviceTypeToStyleResp();
            deviceTypeToStyleResp.setId(m.getId());
            deviceTypeToStyleResp.setDeviceTypeId(m.getDeviceTypeId());
            deviceTypeToStyleResp.setStyleTemplateId(m.getStyleTemplateId());
            deviceTypeToStyleResp.setCreateBy(m.getCreateBy());
            deviceTypeToStyleResp.setCreateTime(m.getCreateTime());
            deviceTypeToStyleResp.setUpdateBy(m.getUpdateBy());
            deviceTypeToStyleResp.setUpdateTime(m.getUpdateTime());
            deviceTypeToStyleResp.setDescription(m.getDescription());
            rspList.add(deviceTypeToStyleResp);
        });
        return rspList;
    }
}
