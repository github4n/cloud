package com.iot.shcs.ifttt.vo.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 描述：联动列表请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 14:51
 */
@Data
@ToString
public class AddAutoRuleReq {
    private Long userId;

    private String UserUuid;

    private Long spaceId;

    private Long tenantId;

    private String seq;

    private String autoId;

    private Integer enable;

    private Integer enableDelay;

//    @JsonProperty(value = "real_name")
    @JsonProperty("if")
    private Map<String, Object> ifParam;

    private List then;

    private Map<String, Object> payload;

}
