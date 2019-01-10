package com.iot.portal.web.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.*;
import com.iot.device.enums.ServicePayStatusEnum;
import com.iot.device.vo.req.service.CreateServiceBuyRecordReq;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.req.servicereview.SetServiceReviewReq;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.enums.SkipUrl;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.payment.enums.order.OrderTypeEnum;
import com.iot.payment.vo.goods.req.GoodsExtendServiceReq;
import com.iot.payment.vo.order.req.CreateOrderRecordReq;
import com.iot.payment.vo.order.req.OrderPayReq;
import com.iot.portal.common.service.controller.CommonController;
import com.iot.portal.constant.TenantConstant;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.exception.UuidBusinessExceptionEnum;
import com.iot.portal.service.PayService;
import com.iot.portal.web.vo.PortalServiceDetailResp;
import com.iot.portal.web.vo.PortalServiceResp;
import com.iot.portal.web.vo.req.ServiceBuySubmitReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.DictApi;
import com.iot.system.api.LangApi;
import com.iot.system.enums.DictTypeEnum;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.AppReviewApi;
import com.iot.tenant.enums.AuditStatusEnum;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 14:59
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 14:59
 * 修改描述：
 */
@Api(value = "虚拟服务购买记录" ,description = "虚拟服务购买记录")
@RestController
@RequestMapping(value = "/portal/serviceBuyRecord")
public class PortalServiceBuyRecordController extends CommonController {

    private static Logger logger = LoggerFactory.getLogger(PortalServiceBuyRecordController.class);

    /**商品描述特殊分隔符*/
    private static final String DESC_SPLIT_BY = "@@";

    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;

    @Autowired
    private PaymentApi paymentApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private PayService payService;

    @Autowired
    private DictApi dictApi;

    @Value("${paypal.callBackPort}")
    private String callBackPort;

    @Value("${paypal.serverIp}")
    private String serverIp;

    @Autowired
    private ServiceReviewApi serviceReviewApi;

    @Autowired
    private AppReviewApi appReviewApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private LangApi langApi;
    @Autowired
    private DeviceTypeToGoodsApi deviceTypeToGoodsApi;

