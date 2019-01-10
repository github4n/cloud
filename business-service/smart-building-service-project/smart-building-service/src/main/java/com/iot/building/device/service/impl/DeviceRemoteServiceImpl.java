package com.iot.building.device.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.iot.building.device.mapper.*;
import com.iot.building.device.vo.*;
import com.iot.device.vo.rsp.ProductResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.device.service.IDeviceRemoteService;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.exception.DeviceRemoteExceptionEnum;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Service
public class DeviceRemoteServiceImpl implements IDeviceRemoteService {
    @Autowired
    private DeviceRemoteControlMapper deviceRemoteControlMapper;
    @Autowired
    private DeviceRemoteControlSceneMapper deviceRemoteControlSceneMapper;
    @Autowired
    private DeviceRemoteControlTemplateMapper deviceRemoteControlTemplateMapper;
    @Autowired
    private DeviceRemoteTemplateMapper deviceRemoteTemplateMapper;
    @Autowired
    private DeviceRemoteControlSceneTemplateMapper deviceRemoteControlSceneTemplateMapper;
    @Autowired
    private DeviceRemoteTypeMapper deviceRemoteTypeMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 新增遥控器模板
     *
     * @param deviceRemoteTemplateReq
     */
    @Override
    public void addDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq) {
        //判断 一个business_type 一个遥控器模板
        DeviceRemoteTemplate deviceRemoteTemplateTemp = deviceRemoteTemplateMapper.findByBusinessType(deviceRemoteTemplateReq.getBusinessTypeId());
        if (deviceRemoteTemplateTemp != null) {
            throw new BusinessException(DeviceRemoteExceptionEnum.DEVICEREMOTE_DEVICEBUSINESSTYPE_EXIST);
        }
        //新增遥控器主表
        DeviceRemoteTemplate deviceRemoteTemplate = new DeviceRemoteTemplate();
        BeanUtils.copyProperties(deviceRemoteTemplateReq, deviceRemoteTemplate);
        deviceRemoteTemplate.setType(Long.valueOf(deviceRemoteTemplateReq.getType()));
        deviceRemoteTemplateMapper.insertGetId(deviceRemoteTemplate);
        Long remoteid = deviceRemoteTemplate.getId();

        addDeviceRemoteControlTemplate(deviceRemoteTemplateReq, remoteid);

    }

    private void addDeviceRemoteControlTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq, Long remoteid) {
        int j = 1;
        Integer press = 1;
        for (DeviceRemoteControlTemplateReq deviceRemoteControlTemplateReq : deviceRemoteTemplateReq.getDeviceRemoteControlTemplateReqs()) {
            DeviceRemoteControlTemplate deviceRemoteControlTemplate = new DeviceRemoteControlTemplate();
            BeanUtils.copyProperties(deviceRemoteControlTemplateReq, deviceRemoteControlTemplate);
            deviceRemoteControlTemplate.setKeyCode(j + "");
            deviceRemoteControlTemplate.setRemoteId(remoteid);
            deviceRemoteControlTemplate.setDefaultValue("");
            setEventStatus(deviceRemoteControlTemplate);
            //新增遥控器细表
            deviceRemoteControlTemplateMapper.insert(deviceRemoteControlTemplate);
            if (!StringUtils.isEmpty(deviceRemoteControlTemplateReq.getScenes())) {
                int i = 1;
                for (String sceneId : deviceRemoteControlTemplateReq.getScenes().split(",")) {
                    DeviceRemoteControlSceneTemplate deviceRemoteControlSceneTemplate = new DeviceRemoteControlSceneTemplate();
                    deviceRemoteControlSceneTemplate.setControlTemplateId(deviceRemoteControlTemplate.getId());
                    deviceRemoteControlSceneTemplate.setRelationId(Long.valueOf(sceneId));
                    deviceRemoteControlSceneTemplate.setDataStatus(1);
                    deviceRemoteControlSceneTemplate.setSort(i);
                    deviceRemoteControlSceneTemplate.setTenantId(deviceRemoteControlTemplateReq.getTenantId());
                    //如果遥控器细表的type是scene 保存到中间表
                    deviceRemoteControlSceneTemplateMapper.insert(deviceRemoteControlSceneTemplate);
                    i++;
                }
            }
            j++;
            //如果是长按改成11
            if (deviceRemoteControlTemplateReq.getPress() == 2 && j < 11) {
                j = 11;
            }
        }
    }

    /**
     * 修改遥控器模板
     *
     * @param deviceRemoteTemplateReq
     */
    @Override
    public void updateDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq) {
        //判断 一个business_type 一个遥控器模板
        DeviceRemoteTemplate deviceRemoteTemplateTemp = deviceRemoteTemplateMapper.findByBusinessType(deviceRemoteTemplateReq.getBusinessTypeId());
        if (deviceRemoteTemplateTemp != null && (deviceRemoteTemplateTemp.getId() != deviceRemoteTemplateReq.getId())) {
            throw new BusinessException(DeviceRemoteExceptionEnum.DEVICEREMOTE_DEVICEBUSINESSTYPE_EXIST);
        }
        DeviceRemoteTemplate deviceRemoteTemplate = new DeviceRemoteTemplate();
        BeanUtils.copyProperties(deviceRemoteTemplateReq, deviceRemoteTemplate);
        deviceRemoteTemplate.setType(Long.valueOf(deviceRemoteTemplateReq.getType()));
        deviceRemoteTemplateMapper.updateDeviceRemoteTemplate(deviceRemoteTemplate);
        //删除之前的细表 和 细表的场景关联表
        List<DeviceRemoteControlTemplate> deviceRemoteControlTemplates = deviceRemoteControlTemplateMapper.selectByRemoteId(deviceRemoteTemplateReq.getTenantId(), deviceRemoteTemplateReq.getId());
        if (deviceRemoteControlTemplates != null && deviceRemoteControlTemplates.size() > 0) {
            for (DeviceRemoteControlTemplate deviceRemoteControlTemplateReq : deviceRemoteControlTemplates) {
                Long deviceRemoteControlTemplateId = deviceRemoteControlTemplateReq.getId();
                //物理删除之前的细表
                deviceRemoteControlTemplateMapper.deleteById(deviceRemoteControlTemplateId);
                //物理删除细表的场景关联表
                deviceRemoteControlSceneTemplateMapper.deleteByControlTemplateTd(deviceRemoteControlTemplateId);
            }
        }

        //重新添加之前的细表和 细表的场景关联表素具
        addDeviceRemoteControlTemplate(deviceRemoteTemplateReq, deviceRemoteTemplateReq.getId());
    }

    /**
     * 根据id删除遥控器模板  逻辑删除
     *
     * @param id
     */
    @Override
    public void deleteDeviceRemoteTemplate(Long tenantId, Long id, Long userId) {
        // 逻辑删除遥控器主表
        deviceRemoteTemplateMapper.deleteDeviceRemoteTemplateById(id, userId);
        //获取遥控器细表的数据
        List<DeviceRemoteControlTemplate> deviceRemoteControlTemplates = deviceRemoteControlTemplateMapper.selectByRemoteId(tenantId, id);
        if (deviceRemoteControlTemplates != null && deviceRemoteControlTemplates.size() > 0) {
            //删除之前的细表 和 细表的场景关联表
            for (DeviceRemoteControlTemplate deviceRemoteControlTemplate : deviceRemoteControlTemplates) {
                Long deviceRemoteControlTemplateId = deviceRemoteControlTemplate.getId();
                //逻辑删除之前的细表
                deviceRemoteControlTemplateMapper.deleteDeviceRemoteControlTemplateById(deviceRemoteControlTemplateId);
                //逻辑删除细表的场景关联表
                deviceRemoteControlSceneTemplateMapper.deleteDeviceRemoteControlSceneTemplateById(deviceRemoteControlTemplateId);
            }
        }
    }

    /**
     * 获取遥控器模板详情
     *
     * @param id
     * @return
     */
    public DeviceRemoteTemplateResp getDeviceRemoteTemplateById(Long tenantId, Long id) {
        DeviceRemoteTemplateResp deviceRemoteTemplateResp = new DeviceRemoteTemplateResp();
        //查询模板主表
        DeviceRemoteTemplate deviceRemoteTemplate = deviceRemoteTemplateMapper.findById(id);
        BeanUtils.copyProperties(deviceRemoteTemplate, deviceRemoteTemplateResp);
        deviceRemoteTemplateResp.setType(deviceRemoteTemplate.getType() + "");
        //查询模板细表
        List<DeviceRemoteControlTemplate> deviceRemoteControlTemplates = deviceRemoteControlTemplateMapper.selectByRemoteId(tenantId, id);
        for (DeviceRemoteControlTemplate deviceRemoteControlTemplate : deviceRemoteControlTemplates) {
            DeviceRemoteControlTemplateResp deviceRemoteControlTemplateResp = new DeviceRemoteControlTemplateResp();
            BeanUtils.copyProperties(deviceRemoteControlTemplate, deviceRemoteControlTemplateResp);
            //获取细表关联的场景表
            List<DeviceRemoteControlSceneTemplate> deviceRemoteControlSceneTemplates = deviceRemoteControlSceneTemplateMapper.findByDeviceRemoteControlTemplateId(deviceRemoteControlTemplate.getId());
            if (deviceRemoteControlSceneTemplates != null && deviceRemoteControlSceneTemplates.size() > 0) {
                //转逗号分隔
                deviceRemoteControlTemplateResp.setScenes(deviceRemoteControlSceneTemplates.stream().map(one -> one.getRelationId().toString()).collect(Collectors.joining(",")));
            }
            deviceRemoteTemplateResp.getDeviceRemoteControlTemplateReqs().add(deviceRemoteControlTemplateResp);
        }

        return deviceRemoteTemplateResp;
    }

    @Override
    public DeviceRemoteTemplateResp getDeviceRemoteTemplateByBusinessTypeId(Long tenantId, Long businessTypeId) {
        DeviceRemoteTemplateResp deviceRemoteTemplateResp = new DeviceRemoteTemplateResp();
        //查询模板主表
        DeviceRemoteTemplate deviceRemoteTemplate = deviceRemoteTemplateMapper.findByBusinessTypeId(businessTypeId);
        BeanUtils.copyProperties(deviceRemoteTemplate, deviceRemoteTemplateResp);
        deviceRemoteTemplateResp.setType(deviceRemoteTemplate.getType() + "");
        //查询模板细表
        List<DeviceRemoteControlTemplate> deviceRemoteControlTemplates = deviceRemoteControlTemplateMapper.selectByRemoteId(tenantId, deviceRemoteTemplate.getId());
        for (DeviceRemoteControlTemplate deviceRemoteControlTemplate : deviceRemoteControlTemplates) {
            DeviceRemoteControlTemplateResp deviceRemoteControlTemplateResp = new DeviceRemoteControlTemplateResp();
            BeanUtils.copyProperties(deviceRemoteControlTemplate, deviceRemoteControlTemplateResp);
            //获取细表关联的场景表
            List<DeviceRemoteControlSceneTemplate> deviceRemoteControlSceneTemplates = deviceRemoteControlSceneTemplateMapper.findByDeviceRemoteControlTemplateId(deviceRemoteControlTemplate.getId());
            if (deviceRemoteControlSceneTemplates != null && deviceRemoteControlSceneTemplates.size() > 0) {
                //转逗号分隔
                deviceRemoteControlTemplateResp.setScenes(deviceRemoteControlSceneTemplates.stream().map(one -> one.getRelationId().toString()).collect(Collectors.joining(",")));
            }
            deviceRemoteTemplateResp.getDeviceRemoteControlTemplateReqs().add(deviceRemoteControlTemplateResp);
        }

        return deviceRemoteTemplateResp;
    }

    @Override
    public void deleteDeviceRemoteControlIfExsit(Long tenantId, String deviceId, Long businessTypeId) {
        //删除从表
        deviceRemoteControlSceneMapper.removeByDeviceId(deviceId);
        //删除主表
        deviceRemoteControlMapper.removeByDeviceId(deviceId);
    }

    @Override
    public void addDeviceRemoteControl(List<DeviceRemoteControlReq> deviceRemoteControlReqs) {
        for (DeviceRemoteControlReq deviceRemoteControlReq : deviceRemoteControlReqs) {
            //添加遥控器键的表
            Long id = deviceRemoteControlMapper.insertGetId(deviceRemoteControlReq);
            String scenes = deviceRemoteControlReq.getScenes();
            //判断是否有场景 有的话添加中间表
            if (!StringUtils.isEmpty(scenes)) {
                String[] sceneArr = scenes.split(",");
                for (int i = 0; i < sceneArr.length; i++) {
                    String sceneId = sceneArr[i];
                    DeviceRemoteControlScene deviceRemoteControlScene = new DeviceRemoteControlScene();
                    deviceRemoteControlScene.setControlId(id);
                    deviceRemoteControlScene.setRelationId(Long.valueOf(sceneId));
                    deviceRemoteControlScene.setSort(i + 1);
                    deviceRemoteControlSceneMapper.insert(deviceRemoteControlScene);
                }
            }
        }
    }

    @Override
    public List<Long> listDeviceRemoteBusinessType(Long tenantId) {
        return deviceRemoteTemplateMapper.listDeviceRemoteBusinessType(tenantId);
    }

    @Override
    public List<DeviceRemoteControlResp> listDeviceRemoteControlByBusinessTypeId(Long tenantId, Long businessTypeId) {
        return null;
    }

    public Page<DeviceRemoteTemplateSimpleResp> findDeviceRemoteTemplatePage(DeviceRemoteTemplatePageReq pageReq) {
        Page<DeviceRemoteTemplateSimpleResp> pageResult = new Page<>();
        Pagination pagination = new Pagination(pageReq.getPageNum(), pageReq.getPageSize());
        List<DeviceRemoteTemplateSimpleResp> respList = deviceRemoteTemplateMapper.findDeviceRemoteTemplatePage(pagination, pageReq);
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(respList);
        return pageResult;
    }

    public List<DeviceRemoteTypeResp> listDeviceRemoteTypes(Long tenantId) {
        return deviceRemoteTypeMapper.listAll(tenantId);
    }

    public void setEventStatus(DeviceRemoteControlTemplate deviceRemoteControlTemplate) {
        deviceRemoteControlTemplate.setEventStatus("default");
        //如果是组的情况
        if ("GROUP".equals(deviceRemoteControlTemplate.getType())) {
            if (!StringUtils.isEmpty(deviceRemoteControlTemplate.getFunction())) {
                switch (deviceRemoteControlTemplate.getFunction()) {
                    case "ONOFF": {
                        deviceRemoteControlTemplate.setEventStatus("onoff");
                        break;
                    }
                    case "DIMMING_ADD": {
                        deviceRemoteControlTemplate.setEventStatus("dimdec");
                        break;
                    }
                    case "DIMMING_SUB": {
                        deviceRemoteControlTemplate.setEventStatus("diminc");
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public List<DeviceRemoteControlResp> findRemoteControlByDeviceType(Long tenantId, Long deviceTypeId) {
        return deviceRemoteControlMapper.findRemoteControlByDeviceType(deviceTypeId);
    }

    @Override
    public List<DeviceRespVo> findDevListByDeviceIds(List<String> deviceIdList, boolean isCheckUserNotNull, Long userId) {

        List<DeviceRespVo> deviceRespList = deviceMapper.selectDevListByDeviceIds(deviceIdList, isCheckUserNotNull, userId);
        return deviceRespList;
    }

    @Override
    public List<ProductResp> listProducts(List<Long> productIds) {
        List<ProductResp> list = deviceMapper.listProducts(productIds);
        return list;
    }
}


