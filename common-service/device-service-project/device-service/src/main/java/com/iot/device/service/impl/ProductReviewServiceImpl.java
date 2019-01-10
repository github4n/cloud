package com.iot.device.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.mapper.ProductReviewRecordMapper;
import com.iot.device.model.ProductReviewRecord;
import com.iot.device.service.IProductReviewRecordService;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：产品审核记录service impl
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 16:49
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 16:49
 * 修改描述：
 */
@Service
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewRecordMapper, ProductReviewRecord> implements IProductReviewRecordService {

    @Autowired
    private ProductReviewRecordMapper productReviewRecordMapper;

    @Override
    public Long add(ProductReviewRecord productReviewRecord) {
        super.insert(productReviewRecord);
        return productReviewRecord.getId();
    }

    /**
     * @despriction：获取审核记录
     * @author  yeshiyuan
     * @created 2018/10/24 17:52
     * @return
     */
    @Override
    public List<ProductReviewRecordResp> getReviewRecord(Long productId) {
        return productReviewRecordMapper.getReviewRecord(productId);
    }

    /**
     * @despriction：获取租户id
     * @author  yeshiyuan
     * @created 2018/11/2 13:52
     * @return
     */
    @Override
    public Long getTenantIdById(Long id) {
        return productReviewRecordMapper.getTenantById(id);
    }
}
