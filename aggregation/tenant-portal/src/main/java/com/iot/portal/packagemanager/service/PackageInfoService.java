package com.iot.portal.packagemanager.service;

import com.iot.control.packagemanager.vo.resp.PackageBasicResp;
import com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import com.iot.device.vo.rsp.product.GatewayChildProductResp;
import com.iot.device.vo.rsp.product.PackageProductResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.portal.packagemanager.vo.req.SavePackageBaseInfoReq;
import com.iot.portal.packagemanager.vo.resp.PackageDeviceTypeDetailResp;
import com.iot.portal.packagemanager.vo.resp.PackageProductInfoResp;
import com.iot.portal.packagemanager.vo.resp.PackageTypeResp;

import java.util.List;

/**
  * @despriction：套包信息（基本信息、产品）service
  * @author  yeshiyuan
  * @created 2018/11/24 9:51
  */
public interface PackageInfoService {

    /**
      * @despriction：加载套包类型
      * @author  yeshiyuan
      * @created 2018/12/7 10:51
      */
    List<PackageTypeResp> loadPackageType();



    /**
      * @despriction：获取产品功能模组详情
      * @author  yeshiyuan
      * @created 2018/12/10 11:55
      */
    PackageServiceModuleDetailResp getProductModuleDetail(Long productId, String iftttType);

    /**
     * @despriction：删除套包
     * @author  yeshiyuan
     * @created 2018/12/10 19:26
     */
    void deletePackage(Long packageId);
    
    /**
     * 
     * 描述：查询套包产品
     * @author 李帅
     * @created 2018年12月10日 下午8:55:22
     * @since 
     * @return
     */
    List<PackageProductResp> getPackageProducts();
    
    /**
     * 
     * 描述：查询网管子产品
     * @author 李帅
     * @created 2018年12月10日 下午8:55:53
     * @since 
     * @param productId
     * @return
     */
    List<GatewayChildProductResp> getGatewayChildProducts(Long productId);

    /**
     *@description 获取套包详情
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/13 14:18
     *@return com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp
     */
    PackageDeviceTypeDetailResp getBossPackageInfo(Long packageId);

    /**
     *@description 获取boss创建的所有套包信息
     *@author wucheng
     *@params []
     *@create 2018/12/13 15:33
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageResp>
     */
    List<PackageBasicResp> getBossPackageList();

    /**
     * @despriction：保存套包基本信息
     * @author  yeshiyuan
     * @created 2018/12/17 9:48
     * @params []
     * @return com.iot.common.beans.CommonResponse
     */
    void savePackageBaseInfo(SavePackageBaseInfoReq savePackageBaseInfoReq);

    /**
     * @despriction：根据ifttt类型过滤加载套包下的产品
     * @author  yeshiyuan
     * @created 2018/12/17 10:27
     * @params [packageId, iftttType]
     * @return com.iot.common.beans.CommonResponse
     */
    List<PackageProductInfoResp> getProductByIftttType(Long packageId, String iftttType);
}
