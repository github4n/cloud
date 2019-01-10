package com.iot.control.packagemanager.vo.req;

import com.iot.common.beans.SearchParam;

/**
 * 项目名称：IOT云平台
 * 模块名称：场景
 * 功能描述：场景
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public class SearchSceneInfoReq extends SearchParam {

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 套包id
     */
    private Long packageById;


    public SearchSceneInfoReq() {

    }

    public SearchSceneInfoReq(Long tenantId, Long packageById) {
        this.tenantId = tenantId;
        this.packageById = packageById;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getPackageById() {
        return packageById;
    }

    public void setPackageById(Long packageById) {
        this.packageById = packageById;
    }
}