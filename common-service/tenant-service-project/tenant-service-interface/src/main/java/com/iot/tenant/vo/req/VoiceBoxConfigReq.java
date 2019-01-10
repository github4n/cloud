package com.iot.tenant.vo.req;

import com.iot.tenant.common.costants.VoiceBoxConfigConstant;

import java.io.Serializable;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/11 11:07
 * @Modify by:
 */
public class VoiceBoxConfigReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long tenantId;
    private String type;
    private String skillType;

    public static VoiceBoxConfigReq createAlexaSmartHomeVoiceBoxConfigReq(Long tenantId) {
        VoiceBoxConfigReq req = new VoiceBoxConfigReq();
        req.setType(VoiceBoxConfigConstant.VOICE_BOX_TYPE_ALEXA);
        req.setSkillType(VoiceBoxConfigConstant.SKILL_TYPE_SMART_HOME);
        req.setTenantId(tenantId);
        return req;
    }

    public static VoiceBoxConfigReq createAlexaCustomVoiceBoxConfigReq(Long tenantId) {
        VoiceBoxConfigReq req = new VoiceBoxConfigReq();
        req.setType(VoiceBoxConfigConstant.VOICE_BOX_TYPE_ALEXA);
        req.setSkillType(VoiceBoxConfigConstant.SKILL_TYPE_CUSTOM_SKILL);
        req.setTenantId(tenantId);
        return req;
    }

    public static VoiceBoxConfigReq createGoogleHomeSmartHomeVoiceBoxConfigReq(Long tenantId) {
        VoiceBoxConfigReq req = new VoiceBoxConfigReq();
        req.setType(VoiceBoxConfigConstant.VOICE_BOX_TYPE_GOOGLE_HOME);
        req.setSkillType(VoiceBoxConfigConstant.SKILL_TYPE_SMART_HOME);
        req.setTenantId(tenantId);
        return req;
    }

    public static VoiceBoxConfigReq createGoogleHomeCustomVoiceBoxConfigReq(Long tenantId) {
        VoiceBoxConfigReq req = new VoiceBoxConfigReq();
        req.setType(VoiceBoxConfigConstant.VOICE_BOX_TYPE_GOOGLE_HOME);
        req.setSkillType(VoiceBoxConfigConstant.SKILL_TYPE_CUSTOM_SKILL);
        req.setTenantId(tenantId);
        return req;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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
}
