package com.iot.vo;

import com.iot.shcs.space.vo.SpacePageResp;

import java.io.Serializable;
import java.util.List;

/**
 * @description: home list
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/08/02 11:06
 **/
public class RoomPageVo implements Serializable {

    private Long totalCount;

    private List<SpacePageResp> rooms;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<SpacePageResp> getRooms() {
        return rooms;
    }

    public void setRooms(List<SpacePageResp> rooms) {
        this.rooms = rooms;
    }
}
