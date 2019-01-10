package com.iot.airswitch.vo.resp;

import java.util.Date;
import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/7
 * @Description: *
 */
public class SwitchElectricityStatisticsTopResp {

    public List<TopData> list;

    public static class TopData {

        /**
         * 设备ID
         */
        private String deviceId;

        /**
         * 设备名称
         */
        private String deviceName;

        /**
         * 空间ID
         */
        private Long spaceId;

        /**
         * 空间名称
         */
        private String spaceName;

        /**
         * 用途
         */
        private String useage;

        /**
         * 数量
         */
        private Long count;

        /**
         * 告警原因
         */
        private String reason;

        /**
         * 时间
         */
        private Date date;

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getSpaceName() {
            return spaceName;
        }

        public void setSpaceName(String spaceName) {
            this.spaceName = spaceName;
        }

        public String getUseage() {
            return useage;
        }

        public void setUseage(String useage) {
            this.useage = useage;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Long getSpaceId() {
            return spaceId;
        }

        public void setSpaceId(Long spaceId) {
            this.spaceId = spaceId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public List<TopData> getList() {
        return list;
    }

    public void setList(List<TopData> list) {
        this.list = list;
    }
}
