package com.iot.device.api;

import com.iot.device.api.fallback.ProductTimerApiFallbackFactory;
import com.iot.device.vo.req.TimerConfigReq;
import com.iot.device.vo.rsp.ProductTimerResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：设备
 * 功能描述：产品定时配置
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/23 10:58
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/23 10:58
 * 修改描述：
 */
@Api(tags = "产品定时配置")
@FeignClient(value = "device-service", fallbackFactory = ProductTimerApiFallbackFactory.class)
@RequestMapping(value = "/productTimer")
public interface ProductTimerApi {

    /**
     * 描述：删除产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:08
     * @param productId 产品ID
     * @return void
     */
    @ApiOperation("删除产品定时配置")
    @RequestMapping(value = "/delProductTimer", method = RequestMethod.DELETE)
    void delProductTimer(@RequestParam("productId") Long productId);

    /**
     * 描述：新增产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:35
     * @param req 请求参数
     * @return void
     */
    @ApiOperation("保存产品定时配置（会先删除之前的配置）")
    @RequestMapping(value = "/uptProductTimer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void uptProductTimer(@RequestBody TimerConfigReq req);

    /**
     * 描述：新增产品定时配置
     * @author maochengyuan
     * @created 2018/11/23 10:35
     * @param req 请求参数
     * @return void
     */
    @ApiOperation("保存产品定时配置")
    @RequestMapping(value = "/addProductTimer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addProductTimer(@RequestBody TimerConfigReq req);

    /**
     * 描述：查询产品定时配置-单个
     * @author maochengyuan
     * @created 2018/11/23 10:13
     * @param productId 产品ID
     * @return java.util.List<com.iot.device.vo.rsp.ProductTimerResp>
     */
    @ApiOperation("查询产品定时配置-单个")
    @RequestMapping(value = "/getProductTimer", method = RequestMethod.GET)
    List<ProductTimerResp> getProductTimer(@RequestParam("productId") Long productId);

    /**
     * 描述：查询产品定时配置-批量
     * @author maochengyuan
     * @created 2018/11/23 17:45
     * @param productIds 产品ID
     * @return java.util.Map<java.lang.Long,java.util.List<com.iot.device.vo.rsp.ProductTimerResp>>
     */
    @ApiOperation("查询产品定时配置-批量")
    @RequestMapping(value = "/getProductTimers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<Long, List<ProductTimerResp>> getProductTimers(@RequestBody List<Long> productIds);

}
