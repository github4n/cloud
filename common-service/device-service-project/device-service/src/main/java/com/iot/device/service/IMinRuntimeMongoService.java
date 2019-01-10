package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.MinRuntime;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.RuntimeRsp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
public interface IMinRuntimeMongoService{

	List<RuntimeRsp> selectByCondition();
	
	RuntimeRsp selectRuntimeRspByDeviceAndUser(RuntimeReq2Runtime runtimeReq);

	Boolean haveSameDataWithHourses(MinRuntime minRuntime);

}
