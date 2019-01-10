package com.iot.user.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.*;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.vo.req.AddUserOrgReq;
import com.iot.tenant.vo.req.AddUserReq;
import com.iot.tenant.vo.req.AddVirtualOrgReq;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.constant.UserConstants;
import com.iot.user.entity.User;
import com.iot.user.entity.UserLog;
import com.iot.user.entity.UserToken;
import com.iot.user.enums.SystemTypeEnum;
import com.iot.user.enums.UserLevelEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.util.BeanCopyUtil;
import com.iot.user.util.OAuthIssuer;
import com.iot.user.util.RedisKeyUtil;
import com.iot.user.util.UserTokenUtil;
import com.iot.user.vo.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：用户业务逻辑类
 * 创建人： laiguiming
 * 创建时间： 2018年4月09日
 */
@Service
public class UserService extends BaseUserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    /**
     * 用户注册
     *
     * @param req
     * @return
     */
    public Long register(RegisterReq req) {
        logger.debug("=== user service register ===" + req.toString());
        //参数验证
        if (StringUtil.isEmpty(req.getUserName())) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        if (StringUtil.isEmpty(req.getVerifyCode())) {
            throw new BusinessException(UserExceptionEnum.VERIFYCODE_IS_NULL);
        }
        Long tenantId = req.getTenantId();
        SaaSContextHolder.setCurrentTenantId(tenantId);
        String email = req.getEmail();
        //若邮箱信息为空，则从userName中取
        if (StringUtil.isEmpty(email)) {
            email = req.getUserName();
            ValidateUtil.validateEmail(email);
        }
        //验证码校验
        String key = RedisKeyUtil.getRegisterCodeKey(email);
        String code = RedisCacheUtil.valueGet(key);
        if (StringUtil.isEmpty(code)) {
            throw new BusinessException(UserExceptionEnum.REDIS_VERIFYCODE_IS_NULL);
        }
        //验证码判断
        if (!code.equals(req.getVerifyCode())) {
            checkVerifyCodeErrorNumb(email,key);
            throw new BusinessException(UserExceptionEnum.REGISTER_VERIFYCODE_ERROR);
        }
        //判断用户是否存在
        User oldUser = getUserByNameCache(req.getUserName());
        if (oldUser != null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_EXIST);
        }
        //添加用户
        User user = BeanCopyUtil.getUserFromRegister(req);
        Date now = new Date();
        user.setTenantId(tenantId);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        String mqttPassWord = ToolUtil.getUUID().substring(0, 7);
        user.setMqttPassword(mqttPassWord);
        //添加mqtt策略
        String uuid = ToolUtil.getUUID();
        mqttPloyApi.saveAcls("user", uuid, uuid, DigestUtils.sha256Hex(mqttPassWord));
        user.setUuid(uuid);
        user.setState(UserConstants.USERSTATE_ACTIVE);
        user.setUserLevel(UserLevelEnum.CONSUMER.getCode());
        userMapper.insertSelective(user);
        //添加组织 + 用户对应的组织
        addOrg(user, tenantId);
        //注册成功，删除验证码
        RedisCacheUtil.delete(key);
        //插入接受条款数据
        UserLog userLog=new UserLog();
        userLog.setTenantId(tenantId);
        userLog.setUserName(email);
        userLog.setUuid(uuid);
        userLog.setAccept(1);
        userLog.setCancel(0);
        userLog.setAcceptTime(new Date());
        userLog.setCancelTime(new Date());
        logger.info("***********************registerregister "+userLog.toString());
        int result = userLogMapper.inserUserLog(userLog);
        // 将用户激活信息缓存，用于报表统计
        if (result > 0) {
            String redisKey = UserConstants.USER_ACTIVATED + CalendarUtil.format(new Date(), CalendarUtil.YYYYMMDD); // 组和缓存的key
            UserActivated activatedUser = new UserActivated(uuid, CalendarUtil.getFormatTime(new Date()).getTime(), tenantId);
            RedisCacheUtil.listPush(redisKey, activatedUser, false);
        }
        return user.getId();
    }

    //检测验证码输错的次数，当输错3次之后缓存中的验证码将被删除，需要重新发送验证码
    private void checkVerifyCodeErrorNumb(String email,String key){
        String verifyCodeErrorNumbKey=RedisKeyUtil.getVerifyCodeErrorNumbKey(email);
        Long count = RedisCacheUtil.incr(verifyCodeErrorNumbKey, UserConstants.EXPIRATION_TIME);
        if(count>3){
            RedisCacheUtil.delete(key);
            throw new BusinessException(UserExceptionEnum.REDIS_VERIFYCODE_IS_NULL);
        }
    }

    /**
     * 用户添加组织
     *
     * @param user
     * @param tenantId
     */
    private void addOrg(User user, Long tenantId) {
        AddUserOrgReq addUserOrgReq = new AddUserOrgReq();
        AddUserReq userReq = new AddUserReq();
        userReq.setUserId(user.getId());
        addUserOrgReq.setUserReq(userReq);
        AddVirtualOrgReq orgReq = new AddVirtualOrgReq();
        orgReq.setTenantId(tenantId);
        String name = RandomUtils.nextInt(10) + "";
        orgReq.setName(name);
        addUserOrgReq.setOrgReq(orgReq);
        virtualOrgApi.addUserOrg(addUserOrgReq);
    }

    /**
     * 用户登录
     *
     * @param req
     * @return
     */
    public LoginResp login(LoginReq req) {
        logger.debug("=== user service login 2C ===" + req.toString());
        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
//        //登录失败校验
//        checkVerifyCode(req.getUserName(), req.getVerifyCode());
        //检测错误锁定时间
        checkLockTime(req.getUserName());
        String loginFailNumKey = RedisKeyUtil.getLoginFailNumKey(req.getUserName());
        String loginFailLockedTimeKey=RedisKeyUtil.getLoginFailLockedTime(req.getUserName());
        //设置tenantId
        //SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        //根据用户名获取用户
        User user = getUserByNameCache(req.getUserName());
        if (user == null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }

        //校验密码
        if (MD5SaltUtil.verify(req.getPwd(), user.getPassword())) {//req.getPwd().equals(user.getPassword())
            Long userId = user.getId();
            logger.debug("user【" + userId + "】 login success.");
            //登录成功，删除登录失败记录缓存
            RedisCacheUtil.delete(loginFailNumKey);
            //刷新token缓存
            UserToken userToken = UserTokenUtil.loginRefreshToken(userId, req.getTerminalMark(), user.getTenantId(), user.getUuid(), SystemTypeEnum.USER_2C.getCode(), req.getAppId());
            //应答封装
            LoginResp res = BeanCopyUtil.getRespFromToken(userToken);
            res.setNickName(user.getNickname());
            res.setLocationId(user.getLocationId());
            //mqtt密码生成
            String mqttPassWord = SecurityUtil.generateToken();
            res.setMqttPassword(mqttPassWord);
            mqttPloyApi.changePassword("app", user.getUuid(), DigestUtils.sha256Hex(mqttPassWord));
            //登录后异步操作
            processAfterLogin(userId, user.getUuid(), req);
            return res;
        } else {
            Long count = RedisCacheUtil.incr(loginFailNumKey, UserConstants.LOGIN_FAIL_EXPIRATION_TIME);
            if (count == 3 && StringUtil.isEmpty(req.getVerifyCode())) {
                //原先3次登录错误则发送验证码
//                throw new BusinessException(UserExceptionEnum.PASSWORD_NEED_VERIFY);
                Long lockedTime=60L;
                RedisCacheUtil.setNx(loginFailLockedTimeKey,"1min",lockedTime);
                throw new BusinessException(UserExceptionEnum.ACCOUNT_IS_LOCKED_1MIN);
            }else if (count>3 && StringUtil.isEmpty(req.getVerifyCode())){
                //错误次数大于3次每次锁定3分钟
                Long lockedTime=60*3L;
                RedisCacheUtil.setNx(loginFailLockedTimeKey,"3min",lockedTime);
                throw new BusinessException(UserExceptionEnum.ACCOUNT_IS_LOCKED_3MIN);
            }
            throw new BusinessException(UserExceptionEnum.PASSWORD_IS_ERROR);
        }
    }

    /**
     * 检测错误次数后是否到达锁定时间
     *
     * @param userName
     */
    protected void checkLockTime(String userName) {

        Long errorCount = RedisCacheUtil.getIncr(RedisKeyUtil.getLoginFailNumKey(userName));
        //登录失败3次，将锁定1分钟，检测是否到达锁定1min中的时间
        String loginFailLockedTime=RedisKeyUtil.getLoginFailLockedTime(userName);
        boolean locked=RedisCacheUtil.hasKey(loginFailLockedTime);
        if (errorCount != null && errorCount == 3&&locked) {
                throw new BusinessException(UserExceptionEnum.ACCOUNT_IS_LOCKED_1MIN);
        }else if(errorCount!=null && errorCount>3&&locked){
                throw new BusinessException(UserExceptionEnum.ACCOUNT_IS_LOCKED_3MIN);
        }

    }
    public Long registerMock(RegisterReq req) {
        logger.debug("=== user service register ===" + req.toString());
        //参数验证
        if (StringUtil.isEmpty(req.getUserName())) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
//        if (StringUtil.isEmpty(req.getVerifyCode())) {
//            throw new BusinessException(UserExceptionEnum.VERIFYCODE_IS_NULL);
//        }
        Long tenantId = getTenantId();
        String email = req.getEmail();
        //若邮箱信息为空，则从userName中取
        if (StringUtil.isEmpty(email)) {
            email = req.getUserName();
            ValidateUtil.validateEmail(email);
        }
        //验证码校验
//        String key = RedisKeyUtil.getRegisterCodeKey(email);
//        String code = RedisCacheUtil.valueGet(key);
//        if (StringUtil.isEmpty(code)) {
//            throw new BusinessException(UserExceptionEnum.REDIS_VERIFYCODE_IS_NULL);
//        }
        //验证码判断
//        if (!code.equals(req.getVerifyCode())) {
//            throw new BusinessException(UserExceptionEnum.REGISTER_VERIFYCODE_ERROR);
//        }
        //判断用户是否存在
        User oldUser = getUserByNameCache(req.getUserName());
        if (oldUser != null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_EXIST);
        }
        //添加用户
        User user = BeanCopyUtil.getUserFromRegister(req);
        Date now = new Date();
        user.setTenantId(tenantId);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        String mqttPassWord = "123456";// ToolUtil.getUUID().substring(0, 7);
        user.setMqttPassword(mqttPassWord);
        //添加mqtt策略
        String uuid = ToolUtil.getUUID();
        mqttPloyApi.saveAcls("user", uuid, uuid, DigestUtils.sha256Hex(mqttPassWord));
        user.setUuid(uuid);
        user.setState(UserConstants.USERSTATE_ACTIVE);
        user.setUserLevel(UserLevelEnum.CONSUMER.getCode());
        userMapper.insertSelective(user);
        //添加组织 + 用户对应的组织
        addOrg(user, tenantId);
        //注册成功，删除验证码
