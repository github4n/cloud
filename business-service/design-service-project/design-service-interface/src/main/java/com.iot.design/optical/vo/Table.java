package com.iot.design.optical.vo;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/7
 */
public class Table {
    private Float tableLength;//桌子的长

    private Float tableWidth;//桌子的宽

    private Float tableHeight;//桌子的高

    private Float tbaleCenterX;//桌子中点的X坐标

    private Float tbaleCenterY;//桌子中点的Y坐标

    private Float tbaleCenterZ;//桌子中点的Z坐标

    public Float getTableLength() {
        return tableLength;
    }

    public void setTableLength(Float tableLength) {
        this.tableLength = tableLength;
    }

    public Float getTableWidth() {
        return tableWidth;
    }

    public void setTableWidth(Float tableWidth) {
        this.tableWidth = tableWidth;
    }

    public Float getTableHeight() {
        return tableHeight;
    }

    public void setTableHeight(Float tableHeight) {
        this.tableHeight = tableHeight;
    }

    public Float getTbaleCenterX() {
        return tbaleCenterX;
    }

    public void setTbaleCenterX(Float tbaleCenterX) {
        this.tbaleCenterX = tbaleCenterX;
    }

    public Float getTbaleCenterY() {
        return tbaleCenterY;
    }

    public void setTbaleCenterY(Float tbaleCenterY) {
        this.tbaleCenterY = tbaleCenterY;
    }

    public Float getTbaleCenterZ() {
        return tbaleCenterZ;
    }

    public void setTbaleCenterZ(Float tbaleCenterZ) {
        this.tbaleCenterZ = tbaleCenterZ;
    }

    public Table(Float tableLength, Float tableWidth, Float tableHeight, Float tbaleCenterX, Float tbaleCenterY, Float tbaleCenterZ) {
        this.tableLength = tableLength;
        this.tableWidth = tableWidth;
        this.tableHeight = tableHeight;
        this.tbaleCenterX = tbaleCenterX;
        this.tbaleCenterY = tbaleCenterY;
        this.tbaleCenterZ = tbaleCenterZ;
    }

    public Table() {
    }
}
