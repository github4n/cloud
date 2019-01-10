package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.tenant.domain.UserToVirtualOrg;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户-组织表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Mapper
public interface UserToVirtualOrgMapper extends BaseMapper<UserToVirtualOrg> {

}
