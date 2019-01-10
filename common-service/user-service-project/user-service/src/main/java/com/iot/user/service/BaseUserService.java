package com.iot.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.MD5SaltUtil;
import com.iot.common.util.StringUtil;
import com.iot.message.api.MessageApi;
import com.iot.message.entity.UserPhoneRelate;
import com.iot.message.enums.MessageTempType;
import com.iot.mqttploy.api.MqttPloyApi;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.api.VirtualOrgApi;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.user.constant.UserConstants;
import com.iot.user.entity.User;
import com.iot.user.entity.UserLogin;
import com.iot.user.entity.UserToken;
import com.iot.user.enums.UserStatusEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.mapper.UserLogMapper;
import com.iot.user.mapper.UserLoginMapper;
import com.iot.user.mapper.UserMapper;
import com.iot.user.rabbit.bean.UserLoginAfterMessage;
import com.iot.user.rabbit.sender.UserLoginAfterSender;
import com.iot.user.util.BeanCopyUtil;
import com.iot.user.util.RedisKeyUtil;
import com.iot.user.util.UserTokenUtil;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginReq;
import com.iot.user.vo.ModifyPwdReq;
import com.iot.user.vo.UpdateUserReq;
import com.iot.user.vo.UserSearchReq;
import com.iot.user.vo.UserTokenResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class BaseUserService {

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private final Logger logger = LoggerFactory.getLogger(BaseUserService.class);

    private final Long BOSS_APP = -1L;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected UserLoginMapper userLoginMapper;

    @Autowired
    protected MessageApi messageApi;

    @Autowired
    protected MqttPloyApi mqttPloyApi;

    @Autowired
    protected VirtualOrgApi virtualOrgApi;

    @Autowired
    protected UserVirtualOrgApi userVirtualOrgApi;

    @Autowired
    protected TenantApi tenantApi;

    @Autowired
    protected SmartTokenService smartTokenService;

    @Autowired
    protected UserLogMapper userLogMapper;

    @Autowired
    protected UserLoginAfterSender userLoginAfterSender;


    /**
     * 获取租户主键
     *
     * @return
     */
    protected Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }


    /**
     * 登录异步操作，更新登录信息
     *
     * @param userId
     * @param uuid
     * @param req
     */
    protected void processAfterLogin(Long userId, String uuid, LoginReq req) {
        //这些操作做异步处理，加快响应速度
        /*executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    SaaSContextHolder.setCurrentTenantId(req.getTenantId());
                    //更新用户
                    User user = new User();
                    user.setId(userId);
                    user.setUpdateTime(new Date());
                    user.setState(UserConstants.USERSTATE_ONLINE); //在线状态
                    userMapper.updateByPrimaryKeySelective(user);
                    //删除用户缓存
                    delUserCache(userId);

                    //取出最新登录信息
                    UserLogin login = getUserLogin(userId, req.getTenantId());
                    updateUserLogin(login, req, userId);
                    //如果是手机移动端登录，需要调用消息服务记录用户与手机phoneId的关系
                    if (isApp(req.getTerminalMark())) {
                        UserPhoneRelate userPhoneRelate = new UserPhoneRelate(uuid, req.getPhoneId(), req.getOs(), uuid, req.getTenantId(), req.getAppId());
                        messageApi.addUserPhoneRelate(userPhoneRelate);
                    }
                    //删除登录失败验证码
                    RedisCacheUtil.delete(RedisKeyUtil.getLoginFailCodeKey(req.getUserName()));
                }catch (Exception e){
                    logger.error("更新登录信息失败",e);
                } finally {
                    SaaSContextHolder.currentContextMap();
                }
            }
        });*/
        userLoginAfterSender.send(UserLoginAfterMessage.builder()
                .userId(userId)
                .uuid(uuid)
                .tenantId(req.getTenantId())
                .userName(req.getUserName())
                .lastIp(req.getLastIp())
                .os(req.getOs())
                .phoneId(req.getPhoneId())
                .appId(req.getAppId())
                .terminalMark(req.getTerminalMark())
                .build());
    }

    /**
     * 是否app登录
     *
     * @param terminalMark
     * @return
     */
    public Boolean isApp(String terminalMark) {
        if (UserConstants.TERMINALMARK_APP.equalsIgnoreCase(terminalMark)) {
            return true;
        }
        return false;
    }

    /**
     * 更新登录信息
     *
     * @param userLogin
     * @param req
     * @param userId
     */
    public void updateUserLogin(UserLogin userLogin, LoginReq req, Long userId) {
        Date now = new Date();
        UserLogin login = userLogin;
        if (login == null) { //新增
            login = new UserLogin();
            login.setUserId(userId);
            login.setLastLoginTime(now);
            login.setLastIp(req.getLastIp());
            login.setOs(req.getOs());
            login.setPhoneId(req.getPhoneId());
            login.setTenantId(req.getTenantId());
            userLoginMapper.insertSelective(login);
        } else { //修改
            login.setLastLoginTime(now);
            login.setLastIp(req.getLastIp());
            login.setOs(req.getOs());
            login.setPhoneId(req.getPhoneId());
            userLoginMapper.updateByPrimaryKeySelective(login);
            // 把login缓存 更新至最新
            RedisCacheUtil.valueObjSet(RedisKeyUtil.getUserLoginKey(userId), userLogin, RedisKeyUtil.DEFAULT_CACHE_TIME);
        }
    }

    /**
     * 退出登录
     *
     * @param userId
     */
    public void logout(Long userId, String terminalMark) {
        logger.debug("=== user service logout ===" + userId + "/" + terminalMark);
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        //更新用户状态
        User user = new User();
        user.setId(userId);
        user.setState(UserConstants.USERSTATE_OFFLINE);
        userMapper.updateByPrimaryKeySelective(user);
        //删除token缓存
        UserTokenUtil.deleteToken(userId, terminalMark);
        //删除用户缓存
        delUserCache(userId);
    }

    /**
     * 获取用户登录信息
     *
     * @param userId
     * @return
     */
    public UserLogin getUserLogin(Long userId, Long tenantId) {
        String userLoginKey = RedisKeyUtil.getUserLoginKey(userId);
        UserLogin userLogin = RedisCacheUtil.valueObjGet(userLoginKey, UserLogin.class);
        if (CommonUtil.isEmpty(userLogin)) {
            userLogin = userLoginMapper.selectByUserId(userId, tenantId);
            if (!CommonUtil.isEmpty(userLogin)) {
                RedisCacheUtil.valueObjSet(userLoginKey, userLogin, RedisKeyUtil.DEFAULT_CACHE_TIME);
            }
        }
        return userLogin;
    }

    /**
     * 修改用户信息
     *
     * @param req
     * @return
     */
    public void updateUser(UpdateUserReq req) {
        logger.debug("=== user service updateUser ===" + req.toString());
        Long userId = req.getId();
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        User user = BeanCopyUtil.getUserFromUpdate(req);
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        //删除用户缓存
        delUserCache(userId);
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public FetchUserResp getUser(Long userId) {
        logger.debug("=== user service getUser ===" + userId);
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        //从缓存中获取
        User user = getUserByIdCache(userId);
        if (user != null) {
            return BeanCopyUtil.getRespFromUser(user);
        } else {
            return null;
        }
    }

    /**
     * 获取用户信息根据uuid
     *
     * @param uuid
     * @return
     */
    public FetchUserResp getUserByUuid(String uuid) {
        logger.debug("=== user service getUserByUuid ===" + uuid);
        if (StringUtil.isEmpty(uuid)) {
            throw new BusinessException(UserExceptionEnum.UUID_IS_NULL);
        }
        //从缓存中获取
        User user = getUserByUuidCache(uuid);
        if (user != null) {
            return BeanCopyUtil.getRespFromUser(user);
        } else {
            return null;
        }
    }

    /**
     * 获取uuid
     *
     * @return
     */
    public Long getUserId(String uuid) {
        return getUserByUuid(uuid).getId();
    }

    /**
     * 获取userId
     *
     * @param userId
     * @return
     */
    public String getUuid(Long userId) {
        return getUser(userId).getUuid();
    }

    /**
     * 修改密码
     *
     * @param req
     */
    public void modifyPwd(ModifyPwdReq req) {
        logger.debug("=== user service modifyPwd ===" + req.toString());
        if (req.getUserId() == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        User user = getUserByIdCache(req.getUserId());
        if (CommonUtil.isEmpty(user)) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        // 判断密码
        if (!MD5SaltUtil.verify(req.getOldPwd(), user.getPassword())) {//user.getPassword().equals(req.getOldPwd())
            throw new BusinessException(UserExceptionEnum.OLD_PASSWORD_IS_ERROR);
        }
        // 更新密码
        User entity = new User();
        entity.setPassword(MD5SaltUtil.generate(req.getNewPwd()));//req.getNewPwd()
        entity.setId(req.getUserId());
        entity.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(entity);
        //删除用户缓存
        delUserCache(user.getId());
    }


    /**
     * 获取凭证
     *
     * @param accessToken
     * @return
     */
    public UserTokenResp getUserToken(String accessToken) {
        logger.debug("=== user service getUserToken ===" + accessToken);
        String key = RedisKeyUtil.getAccessTokenKey(accessToken);
        logger.info("getUserToken -->key:{}", key);
        UserToken userToken = RedisCacheUtil.valueObjGet(key, UserToken.class);
        if (StringUtils.isEmpty(userToken)) {
            throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
        }
        return BeanCopyUtil.getTokenRespFromToken(userToken);
    }

    /**
     * 检测凭证
     *
     * @param userId
     * @param accessToken
     * @param terminalMark
     * @return
     */
    public UserTokenResp checkUserToken(Long userId, String accessToken, String terminalMark) {
        logger.debug("=== user service checkUserToken ===" + accessToken + "/" + terminalMark);
//        if (userId == null) {
//            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
//        }
        String terminal = terminalMark;
        if (StringUtil.isEmpty(terminalMark)) {
            terminal = UserConstants.TERMINALMARK_APP;
        }
        //检测凭证
        UserToken userToken = UserTokenUtil.checkToken(userId, accessToken, terminal);
        return BeanCopyUtil.getTokenRespFromToken(userToken);
    }

    /**
     * 刷新凭证
     *
     * @param refreshToken
     * @param terminalMark
     * @return
     */
    public UserTokenResp refreshUserToken(String refreshToken, String terminalMark, String systemType) {
        logger.debug("=== user service refreshUserToken ===/" + refreshToken + "/" + terminalMark);
        String terminal = terminalMark;
        if (StringUtil.isEmpty(terminalMark)) {
            terminal = UserConstants.TERMINALMARK_APP;
        }
        UserToken userToken = UserTokenUtil.refreshToken(refreshToken, terminal, systemType);
        return BeanCopyUtil.getTokenRespFromToken(userToken);
    }

    /**
     * 发送验证码
     *
     * @param email
     * @param type  1=注册 2=重置密码 3=登录失败
     */
    public void sendVerifyCode(String email, Byte type, String langage, Long appId) {
        logger.debug("=== user service sendVerifyCode ===" + email + "/" + type);
        //验证码生成规则
        String code = CommonUtil.getRandNum(1000, 9999).toString();
        Map<String, String> noticeMap = Maps.newHashMap();
        MessageTempType tempType = null;
        //验证码缓存Redis
        tempType = MessageTempType.EN00001;
        if (type == 1) {
            noticeMap.put("activateCode", code);
            RedisCacheUtil.valueSet(RedisKeyUtil.getRegisterCodeKey(email), code, UserConstants.EXPIRATION_TIME);
        } else if (type == 2) {
            noticeMap.put("activateCode", code);
            RedisCacheUtil.valueSet(RedisKeyUtil.getResetPwdCodeKey(email), code, UserConstants.EXPIRATION_TIME);
        } else if (type == 3) {
            noticeMap.put("activateCode", code);
            RedisCacheUtil.valueSet(RedisKeyUtil.getLoginFailCodeKey(email), code, UserConstants.EXPIRATION_TIME);
        } else {
            throw new BusinessException(UserExceptionEnum.VERIFYCODE_TYPE_ERROR);
        }
        logger.info("发送验证码，email:"+email+"type:"+type+",verifyCode:"+code);

        //时间限制
        String mark = RedisCacheUtil.valueGet("Emial_limit:" + email + "_" + type);
        if(mark != null) {
        	throw new BusinessException(UserExceptionEnum.MAIL_TOO_FREQUENT);
        }
        //发送邮件
        String to = email;
        noticeMap.put("subject", "Account verification code:"+code);
        noticeMap.put("templateId", tempType.getTempId());

        //异步处理
//        Long tenantId = SaaSContextHolder.currentTenantId();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
            	if(appId == null) {
            		messageApi.mailSinglePush(BOSS_APP, to, noticeMap, 1, langage);
                }else {
                	messageApi.mailSinglePush(appId, to, noticeMap, 1, langage);
                }

            }
        });
        //发送新的注册码的时候需要，需要删除掉限制缓存
        String verifyCodeErrorNumbKey=RedisKeyUtil.getVerifyCodeErrorNumbKey(email);
        RedisCacheUtil.delete(verifyCodeErrorNumbKey);
        RedisCacheUtil.valueSet("Emial_limit:" + email + "_" + type, "1", 60L);
    }
    /**
     * 描述：获取用户头像
     *
     * @param userId 用户id
     * @return java.lang.String
     * @author mao2080@sina.com
     * @created 2018/4/18 10:51
     */
    public String getUserHeadImg(Long userId) {
        logger.debug("=== user service getUserHeadImg ===" + userId);
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        //从缓存中获取
        User user = getUserByIdCache(userId);
        if (user != null) {
            return user.getHeadImg();
        } else {
            return null;
        }
    }

    /**
     * 描述：修改用户头像
     *
     * @param userId  用户id
     * @param headImg 用户头像fileId或编号
     * @return void
     * @author mao2080@sina.com
     * @created 2018/4/18 10:52
     */
    public void setUserHeadImg(Long userId, String headImg) {
        logger.debug("=== user service setUserHeadImg ===" + userId + "/" + headImg);
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        if (StringUtil.isEmpty(headImg)) {
            throw new BusinessException(UserExceptionEnum.USER_HEAD_IMG_IS_NULL);
        }
        User entity = new User();
        entity.setId(userId);
        entity.setHeadImg(headImg);
        userMapper.updateByPrimaryKeySelective(entity);
        //删除用户缓存
        delUserCache(userId);
    }

    /**
     * 获取背景图片
     *
     * @param userId
     * @return
     */
    public String getBackground(Long userId) {
        logger.debug("=== user service getBackground ===" + userId);
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        //从缓存中获取
        User user = getUserByIdCache(userId);
        if (user != null) {
            return user.getBackground();
        } else {
            return null;
        }
    }

    /**
     * 修改背景图片
     *
     * @param userId
     * @param bg
     */
    public void setBackground(Long userId, String bg) {
        logger.debug("=== user service setBackground ===" + userId + "/" + bg);
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        if (StringUtil.isEmpty(bg)) {
            throw new BusinessException(UserExceptionEnum.USER_BACKGROUND_IS_NULL);
        }
        User entity = new User();
        entity.setId(userId);
        entity.setBackground(bg);
        userMapper.updateByPrimaryKeySelective(entity);

        //删除用户缓存
        delUserCache(userId);
    }

    public PageInfo<FetchUserResp> getUserPageList(UserSearchReq searchReq) {
        if(null  == searchReq.getTenantId() || null == searchReq.getUserLevel()){
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }

        Integer pageNum = searchReq.getPageNum();
        Integer pageSize = searchReq.getPageSize();

        if (null == pageNum) {
            pageNum = 0;
        }
        if (null == pageSize || 0 == pageSize) {
            pageSize=10;
        }

        PageHelper.startPage(pageNum,pageSize,true);
        List<User> userList = userMapper.getByTenantIdAndLevel(searchReq.getTenantId(),searchReq.getUserLevel());
        PageInfo pageInfo = new PageInfo(userList,10);
        List<FetchUserResp> fetchUserRespList = new ArrayList<>();
        FetchUserResp fetchUserResp = null;
        for(User user : userList){
            fetchUserResp = new FetchUserResp();
            BeanUtils.copyProperties(user, fetchUserResp);
            fetchUserResp.setPassword("");
            fetchUserResp.setUserStatus("");
            fetchUserResp.setUuid("");
            fetchUserRespList.add(fetchUserResp);
        }
        pageInfo.setList(fetchUserRespList);
        return pageInfo;
    }

    /**
     * 描述： 查uuid
     * @author 490485964@qq.com
     * @date 2018/8/7 15:33
     * @param
     * @return
     */
    public Map<Long,String> getBathUuid(List<Long> userIdList) {
        logger.debug("=== user service getByUserIds ===" + userIdList);
        if (null == userIdList || userIdList.size() == 0) {
            return null;
        }
        if(userIdList.size()>1000){
            throw new BusinessException(UserExceptionEnum.PARAM_SIZE_IS_TOO_LARGE);
        }
        Map<Long,String> userIdMap = new HashMap<>();
        List<User> userList = userMapper.getBathUuid(userIdList);
        if(null != userList && userList.size()>0){
            for(User user : userList){
                userIdMap.put(user.getId(),user.getUuid());
            }
        }
        return userIdMap;
    }


    /**
     * 获取注册验证码
     *
     * @param email
     * @return
     */
    public String getRegisterCode(String email) {
        return RedisCacheUtil.valueGet(RedisKeyUtil.getRegisterCodeKey(email));
    }

    /****************************************** Redis function start **************************************************/
    /**
     * 根据用户主键获取
     *
     * @param userId
     * @return
     */
    protected User getUserByIdCache(Long userId) {
        //从缓存中获取
        String key = RedisKeyUtil.getUserKey(userId);
        User user = RedisCacheUtil.valueObjGet(key, User.class);
        //缓存没有值，则从数据库中获取
        if (user == null) {
            user = userMapper.selectByPrimaryKey(userId);
            if (user == null) {
                return null;
            } else {
                UserDefaultOrgInfoResp userDefaultOrgInfoResp = userVirtualOrgApi.getDefaultUsedOrgInfoByUserId(user.getId());
                user.setOrgId(userDefaultOrgInfoResp.getId());
                RedisCacheUtil.valueObjSet(key, user);
            }
        }
        // 需求未确定 先注释掉 真正的用户在线状态
//        FetchUserResp fetchUserResp = RedisCacheUtil.valueObjGet("user:status:" + userId, FetchUserResp.class);
//        if (fetchUserResp != null) {
//            user.setState(fetchUserResp.getState());
//            user.setUpdateTime(fetchUserResp.getUpdateTime());
//        }
        return user;
    }

    /**
     * 根据用户uuid获取
     *
     * @param uuid
     * @return
     */
    protected User getUserByUuidCache(String uuid) {
        String key = RedisKeyUtil.getUserUuidKey(uuid);
        Long userId = RedisCacheUtil.valueObjGet(key, Long.class);
        if (userId == null) {
            User user = userMapper.selectByUuid(uuid);
            if (CommonUtil.isEmpty(user)) {
                return null;
            } else {
                userId = user.getId();
                RedisCacheUtil.valueObjSet(key, userId, RedisKeyUtil.DEFAULT_CACHE_TIME);
            }
        }
        return getUserByIdCache(userId);
    }

    /**
     * 描述：依据userId修改用户状态
     * @author maochengyuan
     * @created 2018/7/13 17:50
     * @param userId 用户id
     * @param userStatus 用户状态
     * @return int
     */
    public int updateUserStatusByUserId(Long userId, UserStatusEnum userStatus) {
        if (userId == null) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        if (userStatus == null) {
            throw new BusinessException(UserExceptionEnum.USER_STATUS_IS_NULL);
        }
        int result = this.userMapper.updateUserStatusByUserId(userId, userStatus.getCode());
        this.delUserCache(userId);
        return result;
    }

    /**
     * 删除缓存，用户更新
     *
     * @param userId
     */
    public void delUserCache(Long userId) {
        String key = RedisKeyUtil.getUserKey(userId);
        RedisCacheUtil.delete(key);
    }

    /****************************************** Redis function end ****************************************************/
}