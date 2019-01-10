package com.iot.control.activity.vo.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/27 11:54
 * 修改人:
 * 修改时间：
 */
public class ActivityRecordReq implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    private Long id;

    private String type;

    private String icon;

    private String activity;

    private Date time;

    private Long createBy;

    private String foreignId;

    private Integer delFlag;

    private Long tenantId;

    private Long orgId;

    private String deviceName; //设备名称

    private Integer result; //执行结果 0:正常 1:异常

    private Date setTime;   // 模板设置的执行时间

    private Long userId;

    private String userName;

    // *******
    private Integer pageSize;
    private Integer pageNum;
    private String startTime;
    private String endTime;

    private List<Long> spaceIds;
    
    private List<Long> spaceTemplateIds;
    
    private Long locationId;

    private String templateName;

    //************************

    private Object basicDBObject;

    private Object basicSort;

    private Object basicResult;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }


    public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    

	public List<Long> getSpaceIds() {
		return spaceIds;
	}

	public void setSpaceIds(List<Long> spaceIds) {
		this.spaceIds = spaceIds;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
    

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getSetTime() {
        return setTime;
    }

    public void setSetTime(Date setTime) {
        this.setTime = setTime;
    }

	public List<Long> getSpaceTemplateIds() {
		return spaceTemplateIds;
	}

	public void setSpaceTemplateIds(List<Long> spaceTemplateIds) {
		this.spaceTemplateIds = spaceTemplateIds;
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Object getBasicDBObject() {
        return basicDBObject;
    }

    public void setBasicDBObject(Object basicDBObject) {
        this.basicDBObject = basicDBObject;
    }

    public Object getBasicSort() {
        return basicSort;
    }

    public void setBasicSort(Object basicSort) {
        this.basicSort = basicSort;
    }

    public Object getBasicResult() {
        return basicResult;
    }

    public void setBasicResult(Object basicResult) {
        this.basicResult = basicResult;
    }
}
