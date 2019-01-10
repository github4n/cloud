package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ServiceToAction;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 模组-to-方法表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ServiceToActionMapper extends BaseMapper<ServiceToAction> {

}
