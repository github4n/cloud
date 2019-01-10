package com.iot.ifttt.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * this组表
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public class AppletThis implements Serializable {

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
     * 逻辑 or/and
     */
    private String logic;
    /**
     * 传参
     */
    private String param;


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

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "AppletThis{" +
                ", id=" + id +
                ", appletId=" + appletId +
                ", serviceCode=" + serviceCode +
                ", logic=" + logic +
                ", param=" + param +
                "}";
    }
}
