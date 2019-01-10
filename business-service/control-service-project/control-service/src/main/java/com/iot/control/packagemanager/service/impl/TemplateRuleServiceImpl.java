package com.iot.control.packagemanager.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.beans.BeanUtil;
import com.iot.control.packagemanager.entity.TemplateRule;
import com.iot.control.packagemanager.mapper.TemplateRuleMapper;
import com.iot.control.packagemanager.service.ITemplateRuleService;
import com.iot.control.packagemanager.vo.req.rule.UpdateTemplateRuleReq;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TemplateRuleServiceImpl extends ServiceImpl<TemplateRuleMapper, TemplateRule> implements ITemplateRuleService {
    @Autowired
    private TemplateRuleMapper templateRuleMapper;
    @Override
    public int getTotalNumber(Long packageId, Long tenantId) {
        return templateRuleMapper.getTotalNumber(packageId, tenantId);
    }

    /**
     *@description 修改策略的json、ruleName、updateTime
     *@author wucheng
     *@params [t]
     *@create 2018/12/11 15:41
     *@return int
     */
    @Override
    public int updateTemplateRuleById(UpdateTemplateRuleReq t) {
        return templateRuleMapper.updateTemplateRuleById(t);
    }

    /**
     * @despriction：通过id获取策略信息
     * @author  yeshiyuan
     * @created 2018/12/11 13:46
     */
    @Override
    public TemplateRuleResp getRuleInfoById(Long id) {
        TemplateRuleResp templateRuleResp = null;
        TemplateRule templateRule = templateRuleMapper.selectById(id);
        if (templateRule!=null) {
            templateRuleResp = new TemplateRuleResp();
            BeanUtil.copyProperties(templateRule, templateRuleResp);
        }
        return templateRuleResp;
    }
  
    /**
     *@description 根据策略id和租户tenantId获取策略
     *@author wucheng
     *@params [id, tenantId]
     *@create 2018/12/12 16:08
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleResp
     */
    @Override
    public TemplateRuleResp getTemplateRuleById(Long id, Long tenantId) {
        return templateRuleMapper.getTemplateRuleById(id, tenantId);
    }
}
