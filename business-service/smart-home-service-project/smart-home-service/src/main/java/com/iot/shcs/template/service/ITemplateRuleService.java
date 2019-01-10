package com.iot.shcs.template.service;

import com.iot.shcs.template.entity.TemplateRule;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 模板规则表 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-12
 */
public interface ITemplateRuleService extends IService<TemplateRule> {

    List<TemplateRule> getTemplateByPackId(Long packId);
}
