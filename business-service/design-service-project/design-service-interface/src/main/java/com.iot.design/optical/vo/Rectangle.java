package com.iot.design.optical.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 长方体
 *
 * @author fenglijian
 */
public class Rectangle implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7357623164932151506L;

    private Float length;

    private Float width;

    private Float high;

    private Float unitLength;

    private Float unitWidth;

    private Float refractivity;  // 折射率

    private String fileId;  // 文件Id

    private List<Table> tables = new ArrayList<>();
    private List<LightSource> lightSources = new ArrayList<>();

    private String tableFlag;//桌子的标志

    public String getTableFlag() {
        return tableFlag;
    }

    public void setTableFlag(String tableFlag) {
        this.tableFlag = tableFlag;
    }


    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHigh() {
        return high;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public Float getUnitLength() {
        return unitLength;
    }

    public void setUnitLength(Float unitLength) {
        this.unitLength = unitLength;
    }

    public Float getUnitWidth() {
        return unitWidth;
    }

    public void setUnitWidth(Float unitWidth) {
        this.unitWidth = unitWidth;
    }

    public Float getRefractivity() {
        return refractivity;
    }

    public void setRefractivity(Float refractivity) {
        this.refractivity = refractivity;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public List<LightSource> getLightSources() {
        return lightSources;
    }

    public void setLightSources(List<LightSource> lightSources) {
        this.lightSources = lightSources;
    }
}
