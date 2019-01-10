package com.iot.tenant.api.fallback;

import com.iot.tenant.api.VoiceBoxConfigApi;
import com.iot.tenant.vo.req.VoiceBoxConfigReq;
import com.iot.tenant.vo.resp.VoiceBoxConfigResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:57 2018/4/26
 * @Modify by:
 */
@Component
public class VoiceBoxConfigApiFallbackFactory implements FallbackFactory<VoiceBoxConfigApi> {

    @Override
    public VoiceBoxConfigApi create(Throwable cause) {
        return new VoiceBoxConfigApi() {

            @Override
            public VoiceBoxConfigResp getByOauthClientId(String oauthClientId) {
                return null;
            }

            @Override
            public VoiceBoxConfigResp getByVoiceBoxConfigReq(VoiceBoxConfigReq voiceBoxConfigReq) {
                return null;
            }
        };
    }
}
