package com.iot.device.vo.req;

import com.iot.device.vo.LicenseUsageVo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class LicenseUsageReq {

    private List<LicenseUsageVo> LicenseUsageList;

}
