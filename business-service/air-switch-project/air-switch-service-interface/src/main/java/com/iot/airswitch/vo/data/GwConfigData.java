package com.iot.airswitch.vo.data;

import com.iot.airswitch.constant.CommGwTypeEnum;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public class GwConfigData {

    /**
     * 节点号
     */
    private String NNO;

    /**
     * 产品类型
     * 0x80-单相节点
     * 0x84-三相节点主路
     * 0x94-塑壳主路
     * 0x85-三相节点A相
     * 0x86-三相节点B相
     * 0x87-三相节点C相
     * 0x95-塑壳A相
     * 0x96-塑壳B相
     * 0x97-塑壳C相
     */
    private String TYPE;

    /**
     * 产品型号
     */
    private String MODEL;

    /**
     * VER
     */
    private String VER;

    /**
     * 序列号
     */
    private String SER;

    /**
     * 保留
     */
    private String REV;

    /**
     * 控制位
     * bit10~15 保留
     * bit9 禁止显示
     * bit8 禁用远程控制
     * bit0~7 电量目标节点号
     */
    private String CTRL;

    /**
     * 电压上限(V)
     */
    private Integer VOLH;

    /**
     * 电压下限(V)
     */
    private Integer VOLL;

    /**
     * 电压步长
     */
    private Integer VOLS;

    /**
     * 漏电流上限(0.1mA)
     */
    private Integer LKIH;

    /**
     * 漏电流步长(0.1mA)
     */
    private Integer LKIS;

    /**
     * 功率上限
     *  TYPE 为 塑壳产品  功率 20W 电流 0.1A
     *  TYPE 不为 塑壳产品 功率 1W 电流 10mA
     */
    private Integer PWRH;

    /**
     * 功率下限
     */
    private Integer PWRL;

    /**
     * 功率步长
     */
    private Integer PWRS;

    /**
     * 温度上限
     */
    private Integer TMPH;

    /**
     * 温度步长
     */
    private Integer TMPS;

    /**
     * 电流上限
     */
    private Integer CURH;

    /**
     * 电流步长
     */
    private Integer CURS;

    /**
     * 保留
     */
    private String REV1;

    /**
     * 漏电检测时间
     */
    private String LKDT;

    /**
     * 漏电检测设定
     */
    private String LKDI;

    /**
     * 保留
     */
    private String REV2;

    /**
     * 名称
     */
    private String NAME;


    public String getNNO() {
        return NNO;
    }

    public void setNNO(String NNO) {
        this.NNO = NNO;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getVER() {
        return VER;
    }

    public void setVER(String VER) {
        this.VER = VER;
    }

    public String getSER() {
        return SER;
    }

    public void setSER(String SER) {
        this.SER = SER;
    }

    public String getREV() {
        return REV;
    }

    public void setREV(String REV) {
        this.REV = REV;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCTRL() {
        return CTRL;
    }

    public void setCTRL(String CTRL) {
        this.CTRL = CTRL;
    }

    public Integer getVOLH() {
        return VOLH;
    }

    public void setVOLH(Integer VOLH) {
        this.VOLH = VOLH;
    }

    public Integer getVOLL() {
        return VOLL;
    }

    public void setVOLL(Integer VOLL) {
        this.VOLL = VOLL;
    }

    public Integer getVOLS() {
        return VOLS;
    }

    public void setVOLS(Integer VOLS) {
        this.VOLS = VOLS;
    }

    public Integer getLKIH() {
        return LKIH;
    }

    public void setLKIH(Integer LKIH) {
        this.LKIH = LKIH;
    }

    public Integer getLKIS() {
        return LKIS;
    }

    public void setLKIS(Integer LKIS) {
        this.LKIS = LKIS;
    }

    public Integer getPWRH() {
        return PWRH;
    }

    public void setPWRH(Integer PWRH) {
        this.PWRH = PWRH;
    }

    public Integer getPWRL() {
        return PWRL;
    }

    public void setPWRL(Integer PWRL) {
        this.PWRL = PWRL;
    }

    public Integer getPWRS() {
        return PWRS;
    }

    public void setPWRS(Integer PWRS) {
        this.PWRS = PWRS;
    }

    public Integer getTMPH() {
        return TMPH;
    }

    public void setTMPH(Integer TMPH) {
        this.TMPH = TMPH;
    }

    public Integer getTMPS() {
        return TMPS;
    }

    public void setTMPS(Integer TMPS) {
        this.TMPS = TMPS;
    }

    public Integer getCURH() {
        return CURH;
    }

    public void setCURH(Integer CURH) {
        this.CURH = CURH;
    }

    public Integer getCURS() {
        return CURS;
    }

    public void setCURS(Integer CURS) {
        this.CURS = CURS;
    }

    public String getREV1() {
        return REV1;
    }

    public void setREV1(String REV1) {
        this.REV1 = REV1;
    }

    public String getLKDT() {
        return LKDT;
    }

    public void setLKDT(String LKDT) {
        this.LKDT = LKDT;
    }

    public String getLKDI() {
        return LKDI;
    }

    public void setLKDI(String LKDI) {
        this.LKDI = LKDI;
    }

    public String getREV2() {
        return REV2;
    }

    public void setREV2(String REV2) {
        this.REV2 = REV2;
    }

    /**
     * 漏电流上限（mA）
     */
    public Integer getLKIHActual() {
        return LKIH/10;
    }

    /**
     * 功率上限（W）
     */
    public Integer getPWRHActual() {
        if (CommGwTypeEnum.isPlasticCase(TYPE)) {
            return PWRH*20;
        }
        return PWRH;
    }

    /**
     * 功率下限（W）
     */
    public Integer getPWRLActual() {
        if (CommGwTypeEnum.isPlasticCase(TYPE)) {
            return PWRL*20;
        }
        return PWRL;
    }

    /**
     * 温度上限（℃）
     */
    public Integer getTMPHActual() {
        return TMPH/10;
    }

    /**
     * 电流上限（A）
     */
    public Integer getCURHActual() {
        if (CommGwTypeEnum.isPlasticCase(TYPE)) {
            return CURH/10;
        }
        return CURH/100;
    }


}
