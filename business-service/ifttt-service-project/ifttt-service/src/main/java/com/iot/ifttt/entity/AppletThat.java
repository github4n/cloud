package com.iot.ifttt.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * that组表
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public class AppletThat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 程序主键
     */
    private Long appletId;
    /**
     * 服务标识
     */
    private String serviceCode;
    /**
     * 触发码
     */
    private String code;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppletId() {
        return appletId;
    }

    public void setAppletId(Long appletId) {
        this.appletId = appletId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "AppletThat{" +
                ", id=" + id +
                ", appletId=" + appletId +
                ", serviceCode=" + serviceCode +
                ", code=" + code +
                "}";
    }
}
