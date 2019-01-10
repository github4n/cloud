package com.iot.device.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.mapper.ServiceBuyRecordMapper;
import com.iot.device.model.ServiceBuyRecord;
import com.iot.device.service.IServiceBuyRecordService;
import com.iot.device.vo.req.service.ServiceBuyRecordPageReq;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.req.servicebuyrecordreq.ServiceBuyRecordReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录serviceImpl
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 14:56
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 14:56
 * 修改描述：
 */
@Service
public class ServiceBuyRecordServiceImpl implements IServiceBuyRecordService {

    @Autowired
    private ServiceBuyRecordMapper serviceBuyRecordMapper;

    /**
     * 描述：查询虚拟服务购买记录
     * @author maochengyuan
     * @created 2018/9/13 17:49
     * @param serviceId 虚拟服务ID
     * @param goodsTypeId 商品类别ID
     * @param tenantId 租户ID
     * @return java.util.List<com.iot.device.vo.rsp.ServiceBuyRecordResp>
     */
    @Override
    public List<ServiceBuyRecordResp> getServiceBuyRecord(Long serviceId, Integer goodsTypeId, Long tenantId) {
        return this.serviceBuyRecordMapper.getServiceBuyRecord(serviceId, goodsTypeId, tenantId);
    }

    /**
     * 描述：查询虚拟服务购买记录详情
     * @author maochengyuan
     * @created 2018/9/13 17:49
     * @param serviceId 虚拟服务ID
     * @param goodsId 商品ID
     * @param tenantId 租户ID
     * @return com.iot.device.vo.rsp.ServiceBuyRecordResp
     */
    @Override
    public ServiceBuyRecordResp getServiceBuyRecordDetail(Long serviceId, Long goodsId, Long tenantId) {
        return this.serviceBuyRecordMapper.getServiceBuyRecordDetail(serviceId, goodsId, tenantId);
    }

    /**
     * @despriction：创建记录
     * @author  yeshiyuan
     * @created 2018/9/13 15:27
     * @return
     */
    @Override
    public int createRecord(ServiceBuyRecord serviceBuyRecord) {
        return serviceBuyRecordMapper.insert(serviceBuyRecord);
    }

    /**
     * @despriction：校验是否创建过
     * @author  yeshiyuan
     * @created 2018/9/13 16:29
     * @return
     */
    @Override
    public boolean checkIsCreate(Long tenantId, Long serviceId, Long goodsId) {
        int count = serviceBuyRecordMapper.checkIsCreate(tenantId, serviceId, goodsId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * @despriction：修改支付状态
     * @author  yeshiyuan
     * @created 2018/9/13 17:00
     * @return
     */
    @Override
    public int updatePayStatus(UpdateServicePayStatusReq req) {
        return serviceBuyRecordMapper.updatePayStatus(req, new Date());
    }

    /**
     * @despriction：根据订单号查找服务购买记录信息
     * @author  yeshiyuan
     * @created 2018/9/14 11:25
     * @return
     */
    @Override
    public ServiceBuyRecordResp getServiceBuyRecordByOrderId(String orderId, Long tenantId) {
        return serviceBuyRecordMapper.getServiceBuyRecordByOrderId(orderId, tenantId);
    }
    
    /**
     * 
     * 描述：根据服务ID和商品类别id获取订单号ID
     * @author 李帅
     * @created 2018年10月29日 下午3:42:16
     * @since 
     * @param serviceId
     * @param goodsTypeId
     * @return
     */
    @Override
    public String getOrderIdByServiceIdAndGoodsTypeId(Long serviceId, Long productId, Integer goodsTypeId) {
        return serviceBuyRecordMapper.getOrderIdByServiceIdAndGoodsTypeId(serviceId, productId, goodsTypeId);
    }
    /**
     * @description: 根据goodsIds 获取查询虚拟服务购买记录
     * @author wucheng
     * @param req
     * @create 2018-11-13 20:10:33
     * @return  Page<ServiceBuyRecordResp>
     */
    @Override
    public Page<ServiceBuyRecordResp> getServiceBuyRecordByGoodsIds(ServiceBuyRecordReq req) {
        Page<ServiceBuyRecordResp> resp =  new Page<>();
        // 获取分页信息
        if (req.getPageNum() == null) {
            req.setPageNum(1);
        }
        if (req.getPageSize() == null) {
            req.setPageSize(10);
        }
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();

        com.baomidou.mybatisplus.plugins.Page<ServiceBuyRecordReq> selectPage = new  com.baomidou.mybatisplus.plugins.Page<>(req.getPageNum(), req.getPageSize());
        // 返回结果集
        List<ServiceBuyRecordResp> serviceBuyRecordList = serviceBuyRecordMapper.getServiceBuyRecordByGoodsIds(selectPage, req);
        resp.setResult(serviceBuyRecordList);
        resp.setTotal(selectPage.getTotal());
        resp.setPageNum(pageNum);
        resp.setPageSize(pageSize);
        resp.setPages(selectPage.getPages());
        return resp;
    }

    /**
     * @despriction：分页查询
     * @author  yeshiyuan
     * @created 2018/11/14 15:11
     * @return
     */
    @Override
    public Page<ServiceBuyRecordResp> pageQuery(ServiceBuyRecordPageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<ServiceBuyRecordResp> page =new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<ServiceBuyRecordResp> list = serviceBuyRecordMapper.pageQuery(page, pageReq);
        page.setRecords(list);
        com.iot.common.helper.Page<ServiceBuyRecordResp> myPage=new com.iot.common.helper.Page<>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page,myPage);
        return myPage;
    }

}
