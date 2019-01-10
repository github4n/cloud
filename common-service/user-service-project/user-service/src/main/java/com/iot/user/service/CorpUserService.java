package com.iot.user.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.*;
import com.iot.message.enums.MessageTempType;
import com.iot.permission.api.PermissionApi;
import com.iot.permission.vo.RoleDto;
import com.iot.permission.vo.RoleReqDto;
import com.iot.permission.vo.UsersToRoleDto;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.vo.req.SaveTenantReq;
import com.iot.tenant.vo.req.SaveTenantReviewRecordReq;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.user.constant.UserConstants;
import com.iot.user.entity.User;
import com.iot.user.entity.UserToken;
import com.iot.user.enums.*;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.util.BeanCopyUtil;
import com.iot.user.util.RedisKeyUtil;
import com.iot.user.util.UserTokenUtil;
import com.iot.user.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;



@Service
public class CorpUserService extends BaseUserService {

    private final Logger logger = LoggerFactory.getLogger(CorpUserService.class);

    private final Integer TenantAuditApply = 0;
    private final Integer TenantAuditNoPass = 1;
    // 租户redis缓存前缀
    private static final String TENANT_KEY = "tenant:";
    private final Long BOSS_APP = -1L;

    @Autowired
    protected PermissionApi permissionApi;


    /**
     * 描述：用户注册
     * @author 490485964@qq.com
     * @date 2018/7/3 20:13
     * @param
     * @return
     */
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
        logger.debug("=== user service register ===" + registerReq.toString());
        String validateKey = RedisKeyUtil.getCorpRegisterCodeKey(registerReq.getUserName());
        String validateCode = RedisCacheUtil.valueGet(validateKey);
        if (StringUtil.isEmpty(validateCode)) {
            logger.error("redis register email verifyCode is null {}", registerReq.getUserName());
            throw new BusinessException(UserExceptionEnum.REGISTER_VERIFYCODE_ERROR);
        }
        if (!validateCode.equals(registerReq.getVerifyCode())) {
            throw new BusinessException(UserExceptionEnum.REGISTER_VERIFYCODE_ERROR);
        }
        User oldUser = getCorpUserByNameCache(registerReq.getUserName(), UserLevelEnum.BUSINESS.getCode());
        if (oldUser != null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_EXIST);
        }
        SaveTenantReq req = new SaveTenantReq();
        String tenantUuid = ToolUtil.getUUID();
        req.setCode(tenantUuid);
        req.setAuditStatus(AuditStatusEnum.UNREVIEWED.getCode());
        req.setLockStatus(LockStatusEnum.NOLOCKED.getCode());
        Long tenantId = tenantApi.save(req);
        User user = new User();
        BeanUtils.copyProperties(registerReq, user);
        Date now = new Date();
        user.setPassword(MD5SaltUtil.generate(registerReq.getPassWord()));
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setTenantId(tenantId);
        user.setEmail(registerReq.getUserName());
        user.setUserLevel(UserLevelEnum.BUSINESS.getCode());
        user.setAdminStatus(AdminStatusEnum.SUPER.getCode());
        String uuid = ToolUtil.getUUID();
        user.setUuid(uuid);
        user.setState(UserConstants.USERSTATE_ACTIVE);
        user.setUserStatus(UserStatusEnum.NORMAL.getCode());
//        user.setUserStatus(UserStatusEnum.NORMAL.getCode());
        userMapper.insertSelective(user);


        RoleReqDto roleReqVo = new RoleReqDto();
        roleReqVo.setRoleCode(UserConstants.HIDE_USER_ROLE_CODE);
        roleReqVo.setRoleType(UserConstants.HIDE_USER_ROLE_TYPE);
        List<RoleDto> roleVoList= permissionApi.getRole(roleReqVo);
        if(null == roleVoList || roleVoList.size() == 0){
            logger.error("*****************roleVoList is null*******************");
            throw new BusinessException(UserExceptionEnum.SYSTEM_ERROR);
        }
        RoleDto roleVo = roleVoList.get(0);
        UsersToRoleDto usersToRoleVo = new UsersToRoleDto();
        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());
        usersToRoleVo.setRoleId(roleVo.getId());
        usersToRoleVo.setUserIds(userIds);
        usersToRoleVo.setCreateId(user.getId());
        permissionApi.addUsersToRole(usersToRoleVo);

        SaveTenantReviewRecordReq tenantReviewRecord = new SaveTenantReviewRecordReq();
        tenantReviewRecord.setTenantId(tenantId);
        tenantReviewRecord.setProcessStatus(0);
