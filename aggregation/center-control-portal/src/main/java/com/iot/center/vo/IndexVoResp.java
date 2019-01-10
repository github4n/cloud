package com.iot.center.vo;

import java.io.Serializable;
import java.util.List;

import com.iot.building.index.vo.IndexDetailResp;

public class IndexVoResp implements Serializable {

    private int type;

    private Long id;

    private String title;

    private int enable;//0.不启用，1启用

    private List<IndexDetailResp> dataList;

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IndexDetailResp> getDataList() {
        return dataList;
    }

    public void setDataList(List<IndexDetailResp> dataList) {
        this.dataList = dataList;
    }
}
