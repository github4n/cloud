package com.iot.boss.service.user.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.iot.boss.service.user.UserService;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.ValidateUtil;
import com.iot.user.api.UserApi;
import com.iot.user.enums.AdminStatusEnum;
import com.iot.user.enums.UserLevelEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginReq;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.RegisterReq;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserApi userApi;

    /**
     * 描述：添加子账户
     *
     * @param user
     * @author chq
     * @created 2018年8月21日 下午2:37:31
     * @since
     */
    @Override
    public void createSubUser(RegisterReq user) {
        logger.info("boss createSubUser {}", user.toString());
        //参数校验
        ValidateUtil.validateEmail(user.getUserName());
        if(user.getTel() != null) {
            ValidateUtil.isNumeric(user.getTel());
        }
        user.setUserLevel(UserLevelEnum.BOSS.getCode());
        //给用户一个默认头像
        user.setHeadImg("img_20");
        Long userId = this.userApi.addSubCorpUser(user);
        logger.info("boss userId {}",userId);
    }

    /**
     * @Description: 用户登录
     *
     * @param req
     * @return: com.iot.user.vo.LoginResp
     * @author: chq
     * @date: 2018/8/23 19:31
     **/
    @Override
    public LoginResp userLogin(LoginReq req) {
        ValidateUtil.validateEmail(req.getUserName());
        LoginResp loginResp = this.userApi.login2Boss(req);
        return loginResp;
    }

    /**
     * @Description: userRegister
     *
     * @param user
     * @return: 用户Id
     * @author: chq
     * @date: 2018/8/24 15:06
     **/
    @Override
    public Long userRegister(RegisterReq user) {

        // 参数校验
        ValidateUtil.validateEmail(user.getUserName());
        if(user.getTel() != null) {
            ValidateUtil.isNumeric(user.getTel());
        }
        // 给用户一个默认头像
        user.setHeadImg("img_20");
        return this.userApi.register2Boss(user);
    }

    @Override
    public void sendVerifyCode(String email, Byte type) {
        String language = LocaleContextHolder.getLocale().toString();
        this.userApi.sendCorpVerifyCode(email, type, language);
    }

    @Override
    public void checkUserName(RegisterReq req) {
        if(req != null && req.getEmail() != null){
            req.setUserName(req.getUserName() == null ? req.getEmail() : req.getUserName());
        }
        req.setUserLevel(UserLevelEnum.BOSS.getCode());
        userApi.checkUser(req);

    }

    public void auditUser(RegisterReq registerReq){
        userApi.auditUser(registerReq);
    }

    public void checkUserHadRight(Long userId){
        //校验用户是否有权限
        FetchUserResp user = userApi.getUser(userId);
        if (user == null || UserLevelEnum.BOSS.getCode() != user.getUserLevel()) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if(AdminStatusEnum.SUPER.getCode() != user.getAdminStatus()){
            throw new BusinessException(UserExceptionEnum.USER_DO_NOT_HAVE_PERMISSION);
        }
    }
}
