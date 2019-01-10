package com.iot.user.service;

import com.alibaba.fastjson.JSON;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.MD5SaltUtil;
import com.iot.common.util.StringUtil;
import com.iot.common.util.ToolUtil;
import com.iot.common.util.ValidateUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.user.constant.UserConstants;
import com.iot.user.entity.User;
import com.iot.user.entity.UserToken;
import com.iot.user.enums.AdminStatusEnum;
import com.iot.user.enums.SystemTypeEnum;
import com.iot.user.enums.UserLevelEnum;
import com.iot.user.enums.UserStatusEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.util.BeanCopyUtil;
import com.iot.user.util.RedisKeyUtil;
import com.iot.user.util.UserTokenUtil;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginReq;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.RegisterReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/08/23 19:53
 **/
@Service
public class BossUserService extends BaseUserService{

    private final Logger logger = LoggerFactory.getLogger(CorpUserService.class);

    @Autowired
    private CorpUserService corpUserService;

    @Autowired
    private UserService userService;

    public LoginResp login(LoginReq req) {
        logger.debug("=== user service login Boss ===" + req.toString());
        String lockKey = RedisKeyUtil.getCorpLoginLockKey(req.getUserName());
        String lock = RedisCacheUtil.valueGet(lockKey);
        String loginFailNumKey = RedisKeyUtil.getCorpLoginFailNumKey(req.getUserName());
        Long errorCount = RedisCacheUtil.getIncr(loginFailNumKey);
        if(lock != null) {
            if (errorCount != null && errorCount != 0) {
                if(errorCount == 3) {
                    throw new BusinessException(UserExceptionEnum.LOCK_10_MINUTES);
                }else if(errorCount == 6) {
                    throw new BusinessException(UserExceptionEnum.LOCK_1_HOUR);
                }
            }else {
                throw new BusinessException(UserExceptionEnum.LOCK_24_HOURS);
            }
        }

        //根据用户名获取用户 判断用户是否存在
        User user = corpUserService.getCorpUserByNameCache(req.getUserName(), UserLevelEnum.BOSS.getCode());
        if (user == null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if (UserStatusEnum.UNREVIEWED.getCode().equals(user.getUserStatus())) {
            throw new BusinessException(UserExceptionEnum.ACCOUNT_UNAUDIT);
        }
        if (UserStatusEnum.AUDITFAILED.getCode().equals(user.getUserStatus())) {
            throw new BusinessException(UserExceptionEnum.ACCOUNT_AUDIT_FAILED);
        }
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(user.getTenantId());
        if (null == tenantInfoResp) {
            throw new BusinessException(UserExceptionEnum.TENANT_INFO_IS_NULL);
        }

        //验证密码
        if (null != req.getPwd() && MD5SaltUtil.verify(req.getPwd(), user.getPassword())) {//req.getPwd().equals(user.getPassword())
            Long userId = user.getId();
            logger.debug("user【" + userId + "】 login success.");
            //登录成功，删除登录失败记录缓存
            RedisCacheUtil.delete(loginFailNumKey);
            //token刷新方式与C端共用
            UserToken userToken = UserTokenUtil.loginRefreshToken(userId, req.getTerminalMark(), user.getTenantId(), user.getUuid(), SystemTypeEnum.USER_BOSS.getCode());
            //应答
            LoginResp res = BeanCopyUtil.getRespFromToken(userToken);
            res.setNickName(user.getNickname());
            res.setUserUuid(user.getUuid());
            res.setUserStatus(user.getUserStatus());
            req.setTenantId(user.getTenantId());
            processAfterLogin(userId, user.getUuid(), req);
            return res;
        } else {
            Long num = RedisCacheUtil.incr(loginFailNumKey, UserConstants.B_LOGIN_FAIL_EXPIRATION_TIME);
            if(num == 3) {
                RedisCacheUtil.valueSet(lockKey, "1", UserConstants.LOCK_10_MINUTES);
            }else if(num == 6) {
                RedisCacheUtil.valueSet(lockKey, "1", UserConstants.LOCK_1_HOUR);
            }else if(num == 12) {
                RedisCacheUtil.valueSet(lockKey, "1", UserConstants.LOCK_24_HOURS);
                RedisCacheUtil.delete(loginFailNumKey);
            }
            throw new BusinessException(UserExceptionEnum.PASSWORD_IS_ERROR);
        }
    }

    public Long register(RegisterReq registerReq) {
        if (null == registerReq){
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }
        if (StringUtil.isBlank(registerReq.getUserName())) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        ValidateUtil.validateEmail(registerReq.getUserName());
        if (StringUtil.isBlank(registerReq.getPassWord())) {
            throw new BusinessException(UserExceptionEnum.PWD_IS_NULL);
        }
        if (StringUtil.isBlank(registerReq.getVerifyCode())) {
            throw new BusinessException(UserExceptionEnum.VERIFYCODE_IS_NULL);
        }
        logger.debug("=== boss user service register ===" + registerReq.toString());
        String validateKey = RedisKeyUtil.getCorpRegisterCodeKey(registerReq.getUserName());
        String validateCode = RedisCacheUtil.valueGet(validateKey);
        if (StringUtil.isEmpty(validateCode)) {
            logger.error("redis register email verifyCode is null {}", registerReq.getUserName());
            throw new BusinessException(UserExceptionEnum.REGISTER_VERIFYCODE_ERROR);
        }
        if (!validateCode.equals(registerReq.getVerifyCode())) {
            throw new BusinessException(UserExceptionEnum.REGISTER_VERIFYCODE_ERROR);
        }
        User oldUser = corpUserService.getCorpUserByNameCache(registerReq.getUserName(), UserLevelEnum.BOSS.getCode());
        if (oldUser != null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_EXIST);
        }

        User user = new User();
        BeanUtils.copyProperties(registerReq, user);
        Date now = new Date();
        user.setPassword(MD5SaltUtil.generate(registerReq.getPassWord()));
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setEmail(registerReq.getUserName());
        user.setUserLevel(UserLevelEnum.BOSS.getCode());
        user.setAdminStatus(AdminStatusEnum.SUPER.getCode());
        user.setTenantId(SystemConstants.BOSS_TENANT);
        String uuid = ToolUtil.getUUID();
        user.setUuid(uuid);
        user.setState(UserConstants.USERSTATE_ACTIVE);
        //TODO 是否需要审核
        user.setUserStatus(UserStatusEnum.UNREVIEWED.getCode());
//        user.setUserStatus(UserStatusEnum.NORMAL.getCode());
        userMapper.insertSelective(user);

        RedisCacheUtil.delete(validateKey);
        return user.getId();
    }

