package com.iot.device.business;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.comm.utils.HitCacheUtils;
import com.iot.device.core.ProductCacheCoreUtils;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.model.Product;
import com.iot.device.service.IProductService;
import com.iot.device.vo.req.device.UpdateProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:03 2018/10/10
 * @Modify by:
 */
@Component
public class ProductBusinessService {

    @Autowired
    private IProductService productService;


    public List<Product> listProducts(List<Long> productIds) {
        List<Product> resultDataList;
        //1.获取缓存数据
        List<Product> cacheDataList = ProductCacheCoreUtils.getCacheDataList(productIds, VersionEnum.V1);
        List<Long> cacheIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            cacheDataList.forEach(device -> {
                cacheIds.add(device.getId());
            });
        }
        // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
        List<Long> noHitDeviceIds = HitCacheUtils.getNoHitCacheIds(cacheIds, productIds);
        // 3.db获取
        List<Product> dbDataList = this.productService.findListByProductIds(noHitDeviceIds);
        // 4.缓存db数据
        ProductCacheCoreUtils.cacheDataList(dbDataList, VersionEnum.V1);
        // 5.组转返回
        resultDataList = HitCacheUtils.installList(cacheDataList, dbDataList);
        return resultDataList;
    }

    public List<Product> listBatchProducts(List<Long> productIds) {
        return listBatchProducts(productIds, 15);
    }


    public List<Product> listBatchProducts(List<Long> productIds, int batchSize) {
        if (CollectionUtils.isEmpty(productIds)) {
            return Lists.newArrayList();
        }
        List<Product> resultDataList = Lists.newArrayList();

        List<Long> batchIds = Lists.newArrayList();
        for (int i = 0; i < productIds.size(); i++) {
            batchIds.add(productIds.get(i));
            if (i >= 1 && i % batchSize == 0) {
                //获取一次 并清空batchIds
                List<Product> tempResultDataList = this.listProducts(batchIds);
                if (!CollectionUtils.isEmpty(tempResultDataList)) {
                    resultDataList.addAll(tempResultDataList);
                }
                batchIds.clear();
            }
        }
        List<Product> tempResultDataList = this.listProducts(batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataList)) {
            resultDataList.addAll(tempResultDataList);
        }
        return resultDataList;
    }

    public Product getProduct(Long productId) {

        // 1.获取缓存数据
        Product cacheData = ProductCacheCoreUtils.getCacheData(productId, VersionEnum.V1);
        if (null != cacheData) {
            return cacheData;
        }
        // 1.1防止穿透db处理
        // 2.db获取
        Product dbData = this.productService.getProductByProductId(productId);
        // 3.缓存db数据
        if (null != dbData) {
            ProductCacheCoreUtils.cacheData(productId, dbData, VersionEnum.V1);
        }
        return dbData;
    }

    public Product getProduct(String productModel) {

        // 1.获取缓存数据
        Long cacheData = ProductCacheCoreUtils.getCacheData(productModel, VersionEnum.V1);
        if (null != cacheData) {
            return getProduct(cacheData);
        }
        // 1.1防止穿透db处理
        // 2.db获取
        Product dbData = this.productService.getProductByProductModel(productModel);
        // 3.缓存db数据
        if (null != dbData) {
            ProductCacheCoreUtils.cacheData(dbData.getId(), dbData, VersionEnum.V1);
            ProductCacheCoreUtils.cacheData(productModel, dbData.getId(), VersionEnum.V1);
        }
        return dbData;
    }

    public List<Product> listBatchProductsByModels(List<String> productModels) {
        List<Product> resultDataList = Lists.newArrayList();

        // 1.获取缓存数据[productModels]
        List<Long> cacheDataList = ProductCacheCoreUtils.getCacheDataIdList(productModels, VersionEnum.V1);
        if (CollectionUtils.isEmpty(cacheDataList)) {
            //1.1 获取数据库
            List<Product> dbResultDataList = this.productService.findListByProductModels(productModels);
            if (!CollectionUtils.isEmpty(dbResultDataList)) {
                resultDataList.addAll(dbResultDataList);
                ProductCacheCoreUtils.cacheDataList(dbResultDataList, VersionEnum.V1);
            }
            return resultDataList;
        }
        //2.获取缓存数据[productIds]
        List<Product> cacheResultDataList = this.listBatchProducts(cacheDataList);
        Map<String, Product> productMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(cacheResultDataList)) {
            resultDataList.addAll(cacheResultDataList);
            cacheResultDataList.forEach(product -> {
                productMap.put(product.getModel(), product);
            });
        }
        List<String> cacheProductModels = Lists.newArrayList(productMap.keySet());
        //2.1获取未命中的productModel
        List<String> notHitProductModels = HitCacheUtils.getNoHitCacheIds(cacheProductModels, productModels);
        //3. 获取数据库
        List<Product> dbDataList = this.productService.findListByProductModels(notHitProductModels);
        if (!CollectionUtils.isEmpty(dbDataList)) {
            resultDataList.addAll(dbDataList);
            ProductCacheCoreUtils.cacheDataList(dbDataList, VersionEnum.V1);
        }

        return resultDataList;
    }


    public void saveOrUpdate(UpdateProductReq params) {
        Product sourceData = installProduct(params);
        this.productService.insertOrUpdate(sourceData);
        if (sourceData.getId() != null) {
            ProductCacheCoreUtils.removeData(sourceData.getModel(), VersionEnum.V1);
            ProductCacheCoreUtils.removeData(params.getId(), VersionEnum.V1);
        }
    }

    public void saveOrUpdateBatch(List<UpdateProductReq> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }
        List<Product> targetList = Lists.newArrayList();
        paramsList.forEach(params -> {
            Product sourceData = installProduct(params);
            targetList.add(sourceData);
        });
        this.productService.insertOrUpdateBatch(targetList);
    }

    private Product installProduct(UpdateProductReq params) {
        Product sourceData = this.getProduct(params.getModel());
        if (null == sourceData) {
            sourceData = new Product();
            sourceData.setCreateTime(new Date());
            if (params.getId() != null) {
                //修改 product
                Product origSourceData = this.getProduct(params.getId());
                if (origSourceData != null) {
                    //删除原有的 model关联的 产品缓存
                    ProductCacheCoreUtils.removeData(origSourceData.getModel(), VersionEnum.V1);
                    ProductCacheCoreUtils.removeData(params.getId(), VersionEnum.V1);
                }
            }
        } else {
            if (params.getId() != null) {
                ProductCacheCoreUtils.removeData(params.getId(), VersionEnum.V1);
                if (sourceData.getId().compareTo(params.getId()) == 0) {
                    if (!params.getModel().equals(sourceData.getModel())) {
                        //model修改 但修改成已经存在的model
                        throw new BusinessException(ProductExceptionEnum.PRODUCT_MODEL_EXIST);
                    }
                }
            }
        }
        sourceData.setTenantId(params.getTenantId());
        sourceData.setDeviceTypeId(params.getDeviceTypeId());
        sourceData.setProductName(params.getProductName());
        sourceData.setCommunicationMode(params.getCommunicationMode());
        sourceData.setModel(params.getModel());
        sourceData.setRemark(params.getRemark());
        sourceData.setConfigNetMode(params.getConfigNetMode());
        sourceData.setIsKit(params.getIsKit());
        sourceData.setIsDirectDevice(params.getIsDirectDevice());
        sourceData.setIcon(params.getIcon());
        sourceData.setDevelopStatus(params.getDevelopStatus());
        sourceData.setEnterpriseDevelopId(params.getEnterpriseDevelopId());

        return sourceData;
    }


}
