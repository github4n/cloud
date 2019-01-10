package com.iot.device.mapper;

import com.iot.device.model.Configuration;
import com.iot.device.vo.rsp.ConfigurationRsp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CHQ
 * @since 2018-05-10
 */
@Mapper
public interface ConfigurationMapper extends BaseMapper<Configuration> {

	@Select({
    	"SELECT * " + 
    	" FROM configuration " + 
    	" WHERE tenant_id = #{tenantId} "
    })
	List<ConfigurationRsp> selectConfigByTenantId(Long tenantId);
		
}
