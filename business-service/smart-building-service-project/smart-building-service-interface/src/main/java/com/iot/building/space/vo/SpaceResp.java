package com.iot.building.space.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ApiModel("家庭房间模型")
public class SpaceResp {

    private Long id;

    private String icon;

    private String position;

    private String name;

    private Long parentId;

    private Long locationId;

    @ApiModelProperty(value = "家庭HOME,房间ROOM", allowableValues = "BUILD,FLOOR,GROUP,HOME, ROOM")
    private String type;

    @ApiModelProperty(value = "空间排序")
    private Integer sort;

    private Integer defaultSpace;

    private Long userId;

    private String cookieUserToken;

    private String homeId;

    private int pageSize;

    private int offset;
    
    private Date createTime;

    private Date updateTime;
    
    private Long createBy;

    private Long updateBy;
    
    private Long tenantId;
    
    private String style;

    private Long orgId;

    private String deviceId;
    
    private Integer model;//0普通房间 1会议室

    private Integer seq;

    private Integer devNum;
    
    private Long deployId;
    

}
