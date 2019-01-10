package com.iot.ifttt.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.BusinessExceptionEnum;
import com.iot.common.Service.CommonStringUtil;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.AuthorizeShareRequired;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.enums.PermissionEnum;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.ifttt.constant.IftttConstants;
import com.iot.ifttt.vo.ListReq;
import com.iot.ifttt.vo.RuleListResp;
import com.iot.ifttt.vo.RuleReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.ifttt.api.AutoApi;
import com.iot.shcs.ifttt.vo.req.AddAutoRuleReq;
import com.iot.shcs.ifttt.vo.req.AutoListReq;
import com.iot.shcs.ifttt.vo.req.SaveAutoReq;
import com.iot.shcs.ifttt.vo.res.AutoListResp;
import com.iot.user.api.UserApi;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "IFTTT接口")
@RestController
@RequestMapping("/automation")
public class IftttController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IftttController.class);

    @Autowired
    private AutoApi iftttApi;

    @Autowired
    private UserApi userApi;

    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value = PermissionEnum.AUTO_LIST)
    @ApiOperation(value = "获取Automation列表", notes = "获取Automation列表")
    @RequestMapping(value = "/getAutoList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Page<RuleListResp>> list(@RequestBody ListReq req) {
        LOGGER.info("获取Automation列表请求，req=" + req.toString());
        AssertUtils.notNull(req.getHomeId(), "homeId.notnull");
        CommonResponse<Page<RuleListResp>> result = null;
        try {
            Long userId = SaaSContextHolder.getCurrentUserId();
            AutoListReq autoListReq = new AutoListReq();
            autoListReq.setUserId(userId);
            if (req.getHomeId() != null) {
                autoListReq.setSpaceId(Long.parseLong(req.getHomeId()));
            }
            autoListReq.setTenantId(SaaSContextHolder.currentTenantId());

            List<AutoListResp> list = iftttApi.list(autoListReq);
            Page<RuleListResp> resPage = new Page<RuleListResp>();
            if (CollectionUtils.isNotEmpty(list)) {
                List<RuleListResp> resList = Lists.newArrayList();
                for (AutoListResp vo : list) {
                    RuleListResp rule = new RuleListResp();
                    rule.setAutoId(vo.getId().toString());
                    rule.setIcon(vo.getIcon());
                    rule.setName(vo.getName());
                    rule.setType(vo.getType());
                    rule.setDevSceneId(vo.getDevSceneId());
                    rule.setDevTimerId(vo.getDevTimerId());
                    //获取时间格式
                    rule = setAutoTimeFormat(rule, vo.getType(), vo.getTimeJson());
                    Integer enable = 0;
                    if (vo.getStatus() == 1) {
                        enable = 1;
                    }
                    rule.setEnable(enable);
                    resList.add(rule);
                }
                resPage.setResult(resList);
            }

            result = new CommonResponse<Page<RuleListResp>>(ResultMsg.SUCCESS, resPage);
        } catch (BusinessException e) {
            e.printStackTrace();
            LOGGER.error("获取Automation列表失败!", e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
        return result;
    }


    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "添加Automation", notes = "添加Automation")
    @RequestMapping(value = "/addAuto", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Map<String, Object>> save(@RequestBody RuleReq req) {
        LOGGER.info("收到添加Automation请求：" + req.toString());
        CommonResponse<Map<String, Object>> result = null;
        //校验名称字符长度
        String language = LocaleContextHolder.getLocale().toString();
        String name = req.getName();
        if ("zh_CN".equals(language)) {
            CommonStringUtil.checkStringParam(name, 0, 30, language);
        } else {
            CommonStringUtil.checkStringParam(name, 0, 30, language);
        }
        try {
            //赋值
            SaveAutoReq saveAutoReq = getAutoReq(req);
            saveAutoReq.setIsMulti(0);
            Long autoId = iftttApi.saveAuto(saveAutoReq);
            if (autoId == null || autoId == 0) {
                throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("autoId", autoId.toString());
            result = new CommonResponse<Map<String, Object>>(ResultMsg.SUCCESS, resultMap);
        } catch (BusinessException e) {
            LOGGER.error("添加Automation失败!", e);
            throw e;
        }
        return result;
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "编辑Automation", notes = "编辑Automation")
    @RequestMapping(value = "/editAuto", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<String> edit(@RequestBody RuleReq req) {
        LOGGER.info("收到编辑Automation请求：" + req.toString());
        CommonResponse<String> result = null;
        //校验名称字符长度
        String language = LocaleContextHolder.getLocale().toString();
        String name = req.getName();
        if ("zh_CN".equals(language)) {
            CommonStringUtil.checkStringParam(name, 0, 30, language);
        } else {
            CommonStringUtil.checkStringParam(name, 0, 30, language);
        }
        try {
            Long ruleId = Long.parseLong(req.getAutoId());
            Long userId = SaaSContextHolder.getCurrentUserId();
            AssertUtils.notEmpty(userId, "userId.is.null");

            //赋值
            SaveAutoReq saveAutoReq = getAutoReq(req);
            saveAutoReq.setId(ruleId);
            iftttApi.saveAuto(saveAutoReq);
            result = new CommonResponse<String>(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            LOGGER.error("编辑Automation失败!", e);
            throw e;
        }
        return result;
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取墙控情景数据", notes = "获取墙控情景数据")
    @RequestMapping(value = "/getSceneListByDevId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<List<Map<String, Object>>> getSceneListByDevId(@RequestParam("devId") String devId) {
        LOGGER.info("收到获取墙控情景数据请求：" + devId);
        CommonResponse<List<Map<String, Object>>> result = null;
        try {
            AssertUtils.notEmpty(devId, "devId.is.null");
            //赋值
            List<Map<String, Object>> resList = iftttApi.getSceneListByDevId(devId);
            result = new CommonResponse<List<Map<String, Object>>>(ResultMsg.SUCCESS, resList);
        } catch (BusinessException e) {
            LOGGER.error("获取墙控情景数据失败!", e);
            throw e;
        }
        return result;
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "删除Automation", notes = "删除Automation")
    @RequestMapping(value = "/delAuto", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CommonResponse delAuto(@RequestBody RuleReq req) {
        LOGGER.info("收到删除Automation请求：" + req.toString());
        AssertUtils.notNull(req, "auto.notnull");
        AssertUtils.notNull(req.getAutoId(), "autoId.notnull");

        try {
            Long autoId = Long.valueOf(req.getAutoId());
            SaveAutoReq saveAutoReq = new SaveAutoReq();
            saveAutoReq.setId(autoId);
            saveAutoReq.setTenantId(SaaSContextHolder.currentTenantId());
            saveAutoReq.setUserId(SaaSContextHolder.getCurrentUserId());
            iftttApi.delBleAuto(saveAutoReq);
            return new CommonResponse(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            LOGGER.error("删除Automation失败!", e);
            throw e;
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取Automation规则")
    @RequestMapping(value = "/getAutoRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CommonResponse<Map<String, Object>> getAutoRule(@RequestBody RuleReq req) {
        LOGGER.info("收到获取Automation规则请求：" + req.toString());
        AssertUtils.notNull(req, "auto.notnull");
        AssertUtils.notNull(req.getAutoId(), "autoId.notnull");

        try {
            Long autoId = Long.valueOf(req.getAutoId());
            SaveAutoReq saveAutoReq = new SaveAutoReq();
            saveAutoReq.setId(autoId);
            saveAutoReq.setTenantId(SaaSContextHolder.currentTenantId());
            saveAutoReq.setUserId(SaaSContextHolder.getCurrentUserId());

            return new CommonResponse(ResultMsg.SUCCESS, iftttApi.getAutoDetail(saveAutoReq));
        } catch (BusinessException e) {
            LOGGER.error("删除Automation失败!", e);
            throw e;
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "添加Automation规则")
    @RequestMapping(value = "/addAutoRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CommonResponse addAutoRule(@RequestBody AddAutoRuleReq req) {
        LOGGER.info("收到添加Automation规则请求：" + req.toString());
        AssertUtils.notNull(req, "auto.notnull");
        AssertUtils.notNull(req.getAutoId(), "autoId.notnull");

        try {
            req.setTenantId(SaaSContextHolder.currentTenantId());
            req.setUserId(SaaSContextHolder.getCurrentUserId());
            Map<String, Object> payload = changeAutoReq(req);
            req.setPayload(payload);
            iftttApi.addAutoRule(req);
            return new CommonResponse(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            LOGGER.error("删除Automation失败!", e);
            throw e;
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "编辑Automation规则")
    @RequestMapping(value = "/editAutoRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CommonResponse editAutoRule(@RequestBody AddAutoRuleReq req) {
        LOGGER.info("收到添加Automation规则请求：" + req.toString());
        AssertUtils.notNull(req, "auto.notnull");
        AssertUtils.notNull(req.getAutoId(), "autoId.notnull");

        try {
            req.setTenantId(SaaSContextHolder.currentTenantId());
            req.setUserId(SaaSContextHolder.getCurrentUserId());
            Map<String, Object> payload = changeAutoReq(req);
            req.setPayload(payload);
            iftttApi.editAutoRule(req);
            return new CommonResponse(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            LOGGER.error("删除Automation失败!", e);
            throw e;
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "编辑Automation规则")
    @RequestMapping(value = "/setAutoEnable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CommonResponse setAutoEnable(@RequestBody AddAutoRuleReq req) {
        LOGGER.info("收到添加Automation规则请求：" + req.toString());
        AssertUtils.notNull(req, "auto.notnull");
        AssertUtils.notNull(req.getAutoId(), "autoId.notnull");

        try {
            req.setTenantId(SaaSContextHolder.currentTenantId());
            req.setUserId(SaaSContextHolder.getCurrentUserId());
            req.setUserUuid(SaaSContextHolder.getCurrentUserUuid());
            Map<String, Object> payload = changeAutoReq(req);
            req.setPayload(payload);
            iftttApi.setAutoEnable(req);
            return new CommonResponse(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            LOGGER.error("删除Automation失败!", e);
            throw e;
        }
    }

    private Map<String, Object> changeAutoReq(AddAutoRuleReq req) {
        Map<String, Object> payload = new HashMap<>();
        if (req.getAutoId() != null) {
            payload.put("autoId", req.getAutoId());
        }
        if (req.getEnable() != null) {
            payload.put("enable", req.getEnable());
        }
        if (req.getEnableDelay() != null) {
            payload.put("enableDelay", req.getEnableDelay());
        }
        if (req.getIfParam() != null) {
            payload.put("if", req.getIfParam());
        }
        if (CollectionUtils.isNotEmpty(req.getThen())) {
            payload.put("then", req.getThen());
        }
        return payload;
    }

    /**
     * 获取请求对象
     *
     * @param req
     * @return
     */
    private SaveAutoReq getAutoReq(RuleReq req) {
        SaveAutoReq saveAutoReq = new SaveAutoReq();
        saveAutoReq.setSpaceId(Long.parseLong(req.getHomeId()));
        saveAutoReq.setUserId(SaaSContextHolder.getCurrentUserId());
        int status = req.getEnable() == 1 ? 1 : 0;
        saveAutoReq.setStatus(status);
        saveAutoReq.setName(req.getName());
        saveAutoReq.setIcon(req.getIcon());
        saveAutoReq.setTriggerType(req.getType());
        saveAutoReq.setTenantId(SaaSContextHolder.currentTenantId());
        saveAutoReq.setDevSceneId(req.getDevSceneId());
        saveAutoReq.setDevTimerId(req.getDevTimerId());
        saveAutoReq.setVisiable(req.getVisiable());
        return saveAutoReq;
    }

    /**
     * 赋值时间信息
     *
     * @param ruleResp
     * @param timeJson
     * @return
     */
    private RuleListResp setAutoTimeFormat(RuleListResp ruleResp, String type, String timeJson) {
        if (StringUtil.isEmpty(timeJson) || "null".equals(timeJson)) {
            return ruleResp;
        }

        if (type.equals(IftttConstants.IFTTT_TRIGGER_DEVICE)) {
            //4:20 AM-3:20 PM
            //Mon,Tue,Wed,Thu,Fri,Sat,Sun
            Map validMap = JSON.parseObject(timeJson, Map.class);
            String begin = (String) validMap.get("begin");
            String end = (String) validMap.get("end");
            List<Integer> week = (List<Integer>) validMap.get("week");
            begin = getTime(begin);
            end = getTime(end);
            String time = begin + "-" + end;
            ruleResp.setExecuteTime(time);
            ruleResp.setExecuteDate(getWeeks(week));
        } else if (type.equals(IftttConstants.IFTTT_TRIGGER_TIMER)) {
            Map map = JSON.parseObject(timeJson, Map.class);
            String at = (String) map.get("at");
            List<Integer> week = (List<Integer>) map.get("repeat");
            ruleResp.setExecuteTime(getTime(at));
            ruleResp.setExecuteDate(getWeeks(week));
        } else if (type.equals(IftttConstants.IFTTT_TRIGGER_SUNRISE) ||
                type.equals(IftttConstants.IFTTT_TRIGGER_SUNSET)) {
            Map map = JSON.parseObject(timeJson, Map.class);
            String trigType = (String) map.get("trigType");
            Integer intervalType = Integer.valueOf(map.get("intervalType").toString());
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
            ruleResp.setExecuteDate(getWeeks(week));
        }
        return ruleResp;
    }

    /**
     * 获取时间格式
     *
     * @param time
     * @return
     */
    private String getTime(String time) {
        String[] times = time.split(":");
        int h = Integer.parseInt(times[0]);
        int m = Integer.parseInt(times[1]);
        boolean pm_flag = false;
        if (h >= 12) {
            pm_flag = true;
            h = h - 12;
        }
        String h_str = h + "";
        if (h < 10) {
            h_str = "0" + h;
        }
        String m_str = m + "";
        if (m < 10) {
            m_str = "0" + m;
        }

        String newTime = h_str + ":" + m_str;
        if (pm_flag) {
            newTime += " PM";
        } else {
            newTime += " AM";
        }
        return newTime;
    }

    /**
     * 获取周格式
     *
     * @param week
     * @return
     */
    private String getWeeks(List<Integer> week) {
        String weeks = "";
        if (week.size() == 7) {
            weeks = "Every day";
            return weeks;
        }

        for (Integer w : week) {
            switch (w) {
                case 0:
                    weeks += "Sun,";
                    break;
                case 1:
                    weeks += "Mon,";
                    break;
                case 2:
                    weeks += "Tue,";
                    break;
                case 3:
                    weeks += "Wed,";
                    break;
                case 4:
                    weeks += "Thu,";
                    break;
                case 5:
                    weeks += "Fri,";
                    break;
                case 6:
                    weeks += "Sat,";
                    break;
                default:
                    break;
            }
        }
        if (!"".equals(weeks)) {
            weeks = weeks.substring(0, weeks.length() - 1);
        }
        return weeks;
    }
}
