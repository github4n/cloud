package com.iot.control.packagemanager.service;

import com.iot.control.packagemanager.vo.req.SavePackageDeviceTypeReq;
import com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
  * @despriction：套包設備類型service
  * @author  yeshiyuan
  * @created 2018/11/20 21:29
  */
public interface IPackageDeviceTypeService {

    /**
      * @despriction：保存套包設備類型（先刪後插）
      * @author  yeshiyuan
      * @created 2018/11/20 21:39
      */
    void save(SavePackageDeviceTypeReq saveReq);

    /**
     * @despriction：通过套包id找到设备类型
     * @author  yeshiyuan
     * @created 2018/11/21 10:40
     */
    List<Long> getDeviceTypesByPackageId(Long packageId);

    /**
     *@description 根据套包id，批量删除
     *@author wucheng
     *@params [packageIds]
     *@create 2018/12/6 14:09
     *@return int
     */
    int batchDeleteByPackageId(List<Long> packageIds);

    /**
     *@description 获取套包设备详细信息
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 10:31
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp>
     */
    List<PackageDeviceTypeInfoResp> getPackageDeviceTypeInfo(Long packageId,Long tenantId);
}
