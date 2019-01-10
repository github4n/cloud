package com.iot.building.template.service;

import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.template.vo.req.BuildIftttReq;
import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

public interface TemplateIftttService {

    /**
     * 保存联动模板
     *
     * @param req
     */
    void saveIftttTemplate(SaveIftttTemplateReq req);

    /**
     * 删除联动模板根据模板主键
     *
     * @param templateId
     */
    void deleteIftttTemplate(Long templateId) ;

    /**
     * 根据套包主键删除模板
     * @param templateId
     */
    void deleteByTemplateId(Long templateId);

    /**
     * 查询联动模板列表
     *
     * @param templateId
     * @return
     */
    List<RuleResp> getIftttTemplateList(Long templateId);

    /**
     * 查询单个联动模板
     *
     * @param ruleId
     * @return
     */
    RuleResp getIftttTemplateByRuleId(@RequestParam("ruleId") Long ruleId);

    /**
     * 生成联动规则2C
     * @param req
     */
    void buildIfttt2C(@RequestBody BuildIftttReq req);

    /**
     * 生成联动规则2B
     * @param req
     */
    Long buildIfttt2B(@RequestBody BuildIftttReq req);
}
