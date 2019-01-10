package com.iot.boss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.boss.enums.DateTypeEnum;
import com.iot.boss.enums.MalfRankEnum;
import com.iot.boss.enums.MalfStatusEnum;
import com.iot.common.beans.SearchParam;

import java.util.Date;

public class UserMalfHistorySearch extends SearchParam{

    private int malfStatus = -1;

    private int malfRank = -1;

    private int malfId = -1;

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date searchDate;

    private int dateType = -1;

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

    public int getMalfId() {
        return malfId;
    }

    public void setMalfId(int malfId) {
        this.malfId = malfId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
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
