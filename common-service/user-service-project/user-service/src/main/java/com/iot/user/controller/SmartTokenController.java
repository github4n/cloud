package com.iot.user.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.util.StringUtil;
import com.iot.tenant.api.VoiceBoxConfigApi;
import com.iot.tenant.vo.req.VoiceBoxConfigReq;
import com.iot.tenant.vo.resp.VoiceBoxConfigResp;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.entity.SmartToken;
import com.iot.user.service.SmartTokenService;
import com.iot.user.service.UserService;
import com.iot.user.util.alexa.AuthUtil;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.SmartTokenReq;
import com.iot.user.vo.SmartTokenResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/19 8:39
 * @Modify by:
 */
@RestController
public class SmartTokenController implements SmartTokenApi {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private Logger log = LoggerFactory.getLogger(SmartTokenController.class);
    @Autowired
    private VoiceBoxConfigApi voiceBoxConfigApi;
    @Autowired
    private SmartTokenService smartTokenService;
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
//    	AuthUtil.getAccessTokenByCode("ANKLAiUKiHrDTnHjsRJC", "amzn1.application-oa2-client.76b2b5748b84442184b30a97f5fd3097",
//    			"6f49729cadd0773490f25a569758e0c4a51b34f4f67532f6346f0969ba8564da");
        AuthUtil.refreshAccessToken("Atzr|IwEBIGogDq6OMwLpI2pdBZD6SJsEk5mMRFU0a7x9c3ndGf8L1AeitDFB4iIzBe80JvVBI5VJvpauPlNW06GPJB5-Wt0RthYGOugRcf4XXPhJqqItak4jnzT6RqSkh-4tp6ac6CpZ2KhbmkN-8cEfLOBcAz58mUYZKaYoGKMWI83Vvdn7XqZmlB1JuO39h7Z0pDKOpzjkvZcXst2KWEl-RQbUuUh26JL3zHEwbEkJL4XxE9rIlQ3bmrkmo72-EAJtsMKwAZ8sLp7ug8WScEruMcHXWb-c0vMxe9GSvUAlfUFBagzy0la8SK99qp6jnnZTyisLzowW7Qp4kIkaB7Pfgohc1aC4Kc87OC_YofENqwMHwuf5YziLg9woev8SYvcKEP1F_2QndpnCB85vbxGHb9W3bo8sd8iQc23jK-udA7YZCzDOmDUOLXMuDaXJam8eJBfvkvnQjjkMdeSWsfXmnlZ1AwnHnDiH-RQffqqRSsvWoPVBl4AlKCktYwEpFcf4KQ46G20", "amzn1.application-oa2-client.76b2b5748b84442184b30a97f5fd3097",
                "6f49729cadd0773490f25a569758e0c4a51b34f4f67532f6346f0969ba8564da");

    }


    /**
     * 根据 userId、smart 获取一个 SmartTokenResp
     */
    @Override
    public SmartTokenResp getSmartTokenByUserIdAndSmart(@RequestParam("userId") Long userId, @RequestParam("smart") Integer smart) {
        SmartTokenResp token = smartTokenService.getSmartTokenByUserIdAndSmart(userId, smart);
        if (token == null) {
            return token;
        }

        String UUID = userService.getUuid(userId);
        token.setUserUUID(UUID);
        return token;
    }

    @Override
    public void deleteSmartTokenByUserIdAndSmart(@RequestParam("userId") Long userId, @RequestParam("smart") Integer smart) {
        if (userId == null || smart == null) {
            return;
        }
        log.debug("***** deleteSmartTokenByUserIdAndSmart, userId={}, smart={} unlink skill, will delete smartToken", userId, smart);
        smartTokenService.deleteSmartTokenByUserIdAndSmart(userId, smart);
    }

    @Override
    public void deleteByUserIdAndLocalAccessToken(@RequestParam("userId") Long userId, @RequestParam("localAccessToken") String localAccessToken) {
        log.debug("***** deleteByUserIdAndLocalAccessToken, userId={}, localAccessToken={} unlink skill, will delete smartToken", userId, localAccessToken);
        smartTokenService.deleteByUserIdAndLocalAccessToken(userId, localAccessToken);
    }

    /**
     * 获取用户所有的 smartToken
     *
     * @param userId
     * @return
     */
    @Override
    public List<SmartTokenResp> findSmartTokenListByUserId(@RequestParam("userId") Long userId) {
        List<SmartTokenResp> smartTokenRespList = Lists.newArrayList();

        SmartTokenResp alexa = getAlexaSmartTokenByUserId(userId);
        if (alexa != null) {
            smartTokenRespList.add(alexa);
        }

        SmartTokenResp googleHome = smartTokenService.getGoogleSmartTokenByUserId(userId);
        if (googleHome != null) {
            smartTokenRespList.add(googleHome);
        }
        return smartTokenRespList;
    }

    /**
     *  根据 userId 获取 third_party_info_id 不为空的记录
     *
     * @param userId
     * @return
     */
    @Override
    public List<SmartTokenResp> findThirdPartyInfoIdNotNull(@RequestParam("userId") Long userId) {
        List<SmartTokenResp> respList = null;

        List<SmartToken> smartTokenList = smartTokenService.findThirdPartyInfoIdNotNull(userId);
        if (CollectionUtils.isNotEmpty(smartTokenList)) {
            respList = Lists.newArrayList();

            for (SmartToken st : smartTokenList) {
                SmartTokenResp resp = new SmartTokenResp();
                BeanUtils.copyProperties(st, resp);
                respList.add(resp);
            }
        }

        return respList;
    }

    /**
     * 获取 alexa的 smartToken
     *
     * @param userId
     * @return
     */
    public SmartTokenResp getAlexaSmartTokenByUserId(@RequestParam("userId") Long userId) {
        SmartTokenResp token = smartTokenService.getAlexaSmartTokenByUserId(userId);
        if (token == null) {
            return token;
        }

        if (StringUtil.isBlank(token.getAccessToken())) {
            log.debug("***** userId={} smartToken.accessToken is empty, smartToken={}", userId, JSON.toJSONString(token));
            return null;
        }

        Date now = new Date();
        Date expireTime = token.getUpdateTime();
        long timec = expireTime.getTime() - now.getTime();
        log.info("***** userId={} smartToken expire after {} min", userId, timec / 1000 / 60);

        if (timec <= 2 * 1000 * 60) {
            // 小于2分钟
            // 刷新等待
            SmartTokenReq req = refreshAlexaToken(token.getRefreshToken(), userId);
            BeanUtils.copyProperties(req, token);
        }

        String refreshToken = token.getRefreshToken();
        if (timec > 2 * 1000 * 60 && timec < 6 * 1000 * 60) {
            // 小于6分钟, 大于2分钟
            // 异步(先使用再刷新)
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    refreshAlexaToken(refreshToken, userId);
                }
            });
        }

        //String UUID = this.getUuid(userId);
        //String mqttPw = RedisCacheUtil.hashGetString(SmartHomeConstants.MQTT_PREFIX_ALEXA, UUID);
        //token.setMqttPw(mqttPw);
        //token.setUserUUID(UUID);
        return token;
    }

    @Override
    public boolean saveTokenForAlexa(@RequestParam("code") String code, @RequestParam("userId") Long userId) {
        try {
            FetchUserResp userResp = userService.getUser(userId);
            VoiceBoxConfigResp voiceBoxConfigResp = voiceBoxConfigApi.getByVoiceBoxConfigReq(VoiceBoxConfigReq.createAlexaSmartHomeVoiceBoxConfigReq(userResp.getTenantId()));

            SmartTokenReq req = AuthUtil.getAccessTokenByCode(code, voiceBoxConfigResp.getReportClientId(), voiceBoxConfigResp.getReportClientSecret());
            req.setUserId(userId);
            req.setSmart(SmartTokenReq.ALEXA);
            smartTokenService.merge(req);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 重新获取 alexa 的 smartToken信息(access_token、refresh_token、Expires_in)
     *
     * @param refreshToken
     * @param userId
     * @return
     */
    private SmartTokenReq refreshAlexaToken(String refreshToken, Long userId) {
        FetchUserResp userResp = userService.getUser(userId);
        VoiceBoxConfigResp voiceBoxConfigResp = voiceBoxConfigApi.getByVoiceBoxConfigReq(VoiceBoxConfigReq.createAlexaSmartHomeVoiceBoxConfigReq(userResp.getTenantId()));

        SmartTokenReq req = AuthUtil.refreshAccessToken(refreshToken, voiceBoxConfigResp.getReportClientId(), voiceBoxConfigResp.getReportClientSecret());
        req.setUserId(userId);
        req.setSmart(SmartTokenReq.ALEXA);
        smartTokenService.merge(req);
        return req;
    }

    /**
     *  根据 本地生成的access_token 获取记录
     * @param localAccessToken
     * @return
     */
    @Override
    public SmartTokenResp getByLocalAccessToken(@RequestParam("localAccessToken") String localAccessToken) {
        return smartTokenService.getByLocalAccessToken(localAccessToken);
    }
}
