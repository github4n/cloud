package com.iot.device.controller;

import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.api.ServiceBuyRecordApi;
import com.iot.device.enums.ServicePayStatusEnum;
import com.iot.device.exception.ServiceBuyRecordExceptionEnum;
import com.iot.device.model.ServiceBuyRecord;
import com.iot.device.service.IServiceBuyRecordService;
import com.iot.device.vo.req.service.CreateServiceBuyRecordReq;
import com.iot.device.vo.req.service.ServiceBuyRecordPageReq;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.req.servicebuyrecordreq.ServiceBuyRecordReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录接口
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 15:30
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 15:30
 * 修改描述：
 */
@RestController
public class ServiceBuyRecordController implements ServiceBuyRecordApi {

    @Autowired
    private IServiceBuyRecordService serviceBuyRecordService;

    /**
     * @return
     * @despriction：创建记录
     * @author yeshiyuan
     * @created 2018/9/13 15:27
     */
    @Override
    public int createRecord(@RequestBody CreateServiceBuyRecordReq recordReq) {
        CreateServiceBuyRecordReq.check(recordReq);
        //去重，防止提交
        if (serviceBuyRecordService.checkIsCreate(recordReq.getTenantId(), recordReq.getServiceId(), recordReq.getGoodsId())) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.HAD_CREATE);
        }
        ServiceBuyRecord serviceBuyRecord = new ServiceBuyRecord();
        BeanUtil.copyProperties(recordReq, serviceBuyRecord);
        serviceBuyRecord.setCreateBy(recordReq.getUserId());
        serviceBuyRecord.setPayStatus(ServicePayStatusEnum.WAIT_PAY.getValue());
        return serviceBuyRecordService.createRecord(serviceBuyRecord);
    }


    /**
     * @return
     * @despriction：修改支付状态
     * @author yeshiyuan
     * @created 2018/9/13 17:00
     */
    @Override
    public int updatePayStatus(@RequestBody UpdateServicePayStatusReq req) {
        UpdateServicePayStatusReq.check(req);
        return serviceBuyRecordService.updatePayStatus(req);
    }

    /**
     * 描述：查询虚拟服务购买记录
     *
     * @param serviceId   虚拟服务ID
     * @param goodsTypeId 商品类别ID
     * @param tenantId    租户ID
     * @return java.util.List<com.iot.device.vo.rsp.ServiceBuyRecordResp>
     * @author maochengyuan
     * @created 2018/9/13 17:49
     */
    @Override
    public List<ServiceBuyRecordResp> getServiceBuyRecord(@RequestParam(value = "serviceId") Long serviceId, @RequestParam(value = "goodsTypeId") Integer goodsTypeId, @RequestParam(value = "tenantId") Long tenantId) {
        return this.serviceBuyRecordService.getServiceBuyRecord(serviceId, goodsTypeId, tenantId);
    }

    /**
     * 描述：查询虚拟服务购买记录详情
     *
     * @param serviceId 虚拟服务ID
     * @param goodsId   商品ID
     * @param tenantId  租户ID
     * @return com.iot.device.vo.rsp.ServiceBuyRecordResp
     * @author maochengyuan
     * @created 2018/9/13 17:49
     */
    @Override
    public ServiceBuyRecordResp getServiceBuyRecordDetail(@RequestParam(value = "serviceId") Long serviceId, @RequestParam(value = "goodsId") Long goodsId, @RequestParam(value = "tenantId") Long tenantId) {
        return this.serviceBuyRecordService.getServiceBuyRecordDetail(serviceId, goodsId, tenantId);
    }

    /**
     * @return
     * @despriction：根据订单号查找服务购买记录信息
     * @author yeshiyuan
     * @created 2018/9/14 11:25
     */
    @Override
    public ServiceBuyRecordResp getServiceBuyRecordByOrderId(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId) {
        return this.serviceBuyRecordService.getServiceBuyRecordByOrderId(orderId, tenantId);
    }

    /**
     * 描述：根据服务ID和商品类别id获取订单号ID
     *
     * @param serviceId
     * @param productId
     * @param goodsTypeId
     * @return
     * @author 李帅
     * @created 2018年10月29日 下午3:39:14
     * @since
     */
    @Override
    public String getOrderIdByServiceIdAndGoodsTypeId(@RequestParam("serviceId") Long serviceId, @RequestParam("productId") Long productId, @RequestParam("goodsTypeId") Integer goodsTypeId) {
        return this.serviceBuyRecordService.getOrderIdByServiceIdAndGoodsTypeId(serviceId, productId, goodsTypeId);
    }

    /**
     * @param req
     * @return List<ServiceBuyRecordResp>
     * @description: 根据goodsIds分页获取查询虚拟服务购买记录
     * @author wucheng
     * @create 2018-11-13 20:10:33
     */
    @Override
    public Page<ServiceBuyRecordResp> getServiceBuyRecordByGoodsIds(@RequestBody ServiceBuyRecordReq req) {
        return serviceBuyRecordService.getServiceBuyRecordByGoodsIds(req);
    }

    /**
     * @despriction：分页查询
     * @author  yeshiyuan
     * @created 2018/11/14 15:11
     * @return
     */
    @Override
    public Page<ServiceBuyRecordResp> pageQuery(@RequestBody ServiceBuyRecordPageReq pageReq) {
        return serviceBuyRecordService.pageQuery(pageReq);
    }

    @Override
    public boolean checkIsCreate(Long tenantId, Long serviceId, Long goodsId) {
        return serviceBuyRecordService.checkIsCreate(tenantId, serviceId, goodsId);
    }
}
