package com.iot.device.service;

import com.iot.device.vo.req.ProductTimerReq;
import com.iot.device.vo.rsp.ProductTimerResp;

import java.util.List;
import java.util.Map;

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
public interface IProductTimerService {

    /**
     * 描述：保存产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:35
     * @param timers 配置信息
     * @return int
     */
    int addProductTimer(List<ProductTimerReq> timers);

    /**
     * 描述：删除产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:08
     * @param productId 产品ID
     * @return int
     */
    int delProductTimer(Long productId);

    /**
     * 描述：查询产品定时配置-单个
     * @author maochengyuan
     * @created 2018/11/23 10:13
     * @param productId 产品ID
     * @return java.util.List<com.iot.device.vo.rsp.ProductTimerResp>
     */
    List<ProductTimerResp> getProductTimer(Long productId);

    /**
     * 描述：查询产品定时配置-批量
     * @author maochengyuan
     * @created 2018/11/23 17:40
     * @param productIds 产品ID
     * @return java.util.Map<java.lang.Long,java.util.List<com.iot.device.vo.rsp.ProductTimerResp>>
     */
    Map<Long, List<ProductTimerResp>> getProductTimers(List<Long> productIds);

}
