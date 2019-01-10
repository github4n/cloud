package com.iot.device.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.core.service.DeviceTypeServiceCoreUtils;
import com.iot.device.exception.DeviceTypeExceptionEnum;
import com.iot.device.mapper.DeviceTypeMapper;
import com.iot.device.model.DeviceType;
import com.iot.device.service.IDeviceTypeService;
import com.iot.device.service.IDeviceTypeToServiceModuleService;
import com.iot.device.vo.req.DeviceTypeReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
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
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {


    @Autowired
    private IDeviceTypeToServiceModuleService iDeviceTypeToServiceModuleService;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public void checkDeviceTypExistByDeviceTypeId(Long deviceTypeId) {
        AssertUtils.notNull(deviceTypeId, "deviceTypeId.notnull");

        DeviceType deviceType = DeviceTypeServiceCoreUtils.getDeviceTypeByDeviceTypeId(deviceTypeId);
        if (deviceType == null) {
            throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_NOT_EXIST);
        }
    }

    @Override
    public void delete(Long id,Long tenantId) {
        super.deleteById(id);
        ArrayList list = new ArrayList();
        iDeviceTypeToServiceModuleService.listByDeviceTypeId(id,tenantId).forEach(m->{
            list.add(m.getId());
        });
        iDeviceTypeToServiceModuleService.delete(list);
    }

    public Page<DeviceTypeResp> getDeviceTypeByCondition(@RequestBody DeviceTypeReq req) {
        AssertUtils.notEmpty(req, "deviceType.notnull");
        Page<DeviceTypeResp> page = new Page<>();
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();
        if (pageNum < 1 || pageSize <= 0 || pageSize > 100) {
            throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_PAGE_ILLEGAL);
        }
        EntityWrapper<DeviceType> wrapper = new EntityWrapper<>();
        if (req.getDeviceCatalogId() != null) {
            wrapper.eq("a.device_catalog_id", req.getDeviceCatalogId());
        }
        if (req.getTenantId() != null) {
            wrapper.eq("a.tenant_id", req.getTenantId());
        }else {
            wrapper.eq("a.tenant_id", SaaSContextHolder.currentTenantId());
        }
        //如果传参为-1则过滤不支持ifttt的设备类型
        if (req.getIftttType()!=null &&  req.getIftttType() == -1 ){
            wrapper.ne("a.ifttt_type", "0");
        }

        if (!StringUtils.isEmpty(req.getSearchValues())) {
            //如果传参为-1, 则为套包设备类型查询
            if (req.getIftttType()!=null &&  req.getIftttType() == -1 ){
                if (StringUtil.isNotBlank(req.getSearchValues())) {
                    wrapper.like("a.name", req.getSearchValues(), SqlLike.DEFAULT);
                }
            }else {
                wrapper.andNew(true, "")
                        .like("a.name", req.getSearchValues(), SqlLike.DEFAULT)
                        .or().like("b.name", req.getSearchValues(), SqlLike.DEFAULT)
                        .or().like("a.type", req.getSearchValues(), SqlLike.DEFAULT);
            }

        }
        wrapper.orderBy(true, "a.id", false);
        com.baomidou.mybatisplus.plugins.Page selectPage = new com.baomidou.mybatisplus.plugins.Page(req.getPageNum(), req.getPageSize());

        List<DeviceType> types = deviceTypeMapper.selectDeviceTypePage(selectPage, wrapper);
        List<DeviceTypeResp> res = new ArrayList<DeviceTypeResp>();

        types.forEach((type) -> {
            DeviceTypeResp r = new DeviceTypeResp();
            BeanUtils.copyProperties(type, r);
            res.add(r);
        });
        page.setResult(res);
        page.setTotal(selectPage.getTotal());
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPages(selectPage.getPages());
        return page;
    }

    @Override
    public List<DeviceType> findListByDeviceTypeIds(List<Long> deviceTypeIds) {
        List<DeviceType> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceTypeIds)) {
            return resultDataList;
        }
        resultDataList = super.selectBatchIds(deviceTypeIds);
        return resultDataList;
    }

    @Override
    public DeviceType getDeviceTypeById(Long deviceTypeId) {
        return super.selectById(deviceTypeId);
    }

    @Override
    public DeviceType getDeviceTypeByTypeModel(String typeModel) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("type", typeModel);
        return super.selectOne(wrapper);
    }

    @Override
    public List<DeviceTypeResp> getByIds(List<Long> deviceTypeIds) {
        return deviceTypeMapper.getByIds(deviceTypeIds);
    }

    /**
     * @despriction：根据ifttt类型过滤
     * @author  yeshiyuan
     * @created 2018/11/23 17:21
     */
    @Override
    public List<DeviceTypeResp> getByIdsAndIfffType(List<Long> ids, String iftttType) {
        return deviceTypeMapper.getByIdsAndIfffType(ids, iftttType);
    }

    @Override
    public List<String> getDeviceTypeNameByIds(List<Long> deviceTypeIds) {
        return deviceTypeMapper.getDeviceTypeNameByIds(deviceTypeIds);
    }
    /**
     *@description 根据设备类型id，获取设备类型信息
     *@author wucheng
     *@params [deviceTypeIds]
     *@create 2018/12/12 10:56
     *@return java.util.List<com.iot.device.vo.rsp.DeviceTypeResp>
     */
    @Override
    public List<DeviceTypeResp> getDeviceTypeIdAndNameByIds(List<Long> deviceTypeIds) {
        return deviceTypeMapper.getDeviceTypeIdAndNameByIds(deviceTypeIds);
    }
}
