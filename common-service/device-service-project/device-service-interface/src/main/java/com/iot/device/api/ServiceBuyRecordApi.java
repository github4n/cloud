package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.service.CreateServiceBuyRecordReq;
import com.iot.device.vo.req.service.ServiceBuyRecordPageReq;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.req.servicebuyrecordreq.ServiceBuyRecordReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录api
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 14:39
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 14:39
 * 修改描述：
 */
@Api(tags = "虚拟服务购买记录api")
@RequestMapping(value = "/serviceBuyRecord")
@FeignClient(value = "device-service")
public interface ServiceBuyRecordApi {

    /**
      * @despriction：创建记录
      * @author  yeshiyuan
      * @created 2018/9/13 15:27
      * @return
      */
    @ApiOperation(value = "创建记录", notes = "创建记录")
    @RequestMapping(value = "/createRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int createRecord(@RequestBody CreateServiceBuyRecordReq createServiceBuyRecordReq);

    /**
     * 描述：查询虚拟服务购买记录
     * @author maochengyuan
     * @created 2018/9/13 16:59
     * @param serviceId 服务Id
     * @param goodsTypeId 商品类别
     * @param tenantId 租户Id
     * @return int
     */
    @ApiOperation(value = "查询虚拟服务购买记录", notes = "查询虚拟服务购买记录")
    @RequestMapping(value = "/getServiceBuyRecord", method = RequestMethod.GET)
    List<ServiceBuyRecordResp> getServiceBuyRecord(@RequestParam("serviceId") Long serviceId, @RequestParam("goodsTypeId") Integer goodsTypeId, @RequestParam("tenantId") Long tenantId);

    /**
     * 描述：查询虚拟服务购买记录详情
     * @author maochengyuan
     * @created 2018/9/13 16:59
     * @param serviceId 服务Id
     * @param goodsId 商品类别
     * @param tenantId 租户Id
     * @return int
     */
    @ApiOperation(value = "查询虚拟服务购买记录详情", notes = "查询虚拟服务购买记录详情")
    @RequestMapping(value = "/getServiceBuyRecordDetail", method = RequestMethod.GET)
    ServiceBuyRecordResp getServiceBuyRecordDetail(@RequestParam("serviceId") Long serviceId, @RequestParam("goodsId") Long goodsId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：修改支付状态
      * @author  yeshiyuan
      * @created 2018/9/13 17:00
      * @return
      */
    @ApiOperation(value = "修改支付状态", notes = "修改支付状态")
    @RequestMapping(value = "/updatePayStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updatePayStatus(@RequestBody UpdateServicePayStatusReq req);

    /**
      * @despriction：根据订单号查找服务购买记录信息
      * @author  yeshiyuan
      * @created 2018/9/14 11:25
      * @return
      */
    @ApiOperation(value = "根据订单号查找服务购买记录信息", notes = "根据订单号查找服务购买记录信息")
    @RequestMapping(value = "/getServiceBuyRecordByOrderId", method = RequestMethod.GET)
    ServiceBuyRecordResp getServiceBuyRecordByOrderId(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId);
    
    /**
     * 
     * 描述：根据服务ID和商品类别id获取订单号ID
     * @author 李帅
     * @created 2018年10月29日 下午3:39:06
     * @since 
     * @param serviceId
     * @param goodsTypeId
     * @return
     */
   @ApiOperation(value = "根据服务ID和商品类别id获取订单号ID", notes = "根据服务ID和商品类别id获取订单号ID")
   @RequestMapping(value = "/getOrderIdByServiceIdAndGoodsTypeId", method = RequestMethod.GET)
   String getOrderIdByServiceIdAndGoodsTypeId(@RequestParam("serviceId") Long serviceId, @RequestParam("productId") Long productId, @RequestParam("goodsTypeId") Integer goodsTypeId);

    /**
     * @description: 根据goodsIds 获取查询虚拟服务购买记录
     * @author wucheng
     * @param goodsIds
     * @return
     */
    @ApiOperation(value = "根据goodsIds分页获取查询虚拟服务购买记录", notes = "根据goodsIds分页获取查询虚拟服务购买记录")
    @RequestMapping(value = "/getServiceBuyRecordByGoodsIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ServiceBuyRecordResp> getServiceBuyRecordByGoodsIds(@RequestBody ServiceBuyRecordReq req);

    /**
      * @despriction：分页查询
      * @author  yeshiyuan
      * @created 2018/11/14 15:11
      * @return
      */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ServiceBuyRecordResp> pageQuery(@RequestBody ServiceBuyRecordPageReq pageReq);

    /**
      * @despriction：校验是否已创建过此订单
      * @author  yeshiyuan
      * @created 2018/11/15 19:49
      * @return
      */
    @ApiOperation(value = "/校验是否已创建过此订单", notes = "校验是否已创建过此订单")
    @RequestMapping(value = "/checkIsCreate", method = RequestMethod.GET)
    boolean checkIsCreate(@RequestParam("tenantId") Long tenantId, @RequestParam("serviceId")Long serviceId,@RequestParam("goodsId") Long goodsId);
}
