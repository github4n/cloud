package com.iot.portal.corpuser.service;

import com.github.pagehelper.PageInfo;
import com.iot.common.beans.SearchParam;
import com.iot.permission.vo.RoleDto;
import com.iot.portal.corpuser.vo.*;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.UserSearchReq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;


/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户接口
 * 创建人： nongchongwei
 * 创建时间：2018年07月04日 10:09
 * 修改人： nongchongwei
 * 修改时间：2018年07月04日 10:09
 */
public interface UserBusinessService {

    /**
     * 描述：企业用户注册
     * @author nongchongwei
     * @date 2018/7/2 20:28
     * @param
     * @return
     */
    Long userRegister(UserRegisterReq user);


    /**
     * 描述：企业用户登录
     * @author nongchongwei
     * @date 2018/7/2 20:28
     * @param
     * @return
     */
    UserLoginResp userLogin(UserLoginReq ulpVO);

    /**
     * 描述：完善企业信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param
     * @return
     */
    Long improveTenantInfo(TenantUpdateReq req);
    
    /**
     *@description 保存企业信息
     *@author wucheng
     *@params [req]
     *@create 2018/12/15 14:21
     *@return java.lang.Long
     */
    Long saveTenantInfo(TenantUpdateReq req);

    /**
     * 描述：获取企业信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param
     * @return
     */
    TenantResp getTenantInfo();

    /**
     * 描述：用户退出
     * @author nongchongwei
     * @date 2018/7/4 15:58
     * @param  terminalMark 终端类型（app|web）
     * @return
     */
    void userLogout(String terminalMark);

    /**
     * 描述：查询用户详细信息
     * @author nongchongwei
     * @date 2018/7/4 15:58
     * @param
     * @return
     */
    UserResp getUserInfo();

    /**
     * 描述：修改用户详细信息
     * @author nongchongwei
     * @date 2018/7/4 15:59
     * @param user 用户对象
     * @return
     */
    void editUserInfo(UserUpdateReq user);


    /**
     * 描述：头像上传
     * @author nongchongwei
     * @date 2018/7/4 15:59
     * @param multipartRequest 文件参数
     * @param headImg          系统头像编号
     * @return
     */
    String uploadPhoto(MultipartHttpServletRequest multipartRequest, String headImg);

    /**
     * 描述：修改密码
     * @author nongchongwei
     * @date 2018/7/4 15:59
     * @param oldPwd 旧密码（经md5加密）
     * @param newPwd 新密码（经md5加密）
     * @return
     */
    void changePassword(String oldPwd, String newPwd);

    /**
     * 描述：重置密码
     * @author nongchongwei
     * @date 2018/7/4 15:59
     * @param userName   email或手机号
     * @param verifyCode 验证码
     * @param newPwd     新密码（经md5加密）
     * @return
     */
    void resetPassword(String userName, String verifyCode, String newPwd);

    /**
     * 描述：创建子账户
     * @author nongchongwei
     * @date 2018/7/4 15:59
     * @param  user 用户对象
     * @return
     */
    void createSubUser(UserRegisterReq user);
    /**
     * 描述：查询子账号
     * @author nongchongwei
     * @date 2018/11/5 16:02
     * @param
     * @return
     */
    PageInfo<FetchUserResp> querySubUserList(SearchParam searchParam);

    /**
     * 描述：刷新凭证
     * @author nongchongwei
     * @date 2018/7/4 15:59
     * @param refreshToken 刷新令牌
     * @param terminalMark 终端类型
     * @return
     */
    UserToken refreshUserToken(String refreshToken, String terminalMark);

    /**
     * 描述：发送注册验证码
     * @author nongchongwei
     * @date 2018/7/4 15:59
     * @param email 账号
     * @return
     */
    void sendVerifyCode(String email,Byte type);

    /**
     * 描述：发送密码重置验证码
     * @author nongchongwei
     * @date 2018/7/4 16:00
     * @param email 账号
     * @return
     */
    void sendResetPasswordVerifyCode(String email);

    /**
     * 描述：密码错误3次，发送验证码
     * @author nongchongwei
     * @date 2018/7/4 16:00
     * @param email 账号
     * @return
     */
    void sendPasswordErrorVerifyCode(String email);

    /**
     * 描述：校验用户账号是否已注册
     * @author nongchongwei
     * @date 2018/7/4 16:00
     * @param userName 用户账号
     * @return
     */
    Integer checkUserName(String userName);


    /**
     * 描述：获取图片验证码
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param userName 用户账号
     * @return
     */
    String getVerifyCodeImage(String userName);

    /**
     * 描述：依据userId删除用户
     * @author maochengyuan
     * @created 2018/7/13 18:24
     * @param userId 用户id
     * @return int
     */
    int deleteUserByUserId(Long userId);

    /**
     * 描述：给单个用户分配角色
     * @author maochengyuan
     * @created 2018/7/14 11:23
     * @param roleIds 角色id集合
     * @param userId 用户id
     * @return void
     */
    void AssignRolesToSingleUser(List<Long> roleIds, Long userId);

    /**
     * 描述：初始化用户角色信息
     * @author maochengyuan
     * @created 2018/7/14 11:23
     * @param userId 用户id
     * @return void
     */
    RoleInitResp initUserRoleInfo(Long userId);

    /**
     * 描述：分页查询用户信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param searchReq 用户账号
     * @return
     */
    PageInfo<FetchUserResp> getUserPageList(@RequestBody UserSearchReq searchReq);

    /**
     * 描述：分页查询企业用户信息
     * @author nongchongwei
     * @date 2018/7/2 20:31
     * @param searchReq 用户账号
     * @return
     */
    PageInfo<FetchUserResp> getCorpUserPageList(@RequestBody UserSearchReq searchReq);

    List<TenantReviewRecordInfoResp> getTenantReviewRecordByTenantId();
    
    /**
     * 
     * 描述：获取租户角色列表
     * @author 李帅
     * @created 2018年11月5日 下午3:57:40
     * @since 
     * @return
     */
    List<RoleDto> getPortalRole();

    /**
     * 描述：删除子账户
     * @author wucheng
     * @date 2018-11-06 11:15:13
     * @param userId 用户id
     * @return
     */
    int deleteSubUser(Long userId);

    /**
     * 描述：修改子账户信息
     * @author wucheng
     * @param userId
     * @param roleIds 角色id,多个角色使用,隔开
     * @return
     */
    int editSubUser(String roleIds, Long userId);

    /**
     * 描述：修改子账户密码或者当前账号密码
     * @param userId 当前子账户id或者当前账户id
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @return
     */
    int updatePasswordByUserId(Long userId, String newPassword, String oldPassword);
}
