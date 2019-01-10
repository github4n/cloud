package com.iot.boss.service.review.impl;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.service.review.ReviewService;
import com.iot.boss.vo.product.resp.BossProductAuditListResp;
import com.iot.boss.vo.product.resp.ProductDetailInfoResp;
import com.iot.boss.vo.review.req.*;
import com.iot.boss.vo.review.resp.AppInfoReviewResp;
import com.iot.boss.vo.review.resp.DetailInfoResp;
import com.iot.boss.vo.review.resp.ReviewRecordResp;
import com.iot.boss.vo.review.resp.ServiceDetailInfoResp;
import com.iot.boss.vo.servicereview.BossServiceAuditListReq;
import com.iot.boss.vo.servicereview.BossServiceAuditListResp;
import com.iot.boss.vo.servicereview.ReSetServiceReviewReq;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.*;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductReviewRecordReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.GetServiceReviewReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.req.servicereview.SetServiceReviewReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.product.ProductAuditListResp;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import com.iot.device.vo.rsp.servicereview.ServiceReviewRecordResp;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.goods.GoodsCoodEnum;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.payment.vo.order.resp.OrderDetailInfoResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.LangApi;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.AppReviewApi;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：boss聚合层
 * 功能描述：审核功能接口实现
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 15:57
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 15:57
 * 修改描述：
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private AppReviewApi appReviewApi;

    @Autowired
    private ProductReviewRecodApi productReviewRecodApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @Autowired
    private TenantApi tenantApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private ServiceReviewApi serviceReviewApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    private ProductServiceInfoApi  productServiceInfoApi; // 第三方服务接入api

    @Autowired
    private LangApi langApi;

    /**
     * 描述：app审核操作
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    @Override
    public void appReview(AppReviewReq req){
        this.checkPrimaryKey(req.getAppId(), "appId");
        this.checkOperateDesc(req.getProcessStatus(), req.getOperateDesc());
        AppReviewRecordReq dto = new AppReviewRecordReq();
        BeanUtil.copyProperties(req, dto);
        dto.setCreateBy(SaaSContextHolder.getCurrentUserId());
        dto.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        this.appReviewApi.review(dto);
    }

    /**
     * 描述：获取App审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param appId appId
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    @Override
    public DetailInfoResp getAppReviewInfo(Long appId){
        this.checkPrimaryKey(appId, "appId");
        DetailInfoResp detailInfoResp = new DetailInfoResp();
        AppInfoReviewResp appInfoReviewResp = new AppInfoReviewResp();
        AppInfoResp appInfoResp = appApi.getAppById(appId);
        if (appInfoResp == null) {
            return null;
        }
        appInfoReviewResp.setId(appId);
        appInfoReviewResp.setAppName(appInfoResp.getAppName());
        appInfoReviewResp.setTenantId(appInfoResp.getTenantId());
        appInfoReviewResp.setAuditStatus(appInfoResp.getAuditStatus());
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(appInfoResp.getTenantId());
        appInfoReviewResp.setEntName(tenantInfoResp.getName());
        appInfoReviewResp.setEntCellphone(tenantInfoResp.getCellphone());
        appInfoReviewResp.setEntContacts(tenantInfoResp.getContacts());
        List<FetchUserResp> userResps = userApi.getAdminUserByTenantId(Arrays.asList(appInfoResp.getTenantId()));
        if(userResps!=null && !userResps.isEmpty()) {
            appInfoReviewResp.setMainAccount(userResps.get(0).getUserName());
        }
        List<GoodsInfo> goodsInfos = goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.APP_PACKAGE.getCode());
        String orderId = serviceBuyRecordApi.getOrderIdByServiceIdAndGoodsTypeId(goodsInfos.get(0).getId(), appId, goodsInfos.get(0).getTypeId());
        OrderDetailInfoResp orderDetailInfoResp = orderApi.getOrderDetailInfo(orderId, appInfoReviewResp.getTenantId());
        appInfoReviewResp.setOrderNo(orderId);
        appInfoReviewResp.setPayType("Paypal");
        appInfoReviewResp.setTotalPrice(orderDetailInfoResp.getTotalPrice());
        detailInfoResp.setObjInfo(appInfoReviewResp);
        /**查询审核记录*/
        List<AppReviewRecordResp> list = this.appReviewApi.getAppReviewRecord(appId);
        if(!CommonUtil.isEmpty(list)){
            Set<Long> userIds = list.stream().map(AppReviewRecordResp::getCreateBy).collect(Collectors.toSet());
            /**查询操作员信息*/
            Map<Long,FetchUserResp> userMap = userApi.getByUserIds(new ArrayList<>(userIds));
            List<ReviewRecordResp> temp = new ArrayList<>();
            list.forEach(o->{
                ReviewRecordResp t = new ReviewRecordResp();
                BeanUtil.copyProperties(o, t);
                if(userMap.containsKey(o.getCreateBy())){
                    t.setOperateUserName(userMap.get(o.getCreateBy()).getUserName());
                }
                temp.add(t);
            });
            detailInfoResp.setRecords(temp);
        }
        return detailInfoResp;
    }

    /**
     * 描述：提交产品审核
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    @Override
    public void productAudit(ProductReviewReq req){
        this.checkPrimaryKey(req.getProductId(), "productId");
        this.checkOperateDesc(req.getProcessStatus().byteValue(), req.getOperateDesc());
        ProductReviewRecordReq dto = new ProductReviewRecordReq();
        BeanUtil.copyProperties(req, dto);
        dto.setCreateBy(SaaSContextHolder.getCurrentUserId());
        dto.setOperateTime(new Date());
        productReviewRecodApi.submitAudit(dto);
    }

    /**
     * 描述：获取产品审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param productId 产品id
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    @Override
    public DetailInfoResp getProductReviewInfo(Long productId){
        this.checkPrimaryKey(productId, "productId");
        DetailInfoResp resp = new DetailInfoResp();
        resp.setObjInfo(getProductDetailInfo(productId));
        List<ProductReviewRecordResp> reviewRecordResps = productReviewRecodApi.getReviewRecord(productId);
        List<ReviewRecordResp> recordResps = null;
        try {
            if (!reviewRecordResps.isEmpty()){
                recordResps = BeanUtil.listTranslate(reviewRecordResps, ReviewRecordResp.class);
                List<Long> userIds = recordResps.stream().map(ReviewRecordResp::getCreateBy).collect(Collectors.toList());
                Map<Long,FetchUserResp> userMap = userApi.getByUserIds(userIds);
                recordResps.forEach(reviewRecordResp -> {
                    reviewRecordResp.setOperateUserName(userMap.get(reviewRecordResp.getCreateBy()).getNickname());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.setRecords(recordResps);
        return resp;
    }

    /**
     * 描述：提交租户审核
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    @Override
    public void tenantReview(TenantReviewReq req){
        this.checkPrimaryKey(req.getTenantId(), "tenantId");
        this.checkOperateDesc(req.getProcessStatus(), req.getOperateDesc());
    }

    /**
     * 描述：获取租户审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param tenantId 租户id
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    @Override
    public DetailInfoResp getTenantReviewInfo(Long tenantId){
        this.checkPrimaryKey(tenantId, "tenantId");
        return null;
    }

    /**
     * 描述：提交语音服务审核
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    @Override
    public void serviceReview(ServiceReviewReq req){
        this.checkPrimaryKey(req.getProductId(), "productId");
        this.checkPrimaryKey(req.getServiceId(), "serviceId");
        this.checkPrimaryKey(req.getTenantId(), "tenantId");
        this.checkOperateDesc(req.getProcessStatus(), req.getOperateDesc());

        SetServiceReviewReq setServiceReviewReq = new SetServiceReviewReq();
        setServiceReviewReq.setTenantId(req.getTenantId());
        setServiceReviewReq.setUserId(SaaSContextHolder.getCurrentUserId());
        setServiceReviewReq.setServiceId(req.getServiceId());
        setServiceReviewReq.setProductId(req.getProductId());
        setServiceReviewReq.setProcessStatus(req.getProcessStatus());
        setServiceReviewReq.setOperateDesc(req.getOperateDesc());
        serviceReviewApi.setServiceReview(setServiceReviewReq);
    }

    /**
     * 描述：获取语音服务审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param productId 产品id
     * @param serviceId 语音服务id
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    @Override
    public DetailInfoResp getServiceReviewInfo(Long productId, Long serviceId){
        this.checkPrimaryKey(productId, "productId");
        this.checkPrimaryKey(serviceId, "serviceId");
        ServiceDetailInfoResp serviceDetailInfoResp = new ServiceDetailInfoResp();
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "产品不存在");
        }
        serviceDetailInfoResp.setProductName(productResp.getProductName());
        serviceDetailInfoResp.setModel(productResp.getModel());
        DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(productResp.getDeviceTypeId());
        serviceDetailInfoResp.setDeviceTypeName(deviceTypeResp.getName());
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(productResp.getTenantId());
        List<FetchUserResp> userResps = userApi.getAdminUserByTenantId(Arrays.asList(productResp.getTenantId()));
        if(userResps!=null && !userResps.isEmpty()) {
            serviceDetailInfoResp.setEmail(userResps.get(0).getUserName());
        }
        serviceDetailInfoResp.setPhone(tenantInfoResp.getCellphone());
        serviceDetailInfoResp.setContacts(tenantInfoResp.getContacts());
        GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(serviceId);
        if(goodsInfo == null) {
        	throw new BusinessException(BossExceptionEnum.GOODSINFO_NOT_EXIST, "goodsInfo not exist");
        }
        String orderId = serviceBuyRecordApi.getOrderIdByServiceIdAndGoodsTypeId(serviceId, productId, goodsInfo.getTypeId());
        if(StringUtil.isEmpty(orderId)) {
        	throw new BusinessException(BossExceptionEnum.ORDERID_IS_NULL, "orderId is null");
        }
        OrderDetailInfoResp orderDetailInfoResp = orderApi.getOrderDetailInfo(orderId, productResp.getTenantId());
        if(orderDetailInfoResp == null) {
        	throw new BusinessException(BossExceptionEnum.ORDERDETAILINFO_NOT_EXIST, "orderDetailInfo not exist");
        }
        serviceDetailInfoResp.setPaymentMethod("Paypal");
        serviceDetailInfoResp.setPaymentAmount(orderDetailInfoResp.getTotalPrice());

        DetailInfoResp resp = new DetailInfoResp();
        resp.setObjInfo(serviceDetailInfoResp);
        GetServiceReviewReq getServiceReviewReq = new GetServiceReviewReq();
        getServiceReviewReq.setProductId(productId);
        getServiceReviewReq.setServiceId(serviceId);
        getServiceReviewReq.setTenantId(productResp.getTenantId());
        List<ServiceReviewRecordResp> reviewRecordResps = serviceReviewApi.getServiceReviewRecord(getServiceReviewReq);
        List<ReviewRecordResp> recordResps = null;
        try {
            if (!reviewRecordResps.isEmpty()){
                recordResps = BeanUtil.listTranslate(reviewRecordResps, ReviewRecordResp.class);
                List<Long> userIds = recordResps.stream().map(ReviewRecordResp::getCreateBy).collect(Collectors.toList());
                Map<Long,FetchUserResp> userMap = userApi.getByUserIds(userIds);
                recordResps.forEach(reviewRecordResp -> {
                    reviewRecordResp.setOperateUserName(userMap.get(reviewRecordResp.getCreateBy()).getNickname());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.setRecords(recordResps);
        return resp;
    }

    /**
     * 描述：检查主键是合法
     * @author maochengyuan
     * @created 2018/7/25 14:57
     * @param key key值
     * @param keyName key名称
     * @return void
     */
    private void checkPrimaryKey(Long key, String keyName){
        if(CommonUtil.isEmpty(key)){
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, keyName+" is empty");
        }
    }

    /**
     * 描述：检查参数是合法
     * @author maochengyuan
     * @created 2018/7/25 14:57
     * @param processStatus 审核目标状态
     * @param operateDesc 审核描述
     * @return void
     */
    private void checkOperateDesc(Byte processStatus, String operateDesc){
        if(CommonUtil.isEmpty(processStatus)){
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "审核状态不能为空");
        }
        if(CommonUtil.isEmpty(operateDesc)){
            return;
        }
        if(operateDesc.length() > 500){
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "审核描述超过500个字符");
        }
    }

    /**
     * @despriction：获取产品审核列表数据
     * @author  yeshiyuan
     * @created 2018/10/25 10:29
     * @return
     */
    @Override
    public Page<BossProductAuditListResp> getProductAuditList(BossProductAuditPageReq pageReq) {
        Page<BossProductAuditListResp> myPage = new Page<>();
        ProductAuditPageReq pageReq1 = new ProductAuditPageReq();
        BeanUtil.copyProperties(pageReq, pageReq1);
        if (!StringUtil.isBlank(pageReq.getTenantName())){
            List<Long> tenantIds = tenantApi.searchTenantIdsByName(pageReq.getTenantName());
            if (tenantIds == null || tenantIds.isEmpty()) {
                myPage.setPageNum(pageReq.getPageNum());
                myPage.setPageSize(pageReq.getPageSize());
                return myPage;
            }
            pageReq1.setTenantIds(tenantIds);
        }
        // 账号名称 即账号 精确搜索即传入userId
        if(StringUtil.isNotBlank(pageReq.getAccountName())) {
            FetchUserResp user = userApi.getAdminUserByUserName(pageReq.getAccountName());
            if (user != null) {
                pageReq1.setCreateBy(user.getId());
            }else {
                myPage.setPageNum(pageReq.getPageNum());
                myPage.setPageSize(pageReq.getPageSize());
                return myPage;
            }
        }
        Page<ProductAuditListResp> page = productApi.queryProductAuditList(pageReq1);
        BeanUtil.copyProperties(page, myPage);
        try {
            List<BossProductAuditListResp> list = BeanUtil.listTranslate(page.getResult(),BossProductAuditListResp.class);
            if (!list.isEmpty()) {
                List<Long> userIds = list.stream().filter(i->i.getOperateUserId() !=null).map(BossProductAuditListResp::getOperateUserId).collect(Collectors.toList());
                Map<Long,String> userName = new HashMap<>();
                if (!userIds.isEmpty()) {
                    Map<Long,FetchUserResp> userMap = userApi.getByUserIds(userIds);
                    userName = userMap.values().stream().collect(Collectors.toMap(FetchUserResp::getId, FetchUserResp::getNickname, (a,b)->a));
                }
                Set<Long> tenantIds = list.stream().map(BossProductAuditListResp::getTenantId).collect(Collectors.toSet());
                Map<Long,TenantInfoResp> tenantInfoResps = tenantApi.getTenantByIds(new ArrayList<>(tenantIds)).stream().collect(Collectors.toMap(TenantInfoResp::getId, a->a, (a,b) -> a));
                for (BossProductAuditListResp auditListResp : list) {
                    Long tenantId = auditListResp.getTenantId();
                    TenantInfoResp tenantInfoResp = tenantInfoResps.get(tenantId);
                    auditListResp.setTenantName(tenantInfoResp.getName());
                    //auditListResp.setEmail(tenantInfoResp.getEmail());
                    auditListResp.setOperateUserName(userName.get(auditListResp.getOperateUserId()));
                }
                /**获取主账号*/
                List<FetchUserResp> users = this.userApi.getAdminUserByTenantId(new ArrayList<>(tenantIds));
                Map<Long, String> emailmap = users.stream().collect(Collectors.toMap(FetchUserResp::getTenantId, FetchUserResp::getUserName, (a,b)->a));
                list.forEach(o ->{
                    o.setEmail(emailmap.get(o.getTenantId()));
                });
            }
            myPage.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myPage;
    }

    /**
      * @despriction：获取产品详情
      * @author  yeshiyuan
      * @created 2018/10/25 10:12
      * @return
      */
    private ProductDetailInfoResp getProductDetailInfo(Long productId) {
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "产品不存在");
        }
        ProductDetailInfoResp resp = new ProductDetailInfoResp();
        resp.setProductName(productResp.getProductName());
        resp.setModel(productResp.getModel());
        DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(productResp.getDeviceTypeId());
        resp.setDeviceTypeName(deviceTypeResp.getName());
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(productResp.getTenantId());
        List<FetchUserResp> userResps = userApi.getAdminUserByTenantId(Arrays.asList(productResp.getTenantId()));
        if(userResps!=null && !userResps.isEmpty()) {
            resp.setEmail(userResps.get(0).getUserName());
        }
        resp.setPhone(tenantInfoResp.getCellphone());
        return resp;
    }

    /**
     * @despriction：重开产品审核
     * @author  yeshiyuan
     * @created 2018/10/25 17:06
     * @return
     */
    @Override
    public void reOpenProductAudit(Long productId) {
        ReopenAuditReq reopenAuditReq = new ReopenAuditReq();
        reopenAuditReq.setProductId(productId);
        reopenAuditReq.setOperateTime(new Date());
        reopenAuditReq.setUserId(SaaSContextHolder.getCurrentUserId());
        productApi.reOpenAudit(reopenAuditReq);
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp != null) {
            appApi.unbindProductRelateApp(productId, productResp.getTenantId());
        }
    }

	/**
     *
     * 描述：获取语音服务审核列表数据
     * @author 李帅
     * @created 2018年10月26日 上午11:30:23
     * @since
     * @param
     * @return
     */
    @Override
    public Page<BossServiceAuditListResp> getServiceAuditList(BossServiceAuditListReq bossServiceAuditListReq) {
        Page<BossServiceAuditListResp> myPage = new Page<>();
        ServiceAuditPageReq pageReq = new ServiceAuditPageReq();
        BeanUtil.copyProperties(bossServiceAuditListReq, pageReq);
        if(!StringUtil.isEmpty(bossServiceAuditListReq.getTenantParam())) {
        	List<Long> tenantIds = tenantApi.searchTenantIdsByName(bossServiceAuditListReq.getTenantParam());
        	if (tenantIds != null && tenantIds.size() > 0) {
                pageReq.setTenantIds(tenantIds);
            } else {
        	    return myPage;
            }
        }
        // 获取语音接入商品信息
        List<GoodsInfo> goodsInfos = goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.VoiceService.getCode());
        // 定义商品类型goodsMap
        Map<Long, String> goodsMap  = new HashMap<>();
        // 定义语音文案翻译langMap
        Map<String, String> langMap = new HashMap<>();
        if (goodsInfos != null && goodsInfos.size() > 0) {
            Set<String> langKeys = goodsInfos.stream().map(GoodsInfo :: getGoodsName).collect(Collectors.toSet());
            if (langKeys != null && langKeys.size() > 0) {
                langMap = langApi.getLangValueByKey(langKeys, LocaleContextHolder.getLocale().toString());
            }
            goodsMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfo::getId, GoodsInfo::getGoodsName, (k1,k2)->k1));
        }

        // 用户存在传入服务类型
        Long serviceType = bossServiceAuditListReq.getServiceType();
        if (serviceType != null) {
            pageReq.setServiceType(serviceType);
        }

        // 账号名称 即账号 精确搜索即传入userId
        if(StringUtil.isNotEmpty(bossServiceAuditListReq.getAccountName())){
            FetchUserResp user = userApi.getAdminUserByUserName(bossServiceAuditListReq.getAccountName());
            if (user != null) {
                pageReq.setCreateBy(user.getId());
            }else {
                return myPage;
            }
        }
        // 订单号
        if (StringUtil.isNotBlank(bossServiceAuditListReq.getOrderId())) {
            pageReq.setOrderId(bossServiceAuditListReq.getOrderId());
        }

        Page<ServiceAuditListResp> page = productServiceInfoApi.queryServiceAuditList(pageReq);
        BeanUtil.copyProperties(page, myPage);
        try {
            Map<Long, TenantInfoResp> tenantNameMap = new HashMap<Long, TenantInfoResp>();
            Map<Long, FetchUserResp> adminUserMap = new HashMap<Long, FetchUserResp>();
            List<BossServiceAuditListResp> list = BeanUtil.listTranslate(page.getResult(),BossServiceAuditListResp.class);
            if (!list.isEmpty()) {
            	List<Long> userIds = list.stream().filter(i->i.getOperateUserId() !=null).map(BossServiceAuditListResp::getOperateUserId).collect(Collectors.toList());
                Map<Long,String> userName = new HashMap<>();
                if (!userIds.isEmpty()) {
                    Map<Long,FetchUserResp> userMap = userApi.getByUserIds(userIds);
                    userName = userMap.values().stream().collect(Collectors.toMap(FetchUserResp::getId, a->a.getNickname()));
                }
                List<Long> tenantIds = list.stream().map(BossServiceAuditListResp::getTenantId).collect(Collectors.toList());
                List<TenantInfoResp> tenantInfoResps = tenantApi.getTenantByIds(tenantIds);
                for(TenantInfoResp tenantInfoResp : tenantInfoResps) {
                	tenantNameMap.put(tenantInfoResp.getId(), tenantInfoResp);
                }
                List<FetchUserResp> fetchUserResps = userApi.getAdminUserByTenantId(tenantIds);
                for(FetchUserResp adminUser : fetchUserResps) {
                	adminUserMap.put(adminUser.getTenantId(), adminUser);
                }
                for(BossServiceAuditListResp bossServiceAuditListResp : list) {
                	if(tenantNameMap.get(bossServiceAuditListResp.getTenantId()) != null) {
                		bossServiceAuditListResp.setTenantName(tenantNameMap.get(bossServiceAuditListResp.getTenantId()).getName());
                	}
                	if(adminUserMap.get(bossServiceAuditListResp.getTenantId()) != null) {
                		bossServiceAuditListResp.setUserName(adminUserMap.get(bossServiceAuditListResp.getTenantId()).getUserName());
                	}
                	// 设置服务描述
                    String goodsName = goodsMap.get(bossServiceAuditListResp.getServiceId());
                	if (StringUtil.isNotBlank(goodsName)) {
                        bossServiceAuditListResp.setServiceDesc(langMap.get(goodsName));
                    }
                	bossServiceAuditListResp.setOperateUserName(userName.get(bossServiceAuditListResp.getOperateUserId()));
                }
            }
            myPage.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myPage;
    }

	/**
	 *
	 * 描述：语音服务重新申请操作
	 * @author 李帅
	 * @created 2018年11月1日 下午8:23:53
	 * @since
	 * @param reSetServiceReviewReq
	 */
	@Override
	public void reSetServiceReview(ReSetServiceReviewReq reSetServiceReviewReq) {

		Long userId = SaaSContextHolder.getCurrentUserId();
		SetServiceReviewReq setServiceReviewReq = new SetServiceReviewReq();
		BeanUtils.copyProperties(reSetServiceReviewReq, setServiceReviewReq);
		setServiceReviewReq.setTenantId(reSetServiceReviewReq.getTenantId());
		setServiceReviewReq.setUserId(userId);
		setServiceReviewReq.setProcessStatus((byte) 0);
		setServiceReviewReq.setServiceId(reSetServiceReviewReq.getServiceId());
		this.serviceReviewApi.reSetServiceReview(setServiceReviewReq);
	}

}
