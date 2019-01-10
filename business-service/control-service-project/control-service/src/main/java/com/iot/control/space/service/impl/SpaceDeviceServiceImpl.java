package com.iot.control.space.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.control.space.domain.SpaceDevice;
import com.iot.control.space.mapper.SpaceDeviceMapper;
import com.iot.control.space.service.ISpaceDeviceService;
import com.iot.control.space.service.ISpaceService;
import com.iot.control.space.service.SpaceCoreServiceUtils;
import com.iot.control.space.util.BeanCopyUtil;
import com.iot.control.space.util.SpaceBaseCacheUtils;
import com.iot.control.space.vo.RoomSpaceVo;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.control.space.vo.SpaceResp;
import com.iot.util.AssertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/09 10:33
 **/
@Service
public class SpaceDeviceServiceImpl extends ServiceImpl<SpaceDeviceMapper, SpaceDevice> implements ISpaceDeviceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpaceDeviceServiceImpl.class);

    @Autowired
    SpaceDeviceMapper spaceDeviceMapper;

    @Autowired
    ISpaceService spaceService;

    @Override
    public boolean inserSpaceDevice(SpaceDeviceReq spaceDeviceReq){
        AssertUtils.notNull(spaceDeviceReq, "spaceDeviceReq.notnull");
        SpaceDevice spaceDevice = BeanCopyUtil.spaceDeviceReqToSpaceDevice(spaceDeviceReq);
        List<Long> spaceIds = new ArrayList<>();
        Long tenantId = spaceDeviceReq.getTenantId();
        spaceIds.add(spaceDeviceReq.getSpaceId());
        //删除spaceDevice缓存
        delSpaceDeviceCache(tenantId, spaceIds);
        spaceDevice.setCreateTime(new Date());
        spaceDevice.setUpdateTime(new Date());
        return super.insert(spaceDevice);
    }

    private void delSpaceDeviceCache(Long tenantId, List<Long> spaceIdList){
        List<Long> spaceIds = new ArrayList<>();
        SpaceAndSpaceDeviceVo spaceVo = new SpaceAndSpaceDeviceVo();
        spaceVo.setSpaceIds(spaceIdList);
        spaceVo.setTenantId(tenantId);
        List<SpaceResp> spaces = spaceService.findSpaceInfoBySpaceIds(spaceVo);
        spaces.forEach(space ->{
            spaceIds.add(space.getId());
            Long parentId = space.getParentId();
            if (parentId != null && parentId != -1) {
                spaceIds.add(space.getParentId());
            }
        });
        SpaceBaseCacheUtils.deleteCacheSpaceDevices(tenantId, spaceIds);
    }

    @Override
    public boolean updateSpaceDevice(SpaceDeviceReq spaceDeviceReq){
        AssertUtils.notNull(spaceDeviceReq, "spaceDeviceReq.notnull");
        AssertUtils.notNull(spaceDeviceReq.getId(), "spaceDevice.id.notnull");
        SpaceDevice spaceDevice = BeanCopyUtil.spaceDeviceReqToSpaceDevice(spaceDeviceReq);
        Long tenantId = spaceDeviceReq.getTenantId();
        List<Long> spaceIds = new ArrayList<>();
        SpaceDevice oldSapceDevice = super.selectById(spaceDevice.getId());//旧spaceDevcie
        spaceIds.add(oldSapceDevice.getSpaceId());
        spaceIds.add(spaceDeviceReq.getSpaceId());
        delSpaceDeviceCache(tenantId, spaceIds);
        spaceDevice.setUpdateTime(new Date());
        return super.updateById(spaceDevice);
    }

    @Override
    public boolean insertOrUpdateSpaceDeviceByDevId(SpaceDeviceReq spaceDeviceReq){
        AssertUtils.notNull(spaceDeviceReq, "spaceDeviceReq.notnull");
        AssertUtils.notNull(spaceDeviceReq.getDeviceId(), "device.id.notnull");
        SpaceDevice spaceDevice = BeanCopyUtil.spaceDeviceReqToSpaceDevice(spaceDeviceReq);
        Long tenantId = spaceDevice.getTenantId();
        String devId = spaceDevice.getDeviceId();
        Long orgId = spaceDevice.getOrgId();
        EntityWrapper<SpaceDevice> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("device_id", devId);
        wrapper.eq("org_id",orgId);
        SpaceDevice oldSapceDevice = super.selectOne(wrapper); //旧spaceDevcie
        List<Long> spaceIds = new ArrayList<>();
        if(oldSapceDevice != null){  //更新spaceDevice
            spaceDevice.setId(oldSapceDevice.getId());
            spaceIds.add(oldSapceDevice.getSpaceId());
        }else { //新增spaceDevice
            spaceDevice.setCreateTime(new Date());
        }
        spaceDevice.setUpdateTime(new Date());
        spaceIds.add(spaceDeviceReq.getSpaceId());
        delSpaceDeviceCache(tenantId, spaceIds);
        return super.insertOrUpdate(spaceDevice);
    }

    /**
     * @Description: 批量保存spaceDevice
     *
     * @param spaceDeviceReqs
     * @return: boolean
     * @author: chq
     * @date: 2018/10/9 21:13
     **/
    @Override
    public boolean saveSpaceDeviceList(List<SpaceDeviceReq> spaceDeviceReqs) {
        AssertUtils.notNull(spaceDeviceReqs, "spaceDeviceReq.notnull");
        List<SpaceDevice> spaceDevices = BeanCopyUtil.spaceDeviceReqsToSpaceDevices(spaceDeviceReqs);
        List<Long> spaceIds = new ArrayList<>();
        Long tenantId = null;
        if(CollectionUtils.isNotEmpty(spaceDeviceReqs)){
            tenantId = spaceDeviceReqs.get(0).getTenantId();
        }
        spaceDeviceReqs.forEach(spaceDevice->{
            spaceIds.add(spaceDevice.getSpaceId());
            spaceDevice.setCreateTime(new Date());
            spaceDevice.setUpdateTime(new Date());
        });
        //删除spaceDevice缓存
        delSpaceDeviceCache(tenantId, spaceIds);
        return super.insertBatch(spaceDevices);
    }

    @Override
    public boolean updateSpaceDevices(List<SpaceDeviceReq> spaceDeviceReqs){
        AssertUtils.notNull(spaceDeviceReqs, "spaceDeviceReq.notnull");
        List<SpaceDevice> spaceDevices = BeanCopyUtil.spaceDeviceReqsToSpaceDevices(spaceDeviceReqs);
        List<Long> spaceDeviceIds = new ArrayList<>();
        Long tenantId = null;
        spaceDeviceReqs.forEach(old ->{
            spaceDeviceIds.add(old.getId());
        });
        List<Long> spaceIds = new ArrayList<>();
        List<SpaceDevice> sapceDeviceList = super.selectBatchIds(spaceDeviceIds);//旧spaceDevcie
        sapceDeviceList.addAll(spaceDevices);
        sapceDeviceList.forEach(spaceDevice -> {
            spaceIds.add(spaceDevice.getSpaceId());
            spaceDevice.setUpdateTime(new Date());
        });
        if (CollectionUtils.isNotEmpty(sapceDeviceList)){
            tenantId = sapceDeviceList.get(0).getTenantId();
        }
        delSpaceDeviceCache(tenantId, spaceIds);
        return super.updateBatchById(spaceDevices);
    }

    /**
     * @Description: 条件查找spaceDevice
     *（可选择查询条件id、device_id、space_id、status、position、tenant_id、
     * location_id、device_type_id、order、business_type_id、product_id）
     * @param spaceDeviceReq
     * @return: java.util.List<com.iot.control.space.vo.SpaceDeviceResp>
     * @author: chq
     * @date: 2018/10/9 21:20
     **/
    //TODO 是否需要增加缓存？
    @Override
    public List<SpaceDeviceResp> findSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq) {

        List<SpaceDevice> spaceDeviceList = spaceDeviceMapper.findSpaceDeviceByCondition(spaceDeviceReq);
        List<SpaceDeviceResp> spaceDeviceRespList = BeanCopyUtil.spaceDeviceListToSpaceDeviceRespList(spaceDeviceList);
        return spaceDeviceRespList;
    }

    //TODO 内部接口需修改
    /**
    * @Description: 通过spaceId查找spaceDevice信息
    *
    * @param spaceId
    * @return: java.util.List<com.iot.control.space.vo.SpaceDeviceVo>
    * @author: chq
    * @date: 2018/10/12 13:54
    **/
    public List<SpaceDeviceVo> findSpaceDeviceVOBySpaceId(Long tenantId, Long spaceId) {
        RoomSpaceVo spaceVo = new RoomSpaceVo();
        spaceVo.setTenantId(tenantId);
        spaceVo.setHomeId(spaceId);
        return SpaceCoreServiceUtils.findSpaceDeviceVOByHomeIdAndUserId(spaceVo);
    }

    @Override
    public List<SpaceDeviceResp> findSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req){
        AssertUtils.notNull(req, "SpaceAndSpaceDeviceVo.notnull");
        List<Long> spaceIds = req.getSpaceIds();
        List<String> deviceIds = req.getDeviceIds();
        EntityWrapper<SpaceDevice> wrapper = new EntityWrapper<>();
        if(CollectionUtils.isNotEmpty(spaceIds)){
            wrapper.in("space_id", spaceIds);
        }
        if(CollectionUtils.isNotEmpty(deviceIds)){
            wrapper.in("device_id", deviceIds);
        }
        if(req.getTenantId() != null){
            wrapper.eq("tenant_id", req.getTenantId());
        }
        if (req.getOrgId()!=null){
            wrapper.eq("org_id",req.getOrgId());
        }
        List<SpaceDevice>spaceDeviceList = super.selectList(wrapper);
        List<SpaceDeviceResp> spaceDeviceRespList = BeanCopyUtil.spaceDeviceListToSpaceDeviceRespList(spaceDeviceList);
        return spaceDeviceRespList;
    }

    /**
     * @Description: 条件查找spaceDevice
     *
     * @param spaceDeviceReq
     * @return: int
     * @author: chq
     * @date: 2018/10/9 21:20
     **/
    //TODO 是否需要增加缓存？
    @Override
    public int countSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq) {
        AssertUtils.notNull(spaceDeviceReq, "spaceDeviceReq.notnull");
        EntityWrapper<SpaceDevice> wrapper = new EntityWrapper<>();
        if(spaceDeviceReq.getId() != null){
            wrapper.eq("id", spaceDeviceReq.getId());
        }
        if(spaceDeviceReq.getDeviceId() != null){
            wrapper.eq("device_id", spaceDeviceReq.getDeviceId());
        }
        if(spaceDeviceReq.getSpaceId() != null){
            wrapper.eq("space_id", spaceDeviceReq.getSpaceId());
        }
        if(spaceDeviceReq.getStatus() != null){
            wrapper.eq("status", spaceDeviceReq.getStatus());
        }
        if(spaceDeviceReq.getLocationId() != null){
            wrapper.eq("location_id", spaceDeviceReq.getLocationId());
        }
        if(spaceDeviceReq.getPosition() != null){
            wrapper.eq("position", spaceDeviceReq.getPosition());
        }
        if(spaceDeviceReq.getDeviceTypeId() != null){
            wrapper.eq("device_type_id", spaceDeviceReq.getDeviceTypeId());
        }
        if(spaceDeviceReq.getTenantId() != null){
            wrapper.eq("tenant_id", spaceDeviceReq.getTenantId());
        }
        if(spaceDeviceReq.getOrder() != null){
            wrapper.eq("order", spaceDeviceReq.getOrder());
        }
        if(spaceDeviceReq.getBusinessTypeId() != null){
            wrapper.eq("business_type_id", spaceDeviceReq.getBusinessTypeId());
        }
        if(spaceDeviceReq.getProductId() != null){
            wrapper.eq("product_id", spaceDeviceReq.getProductId());
        }
        if (spaceDeviceReq.getOrgId()!=null){
            wrapper.eq("org_id",spaceDeviceReq.getOrgId());
        }
        return super.selectCount(wrapper);
    }

    /**
     * @Description: 条件更新spaceDevice
     * setValueParam; //需要修改的对象
     * requstParam;//where查找条件
     * @param reqVo
     * @return: boolean
     * @author: chq
     * @date: 2018/10/11 14:19
     **/
    @Override
    public boolean updateSpaceDeviceByCondition(SpaceDeviceReqVo reqVo){

        AssertUtils.notNull(reqVo, "spaceDeviceReq.notnull");
        AssertUtils.notNull(reqVo.getSetValueParam(), "setValueParam.notnull");
        AssertUtils.notNull(reqVo.getRequstParam(), "requestParam.notnull");
        SpaceDeviceReq setValueParam = reqVo.getSetValueParam();
        SpaceDeviceReq spaceDevice = reqVo.getRequstParam();
        Long tenantId = spaceDevice.getTenantId();
        SpaceDevice setValue = BeanCopyUtil.spaceDeviceReqToSpaceDevice(setValueParam);
        EntityWrapper<SpaceDevice> wrapper = new EntityWrapper<>();
        if(spaceDevice.getId() != null){
            wrapper.eq("id", spaceDevice.getId());
        }
        if(spaceDevice.getDeviceId() != null){
            wrapper.eq("device_id", spaceDevice.getDeviceId());
        }
        if(spaceDevice.getSpaceId() != null){
            wrapper.eq("space_id", spaceDevice.getSpaceId());
        }
        if(spaceDevice.getStatus() != null){
            wrapper.eq("status", spaceDevice.getStatus());
        }
        if(spaceDevice.getLocationId() != null){
            wrapper.eq("location_id", spaceDevice.getLocationId());
        }
        if(spaceDevice.getPosition() != null){
            wrapper.eq("position", spaceDevice.getPosition());
        }
        if(spaceDevice.getDeviceTypeId() != null){
            wrapper.eq("device_type_id", spaceDevice.getDeviceTypeId());
        }
        if(spaceDevice.getTenantId() != null){
            wrapper.eq("tenant_id", spaceDevice.getTenantId());
        }
        if(spaceDevice.getOrder() != null){
            wrapper.eq("order", spaceDevice.getOrder());
        }
        if(spaceDevice.getBusinessTypeId() != null){
            wrapper.eq("business_type_id", spaceDevice.getBusinessTypeId());
        }
        if(spaceDevice.getProductId() != null){
            wrapper.eq("product_id", spaceDevice.getProductId());
        }
        if (spaceDevice.getOrgId()!=null){
            wrapper.eq("org_id", spaceDevice.getOrgId());
        }
        List<SpaceDevice> spaceDevices = super.selectList(wrapper);
        List<Long> spaceIds = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(spaceDevices)){
            spaceDevices.forEach( s-> {
                spaceIds.add(s.getSpaceId());
            });
        }
        SpaceAndSpaceDeviceVo req = new SpaceAndSpaceDeviceVo();
        req.setSpaceIds(spaceIds);
        req.setTenantId(tenantId);
        List<SpaceResp>  spaceResps = spaceService.findSpaceInfoBySpaceIds(req);
        if (CollectionUtils.isNotEmpty(spaceResps)) {
            // 将space_device缓存删除
            spaceResps.forEach(
                    m -> {
                        Long parentId = m.getParentId();
                        if (parentId != null && parentId != -1) {
                            spaceIds.add(m.getParentId());
                        }
                    });
            //可能是修改了房间 所以也要清原有的房间缓存
            if (setValueParam.getSpaceId() != null) {
                spaceIds.add(setValueParam.getSpaceId());
            }

        }
        SpaceBaseCacheUtils.deleteCacheSpaceDevices(tenantId, spaceIds);
        return super.update(setValue,wrapper);
    }

    //TODO 2B、2C共用接口 接口需修改
    /**
    * @Description: 通过deviceIds删除spaceDevice
    *
    * @param deviceId
    * @return: int
    * @author: chq
    * @date: 2018/10/12 10:10
    **/
    @Override
    public int deleteSpaceDeviceByDeviceId(Long tenantId, String deviceId) {// 解除设备与房间的关系
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("tenantId", tenantId);
        List<SpaceDevice> spaceDeviceList = spaceDeviceMapper.findSpaceIdByDeviceId(params);
        List<Long> spaceIds = Lists.newArrayList();
        if (!org.springframework.util.CollectionUtils.isEmpty(spaceDeviceList)) {
            spaceDeviceList.forEach(spaceDev -> {
                //房间和家下面的对应设备清空
                spaceIds.add(spaceDev.getSpaceId());
                spaceIds.add(spaceDev.getLocationId());
            });
        }
        SpaceBaseCacheUtils.deleteCacheSpaceDevices(tenantId, spaceIds);
        int result = spaceDeviceMapper.deleteByDeviceId(tenantId, deviceId);
        return result;
    }

    @Override
    public boolean deleteSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req){
        AssertUtils.notNull(req, "SpaceAndSpaceDeviceVo.notnull");
        Long tenantId = req.getTenantId();
        List<SpaceDeviceResp> resps = findSpaceDeviceBySpaceIdsOrDeviceIds(req);
        List<Long> spaceIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(resps)){
            resps.forEach(spaceDevice ->{
                //房间和家下面的对应设备清空
                spaceIds.add(spaceDevice.getSpaceId());
                spaceIds.add(spaceDevice.getLocationId());
            });
        }
        SpaceBaseCacheUtils.deleteCacheSpaceDevices(tenantId, spaceIds);
        EntityWrapper<SpaceDevice> wrapper = new EntityWrapper<>();
        if(CollectionUtils.isNotEmpty(req.getSpaceIds())){
            wrapper.in("space_id", req.getSpaceIds());
        }
        if(CollectionUtils.isNotEmpty(req.getDeviceIds())){
            wrapper.in("device_id", req.getDeviceIds());
        }
        if (req.getOrgId()!=null){
            wrapper.eq("org_id",req.getOrgId());
        }
        return super.delete(wrapper);
    }

    /**
    * @Description: 通过deviceId 查找spaceDevice及space信息
    *
    * @param req
    * @return: com.iot.control.space.vo.SpaceDeviceVo
    * @author: chq
    * @date: 2018/10/15 12:17
    **/
    @Override
    public List<SpaceDeviceVo> findSpaceDeviceInfoByDeviceIds(SpaceAndSpaceDeviceVo req){
        EntityWrapper wrapper = new EntityWrapper<>();
        if(CollectionUtils.isNotEmpty(req.getDeviceIds())){
            wrapper.in("device_id", req.getDeviceIds());
        }
        if(req.getTenantId() != null){
            wrapper.eq("s.tenant_id", req.getTenantId());
            wrapper.eq("sd.tenant_id", req.getTenantId());
        }
        if (req.getOrgId() != null){
            wrapper.eq("org_id",req.getOrgId());
        }
        return spaceDeviceMapper.findSpaceDeviceInfoByDeviceIds(wrapper);
    }

}
