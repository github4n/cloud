package com.iot.shcs.space.vo;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class SpacePageResp implements Serializable {

    private String homeId;

    private String name;

    private Integer defaultHome;

    private String icon;

    private Boolean isSecurityPwd;

    private String roomId;

    private Integer devNum;

    private Integer seq;

    private String meshName;

    private String meshPassword;

    private Boolean isSecurityPwdNull;

    private boolean belongCurrentUser = true;
    //当前用户id
    private Long currentUserId;

    //当前用户uuid
    private String currentUserUuid;

    //所属用户id
    private Long belongUserId;

    //所属用户uuid
    private String belongUserUuid;

}
