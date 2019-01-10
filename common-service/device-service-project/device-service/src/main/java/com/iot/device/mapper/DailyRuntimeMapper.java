package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.RuntimeSqlProvider;
import com.iot.device.model.DailyRuntime;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.EnergyRsp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Mapper
public interface DailyRuntimeMapper extends BaseMapper<DailyRuntime> {

    @SelectProvider(type = RuntimeSqlProvider.class, method = "selectDailyRuntimeRsp")
    List<EnergyRsp> selectDailyRuntimeRsp(RuntimeReq2Runtime runtimeReq);
}
