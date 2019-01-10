package com.iot.device.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.core.service.ProductServiceCoreUtils;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.enums.product.ProductAuditStatusEnum;
import com.iot.device.enums.product.ProductReviewProcessStatusEnum;
import com.iot.device.exception.OtaExceptionEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.mapper.*;
import com.iot.device.model.*;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.service.IDeviceTypeService;
import com.iot.device.service.IProductConfigNetmodeService;
import com.iot.device.service.IProductService;
import com.iot.device.service.core.ModuleCoreService;
import com.iot.device.vo.req.*;
import com.iot.device.vo.req.DataPointReq.SmartWraper;
import com.iot.device.vo.req.ota.ProductOtaReq;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.*;
import com.iot.device.vo.rsp.product.*;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import com.iot.payment.enums.goods.TechnicalSchemeEnum;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductDataPointMapper productDataPointMapper;

    @Autowired
    private DataPointMapper dataPointMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SmartDataPointMapper smartDataPointMapper;

    @Autowired
    private IDeviceTypeService deviceTypeService;

    @Autowired
    private ModuleCoreService moduleCoreService;

    @Autowired
    private ProductReviewRecordMapper productReviewRecordMapper;

    @Autowired
    private ServiceReviewMapper serviceReviewMapper;

    @Autowired
    private IProductConfigNetmodeService iProductConfigNetmodeService;

    public List<DataPointResp> findDataPointListByProductId(Long productId) {
        List<DataPointResp> dataPointRespList = null;
        EntityWrapper<ProductDataPoint> productDataPointWrapper = new EntityWrapper<>();
        productDataPointWrapper.eq("product_id", productId);
        productDataPointWrapper.setSqlSelect("data_point_id");
        List<Object> productDataPointIds = productDataPointMapper.selectObjs(productDataPointWrapper);
        if (!CollectionUtils.isEmpty(productDataPointIds)) {
            EntityWrapper<DataPoint> wrapper = new EntityWrapper<>();
            wrapper.in("id", productDataPointIds);
            List<DataPoint> dataPointList = dataPointMapper.selectList(wrapper);
            if (!CollectionUtils.isEmpty(dataPointList)) {
                dataPointRespList = new ArrayList<>();

                for (DataPoint dp : dataPointList) {
                    EntityWrapper<SmartDataPoint> eq = new EntityWrapper<>();
                    eq.eq("data_point_id", dp.getId());
                    List<SmartDataPoint> sdps = smartDataPointMapper.selectList(eq);
                    DataPointResp dest = new DataPointResp();
                    try {
                        PropertyUtils.copyProperties(dest, dp);
                        if (sdps != null) {
                            sdps.forEach((sd) -> {
                                SmartWraper sw = new SmartWraper();
                                sw.setCode(sd.getSmartCode());
                                sw.setId(sd.getId());
                                sw.setSmart(sd.getSmart());
                                dest.add(sw);
                            });
                        }
                        dataPointRespList.add(dest);
                    } catch (Exception e) {
                        LOGGER.info("findDataPointListByProductId error", e);
                        throw new BusinessException(ProductExceptionEnum.PRODUCT_GET_ERROR);
                    }

                }
            }
        }
        return dataPointRespList;
    }

    public ProductResp getProductByDeviceTypeIdAndProperties(Long deviceTypeId, List<Map<String, Object>> propertyList) {
        ProductResp productResp = null;
        Long tenantId = SaaSContextHolder.currentTenantId();
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("device_type_id", deviceTypeId);
        List<Product> productList = productMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(productList)) {
            for (Product product : productList) {// 遍历产品列表
                List<DeviceFunResp> deviceFunRespList = dataPointMapper.selectDataPointsByProductId(product.getId());// 获得产品功能点列表
                boolean Result = matchProductDataPoint(propertyList, deviceFunRespList);
                if (Result) {
                    productResp = new ProductResp();
                    try {
                        PropertyUtils.copyProperties(productResp, product);
                    } catch (Exception e) {
                        LOGGER.info("findProductListByDeviceTypeId error", e);
                        throw new BusinessException(ProductExceptionEnum.PRODUCT_GET_ERROR);
                    }
                    break;
                }
            }
        }
        return productResp;
    }


    /**
     * 描述：匹配产品功能点
     *
     * @param propertyList
     * @param dataPointList
     * @return
     * @author chenhongqiao
     * @created 2018年2月6日 下午9:19:07
     */
    private boolean matchProductDataPoint(List<Map<String, Object>> propertyList, List<DeviceFunResp> dataPointList) {
        if (dataPointList.size() != propertyList.size()) {
            return false;
        } else {
            Map<Object, Long> map = new HashMap<>();
            for (DeviceFunResp dataPoint : dataPointList) {// 遍历功能点列表，datapoint对象
                map.put(dataPoint.getPropertyCode(), dataPoint.getDataPointId());
            }
            DeviceFunResp dataPoint1 = null;

            for (Map<String, Object> propertyMap : propertyList) {// 对比设备上报功能点
                dataPoint1 = new DeviceFunResp();
                for (Map.Entry<String, Object> dpMap : propertyMap.entrySet()) {// 对比1个功能点内容
                    switch (dpMap.getKey()) {// 数据组装
                        case "name":
                            dataPoint1.setPropertyCode(dpMap.getValue().toString());
                            break;
                        case "type":
                            Long id = map.get(dataPoint1.getPropertyCode());
                            if (id != null) {
                                dataPoint1.setDataPointId(id);
                                //type类型的分解，字符串拼接
                                Object typeObject = dpMap.getValue();
                                if (typeObject.toString().equalsIgnoreCase("boolean")) {
                                    dataPoint1.setDataType((byte) 0);
                                    dataPoint1.setProperty("{\"boolean\":\"0\"}");
                                }
                                if (typeObject.toString().equalsIgnoreCase("unsignedInt")) {
                                    dataPoint1.setDataType((byte) 2);
                                }
                                if (typeObject.toString().contains("int")) {// int[0:100]
                                    dataPoint1.setDataType((byte) 1);
                                    String[] str = typeObject.toString().split("\\[|:|\\]");
                                    if (str != null && str.length == 3) {// 判断切割
                                        dataPoint1.setProperty("{\"min\":\"" + str[1] + "\"," + "\"max\":\"" + str[2] + "\"}");
                                    } else {
                                        dataPoint1.setProperty("{\"min\":\"" + "" + "\"," + "\"max\":\"" + "" + "\"}");// 单独int
                                    }
                                }
                                if (typeObject.toString().equalsIgnoreCase("string")) {
                                    dataPoint1.setDataType((byte) 5);
                                }
                            } else {
                                return false;
                            }
                            break;
                        case "access":
                            Byte mode = null;
                            if (dpMap.getValue().toString().equals("r")) {
                                mode = 0;
                            } else if (dpMap.getValue().toString().equals("w")) {
                                mode = 1;
                            } else if (dpMap.getValue().toString().equals("rw")) {
                                mode = 2;
                            }
                            dataPoint1.setMode(mode);
                            break;
                        default:
                            break;
                    }
                }

                EntityWrapper<DataPoint> wrapper = new EntityWrapper<>();
                wrapper.eq("id", dataPoint1.getDataPointId());
                wrapper.eq("model", dataPoint1.getMode());
                wrapper.eq("data_type", dataPoint1.getDataType());
                wrapper.eq("property_code", dataPoint1.getPropertyCode());

                if (!StringUtils.isEmpty(dataPoint1.getProperty())) {
                    wrapper.eq("property", dataPoint1.getProperty());
                }

                List<DataPoint> tempDataPointList = dataPointMapper.selectList(wrapper);
                if (CollectionUtils.isEmpty(tempDataPointList)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public List<ProductResp> findProductListByTenantId(ProductReq product) {
        List<ProductResp> productList = productMapper.findProductListByTenantId(product.getTenantId(), product.getProductName());
        return productList;
    }

    /**
     * 描述：根据租户id获取直连产品列表
     *
     * @param product
     * @return
     * @author 李帅
     * @created 2018年11月8日 上午11:26:38
     * @since
     */
    @Override
    public List<ProductResp> findDirectProductListByTenantId(ProductReq product) {
        List<ProductResp> productList = productMapper.findDirectProductListByTenantId(product.getTenantId(), product.getProductName());
        return productList;
    }

    @Override
    public List<ProductResp> findAllDirectProductList() {
        List<ProductResp> productList = productMapper.findAllDirectProductList();
        return productList;
    }

    @Override
    public Page<ProductResp> findProductPageByTenantId(GetProductReq req) {
        Page<ProductResp> page = new Page<>();
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();
        if (pageNum < 1 || pageSize <= 0 || pageSize > 100) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_PAGE_ILLEGAL);
        }

        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        if (req.getDeviceTypeId() != null) {
            wrapper.eq("a.device_type_id", req.getDeviceTypeId());
        }
        if (req.getTenantId() != null) {
            wrapper.eq("a.tenant_id", req.getTenantId());
        }
        if (req.getIsKit() != null) {
            wrapper.eq("a.is_kit", req.getTenantId());
        }
        if (req.getIsDirectDevice() != null) {
            wrapper.eq("a.is_direct_device", req.getIsDirectDevice());
        }
        if (req.getCatalogId() != null) {
            wrapper.eq("c.id", req.getCatalogId());
        }
        if (!StringUtils.isEmpty(req.getSearchValues())) {
            wrapper.andNew(true, "")
                    .like("a.product_name", req.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("a.model", req.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("b.type", req.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("b.name", req.getSearchValues(), SqlLike.DEFAULT);
        }
        wrapper.orderBy(true, "a.id", false);
        com.baomidou.mybatisplus.plugins.Page selectPage = new com.baomidou.mybatisplus.plugins.Page(req.getPageNum(), req.getPageSize());
        List<Product> productList = productMapper.selectProductPage(selectPage, wrapper);
        page.setResult(productList2ProductRespList(productList));
        page.setTotal(selectPage.getTotal());
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPages(selectPage.getPages());
        return page;
    }

    private List<ProductResp> productList2ProductRespList(List<Product> pros) {
        List<ProductResp> productList = new ArrayList<ProductResp>();
        if (pros != null) {
            pros.forEach((p) -> {
                ProductResp re = new ProductResp();
                BeanUtils.copyProperties(p, re);
                productList.add(re);
            });
        }
        return productList;
    }

    @Override
    public Page<ProductPageResp> findProductPage(ProductPageReq pageReq) {
        EntityWrapper wrapper = new EntityWrapper();
        if (!StringUtils.isEmpty(pageReq.getSearchValues())) {
            wrapper.like("a.model", "%" + pageReq.getSearchValues() + "%");
        }
        if (pageReq.getDeviceTypeId() != null) {
            wrapper.eq("a.device_type_id", pageReq.getDeviceTypeId());
        }
        if (pageReq.getEnterpriseDevelopId() != null) {
            wrapper.eq("a.enterprise_develop_id", pageReq.getEnterpriseDevelopId());
        }
        Long tenantId = null;
        try {
            tenantId = SaaSContextHolder.currentTenantId();
            LOGGER.info("findProductPage tenantId---------{}", tenantId);
        } catch (Exception e) {
            LOGGER.info("get tenantId error", e);
        }
        if (tenantId != null) {
            wrapper.eq("a.tenant_id", tenantId);
        }
        wrapper.orderBy("a.create_time", false);
        wrapper.orderBy("a.id", false);
        com.baomidou.mybatisplus.plugins.Page<Product> page = new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<Product> productList = this.productMapper.selectProductPage(page, wrapper);
        List<ProductPageResp> targetList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(productList)) {

            for (Product product : productList) {
                ProductPageResp target = new ProductPageResp();

                BeanCopyUtils.copyProduct(product, target);
                target.setAuditStatus(product.getAuditStatus());
                target.setServiceAlxAuditStatus(product.getServiceAlxAuditStatus());
                target.setServiceGooAuditStatus(product.getServiceGooAuditStatus());
                targetList.add(target);
            }
        }
        Page<ProductPageResp> pageResult = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        pageResult.setResult(targetList);
        pageResult.setTotal(page.getTotal());
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setPages(page.getPages());

        return pageResult;
    }

    @Transactional
    public Long addProduct(AddProductReq productReq) {
        Product whetherExistProductModel = ProductServiceCoreUtils.getProductByProductModel(productReq.getModel());
        if (whetherExistProductModel != null) {
            //check model whether exist db  if exist then throw model exist .
            throw new BusinessException(ProductExceptionEnum.PRODUCT_MODEL_EXIST);
        }
        //check deviceType whether exist db or cache if no exist throw error.
        deviceTypeService.checkDeviceTypExistByDeviceTypeId(productReq.getDeviceTypeId());

        Product product = new Product();
        product.setProductName(productReq.getProductName());
        product.setModel(productReq.getModel());
        product.setRemark(productReq.getRemark());
        product.setDeviceTypeId(productReq.getDeviceTypeId());
        product.setCreateBy(productReq.getCreateBy());
        product.setCreateTime(new Date());
        product.setTransmissionMode(productReq.getTransmissionMode());
        product.setCommunicationMode(productReq.getCommunicationMode());
        //根据技术方案判断是否是直连设备
        if (productReq.getCommunicationMode()!=null) {
            product.setIsDirectDevice(TechnicalSchemeEnum.checkIsDirect(Long.valueOf(productReq.getCommunicationMode())));
        }
        product.setDevelopStatus((Integer) DevelopStatusEnum.OPERATING.getValue());//开发中
        product.setEnterpriseDevelopId(productReq.getEnterpriseDevelopId());
        product.setIcon(productReq.getIcon());
        product.setTenantId(productReq.getTenantId());
        product.setIsKit(productReq.getIsKit());

        List<ProductConfigNetmodeReq> list = new ArrayList<>();
        if (StringUtil.isNotEmpty(productReq.getConfigNetModes())) {
            String[] result = productReq.getConfigNetModes().split(",");
            for (String configNetMode : result) {
                ProductConfigNetmodeReq productConfigNetmodeReq = new ProductConfigNetmodeReq();
                productConfigNetmodeReq.setName(configNetMode);
                productConfigNetmodeReq.setProductId(product.getId());
                productConfigNetmodeReq.setCreateBy(productReq.getCreateBy());
                productConfigNetmodeReq.setCreateTime(new Date());
                productConfigNetmodeReq.setUpdateBy(productReq.getUpdateBy());
                productConfigNetmodeReq.setUpdateTime(new Date());
                list.add(productConfigNetmodeReq);
            }
        }

        super.insert(product);
        //add cache product
        ProductServiceCoreUtils.cacheProduct(product);

        //谁的bug 增加了设置产品id 坑
        int i = 0;
        for (ProductConfigNetmodeReq productConfigNetmodeReq : list) {
            //
            productConfigNetmodeReq.setProductId(product.getId());
            list.set(i, productConfigNetmodeReq);
            i++;
        }

        iProductConfigNetmodeService.insertMore(list);

        return product.getId();
    }

    public ProductResp findProductByDeviceTypeId(Long deviceTypeId) {
        ProductResp productResp = productMapper.findProductByDeviceTypeId(deviceTypeId);
        return productResp;
    }

    /**
     * 
     * 描述：只更新产品信息
     * @author 李帅
     * @created 2018年12月6日 下午3:07:05
     * @since 
     * @param productReq
     * @return
     */
    @Transactional
    public Long onlyUpdateProduct(AddProductReq productReq) {
        Product product = super.selectById(productReq.getId());

        Long tenantId = productReq.getTenantId();
        if (tenantId == null) {
            tenantId = SaaSContextHolder.currentTenantId();
        }
        if (product == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        if (!SystemConstants.BOSS_TENANT.equals(tenantId) && !tenantId.equals(product.getTenantId())) {
            throw new BusinessException(ProductExceptionEnum.ACCESS_DENIED_EXCEPTION);//权限不足
        }
        String origModel = product.getModel();
        product.setProductName(productReq.getProductName());
        product.setModel(productReq.getModel());
        product.setRemark(productReq.getRemark());
        product.setDeviceTypeId(productReq.getDeviceTypeId());
        product.setUpdateTime(new Date());
        product.setTransmissionMode(productReq.getTransmissionMode());
        product.setCommunicationMode(productReq.getCommunicationMode());
        product.setConfigNetMode(null);
        product.setDevelopStatus((Integer) DevelopStatusEnum.OPERATING.getValue());//开发中
        product.setTenantId(tenantId);
        product.setEnterpriseDevelopId(productReq.getEnterpriseDevelopId());
        product.setIcon(productReq.getIcon());
        product.setIsDirectDevice(productReq.getIsDirectDevice());
        product.setIsKit(productReq.getIsKit());
//        product.setServiceGooAuditStatus(productReq.getServiceGooAuditStatus());
//        product.setServiceAlxAuditStatus(productReq.getServiceAlxAuditStatus());
        super.updateById(product);
        ProductServiceCoreUtils.removeCacheProduct(product.getId(), origModel);
        return product.getId();
    }
    
    @Transactional
    public Long updateProduct(AddProductReq productReq) {
        Product product = super.selectById(productReq.getId());

        Long tenantId = productReq.getTenantId();
        if (tenantId == null) {
            tenantId = SaaSContextHolder.currentTenantId();
        }
        if (product == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
//        if (product.getAuditStatus() != null && ProductAuditStatusEnum.AUDIT_SUCCESS.getValue() == product.getAuditStatus()) {
//            throw new BusinessException(ProductExceptionEnum.PRODUCT_AUDITED);//已审核，不能修改
//        }
        if (!SystemConstants.BOSS_TENANT.equals(tenantId) && !tenantId.equals(product.getTenantId())) {
            throw new BusinessException(ProductExceptionEnum.ACCESS_DENIED_EXCEPTION);//权限不足
        }

        //删除配置
        List<ProductConfigNetmodeRsp> list = iProductConfigNetmodeService.listByProductId(product.getId());
        List result = new ArrayList();
        list.forEach(m -> {
            result.add(m.getId());
        });
        iProductConfigNetmodeService.deleteMore(result);

        //添加配置
        List<ProductConfigNetmodeReq> productConfigNetmodeReqs = new ArrayList<>();
        if (StringUtil.isNotEmpty(productReq.getConfigNetModes())) {
            String[] splitResult = productReq.getConfigNetModes().split(",");
            for (String configNetMode : splitResult) {
                ProductConfigNetmodeReq productConfigNetmodeReq = new ProductConfigNetmodeReq();
                productConfigNetmodeReq.setName(configNetMode);
                productConfigNetmodeReq.setProductId(product.getId());
                productConfigNetmodeReq.setCreateBy(productReq.getCreateBy());
                productConfigNetmodeReq.setCreateTime(new Date());
                productConfigNetmodeReq.setUpdateBy(productReq.getUpdateBy());
                productConfigNetmodeReq.setUpdateTime(new Date());
                productConfigNetmodeReqs.add(productConfigNetmodeReq);
            }

        }
        iProductConfigNetmodeService.insertMore(productConfigNetmodeReqs);

        if (!product.getModel().equals(productReq.getModel())) {
            //update model check model
            Product whetherExistProductModel = ProductServiceCoreUtils.getProductByProductModel(productReq.getModel());
            if (whetherExistProductModel != null) {
                //check model whether exist db  if exist then throw model exist .
                throw new BusinessException(ProductExceptionEnum.PRODUCT_MODEL_EXIST);
            }
        }
        //check deviceType whether exist db or cache if no exist throw error.
        if (productReq.getDeviceTypeId() != null) {
            deviceTypeService.checkDeviceTypExistByDeviceTypeId(productReq.getDeviceTypeId());
        }

        String origModel = product.getModel();

        product.setProductName(productReq.getProductName());
        product.setModel(productReq.getModel());
        product.setRemark(productReq.getRemark());
        product.setDeviceTypeId(productReq.getDeviceTypeId());
        product.setUpdateTime(new Date());
        product.setTransmissionMode(productReq.getTransmissionMode());
        product.setCommunicationMode(productReq.getCommunicationMode());
        //根据技术方案判断是否是直连设备
        if (productReq.getCommunicationMode()!=null) {
            product.setIsDirectDevice(TechnicalSchemeEnum.checkIsDirect(Long.valueOf(productReq.getCommunicationMode())));
        }
        product.setConfigNetMode(null);
        product.setDevelopStatus((Integer) DevelopStatusEnum.OPERATING.getValue());//开发中
        if (productReq.getTenantId() == null) {
            product.setTenantId(SaaSContextHolder.currentTenantId());
        } else {
            product.setTenantId(productReq.getTenantId());
        }
        product.setEnterpriseDevelopId(productReq.getEnterpriseDevelopId());
        product.setIcon(productReq.getIcon());
        product.setIsKit(productReq.getIsKit());
        product.setServiceGooAuditStatus(productReq.getServiceGooAuditStatus());
        product.setServiceAlxAuditStatus(productReq.getServiceAlxAuditStatus());
        super.updateById(product);
        //remove cache product
        ProductServiceCoreUtils.removeCacheProduct(product.getId(), origModel);
        return product.getId();
    }


    @Override
    public Long updateProductBaseInfo(AddProductReq productReq) {
        Product product = super.selectById(productReq.getId());
        String origModel = product.getModel();
        product.setProductName(productReq.getProductName());
        product.setUpdateTime(new Date());
        product.setTenantId(productReq.getTenantId());
        product.setIcon(productReq.getIcon());
        super.updateById(product);
        ProductServiceCoreUtils.removeCacheProduct(product.getId(), origModel);
        return product.getId();
    }

    @Override
    public Page<ProductResp> getProduct(ProductOtaReq productOtaReq) {
        if (null == productOtaReq.getTenantId()) {
            throw new BusinessException(ProductExceptionEnum.PARAM_IS_ERROR, " tenant id is null");
        }
        com.baomidou.mybatisplus.plugins.Page<ProductResp> page = new com.baomidou.mybatisplus.plugins.Page<ProductResp>(CommonUtil.getPageNum(productOtaReq), CommonUtil.getPageSize(productOtaReq));
        List<ProductResp> productRespList = null;
        try {
            productRespList = productMapper.findProductBytenantId(page, productOtaReq.getTenantId(), productOtaReq.getProductName(), productOtaReq.getModel());
        } catch (Exception e) {
            LOGGER.error("getProduct error", e);
            throw new BusinessException(OtaExceptionEnum.QUERY_ERROR, e);
        }
        page.setRecords(productRespList);

        com.iot.common.helper.Page<ProductResp> myPage = new com.iot.common.helper.Page<ProductResp>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page, myPage);
        return myPage;
    }

    @Override
    public void addProductPublish(ProductPublishHistory req) {
        productMapper.addProductPublish(req);
    }

    @Override
    public List<ProductPublishHistory> getProductPublishHis(Long productId, Long tenantId) {
        return productMapper.getProductPublishHis(productId, tenantId);
    }

    /**
     * @return
     * @despriction：产品确认发布
     * @author yeshiyuan
     * @created 2018/9/12 14:51
     */
    @Transactional
    @Override
    public int confirmRelease(ProductConfirmReleaseReq req) {
        int i = productMapper.confirmRelease(req, DevelopStatusEnum.OPERATING.getValueInt(), DevelopStatusEnum.RELEASE.getValueInt());
        ProductReviewRecord reviewRecord = new ProductReviewRecord(req.getTenantId(), req.getProductId(), new Date(),
                req.getRemark(), new Date(), req.getUserId(), ProductReviewProcessStatusEnum.WAIT_AUDIT.getValue());
        productReviewRecordMapper.insert(reviewRecord);
        return i;
    }

    @Override
    public List<Product> findListByProductIds(List<Long> productIds) {
        List<Product> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(productIds)) {
            return resultDataList;
        }
        resultDataList = super.selectBatchIds(productIds);
        return resultDataList;
    }

    @Override
    public Product getProductByProductId(Long productId) {
        return super.selectById(productId);
    }

    @Override
    public Product getProductByProductModel(String productModel) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("model", productModel);
        return super.selectOne(wrapper);
    }

    @Override
    public List<Product> findListByProductModels(List<String> productModels) {
        if (CollectionUtils.isEmpty(productModels)) {
            return Lists.newArrayList();
        }
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("model", productModels);
        return super.selectList(wrapper);
    }

    /**
     * @return
     * @despriction：查询产品审核列表（boss使用）
     * @author yeshiyuan
     * @created 2018/10/24 14:36
     */
    @Override
    public Page<ProductAuditListResp> queryProductAuditList(ProductAuditPageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<ProductAuditListResp> page = new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<ProductAuditListResp> productRespList = productMapper.queryProductAuditList(page, pageReq);
        if (!productRespList.isEmpty()) {
            //查询申请时间
            List<Long> productIds = productRespList.stream().map(ProductAuditListResp::getProductId).collect(Collectors.toList());
            /*List<ProductReviewRecord> applyTimeList = productReviewRecordMapper.queryApplyTimeByProductIds(productIds);
            Map<Long, Date> applyTimeMap = applyTimeList.stream().collect(Collectors.toMap(ProductReviewRecord::getProductId, a->a.getOperateTime()));
            productRespList.forEach(o->{
                o.setApplyTime(applyTimeMap.get(o.getProductId()));
            });*/
            if (pageReq.getType() != 0) {
                if (!productRespList.isEmpty()) {
                    List<ProductReviewRecord> reviewRecords = productReviewRecordMapper.queryUserIdByProductIds(productIds);
                    Map<Long, Long> map = reviewRecords.stream().collect(Collectors.toMap(ProductReviewRecord::getProductId, a -> a.getCreateBy()));
                    productRespList.forEach(o -> {
                        o.setOperateUserId(map.get(o.getProductId()));
                    });
                }
            }
        }
        page.setRecords(productRespList);
        com.iot.common.helper.Page<ProductAuditListResp> myPage = new com.iot.common.helper.Page<>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page, myPage);
        return myPage;
    }

    /**
     * 描述：查询语音服务审核审核列表（boss使用）
     *
     * @param pageReq
     * @return
     * @author 李帅
     * @created 2018年10月26日 上午11:32:24
     * @since
     */
    @Override
    public Page<ServiceAuditListResp> queryServiceAuditList(ServiceAuditPageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<ServiceAuditListResp> page = new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<ServiceAuditListResp> productRespList = productMapper.queryServiceAuditList(page, pageReq);
        if (!productRespList.isEmpty()) {
            //查询申请时间
            List<Long> productIds = productRespList.stream().map(ServiceAuditListResp::getProductId).collect(Collectors.toList());
            List<ServiceReviewRecord> applyTimeList = serviceReviewMapper.queryApplyTimeByProductIds(productIds);
            Map<Long, Date> applyTimeMap = applyTimeList.stream().collect(Collectors.toMap(ServiceReviewRecord::getProductId, a -> a.getOperateTime()));
            productRespList.forEach(o -> {
                o.setApplyTime(applyTimeMap.get(o.getProductId()));
            });
            if (pageReq.getType() != 0) {
                if (!productRespList.isEmpty()) {
                    List<ServiceReviewRecord> reviewRecords = serviceReviewMapper.queryUserIdByProductIds(productIds);
                    Map<Long, Long> map = reviewRecords.stream().collect(Collectors.toMap(ServiceReviewRecord::getProductId, a -> a.getCreateBy()));
                    productRespList.forEach(o -> {
                        o.setOperateUserId(map.get(o.getProductId()));
                    });
                }
            }
        }
        page.setRecords(productRespList);
        com.iot.common.helper.Page<ServiceAuditListResp> myPage = new com.iot.common.helper.Page<>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page, myPage);
        return myPage;
    }

    /**
     * @return
     * @despriction：修改审核状态
     * @author yeshiyuan
     * @created 2018/10/24 17:22
     */
    @Override
    public int updateAuditStatus(Long productId, Integer auditStatus, Integer developStatus, Date updateTime) {
        return productMapper.updateAuditStatus(productId, auditStatus, developStatus, updateTime);
    }

    /**
     * @return
     * @despriction：重开审核
     * @author yeshiyuan
     * @created 2018/10/25 17:06
     */
    @Transactional
    @Override
    public void reOpenAudit(ReopenAuditReq reopenAuditReq) {
        Product product = productMapper.selectById(reopenAuditReq.getProductId());
        if (product == null) {
            throw new BusinessException(ProductExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        if (product.getAuditStatus() == ProductAuditStatusEnum.WAIT_AUDIT.getValue()) {
            throw new BusinessException(ProductExceptionEnum.PARAM_IS_ERROR, "this product is waiting audit");
        }
        ProductReviewRecord reviewRecord = new ProductReviewRecord(product.getTenantId(), reopenAuditReq.getProductId(), reopenAuditReq.getOperateTime(),
                "", reopenAuditReq.getOperateTime(), reopenAuditReq.getUserId(), ProductReviewProcessStatusEnum.WAIT_AUDIT.getValue());
        productReviewRecordMapper.insert(reviewRecord);
        productMapper.updateAuditStatus(reopenAuditReq.getProductId(), ProductAuditStatusEnum.WAIT_AUDIT.getValue(), DevelopStatusEnum.RELEASE.getValueInt(), reopenAuditReq.getOperateTime());
        ProductServiceCoreUtils.removeCacheProduct(reopenAuditReq.getProductId(), product.getModel());
    }

    @Override
    public List<ProductMinimumSubsetResp> getProductListByTenantIdAndCommunicationMode(Long tenantId, Long communicationMode) {
        return productMapper.getProductListByTenantIdAndCommunicationMode(tenantId, communicationMode);
    }

    @Override
    public int updateServiceGooAndAlxAuditStatus(Long productId, int flag, Integer gooAuditStatus, Integer alxAuditStatus) {
        return productMapper.updateServiceGooAndAlxAuditStatus(productId, flag, gooAuditStatus, alxAuditStatus);
    }
    
    /**
     * 
     * 描述：查询套包产品
     * @author 李帅
     * @created 2018年12月10日 下午3:42:29
     * @since 
     * @param tenantId
     * @return
     */
    @Override
    public List<PackageProductResp> getPackageProducts(Long tenantId) {
    	List<PackageProductResp> packageProductResps =  new ArrayList<PackageProductResp>();
    	List<PackageProduct> packageProducts = productMapper.getPackageProducts(tenantId);
    	PackageProductResp packageProductResp = null;
    	for(PackageProduct packageProduct : packageProducts){
    		packageProductResp = new PackageProductResp();
    		BeanUtils.copyProperties(packageProduct, packageProductResp);
    		if(packageProduct.getDeviceCatalogId() != null && packageProduct.getDeviceCatalogId() == 1){
    			packageProductResp.setGateway(true);
    		}else{
    			packageProductResp.setGateway(false);
    		}
    		
    		packageProductResps.add(packageProductResp);
    	}
        return packageProductResps;
    }
    
    /**
     * 
     * 描述：查询网管子产品
     * @author 李帅
     * @created 2018年12月10日 下午5:23:30
     * @since 
     * @param tenantId
     * @param productId
     * @return
     */
    @Override
    public List<GatewayChildProductResp> getGatewayChildProducts(Long tenantId, Long productId) {
    	List<GatewayChildProductResp> packageProductResps =  new ArrayList<GatewayChildProductResp>();
    	List<PackageProduct> packageProducts = productMapper.getGatewayChildProducts(tenantId, productId);
    	GatewayChildProductResp gatewayChildProductResp = null;
    	for(PackageProduct packageProduct : packageProducts){
    		gatewayChildProductResp = new GatewayChildProductResp();
    		BeanUtils.copyProperties(packageProduct, gatewayChildProductResp);
    		packageProductResps.add(gatewayChildProductResp);
    	}
        return packageProductResps;
    }

    /**
     *@description 套包根据ids查询产品的名称
     *@author wucheng
     *@params [ids]
     *@create 2018/12/12 16:58
     *@return java.util.List<java.util.Map<java.lang.Long,java.lang.String>>
     */
    @Override
    public List<PackageProductNameResp> getProductByIds(List<Long> ids) {
        return productMapper.getProductByIds(ids);
    }
}
