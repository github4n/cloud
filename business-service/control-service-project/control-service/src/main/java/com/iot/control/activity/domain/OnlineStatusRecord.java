package com.iot.control.activity.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class OnlineStatusRecord implements Serializable {
    private String id;

    private String status;

    private String type;

    private Date recordTime;

    private Long tenantId;

}