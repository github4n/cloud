package com.iot.device.controller;

import com.iot.device.api.ProductToStyleApi;
import com.iot.device.service.IProductToStyleService;
import com.iot.device.vo.req.ProductToStyleReq;
import com.iot.device.vo.rsp.ProductToStyleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductToStyleController implements ProductToStyleApi {

    @Autowired
    private IProductToStyleService iProductToStyleService;

    @Override
    public Long saveOrUpdate(@RequestBody ProductToStyleReq productToStyleReq) {
        return iProductToStyleService.saveOrUpdate(productToStyleReq);
    }

    @Override
    public void delete(@RequestParam("id") Long id) {
        iProductToStyleService.delete(id);
    }

    @Override
    public List<ProductToStyleResp> list(@RequestParam("productId") Long productId) {
        return iProductToStyleService.list(productId);
    }
}
