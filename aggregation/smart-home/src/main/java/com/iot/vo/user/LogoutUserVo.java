package com.iot.vo.user;

import com.iot.vo.AuthenticationVo;

public class LogoutUserVo extends AuthenticationVo {

    private String terminalMark;

    public String getTerminalMark() {
        return terminalMark;
    }

    public void setTerminalMark(String terminalMark) {
        this.terminalMark = terminalMark;
    }
}