//        RedisCacheUtil.delete(key);
        return user.getId();
    }


    /**
     * 校验登录验证码
     *
     * @param userName
     * @param verifyCode
     */
    protected void checkVerifyCode(String userName, String verifyCode) {
        //登录失败3次，验证码判断
        Long errorCount = RedisCacheUtil.getIncr(RedisKeyUtil.getLoginFailNumKey(userName));
        if (errorCount != null && errorCount >= 3) {
            String redisCode = RedisCacheUtil.valueGet(RedisKeyUtil.getLoginFailCodeKey(userName));
            if (verifyCode == null) {
                throw new BusinessException(UserExceptionEnum.VERIFYCODE_IS_NULL);
            }
            if (redisCode == null) {
                throw new BusinessException(UserExceptionEnum.REDIS_VERIFYCODE_IS_NULL);
            }
            if (!verifyCode.equals(redisCode)) {
                throw new BusinessException(UserExceptionEnum.LOGIN_FAIL_VERIFYCODE_ERROR);
            }
        }
    }



    /**
     * 获取用户信息根据用户名
     *
     * @param userName
     * @return
     */
    public FetchUserResp getUserByUserName(String userName) {
        logger.debug("=== user service getUserByUserName ===" + userName);
        if (StringUtil.isEmpty(userName)) {
            throw new BusinessException(UserExceptionEnum.UUID_IS_NULL);
        }
        //从缓存中获取
        User user = getUserByNameCache(userName);
        if (user != null) {
            return BeanCopyUtil.getRespFromUser(user);
        } else {
            return null;
        }
    }

    /**
     * 重置密码(忘记密码)
     *
     * @param req
     */
    public void resetPwd(ResetPwdReq req) {
        logger.debug("=== user service resetPwd ===" + req.toString());
        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        if (StringUtil.isEmpty(req.getUserName())) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        if (StringUtil.isEmpty(req.getVerifyCode())) {
            throw new BusinessException(UserExceptionEnum.VERIFYCODE_IS_NULL);
        }
        //根据用户名取对象
        User user = getUserByNameCache(req.getUserName());
        if (CommonUtil.isEmpty(user)) {
            throw new BusinessException(UserExceptionEnum.USER_NOT_REGISTER);
        }
        String email = user.getEmail();
        //若邮箱信息为空，则从userName中取
        if (StringUtil.isEmpty(email)) {
            email = user.getUserName();
            ValidateUtil.validateEmail(email);
        }
        //验证码校验
        String key = RedisKeyUtil.getResetPwdCodeKey(email);

        String code = RedisCacheUtil.valueGet(key);
        logger.info("mykey----:{},mycode--{}",key,code);
        
        if (StringUtil.isEmpty(code)) {
            throw new BusinessException(UserExceptionEnum.REDIS_VERIFYCODE_IS_NULL);
        }
        //验证码判断
        if (!code.equals(req.getVerifyCode())) {
            checkVerifyCodeErrorNumb(email,key);
            throw new BusinessException(UserExceptionEnum.RESET_PWD_VERIFYCODE_ERROR);
        }
        // 更新密码
        User entity = new User();
        entity.setPassword(MD5SaltUtil.generate(req.getNewPwd()));//req.getNewPwd()
        entity.setId(user.getId());
        entity.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(entity);
        //删除缓存
        delUserCache(user.getId());
        //删除用户名-user缓存
        RedisCacheUtil.delete(RedisKeyUtil.getUserNameKey(req.getUserName()));
        //删除验证码缓存
        RedisCacheUtil.delete(key);
    }

    /**
     * 检查用户名
     *
     * @param userName
     */
    public void checkUserName(String userName, Long tenantId) {
        logger.debug("=== user service checkUserName ===" + userName);
        SaaSContextHolder.setCurrentTenantId(tenantId);
        if (StringUtil.isEmpty(userName)) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        //判断用户是否存在
        if (userMapper.countByNameAndTenant(userName, getTenantId(), UserLevelEnum.CONSUMER.getCode()) > 0) {
            throw new BusinessException(UserExceptionEnum.USER_IS_EXIST);
        }
        //判断邮箱是否合法
        ValidateUtil.validateEmail(userName);
    }

    public LoginResp login2Robot(LoginRobotReq req) {
        logger.info("***** login2Robot, tenantId={}, req.jsonString={}：", getTenantId(), JSON.toJSON(req));
        //判断用户是否存在
        User user = getUserByNameCache(req.getUserName());
        if (user != null && MD5SaltUtil.verify(req.getPwd(), user.getPassword())) {
            // 是否保存 smartToken
            boolean saveSmartToken = false;
            if(req.getClient() != null &&
                    req.getClient() == SmartHomeConstants.GOOGLE_HOME){
                saveSmartToken = true;
            } else if (req.getThirdPartyInfoId() != null) {
                saveSmartToken = true;
            }

            Long userId = user.getId();
            logger.info("***** login2Robot, userId={} is login, saveSmartToken={}", userId, saveSmartToken);
            if (saveSmartToken) {
                try {
                    // 生成smart_token记录
                    SmartTokenReq smartTokenReq = new SmartTokenReq();
                    smartTokenReq.setUserId(user.getId());
                    smartTokenReq.setSmart(req.getClient());
                    smartTokenReq.setThirdPartyInfoId(req.getThirdPartyInfoId());
                    smartTokenReq.setUpdateTime(new Date());
                    smartTokenReq.setExpiresIn(999999999);
                    smartTokenService.merge(smartTokenReq);
                    logger.info("***** login2Robot, save SmartToken, smartType={}, thirdPartyInfoId={}", req.getClient(), req.getThirdPartyInfoId());
                } catch (Exception e) {
                    logger.info("***** login2Robot, save SmartToken error.");
                    e.printStackTrace();
                }
            }

            LoginResp res = new LoginResp();
            res.setUserId(user.getId());
            res.setUserUuid(user.getUuid());
            res.setNickName(user.getNickname());
            res.setLocationId(user.getLocationId());
            res.setTenantId(user.getTenantId());
            return res;
        } else {
            throw new BusinessException(UserExceptionEnum.LOGIN_FAIL);
        }
    }

    /**
     *  根据code 生成 token
     * @param type
     * @param code
     * @return
     */
    public UserTokenResp createOauthTokenByCode(String type, String code) {
        OAuthIssuer oau = new OAuthIssuer();
        String key = RedisKeyUtil.getOauthCodeKey(code);
        String userUUID = RedisCacheUtil.valueGet(key);
        if (userUUID == null) {
            return null;
        }

        // 删除缓存里 旧的 code
        RedisCacheUtil.delete(key);

        String accessToken = oau.accessToken();
        String refreshToken = oau.refreshToken();
        RedisCacheUtil.valueSet(RedisKeyUtil.getOauthAccessTokenKey(type, accessToken), userUUID, 7200L);
        RedisCacheUtil.valueSet(RedisKeyUtil.getOauthRefreshTokenKey(type, refreshToken), userUUID);

        UserTokenResp res = new UserTokenResp();
        res.setAccessToken(accessToken);
        res.setRefreshToken(refreshToken);
        res.setExpireIn(7200L);
        res.setUserId(getUserId(userUUID));
        return res;
    }

    /**
     *  根据 refreshToken 生成 token
     * @param type
     * @param token     refreshToken
     * @return
     */
    public UserTokenResp createOauthTokenByRefreshToken(String type, String token) {
        OAuthIssuer oau = new OAuthIssuer();
        String key = RedisKeyUtil.getOauthRefreshTokenKey(type, token);
        String userUUID = RedisCacheUtil.valueGet(key);
        if (userUUID == null) {
            return null;
        }

        logger.debug("***** createOauthTokenByRefreshToken, userUUID={}, type={}, token={}", userUUID, type, token);

        // 删除缓存里 旧的refreshToken
        RedisCacheUtil.delete(key);

        String accessToken = oau.accessToken();
        String refreshToken = oau.refreshToken();
        RedisCacheUtil.valueSet(RedisKeyUtil.getOauthAccessTokenKey(type, accessToken), userUUID, 7200L);
        RedisCacheUtil.valueSet(RedisKeyUtil.getOauthRefreshTokenKey(type, refreshToken), userUUID);

        UserTokenResp res = new UserTokenResp();
        res.setAccessToken(accessToken);
        res.setRefreshToken(refreshToken);
        res.setExpireIn(7200L);
        res.setUserId(getUserId(userUUID));
        return res;
    }

    public FetchUserResp getUserByCondition(LoginReq user) {
        return userMapper.getUserByCondition(user, UserLevelEnum.CONSUMER.getCode());
    }

    public FetchUserResp getUserByUserNameTenantId(String userName, Long tenantId) {
        User user = userMapper.getByUserNameAndTenantId(userName, tenantId, UserLevelEnum.CONSUMER.getCode());
        return BeanCopyUtil.getRespFromUser(user);
    }

    /**
     * 
     * 描述：获取租户信息根据租户ID
     * @author 李帅
     * @created 2018年11月13日 下午7:02:58
     * @since 
     * @param tenantIds
     * @return
     */
    public List<FetchUserResp> getAdminUserByTenantId(List<Long> tenantIds) {
    	if(tenantIds == null || tenantIds.size() == 0) {
    		throw new BusinessException(UserExceptionEnum.TENANTID_IS_NULL);
    	}
        List<User> users = userMapper.getAdminUserByTenantId(tenantIds, UserLevelEnum.BUSINESS.getCode());
        if(users == null) {
        	return null;
        }
        List<FetchUserResp> fetchUserResps = new ArrayList<FetchUserResp>();
        for(User user : users) {
        	fetchUserResps.add(BeanCopyUtil.getRespFromUser(user));
        }
        return fetchUserResps;
    }
    
    /**
     * 
     * 描述：获取租户主账号信息根据用户名称
     * @author 李帅
     * @created 2018年11月13日 下午7:02:31
     * @since 
     * @param userName
     * @return
     */
    public FetchUserResp getAdminUserByUserName(String userName) {
    	if (StringUtil.isEmpty(userName)) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        User user = userMapper.getAdminUserByUserName(userName, UserLevelEnum.BUSINESS.getCode());
        if(user == null) {
        	return null;
        }
        return BeanCopyUtil.getRespFromUser(user);
    }



    /**
     * 根据用户名称获取 2C
     *
     * @param userName
     * @return
     */
    private User getUserByNameCache(String userName) {
        String key = RedisKeyUtil.getUserNameKey(userName);
        Long userId = RedisCacheUtil.valueObjGet(key, Long.class);
        if (userId == null) {
            //同一个租户下，C端账号可能跟管理员账号相同，加level区分
            User user = userMapper.getByUserNameAndTenantId(userName, getTenantId(), UserLevelEnum.CONSUMER.getCode());
            if (CommonUtil.isEmpty(user)) {
                return null;
            } else {
                userId = user.getId();
                RedisCacheUtil.valueObjSet(key, userId, 86400L);
            }
        }
        return getUserByIdCache(userId);
    }

    public Page<UserResp> getUseList(UserSearchReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<UserResp> userList = userMapper.queryList(req);
        PageInfo<UserResp> info = new PageInfo(userList);
        Page page = new Page();
        BeanUtil.copyProperties(info, page);
        page.setResult(info.getList());
        return page;
    }

    public Long addUser(UserReq req) {
        User user = new User();
        BeanUtil.copyProperties(req, user);
        // 设置密码
        user.setPassword(MD5SaltUtil.generate(req.getPassword()));
        // 设置用户状态（0-未激活，1-已激活，2-在线，3-离线，4-已冻结，5-已注销）
        user.setState(Byte.valueOf("1"));
        userMapper.insert(user);
        return user.getId();
    }

    public void editUser(UserReq req) {
        User user = userMapper.selectByPrimaryKey(req.getId());
        user.setNickname(req.getNickname());
        user.setUpdateTime(req.getUpdateTime());
        userMapper.updateByPrimaryKey(user);
    }

    //删除用户
    public void deleteUser(Long id) {
        if (id == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        //找到用户名
        User user = userMapper.selectByPrimaryKey(id);
        if(CommonUtil.isEmpty(user)){
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        String userName=user.getUserName();
        //删除缓存
        String key = RedisKeyUtil.getUserNameKey(userName);
        //删除关联信息
        RedisCacheUtil.delete(key);

        String redisKey = RedisKeyUtil.getUserKey(id);
        RedisCacheUtil.delete(redisKey);

        //删除数据库中的用户表数据
        userMapper.deleteByPrimaryKey(id);
    }
    //注销用户
    public void cancelUser(Long userId, String userUuid, Long tenantId) {
        logger.info("***********************cancelUser");
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        if(StringUtil.isEmpty(userUuid)){
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if(tenantId==null){
            throw new BusinessException(UserExceptionEnum.TENANTID_IS_NULL);
        }

        //找到用户名
        User user = userMapper.selectByPrimaryKey(userId);
        if(CommonUtil.isEmpty(user)){
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        String userName=user.getUserName();
        userMapper.deleteByPrimaryKey(userId);
        //删除缓存
        String key = RedisKeyUtil.getUserNameKey(userName);
        //删除关联信息
        RedisCacheUtil.delete(key);
        //记录用户注销的时间
        UserLog userLog=userLogMapper.selectByUuidAndTenatId(userUuid,tenantId);
        //兼容以前注册的用户
        if(userLog==null){
            UserLog log=new UserLog();
            log.setUuid(userUuid);
            log.setTenantId(tenantId);
            log.setAccept(1);
            log.setUserName(userName);
            log.setAcceptTime(user.getCreateTime());
            log.setCancel(1);
            log.setCancelTime(new Date());
            logger.info("***********************cancelUser"+log.toString());
            userLogMapper.inserUserLog(log);
        }else {
            userLog.setCancel(1);
            userLog.setCancelTime(new Date());
            logger.info("***********************cancelUser"+userLog.toString());
            userLogMapper.updateUserLog(userLog);
        }
    }

    //验证用户密码
    public boolean verifyAccount(VerifyUserReq verifyUserReq){
        if(verifyUserReq==null ){
            return false;
        }

        String userUuid=verifyUserReq.getUserUuid();
        if(StringUtil.isEmpty(userUuid)){
            return false;
        }
        FetchUserResp user=getUserByUuid(userUuid);

        if(user!=null&&MD5SaltUtil.verify(verifyUserReq.getPassWord(), user.getPassword())){
            return true;
        }
        return false;
    }

    /**
     * 获取用户信息根据用户名租户---TOB使用
     * @param userName
     * @param tenantId
     * @return
     */
    public FetchUserResp getUserByUserNameTenantIdTOB(String userName, Long tenantId) {
        User user = userMapper.getByUserNameAndTenantId(userName, tenantId, UserLevelEnum.BUSINESS.getCode());
        return BeanCopyUtil.getRespFromUser(user);
    }
    /**
     * 描述：修改用户密码或修改子账户密码
     * @author wucheng
     * @param userId
     * @param password
     * @return
     */
    public int updatePasswordByUserId(Long userId, String password) {
        int result = userMapper.updatePasswordByUserId(userId, password);
        RedisCacheUtil.deleteBlurry(RedisKeyUtil.getUserKey(userId));
        return result;
    }
    /**
     *@description 根据用户名称和层级获取用户信息
     *@author wucheng
     *@params [userName]
     *@create 2018/11/29 11:21
     *@return com.iot.user.vo.FetchUserResp
     */
    public FetchUserResp getBusinessUserByUserName(String userName) {
        if (StringUtil.isEmpty(userName)) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        User user = userMapper.getBusinessUserByUserName(userName, UserLevelEnum.BUSINESS.getCode());
        if(user == null) {
            return null;
        }
        return BeanCopyUtil.getRespFromUser(user);
    }

    public FetchUserResp getUserByName(Long tenantId, String userName) {
        if (StringUtil.isEmpty(userName)) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        User user = userMapper.getUserByUserName(tenantId, userName);
        if (user == null) {
            return null;
        }
        return BeanCopyUtil.getRespFromUser(user);
    }

    /**
     *@description 获取App用户注册总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 13:57
     *@return java.lang.Long
     */
    public Long getAppUserCount(Long tenantId) {
        return userMapper.getAppUserCount(tenantId);
    }

}