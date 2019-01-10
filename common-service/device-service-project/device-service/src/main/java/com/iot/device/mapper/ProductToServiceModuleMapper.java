package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ProductToServiceModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 产品对应模组表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ProductToServiceModuleMapper extends BaseMapper<ProductToServiceModule> {

    @Select("select service_module_id from product_to_service_module where product_id = #{productId}")
    List<Long> findServiceModuleIdByProductId(@Param("productId") Long productId);

}
