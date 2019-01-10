package com.iot.tenant.mapper;

import com.iot.tenant.domain.AppProduct;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * app关联产品 Mapper 接口
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
@Mapper
public interface AppProductMapper extends BaseMapper<AppProduct> {

}
