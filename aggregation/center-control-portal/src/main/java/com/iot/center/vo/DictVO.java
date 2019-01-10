package com.iot.center.vo;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/31
 */
public class DictVO {
    private String name;
    private Long code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
    public DictVO() {
    }
    public DictVO( Long code,String name) {
        this.name = name;
        this.code = code;
    }
}
