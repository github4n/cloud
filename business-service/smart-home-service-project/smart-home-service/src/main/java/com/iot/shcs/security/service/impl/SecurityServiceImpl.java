package com.iot.shcs.security.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.security.constant.ArmModeEnum;
import com.iot.shcs.security.constant.SecurityConstants;
import com.iot.shcs.security.domain.Security;
import com.iot.shcs.security.exception.SecurityExceptionEnum;
import com.iot.shcs.security.mapper.SecurityMapper;
import com.iot.shcs.security.service.SecurityRuleService;
import com.iot.shcs.security.service.SecurityService;
import com.iot.shcs.security.util.BeanCopyUtil;
import com.iot.shcs.security.util.RedisKeyUtil;
import com.iot.shcs.security.vo.SecurityRule;
import com.iot.shcs.security.vo.rsp.SecurityResp;
import com.iot.shcs.space.service.impl.SpaceServiceImpl;
import com.iot.shcs.space.vo.SpaceDeviceResp;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.AssertUtils;
import com.iot.util.ToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/9 17:58
 * 修改人:
 * 修改时间：
 */

@Service
public class SecurityServiceImpl implements SecurityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Autowired
    private SecurityMapper securityMapper;
    @Autowired
    private UserApi userApi;
    @Autowired
    private UserVirtualOrgApi userVirtualOrgApi;
    @Autowired
    private SpaceServiceImpl spaceService;
    @Autowired
    private SecurityRuleService securityRuleService;
    @Autowired
    private SecurityMqttService securityMqttService;

    @Override
    public Security getBySpaceId(Long spaceId,Long tenantId) {

      Security security = RedisCacheUtil.valueObjGet(RedisKeyUtil.getSecurityHomeIdKey(spaceId,tenantId), Security.class);
      if (security == null) {
          security=securityMapper.getBySpaceId(spaceId);
          if (security != null) {
                // 加入缓存
              RedisCacheUtil.valueObjSet(RedisKeyUtil.getSecurityHomeIdKey(spaceId,tenantId), security, RedisKeyUtil.DEFAULT_CACHE_TIME);
          }
      }
        return security;
    }

    @Override
    public SecurityResp getSecurityRespBySpaceId(Long spaceId,Long tenantId) {
        SecurityResp securityResp = null;

        Security security = this.getBySpaceId(spaceId,tenantId);
        try {
            if (security != null) {
                securityResp = new SecurityResp();
                BeanCopyUtil.copySecurityResp(securityResp, security);
            }
        } catch (Exception e) {
            LOGGER.info("getSecurityRespBySpaceId error.", e);
            throw new BusinessException(BusinessExceptionEnum.GET_SECURITY_ERROR, e);
        }

        return securityResp;
    }

    @Override
    public void updateArmModeBySpaceId(Long spaceId, Long tenantId,Long updateBy, com.iot.shcs.security.constant.ArmModeEnum armModeEnum) {
        securityMapper.updateArmModeBySpaceId(spaceId,tenantId, updateBy, armModeEnum.getArmMode());
        // 移除缓存
        RedisCacheUtil.delete(RedisKeyUtil.getSecurityHomeIdKey(spaceId,tenantId));
    }



    @Override
    public void updatePasswordById(Security security, Long updateBy, String password) {
        securityMapper.updatePasswordById(security.getId(), updateBy, password);
        // 移除缓存
        RedisCacheUtil.delete(RedisKeyUtil.getSecurityHomeIdKey(security.getSpaceId(),security.getTenantId()));
    }

    /**
     *  安防信息恢复默认值
     *      arm_mode=off
     */
    @Override
    public Long securityResetFactory(String deviceUuid,Long tenantId) {
        Long userId = null;
       // SpaceDeviceResp spaceDeviceResp = spaceService.getSpaceDeviceByDeviceUuid(deviceUuid,tenantId);
        Long spaceId=securityMqttService.getSpaceIdByDeviceUuid(deviceUuid,tenantId);

        Security security = new Security();
        security.setSpaceId(spaceId);
        security.setArmMode(ArmModeEnum.off.getArmMode());
        security.setUpdateTime(new Date());
        securityMapper.securityResetFactory(security);

        Security cache = getBySpaceId(spaceId,tenantId);
        if (cache != null) {
            userId = cache.getCreateBy();
            // 移除缓存
            RedisCacheUtil.delete(RedisKeyUtil.getSecurityHomeIdKey(spaceId, tenantId));
        }

        return userId;
    }


    /**
     * 创建security
     *
     * @param userId
     * @param spaceId  家的id
     * @param password 安防密码(加密后的)
     * @return
     */
    @Override
    public Security createSecurity(Long tenantId, Long userId, Long spaceId, String password) {
        AssertUtils.notNull(userId, UserExceptionEnum.USERID_IS_NULL.getMessageKey());
        AssertUtils.notNull(spaceId, BusinessExceptionEnum.SPACE_ID_IS_NULL.getMessageKey());
        AssertUtils.notNull(password, SecurityExceptionEnum.SECURITY_PASSWORD_IS_NULL.getMessageKey());

        FetchUserResp fetchUserResp = userApi.getUser(userId);
        AssertUtils.notNull(fetchUserResp, UserExceptionEnum.USER_IS_NOT_EXIST.getMessageKey());

        UserDefaultOrgInfoResp userDefaultOrgInfoResp = userVirtualOrgApi.getDefaultUsedOrgInfoByUserId(userId);
        if (userDefaultOrgInfoResp == null || userDefaultOrgInfoResp.getId() == null) {
            throw new BusinessException(TenantExceptionEnum.USER_VIRTUAL_ORG_NOT_EXIST);
        }

        SpaceResp spaceResp = spaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
        AssertUtils.notNull(spaceResp, BusinessExceptionEnum.SPACE_IS_NOT_EXIST.getMessageKey());

        if (!ToolUtils.isEqual(spaceResp.getCreateBy(), userId)) {
            LOGGER.error("initSecurityAndIfttt() error! because spaceResp.getCreateBy() != userId");
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }

        Security security = getBySpaceId(spaceId,tenantId);
        if (security != null) {
            LOGGER.error("initSecurityAndIfttt() error! 指定的spaceId已经存在 security 记录");
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }

        Date currentTime = new Date();

        // 保存 安防信息主表
        security = new Security();
        security.setSpaceId(spaceId);
        security.setArmMode(ArmModeEnum.off.getArmMode());
        security.setPassword(password);
        security.setCreateBy(userId);
        security.setCreateTime(currentTime);
        security.setUpdateBy(userId);
        security.setUpdateTime(currentTime);
        security.setTenantId(fetchUserResp.getTenantId());
        security.setOrgId(userDefaultOrgInfoResp.getId());
        securityMapper.insert(security);
        return security;
    }

    /**
     * 设置安防状态
     *
     * @param type
     * @param securityId
     */
    public void setSecurityStatus(Integer type, Long securityId,Long tenantId) {
        LOGGER.info("=== receive setSecurityStatus request ===" + "type:" + type + "securityId" + securityId + ",tenantId:" +tenantId);
        try {
            //根据家庭主键获取security_rule
            if (securityId!= null) {
                SecurityRule req = new SecurityRule();
                req.setSecurityId(securityId);
                req.setTenantId(tenantId);
                //获取security_rule列表
                List<SecurityRule> list= securityRuleService.list(req);
                if (!list.isEmpty()) {
                    for (SecurityRule ruleResp : list) {
                        updateStatus(type, ruleResp.getSecurityId(), ruleResp.getType(),ruleResp);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("set security status error", e);
            throw new BusinessException(SecurityExceptionEnum.SET_SECURITY_STATUS_ERROR, e);
        }
    }

    @Override
    public Security getSecurityById(Long id) {
            return securityMapper.getSecurityById(id);
    }

    ////////////////////////////抽取方法////////////////////////////////////

    private void updateStatus(Integer type, Long securityId, String securityType,SecurityRule rule) {
//        SecurityRule securityRule=new SecurityRule();
//        BeanCopyUtil.copySecurityRule(securityRule,rule);
        if (type == SecurityConstants.SECURITY_TYPE_STAY) {
            //1.在家 离家rule 禁用、在家rule启用
            if (SecurityConstants.SECURITY_STAY.equals(securityType)) {
                rule.setEnabled(SecurityConstants.RUNNING);
            } else if (SecurityConstants.SECURITY_AWAY.equals(securityType)) {
                rule.setEnabled(SecurityConstants.STOP);
            }
        } else if (type == SecurityConstants.SECURITY_TYPE_AWAY) {
            //2.离家： 离家rule 启用、在家rule禁用
            if (SecurityConstants.SECURITY_STAY.equals(securityType)) {
                rule.setEnabled(SecurityConstants.STOP);
            } else if (SecurityConstants.SECURITY_AWAY.equals(securityType)) {
                rule.setEnabled(SecurityConstants.RUNNING);
            }
        } else if (type == SecurityConstants.SECURITY_TYPE_OFF) {
            //3.撤防 在家、离家rule禁用
            if (SecurityConstants.SECURITY_STAY.equals(securityType)) {
                rule.setEnabled(SecurityConstants.STOP);
            } else if (SecurityConstants.SECURITY_AWAY.equals(securityType)) {
                rule.setEnabled(SecurityConstants.STOP);
            }
        }
        if (rule.getEnabled() != null) {
            securityRuleService.saveSecurityRule(rule);
        }
    }

}
