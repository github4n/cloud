package com.iot.tenant.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.tenant.domain.TenantReviewRecord;

/**
 * <p>
 * 租户表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Mapper
public interface TenantReviewRecordMapper extends BaseMapper<TenantReviewRecord> {

}
