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
 * 功能描述：情景 实体
 * 创建人: yuChangXing
 * 创建时间: 2018/4/16 16:12
 * 修改人:
 * 修改时间：
 */
@Data
@ToString
@TableName("scene")
public class Scene extends Model<Scene> implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	private Long id;

	/** 情景名称*/
	private String sceneName;

	/** 空间ID*/
	private Long spaceId;

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

	/** 组织机构id*/
	private Long orgId;

	/** 图标*/
	private String icon;

	// 1.设备类型  2.全量设置 3.业务类型
	private Integer setType;

	// 排序
	private Integer sort;

	private Integer uploadStatus;
	
	private Long locationId;

	private Long templateId;

	/** Ble设备控制场景ID*/
	private Long devSceneId;

	private Integer silenceStatus;

	@Override
	protected Serializable pkVal() {
		return id;
	}
}
