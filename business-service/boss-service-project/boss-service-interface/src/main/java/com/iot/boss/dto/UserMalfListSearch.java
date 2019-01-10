package com.iot.boss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.boss.enums.DateTypeEnum;
import com.iot.boss.enums.MalfHandleStatusEnum;
import com.iot.boss.enums.MalfRankEnum;
import com.iot.boss.enums.MalfStatusEnum;
import com.iot.common.beans.SearchParam;

import java.util.Date;

public class UserMalfListSearch extends SearchParam {

    /* 报障处理状态 */
    private int handleStatus = 3;

    /* 报障处理进度 */
    private int malfStatus = -1;

    /* 报障等级 */
    private int malfRank = -1;

    /* 报障单号 */
    private long malfId = -1L;

    /* 报障用户id */
    private String userId;

    /* 日期类型 */
    private int dateType = -1;

    /* 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;

    /* 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;

    public int getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(int handleStatus) {
        this.handleStatus = handleStatus;
    }

    public int getMalfStatus() {
        return malfStatus;
    }

    public void setMalfStatus(int malfStatus) {
        this.malfStatus = malfStatus;
    }

    public int getMalfRank() {
        return malfRank;
    }

    public void setMalfRank(int malfRank) {
        this.malfRank = malfRank;
    }

    public long getMalfId() {
        return malfId;
    }

    public void setMalfId(long malfId) {
        this.malfId = malfId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean checkHandleStatusIsDefault(){
        if(3 == handleStatus){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkMalfStausIsDefault(){
        if(-1 == malfStatus){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkMalfRankIsDefault(){
        if(-1 == malfRank){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkDateTypeIsDefault(){
        if(-1 == dateType){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkHandleStatusValid(){
        if(!checkHandleStatusIsDefault() && !MalfHandleStatusEnum.checkIsValid(handleStatus)) {
            return false;
        }else {
            return true;
        }
    }

    public boolean checkMalfStatusValid(){
        if(!checkMalfStausIsDefault() && !MalfStatusEnum.checkIsValid(malfStatus)) {
            return false;
        }else {
            return true;
        }
    }

    public boolean checkMalRankValid(){
        if(!checkMalfRankIsDefault() && !MalfRankEnum.checkIsValid(malfRank)) {
            return false;
        }else {
            return true;
        }
    }

    public boolean checkDateTypeValid(){
        if(!DateTypeEnum.checkIsValid(dateType)){
            return false;
        }else {
            return true;
        }
    }

}
