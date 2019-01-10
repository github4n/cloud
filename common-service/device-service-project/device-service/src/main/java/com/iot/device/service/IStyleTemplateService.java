package com.iot.device.service;

import com.github.pagehelper.PageInfo;
import com.iot.device.model.StyleTemplate;
import com.baomidou.mybatisplus.service.IService;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.StyleTemplateResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IStyleTemplateService extends IService<StyleTemplate> {

    /**
     * 增加
     * @param styleTemplateReq
     */
     Long saveOrUpdate(StyleTemplateReq styleTemplateReq);

    /**
     * 删除
     * @param ids
     */
     void delete(ArrayList<Long> ids);

    /**
     * 获取
     * @return
     */
     PageInfo<StyleTemplateResp> list(StyleTemplateReq styleTemplateReq);

    /**
     * 根据产品id获取
     * @param deviceTypeId
     * @return
     */
     List<StyleTemplateResp> listByDeviceTypeId(Long deviceTypeId);


     List<StyleTemplateResp> listByModuleStyleId(Long moduleStyleId);

    List<StyleTemplateResp> listByProductId(Long productId);

    /**
     * 根据Id获取
     * @param id
     * @return
     */
    StyleTemplateResp infoById(Long id);

}
