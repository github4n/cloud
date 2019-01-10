package com.iot.device.service;

import com.iot.device.model.ProductReviewRecord;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：产品审核记录service
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 16:48
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 16:48
 * 修改描述：
 */
public interface IProductReviewRecordService {

    /**
      * @despriction：添加产品审核记录
      * @author  yeshiyuan
      * @created 2018/10/24 17:15
      * @return
      */
    Long add(ProductReviewRecord productReviewRecord);

    /**
      * @despriction：获取审核记录
      * @author  yeshiyuan
      * @created 2018/10/24 17:52
      * @return
      */
    List<ProductReviewRecordResp> getReviewRecord(Long productId);

    /**
     * @despriction：获取租户id
     * @author  yeshiyuan
     * @created 2018/11/2 13:52
     * @return
     */
    Long getTenantIdById(Long id);
}
