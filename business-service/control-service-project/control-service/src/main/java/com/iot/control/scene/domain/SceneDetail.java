package com.iot.control.scene.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：情景详情 实体
 * 创建人: yuChangXing
 * 创建时间: 2018/4/16 16:12
 * 修改人:
 * 修改时间：
 */

@Data
@ToString
@TableName("scene_detail")
public class SceneDetail extends Model<SceneDetail> implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	private Long id;

	/** 情景id*/
	private Long sceneId;

	/** 设备id*/
	private String deviceId;

	/** 空间id*/
	private Long spaceId;

	/** 设备类型id*/
	private Long deviceTypeId;

	/** 目标值json格式*/
	private String targetValue;

	/** 创建者*/
	private Long createBy;

	/** 更新者*/
	private Long updateBy;

	/** 创建时间*/
	private Date createTime;

	/** 更新时间*/
	private Date updateTime;

	/** 租户ID*/
	private Long tenantId;

	/** 排序*/
	private Integer sort;
	
	/** 区域ID*/
	private Long locationId;

	/** 调用设备事件的方法*/
	private String method;

	/**
	 *
	 */
	private Long orgId;

	@Override
	protected Serializable pkVal() {
		return id;
	}
}
