package com.iot.tenant.service;

import com.iot.tenant.domain.AppProduct;
import com.baomidou.mybatisplus.service.IService;
import com.iot.tenant.vo.resp.AppProductResp;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * app关联产品 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
public interface IAppProductService extends IService<AppProduct> {

    List<AppProductResp> productByAppIdAndTenantId(Long appId,Long tenantId);

    Integer countAppProductByproductId(Long productId);

    /**
      * @despriction：查找绑定此产品的appId
      * @author  yeshiyuan
      * @created 2018/12/15 16:01
      * @params [productId, tenantId]
      * @return java.util.List<java.lang.Long>
      */
    List<Long> findAppIdByProductId(Long productId, Long tenantId);

    /**
      * @despriction：删除产品绑定app关系数据
      * @author  yeshiyuan
      * @created 2018/12/15 16:29
      * @params [productId, tenantId]
      * @return int
      */
    void deleteByProductId(Long productId, Long tenantId);
}
