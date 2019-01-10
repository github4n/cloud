package com.iot.shcs.ifttt.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 联动子项
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-15
 */
@Data
public class AutomationItem extends Model<AutomationItem> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 类型 dev/scene
     */
    private String type;
    /**
     * 关联id
     */
    private String objectId;
    /**
     * ifttt item主键
     */
    private Long itemId;

    private Long appletId;

    private Long tenantId;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
