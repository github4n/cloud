package com.iot.airswitch.vo.data;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public class RegisterData {

    /**
     * 节点号
     */
    private Integer NNO;

    /**
     * 产品类型（88, 89, 8A, 8B）
     */
    private String TYPE;

    /**
     * 版本
     */
    private String VER;

    /**
     * 设备型号
     */
    private String Model;

    public Integer getNNO() {
        return NNO;
    }

    public void setNNO(Integer NNO) {
        this.NNO = NNO;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getVER() {
        return VER;
    }

    public void setVER(String VER) {
        this.VER = VER;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }
}
