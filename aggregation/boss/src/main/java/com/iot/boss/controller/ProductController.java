package com.iot.boss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.boss.util.DateJsonValueProcessor;
import com.iot.boss.util.fileExportUtil;
import com.iot.boss.vo.FileResp;
import com.iot.boss.vo.product.req.ProductExportReq;
import com.iot.boss.vo.product.resp.ProductExportResp;
import com.iot.boss.vo.product.resp.ProductModuleIftttResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.api.*;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.vo.req.AddProductReq;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.req.GetProductReq;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.rsp.*;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.file.api.FileApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.module.api.ModuleCoreApi;
import com.iot.shcs.module.vo.resp.GetProductModuleResp;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年5月11日 上午9:36:06
 * 修改人： chenxiaolin
 * 修改时间：2018年5月11日 上午9:36:06
 */
@RestController
@Api(description = "产品接口",value = "产品接口")
@RequestMapping("/api/product")
public class ProductController {
	
	private Logger log = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private ProductApi productApi;
	@Autowired
	private DataPointApi dataPointApi;
	@Autowired
	private TenantApi tenantApi;
	@Autowired
	private AppApi appApi;
	@Autowired
	private TechnicalRelateApi technicalRelateApi;
	@Autowired
	private ProductTimerApi productTimerApi;
	@Autowired
	private ServiceModuleApi serviceModuleApi;

	@Autowired
	private ServiceActionApi serviceActionApi;

	@Autowired
	private ServicePropertyApi servicePropertyApi;

	@Autowired
	private ServiceEventApi serviceEventApi;
	@Autowired
	private ProductToStyleApi productToStyleApi;
	@Autowired
	private FileApi fileApi;

	@Autowired
	private ModuleCoreApi moduleCoreApi;

	@Autowired
	private ProductReviewRecodApi productReviewRecodApi;

	@Autowired
	private ProductServiceInfoApi productServiceInfoApi;

	@Autowired
	private LangInfoTenantApi langInfoTenantApi;

	@Autowired
	private GoodsServiceApi goodsServiceApi;

	@ApiOperation("新增产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public CommonResponse<Long> save(@RequestBody ProductReq req) {
		log.debug("新增产品:{}", JSONObject.toJSON(req));
		req.setTenantId(req.getTenantId());
		req.setCreateBy(SaaSContextHolder.getCurrentUserId());
		req.setUpdateBy(SaaSContextHolder.getCurrentUserId());
		Long productId;
		CommonResponse<Long> result;
		if ((productId = productApi.saveProduct(req)) == 0) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		result = new CommonResponse<>(ResultMsg.SUCCESS, productId);
		return result;
	}
	
	@ApiOperation("删除产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	public CommonResponse<Boolean> delete(@PathVariable Long id) {
		log.debug("删除产品id:{}", id);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
		Integer appCount = appApi.countAppProductByproductId(id);
		if(appCount > 0){
			throw new BusinessException(ProductExceptionEnum.PRODUCT_IS_USED_PRODUCT);
		}
		if (!productApi.deleteProductById(id)) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		return result;
	}

	@ApiOperation("删除产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "deleteByPost/{id}", method = RequestMethod.POST)
	public CommonResponse<Boolean> deleteByPost(@PathVariable Long id) {
		log.debug("删除产品id:{}", id);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
		Integer appCount = appApi.countAppProductByproductId(id);
		if(appCount > 0){
			throw new BusinessException(ProductExceptionEnum.PRODUCT_IS_USED_PRODUCT);
		}
		if (!productApi.deleteProductById(id)) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		return result;
	}
	
	@ApiOperation("修改产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public CommonResponse<Boolean> updateProduct(@RequestBody AddProductReq req) {
		log.debug("更新产品:{}", JSONObject.toJSON(req));
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
		Long productId = productApi.updateProduct(req);
		if (null == productId) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		return result;
	}

    @ApiOperation("更新产品及其功能点")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "updateProduct", method = RequestMethod.POST)
    public CommonResponse<Boolean> updateProductAndDataPoints(@RequestBody ProductReq req) {
        log.debug("更新产品:{}", JSONObject.toJSON(req));
        CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
        if (!productApi.updateProduct(req)) {
            result = new CommonResponse<>(ResultMsg.FAIL);
        }
        return result;
    }
	
