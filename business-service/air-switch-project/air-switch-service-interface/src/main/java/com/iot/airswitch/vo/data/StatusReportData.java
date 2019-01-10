package com.iot.airswitch.vo.data;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public class StatusReportData {

    private String TIME;

    private List<StatusReport> list;

    public static class StatusReport {

        public StatusReport() {}

        /**
         * 节点号 1~30
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
         */
        private String TYPE;

        /**
         * 电压值(V)
         */
        private Integer VOL;

        /**
         * 漏电流值(0.1mA)
         */
        private Integer LKI;

        /**
         * 功率值(W)
         */
        private Integer PWR;

        /**
         * 温度值(0.1℃)
         */
        private Integer TMP;

        /**
         * 电流值(10mA)
         */
        private Integer CUR;

        /**
         * N相电流值
         */
        private String CUR2;

        /**
         * 报警位
         */
        private String ALM;

        /**
         * 累计电量
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

        public Integer getPWR() {
            return PWR;
        }

        public void setPWR(Integer PWR) {
            this.PWR = PWR;
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

        public String getCUR2() {
            return CUR2;
        }

        public void setCUR2(String CUR2) {
            this.CUR2 = CUR2;
        }

        public String getALM() {
            return ALM;
        }

        public void setALM(String ALM) {
            this.ALM = ALM;
        }

        public Integer getPS() {
            return PS;
        }

        public void setPS(Integer PS) {
            this.PS = PS;
        }

        /**
         * 漏电流（mA）
         */
        public Integer getLKIActual() {
            return LKI/10;
        }

        /**
         * 温度值
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

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public List<StatusReport> getList() {
        return list;
    }

    public void setList(List<StatusReport> list) {
        this.list = list;
    }
}
