package com.iot.systempush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.message.api.MessageApi;
import com.iot.message.dto.SystemPushControlDto;
import com.iot.message.entity.SystemPushControl;
import com.iot.saas.SaaSContextHolder;
import com.iot.systempush.service.SystemPushBusinessService;
import com.iot.systempush.vo.SystemPushControlVo;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：系统推送控制
 * 创建人： 李帅
 * 创建时间：2018年11月19日 下午2:21:10
 * 修改人：李帅
 * 修改时间：2018年11月19日 下午2:21:10
 */
@Service("systemPushBusiness")
public class SystemPushBusinessServiceImpl implements SystemPushBusinessService {


    @Autowired
    private MessageApi messageApi;

    /**
     * 
     * 描述：查询系统推送控制
     * @author 李帅
     * @created 2018年11月19日 下午2:18:36
     * @since 
     * @param appId
     * @return
     */
    @Override
    public String getSystemPushControl(Long appId) {
    	SystemPushControl systemPushControl = this.messageApi.getSystemPushControl(SaaSContextHolder.getCurrentUserUuid(), appId);
    	if(systemPushControl != null){
    		return systemPushControl.getSwitCh();
    	}
        return null;
    }

    /**
     * 
     * 描述：添加或修改系统推送控制
     * @author 李帅
     * @created 2018年11月19日 下午2:31:48
     * @since 
     * @param systemPushControlVo
     */
    @Override
    public void addSystemPushControl(SystemPushControlVo systemPushControlVo) {
    	SystemPushControlDto systemPushControlDto = new SystemPushControlDto();
    	systemPushControlDto.setAppId(systemPushControlVo.getAppId());
    	systemPushControlDto.setCreateBy(SaaSContextHolder.getCurrentUserId());
    	systemPushControlDto.setSwitCh(systemPushControlVo.getSwitCh());
    	systemPushControlDto.setTenantId(SaaSContextHolder.currentTenantId());
    	systemPushControlDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
        this.messageApi.addSystemPushControl(systemPushControlDto);
    }
}
