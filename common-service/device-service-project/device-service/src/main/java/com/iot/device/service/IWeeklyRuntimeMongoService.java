package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.WeeklyRuntime;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.EnergyRsp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
public interface IWeeklyRuntimeMongoService {

	public Boolean insertOrUpdateBatch(List<WeeklyRuntime> weeklylist);

	List<WeeklyRuntime> selectByCondition(WeeklyRuntime weeklyRuntime);

	Page<EnergyRsp> selectWeeklyRuntimeRsp(RuntimeReq2Runtime runtimeReq);

}
