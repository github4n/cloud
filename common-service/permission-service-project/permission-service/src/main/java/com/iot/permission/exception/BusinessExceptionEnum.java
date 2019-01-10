package com.iot.permission.exception;


import com.iot.common.exception.IBusinessException;

/**
 * 
 * 项目名称：IOT视频云
 * 模块名称：服务层
 * 功能描述：文件服务异常枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

    /**未知异常*/
    UNKNOWN_EXCEPTION("permissionservice.unknow.exception"),
    /**角色ID为空*/
    ROLEID_ISNULL("permissionservice.roleId.isnull"),
    /**角色编码为空*/
    ROLECODE_ISNULL("permissionservice.roleCode.isnull"),
    /**角色编码为空*/
    ROLETYPE_ISNULL("permissionservice.roleType.isnull"),
    /**用户ID为空*/
    USERIDS_ISNULL("permissionservice.userIds.isnull"),
    /**角色类型为空*/
    ROLE_TYPE_ISNULL("permissionservice.role.type.isnull"),
    /**权限编码为空*/
    PERMISSIONURL_ISNULL("permissionservice.permissionUrl.isnull"),
    
    ROLE_IS_NOT_EXIST("permissionservice.role.is.not.exist"),
    ROLE_CANNOT_DELETED("permissionservice.This.role.is.not.a.custom.role.and.cannot.be.deleted"),
    USE_NO_MODIFY_PERMISSION("Current.user.does.not.have.permission.to.modify"),
    PERMISSIONID_ISNULL("permissionservice.permissionId.isnull"),
    USERID_ISNULL("permissionservice.userId.isnull"),
    ;

    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return this.messageKey;
    }

}