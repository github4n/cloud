package com.iot.boss.service.packagemanager.impl;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.service.packagemanager.IPackageInfoService;
import com.iot.boss.vo.packagemanager.req.BossSavePackageReq;
import com.iot.boss.vo.packagemanager.req.BossSearchPackageReq;
import com.iot.boss.vo.packagemanager.req.BossUpdatePackageReq;
import com.iot.boss.vo.packagemanager.resp.PackageDeviceTypeInfoResp;
import com.iot.boss.vo.packagemanager.resp.PackageListResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.PackageApi;
import com.iot.control.packagemanager.api.PackageDeviceTypeApi;
import com.iot.control.packagemanager.enums.PackageTypeEnum;
import com.iot.control.packagemanager.vo.req.PackageReq;
import com.iot.control.packagemanager.vo.req.PagePackageReq;
import com.iot.control.packagemanager.vo.req.SavePackageDeviceTypeReq;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import com.iot.device.api.*;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.EventResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;
import com.iot.file.api.FileApi;
import com.iot.payment.enums.goods.TechnicalSchemeEnum;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @despriction：套包信息service
 * @author  yeshiyuan
 * @created 2018/11/21 8:59
 */
@Service
public class PackageInfoServiceImpl implements IPackageInfoService{
    @Autowired
    private FileApi fileApi;
    @Autowired
    private PackageApi packageApi;
    @Autowired
    private PackageDeviceTypeApi packageDeviceTypeApi;
    @Autowired
    private DeviceTypeApi deviceTypeApi;
    @Autowired
    private TechnicalRelateApi technicalRelateApi;
    @Autowired
    private ServicePropertyApi servicePropertyApi;

    private static Long TENANT_ID = -1L;

    @Autowired
    private DeviceTypeToServiceModuleApi deviceTypeToServiceModuleApi;

