package com.iot.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.util.StringUtil;
import com.iot.oauth.util.MD5Util;
import com.iot.oauth.util.OauthResponseUtil;
import com.iot.oauth.vo.AuthorVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.VoiceBoxConfigApi;
import com.iot.tenant.common.costants.VoiceBoxConfigConstant;
import com.iot.tenant.vo.resp.VoiceBoxConfigResp;
import com.iot.user.api.UserApi;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.LoginRobotReq;
import com.iot.user.vo.OauthTokenCreateVO;
import com.iot.user.vo.UserTokenResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.error.OAuthError.TokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * 模块名称：OAuth2.0
 * 创建人：chenxiaolin
 * 创建时间：2018年3月23日 下午6:04:46
 * 修改人： chenxiaolin
 * 修改时间：2018年3月23日 下午6:04:46
 */
@RestController
@Api("授权接口")
public class OauthController {

    private Logger log = LoggerFactory.getLogger(OauthController.class);
    @Autowired
    private UserApi userApi;
    @Autowired
    private VoiceBoxConfigApi voiceBoxConfigApi;

    private static final String OAUTH_AUTHORIZE_KEY = "oauth:authorize:";

    private String getOauthAuthorizeKey(String uuid) {
        return OAUTH_AUTHORIZE_KEY + uuid;
    }



    /**
     * 跳转到 index界面，去输账号、密码
     */
    @ApiOperation("授权入口")
    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public ModelAndView authorize(@ModelAttribute AuthorVo vo, Model model) {
        log.info("***** authorize(), AuthorVo={}", JSONObject.toJSON(vo));
        ModelAndView mv = new ModelAndView("index");

        // oauth端 生成的临时 uuid
        String uuid = vo.getUuid();
        if (StringUtil.isBlank(uuid)) {
            // uuid 为空
            if (StringUtil.isBlank(vo.getClient_id())) {
                log.info("***** authorize(), client_id is empty");
                mv.setViewName("error");
                mv.addObject("errorMsg", "invalid access.");
                return mv;
            }

            uuid = UUID.randomUUID().toString();
            log.info("***** authorize(), create new uuid={}", uuid);
            RedisCacheUtil.valueObjSet(getOauthAuthorizeKey(uuid), vo, 600L);
        } else {
            // uuid 不为空
            vo = RedisCacheUtil.valueObjGet(getOauthAuthorizeKey(uuid), AuthorVo.class);
            log.info("***** authorize(), uuid={} is not empty, AuthorVo={}", uuid, JSONObject.toJSON(vo));
        }

        String oauthClientId = vo.getClient_id();
        VoiceBoxConfigResp voiceBoxConfigResp = voiceBoxConfigApi.getByOauthClientId(oauthClientId);
        log.info("***** authorize(), client_id={}, voiceBoxConfigResp={}", oauthClientId, JSONObject.toJSON(voiceBoxConfigResp));
        if (voiceBoxConfigResp == null) {
            // 租户配置信息不存在
            mv.setViewName("error");
            mv.addObject("errorMsg", "client_id config information is not exist.");
            return mv;
        }

        mv.addObject("companyName", voiceBoxConfigResp.getCompanyName());
        mv.addObject("logo", voiceBoxConfigResp.getLogo());
        mv.addObject("uuid", uuid);

        return mv;
    }

