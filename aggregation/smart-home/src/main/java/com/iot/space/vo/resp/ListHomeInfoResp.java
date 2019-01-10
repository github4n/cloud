package com.iot.space.vo.resp;

import lombok.*;

import java.io.Serializable;

/**
 * @author lucky
 * @ClassName ListHomeInfoResp
 * @Description homeInfo list
 * @date 2019/1/3 14:35
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ListHomeInfoResp implements Serializable {

    private Long homeId;

    private String homeName;

    private Long tenanId;

    //当前用户id
    private Long currentUserId;

    //当前用户uuid
    private String currentUserUuid;

    //所属用户id
    private Long belongUserId;

    //所属用户uuid
    private String belongUserUuid;

}
