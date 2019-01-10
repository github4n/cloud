package com.iot.portal.packagemanager.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.iot.control.packagemanager.vo.req.rule.RuleDetailInfoReq;
import com.iot.control.packagemanager.vo.req.rule.SaveRuleInfoReq;
import com.iot.control.packagemanager.vo.req.rule.TemplateRuleReq;
import com.iot.control.packagemanager.vo.req.rule.UpdateTemplateRuleReq;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import com.iot.control.packagemanager.vo.resp.rule.RuleDetailInfoResp;
import com.iot.portal.exception.PackageExceptionEnum;
import com.iot.portal.packagemanager.service.IPackageRuleService;
import com.iot.portal.packagemanager.vo.resp.RuleListResp;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @despriction：套包策略service
 * @author  yeshiyuan
 * @created 2018/12/7 10:43
 */
@Service
public class PackageRuleServiceImpl implements IPackageRuleService{

    @Autowired
    private TemplateRuleApi templateRuleApi;

    @Autowired
    private PackageApi packageApi;

    /** 策略最多可添加数量*/
    private final static int ruleMaxNum = 30;

    /** 安防策略只有3条 */
    private final static int securityRuleMaxNum = 3;

    @Override
    public void saveRule(SaveRuleInfoReq saveReq) {
        SaveRuleInfoReq.chechParam(saveReq);
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long packageId = saveReq.getPackageId();
        PackageResp packageResp = packageApi.getPackageById(packageId, tenantId);
        if (packageResp == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_EXIST);
        } else if (!tenantId.equals(packageResp.getTenantId())) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_TENANTID_ERROR);
        }
        RuleDetailInfoReq ruleDetailInfoReq = saveReq.getDetail();
        StrategyUtil.analysisStrategyJson(ruleDetailInfoReq,SearchTypeEnum.PRODUCT.getCode(), SaaSContextHolder.currentTenantId(), packageResp.getPackageType());
        if (saveReq.getTemplateRuleId() != null) {
            //update
            TemplateRuleResp templateRuleResp = templateRuleApi.getRuleInfoById(saveReq.getTemplateRuleId());
            if (templateRuleResp == null) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_RULE_NOT_EXIST);
            } else if (!templateRuleResp.getPackageId().equals(packageId)) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_EXIST);
            }
            UpdateTemplateRuleReq updateTemplateRuleReq = new UpdateTemplateRuleReq(saveReq.getTemplateRuleId(), JsonUtil.toJson(saveReq.getDetail()), new Date(), saveReq.getRuleName());
            templateRuleApi.updateTemplateRuleById(updateTemplateRuleReq);
        } else {
            //插入
            if (PackageTypeEnum.SECURITY.getValue() == packageResp.getPackageType()) {
                //安防套包只能添加3条策略，且策略类型不能重复
                SearchTemplateRuleReq searchTemplateRuleReq = new SearchTemplateRuleReq(packageResp.getId(), packageResp.getTenantId());
                List<TemplateRuleResp> templateRuleResps = templateRuleApi.getTemplateRuleBy(searchTemplateRuleReq);
                if (templateRuleResps != null && !templateRuleResps.isEmpty()) {
                    if (securityRuleMaxNum <= templateRuleResps.size()) {
                        throw new BusinessException(PackageExceptionEnum.PACKAGE_RULE_NUM_ERROR, securityRuleMaxNum);
                    } else {
                        //校验策略类型是否有重复
                        List<String> ruleTypes = templateRuleResps.stream().map(rule -> {
                            RuleDetailInfoReq ruleDetail = JSON.parseObject(rule.getJson(), RuleDetailInfoReq.class);
                            return ruleDetail.getSecurity().getType();
                        }).collect(Collectors.toList());
                        String securityRuleType = saveReq.getDetail().getSecurity().getType();
                        if (ruleTypes.contains(securityRuleType)) {
                            throw new BusinessException(PackageExceptionEnum.PACKAGE_RULE_TYPE_EXIST, securityRuleType);
                        }
                    }
                }
            } else {
                int number = templateRuleApi.getTotalNumber(packageId, tenantId);
                if (ruleMaxNum <= number) {
                    throw new BusinessException(PackageExceptionEnum.PACKAGE_RULE_NUM_ERROR, ruleMaxNum);
                }
            }
            TemplateRuleReq templateRuleReq = new TemplateRuleReq(packageId, tenantId, TemplateRuleTypeEnum.TYPE_STRATEGY.getCode(), JsonUtil.toJson(saveReq.getDetail()), new Date(), new Date(), saveReq.getRuleName());
            templateRuleApi.insert(templateRuleReq);
        }
    }

    /**
     * @despriction：删除策略
     * @author  yeshiyuan
     * @created 2018/12/11 14:43
     */
    @Override
    public void delete(Long ruleId) {
        TemplateRuleResp templateRuleResp = templateRuleApi.getRuleInfoById(ruleId);
        if (templateRuleResp == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_RULE_NOT_EXIST);
        } else if (!templateRuleResp.getTenantId().equals(SaaSContextHolder.currentTenantId())) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_TENANTID_ERROR);
        }
        templateRuleApi.deleteById(ruleId);
    }

    /**
     *@description 根据套包id、租户id获取当前用户套包下的策略列表
     *@author wucheng
     *@params [tenantId]
     *@create 2018/12/12 9:23
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.TemplateRuleResp>
     */
    @Override
    public List<RuleListResp> getRuleList(Long packageId) {
        List<RuleListResp> ruleList = new ArrayList<>();
        SearchTemplateRuleReq searchTemplateRuleReq = new SearchTemplateRuleReq();
        searchTemplateRuleReq.setTenantId(SaaSContextHolder.currentTenantId());
        searchTemplateRuleReq.setPackageId(packageId);
        List<TemplateRuleResp> templateRuleRespList = templateRuleApi.getTemplateRuleBy(searchTemplateRuleReq);
        if (templateRuleRespList != null && !templateRuleRespList.isEmpty()) {
            boolean first = true;
            templateRuleRespList.forEach(rule -> {
                RuleListResp resp = new RuleListResp(rule.getId(), rule.getRuleName());
                if (first) {
                    RuleDetailInfoResp ruleDetailInfoResp = StrategyToBeanUtil.strategyJsonToBean(rule.getJson(), SearchTypeEnum.PRODUCT.getCode());
                    resp.setRuleDetail(ruleDetailInfoResp);
                }
                ruleList.add(resp);
            });
        }
        return ruleList;
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
            String json = templateRuleResp.getJson();
            if (StringUtil.isNotBlank(json)) {
                templateRuleInfoDetailResp = new TemplateRuleInfoDetailResp();
                BeanUtil.copyProperties(templateRuleResp, templateRuleInfoDetailResp);
                RuleDetailInfoResp newRuleDetailInfoResp = StrategyToBeanUtil.strategyJsonToBean(json, SearchTypeEnum.PRODUCT.getCode());
                templateRuleInfoDetailResp.setRuleDetailInfoResp(newRuleDetailInfoResp);
            }
        }
        return templateRuleInfoDetailResp;
    }

    /*@Override
    public List<TemplateRuleInfoDetailResp> getTemplateRuleByPackageId(Long packageId) {
        // 判断当前套包Id是否为空
        if (packageId == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PARAM_NULL, "packageId is null");
        }
        // 策略详情列表
        List<TemplateRuleInfoDetailResp> result = new ArrayList<>();

        SearchTemplateRuleReq searchTemplateRuleReq = new SearchTemplateRuleReq();
        searchTemplateRuleReq.setPackageId(packageId);
        searchTemplateRuleReq.setTenantId(SaaSContextHolder.currentTenantId());
        // 获取策略信息
        List<TemplateRuleResp> resultList = templateRuleApi.getTemplateRuleBy(searchTemplateRuleReq);
        boolean first = true;
        // 返回第一条策略的详细，其他只返回策略名称即可
        if (resultList != null && resultList.size() > 0) {
            for (TemplateRuleResp t : resultList) {
                if (first) {
                    RuleDetailInfoResp ruleDetailInfoResp = StrategyToBeanUtil.strategyJsonToBean(t.getJson(), SearchTypeEnum.PRODUCT.getCode());
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
    }*/
}
