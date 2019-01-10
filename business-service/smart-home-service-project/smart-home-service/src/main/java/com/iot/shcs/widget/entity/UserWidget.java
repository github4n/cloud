package com.iot.shcs.widget.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 22:09
 * 修改人:
 * 修改时间：
 */

@Data
@ToString
@TableName("user_widget")
public class UserWidget extends Model<UserWidget> implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    private Long id;

    private Long userId;

    // 组件类型(security/device/scene)
    private String type;

    // 记录值(安防规则类型/设备id/情景id)
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
