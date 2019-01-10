package com.iot.building.template.service;

import com.iot.building.template.domain.TemplateDetail;
import com.iot.building.template.vo.rsp.DeviceTarValueResp;
import com.iot.building.template.vo.rsp.TemplateDetailResp;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TemplateDetailService {
    /**
     * 	插入模板详情
     *
     * @param templateDetail
     * @return
     */
    int insert(TemplateDetail templateDetail);

    /**
     * 	根据 templateId 删除模板详情
     *
     * @param templateId
     */
    void delTemplateDetail(Long templateId);

    /**
     * 	根据 templateId 获取TemplateDetail列表
     *
     * @param templateId
     * @param tenantId
     * @return
     */
    List<TemplateDetail> findByTemplateId(Long templateId, Long tenantId);

    /**
     * 	根据 templateId、productId获取TemplateDetail
     *
     * @param templateId
     * @param productId
     * @param tenantId
     * @return
     */
    TemplateDetail getByTemplateIdAndProductId(Long templateId, Long productId, Long tenantId);


    /**
     * 	查询房间中是否绑定模板
     *
     * @param templateId
     * @return
     */
    List<String> getRoomByTemplateId(Long templateId, String templateType);

    /**
     *  获取 模板详情目标值
     *
     * @param templateId
     * @return
     */
    List<DeviceTarValueResp> findDeviceTargetValueList(Long templateId);

    /**
     * 	根据 templateId 获取模板详情列表
     *
     * @param templateId
     * @return
     */
    List<TemplateDetailResp> findTemplateDetailList(@Param("templateId") Long templateId);
}
