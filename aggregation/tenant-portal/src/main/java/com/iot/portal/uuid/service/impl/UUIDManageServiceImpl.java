package com.iot.portal.uuid.service.impl;

import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.*;
import com.iot.device.api.ProductApi;
import com.iot.device.api.TechnicalRelateApi;
import com.iot.device.api.UUIDManegerApi;
import com.iot.device.api.UuidApplyRecordApi;
import com.iot.device.dto.UUIDInfoDto;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.enums.uuid.UuidPayStatusEnum;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.req.uuid.UuidApplyRecordReq;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.uuid.UuidApplyRecordResp;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.SkipUrl;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.payment.enums.order.OrderTypeEnum;
import com.iot.payment.vo.goods.req.GoodsExtendServiceReq;
import com.iot.payment.vo.order.OrderGoodsAndExtendServiceVo;
import com.iot.payment.vo.order.req.CreateOrderRecordReq;
import com.iot.payment.vo.order.req.OrderPayReq;
import com.iot.payment.vo.order.resp.OrderDetailInfoResp;
import com.iot.portal.constant.TenantConstant;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.exception.UuidBusinessExceptionEnum;
import com.iot.portal.service.PayService;
import com.iot.portal.uuid.service.UUIDManageService;
import com.iot.portal.uuid.vo.req.GetUUIDOrderReq;
import com.iot.portal.uuid.vo.req.GetUUIDReq;
import com.iot.portal.uuid.vo.req.UuidApplyReq;
import com.iot.portal.uuid.vo.resp.*;
import com.iot.portal.web.vo.PortalTechnicalResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.DictApi;
import com.iot.system.api.LangApi;
import com.iot.system.enums.DictTypeEnum;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：UUID管理接口实现
 * 创建人： maochengyuan
 * 创建时间：2018/6/29 15:32
 * 修改人： maochengyuan
 * 修改时间：2018/6/29 15:32
 * 修改描述：
 */
@Service("UUIDManegerService")
public class UUIDManageServiceImpl implements UUIDManageService {

    private final static Logger logger = LoggerFactory.getLogger(UUIDManageServiceImpl.class);

    @Autowired
    private DictApi dictApi;

    @Autowired
    private UUIDManegerApi uuidManegerApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private UuidApplyRecordApi uuidApplyRecordApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private LangApi langApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private PaymentApi paymentApi;

    @Autowired
    private TechnicalRelateApi technicalRelateApi;

    @Autowired
    private PayService payService;

    @Value("${paypal.callBackPort}")
    private String callBackPort;

    @Value("${paypal.serverIp}")
    private String serverIp;

    /**
     * 描述：获取UUID商品列表
     * @author maochengyuan
     * @created 2018/7/3 18:16
     * @param 
     * @return java.util.List<GoodsInfoResp>
     */
    @Override
    public List<GoodsInfoResp> getUUIDGoodsList() {
        List<GoodsInfo> list = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        List<GoodsInfoResp> temp = new ArrayList<>();
        Set<String> goodsName = new HashSet<>();
        list.forEach(o ->{
            GoodsInfoResp resp = new GoodsInfoResp();
            BeanUtils.copyProperties(o, resp);
            resp.setId(SecurityUtil.EncryptByAES(StringUtil.toString(o.getId()), TenantConstant.SECURITY_KEY));
            temp.add(resp);
            goodsName.add(resp.getGoodsName());
        });
        Map<String, String> nameMap =  this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        temp.forEach(o ->{
            o.setGoodsName(nameMap.get(o.getGoodsName()));
        });
        list.clear();
        return temp;
    }

