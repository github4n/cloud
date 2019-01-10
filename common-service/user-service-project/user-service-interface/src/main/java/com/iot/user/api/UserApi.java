package com.iot.user.api;

import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;
import com.iot.user.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

//, url = "http://220.160.105.203:8443"
@Api("用户接口")
@FeignClient(value = "user-service", fallbackFactory = UserApiFallbackFactory.class)
@RequestMapping("/user")
public interface UserApi {

    @ApiOperation("用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long register(@RequestBody RegisterReq req);

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    LoginResp login(@RequestBody LoginReq req);

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login2Robot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    LoginResp login2Robot(@RequestBody LoginRobotReq req);

    @ApiOperation("退出登录")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    void logout(@RequestParam("userId") Long userId,
                @RequestParam("terminalMark") String terminalMark);

    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    FetchUserResp getUser(@RequestParam("userId") Long userId);

    @ApiOperation("获取用户信息根据uuid")
    @RequestMapping(value = "/getUserByUuid", method = RequestMethod.GET)
    FetchUserResp getUserByUuid(@RequestParam("uuid") String uuid);

    @ApiOperation("获取用户信息根据用户名")
    @RequestMapping(value = "/getUserByUserName", method = RequestMethod.GET)
    FetchUserResp getUserByUserName(@RequestParam("userName") String userName);

    @ApiOperation("根据用户名称获取用户信息（用户级别为BUSINESS）")
    @RequestMapping(value = "/getBusinessUserByUserName", method = RequestMethod.GET)
    FetchUserResp getBusinessUserByUserName(@RequestParam("userName") String userName);

    @ApiOperation("获取用户uuid")
    @RequestMapping(value = "/getUuid", method = RequestMethod.GET)
    String getUuid(@RequestParam("userId") Long userId);

    @ApiOperation("获取用户主键")
    @RequestMapping(value = "/getUserId", method = RequestMethod.GET)
    Long getUserId(@RequestParam("uuid") String uuid);

    @ApiOperation("修改用户信息")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(@RequestBody UpdateUserReq appUser);

    @ApiOperation("修改密码")
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void modifyPwd(@RequestBody ModifyPwdReq req);

    @ApiOperation("重置密码")
    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void resetPwd(@RequestBody ResetPwdReq req);

    @ApiOperation("检查用户名")
    @RequestMapping(value = "/checkUserName", method = RequestMethod.GET)
    void checkUserName(@RequestParam("userName") String userName, @RequestParam("tenantId") Long tenantId);


    @ApiOperation("检查用户名")
    @RequestMapping(value = "/getUserByName", method = RequestMethod.GET)
    FetchUserResp getUserByName(@RequestParam("tenantId") Long tenantId, @RequestParam("userName") String userName);

    @ApiOperation("检测凭证")
    @RequestMapping(value = "/checkUserToken", method = RequestMethod.GET)
    UserTokenResp checkUserToken(@RequestParam("userId") Long userId,
                                 @RequestParam("token") String token,
                                 @RequestParam("terminalMark") String terminalMark);

    @ApiOperation("刷新凭证")
    @RequestMapping(value = "/refreshUserToken", method = RequestMethod.GET)
    UserTokenResp refreshUserToken(@RequestParam("refreshToken") String refreshToken,
                                   @RequestParam("terminalMark") String terminalMark,
                                   @RequestParam("systemType") String systemType);

    @ApiOperation("获取凭证")
    @RequestMapping(value = "/getUserToken", method = RequestMethod.GET)
    UserTokenResp getUserToken(@RequestParam("accessToken") String accessToken);

    @ApiOperation("发送验证码")
    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.GET)
    void sendVerifyCode(@RequestParam("email") String email,
                        @RequestParam("type") Byte type, @RequestParam("langage") String langage, @RequestParam("appId") Long appId);

    @ApiOperation("发送验证码2B")
    @RequestMapping(value = "/sendCorpVerifyCode", method = RequestMethod.GET)
    void sendCorpVerifyCode(@RequestParam("email") String email,
                               @RequestParam("type") Byte type, @RequestParam("language") String language);

    @ApiOperation("获取用户头像")
    @RequestMapping(value = "/getUserHeadImg", method = RequestMethod.GET)
    String getUserHeadImg(@RequestParam("userId") Long userId);

