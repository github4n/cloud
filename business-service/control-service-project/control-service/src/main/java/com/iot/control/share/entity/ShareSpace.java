package com.iot.control.share.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 家分享表
 * </p>
 *
 * @author lucky
 * @since 2019-01-02
 */
@Data
@ToString
@TableName("share_space")
public class ShareSpace extends Model<ShareSpace> {

    public static final Integer INVITE_DOING = 0;
    public static final Integer INVITE_SUCCESS = 1;
    public static final Integer INVITE_ERROR = 2;
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    /**
     * 主键id
     */
    private Long id;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 分享用户id
     */
    @TableField("from_user_id")
    private Long fromUserId;
    /**
     * 分享用户uuid
     */
    @TableField("from_user_uuid")
    private String fromUserUuid;
    /**
     * 受邀分享用户id
     */
    @TableField("to_user_id")
    private Long toUserId;

    /**
     * 受邀人别名 eg: Daughter sister and so on.
     */
    @TableField("alias")
    private String alias;
    /**
     * 受邀分享用户uuid
     */
    @TableField("to_user_uuid")
    private String toUserUuid;
    /**
     * 分享的房间id
     */
    @TableField("space_id")
    private Long spaceId;
    /**
     * 分享uuid
     */
    @TableField("share_uuid")
    private String shareUuid;
    /**
     * 失效时间,单位秒
     */
    @TableField("expire_time")
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
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    private String toUserEmail;

}
