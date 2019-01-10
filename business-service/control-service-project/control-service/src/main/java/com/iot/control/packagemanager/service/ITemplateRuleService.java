package com.iot.control.packagemanager.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.control.packagemanager.entity.TemplateRule;
import com.iot.control.packagemanager.vo.req.rule.UpdateTemplateRuleReq;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import org.apache.ibatis.annotations.Param;

public interface ITemplateRuleService extends IService<TemplateRule>{
    /**
     *@description 根据套包id和租户id，获取当前套包绑定的策略数量
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/7 15:21
     *@return int
     */
    int getTotalNumber(Long packageId,  Long tenantId);

    /**
     *@description 修改策略的json、ruleName、updateTime
     *@author wucheng
     *@params [t]
     *@create 2018/12/11 15:41
     *@return int
     */
    int updateTemplateRuleById(UpdateTemplateRuleReq t);

    /**
     * @despriction：通过id获取策略信息
     * @author  yeshiyuan
     * @created 2018/12/11 13:46
     */
    TemplateRuleResp getRuleInfoById(Long id);
    /**
     *@description
     *@author wucheng
     *@params [id, tenantId]
     *@create 2018/12/12 16:06
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleResp
     */
    TemplateRuleResp getTemplateRuleById(Long id, Long tenantId);

}
