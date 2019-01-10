package com.iot.building.template.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yuChangXing
 * Date: 2018/5/14
 * Time: 19:42
 *
 */
public class BuildTemplateReq implements Serializable {
    private static final long serialVersionUID = -7276209311915920905L;

    /** 创建者*/
    private Long createBy;

    /** 更新者*/
    private Long updateBy;

    /** 租户ID*/
    private Long tenantId;


    // 模板主表
    private TemplateReq templateReq;

    // ifttt模板(2B业务可不设置这个属性值)
    private List<SaveIftttTemplateReq> saveIftttTemplateReqList;

    // 情景模板
    private List<TemplateDetailReq> templateDetailList;


    public TemplateReq getTemplateReq() {
        return templateReq;
    }

    public void setTemplateReq(TemplateReq templateReq) {
        this.templateReq = templateReq;
    }

    public List<SaveIftttTemplateReq> getSaveIftttTemplateReqList() {
        return saveIftttTemplateReqList;
    }

    public void setSaveIftttTemplateReqList(List<SaveIftttTemplateReq> saveIftttTemplateReqList) {
        this.saveIftttTemplateReqList = saveIftttTemplateReqList;
    }

    public List<TemplateDetailReq> getTemplateDetailList() {
        return templateDetailList;
    }

    public void setTemplateDetailList(List<TemplateDetailReq> templateDetailList) {
        this.templateDetailList = templateDetailList;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
