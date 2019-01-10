package com.iot.user.util;

import com.alibaba.fastjson.JSON;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.api.VirtualOrgApi;
import com.iot.tenant.vo.req.AddUserOrgReq;
import com.iot.tenant.vo.req.AddUserReq;
import com.iot.tenant.vo.req.AddVirtualOrgReq;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.user.entity.Token;
import com.iot.user.entity.UserToken;
import com.iot.user.exception.UserExceptionEnum;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 描述：用户拦截器操作类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/11 10:59
 */
public class UserTokenUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserTokenUtil.class);
    /**
     * 登录刷新token缓存
     *
     * @param userId
     * @param terminalMark
     * @param tenantId
     * @param userUuid     用户uuid
     */
    public static UserToken loginRefreshToken(Long userId, String terminalMark, Long tenantId, String userUuid, String systemType) {
        return loginRefreshToken(userId, terminalMark, tenantId, userUuid, systemType, null);
    }

    /**
     * 登录刷新token缓存
     *
     * @param userId
     * @param terminalMark
     * @param tenantId
     * @param userUuid     用户uuid
     */
    public static UserToken loginRefreshToken(Long userId, String terminalMark, Long tenantId, String userUuid, String systemType, Long appId) {
        UserVirtualOrgApi orgApi = ApplicationContextHelper.getBean(UserVirtualOrgApi.class);
        UserDefaultOrgInfoResp orgInfoResp = orgApi.getDefaultUsedOrgInfoByUserId(userId);
        Long orgId = null;
        if (orgInfoResp != null) {
            orgId = orgInfoResp.getId();
        } else {
            //添加组织 + 用户对应的组织 ---兼容原先的账号 可能存在未添加组织的操作
            orgId = addOrg(userId, tenantId);
        }
        String accessTokenNew = systemType + UUID.randomUUID().toString();
        String refreshTokenNew = systemType + UUID.randomUUID().toString();
        /**
         * 1.获取旧缓存删除
         */
        //获取旧token
        String userTokenKey = RedisKeyUtil.getUserTokenKey(userId);
        String terminalKey = RedisKeyUtil.getUserTokenTerminalKey(userId, terminalMark);
        Token token = RedisCacheUtil.hashGet(userTokenKey, terminalKey, Token.class);
        //判空
        if (token == null) { //添加
            token = new Token();
        } else { //修改
            //删除旧的userToken
            String accessTokenKeyOld = RedisKeyUtil.getAccessTokenKey(token.getAccessToken());
            String refreshTokenKeyOld = RedisKeyUtil.getRefreshTokenKey(token.getRefreshToken());
            RedisCacheUtil.delete(accessTokenKeyOld);
            RedisCacheUtil.delete(refreshTokenKeyOld);
        }
        /**
         * 2.存入新的缓存
         */
        //存入新的token
        token.setAccessToken(accessTokenNew);
        token.setRefreshToken(refreshTokenNew);
        RedisCacheUtil.hashPut(userTokenKey, terminalKey, token);
        //封装userToken
        UserToken userToken = new UserToken();
        userToken.setAccessToken(accessTokenNew);
        userToken.setRefreshToken(refreshTokenNew);
        userToken.setTenantId(tenantId);
        userToken.setUserId(userId);
        userToken.setTerminalMark(terminalMark);
        userToken.setAppId(appId);
        userToken.setUserUuid(userUuid);
        userToken.setOrgId(orgId); //添加用户所属默认组织  需要其他的组织自行调用获取
        userToken.setExpireIn(SystemConstants.TOKEN_DEFAULT_DURATION);
        //存入新的userToken
        String accessTokenKeyNew = RedisKeyUtil.getAccessTokenKey(accessTokenNew);
        String refreshTokenKeyNew = RedisKeyUtil.getRefreshTokenKey(refreshTokenNew);
        RedisCacheUtil.valueObjSet(accessTokenKeyNew, userToken, SystemConstants.TOKEN_DEFAULT_DURATION);
        RedisCacheUtil.valueObjSet(refreshTokenKeyNew, userToken, SystemConstants.REFRESHTOKEN_DEFAULT_DURATION);
        return userToken;
    }


    private static Long addOrg(Long userId, Long tenantId) {
        VirtualOrgApi virtualOrgApi = ApplicationContextHelper.getBean(VirtualOrgApi.class);
        AddUserOrgReq addUserOrgReq = new AddUserOrgReq();
        AddUserReq userReq = new AddUserReq();
        userReq.setUserId(userId);
        addUserOrgReq.setUserReq(userReq);
        AddVirtualOrgReq orgReq = new AddVirtualOrgReq();
        orgReq.setTenantId(tenantId);
        String name = RandomUtils.nextInt(10) + "";
        orgReq.setName(name);
        addUserOrgReq.setOrgReq(orgReq);
        return virtualOrgApi.addUserOrg(addUserOrgReq);
    }

    /**
     * 删除token缓存
     *
     * @param userId
     * @param terminalMark
     */
    public static void deleteToken(Long userId, String terminalMark) {
        String userTokenKey = RedisKeyUtil.getUserTokenKey(userId);
        String terminalMarkKey = RedisKeyUtil.getUserTokenTerminalKey(userId, terminalMark);

        Token token = RedisCacheUtil.hashGet(userTokenKey, terminalMarkKey, Token.class);

        if (!StringUtils.isEmpty(token)) {
            //删除用户对应的hashMap元素
            RedisCacheUtil.hashRemove(userTokenKey, terminalMarkKey);

            //删除登录入信息
            String accessToken = token.getAccessToken();
            String refreshToken = token.getRefreshToken();
            String accessTokenKey = RedisKeyUtil.getAccessTokenKey(accessToken);
            String refreshTokenKey = RedisKeyUtil.getRefreshTokenKey(refreshToken);
            RedisCacheUtil.delete(accessTokenKey);
            RedisCacheUtil.delete(refreshTokenKey);
        }
    }

    /**
     * 校验凭证缓存
     *
     * @param userId
     * @param accessToken
     * @param terminalMark
     */
    public static UserToken checkToken(Long userId, String accessToken, String terminalMark) {

        //获取userToken
        String accessTokenKey = RedisKeyUtil.getAccessTokenKey(accessToken);
        UserToken userToken = RedisCacheUtil.valueObjGet(accessTokenKey, UserToken.class);
        if (StringUtils.isEmpty(userToken)) {
            throw new BusinessException(UserExceptionEnum.AUTH_REFRESH); //app 调用刷新token 接口获取
        }
        userId = userToken.getUserId();
        String userTokenKey = RedisKeyUtil.getUserTokenKey(userId);
        String terminalMarkKey = RedisKeyUtil.getUserTokenTerminalKey(userId, terminalMark);

        Token token = RedisCacheUtil.hashGet(userTokenKey, terminalMarkKey, Token.class);
        if (StringUtils.isEmpty(token)) {
            LOGGER.info("map.checkToken-loginRetry.userToken{},token{}", JSON.toJSONString(userToken), JSON.toJSONString(token));
            throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
        }

        //校验accessToken
        if (!token.getAccessToken().equals(accessToken)) {
            LOGGER.info("checkToken-loginRetry.userToken{},token{}", JSON.toJSONString(userToken), JSON.toJSONString(token));
            throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
        }
        return userToken;
    }

    /**
     * 主动刷新token缓存
     *
     * @param refreshToken
     * @param terminalMark
     * @return
     */
    public static UserToken refreshToken(String refreshToken, String terminalMark, String systemType) {
        String refreshTokenKey = RedisKeyUtil.getRefreshTokenKey(refreshToken);

        UserToken userToken = RedisCacheUtil.valueObjGet(refreshTokenKey, UserToken.class);
        if (StringUtils.isEmpty(userToken)) { //重新登入
            throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
        }

        Long userId = userToken.getUserId();
        String terminalMarkKey = RedisKeyUtil.getUserTokenTerminalKey(userId, terminalMark);

        //删除旧的accessToken
        String accessTokenOld = userToken.getAccessToken();
        String accessTokenKeyByOld = RedisKeyUtil.getAccessTokenKey(accessTokenOld);
        RedisCacheUtil.delete(accessTokenKeyByOld);

        //生成新的accessToken
        String accessTokenNew = systemType + UUID.randomUUID().toString();
        String accessTokenKeyNew = RedisKeyUtil.getAccessTokenKey(accessTokenNew);
        String userTokenKey = RedisKeyUtil.getUserTokenKey(userId);

        //生成新的refreshToken
        String refreshTokenNew = systemType + UUID.randomUUID().toString();
        String refreshTokenKeyNew = RedisKeyUtil.getRefreshTokenKey(refreshTokenNew);


        //封装token
        Token token = new Token();
        token.setAccessToken(accessTokenNew);
        token.setRefreshToken(refreshTokenNew);

        userToken.setAccessToken(accessTokenNew);
        userToken.setRefreshToken(refreshTokenNew);

        //更新缓存
        RedisCacheUtil.valueObjSet(accessTokenKeyNew, userToken, SystemConstants.TOKEN_DEFAULT_DURATION);
        RedisCacheUtil.valueObjSet(refreshTokenKeyNew, userToken, SystemConstants.REFRESHTOKEN_DEFAULT_DURATION);
        RedisCacheUtil.hashPut(userTokenKey, terminalMarkKey, token);

        return userToken;
    }
}
