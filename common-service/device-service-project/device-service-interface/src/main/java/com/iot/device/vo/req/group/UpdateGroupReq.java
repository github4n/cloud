package com.iot.device.vo.req.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateGroupReq {

    private Long id;
    /**
     * 组名称
     */

    private Long groupId;

    private String name;
    /**
     * 空间id
     */
    private Long homeId;

    /**
     * 设备控制组Id
     */
    private Integer devGroupId;
    /**
     * 图标
     */
    private String icon;
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

    private List groupIds = new ArrayList();

}
