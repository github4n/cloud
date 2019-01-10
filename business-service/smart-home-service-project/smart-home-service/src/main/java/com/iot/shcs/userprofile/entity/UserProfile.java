package com.iot.shcs.userprofile.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：用户配置信息表
 * 创建人: yuChangXing
 * 创建时间: 2019/1/8 19:08
 * 修改人:
 * 修改时间：
 */

@Data
@ToString
@TableName("user_profile")
public class UserProfile extends Model<UserProfile> implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private Long id;

    private Long userId;

    // 类型(scene_short_cut/)
    private String type;

    // 记录值
    private String value;

    private Long tenantId;

    /** 创建者*/
    private Long createBy;

    /** 更新者*/
    private Long updateBy;

    /** 创建时间*/
    private Date createTime;

    /** 更新时间*/
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
