package com.iot.device.service;

import com.iot.common.helper.Page;
import com.iot.device.model.ServiceBuyRecord;
import com.iot.device.vo.req.service.ServiceBuyRecordPageReq;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.req.servicebuyrecordreq.ServiceBuyRecordReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录service
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 14:54
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 14:54
 * 修改描述：
 */
public interface IServiceBuyRecordService {

    /**
     * 描述：查询虚拟服务购买记录
     * @author maochengyuan
     * @created 2018/9/13 17:49
     * @param serviceId 虚拟服务ID
     * @param goodsTypeId 商品类别ID
     * @param tenantId 租户ID
     * @return java.util.List<com.iot.device.vo.rsp.ServiceBuyRecordResp>
     */
    List<ServiceBuyRecordResp> getServiceBuyRecord(Long serviceId, Integer goodsTypeId, Long tenantId);

    /**
     * 描述：查询虚拟服务购买记录详情
     * @author maochengyuan
     * @created 2018/9/13 17:49
     * @param serviceId 虚拟服务ID
     * @param goodsId 商品ID
     * @param tenantId 租户ID
     * @return com.iot.device.vo.rsp.ServiceBuyRecordResp
     */
    ServiceBuyRecordResp getServiceBuyRecordDetail(Long serviceId, Long goodsId, Long tenantId);


    /**
     * @despriction：创建记录
     * @author  yeshiyuan
     * @created 2018/9/13 15:27
     * @return
     */
    int createRecord(ServiceBuyRecord serviceBuyRecord);

    /**
      * @despriction：校验是否创建过
      * @author  yeshiyuan
      * @created 2018/9/13 16:29
      * @return
      */
    boolean checkIsCreate(Long tenantId, Long serviceId, Long goodsId);

    /**
     * @despriction：修改支付状态
     * @author  yeshiyuan
     * @created 2018/9/13 17:00
     * @return
     */
    int updatePayStatus(UpdateServicePayStatusReq req);

    /**
     * @despriction：根据订单号查找服务购买记录信息
     * @author  yeshiyuan
     * @created 2018/9/14 11:25
     * @return
     */
    ServiceBuyRecordResp getServiceBuyRecordByOrderId(String orderId, Long tenantId);
    
    /**
     * 
     * 描述：根据服务ID和商品类别id获取订单号ID
     * @author 李帅
     * @created 2018年10月29日 下午3:42:09
     * @since 
     * @param serviceId
     * @param goodsTypeId
     * @return
     */
    String getOrderIdByServiceIdAndGoodsTypeId(Long serviceId, Long productId, Integer goodsTypeId);

    /**
     * @description:  订单管理-根据goodsIds分页获取查询虚拟服务购买记录
     * @author wucheng
     * @param req
     * @create 2018-11-13 20:10:33
     * @return  Page<ServiceBuyRecordResp>
     */
    Page<ServiceBuyRecordResp> getServiceBuyRecordByGoodsIds(ServiceBuyRecordReq req);

    /**
     * @despriction：分页查询
     * @author  yeshiyuan
     * @created 2018/11/14 15:11
     * @return
     */
    Page<ServiceBuyRecordResp> pageQuery(ServiceBuyRecordPageReq pageReq);


}
