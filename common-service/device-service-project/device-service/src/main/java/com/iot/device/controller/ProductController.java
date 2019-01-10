package com.iot.device.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.google.common.collect.Lists;
import com.iot.common.beans.BeanUtil;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.api.ProductApi;
import com.iot.device.business.DeviceTypeBusinessService;
import com.iot.device.business.ProductBusinessService;
import com.iot.device.core.service.DeviceTypeServiceCoreUtils;
import com.iot.device.core.service.ProductServiceCoreUtils;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.exception.DataPointExceptionEnum;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.exception.DeviceTypeExceptionEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.model.*;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.service.*;
import com.iot.device.service.core.ModuleCoreService;
import com.iot.device.vo.req.*;
import com.iot.device.vo.req.DataPointReq.SmartWraper;
import com.iot.device.vo.req.ota.ProductOtaReq;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.*;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import com.iot.device.vo.rsp.product.*;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.util.AssertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 前端控制器
 *
 * @author lucky
 * @since 2018-04-12
 */
@RestController
public class ProductController implements ProductApi {

	public static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductBusinessService productBusinessService;

	@Autowired
	private DeviceTypeBusinessService deviceTypeBusinessService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IProductDataPointService productDataPointService;

	@Autowired
	private IDataPointService dataPointService;

	@Autowired
	private ISmartDataPointService smartDataPointService;

	@Autowired
	private IDeviceTypeService deviceTypeService;

	@Autowired
	private DataSource source;

	@Autowired
	private IDevelopInfoService developInfoService;

	@Autowired
	private IProductToServiceModuleService productToServiceModuleService;

	@Autowired
	private FileApi fileApi;

	@Autowired
	private ModuleCoreService moduleCoreService;

	@Autowired
	private IDeviceCatalogService deviceCatalogService;

	@Autowired
	private ISmartDeviceTypeService smartDeviceTypeService;

	@Autowired
	private IProductConfigNetmodeService iProductConfigNetmodeService;

	@Autowired
	private GatewaySubDevRelationService gatewaySubDevRelationService;

	@Autowired
	private IDeviceService deviceService;

	@Autowired
	private AppApi appApi;

