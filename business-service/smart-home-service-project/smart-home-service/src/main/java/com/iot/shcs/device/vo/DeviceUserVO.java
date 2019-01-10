package com.iot.shcs.device.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class DeviceUserVO implements Serializable {

    private String userId;
    private String userType;
    private String password;


}
