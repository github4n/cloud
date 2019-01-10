package com.iot.device.mapper;

import com.iot.device.mapper.sql.RuntimeSqlProvider;
import com.iot.device.model.MinRuntime;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.RuntimeRsp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Mapper
public interface MinRuntimeMapper extends BaseMapper<MinRuntime> {

	@SelectProvider(type = RuntimeSqlProvider.class,method = "selectRuntimeRspByCondition")

	List<RuntimeRsp> selectRuntimeRspByCondition();

	@SelectProvider(type = RuntimeSqlProvider.class,method = "selectRuntimeRspByDeviceAndUser")
	RuntimeRsp selectRuntimeRspByDeviceAndUser(RuntimeReq2Runtime runtimeReq);

}
