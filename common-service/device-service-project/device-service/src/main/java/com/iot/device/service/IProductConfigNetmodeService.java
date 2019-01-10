package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ProductConfigNetmode;
import com.iot.device.vo.req.*;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IProductConfigNetmodeService extends IService<ProductConfigNetmode> {

    Long insert(ProductConfigNetmodeReq productConfigNetmodeReq);

    void insertMore(List<ProductConfigNetmodeReq> productConfigNetmodeReq);

    List<ProductConfigNetmodeRsp> listByProductId(Long productId);

    void deleteMore(List ids);

}
