package com.iot.user.api.fallback;

import com.iot.user.api.SmartTokenApi;
import com.iot.user.vo.SmartTokenResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class SmartTokenApiFallbackFactory implements FallbackFactory<SmartTokenApi> {

    private final Logger logger = LoggerFactory.getLogger(SmartTokenApiFallbackFactory.class);

    @Override
    public SmartTokenApi create(Throwable cause) {
        return new SmartTokenApi() {
            @Override
            public SmartTokenResp getAlexaSmartTokenByUserId(Long userId) {
                return null;
            }

            @Override
            public SmartTokenResp getSmartTokenByUserIdAndSmart(Long userId, Integer smart) {
                return null;
            }

            @Override
            public void deleteSmartTokenByUserIdAndSmart(Long userId, Integer smart) {

            }

            @Override
            public void deleteByUserIdAndLocalAccessToken(Long userId, String localAccessToken) {

            }

            @Override
            public List<SmartTokenResp> findSmartTokenListByUserId(Long userId) {
                return null;
            }

            @Override
            public List<SmartTokenResp> findThirdPartyInfoIdNotNull(Long userId) {
                return null;
            }

            @Override
            public boolean saveTokenForAlexa(String code, Long userId) {
                return false;
            }

            @Override
            public SmartTokenResp getByLocalAccessToken(@RequestParam("localAccessToken") String localAccessToken) {
                return null;
            }
        };
    }
}
