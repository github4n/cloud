package com.iot.tenant.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.tenant.domain.VirtualOrg;
import com.iot.tenant.mapper.VirtualOrgMapper;
import com.iot.tenant.service.IVirtualOrgService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户-组织表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Service
public class VirtualOrgServiceImpl extends ServiceImpl<VirtualOrgMapper, VirtualOrg> implements IVirtualOrgService {

}