    /**
      * @despriction：虚拟服务订单提交
      * @author  yeshiyuan
      * @created 2018/9/13 17:38
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "创建虚拟服务订单", notes = "创建虚拟服务订单")
    @RequestMapping(value = "/createRecord", method = RequestMethod.POST)
    public CommonResponse createRecord(@RequestBody ServiceBuySubmitReq submitReq){
        ServiceBuySubmitReq.checkParam(submitReq);
        Long currentTenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        Integer goodsTypeId = Integer.valueOf(SecurityUtil.DecryptAES(submitReq.getGoodsTypeId(), TenantConstant.SECURITY_KEY));
        int orderType = 0;
        Long tenantId = 0L;
        //1、校验绑定id是否存在、是否属于当前租户
        if (goodsTypeId.equals(GoodsTypeEnum.VoiceService.getCode())){
            ProductResp productResp = productApi.getProductById(submitReq.getServiceId());
            if (productResp==null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "product not exist");
            }
            tenantId = productResp.getTenantId();
            orderType = OrderTypeEnum.VIDEO_SERVICE.getCode();
        }else if (goodsTypeId.equals(GoodsTypeEnum.APP_PACKAGE.getCode())){
            AppInfoResp appInfoResp = appApi.getAppById(submitReq.getServiceId());
            if (appInfoResp == null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "app not exist");
            }
            tenantId = appInfoResp.getTenantId();
            orderType = OrderTypeEnum.APP_PACK.getCode();
        }else {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "goodsTypeId error");
        }
        if (!tenantId.equals(currentTenantId)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The service does not belong to you.");
        }
        Long goodsId = Long.valueOf(SecurityUtil.DecryptAES(submitReq.getGoodsId(), TenantConstant.SECURITY_KEY));
        if (serviceBuyRecordApi.checkIsCreate(currentTenantId,submitReq.getServiceId() ,goodsId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The service has already existed.");
        }
        //2.创建订单相关信息(uuid商品校验、金额校验、建立订单与商品关系)
        CreateOrderRecordReq createOrderRecordReq = new CreateOrderRecordReq(currentTenantId, userId, submitReq.getTotalPrice(), submitReq.getCurrency(), orderType);
        GoodsExtendServiceReq goodsExtendServiceReq = new GoodsExtendServiceReq(goodsId,1);
        List<GoodsExtendServiceReq> list = new ArrayList<>();
        list.add(goodsExtendServiceReq);
        createOrderRecordReq.setGoodsExtendServiceReq(list);
        String orderId = orderApi.createOrderRecord(createOrderRecordReq);
        //3、创建虚拟服务购买记录
        CreateServiceBuyRecordReq createServiceBuyRecordReq = new CreateServiceBuyRecordReq(currentTenantId, userId, orderId, submitReq.getServiceId(),
                goodsId, goodsTypeId, 1, submitReq.getAddDemandDesc(), new Date());
        serviceBuyRecordApi.createRecord(createServiceBuyRecordReq);
        return CommonResponse.success(orderId);
    }

    /**
      * @despriction：订单支付
      * @author  yeshiyuan
      * @created 2018/9/14 10:44
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "订单支付", notes = "订单支付")
    @RequestMapping(value = "/orderPay", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse orderPay(@RequestBody OrderPayReq orderPayReq){
        //1.校验订单是否存在，是否属于当前租户的,是否待支付
        Long tenantId = SaaSContextHolder.currentTenantId();
        String orderId = orderPayReq.getOrderId();
        ServiceBuyRecordResp recordResp = serviceBuyRecordApi.getServiceBuyRecordByOrderId(orderId, tenantId);
        if (recordResp == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "No buying history.");
        }else if (ServicePayStatusEnum.PAY_SUCCESS.getValue().equals(recordResp.getPayStatus())){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The order has been paid. Please refresh the page.");
        }

        //2.生成支付url
        OrderRecord orderRecord = orderApi.getOrderRecord(orderId,tenantId);
        if(null != orderRecord && null != orderRecord.getOrderType()){
            Map<String, String> itemMap = null;
            String returnUrl = serverIp + ":" + callBackPort;
            if(OrderTypeEnum.APP_PACK.getCode() == orderRecord.getOrderType()){
                Short key = DictTypeEnum.APP_PAY_SKIP_URL.getCode();
                itemMap = dictApi.getDictItemNames(key);
            }else if(OrderTypeEnum.VIDEO_SERVICE.getCode() == orderRecord.getOrderType()){
                Short key = DictTypeEnum.GOOGLE_PAY_SKIP_URL.getCode();
                itemMap = dictApi.getDictItemNames(key);
            }
            if(null != itemMap && !itemMap.isEmpty()){
                orderPayReq.setCancelUrl(returnUrl + itemMap.get(SkipUrl.CANCEL_URL.getCode()));
                orderPayReq.setSuccessUrl(returnUrl + itemMap.get(SkipUrl.SUCCESS_URL.getCode()));
                orderPayReq.setErrorUrl(returnUrl + itemMap.get(SkipUrl.FAILED_URL.getCode()));
            }
        }
        String payUrl = payService.payAndGetUrl(orderPayReq, "service");
        return CommonResponse.success(payUrl);
    }

    /**
      * @despriction：paypal支付回调（虚拟服务支付）
      * @author  yeshiyuan
      * @created 2018/9/13 17:35
      * @return
      */
    @LoginRequired(value = Action.Skip)
    @ApiIgnore
    @ApiOperation(value = "paypal支付回调（虚拟服务支付）", notes = "paypal支付回调（虚拟服务支付）")
    @RequestMapping(value = "/payCallBack", method = {RequestMethod.GET})
    public void payCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = null;
        TransationDto transationDto = null;
        String payerId = request.getParameter("PayerID");
        String paymentId = request.getParameter("paymentId");
        String orderId = request.getParameter("orderId");
        try {
            //先获取交易信息
            transationDto = paymentApi.getTransationDtoFromRedis(paymentId);
            if (transationDto == null){
                throw new BusinessException(BusinessExceptionEnum.PAY_CALLBACK_ERROR, "The transition does not exist.");
            }
            //修改服务购买记录支付状态
            UpdateServicePayStatusReq payStatusReq = new UpdateServicePayStatusReq(transationDto.getTenantId(),orderId,ServicePayStatusEnum.WAIT_PAY.getValue(), ServicePayStatusEnum.PAY_SUCCESS.getValue());
            int j = serviceBuyRecordApi.updatePayStatus(payStatusReq);
            if (j<0){
                throw new BusinessException(BusinessExceptionEnum.PAY_CALLBACK_ERROR, "Unable to update the buying history.");
            }
            ServiceBuyRecordResp serviceBuyRecordResp = serviceBuyRecordApi.getServiceBuyRecordByOrderId(transationDto.getOrderId(), transationDto.getTenantId());
            //找到对应的虚拟订单，查找商品是什么
            GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(serviceBuyRecordResp.getGoodsId());
            FetchUserResp userResp = userApi.getUserByUuid(transationDto.getUserId());
            if (GoodsTypeEnum.APP_PACKAGE.getCode().equals(goodsInfo.getTypeId())) {
                AppReviewRecordReq appReviewRecordReq = new AppReviewRecordReq(transationDto.getTenantId(), serviceBuyRecordResp.getServiceId(),
                        new Date(), AuditStatusEnum.Pending.getAuditStatus(),"",userResp.getId(), new Date());
                appReviewApi.submitAudit(appReviewRecordReq);
            } else if (GoodsTypeEnum.VoiceService.getCode().equals(goodsInfo.getTypeId())){
                SetServiceReviewReq setServiceReviewReq = new SetServiceReviewReq(transationDto.getTenantId(),userResp.getId(),
                        serviceBuyRecordResp.getGoodsId(), serviceBuyRecordResp.getServiceId(), AuditStatusEnum.Pending.getAuditStatus(),"");
                serviceReviewApi.submitServiceReview(setServiceReviewReq);
            }
            //paypal扣款确认
            this.paymentApi.pay(payerId, paymentId);
            url = transationDto.getSuccessUrl();
        } catch (Exception e) {
            logger.error("虚拟服务->订单号："+ orderId +"的支付回调出错:", e);
            //状态回滚
            UpdateServicePayStatusReq payStatusReq = new UpdateServicePayStatusReq(transationDto.getTenantId(),orderId,ServicePayStatusEnum.PAY_SUCCESS.getValue(), ServicePayStatusEnum.WAIT_PAY.getValue());
            serviceBuyRecordApi.updatePayStatus(payStatusReq);
            if (transationDto != null) {
                logger.info("支付回调出错的transationDto：{}", JsonUtil.toJson(transationDto));
                url = transationDto.getErrorUrl();
            }
        }
        response.sendRedirect(url);
    }


    /**
     * 描述：查询语音服务信息
     * @author maochengyuan
     * @created 2018/9/13 16:12
     * @param productId 产品id
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询语音服务信息", notes = "查询语音服务信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/getVoiceService", method = RequestMethod.GET)
    public CommonResponse getVoiceService(@RequestParam("productId") Long productId) {
        super.checkProductId(productId);
        return ResultMsg.SUCCESS.info(this.getServiceList(productId, GoodsTypeEnum.VoiceService));
    }

    /**
     * 描述：查询语音服务信息详情
     * @author maochengyuan
     * @created 2018/9/13 16:12
     * @param productId 产品id
     * @param goodsId 商品id
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询语音服务信息详情", notes = "查询语音服务信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getVoiceServiceDetail", method = RequestMethod.GET)
    public CommonResponse getVoiceServiceDetail(@RequestParam("productId") Long productId, @RequestParam("goodsId") String goodsId) {
        super.checkProductId(productId);
        //查询商品服务详情
        PortalServiceDetailResp ps = this.getServiceDetail(productId, goodsId);
        //查询产品详细信息
        ProductResp pr = this.productApi.getProductById(productId);
        if(pr == null){
            throw new BusinessException(UuidBusinessExceptionEnum.PRODUCT_NOT_EXIST);
        }
        Map servcieInfo = new HashMap();
        servcieInfo.put("productName", pr.getProductName());
        ps.setServcieInfo(servcieInfo);
        return ResultMsg.SUCCESS.info(ps);
    }

    /**
     * 描述：查询应用打包发布信息
     * @author maochengyuan
     * @created 2018/9/13 16:12
     * @param appId APPId
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询应用打包发布信息", notes = "查询应用打包发布信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "APPId", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/getAppBaleService", method = RequestMethod.GET)
    public CommonResponse getAppBaleService(@RequestParam("appId") Long appId) {
        super.checkAppId(appId);
        return ResultMsg.SUCCESS.info(this.getServiceList(appId, GoodsTypeEnum.APP_PACKAGE));
    }

    /**
     * 描述：查询应用打包发布信息详情
     * @author maochengyuan
     * @created 2018/9/13 16:12
     * @param appId appId
     * @param goodsId 商品id
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询应用打包发布信息详情", notes = "查询应用打包发布信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getAppBaleServiceDetail", method = RequestMethod.GET)
    public CommonResponse getAppBaleServiceDetail(@RequestParam("appId") Long appId, @RequestParam("goodsId") String goodsId) {
        super.checkAppId(appId);
        //查询商品服务详情
        PortalServiceDetailResp ps = this.getServiceDetail(appId, goodsId);
        //查询APP详细信息
        AppInfoResp ar = this.appApi.getAppById(appId);
        if(ar == null){
            throw new BusinessException(BusinessExceptionEnum.APP_INFO_NOT_FOUND);
        }
        Map servcieInfo = new HashMap();
        servcieInfo.put("appName", ar.getAppName());
        //查询Logo详细信息
        if(!StringUtil.isEmpty(ar.getLogoFileId())){
            FileDto file = this.fileApi.getGetUrl(ar.getLogoFileId());
            if(file != null){
                servcieInfo.put("logoUrl", file.getPresignedUrl());
            }
        }
        ps.setServcieInfo(servcieInfo);
        return ResultMsg.SUCCESS.info(ps);
    }

    /**
     * 描述：根据产品id查询该产品绑定虚拟服务列表
     * @author maochengyuan
     * @created 2018/9/14 10:37
     * @param serviceId 服务id
     * @param goodsTypeEnum 商品枚举
     * @return java.util.List<com.iot.portal.web.vo.PortalServiceResp>
     */
    private List<PortalServiceResp> getServiceList(Long serviceId, GoodsTypeEnum goodsTypeEnum) {
        // 定义返回结果集
        List<PortalServiceResp> list = new ArrayList<>();
        List<GoodsInfo> goodsList = new ArrayList<>();
        if (goodsTypeEnum.getCode().equals(GoodsTypeEnum.APP_PACKAGE.getCode())) { // App打包
            goodsList = this.goodsServiceApi.getGoodsInfoByTypeId(goodsTypeEnum.getCode());
        } else if (goodsTypeEnum.getCode().equals(GoodsTypeEnum.VoiceService.getCode())){
            // 获取设备信息
            ProductResp productResp = productApi.getProductById(serviceId);
            if (productResp != null) {
                Long deviceTypeId = productResp.getDeviceTypeId();
                if (deviceTypeId != null) {
                    // 商品的goodsCode
                    List<String> goodsCodes = deviceTypeToGoodsApi.getDeviceTypeGoodsCodeByDeviceTypeId(deviceTypeId);
                    if (goodsCodes != null && goodsCodes.size() > 0) {
                        goodsList = this.goodsServiceApi.getListGoodsInfoByGoodsCode(goodsCodes);
                    }
                }
            }
        }
        if (goodsList != null && goodsList.size() > 0) {
            Set<String> goodsName = goodsList.stream().map(GoodsInfo::getGoodsName).collect(Collectors.toSet());
            Set<String> goodsDesc = goodsList.stream().map(GoodsInfo::getDescription).collect(Collectors.toSet());
            if (!CommonUtil.isEmpty(goodsName) && !CommonUtil.isEmpty(goodsDesc)) {
                goodsName.addAll(goodsDesc);
            }
            Map<String, String> nameMap = this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
            //查询服务购买信息
            List<ServiceBuyRecordResp> records = this.serviceBuyRecordApi.getServiceBuyRecord(serviceId, goodsTypeEnum.getCode(), SaaSContextHolder.currentTenantId());
            Map<Long, Integer> maps = records.stream().collect(Collectors.toMap(ServiceBuyRecordResp::getGoodsId, ServiceBuyRecordResp::getPayStatus, (k1, k2) -> k1));
            Map<Long, String> orderMaps = records.stream().collect(Collectors.toMap(ServiceBuyRecordResp::getGoodsId, ServiceBuyRecordResp::getOrderId, (k1, k2) -> k1));

            goodsList.forEach(o -> {
                PortalServiceResp p = new PortalServiceResp();
                p.setPrice(o.getPrice());
                p.setIcon(o.getIcon());
                String desc = nameMap.get(o.getDescription());
                if (!StringUtil.isEmpty(desc)) {
                    String[] array = desc.split(DESC_SPLIT_BY);
                    if (array.length == 1) {
                        p.setDescription(array[0]);
                    } else if (array.length == 2) {
                        p.setDescription(array[0]);
                        p.setDetailDesc(array[1]);
                    }
                }
                p.setGoodsId(super.encryptKey(o.getId()));
                p.setGoodsTypeId(super.encryptKey(new Long(o.getTypeId())));
                p.setPayStatus(maps.get(o.getId()));
                p.setOrderId(orderMaps.get(o.getId()));
                p.setCurrency(o.getCurrency());
                p.setGoodsName(nameMap.get(o.getGoodsName()));
                list.add(p);
            });
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 描述：查询商品服务详情
     * @author maochengyuan
     * @created 2018/9/14 9:49
     * @param serviceId 服务id
     * @param goodsId 商品id
     * @return com.iot.portal.web.vo.PortalServiceDetailResp
     */
    private PortalServiceDetailResp getServiceDetail(Long serviceId, String goodsId) {
        //查询商品信息
        GoodsInfo gi =  this.goodsServiceApi.getGoodsInfoByGoodsId(super.checkAppId(goodsId));
        if(gi == null){
            throw new BusinessException(BusinessExceptionEnum.GOODS_INFO_NOT_FOUND);
        }
        //查询服务信息
        ServiceBuyRecordResp sb = this.serviceBuyRecordApi.getServiceBuyRecordDetail(serviceId, gi.getId(), SaaSContextHolder.currentTenantId());
        //组装返回对象
        PortalServiceDetailResp ps = new PortalServiceDetailResp(super.encryptKey(gi.getId()), gi.getIcon(), gi.getPrice());
        if(sb != null){
            ps.setDescription(sb.getAddDemandDesc());
            ps.setOrderId(sb.getOrderId());
            ps.setOrderTime(sb.getCreateTime());
            ps.setPayStatus(sb.getPayStatus());
            ps.setCurrency(gi.getCurrency());
        }
        return ps;
    }

}