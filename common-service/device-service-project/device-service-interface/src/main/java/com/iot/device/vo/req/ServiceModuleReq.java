package com.iot.device.vo.req;

import com.iot.common.beans.SearchParam;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Data
@ToString
public class ServiceModuleReq extends SearchParam implements Serializable {

    private Long id;
    private Long parentId;
    private Long tenantId;
    private String version;
    private String name;
    private String code;
    private Integer propertyStatus;
    private Integer developStatus;
    private String testCase;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;
    private String img;
    private String changeImg;
    private String componentType;
    private String searchValues;
    private ArrayList<Long> filterIds;

}
