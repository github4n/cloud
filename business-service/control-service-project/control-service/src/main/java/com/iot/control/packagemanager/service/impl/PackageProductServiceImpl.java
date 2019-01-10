package com.iot.control.packagemanager.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.exception.BusinessException;
import com.iot.control.packagemanager.entity.PackageProduct;
import com.iot.control.packagemanager.execption.PackageExceptionEnum;
import com.iot.control.packagemanager.mapper.PackageProductMapper;
import com.iot.control.packagemanager.service.IPackageProductService;
import com.iot.control.packagemanager.vo.req.SavePackageProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
  * @despriction：套包产品管理service
  * @author  yeshiyuan
  * @created 2018/11/23 17:52
  */
@Service
public class PackageProductServiceImpl extends ServiceImpl<PackageProductMapper, PackageProduct> implements IPackageProductService{

    @Autowired
    private PackageProductMapper packageProductMapper;

    /**
     * @despriction：保存套包关联产品
     * @author  yeshiyuan
     * @created 2018/11/23 20:02
     */
    @Override
    public void savePackageProduct(SavePackageProductReq saveReq) {
      /*  List<Long> hadAddProductId = packageProductMapper.chcekProductHadAdd(saveReq.getProductIds(), saveReq.getTenantId());
        if (hadAddProductId.size() > 0) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_HAD_ADD);
        }*/
        List<PackageProduct> list = new ArrayList<>();
        saveReq.getProductIds().forEach(productId -> {
            PackageProduct pp = new PackageProduct(saveReq.getPackageId(), productId, saveReq.getTenantId(), saveReq.getCreateBy(), new Date());
            list.add(pp);
        });
        packageProductMapper.batchInsert(list);
    }

    /**
     * @despriction：获取产品id
     * @author  yeshiyuan
     * @created 2018/11/23 20:02
     */
    @Override
    public List<Long> getProductIds(Long packageId, Long tenantId) {
        return packageProductMapper.getProductIds(packageId, tenantId);
    }

    /**
     * @despriction：校验哪些产品已添加过套包
     * @author  yeshiyuan
     * @created 2018/11/26 11:15
     */
    @Override
    public List<Long> chcekProductHadAdd(List<Long> productIds, Long tenantId) {
        return packageProductMapper.chcekProductHadAdd(productIds, tenantId);
    }
}
