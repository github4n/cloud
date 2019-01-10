package com.iot.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.util.StringUtil;
import com.iot.oauth.util.MD5Util;
import com.iot.oauth.util.OauthResponseUtil;
import com.iot.oauth.vo.AuthorVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.smarthome.api.ThirdPartyInfoApi;
import com.iot.smarthome.constant.SmartHomeConstant;
import com.iot.smarthome.vo.resp.ThirdPartyInfoResp;
import com.iot.tenant.common.costants.VoiceBoxConfigConstant;
import com.iot.user.api.UserApi;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.LoginRobotReq;
import com.iot.user.vo.OauthTokenCreateVO;
import com.iot.user.vo.UserTokenResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/12 15:21
 * @Modify by:
 */

@Api("授权接口")
@RestController()
@RequestMapping("/v1")
public class OauthV1Controller {
    private Logger log = LoggerFactory.getLogger(OauthController.class);

    private static final Long TENANT_ID_ARNOO = 2L;
    private static final String OAUTH_AUTHORIZE_KEY = "oauth:authorize:";

    @Autowired
    private UserApi userApi;
    @Autowired
    private ThirdPartyInfoApi thirdPartyInfoApi;

    private String getOauthAuthorizeKey(String uuid) {
        return OAUTH_AUTHORIZE_KEY + uuid;
    }


    /**
     * 跳转到 login 界面，去输账号、密码
     */
    @ApiOperation("授权入口")
    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public ModelAndView authorize(@ModelAttribute AuthorVo vo) {
        log.info("***** authorize, AuthorVo={}", JSONObject.toJSON(vo));
        ModelAndView mv = new ModelAndView("v1/login");

        // oauth端 生成的临时 uuid
        String uuid = vo.getUuid();
        if (StringUtil.isBlank(uuid)) {
            // uuid 为空
            if (StringUtil.isBlank(vo.getClient_id())) {
                log.info("***** authorize, client_id is empty");
                mv.setViewName("v1/error");
                mv.addObject("errorMsg", "invalid access.");
                return mv;
            }

            uuid = UUID.randomUUID().toString();
            log.info("***** authorize, create new uuid={}", uuid);
            RedisCacheUtil.valueObjSet(getOauthAuthorizeKey(uuid), vo, 600L);
        } else {
            // uuid 不为空
            vo = RedisCacheUtil.valueObjGet(getOauthAuthorizeKey(uuid), AuthorVo.class);
            log.info("***** authorize, uuid={} is not empty, AuthorVo={}", uuid, JSONObject.toJSON(vo));
        }

        // 判断 clientId 是否存在
        String clientId = vo.getClient_id();
        ThirdPartyInfoResp thirdPartyInfoResp = thirdPartyInfoApi.getByClientId(clientId);
        log.info("***** authorize, client_id={}, thirdPartyInfoResp={}", clientId, JSONObject.toJSON(thirdPartyInfoResp));
        if (thirdPartyInfoResp == null) {
            // 配置信息不存在
            mv.setViewName("v1/error");
            mv.addObject("errorMsg", "client_id is invalid.");
            return mv;
        }

        mv.addObject("uuid", uuid);

        return mv;
    }

    /**
     * 提交账号、密码， 验证正确 -> 重定向到 第三方的 redirect_url
     */
    @ApiOperation("登陆接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                              @RequestParam("uuid") String uuid, @RequestParam(value = "tenantId", required=false) Long tenantId) {
        log.info("***** login, userName={}, uuid={}, tenantId={}", userName, uuid, tenantId);

        if (tenantId == null) {
            tenantId = TENANT_ID_ARNOO;
        }

        // 把tenantId 存入 SaaSContext
        SaaSContextHolder.setCurrentTenantId(tenantId);

        ModelAndView mv = new ModelAndView("v1/error");

        if (StringUtil.isEmpty(uuid)) {
            log.error("***** login error, uuid is empty.");
            mv.setViewName("v1/error");
            mv.addObject("errorMsg", "invalid access.");
            return mv;
        }

        String authorizeKey = getOauthAuthorizeKey(uuid);
        AuthorVo vo = RedisCacheUtil.valueObjGet(authorizeKey, AuthorVo.class);
        if (vo == null) {
            log.error("***** login error, authorizeKey={} not in cache(or expire)", authorizeKey);
            mv.setViewName("v1/error");
            mv.addObject("errorMsg", "invalid access.");
            return mv;
        }

        log.info("***** login, authorizeKey={}, AuthorVo={}", uuid, JSONObject.toJSON(vo));

        // 获取第三方信息
        String clientId = vo.getClient_id();
        ThirdPartyInfoResp thirdPartyInfoResp = thirdPartyInfoApi.getByClientId(clientId);
        log.info("***** login, client_id={}, thirdPartyInfoResp={}", clientId, JSONObject.toJSON(thirdPartyInfoResp));
        if (thirdPartyInfoResp == null) {
            // 配置信息不存在
            mv.setViewName("v1/error");
            mv.addObject("errorMsg", "client_id is invalid.");
            return mv;
        }

        LoginRobotReq lov = new LoginRobotReq();
        lov.setTerminalMark("OAUTH2.0");
        lov.setUserName(userName);
        lov.setPwd(MD5Util.to32String(password));
        lov.setTenantId(tenantId);
        lov.setThirdPartyInfoId(thirdPartyInfoResp.getId());

        // 登录
        LoginResp user = null;
        try {
            user = userApi.login2Robot(lov);
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("v1/error");
            mv.addObject("errorMsg", "You entered an incorrect email address or password. Please try again using the correct email address and password.");
            mv.addObject("uuid", uuid);
            return mv;
        }

        log.info("***** login success.");
        String code = userApi.createCode(user.getUserUuid());
        String url = thirdPartyInfoResp.getRedirectUri() + "?code=" + code;
        if (StringUtils.isNotBlank(vo.getState())) {
            url = url + "&state=" + vo.getState();
        }

        log.info("***** login, final redirect = {}", url);
        mv.setViewName("redirect:" + url);
        return mv;
    }

