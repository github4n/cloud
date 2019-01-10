package com.iot.shcs.space.service.impl;

import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.DelSpaceDeviceReq;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.space.service.ISpaceDeviceService;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.util.BeanCopyUtil;
import com.iot.util.AssertUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/09 10:33
 **/
@Slf4j
@Service
public class SpaceDeviceServiceImpl implements ISpaceDeviceService {

    @Autowired
    SpaceDeviceApi spaceDeviceService;

    @Autowired
    ISpaceService spaceService;

    @Override
    public boolean inserSpaceDevice(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq){
        SpaceDeviceReq req = BeanCopyUtil.copyProperties(spaceDeviceReq);
        return spaceDeviceService.inserSpaceDevice(req);

    }

    @Override
    public boolean updateSpaceDevice(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq){
        SpaceDeviceReq req = BeanCopyUtil.copyProperties(spaceDeviceReq);
        return spaceDeviceService.updateSpaceDevice(req);
    }

    @Override
    public boolean insertOrUpdateSpaceDeviceByDevId(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq){
        SpaceDeviceReq req = BeanCopyUtil.copyProperties(spaceDeviceReq);
        return spaceDeviceService.insertOrUpdateSpaceDeviceByDevId(req);
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
    public boolean saveSpaceDeviceList(List<com.iot.shcs.space.vo.SpaceDeviceReq> spaceDeviceReqs) {
        List<SpaceDeviceReq> list = BeanCopyUtil.copySpaceDeviceReq(spaceDeviceReqs);
        return spaceDeviceService.saveSpaceDeviceList(list);
    }

    @Override
    public boolean updateSpaceDevices(List<com.iot.shcs.space.vo.SpaceDeviceReq> spaceDeviceReqs){
        AssertUtils.notNull(spaceDeviceReqs, "spaceDeviceReq.notnull");
        List<SpaceDeviceReq> list = BeanCopyUtil.copySpaceDeviceReq(spaceDeviceReqs);
        return spaceDeviceService.updateSpaceDevices(list);

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
    @Override
    public List<com.iot.shcs.space.vo.SpaceDeviceResp> findSpaceDeviceByCondition(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq) {
        SpaceDeviceReq req = BeanCopyUtil.copyProperties(spaceDeviceReq);
        List<SpaceDeviceResp> resp = spaceDeviceService.findSpaceDeviceByCondition(req);
        return BeanCopyUtil.copySpaceDeviceResp(resp);
    }
    @Override
    //TODO 内部接口需修改
    /**
     * @Description: 通过spaceId查找spaceDevice信息
     *
     * @param spaceId
     * @return: java.util.List<com.iot.control.space.vo.SpaceDeviceVo>
     * @author: chq
     * @date: 2018/10/12 13:54
     **/
    public List<com.iot.shcs.space.vo.SpaceDeviceVo> findSpaceDeviceVOBySpaceId(Long spaceId){

        List<SpaceDeviceVo> resp = spaceDeviceService.findSpaceDeviceVOBySpaceId(SaaSContextHolder.currentTenantId(), spaceId);
        return BeanCopyUtil.copySpaceDeviceVo(resp);
    }

    public List<com.iot.shcs.space.vo.SpaceDeviceResp> findSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req){
        List<SpaceDeviceResp> resps = spaceDeviceService.findSpaceDeviceBySpaceIdsOrDeviceIds(req);
        return BeanCopyUtil.copySpaceDeviceResp(resps);
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
    public int countSpaceDeviceByCondition(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq) {
        SpaceDeviceReq req = BeanCopyUtil.copyProperties(spaceDeviceReq);
        return spaceDeviceService.countSpaceDeviceByCondition(req);
    }

    /**
     * @Description: 条件更新spaceDevice
     *
     * @param reqVo
     * @return: boolean
     * @author: chq
     * @date: 2018/10/11 14:19
     **/
    @Override
    public boolean updateSpaceDeviceByCondition(com.iot.shcs.space.vo.SpaceDeviceReqVo reqVo){
        SpaceDeviceReqVo req = BeanCopyUtil.copyProperties(reqVo);
        return spaceDeviceService.updateSpaceDeviceByCondition(req);
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
       return spaceDeviceService.deleteSpaceDeviceByDeviceId(tenantId, deviceId);
    }

    @Override
    public void deleteBatchSpaceDeviceByDevIds(Long tenantId, List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        spaceDeviceService.deleteSpaceDeviceByBatchDeviceIds(DelSpaceDeviceReq.builder().deviceIds(deviceIds).tenantId(tenantId).build());
    }

    @Override
    public boolean deleteSpaceDeviceBySpaceIdsOrDeviceIds(com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo req){
        SpaceAndSpaceDeviceVo spaceDeviceVo = BeanCopyUtil.copyProperties(req);
        return spaceDeviceService.deleteSpaceDeviceBySpaceIdsOrDeviceIds(spaceDeviceVo);
    }

    @Override
    public List<com.iot.shcs.space.vo.SpaceDeviceVo> findSpaceDeviceInfoByDeviceIds(com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo req){
        SpaceAndSpaceDeviceVo reqVo = BeanCopyUtil.copyProperties(req);
        List<SpaceDeviceVo> resp = spaceDeviceService.findSpaceDeviceInfoByDeviceIds(reqVo);
        return BeanCopyUtil.copySpaceDeviceVo(resp);
    }

}
