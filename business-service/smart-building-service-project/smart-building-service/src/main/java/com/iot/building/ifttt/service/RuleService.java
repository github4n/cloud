package com.iot.building.ifttt.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.ifttt.constant.IftttConstants;
import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.mapper.RuleMapper;
import com.iot.building.ifttt.mapper.RuleTobMapper;
import com.iot.building.ifttt.util.BeanCopyUtil;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.building.ifttt.util.TimeUtil;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.vo.DeploymentReq;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;

/**
 * 描述：Rule业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:00
 */
@Service
public class RuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private UserApi userApi;
    @Autowired
    private RuleTobMapper ruleTobMapper;
    @Autowired
    private RuleMapper ruleMapper;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private SpaceApi spaceApi;

    /**
     * 根据 ids 获取列表
     *
     * @param dbIds
     * @return
     */
    public List<Rule> listRuleRespByIds(List<Long> dbIds) {
        if (CollectionUtils.isEmpty(dbIds)) {
            return null;
        }

        // 返回的 结果记录
        List<Rule> returnList = Lists.newArrayList();

        List<String> ruleIdKeysList = Lists.newArrayList();


        dbIds.forEach(ruleId -> {
            ruleIdKeysList.add(RedisKeyUtil.getIftttRuleForListIdKey(ruleId));
        });
        LOGGER.info("rule id list = " + JSON.toJSONString(ruleIdKeysList));

        // 已经命中的 id
        Map<String, Long> cacheIdMap = Maps.newHashMap();
        List<Rule> cacheList = RedisCacheUtil.mget(ruleIdKeysList, Rule.class);
        if (cacheList == null || cacheList.size() != dbIds.size()) {
            LOGGER.debug("listRuleRespByIds(), 从缓存取出的 cacheList is null OR 数量不等于 dbIds(dbIds.size={}).", dbIds.size());

            if (cacheList == null) {
                cacheList = Lists.newArrayList();
            }

            cacheList.forEach(cacheBean -> {
                cacheIdMap.put(cacheBean.getId() + "", cacheBean.getId());
            });

            // 未命中id
            List<String> unHitIdList = Lists.newArrayList();
            dbIds.forEach(dbId -> {
                Long tempId = cacheIdMap.get(dbId + "");
                if (tempId == null) {
                    unHitIdList.add(dbId + "");
                }
            });

            // 加入到缓存
            Map<String, Object> multiSet = Maps.newHashMap();
            // 未命中 结果记录
            List<Rule> unHitRuleList = Lists.newArrayList();

            //List<Rule> ruleList = ruleTobMapper.listByIds(unHitIdList);
            List<Rule> ruleList = ruleMapper.listByIds(unHitIdList);
            if (CollectionUtils.isNotEmpty(ruleList)) {
                ruleList.forEach(rule -> {
                    unHitRuleList.add(rule);
                    multiSet.put(RedisKeyUtil.getIftttRuleForListIdKey(rule.getId()), rule);
                });

                RedisCacheUtil.multiSet(multiSet, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);

                returnList.addAll(unHitRuleList);
            }

            returnList.addAll(cacheList);
        } else {
            LOGGER.debug("listRuleRespByIds(), 直接使用从缓存查询出的 cacheList." + JSON.toJSONString(cacheList));
            returnList = cacheList;
        }

        return returnList;
    }


    /**
     * 分页获取规则
     *
     * @param req
     * @return
     */
    public Page<RuleResp> list(RuleListReq req) {
        LOGGER.info("=== receive rule list request ===" + req.toString());
        Page<RuleResp> page = new Page<RuleResp>();
        try {
            Integer pageNum = req.getPageNum();
            Integer pageSize = req.getPageSize();
            if (pageNum == null) {
                pageNum = 1;
            }
            if (pageSize == null) {
                pageSize = 100;
            }

            // 目标页记录 列表
            List<RuleResp> resultList = Lists.newArrayList();

            // 目标页记录 的id列表
            com.github.pagehelper.Page<Rule> page1 = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
            //List<Rule> tempList = ruleTobMapper.findSimpleList(req);
            List<Rule> tempList = ruleMapper.findSimpleList(req);
            if (CollectionUtils.isNotEmpty(tempList)) {
                LOGGER.debug("list(), 从数据库查询出的 tempList.size()={}", tempList.size());
                if (SaaSContextHolder.currentContextMap().get(SaaSContextHolder.KEY_TENANT_ID) == null) {
                    LOGGER.debug("set SaaSContextHolder tenantId is {}.", tempList.get(0).getTenantId());
                    SaaSContextHolder.setCurrentTenantId(tempList.get(0).getTenantId());
                }
                List<Long> ruleIdList = buildRuleIdList(tempList);
                List<Rule> ruleList = listRuleRespByIds(ruleIdList);
                //LOGGER.debug("list(), 从缓存中查询出的 cacheList={}", ruleList.toString());

                if (CollectionUtils.isNotEmpty(ruleList)) {
                    for(Rule rule:ruleList){
                        DeploymentResp deploymentResp = spaceApi.findDeploymentById(rule.getTenantId(),rule.getOrgId(),rule.getDeployMentId());
                        RuleResp resp = getRuleResp(rule, req.getShowTime());
                        if (rule.getUserId() != null && userApi.getUser(rule.getUserId()) != null) {
                            resp.setUserName(userApi.getUser(rule.getUserId()).getUserName());
                        }
                        resp.setDeployMentName(deploymentResp.getDeployName());
                        resultList.add(resp);
                    }
                   /* ruleList.forEach(rule->{
                        RuleResp resp = getRuleResp(rule, req.getShowTime());
                        resp.setUserName(userApi.getUser(rule.getUserId()).getUserName());
                        resultList.add(resp);
                    });*/

                    // 排序
                    Collections.sort(resultList, new Comparator<RuleResp>() {
                        @Override
                        public int compare(RuleResp b1, RuleResp b2) {
                            //return b1.getName().compareTo(b2.getName());
                            return Collator.getInstance(Locale.CHINESE).compare(b1.getName(),b2.getName());
                        }
                    });
                    //LOGGER.debug("list(), 排序后数据 resultList={}", resultList.toString());
                }
            } else {
                LOGGER.info("list(), 从数据库查询出的 tempList is empty.");
            }

            page.setResult(resultList);
            page.setTotal(page1.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            //分页获取IFTTT规则失败
            LOGGER.error("get ifttt list error", e);
            throw new BusinessException(IftttExceptionEnum.GET_IFTTT_LIST_ERROR, e);
        }
        return page;
    }

    /**
     * 获取规则(没有分页)
     *
     * @param req
     * @return
     */
    public List<RuleResp> listNoPage(RuleListReq req) {
        LOGGER.info("=== receive rule list(no page) request ===" + req.toString());
        // 目标页记录 列表
        List<RuleResp> resultList = Lists.newArrayList();
        try {
            // 目标页记录 的id列表
            List<Rule> tempList = ruleMapper.findSimpleList(req);
            if (CollectionUtils.isNotEmpty(tempList)) {
                LOGGER.debug("list(), 从数据库查询出的 tempList.size()={}", tempList.size());
                if (SaaSContextHolder.currentContextMap().get(SaaSContextHolder.KEY_TENANT_ID) == null) {
                    LOGGER.debug("set SaaSContextHolder tenantId is {}.", tempList.get(0).getTenantId());
                    SaaSContextHolder.setCurrentTenantId(tempList.get(0).getTenantId());
                }
                List<Long> ruleIdList = buildRuleIdList(tempList);
                List<Rule> ruleList = listRuleRespByIds(ruleIdList);
                //LOGGER.debug("list(), 从缓存中查询出的 cacheList={}", ruleList.toString());

                if (CollectionUtils.isNotEmpty(ruleList)) {
                    for(Rule rule:ruleList){
                        RuleResp resp = getRuleResp(rule, req.getShowTime());
                        if (rule.getUserId() != null && userApi.getUser(rule.getUserId()) != null) {
                            resp.setUserName(userApi.getUser(rule.getUserId()).getUserName());
                        }
                        resultList.add(resp);
                    }
                    // 排序
                    Collections.sort(resultList, new Comparator<RuleResp>() {
                        @Override
                        public int compare(RuleResp b1, RuleResp b2) {
                            return b2.getCreateTime().compareTo(b1.getCreateTime());
                        }
                    });
                }
            } else {
                LOGGER.info("list(), 从数据库查询出的 tempList is empty.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("get ifttt list error", e);
            throw new BusinessException(IftttExceptionEnum.GET_IFTTT_LIST_ERROR, e);
        }
        return resultList;
    }


    /**
     * 修改rule
     *
     * @param rule
     */
    public Long save(Rule rule) {
        try {
            Long orgId = rule.getOrgId();
            Long tenantId = rule.getTenantId();
            rule.setOrgId(orgId);
            rule.setTenantId(tenantId);

            if (rule.getId() != null && rule.getId() != 0) {
                //修改
                rule.setTenantId(tenantId);
                rule.setUpdateTime(new Date());
                ruleMapper.updateByPrimaryKeySelective(rule);
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleKey(rule.getId()));
            } else {
                //新增规则
                rule.setTenantId(tenantId);
                rule.setCreateTime(new Date());
                rule.setUpdateTime(new Date());
                ruleMapper.insertSelective(rule);
            }

            // 删除用户的ifttt-rule列表缓存
            RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleForListIdKey(rule.getId()));
        } catch (Exception e) {
            //保存Rule失败
            LOGGER.error("save rule error", e);
            throw new BusinessException(IftttExceptionEnum.SAVE_RULE_ERROR, e);
        }
        return rule.getId();
    }

    /**
     * 获取缓存对象
     *
     * @param id
     * @return
     */
    public Rule getCache(Long id) {
        String key = RedisKeyUtil.getIftttRuleKey(id);
        Rule rule = RedisCacheUtil.valueObjGet(key, Rule.class);
        if (rule == null) {
            //rule = ruleTobMapper.selectByPrimaryKey(id);
            rule = ruleMapper.selectByPrimaryKey(id);
            if (rule == null) {
                return null;
                //throw new BusinessException(IftttExceptionEnum.RULE_IS_NULL);
            }
            RedisCacheUtil.valueObjSet(key, rule, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
        }
        return rule;
    }

    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    /**
     * 填充 ruleResp其它信息
     *
     * @param isShow
     * @return
     */
    public RuleResp fillRuleRespOtherInfo(RuleResp ruleResp, Boolean isShow) {
        if (ruleResp != null &&
                (isShow != null && isShow)) {
            List<SensorVo> sensors = sensorService.selectByRuleId(ruleResp.getId());
            if (!sensors.isEmpty()) {
                SensorVo sensor = sensors.get(0);
                if (sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_DEVICE)) {
                    //4:20 AM-3:20 PM
                    //Mon,Tue,Wed,Thu,Fri,Sat,Sun
                    String valid = sensor.getTiming();
                    if (StringUtil.isEmpty(valid) || "null".equals(valid)) {
                        return ruleResp;
                    }
                    Map validMap = JSON.parseObject(valid, Map.class);
                    String begin = (String) validMap.get("begin");
                    String end = (String) validMap.get("end");
                    List<Integer> week = (List<Integer>) validMap.get("week");
                    begin = TimeUtil.getTime(begin);
                    end = TimeUtil.getTime(end);
                    String time = begin + "-" + end;
                    ruleResp.setExecuteTime(time);
                    ruleResp.setExecuteDate(TimeUtil.getWeeks(week));
                } else if (sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_TIMER)) {
                    //定时任务，只有一个sensor
                    String properties = sensor.getProperties();
                    Map map = JSON.parseObject(properties, Map.class);
                    String at = (String) map.get("at");
                    List<Integer> week = (List<Integer>) map.get("repeat");
                    ruleResp.setExecuteTime(TimeUtil.getTime(at));
                    ruleResp.setExecuteDate(TimeUtil.getWeeks(week));
                } else if (sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_SUNRISE) ||
                        sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_SUNSET)) {
                    //sunrise sunset,只有一个sensor
                    String properties = sensor.getProperties();
                    Map map = JSON.parseObject(properties, Map.class);
                    String trigType = (String) map.get("trigType");
                    Integer intervalType = (Integer) map.get("intervalType");
                    String intervalTimeStr = (String) map.get("intervalTime");
                    Integer intervalTime = Integer.parseInt(intervalTimeStr);
                    List<Integer> week = (List<Integer>) map.get("repeat");
                    //1 min before sunrise
                    String time = trigType;
                    if (intervalType == 1 && intervalTime > 60) {
                        //before
                        intervalTime = intervalTime / 60;
                        time = intervalTime + " min before " + trigType;
                    } else if (intervalType == 2 && intervalTime > 60) {
                        //after
                        intervalTime = intervalTime / 60;
                        time = intervalTime + " min after " + trigType;
                    }
                    ruleResp.setExecuteTime(time);
                    ruleResp.setExecuteDate(TimeUtil.getWeeks(week));
                }
            }
        }
        return ruleResp;
    }

    /**
     * 封装RuleResp
     *
     * @param rule
     * @param isShow
     * @return
     */
    public RuleResp getRuleResp(Rule rule, Boolean isShow) {
        RuleResp ruleResp = BeanCopyUtil.getRuleResp(rule);
        return fillRuleRespOtherInfo(ruleResp, isShow);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        Long tenantId = getTenantId();

        //ruleTobMapper.deleteByPrimaryKey(id, tenantId);
        ruleMapper.deleteByPrimaryKey(id, tenantId);
        //删除rule对象缓存
        RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleKey(id));
        //删除rule-user关系缓存
        RedisCacheUtil.delete(RedisKeyUtil.getIftttUserKey(id));

        // 删除用户的ifttt-rule列表缓存
        RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleForListIdKey(id));

        LOGGER.debug("delete rule success");
    }


    public Boolean updateStatus(Long id, Boolean start) {
        LOGGER.info("=== receive update rule status request ===" + "{" + id + "，" + start + "}");
        try {
            Rule rule = getCache(id);
            if (rule == null) {
                throw new BusinessException(IftttExceptionEnum.RULE_IS_NULL);
            }
            Byte status = start ? IftttConstants.RUNNING : IftttConstants.STOP;
            if (status != rule.getStatus()) {
                rule.setStatus(status);
                //int result = ruleTobMapper.updateByPrimaryKeySelective(rule);
                int result = ruleMapper.updateByPrimaryKeySelective(rule);
                if (result != 1) {
                    throw new BusinessException(IftttExceptionEnum.UPDATE_RULE_STATUS_FAILED);
                }
                //清除缓存 下次从数据库取最新数据
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleKey(id));

                // 删除用户的ifttt-rule列表缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleForListIdKey(id));
            }
        } catch (Exception e) {
            //修改规则执行状态
            LOGGER.error("update rule status error", e);
            throw new BusinessException(IftttExceptionEnum.UPDATE_RULE_STATUS_FAILED, e);
        }
        return true;
    }

    /**
     * 检测规则名
     *
     * @param ruleId
     * @param ruleName
     * @param userId
     * @return
     */
    public Boolean checkName(Long ruleId, String ruleName, Long userId) {
        LOGGER.info("=== receive checkName request ===" + "ruleId:" + ruleId + ",ruleName:" + ruleName + ",userId:" + userId);
        boolean flag = false;
        try {
            Long tenantId = getTenantId();
            List<Map<String, Object>> ruleList = ruleTobMapper.checkName(ruleName, userId, tenantId);
            if (!ruleList.isEmpty()) {
                for (Map<String, Object> ruleMap : ruleList) {
                    Long dbRuleId = (Long) ruleMap.get("id");
                    if (!dbRuleId.equals(ruleId)) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("ifttt check name error", e);
            throw new BusinessException(IftttExceptionEnum.CHECK_NAME_ERROR, e);
        }
        return flag;
    }

    /**
     * 根据 直连设备id 获取rule
     *
     * @param directDeviceId
     */
    public List<Rule> getByDirectDeviceId(String directDeviceId) {
        return ruleTobMapper.getByDirectDeviceId(directDeviceId);
    }

    public Integer getTimeZoneOffset(Long ruleId) {
        Integer offset = ruleTobMapper.getTimeZoneOffsetByRuleId(ruleId);
        if (offset == null) {
            offset = 0;
        }
        return offset;
    }

    /**
     * 删除模板下ifttt
     *
     * @param templateId
     * @return
     */
    public List<Long> selectRuleIdByTemplateId(Long templateId) {
        return ruleTobMapper.selectRuleIdByTemplateId(templateId);
    }

    /**
     * 根据类型获取列表
     *
     * @param type
     * @return
     */
    public List<Rule> selectByType(String type) {
        return ruleTobMapper.selectByType(type);
    }

    /**
     * 构建 rule.id 的列表
     *
     * @param ruleList
     * @return
     */
    public List<Long> buildRuleIdList(List<Rule> ruleList) {
        List<Long> idList = Lists.newArrayList();
        for (Rule rule : ruleList) {
            idList.add(rule.getId());
        }

        return idList;
    }

    public  List<RuleResp> getRuleListByName(SaveIftttReq ruleVO) {
        List<RuleResp> list = ruleTobMapper.getRuleListByName(ruleVO);
        return list;
    }

    public List<Rule> getRuleList(RuleListReq req) {
        return ruleTobMapper.list(req);
    }

    public boolean findTemplateListByName(SaveIftttTemplateReq iftttReq) {
        boolean flag = false;
        SaveIftttReq ruleVO = new SaveIftttReq();
        ruleVO.setTenantId(iftttReq.getTenantId());
        ruleVO.setName(iftttReq.getName());
        List<RuleResp> list = ruleMapper.getRuleListByName(iftttReq);
        if(CollectionUtils.isNotEmpty(list)){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }
}
