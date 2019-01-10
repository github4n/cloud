package com.iot.design.optical.vo;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/7
 */
public class LightSource {
    private Float x;//灯的X坐标

    private Float y;//灯的Y坐标

    private Float z;//灯的Z坐标

    private Float directionX;//方向向量X

    private Float directionY;//方向向量Y

    private Float directionZ;//方向向量Z

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public Float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(Float directionX) {
        this.directionX = directionX;
    }

    public Float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(Float directionY) {
        this.directionY = directionY;
    }

    public Float getDirectionZ() {
        return directionZ;
    }

    public void setDirectionZ(Float directionZ) {
        this.directionZ = directionZ;
    }

    public LightSource(Float[] lightSource) {
        this.x = lightSource[0];
        this.y = lightSource[1];
        this.z = lightSource[2];
        this.directionX = lightSource[3];
        this.directionY = lightSource[4];
        this.directionZ = lightSource[5];
    }

    public LightSource() {
    }

    public Float[] toFloat() {
        return new Float[]{this.x, this.z, this.y, this.directionX, this.directionY, this.directionZ};
    }

}
