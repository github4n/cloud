package com.iot.tenant.vo.req;

/**
 * 描述：获取租户信息请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/23 13:55
 */
public class GetTenantReq {
    private Integer pageNum;
    private Integer pageSize;
    private String name;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
