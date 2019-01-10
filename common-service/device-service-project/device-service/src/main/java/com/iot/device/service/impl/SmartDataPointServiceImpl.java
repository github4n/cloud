package com.iot.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.util.StringUtil;
import com.iot.device.mapper.SmartDataPointMapper;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.model.SmartDataPoint;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.ISmartDataPointService;
import com.iot.device.vo.req.voicebox.SearchSmartDataPointReq;
import com.iot.device.vo.req.voicebox.SmartDataPointReq;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import com.iot.util.AssertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年5月25日 下午4:24:20
 * 修改人： chenxiaolin
 * 修改时间：2018年5月25日 下午4:24:20
 */
@Service
public class SmartDataPointServiceImpl extends ServiceImpl<SmartDataPointMapper, SmartDataPoint> implements ISmartDataPointService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartDataPointServiceImpl.class);

    @Autowired
    private SmartDataPointMapper smartDataPointMapper;

    @Autowired
    private IServiceModulePropertyService iPropertyService;

	@Override
	public boolean updateBatchByIdAndDataPointId(List<SmartDataPoint> upss) {
		if (CollectionUtils.isEmpty(upss)) {
//            throw new IllegalArgumentException("Error: entityList must not be empty");
            return true;
        }
        updateBatchById(upss, 30);
        return true;
	}

    @Override
    public boolean insertBatchDataPoint(List<SmartDataPoint> dataPointList) {
        if (CollectionUtils.isEmpty(dataPointList)) {
            return true;
        }
        return super.insertBatch(dataPointList);
    }

    @Override
    public boolean delBatchDataPoint(List<Long> dels){
        if (CollectionUtils.isEmpty(dels)) {
            return true;
        }
        super.deleteBatchIds(dels);
        return true;
    }

    /**
     *  "根据tenantId、propertyId、smart获取一个SmartDataPoint
     * @param tenantId
     * @param propertyId
     * @param smart
     * @return
     */
    @Override
    public SmartDataPointResp getSmartDataPoint(Long tenantId, Long propertyId, Integer smart) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(propertyId, "propertyId.notnull");
        AssertUtils.notNull(smart, "smart.notnull");

        SearchSmartDataPointReq req = new SearchSmartDataPointReq();
        req.setSmart(smart);
        req.setPropertyId(propertyId);
        req.setTenantId(tenantId);

        List<SmartDataPoint> smartDataPointList = this.findBySmartDataPointReq(req);

        SmartDataPointResp smartDataPointResp = null;
        if (CollectionUtils.isNotEmpty(smartDataPointList)) {
            smartDataPointResp = new SmartDataPointResp();
            BeanUtils.copyProperties(smartDataPointList.get(0), smartDataPointResp);
        }

        return smartDataPointResp;
    }

    /**
     *  根据tenantId、propertyId获取SmartDataPoint 列表
     *
     * @param tenantId
     * @param propertyId
     * @return
     */
    @Override
    public List<SmartDataPoint> findSmartDataPointList(Long tenantId, Long propertyId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(propertyId, "propertyId.notnull");

        SearchSmartDataPointReq req = new SearchSmartDataPointReq();
        req.setPropertyId(propertyId);
        req.setTenantId(tenantId);

        List<SmartDataPoint> smartDataPointList = this.findBySmartDataPointReq(req);
        return smartDataPointList;
    }

    /**
     *  获取 SmartDataPoint 列表
     * @param req
     * @return
     */
    @Override
    public List<SmartDataPoint> findBySmartDataPointReq(SearchSmartDataPointReq req) {
        EntityWrapper<SmartDataPoint> wrapper = new EntityWrapper<>();

        if(req.getPropertyCode() != null){
            wrapper.eq("property_code", req.getPropertyCode());
        }
        if(req.getPropertyId() != null){
            wrapper.eq("property_id", req.getPropertyId());
        }
        if(req.getSmart() != null){
            wrapper.eq("smart", req.getSmart());
        }
        if(req.getTenantId() != null){
            wrapper.eq("tenant_id", req.getTenantId());
        }

	    return super.selectList(wrapper);
    }

    /**
     *  copy 属性关联的音箱功能点 到 新创建的属性
     *
     * @param parentPropertyId  父属性
     * @param newPropertyId     新创建的属性
     */
    @Override
    public void copySmartDataPoint(Long parentPropertyId, Long newPropertyId) {
        LOGGER.debug("copySmartDataPoint, parentPropertyId={}, newPropertyId={}", parentPropertyId, newPropertyId);

        ServiceModuleProperty parentProperty = iPropertyService.get(parentPropertyId);
        if (parentProperty == null) {
            LOGGER.debug("copySmartDataPoint, parentProperty is null");
            return ;
        }

        List<SmartDataPoint> parentSmartDataPointList = this.findSmartDataPointList(parentProperty.getTenantId(), parentProperty.getId());
        if (CollectionUtils.isEmpty(parentSmartDataPointList)) {
            LOGGER.debug("copySmartDataPoint, parentSmartDataPointList is empty");
            return ;
        }

        ServiceModuleProperty newProperty = iPropertyService.get(newPropertyId);
        if (newProperty == null) {
            LOGGER.debug("copySmartDataPoint, newProperty is null");
            return ;
        }

        List<SmartDataPointReq> smartList = Lists.newArrayList();
        for (SmartDataPoint sdp : parentSmartDataPointList) {
            SmartDataPointReq sdpr = new SmartDataPointReq();
            sdpr.setSmart(sdp.getSmart());
            sdpr.setSmartCode(sdp.getSmartCode());

            smartList.add(sdpr);
        }

        this.createSmartDataPoint(smartList, newProperty.getTenantId(), newPropertyId, newProperty.getCreateBy());
    }

    /**
     *  根据 propertyId、tenantId 删除记录
     * @param propertyId
     * @param tenantId
     */
    @Override
    public void delByPropertyIdAndTenantId(Long propertyId, Long tenantId) {
        AssertUtils.notNull(propertyId, "propertyId.notnull");
        AssertUtils.notNull(tenantId, "tenantId.notnull");

        EntityWrapper<SmartDataPoint> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("property_id", propertyId);

        super.delete(wrapper);
    }

    /**
     *  根据 propertyIds 删除 音箱功能点记录
     * @param propertyIds
     */
    @Override
    public void delByPropertyIds(List<Long> propertyIds) {
        if (CollectionUtils.isEmpty(propertyIds)) {
            LOGGER.debug("delByPropertyIds, propertyIds is empty");
            return ;
        }

        for (Long propertyId : propertyIds) {
            ServiceModuleProperty property = iPropertyService.get(propertyId);
            if (property == null) {
                continue;
            }

            this.delByPropertyIdAndTenantId(propertyId, property.getTenantId());
        }
    }

    /**
     *  创建smartDataPoint记录
     *
     * @param smartList
     * @param tenantId
     * @param propertyId
     * @param createBy
     */
    @Override
    public void createSmartDataPoint(List<SmartDataPointReq> smartList, Long tenantId, Long propertyId, Long createBy) {
        // 先删除旧的
        this.delByPropertyIdAndTenantId(propertyId, tenantId);

        if (smartList == null || smartList.size() == 0) {
            LOGGER.debug("createSmartDataPoint, smartList null or empty");
            return ;
        }

        AssertUtils.notNull(tenantId, "tenantId.notNull");
        AssertUtils.notNull(propertyId, "propertyId.notNull");
        AssertUtils.notEmpty(smartList, "smartList.notEmpty");

        // 插入新数据
        Date currentTime = new Date();
        for (SmartDataPointReq smartDataPointReq : smartList) {
            if (StringUtil.isBlank(smartDataPointReq.getSmartCode())) {
                LOGGER.debug("createSmartDataPoint, smartCode is blank, smartDataPointReq={}", JSON.toJSONString(smartDataPointReq));
                continue;
            }

            ServiceModuleProperty property = iPropertyService.get(propertyId);
            if (property == null) {
                LOGGER.debug("createSmartDataPoint, ServiceModuleProperty is null, propertyId={}", propertyId);
                continue;
            }

            SmartDataPoint sdp = new SmartDataPoint();
            sdp.setCreateBy(createBy);
            sdp.setCreateTime(currentTime);
            sdp.setUpdateBy(createBy);
            sdp.setUpdateTime(currentTime);

            sdp.setPropertyId(propertyId);
            sdp.setPropertyCode(property.getCode());
            sdp.setSmartCode(smartDataPointReq.getSmartCode());
            sdp.setSmart(smartDataPointReq.getSmart());
            sdp.setTenantId(tenantId);

            this.insert(sdp);
        }
    }
}