	@ApiOperation("查询产品功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "findDataPointListByProductId", method = RequestMethod.GET)
	public CommonResponse<Map<String,List<DataPointResp>>> findDataPointListByProductId(@RequestParam(required = true) Long productId) {
		log.debug("查询产品功能点-产品id:{}", productId);
		Map<String,List<DataPointResp>> resm = new HashMap<>();
		List<DataPointResp> res = productApi.findDataPointListByProductId(productId);
		List<DataPointResp> isCustom = new ArrayList<DataPointResp>();
		if (res == null) {
			res = new ArrayList<DataPointResp>();
		}
		for (int i = 0; i < res.size(); i++) {
			DataPointResp r = res.get(i);
			if (r.getIsCustom() != null && r.getIsCustom() == 1) {
				isCustom.add(r);
				res.remove(i);
				i--;
			}
		}
		resm.put("custom", isCustom);
		resm.put("unCustom", res);
		CommonResponse<Map<String,List<DataPointResp>>> result = new CommonResponse<>(ResultMsg.SUCCESS, resm);
		return result;
	}
	
	@ApiOperation("查询设备类型下的所有产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "findProductListByDeviceTypeId", method = RequestMethod.GET)
	public CommonResponse<List<ProductResp>> findProductListByDeviceTypeId(@RequestParam(required = true) Long deviceTypeId) {
		log.debug("查询设备类型下的所有产品-设备类型id:{}", deviceTypeId);
		CommonResponse<List<ProductResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, productApi.findProductListByDeviceTypeId(deviceTypeId));
		return result;
	}
	
	@ApiOperation("查询所有产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "findProductList", method = RequestMethod.GET)
	public CommonResponse<List<ProductResp>> findProductList() {
		Long tenantId = SaaSContextHolder.currentTenantId();
		log.debug("查询所有产品-tenantId:{}", tenantId);
		ProductReq req = new ProductReq();
		req.setTenantId(tenantId);
		CommonResponse<List<ProductResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, productApi.findProductListByTenantId(req));
		return result;
	}

	@ApiOperation("查询所有直连产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "findAllDirectProductList", method = RequestMethod.GET)
	public CommonResponse<List<ProductResp>> findAllDirectProductList() {
		CommonResponse<List<ProductResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, productApi.findAllDirectProductList());
		return result;
	}

	@ApiOperation("分页查询产品")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "findProductPage", method = RequestMethod.POST)
	public CommonResponse<Page<ProductResp>> findProductPage(@RequestBody GetProductReq req) {
		CommonResponse<Page<ProductResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, productApi.findProductPageByTenantId(req));
		return result;
	}
	
	@ApiOperation("批量删除自定义功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "deleteDataPoint/{productId}", method = RequestMethod.DELETE)
	public CommonResponse<Boolean> deleteDataPoint(@PathVariable("productId") Long productId, @RequestBody ArrayList<Long> ids) {
		log.debug("批量删除自定义功能点  product:{},ids:{}",productId, JSONArray.toJSON(ids));
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.deleteByIdsAndProduct(productId,ids));
		return result;
	}

	@ApiOperation("批量删除自定义功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "deleteDataPointByPost/{productId}", method = RequestMethod.POST)
	public CommonResponse<Boolean> deleteDataPointByPost(@PathVariable("productId") Long productId, @RequestBody ArrayList<Long> ids) {
		log.debug("批量删除自定义功能点  product:{},ids:{}",productId, JSONArray.toJSON(ids));
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.deleteByIdsAndProduct(productId,ids));
		return result;
	}
	
	@ApiOperation("保存自定义功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "saveDataPoint/{productId}", method = RequestMethod.POST)
	public CommonResponse<Boolean> saveDataPoint(@PathVariable("productId") Long productId, @RequestBody ArrayList<DataPointReq> reqs) {
		log.debug("保存自定义功能点  product:{},ids:{}");
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, productApi.saveDataPoint(productId, reqs));
		return result;
	}

	@ApiOperation("产品增加功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "addDataPoint", method = RequestMethod.POST)
	public CommonResponse<Boolean> addDataPoint(@RequestBody DeviceType2PointsReq req) {
		log.debug("新增一级类目:{}", JSONObject.toJSON(req));
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
//		if (!deviceTypeApi.addDataPoint(req)) {
//			result = new CommonResponse<>(ResultMsg.FAIL);
//		}
		return result;
	}
	
	@ApiOperation("通过产品id获取产品信息")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getProductById/{productId}", method = RequestMethod.GET)
	public CommonResponse<ProductResp> getProductById(@PathVariable(value = "productId", required = true) Long productId) {
		log.debug("通过产品id获取产品信息-产品id:{}", productId);
		ProductResp res = productApi.getProductById(productId);
		if(res !=null ){
			if (res.getTenantId() != null) {
				TenantInfoResp tir = tenantApi.getTenantById(res.getTenantId());
				if (tir != null) {
					res.setTenantCode(tir.getCode());
					res.setTenantName(tir.getName());
				}
			}
		}
		CommonResponse<ProductResp> result = new CommonResponse<>(ResultMsg.SUCCESS, res);
		return result;
	}
	
	@ApiOperation("通过产品model获取产品信息")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getProductByModel/{model}", method = RequestMethod.GET)
	public CommonResponse<ProductResp> getProductByModel(@PathVariable(value = "model", required = true) String model) {
		log.debug("通过产品model获取产品信息息-产品model:{}", model);
		CommonResponse<ProductResp> result = new CommonResponse<>(ResultMsg.SUCCESS, productApi.getProductByModel(model));
		return result;
	}

	/**
	 * @despriction：查找设备类型某种技术方案支持的配网方式
	 * @author  yeshiyuan
	 * @created 2018/12/11 16:10
	 */
	@ApiOperation("查找此设备类型某种技术方案支持的配网方式")
	@RequestMapping(value = "/getNetworkTypeByTechnicalId", method = RequestMethod.GET)
	public CommonResponse getNetworkTypeByTechnicalId(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam("technicalId") Long technicalId) {
		List<String> networkCodeList = new ArrayList<>();
		List<NetworkTypeResp> networkTypeResps = technicalRelateApi.deviceSupportNetwrokByTechnicalId(deviceTypeId, technicalId);
		if (networkTypeResps!=null) {
			networkCodeList = networkTypeResps.stream().map(NetworkTypeResp::getTypeCode).collect(Collectors.toList());
		}
		return CommonResponse.success(networkCodeList);
	}


	/**
	 * @despriction：查询产品选中的联动配置的模组
	 * @author  yeshiyuan
	 * @created 2018/10/23 15:10
	 * @return
	 */
	@LoginRequired(Action.Normal)
	@ApiOperation(value = "查询产品选中的联动配置的模组", notes = "查询产品选中的联动配置的模组")
	@RequestMapping(value = "/queryIftttModule", method = RequestMethod.GET)
	public CommonResponse queryIftttModule(Long productId) {
		ProductModuleIftttResp resp = new ProductModuleIftttResp();
		List<ProductTimerResp> list = this.productTimerApi.getProductTimer(productId);
		if(!ObjectUtils.isEmpty(list)){
			List<String> timers = list.stream().map(ProductTimerResp::getTimerType).collect(Collectors.toList());
			resp.setTimerTypes(timers);
		}
		List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
		if (productModuleList == null) {
			return CommonResponse.success(resp);
		}
		List<Long> serviceModuleIds = productModuleList.stream().map(ServiceModuleListResp::getId).collect(Collectors.toList());
		if (!serviceModuleIds.isEmpty()) {
			ModuleSupportIftttResp actionResp = serviceActionApi.findSupportIftttActions(serviceModuleIds, SaaSContextHolder.currentTenantId());
			resp.setAction(filterSelect(actionResp));
			ModuleSupportIftttResp eventResp = serviceEventApi.findSupportIftttEvents(serviceModuleIds, SaaSContextHolder.currentTenantId());
			resp.setEvent(filterSelect(eventResp));
			ModuleSupportIftttResp propertyResp = servicePropertyApi.findSupportIftttProperties(serviceModuleIds, SaaSContextHolder.currentTenantId());
			resp.setProperty(filterSelect(propertyResp));
		}
		return CommonResponse.success(resp);
	}

	/**
	  * @despriction：过滤选中的
	  * @author  yeshiyuan
	  * @created 2018/12/12 14:09
	  */
	private ModuleSupportIftttResp filterSelect(ModuleSupportIftttResp resp) {
		ModuleSupportIftttResp newResp = new ModuleSupportIftttResp();
		if (resp!=null) {
			List<ModuleIftttDataResp> ifList = resp.getIfList();
			if ( ifList != null && !ifList.isEmpty()) {
				List<ModuleIftttDataResp> newIfList = new ArrayList<>();
				ifList.forEach(ifData -> {
					if (ifData.getFlag() == 1) {
						newIfList.add(ifData);
					}
				});
				newResp.setIfList(newIfList);
			}
			List<ModuleIftttDataResp> thenList = resp.getThenList();
			if ( thenList != null && !thenList.isEmpty()) {
				List<ModuleIftttDataResp> newThenList = new ArrayList<>();
				thenList.forEach(thenData -> {
					if (thenData.getFlag() == 1) {
						newThenList.add(thenData);
					}
				});
				newResp.setThenList(newThenList);
			}
		}
		return newResp;
	}

	@ApiOperation("根据productId获取")
	@LoginRequired(Action.Normal)
	@RequestMapping(value = "/style", method = RequestMethod.GET)
	public CommonResponse list(@RequestParam("productId") Long productId){
		AssertUtils.notNull(productId, "productId.notnull");
		List<ProductToStyleResp> list = productToStyleApi.list(productId);
		if (list != null && !list.isEmpty()) {
			List<String> fileIds = list.stream().filter(a -> !StringUtil.isBlank(a.getImg())).map(ProductToStyleResp::getImg).collect(Collectors.toList());
			List<String> zipFileIds = list.stream().filter(a -> !StringUtil.isBlank(a.getResourceLink())).map(ProductToStyleResp::getResourceLink).collect(Collectors.toList());
			fileIds.addAll(zipFileIds);
			if (fileIds!= null && !fileIds.isEmpty()) {
				Map<String, String> fileUrlMap = fileApi.getGetUrl(fileIds);
				list.forEach(m->{
					if (StringUtils.isNotEmpty(m.getImg())){
						m.setImg(fileUrlMap.get(m.getImg()));
					}
					if (StringUtils.isNoneBlank(m.getResourceLink())) {
						m.setResourceLink(fileUrlMap.get(m.getResourceLink()));
					}
				});
			}
		}
		return CommonResponse.success(list);
	}

	@ApiOperation("根据产品Id导出数据")
	@LoginRequired(value = Action.Skip)
	@RequestMapping(value = "/exportProduct", method = RequestMethod.POST)
	public void exportProduct(@RequestBody ProductExportReq exportReq, HttpServletResponse res) {
		AssertUtils.notNull(exportReq, "exportReq.notnull");
		ProductExportResp resp = new ProductExportResp();
		Long tenantId = exportReq.getTenantId();
		Long productId = exportReq.getProductId();
		List<Long> productIds = new ArrayList<>();
		//产品功能组
		GetProductModuleResp productModule =  moduleCoreApi.findServiceModuleListByProductId(tenantId, productId);
		BeanUtil.copyProperties(productModule, resp);
		//产品配置定时方式
		List<ProductTimerResp> timers = productTimerApi.getProductTimer(productId);
		if(!CollectionUtils.isEmpty(timers)){
			resp.setTimers(timers);
		}
		//产品发布历史
		List<ProductPublishHistoryResp> publishHistorys = productApi.getProductPublishHis(productId, tenantId);
		if(!CollectionUtils.isEmpty(publishHistorys)){
			resp.setPublishHistorys(publishHistorys);
		}
		//产品审核记录
		List<ProductReviewRecordResp> reviewRecords = productReviewRecodApi.getReviewRecord(productId);
		if(!CollectionUtils.isEmpty(reviewRecords)){
			resp.setReviewRecords(reviewRecords);
		}
		//产品关联第三方服务信息
		List<ProductServiceInfoResp> serviceInfos = productServiceInfoApi.getServiceInfoByProductId(tenantId, productId);
		if(!CollectionUtils.isEmpty(serviceInfos)){
			serviceInfos.forEach(serviceInfo->{
				//处理替换serviceId成goods_info表goodCode
				Long serviceId = serviceInfo.getServiceId();
				if(serviceId != null){
					GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(serviceId);
					if(goodsInfo != null) {
						serviceInfo.setGoodsCode(goodsInfo.getGoodsCode());
					}
				}
			});
			resp.setServiceInfos(serviceInfos);
		}
		//产品文案信息
		productIds.add(productId);
		List<LangInfoTenant> langInfos = langInfoTenantApi.getLangInfoByProductIds(tenantId, productIds);
		if(!CollectionUtils.isEmpty(langInfos)){
			resp.setLangInfoTenants(langInfos);
		}
		//生成、下载产品信息
		createAndDownProduct(resp, exportReq, res);
	}

	private void createAndDownProduct(ProductExportResp product, ProductExportReq exportReq, HttpServletResponse res){
		String filePath = exportReq.getFilePath();
		String fileName = exportReq.getFileName();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
		net.sf.json.JSONArray jsonObject = net.sf.json.JSONArray.fromObject(product,jsonConfig);
		String jsonString1 = jsonObject.toString();
		//生成json文件
		boolean createFile = fileExportUtil.createJsonFile(jsonString1, filePath, fileName);
		log.info("create product json file is {}", createFile);
		//下载JSON文件
		fileExportUtil.download(filePath, fileName, res);
		log.info("HttpServletResponse {}", res);
	}


}
