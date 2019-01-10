package com.iot.building.device.mapper;

import com.iot.building.device.vo.DeviceRespVo;
import com.iot.device.vo.rsp.ProductResp;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DeviceMapper{

	@SelectProvider(type = DeviceSqlProvider.class, method = "selectDevListByDeviceIds")
	List<DeviceRespVo> selectDevListByDeviceIds(List<String> deviceIdList, @Param("isCheckUserNotNull") boolean isCheckUserNotNull, @Param("userId") Long userId);

	@SelectProvider(type = DeviceSqlProvider.class, method = "listProducts")
    List<ProductResp> listProducts(@Param("productIds") List<Long> productIds);
}
