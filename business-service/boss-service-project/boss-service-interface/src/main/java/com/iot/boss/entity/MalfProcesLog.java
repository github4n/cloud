package com.iot.boss.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：报障处理流程记录实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 16:45
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 16:45
 * 修改描述：
 */
public class MalfProcesLog {

    /**
     * 主键
     */
    private Long id;

    /**
     * 报障单id
     */
    private Long malfId;

    /**
     * 处理类型  0：确认故障  1：处理完毕 2：确认已修复 3：确认未修复
     */
    private Integer handleType;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date handleTime;

    /**
     * 处理人留言
     */
    private String handleMsg;

    /**
     * 处理人ID
     */
    private Long handleAdminId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMalfId() {
        return malfId;
    }

    public void setMalfId(Long malfId) {
        this.malfId = malfId;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public Long getHandleAdminId() {
        return handleAdminId;
    }

    public void setHandleAdminId(Long handleAdminId) {
        this.handleAdminId = handleAdminId;
    }


    public MalfProcesLog() {

    }

    public MalfProcesLog(Long id, Long malfId) {
        this.id = id;
        this.malfId = malfId;
    }

    public MalfProcesLog(Long id, Long malfId, Integer handleType, Date handleTime, String handleMsg, Long handleAdminId) {
        this.id = id;
        this.malfId = malfId;
        this.handleType = handleType;
        this.handleTime = handleTime;
        this.handleMsg = handleMsg;
        this.handleAdminId = handleAdminId;
    }
}
