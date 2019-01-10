package com.iot.boss.service.order.impl;

import com.iot.boss.service.order.IOrderManagerService;
import com.iot.boss.vo.order.req.AppOrderPageReq;
import com.iot.boss.vo.order.resp.AppOrderListResp;
import com.iot.boss.vo.uuid.GetUUIDRefundReq;
import com.iot.boss.vo.uuid.UUIDOrderResp;
import com.iot.boss.vo.voiceaccess.VoiceAccessReq;
import com.iot.boss.vo.voiceaccess.VoiceAccessResp;
import com.iot.boss.vo.voiceaccess.VoiceAccessTypeResp;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.api.ServiceBuyRecordApi;
import com.iot.device.api.UUIDManegerApi;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.vo.req.service.ServiceBuyRecordPageReq;
import com.iot.device.vo.req.servicebuyrecordreq.ServiceBuyRecordReq;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.entity.PayTransation;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.enums.goods.GoodsCoodEnum;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.LangApi;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/11/14 10:16
 * 修改人： yeshiyuan
 * 修改时间：2018/11/14 10:16
 * 修改描述：
 */
@Service
public class OrderManagerServiceImpl implements IOrderManagerService {

    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;
    @Autowired
    private UUIDManegerApi uuidManegerApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private LangApi langApi;

    @Autowired
    private TenantApi tenantApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    private PaymentApi paymentApi;

    /**
     * @despriction：app列表
     * @author  yeshiyuan
     * @created 2018/11/14 10:17
     * @return
     */
    @Override
    public Page<AppOrderListResp> appList(AppOrderPageReq appOrderPageReq) {
        ServiceBuyRecordPageReq pageReq = new ServiceBuyRecordPageReq();
        BeanUtils.copyProperties(appOrderPageReq, pageReq);
        pageReq.setGoodsTypeId(GoodsTypeEnum.APP_PACKAGE.getCode());
        if (!StringUtil.isBlank(appOrderPageReq.getUserName())){
            FetchUserResp user = userApi.getAdminUserByUserName(appOrderPageReq.getUserName());
            if(user != null) {
                pageReq.setTenantIds(Arrays.asList(user.getTenantId()));
            } else {
                return new Page<AppOrderListResp>(appOrderPageReq.getPageNum(),appOrderPageReq.getPageSize());
            }
        }
        if (!StringUtil.isBlank(appOrderPageReq.getTenantName())) {
            List<Long> tenantIds = tenantApi.searchTenantIdsByName(appOrderPageReq.getTenantName());
            if (tenantIds == null || tenantIds.isEmpty()) {
                return new Page<AppOrderListResp>(appOrderPageReq.getPageNum(),appOrderPageReq.getPageSize());
            }
            pageReq.setTenantIds(tenantIds);
        }
        if (appOrderPageReq.getAppPackStatus()!=null) {
            List<Long> appIds = appApi.getAppIdByPackStatus(appOrderPageReq.getAppPackStatus());
            if (appIds == null || appIds.isEmpty()){
                return new Page<AppOrderListResp>(appOrderPageReq.getPageNum(),appOrderPageReq.getPageSize());
            }
            pageReq.setServiceIds(appIds);
        }
        if (!(StringUtil.isEmpty(appOrderPageReq.getAppName()))) {
        	List<AppInfoResp> appInfoResps = appApi.getAppByAppName(appOrderPageReq.getAppName());
            if (appInfoResps == null || appInfoResps.isEmpty()){
                return new Page<AppOrderListResp>(appOrderPageReq.getPageNum(),appOrderPageReq.getPageSize());
            }
            List<Long> appIds = new ArrayList<Long>();
            for(AppInfoResp appInfoResp : appInfoResps){
            	appIds.add(appInfoResp.getId());
            }
            pageReq.setServiceIds(appIds);
        }
        Page<ServiceBuyRecordResp> page = serviceBuyRecordApi.pageQuery(pageReq);
        Page<AppOrderListResp> myPage = new Page<>();
        BeanUtils.copyProperties(page, myPage);
        List<ServiceBuyRecordResp> list = page.getResult();
        List<AppOrderListResp> resultList = new ArrayList<>();
        if (list !=null && !list.isEmpty()) {
            //主账户
            Set<Long> tenantIdSet = list.stream().map(ServiceBuyRecordResp::getTenantId).collect(Collectors.toSet());
            List<FetchUserResp> userResps = userApi.getAdminUserByTenantId(new ArrayList<>(tenantIdSet));
            Map<Long, String> userMap = new HashMap<>();
            if (userResps != null) {
                userMap = userResps.stream().collect(Collectors.toMap(FetchUserResp::getTenantId, a->a.getUserName(), (a,b)->a));
            }
            //租户名称
            List<TenantInfoResp> tenantInfoResps = tenantApi.getTenantByIds(new ArrayList<>(tenantIdSet));
            Map<Long, String> tenantNameMap = new HashMap<>();
            if (tenantInfoResps != null) {
                tenantNameMap = tenantInfoResps.stream().collect(Collectors.toMap(TenantInfoResp::getId, a->a.getName(), (a,b)->a));
            }
            //查app信息
            Set<Long> appIdSet = list.stream().map(ServiceBuyRecordResp::getServiceId).collect(Collectors.toSet());
            List<AppInfoResp> appInfoResps = appApi.getAppByIds(new ArrayList<>(appIdSet));
            Map<Long, AppInfoResp> appInfoMap = new HashMap<>();
            if (appInfoResps != null && !appInfoResps.isEmpty()) {
                appInfoMap = appInfoResps.stream().collect(Collectors.toMap(AppInfoResp::getId, a->a, (a,b)->a));
            }
            //订单价格
            List<String> orderIds = list.stream().map(ServiceBuyRecordResp::getOrderId).collect(Collectors.toList());
            List<PayTransation> payTransations = paymentApi.getPayTransation(orderIds);
            Map<String, PayTransation> payMap = new HashMap<>();
            if (payTransations != null && !payTransations.isEmpty()) {
                payMap = payTransations.stream().collect(Collectors.toMap(PayTransation::getOrderId, a->a, (a,b)->a));
            }
            for (ServiceBuyRecordResp resp : list) {
                AppOrderListResp appOrderInfo = new AppOrderListResp();
                appOrderInfo.setAccount(userMap.get(resp.getTenantId()));
                appOrderInfo.setTenantName(tenantNameMap.get(resp.getTenantId()));
                appOrderInfo.setPayWay("paypal");
                appOrderInfo.setOrderId(resp.getOrderId());
                appOrderInfo.setPayStatus(resp.getPayStatus());
                appOrderInfo.setTenantId(resp.getTenantId());
                AppInfoResp appInfoResp = appInfoMap.get(resp.getServiceId());
                if (appInfoResp != null) {
                    appOrderInfo.setAppName(appInfoResp.getAppName());
                    appOrderInfo.setAppPackStatus(appInfoResp.getStatus());
                }
                PayTransation pay = payMap.get(resp.getOrderId());
                if (pay != null) {
                    appOrderInfo.setTotalPrice(new BigDecimal(pay.getPayPrice()));
                    appOrderInfo.setPayTime(pay.getPayTime());
                }
                resultList.add(appOrderInfo);
            }
        }
        myPage.setResult(resultList);
        return myPage;
    }

