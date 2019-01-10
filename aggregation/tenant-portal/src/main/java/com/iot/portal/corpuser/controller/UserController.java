package com.iot.portal.corpuser.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.iot.common.beans.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.permission.vo.RoleDto;
import com.iot.portal.corpuser.service.UserBusinessService;
import com.iot.portal.corpuser.vo.ChangePwdReq;
import com.iot.portal.corpuser.vo.ResetPasswordReq;
import com.iot.portal.corpuser.vo.SendEmailReq;
import com.iot.portal.corpuser.vo.TenantUpdateReq;
import com.iot.portal.corpuser.vo.UserLoginReq;
import com.iot.portal.corpuser.vo.UserRegisterReq;
import com.iot.portal.corpuser.vo.UserToken;
import com.iot.portal.corpuser.vo.UserUpdateReq;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;
import com.iot.user.constant.UserConstants;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.UserSearchReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 用户服务
     */
    @Autowired
    private UserBusinessService userBusiness;

    /**
     * 描述：用户注册
     * @author nongchongwei
     * @date 2018/7/4 14:13
     * @param
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse userRegister(@RequestBody UserRegisterReq user) {
        this.userBusiness.userRegister(user);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：用户登录
     * @author nongchongwei
     * @date 2018/7/4 15:42
     * @param ulpVO 用户登录对象
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @RequestMapping(value = "/userLogin", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse userLogin(@RequestBody UserLoginReq ulpVO) {
        return ResultMsg.SUCCESS.info(this.userBusiness.userLogin(ulpVO));
    }

    /**
     * 描述：用户退出
     * @author nongchongwei
     * @date 2018/7/4 15:43
     * @param  request terminalMark 终端类型（app|web）
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "用户退出", notes = "用户退出")
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    public CommonResponse userLogout(HttpServletRequest request) {
        String terminalMark = request.getHeader(UserConstants.HEADER_TERMINALMARK); //header 終端類型 app web等
        this.userBusiness.userLogout(terminalMark);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：查询用户详细信息
     * @author nongchongwei
     * @date 2018/7/4 15:43
     * @param
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询用户详细信息", notes = "查询用户详细信息")
    @RequestMapping(value = "/getUser", method = {RequestMethod.POST})
    public CommonResponse getUserInfo() {
        return ResultMsg.SUCCESS.info(this.userBusiness.getUserInfo());
    }

    /**
     * 描述：修改用户详细信息
     * @author nongchongwei
     * @date 2018/7/4 15:43
     * @param user 用户对象
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "修改用户详细信息", notes = "修改用户详细信息")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse editUserInfo(@RequestBody UserUpdateReq user) {
        this.userBusiness.editUserInfo(user);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：上传头像
     * @author nongchongwei
     * @date 2018/7/4 15:44
     * @param multipartRequest 文件对象
     * @param headImg 系统头像编号
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "uploadPhoto", notes = "上传头像")
    @ApiImplicitParams({@ApiImplicitParam(name = "multipartRequest", value = "头像文件", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "headImg", value = "头像地址", required = false, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    public CommonResponse uploadPhoto(MultipartHttpServletRequest multipartRequest, @RequestParam(value = "headImg", required = false) String headImg) {
        String headImgUrl = this.userBusiness.uploadPhoto(multipartRequest, headImg);
        return ResultMsg.SUCCESS.info(headImgUrl);
    }

    /**
     * 描述：修改密码
     * @author nongchongwei
     * @date 2018/7/4 15:45
     * @param
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "oldPassword", value = "旧密码（经md5加密）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码（经md5加密）", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/modifyPwd", method = {RequestMethod.POST})
    public CommonResponse changePassword(@RequestBody ChangePwdReq req) {
        this.userBusiness.changePassword(req.getOldPassword(), req.getNewPassword());
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：重置密码
     * @author nongchongwei
     * @date 2018/7/4 15:45
     * @param
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "账号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "newPwd", value = "新密码（经md5加密）", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/resetPwd", method = {RequestMethod.POST})
    public CommonResponse resetPassword(@RequestBody ResetPasswordReq req) {
        this.userBusiness.resetPassword(req.getUserName(), req.getVerifyCode(), req.getNewPassWord());
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：创建子账户
     * @author nongchongwei
     * @date 2018/7/4 15:45
     * @param user 用户对象
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "创建子账户", notes = "创建子账户")
    @RequestMapping(value = "/createSubUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createSubUser(@RequestBody UserRegisterReq user) {
        this.userBusiness.createSubUser(user);
        return ResultMsg.SUCCESS.info();
    }
    /**
     * 描述：查询子账户
     * @author nongchongwei
     * @date 2018/7/4 15:45
     * @param
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询子账户", notes = "查询子账户")
    @RequestMapping(value = "/querySubUserList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse querySubUserList(@RequestBody SearchParam searchParam) {
        return ResultMsg.SUCCESS.info(this.userBusiness.querySubUserList(searchParam));
    }

    /**
     * 描述：删除子账户
     * @author wucheng
     * @date 2018/11/5 20:31
     * @param userId 当前子账户的用户id
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "删除子账户", notes = "删除字账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "当前子账户的用户id", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/deleteSubUser", method = RequestMethod.POST)
    public CommonResponse deleteSubUser(@RequestParam("userId") Long userId) {
        return ResultMsg.SUCCESS.info(this.userBusiness.deleteSubUser(userId));
    }

    /**
     * 描述：编辑子账户
     * @author wucheng
     * @date 2018/11/5 20:31
     * @param userId 当前子账户id
     * @param roleIds 用户绑定的角色id集合List<Long> roleIds
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "编辑子账户", notes = "编辑子账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds", value = "角色ids", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "当前子账户id", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/editSubUser", method = RequestMethod.POST)
    public CommonResponse editSubUser( @RequestParam("roleIds") String roleIds,@RequestParam("userId") Long userId) {
        return ResultMsg.SUCCESS.info(this.userBusiness.editSubUser(roleIds, userId));
    }

    /**
     * 描述：修改子账户密码
     * @param userId
     * @param tenantId
     * @param newPassword
     * @param oldPassword
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "修改子账户密码", notes = "修改子账户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "当前子账户id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "newPassword", value = "(md5)新密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "oldPassword", value = "(md5)旧密码", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/updatePasswordByUserId", method = RequestMethod.POST)
    public CommonResponse  updatePasswordByUserId(@RequestParam("userId") Long userId, @RequestParam("newPassword") String newPassword,@RequestParam("oldPassword") String oldPassword) {
       return ResultMsg.SUCCESS.info(this.userBusiness.updatePasswordByUserId(userId,newPassword,oldPassword));

    }
    /**
     * 描述：刷新凭证
     * @author nongchongwei
     * @date 2018/7/4 15:46
     * @param
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "刷新凭证", notes = "刷新凭证")
    @ApiImplicitParams({@ApiImplicitParam(name = "refreshToken", value = "刷新令牌", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/refreshUserToken", method = RequestMethod.GET)
    public CommonResponse refreshUserToken(@RequestParam("refreshToken") String refreshToken, HttpServletRequest request) {
        String terminalMark = request.getHeader("terminal");
        UserToken token = this.userBusiness.refreshUserToken(refreshToken, terminalMark);
        return ResultMsg.SUCCESS.info(token);
    }

    /**
     * 描述：发送验证码
     * @author nongchongwei
     * @date 2018/7/4 15:46
     * @param req email 账号
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "发送验证码", notes = "type 注册验证码:1 密码重置验证码:2 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/sendVerifyCode", method = {RequestMethod.POST,RequestMethod.GET})
    public CommonResponse sendVerifyCode(@RequestBody SendEmailReq req) {
        this.userBusiness.sendVerifyCode(req.getEmail(),req.getType());
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：发送密码重置验证码
     * @author nongchongwei
     * @date 2018/7/4 15:46
     * @param   req email 账号
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "发送密码重置验证码", notes = "发送密码重置验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/sendResetPwdVerifyCode", method = {RequestMethod.POST})
    public CommonResponse sendResetPasswordVerifyCode(@RequestBody SendEmailReq req) {
        this.userBusiness.sendResetPasswordVerifyCode(req.getEmail());
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：密码错误3次，发送验证码
     * @author nongchongwei
     * @date 2018/7/4 15:46
     * @param req email 账号
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "密码错误3次，发送验证码", notes = "密码错误3次，发送验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/sendPwdErrorVerifyCode", method = {RequestMethod.POST})
    public CommonResponse sendPasswordErrorVerifyCode(@RequestBody SendEmailReq req) {
        this.userBusiness.sendPasswordErrorVerifyCode(req.getEmail());
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：userName 账号
     * @author nongchongwei
     * @date 2018/7/4 15:47
     * @param
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "校验用户账号是否存在", notes = "校验用户账号是否存在,账号存在返回0 不存在返回1")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/checkUserName", method = RequestMethod.GET)
    public CommonResponse checkUserName(@RequestParam(value = "userName")  String userName) {
        return ResultMsg.SUCCESS.info(this.userBusiness.checkUserName(userName));
    }

    /**
     * 描述：完善企业信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param req
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "完善企业信息", notes = "完善企业信息")
    @RequestMapping(value = "/improveTenantInfo", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse improveTenantInfo(@RequestBody TenantUpdateReq req) {
        return ResultMsg.SUCCESS.info(this.userBusiness.improveTenantInfo(req));
    }

    /**
     *@description
     *@author wucheng
     *@params [req]
     *@create 2018/12/15 14:23
     *@return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "保存企业信息（不提前审核）", notes = "保存企业信息（不提前审核）")
    @RequestMapping(value = "/saveTenantInfo", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveTenantInfo(@RequestBody TenantUpdateReq req) {
        return ResultMsg.SUCCESS.info(this.userBusiness.saveTenantInfo(req));
    }

    /**
     * 描述：获取企业信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取企业信息", notes = "获取企业信息")
    @RequestMapping(value = "/getTenantInfo", method = {RequestMethod.POST})
    public CommonResponse getTenantInfo() {
        return ResultMsg.SUCCESS.info(this.userBusiness.getTenantInfo());
    }

    /**
     * 描述：获取图片验证码
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param userName 用户名
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation("获取图片验证码")
    @RequestMapping(value = "/getVerifyCodeImage", method = RequestMethod.GET)
    public String getVerifyCodeImage(@RequestParam("userName") String userName){
        return this.userBusiness.getVerifyCodeImage(userName);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("依据userId删除用户")
    @RequestMapping(value = "/deleteUserByUserId", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "Long")
    })
    public CommonResponse deleteUserByUserId(@RequestParam("userId") Long userId){
        this.userBusiness.deleteUserByUserId(userId);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：给单个用户分配角色
     * @author maochengyuan
     * @created 2018/7/14 11:19
     * @param roleIds 角色id
     * @param userId 用户id
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation("给单个用户分配角色")
    @RequestMapping(value = "/AssignRolesToSingleUser", method = RequestMethod.POST)
    public CommonResponse AssignRolesToSingleUser(@RequestParam("roleIds") List<Long> roleIds, @RequestParam("userId") Long userId){
        this.userBusiness.AssignRolesToSingleUser(roleIds, userId);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：初始化用户角色信息
     * @author maochengyuan
     * @created 2018/7/14 11:19
     * @param userId 用户id
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation("初始化用户角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/initUserRoleInfo", method = RequestMethod.GET)
    public CommonResponse initUserRoleInfo(@RequestParam("userId") Long userId){
        return ResultMsg.SUCCESS.info(this.userBusiness.initUserRoleInfo(userId));
    }

    /**
     * 描述：分页获取企业用户信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param searchReq 分页信息
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation("分页获取用户信息")
    @RequestMapping(value = "/getUserList",  method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserList(@RequestBody UserSearchReq searchReq){
        PageInfo<FetchUserResp> result = this.userBusiness.getUserPageList(searchReq);
        return ResultMsg.SUCCESS.info(result);
    }

    /**
     * 描述：分页获取企业用户信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param searchReq 分页信息
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation("分页获取企业用户信息")
    @RequestMapping(value = "/getCorpUserList",  method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCorpUserList(@RequestBody UserSearchReq searchReq){
        PageInfo<FetchUserResp> result = this.userBusiness.getCorpUserPageList(searchReq);
        return ResultMsg.SUCCESS.info(result);
    }

    @ApiOperation("获取租户审核记录")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getTenantReviewRecordByTenantId", method = RequestMethod.GET)
    CommonResponse<List<TenantReviewRecordInfoResp>> getTenantReviewRecordByTenantId(){
    	return new CommonResponse<>(ResultMsg.SUCCESS, this.userBusiness.getTenantReviewRecordByTenantId());
    }
    
    @ApiOperation("获取租户角色列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getPortalRole", method = RequestMethod.GET)
    public CommonResponse<List<RoleDto>> getPortalRole(){
    	return new CommonResponse<>(ResultMsg.SUCCESS, this.userBusiness.getPortalRole());
    }
}
