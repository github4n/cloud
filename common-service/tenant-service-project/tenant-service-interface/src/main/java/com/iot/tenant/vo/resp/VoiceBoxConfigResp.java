package com.iot.tenant.vo.resp;

import java.util.Date;

/**
 * @Descrpiton: 租户的智能音箱配置信息
 * @Author: yuChangXing
 * @Date: 2018/9/11 10:33
 * @Modify by:
 */
public class VoiceBoxConfigResp {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long tenantId;

    private Date createTime;

    private String type;
    private String skillType;
    private String logo;

    private String oauthClientId;
    private String oauthClientSecret;
    private String companyName;
    private String oauthTipContent;

    private String reportClientId;
    private String reportClientSecret;

    private String projectId;
    private String privateKey;
    private String privateKeyId;
    private String clientEmail;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getOauthClientId() {
        return oauthClientId;
    }

    public void setOauthClientId(String oauthClientId) {
        this.oauthClientId = oauthClientId;
    }

    public String getOauthClientSecret() {
        return oauthClientSecret;
    }

    public void setOauthClientSecret(String oauthClientSecret) {
        this.oauthClientSecret = oauthClientSecret;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOauthTipContent() {
        return oauthTipContent;
    }

    public void setOauthTipContent(String oauthTipContent) {
        this.oauthTipContent = oauthTipContent;
    }

    public String getReportClientId() {
        return reportClientId;
    }

    public void setReportClientId(String reportClientId) {
        this.reportClientId = reportClientId;
    }

    public String getReportClientSecret() {
        return reportClientSecret;
    }

    public void setReportClientSecret(String reportClientSecret) {
        this.reportClientSecret = reportClientSecret;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyId() {
        return privateKeyId;
    }

    public void setPrivateKeyId(String privateKeyId) {
        this.privateKeyId = privateKeyId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
