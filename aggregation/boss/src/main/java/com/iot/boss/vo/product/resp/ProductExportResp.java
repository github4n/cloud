package com.iot.boss.vo.product.resp;

import com.iot.boss.vo.module.BossServiceModuleResp;
import com.iot.device.vo.rsp.ProductPublishHistoryResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ProductTimerResp;
import com.iot.device.vo.rsp.StyleTemplateResp;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.shcs.module.vo.resp.GetProductModuleResp;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProductExportResp extends GetProductModuleResp implements Serializable {

//    @ApiModelProperty(value = "产品详情")
//    private ProductResp productInfo; //产品详情

//    @ApiModelProperty(value = "功能组列表")
//    private List<BossServiceModuleResp> serviceModules;

//    @ApiModelProperty(value = "产品样式")
//    private StyleTemplateResp style;

    @ApiModelProperty(value = "产品配置定时方式")
    private List<ProductTimerResp> timers;

    @ApiModelProperty(value = "产品发布历史")
    private List<ProductPublishHistoryResp> publishHistorys;

    @ApiModelProperty(value = "产品审核记录")
    private List<ProductReviewRecordResp> reviewRecords;

    @ApiModelProperty(value = "产品关联第三方服务信息")
    private List<ProductServiceInfoResp> serviceInfos;

//    private LangInfoTenantResp langInfoTenants;

    @ApiModelProperty(value = "租户文案信息")
    private List<LangInfoTenant> langInfoTenants;

}
