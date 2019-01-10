package com.iot;

import com.iot.common.exception.IBusinessException;

public enum BusinessExceptionEnum implements IBusinessException {

    COMMOMN_EXCEPTION(-1, "Something wrong with the server, please try again."),

    SCENENAME_IS_NULL(-4, "scenename is not null"),

    USER_IS_EXIT(21001, "User is exist."),

    PARAMETER_ILLEGALITY(-2, "Parameter illegality"),

    HOME_IS_EXIST(-3, "Home.name.is.exist"),

    ROOM_IS_EXIST(-3, "Room.name.is.exist"),

    AUTO_IS_EXIST(-3, "Auto.name.is.exist"),

    USER_HOME_IS_EXIST(20130, "User.default.home.is.exist"),

    PASSWORD_IS_NOT_THE_SAME("password.is.not.the.same"),

    WRONG_TYPE_OF_IMG("wrong type of img");

    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.code = 0;
        this.messageKey = messageKey;
    }

    /**
     * 描述：构建异常
     *
     * @param code       错误代码
     * @param messageKey 错误描述
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:50:58
     * @since
     */
    BusinessExceptionEnum(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

}
