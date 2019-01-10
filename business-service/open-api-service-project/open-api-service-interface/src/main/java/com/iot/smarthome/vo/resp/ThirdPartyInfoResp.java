package com.iot.smarthome.vo.resp;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 11:33
 * @Modify by:
 */

@Data
@ToString
public class ThirdPartyInfoResp {
    private Long id;
    /**
     * 第三方类型(要唯一索引)
     */
    private String type;
    /**
     * 第三方公司名称
     */
    private String companyName;
    /**
     * 第三方描述
     */
    private String description;
    /**
     * 第三方公司网址
     */
    private String companyWebsite;
    /**
     * 第三方app网址
     */
    private String appWebsite;
    /**
     * 第三方图标
     */
    private String logo;
    /**
     * oauth的重定向地址
     */
    private String redirectUri;
    /**
     * 第三方事件通知地址
     */
    private String webhookUrl;
    /**
     * 应用的client_id
     */
    private String clientId;
    /**
     * 应用的client_secret
     */
    private String clientSecret;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新者
     */
    private Long updateBy;
}
