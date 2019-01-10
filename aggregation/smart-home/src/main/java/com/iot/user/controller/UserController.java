package com.iot.user.controller;

import com.iot.BusinessExceptionEnum;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.constant.UserConstants;
import com.iot.user.service.UserBusinessService;
import com.iot.user.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // 旧版本app上报的 a023 tenantId
    private static final Long A023_TENANT_ID_OLD = 0L;
    // 新版本 a023 tenantId
    private static final Long A023_TENANT_ID_NEW = 1L;

    /**
     * 用户服务
     */
    @Autowired
    private UserBusinessService userBusiness;

    /**
     *  检查 tenantId
     *
     *  旧版本a023 app 上传的 tenantId 为null, 或者 为 0、1
     *  新版本a023 app 上传的 tenantId 为 1
     *
     *  arnoo 上传的tenantId 为2
     *
     * @param tenantId
     * @return
     */
    private Long checkTenantId(Long tenantId) {
        LOGGER.debug("***** checkTenantId, current tenantId={}", tenantId);

        if (tenantId == null || tenantId.compareTo(A023_TENANT_ID_OLD) == 0) {
            LOGGER.debug("***** compatible with old version a023 app");
            tenantId = A023_TENANT_ID_NEW;
        }

        LOGGER.debug("***** checkTenantId, return tenantId={}", tenantId);
        return tenantId;
    }

    /**
     * 描述：用户注册
     *
     * @param user 用户对象
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:03
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse userRegist(@RequestBody UserParamVO user) {
        // TODO 兼容旧版本app
        user.setTenantId(checkTenantId(user.getTenantId()));

        this.userBusiness.userRegist(user);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：用户登录
     *
     * @param ulpVO 用户登录对象
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:04
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse userLogin(@RequestBody UserLoginParamVO ulpVO) {
        // TODO 兼容旧版本app
        ulpVO.setTenantId(checkTenantId(ulpVO.getTenantId()));

        UserTokenVO token = this.userBusiness.userLogin(ulpVO);
        return ResultMsg.SUCCESS.info(token);
    }

    /**
     * 描述：用户退出
     *
     * @param request terminalMark 终端类型（app|web）
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 14:45
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
     *
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 14:46
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询用户详细信息", notes = "查询用户详细信息")
    @RequestMapping(value = "/getUser", method = {RequestMethod.POST})
    public CommonResponse getUserInfo() {
        return ResultMsg.SUCCESS.info(this.userBusiness.getUserInfo());
    }

    /**
     * 描述：修改用户详细信息
     *
     * @param user 用户对象
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 14:47
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "修改用户详细信息", notes = "修改用户详细信息")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse editUserInfo(@RequestBody UserUpdateParamVO user) {
        this.userBusiness.editUserInfo(user);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述: 获取用户头像
     *
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "getUserHeadImg", notes = "获取用户头像")
    @RequestMapping(value = "/getUserHeadImg", method = RequestMethod.GET)
    public CommonResponse getUserHeadImg() {
        return ResultMsg.SUCCESS.info( userBusiness.getUserHeadImg());
    }

    /**
     * 描述：上传头像
     * 方式一：系统修改为系统，修改字段->返回系统头像
     * 方式二：系统修改为自定义，上传图片->修改字段->返回生成预签名
     * 方式三：自定义修改为系统，删除图片->修改字段->返回系统头像
     * 方式四：自定义修改为自定义，上传图片->修改字段->删除图片->返回生成预签名
     *
     * @param multipartRequest 文件对象
     * @param headImg          系统头像编号
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/16 14:47
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
     *
     * @param req
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 14:47
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
     *
     * @param req
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:01
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "账号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "newPwd", value = "新密码（经md5加密）", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/resetPwd", method = {RequestMethod.POST})
    public CommonResponse resetPassword(@RequestBody ResetPasswordReq req) {
        if (!req.getNewPassWord().equals(req.getPassWord2())) {
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_IS_NOT_THE_SAME);
        }

        // TODO 兼容旧版本app
        req.setTenantId(checkTenantId(req.getTenantId()));

        this.userBusiness.resetPassword(req.getUserName(), req.getVerifyCode(), req.getNewPassWord(), req.getTenantId());
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：创建子账户
     *
     * @param user 用户对象
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:03
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "创建子账户", notes = "创建子账户")
    @RequestMapping(value = "/createSubUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createSubUser(@RequestBody UserParamVO user) {
        this.userBusiness.createSubUser(user);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：刷新凭证
     *
     * @param refreshToken 账号
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:08
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "刷新凭证", notes = "刷新凭证")
    @ApiImplicitParams({@ApiImplicitParam(name = "refreshToken", value = "刷新令牌", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/refreshUserToken", method = RequestMethod.GET)
    public CommonResponse refreshUserToken(@RequestParam("refreshToken") String refreshToken, HttpServletRequest request) {
        String terminalMark = request.getHeader("terminal");
        UserTokenVO token = this.userBusiness.refreshUserToken(refreshToken, terminalMark);
        return ResultMsg.SUCCESS.info(token);
    }

    /**
     * 描述：发送注册验证码
     *
     * @param req email 账号
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:08
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "发送注册验证码", notes = "发送注册验证码")
//    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/sendRegistVerifyCode", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse sendRegistVerifyCode(@RequestBody SendEmailReq req) {
        // TODO 兼容旧版本app
        req.setTenantId(checkTenantId(req.getTenantId()));

        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        if(req.getAppId() == null){
        	this.userBusiness.sendRegistVerifyCode(req.getEmail(), req.getTenantId());
        }else{
        	this.userBusiness.sendRegistVerifyCode(req.getEmail(), req.getAppId());
        }
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：发送密码重置验证码
     *
     * @param req email 账号
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:08
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "发送密码重置验证码", notes = "发送密码重置验证码")
//    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/sendResetPwdVerifyCode", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse sendResetPasswordVerifyCode(@RequestBody SendEmailReq req) {
        // TODO 兼容旧版本app
        req.setTenantId(checkTenantId(req.getTenantId()));

        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        if(req.getAppId() == null){
        	this.userBusiness.sendResetPasswordVerifyCode(req.getEmail(), req.getTenantId());
        }else{
        	this.userBusiness.sendResetPasswordVerifyCode(req.getEmail(), req.getAppId());
        }
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：密码错误3次，发送验证码
     *
     * @param req email 账号
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/4/8 15:08
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "密码错误3次，发送验证码", notes = "密码错误3次，发送验证码")
//    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/sendPwdErrorVerifyCode", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse sendPasswordErrorVerifyCode(@RequestBody SendEmailReq req) {
        // TODO 兼容旧版本app
        req.setTenantId(checkTenantId(req.getTenantId()));

        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        if(req.getAppId() == null){
        	this.userBusiness.sendPasswordErrorVerifyCode(req.getEmail(), req.getTenantId());
        }else{
        	this.userBusiness.sendPasswordErrorVerifyCode(req.getEmail(), req.getAppId());
        }
        return ResultMsg.SUCCESS.info();
    }

    /**
     * @param userName 账号
     * @return
     * @despriction：校验用户账号是否存在
     * @author yeshiyuan
     * @created 2018/5/2 11:17
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "校验用户账号是否存在", notes = "校验用户账号是否存在")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "账号", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/checkUserName/{userName:.+}/{tenantId}", method = RequestMethod.GET)
    public CommonResponse checkUserName(@PathVariable("userName") String userName, @PathVariable("tenantId")Long tenantId) {
        // TODO 兼容旧版本app
        Long tenantIdNew = checkTenantId(tenantId);

        this.userBusiness.checkUserName(userName, tenantIdNew);
        return ResultMsg.SUCCESS.info();
    }


    /**
     *  清除手机未读记录
     * @param phoneId
     * @author  yuChangXing
     * @created  2018/8/1 19:45
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value="清除手机未读记录", notes="清除手机未读记录")
    @ApiImplicitParam(name = "phoneId", value = "手机的phoneId", required = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "/clearUnReadRecord", method = { RequestMethod.GET })
    public CommonResponse clearUnReadRecord(@RequestParam("phoneId") String phoneId) {
        userBusiness.clearUnReadRecord(phoneId);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value="验证用户密码", notes="验证用户密码")
    @RequestMapping(value = "/verifyAccount", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse verifyAccount(@RequestBody VerifyUserReq verifyUserReq){
        String userUuid=SaaSContextHolder.getCurrentUserUuid();
        VerifyUserReq newVerifyUserReq=new VerifyUserReq();
        newVerifyUserReq.setUserUuid(userUuid);
        newVerifyUserReq.setPassWord(verifyUserReq.getPassWord());
        return userBusiness.verifyAccount(newVerifyUserReq);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value="用户注销", notes="用户注销")
    @RequestMapping(value = "/deleteUser", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteUser(){
        //通过用户id删除用户
        Long userId=SaaSContextHolder.getCurrentUserId();
        String userUuid=SaaSContextHolder.getCurrentUserUuid();
        Long tenantId =SaaSContextHolder.currentTenantId();
        LOGGER.info("deleteUser and the userId is {},userUuid is {} and the tenantId is {}",userId,userUuid,tenantId);
        return userBusiness.deleteUser(userId,userUuid,tenantId);
    }

}
