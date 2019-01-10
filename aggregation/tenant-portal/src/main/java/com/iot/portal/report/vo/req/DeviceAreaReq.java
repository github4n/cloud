package com.iot.portal.report.vo.req;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：地区分布请求参数
 * 创建人： maochengyuan
 * 创建时间：2019/1/4 11:43
 * 修改人： maochengyuan
 * 修改时间：2019/1/4 11:43
 * 修改描述：
 */
public class DeviceAreaReq {

    /**国家代码*/
    private String countryCode;

    /**省份代码*/
    private String provinceCode;

    /**查询周期（1:前一天，7：近7天，14：近14天，30：近30天）*/
    private String cycle;

    public DeviceAreaReq() {

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

}
