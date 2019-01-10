package com.iot.control.scene.vo.req;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class GetSceneReq implements Serializable {
    private List<Long> sceneIds;
    /** 租户ID*/
    @NotNull
    private Long tenantId;

    private Long orgId;
}
