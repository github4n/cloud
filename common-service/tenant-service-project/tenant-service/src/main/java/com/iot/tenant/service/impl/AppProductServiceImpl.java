package com.iot.tenant.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.tenant.domain.AppProduct;
import com.iot.tenant.mapper.AppProductMapper;
import com.iot.tenant.service.IAppProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.tenant.vo.resp.AppProductResp;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * app关联产品 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
@Service
public class AppProductServiceImpl extends ServiceImpl<AppProductMapper, AppProduct> implements IAppProductService {

    @Override
    public List<AppProductResp> productByAppIdAndTenantId(Long appId, Long tenantId) {
        EntityWrapper<AppProduct> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id",tenantId);
        wrapper.eq("app_id", appId);
        List<AppProduct> list = super.selectList(wrapper);
        List<AppProductResp> respList = Lists.newArrayList();
        for (AppProduct vo : list) {
            AppProductResp resp = new AppProductResp();
            resp.setProductId(vo.getProductId());
            resp.setId(vo.getId());
            respList.add(resp);
        }
        return respList;
    }

    @Override
    public Integer countAppProductByproductId(Long productId) {
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.eq("product_id", productId);
        return this.selectCount(wrapper);
    }

    /**
     * @despriction：查找绑定此产品的appId
     * @author  yeshiyuan
     * @created 2018/12/15 16:01
     * @params [productId, tenantId]
     * @return java.util.List<java.lang.Long>
     */
    @Override
    public List<Long> findAppIdByProductId(Long productId, Long tenantId) {
        List<Long> appIds = new ArrayList<>();
        EntityWrapper<AppProduct> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id",tenantId);
        wrapper.eq("product_id", productId);
        List<AppProduct> list = super.selectList(wrapper);
        if (list != null && !list.isEmpty()) {
            appIds = list.stream().map(AppProduct::getAppId).collect(Collectors.toList());
        }
        return appIds;
    }

    /**
     * @despriction：删除产品绑定app关系数据
     * @author  yeshiyuan
     * @created 2018/12/15 16:29
     * @params [productId, tenantId]
     * @return int
     */
    @Override
    public void deleteByProductId(Long productId, Long tenantId) {
        EntityWrapper<AppProduct> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id",tenantId);
        wrapper.eq("product_id", productId);
        super.delete(wrapper);
    }
}
