package com.iot.control.space.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SpaceVo {

    private Long id;
    private String name;
    private String createTime;
    private String updateTime;
    private String parentId;
    private String type;
    private Integer sort;
    private Integer flag;

}
