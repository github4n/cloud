package com.iot.tenant.vo.req;

/**
 * 描述：分页获取app请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/5 17:48
 */
public class GetAppReq {
    private int pageNum;
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "AppReviewSearchReq{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
