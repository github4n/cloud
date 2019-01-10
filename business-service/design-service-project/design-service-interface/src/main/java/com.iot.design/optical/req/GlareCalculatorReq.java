package com.iot.design.optical.req;

import java.util.List;
import java.util.Map;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/17
 */
public class GlareCalculatorReq {
    private Float[] testPoint; //测试点的坐标
    private List<Float[]> lightPoint; //光源坐标
    private Float[] lightParams;//光源参数
    private String ies;//ies文件
    private List<Map<String, Object>> coordinateList; //房间的光照度

    public GlareCalculatorReq() {
    }

    public GlareCalculatorReq(Float[] testPoint, List<Float[]> lightPoint, Float[] lightParams, String ies, List<Map<String, Object>> coordinateList) {
        this.testPoint = testPoint;
        this.lightPoint = lightPoint;
        this.lightParams = lightParams;
        this.ies = ies;
        this.coordinateList = coordinateList;
    }

    public Float[] getTestPoint() {
        return testPoint;
    }

    public void setTestPoint(Float[] testPoint) {
        this.testPoint = testPoint;
    }

    public List<Float[]> getLightPoint() {
        return lightPoint;
    }

    public void setLightPoint(List<Float[]> lightPoint) {
        this.lightPoint = lightPoint;
    }

    public Float[] getLightParams() {
        return lightParams;
    }

    public void setLightParams(Float[] lightParams) {
        this.lightParams = lightParams;
    }

    public String getIes() {
        return ies;
    }

    public void setIes(String ies) {
        this.ies = ies;
    }

    public List<Map<String, Object>> getCoordinateList() {
        return coordinateList;
    }

    public void setCoordinateList(List<Map<String, Object>> coordinateList) {
        this.coordinateList = coordinateList;
    }
}
