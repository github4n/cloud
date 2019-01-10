package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.iot.device.vo.req.DataPointReq.SmartWraper;

public class DataPointResp implements Serializable {

    private Long id;
    /**
     * 显示名称
     * label_name
     */
    private String labelName;
    /**
     * 属性标识
     * property_code
     */
    private String propertyCode;
    /**
     * 读写类型(0 r, 1 w, 2 rw)
     */
    private Integer mode;
    /**
     * 数值类型(0 boolean，1 int，2 unsignedInt，3.枚举型，4故障型，5字符型，6 RAW型）
     * data_type
     */
    private Integer dataType;
    /**
     * 图标名称
     * icon_name
     */
    private String iconName;
    /**
     * 数值属性
     */
    private String property;
    /**
     * 描述
     */
    private String description;

    private Long createBy;

    private Date createTime;
    
    private ArrayList<SmartWraper> smart = new ArrayList<>();
    
    /**
	 * 是否自定义功能点 1是  0否
	 */
	private Integer isCustom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName == null ? null : labelName.trim();
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode == null ? null : propertyCode.trim();
    }


    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Integer getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(Integer isCustom) {
		this.isCustom = isCustom;
	}

	public ArrayList<SmartWraper> getSmart() {
		return smart;
	}

	public void add(SmartWraper smart) {
		this.smart.add(smart);
	}
}