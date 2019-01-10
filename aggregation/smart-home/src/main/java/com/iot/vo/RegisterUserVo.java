package com.iot.vo;

public class RegisterUserVo /*extends User*/ {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String verifyCode;

    private String terminalMark;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getTerminalMark() {
        return terminalMark;
    }

    public void setTerminalMark(String terminalMark) {
        this.terminalMark = terminalMark;
    }
}
