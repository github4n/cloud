package com.iot.boss.service.app.impl;

import com.iot.boss.service.app.IAPPService;
import com.iot.boss.vo.review.req.AppReviewListSearchReq;
import com.iot.boss.vo.review.resp.AppInfoReviewResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.ServiceBuyRecordApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.payment.vo.order.resp.OrderDetailInfoResp;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.vo.req.SaveAppReq;
import com.iot.tenant.vo.req.review.AppReviewSearchReq;
import com.iot.tenant.vo.resp.review.AppReviewResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class APPServiceImpl implements IAPPService {

    @Autowired
    private AppApi appApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;

    @Autowired
    private OrderApi orderApi;

    /**
     * 描述：获取APP审核列表
     * @author maochengyuan
     * @created 2018/10/26 9:29
     * @param req 请求参数
     * @return java.util.List<com.iot.boss.vo.review.resp.AppInfoReviewResp>
     */
    @Override
    public Page<AppInfoReviewResp> getAppListByAuditStatus(AppReviewListSearchReq req){
        Page<AppInfoReviewResp> myPage = new Page<>();
        AppReviewSearchReq r = new AppReviewSearchReq();
        BeanUtil.copyProperties(req, r); // 账号名称 即账号 精确搜索即传入userId
        // 根据账号名称 精确查询userId，根据userId 精确搜索
        if(StringUtil.isNotBlank(req.getAccountName())) {
            FetchUserResp user = userApi.getAdminUserByUserName(req.getAccountName());
            if (user != null) {
                r.setCreateBy(user.getId());
            }else {
                return myPage;
            }
        }
        Page<AppReviewResp> page = this.appApi.getAppListByAuditStatus(r);
        BeanUtil.copyProperties(page, myPage);
        if(!CommonUtil.isEmpty(page.getResult())){
            Set<Long> tids = new HashSet<>();
            List<GoodsInfo> goodsInfos = goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.APP_PACKAGE.getCode());
            List<AppInfoReviewResp> resl = new ArrayList<>();
            //获取审核人员名称
            List<Long> userIds = page.getResult().stream().filter(i->i.getAuditOperId() !=null).map(AppReviewResp::getAuditOperId).collect(Collectors.toList());
            //List<Long> createByIds = page.getResult().stream().filter(i->i.getApplyOperId() !=null).map(AppReviewResp::getApplyOperId).collect(Collectors.toList());;
            //userIds.addAll(createByIds);
            Map<Long,String> userName = new HashMap<>();
            if (!userIds.isEmpty()) {
                Map<Long,FetchUserResp> userMap = userApi.getByUserIds(userIds);
                userName = userMap.values().stream().collect(Collectors.toMap(FetchUserResp::getId, a->a.getUserName(), (a, b) -> a));
            }
            for (AppReviewResp o : page.getResult()) {
                tids.add(o.getTenantId());
                AppInfoReviewResp a = new AppInfoReviewResp();
                BeanUtil.copyProperties(o, a);
                String orderId = serviceBuyRecordApi.getOrderIdByServiceIdAndGoodsTypeId(goodsInfos.get(0).getId(), o.getId(), goodsInfos.get(0).getTypeId());
                if(!StringUtil.isEmpty(orderId)) {
                    //throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "orderId is null");
                    OrderDetailInfoResp orderDetailInfoResp = orderApi.getOrderDetailInfo(orderId, o.getTenantId());
                    if(orderDetailInfoResp != null) {
                        //throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "orderDetailInfo not exist");
                        a.setTotalPrice(orderDetailInfoResp.getTotalPrice());
                    }
                }
                a.setOrderNo(orderId);
                a.setPayType("Paypal");
                a.setAuditOperName(userName.get(o.getAuditOperId()));
                if (o.getApplyOperId()!=null) {
                    a.setApplyOperName(userName.get(o.getApplyOperId()));
                }
                resl.add(a);
            }
            /**获取主账号*/
            List<FetchUserResp> users = this.userApi.getAdminUserByTenantId(new ArrayList<>(tids));
            Map<Long, String> emailmap = users.stream().collect(Collectors.toMap(FetchUserResp::getTenantId, FetchUserResp::getUserName, (a,b)->a));
            resl.forEach(o ->{
                o.setMainAccount(emailmap.get(o.getTenantId()));
            });
            myPage.setResult(resl);
        }
        return myPage;
    }

    @Override
    public void reOpen(Long appId) {
        appApi.reOpen(appId);
    }


    @Override
    public void updateAppStatus(Long id) {
        SaveAppReq saveAppReq = new SaveAppReq();
        saveAppReq.setId(id);
        saveAppReq.setStatus(3);
        appApi.saveApp(saveAppReq);
    }
}
