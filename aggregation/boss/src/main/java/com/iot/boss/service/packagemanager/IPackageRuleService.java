package com.iot.boss.service.packagemanager;

import com.iot.control.packagemanager.vo.req.rule.SaveRuleInfoReq;
import com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp;

import java.util.List;

public interface IPackageRuleService {
    /**
     *@description 保存套包策略
     *@author wucheng
     *@params [req]
     *@create 2018/12/11 14:53
     *@return int
     */
    int saveTemplateRule(SaveRuleInfoReq req);
    /**
     * @return void
     * @description 批量保存策略
     * @author wucheng
     * @params [reqs]
     * @create 2018/12/7 15:17
     */
    int batchSaveTemplateRule(List<SaveRuleInfoReq> reqs);
    /**
     *@description 根据id 修改策略
     *@author wucheng
     *@params [req]
     *@create 2018/12/7 16:04
     *@return int
     */
    int updateTemplateRuleById(SaveRuleInfoReq req);

    /**
     *@description 据套包id，获取其绑定的策略信息
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/12 14:43
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp>
     */
    List<TemplateRuleInfoDetailResp> getTemplateRuleListByPackageId(Long packageId);

    /**
     *@description 根据策略id，删除策略
     *@author wucheng
     *@params [id]
     *@create 2018/12/13 10:01
     *@return int
     */
    int deleteTemplateRuleById(Long id);

    /**
     *@description 根据策略id，获取该策略的详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:04
     *@return com.iot.control.packagemanager.vo.resp.TemplateRuleInfoDetailResp
     */
    TemplateRuleInfoDetailResp getTemplateRuleDetailInfoById(Long id);
}