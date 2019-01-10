package com.iot.pack.vo;

/**
 * 描述：执行结果
 * 创建人： LaiGuiMing
 * 创建时间： 2018/8/21 13:59
 */
public class ExecResult {
    private Boolean flag;
    private String error;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ExecResult{" +
                "flag=" + flag +
                ", error='" + error + '\'' +
                '}';
    }
}
