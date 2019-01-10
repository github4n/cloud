package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ProductToStyle;
import com.iot.device.vo.req.ProductToStyleReq;
import com.iot.device.vo.rsp.ProductToStyleResp;

import java.util.List;

public interface IProductToStyleService extends IService<ProductToStyle> {

    /**
     * 增加或修改
     * @param productToStyleReq
     * @return
     */
    Long saveOrUpdate(ProductToStyleReq productToStyleReq);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 获取
     * @param productId
     * @return
     */
    List<ProductToStyleResp> list(Long productId);

}
