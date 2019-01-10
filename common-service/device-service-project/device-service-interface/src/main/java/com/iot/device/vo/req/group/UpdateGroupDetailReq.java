package com.iot.device.vo.req.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateGroupDetailReq {

    private Long id;
    /**
     * 组名称
     */
    private Long groupId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 租户id
     */
    @NotNull
    private Long tenantId;
    /**
     * 创建者id
     */
    private Long createBy;
    /**
     * 更新者id
     */
    private Long updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private List<String> members;

    private List groupIds = new ArrayList();

}
