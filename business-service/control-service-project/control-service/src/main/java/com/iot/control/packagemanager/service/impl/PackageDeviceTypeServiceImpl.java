package com.iot.control.packagemanager.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.control.packagemanager.entity.PackageDeviceType;
import com.iot.control.packagemanager.mapper.PackageDeviceTypeMapper;
import com.iot.control.packagemanager.service.IPackageDeviceTypeService;
import com.iot.control.packagemanager.vo.req.SavePackageDeviceTypeReq;
import com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
  * @despriction：套包設備類型serviceImpl
  * @author  yeshiyuan
  * @created 2018/11/20 21:29
  */
@Service
public class PackageDeviceTypeServiceImpl extends ServiceImpl<PackageDeviceTypeMapper, PackageDeviceType> implements IPackageDeviceTypeService{

    @Autowired
    private PackageDeviceTypeMapper packageDeviceTypeMapper;

    /**
     * @despriction：保存套包設備類型（先刪後插）
     * @author  yeshiyuan
     * @created 2018/11/20 21:39
     */
    @Override
    @Transactional
    public void save(SavePackageDeviceTypeReq saveReq) {
        packageDeviceTypeMapper.deleteByPackageId(saveReq.getPackageId());
        List<PackageDeviceType> list = new ArrayList<>();
        if (saveReq.getDeviceTypeIds() != null && !saveReq.getDeviceTypeIds().isEmpty()) {
            saveReq.getDeviceTypeIds().forEach(deviceTypeId -> {
                list.add(new PackageDeviceType(saveReq.getPackageId(),
                        deviceTypeId, saveReq.getCreateBy(), new Date()));
            });
        }
        if (!list.isEmpty()) {
            packageDeviceTypeMapper.batchInsert(list);
        }
    }

    /**
     * @despriction：通过套包id找到设备类型
     * @author  yeshiyuan
     * @created 2018/11/21 10:40
     */
    @Override
    public List<Long> getDeviceTypesByPackageId(Long packageId) {
        return packageDeviceTypeMapper.getDeviceTypesByPackageId(packageId);
    }
    
  /**
   *@description 根据套包id，批量删除关联设备类型
   *@author wucheng
   *@params [packageIds]
   *@create 2018/12/6 14:10
   *@return int
   */
    @Override
    public int batchDeleteByPackageId(List<Long> packageIds) {
        return packageDeviceTypeMapper.batchDeleteByPackageId(packageIds);
    }

    /**
     *@description 获取套包设备详细信息
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 10:32
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp>
     */
    @Override
    public List<PackageDeviceTypeInfoResp> getPackageDeviceTypeInfo(Long packageId, Long tenantId) {
        return packageDeviceTypeMapper.getPackageDeviceTypeInfo(packageId, tenantId);
    }
}
