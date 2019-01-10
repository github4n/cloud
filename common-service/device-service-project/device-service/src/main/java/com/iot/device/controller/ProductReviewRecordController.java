package com.iot.device.controller;

import com.iot.common.exception.BusinessException;
import com.iot.device.api.ProductReviewRecodApi;
import com.iot.device.core.service.ProductServiceCoreUtils;
import com.iot.device.enums.product.ProductAuditStatusEnum;
import com.iot.device.enums.product.ProductPublishStatusEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.model.Product;
import com.iot.device.model.ProductPublishHistory;
import com.iot.device.model.ProductReviewRecord;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.service.IOtaService;
import com.iot.device.service.IProductReviewRecordService;
import com.iot.device.service.IProductService;
import com.iot.device.vo.req.ota.FirmwareVersionUpdateVersionDto;
import com.iot.device.vo.req.product.ProductReviewRecordReq;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：产品审核记录controller
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 16:47
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 16:47
 * 修改描述：
 */
@RestController
public class ProductReviewRecordController implements ProductReviewRecodApi{

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductReviewRecordService productReviewService;

    @Autowired
    private IOtaService otaService;

    /**
     * @despriction：提交审核结果
     * @author  yeshiyuan
     * @created 2018/10/24 16:43
     * @return
     */
    @Transactional
    @Override
    public void submitAudit(@RequestBody ProductReviewRecordReq req) {
        Product product = productService.getProductByProductId(req.getProductId());
        if (product == null) {
            throw new BusinessException(ProductExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        if (product.getAuditStatus() != ProductAuditStatusEnum.WAIT_AUDIT.getValue()) {
            throw new BusinessException(ProductExceptionEnum.PARAM_IS_ERROR, "this product audit status isn't waiting audit");
        }
        if (!ProductAuditStatusEnum.checkAuditResult(req.getProcessStatus())) {
            throw new BusinessException(ProductExceptionEnum.PARAM_IS_ERROR, "audit status error");
        }
        ProductReviewRecord reviewRecord = new ProductReviewRecord(product.getTenantId(), req.getProductId(), req.getOperateTime(),
                req.getOperateDesc(),req.getOperateTime(), req.getCreateBy(), req.getProcessStatus());
        productReviewService.add(reviewRecord);
        Integer developStatus = null;
        String publishStatus = "";
        if (ProductAuditStatusEnum.AUDIT_SUCCESS.getValue() == req.getProcessStatus()){
            developStatus = DevelopStatusEnum.RELEASE.getValueInt();
            publishStatus = ProductPublishStatusEnum.SUCCESS.getValue();
            /***产品审核通过，初始ota固件上线*/
            FirmwareVersionUpdateVersionDto dto = new FirmwareVersionUpdateVersionDto();
            dto.setTenantId(product.getTenantId());
            dto.setProductId(req.getProductId());
            dto.setVersionOnlineTime(new Date());
            otaService.updateVersionOnlineTimeNoVersion(dto);
        } else {
            publishStatus = ProductPublishStatusEnum.FAIL.getValue();
        }
        productService.updateAuditStatus(req.getProductId(),req.getProcessStatus(), developStatus, req.getOperateTime());
        //发布历史
        ProductPublishHistory productPublishHistory = new ProductPublishHistory();
        productPublishHistory.setTenantId(product.getTenantId());
        productPublishHistory.setProductId(req.getProductId());
        productPublishHistory.setPublishStatus(publishStatus);
        productPublishHistory.setFailureReason(req.getOperateDesc());
        productPublishHistory.setCreateBy(req.getCreateBy());
        productService.addProductPublish(productPublishHistory);
        ProductServiceCoreUtils.removeCacheProduct(req.getProductId(), product.getModel());
    }

    /**
     * @despriction：获取产品审核记录
     * @author  yeshiyuan
     * @created 2018/10/24 16:43
     * @return
     */
    @Override
    public List<ProductReviewRecordResp> getReviewRecord(Long productId) {
        return productReviewService.getReviewRecord(productId);
    }

    /**
     * @despriction：获取租户id
     * @author  yeshiyuan
     * @created 2018/11/3 14:18
     * @return
     */
    @Override
    public Long getTenantIdById(Long id) {
        return productReviewService.getTenantIdById(id);
    }

    /**
     * @despriction：添加记录
     * @author  yeshiyuan
     * @created 2018/11/3 14:49
     * @return
     */
    @Override
    public Long addRecord(@RequestBody ProductReviewRecordReq req) {
        Product product = productService.getProductByProductId(req.getProductId());
        if (product == null) {
            throw new BusinessException(ProductExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        ProductReviewRecord reviewRecord = new ProductReviewRecord(product.getTenantId(), req.getProductId(), req.getOperateTime(),
                req.getOperateDesc(),req.getOperateTime(), req.getCreateBy(), req.getProcessStatus());
        return productReviewService.add(reviewRecord);
    }
}
