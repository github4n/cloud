package com.iot.device.core.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.core.ProductCacheCoreUtils;
import com.iot.device.model.Product;
import com.iot.device.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:11 2018/6/20
 * @Modify by:
 */
@Slf4j
public class ProductServiceCoreUtils {

    /**
     * 组装获取产品详细信息
     *
     * @param productId
     * @return
     * @author lucky
     * @date 2018/6/20 17:14
     */
    public static Product getProductByProductId(Long productId) {
        if (productId == null) {
            return null;
        }
        Product product = ProductCacheCoreUtils.getCacheData(productId, VersionEnum.V1);
        if (product == null) {
            product = getDBProductById(productId);
        }
        return product;
    }


    /**
     * 批量获取产品
     *
     * @param productIds
     * @return
     * @author lucky
     * @date 2018/7/12 9:47
     */
    public static List<Product> getProductsByProductIds(List<Long> productIds) {
        List<Product> cacheList = ProductCacheCoreUtils.getCacheDataList(productIds, VersionEnum.V1);
        //检查部分缓存过期的 ---捞出未命中的deviceIds
        List<Long> noHitCacheIds = getNoHitCacheProductIds(cacheList, productIds);
        //@防止缓存穿透
        List<Long> needGetDBIds = needGetDBIds(noHitCacheIds);
        //db获取
        List<Product> dbList = findDBProductListByIds(needGetDBIds);
        //组转返回
        return installList(cacheList, dbList);

    }