    /**
     *@description 保存套包，并返回该套包的id
     *@author wucheng
     *@params [req]
     *@create 2018/12/5 10:30
     *@return java.lang.Long
     */
    @Override
    public Long addPackage(BossSavePackageReq req) {
        if (req == null) {
            throw new BusinessException(BossExceptionEnum.PACKAGE_PARAMS_NOT_EXIST);
        }
        if (StringUtil.isBlank(req.getFileId())) {
            throw new BusinessException(BossExceptionEnum.PACKAGE_IMAGE_IS_NULL);
        }
        if (StringUtil.isBlank(req.getName())) {
            throw new BusinessException(BossExceptionEnum.PACKAGE_NAME_IS_NULL);
        }
        // 保存图片uuid
        fileApi.saveFileInfosToDb(Arrays.asList(req.getFileId()));

        PackageReq packageReq = new PackageReq();
        BeanUtil.copyProperties(req, packageReq);
        packageReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        packageReq.setCreateTime(new Date());
        packageReq.setIcon(req.getFileId());
        // 设置套包类型
        if (PackageTypeEnum.AUTOMATION.getValue() == req.getPackageType()) {
            packageReq.setPackageType(PackageTypeEnum.AUTOMATION.getValue());
        } else if (PackageTypeEnum.SECURITY.getValue() == req.getPackageType()) {
            packageReq.setPackageType(PackageTypeEnum.SECURITY.getValue());
        } else {
            throw new BusinessException(BossExceptionEnum.PACKAGE_TYPE_IS_NULL);
        }
        // boss添加 tenantId 设置为 -1
        packageReq.setTenantId(TENANT_ID);
        return packageApi.addPackage(packageReq);
    }
    /**
     *@description 分页获取套包数据列表，默认为第一页，每页显示20
     *@author wucheng
     *@params [req]
     *@create 2018/12/5 10:31
     *@return com.iot.common.helper.Page<com.iot.control.packagemanager.vo.resp.PackageResp>
     */
    @Override
    public Page<PackageListResp> getPagePackage(BossSearchPackageReq req) {
        // 定义返回对象
        Page<PackageListResp> result = new Page<>();
        if (req.getPageNum() == null || req.getPageNum() == 0) {
            req.setPageNum(1);
        }
        if (req.getPageSize() == null || req.getPageSize() == 0) {
            req.setPageSize(20);
        }
        PagePackageReq pagePackageReq = new PagePackageReq();
        BeanUtil.copyProperties(req, pagePackageReq);
        Page<PackageResp> packageRespPage = packageApi.getPagePackage(pagePackageReq);
        BeanUtil.copyProperties(packageRespPage, result);
        // 定义返回列表
        List<PackageListResp> resultList = new ArrayList<>();
        if (packageRespPage != null) {
            List<PackageResp> packageResps = packageRespPage.getResult();
            packageResps.forEach(t->{
                PackageListResp packageListResp = new PackageListResp();
                BeanUtil.copyProperties(t, packageListResp);
                resultList.add(packageListResp);
            });
            result.setResult(resultList);
        }
        return result;
    }
    /**
     *@description 修改套包
     *@author wucheng
     *@params [req]
     *@create 2018/12/5 10:31
     *@return int
     */
    @Override
    public int updatePackageById(BossUpdatePackageReq req) {
        PackageReq packageReq = new PackageReq();
        BeanUtil.copyProperties(req, packageReq);
        packageReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        packageReq.setUpdateTime(new Date());
        // 设置套包类型
        if (PackageTypeEnum.AUTOMATION.getValue() == req.getPackageType()) {
            packageReq.setPackageType(PackageTypeEnum.AUTOMATION.getValue());
        } else if (PackageTypeEnum.SECURITY.getValue() == req.getPackageType()) {
            packageReq.setPackageType(PackageTypeEnum.SECURITY.getValue());
        } else {
            throw new BusinessException(BossExceptionEnum.PACKAGE_TYPE_IS_NULL);
        }
        return packageApi.updatePackageById(packageReq);
    }
    /**
     *@description 根据套包删除数据
     *@author wucheng
     *@params [ids]
     *@create 2018/12/5 10:32
     *@return int
     */
    @Override
    public void deleteByIds(String ids){
        String[] idsArray = ids.split(",");
        List<Long> lists = new ArrayList<>();
        for (int i = 0; i < idsArray.length; i++) {
            lists.add(Long.parseLong(idsArray[i]));
        }
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                packageApi.deletePackageRelateData(lists.get(i), TENANT_ID);
            }
        } else {
            throw new BusinessException(BossExceptionEnum.PACKAGE_ID_IS_NULL);
        }
    }
    /**
     *@description 根据套包id，获取该套包的信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/5 10:32
     *@return com.iot.control.packagemanager.vo.resp.PackageResp
     */
    @Override
    public PackageListResp getPackageById(Long id) {
        PackageListResp packageListResp = null;
        PackageResp packageResp = packageApi.getPackageById(id, SaaSContextHolder.currentTenantId());
        if (packageResp != null) {
            packageListResp = new PackageListResp();
            BeanUtil.copyProperties(packageResp, packageListResp);
        }
        return packageListResp;
    }

    /**
     * @despriction：保存套包绑定设备类型
     * @author  yeshiyuan
     * @created 2018/11/21 10:30
     */
    @Override
    public void savePackageDeviceType(SavePackageDeviceTypeReq saveReq) {
        saveReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        List<Long> deviceTypeIds = saveReq.getDeviceTypeIds();
        // 技术类型id
        List<Long> technicalIds = new ArrayList<>();
        if (deviceTypeIds!= null && !deviceTypeIds.isEmpty()) {
            deviceTypeIds.forEach(deviceTypeId -> {
                List<Long> result = technicalRelateApi.queryDeviceTechnicalIds(deviceTypeId);
                if (result != null && result.size() > 0 ) {
                    technicalIds.addAll(result);
                }
                if (!deviceTypeToServiceModuleApi.checkDeviceTypeHadIftttType(deviceTypeId)) {
                    DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(deviceTypeId);
                    throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, deviceTypeResp.getName()+"haven't ifttt ation/event/property");
                }
            });
            List<Long> gateWayTechnicalIds = technicalIds.stream().filter(t -> t == TechnicalSchemeEnum.gateway.getCode()).collect(Collectors.toList());
            if (gateWayTechnicalIds != null && gateWayTechnicalIds.size() > 0) {
                packageDeviceTypeApi.save(saveReq);
            } else {
                throw new BusinessException(BossExceptionEnum.PACKAGE_DEVICETYPE_ERROR, "there must be a device type of technology type gateway");
            }
        } else {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "please select device type");
        }

    }

    /**
     * @despriction：套包绑定设备类型信息
     * @author  yeshiyuan
     * @created 2018/11/21 11:01
     * @return
     */
    @Override
    public List<PackageDeviceTypeInfoResp> getDeviceTypesByPackageId(Long packageId) {
        List<PackageDeviceTypeInfoResp> list = new ArrayList<>();
        List<Long> deviceTypeIds = packageDeviceTypeApi.getDeviceTypesByPackageId(packageId);
        if (deviceTypeIds== null || deviceTypeIds.isEmpty()) {
            return list;
        }
        List<DeviceTypeResp> deviceTypeResps = deviceTypeApi.getByIds(deviceTypeIds);
        deviceTypeResps.forEach(deviceTypeResp -> {
            PackageDeviceTypeInfoResp infoResp = new PackageDeviceTypeInfoResp(deviceTypeResp.getId(),
                    deviceTypeResp.getName(), deviceTypeResp.getDeviceCatalogName(), deviceTypeResp.getDescription(), deviceTypeResp.getIftttType());
            list.add(infoResp);
        });
        return list;
    }

    /**
     * @despriction：根据ifttt属性查找设备的功能模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 21:11
     */
    @Override
    public PackageServiceModuleDetailResp getDeviceModuleDetail(Long deviceTypeId, String iftttType) {
        PackageServiceModuleDetailResp packageServiceModuleDetailResp = deviceTypeToServiceModuleApi.queryServiceModuleDetailByIfttt(deviceTypeId, iftttType);
        // 对propertyResps中的allowedValues进行翻译
        if (packageServiceModuleDetailResp != null) {
            List<PropertyResp> propertyResps = packageServiceModuleDetailResp.getProperties();
            if (propertyResps != null && propertyResps.size() > 0) {
                for (PropertyResp propertyResp : propertyResps) {
                    ServiceModulePropertyResp serviceModulePropertyResp = servicePropertyApi.getPropertyInfoByPropertyId(propertyResp.getId());
                    propertyResp.setAllowedValues(serviceModulePropertyResp.getAllowedValues());
                }
            }
            List<ActionResp> actionResps = packageServiceModuleDetailResp.getActions();
            if (actionResps != null && actionResps.size() > 0) {
                for (ActionResp actionResp : actionResps) {
                    List<PropertyResp> actionPropertyResps = actionResp.getProperties();
                    if (actionPropertyResps != null && actionPropertyResps.size() > 0) {
                        for (PropertyResp propertyResp : actionPropertyResps) {
                            ServiceModulePropertyResp serviceModulePropertyResp = servicePropertyApi.getPropertyInfoByPropertyId(propertyResp.getId());
                            propertyResp.setAllowedValues(serviceModulePropertyResp.getAllowedValues());
                        }
                    }
                }
            }
            List<EventResp> eventResps = packageServiceModuleDetailResp.getEvents();
            if (eventResps != null && eventResps.size() > 0) {
                for (EventResp eventResp : eventResps) {
                    List<PropertyResp> eventPropertyResps = eventResp.getProperties();
                    if (eventPropertyResps != null && eventPropertyResps.size() > 0) {
                        for (PropertyResp propertyResp : eventPropertyResps) {
                            ServiceModulePropertyResp serviceModulePropertyResp = servicePropertyApi.getPropertyInfoByPropertyId(propertyResp.getId());
                            propertyResp.setAllowedValues(serviceModulePropertyResp.getAllowedValues());
                        }
                    }
                }
            }
        }
        return packageServiceModuleDetailResp;
    }



    /**
     * @despriction：根据ifttt类型过滤加载套包下的设备类型
     * @author  yeshiyuan
     * @created 2018/11/23 17:32
     */
    @Override
    public List<PackageDeviceTypeInfoResp> getDeviecTypeByIftttType(Long packageId, String iftttType) {
        List<PackageDeviceTypeInfoResp> list = new ArrayList<>();
        List<Long> deviceTypeIds = packageDeviceTypeApi.getDeviceTypesByPackageId(packageId);
        if (deviceTypeIds== null || deviceTypeIds.isEmpty()) {
            return list;
        }
        List<DeviceTypeResp> deviceTypeResps = deviceTypeApi.getByIdsAndIfffType(deviceTypeIds, iftttType);
        if (deviceTypeResps!= null && !deviceTypeResps.isEmpty()) {
            deviceTypeResps.forEach(deviceTypeResp -> {
                PackageDeviceTypeInfoResp infoResp = new PackageDeviceTypeInfoResp(deviceTypeResp.getId(),deviceTypeResp.getName());
                list.add(infoResp);
            });
        }
        return list;
    }
}
