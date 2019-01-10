package com.iot.device.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.business.ProductBusinessService;
import com.iot.device.business.core.DeviceCoreBusinessService;
import com.iot.device.model.Product;
import com.iot.device.service.IProductService;
import com.iot.device.vo.req.device.ListProductInfoReq;
import com.iot.device.vo.req.device.UpdateProductReq;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:03 2018/9/25
 * @Modify by:
 */
@RestController
public class ProductCoreController implements ProductCoreApi {

    @Autowired
    private DeviceCoreBusinessService deviceCoreBusinessService;

    @Autowired
    private ProductBusinessService productBusinessService;

    @Autowired
    private IProductService productService;


    @Override
    public List<ListProductRespVo> listProducts(@RequestBody @Validated ListProductInfoReq params) {
        List<ListProductRespVo> resultDataList = Lists.newArrayList();
        List<Product> sourceDataList = productBusinessService.listBatchProducts(params.getProductIds());
        if (CollectionUtils.isEmpty(sourceDataList)) {
            return resultDataList;
        }
        sourceDataList.forEach(source -> {
            ListProductRespVo target = new ListProductRespVo();
            BeanUtils.copyProperties(source, target);
            resultDataList.add(target);
        });
        return resultDataList;
    }


    @Override
    public GetProductInfoRespVo getByProductId(@RequestParam(value = "productId", required = true) Long productId) {
        GetProductInfoRespVo resultData = null;
        Product sourceData = productBusinessService.getProduct(productId);
        if (null == sourceData) {
            return resultData;
        }
        resultData = new GetProductInfoRespVo();
        BeanUtils.copyProperties(sourceData, resultData);
        return resultData;
    }

    @Override
    public GetProductInfoRespVo getByProductModel(@RequestParam(value = "productModel", required = true) String productModel) {
        GetProductInfoRespVo resultData = null;
        // 注意将model转换为小写
        Product sourceData = productBusinessService.getProduct(productModel.toLowerCase());
        if (null == sourceData) {
            return resultData;
        }
        resultData = new GetProductInfoRespVo();
        BeanUtils.copyProperties(sourceData, resultData);
        return resultData;
    }

    @Override
    public List<GetProductInfoRespVo> listByProductModel(@RequestBody Collection<String> productModelList) {
        List<GetProductInfoRespVo> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(productModelList)) {
            return resultDataList;
        }
        List<String> targetProductModelList = Lists.newArrayList();
        for (String productModel : productModelList) {
            // 注意将model转换为小写
            targetProductModelList.add(productModel.toLowerCase());
        }
        List<Product> targetProductList = productBusinessService.listBatchProductsByModels(targetProductModelList);
        if (!CollectionUtils.isEmpty(targetProductList)) {
            targetProductList.forEach(sourceData -> {
                GetProductInfoRespVo resultData = new GetProductInfoRespVo();
                BeanUtils.copyProperties(sourceData, resultData);
                resultDataList.add(resultData);
            });
        }
        return resultDataList;
    }

    @Override
    public void saveOrUpdate(@RequestBody UpdateProductReq params) {
        productBusinessService.saveOrUpdate(params);
    }


    @Override
    public void saveOrUpdateBatch(@RequestBody List<UpdateProductReq> paramsList) {

        productBusinessService.saveOrUpdateBatch(paramsList);
    }

    @Override
    public List<ListProductRespVo> listProductAll() {
        List<ListProductRespVo> resultDataList = Lists.newArrayList();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.orderBy("product_name", true);
        List<Product> sourceDataList = productService.selectList(wrapper);
        if (CollectionUtils.isEmpty(sourceDataList)) {
            return resultDataList;
        }
        sourceDataList.forEach(source -> {
            ListProductRespVo target = new ListProductRespVo();
            BeanUtils.copyProperties(source, target);
            resultDataList.add(target);
        });
        return resultDataList;
    }
}
