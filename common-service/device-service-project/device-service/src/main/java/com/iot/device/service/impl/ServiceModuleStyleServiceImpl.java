package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.exception.BusinessException;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.mapper.ServiceModuleStyleMapper;
import com.iot.device.mapper.ServiceStyleToTemplateMapper;
import com.iot.device.model.ServiceModuleStyle;
import com.iot.device.service.IServiceModuleStyleService;
import com.iot.device.service.IServiceStyleToTemplateService;
import com.iot.device.vo.req.ServiceModuleStyleReq;
import com.iot.device.vo.rsp.ServiceModuleStyleResp;
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
 * 模组-样式表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
@Transactional
public class ServiceModuleStyleServiceImpl extends ServiceImpl<ServiceModuleStyleMapper, ServiceModuleStyle> implements IServiceModuleStyleService {


    @Autowired
    private ServiceModuleStyleMapper serviceModuleStyleMapper;

    @Autowired
    private IServiceStyleToTemplateService iServiceStyleToTemplateService;

    @Override
    public Long saveOrUpdate(ServiceModuleStyleReq serviceModuleStyleReq) {

        ServiceModuleStyle serviceModuleStyle = null;
        if (serviceModuleStyleReq.getId() != null && serviceModuleStyleReq.getId() > 0) {
            serviceModuleStyle = super.selectById(serviceModuleStyleReq.getId());
            if (serviceModuleStyle == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            serviceModuleStyle.setUpdateTime(new Date());
            serviceModuleStyle.setUpdateBy(serviceModuleStyleReq.getUpdateBy());
        } else {
            serviceModuleStyle = new ServiceModuleStyle();
            serviceModuleStyle.setUpdateTime(new Date());
            serviceModuleStyle.setUpdateBy(serviceModuleStyleReq.getUpdateBy());
            serviceModuleStyle.setCreateTime(new Date());
            serviceModuleStyle.setCreateBy(serviceModuleStyleReq.getCreateBy());
        }
        serviceModuleStyle.setTenantId(serviceModuleStyleReq.getTenantId());
        serviceModuleStyle.setServiceModuleId(serviceModuleStyleReq.getServiceModuleId());
        serviceModuleStyle.setName(serviceModuleStyleReq.getName());
        serviceModuleStyle.setCode(serviceModuleStyleReq.getCode());
        serviceModuleStyle.setThumbnail(serviceModuleStyleReq.getThumbnail());
        serviceModuleStyle.setDescription(serviceModuleStyleReq.getDescription());
        super.insertOrUpdate(serviceModuleStyle);
        return serviceModuleStyle.getId();
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (ids.size()>0 && ids!=null){
            super.deleteBatchIds(ids);
            List<ServiceStyleToTemplateResp> list = iServiceStyleToTemplateService.list(ids);
            ArrayList idsResult = new ArrayList();
            list.forEach(m->{
                idsResult.add(m.getId());
            });
            iServiceStyleToTemplateService.delete(idsResult);
        }
    }

    @Override
    public List<ServiceModuleStyleResp> list(ArrayList serviceModuleId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        wrapper.in("service_module_id",serviceModuleId);
        wrapper.orderDesc(Arrays.asList("create_time"));
        List<ServiceModuleStyle> list = super.selectList(wrapper);
        List<ServiceModuleStyleResp> rspList = new ArrayList<>();
        list.forEach(m->{
            ServiceModuleStyleResp serviceModuleStyleResp = new ServiceModuleStyleResp();
            serviceModuleStyleResp.setId(m.getId());
            serviceModuleStyleResp.setTenantId(m.getTenantId());
            serviceModuleStyleResp.setServiceModuleId(m.getServiceModuleId());
            serviceModuleStyleResp.setName(m.getName());
            serviceModuleStyleResp.setCode(m.getCode());
            serviceModuleStyleResp.setThumbnail(m.getThumbnail());
            serviceModuleStyleResp.setCreateBy(m.getCreateBy());
            serviceModuleStyleResp.setCreateTime(m.getCreateTime());
            serviceModuleStyleResp.setUpdateBy(m.getUpdateBy());
            serviceModuleStyleResp.setUpdateTime(m.getUpdateTime());
            serviceModuleStyleResp.setDescription(m.getDescription());
            rspList.add(serviceModuleStyleResp);
        });
        return rspList;
    }

    @Override
    public List<ServiceModuleStyleResp> listByStyleTemplateId(Long styleTemplateId) {
        List<ServiceModuleStyle> list = serviceModuleStyleMapper.listByStyleTemplateId(styleTemplateId,SaaSContextHolder.currentTenantId());
        List<ServiceModuleStyleResp> rspList = new ArrayList<>();
        list.forEach(m->{
            ServiceModuleStyleResp serviceModuleStyleResp = new ServiceModuleStyleResp();
            serviceModuleStyleResp.setId(m.getId());
            serviceModuleStyleResp.setTenantId(m.getTenantId());
            serviceModuleStyleResp.setServiceModuleId(m.getServiceModuleId());
            serviceModuleStyleResp.setName(m.getName());
            serviceModuleStyleResp.setCode(m.getCode());
            serviceModuleStyleResp.setThumbnail(m.getThumbnail());
            serviceModuleStyleResp.setCreateBy(m.getCreateBy());
            serviceModuleStyleResp.setCreateTime(m.getCreateTime());
            serviceModuleStyleResp.setUpdateBy(m.getUpdateBy());
            serviceModuleStyleResp.setUpdateTime(m.getUpdateTime());
            serviceModuleStyleResp.setDescription(m.getDescription());
            rspList.add(serviceModuleStyleResp);
        });
        return rspList;
    }
}
