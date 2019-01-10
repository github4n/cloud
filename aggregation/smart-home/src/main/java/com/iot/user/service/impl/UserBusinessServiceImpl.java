package com.iot.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.IBusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.StringUtil;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.dto.FileDto;
import com.iot.file.vo.FileInfoResp;
import com.iot.message.api.MessageApi;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.tenant.api.AppApi;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.enums.SystemTypeEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.service.UserBusinessService;
import com.iot.user.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：用户接口实现
 * 创建人：mao2080@sina.com
 * 创建时间：2018/3/20 17:27
 * 修改人：mao2080@sina.com
 * 修改时间：2018/3/20 17:27
 * 修改描述：
 */
@Service("userBusiness")
public class UserBusinessServiceImpl implements UserBusinessService {

    /**
     * 日志
     */
    private static final Log LOGGER = LogFactory.getLog(UserBusinessServiceImpl.class);

    /**
     * 用户服务
     */
    @Autowired
    private UserApi userApi;

    /**
     * 文件服务
     */
    @Autowired
    private FileApi fileApi;

    @Autowired
    private MessageApi messageApi;

    @Autowired
    private FileUploadApi fileUploadApi;

    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;

    @Autowired
    private AppApi appApi;

    //接受的图片类型
    @Value("${upload.img.str}")
    private String uploadImgStr;
    /**
     * 描述：用户注册
     *
     * @param user 用户对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 15:03
     */
    @Override
    public void userRegist(UserParamVO user) {
        RegisterReq o = new RegisterReq();
        BeanUtils.copyProperties(user, o);
        //给用户一个默认头像
        o.setHeadImg("img_20");
        this.userApi.register(o);
    }

    /**
     * 描述：用户登录
     *
     * @param ulpVO 用户登录对象
     * @return com.lds.iot.vo.user.UserTokenVO
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public UserTokenVO userLogin(UserLoginParamVO ulpVO) {
        String terminalMark = ulpVO.getTerminalMark();
        if (UserConstants.TERMINALMARK_APP.equals(terminalMark.toLowerCase())) {
            if (ulpVO.getAppId() == null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "app id is null");
            }
            //校验app是否已被禁用
            if (!appApi.checkAppCanUsed(ulpVO.getAppId(), ulpVO.getTenantId())) {
                throw new BusinessException(BusinessExceptionEnum.CHECK_NO_PASS, "this app is forbidden used");
            }
        }
        LoginReq loginReq = new LoginReq();
        loginReq.setUserName(ulpVO.getUserName());
        loginReq.setPwd(StringUtil.isNotEmpty(ulpVO.getPassWord()) ? ulpVO.getPassWord() : ulpVO.getPwd());
        loginReq.setVerifyCode(ulpVO.getVerifyCode());
        loginReq.setLastIp(ulpVO.getLastIp());
        loginReq.setLastLoginTime(ulpVO.getLastLoginTime());
        loginReq.setOs(ulpVO.getOs());
        loginReq.setTerminalMark(ulpVO.getTerminalMark());
        loginReq.setPhoneId(ulpVO.getPhoneId());
        loginReq.setAppId(ulpVO.getAppId());
        loginReq.setTenantId(ulpVO.getTenantId());
        LoginResp loginResp = this.userApi.login(loginReq);
        UserTokenVO userToken = new UserTokenVO();
        userToken.setUserUuId(loginResp.getUserUuid());
        userToken.setAccessToken(loginResp.getAccessToken());
        userToken.setRefreshToken(loginResp.getRefreshToken());
        userToken.setNickName(loginResp.getNickName());
        userToken.setMqttPassword(loginResp.getMqttPassword());
        userToken.setExpireIn(loginResp.getExpireIn());
        return userToken;
    }

    /**
     * 描述：用户退出
     *
     * @param terminalMark 终端类型（app|web）
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void userLogout(String terminalMark) {
        this.userApi.logout(SaaSContextHolder.getCurrentUserId(), terminalMark);
        //如果是app登录的话要删掉对应的用户与手机关系，避免退出还推送信息
        if (UserConstants.TERMINALMARK_APP.equalsIgnoreCase(terminalMark)) {
            messageApi.deleteUserPhoneId(SaaSContextHolder.getCurrentUserUuid());
        }
    }

    /**
     * 描述：查询用户详细信息
     *
     * @return com.lds.iot.vo.user.UserVO
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public UserVO getUserInfo() {
        FetchUserResp user = this.userApi.getUser(SaaSContextHolder.getCurrentUserId());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 描述：修改用户详细信息
     *
     * @param userVO 用户对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void editUserInfo(UserUpdateParamVO userVO) {
        userVO.setId(SaaSContextHolder.getCurrentUserId());
        UpdateUserReq user = new UpdateUserReq();
        BeanUtils.copyProperties(userVO, user);
        this.userApi.updateUser(user);
    }

    /**
     * 描述: 获取用户头像
     *
     * @return
     */
    @Override
    public String getUserHeadImg() {
        String fileId = this.userApi.getUserHeadImg(SaaSContextHolder.getCurrentUserId());
        if (fileId != null && !fileId.contains("img_")) {
            FileDto fileDto = fileApi.getGetUrl(fileId);
            return fileDto.getPresignedUrl();
        } else {
            return fileId;
        }
    }

