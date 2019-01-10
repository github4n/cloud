package com.iot.tenant.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.tenant.domain.TenantReviewRecord;
import com.iot.tenant.mapper.TenantReviewRecordMapper;
import com.iot.tenant.service.ITenantReviewRecordService;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Service
public class TenantReviewRecordServiceImpl extends ServiceImpl<TenantReviewRecordMapper, TenantReviewRecord> implements ITenantReviewRecordService {
	
}
