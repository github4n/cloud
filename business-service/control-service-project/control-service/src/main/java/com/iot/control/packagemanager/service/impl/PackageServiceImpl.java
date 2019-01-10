package com.iot.control.packagemanager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.common.exception.BusinessException;
import com.iot.control.packagemanager.execption.PackageExceptionEnum;
import com.iot.control.packagemanager.mapper.*;
import com.iot.control.packagemanager.service.IPackageService;
import com.iot.control.packagemanager.vo.req.PackageReq;
import com.iot.control.packagemanager.vo.resp.PackageBasicResp;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 *@description 套包服务类
 *@author wucheng
 *@create 2018/11/21 17:17
 */
@Slf4j
@Service
public class PackageServiceImpl  implements IPackageService {
    @Autowired
    private PackageMapper packageMapper;
    @Autowired
    private PackageDeviceTypeMapper packageDeviceTypeMapper;
    @Autowired
    private PackageProductMapper packageProductMapper;
    @Autowired
    private SceneInfoMapper sceneInfoMapper;
    @Autowired
    private TemplateRuleMapper templateRuleMapper;

    @Override
    public PackageResp getPackageById(Long id, Long tenantId) {
        return packageMapper.selectById(id, tenantId);
    }

    @Override
    @Transactional
    public Long addPackage(PackageReq req) {
        Long result = packageMapper.addPackage(req);
        if (result > 0) {
            return req.getId();
        }
        return result;
    }

    @Override
    @Transactional
    public int updatePackageById(PackageReq req) {
        if (req.getId() == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_UPDATE_ID_NULL, "id is null");
        }
        return packageMapper.updatePackageById(req);
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_DELETE_IDS_NULL, "ids is null");
        }
        return packageMapper.deleteByIds(ids);
    }

    @Override
    public List<PackageResp> getPagePackage(Page<PackageReq> page, EntityWrapper ew) {
        return packageMapper.getPagePackage(page, ew);
    }

    /**
     * @despriction：删除套包相关数据（套包基本信息、场景、策略等等）
     * @author  yeshiyuan
     * @created 2018/12/5 19:58
     */
    @Override
    public void deletePackageRelateData(Long packageId, Long tenantId) {
        packageMapper.deleteByIds(Arrays.asList(packageId));
        //根据租户id，删除套包绑定设备类型表还是套包绑定产品表
        if (tenantId.equals(-1L)) {
            packageDeviceTypeMapper.deleteByPackageId(packageId);
        } else {
            packageProductMapper.deleteByPackageIdAndTenantId(packageId, tenantId);
        }
        sceneInfoMapper.deleteByPackageIdAndTenantId(packageId, tenantId);
        templateRuleMapper.deleteByPackageIdAndTenantId(packageId, tenantId);
    }
    /**
     *@description 根据租户id，获取该租户的套包基本信息
     *@author wucheng
     *@params [tenantId]
     *@create 2018/12/13 16:00
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.PackageBasicResp>
     */
    @Override
    public List<PackageBasicResp> getPackageInfo(Long tenantId) {
        if (tenantId == null) {
            throw new BusinessException(PackageExceptionEnum.PARAM_ERROR, "tenantId is null");
        }
        return packageMapper.getPackageInfo(tenantId);
    }
}
