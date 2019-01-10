package com.iot.payment.api;

import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.vo.order.req.CreateOrderRecordReq;
import com.iot.payment.vo.order.resp.OrderDetailInfoResp;
import com.iot.payment.vo.order.resp.VideoPlanTypeResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * 项目名称：cloud
 * 功能描述：订单操作api
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 13:58
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 13:58
 * 修改描述：
 */
@Api(value = "订单操作api", description = "订单操作api")
@FeignClient(value = "payment-service")
@RequestMapping("/api/service/order")
public interface OrderApi {

    /**
      * @despriction：创建订单记录，
      * @author  yeshiyuan
      * @created 2018/7/3 14:10
      * @return 订单id
      */
    @ApiOperation(value = "创建订单记录", notes = "创建订单记录")
    @RequestMapping(value = "/createOrderRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    String createOrderRecord(@RequestBody CreateOrderRecordReq createOrderRecordReq);

    /** 
     * 描述：编辑UUID订单
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    @ApiOperation(value = "编辑UUID订单", notes = "编辑UUID订单-修改数量")
    @RequestMapping(value = "/editOrderCreateNum", method = RequestMethod.GET)
    void editOrderCreateNum(@RequestParam("orderId") String orderId, @RequestParam("createNum") Integer createNum);

    /**
      * @despriction：获取订单记录信息
      * @author  yeshiyuan
      * @created 2018/7/4 15:43
      * @param orderId 订单id
      * @param 租户id 租户id
      * @return
      */
    @ApiOperation(value = "获取订单记录信息", notes = "获取订单记录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "string", required = true),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "long", required = true)
    })
    @RequestMapping(value = "/getOrderRecord", method = RequestMethod.GET)
    OrderRecord getOrderRecord(@RequestParam("orderId") String orderId,@RequestParam("tenantId") Long tenantId);

    /**
     * 
     * 描述：依据订单id批量获取订单信息
     * @author 李帅
     * @created 2018年11月14日 上午9:51:09
     * @since 
     * @param orderIds
     * @return
     */
    @ApiOperation(value = "获取订单记录信息", notes = "获取订单记录信息")
    @RequestMapping(value = "/getOrderRecordByOrderIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, OrderRecord> getOrderRecordByOrderIds(@RequestBody List<String> orderIds);
    
    /**
      * @despriction：获取订单下的商品
      * @author  yeshiyuan
      * @created 2018/7/4 16:09
      * @param orderId 订单id
      * @param 租户id 租户id
      * @return
      */
    @ApiOperation(value = "获取订单下的商品id", notes = "获取订单下的商品id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "string", required = true),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "long", required = true)
    })
    @RequestMapping(value = "/getOrderGoodsId", method = RequestMethod.GET)
    List<Long> getOrderGoodsIds(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：修改订单状态
      * @author  yeshiyuan
      * @created 2018/7/4 16:59
      * @param orderId 订单id
      * @param tenantId 租户id
      * @param orderStatus 订单状态
      * @return
      */
    @ApiOperation(value = "修改订单状态", notes = "修改订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "string", required = true),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "long", required = true),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态", dataType = "int", required = true),
            @ApiImplicitParam(name = "oldOrderStatus", value = "旧订单状态", dataType = "int", required = true)
    })
    @RequestMapping(value = "/updateOrderRecordStatus", method = RequestMethod.POST)
    int updateOrderRecordStatus(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId,
                                @RequestParam("orderStatus") Integer orderStatus,@RequestParam("oldOrderStatus")Integer oldOrderStatus);

    /**
      * @despriction：查询订单详情
      * @author  yeshiyuan
      * @created 2018/7/5 11:07
      * @param orderId 订单id
      * @param tenantId 租户id
      * @return
      */
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "long", required = true, paramType = "query")
    })
    @RequestMapping(value = "/getOrderDetailInfo", method = RequestMethod.GET)
    OrderDetailInfoResp getOrderDetailInfo(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：查询视频计划类型、容量
      * @author  yeshiyuan
      * @created 2018/7/17 15:39
      * @param goodsId 订单对应的原商品id
      * @return
      */
    @ApiOperation(value = "查询视频计划类型、容量", notes = "查询视频计划类型、容量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "oriGoodsId", value = "订单下的原商品id", dataType = "Long", required = true, paramType = "query")
    })
    @RequestMapping(value = "/getVideoPlanType", method = RequestMethod.GET)
    VideoPlanTypeResp getVideoPlanType(@RequestParam("orderId") String orderId, @RequestParam("oriGoodsId") Long oriGoodsId);
}
