package com.iot.control.ifttt.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.ifttt.constant.IftttConstants;
import com.iot.control.ifttt.entity.Rule;
import com.iot.control.ifttt.exception.IftttExceptionEnum;
import com.iot.control.ifttt.mapper.RuleMapper;
import com.iot.control.ifttt.util.BeanCopyUtil;
import com.iot.control.ifttt.util.RedisKeyUtil;
import com.iot.control.ifttt.util.TimeUtil;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.RuleListReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 描述：Rule业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:00
 */
@Service
public class RuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);
    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserApi userApi;


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
        SaaSContextHolder.setCurrentTenantId(0l);
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
            List<Rule> tempList = ruleMapper.findSimpleList(req);
            if (CollectionUtils.isNotEmpty(tempList)) {
                //LOGGER.debug("list(), 从数据库查询出的 tempList.size()={}", tempList.size());
                List<Long> ruleIdList = buildRuleIdList(tempList);
                List<Rule> ruleList = listRuleRespByIds(ruleIdList);

                if (CollectionUtils.isNotEmpty(ruleList)) {
                    for(Rule rule:ruleList){
                        RuleResp resp = getRuleResp(rule, false);
                        /*if (rule.getUserId() != null && userApi.getUser(rule.getUserId()) != null) {
                            resp.setUserName(userApi.getUser(rule.getUserId()).getUserName());
                        }*/
                        if(resp.getCreateTime()!=null){
                            resultList.add(resp);
                        }
                    }

                    // 排序
                    /*Collections.sort(resultList, new Comparator<RuleResp>() {
                        @Override
                        public int compare(RuleResp b1, RuleResp b2) {
                            return b2.getCreateTime().compareTo(b1.getCreateTime());
                        }
                    });*/
                }
            } else {
                LOGGER.debug("list(), 从数据库查询出的 tempList is empty.");
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
     * 获取缓存对象
     *
     * @param id
     * @return
     */
    public Rule getCache(Long id) {
        String key = RedisKeyUtil.getIftttRuleKey(id);
        Rule rule = RedisCacheUtil.valueObjGet(key, Rule.class);
        if (rule == null) {
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
}
