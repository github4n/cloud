package com.iot.smarthome.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.smarthome.model.DeviceClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 8:59
 * @Modify by:
 */

@Mapper
public interface DeviceClassifyMapper extends BaseMapper<DeviceClassify> {

    @Select({
            "select dc.* from device_classify dc",
            "join device_classify_product_xref dcpXref on dc.id=dcpXref.device_classify_id",
            "where dcpXref.product_id=#{productId}"
    })
    public DeviceClassify getDeviceClassifyByProductId(@Param("productId") Long productId);


}