    @ApiOperation("修改用户头像")
    @RequestMapping(value = "/setUserHeadImg", method = RequestMethod.GET)
    void setUserHeadImg(@RequestParam("userId") Long userId,
                        @RequestParam("headImg") String headImg);

    @ApiOperation("获取背景图片")
    @RequestMapping(value = "/getBackground", method = RequestMethod.GET)
    String getBackground(@RequestParam("userId") Long userId);

    @ApiOperation("修改背景图片")
    @RequestMapping(value = "/setBackground", method = RequestMethod.GET)
    void setBackground(@RequestParam("userId") Long userId,
                       @RequestParam("background") String background);

    @ApiOperation("生成code")
    @RequestMapping(value = "/createCode", method = RequestMethod.GET)
    String createCode(@RequestParam("userUUID") String userUUID);

    @ApiOperation("生成oauth-token")
    @RequestMapping(value = "/createOauthTokenByCode", method = RequestMethod.GET)
    UserTokenResp createOauthTokenByCode(@RequestParam("type") String type, @RequestParam("code") String code);

    @ApiOperation("生成oauth-token")
    @RequestMapping(value = "/createOauthTokenByCodeVo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserTokenResp createOauthTokenByCodeVo(@RequestBody OauthTokenCreateVO oauthTokenCreateVO);

    @ApiOperation("生成oauth-token")
    @RequestMapping(value = "/createOauthTokenByRefreshToken", method = RequestMethod.GET)
    UserTokenResp createOauthTokenByRefreshToken(@RequestParam("type") String type, @RequestParam("token") String token);

    @ApiOperation("生成oauth-token")
    @RequestMapping(value = "/createOauthTokenByRefreshTokenVo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserTokenResp createOauthTokenByRefreshTokenVo(@RequestBody OauthTokenCreateVO oauthTokenCreateVO);

    @ApiOperation("获取oauth-用户信息")
    @RequestMapping(value = "/getOauthUserInfo", method = RequestMethod.GET)
    LoginResp getOauthUserInfo(@RequestParam("type") String type, @RequestParam("token") String token);

    @ApiOperation("删除oauthToken")
    @RequestMapping(value = "/deleteOauthToken", method = RequestMethod.GET)
    void deleteOauthToken(@RequestParam("type") String type, @RequestParam("token") String token, @RequestParam("refreshToken") String refreshToken);

    @ApiOperation("查询用户信息")
    @RequestMapping(value = "/getUserByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    FetchUserResp getUserByCondition(@RequestBody LoginReq user);

    @ApiOperation("获取用户信息根据用户名租户")
    @RequestMapping(value = "/getUserByUserNameTenantId", method = RequestMethod.GET)
    FetchUserResp getUserByUserNameTenantId(@RequestParam("userName") String userName, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("获取租户主账号信息根据租户ID")
    @RequestMapping(value = "/getAdminUserByTenantId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<FetchUserResp> getAdminUserByTenantId(@RequestBody List<Long> tenantIds);
    
    /**
     * 
     * 描述：获取租户主账号信息根据用户名称
     * @author 李帅
     * @created 2018年11月13日 下午7:02:04
     * @since 
     * @param userName
     * @return
     */
    @ApiOperation("获取租户主账号信息根据用户名称")
    @RequestMapping(value = "/getAdminUserByUserName", method = RequestMethod.GET)
    FetchUserResp getAdminUserByUserName(@RequestParam("userName") String userName);
    //////////////////////////////测试使用//////////////////////////////////////

    @ApiOperation("获取注册验证码")
    @RequestMapping(value = "/getRegisterCode", method = RequestMethod.GET)
    String getRegisterCode(@RequestParam("email") String email);

    /*********************************************  2B function ********************************************************/
    @ApiOperation("2B用户注册")
    @RequestMapping(value = "/register2B", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long register2B(@RequestBody RegisterReq req);

    @ApiOperation("2B重置密码")
    @RequestMapping(value = "/resetPwd2B", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void resetPwd2B(@RequestBody ResetPwdReq req);

    @ApiOperation("2B校验用户名")
    @RequestMapping(value = "/checkUserName2B", method = RequestMethod.POST)
    Integer checkUserName2B(@RequestParam("userName") String userName);

    @ApiOperation("用户登录2B")
    @RequestMapping(value = "/login2B", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    LoginResp login2B(@RequestBody LoginReq req);

    @ApiOperation("完善企业信息")
    @RequestMapping(value = "/improveTenantInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long improveTenantInfo(@RequestBody TenantReq req);

    /**
     *@description 保存企业信息
     *@author wucheng
     *@params [req]
     *@create 2018/12/15 14:18
     *@return java.lang.Long
     */
    @ApiOperation("保存企业信息")
    @RequestMapping(value = "/saveTenantInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveTenantInfo(@RequestBody TenantReq req);
    
    /**
     *@description 获取App用户注册总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 14:00
     *@return java.lang.Long
     */
    @ApiOperation("获取租户用户注册总数")
    @RequestMapping(value = "/getAppUserCount", method = RequestMethod.POST)
    Long getAppUserCount(@RequestParam("tenantId") Long tenantId);

    @ApiOperation("批量获取用户信息")
    @RequestMapping(value = "/getByUserIds", method = RequestMethod.POST)
    Map<Long, FetchUserResp> getByUserIds(@RequestParam("userIdList") List<Long> userIdList);

    @ApiOperation("获取图片验证码")
    @RequestMapping(value = "/getVerifyCodeImage", method = RequestMethod.GET)
    String getVerifyCodeImage(@RequestParam("userName") String userName);

    @ApiOperation("依据用户id删除用户")
    @RequestMapping(value = "/deleteUserByUserId", method = RequestMethod.GET)
    int deleteUserByUserId(@RequestParam("userId") Long userId);

    @ApiOperation("分页获取用户信息")
    @RequestMapping(value = "/getUserPageList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo<FetchUserResp> getUserPageList(@RequestBody UserSearchReq searchReq);
    
    @ApiOperation("分页获取企业用户信息")
    @RequestMapping(value = "/getCorpUserPageList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo<FetchUserResp> getCorpUserPageList(@RequestBody UserSearchReq searchReq);

    @ApiOperation("添加子账号")
    @RequestMapping(value = "/addSubCorpUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addSubCorpUser(@RequestBody RegisterReq req);

    @ApiOperation("查询子账号")
    @RequestMapping(value = "/querySubUserList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo<FetchUserResp> querySubUserList(@RequestBody UserSearchReq req);

    @ApiOperation("批量获取用户uuid")
    @RequestMapping(value = "/getBathUuid", method = RequestMethod.POST)
    Map<Long,String> getBathUuid(@RequestParam("userIdList") List<Long> userIdList);

    ///////////////boss////////////
    @ApiOperation("用户登录Boss")
    @RequestMapping(value = "/login2Boss", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    LoginResp login2Boss(@RequestBody LoginReq req);

    @ApiOperation("Boss用户注册")
    @RequestMapping(value = "/register2Boss", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long register2Boss(@RequestBody RegisterReq req);

    @ApiOperation("Boss用户授权")
    @RequestMapping(value = "/auditUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void auditUser(@RequestBody RegisterReq registerReq);

    @ApiOperation("获取用户信息列表")
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<UserResp> getUserList(@RequestBody UserSearchReq req);

    @ApiOperation("新增用户")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addUser(@RequestBody UserReq req);

    @ApiOperation("编辑用户")
    @RequestMapping(value = "/editUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void editUser(@RequestBody UserReq req);

    @ApiOperation("删除用户")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    void deleteUser(@RequestParam("id") Long id);

    @ApiOperation("注销用户")
    @RequestMapping(value = "/cancelUser", method = RequestMethod.GET)
    void cancelUser(@RequestParam("userId") Long userId,@RequestParam("userUuid") String userUuid,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("Boss用户检查")
    @RequestMapping(value = "/checkUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void checkUser(@RequestBody RegisterReq req);

    @ApiOperation("Boss用户检查")
    @RequestMapping(value = "/verifyAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean verifyAccount(@RequestBody VerifyUserReq userReq);

    @ApiOperation("Boss用户权限检查")
    @RequestMapping(value = "/checkUserHadRight", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void checkUserHadRight(@RequestParam("id") Long userId);

    @ApiOperation("获取用户信息根据用户名租户---TOB使用")
    @RequestMapping(value = "/getUserByUserNameTenantIdTOB", method = RequestMethod.GET)
    FetchUserResp getUserByUserNameTenantIdTOB(@RequestParam("userName") String userName, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("修改用户密码或修改用户子账号密码")
    @RequestMapping(value="/updatePasswordByUserId", method = RequestMethod.POST)
    int updatePasswordByUserId(@RequestParam("userId") Long userId, @RequestParam("password") String password);
}
