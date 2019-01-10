package com.iot.tenant.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @Descrpiton:   租户的智能音箱配置信息
 * @Author: yuChangXing
 * @Date: 2018/9/11 10:33
 * @Modify by:
 */
@TableName("voice_box_config")
public class VoiceBoxConfig extends Model<VoiceBoxConfig> {

    public static final String TABLE_NAME = "voice_box_config";

    private static final long serialVersionUID = 1L;

    // 主键id
    private Long id;

    // 租户ID
    private Long tenantId;

    private Date createTime;

    // 第三方类型
    private String type;
    // 技能类型
    private String skillType;
    // 租户logo
    private String logo;

    // 第三方 来 云端授权(获取token)使用
    private String oauthClientId;
    // 第三方 来 云端授权(获取token)使用
    private String oauthClientSecret;
    // oauth界面展示的 公司名称
    private String companyName;
    // oauth界面展示的 授权提示内容
    private String oauthTipContent;

    // alexa/googleHome 上报数据(请求第三方服务)用到
    private String reportClientId;
    // alexa
    private String reportClientSecret;

    // googleHome
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
