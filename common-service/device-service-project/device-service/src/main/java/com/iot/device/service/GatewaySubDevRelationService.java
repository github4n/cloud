package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.GatewaySubDevRelation;
import com.iot.device.vo.req.gatewaysubdev.GatewaySubDevRelationReq;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;

import java.util.List;

/**
 * @descrpiction: 最小网关子集-网关子设备关联
 * @author wucheng
 * @created 2018/11/9 14:11
 */
public interface GatewaySubDevRelationService extends IService<GatewaySubDevRelation> {
     /**
      * @descrpiction: 批量插入网关子设备关联信息
      * @author wucheng
      * @created 2018/11/9 15:47
      * @param
      * @return
      */
     int batchInsert(List<GatewaySubDevRelationReq> lists);
     /**
      * @descrpiction: 根据id批量删除网关子设备信息
      * @author wucheng
      * @created 2018/11/9 15:48
      * @param 
      * @return 
      */
     int deleteById(List<Long> ids);
     /**
      * @descrpiction: 根据网关产品id，获取该网关绑定的子设备信息
      * @author wucheng
      * @created 2018/11/9 15:48
      * @param 
      * @return 
      */
     List<GatewaySubDevRelationResp> getGatewaySubDevByParDevId(Long parDevId, Long tenantId);

     /**
      * @descrpiction: 根据网关产品id，获取该网关绑定的子设备信息
      * @author wucheng
      * @created 2018/11/9 15:48
      * @param
      * @return
      */
     List<GatewaySubDevRelationResp> getGatewaySubDevByParDevIds(List parDevIds, Long tenantId);

     /**
       * @despriction：父产品id
       * @author  yeshiyuan
       * @created 2018/11/29 14:26
       */
     List<Long> parentProductIds(Long productId, Long tenantId);
}
