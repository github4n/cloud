package com.iot.tenant.vo.req;

public class CheckVersionRequest {

    private String oldVer;

    private String newVer;

    public String getOldVer() {
        return oldVer;
    }

    public void setOldVer(String oldVer) {
        this.oldVer = oldVer;
    }

    public String getNewVer() {
        return newVer;
    }

    public void setNewVer(String newVer) {
        this.newVer = newVer;
    }
}
