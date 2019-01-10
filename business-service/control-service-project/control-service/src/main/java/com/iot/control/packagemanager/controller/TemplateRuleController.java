package com.iot.control.packagemanager.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.api.TemplateRuleApi;
import com.iot.control.packagemanager.entity.TemplateRule;
import com.iot.control.packagemanager.enums.TemplateRuleTypeEnum;
import com.iot.control.packagemanager.exception.TemplateRuleExceptionEnum;
import com.iot.control.packagemanager.service.ITemplateRuleService;
import com.iot.control.packagemanager.vo.req.SearchTemplateRuleReq;
import com.iot.control.packagemanager.vo.req.rule.TemplateRuleReq;
import com.iot.control.packagemanager.vo.req.rule.UpdateTemplateRuleReq;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class TemplateRuleController implements TemplateRuleApi{

    @Autowired
    private ITemplateRuleService templateRuleService;

    @Override
    public boolean insert(@RequestBody  TemplateRuleReq req) {
        TemplateRule entity = new TemplateRule();
        BeanUtil.copyProperties(req, entity);
        return templateRuleService.insert(entity);
    }

    @Override
    public boolean batchInsert(@RequestBody  List<TemplateRuleReq> req) {
        List<TemplateRule> templateRules = new ArrayList<>();
        if (req != null && req.size() > 0) {
            for (TemplateRuleReq trr : req) {
                TemplateRule entity = new TemplateRule();
                BeanUtil.copyProperties(trr, entity);
                templateRules.add(entity);
            }
            return templateRuleService.insertBatch(templateRules);
        } else {
            throw new BusinessException(TemplateRuleExceptionEnum.PARAM_IS_NULL);
        }
    }

    @Override
    public boolean deleteByIds(@RequestParam("ids") List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            return templateRuleService.deleteBatchIds(ids);
        } else {
            throw new BusinessException(TemplateRuleExceptionEnum.PARAM_IS_NULL);
        }
    }

    @Override
    public boolean deleteById(@RequestParam("id") Long id) {
        if (id != null) {
            return templateRuleService.deleteById(id);
        } else {
            throw new BusinessException(TemplateRuleExceptionEnum.PARAM_IS_NULL);
        }
    }

    @Override
    public List<TemplateRuleResp> getTemplateRuleBy(@RequestBody SearchTemplateRuleReq req) {
        List<TemplateRuleResp> result = new ArrayList<>();
        EntityWrapper<TemplateRule> ew = new EntityWrapper<>();
        if (req.getPackageId() != null) {
            ew.eq("package_id", req.getPackageId());
        }
        if (req.getTenantId() != null) {
            ew.eq("tenant_id", req.getTenantId());
        }

        // 设定类型为策略
        ew.eq("type", TemplateRuleTypeEnum.TYPE_STRATEGY.getCode());

        if (StringUtil.isNotBlank(req.getRuleName())) {
            ew.like("rule_name", req.getRuleName());
        }
        ew.orderDesc(Arrays.asList("id"));
        List<TemplateRule> templateRuleList =  templateRuleService.selectList(ew);
        for (TemplateRule tr : templateRuleList) {
            TemplateRuleResp trr = new TemplateRuleResp();
            BeanUtil.copyProperties(tr, trr);
            result.add(trr);
        }
        return result;
    }

    @Override
    public int getTotalNumber(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId) {
        return templateRuleService.getTotalNumber(packageId, tenantId);
    }

    /**
     *@description 修改策略的json、ruleName、updateTime
     *@author wucheng
     *@params [t]
     *@create 2018/12/11 15:41
     *@return int
     */
    @Override
    public int updateTemplateRuleById(@RequestBody UpdateTemplateRuleReq t) {
        return templateRuleService.updateTemplateRuleById(t);
    }

    /**
     * @despriction：通过id获取策略信息
     * @author  yeshiyuan
     * @created 2018/12/11 13:46
     */
    @Override
    public TemplateRuleResp getRuleInfoById(@RequestParam("id") Long id) {
        return templateRuleService.getRuleInfoById(id);
    }
    /**
     *@description 根据策略id和租户id获取策略信息
     *@author wucheng
     *@params [id, tenantId]
     *@create 2018/12/12 16:16
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleResp
     */
    @Override
    public TemplateRuleResp getTemplateRuleById(@RequestParam("id") Long id, @RequestParam("tenantId") Long tenantId) {
        return templateRuleService.getTemplateRuleById(id, tenantId);
    }
}