	/**
	 * override说明：
	 *
	 * @author chenxiaolin
	 */
	@Transactional
	public Long saveProduct(@RequestBody ProductReq product) {
		AssertUtils.notNull(product, "product.notnull");
		AssertUtils.notNull(product.getTenantId(), "tenantId.notnull");
		AssertUtils.notNull(product.getDeviceTypeId(), "deviceTypeId.notnull");
		AssertUtils.notEmpty(product.getCommunicationMode(), "communicationMode.notnull");
		AssertUtils.notEmpty(product.getConfigNetModes(), "configNetModes.notnull");
		AssertUtils.notEmpty(product.getModel(), "model.notnull");
		AssertUtils.notEmpty(product.getProductName(), "productName.notnull");
		AssertUtils.notEmpty(product.getTransmissionMode(), "transmissionMode.notnull");
		AssertUtils.notEmpty(product.getIsKit(), "isKit.notnull");
		AssertUtils.notEmpty(product.getIsDirectDevice(), "isDirectDevice.notnull");

		DeviceType deviceType =
				deviceTypeBusinessService.getDeviceType(product.getDeviceTypeId());
		if (deviceType == null) {
			throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_NOT_EXIST);
		}
		EntityWrapper<Product> eq = new EntityWrapper<>();
		eq.eq("model", product.getModel());
		if (productService.selectCount(eq) > 0) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_MODEL_EXIST);
		}
		Product dest = new Product();
		BeanUtils.copyProperties(product, dest);
		Date now = new Date();
		dest.setDevelopStatus((Integer) DevelopStatusEnum.OPERATING.getValue());//开发中
		dest.setCreateTime(now);
		dest.setUpdateTime(now);

		productService.insert(dest);

		List<ProductConfigNetmodeReq> list = new ArrayList<>();
		if (StringUtil.isNotEmpty(product.getConfigNetModes())) {
			String[] result = product.getConfigNetModes().split(",");
			for (String strings : result) {
				ProductConfigNetmodeReq productConfigNetmodeReq = new ProductConfigNetmodeReq();
				productConfigNetmodeReq.setName(strings);
				productConfigNetmodeReq.setProductId(dest.getId());
				productConfigNetmodeReq.setCreateBy(dest.getCreateBy());
				productConfigNetmodeReq.setCreateTime(new Date());
				productConfigNetmodeReq.setUpdateBy(dest.getCreateBy());
				productConfigNetmodeReq.setUpdateTime(new Date());
				list.add(productConfigNetmodeReq);
			}
		}
		iProductConfigNetmodeService.insertMore(list);

		return dest.getId();
	}

	/**
	 * override说明：
	 *
	 * @author chenxiaolin
	 */
	@Transactional
	public boolean deleteProductById(@RequestParam(value = "id") Long id) {
		AssertUtils.notNull(id, "productId.notnull");
		//产品横向越权处理,boss级租户，或者本产品租户才允许删除
		Long tenantId = SaaSContextHolder.currentTenantId();
		if (tenantId == null) {
			throw new BusinessException(ExceptionEnum.TENANT_ID_IS_NULL);
		}
		Product product = productBusinessService.getProduct(id);
		if (product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		Long productTenantId = product.getTenantId();
		if (SystemConstants.BOSS_TENANT.equals(tenantId) || tenantId.equals(productTenantId)) {
			//检查产品是否被引用
			EntityWrapper delQ2 = new EntityWrapper<>();
			delQ2.eq("product_id", id);
			Integer devCount = deviceService.selectCount(delQ2);
			if(devCount > 0 ){
				throw new BusinessException(ProductExceptionEnum.PRODUCT_IS_USED_PRODUCT);
			}
			productService.deleteById(product.getId());
			// remove cache product
			ProductServiceCoreUtils.removeCacheProduct(product.getId(), product.getModel());

			return productDataPointService.delete(delQ2);
		} else {
			throw new BusinessException(ProductExceptionEnum.ACCESS_DENIED_EXCEPTION);//权限不足
		}

	}

	public ProductResp getProductById(@RequestParam("id") Long id) {
		AssertUtils.notNull(id, "productId.notnull");
		ProductResp productResp = null;
		Product product = ProductServiceCoreUtils.getProductByProductId(id);
        if (product == null) {
            return productResp;
        }
//		Long tenantId = SaaSContextHolder.currentTenantId();
//		if (tenantId == null) {
//			throw new BusinessException(ExceptionEnum.TENANT_ID_IS_NULL);
//		}
//		if (!SystemConstants.BOSS_TENANT.equals(tenantId) && !tenantId.equals(product.getTenantId())) {
//			throw new BusinessException(ProductExceptionEnum.ACCESS_DENIED_EXCEPTION);
//		}

        productResp = new ProductResp();
        BeanCopyUtils.copyProduct(product, productResp);
		productResp.setAuditStatus(product.getAuditStatus());
        String imageFileID = null;
        if (!StringUtils.isEmpty(product.getIcon())) {
            imageFileID = product.getIcon();
        }
        Long deviceTypeId = product.getDeviceTypeId();
        if (deviceTypeId != null) {
            DeviceType dt = DeviceTypeServiceCoreUtils.getDeviceTypeByDeviceTypeId(deviceTypeId);
            if (dt != null) {
            	Long catalogId = dt.getDeviceCatalogId();
				DeviceCatalog catalog = deviceCatalogService.selectById(catalogId);
                productResp.setWhetherSoc(dt.getWhetherSoc());
                productResp.setCatalogName(catalog != null ? catalog.getName() : null);
				productResp.setCatalogId(catalog != null ? catalog.getId() : null);
				productResp.setCatalogOrder(catalog.getOrder());
                productResp.setDeviceTypeName(dt.getName());
                productResp.setDeviceType(dt.getType());
                productResp.setDeviceTypeIcon(dt.getImg());
                if (StringUtils.isEmpty(imageFileID) && (!StringUtils.isEmpty(dt.getImg()))) {
                    imageFileID = dt.getImg();
                }
				EntityWrapper<SmartDeviceType> wrapper = new EntityWrapper<>();
				wrapper.eq("device_type_id", dt.getId());
				List<SmartDeviceType> smartDeviceTypes = smartDeviceTypeService.selectList(wrapper);
				List<SmartDeviceTypeResp> smartDeviceTypeResps = Lists.newArrayList();
				if (!CollectionUtils.isEmpty(smartDeviceTypes)) {
					smartDeviceTypes.forEach(s -> {
						SmartDeviceTypeResp smartDeviceTypeResp = new SmartDeviceTypeResp();
						smartDeviceTypeResp.setSmart(s.getSmart());
						smartDeviceTypeResp.setSmartType(s.getSmartType());
						smartDeviceTypeResps.add(smartDeviceTypeResp);
					});
				}
				productResp.setSmartDeviceTypeResps(smartDeviceTypeResps);
            }
        }
        if (!StringUtils.isEmpty(imageFileID)) {
            try {
                FileDto fileDto = fileApi.getGetUrl(imageFileID);
                if (fileDto != null) {
                    productResp.setDefaultIcon(fileDto.getPresignedUrl());
                }
            } catch (Exception e) {
            }
        }
		List<ProductConfigNetmodeRsp> list = iProductConfigNetmodeService.listByProductId(id);
        List result = new ArrayList();
        list.forEach(m->{
        	result.add(m.getName());
		});
		productResp.setConfigNetModes(result);
        return productResp;
	}

	public ProductResp getSomeProductPropertyById(@RequestParam("id") Long id) {
		AssertUtils.notNull(id, "productId.notnull");
		ProductResp productResp = null;
		Product product = ProductServiceCoreUtils.getProductByProductId(id);

		if (product != null){
			productResp = new ProductResp();
			try{
				//cp
				BeanCopyUtils.copyProduct(product, productResp);
			} catch (Exception e) {
				LOGGER.info("getProductById-error.{}", e);
				throw new BusinessException(ProductExceptionEnum.PRODUCT_GET_ERROR);
			}

			List<ProductConfigNetmodeRsp> list = iProductConfigNetmodeService.listByProductId(product.getId());
			List result = new ArrayList();
			list.forEach(m->{
				result.add(m.getName());
			});
			productResp.setConfigNetModes(result);

		}
		return productResp;
	}

	public ProductInfoResp getProductInfoByProductId(@RequestParam("productId") Long productId) {

		Product product = ProductServiceCoreUtils.getProductByProductId(productId);
		if (product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		DeviceType deviceType =
				DeviceTypeServiceCoreUtils.getDeviceTypeByDeviceTypeId(product.getDeviceTypeId());
		String devType = null;
		if (deviceType != null) {
			devType = deviceType.getName();
		}
		List<DataPointResp> dataPointList = this.findDataPointListByProductId(productId);

		ProductInfoResp resultData = new ProductInfoResp();
		resultData.setProductName(product.getProductName());
		resultData.setDisplayName(product.getProductName());
		resultData.setModel(product.getModel());
		resultData.setDevType(devType);
		resultData.setNetworkType(product.getCommunicationMode());
		resultData.setConfigNetMode(product.getConfigNetMode());
		resultData.setAttrs(dataPointList);

		return resultData;
	}

	@Override
	public ProductResp getProductByModel(@RequestParam(value = "model") String model) {

		AssertUtils.notEmpty(model, "productModel.notnull");
		ProductResp productResp = null;

		Product product = ProductServiceCoreUtils.getProductByProductModel(model.toLowerCase());
		if (product != null) {
			productResp = new ProductResp();
			// cp
			LOGGER.info("product.getDeviceTypeId(): " + product.getDeviceTypeId());
			BeanCopyUtils.copyProduct(product, productResp);
			LOGGER.info("productResp.getDeviceTypeId(): " + productResp.getDeviceTypeId());

			List<ProductConfigNetmodeRsp> list = iProductConfigNetmodeService.listByProductId(product.getId());
			List result = new ArrayList();
			list.forEach(m->{
				result.add(m.getName());
			});
			productResp.setConfigNetModes(result);
		}
		return productResp;
	}

	@Transactional
	public boolean updateProduct(@RequestBody ProductReq product) {
		Long userId = product.getCreateBy();
		if (product != null) {
			Long productId = product.getId();
			Product exit = this.checkProduct(productId);
			List<Long> datapointIds = product.getDatapointIds();
			List<DataPointReq> customs = product.getCustomDataPoints();
			List<ProductDataPoint> pds = new ArrayList<>();
			if (productId == null || productId <= 0) {
				throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
			}

			if (exit != null) {
				exit.setCommunicationMode(product.getCommunicationMode());
				exit.setDeviceTypeId(product.getDeviceTypeId());
				exit.setModel(product.getModel());
				exit.setProductName(product.getProductName());
				exit.setTransmissionMode(product.getTransmissionMode());
				exit.setUpdateTime(new Date());
				if (!productService.updateById(exit)) {
					throw new BusinessException(ProductExceptionEnum.PRODUCT_UPDATE_ERROR);
				}

				iProductConfigNetmodeService.deleteById(product.getId());

				List<ProductConfigNetmodeReq> list = new ArrayList<>();
				if (StringUtil.isNotEmpty(product.getConfigNetModes())) {
					String[] result = product.getConfigNetModes().split(",");
					for (String strings : result) {
						ProductConfigNetmodeReq productConfigNetmodeReq = new ProductConfigNetmodeReq();
						productConfigNetmodeReq.setName(strings);
						productConfigNetmodeReq.setProductId(product.getId());
						productConfigNetmodeReq.setCreateBy(product.getCreateBy());
						productConfigNetmodeReq.setCreateTime(new Date());
						productConfigNetmodeReq.setUpdateBy(product.getUpdateBy());
						productConfigNetmodeReq.setUpdateTime(new Date());
						list.add(productConfigNetmodeReq);
					}
				}
				iProductConfigNetmodeService.insertMore(list);



				// remove product cache
				ProductServiceCoreUtils.removeCacheProduct(exit.getId(), exit.getModel());

				this.checkDataPointSizeByEq(
						datapointIds.size(),
						dataPointService,
						new EntityWrapper<DataPoint>().in("id", datapointIds).eq("is_custom", 0));

				if (customs != null) {
					if (!saveDataPoint(productId, (ArrayList<DataPointReq>) customs)) {
						throw new BusinessException(DataPointExceptionEnum.POINT_ADD_ERROR);
					}
					customs.forEach(
							(dr) -> {
								datapointIds.add(dr.getId());
							});
				}

				datapointIds.forEach(
						(id) -> {
							ProductDataPoint pd = new ProductDataPoint();
							pd.setProductId(product.getId());
							pd.setDataPointId(id);
							pd.setCreateBy(userId);
							pd.setCreateTime(new Date());
							pds.add(pd);
						});

				EntityWrapper<ProductDataPoint> eq = new EntityWrapper<>();
				eq.eq("product_id", productId);
				if (!productDataPointService.delete(eq)) {
					throw new BusinessException(ProductExceptionEnum.PRODUCT_UPDATE_ERROR);
				}
				if (!CollectionUtils.isEmpty(pds)) {
					return productDataPointService.insertBatch(pds);
				}
				return true;
			}
		} else {
			LOGGER.info("update error by product null");
		}
		return false;
	}

	public List<ProductResp> findProductListByDeviceTypeId(
			@RequestParam(value = "id") Long deviceTypeId) {
		AssertUtils.notNull(deviceTypeId, "deviceTypeId.notnull");
		Long tenantId = SaaSContextHolder.currentTenantId();
		List<ProductResp> productRespList = null;
		EntityWrapper<Product> wrapper = new EntityWrapper<>();
		wrapper.eq("device_type_id", deviceTypeId);
		wrapper.eq("tenant_id", tenantId);
		List<Product> productList = productService.selectList(wrapper);
		if (!CollectionUtils.isEmpty(productList)) {
			productRespList = new ArrayList<>();
			for (Product orig : productList) {
				ProductResp dest = new ProductResp();
				try {
					PropertyUtils.copyProperties(dest, orig);
					productRespList.add(dest);
				} catch (Exception e) {
					LOGGER.info("findProductListByDeviceTypeId error", e);
					throw new BusinessException(ProductExceptionEnum.PRODUCT_GET_ERROR);
				}
			}
		}
		return productRespList;
	}

	public ProductResp getProductByDeviceTypeIdAndProperties(
			@RequestParam("deviceTypeId") Long deviceTypeId,
			@RequestBody List<Map<String, Object>> propertyList) {
		return productService.getProductByDeviceTypeIdAndProperties(deviceTypeId, propertyList);
	}

	public List<DataPointResp> findDataPointListByProductId(
			@RequestParam(value = "productId") Long productId) {
		return productService.findDataPointListByProductId(productId);
	}

	@Override
	public List<ProductResp> findProductListByTenantId(@RequestBody ProductReq product) {
		List<ProductResp> productRespList = productService.findProductListByTenantId(product);
		List productIdList = new ArrayList();
		productRespList.forEach(m->{
			List<GatewaySubDevRelationResp> list = gatewaySubDevRelationService.getGatewaySubDevByParDevId(m.getId(),product.getTenantId());
			list.forEach(n->{
				productIdList.add(n.getSubDevId());
			});
		});
		List<Product> result = productService.findListByProductIds(productIdList);
		result.forEach(r->{
			ProductResp productResp = new ProductResp();
			BeanUtil.copyProperties(r,productResp);
			productRespList.add(productResp);
		});
		// 去掉重复数据
		List<ProductResp> finalResult = productRespList.stream().collect(
				Collectors.collectingAndThen(
						Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProductResp::getId))), ArrayList::new));
		return finalResult;
	}

	@Override
	public List<ProductResp> findPrimaryProductListByTenantId(@RequestBody ProductReq product) {
		List<ProductResp> productRespList = productService.findProductListByTenantId(product);
		return productRespList;
	}

	/**
	 * 
	 * 描述：根据租户id获取直连产品列表
	 * @author 李帅
	 * @created 2018年11月8日 上午11:26:18
	 * @since 
	 * @param product
	 * @return
	 */
	@Override
	public List<ProductResp> findDirectProductListByTenantId(@RequestBody ProductReq product) {
		List<ProductResp> productRespList = productService.findDirectProductListByTenantId(product);
		return productRespList;
	}
	
	@Override
	public List<ProductResp> findAllDirectProductList(){
		List<ProductResp> productRespList = productService.findAllDirectProductList();
		return productRespList;
	}

	@Override
	public ProductResp getProductByModelAndTenantId(
			@RequestParam("productModel") String productModel, @RequestParam("tenantId") Long tenantId) {

		ProductResp dest = null;
		EntityWrapper<Product> wrapper = new EntityWrapper<>();
		wrapper.eq("model", productModel);
		wrapper.eq("tenant_id", tenantId);
		List<Product> productList = productService.selectList(wrapper);
		if (!CollectionUtils.isEmpty(productList)) {
			Product orig = productList.get(0);
			dest = new ProductResp();
			try {
				PropertyUtils.copyProperties(dest, orig);
			} catch (Exception e) {
				LOGGER.info("findProductListByDeviceTypeId error", e);
				throw new BusinessException(ProductExceptionEnum.PRODUCT_GET_ERROR);
			}
		}
		return dest;
	}
	//保存自定义功能点
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveDataPoint(Long productId, ArrayList<DataPointReq> req) {
		this.checkProduct(productId);
		List<DataPoint> updates = new ArrayList<>();
		List<Long> updateDataPointIds = new ArrayList<>();
		List<SmartDataPoint> addss = new ArrayList<SmartDataPoint>();
		List<SmartDataPoint> upOrDels = new ArrayList<SmartDataPoint>();
		List<SmartDataPoint> ups = new ArrayList<SmartDataPoint>();
        List<Long> dels = new ArrayList<Long>();
		req.forEach(
				(dpq) -> {
					DataPoint dp = new DataPoint();
					BeanUtils.copyProperties(dpq, dp);
					dp.setIsCustom(1);
					if (dpq.getId() == null) {
						dp.setCreateTime(new Date());
						dp.setCreateBy(dpq.getCreateBy());

						if (!dataPointService.insert(dp)) {
							throw new BusinessException(ProductExceptionEnum.PRODUCT_UPDATE_ERROR);
						}
						dpq.setId(dp.getId());
						convert(addss, dpq, true);
						if (!CollectionUtils.isEmpty(addss) && !smartDataPointService.insertBatch(addss)){
								throw new BusinessException(ProductExceptionEnum.PRODUCT_UPDATE_ERROR);
						}
						addss.clear();
					} else {
						updates.add(dp);
						updateDataPointIds.add(dp.getId());
						convert(upOrDels, dpq, false);
					}
				});

		if (!updates.isEmpty()) {

			this.checkDataPointSizeByEq(
					updates.size(),
					productDataPointService,
					new EntityWrapper<ProductDataPoint>()
							.eq("product_id", productId)
							.in("data_point_id", updateDataPointIds));

			// 检测是否含有非自定义功能点
			this.checkDataPointSizeByEq(
					updates.size(),
					dataPointService,
					new EntityWrapper<DataPoint>().in("id", updateDataPointIds).eq("is_custom", 1));

//			if (!dataPointService.updateBatchById(updates)
////					|| !smartDataPointService.updateBatchByIdAndDataPointId(upss)) {
////				throw new BusinessException(ProductExceptionEnum.PRODUCT_UPDATE_ERROR);
////			}
			upOrDels.forEach(up->{
				if(up.getId() == null){
					addss.add(up);
				}else if(up.getSmartCode() != null && !up.getSmartCode().isEmpty()){
					ups.add(up);
                }else {
                    dels.add(up.getId());
                }
            });
			if(!dataPointService.updateBatchById(updates)
					|| !smartDataPointService.insertBatchDataPoint(addss)
                    || !smartDataPointService.updateBatchByIdAndDataPointId(ups)
                    || !smartDataPointService.delBatchDataPoint(dels)){
                throw new BusinessException(ProductExceptionEnum.PRODUCT_UPDATE_ERROR);
            }
		}

		return true;
	}

	private void convert(List<SmartDataPoint> ss, DataPointReq dpq, boolean add) {
		Long userId = dpq.getCreateBy();
		List<SmartWraper> smart = dpq.getSmart();
		if (smart != null) {
			smart.forEach(
					(s) -> {
						SmartDataPoint sd = new SmartDataPoint();
						sd.setSmart(s.getSmart());
						sd.setId(s.getId());
						sd.setUpdateBy(userId);
						sd.setUpdateTime(new Date());
						sd.setDataPointId(dpq.getId());
						sd.setPropertyCode(dpq.getPropertyCode());
						sd.setSmartCode(s.getCode());
						sd.setTenantId(dpq.getTenantId());
						if (add) {
							sd.setId(null);
							sd.setDataPointId(dpq.getId());
							sd.setCreateBy(userId);
							sd.setCreateTime(new Date());
						}
						ss.add(sd);
					});
		}
	}

	private Product checkProduct(Long productId) {
		Long tenantId = SaaSContextHolder.currentTenantId();
		Product exitPro = ProductServiceCoreUtils.getProductByProductId(productId);
		if (exitPro == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		if (SystemConstants.BOSS_TENANT == tenantId || tenantId == exitPro.getTenantId()){
			return exitPro;
		}else{
			throw new BusinessException(ProductExceptionEnum.ACCESS_DENIED_EXCEPTION);//权限不足
		}
	}

	private void checkDataPointSizeByEq(int size, IService service, Wrapper wrapper) {
		if (size < 1 || wrapper == null) {
			return;
		}
		try {
			@SuppressWarnings("unchecked")
			ResultSet set =
					source
							.getConnection()
							.createStatement()
							.executeQuery("select count(1) from product_data_point");
			if (set.next()) {
				System.out.println(set.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int exitCount = service.selectCount(wrapper);
		if (size != exitCount) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_UPDATE_POINT_ILLEGAL);
		}
	}

	@Override
	public Page<ProductResp> findProductPageByTenantId(@RequestBody GetProductReq req) {
		int pageNum = req.getPageNum();
		int pageSize = req.getPageSize();
		if (pageNum < 1 || pageSize <= 0 || pageSize > 100) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_PAGE_ILLEGAL);
		}
		Page<ProductResp> res = productService.findProductPageByTenantId(req);
		return res;
	}

	public Page<ProductPageResp> findProductPage(@RequestBody ProductPageReq pageReq) {

		return productService.findProductPage(pageReq);
	}

	@Override
	public ProductResp findProductByDeviceTypeId(Long deviceTypeId) {
		AssertUtils.notNull(deviceTypeId, "deviceTypeId.notnull");
		ProductResp productResp = null;
		productResp = productService.findProductByDeviceTypeId(deviceTypeId);
		return productResp;
	}

	public Long addProduct(@RequestBody AddProductReq productReq) {
		AssertUtils.notNull(productReq, "product.notnull");
		return productService.addProduct(productReq);
	}

	public Long updateProduct(@RequestBody AddProductReq productReq) {
		AssertUtils.notNull(productReq, "product.notnull");
		AssertUtils.notNull(productReq.getId(), "productId.notnull");
		AssertUtils.notNull(productReq.getTenantId(), "product.tenantId.notnull");
		return productService.updateProduct(productReq);
	}


	@Override
	public Long updateProductBaseInfo(@RequestBody AddProductReq productReq) {
		return productService.updateProductBaseInfo(productReq);
	}

	@Override
	public void updateDevelopInfoByProductId(
			@RequestParam("enterpriseDevelopId") Long enterpriseDevelopId,
			@RequestParam("productId") Long productId) {
		AssertUtils.notNull(enterpriseDevelopId, "enterpriseDevelopId.notnull");
		AssertUtils.notNull(productId, "productId.notnull");
		DevelopInfo developInfo = developInfoService.selectById(enterpriseDevelopId);
		if (developInfo == null) {
			throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
		}
		Product product = productService.selectById(productId);
		if (product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		product.setEnterpriseDevelopId(enterpriseDevelopId);

		productService.updateById(product);
	}

	@Transactional
	public void delProductByProductId(@RequestParam("productId") Long productId) {
		AssertUtils.notNull(productId, "productId.notnull");
		Long tenantId = SaaSContextHolder.currentTenantId();
		Product product = ProductServiceCoreUtils.getProductByProductId(productId);
		if (product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
//		if (ProductAuditStatusEnum.AUDIT_SUCCESS.getValue() == product.getAuditStatus()) {
//			throw new BusinessException(ProductExceptionEnum.PRODUCT_AUDITED);
//		}
		if (!tenantId.equals(product.getTenantId())) {
			throw new BusinessException(ProductExceptionEnum.ACCESS_DENIED_EXCEPTION);
		}

		// 判断产品是否在APP中
        Integer appNum = appApi.countAppProductByproductId(productId);
        if (appNum > 0) {
            LOGGER.warn("delProductByProductId, product used by app, num={}", appNum);
			throw new BusinessException(ProductExceptionEnum.PRODUCT_IS_USED_PRODUCT);
		}

		// 判断产品是否在网关下
		List<Long> parentDevIds = gatewaySubDevRelationService.parentProductIds(productId, tenantId);
		if (!CollectionUtils.isEmpty(parentDevIds)) {
            LOGGER.warn("delProductByProductId, product used by gateway {}", JSON.toJSONString(parentDevIds));
			throw new BusinessException(ProductExceptionEnum.PRODUCT_IS_USED_PRODUCT);
		}

		EntityWrapper<ProductDataPoint> delQ2 = new EntityWrapper<>();
		delQ2.eq("product_id", productId);
		productDataPointService.delete(delQ2);

		// 删除功能
		productToServiceModuleService.delServicesByProductId(productId);

		productService.deleteById(product.getId());
		// remove cache product
		ProductServiceCoreUtils.removeCacheProduct(product.getId(), product.getModel());
	}

	public Long copyProductByProductId(@RequestParam("productId") Long productId) {
		AssertUtils.notNull(productId, "productId.notnull");
		// Long tenantId = SaaSContextHolder.currentTenantId();
		Product product = ProductServiceCoreUtils.getProductByProductId(productId);
		if (product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		Product tempProduct = new Product();
		BeanUtils.copyProperties(product, tempProduct);
		tempProduct.setId(null);
		tempProduct.setProductName(product.getProductName() + "01");
		tempProduct.setModel(product.getModel() + "01");
		tempProduct.setAuditStatus(null);
		//复制去掉审核
		tempProduct.setDevelopStatus(0);
		tempProduct.setCreateTime(new Date());
		tempProduct.setUpdateTime(new Date());
		productService.insert(tempProduct);
		Long targetProductId = tempProduct.getId();
		moduleCoreService.copyModule(productId, targetProductId);
		return targetProductId;
	}

	@ResponseBody
	@Override
	public Page<ProductResp> getProduct(@RequestBody ProductOtaReq productOtaReq) {
		return productService.getProduct(productOtaReq);
	}

	@Override
	public Integer getStepByProductId(@RequestParam("productId") Long productId) {
		AssertUtils.notNull(productId, "productId.notnull");
		Product product = ProductServiceCoreUtils.getProductByProductId(productId);
		if (product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		//TODO step 获取
		Integer step = 0;
		return step;
//		if (product.getStep() == null) {
//			ProductServiceCoreUtils.removeCacheProduct(productId, product.getModel());//兼容历史缓存数据
//			product.setStep(0);
//			productService.updateById(product);
//			return 0;
//		} else {
//			return product.getStep();
//		}
	}

	@Override
	public void addProductStep(@RequestBody ProductStepVoReq stepVoReq) {
		AssertUtils.notNull(stepVoReq.getProductId(), "productId.notnull");
		AssertUtils.notNull(stepVoReq.getStep(), "step.notnull");
		Product product = ProductServiceCoreUtils.getProductByProductId(stepVoReq.getProductId());
		if (product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		ProductServiceCoreUtils.removeCacheProduct(product.getId(), product.getModel());
//		product.setStep(stepVoReq.getStep());
		productService.updateById(product);

	}

	/**
	 * @despriction：产品确认发布
	 * @author  yeshiyuan
	 * @created 2018/9/12 14:51
	 * @return
	 */
	@Override
	public void confirmRelease(@RequestBody ProductConfirmReleaseReq req) {
		AssertUtils.notEmpty(req.getProductId(), "productId.notnull");
		AssertUtils.notEmpty(req.getModel(), "product.model.notnull");
		int i = productService.confirmRelease(req);
		if (i<1){
			throw new BusinessException(ProductExceptionEnum.PRODUCT_RELEASE_FAIL);
		}
		ProductServiceCoreUtils.removeCacheProduct(req.getProductId(), req.getOldModel());
	}
	
	@Override
	public void addProductPublish(@RequestBody ProductPublishHistoryReq req) {
		AssertUtils.notNull(req.getTenantId(), "tenantId.notnull");
		AssertUtils.notNull(req.getProductId(), "productId.notnull");
		AssertUtils.notNull(req.getPublishStatus(), "publishStatus.notnull");
		AssertUtils.notNull(req.getFailureReason(), "failureReason.notnull");
		AssertUtils.notNull(req.getCreateBy(), "createBy.notnull");
		
		ProductResp product = getProductById(req.getProductId());
		if(product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		
		ProductPublishHistory productPublishHistory = new ProductPublishHistory();
		BeanUtils.copyProperties(req, productPublishHistory);
		productService.addProductPublish(productPublishHistory);
	}
	
	@Override
	public List<ProductPublishHistoryResp> getProductPublishHis(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId) {
		AssertUtils.notNull(tenantId, "tenantId.notnull");
		AssertUtils.notNull(productId, "productId.notnull");
		List<ProductPublishHistoryResp> productPublishHistoryResps = null;
		Product product = ProductServiceCoreUtils.getProductByProductId(productId);
		List<ProductPublishHistory> productPublishHistorys = productService.getProductPublishHis(productId, tenantId);
		if(productPublishHistorys != null && productPublishHistorys.size() > 0) {
			productPublishHistoryResps = new ArrayList<ProductPublishHistoryResp>();
			ProductPublishHistoryResp productPublishHistoryResp = null;
			for(ProductPublishHistory productPublishHistory : productPublishHistorys) {
				productPublishHistoryResp = new ProductPublishHistoryResp();
				BeanUtils.copyProperties(productPublishHistory, productPublishHistoryResp);
				productPublishHistoryResp.setProductName(product.getProductName());
				productPublishHistoryResps.add(productPublishHistoryResp);
			}
		}
		return productPublishHistoryResps;
	}

	/**
	 * @despriction：查询产品审核列表（boss使用）
	 * @author  yeshiyuan
	 * @created 2018/10/24 14:36
	 * @return
	 */
	@Override
	public Page<ProductAuditListResp> queryProductAuditList(@RequestBody ProductAuditPageReq pageReq) {
		return productService.queryProductAuditList(pageReq);
	}

	/**
	 *
	 * 描述：查询语音服务审核审核列表（boss使用）
	 * @author 李帅
	 * @created 2018年10月26日 上午11:31:42
	 * @since
	 * @param pageReq
	 * @return
	 */
	@Override
	public Page<ServiceAuditListResp> queryServiceAuditList(@RequestBody ServiceAuditPageReq pageReq) {
		return productService.queryServiceAuditList(pageReq);
	}


	/**
	 * @despriction：重开审核
	 * @author  yeshiyuan
	 * @created 2018/10/25 17:06
	 * @return
	 */
	@Override
	public void reOpenAudit(@RequestBody ReopenAuditReq reopenAuditReq) {
		productService.reOpenAudit(reopenAuditReq);
	}

	@Override
	public List<ProductMinimumSubsetResp> getProductListByTenantIdAndCommunicationMode(Long tenantId, Long communicationMode) {
		return productService.getProductListByTenantIdAndCommunicationMode(tenantId, communicationMode);
	}

	@Override
	public int updateServiceGooAndAlxAuditStatus(Long productId, int flag, Integer gooAuditStatus, Integer alxAuditStatus) {
		return productService.updateServiceGooAndAlxAuditStatus(productId, flag, gooAuditStatus, alxAuditStatus);
	}
	
	/**
	 * 
	 * 描述：查询套包产品
	 * @author 李帅
	 * @created 2018年12月10日 下午3:42:12
	 * @since 
	 * @param tenantId
	 * @return
	 */
	@Override
	public List<PackageProductResp> getPackageProducts(Long tenantId) {
		return productService.getPackageProducts(tenantId);
	}
	
	/**
	 * 
	 * 描述：查询网管子产品
	 * @author 李帅
	 * @created 2018年12月10日 下午5:22:55
	 * @since 
	 * @param tenantId
	 * @param productId
	 * @return
	 */
	@Override
	public List<GatewayChildProductResp> getGatewayChildProducts(Long tenantId, Long productId) {
		return productService.getGatewayChildProducts(tenantId, productId);
	}


	/**
	 *@description 套包根据ids查询产品的名称
	 *@author wucheng
	 *@params [ids]
	 *@create 2018/12/12 16:58
	 *@return java.util.List<java.util.Map<java.lang.Long,java.lang.String>>
	 */
	@Override
	public  List<PackageProductNameResp>  getProductByIds(@RequestParam("ids") List<Long> ids) {
		return productService.getProductByIds(ids);
	}
}
