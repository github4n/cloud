package com.iot.user.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;
import com.iot.user.vo.*;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 描述：用户熔断类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/9 14:14
 */
@Component
public class UserApiFallbackFactory implements FallbackFactory<UserApi> {

    private final Logger logger = LoggerFactory.getLogger(UserApiFallbackFactory.class);

    @Override
    public UserApi create(Throwable cause) {
        return new UserApi() {
            @Override
            public Long register(RegisterReq req) {
                return -1L;
            }

            @Override
            public LoginResp login(LoginReq req) {
                return null;
            }

            @Override
            public void logout(Long uid, String terminalMark) {
            }

            @Override
            public FetchUserResp getUser(Long userId) {
                return null;
            }

            @Override
            public FetchUserResp getUserByUuid(String uuid) {
                return null;
            }

            @Override
            public FetchUserResp getUserByUserName(String userName) {
                return null;
            }
            
            @Override
            public String getUuid(Long userId) {
                return null;
            }

            @Override
            public Long getUserId(String uuid) {
                return null;
            }

            @Override
            public void updateUser(UpdateUserReq appUser) {
            }

            @Override
            public void modifyPwd(ModifyPwdReq req) {
            }

            @Override
            public void resetPwd(ResetPwdReq req) {
            }

            @Override
            public void checkUserName(String userName, Long tenantId) {
            }

            @Override
            public FetchUserResp getUserByName(Long tenantId, String userName) {
                return null;
            }

            @Override
            public UserTokenResp checkUserToken(Long userId, String token, String terminalMark) {
                return null;
            }

            @Override
            public UserTokenResp refreshUserToken(String refreshToken, String terminalMark, String systemType) {
                return null;
            }

            @Override
            public UserTokenResp getUserToken(String accessToken) {
                return null;
            }

            @Override
            public void sendVerifyCode(String email, Byte type, String langage, Long appId) {

            }

            @Override
            public void sendCorpVerifyCode(String email, Byte type, String language) {

            }

            @Override
            public String getUserHeadImg(Long userId) {
                return null;
            }

            @Override
            public void setUserHeadImg(Long userId, String headImg) {
            }

            @Override
            public String getBackground(Long userId) {
                return null;
            }

            @Override
            public void setBackground(Long userId, String background) {
            }

            @Override
            public LoginResp login2B(LoginReq req) {
                return null;
            }

            @Override
            public Long improveTenantInfo(TenantReq req) {
                return null;
            }

            @Override
            public Map<Long, FetchUserResp> getByUserIds(List<Long> userIdList) {
                return null;
            }

            @Override
            public String getVerifyCodeImage(String userName) {
                return null;
            }

            @Override
            public LoginResp login2Robot(LoginRobotReq req) {
                return null;
            }

            @Override
            public String createCode(String userUUID) {
                return null;
            }

            @Override
            public UserTokenResp createOauthTokenByCode(String type, String code) {
                return null;
            }

            @Override
            public UserTokenResp createOauthTokenByCodeVo(OauthTokenCreateVO oauthTokenCreateVO) {
                return null;
            }

            @Override
            public UserTokenResp createOauthTokenByRefreshToken(String type, String token) {
                return null;
            }

            @Override
            public UserTokenResp createOauthTokenByRefreshTokenVo(OauthTokenCreateVO oauthTokenCreateVO) {
                return null;
            }

            @Override
            public LoginResp getOauthUserInfo(String type, String token) {
                return null;
            }

            @Override
            public void deleteOauthToken(String type, String token, String refreshToken) {

            }

            @Override
            public FetchUserResp getUserByCondition(LoginReq user) {
                return null;
            }

            @Override
            public FetchUserResp getUserByUserNameTenantId(String userName, Long tenantId) {
                return null;
            }

            @Override
            public List<FetchUserResp> getAdminUserByTenantId(List<Long> tenantIds) {
            	return null;
            }
            
            @Override
            public FetchUserResp getAdminUserByUserName(String userName){
            	return null;
            }
            
            @Override
            public String getRegisterCode(String email) {
                return null;
            }

            @Override
            public Long register2B(RegisterReq req) {
                return null;
            }

            @Override
            public void resetPwd2B(ResetPwdReq req) {

            }

            @Override
            public Integer checkUserName2B(String userName) {
                return null;
            }

            @Override
            public int deleteUserByUserId(Long userId) {
                return 0;
            }

            @Override
            public PageInfo<FetchUserResp> getUserPageList(UserSearchReq searchReq) {
                return null;
            }

            @Override
            public PageInfo<FetchUserResp> getCorpUserPageList(UserSearchReq searchReq) {
                return null;
            }
            


            @Override
            public Long addSubCorpUser(RegisterReq req) {
                logger.info("addSubCorpUser.error:{}", JSON.toJSONString(req));
                return null;
            }

            @Override
            public PageInfo<FetchUserResp> querySubUserList(UserSearchReq req) {
                return null;
            }

            @Override
            public Map<Long, String> getBathUuid(List<Long> userIdList) {
                return null;
            }

            @Override
            public LoginResp login2Boss(LoginReq req) {
                return null;
            }

            @Override
            public Long register2Boss(RegisterReq req) {
                return null;
            }

            @Override
            public void auditUser(RegisterReq registerReq) {

            }

            @Override
            public Page<UserResp> getUserList(UserSearchReq req) {
                return null;
            }

            @Override
            public Long addUser(UserReq req) {
                return null;
            }

            @Override
            public void editUser(UserReq req) {

            }

            @Override
            public void deleteUser(Long id) {

            }

            @Override
            public void cancelUser(Long userId, String userUuid, Long tenantId) {

            }

            @Override
            public void checkUser(RegisterReq req) {

            }

            @Override
            public boolean verifyAccount(VerifyUserReq userReq) {
                return false;
            }

            @Override
            public void checkUserHadRight(Long userId) {

            }

            @Override
            public FetchUserResp getUserByUserNameTenantIdTOB(String username, Long tenantId) {
                return null;
            }

            @Override
            public int updatePasswordByUserId(Long userId, String password) { return 0;}

            @Override
            public FetchUserResp getBusinessUserByUserName(String userName) {
                return null;
            }

            @Override
            public Long saveTenantInfo(TenantReq req) {
                return null;
            }

            @Override
            public Long getAppUserCount(Long tenantId) {
                return null;
            }
        };
    }
}
