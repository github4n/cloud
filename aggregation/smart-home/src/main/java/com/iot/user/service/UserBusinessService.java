package com.iot.user.service;

import com.iot.common.beans.CommonResponse;
import com.iot.user.vo.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 项目名称：IOT视频云
 * 模块名称：聚合层
 * 功能描述：用户接口
 * 创建人：mao2080@sina.com
 * 创建时间：2018/3/20 17:27
 * 修改人：mao2080@sina.com
 * 修改时间：2018/3/20 17:27
 * 修改描述：
 */
public interface UserBusinessService {

    /**
     * 描述：用户注册
     *
     * @param user 用户对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 15:03
     */
    void userRegist(UserParamVO user);

    /**
     * 描述：用户登录
     *
     * @param ulpVO 用户登录对象
     * @return com.lds.iot.vo.user.UserTokenVO
     * @author mao2080@sina.com
     * @created 2018/4/8 15:04
     */
    UserTokenVO userLogin(UserLoginParamVO ulpVO);

    /**
     * 描述：用户退出
     *
     * @param terminalMark 终端类型（app|web）
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 14:45
     */
    void userLogout(String terminalMark);

    /**
     * 描述：查询用户详细信息
     *
     * @return com.lds.iot.vo.user.UserVO
     * @author mao2080@sina.com
     * @created 2018/4/8 14:46
     */
    UserVO getUserInfo();

    /**
     * 描述：修改用户详细信息
     *
     * @param user 用户对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 14:47
     */
    void editUserInfo(UserUpdateParamVO user);

    /**
     * 描述: 获取用户头像
     *
     * @return
     */
    String getUserHeadImg();

    /**
     * 描述：头像上传
     * 方式一：系统修改为系统，修改字段->返回系统头像
     * 方式二：系统修改为自定义，上传图片->修改字段->返回生成预签名
     * 方式三：自定义修改为系统，删除图片->修改字段->返回系统头像
     * 方式四：自定义修改为自定义，上传图片->修改字段->删除图片->返回生成预签名
     *
     * @param multipartRequest 文件参数
     * @param headImg          系统头像编号
     * @return java.lang.String
     * @author mao2080@sina.com
     * @created 2018/4/16 14:23
     */
    String uploadPhoto(MultipartHttpServletRequest multipartRequest, String headImg);

    /**
     * 描述：修改密码
     *
     * @param oldPwd 旧密码（经md5加密）
     * @param newPwd 新密码（经md5加密）
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 14:47
     */
    void changePassword(String oldPwd, String newPwd);

    /**
     * 描述：重置密码
     *
     * @param userName   email或手机号
     * @param verifyCode 验证码
     * @param newPwd     新密码（经md5加密）
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 15:01
     */
    void resetPassword(String userName, String verifyCode, String newPwd, Long tenantId);

    /**
     * 描述：创建子账户
     *
     * @param user 用户对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 15:03
     */
    void createSubUser(UserParamVO user);

    /**
     * 描述：刷新凭证
     *
     * @param refreshToken 刷新令牌
     * @param terminalMark 终端类型
     * @return com.lds.iot.vo.user.UserTokenVO
     * @author mao2080@sina.com
     * @created 2018/4/11 14:32
     */
    UserTokenVO refreshUserToken(String refreshToken, String terminalMark);

    /**
     * 描述：发送注册验证码
     *
     * @param email 账号
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 15:08
     */
    void sendRegistVerifyCode(String email, Long appId);

    /**
     * 描述：发送密码重置验证码
     *
     * @param email 账号
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 15:08
     */
    void sendResetPasswordVerifyCode(String email, Long appId);

    /**
     * 描述：密码错误3次，发送验证码
     *
     * @param email 账号
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/8 15:08
     */
    void sendPasswordErrorVerifyCode(String email, Long appId);

    /**
     * @param userName 用户账号
     * @return
     * @despriction：校验用户账号是否已注册
     * @author yeshiyuan
     * @created 2018/5/2 11:08
     */
    void checkUserName(String userName,Long tenantId);

    /**
     * 清除手机未读记录
     *
     * @param phoneId
     * @return  void
     * @author  yuChangXing
     * @created  2018/8/1 19:46
     */
    void clearUnReadRecord(String phoneId);

    CommonResponse verifyAccount(@RequestBody VerifyUserReq verifyUserReq);

    //用户注销
    CommonResponse deleteUser(Long userId,String userUuid,Long tenantId);
}
