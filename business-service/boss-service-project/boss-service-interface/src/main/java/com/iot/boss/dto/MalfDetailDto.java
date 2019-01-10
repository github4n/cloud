package com.iot.boss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：用户报障详情
 * 创建人： yeshiyuan
 * 创建时间：2018/5/16 11:24
 * 修改人： yeshiyuan
 * 修改时间：2018/5/16 11:24
 * 修改描述：
 */
public class MalfDetailDto {
    /**
     * 主键id
     */
    private Long malfId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 管理员确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date confirmTime;

    /**
     * 运维处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date handleTime;


    /**
     * 修复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date renovateTime;

    /**
     * 处理状态 0：创建 1：处理中 2：处理完成 3：修复完毕
     */
    private Integer handleStatus;

    /**
     * 处理详情
     */
    private List<MalfProcesLogDto> list;


    public Long getMalfId() {
        return malfId;
    }

    public void setMalfId(Long malfId) {
        this.malfId = malfId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Date getRenovateTime() {
        return renovateTime;
    }

    public void setRenovateTime(Date renovateTime) {
        this.renovateTime = renovateTime;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public List<MalfProcesLogDto> getList() {
        return list;
    }

    public void setList(List<MalfProcesLogDto> list) {
        this.list = list;
    }
}
