package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.mapper.DevelopInfoMapper;
import com.iot.device.model.DevelopInfo;
import com.iot.device.service.IDevelopInfoService;
import com.iot.device.vo.req.AddDevelopInfoReq;
import com.iot.device.vo.req.DevelopInfoListResp;
import com.iot.device.vo.req.DevelopInfoPageReq;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 开发信息表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-29
 */
@Service
public class DevelopInfoServiceImpl extends ServiceImpl<DevelopInfoMapper, DevelopInfo> implements IDevelopInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevelopInfoServiceImpl.class);

    @Transactional
    @Override
    public Long addOrUpdateDevelopInfo(AddDevelopInfoReq infoReq) {
        DevelopInfo developInfo = null;
        if (infoReq.getId() != null && infoReq.getId() > 0) {
            developInfo = super.selectById(infoReq.getId());
            if (developInfo == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            developInfo.setUpdateTime(new Date());
            developInfo.setUpdateBy(infoReq.getUpdateBy());
        } else {
            developInfo = new DevelopInfo();
            developInfo.setCreateTime(new Date());
            developInfo.setCreateBy(infoReq.getCreateBy());
        }

        developInfo.setTenantId(infoReq.getTenantId());
        developInfo.setType(infoReq.getType());
        developInfo.setCode(infoReq.getCode());
        developInfo.setName(infoReq.getName());
        developInfo.setDescription(infoReq.getDescription());
        super.insertOrUpdate(developInfo);
        return developInfo.getId();
    }

    @Override
    public List<DevelopInfoListResp> findDevelopInfoListAll() {
        Long tenantId = null;
        try {
            tenantId = SaaSContextHolder.currentTenantId();
        } catch (Exception e) {
            LOGGER.info("get tenantId error", e);
        }
        EntityWrapper wrapper = new EntityWrapper();
        if (tenantId != null && tenantId > 0) {
            wrapper.eq("tenant_id", tenantId);

        }
        List<DevelopInfo> developInfos = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(developInfos)) {
            return null;
        }
        List<DevelopInfoListResp> respList = Lists.newArrayList();
        for (DevelopInfo develop : developInfos) {
            DevelopInfoListResp target = new DevelopInfoListResp();
            target.setId(develop.getId());
            target.setType(develop.getType());
            target.setCode(develop.getCode());
            target.setName(develop.getName());
            target.setDescription(develop.getDescription());
            target.setCreateTime(DateFormatUtils.format(develop.getCreateTime(), "yyyy-MM-dd"));
            respList.add(target);
        }
        return respList;
    }

    @Override
    public Page<DevelopInfoListResp> findDevelopInfoPage(DevelopInfoPageReq pageReq) {
        Long tenantId = null;
        try {
            tenantId = SaaSContextHolder.currentTenantId();
        } catch (Exception e) {
            LOGGER.info("get tenantId error", e);
        }
        EntityWrapper wrapper = new EntityWrapper();
        if (tenantId != null && tenantId > 0) {
            wrapper.eq("tenant_id", tenantId);
        }
        if (!StringUtils.isEmpty(pageReq.getSearchValues())) {
            wrapper.like("code", "%" + pageReq.getSearchValues() + "%");
        }
        com.baomidou.mybatisplus.plugins.Page<DevelopInfo> page = new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        page = super.selectPage(page, wrapper);
        List<DevelopInfoListResp> targetList = Lists.newArrayList();
        if (page.getTotal() > 0) {
            for (DevelopInfo develop : page.getRecords()) {
                DevelopInfoListResp target = new DevelopInfoListResp();
                target.setId(develop.getId());
                target.setType(develop.getType());
                target.setCode(develop.getCode());
                target.setName(develop.getName());
                target.setDescription(develop.getDescription());
                target.setCreateTime(DateFormatUtils.format(develop.getCreateTime(), "yyyy-MM-dd"));
                targetList.add(target);
            }
        }
        Page<DevelopInfoListResp> pageResult = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        pageResult.setResult(targetList);
        pageResult.setTotal(page.getTotal());
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setPages(page.getPages());

        return pageResult;
    }
}
