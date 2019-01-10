package com.iot.building.template.vo.req;

import java.io.Serializable;
import java.util.List;

import com.iot.control.space.vo.SpaceDeviceVo;

/**
 * Created with IntelliJ IDEA.
 * User: 余昌兴
 * Date: 2018/5/18
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class CreateSceneFromTemplateReq implements Serializable {
    private static final long serialVersionUID = -7276209311915920905L;

    private Long templateId;
    private Long userId;
    private Long spaceId;
    private List<String> deviceIdList;
    private Long locationId;
    private Long tenantId;
    private Integer silenceStatus;
    
    private List<SpaceDeviceVo> deviceList;
    
    public List<SpaceDeviceVo> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<SpaceDeviceVo> deviceList) {
		this.deviceList = deviceList;
	}

	public Integer getSilenceStatus() {
		return silenceStatus;
	}

	public void setSilenceStatus(Integer silenceStatus) {
		this.silenceStatus = silenceStatus;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public List<String> getDeviceIdList() {
        return deviceIdList;
    }

    public void setDeviceIdList(List<String> deviceIdList) {
        this.deviceIdList = deviceIdList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
    
}