    /**
     * 授权第三方成功后， 第三方会来 调用此接口（获取token）
     */
    @ApiOperation("令牌获取接口")
    @RequestMapping(value = "/generateToken", method = RequestMethod.POST)
    public JSONObject generateToken(String code, String client_id, String grant_type,
                                    String refresh_token) throws OAuthSystemException {
        log.info("***** generateToken, grantType:{}, clientId:{}, code:{}, refreshToken:{}", grant_type, client_id, code, refresh_token);

        // 验证 client_id
        OAuthResponse errorResponse = null;
        ThirdPartyInfoResp thirdPartyInfoResp = null;
        if (StringUtils.isBlank(client_id)) {
            errorResponse = OauthResponseUtil.getFailResponse(OAuthError.TokenResponse.INVALID_CLIENT, "invalid_client");
        } else {
            // 获取第三方信息
            thirdPartyInfoResp = thirdPartyInfoApi.getByClientId(client_id);
            log.info("***** generateToken, client_id={}, thirdPartyInfoResp={}", client_id, JSONObject.toJSON(thirdPartyInfoResp));
            if (thirdPartyInfoResp == null) {
                // 配置信息不存在
                errorResponse = OauthResponseUtil.getFailResponse(OAuthError.TokenResponse.INVALID_CLIENT, "invalid_client");
            }
        }

        if (errorResponse != null) {
            log.info("***** generateToken, errorResponse={}", errorResponse.getBody());
            return JSONObject.parseObject(errorResponse.getBody());
        }

        // 创建 vo
        OauthTokenCreateVO tokenCreateVo = new OauthTokenCreateVO();
        tokenCreateVo.setClientType(SmartHomeConstant.CLIENT_TYPE);
        tokenCreateVo.setSkillType(VoiceBoxConfigConstant.SKILL_TYPE_SMART_HOME);
        tokenCreateVo.setThirdPartyInfoId(thirdPartyInfoResp.getId());

        OAuthResponse oAuthResponse = null;
        UserTokenResp res = null;
        if ("authorization_code".equals(grant_type)) {
            // 通过 code 获取 token
            tokenCreateVo.setCode(code);
            res = userApi.createOauthTokenByCodeVo(tokenCreateVo);
            if (res == null) {
                oAuthResponse = OauthResponseUtil.getFailResponse(OAuthError.TokenResponse.INVALID_GRANT, "invalid code");
            } else {
                oAuthResponse = OauthResponseUtil.getSuccessResponse(res.getAccessToken(), res.getRefreshToken(), res.getExpireIn() + "");
            }

        } else if ("refresh_token".equals(grant_type)) {
            // 通过 refresh_token 获取 token
            tokenCreateVo.setRefreshToken(refresh_token);
            res = userApi.createOauthTokenByRefreshTokenVo(tokenCreateVo);
            if (res == null) {
                oAuthResponse = OauthResponseUtil.getFailResponse(OAuthError.TokenResponse.INVALID_GRANT, "invalid refresh_token");
            } else {
                oAuthResponse = OauthResponseUtil.getSuccessResponse(res.getAccessToken(), res.getRefreshToken(), res.getExpireIn() + "");
            }

        } else {
            // 不支持的 grant_type
            oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_OK)
                    .setError(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE)
                    .setErrorDescription("unsupported_grant_type")
                    .buildJSONMessage();
        }

        // 生成OAuth响应
        JSONObject finalJsonObject = JSONObject.parseObject(oAuthResponse.getBody());
        log.info("***** generateToken, finalJsonObject={}", finalJsonObject);
        return finalJsonObject;
    }
}
