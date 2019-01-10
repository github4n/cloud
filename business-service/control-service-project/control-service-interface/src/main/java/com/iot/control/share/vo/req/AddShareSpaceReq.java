package com.iot.control.share.vo.req;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lucky
 * @ClassName AddShareSpaceReq
 * @Description 添加、修改分享家信息
 * @date 2019/1/2 14:07
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class AddShareSpaceReq implements Serializable {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 分享用户id
     */
    private Long fromUserId;
    /**
     * 分享用户uuid
     */
    private String fromUserUuid;
    /**
     * 受邀分享用户id
     */
    private Long toUserId;

    /**
     * 受邀人别名 eg: Daughter sister and so on.
     */
    private String alias;
    /**
     * 受邀分享用户uuid
     */
    private String toUserUuid;
    /**
     * 分享的房间id
     */
    private Long spaceId;
    /**
     * 分享uuid
     */
    private String shareUuid;
    /**
     * 失效时间,单位秒
     */
    private Long expireTime;
    /**
     * 状态 默认0 0邀请中 1邀请成功、2邀请失败
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


    private String toUserEmail;
}
