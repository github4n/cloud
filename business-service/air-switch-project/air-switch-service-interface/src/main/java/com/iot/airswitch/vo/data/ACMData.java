package com.iot.airswitch.vo.data;


import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/22
 * @Description: *
 */
public class ACMData {

    /**
     * 序列号
     */
    private String ASN;

    /**
     * 节点数
     */
    private Integer NUM;

    /**
     * 类型（F1 按小时统计  F2 按天统计）
     */
    private String TYPE;

    /**
     * 本地时间
     */
    private String TIME;


    private List<ACMNodeData> data;

    public static class ACMNodeData {

        /**
         * 节点号（1~30）
         */
        private String NNO;

        /**
         * 类型（80,84,85,86,87）
         */
        private String TYPE;

        /**
         * 统计次数
         */
        private Integer DN;

        /**
         * 电压值（V）
         */
        private Integer VOL;

        /**
         * 漏电流值（0.1mA）
         */
        private Integer LKI;

        /**
         * 温度值（0.1℃）
         */
        private Integer TMP;

        /**
         * 电流值（10mA）
         */
        private Integer CUR;

        /**
         * 区间电量
         */
        private Integer PS;


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

        public Integer getDN() {
            return DN;
        }

        public void setDN(Integer DN) {
            this.DN = DN;
        }

        public Integer getVOL() {
            return VOL;
        }

        public void setVOL(Integer VOL) {
            this.VOL = VOL;
        }

        public Integer getLKI() {
            return LKI;
        }

        public void setLKI(Integer LKI) {
            this.LKI = LKI;
        }

        public Integer getTMP() {
            return TMP;
        }

        public void setTMP(Integer TMP) {
            this.TMP = TMP;
        }

        public Integer getCUR() {
            return CUR;
        }

        public void setCUR(Integer CUR) {
            this.CUR = CUR;
        }

        public Integer getPS() {
            return PS;
        }

        public void setPS(Integer PS) {
            this.PS = PS;
        }

        /**
         * 漏电流值（mA）
         */
        public Integer getLKIActual() {
            return LKI/10;
        }

        /**
         * 温度值℃
         */
        public Integer getTMPActual() {
            return TMP/10;
        }

        /**
         * 电流值（mA）
         */
        public Integer getCURActual() {
            return CUR*10;
        }
    }

    public String getASN() {
        return ASN;
    }

    public void setASN(String ASN) {
        this.ASN = ASN;
    }

    public Integer getNUM() {
        return NUM;
    }

    public void setNUM(Integer NUM) {
        this.NUM = NUM;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public List<ACMNodeData> getData() {
        return data;
    }

    public void setData(List<ACMNodeData> data) {
        this.data = data;
    }
}
