package com.iot.boss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.boss.enums.MalfHandleTypeEnum;

import java.util.Date;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/16 11:47
 * 修改人： yeshiyuan
 * 修改时间：2018/5/16 11:47
 * 修改描述：
 */
public class MalfProcesLogDto {

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
     * 处理类型描述（0：确认故障  1：处理完毕 2：确认已修复 3：确认未修复）
     */
    private String handleTypeDesc;

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
     * 处理人真实姓名
     */
    private String nickName;

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

    public String getHandleTypeDesc() {
        if (getHandleType()!=null){
            handleTypeDesc = MalfHandleTypeEnum.getDescByValue(getHandleType());
        }
        return handleTypeDesc;
    }

    public void setHandleTypeDesc(String handleTypeDesc) {
        this.handleTypeDesc = handleTypeDesc;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
