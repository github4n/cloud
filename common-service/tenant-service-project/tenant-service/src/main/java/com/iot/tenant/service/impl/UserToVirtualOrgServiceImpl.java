package com.iot.tenant.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.tenant.domain.UserToVirtualOrg;
import com.iot.tenant.mapper.UserToVirtualOrgMapper;
import com.iot.tenant.service.IUserToVirtualOrgService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-组织表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Service
public class UserToVirtualOrgServiceImpl extends ServiceImpl<UserToVirtualOrgMapper, UserToVirtualOrg> implements IUserToVirtualOrgService {

}
