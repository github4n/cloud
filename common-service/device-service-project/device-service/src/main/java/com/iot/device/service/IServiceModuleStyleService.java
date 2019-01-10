package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ServiceModuleStyle;
import com.iot.device.vo.req.ServiceModuleStyleReq;
import com.iot.device.vo.rsp.ServiceModuleStyleResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-样式表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceModuleStyleService extends IService<ServiceModuleStyle> {

    /**
     * 增加或修改
     * @param serviceModuleStyleReq
     * @return
     */
    Long saveOrUpdate(ServiceModuleStyleReq serviceModuleStyleReq);


    /**
     * 删除
     * @param id
     */
    void delete(ArrayList<Long> id);


    List<ServiceModuleStyleResp> list(ArrayList serviceModuleId);

    List<ServiceModuleStyleResp> listByStyleTemplateId(Long styleTemplateId);
}
