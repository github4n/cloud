package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ProductToStyle;
import com.iot.device.vo.ProductToStyleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductToStyleMapper extends BaseMapper<ProductToStyle> {

    @Select({
            "select a.id, a.style_template_id, a.product_id, b.`name` as name,b.`code` as code,b.img as img, b.resource_link as resourceLink, b.description as description from product_to_style a ",
            "left JOIN style_template b on a.style_template_id=b.id where a.product_id=#{productId}"
    })
    List<ProductToStyleVo> listByProductId(@Param("productId") Long productId);

}
