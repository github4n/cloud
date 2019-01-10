package com.iot.oauth.ifttt.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.oauth.ifttt.vo.LoginReq;
import com.iot.oauth.ifttt.vo.TokenReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.LoginRobotReq;
import com.iot.user.vo.UserTokenResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 模块名称：OAuth2.0
 * 创建人：laiguiming
 * 创建时间：2018年11月26日 下午6:04:46
 * 修改人： laiguiming
 * 修改时间：2018年11月26日 下午6:04:46
 */

@Slf4j
@RestController
@Api("授权接口")
@RequestMapping("/oauth2")
public class IftttOauthController {

    @Autowired
    private UserApi userApi;

    @ApiOperation("授权")
    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public ModelAndView authorize(@RequestParam("state") String state, @RequestParam("redirect_uri") String redirectUrl,@RequestParam("tenantId") Long tenantId) {
        log.info("收到授权请求");
        ModelAndView mv = new ModelAndView("ifttt_login");
        mv.addObject("state", state);
        mv.addObject("redirectUrl", redirectUrl);
        mv.addObject("tenantId", tenantId);
        log.info("接收到的租户："+tenantId);
        return mv;
    }


    @ApiOperation("登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(@ModelAttribute LoginReq req) throws IOException {
        log.info("收到登录请求:{}",req.toString() );

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String redirectUrl = req.getRedirectUrl();
        String url;
        if("0".equals(req.getFlag())){
            log.info("拒绝授权，登录失败！");
            url = redirectUrl + "?error=access_denied";
            response.sendRedirect(url);
            return;
        }

        //租户ID
        Long tenantId = req.getTenantId();
        SaaSContextHolder.setCurrentTenantId(tenantId);
        String state = req.getState();

        //登录
        LoginRobotReq loginRobotReq = new LoginRobotReq();
        loginRobotReq.setTerminalMark("OAUTH2.0");
        loginRobotReq.setUserName(req.getUserName());
        loginRobotReq.setPwd(req.getPassword());
        loginRobotReq.setTenantId(tenantId);
        loginRobotReq.setClient(SmartHomeConstants.SMART_TYPE_IFTTT);

        LoginResp user = null;
        try {
            user = userApi.login2Robot(loginRobotReq);
        } catch (Exception e) {
            log.info("登录失败！");
            url = redirectUrl + "?error=access_denied";
            response.sendRedirect(url);
            return;
        }

        log.info("登录成功！");
        String code = userApi.createCode(user.getUserUuid());
        url = redirectUrl + "?code=" + code + "&state=" + state;
        response.sendRedirect(url);
    }

    @ApiOperation("获取token")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public String token(@ModelAttribute TokenReq req) {
        //验证key值是否一致
        log.info("收到获取token请求:" + req.toString());

        String clientType = "IFTTT";
        String code = req.getCode();
        String refresh_token = req.getRefresh_token();
        UserTokenResp res = null;
        if ("authorization_code".equals(req.getGrant_type())) {
            // 第一次通过 code
            res = userApi.createOauthTokenByCode(clientType, code);
        } else if("refresh_token".equals(req.getGrant_type())){
            // 后续通过 refresh_token
            res = userApi.createOauthTokenByRefreshToken(clientType, refresh_token);
        }else{
            log.info("刷新令牌，类型不正确，"+req.getGrant_type());
        }

        Map<String, String> resMap = Maps.newHashMap();
        if (res == null) {
            log.info("没有获取到token信息");
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setStatus(401);
        } else {
            log.info("token信息："+res.toString());
            resMap.put("token", "Bearer");
            resMap.put("access_token", res.getAccessToken());
            resMap.put("refresh_token", res.getRefreshToken());
        }

        log.info("应答消息："+JSON.toJSONString(resMap));

        return JSON.toJSONString(resMap);
    }

}
