package com.iot.portal.packagemanager.service.impl;


import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.*;
import com.iot.control.packagemanager.vo.req.PackageReq;
import com.iot.control.packagemanager.vo.resp.DeviceTypeIdAndName;
import com.iot.control.packagemanager.vo.resp.PackageBasicResp;
import com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductToServiceModuleApi;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.product.GatewayChildProductResp;
import com.iot.device.vo.rsp.product.PackageProductNameResp;
import com.iot.device.vo.rsp.product.PackageProductResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.file.api.FileApi;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.exception.PackageExceptionEnum;
import com.iot.portal.packagemanager.service.PackageInfoService;
import com.iot.portal.packagemanager.vo.req.SavePackageBaseInfoReq;
import com.iot.portal.packagemanager.vo.resp.PackageDeviceTypeDetailResp;
import com.iot.portal.packagemanager.vo.resp.PackageProductInfoResp;
import com.iot.portal.packagemanager.vo.resp.PackageTypeResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.DictApi;
import com.iot.system.api.LangApi;
import com.iot.system.enums.DictTypeEnum;
import com.iot.system.vo.DictItemResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @despriction：套包基本信息service
 * @author  yeshiyuan
 * @created 2018/12/7 10:43
 */
@Service
public class PackageInfoServiceImpl implements PackageInfoService{

    @Autowired
    private PackageProductApi packageProductApi;

    @Autowired
    private ProductToServiceModuleApi productToServiceModuleApi;

    @Autowired
    private PackageApi packageApi;

    @Autowired
    private PackageDeviceTypeApi packageDeviceTypeApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @Autowired
    private TemplateRuleApi templateRuleApi;

    @Autowired
    private SceneInfoApi sceneInfoApi;

    @Autowired
    private DictApi dictApi;

    @Autowired
    private LangApi langApi;

    @Autowired
    private FileApi fileApi;

    @Override
    public List<PackageTypeResp> loadPackageType() {
        List<PackageTypeResp> packageTypeResps = new ArrayList<>();
        Map<String, DictItemResp> map = dictApi.getDictItem(DictTypeEnum.PACKAGE_TYPE.getCode());
        if (map != null) {
            List<String> itemNames = new ArrayList<>();
            map.values().forEach(dictItemResp -> {
                itemNames.add(dictItemResp.getItemName());
                packageTypeResps.add(new PackageTypeResp(Integer.valueOf(dictItemResp.getItemId()),dictItemResp.getItemName() ));
            });
            Map<String, String> langMap = langApi.getLangValueByKey(itemNames, LocaleContextHolder.getLocale().toString());
            packageTypeResps.forEach(packageType -> {
                packageType.setPackageType(langMap.get(packageType.getPackageType()));
            });
        }
        return packageTypeResps;
    }

