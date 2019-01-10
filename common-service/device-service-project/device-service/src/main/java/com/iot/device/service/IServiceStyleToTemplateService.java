package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ServiceStyleToTemplate;
import com.iot.device.vo.req.ServiceStyleToTemplateReq;
import com.iot.device.vo.rsp.ServiceStyleToTemplateResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-样式-to-模板样式表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceStyleToTemplateService extends IService<ServiceStyleToTemplate> {

    /**
     * 增加或者修改
     * @param serviceStyleToTemplateReq
     * @return
     */
    Long saveOrUpdate(ServiceStyleToTemplateReq serviceStyleToTemplateReq);

    public void saveMore(ServiceStyleToTemplateReq serviceStyleToTemplateReq);

    /**
     * 删除
     * @param id
     */
    void delete(ArrayList<Long> id);

    /**
     * 根据moduleStyleId查询
     * @param moduleStyleIds
     * @return
     */
    List<ServiceStyleToTemplateResp> list(ArrayList<Long> moduleStyleIds);


}
