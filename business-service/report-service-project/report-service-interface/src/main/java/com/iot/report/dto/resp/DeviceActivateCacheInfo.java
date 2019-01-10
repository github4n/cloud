package com.iot.report.dto.resp;

public class DeviceActivateCacheInfo {

    private String countryCode;

    private String province;

    private String city;

    public DeviceActivateCacheInfo() {
    }

    public DeviceActivateCacheInfo(String countryCode, String province, String city) {
        this.countryCode = countryCode;
        this.province = province;
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