    /**
     * 描述：头像上传
     * 方式一：系统修改为系统，修改字段->返回系统头像
     * 方式二：系统修改为自定义，上传图片->修改字段->返回生成预签名
     * 方式三：自定义修改为系统，修改字段->删除之前图片->返回系统头像
     * 方式四：自定义修改为自定义，上传图片->修改字段->删除之前图片->返回生成预签名
     *
     * @param multipartRequest 文件参数
     * @param newHeadImg       系统头像编号
     * @return java.lang.String
     * @author mao2080@sina.com
     * @created 2018/4/16 14:23
     */
    @Override
    public String uploadPhoto(MultipartHttpServletRequest multipartRequest, String newHeadImg) {
        if ((multipartRequest != null && !multipartRequest.getFileNames().hasNext()) || (StringUtil.isEmpty(newHeadImg) && multipartRequest == null)) {
            throw new BusinessException(BusinessExceptionEnum.USER_PHOTO_IS_EMPTY);
        }
        //判断图片类型
        MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        String ImgSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        String[] result = uploadImgStr.split(",");
        Boolean isOk = Arrays.asList(result).contains(ImgSuffix);
        if(isOk){
            String oldHeadImg = this.userApi.getUserHeadImg(SaaSContextHolder.getCurrentUserId());
            if (StringUtil.isEmpty(newHeadImg)) {
                return chargeAndUpdateOldHeadImg(multipartRequest, oldHeadImg);
            } else {
                this.userApi.setUserHeadImg(SaaSContextHolder.getCurrentUserId(), newHeadImg);
                if (!oldHeadImg.contains("img_")) {   //不是系统头像才去删除
                    this.deleteOldHeadImg(oldHeadImg);
                }
                return newHeadImg;
            }
        }else {
            LOGGER.error("wrong type of Img");
            throw new BusinessException(com.iot.BusinessExceptionEnum.WRONG_TYPE_OF_IMG);
        }
    }

    private String chargeAndUpdateOldHeadImg(MultipartHttpServletRequest multipartRequest, String oldHeadImg) {
        try {
            MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
            //上传文件
            FileInfoResp fileInfoResp = this.fileUploadApi.upLoadFileAndGetUrl(multipartFile, SaaSContextHolder.currentTenantId());
            this.userApi.setUserHeadImg(SaaSContextHolder.getCurrentUserId(), fileInfoResp.getFileId());
            if (!oldHeadImg.contains("img_")) {   //不是系统头像才去删除
                this.deleteOldHeadImg(oldHeadImg);
            }
            return fileInfoResp.getUrl();
        } catch (Exception e) {
            LOGGER.error(e);
            throw new BusinessException(BusinessExceptionEnum.USER_PHOTO_UPLOAD_FAILED, e);
        }
    }

