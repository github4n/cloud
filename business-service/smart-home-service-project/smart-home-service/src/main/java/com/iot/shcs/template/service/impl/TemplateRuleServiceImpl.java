package com.iot.shcs.template.service.impl;

import com.google.common.collect.Maps;
import com.iot.shcs.template.entity.TemplateRule;
import com.iot.shcs.template.mapper.TemplateRuleMapper;
import com.iot.shcs.template.service.ITemplateRuleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模板规则表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-12
 */
@Service
public class TemplateRuleServiceImpl extends ServiceImpl<TemplateRuleMapper, TemplateRule> implements ITemplateRuleService {

    public List<TemplateRule> getTemplateByPackId(Long packId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("package_id", packId);
        return selectByMap(params);
    }
}
