package com.iot.device.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.mapper.StyleTemplateMapper;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.StyleTemplate;
import com.iot.device.service.IDeviceTypeToStyleService;
import com.iot.device.service.IProductStyleTemplateService;
import com.iot.device.service.IServiceStyleToTemplateService;
import com.iot.device.service.IStyleTemplateService;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.StyleTemplateResp;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
@Transactional
public class StyleTemplateServiceImpl extends ServiceImpl<StyleTemplateMapper, StyleTemplate> implements IStyleTemplateService {

    private static final Logger log = LoggerFactory.getLogger(StyleTemplateServiceImpl.class);

    @Autowired
    private StyleTemplateMapper styleTemplateMapper;

    @Autowired
    private IServiceStyleToTemplateService iServiceStyleToTemplateService;

    @Autowired
    private IProductStyleTemplateService iProductStyleTemplateService;

    @Autowired
    private IDeviceTypeToStyleService iDeviceTypeToStyleService;

    @Override
    public Long saveOrUpdate(StyleTemplateReq styleTemplateReq) {
        StyleTemplate styleTemplate = null;
        if (styleTemplateReq.getId() != null && styleTemplateReq.getId() > 0) {
            styleTemplate = super.selectById(styleTemplateReq.getId());
            if (styleTemplate == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            styleTemplate.setUpdateTime(new Date());
            styleTemplate.setUpdateBy(styleTemplateReq.getUpdateBy());
        } else {
            styleTemplate = new StyleTemplate();
            styleTemplate.setUpdateTime(new Date());
            styleTemplate.setUpdateBy(styleTemplateReq.getUpdateBy());
            styleTemplate.setCreateTime(new Date());
            styleTemplate.setCreateBy(styleTemplateReq.getCreateBy());
        }
        styleTemplate.setTenantId(styleTemplateReq.getTenantId());
        styleTemplate.setName(styleTemplateReq.getName());
        styleTemplate.setCode(styleTemplateReq.getCode());
        styleTemplate.setImg(styleTemplateReq.getImg());
        styleTemplate.setDescription(styleTemplateReq.getDescription());
        styleTemplate.setResourceLink(styleTemplateReq.getResourceLink());
        styleTemplate.setResourceLinkValidation(styleTemplateReq.getResourceLinkValidation());
        super.insertOrUpdate(styleTemplate);
        return styleTemplate.getId();
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            //检查是否被关联
            ids.forEach(styleTemplateId -> {
                EntityWrapper wrapper = new EntityWrapper<>();
                wrapper.eq("style_template_id", styleTemplateId);
                int moduleStyleCount = iServiceStyleToTemplateService.selectCount(wrapper);
                int productStyleCount = iProductStyleTemplateService.selectCount(wrapper);
                int deviceTypeCount = iDeviceTypeToStyleService.selectCount(wrapper);
                if (moduleStyleCount > 0 || productStyleCount > 0 || deviceTypeCount > 0 ) {
                    throw new BusinessException(ServiceModuleExceptionEnum.STYLE_TEMPLATE_IS_USED);
                }
            });
            super.deleteBatchIds(ids);
        }
    }