    /**
     * 描述：删除旧头像
     *
     * @param oldHeadImg 旧头像
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/16 17:09
     */
    private void deleteOldHeadImg(String oldHeadImg) {
        try {
            if (StringUtil.isNotBlank(oldHeadImg) && oldHeadImg.length() > 2) {
                this.fileApi.deleteObject(oldHeadImg);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    /**
     * 描述：修改密码
     *
     * @param oldPwd 旧密码（经md5加密）
     * @param newPwd 新密码（经md5加密）
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void changePassword(String oldPwd, String newPwd) {
        ModifyPwdReq o = new ModifyPwdReq();
        o.setOldPwd(oldPwd);
        o.setNewPwd(newPwd);
        o.setUserId(SaaSContextHolder.getCurrentUserId());
        this.userApi.modifyPwd(o);
    }

    /**
     * 描述：重置密码
     *
     * @param userName   email或手机号
     * @param verifyCode 验证码
     * @param newPwd     新密码（经md5加密）
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void resetPassword(String userName, String verifyCode, String newPwd, Long tenantId) {
        ResetPwdReq o = new ResetPwdReq();
        o.setUserName(userName);
        o.setNewPwd(newPwd);
        o.setVerifyCode(verifyCode);
        o.setTenantId(tenantId);
        this.userApi.resetPwd(o);
    }

    /**
     * 描述：创建子账户
     *
     * @param user 用户对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void createSubUser(UserParamVO user) {

    }

    /**
     * 描述：刷新凭证
     *
     * @param refreshToken 刷新令牌
     * @param terminalMark 终端类型
     * @return com.lds.iot.vo.user.UserTokenVO
     * @author mao2080@sina.com
     * @created 2018/4/11 14:32
     */
    @Override
    public UserTokenVO refreshUserToken(String refreshToken, String terminalMark) {
        UserTokenResp userToken = this.userApi.refreshUserToken(refreshToken, terminalMark, SystemTypeEnum.USER_2C.getCode());
        UserTokenVO userTokenVO = new UserTokenVO();
        BeanUtils.copyProperties(userToken, userTokenVO);
        return userTokenVO;
    }

    /**
     * 描述：发送注册验证码
     *
     * @param email 账号
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void sendRegistVerifyCode(String email, Long appId) {
    	String language = LocaleContextHolder.getLocale().toString();
        this.userApi.sendVerifyCode(email, (byte) 1, language, appId);
    }

    /**
     * 描述：发送密码重置验证码
     *
     * @param email 账号
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void sendResetPasswordVerifyCode(String email, Long appId) {
    	String language = LocaleContextHolder.getLocale().toString();
        this.userApi.sendVerifyCode(email, (byte) 2, language, appId);
    }

    /**
     * 描述：密码错误3次，发送验证码
     *
     * @param email 账号
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 16:30
     */
    @Override
    public void sendPasswordErrorVerifyCode(String email, Long appId) {
    	String language = LocaleContextHolder.getLocale().toString();
        this.userApi.sendVerifyCode(email, (byte) 3, language, appId);
    }

    @Override
    public void checkUserName(String userName,Long tenantId) {
        userApi.checkUserName(userName,tenantId);
    }

    /**
     * 清除手机未读记录
     *
     * @param phoneId
     * @return  void
     * @author  yuChangXing
     * @created  2018/8/1 19:46
     */
    @Override
    public void clearUnReadRecord(String phoneId) {
        if (StringUtil.isBlank(phoneId)) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 400;
                }
                @Override
                public String getMessageKey() {
                    return "phoneId.notnull";
                }
            });
        }
        messageApi.clearUnReadRecord(phoneId);
    }

    @Override
    public CommonResponse verifyAccount(VerifyUserReq verifyUserReq) {
        boolean flag=userApi.verifyAccount(verifyUserReq);
        return CommonResponse.success(flag);
    }
    //删除用户
    @Override
    public CommonResponse deleteUser(Long userId,String userUuid,Long tenantId) {

        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        if(StringUtil.isEmpty(userUuid)){
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if(tenantId==null){
            throw new BusinessException(UserExceptionEnum.TENANTID_IS_NULL);
        }
        //通过用户id 看看是否有直连设备存在

        deviceCoreServiceApi.cancelUser(userId,userUuid,tenantId);
        userApi.cancelUser(userId,userUuid,tenantId);
        return CommonResponse.success();
    }
}