    /**
     * @despriction：uuid列表
     * @author  yeshiyuan
     * @created 2018/11/14 10:18
     * @return
     */
    @Override
    public Page<UUIDOrderResp> uuidList(GetUUIDRefundReq uuidOrderReq) {
        GetUUIDOrderReq deviceServiceReqBean=new GetUUIDOrderReq();
        BeanUtils.copyProperties(uuidOrderReq,deviceServiceReqBean);
        if(!StringUtil.isEmpty(uuidOrderReq.getUserName())) {
            FetchUserResp user = userApi.getAdminUserByUserName(uuidOrderReq.getUserName());
            if(user != null) {
                deviceServiceReqBean.setTenantId(user.getTenantId());
            } else {
                return new Page<UUIDOrderResp>();
            }
        }
        if(!StringUtil.isEmpty(uuidOrderReq.getTenantName())) {
            List<Long> tenantIds = tenantApi.searchTenantIdsByName(uuidOrderReq.getTenantName());
            if(tenantIds != null && tenantIds.size() == 1) {
                deviceServiceReqBean.setTenantId(tenantIds.get(0));
            }else {
                return new Page<UUIDOrderResp>();
            }
        }
        deviceServiceReqBean.setRefundFlag("1");
        Page pageDto=uuidManegerApi.getUUIDOrder(deviceServiceReqBean);
        Page<UUIDOrderResp> pageResp=new Page<>();
        BeanUtils.copyProperties(pageDto,pageResp,"result");
        List<UUIDOrderDto> records=pageDto.getResult();
        if (records==null||records.isEmpty()){
            return pageResp;
        }
        ArrayList<Long> userIds=new ArrayList<>(records.stream().filter(e->e.getUserId()!=null).map(e->e.getUserId()).collect(Collectors.toSet()));
        Map<Long, FetchUserResp> userMap = userApi.getByUserIds(userIds);
        ArrayList<String> orderIds=new ArrayList<>(records.stream().filter(e->e.getOrderId()!=null).map(e->e.getOrderId()).collect(Collectors.toSet()));
        Map<String, OrderRecord> orderRecordMap = orderApi.getOrderRecordByOrderIds(orderIds);
        ArrayList<Long> goodsIds=new ArrayList<>(records.stream().filter(e->e.getGoodsId()!=null).map(e->e.getGoodsId()).collect(Collectors.toSet()));
        Map<Long, String> goodsMap = goodsServiceApi.getGoodsNameByGoodsId(goodsIds);
        Set<String> keys=new HashSet<>();
        for (Long in : goodsMap.keySet()) {
            keys.add(goodsMap.get(in));
        }
        Map<String,String> langMap=langApi.getLangValueByKey(keys,LocaleContextHolder.getLocale().toString());

        List<UUIDOrderResp> orderResps = new ArrayList<UUIDOrderResp>();
        UUIDOrderResp orderResp = null;
        for(UUIDOrderDto orderDto : records) {
            orderResp = new UUIDOrderResp();
            BeanUtils.copyProperties(orderDto,orderResp);
            if(orderResp.getUserId() != null && userMap.get(orderResp.getUserId()) != null) {
                orderResp.setClientAccount(userMap.get(orderResp.getUserId()).getUserName());
            }
            if(orderDto.getGoodsId() != null && goodsMap.get(orderDto.getGoodsId()) != null && langMap.get(goodsMap.get(orderDto.getGoodsId())) != null) {
                orderResp.setProductSchema(langMap.get(goodsMap.get(orderDto.getGoodsId())));
            }
            if(orderDto.getOrderId() != null && orderRecordMap.get(orderDto.getOrderId()) != null) {
                orderResp.setTotalPrice(orderRecordMap.get(orderDto.getOrderId()).getTotalPrice());
                orderResp.setCurrency(orderRecordMap.get(orderDto.getOrderId()).getCurrency());
            }
            orderResps.add(orderResp);
        }
        pageResp.setResult(orderResps);
        return pageResp;
    }

