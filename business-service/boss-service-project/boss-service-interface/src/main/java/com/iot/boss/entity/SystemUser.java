package com.iot.boss.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：管理员实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 16:51
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 16:51
 * 修改描述：
 */
public class SystemUser implements Serializable{

    /**
     * 主键
     */
    private Long id;

    /**
     * 管理员名称
     */
    private String adminName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String nickName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 管理员编号
     */
    private String adminNo;

    /**
     * 管理员描述
     */
    private String adminDesc;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 注销时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date cancelTime;

    /**
     * 管理员状态 0-未激活，1-审核中，2-已认证，3-在线，4-离线，5-已冻结，6-已注销
     */
    private Integer adminState;

    /**
     * 管理员类型 0-超级管理员，1-普通管理员,2-运维人员
     */
    private Integer adminType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(String adminNo) {
        this.adminNo = adminNo;
    }

    public String getAdminDesc() {
        return adminDesc;
    }

    public void setAdminDesc(String adminDesc) {
        this.adminDesc = adminDesc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Integer getAdminState() {
        return adminState;
    }

    public void setAdminState(Integer adminState) {
        this.adminState = adminState;
    }

    public Integer getAdminType() {
        return adminType;
    }

    public void setAdminType(Integer adminType) {
        this.adminType = adminType;
    }
}