    /**
     * 提交账号、密码， 然后跳转到 authorization.ftl界面
     */
    @ApiOperation("登陆接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                              @RequestParam("uuid") String uuid) {
        log.info("***** login(), userName={}, uuid={}", userName, uuid);

        ModelAndView mv = new ModelAndView("authorization");

        if (StringUtil.isEmpty(uuid)) {
            log.error("***** login() error, uuid is empty.");
            mv.setViewName("error");
            mv.addObject("errorMsg", "invalid access.");
            return mv;
        }

        String authorizeKey = getOauthAuthorizeKey(uuid);
        AuthorVo vo = RedisCacheUtil.valueObjGet(authorizeKey, AuthorVo.class);
        if (vo == null) {
            log.error("***** login() error, authorizeKey={} not in cache(or expire)", authorizeKey);
            mv.setViewName("error");
            mv.addObject("errorMsg", "invalid access.");
            return mv;
        }

        log.info("***** login(), authorizeKey={}, AuthorVo={}", uuid, JSONObject.toJSON(vo));

        String oauthClientId = vo.getClient_id();
        VoiceBoxConfigResp voiceBoxConfigResp = voiceBoxConfigApi.getByOauthClientId(oauthClientId);
        log.info("***** login(), voiceBoxConfigResp={}", JSONObject.toJSON(voiceBoxConfigResp));
        if (voiceBoxConfigResp == null) {
            // 租户配置信息不存在
            mv.setViewName("error");
            mv.addObject("errorMsg", "client_id config information is not exist.");
            return mv;
        }

        LoginRobotReq lov = new LoginRobotReq();
        lov.setTerminalMark("OAUTH2.0");
        lov.setUserName(userName);
        lov.setPwd(MD5Util.to32String(password));
        lov.setTenantId(voiceBoxConfigResp.getTenantId());

        // 把tenantId 存入 SaaSContext
        SaaSContextHolder.setCurrentTenantId(voiceBoxConfigResp.getTenantId());

        // 授权按钮显示的内容
        String showButtonAuthorizeContent = null;
        String companyName = voiceBoxConfigResp.getCompanyName();
        String tipContent = voiceBoxConfigResp.getOauthTipContent();

        if (VoiceBoxConfigConstant.VOICE_BOX_TYPE_ALEXA.equals(voiceBoxConfigResp.getType())) {
            // alexa
            lov.setClient(SmartHomeConstants.ALEXA);
            showButtonAuthorizeContent = "authorize for Alexa";
        } else if(VoiceBoxConfigConstant.VOICE_BOX_TYPE_GOOGLE_HOME.equals(voiceBoxConfigResp.getType())){
            // googlehome
            lov.setClient(SmartHomeConstants.GOOGLE_HOME);
            showButtonAuthorizeContent = "authorize for Google";
        }

        LoginResp user = null;
        try {
            user = userApi.login2Robot(lov);
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("error");
            mv.addObject("errorMsg", "You entered an incorrect email address or password. Please try again using the correct email address and password.");
            mv.addObject("uuid", uuid);
            return mv;
        }

        String userUUID = user.getUserUuid();
        vo.setUserUuid(userUUID);

        RedisCacheUtil.valueObjSet(getOauthAuthorizeKey(uuid), vo, 600L);

        mv.addObject("tipContent", tipContent);
        mv.addObject("companyName", companyName);
        mv.addObject("logo", voiceBoxConfigResp.getLogo());
        mv.addObject("showButtonAuthorizeContent", showButtonAuthorizeContent);
        mv.addObject("uuid", uuid);
        return mv;
    }

    /**
     * 在界面点击 "authorize"， 即系统给 第三方(alexa、googleHome)授权
     */
    @ApiOperation("授权接口")
    @RequestMapping(value = "/authorization", method = RequestMethod.GET)
    public ModelAndView authorization(@RequestParam("uuid") String uuid){
        log.info("***** authorization(), uuid={}", uuid);

        //校验客户端Id是否正确
        ModelAndView mv = new ModelAndView("error");

        if (StringUtil.isEmpty(uuid)) {
            log.error("***** authorization() error, uuid is empty.");
            mv.setViewName("error");
            mv.addObject("errorMsg", "invalid access.");
            return mv;
        }

        String authorizeKey = getOauthAuthorizeKey(uuid);
        AuthorVo vo = RedisCacheUtil.valueObjGet(authorizeKey, AuthorVo.class);
        if (vo == null) {
            log.error("***** authorization() error, authorizeKey={} not in cache(or expire)", authorizeKey);
            mv.setViewName("error");
            mv.addObject("errorMsg", "invalid access.");
            return mv;
        }

        log.info("***** authorization(), authorizeKey={}, AuthorVo={}", uuid, JSONObject.toJSON(vo));

        //生成授权码
        String authCode = null;
        if (vo.getResponse_type().equals(ResponseType.CODE.toString())) {
            String userUUID = vo.getUserUuid();
            authCode = userApi.createCode(userUUID);
        }

        String url = vo.getRedirect_uri() + "?code=" + authCode + "&state=" + vo.getState();
        log.info("***** authorization(), final redirect = {}", url);
        mv.setViewName("redirect:" + url);
        return mv;
    }

