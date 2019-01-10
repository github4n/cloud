package com.iot.tenant.util;

import com.iot.tenant.domain.Tenant;
import com.iot.tenant.vo.req.SaveTenantReq;
import com.iot.tenant.vo.resp.TenantInfoResp;

/**
 * 描述：bean字段赋值工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:37
 */
public class BeanCopyUtil {

    public static Tenant getTenant(SaveTenantReq source) {
        Tenant target = new Tenant();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setBusiness(source.getBusiness());
        target.setCode(source.getCode());
        target.setCellphone(source.getCellphone());
        target.setEmail(source.getEmail());
        target.setContacts(source.getContacts());
        target.setJob(source.getJob());
        target.setCountry(source.getCountry());
        target.setProvince(source.getProvince());
        target.setCity(source.getCity());
        target.setAddress(source.getAddress());
        target.setType(source.getType());
        target.setAuditStatus(source.getAuditStatus());
        target.setLockStatus(source.getLockStatus());
        return target;
    }

    public static TenantInfoResp getTenantInfoResp(Tenant source) {
        TenantInfoResp target = new TenantInfoResp();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setBusiness(source.getBusiness());
        target.setCode(source.getCode());
        target.setCellphone(source.getCellphone());
        target.setEmail(source.getEmail());
        target.setContacts(source.getContacts());
        target.setJob(source.getJob());
        target.setCountry(source.getCountry());
        target.setProvince(source.getProvince());
        target.setCity(source.getCity());
        target.setAddress(source.getAddress());
        target.setType(source.getType());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setAuditStatus(source.getAuditStatus());
        target.setLockStatus(source.getLockStatus());
        return target;
    }
}
