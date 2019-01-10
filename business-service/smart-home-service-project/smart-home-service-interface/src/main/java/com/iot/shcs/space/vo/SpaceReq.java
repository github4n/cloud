package com.iot.shcs.space.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ApiModel("家庭房间模型")
public class SpaceReq {

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

    private Long homeId;

    private int pageSize = 10;

    private int offset = 0;

    @NotNull
    private Long tenantId;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Long updateBy;

    private String style;

    private String orderBy;

    private Long orgId;

    private Long deployId;

    private Integer model;//0普通房间 1会议室

    private Integer seq;

    private List<Long> spaceIds;

    private String meshName;

    private String meshPassword;
}
