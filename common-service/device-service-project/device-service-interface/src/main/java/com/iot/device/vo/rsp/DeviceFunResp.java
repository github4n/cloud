package com.iot.device.vo.rsp;

import java.io.Serializable;

/**
     * 设备对应的功能类型
     */
    public class DeviceFunResp implements Serializable {
        /**
         * 功能id
         */
        private Long dataPointId;

        /**
         * 功能名称
         */
        private String dataPointName;

        /**
         * 属性
         */
        private String property;
        /**
         * 属性编码code
         */
        private String propertyCode;

        /**
         * 读写类型(0 r, 1 w, 2 rw)
         */
        private int mode;

        /**
         * 数值类型(0 boolean，1 int，2 unsignedInt，3.枚举型，4故障型，5字符型，6 RAW型）
         */
        private int dataType;

        /**
         * 图标
         */
        private String iconName;
        /**
         * 描述
         */
        private String description;

        public Long getDataPointId() {
            return dataPointId;
        }

        public void setDataPointId(Long dataPointId) {
            this.dataPointId = dataPointId;
        }

        public String getDataPointName() {
            return dataPointName;
        }

        public void setDataPointName(String dataPointName) {
            this.dataPointName = dataPointName;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getPropertyCode() {
            return propertyCode;
        }

        public void setPropertyCode(String propertyCode) {
            this.propertyCode = propertyCode;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public String getIconName() {
            return iconName;
        }

        public void setIconName(String iconName) {
            this.iconName = iconName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }