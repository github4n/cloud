package com.iot.boss.controller;

import com.iot.boss.service.order.IOrderManagerService;
import com.iot.boss.service.refund.impl.AppRefundProcesser;
import com.iot.boss.service.refund.impl.UUIDRefundProcesser;
import com.iot.boss.service.refund.impl.VoiceAccessRefundProcesser;
import com.iot.boss.vo.order.req.AppOrderPageReq;
import com.iot.boss.vo.refund.req.RefundReq;
import com.iot.boss.vo.uuid.GetUUIDRefundReq;
import com.iot.boss.vo.voiceaccess.VoiceAccessReq;
import com.iot.common.beans.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：cloud
 * 功能描述：订单管理ctrl
 * 创建人： yeshiyuan
 * 创建时间：2018/11/14 9:21
 * 修改人： yeshiyuan
 * 修改时间：2018/11/14 9:21
 * 修改描述：
 */
@Api(tags = "订单管理ctrl")
@RestController
@RequestMapping(value = "/api/order")
public class OrderManagerController {

    @Autowired
    private AppRefundProcesser appRefundProcesser;

    @Autowired
    private VoiceAccessRefundProcesser voiceAccessRefundProcesser;

    @Autowired
    private UUIDRefundProcesser uuidRefundProcesser;

    @Autowired
    private IOrderManagerService orderManagerService;

    /**
      * @despriction：app列表（只加载支付成功、退款中、退款成功、退款失败的订单）
      * @author  yeshiyuan
      * @created 2018/11/14 9:29
      * @return
      */
    @ApiOperation(value = "app列表（只加载支付成功、退款中、退款成功、退款失败的订单）", notes = "app列表（只加载支付成功、退款中、退款成功、退款失败的订单）")
    @RequestMapping(value = "/appList", method = RequestMethod.POST)
    public CommonResponse appList(@RequestBody AppOrderPageReq appOrderPageReq) {
        return CommonResponse.success(orderManagerService.appList(appOrderPageReq));
    }

    /**
     * @despriction：产品第三方接入列表（只加载支付成功、退款中、退款成功、退款失败的订单）
     * @author  yeshiyuan
     * @created 2018/11/14 9:29
     * @return
     */
    @ApiOperation(value = "产品第三方接入列表", notes = "（只加载支付成功、退款中、退款成功、退款失败的订单）")
    @RequestMapping(value = "/voiceAccessList", method = RequestMethod.POST)
    public CommonResponse voiceAccessList(@RequestBody VoiceAccessReq req) {
        return CommonResponse.success(orderManagerService.voiceAccessList(req));
    }

    /**
     *@description 第三方产品接入类型列表
     *@author wucheng
     *@params []
     *@create 2018/12/24 14:14
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "第三方产品接入类型列表", notes = "第三方产品接入类型列表")
    @RequestMapping(value = "/getVoiceAccessType", method = RequestMethod.GET)
    public CommonResponse getVoiceAccessType() {
        return CommonResponse.success(orderManagerService.getVoiceAccessType());
    }

    /**
     * @despriction：uuid列表（只加载支付成功、退款中、退款成功、退款失败的订单）
     * @author  yeshiyuan
     * @created 2018/11/14 9:29
     * @return
     */
    @ApiOperation(value = "uuid列表", notes = "（只加载支付成功、退款中、退款成功、退款失败的订单）")
    @RequestMapping(value = "/uuidList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse uuidList(@RequestBody GetUUIDRefundReq uuidOrderReq) {
        return CommonResponse.success(orderManagerService.uuidList(uuidOrderReq));
    }


    /**
      * @despriction：app退款
      * @author  yeshiyuan
      * @created 2018/11/14 10:01
      * @param null
      * @return
      */
    @ApiOperation(value = "app退款", notes = "app退款")
    @RequestMapping(value = "/appRefund", method = RequestMethod.POST)
    public CommonResponse appRefund(@RequestBody RefundReq refundReq) {
        String message = appRefundProcesser.execute(refundReq);
        return CommonResponse.success(message);
    }

    /**
     * @despriction：uuid退款
     * @author  yeshiyuan
     * @created 2018/11/14 10:01
     * @param null
     * @return
     */
    @ApiOperation(value = "uuid退款", notes = "uuid退款")
    @RequestMapping(value = "/uuidRefund", method = RequestMethod.POST)
    public CommonResponse uuidRefund(@RequestBody RefundReq refundReq) {
        String message = uuidRefundProcesser.execute(refundReq);
        return CommonResponse.success(message);
    }

    /**
     * @despriction：语音接入退款
     * @author  yeshiyuan
     * @created 2018/11/14 10:01
     * @param null
     * @return
     */
    @ApiOperation(value = "语音接入退款", notes = "语音接入退款")
    @RequestMapping(value = "/voiceAccessRefund", method = RequestMethod.POST)
    public CommonResponse voiceAccessRefund(@RequestBody RefundReq refundReq) {
        String message = voiceAccessRefundProcesser.execute(refundReq);
        return CommonResponse.success(message);
    }


}