    /**
     * 描述：编辑UUID订单
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    @Override
    public void editOrderCreateNum(String orderId, Integer createNum) {
        this.orderApi.editOrderCreateNum(orderId, createNum);
        this.uuidApplyRecordApi.editOrderCreateNum(orderId, createNum, SaaSContextHolder.currentTenantId());
    }

    /**
     * 转换UUID订单对象
     * @param dto
     * @return
     */
    private UUIDOrderResp convert(UUIDOrderDto dto){
        UUIDOrderResp resp=new UUIDOrderResp();
        resp.setBatchNumId(dto.getBatchNumId());
        resp.setOrderId(dto.getOrderId());
        resp.setApplyStatus(dto.getApplyStatus());
        resp.setUserId(dto.getUserId());
        resp.setClientAccount(String.valueOf(dto.getUserId()));
        resp.setCreateTime(dto.getCreateTime());
        resp.setPayStatus(dto.getPayStatus());
        resp.setGoodsId(dto.getGoodsId());
        resp.setUuidNum(dto.getUuidNum());
        resp.setProductModel(dto.getProductName());
        return resp;
    }

    /**
     * 转换UUID详情对象
     * @param dto
     * @return
     */
    private UUIDInfoResp convert(UUIDInfoDto dto){
        UUIDInfoResp resp=new UUIDInfoResp();
        resp.setUuid(dto.getUuid());
        resp.setBatchNumId(dto.getBatchNumId());
        resp.setGoodsId(dto.getGoodsId());
        resp.setActiveStatus(dto.getActiveStatus());
        resp.setActiveTime(dto.getActiveTime());
        resp.setLastLoginTime(dto.getLastLoginTime());
        resp.setProductModel(dto.getProductModel());
        return resp;
    }

