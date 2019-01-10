package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.exception.BusinessException;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.mapper.ProductToStyleMapper;
import com.iot.device.model.ProductToStyle;
import com.iot.device.service.IProductToStyleService;
import com.iot.device.vo.ProductToStyleVo;
import com.iot.device.vo.req.ProductToStyleReq;
import com.iot.device.vo.rsp.ProductToStyleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductToStyleServiceImpl extends ServiceImpl<ProductToStyleMapper,ProductToStyle> implements IProductToStyleService {

    @Autowired
    private ProductToStyleMapper productToStyleMapper;

    @Override
    public Long saveOrUpdate(ProductToStyleReq productToStyleReq) {
        ProductToStyle productToStyle = null;
        if (productToStyleReq.getId() != null && productToStyleReq.getId() > 0) {
            productToStyle = super.selectById(productToStyleReq.getId());
            if (productToStyle == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            productToStyle.setUpdateTime(new Date());
            productToStyle.setUpdateBy(productToStyleReq.getUpdateBy());
        } else {
            productToStyle = new ProductToStyle();
            productToStyle.setUpdateTime(new Date());
            productToStyle.setUpdateBy(productToStyleReq.getUpdateBy());
            productToStyle.setCreateTime(new Date());
            productToStyle.setCreateBy(productToStyleReq.getCreateBy());
            productToStyle.setTenantId(productToStyleReq.getTenantId());
        }
        productToStyle.setProductId(productToStyleReq.getProductId());
        productToStyle.setStyleTemplateId(productToStyleReq.getStyleTemplateId());
        productToStyle.setDescription(productToStyleReq.getDescription());
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("product_id",productToStyle.getProductId());
        wrapper.eq("style_template_id",productToStyle.getStyleTemplateId());
        List list = super.selectList(wrapper);
        if (list.size()>0){
            return new Long(0);
        } else {
            super.insertOrUpdate(productToStyle);
            return productToStyle.getId();
        }
    }

    @Override
    public void delete(Long id) {
        super.deleteById(id);
    }

    @Override
    public List<ProductToStyleResp> list(Long productId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("product_id",productId);
        List<ProductToStyleVo> list = productToStyleMapper.listByProductId(productId);
        List<ProductToStyleResp> rspList = new ArrayList<>();
        list.forEach(m->{
            ProductToStyleResp productToStyleResp = new ProductToStyleResp();
            productToStyleResp.setId(m.getId());
            productToStyleResp.setProductId(m.getProductId());
            productToStyleResp.setStyleTemplateId(m.getStyleTemplateId());
            productToStyleResp.setCreateBy(m.getCreateBy());
            productToStyleResp.setCreateTime(m.getCreateTime());
            productToStyleResp.setUpdateBy(m.getUpdateBy());
            productToStyleResp.setUpdateTime(m.getUpdateTime());
            productToStyleResp.setDescription(m.getDescription());
            productToStyleResp.setName(m.getName());
            productToStyleResp.setCode(m.getCode());
            productToStyleResp.setImg(m.getImg());
            productToStyleResp.setTenantId(m.getTenantId());
            productToStyleResp.setResourceLink(m.getResourceLink());
            rspList.add(productToStyleResp);
        });
        return rspList;
    }
}
