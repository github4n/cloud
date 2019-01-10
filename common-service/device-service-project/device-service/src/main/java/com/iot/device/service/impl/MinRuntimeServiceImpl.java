package com.iot.device.service.impl;

import com.iot.device.model.MinRuntime;
import com.iot.device.mapper.MinRuntimeMapper;
import com.iot.device.service.IMinRuntimeService;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.RuntimeRsp;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Service
public class MinRuntimeServiceImpl extends ServiceImpl<MinRuntimeMapper, MinRuntime> implements IMinRuntimeService {

	@Autowired
    private MinRuntimeMapper minRuntimeMapper;
	
	@Override
	public List<RuntimeRsp> selectByCondition() {
		
		return minRuntimeMapper.selectRuntimeRspByCondition();
	}

	@Override
	public RuntimeRsp selectRuntimeRspByDeviceAndUser(RuntimeReq2Runtime runtimeReq) {
		
		return minRuntimeMapper.selectRuntimeRspByDeviceAndUser(runtimeReq);
	}
	
}
