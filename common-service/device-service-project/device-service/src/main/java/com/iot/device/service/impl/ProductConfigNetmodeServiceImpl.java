package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.beans.BeanUtil;
import com.iot.device.mapper.ProductConfigNetmodeMapper;
import com.iot.device.model.ProductConfigNetmode;
import com.iot.device.service.IProductConfigNetmodeService;
import com.iot.device.vo.req.ProductConfigNetmodeReq;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class ProductConfigNetmodeServiceImpl extends ServiceImpl<ProductConfigNetmodeMapper, ProductConfigNetmode> implements IProductConfigNetmodeService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ProductConfigNetmodeServiceImpl.class);

    @Autowired
    private ProductConfigNetmodeMapper productConfigNetmodeMapper;


    @Override
    public Long insert(ProductConfigNetmodeReq productConfigNetmodeReq) {
        ProductConfigNetmode productConfigNetmode = new ProductConfigNetmode();
        productConfigNetmode.setProductId(productConfigNetmodeReq.getProductId());
        productConfigNetmode.setName(productConfigNetmodeReq.getName());
        productConfigNetmode.setCreateBy(productConfigNetmodeReq.getCreateBy());
        productConfigNetmode.setCreateTime(new Date());
        productConfigNetmode.setUpdateBy(productConfigNetmodeReq.getUpdateBy());
        productConfigNetmode.setUpdateTime(new Date());
        super.insert(productConfigNetmode);
        return productConfigNetmode.getId();
    }

    @Override
    public void insertMore(List<ProductConfigNetmodeReq> productConfigNetmodeReq) {
        List<ProductConfigNetmode> result =  new ArrayList<>();
        for (ProductConfigNetmodeReq productConfigNetmode : productConfigNetmodeReq) {
            ProductConfigNetmode configNetmode = new ProductConfigNetmode();
            BeanUtil.copyProperties(productConfigNetmode, configNetmode);
            result.add(configNetmode);
        }
        if (!CollectionUtils.isEmpty(result)) {
            super.insertBatch(result);
        }
    }

    @Override
    public List<ProductConfigNetmodeRsp> listByProductId(Long productId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("product_id",productId);
        List<ProductConfigNetmode> list = super.selectList(wrapper);
        List<ProductConfigNetmodeRsp> result = new ArrayList();
        list.forEach(m->{
            ProductConfigNetmodeRsp productConfigNetmodeRsp = new ProductConfigNetmodeRsp();
            productConfigNetmodeRsp.setId(m.getId());
            productConfigNetmodeRsp.setProductId(m.getProductId());
            productConfigNetmodeRsp.setName(m.getName());
            productConfigNetmodeRsp.setCreateBy(m.getCreateBy());
            productConfigNetmodeRsp.setCreateTime(m.getCreateTime());
            productConfigNetmodeRsp.setUpdateBy(m.getUpdateBy());
            productConfigNetmodeRsp.setUpdateTime(m.getUpdateTime());
            result.add(productConfigNetmodeRsp);
        });
        return result;
    }


    @Override
    public void deleteMore(List ids) {
        if (!CollectionUtils.isEmpty(ids)){
            super.deleteBatchIds(ids);
        }
    }
}
