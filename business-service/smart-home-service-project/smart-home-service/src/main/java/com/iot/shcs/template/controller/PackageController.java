package com.iot.shcs.template.controller;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.shcs.template.api.PackageApi;
import com.iot.shcs.template.entity.Package;
import com.iot.shcs.template.entity.TemplateRule;
import com.iot.shcs.template.service.IPackageService;
import com.iot.shcs.template.service.ITemplateRuleService;
import com.iot.shcs.template.vo.TemplateRuleResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套包表 前端控制器
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-12
 */
@Slf4j
@RestController
public class PackageController implements PackageApi {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private IPackageService packageService;

    @Autowired
    private ITemplateRuleService templateRuleService;

    @Override
    public List<TemplateRuleResp> getRuleByModel(@RequestParam("model") String model) {
        List<TemplateRuleResp> list = Lists.newArrayList();
        ProductResp productResp = productApi.getProductByModel(model);
        if (productResp == null) {
            log.debug("产品不存在！" + model);
            return list;
        }

        Package pack = packageService.getByProductId(productResp.getId(), productResp.getTenantId());
        if (pack == null) {
            log.debug("没有套包数据！" + productResp.getId() + "," + productResp.getTenantId());
            return list;
        }

        List<TemplateRule> templateRules = templateRuleService.getTemplateByPackId(pack.getId());

        if (CollectionUtils.isNotEmpty(templateRules)) {
            for (TemplateRule vo : templateRules) {
                //替换productId 成 model
                Map<String, Object> map = JSON.parseObject(vo.getJson(), Map.class);
                Map<Long, String> product2ModelMap = Maps.newHashMap();
                //if
                Map<String, Object> ifMap = (Map<String, Object>) map.get("if");
                List<Map<String, Object>> triggerList = (List<Map<String, Object>>) ifMap.get("trigger");
                List<Map<String, Object>> newTriggerList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(triggerList)) {
                    for (Map<String, Object> trigger : triggerList) {
                        // 模板中DevId存的是productId
                        String trigType = trigger.get("trigType") != null ? trigger.get("trigType").toString().toLowerCase() : "";
                        if(trigType.equals("dev")) {
                            String productId = String.valueOf(trigger.get("devId"));
                            trigger.put("devId", getModel(productId, product2ModelMap));
                        }
                        newTriggerList.add(trigger);
                    }

                    ifMap.put("trigger", newTriggerList);
                }
                map.put("if", ifMap);
                //then
                List<Map<String, Object>> thenList = (List<Map<String, Object>>) map.get("then");
                List<Map<String, Object>> newThenList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(thenList)) {
                    for (Map<String, Object> then : thenList) {
                        String thenType = then.get("thenType") != null ? then.get("thenType").toString().toLowerCase() : "";
                        if(thenType.equals("dev")) {
                            String productId = String.valueOf(then.get("id").toString());
                            then.put("id", getModel(productId, product2ModelMap));
                        }
                        newThenList.add(then);
                    }
                }

                map.put("then", newThenList);

                //重新赋值
                vo.setJson(JSON.toJSONString(map));

                //添加
                TemplateRuleResp rule = new TemplateRuleResp();
                BeanUtils.copyProperties(vo, rule);
                list.add(rule);
            }
        }

        return list;
    }

    private String getModel(String productId, Map<Long, String> product2ModelMap) {
        Long pid = Long.parseLong(productId);
        if (product2ModelMap.get(productId) == null) {
            ProductResp product = productApi.getProductById(pid);
            if (product != null) {
                product2ModelMap.put(pid, product.getModel());
            }
        }

        return product2ModelMap.get(pid);
    }

}

