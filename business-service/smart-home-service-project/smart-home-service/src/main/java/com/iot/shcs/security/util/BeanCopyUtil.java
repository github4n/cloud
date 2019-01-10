package com.iot.shcs.security.util;

import com.iot.shcs.security.domain.Security;
import com.iot.shcs.security.vo.SecurityRule;
import com.iot.shcs.security.vo.rsp.SecurityResp;

public class BeanCopyUtil {

    public static SecurityRule getSecurityRule(SecurityRule source) {
        SecurityRule target = new SecurityRule();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setSecurityId(source.getSecurityId());
        target.setTenantId(source.getTenantId());
        target.setType(source.getType());
        target.setEnabled(source.getEnabled());
        target.setDefer(source.getDefer());
        target.setDelay(source.getDelay());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setIfCondition(source.getIfCondition());
        target.setThenCondition(source.getThenCondition());
        return target;
    }

    public static void copySecurityResp(SecurityResp target, Security source){
        if(target==null && source==null){
            return ;
        }
        target.setId(source.getId());
        target.setArmMode(source.getArmMode());
        target.setCreateBy(source.getCreateBy());
        target.setOrgId(source.getOrgId());
        target.setPassword(source.getPassword());
        target.setCreateTime(source.getCreateTime());
        target.setSpaceId(source.getSpaceId());
        target.setTenantId(source.getTenantId());
        target.setUpdateTime(source.getUpdateTime());
        target.setUpdateBy(source.getUpdateBy());
    }

    public static void copySecurityRule(SecurityRule target,SecurityRule source){
        if(target==null&&source==null){
            return;
        }
        target.setId(source.getId());
        target.setSecurityId(source.getSecurityId());
        target.setTenantId(source.getTenantId());
        target.setDefer(source.getDefer());
        target.setDelay(source.getDelay());
        target.setType(source.getType());
        target.setThenCondition(source.getThenCondition());
        target.setIfCondition(source.getIfCondition());
        target.setUpdateTime(source.getUpdateTime());
        target.setCreateTime(source.getCreateTime());
        target.setEnabled(source.getEnabled());
    }

}
