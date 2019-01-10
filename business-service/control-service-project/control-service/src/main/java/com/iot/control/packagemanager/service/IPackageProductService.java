package com.iot.control.packagemanager.service;

import com.iot.control.packagemanager.vo.req.SavePackageProductReq;

import java.util.List;

/**
 * @despriction：套包产品管理service
 * @author  yeshiyuan
 * @created 2018/11/23 17:52
 */
public interface IPackageProductService {

    /**
      * @despriction：保存套包关联产品
      * @author  yeshiyuan
      * @created 2018/11/23 20:02
      */
    void savePackageProduct(SavePackageProductReq savePackageProductReq);

    /**
      * @despriction：获取产品id
      * @author  yeshiyuan
      * @created 2018/11/23 20:02
      */
    List<Long> getProductIds(Long packageId, Long tenantId);

    /**
     * @despriction：校验哪些产品已添加过套包
     * @author  yeshiyuan
     * @created 2018/11/26 11:15
     */
    List<Long> chcekProductHadAdd(List<Long> productIds, Long tenantId);
}