    @Override
    public PageInfo<StyleTemplateResp> list(StyleTemplateReq styleTemplateReq) {
        Page<ServiceModuleAction> page = new Page<>(CommonUtil.getPageNum(styleTemplateReq),CommonUtil.getPageSize(styleTemplateReq));
        EntityWrapper<StyleTemplate> wrapper = new EntityWrapper<>();
        if (styleTemplateReq.getFilterIds()!=null || styleTemplateReq.getFilterIds().size()>0){
            wrapper.notIn("id",styleTemplateReq.getFilterIds());
        }
        if (StringUtils.isNotEmpty(styleTemplateReq.getSearchValues())){
            wrapper.andNew(true, "")
                    .like("name", styleTemplateReq.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("code", styleTemplateReq.getSearchValues(), SqlLike.DEFAULT);
        }
        wrapper.orderDesc(Arrays.asList("create_time"));
        List<StyleTemplate> list = styleTemplateMapper.selectPage(page,wrapper);
        List<StyleTemplateResp> rspList = new ArrayList<>();
        list.forEach(m->{
            StyleTemplateResp styleTemplateResp = new StyleTemplateResp();
            styleTemplateResp.setId(m.getId());
            styleTemplateResp.setName(m.getName());
            styleTemplateResp.setCode(m.getCode());
            styleTemplateResp.setImg(m.getImg());
            styleTemplateResp.setCreateBy(m.getCreateBy());
            styleTemplateResp.setCreateTime(m.getCreateTime());
            styleTemplateResp.setUpdateBy(m.getUpdateBy());
            styleTemplateResp.setUpdateTime(m.getUpdateTime());
            styleTemplateResp.setDescription(m.getDescription());
            styleTemplateResp.setTenantId(m.getTenantId());
            styleTemplateResp.setResourceLink(m.getResourceLink());
            styleTemplateResp.setResourceLinkValidation(m.getResourceLinkValidation());
            rspList.add(styleTemplateResp);
        });
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(rspList);
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNum(styleTemplateReq.getPageNum());
        return pageInfo;
    }

    @Override
    public List<StyleTemplateResp> listByDeviceTypeId(Long deviceTypeId) {
        List<StyleTemplate> list = styleTemplateMapper.listByDeviceTypeId(deviceTypeId);
        List<StyleTemplateResp> rspList = new ArrayList<>();
        list.forEach(m->{
            StyleTemplateResp styleTemplateResp = new StyleTemplateResp();
            styleTemplateResp.setId(m.getId());
            styleTemplateResp.setName(m.getName());
            styleTemplateResp.setCode(m.getCode());
            styleTemplateResp.setImg(m.getImg());
            styleTemplateResp.setCreateBy(m.getCreateBy());
            styleTemplateResp.setCreateTime(m.getCreateTime());
            styleTemplateResp.setUpdateBy(m.getUpdateBy());
            styleTemplateResp.setUpdateTime(m.getUpdateTime());
            styleTemplateResp.setDescription(m.getDescription());
            styleTemplateResp.setOtherId(m.getOtherId());
            styleTemplateResp.setTenantId(m.getTenantId());
            styleTemplateResp.setResourceLink(m.getResourceLink());
            styleTemplateResp.setResourceLinkValidation(m.getResourceLinkValidation());
            rspList.add(styleTemplateResp);
        });
        return rspList;
    }


    @Override
    public List<StyleTemplateResp> listByModuleStyleId(Long moduleStyleId) {
        List<StyleTemplate> list = styleTemplateMapper.listByModuleStyleId(moduleStyleId,SaaSContextHolder.currentTenantId());
        List<StyleTemplateResp> rspList = new ArrayList<>();
        list.forEach(m->{
            StyleTemplateResp styleTemplateResp = new StyleTemplateResp();
            styleTemplateResp.setId(m.getId());
            styleTemplateResp.setName(m.getName());
            styleTemplateResp.setCode(m.getCode());
            styleTemplateResp.setImg(m.getImg());
            styleTemplateResp.setCreateBy(m.getCreateBy());
            styleTemplateResp.setCreateTime(m.getCreateTime());
            styleTemplateResp.setUpdateBy(m.getUpdateBy());
            styleTemplateResp.setUpdateTime(m.getUpdateTime());
            styleTemplateResp.setDescription(m.getDescription());
            styleTemplateResp.setOtherId(m.getOtherId());
            styleTemplateResp.setTenantId(m.getTenantId());
            styleTemplateResp.setResourceLink(m.getResourceLink());
            styleTemplateResp.setResourceLinkValidation(m.getResourceLinkValidation());
            rspList.add(styleTemplateResp);
        });
        return rspList;
    }

    @Override
    public List<StyleTemplateResp> listByProductId(Long productId) {
        List<StyleTemplate> list = styleTemplateMapper.listByProductId(productId);
        List<StyleTemplateResp> rspList = new ArrayList<>();
        list.forEach(m->{
            StyleTemplateResp styleTemplateResp = new StyleTemplateResp();
            styleTemplateResp.setId(m.getId());
            styleTemplateResp.setName(m.getName());
            styleTemplateResp.setCode(m.getCode());
            styleTemplateResp.setImg(m.getImg());
            styleTemplateResp.setCreateBy(m.getCreateBy());
            styleTemplateResp.setCreateTime(m.getCreateTime());
            styleTemplateResp.setUpdateBy(m.getUpdateBy());
            styleTemplateResp.setUpdateTime(m.getUpdateTime());
            styleTemplateResp.setDescription(m.getDescription());
            styleTemplateResp.setOtherId(m.getOtherId());
            styleTemplateResp.setTenantId(m.getTenantId());
            styleTemplateResp.setResourceLink(m.getResourceLink());
            styleTemplateResp.setResourceLinkValidation(m.getResourceLinkValidation());
            rspList.add(styleTemplateResp);
        });
        return rspList;
    }

    @Override
    public StyleTemplateResp infoById(Long id) {
        StyleTemplate styleTemplate = super.selectById(id);
        StyleTemplateResp styleTemplateResp = new StyleTemplateResp();
        styleTemplateResp.setId(styleTemplate.getId());
        styleTemplateResp.setName(styleTemplate.getName());
        styleTemplateResp.setCode(styleTemplate.getCode());
        styleTemplateResp.setImg(styleTemplate.getImg());
        styleTemplateResp.setCreateBy(styleTemplate.getCreateBy());
        styleTemplateResp.setCreateTime(styleTemplate.getCreateTime());
        styleTemplateResp.setUpdateBy(styleTemplate.getUpdateBy());
        styleTemplateResp.setUpdateTime(styleTemplate.getUpdateTime());
        styleTemplateResp.setDescription(styleTemplate.getDescription());
        styleTemplateResp.setTenantId(styleTemplate.getTenantId());
        styleTemplateResp.setResourceLink(styleTemplate.getResourceLink());
        styleTemplateResp.setResourceLinkValidation(styleTemplate.getResourceLinkValidation());
        return styleTemplateResp;
    }
}
