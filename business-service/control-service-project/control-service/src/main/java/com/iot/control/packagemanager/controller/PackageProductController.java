package com.iot.control.packagemanager.controller;

import com.iot.control.packagemanager.api.PackageProductApi;
import com.iot.control.packagemanager.service.IPackageProductService;
import com.iot.control.packagemanager.vo.req.SavePackageProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
  * @despriction：套包产品管理ctrl
  * @author  yeshiyuan
  * @created 2018/11/23 17:56
  */
@RestController
public class PackageProductController implements PackageProductApi {

    @Autowired
    private IPackageProductService packageProductService;

    /**
     * @despriction：保存套包关联产品
     * @author  yeshiyuan
     * @created 2018/11/23 19:58
     */
    @Override
    public void save(@RequestBody SavePackageProductReq saveReq) {
        SavePackageProductReq.checkParam(saveReq);
        packageProductService.savePackageProduct(saveReq);
    }

    /**
     * @despriction：获取产品id
     * @author  yeshiyuan
     * @created 2018/11/23 20:00
     */
    @Override
    public List<Long> getProductIds(@RequestParam Long packageId, @RequestParam Long tenantId) {
        return packageProductService.getProductIds(packageId, tenantId);
    }

    /**
     * @despriction：校验哪些产品已添加过套包
     * @author  yeshiyuan
     * @created 2018/11/26 11:15
     */
    @Override
    public List<Long> chcekProductHadAdd(@RequestParam("productIds") List<Long> productIds, @RequestParam("tenantId") Long tenantId) {
        return packageProductService.chcekProductHadAdd(productIds, tenantId);
    }
}
