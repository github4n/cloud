package com.iot.boss.service.packagemanager;


import com.iot.boss.vo.packagemanager.req.BossSavePackageReq;
import com.iot.boss.vo.packagemanager.req.BossSearchPackageReq;
import com.iot.boss.vo.packagemanager.req.BossUpdatePackageReq;
import com.iot.boss.vo.packagemanager.resp.PackageDeviceTypeInfoResp;
import com.iot.boss.vo.packagemanager.resp.PackageListResp;
import com.iot.common.helper.Page;
import com.iot.control.packagemanager.vo.req.PagePackageReq;
import com.iot.control.packagemanager.vo.req.SavePackageDeviceTypeReq;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;

import java.util.List;

/**
  * @despriction：套包基本信息管理
  * @author  yeshiyuan
  * @created 2018/11/21 8:59
  */
public interface IPackageInfoService {
    /**
     *@description 添加套包信息，返回该条数据的id
     *@author wucheng
     *@params [req]
     *@create 2018/11/21 16:29
     *@return java.lang.Long
     */
    Long addPackage(BossSavePackageReq req);
    /**
     *@description 按条件分页获取套包信息
     *@author wucheng
     *@params [req]
     *@create 2018/11/21 16:26
     *@return com.iot.common.helper.Page<com.iot.shcs.template.vo.resp.PackageResp>
     */
    Page<PackageListResp> getPagePackage(BossSearchPackageReq req);
    /**
     *@description 根据id更新数据
     *@author wucheng
     *@params [req]
     *@create 2018/11/21 16:33
     *@return int
     */
    int updatePackageById(BossUpdatePackageReq req);
    /**
     *@description 根据id删除数据
     *@author wucheng
     *@params [ids]
     *@create 2018/11/21 16:33
     *@return int
     */
    void deleteByIds(String ids);
    /**
     *@description 根据id获取套包数据
     *@author wucheng
     *@params [id]
     *@create 2018/11/21 19:59
     *@return com.iot.control.packagemanager.vo.resp.PackageResp
     */
    PackageListResp getPackageById(Long id);

    /**
      * @despriction：保存套包绑定设备类型
      * @author  yeshiyuan
      * @created 2018/11/21 10:30
      */
    void savePackageDeviceType(SavePackageDeviceTypeReq saveReq);

    /**
      * @despriction：套包绑定设备类型信息
      * @author  yeshiyuan
      * @created 2018/11/21 11:01
      * @return
      */
    List<PackageDeviceTypeInfoResp> getDeviceTypesByPackageId(Long packageId);

    /**
      * @despriction：根据ifttt属性查找设备的功能模组信息
      * @author  yeshiyuan
      * @created 2018/11/22 21:11
      */
    PackageServiceModuleDetailResp getDeviceModuleDetail(Long deviceTypeId, String iftttType);



    /**
      * @despriction：根据ifttt类型过滤加载套包下的设备类型
      * @author  yeshiyuan
      * @created 2018/11/23 17:32
      */
    List<PackageDeviceTypeInfoResp> getDeviecTypeByIftttType(Long packageId, String iftttType);
}
