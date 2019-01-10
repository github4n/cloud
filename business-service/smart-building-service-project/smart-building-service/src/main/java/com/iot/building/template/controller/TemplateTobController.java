package com.iot.building.template.controller;

import com.iot.building.excepiton.BusinessExceptionEnum;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.template.api.TemplateTobApi;
import com.iot.building.template.service.TemplateIftttService;
import com.iot.building.template.service.TemplateService;
import com.iot.building.template.vo.req.*;
import com.iot.building.template.vo.rsp.*;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TemplateTobController implements TemplateTobApi {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TemplateTobController.class);

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateIftttService templateIftttService;



    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    @Override
    public List<DeviceTarValueResp> findDeviceTarValueList(@RequestParam("templateId") Long templateId) {
        return templateService.findDeviceTarValueList(templateId);
    }

    @Override
    public SceneTemplateResp getSceneTemplate(@RequestParam("templateId") Long templateId) {
        return templateService.getSceneTemplate(templateId);
    }

    @Override
    public void deleteSceneTemplate(@RequestParam("templateId") Long templateId) throws BusinessException {
        templateService.deleteSceneTemplate(templateId);
    }

    @Override
    public Page<TemplateResp> findTemplateList(@RequestBody TemplateReq remplateReq) {
        return templateService.findTemplateList(remplateReq.getName(),remplateReq.getTemplateType(), remplateReq.getPageNum(),remplateReq.getPageSize(),remplateReq.getTenantId(),remplateReq.getLocationId());
    }

    @Override
    public Long createSceneFromTemplate(CreateSceneFromTemplateReq createSceneFromTemplateReq) throws BusinessException {
        return templateService.createSceneFromTemplate(createSceneFromTemplateReq);
    }

   /* @Override
    public Long createSceneFromTemplate(@RequestBody CreateSceneFromTemplateReq createSceneFromTemplateReq) throws BusinessException {
        return templateService.createSceneFromTemplate(createSceneFromTemplateReq);
    }*/
    
    @Override
    public void delSceneFromTemplate(@RequestBody SpaceTemplateReq spaceTemplateReq) throws BusinessException {
        templateService.delSceneFromTemplate(spaceTemplateReq);
    }

    @Override
    public void buildSceneTemplate2B(@RequestBody BuildTemplateReq buildTemplateReq) throws Exception{
        templateService.buildSceneTemplate2B(buildTemplateReq);
    }



    @Override
	public List<TemplateResp> findSceneSpaceTemplateList(@RequestBody TemplateReq templateReq) {
		return templateService.findSceneSpaceTemplateList(templateReq);
	}

    @Override
    public List<TemplateResp> findSceneTemplateList(@RequestBody TemplateReq templateReq) {
        return templateService.findSceneTemplateList(templateReq);
    }

    @Override
    public void saveIftttTemplate(@RequestBody SaveIftttTemplateReq req) {
        templateIftttService.saveIftttTemplate(req);
    }

    @Override
    public void deleteIftttTemplate(@RequestParam("templateId") Long templateId,@RequestParam("tenantId") Long tenantId) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        templateIftttService.deleteIftttTemplate(templateId);
    }

    @Override
    public List<RuleResp> getIftttTemplateList(@RequestParam("templateId") Long templateId) {
        return templateIftttService.getIftttTemplateList(templateId);
    }

    @Override
    public RuleResp getIftttTemplateByRuleId(@RequestParam("ruleId") Long ruleId) {
        return templateIftttService.getIftttTemplateByRuleId(ruleId);
    }


    @Override
    public Long buildIfttt2B(@RequestBody BuildIftttReq req) {
        return templateIftttService.buildIfttt2B(req);
    }

    @Override
    public TemplateVoResp getTempaltes(@RequestParam("productModel") String productModel,@RequestParam("orgId") Long orgId) {
        LOGGER.info("收到获取模板的请求，产品的model：" + productModel);
        try {
            TemplateVoResp tempaltes = templateService.getTempaltesByModel(productModel,orgId);
            if (tempaltes == null) {
                throw new BusinessException(IftttExceptionEnum.TEMPLATE_IS_NULL);
            }
            return tempaltes;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.info("getTempaltes.error：" + e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION, e);
        }
    }
}
