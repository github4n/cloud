package com.iot.control.packagemanager.vo.req;

import com.iot.common.exception.BusinessException;
import com.iot.control.packagemanager.execption.PackageExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
  * @despriction：保存套包产品
  * @author  yeshiyuan
  * @created 2018/11/23 19:52
  */
@Data
@ApiModel(description = "保存套包产品")
public class SavePackageProductReq {

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "productIds", value = "产品ids", dataType = "Long")
    private List<Long> productIds;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "createBy", value = "创建者主键", dataType = "Long")
    private Long createBy;

    public static void checkParam(SavePackageProductReq saveReq) {
        if (saveReq.getPackageId() == null) {
            throw new BusinessException(PackageExceptionEnum.PARAM_ERROR, "packageId is null");
        }
        if (saveReq.getTenantId() == null) {
            throw new BusinessException(PackageExceptionEnum.PARAM_ERROR, "tenantId is null");
        }
        if (saveReq.getProductIds() == null || saveReq.getProductIds().isEmpty()) {
            throw new BusinessException(PackageExceptionEnum.PARAM_ERROR, "product id is null");
        }
    }

    public SavePackageProductReq() {
    }

    public SavePackageProductReq(Long packageId, List<Long> productIds, Long tenantId, Long createBy) {
        this.packageId = packageId;
        this.productIds = productIds;
        this.tenantId = tenantId;
        this.createBy = createBy;
    }
}
