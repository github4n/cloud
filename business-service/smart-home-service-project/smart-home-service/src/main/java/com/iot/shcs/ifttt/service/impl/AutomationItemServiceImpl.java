package com.iot.shcs.ifttt.service.impl;

import com.google.common.collect.Maps;
import com.iot.shcs.ifttt.entity.AutomationItem;
import com.iot.shcs.ifttt.mapper.AutomationItemMapper;
import com.iot.shcs.ifttt.service.IAutomationItemService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 联动子项 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-15
 */
@Service
public class AutomationItemServiceImpl extends ServiceImpl<AutomationItemMapper, AutomationItem> implements IAutomationItemService {

    @Override
    public List<AutomationItem> getItemByParam(String type, String objectId,Long tenantId) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("type",type);
        params.put("object_id",objectId);
        params.put("tenant_id",tenantId);
        return selectByMap(params);
    }

    @Override
    public boolean delItemByParam(String type, String objectId,Long tenantId) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("type",type);
        params.put("object_id",objectId);
        params.put("tenant_id",tenantId);
        return deleteByMap(params);
    }

    @Override
    public boolean delItemByAppletId(Long appletId,Long tenantId) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("applet_id",appletId);
        params.put("tenant_id",tenantId);
        return deleteByMap(params);
    }

}
