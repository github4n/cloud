package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.exception.BusinessException;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.mapper.ServiceStyleToTemplateMapper;
import com.iot.device.model.ServiceStyleToTemplate;
import com.iot.device.service.IServiceStyleToTemplateService;
import com.iot.device.vo.req.ServiceStyleToTemplateReq;
import com.iot.device.vo.rsp.ServiceStyleToTemplateResp;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 模组-样式-to-模板样式表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
@Transactional
public class ServiceStyleToTemplateServiceImpl extends ServiceImpl<ServiceStyleToTemplateMapper, ServiceStyleToTemplate> implements IServiceStyleToTemplateService {

    @Autowired
    private ServiceStyleToTemplateMapper serviceStyleToTemplateMapper;

    @Override
    public Long saveOrUpdate(ServiceStyleToTemplateReq serviceStyleToTemplateReq) {
        ServiceStyleToTemplate serviceStyleToTemplate = null;
        if (serviceStyleToTemplateReq.getId() != null && serviceStyleToTemplateReq.getId() > 0) {
            serviceStyleToTemplate = super.selectById(serviceStyleToTemplateReq.getId());
            if (serviceStyleToTemplate == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            serviceStyleToTemplate.setUpdateTime(new Date());
            serviceStyleToTemplate.setUpdateBy(serviceStyleToTemplateReq.getUpdateBy());
        } else {
            serviceStyleToTemplate = new ServiceStyleToTemplate();
            serviceStyleToTemplate.setUpdateTime(new Date());
            serviceStyleToTemplate.setUpdateBy(serviceStyleToTemplateReq.getUpdateBy());
            serviceStyleToTemplate.setCreateTime(new Date());
            serviceStyleToTemplate.setCreateBy(serviceStyleToTemplateReq.getCreateBy());
        }
        serviceStyleToTemplate.setTenantId(serviceStyleToTemplateReq.getTenantId());
        serviceStyleToTemplate.setModuleStyleId(serviceStyleToTemplateReq.getModuleStyleId());
        serviceStyleToTemplate.setStyleTemplateId(serviceStyleToTemplateReq.getStyleTemplateId());
        serviceStyleToTemplate.setDescription(serviceStyleToTemplateReq.getDescription());
        super.insertOrUpdate(serviceStyleToTemplate);
        return serviceStyleToTemplate.getId();
    }

    @Override
    public void saveMore(ServiceStyleToTemplateReq serviceStyleToTemplateReq) {
        List<ServiceStyleToTemplate> list = new ArrayList<>();
        serviceStyleToTemplateReq.getStyleTemplateIds().forEach(m->{
            ServiceStyleToTemplate serviceStyleToTemplate = new ServiceStyleToTemplate();
            serviceStyleToTemplate.setTenantId(serviceStyleToTemplateReq.getTenantId());
            serviceStyleToTemplate.setModuleStyleId(serviceStyleToTemplateReq.getModuleStyleId());
            serviceStyleToTemplate.setStyleTemplateId(Long.valueOf(m.toString()));
            serviceStyleToTemplate.setCreateBy(serviceStyleToTemplateReq.getCreateBy());
            serviceStyleToTemplate.setCreateTime(new Date());
            serviceStyleToTemplate.setUpdateBy(serviceStyleToTemplateReq.getUpdateBy());
            serviceStyleToTemplate.setUpdateTime(new Date());
            serviceStyleToTemplate.setDescription(serviceStyleToTemplateReq.getDescription());
            list.add(serviceStyleToTemplate);
        });
        super.insertBatch(list);
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (ids.size()>0 && ids!=null){
            super.deleteBatchIds(ids);
        }

    }

    @Override
    public List<ServiceStyleToTemplateResp> list(ArrayList<Long> moduleStyleIds) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        wrapper.in("module_style_id",moduleStyleIds);
        wrapper.orderDesc(Arrays.asList("create_time"));
        List<ServiceStyleToTemplate> list = super.selectList(wrapper);
        List<ServiceStyleToTemplateResp> rspList = new ArrayList<>();
        list.forEach(m->{
            ServiceStyleToTemplateResp serviceStyleToTemplateResp = new ServiceStyleToTemplateResp();
            serviceStyleToTemplateResp.setId(m.getId());
            serviceStyleToTemplateResp.setTenantId(m.getTenantId());
            serviceStyleToTemplateResp.setModuleStyleId(m.getModuleStyleId());
            serviceStyleToTemplateResp.setStyleTemplateId(m.getStyleTemplateId());
            serviceStyleToTemplateResp.setCreateBy(m.getCreateBy());
            serviceStyleToTemplateResp.setCreateTime(m.getCreateTime());
            serviceStyleToTemplateResp.setUpdateBy(m.getUpdateBy());
            serviceStyleToTemplateResp.setUpdateTime(m.getUpdateTime());
            serviceStyleToTemplateResp.setDescription(m.getDescription());
            rspList.add(serviceStyleToTemplateResp);
        });
        return rspList;
    }
}
