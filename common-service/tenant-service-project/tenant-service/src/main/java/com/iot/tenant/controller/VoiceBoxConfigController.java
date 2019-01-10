package com.iot.tenant.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.common.util.StringUtil;
import com.iot.tenant.api.VoiceBoxConfigApi;
import com.iot.tenant.domain.VoiceBoxConfig;
import com.iot.tenant.service.IVoiceBoxConfigService;
import com.iot.tenant.vo.req.VoiceBoxConfigReq;
import com.iot.tenant.vo.resp.VoiceBoxConfigResp;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descrpiton: voice_box_config 表 控制器
 * @Author: yuChangXing
 * @Date: 2018/9/11 11:18
 * @Modify by:
 */

@RestController
public class VoiceBoxConfigController implements VoiceBoxConfigApi {

    @Autowired
    private IVoiceBoxConfigService voiceBoxConfigService;

    @Override
    public VoiceBoxConfigResp getByOauthClientId(@RequestParam("oauthClientId") String oauthClientId) {
        if (StringUtil.isBlank(oauthClientId)) {
            return null;
        }

        EntityWrapper<VoiceBoxConfig> wrapper = new EntityWrapper<>();
        wrapper.eq("oauth_client_id", oauthClientId);
        VoiceBoxConfig voiceBoxConfig = voiceBoxConfigService.selectOne(wrapper);
        if (voiceBoxConfig == null) {
            return null;
        }

        VoiceBoxConfigResp resp = transferVoiceBoxConfig(voiceBoxConfig);
        return resp;
    }

    @Override
    public VoiceBoxConfigResp getByVoiceBoxConfigReq(@RequestBody VoiceBoxConfigReq voiceBoxConfigReq) {
        AssertUtils.notEmpty(voiceBoxConfigReq, "voiceBoxConfigReq.notnull");
        AssertUtils.notEmpty(voiceBoxConfigReq.getTenantId(), "voiceBoxConfigReq.tenantId.notnull");
        AssertUtils.notEmpty(voiceBoxConfigReq.getType(), "voiceBoxConfigReq.type.notnull");
        AssertUtils.notEmpty(voiceBoxConfigReq.getSkillType(), "voiceBoxConfigReq.skillType.notnull");

        EntityWrapper<VoiceBoxConfig> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", voiceBoxConfigReq.getTenantId());
        wrapper.eq("type", voiceBoxConfigReq.getType());
        wrapper.eq("skill_type", voiceBoxConfigReq.getSkillType());
        VoiceBoxConfig voiceBoxConfig = voiceBoxConfigService.selectOne(wrapper);
        if (voiceBoxConfig == null) {
            return null;
        }

        VoiceBoxConfigResp resp = transferVoiceBoxConfig(voiceBoxConfig);
        return resp;
    }

    // 对象转换
    private VoiceBoxConfigResp transferVoiceBoxConfig(VoiceBoxConfig voiceBoxConfig) {
        if (voiceBoxConfig == null) {
            return null;
        }

        VoiceBoxConfigResp resp = new VoiceBoxConfigResp();
        resp.setId(voiceBoxConfig.getId());
        resp.setClientEmail(voiceBoxConfig.getClientEmail());
        resp.setCompanyName(voiceBoxConfig.getCompanyName());
        resp.setCreateTime(voiceBoxConfig.getCreateTime());
        resp.setOauthTipContent(voiceBoxConfig.getOauthTipContent());
        resp.setOauthClientId(voiceBoxConfig.getOauthClientId());
        resp.setOauthClientSecret(voiceBoxConfig.getOauthClientSecret());
        resp.setPrivateKey(voiceBoxConfig.getPrivateKey());
        resp.setPrivateKeyId(voiceBoxConfig.getPrivateKeyId());
        resp.setProjectId(voiceBoxConfig.getProjectId());
        resp.setReportClientId(voiceBoxConfig.getReportClientId());
        resp.setReportClientSecret(voiceBoxConfig.getReportClientSecret());
        resp.setSkillType(voiceBoxConfig.getSkillType());
        resp.setType(voiceBoxConfig.getType());
        resp.setTenantId(voiceBoxConfig.getTenantId());
        resp.setLogo(voiceBoxConfig.getLogo());
        return resp;
    }
}