    /**
     * @despriction：语音接入列表
     * @author  yeshiyuan
     * @created 2018/11/14 10:18
     * @return
     */
    @Override
    public Page voiceAccessList(VoiceAccessReq req) {
        // 返回前端分页结果集
        Page<VoiceAccessResp> voicePage = new Page<>();
        // 商品信息ids
        List<Long> goodIds = new ArrayList<>();
        // 获取商品信息列表
        List<GoodsInfo> goodsInfoList = goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.VoiceService.getCode());

        // 获取语音类型文案翻译结果
        Map<String,String> lanMaps = new HashMap<>();
        if (goodsInfoList != null && goodsInfoList.size() > 0) {
            // 定义语音类型文案翻译langKeys
            Set<String> langKeys = goodsInfoList.stream().map(GoodsInfo::getGoodsName).collect(Collectors.toSet());
            if (langKeys != null && langKeys.size() > 0) {
                lanMaps = langApi.getLangValueByKey(langKeys, LocaleContextHolder.getLocale().toString());
            }
        }

        if(req.getServiceType() == null) {
            for (GoodsInfo gi : goodsInfoList) {
                goodIds.add(gi.getId());
            }
        } else {
            goodIds.add(req.getServiceType());
        }
        ServiceBuyRecordReq serviceBuyReq = new ServiceBuyRecordReq();
        // 设置商品类型 为google或者alexa
        serviceBuyReq.setGoodsIds(goodIds);
        // 租户名称 即企业名称 模糊搜索
        if(StringUtil.isNotBlank(req.getTenantName())) {
            List<Long> tenantIds = tenantApi.searchTenantIdsByName(req.getTenantName());
            if(tenantIds != null && tenantIds.size() > 0) {
                serviceBuyReq.setTenantIds(tenantIds);
            } else {
                return voicePage;
            }
        }
        // 账号名称 即账号 精确搜索即传入userId
        if(StringUtil.isNotBlank(req.getAccountName())) {
            FetchUserResp user = userApi.getAdminUserByUserName(req.getAccountName());
            if (user != null) {
                serviceBuyReq.setUserId(user.getId());
            }else {
                return voicePage;
            }
        }
        // PlayStatus 1：待付款；2：付款成功；3：付款失败； 4：退款中；5：退款成功；6：退款失败
        if (req.getPlayStatus() != null) {
           serviceBuyReq.setPayStatus(req.getPlayStatus());
        }
        // 订单号
        if (StringUtil.isNotBlank(req.getOrderId())) {
            serviceBuyReq.setOrderId(req.getOrderId());
        }
        // 设置分页信息
        serviceBuyReq.setPageSize(req.getPageSize());
        serviceBuyReq.setPageNum(req.getPageNum());
        // 查询结果
        Page<ServiceBuyRecordResp> serviceBuyRecordPage = serviceBuyRecordApi.getServiceBuyRecordByGoodsIds(serviceBuyReq);

