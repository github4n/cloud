package com.iot.robot.vo;

/**
 * @Descrpiton: 封装 安防控制指令
 * @Author: yuChangXing
 * @Date: 2018/10/11 16:04
 * @Modify by:
 */
public class SecurityCommand {
    // 控制的指令
    // arm:     布防
    // panic:   报警
    // disarm:  把安防 设置为off模式
    private String command;

    // 指令值
    private String armMode;

    // 密码
    private String password;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArmMode() {
        return armMode;
    }

    public void setArmMode(String armMode) {
        this.armMode = armMode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
