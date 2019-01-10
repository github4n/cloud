package com.iot.user.rabbit.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/15 13:46
 * @Modify by:
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class UserLoginAfterMessage extends BaseMessageEntity implements Serializable {
    private Long userId;
    private String uuid;
    private Long tenantId;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 最后登录ip（手机端登录才需要）
     */
    private String lastIp;

    /**
     * app系统类型（1，iOS 2，Android）（手机端登录才需要）
     */
    private String os;
    /**
     * 手机唯一tokenId，用于推送（手机端登录才需要）
     */
    private String phoneId;
    /**
     * 手机唯一tokenId，用于推送（手机端登录才需要）
     */
    private Long appId;
    /**
     * 登录终端类型（app|web），默认手机
     */
    private String terminalMark;

}