    /**
     * 等待衣服任务处理完成
     * @param futures
     */
    private void waitingForCompleted(CompletableFuture... futures){
        try {
            CompletableFuture.allOf(futures).join();
        }catch (Exception e){
            if (e.getCause()!=null){
                if (e.getCause() instanceof RuntimeException){
                    throw (RuntimeException)e.getCause();
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<UUIDOrderResp> getUUIDOrder(GetUUIDOrderReq uuidOrderReq) {
        com.iot.device.vo.req.uuid.GetUUIDOrderReq deviceServiceReqBean=new com.iot.device.vo.req.uuid.GetUUIDOrderReq();
        BeanUtils.copyProperties(uuidOrderReq,deviceServiceReqBean);

        if(StringUtil.isNotBlank(uuidOrderReq.getCondition())){
            if(uuidOrderReq.getCondition().trim().matches("^[0-9]*$")){
                deviceServiceReqBean.setBatchNumId(Long.parseLong(uuidOrderReq.getCondition()));
            }else {
                //产品model不会是纯数字
                deviceServiceReqBean.setModel(uuidOrderReq.getCondition().trim());
            }
        }

        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        deviceServiceReqBean.setTenantId(tenantId);
        FetchUserResp user=userApi.getUser(userId);
        if (user.getUserLevel()!=null&&(user.getUserLevel()==1||user.getUserLevel()==2)&&user.getAdminStatus()!=null&&user.getAdminStatus()==1){//此处应该加上user_level等级的判断
            //管理员可以查询所有用户订单
        }else
            deviceServiceReqBean.setUserId(userId);
        Page pageDto=uuidManegerApi.getUUIDOrder(deviceServiceReqBean);
        Page<UUIDOrderResp> pageResp=new Page<>();
        BeanUtils.copyProperties(pageDto,pageResp,"result");
        List<UUIDOrderDto> records=pageDto.getResult();
        if (records==null||records.isEmpty()){
            return pageResp;
        }
        pageResp.setResult(records.stream().map(e->convert(e)).collect(Collectors.toList()));
        CompletableFuture<Void> setDictValueFuture=CompletableFuture.supplyAsync(()->dictApi.getDictItemNamesBatch(Arrays.asList(DictTypeEnum.UUID_ORDER_STATUS.getCode(),DictTypeEnum.UUID_PAY_STATUS.getCode())))
        .thenAccept(r->{pageResp.getResult().forEach(e-> {
            if (e.getApplyStatus()!=null) {
                Map<String, String> itemMap = r.get(DictTypeEnum.UUID_ORDER_STATUS.getCode());
                e.setApplyStatusDesc(DictUtil.getItemNameByItemId(itemMap, e.getApplyStatus().shortValue()));
            }
            if(e.getPayStatus()!=null){
                Map<String, String>  itemMap = r.get(DictTypeEnum.UUID_PAY_STATUS.getCode());
                e.setPayStatusDesc(DictUtil.getItemNameByItemId(itemMap, e.getPayStatus().shortValue()));
            }
        });});

        ArrayList<Long> userIds=new ArrayList<>(records.stream().filter(e->e.getUserId()!=null).map(e->e.getUserId()).collect(Collectors.toSet()));
        ArrayList<Long> goodsIds=new ArrayList<>(records.stream().filter(e->e.getGoodsId()!=null).map(e->e.getGoodsId()).collect(Collectors.toSet()));
        CompletableFuture<Void> setOtherValueFuture=CompletableFuture.supplyAsync(()->{return userApi.getByUserIds(userIds);})
                .thenAcceptBoth(CompletableFuture.supplyAsync(()->{return goodsServiceApi.getGoodsNameByGoodsId(goodsIds);}),(r1,r2)->{
                    if (r1!=null||r2!=null){
                        pageResp.getResult().forEach(e->{
                            if (r1!=null&&e.getUserId()!=null){
                                FetchUserResp fur=r1.get(e.getUserId());
                                e.setClientAccount(fur!=null?fur.getUserName():null);
                            }
                            if (r2!=null&&e.getGoodsId()!=null){
                                e.setProductSchema(r2.get(e.getGoodsId()));
                            }
                        });
                    }
                });
        waitingForCompleted(setDictValueFuture,setOtherValueFuture);
        Set<String> keys=new HashSet<>();
        pageResp.getResult().forEach(e->{
            if (e.getProductSchema()!=null)
                keys.add(e.getProductSchema());
            if (e.getApplyStatusDesc()!=null)
                keys.add(e.getApplyStatusDesc());
            if (e.getPayStatusDesc()!=null)
                keys.add(e.getPayStatusDesc());
        });
        Map<String,String> langMap=langApi.getLangValueByKey(keys,LocaleContextHolder.getLocale().toString());
        if (langMap!=null){
            pageResp.getResult().forEach(e->{
                if (e.getProductSchema()!=null){
                    e.setProductSchema(langMap.get(e.getProductSchema()));
                }
                if (e.getApplyStatusDesc()!=null){
                    e.setApplyStatusDesc(langMap.get(e.getApplyStatusDesc()));
                }
                if (e.getPayStatusDesc()!=null){
                    e.setPayStatusDesc(langMap.get(e.getPayStatusDesc()));
                }
            });
        }
        return pageResp;
    }

    @Override
    public Page<UUIDInfoResp> getUUIDInfo(GetUUIDReq uuidReq) {
        com.iot.device.vo.req.uuid.GetUUIDReq deviceServiceReqBean=new com.iot.device.vo.req.uuid.GetUUIDReq();
        BeanUtils.copyProperties(uuidReq,deviceServiceReqBean);
        //Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        deviceServiceReqBean.setTenantId(tenantId);
        Page<UUIDInfoDto> pageDto=uuidManegerApi.getUUIDInfo(deviceServiceReqBean);
        Page<UUIDInfoResp> pageResp=new Page<>();
        BeanUtils.copyProperties(pageDto,pageResp,"result");
        List<UUIDInfoDto> records=pageDto.getResult();
        if (records==null||records.isEmpty()) return pageResp;
        pageResp.setResult(records.stream().map(e->convert(e)).collect(Collectors.toList()));
        //设置字典表描述信息字段
        CompletableFuture<Void> setDictValueFuture=CompletableFuture.supplyAsync(()->dictApi.getDictItemNames(DictTypeEnum.UUID_ACTIVE_STATUS.getCode()))
                .thenAcceptAsync(r->{
                    pageResp.getResult().forEach(e-> {
                        if (e.getActiveStatus()!=null){
                            e.setActiveStatusDesc(DictUtil.getItemNameByItemId(r, e.getActiveStatus().shortValue()));
                        }else{
                            //默认给空的状态为未激活
                            e.setActiveStatus(0);
                            e.setActiveStatusDesc(DictUtil.getItemNameByItemId(r, (short)0));
                        }
                      });
                });
        //在此添加批量查询goodsId名称的步骤
        ArrayList<Long> goodsIds=new ArrayList<>(records.stream().filter(e->e.getGoodsId()!=null).map(e->e.getGoodsId()).collect(Collectors.toSet()));
        CompletableFuture<Void> setOtherValueFuture=CompletableFuture.supplyAsync(()->goodsServiceApi.getGoodsNameByGoodsId(goodsIds)).thenAcceptAsync(r->{
            if (r!=null){
                pageResp.getResult().forEach(e->{if (e.getGoodsId()!=null) {e.setProductSchema(r.get(e.getGoodsId()));}});
            }
        });
        waitingForCompleted(setDictValueFuture,setOtherValueFuture);
        Set<String> keys=new HashSet<>();
        pageResp.getResult().forEach(e->{
            if (e.getProductSchema()!=null)
                keys.add(e.getProductSchema());
            if (e.getActiveStatusDesc()!=null)
                keys.add(e.getActiveStatusDesc());

        });
        Map<String,String> langMap=langApi.getLangValueByKey(keys,LocaleContextHolder.getLocale().toString());
        if (langMap!=null){
            pageResp.getResult().forEach(e->{
                if (e.getProductSchema()!=null){
                    e.setProductSchema(langMap.get(e.getProductSchema()));
                }
                if (e.getActiveStatusDesc()!=null){
                    e.setActiveStatusDesc(langMap.get(e.getActiveStatusDesc()));
                }
            });
        }
        return pageResp;
    }

    @Override
    public String uuidApply(UuidApplyReq uuidApplyReq) {
        if(StringUtil.isBlank(uuidApplyReq.getProductId())){
            throw new BusinessException(UuidBusinessExceptionEnum.UUID_PRODUCT_ID_NOT_NULL);
        }
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long productId = Long.valueOf(SecurityUtil.DecryptAES(uuidApplyReq.getProductId(),TenantConstant.SECURITY_KEY));
        //1.校验产品是否属于当前租户
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(UuidBusinessExceptionEnum.PRODUCT_NOT_EXIST);
        }else{
            if (tenantId.compareTo(productResp.getTenantId()) != 0){
                throw new BusinessException(UuidBusinessExceptionEnum.UUID_PRODUCT_NOT_BELONG_YOU);
            }
        }
        if(productResp.getCommunicationMode() == null){
            throw new BusinessException(UuidBusinessExceptionEnum.UUID_GOOD_ID_NOT_NULL);
          }
        Long goodsId = Integer.valueOf(productResp.getCommunicationMode()).longValue();
        //2.创建订单相关信息(uuid商品校验、金额校验、建立订单与商品关系)
        if (uuidApplyReq.getCreateNum()==null || uuidApplyReq.getCreateNum().intValue()<1){
            throw new BusinessException(UuidBusinessExceptionEnum.UUID_NUM_LESS_ONE);
        }
        CreateOrderRecordReq createOrderRecordReq = new CreateOrderRecordReq(tenantId,userId,uuidApplyReq.getTotalPrice(),uuidApplyReq.getCurrency(), OrderTypeEnum.UUID_PROGRAMME.getCode());
        GoodsExtendServiceReq goodsExtendServiceReq = new GoodsExtendServiceReq(goodsId,uuidApplyReq.getCreateNum());
        List<GoodsExtendServiceReq> list = new ArrayList<>();
        list.add(goodsExtendServiceReq);
        createOrderRecordReq.setGoodsExtendServiceReq(list);
        String orderId = orderApi.createOrderRecord(createOrderRecordReq);
        //3.创建待付款的uuid申请记录
        UuidApplyRecordReq uuidApplyRecordReq = new UuidApplyRecordReq(tenantId,userId,orderId,uuidApplyReq.getCreateNum(),goodsId,productId);
        uuidApplyRecordApi.createUuidApplyRecord(uuidApplyRecordReq);
        return orderId;
    }

    /**
     *
     * 描述：通过批次号获取证书下载URL
     * @author 李帅
     * @created 2018年6月8日 下午1:51:26
     * @since
     * @param batchNum
     * @return
     */
    @Override
    public String getDownloadUUID(Long batchNum) {
    	//在Header中获取数据
        String userId = SaaSContextHolder.getCurrentUserUuid();
        Long tenantId = SaaSContextHolder.currentTenantId();
        return uuidManegerApi.getDownloadUUID(tenantId, userId, batchNum);
    }

    /**
     * @despriction：uuid订单支付
     * @author  yeshiyuan
     * @created 2018/7/4 14:56
     * @param uuidOrderPayReq
     * @return
     */
    @Override
    public String uuidOrderPay(OrderPayReq orderPayReq) {
        //1.校验订单是否存在，是否属于当前租户的,是否待支付
        Long tenantId = SaaSContextHolder.currentTenantId();
        String orderId = orderPayReq.getOrderId();
        UuidApplyRecordResp uuidApplyRecordResp = uuidApplyRecordApi.getUuidApplyRecordInfo(orderId, tenantId);
        if (uuidApplyRecordResp == null){
            throw new BusinessException(UuidBusinessExceptionEnum.UUID_ORDER_NOT_EXIST);
        }
        if (!UuidPayStatusEnum.WAIT_PAY.getValue().equals(uuidApplyRecordResp.getPayStatus())){
            throw new BusinessException(UuidBusinessExceptionEnum.UUID_ORDER_HAD_PAY);
        }
        //2.生成支付url
        BeanUtil.copyProperties(uuidApplyRecordResp, orderPayReq);
        String returnUrl = serverIp + ":" + callBackPort;
        Map<String, String> itemMap = dictApi.getDictItemNames(DictTypeEnum.UUID_PAY_SKIP_URL.getCode());
        orderPayReq.setCancelUrl(returnUrl + itemMap.get(SkipUrl.CANCEL_URL.getCode()));
        orderPayReq.setSuccessUrl(returnUrl + itemMap.get(SkipUrl.SUCCESS_URL.getCode()));
        orderPayReq.setErrorUrl(returnUrl + itemMap.get(SkipUrl.FAILED_URL.getCode()));
        String payUrl = payService.payAndGetUrl(orderPayReq, "uuid");
        return payUrl;
    }

    /**
     * @despriction：paypal支付回调
     * @author  yeshiyuan
     * @created 2018/7/4 16:31
     * @param request
     * @return 跳转URL
     */
    @Override
    public String uuidOrderPayCallBack(HttpServletRequest request) {
        String url = null;
        TransationDto transationDto = null;
        String payerId = request.getParameter("PayerID");
        String paymentId = request.getParameter("paymentId");
        String orderId = request.getParameter("orderId");
        boolean canGenerateUUID = false; //能否生成uuid
        try {
            //先获取交易信息
            transationDto = paymentApi.getTransationDtoFromRedis(paymentId);
            if (transationDto == null){
                throw new BusinessException(UuidBusinessExceptionEnum.UUID_PAY_CALLBACK_ERROR, "The transition does not exist.");
            }
            //修改uuid申请状态
            int j = uuidApplyRecordApi.updatePayStatus(orderId,transationDto.getTenantId(), UuidPayStatusEnum.PAYED.getValue(), UuidPayStatusEnum.WAIT_PAY.getValue());
            if (j<0){
                throw new BusinessException(UuidBusinessExceptionEnum.UUID_PAY_CALLBACK_ERROR, "update order_apply_record pay_status fail");
            }
            //paypal扣款确认
            this.paymentApi.pay(payerId, paymentId);
            url = transationDto.getSuccessUrl();
            canGenerateUUID = true;
        } catch (Exception e) {
             logger.error("uuid订单号："+ orderId +"的支付回调出错:", e);
             //状态回滚
             uuidApplyRecordApi.updatePayStatus(orderId,transationDto.getTenantId(), UuidPayStatusEnum.WAIT_PAY.getValue(), UuidPayStatusEnum.PAYED.getValue());
             if (transationDto != null) {
                logger.info("支付回调出错的transationDto：{}", JsonUtil.toJson(transationDto));
                url = transationDto.getErrorUrl();
             }
        }
        if (canGenerateUUID){
            //通知生成uuid
            try{
                UuidApplyRecordResp uuidApplyRecordResp = uuidApplyRecordApi.getUuidApplyRecordInfo(orderId,transationDto.getTenantId());
                uuidManegerApi.generateUUID(uuidApplyRecordResp.getId());
            }catch (Exception e){
                logger.error("生成uuid失败:", e);
            }
        }
        return url;
    }

    /**
     * @despriction：获取uuid订单信息
     * @author  yeshiyuan
     * @created 2018/7/5 9:44
     * @param orderId 订单id
     * @return
     */
    @Override
    public UuidOrderInfoResp getUuidOrderInfo(String orderId) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        OrderDetailInfoResp orderDetailInfoResp = orderApi.getOrderDetailInfo(orderId,tenantId);
        BigDecimal goodsPrice = null;
        Integer buyNum = null;
        String goodsName = null;
        if (orderDetailInfoResp.getGoodsList()!=null){
            //暂时uuid订单只会有一种商品，所以直接get0
            OrderGoodsAndExtendServiceVo vo = orderDetailInfoResp.getGoodsList().get(0);
            goodsPrice = vo.getOrderGoods().getGoodsPrice();
            buyNum = vo.getOrderGoods().getNum();
            goodsName = vo.getOrderGoods().getGoodsName();
        }
        //订单状态翻译
        Map<String, String> itemMap = dictApi.getDictItemNames(DictTypeEnum.UUID_PAY_STATUS.getCode());
        UuidApplyRecordResp uuidApplyRecordResp = uuidApplyRecordApi.getUuidApplyRecordInfo(orderId,tenantId);
        String payStatusStr = DictUtil.getItemNameByItemId(itemMap,uuidApplyRecordResp.getPayStatus().shortValue());
        UuidOrderInfoResp uuidOrderInfoResp = new UuidOrderInfoResp(orderId, goodsPrice, orderDetailInfoResp.getTotalPrice(), buyNum, goodsName, orderDetailInfoResp.getCurrency(),payStatusStr);
        return uuidOrderInfoResp;
    }

    /**
      * @despriction：查找某个租户下面的产品
      * @author  yeshiyuan
      * @created 2018/7/9 10:49
      * @return
      */
    @Override
    public List<ProductListResp> findProductListByTenantId() {
        ProductReq req = new ProductReq();
        req.setTenantId(SaaSContextHolder.currentTenantId());
        List<ProductResp> list = productApi.findProductListByTenantId(req);
        List<ProductListResp> productListResps = list.stream().map(o -> {
            Long id = o.getId();
            return new ProductListResp(SecurityUtil.EncryptByAES(id.toString(), TenantConstant.SECURITY_KEY),o.getProductName(),o.getCommunicationMode());
        }).collect(Collectors.toList());
        return productListResps;
    }

    /**
     * 
     * 描述：查找某个租户下面的直连产品
     * @author 李帅
     * @created 2018年11月8日 上午11:25:54
     * @since 
     * @return
     */
   @Override
   public List<ProductListResp> findDirectProductListByTenantId() {
       ProductReq req = new ProductReq();
       req.setTenantId(SaaSContextHolder.currentTenantId());
       List<ProductResp> list = productApi.findDirectProductListByTenantId(req);
       List<ProductListResp> productListResps = list.stream().map(o -> {
           Long id = o.getId();
           return new ProductListResp(SecurityUtil.EncryptByAES(id.toString(), TenantConstant.SECURITY_KEY),o.getProductName(),o.getCommunicationMode());
       }).collect(Collectors.toList());
       return productListResps;
   }
   
    /**
     * @despriction：加载设备类型支持的技术方案
     * @author  yeshiyuan
     * @created 2018/10/16 11:41
     * @return
     */
    @Override
    public List<PortalTechnicalResp> getTechnicals(Long deviceTypeId) {
        List<Long> technicalIds = technicalRelateApi.queryDeviceTechnicalIds(deviceTypeId);
        if (technicalIds.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "this device type no config technical scheme");
        }
        List<GoodsInfo> list = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        Map<Long, GoodsInfo> goodsInfoMap = list.stream().collect(Collectors.toMap(GoodsInfo::getId, a->a));
        Set<String> goodsName = new HashSet<>();
        technicalIds.forEach(id -> {
            if (goodsInfoMap.containsKey(id)) {
                goodsName.add(goodsInfoMap.get(id).getGoodsName());
            }
        });
        if (goodsName.size() == 0) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "this device type support technical scheme not exists");
        }
        Map<String, String> nameMap = this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        List<PortalTechnicalResp> temp = new ArrayList<>();
        technicalIds.forEach(id -> {
            GoodsInfo o = goodsInfoMap.get(id);
            PortalTechnicalResp resp = new PortalTechnicalResp(o.getId(),nameMap.get(o.getGoodsName()), o.getIcon());
            temp.add(resp);
        });
        return temp;
    }



    /**
     * @despriction：通过技术方案对应的UUID商品列表
     * @author  yeshiyuan
     * @created 2018/10/16 11:53
     * @param null
     * @return
     */
    @Override
    public List<GoodsInfoResp> getUuidGoodsByTechnicalId(Long technicalId) {
        List<GoodsInfo> list = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        Map<Long, GoodsInfo> goodsInfoMap = list.stream().collect(Collectors.toMap(GoodsInfo::getId, a->a));
        Set<String> goodsName = new HashSet<>();
        if (goodsInfoMap.containsKey(technicalId)) {
            goodsName.add(goodsInfoMap.get(technicalId).getGoodsName());
        }
        if (goodsName.size() == 0) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "this device type support technical scheme not exists");
        }
        Map<String, String> nameMap = this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        List<GoodsInfoResp> temp = new ArrayList<>();
        GoodsInfo o = goodsInfoMap.get(technicalId);
        o.setGoodsName(nameMap.get(o.getGoodsName()));
        GoodsInfoResp resp = new GoodsInfoResp();
        BeanUtils.copyProperties(o, resp);
        resp.setId(SecurityUtil.EncryptByAES(StringUtil.toString(o.getId()), TenantConstant.SECURITY_KEY));
        temp.add(resp);
        return temp;
    }
    
    /**
     * 
     * 描述：通过技术方案对应的UUID商品列表(没有gateway)
     * @author 李帅
     * @created 2018年11月8日 下午2:14:44
     * @since 
     * @param technicalId
     * @return
     */
    @Override
    public List<GoodsInfoResp> getUuidGoodsNoGateWayByTechnicalId(Long technicalId) {
        List<GoodsInfo> goodsInfos = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
        if(CommonUtil.isEmpty(goodsInfos)){
            return Collections.emptyList();
        }
        List<GoodsInfo> list = new ArrayList<GoodsInfo> ();
        for(GoodsInfo goodsInfo : goodsInfos) {
        	if(!"0003".equals(goodsInfo.getGoodsCode())) {
        		list.add(goodsInfo);
        	}
        }
        Map<Long, GoodsInfo> goodsInfoMap = list.stream().collect(Collectors.toMap(GoodsInfo::getId, a->a));
        Set<String> goodsName = new HashSet<>();
        if (goodsInfoMap.containsKey(technicalId)) {
            goodsName.add(goodsInfoMap.get(technicalId).getGoodsName());
        }
        if (goodsName.size() == 0) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "this device type support technical scheme not exists");
        }
        Map<String, String> nameMap = this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        List<GoodsInfoResp> temp = new ArrayList<>();
        GoodsInfo o = goodsInfoMap.get(technicalId);
        o.setGoodsName(nameMap.get(o.getGoodsName()));
        GoodsInfoResp resp = new GoodsInfoResp();
        BeanUtils.copyProperties(o, resp);
        resp.setId(SecurityUtil.EncryptByAES(StringUtil.toString(o.getId()), TenantConstant.SECURITY_KEY));
        temp.add(resp);
        return temp;
    }
}
