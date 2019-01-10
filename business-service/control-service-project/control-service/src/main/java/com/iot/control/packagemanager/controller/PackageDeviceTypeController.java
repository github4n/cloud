package com.iot.control.packagemanager.controller;

import com.iot.control.packagemanager.api.PackageDeviceTypeApi;
import com.iot.control.packagemanager.service.IPackageDeviceTypeService;
import com.iot.control.packagemanager.vo.req.SavePackageDeviceTypeReq;
import com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
  * @despriction：套包支持設備類型(BOSS維護)
  * @author  yeshiyuan
  * @created 2018/11/20 21:11
  */
@RestController
public class PackageDeviceTypeController implements PackageDeviceTypeApi {

    @Autowired
    private IPackageDeviceTypeService packageDeviceTypeService;

    /**
     * @despriction：保存
     * @author  yeshiyuan
     * @created 2018/11/20 21:00
     */
    @Override
    public void save(@RequestBody SavePackageDeviceTypeReq req) {
        packageDeviceTypeService.save(req);
    }

    /**
     * @despriction：通过套包id找到设备类型
     * @author  yeshiyuan
     * @created 2018/11/21 10:40
     */
    @Override
    public List<Long> getDeviceTypesByPackageId(Long packageId) {
        return packageDeviceTypeService.getDeviceTypesByPackageId(packageId);
    }

    /**
     *@description 根据套包id，批量删除数据
     *@author wucheng
     *@params [packageIds]
     *@create 2018/12/6 14:12
     *@return int
     */
    @Override
    public int batchDeleteByPackageId(@RequestParam("packageIds") List<Long> packageIds) {
        return packageDeviceTypeService.batchDeleteByPackageId(packageIds);
    }
    
    /**
     *@description
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 10:36
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp>
     */
    @Override
    public List<PackageDeviceTypeInfoResp> getPackageDeviceTypeInfo(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId) {
        return packageDeviceTypeService.getPackageDeviceTypeInfo(packageId, tenantId);
    }
}
