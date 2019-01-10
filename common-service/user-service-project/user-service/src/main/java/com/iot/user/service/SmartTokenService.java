package com.iot.user.service;

import com.alibaba.fastjson.JSON;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.entity.SmartToken;
import com.iot.user.mapper.SmartTokenMapper;
import com.iot.user.util.RedisKeyUtil;
import com.iot.user.vo.SmartTokenReq;
import com.iot.user.vo.SmartTokenResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class SmartTokenService {

    private Logger log = LoggerFactory.getLogger(SmartTokenService.class);

    @Autowired
    private SmartTokenMapper smartTokenMapper;

    private static final Long DEFAULT_EXPIRED_TIME = 3600L;


    // 获取 alexa的 smartToken
    public SmartTokenResp getAlexaSmartTokenByUserId(Long userId) {
        return getSmartTokenByUserIdAndSmart(userId, SmartHomeConstants.ALEXA);
    }

    // 获取 googleHome的 smartToken
    public SmartTokenResp getGoogleSmartTokenByUserId(Long userId) {
        return getSmartTokenByUserIdAndSmart(userId, SmartHomeConstants.GOOGLE_HOME);
    }

    // 根据 userId、smart 获取一个 SmartTokenResp
    public SmartTokenResp getSmartTokenByUserIdAndSmart(Long userId, Integer smart) {
        String redisKey = RedisKeyUtil.getSmartTokenKey(userId, smart);

        // 从 缓存获取
        SmartTokenResp resp = RedisCacheUtil.valueObjGet(redisKey, SmartTokenResp.class);
        if (resp == null) {
            resp = new SmartTokenResp();

            // 从 数据库获取
            SmartToken smartToken = smartTokenMapper.getByUserIdAndSmart(userId, smart);
            if (smartToken != null) {
                BeanUtils.copyProperties(smartToken, resp);
            }

            // 加入缓存
            RedisCacheUtil.valueObjSet(redisKey, resp, DEFAULT_EXPIRED_TIME);
        } else {
            // 判断是否空内容
            if (resp.getId() == null) {
                resp = null;
            }
        }

        return resp;
    }

    public void insert(SmartTokenReq vo, Long userId) {
        SmartToken token = new SmartToken();
        BeanUtils.copyProperties(vo, token);
        smartTokenMapper.insert(token);
        // 移除缓存
        RedisCacheUtil.delete(RedisKeyUtil.getSmartTokenKey(userId, vo.getSmart()));
    }

    public void update(SmartTokenReq req, Long userId) {
        SmartToken token = new SmartToken();
        BeanUtils.copyProperties(req, token);
        smartTokenMapper.updateByPrimaryKey(token);
        // 移除缓存
        RedisCacheUtil.delete(RedisKeyUtil.getSmartTokenKey(userId, req.getSmart()));
    }

    public void merge(SmartTokenReq req) {
        log.debug("merge, req.jsonString={}", JSON.toJSONString(req));

        SmartTokenResp exit = null;
        if (req.getSmart() != null) {
            if (req.getSmart() == SmartHomeConstants.ALEXA) {
                exit = this.getAlexaSmartTokenByUserId(req.getUserId());
            } else if (req.getSmart() == SmartHomeConstants.GOOGLE_HOME) {
                exit = this.getGoogleSmartTokenByUserId(req.getUserId());
            }
        } else if(req.getThirdPartyInfoId() != null){
            exit = this.getByUserIdAndThirdPartyInfoId(req.getUserId(), req.getThirdPartyInfoId());
        }

        log.info("merge, userId={}, req={}, exit={}", req.getUserId(), JSON.toJSONString(req), JSON.toJSONString(exit));

        Integer expire = req.getExpiresIn();
        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.SECOND, expire);

        if (exit != null) {
            // 更新
            exit.setAccessToken(req.getAccessToken());
            exit.setRefreshToken(req.getRefreshToken());
            exit.setUpdateTime(cl.getTime());
            BeanUtils.copyProperties(exit, req);
            this.update(req, req.getUserId());
        } else {
            // 新增
            req.setUpdateTime(cl.getTime());
            this.insert(req, req.getUserId());
        }
    }

    /**
     *  根据 userId、smartType 删除数据
     * @param userId
     * @param smart
     */
    public void deleteSmartTokenByUserIdAndSmart(Long userId, int smart){
        smartTokenMapper.deleteSmartTokenByUserIdAndSmart(userId, smart);
        // 移除缓存
        RedisCacheUtil.delete(RedisKeyUtil.getSmartTokenKey(userId, smart));
    }

    /**
     *  根据 userId、localAccessToken 删除数据
     * @param localAccessToken
     */
    public void deleteByUserIdAndLocalAccessToken(Long userId, String localAccessToken){
        if (userId == null || StringUtils.isBlank(localAccessToken)) {
            log.error("deleteByLocalAccessToken error, userId is null or localAccessToken is empty, userId={}, localAccessToken={}", userId, localAccessToken);
            return ;
        }
        smartTokenMapper.deleteByUserIdAndLocalAccessToken(userId, localAccessToken);
    }

    /**
     *  更新 localToken
     * @param id
     * @param localAccessToken
     * @param localRefreshToken
     */
    public void updateLocalTokenById(Long id, String localAccessToken, String localRefreshToken) {
        smartTokenMapper.updateLocalTokenById(id, localAccessToken, localRefreshToken);
    }

    /**
     *  根据 本地生成的access_token 获取记录
     * @param localAccessToken
     * @return
     */
    public SmartTokenResp getByLocalAccessToken(String localAccessToken) {
        SmartTokenResp resp = null;
        if (StringUtils.isEmpty(localAccessToken)) {
            return resp;
        }

        SmartToken smartToken = smartTokenMapper.getByLocalAccessToken(localAccessToken);
        if (smartToken != null) {
            resp = new SmartTokenResp();
            BeanUtils.copyProperties(smartToken, resp);
        }
        return resp;
    }

    /**
     *  根据 userId、thirdPartyInfoId 获取一个 SmartTokenResp
     *
     * @param userId
     * @param thirdPartyInfoId
     * @return
     */
    public SmartTokenResp getByUserIdAndThirdPartyInfoId(Long userId, Long thirdPartyInfoId) {
        SmartTokenResp resp = null;

        // 从 数据库获取
        SmartToken smartToken = smartTokenMapper.getByUserIdAndThirdPartyInfoId(userId, thirdPartyInfoId);
        if (smartToken != null) {
            resp = new SmartTokenResp();
            BeanUtils.copyProperties(smartToken, resp);
        }

        return resp;
    }

    /**
     *  根据 userId 获取 third_party_info_id 不为空的记录
     * @param userId
     * @return
     */
    public List<SmartToken> findThirdPartyInfoIdNotNull(Long userId) {
        return smartTokenMapper.findThirdPartyInfoIdNotNull(userId);
    }
}
