package com.iot.building.template.service;

import com.iot.building.template.domain.Template;
import com.iot.building.template.vo.req.*;
import com.iot.building.template.vo.rsp.DeviceTarValueResp;
import com.iot.building.template.vo.rsp.SceneTemplateResp;
import com.iot.building.template.vo.rsp.TemplateResp;
import com.iot.building.template.vo.rsp.TemplateVoResp;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import java.util.List;

public interface TemplateService {

    /**
     * 	删除模板
     *
     * @param templateId
     * @return
     */
    void delTemplate(Long templateId);

    /**
     * 描述：查询产品类型目标值
     *
     * @param templateId
     * @return
     * @author wujianlong
     * @created 2017年11月15日 上午9:54:54
     * @since
     */
    public List<DeviceTarValueResp> findDeviceTarValueList(Long templateId);

    /**
     * 描述：查询单个情景模板
     *
     * @param templateId
     * @return
     * @author wujianlong
     * @created 2017年11月15日 上午11:41:38
     * @since
     */
    public SceneTemplateResp getSceneTemplate(Long templateId);

    /**
     * 描述：删除情景
     *
     * @param templateId
     * @author wujianlong
     * @created 2017年11月15日 下午3:12:39
     * @since
     */
    public void deleteSceneTemplate(Long templateId) throws BusinessException;

    /**
     * 描述：更新情景
     *
     * @author wujianlong
     * @created 2017年11月15日 上午11:59:55
     * @since=
     */
    public void updateSceneTemplate(TemplateDetailReq templateDetail, Long userName, Long tenantId);

    /**
     *  根据 情景模板 创建对应的 情景(scene、sceneDetail)
     *
     * @param createSceneFromTemplateReq
     * @return
     */
    //Long createSceneFromTemplate(CreateSceneFromTemplateReq createSceneFromTemplateReq) throws BusinessException;
    
    /**
     *  根据 情景模板 删除对应的 情景(scene、sceneDetail)
     *
     */
    void delSceneFromTemplate(SpaceTemplateReq spaceTemplateReq) throws BusinessException;

    /**
     *  保存 模板主表、情景模板(2B 业务)
     *
     * @param buildTemplateReq
     */
    void buildSceneTemplate2B(BuildTemplateReq buildTemplateReq) throws Exception;


    /**
     * 	根据 模板名、模板类型 分页获取 TemplateVO列表
     *
     * @param name
     * @param templateType
     * @param pageNum
     * @param pageSize
     * @param tenantId
     * @return
     */
    Page<TemplateResp> findTemplateList(String name, String templateType, int pageNum, int pageSize, Long tenantId, Long locationId);

    /**
     *  根据 情景模板 创建对应的 情景(scene、sceneDetail)
     *
     * @param createSceneFromTemplateReq
     * @return
     */
    Long createSceneFromTemplate(CreateSceneFromTemplateReq createSceneFromTemplateReq) throws BusinessException;


    /**
     * 	根据 productId获取 Template(理论上 一个productId 只有一条记录)
     *
     * @param productId
     * @param tenantId
     * @return
     */
    Template getByProductId(Long productId,Long orgId, Long tenantId);

    /**
     * 描述：查询空间模板列表
     *
     * @param templateReq
     * @return
     * @author wujianlong
     * @created 2018年5月23日 上午11:41:38
     * @since
     */
    public List<TemplateResp> findSceneSpaceTemplateList(TemplateReq templateReq);

    /**
     * @param productModel
     * @Description:获取模板
     * @return:
     * @author: chq
     * @date: 2018/6/26 15:24
     **/
    public TemplateVoResp getTempaltesByModel(String productModel,Long orgId);

    public List<TemplateResp> findSceneTemplateList(TemplateReq templateReq);
    
    public List<TemplateResp> findTemplateList(TemplateReq templateReq);
    
    public void saveSpaceDetailSceneByTemplate(TemplateReq template);
}
