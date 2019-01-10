package com.iot.device.vo.req;

import com.iot.common.beans.SearchParam;

import java.io.Serializable;
import java.util.Date;

public class GenerateModuleReq extends SearchParam implements Serializable {

    private Long id;

    private String customer;

    private String deviceTypeName;

    private String agreement;

    private String gF;

    private String deviceTypeInfo;

    private String gN;

    private String code;

    private Long number;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private String codeNumber;

    private String describes;





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getgF() {
        return gF;
    }

    public void setgF(String gF) {
        this.gF = gF;
    }

    public String getDeviceTypeInfo() {
        return deviceTypeInfo;
    }

    public void setDeviceTypeInfo(String deviceTypeInfo) {
        this.deviceTypeInfo = deviceTypeInfo;
    }

    public String getgN() {
        return gN;
    }

    public void setgN(String gN) {
        this.gN = gN;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
