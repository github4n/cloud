package com.iot.portal.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductServiceInfoApi;
import com.iot.device.api.TechnicalRelateApi;
import com.iot.device.vo.req.AddProductReq;
import com.iot.device.vo.req.ProductPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.rsp.*;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.service.FileService;
import com.iot.portal.service.ServiceModuleCoreService;
import com.iot.portal.web.vo.FileResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:02 2018/6/29
 * @Modify by:
 */
@Api(value = "portal-产品管理", description = "portal-产品管理")
@RestController
@RequestMapping("/portal/product")
public class ProtalProductController {

    private Logger LOGGER = LoggerFactory.getLogger(PortalProductToStyleController.class);
    @Autowired
    private ProductApi productApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileUploadApi fileUploadApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private ServiceModuleCoreService serviceModuleCoreService;

    @Autowired
    private LangInfoTenantApi langInfoTenantApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private TechnicalRelateApi technicalRelateApi;

    @Autowired
    private ProductServiceInfoApi productServiceInfoApi; //产品关联第三方服务api

    @ApiOperation("添加产品信息")
    @LoginRequired(Action.Normal)
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Map<String, Object>> addProduct(@Valid @RequestBody AddProductReq productReq) {
        AssertUtils.notNull(productReq, "product.notnull");
        productReq.setTenantId(SaaSContextHolder.currentTenantId());
        // 获取当前登录人主账号信息，创建时以主账号为主
        List<FetchUserResp> userResps = userApi.getAdminUserByTenantId(Arrays.asList(SaaSContextHolder.currentTenantId()));
        if (userResps != null && userResps.size() > 0) {
            productReq.setCreateBy(userResps.get(0).getId());
        }
        productReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        Long id = productApi.addProduct(productReq);
        DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(productReq.getDeviceTypeId());
        //判断是否为免开发产品
        if (deviceTypeResp != null) {
            LOGGER.info("check deviceType:{}", deviceTypeResp);
            if (deviceTypeResp.getWhetherSoc() != null && deviceTypeResp.getWhetherSoc().compareTo(1) == 0) {
                //copy
                serviceModuleCoreService.copyDeviceTypeModuleToProductId(id, deviceTypeResp.getId());
            }
        }
        //拷贝文案
        CopyLangInfoReq langInfoReq = new CopyLangInfoReq(SaaSContextHolder.currentTenantId(), id, productReq.getDeviceTypeId(), LangInfoObjectTypeEnum.deviceType.toString(), SaaSContextHolder.getCurrentUserId());
        langInfoTenantApi.copyLangInfo(langInfoReq);
        Map<String, Object> returnDataMap = Maps.newHashMap();
        returnDataMap.put("productId", id);
        return CommonResponse.success(returnDataMap);
    }

    @ApiOperation("获取产品详情")
    @RequestMapping(value = "/getProductInfoByProductId", method = RequestMethod.GET)
    public CommonResponse<ProductResp> getProductInfoByProductId(@RequestParam("productId") Long productId) {

        ProductResp productInfo = productApi.getProductById(productId);
        return CommonResponse.success(productInfo);
    }

    @ApiOperation("修改产品信息")
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<AddProductReq> updateProduct(@RequestBody AddProductReq productReq) {
        AssertUtils.notNull(productReq, "product.notnull");
        AssertUtils.notNull(productReq.getId(), "productId.notnull");
        productReq.setTenantId(SaaSContextHolder.currentTenantId());
        productApi.updateProduct(productReq);
        return CommonResponse.success();
    }


    @ApiOperation("删除产品")
    @RequestMapping(value = "/deleteProductByProductId", method = RequestMethod.GET)
    public CommonResponse<AddProductReq> deleteProductByProductId(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        productApi.delProductByProductId(productId);
        //删除产品对应的文案
        langInfoTenantApi.deleteLangInfo(LangInfoObjectTypeEnum.deviceType.toString(), productId.toString(), SaaSContextHolder.currentTenantId());
        // 删除产品关联的第三方服务
        productServiceInfoApi.deleteProductServiceInfo(SaaSContextHolder.currentTenantId(), productId, null);
        return CommonResponse.success();
    }

