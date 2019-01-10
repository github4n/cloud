package com.iot.vo;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiModel;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：聚合层基础带分页VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/26 18:48
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/26 18:48
 * 修改描述：
 */
@ApiModel
public class BasePageVO extends SearchParam {

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户id
     */
    private String userId;
    
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
