package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.tenant.domain.VirtualOrg;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 租户-组织表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Mapper
public interface VirtualOrgMapper extends BaseMapper<VirtualOrg> {

}