    /**
     * @Description: boss用户审核
     *
     * @param registerReq
     * @return: void
     * @author: chq
     * @date: 2018/8/24 17:27
     **/
    public void auditUser(RegisterReq registerReq){
        logger.debug("=== boss auditUser req ===" + JSON.toJSONString(registerReq));
        if(registerReq == null || registerReq.getUserName() == null || registerReq.getAdminStatus() == null){
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }
        //1.查询用户是否已存在
        User user = corpUserService.getCorpUserByNameCache(registerReq.getUserName(), UserLevelEnum.BOSS.getCode());
        if(user == null){
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        //2.普通用户审核添加租户,管理用户审核无需租户隔离,状态更新为normal
        if(1 == registerReq.getAdminStatus()){
            user.setAdminStatus(AdminStatusEnum.SUPER.getCode());
        }else{
//            Long tenantId = null;
//            if(registerReq.getTenantId() == null) {
//                SaveTenantReq req = new SaveTenantReq();
//                String tenantUuid = ToolUtil.getUUID();
//                req.setCode(tenantUuid);
//                tenantId = tenantApi.save(req);
//            }else{
//                tenantId = registerReq.getTenantId();
//            }
//            user.setTenantId(tenantId);
            user.setAdminStatus(AdminStatusEnum.NORMAL.getCode());
        }
        user.setUserStatus(UserStatusEnum.NORMAL.getCode());

        userMapper.updateByPrimaryKeySelective(user);
        logger.debug("=== boss auditUser ===" + user.toString());
        //3.删除旧缓存
        delUserCache(user.getId());
    }

    public void checkUser(RegisterReq req) {
        logger.debug("=== boss checkUser ===" + JSON.toJSONString(req));
        if(req == null || req.getUserName().isEmpty()){
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        User user = corpUserService.getCorpUserByNameCache(req.getUserName(), req.getUserLevel());
        if(user != null){
            throw new BusinessException(UserExceptionEnum.USER_IS_EXIST);
        }
    }

    public void checkUserHadRight(Long userId){
        //校验用户是否有权限
        FetchUserResp user = userService.getUser(userId);
        if (user == null || UserLevelEnum.BOSS.getCode() != user.getUserLevel()) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if(AdminStatusEnum.SUPER.getCode() != user.getAdminStatus()){
            throw new BusinessException(UserExceptionEnum.USER_DO_NOT_HAVE_PERMISSION);
        }
    }
}
