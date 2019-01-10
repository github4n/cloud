package com.iot.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 项目名称：cloud
 * 功能描述：存储各个模块对应的加密秘钥
 * 创建人： yeshiyuan
 * 创建时间：2018/4/27 16:28
 * 修改人： yeshiyuan
 * 修改时间：2018/4/27 16:28
 * 修改描述：
 */
@Component
public class SecurityKey {
    private String videoSerivceKey;

    public String getVideoSerivceKey() {
        return this.videoSerivceKey;
    }

    @Value("${security.key.video-service}")
    public void setVideoSerivceKey(String videoSerivceKey) {
        this.videoSerivceKey = videoSerivceKey;
    }
}
