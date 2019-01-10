package com.iot.boss.service.uuid;

import com.iot.boss.vo.TemplateVoResp;

/**
 * 描述：租户管理
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/22 17:04
 */
public interface IftttService {

    /**
     * 描述：获取预设模板
     *
     * @param productModel
     * @author chq
     * @created 2018年6月28日 下午2:37:31
     * @since
     */
    TemplateVoResp getTempaltes(String productModel);

}
