package com.iot.portal.packagemanager.service;

import com.iot.control.packagemanager.vo.req.rule.SaveRuleInfoReq;
import com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import com.iot.portal.packagemanager.vo.resp.RuleListResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  * @despriction：套包策略、场景service
  * @author  yeshiyuan
  * @created 2018/11/24 9:57
  */
public interface IPackageRuleService {

    /**
      * @despriction：保存策略
      * @author  yeshiyuan
      * @created 2018/12/10 20:00
      */
    void saveRule(SaveRuleInfoReq saveRuleInfoReq);

    /**
      * @despriction：删除策略
      * @author  yeshiyuan
      * @created 2018/12/11 14:43
      */
    void delete(Long ruleId);

    /**
     *@description 根据套包id、租户id获取当前用户套包下的策略列表
     *@author wucheng
     *@params [tenantId]
     *@create 2018/12/12 9:23
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.TemplateRuleResp>
     */
    List<RuleListResp> getRuleList(Long packageId);

    /**
     *@description 根据策略id，获取该策略的详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:04
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp
     */
    TemplateRuleInfoDetailResp getTemplateRuleDetailInfoById(Long id);

    /**
     *@description 据套包id，获取其绑定的策略信息
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/12 14:43
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp>
     */
   /* List<TemplateRuleInfoDetailResp> getTemplateRuleByPackageId(Long packageId);*/
}
