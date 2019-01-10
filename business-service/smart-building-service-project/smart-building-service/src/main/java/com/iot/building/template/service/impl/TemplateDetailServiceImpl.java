package com.iot.building.template.service.impl;

import com.iot.building.template.domain.TemplateDetail;
import com.iot.building.template.mapper.TemplateDetailMapper;
import com.iot.building.template.service.TemplateDetailService;
import com.iot.building.template.vo.rsp.DeviceTarValueResp;
import com.iot.building.template.vo.rsp.TemplateDetailResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TemplateDetailServiceImpl implements TemplateDetailService {
    private static final Logger log = LoggerFactory.getLogger(TemplateDetailServiceImpl.class);

    @Autowired
    private TemplateDetailMapper templateDetailMapper;


    @Override
    public int insert(TemplateDetail templateDetail){
        return templateDetailMapper.insert(templateDetail);
    }

    @Override
    public void delTemplateDetail(Long templateId){
        templateDetailMapper.delTemplateDetail(templateId);
    }

    @Override
    public List<TemplateDetail> findByTemplateId(Long templateId, Long tenantId) {
        return templateDetailMapper.findByTemplateId(templateId, tenantId);
    }

    @Override
    public TemplateDetail getByTemplateIdAndProductId(Long templateId, Long productId, Long tenantId){
        return templateDetailMapper.getByTemplateIdAndProductId(templateId, productId, tenantId);
    }

    @Override
    public List<DeviceTarValueResp> findDeviceTargetValueList(Long templateId){
        return templateDetailMapper.findDeviceTargetValueList(templateId);
    }

    @Override
    public List<TemplateDetailResp> findTemplateDetailList(Long templateId){
        return templateDetailMapper.findTemplateDetailList(templateId);
    }

    @Override
    public List<String> getRoomByTemplateId(Long templateId, String templateType){
        return templateDetailMapper.getRoomByTemplateId(templateId,templateType);
    }
}