        BeanUtils.copyProperties(serviceBuyRecordPage,voicePage,"result");
        // 返回result结果集
        List<ServiceBuyRecordResp> serviceBuyRecordList = serviceBuyRecordPage.getResult();
        if (serviceBuyRecordList != null && serviceBuyRecordList.size() > 0) {

            ArrayList<Long> userIds = new ArrayList<>(serviceBuyRecordList.stream().filter(e -> e.getUserId() != null).map(e -> e.getUserId()).collect(Collectors.toSet()));
            Map<Long, FetchUserResp> userMap  = userApi.getByUserIds(userIds);

            ArrayList<String> orderIds = new ArrayList<>(serviceBuyRecordList.stream().filter(e -> e.getOrderId() != null).map(e -> e.getOrderId()).collect(Collectors.toSet()));
            Map<String, OrderRecord> orderRecordMap = orderApi.getOrderRecordByOrderIds(orderIds);

            Map<Long, GoodsInfo> goodsMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getId, a -> a, (k1, k2) -> k1));

            ArrayList<Long> tenantIds = new ArrayList<>(serviceBuyRecordList.stream().filter(e -> e.getTenantId() != null).map(e -> e.getTenantId()).collect(Collectors.toSet()));
            List<TenantInfoResp> tenants = tenantApi.getTenantByIds(tenantIds);
            Map<Long, TenantInfoResp> tenantMap  = tenants.stream().collect(Collectors.toMap(TenantInfoResp::getId, a -> a, (k1, k2) -> k1));

            List<VoiceAccessResp> voiceAccessRespList = new ArrayList<>();
            for (ServiceBuyRecordResp serviceBuyRecordResp : serviceBuyRecordList) {
                VoiceAccessResp voiceAccessResp = new VoiceAccessResp();
                BeanUtils.copyProperties(serviceBuyRecordResp, voiceAccessResp, "result");
                // 设置账号
                if (serviceBuyRecordResp.getUserId() != null && userMap.get(serviceBuyRecordResp.getUserId()) != null) {
                    voiceAccessResp.setAccountName(userMap.get(serviceBuyRecordResp.getUserId()).getUserName());
                }
                // 设置企业名
                if (serviceBuyRecordResp.getTenantId() != null && tenantMap.get(serviceBuyRecordResp.getTenantId()) != null) {
                    voiceAccessResp.setTenantName(tenantMap.get(serviceBuyRecordResp.getTenantId()).getName());
                }
                // 设置虚拟服务类型
                if (serviceBuyRecordResp.getGoodsId() != null && goodsMap.get(serviceBuyRecordResp.getGoodsId()) != null) {
                    String goodsName = goodsMap.get(serviceBuyRecordResp.getGoodsId()).getGoodsName();
                    voiceAccessResp.setServiceType(lanMaps.get(goodsName));
                }
                // 设置创建时间、总金额、状态
                if (serviceBuyRecordResp.getOrderId() != null && orderRecordMap.get(serviceBuyRecordResp.getOrderId()) != null) {
                    voiceAccessResp.setTotalPrice(orderRecordMap.get(serviceBuyRecordResp.getOrderId()).getTotalPrice());
                    voiceAccessResp.setCreateTime(orderRecordMap.get(serviceBuyRecordResp.getOrderId()).getCreateTime());
                    voiceAccessResp.setPayStatus(serviceBuyRecordResp.getPayStatus());
                }
                // 添加到list
                voiceAccessRespList.add(voiceAccessResp);
            }
            voicePage.setResult(voiceAccessRespList);
        }
        return voicePage;
    }

    @Override
    public List<VoiceAccessTypeResp> getVoiceAccessType() {
        // 定义返回结果集
        List<VoiceAccessTypeResp> resultList = new ArrayList<>();
        // 获取语音类型商品列表
        List<GoodsInfo>  goodsInfoList = goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.VoiceService.getCode());
        if (goodsInfoList != null && goodsInfoList.size() > 0) {
            // 定义语音类型文案翻译langKeys
            Set<String> langKeys = goodsInfoList.stream().map(GoodsInfo::getGoodsName).collect(Collectors.toSet());
            // 获取语音类型文案翻译结果
            Map<String,String> lanMaps = new HashMap<>();
            if (langKeys != null && langKeys.size() > 0) {
                lanMaps = langApi.getLangValueByKey(langKeys, LocaleContextHolder.getLocale().toString());
            }
            for (GoodsInfo goodsInfo : goodsInfoList) {
                if (StringUtil.isNotBlank(goodsInfo.getGoodsName())) {
                    VoiceAccessTypeResp resp = new VoiceAccessTypeResp(goodsInfo.getId(), lanMaps.get(goodsInfo.getGoodsName()));
                    resultList.add(resp);
                }
            }
        }
        return resultList;
    }
}
