package com.iot.device.service.impl;

import com.iot.device.mapper.ProductTimerMapper;
import com.iot.device.service.IProductTimerService;
import com.iot.device.vo.req.ProductTimerReq;
import com.iot.device.vo.rsp.ProductTimerResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：设备
 * 功能描述：产品定时配置
 * 创建人： maochengyuan
 * 创建时间：2018/11/23 10:46
 * 修改人： maochengyuan
 * 修改时间：2018/11/23 10:46
 * 修改描述：
 */
@Service
public class ProductTimerServiceImpl implements IProductTimerService{

    @Autowired
    private ProductTimerMapper productTimerMapper;

    /**
     * 描述：保存产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:35
     * @param timers 配置信息
     * @return int
     */
    @Override
    public int addProductTimer(List<ProductTimerReq> timers) {
        return this.productTimerMapper.addProductTimer(timers);
    }

    /**
     * 描述：删除产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:08
     * @param productId 产品ID
     * @return int
     */
    @Override
    public int delProductTimer(Long productId) {
        return this.productTimerMapper.delProductTimer(productId);
    }

    /**
     * 描述：查询产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:13
     * @param productId 产品ID
     * @return java.util.List<com.iot.device.vo.rsp.ProductTimerResp>
     */
    @Override
    public List<ProductTimerResp> getProductTimer(Long productId) {
        return this.productTimerMapper.getProductTimer(productId);
    }

    /**
     * 描述：查询产品定时配置-批量
     * @author maochengyuan
     * @created 2018/11/23 10:13
     * @param productIds 产品ID
     * @return java.util.Map<java.lang.Long,java.util.List<com.iot.device.vo.rsp.ProductTimerResp>>
     */
    @Override
    public Map<Long, List<ProductTimerResp>> getProductTimers(List<Long> productIds){
        List<ProductTimerResp> list = this.productTimerMapper.getProductTimers(productIds);
        if(ObjectUtils.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(ProductTimerResp::getProductId));
    }

}
