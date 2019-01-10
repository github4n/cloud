package com.iot.control.packagemanager.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.PackageApi;
import com.iot.control.packagemanager.entity.Package;
import com.iot.control.packagemanager.execption.PackageExceptionEnum;
import com.iot.control.packagemanager.service.IPackageService;
import com.iot.control.packagemanager.vo.req.PackageReq;
import com.iot.control.packagemanager.vo.req.PagePackageReq;
import com.iot.control.packagemanager.vo.resp.PackageBasicResp;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *@description
 *@author wucheng
 *@create 2018/11/21 17:09
 */
@RestController
public class  PackageController implements PackageApi{

    @Autowired
    private IPackageService packageService;

    @Override
    public Long addPackage(@RequestBody PackageReq req) {
        return  packageService.addPackage(req);
    }

    @Override
    public PackageResp getPackageById(@RequestParam("id") Long id, @RequestParam("tenantId") Long tenantId) {
        return packageService.getPackageById(id, tenantId);
    }

    @Override
    public Page<PackageResp> getPagePackage(@RequestBody PagePackageReq req) {
        Page<PackageResp> page = new Page<>();
        if (req.getPageNum() == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PAGENUM_NULL, "pageNum.is.null");
        }
        if (req.getPageSize() == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PAGESIZE_NULL, "pageSize.is.null");
        }
        EntityWrapper<PackageReq> wrapper = new EntityWrapper<>();

        if (req.getPackageType() != null) {
            wrapper.eq("package_type", req.getPackageType());
        }

        if (req.getTenantId() != null) {
            wrapper.eq("tenant_id", req.getTenantId());
        }

        if (StringUtil.isNotBlank(req.getDescription())) {
            wrapper.like("description", req.getDescription());
        }

        if (StringUtil.isNotBlank(req.getName())) {
            wrapper.like("name", req.getName());
        }

        wrapper.orderBy(true, "create_time", false);
        com.baomidou.mybatisplus.plugins.Page selectPage = new com.baomidou.mybatisplus.plugins.Page(req.getPageNum(), req.getPageSize());

        List<PackageResp> packageList = packageService.getPagePackage(selectPage, wrapper);
        page.setResult(packageList);
        page.setTotal(selectPage.getTotal());
        page.setPageNum(req.getPageNum());
        page.setPageSize(req.getPageSize());
        page.setPages(selectPage.getPages());
        return page;
    }

    @Override
    public int updatePackageById(@RequestBody PackageReq req) {
        return packageService.updatePackageById(req);
    }

    @Override
    public int deleteByIds(@RequestParam("ids") List<Long> ids) {
        return packageService.deleteByIds(ids);
    }

    /**
     * @despriction：删除套包相关数据（套包基本信息、场景、策略等等）
     * @author  yeshiyuan
     * @created 2018/12/5 19:58
     */
    @Override
    public void deletePackageRelateData(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId) {
        packageService.deletePackageRelateData(packageId, tenantId);
    }

    /**
     *@description 根据套包租户id，获取该租户下所有套包信息
     *@author wucheng
     *@params [tenantId]
     *@create 2018/12/13 16:01
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageBasicResp>
     */
    @Override
    public List<PackageBasicResp> getPackageInfo(@RequestParam("tenantId") Long tenantId) {
        return packageService.getPackageInfo(tenantId);
    }
}

