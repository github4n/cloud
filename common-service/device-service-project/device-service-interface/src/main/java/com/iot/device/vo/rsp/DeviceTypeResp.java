package com.iot.device.vo.rsp;

import com.iot.device.vo.req.DataPointReq.SmartWraper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Data
@ToString
public class DeviceTypeResp implements Serializable {
    private Long id;

    private String name;

    private String deviceCatalogName;

    private String description;

    private Long deviceCatalogId;

    private Long tenantId;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Long updateBy;

    private Integer isDeleted;
    
    private String venderFlag;//厂商标识
    
    private String type;//设备真实类型
    
    private ArrayList<SmartWraper> smart;

    private String img;

    private String imgFileId;

    //0非免开发设备 1 免开发设备
    private Integer whetherSoc;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDeviceCatalogId() {
        return deviceCatalogId;
    }

    public void setDeviceCatalogId(Long deviceCatalogId) {
        this.deviceCatalogId = deviceCatalogId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getVenderFlag() {
        return venderFlag;
    }

    public void setVenderFlag(String venderFlag) {
        this.venderFlag = venderFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String getDeviceCatalogName() {
		return deviceCatalogName;
	}

	public void setDeviceCatalogName(String deviceCatalogName) {
		this.deviceCatalogName = deviceCatalogName;
	}

	public ArrayList<SmartWraper> getSmart() {
		return smart;
	}

	public void setSmart(ArrayList<SmartWraper> smart) {
		this.smart = smart;
	}

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgFileId() {
        return imgFileId;
    }

    public void setImgFileId(String imgFileId) {
        this.imgFileId = imgFileId;
    }

    public Integer getWhetherSoc() {
        return whetherSoc;
    }

    public void setWhetherSoc(Integer whetherSoc) {
        this.whetherSoc = whetherSoc;
    }

    @Override
    public String toString() {
        return "DeviceTypeResp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deviceCatalogName='" + deviceCatalogName + '\'' +
                ", description='" + description + '\'' +
                ", deviceCatalogId=" + deviceCatalogId +
                ", tenantId=" + tenantId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", isDeleted=" + isDeleted +
                ", venderFlag='" + venderFlag + '\'' +
                ", type='" + type + '\'' +
                ", smart=" + smart +
                ", img='" + img + '\'' +
                ", whetherSoc=" + whetherSoc +
                '}';
    }
}