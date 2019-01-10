package com.iot.control.packagemanager.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.control.packagemanager.entity.Package;
import com.iot.control.packagemanager.vo.req.PackageReq;
import com.iot.control.packagemanager.vo.resp.PackageBasicResp;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *@description 套包服务类
 *@author wucheng
 *@create 2018/11/21 16:59
 */
public interface IPackageService{
    /**
     *@description 根据id和租户id查询套包信息
     *@author wucheng
     *@params [id, tenantId]
     *@create 2018/12/13 14:34
     *@return com.iot.control.packagemanager.vo.resp.PackageResp
     */
    PackageResp getPackageById(Long id, Long tenantId);
    /**
     *@description：新增套包
     *@author wucheng
     *@params [req]
     *@create 2018/11/21 11:13
     *@return int
     */
    Long addPackage(PackageReq req);
    /**
     *@description 分页获取package信息
     *@author wucheng
     *@params [page, ew]
     *@create 2018/11/21 14:44
     *@return java.util.List<com.iot.shcs.template.vo.resp.PackageResp>
     */
    List<PackageResp> getPagePackage(Page<PackageReq> page, EntityWrapper ew);

    /**
     *@description 根据id修改信息
     *@author wucheng
     *@params [req]
     *@create 2018/11/21 15:20
     *@return int
     */
     int updatePackageById(PackageReq req);

     /**
      *@description 根据id删除数据
      *@author wucheng
      *@params [ids]
      *@create 2018/11/21 15:34
      *@return int
      */
     int deleteByIds(List<Long> ids);

    /**
     * @despriction：删除套包相关数据（套包基本信息、场景、策略等等）
     * @author  yeshiyuan
     * @created 2018/12/5 19:58
     */
    void deletePackageRelateData(Long packageId, Long tenantId);
    
    /**
     *@description
     *@author wucheng
     *@params [tenantId]
     *@create 2018/12/13 15:55
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageBasicResp>
     */
    List<PackageBasicResp> getPackageInfo(Long tenantId); 
}
