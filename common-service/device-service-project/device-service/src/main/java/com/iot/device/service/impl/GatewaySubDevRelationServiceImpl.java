package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.beans.BeanUtil;
import com.iot.device.mapper.GatewaySubDevRelationMapper;
import com.iot.device.model.GatewaySubDevRelation;
import com.iot.device.service.GatewaySubDevRelationService;
import com.iot.device.vo.req.gatewaysubdev.GatewaySubDevRelationReq;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @descrpiction: 最小网关子集-网关子设备关联实现类
 * @author wucheng
 * @created 2018/11/9 14:12
 */
@Service
@Transactional
public class GatewaySubDevRelationServiceImpl extends ServiceImpl<GatewaySubDevRelationMapper,GatewaySubDevRelation> implements GatewaySubDevRelationService{

    @Autowired
    private GatewaySubDevRelationMapper gatewaySubDevRelationMapper;

    @Override
    public int batchInsert(List<GatewaySubDevRelationReq> lists) {
        int result = 0;
        if (lists != null && lists.size() > 0) {
            for (GatewaySubDevRelationReq item : lists) {
                result += gatewaySubDevRelationMapper.insert(item);
            }
        }
        return result;
    }

    @Override
    public int deleteById(List<Long> ids) {
        return gatewaySubDevRelationMapper.deleteById(ids);
    }

    @Override
    public List<GatewaySubDevRelationResp> getGatewaySubDevByParDevId(Long parDevId,  Long tenantId) {
        return gatewaySubDevRelationMapper.getGatewaySubDevByParDevId(parDevId, tenantId);
    }

    @Override
    public List<GatewaySubDevRelationResp> getGatewaySubDevByParDevIds(List parDevIds, Long tenantId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",tenantId);
        wrapper.in("pardev_id",parDevIds);
        List<GatewaySubDevRelation> gatewaySubDevRelation = super.selectList(wrapper);
        List<GatewaySubDevRelationResp> result = new ArrayList<>();
        gatewaySubDevRelation.forEach(m->{
            GatewaySubDevRelationResp gatewaySubDevRelationResp = new GatewaySubDevRelationResp();
            gatewaySubDevRelationResp.setTenantId(m.getTenantId());
            gatewaySubDevRelationResp.setParDevId(m.getParDevId());
            gatewaySubDevRelationResp.setSubDevId(m.getSubDevId());
            gatewaySubDevRelationResp.setCreateBy(m.getCreateBy());
            gatewaySubDevRelationResp.setCreateTime(m.getCreateTime());
            gatewaySubDevRelationResp.setUpdateBy(m.getUpdateBy());
            gatewaySubDevRelationResp.setUpdateTime(m.getUpdateTime());
            gatewaySubDevRelationResp.setIsDeleted(m.getIsDeleted());
            result.add(gatewaySubDevRelationResp);
        });
        return result;
    }

    /**
     * @despriction：父产品id
     * @author  yeshiyuan
     * @created 2018/11/29 14:26
     */
    @Override
    public List<Long> parentProductIds(Long productId, Long tenantId) {
        return gatewaySubDevRelationMapper.parentProductIds(productId, tenantId);
    }
}