    /**
     * 授权第三方成功后， 第三方会来 调用此接口（获取token）
     */
    @ApiOperation("令牌获取接口")
    @RequestMapping(value = "/generateToken")
    public JSONObject accessToken(String code,String client_id,String grant_type,
            String refresh_token) throws OAuthSystemException{
        log.info("***** generateToken(), grantType:{}, clientId:{}, code:{}, refreshToken:{}", grant_type, client_id, code, refresh_token);

        // 验证 client_id
        OAuthResponse errorResponse = null;
        VoiceBoxConfigResp voiceBoxConfigResp = null;
        if (StringUtils.isBlank(client_id)) {
            errorResponse = OauthResponseUtil.getFailResponse(OAuthError.TokenResponse.INVALID_CLIENT, "invalid_client");
        } else {
            // 获取第三方信息
            voiceBoxConfigResp = voiceBoxConfigApi.getByOauthClientId(client_id);
            log.info("***** generateToken, client_id={}, voiceBoxConfigResp={}", client_id, JSONObject.toJSON(voiceBoxConfigResp));
            if (voiceBoxConfigResp == null) {
                // 配置信息不存在
                errorResponse = OauthResponseUtil.getFailResponse(OAuthError.TokenResponse.INVALID_CLIENT, "invalid_client");
            }
        }

        if (errorResponse != null) {
            log.info("***** generateToken, errorResponse={}", errorResponse.getBody());
            return JSONObject.parseObject(errorResponse.getBody());
        }

        // smartHome、customSkill
        String skillType = voiceBoxConfigResp.getSkillType();
        Integer smartType = null;
        String clientType = null;
        if (VoiceBoxConfigConstant.VOICE_BOX_TYPE_ALEXA.equals(voiceBoxConfigResp.getType())) {
            // alexa
            smartType = SmartHomeConstants.ALEXA;
            clientType = voiceBoxConfigResp.getType();
        } else if(VoiceBoxConfigConstant.VOICE_BOX_TYPE_GOOGLE_HOME.equals(voiceBoxConfigResp.getType())){
            // googlehome
            smartType = SmartHomeConstants.GOOGLE_HOME;
            clientType = voiceBoxConfigResp.getType();
        }

        // 创建 vo
        OauthTokenCreateVO tokenCreateVo = new OauthTokenCreateVO();
        tokenCreateVo.setClientType(clientType);
        tokenCreateVo.setSkillType(skillType);
        tokenCreateVo.setSmartType(smartType);

        UserTokenResp res = null;
        if ("authorization_code".equals(grant_type)) {
            // 第一次通过 code
            tokenCreateVo.setCode(code);
            res = userApi.createOauthTokenByCodeVo(tokenCreateVo);
        } else if ("refresh_token".equals(grant_type)) {
            // 后续通过 refresh_token
            tokenCreateVo.setRefreshToken(refresh_token);
            res = userApi.createOauthTokenByRefreshTokenVo(tokenCreateVo);
        }

        JSONObject finalJsonObject = null;
        if (res == null) {
            finalJsonObject = JSONObject.parseObject(OauthResponseUtil.getFailResponse(TokenResponse.INVALID_GRANT, "refresh_token or code is invalid").getBody());
        } else {
            //生成OAuth响应
            finalJsonObject = JSONObject.parseObject(OauthResponseUtil.getSuccessResponse(res.getAccessToken(), res.getRefreshToken(), res.getExpireIn() + "").getBody());
        }

        log.info("***** generateToken(), finalJsonObject={}", finalJsonObject);
        return finalJsonObject;
    }
}