    @ApiOperation("拷贝产品")
    @RequestMapping(value = "/copyProductByProductId", method = RequestMethod.GET)
    public CommonResponse<Map<String, Object>> copyProductByProductId(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        Long targetProductId = productApi.copyProductByProductId(productId);
        //拷贝文案
        CopyLangInfoReq copyLangInfoReq = new CopyLangInfoReq(SaaSContextHolder.currentTenantId(),
                targetProductId, productId,LangInfoObjectTypeEnum.deviceType.toString(),SaaSContextHolder.getCurrentUserId());
        langInfoTenantApi.copyLangInfoTenant(copyLangInfoReq);
        Map<String, Object> returnDataMap = Maps.newHashMap();
        returnDataMap.put("productId", targetProductId);
        return CommonResponse.success(returnDataMap);
    }

    @ApiOperation("分页获取产品列表")
    @RequestMapping(value = "/findProductPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Page<ProductPageResp>> findProductPage(@RequestBody ProductPageReq pageReq) {
        Page<ProductPageResp> pageRespPage = productApi.findProductPage(pageReq);
        if (pageRespPage.getResult() == null) {
            return CommonResponse.success(pageRespPage);
        }
        List<ProductPageResp> productList = Lists.newArrayList();
        for (ProductPageResp product : pageRespPage.getResult()) {
            if (!StringUtils.isEmpty(product.getProductIcon())) {
                try {
                    //后期优化成 批量获取
                    FileResp file = fileService.getUrl(product.getProductIcon());
                    product.setProductIcon(file != null ? file.getUrl() : product.getProductIcon());
                } catch (Exception e) {
                    LOGGER.info("get file error.{}", e);
                    e.printStackTrace();
                }
            } else {
                if (product.getDeviceTypeId() != null) {
                    DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(product.getDeviceTypeId());
                    if (deviceTypeResp != null) {
                        if (!StringUtils.isEmpty(deviceTypeResp.getImg())) {
                            try {
                                //后期优化成 批量获取
                                FileResp file = fileService.getUrl(deviceTypeResp.getImg());
                                product.setProductIcon(file != null ? file.getUrl() : product.getProductIcon());
                            } catch (Exception e) {
                                LOGGER.info("get file error.{}", e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            productList.add(product);
            pageRespPage.setResult(productList);
        }
        return CommonResponse.success(pageRespPage);
    }

    /**
      * @despriction：确认发布
      * @author  yeshiyuan
      * @created 2018/9/12 13:40
      * @return
      */
    @LoginRequired
    @ApiOperation(value = "确认发布", notes = "确认发布")
    @RequestMapping(value = "/confirmRelease", method = RequestMethod.POST)
    public CommonResponse confirmRelease(HttpServletRequest request, Long productId, String productName, String model, String remark){
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }else if (!productResp.getTenantId().equals(SaaSContextHolder.currentTenantId())){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The product does not belong to you.");
        }
        ProductConfirmReleaseReq req = new ProductConfirmReleaseReq(productId, productName, model, remark, productResp.getIcon());
        req.setTenantId(SaaSContextHolder.currentTenantId());
        req.setUserId(SaaSContextHolder.getCurrentUserId());
        req.setOldModel(productResp.getModel());
        if (request instanceof  MultipartHttpServletRequest){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            if (multipartRequest.getFileNames().hasNext()){
                MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
                String fileId = fileUploadApi.uploadFile(multipartFile, SaaSContextHolder.currentTenantId());
                if (StringUtil.isNotBlank(productResp.getIcon())){
                    fileApi.deleteObject(productResp.getIcon());
                }
                req.setIcon(fileId);
            }
        }
        productApi.confirmRelease(req);
        return CommonResponse.success();
    }

    @ApiOperation("查询发布历史记录")
    @RequestMapping(value = "/getProductPublishHis", method = RequestMethod.GET)
    public CommonResponse<List<ProductPublishHistoryResp>> getProductPublishHis(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        return CommonResponse.success(productApi.getProductPublishHis(productId, SaaSContextHolder.currentTenantId()));
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
}