//    	tenantReviewRecord.setOperateDesc("PendReview");
        tenantReviewRecord.setCreateBy(user.getId());
        tenantApi.saveTenantReviewRecord(tenantReviewRecord);

        RedisCacheUtil.delete(validateKey);
        return user.getId();
    }

    /**
     * 用户登录（针对2B用户，2B不需要策略服务）
     * @param req
     * @return
     */
    public LoginResp login(LoginReq req) {
        logger.debug("=== user service login 2B ===" + req.toString());
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
        User user = getCorpUserByNameCache(req.getUserName(), UserLevelEnum.BUSINESS.getCode());
        if (user == null || null == user.getTenantId()) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(user.getTenantId());
        if (null == tenantInfoResp) {
            throw new BusinessException(UserExceptionEnum.TENANT_INFO_IS_NULL);
        }
//		if (tenantInfoResp.getAuditStatus() == null || 
//				(tenantInfoResp.getAuditStatus() != null && tenantInfoResp.getAuditStatus().intValue() != AuditStatusEnum.NORMAL.getCode().intValue())) {
//			throw new BusinessException(UserExceptionEnum.AUDIT_NOT_PASS);
//		}
        if (tenantInfoResp.getLockStatus() == null ||
                (tenantInfoResp.getLockStatus() != null && tenantInfoResp.getLockStatus().intValue() == LockStatusEnum.LOCKED.getCode().intValue())) {
            throw new BusinessException(UserExceptionEnum.ACCOUNT_LOCKED);
        }
        //是否需要完善企业信息
        Boolean improveTenantInfo = validateTenantInfo(tenantInfoResp);
        String key = RedisKeyUtil.getTenantPerfectFlagKey(tenantInfoResp.getId());
        RedisCacheUtil.valueObjSet(key, improveTenantInfo);
        //验证密码
        if (null != req.getPwd() && MD5SaltUtil.verify(req.getPwd(), user.getPassword())) {//req.getPwd().equals(user.getPassword())
            Long userId = user.getId();
            logger.debug("user【" + userId + "】 login success.");
            //登录成功，删除登录失败记录缓存
            RedisCacheUtil.delete(loginFailNumKey);
            //token刷新方式与C端共用
            UserToken userToken = UserTokenUtil.loginRefreshToken(userId, req.getTerminalMark(), user.getTenantId(), user.getUuid(), SystemTypeEnum.USER_PORTAL.getCode());
            //应答
            LoginResp res = BeanCopyUtil.getRespFromToken(userToken);
            res.setNickName(user.getNickname());
            res.setImproveTenantInfo(improveTenantInfo);
            res.setTenantName(tenantInfoResp.getName());
            res.setUserUuid(user.getUuid());
            res.setTenantUuId(tenantInfoResp.getCode());
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

    /**
     * 描述：返回用于生成验证码图片的code
     * @author 490485964@qq.com
     * @date 2018/7/4 10:35
     * @param
     * @return
     */
    public String getVerifyCodeImage(String userName){
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        String verifyCodeKey = RedisKeyUtil.getCorpLoginFailCodeKey(userName);
        RedisCacheUtil.delete(verifyCodeKey);
        RedisCacheUtil.valueSet(verifyCodeKey, verifyCode, UserConstants.EXPIRATION_TIME);
        return verifyCode;
    }


    /**
     * 描述：验证企业信息是否已经完善
     * @author 490485964@qq.com  
     * @date 2018/7/4 10:35
     * @param
     * @return
     */
    private Boolean validateTenantInfo(TenantInfoResp tenantInfoResp) {
        if (StringUtil.isBlank(tenantInfoResp.getCellphone())
                || StringUtil.isBlank(tenantInfoResp.getContacts())
                || StringUtil.isBlank(tenantInfoResp.getCountry())
                || StringUtil.isBlank(tenantInfoResp.getEmail())
                || StringUtil.isBlank(tenantInfoResp.getName())
                || StringUtil.isBlank(tenantInfoResp.getBusiness())
                || StringUtil.isBlank(tenantInfoResp.getJob())) return false;
        return true;
    }

    /**
     *@description 只保存租户信息，不提前审核
     *@author wucheng
     *@params [req]
     *@create 2018/12/15 14:15
     *@return java.lang.Long
     */
    public Long saveTenantInfo(TenantReq req) {
        logger.info("to improve Tenant Info " + req.toString());
        if (StringUtil.isBlank(req.getCellphone())
                || StringUtil.isBlank(req.getContacts())
                || StringUtil.isBlank(req.getCountry())
                || StringUtil.isBlank(req.getEmail())
                || StringUtil.isBlank(req.getName())
                || StringUtil.isBlank(req.getBusiness())
                || StringUtil.isBlank(req.getJob())
                ) {
            throw new BusinessException(UserExceptionEnum.TENANT_INFO_IS_NOT_COMPLETE);
        }
        if (null == req.getId() || null == req.getUserId()) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        User user = getUserByIdCache(req.getUserId());
        if (null == user) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if (!req.getId().equals(user.getTenantId()) ) {
            logger.info("current login user :" + user.getUuid());
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(req.getId());
        if(req.getCellphone().contains("*")){
            req.setCellphone(tenantInfoResp.getCellphone());
        }else {
            ValidateUtil.isNumeric(req.getCellphone());
        }
        SaveTenantReq saveTenantReq = new SaveTenantReq();
        BeanUtils.copyProperties(req, saveTenantReq);
        Long id = tenantApi.save(saveTenantReq);
        RedisCacheUtil.delete(TENANT_KEY + SaaSContextHolder.currentTenantId());
        return id;
    }
    /**
     * 描述：完善企业信息  
     * @author 490485964@qq.com  
     * @date 2018/7/4 10:34  
     * @param
     * @return
     */
    public Long improveTenantInfo(TenantReq req) {
        logger.info("to improve Tenant Info " + JSON.toJSONString(req));
        if (StringUtil.isBlank(req.getCellphone())
                || StringUtil.isBlank(req.getContacts())
                || StringUtil.isBlank(req.getCountry())
                || StringUtil.isBlank(req.getEmail())
                || StringUtil.isBlank(req.getName())
                || StringUtil.isBlank(req.getBusiness())
                || StringUtil.isBlank(req.getJob())
                ) {
            throw new BusinessException(UserExceptionEnum.TENANT_INFO_IS_NOT_COMPLETE);
        }
        if (null == req.getId() || null == req.getUserId()) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }
        User user = getUserByIdCache(req.getUserId());
        if (null == user) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if (!req.getId().equals(user.getTenantId()) ) {
            logger.info("current login user :" + user.getUuid());
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(req.getId());
        if(req.getCellphone().contains("*")){
            req.setCellphone(tenantInfoResp.getCellphone());
        }else {
            ValidateUtil.isNumeric(req.getCellphone());
        }
        SaveTenantReq saveTenantReq = new SaveTenantReq();
        BeanUtils.copyProperties(req, saveTenantReq);
//        if(tenantInfoResp.getAuditStatus() != null && TenantAuditNoPass.equals(tenantInfoResp.getAuditStatus())) {
        saveTenantReq.setAuditStatus(TenantAuditApply);
        SaveTenantReviewRecordReq saveTenantReviewRecordReq = new SaveTenantReviewRecordReq();
        saveTenantReviewRecordReq.setCreateBy(user.getId());
        saveTenantReviewRecordReq.setOperateDesc("");
        saveTenantReviewRecordReq.setProcessStatus(TenantAuditApply);
        saveTenantReviewRecordReq.setTenantId(req.getId());
        tenantApi.saveTenantReviewRecord(saveTenantReviewRecordReq);
//        }
        // 删除用户缓存
        RedisCacheUtil.delete(TENANT_KEY + SaaSContextHolder.currentTenantId());
        String key = RedisKeyUtil.getTenantPerfectFlagKey(req.getId());
        RedisCacheUtil.valueObjSet(key, true);
        Long id = tenantApi.save(saveTenantReq);
        return id;
    }


    public void resetPwd(ResetPwdReq req) {
        logger.debug("=== user service resetPwd ===" + req.toString());
        if (StringUtil.isEmpty(req.getVerifyCode())) {
            throw new BusinessException(UserExceptionEnum.VERIFYCODE_IS_NULL);
        }

        if (StringUtil.isEmpty(req.getUserName())) {
            throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
        }

        //根据用户名取对象
        User user = getCorpUserByNameCache(req.getUserName(), UserLevelEnum.BUSINESS.getCode());
        if (CommonUtil.isEmpty(user)) {
            throw new BusinessException(UserExceptionEnum.USER_NOT_REGISTER);
        }
        String email = user.getEmail();
        //若邮箱信息为空，则从userName中取
        if(StringUtil.isBlank(email)){
            email = user.getUserName();
        }
        ValidateUtil.validateEmail(email);
        //验证码校验
        String key = RedisKeyUtil.getCorpResetPwdCodeKey(email);
        String code = RedisCacheUtil.valueGet(key);
        if (StringUtil.isEmpty(code)) {
            logger.error("*******  reset pwd verifyCode in redis is null  key={} code={} ***** ",key,code);
            throw new BusinessException(UserExceptionEnum.RESET_PWD_VERIFYCODE_ERROR);
        }
        //验证码判断
        if (!code.equals(req.getVerifyCode())) {
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
        //删除验证码缓存
        RedisCacheUtil.delete(key);
    }

    /**
     * 描述：检查用户名
     * @author 490485964@qq.com
     * @date 2018/7/4 15:12
     * @param
     * @return
     */
    public Integer checkUserName(String userName) {
        logger.debug("=== user service checkUserName ===" + userName);
        if (StringUtil.isEmpty(userName)) {
            return UserExistEnum.NOT_EXIST.getCode();
        }
        //判断用户是否存在
        if (userMapper.countByNameAndUserLevel(userName, UserLevelEnum.BUSINESS.getCode()) > 0) {
            return UserExistEnum.EXIST.getCode();
        }else {
            return UserExistEnum.NOT_EXIST.getCode();
        }
    }
    /**
     * 描述：批量查询
     * @author 490485964@qq.com
     * @date 2018/7/12 15:09
     * @param
     * @return
     */
    public Map<Long,FetchUserResp> getByUserIds(List<Long> userIdList) {
        logger.debug("=== user service getByUserIds ===" + userIdList);
        if (null == userIdList || userIdList.size() == 0) {
            return null;
        }
        if(userIdList.size()>1000){
            throw new BusinessException(UserExceptionEnum.PARAM_SIZE_IS_TOO_LARGE);
        }
        Map<Long,FetchUserResp> fetchUserRespMap = new HashMap<>();
        List<User> userList = userMapper.getByUserIds(userIdList);
        if(null != userList && userList.size()>0){
            for(User user : userList){
                fetchUserRespMap.put(user.getId(),BeanCopyUtil.getRespFromUser(user));
            }
        }
        return fetchUserRespMap;
    }


    /**
     * 描述：创建子账号
     * @author nongchongwei
     * @date 2018/11/5 15:46
     * @param
     * @return
     */
    public Long addSubCorpUser(RegisterReq registerReq) {
        if (null == registerReq){
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }
        if (StringUtil.isBlank(registerReq.getUserName())) {
            throw new BusinessException(UserExceptionEnum.USERNAME_IS_NULL);
        }
        if(null == registerReq.getTenantId()){
            throw new BusinessException(UserExceptionEnum.TENANTID_IS_NULL);
        }
        ValidateUtil.validateEmail(registerReq.getUserName());
        if (StringUtil.isBlank(registerReq.getPassWord())) {
            throw new BusinessException(UserExceptionEnum.PWD_IS_NULL);
        }
        User operatorUser = getUserByIdCache(registerReq.getOperatorId());
        if(null == operatorUser || UserLevelEnum.BUSINESS.getCode()!=operatorUser.getUserLevel() || AdminStatusEnum.SUPER.getCode()!=operatorUser.getAdminStatus()){
            throw new BusinessException(UserExceptionEnum.NO_AUTHORITY);
        }
        logger.debug("=== user service addSubCorpUser ===" + registerReq.toString());
        Integer userLevel = UserLevelEnum.BUSINESS.getCode();
        if(registerReq.getUserLevel() != null ){
            userLevel = registerReq.getUserLevel();
        }
        User oldUser = getCorpUserByNameCache(registerReq.getUserName(), userLevel);
        if (oldUser != null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_EXIST);
        }
        User user = new User();
        BeanUtils.copyProperties(registerReq, user);
        Date now = new Date();
        user.setPassword(MD5SaltUtil.generate(registerReq.getPassWord()));
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setTenantId(registerReq.getTenantId());
        user.setEmail(registerReq.getUserName());
        user.setUserLevel(userLevel);
        user.setAdminStatus(AdminStatusEnum.NORMAL.getCode());
        String uuid = ToolUtil.getUUID();
        user.setUuid(uuid);
        user.setState(UserConstants.USERSTATE_ACTIVE);
        user.setUserStatus(UserStatusEnum.NORMAL.getCode());
        user.setTel(registerReq.getTel());
        userMapper.insertSelective(user);

        return user.getId();
    }
    /**
     * 描述：查询子账号
     * @author nongchongwei
     * @date 2018/11/5 15:46
     * @param
     * @return
     */
    public PageInfo<FetchUserResp> querySubUserList(UserSearchReq req) {
        if(null == req.getTenantId()){
            throw new BusinessException(UserExceptionEnum.TENANTID_IS_NULL);
        }
        Integer pageNum = req.getPageNum();
        Integer pageSize = req.getPageSize();

        if (null == pageNum) {
            pageNum = 0;
        }
        if (null == pageSize || 0 == pageSize) {
            pageSize=10;
        }
        PageHelper.startPage(pageNum,pageSize,true);
        List<User> userList = userMapper.querySubUserList(req);
        PageInfo pageInfo = new PageInfo(userList,10);
        List<FetchUserResp> fetchUserRespList = new ArrayList<>();
        FetchUserResp fetchUserResp = null;
        for(User user : userList){
            fetchUserResp = new FetchUserResp();
            BeanUtils.copyProperties(user, fetchUserResp);
            fetchUserRespList.add(fetchUserResp);
        }
        pageInfo.setList(fetchUserRespList);
        return pageInfo;
    }

    /**
     * 描述：查询用户  2B
     * @author nongchongwei
     * @date 2018/7/2 14:58
     * @param
     * @return
     */
    protected User getCorpUserByNameCache(String userName, Integer userLevel) {
        String key = RedisKeyUtil.getCorpUserNameKey(userName, userLevel);
        Long userId = RedisCacheUtil.valueObjGet(key, Long.class);
        if (null == userId) {
            User user = userMapper.getByUserNameAndLevel(userName, userLevel);
            if (null != user) {
                userId = user.getId();
                RedisCacheUtil.valueObjSet(key, userId);
            } else {
                return null;
            }
        }
        return getUserByIdCache(userId);
    }

    /**
     * 发送验证码
     *
     * @param email
     * @param type  1=注册 2=重置密码
     */
    public void sendCorpVerifyCode(String email, Byte type, String language) {
        logger.info("=== user service sendVerifyCode ===" + email + "/" + type);
        //验证码生成规则
        String code = CommonUtil.getRandNum(10000000, 99999999).toString();
        Map<String, String> noticeMap = Maps.newHashMap();
        MessageTempType tempType = null;
        //验证码缓存Redis
        tempType = MessageTempType.EN00001;
        if (type == 1) {
            noticeMap.put("activateCode", code);
            RedisCacheUtil.valueSet(RedisKeyUtil.getCorpRegisterCodeKey(email), code, UserConstants.EXPIRATION_TIME);
        } else if (type == 2) {
            noticeMap.put("activateCode", code);
            RedisCacheUtil.valueSet(RedisKeyUtil.getCorpResetPwdCodeKey(email), code, UserConstants.EXPIRATION_TIME);
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
        noticeMap.put("subject", "User Validate");
        noticeMap.put("templateId", tempType.getTempId());
        messageApi.mailSinglePush(BOSS_APP, to, noticeMap, 1, language);
        RedisCacheUtil.valueSet("Emial_limit:" + email + "_" + type, "1", 60L);
    }
}