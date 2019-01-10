package com.iot.boss.service.malf.impl;

import com.iot.boss.dao.malf.SystemUserMapper;
import com.iot.boss.entity.SystemUser;
import com.iot.boss.exception.BusinessExceptionEnum;
import com.iot.boss.service.malf.SystemUserService;
import com.iot.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 19:40
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 19:40
 * 修改描述：
 */
@Service
public class SystemUserServiceImpl implements SystemUserService{

    @Autowired
    private SystemUserMapper systemUserMapper;


    @Override
    public SystemUser checkAdminAuth(Long id, Integer compareType) {
        SystemUser user = systemUserMapper.getAdminById(id);
        if (user==null){
            throw new BusinessException(BusinessExceptionEnum.ADMIN_NOT_EXISTS);
        }else if (compareType.intValue()!=user.getAdminType().intValue()){
            throw new BusinessException(BusinessExceptionEnum.ADMIN_AUTH_NOT_ENOUGH);
        }
        return user;
    }

    @Override
    public List<Long> getAdminIdByType(Integer adminType) {
        return systemUserMapper.getAdminIdByType(adminType);
    }

    @Override
    public int checkUserExist(Long id) {
        return systemUserMapper.checkUserExist(id);
    }
}
