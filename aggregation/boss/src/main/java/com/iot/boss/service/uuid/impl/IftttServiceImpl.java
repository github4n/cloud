package com.iot.boss.service.uuid.impl;

import com.alibaba.fastjson.JSON;
import com.iot.boss.service.uuid.IftttService;

import com.iot.boss.vo.TemplateVoResp;
import com.iot.control.packagemanager.enums.TemplateRuleTypeEnum;
import com.iot.shcs.template.api.PackageApi;
import com.iot.shcs.template.vo.TemplateRuleResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("iftttService")
public class IftttServiceImpl implements IftttService {

    @Autowired
    private PackageApi packageApi;

    /**
     * 描述：获取预设模板
     *
     * @param productModel
     * @author chq
     * @created 2018年6月28日 下午2:37:31
     * @since
     */
    @Override
    public TemplateVoResp getTempaltes(String productModel) {
        TemplateVoResp templateVoResp = new TemplateVoResp();
        log.info("getTempaltes productModel: {}", productModel);
        List<TemplateRuleResp> templateRuleResps =  packageApi.getRuleByModel(productModel);

        if(!CollectionUtils.isEmpty(templateRuleResps)){
            List<Map> securityTempaltes = new ArrayList<>();
            List<Map> iftttTempaltes = new ArrayList<>();
            List<Map> sceneTempaltes = new ArrayList<>();
            templateRuleResps.forEach(template->{
                Integer templateType = template.getType();
                Map<String, Object> templateMap = JSON.parseObject(template.getJson(), Map.class);
                log.info("template is .......{}", template.getJson());
                if(templateType.compareTo(TemplateRuleTypeEnum.TYPE_SECURITY.getCode()) == 0){
                    securityTempaltes.add(templateMap);
                }
                if(templateType.compareTo(TemplateRuleTypeEnum.TYPE_SCENE.getCode()) == 0){
                    sceneTempaltes.add(templateMap);
                }
                if (templateType.compareTo(TemplateRuleTypeEnum.TYPE_IFTTT.getCode()) == 0){
                    iftttTempaltes.add(templateMap);
                }

            });
            templateVoResp.setSecurityTempaltes(securityTempaltes);
            templateVoResp.setSceneTempaltes(sceneTempaltes);
            templateVoResp.setIftttTempaltes(iftttTempaltes);
        }

        return templateVoResp;
    }

}