    /**
     * @despriction：获取产品功能模组详情
     * @author  yeshiyuan
     * @created 2018/12/10 11:55
     */
    @Override
    public PackageServiceModuleDetailResp getProductModuleDetail(Long productId, String iftttType) {
        if (!ModuleIftttTypeEnum.checkModuleDetail(iftttType)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "ifttt type error");
        }
        return productToServiceModuleApi.queryServiceModuleDetailByIfttt(productId, iftttType);
    }

    /**
     * @despriction：删除套包
     * @author  yeshiyuan
     * @created 2018/12/10 19:26
     */
    @Override
    public void deletePackage(Long packageId) {
        packageApi.deletePackageRelateData(packageId, SaaSContextHolder.currentTenantId());
    }

    /**
     *
     * 描述：查询套包产品
     * @author 李帅
     * @created 2018年12月10日 下午8:55:29
     * @since
     * @return
     */
    @Override
    public List<PackageProductResp> getPackageProducts() {
        List<PackageProductResp> packageProductResps = productApi.getPackageProducts(SaaSContextHolder.currentTenantId());
        List<Long> productIds = new ArrayList<Long>();
        for(PackageProductResp packageProductResp : packageProductResps){
            productIds.add(packageProductResp.getId());
        }
        if(productIds != null && productIds.size() > 0){
            //产品只能被一个套包关联，不能重复关联
            List<Long> hadAddIds = packageProductApi.chcekProductHadAdd(productIds, SaaSContextHolder.currentTenantId());
            List<PackageProductResp> hadAddProductInfos = new ArrayList<PackageProductResp>();
            for(PackageProductResp packageProductResp : packageProductResps){
                if(hadAddIds.contains(packageProductResp.getId())){
                    hadAddProductInfos.add(packageProductResp);
                }
            }
            packageProductResps.removeAll(hadAddProductInfos);
        }
        return packageProductResps;
    }

    /**
     *
     * 描述：查询网管子产品
     * @author 李帅
     * @created 2018年12月10日 下午8:56:00
     * @since
     * @param productId
     * @return
     */
    @Override
    public List<GatewayChildProductResp> getGatewayChildProducts(Long productId) {
        return productApi.getGatewayChildProducts(SaaSContextHolder.currentTenantId(), productId);
    }

    /**
     *@description 获取套包详情
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/13 14:19
     *@return com.iot.control.packagemanager.vo.resp.PackageDeviceTypeInfoResp
     */
    @Override
    public PackageDeviceTypeDetailResp getBossPackageInfo(Long packageId) {
        if (packageId == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PARAM_NULL, " package id is null");
        }
        PackageResp packageResp = packageApi.getPackageById(packageId, -1L);
        if (packageResp == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_EXIST, " package not exists");
        }
        // 获取该套包绑定的所有设备类型信息
        List<PackageDeviceTypeInfoResp> packageDeviceTypeInfoResps = packageDeviceTypeApi.getPackageDeviceTypeInfo(packageId, SaaSContextHolder.currentTenantId());
        // 设备类型id列表
        List<Long> deviceTypeIds = new ArrayList<>();
        packageDeviceTypeInfoResps.forEach(packageDeviceTypeInfoResp -> {
            deviceTypeIds.add(packageDeviceTypeInfoResp.getDeviceTypeId());
        });
        // 根据设备类型id，获取设备类型详细信息
        List<DeviceTypeResp> deviceTypeResps = deviceTypeApi.getDeviceTypeIdAndNameByIds(deviceTypeIds);
        Map<Long, DeviceTypeResp> map = new HashMap<>();
        if (deviceTypeResps != null && deviceTypeResps.size() > 0) {
            map = deviceTypeResps.stream().collect(Collectors.toMap(DeviceTypeResp ::getId, a ->a, (k1,k2) -> k1));
        }

        // 定义返回结果集
        PackageDeviceTypeDetailResp newPackageDeviceTypeInfoResp = new PackageDeviceTypeDetailResp();
        // 套包绑定的设备类型id、设备类型名称
        List<DeviceTypeIdAndName> deviceTypeIdAndNames = new ArrayList<>();
        // 设置套包基本信息
        newPackageDeviceTypeInfoResp.setPackageId(packageResp.getId());
        newPackageDeviceTypeInfoResp.setPackageName(packageResp.getName());

        newPackageDeviceTypeInfoResp.setPackageType(packageResp.getPackageType());
        // 为每个绑定的设备赋值设备类型id、设备类型名称
        for (PackageDeviceTypeInfoResp t : packageDeviceTypeInfoResps) {
            DeviceTypeIdAndName deviceTypeIdAndName = new DeviceTypeIdAndName();
            if (t.getDeviceTypeId() != null) {
                DeviceTypeResp deviceTypeResp = map.get(t.getDeviceTypeId());
                deviceTypeIdAndName.setDeviceTypeId(deviceTypeResp.getId());
                deviceTypeIdAndName.setDeviceTypeName(deviceTypeResp.getName());
            }
            deviceTypeIdAndNames.add(deviceTypeIdAndName);
        }
        newPackageDeviceTypeInfoResp.setDeviceTypeIdAndNames(deviceTypeIdAndNames);
        return newPackageDeviceTypeInfoResp;
    }

    @Override
    public List<PackageBasicResp> getBossPackageList() {
        // 获取boss创建的套包
        List<PackageBasicResp> packageBasicResps = packageApi.getPackageInfo(-1L);
        if (packageBasicResps != null && packageBasicResps.size() > 0) {
            // 获取保存的图片的fileIds
            List<String> iconFileId = new ArrayList<>();
            packageBasicResps.forEach(t-> {
                iconFileId.add(t.getIcon());
            });
            // 获取图片的访问路径
            Map<String, String> map = fileApi.getGetUrl(iconFileId);
            if (map != null && !map.isEmpty()) {
                packageBasicResps.forEach(f -> {
                    String iconUrl = map.get(f.getIcon());
                    if (StringUtil.isNotBlank(iconUrl)) {
                        f.setIconUrl(iconUrl);
                    }
                });
            }
        }
        return packageBasicResps;
    }

    /**
     * @despriction：保存套包基本信息
     * @author  yeshiyuan
     * @created 2018/12/17 9:48
     * @params []
     * @return com.iot.common.beans.CommonResponse
     */
    @Override
    public void savePackageBaseInfo(SavePackageBaseInfoReq saveReq) {
        if (saveReq == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "param is null");
        }
        PackageResp packageResp = packageApi.getPackageById(saveReq.getPackageId(), SaaSContextHolder.currentTenantId());
        if (packageResp == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "package not exist");
        }
        PackageReq updateReq = new PackageReq();
        updateReq.setId(saveReq.getPackageId());
        updateReq.setName(saveReq.getPackageName());
        if (StringUtil.isNotBlank(saveReq.getIcon())) {
            if (!packageResp.getIcon().equals(saveReq.getIcon())) {
                fileApi.deleteObject(packageResp.getIcon());
                updateReq.setIcon(saveReq.getIcon());
            }
        }
        packageApi.updatePackageById(updateReq);
    }

    /**
     * @despriction：根据ifttt类型过滤加载套包下的产品
     * @author  yeshiyuan
     * @created 2018/12/17 10:27
     * @params [packageId, iftttType]
     * @return com.iot.common.beans.CommonResponse
     */
    @Override
    public List<PackageProductInfoResp> getProductByIftttType(Long packageId, String iftttType) {
        List<PackageProductInfoResp> list = new ArrayList<>();
        List<Long> productIds = packageProductApi.getProductIds(packageId, SaaSContextHolder.currentTenantId());
        if (productIds== null || productIds.isEmpty()) {
            return list;
        }
        List<PackageProductNameResp> productResps = productApi.getProductByIds(productIds);
        List<Long> deviceTypeIds = productResps.stream().map(PackageProductNameResp::getDeviceTypeId).collect(Collectors.toList());
        List<DeviceTypeResp> deviceTypeResps = deviceTypeApi.getByIdsAndIfffType(deviceTypeIds, iftttType);
        productResps = productResps.stream().filter(product -> deviceTypeResps.contains(product.getDeviceTypeId())).collect(Collectors.toList());
        if (productResps != null && !productResps.isEmpty()) {
            productResps.forEach(product -> {
                PackageProductInfoResp productInfoResp = new PackageProductInfoResp(product.getId(), product.getProductName());
                list.add(productInfoResp);
            });

        }
        return list;
    }
}
