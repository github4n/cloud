package com.iot.boss.service.packagemanager.impl;

import com.alibaba.fastjson.JSON;
import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.service.packagemanager.IPackageRuleService;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.PackageApi;
import com.iot.control.packagemanager.api.TemplateRuleApi;
import com.iot.control.packagemanager.enums.PackageTypeEnum;
import com.iot.control.packagemanager.enums.SearchTypeEnum;
import com.iot.control.packagemanager.enums.TemplateRuleTypeEnum;
import com.iot.control.packagemanager.utils.StrategyToBeanUtil;
import com.iot.control.packagemanager.utils.StrategyUtil;
import com.iot.control.packagemanager.vo.req.SearchTemplateRuleReq;
import com.iot.control.packagemanager.vo.req.rule.*;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import com.iot.control.packagemanager.vo.resp.rule.RuleDetailInfoResp;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PackageRuleServiceImpl implements IPackageRuleService {

    @Autowired
    private TemplateRuleApi templateRuleApi;

    @Autowired
    private PackageApi packageApi;

    private static Long TENANT_ID = -1L;

    /** 策略最多可添加数量*/
    private final static int ruleMaxNum = 30;

    /** 安防策略只有3条 */
    private final static int securityRuleMaxNum = 3;

    @Override
    public int saveTemplateRule(SaveRuleInfoReq req) {
        // 套包id
        Long packageId = req.getPackageId();
        if (packageId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "packageId is null");
        }
        // 策略名称
        String ruleName = req.getRuleName();
        if(StringUtil.isBlank(ruleName)) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "ruleName is null");
        }
        // 获取该套包信息
        PackageResp packageResp = packageApi.getPackageById(packageId, SaaSContextHolder.currentTenantId());
        if (packageResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "package not exist");
        }
        // 讲获取的json参数转换成对象
        RuleDetailInfoReq ruleDetailInfoReq = req.getDetail();

        if(StrategyUtil.analysisStrategyJson(ruleDetailInfoReq, SearchTypeEnum.DEVICETYPE.getCode(), SaaSContextHolder.currentTenantId(), packageResp.getPackageType())) {
            if (PackageTypeEnum.SECURITY.getValue() == packageResp.getPackageType()) {
                SecurityInfoReq securityInfoReq = ruleDetailInfoReq.getSecurity();
                if (securityInfoReq == null) {
                    throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "rule security config null");
                }
                //安防套包只能添加3条策略，且策略类型不能重复
                SearchTemplateRuleReq searchTemplateRuleReq = new SearchTemplateRuleReq(packageResp.getId(), packageResp.getTenantId());
                List<TemplateRuleResp> templateRuleResps = templateRuleApi.getTemplateRuleBy(searchTemplateRuleReq);
                if (templateRuleResps != null && !templateRuleResps.isEmpty()) {
                    if (securityRuleMaxNum <= templateRuleResps.size()) {
                        throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "rule number greater than " + securityRuleMaxNum);
                    } else {
                        //校验策略类型是否有重复
                        List<String> ruleTypes = templateRuleResps.stream().map(rule -> {
                            RuleDetailInfoReq ruleDetail = JSON.parseObject(rule.getJson(), RuleDetailInfoReq.class);
                            return ruleDetail.getSecurity().getType();
                        }).collect(Collectors.toList());
                        String securityRuleType = securityInfoReq.getType();
                        if (ruleTypes.contains(securityRuleType)) {
                            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, securityRuleType + " rule had add，please refresh");
                        }
                    }
                }
            } else {
                int number = templateRuleApi.getTotalNumber(packageId, TENANT_ID);
                if (ruleMaxNum <= number) {
                    throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "rule number greater than " + ruleMaxNum);
                }
            }
            TemplateRuleReq trr = new TemplateRuleReq(req.getPackageId(), TENANT_ID, TemplateRuleTypeEnum.TYPE_STRATEGY.getCode(), JsonUtil.toJson(ruleDetailInfoReq), new Date(), null, req.getRuleName());
            templateRuleApi.insert(trr);
        }
        return 0;
    }

    @Override
    public int batchSaveTemplateRule(List<SaveRuleInfoReq> reqs) {
        if (reqs != null && reqs.size() > 0) {
            Long packageId = reqs.get(0).getPackageId();
            PackageResp packageResp = packageApi.getPackageById(packageId, SaaSContextHolder.currentTenantId());
            if (packageResp == null) {
                throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "package not exist");
            }
            List<String> hadAddRuleTypes = new ArrayList<>();
            if (PackageTypeEnum.SECURITY.getValue() == packageResp.getPackageType()) {
                //安防套包只能添加3条策略，且策略类型不能重复
                SearchTemplateRuleReq searchTemplateRuleReq = new SearchTemplateRuleReq(packageResp.getId(), packageResp.getTenantId());
                List<TemplateRuleResp> templateRuleResps = templateRuleApi.getTemplateRuleBy(searchTemplateRuleReq);
                if (templateRuleResps != null && !templateRuleResps.isEmpty()) {
                    if (securityRuleMaxNum < templateRuleResps.size()+reqs.size() ) {
                        throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "rule number greater than " + securityRuleMaxNum);
                    } else {
                        //已添加的策略类型
                        hadAddRuleTypes = templateRuleResps.stream().map(rule -> {
                            RuleDetailInfoReq ruleDetail = JSON.parseObject(rule.getJson(), RuleDetailInfoReq.class);
                            return ruleDetail.getSecurity().getType();
                        }).collect(Collectors.toList());
                    }
                }
            } else {
                int number = templateRuleApi.getTotalNumber(packageId, TENANT_ID);
                if (ruleMaxNum <= number) {
                    throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "rule number greater than " + ruleMaxNum);
                }
            }
            List<TemplateRuleReq> addList = new ArrayList<>();
            List<String> newRuleTypes = new ArrayList<>();
            for (SaveRuleInfoReq t : reqs){
                if (StringUtil.isEmpty(t.getRuleName())) {
                    throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "ruleName is null");
                }
                // 讲获取的json参数转换成对象
                RuleDetailInfoReq ruleDetailInfoReq = t.getDetail();
                // 判断当前套包名称是否为空
                if (t.getPackageId() == null) {
                    throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "packageId is null");
                }
                // 解析json，判断json是否存在错误
                if(StrategyUtil.analysisStrategyJson(ruleDetailInfoReq, SearchTypeEnum.DEVICETYPE.getCode(), SaaSContextHolder.currentTenantId(), packageResp.getPackageType())) {
                    //防止安防策略重复
                    String securityRuleType = ruleDetailInfoReq.getSecurity().getType();
                    if (hadAddRuleTypes.contains(securityRuleType) || newRuleTypes.contains(securityRuleType)) {
                        throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, securityRuleType + " rule had add，please refresh");
                    }
                    TemplateRuleReq trr = new TemplateRuleReq(t.getPackageId(), TENANT_ID, TemplateRuleTypeEnum.TYPE_STRATEGY.getCode(), JsonUtil.toJson(ruleDetailInfoReq), new Date(), null, t.getRuleName());
                    addList.add(trr);
                } else {
                    throw new BusinessException(BossExceptionEnum.PACKAGE_JSON_IS_ERROR);
                }
            }
            templateRuleApi.batchInsert(addList);
        }
        return 0;
    }

    @Override
    public int updateTemplateRuleById(SaveRuleInfoReq req) {
        if (StringUtil.isEmpty(req.getRuleName())) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "ruleName is null");
        }
        // 讲获取的json参数转换成对象
        RuleDetailInfoReq ruleDetailInfoReq = req.getDetail();
        // 判断当前策略id是否为空
        if (req.getTemplateRuleId() == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "ruleId is null");
        }
        PackageResp packageResp = packageApi.getPackageById(req.getPackageId(), SaaSContextHolder.currentTenantId());
        if (packageResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "package not exist");
        }
        // 解析json，判断json是否存在错误
        UpdateTemplateRuleReq trr = null;
        if (ruleDetailInfoReq == null) {
            throw new BusinessException(BossExceptionEnum.PACKAGE_JSON_IS_ERROR, "json is null");
        } else if(StrategyUtil.analysisStrategyJson(ruleDetailInfoReq, SearchTypeEnum.DEVICETYPE.getCode(), SaaSContextHolder.currentTenantId(), packageResp.getPackageType())) {
            trr = new UpdateTemplateRuleReq(req.getTemplateRuleId(), JsonUtil.toJson(ruleDetailInfoReq),new Date(), req.getRuleName());
        } else {
            throw new BusinessException(BossExceptionEnum.PACKAGE_JSON_IS_ERROR);
        }
        return templateRuleApi.updateTemplateRuleById(trr);
    }

    @Override
    public List<TemplateRuleInfoDetailResp> getTemplateRuleListByPackageId(Long packageId) {
        // 判断当前套包Id是否为空
        if (packageId == null) {
            throw new BusinessException(BossExceptionEnum.TEMPLATE_PARAM_IS_NULL, "packageId is null");
        }
        // 策略详情列表
        List<TemplateRuleInfoDetailResp> result = new ArrayList<>();

        SearchTemplateRuleReq searchTemplateRuleReq = new SearchTemplateRuleReq();
        searchTemplateRuleReq.setPackageId(packageId);
        searchTemplateRuleReq.setTenantId(TENANT_ID);
        // 获取策略信息
        List<TemplateRuleResp> resultList = templateRuleApi.getTemplateRuleBy(searchTemplateRuleReq);
        boolean first = true;
        // 返回第一条策略的详细，其他只返回策略名称即可
        if (resultList != null && resultList.size() > 0) {
            for (TemplateRuleResp t : resultList) {
                if (first) {
                    RuleDetailInfoResp ruleDetailInfoResp = StrategyToBeanUtil.strategyJsonToBean(t.getJson(), SearchTypeEnum.DEVICETYPE.getCode());
                    TemplateRuleInfoDetailResp resp = new TemplateRuleInfoDetailResp(t.getId(), t.getRuleName(), ruleDetailInfoResp);
                    result.add(resp);
                    first = false;
                } else {
                    TemplateRuleInfoDetailResp resp = new TemplateRuleInfoDetailResp(t.getId(), t.getRuleName(), null);
                    result.add(resp);
                }
            }
        }
        return result;
    }

    @Override
    public int deleteTemplateRuleById(Long id) {
        if (id == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "packageId is null");
        }
        boolean result = templateRuleApi.deleteById(id);
        if (result) {
            return 1;
        }
        return 0;
    }

    /**
     *@description 根据策略id获取该条策略详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:21
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp
     */
    @Override
    public TemplateRuleInfoDetailResp getTemplateRuleDetailInfoById(Long id) {
        TemplateRuleInfoDetailResp templateRuleInfoDetailResp = null;
        TemplateRuleResp templateRuleResp = templateRuleApi.getTemplateRuleById(id, SaaSContextHolder.currentTenantId());
        if (templateRuleResp != null) {
            templateRuleInfoDetailResp = new TemplateRuleInfoDetailResp();
            BeanUtil.copyProperties(templateRuleResp, templateRuleInfoDetailResp);
            RuleDetailInfoResp newRuleDetailInfoResp = StrategyToBeanUtil.strategyJsonToBean(templateRuleResp.getJson(), SearchTypeEnum.DEVICETYPE.getCode());
            templateRuleInfoDetailResp.setRuleDetailInfoResp(newRuleDetailInfoResp);
        }
        return templateRuleInfoDetailResp;
    }
}
