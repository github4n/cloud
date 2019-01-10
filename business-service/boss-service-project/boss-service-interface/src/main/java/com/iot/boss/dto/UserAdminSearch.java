package com.iot.boss.dto;

import com.iot.common.beans.SearchParam;

public class UserAdminSearch extends SearchParam {

    private int type = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean checkIsDefaultType()
    {
        if(2 == type){
            return true;
        }else {
            return false;
        }
    }
}
