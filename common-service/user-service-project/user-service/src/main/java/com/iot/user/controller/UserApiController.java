package com.iot.user.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.common.costants.VoiceBoxConfigConstant;
import com.iot.user.api.UserApi;
import com.iot.user.enums.AdminStatusEnum;
import com.iot.user.enums.UserLevelEnum;
import com.iot.user.enums.UserStatusEnum;
import com.iot.user.service.BossUserService;
import com.iot.user.service.CorpUserService;
import com.iot.user.service.SmartTokenService;
import com.iot.user.service.UserService;
import com.iot.user.util.OAuthIssuer;
import com.iot.user.util.RedisKeyUtil;
import com.iot.user.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserApiController implements UserApi {

    private Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private BossUserService bossUserService;
    @Autowired
    private SmartTokenService smartTokenService;

    @Override
    public Long register(@RequestBody RegisterReq req) {
        return userService.register(req);
    }

    @Override
    public LoginResp login(@RequestBody LoginReq req) {
        return userService.login(req);
    }

    @Override
    public LoginResp login2Robot(@RequestBody LoginRobotReq req) {
        return userService.login2Robot(req);
    }

    @Override
    public void logout(@RequestParam("userId") Long userId,
                       @RequestParam("terminalMark") String terminalMark) {
        userService.logout(userId, terminalMark);
    }

    @Override
    public FetchUserResp getUser(@RequestParam("userId") Long userId) {
        return userService.getUser(userId);
    }

    @Override
    public FetchUserResp getUserByUuid(@RequestParam("uuid") String uuid) {
        return userService.getUserByUuid(uuid);
    }

    @Override
    public FetchUserResp getUserByUserName(@RequestParam("userName") String userName) {
        return userService.getUserByUserName(userName);
    }

    @Override
    public String getUuid(@RequestParam("userId") Long userId) {
        return userService.getUuid(userId);
    }

    @Override
    public Long getUserId(@RequestParam("uuid") String uuid) {
        return userService.getUserId(uuid);
    }

    @Override
    public void updateUser(@RequestBody UpdateUserReq req) {
        userService.updateUser(req);
    }

    @Override
    public void modifyPwd(@RequestBody ModifyPwdReq req) {
        userService.modifyPwd(req);
    }

    @Override
    public void resetPwd(@RequestBody ResetPwdReq req) {
        userService.resetPwd(req);
    }

    @Override
    public void checkUserName(@RequestParam("userName") String userName, @RequestParam("tenantId") Long tenantId) {
        userService.checkUserName(userName,tenantId);
    }

    @Override
    public FetchUserResp getUserByName(@RequestParam("tenantId") Long tenantId, @RequestParam("userName") String userName) {
        return userService.getUserByName(tenantId, userName);
    }


    @Override
    public UserTokenResp checkUserToken(@RequestParam("userId") Long userId,
                                        @RequestParam("token") String token,
                                        @RequestParam("terminalMark") String terminalMark) {
        return userService.checkUserToken(userId, token, terminalMark);
    }

    @Override
    public UserTokenResp refreshUserToken(@RequestParam("refreshToken") String refreshToken,
                                          @RequestParam("terminalMark") String terminalMark,
                                          @RequestParam("systemType") String systemType) {
        return userService.refreshUserToken(refreshToken, terminalMark, systemType);
    }

    @Override
    public UserTokenResp getUserToken(@RequestParam("accessToken") String accessToken) {
        return userService.getUserToken(accessToken);
    }

    @Override
    public void sendVerifyCode(@RequestParam("email") String email,
                               @RequestParam("type") Byte type, @RequestParam("langage") String langage, @RequestParam("appId") Long appId) {
        userService.sendVerifyCode(email, type, langage, appId);
    }

    @Override
    public String getUserHeadImg(@RequestParam("userId") Long userId) {
        return userService.getUserHeadImg(userId);
    }

    @Override
    public void setUserHeadImg(@RequestParam("userId") Long userId, @RequestParam("headImg") String headImg) {
        userService.setUserHeadImg(userId, headImg);
    }

    @Override
    public String getBackground(@RequestParam("userId") Long userId) {
        return userService.getBackground(userId);
    }

    @Override
    public void setBackground(@RequestParam("userId") Long userId,
                              @RequestParam("background") String background) {
        userService.setBackground(userId, background);
    }

    /**
     *  生成 code
     * @param userUUID
     * @return
     */
    @Override
    public String createCode(@RequestParam("userUUID") String userUUID) {
        OAuthIssuer oau = new OAuthIssuer();
        String code = oau.authorizationCode();
        String key = RedisKeyUtil.getOauthCodeKey(code);
        RedisCacheUtil.valueSet(key, userUUID, 600L);
        return code;
    }

    /**
     *  根据code 生成 token
     * @param type
     * @param code
     * @return
     */
    @Override
    public UserTokenResp createOauthTokenByCode(@RequestParam("type") String type, @RequestParam("code") String code) {
        return userService.createOauthTokenByCode(type, code);
    }

    /**
     *  根据code 生成 token
     * @param oauthTokenCreateVO
     * @return
     */
    @Override
    public UserTokenResp createOauthTokenByCodeVo(@RequestBody OauthTokenCreateVO oauthTokenCreateVO) {
        UserTokenResp res = userService.createOauthTokenByCode(oauthTokenCreateVO.getClientType(), oauthTokenCreateVO.getCode());
        if (res != null) {
            if (VoiceBoxConfigConstant.SKILL_TYPE_SMART_HOME.equals(oauthTokenCreateVO.getSkillType())) {
                // smartHome技能才会在 smart_token表生成记录
                SmartTokenResp resp = null;
                if (oauthTokenCreateVO.getSmartType() != null) {
                    resp = smartTokenService.getSmartTokenByUserIdAndSmart(res.getUserId(), oauthTokenCreateVO.getSmartType());
                } else if (oauthTokenCreateVO.getThirdPartyInfoId() != null) {
                    resp = smartTokenService.getByUserIdAndThirdPartyInfoId(res.getUserId(), oauthTokenCreateVO.getThirdPartyInfoId());
                }
                if (resp != null) {
                    smartTokenService.updateLocalTokenById(resp.getId(), res.getAccessToken(), res.getRefreshToken());
                }
            }
        }
        return res;
    }

    /**
     *  根据 refreshToken 生成 token
     * @param type
     * @param token     refreshToken
     * @return
     */
    @Override
    public UserTokenResp createOauthTokenByRefreshToken(@RequestParam("type") String type, @RequestParam("token") String token) {
        return userService.createOauthTokenByRefreshToken(type, token);
    }

    /**
     *  根据 refreshToken 生成 token
     * @param oauthTokenCreateVO
     * @return
     */
    @Override
    public UserTokenResp createOauthTokenByRefreshTokenVo(@RequestBody OauthTokenCreateVO oauthTokenCreateVO) {
        UserTokenResp res =  userService.createOauthTokenByRefreshToken(oauthTokenCreateVO.getClientType(), oauthTokenCreateVO.getRefreshToken());
        if (res != null) {
            if (VoiceBoxConfigConstant.SKILL_TYPE_SMART_HOME.equals(oauthTokenCreateVO.getSkillType())) {
                // smartHome技能才会在 smart_token表生成记录
                SmartTokenResp resp = null;
                if (oauthTokenCreateVO.getSmartType() != null) {
                    resp = smartTokenService.getSmartTokenByUserIdAndSmart(res.getUserId(), oauthTokenCreateVO.getSmartType());
                } else if (oauthTokenCreateVO.getThirdPartyInfoId() != null) {
                    resp = smartTokenService.getByUserIdAndThirdPartyInfoId(res.getUserId(), oauthTokenCreateVO.getThirdPartyInfoId());
                }
                if (resp != null) {
                    smartTokenService.updateLocalTokenById(resp.getId(), res.getAccessToken(), res.getRefreshToken());
                }
            }
        }
        return res;
    }

    /**
     *  获取 授权后的 用户信息
     * @param type
     * @param token
     * @return
     */
    @Override
    public LoginResp getOauthUserInfo(@RequestParam("type") String type, @RequestParam("token") String token) {
        LoginResp res = new LoginResp();
        String key = RedisKeyUtil.getOauthAccessTokenKey(type, token);
        String userUUID = RedisCacheUtil.valueGet(key);
        if (userUUID == null) {
            return null;
        }

        //String mqttPw = RedisCacheUtil.hashGetString(type + "-mqtt", userUUID);
        FetchUserResp userResp = userService.getUserByUuid(userUUID);
        res.setUserId(userResp.getId());
        res.setUserUuid(userUUID);
        //res.setMqttPassword(mqttPw);
        res.setTenantId(userResp.getTenantId());
        return res;
    }

    @Override
    public void deleteOauthToken(@RequestParam("type") String type, @RequestParam("token") String token, @RequestParam("refreshToken") String refreshToken) {
        log.info("deleteOauthToken, type={}, token={}, refreshToken={}", type, token, refreshToken);
        if (StringUtil.isBlank(type) || StringUtil.isBlank(token)) {
            return ;
        }
        RedisCacheUtil.delete(RedisKeyUtil.getOauthAccessTokenKey(type, token));
        RedisCacheUtil.delete(RedisKeyUtil.getOauthRefreshTokenKey(type, refreshToken));
    }

    @Override
    public FetchUserResp getUserByCondition(@RequestBody LoginReq user) {
        return userService.getUserByCondition(user);
    }

    @Override
    public FetchUserResp getUserByUserNameTenantId(@RequestParam("userName") String userName, @RequestParam("tenantId") Long tenantId) {
        return userService.getUserByUserNameTenantId(userName, tenantId);
    }

    /**
     * 
     * 描述：获取租户信息根据租户ID
     * @author 李帅
     * @created 2018年10月22日 上午10:47:28
     * @since 
     * @param
     * @return
     */
    @Override
    public List<FetchUserResp> getAdminUserByTenantId(@RequestBody List<Long> tenantIds) {
        return userService.getAdminUserByTenantId(tenantIds);
    }
    
    /**
     * 
     * 描述：获取租户主账号信息根据用户名称
     * @author 李帅
     * @created 2018年11月13日 下午7:02:24
     * @since 
     * @param userName
     * @return
     */
    @Override
    public FetchUserResp getAdminUserByUserName(@RequestParam("userName") String userName) {
        return userService.getAdminUserByUserName(userName);
    }
    
    @Override
    public String getRegisterCode(@RequestParam("email") String email) {
        return userService.getRegisterCode(email);
    }

    @Override
    public Map<Long, String> getBathUuid(@RequestParam("userIdList") List<Long> userIdList) {
        return userService.getBathUuid(userIdList);
    }

    /****************************************  2B function ***************************************************************/
    @Override
    public Long register2B(@RequestBody RegisterReq req) {
        return corpUserService.register(req);
    }

    @Override
    public LoginResp login2B(@RequestBody LoginReq req) {
        return corpUserService.login(req);
    }

    @Override
    public Long improveTenantInfo(@RequestBody TenantReq req) {
        return corpUserService.improveTenantInfo(req);
    }
    /**
     *@description 保存企业信息
     *@author wucheng
     *@params [req]
     *@create 2018/12/15 14:19
     *@return java.lang.Long
     */
    @Override
    public Long saveTenantInfo(@RequestBody TenantReq req) {
        return corpUserService.saveTenantInfo(req);
    }
    @Override
    public Map<Long, FetchUserResp> getByUserIds(@RequestParam("userIdList") List<Long> userIdList) {
        return corpUserService.getByUserIds(userIdList);
    }

    @Override
    public String getVerifyCodeImage(@RequestParam("userName") String userName) {
        return corpUserService.getVerifyCodeImage(userName);
    }

    @Override
    public void resetPwd2B(@RequestBody ResetPwdReq req) {
        corpUserService.resetPwd(req);
    }

    @Override
    public Integer checkUserName2B(@RequestParam(value = "userName") String userName) {
        return corpUserService.checkUserName(userName);
    }

    /**
     * 描述：删除用户
     * @author maochengyuan
     * @created 2018/7/13 18:07
     * @param userId
     * @return int
     */
    @Override
    public int deleteUserByUserId(@RequestParam(value = "userId") Long userId) {
        return this.userService.updateUserStatusByUserId(userId, UserStatusEnum.DELETED);
    }

    /**
     * 描述：创建子账号
     * @author nongchongwei
     * @date 2018/11/5 15:49
     * @param
     * @return
     */
    @Override
    public Long addSubCorpUser(@RequestBody RegisterReq req) {
        log.info("addSubCorpUser {}", JSON.toJSONString(req));
        return corpUserService.addSubCorpUser(req);
    }

    /**
     * 描述：查询子账号
     * @author nongchongwei
     * @date 2018/11/5 15:49
     * @param
     * @return
     */
    @Override
    public PageInfo<FetchUserResp> querySubUserList(@RequestBody UserSearchReq req) {
        log.info("addSubCorpUser {}", JSON.toJSONString(req));
        req.setUserLevel(UserLevelEnum.BUSINESS.getCode());
        req.setAdminStatus(AdminStatusEnum.NORMAL.getCode());
        return corpUserService.querySubUserList(req);
    }

    @Override
    public PageInfo<FetchUserResp> getUserPageList(@RequestBody UserSearchReq searchReq) {
        searchReq.setUserLevel(UserLevelEnum.CONSUMER.getCode());
        return corpUserService.getUserPageList(searchReq);
    }

    @Override
    public PageInfo<FetchUserResp> getCorpUserPageList(@RequestBody UserSearchReq searchReq) {
        searchReq.setUserLevel(UserLevelEnum.BUSINESS.getCode());
        return corpUserService.getUserPageList(searchReq);
    }

    @Override
    public void sendCorpVerifyCode(@RequestParam("email") String email,
                                   @RequestParam("type") Byte type, @RequestParam("language") String language) {
        corpUserService.sendCorpVerifyCode(email, type, language);
    }


    public LoginResp login2Boss(@RequestBody LoginReq req){
        return bossUserService.login(req);
    }

    public Long register2Boss(@RequestBody RegisterReq req){
        return bossUserService.register(req);
    }

    public void auditUser(@RequestBody RegisterReq registerReq){
        bossUserService.auditUser(registerReq);
    }

    @Override
    public Page<UserResp> getUserList(@RequestBody UserSearchReq req) {
        return userService.getUseList(req);
    }

    @Override
    public Long addUser(@RequestBody UserReq req) {
        return userService.addUser(req);
    }

    @Override
    public void editUser(@RequestBody UserReq req) {
        userService.editUser(req);
    }

    @Override
    public void deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
    }

    @Override
    public void cancelUser(@RequestParam("userId") Long userId,@RequestParam("userUuid") String userUuid,@RequestParam("tenantId") Long tenantId) {
        userService.cancelUser(userId,userUuid,tenantId);
    }

    public void checkUser(@RequestBody RegisterReq req){
        bossUserService.checkUser(req);
    }

    @Override
    public boolean verifyAccount(@RequestBody VerifyUserReq userReq) {
        return userService.verifyAccount(userReq);
    }

    public void checkUserHadRight(@RequestParam("id") Long userId){
        bossUserService.checkUserHadRight(userId);
    }

    /**
     * 获取用户信息根据用户名租户---TOB使用
     * @param userName
     * @param tenantId
     * @return
     */
    @Override
    public FetchUserResp getUserByUserNameTenantIdTOB(@RequestParam("userName") String userName, @RequestParam("tenantId") Long tenantId) {
        return userService.getUserByUserNameTenantIdTOB(userName, tenantId);
    }

    @Override
    public int updatePasswordByUserId(Long userId, String password) {
        return userService.updatePasswordByUserId(userId, password);
    }

    @Override
    public FetchUserResp getBusinessUserByUserName(String userName) {
        return userService.getBusinessUserByUserName(userName);
    }

    @Override
    public Long getAppUserCount(@RequestParam("tenantId") Long tenantId) {
        return userService.getAppUserCount(tenantId);
    }
}
