package com.iot.device.controller;

import com.iot.device.api.ProductTimerApi;
import com.iot.device.service.IProductTimerService;
import com.iot.device.vo.req.ProductTimerReq;
import com.iot.device.vo.req.TimerConfigReq;
import com.iot.device.vo.rsp.ProductTimerResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：设备
 * 功能描述：产品定时配置
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/23 10:54
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/23 10:54
 * 修改描述：
 */
@RestController
public class ProductTimerController implements ProductTimerApi {

    @Autowired
    private IProductTimerService productTimerService;

    /**
     * 描述：删除产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 11:24
     * @param productId 产品Id
     * @return void
     */
    @Override
    public void delProductTimer(Long productId) {
        if(ObjectUtils.isEmpty(productId)){
            return;
        }
        this.productTimerService.delProductTimer(productId);
    }

    /**
     * 描述：保存产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 11:24
     * @param req 请求参数
     * @return void
     */
    @Override
    public void addProductTimer(@RequestBody TimerConfigReq req) {
        if(ObjectUtils.isEmpty(req.getTimerTypes()) || ObjectUtils.isEmpty(req.getProductId())){
            return;
        }
        List<ProductTimerReq> list = new ArrayList<>();
        for(String timerType:req.getTimerTypes()){
            ProductTimerReq obj = new ProductTimerReq(timerType, req.getProductId(), req.getTenantId());
            obj.setCreateBy(req.getUserId());
            obj.setUpdateBy(req.getUserId());
            obj.setCreateTime(new Date());
            obj.setUpdateTime(new Date());
            list.add(obj);
        };
        this.productTimerService.addProductTimer(list);
    }

    /**
     * 描述：更新产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 11:24
     * @param req 请求参数
     * @return void
     */
    @Override
    public void uptProductTimer(@RequestBody TimerConfigReq req) {
        this.delProductTimer(req.getProductId());
        this.addProductTimer(req);
    }

    /**
     * 描述：查询产品定时配置-单个
     * @author maochengyuan
     * @created 2018/11/23 11:24
     * @param productId 产品Id
     * @return java.util.List<com.iot.device.vo.rsp.ProductTimerResp>
     */
    @Override
    public List<ProductTimerResp> getProductTimer(Long productId) {
        if(ObjectUtils.isEmpty(productId)){
            return Collections.emptyList();
        }
        return this.productTimerService.getProductTimer(productId);
    }

    /**
     * 描述：查询产品定时配置-批量
     * @author maochengyuan
     * @created 2018/11/23 17:20
     * @param productIds 产品Id
     * @return java.util.Map<java.lang.Long,java.util.List<com.iot.device.vo.rsp.ProductTimerResp>>
     */
    @Override
    public Map<Long, List<ProductTimerResp>> getProductTimers(@RequestBody List<Long> productIds) {
        if(ObjectUtils.isEmpty(productIds)){
            return Collections.emptyMap();
        }
        return this.productTimerService.getProductTimers(productIds);
    }

}
