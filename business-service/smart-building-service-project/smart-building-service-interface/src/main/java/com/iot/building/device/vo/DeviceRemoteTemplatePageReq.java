package com.iot.building.device.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板主表
 */
public class DeviceRemoteTemplatePageReq  implements Serializable {

    private Integer pageNum =1;

    private Integer pageSize =10;

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
}