    //防止缓存穿透
    public static List<Long> needGetDBIds(List<Long> noHitCacheIds) {
        List<Long> needGetDBIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(noHitCacheIds)) {
            noHitCacheIds.forEach(id -> {
                boolean hasKey = ProductCacheCoreUtils.exist(id, VersionEnum.V1);
                if (!hasKey) {
                    needGetDBIds.add(id);
                }
            });
        }
        return needGetDBIds;
    }
    public static Product getProductByProductModel(String productModel) {
        if (StringUtils.isEmpty(productModel)) {
            return null;
        }
        Product product;
        Long productId = ProductCacheCoreUtils.getCacheData(productModel, VersionEnum.V1);
        if (productId == null) {
            product = getDBProductByProductModel(productModel);
        } else {
            product = ProductCacheCoreUtils.getCacheData(productId, VersionEnum.V1);
            if (product == null) {
                product = getDBProductById(productId);
            }
        }
        return product;
    }

    public static void removeCacheProduct(Long productId, String productModel) {

        if (productId != null) {
            ProductCacheCoreUtils.removeData(productId, VersionEnum.V1);
        }
        if (!StringUtils.isEmpty(productModel)) {
            ProductCacheCoreUtils.removeData(productModel, VersionEnum.V1);
        }
    }

    public static void cacheProductList(List<Product> productList) {
        if (CollectionUtils.isEmpty(productList)) {
            return;
        }
        for (Product product : productList) {
            cacheProduct(product);
        }
    }

    public static void cacheProductList(List<Long> productIds, List<Product> productList) {
        if (CollectionUtils.isEmpty(productIds)) {
            return;
        }
        if (!CollectionUtils.isEmpty(productList)) {
            productList.forEach(product -> {
                cacheProduct(product.getId(), product);
            });
        } else {
            productIds.forEach(productId -> {
                cacheProduct(productId, null);
            });
        }
    }

    public static void cacheProduct(Long productId, Product product) {
        if (productId == null) {
            return;
        }
        // add cache
        ProductCacheCoreUtils.cacheData(productId, product, VersionEnum.V1);
        if (product != null) {
            // add cache productModel
            ProductCacheCoreUtils.cacheData(product.getModel(), productId, VersionEnum.V1);
        }
    }

    public static void cacheProduct(String productModel, Long productId) {
        if (StringUtils.isEmpty(productModel)) {
            return;
        }
        // add cache productModel
        ProductCacheCoreUtils.cacheData(productModel, productId, VersionEnum.V1);
    }

    public static void cacheProduct(Product product) {
        if (product == null) {
            return;
        }
        Long productId = product.getId();
        // add cache
        ProductCacheCoreUtils.cacheData(productId, product, VersionEnum.V1);
        // add cache productModel
        ProductCacheCoreUtils.cacheData(product.getModel(), productId, VersionEnum.V1);
    }


    /**
     * 匹配命中缓存的数据 捞出过时导致未命中的集合 方便从db获取
     *
     * @param resList    命中缓存的结果
     * @param targetList 传入查询的集合
     * @return
     * @author lucky
     * @date 2018/6/27 17:55
     */
    public static List<Long> getNoHitCacheProductIds(List<Product> resList, List<Long> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(resList)) {
            //结果内容为空都未命中 返回请求的集合 去db获取
            return targetList;
        }
        if (resList.contains(null)) {
            resList.remove(null);
        }

        if (resList.size() == targetList.size()) {
            log.debug("getNoHitCacheProductIds-hit-cache-all-------end");
            return null;
        }
        log.debug("getNoHitCacheProductIds-部分缓存失效----》》》");
        Map<Long, Product> tempMap = Maps.newHashMap();
        List<Long> noHitCacheList = Lists.newArrayList();
        for (Product res : resList) {
            if (res == null) {
                continue;
            }
            tempMap.put(res.getId(), res);
        }
        for (Long targetId : targetList) {
            Product temp = tempMap.get(targetId);
            if (temp == null) {
                //过滤出未命中的设备
                noHitCacheList.add(targetId);
            }
        }
        return noHitCacheList;
    }

    public static Product getDBProductById(Long id) {
        if (id == null) {
            return null;
        }
        IProductService productService = ApplicationContextHelper.getBean(IProductService.class);
        Product product = productService.selectById(id);
        cacheProduct(id, product);
        return product;
    }

    public static Product getDBProductByProductModel(String productModel) {
        if (StringUtils.isEmpty(productModel)) {
            return null;
        }
        IProductService productService = ApplicationContextHelper.getBean(IProductService.class);
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.eq("model", productModel);
        Product product = productService.selectOne(wrapper);
        //add cache
        cacheProduct(productModel, product != null ? product.getId() : null);
        return product;
    }

    /**
     * 获取db 缓存的数据
     *
     * @param ids
     * @return
     * @author lucky
     * @date 2018/6/27 17:57
     */
    public static List<Product> findDBProductListByIds(List<Long> ids) {
        List<Product> productList = null;
        if (!CollectionUtils.isEmpty(ids)) {
            IProductService productService = ApplicationContextHelper.getBean(IProductService.class);
            EntityWrapper<Product> wrapper = new EntityWrapper<>();
            if (ids.size() == 1) {
                wrapper.eq("id", ids.get(0));
            } else {
                wrapper.in("id", ids);
            }
            productList = productService.selectList(wrapper);
            cacheProductList(ids, productList);
        }
        return productList;
    }

    /**
     * 组装 缓存命中和db命中的数据
     *
     * @param cacheList 缓存list
     * @param dbList    db list
     * @return
     * @author lucky
     * @date 2018/6/27 18:37
     */
    public static List<Product> installList(List<Product> cacheList, List<Product> dbList) {
        List<Product> returnInstallDeviceList = null;
        if (!CollectionUtils.isEmpty(cacheList)) {
            if (cacheList.contains(null)) {
                cacheList.remove(null);
            }
            returnInstallDeviceList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(dbList)) {
                cacheList.addAll(dbList);
            }
            returnInstallDeviceList.addAll(cacheList);
        } else {
            if (!CollectionUtils.isEmpty(dbList)) {
                returnInstallDeviceList = Lists.newArrayList();
                returnInstallDeviceList.addAll(dbList);
            }
        }
        return returnInstallDeviceList;
    }

}
