package com.iot.shcs.ota.utils;

import org.springframework.util.StringUtils;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:47 2018/5/22
 * @Modify by:
 */
public enum OtaStageEnum {

    idle(0, "idle", ""),

    downloading(1, "downloading", ""),

    dowloaded(2, "dowloaded", ""),

    installing(3, "installing", ""),

    wait_install(4, "wait_install", ""),

    installed(5, "installed", ""),

    failed(6, "failed", ""),

    busy(7, "busy", "");


    private int status;

    private String stage;

    private String message;


    OtaStageEnum(int status, String stage, String message) {
        this.status = status;
        this.stage = stage;
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public static OtaStageEnum getByStage(String stage) {
        OtaStageEnum stageEnum = null;
        if (!StringUtils.isEmpty(stage)) {
            OtaStageEnum[] stageEnums = OtaStageEnum.values();
            for (OtaStageEnum stageEnumTemp : stageEnums) {

                if (stageEnumTemp.stage.equals(stage)) {
                    stageEnum = stageEnumTemp;
                    break;
                }
            }
        }
        return stageEnum;
    }
}
