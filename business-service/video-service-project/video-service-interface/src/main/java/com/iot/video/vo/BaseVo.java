package com.iot.video.vo;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：服务层
 * 功能描述：服务层基础VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 14:43
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 14:43
 * 修改描述：
 */
public class BaseVo {

    /**租户id*/
    private Long tenantId;

    /**用户id*/
    private String userId;

    public BaseVo() {

    }

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
