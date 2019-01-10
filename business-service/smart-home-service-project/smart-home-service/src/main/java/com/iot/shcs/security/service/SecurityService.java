package com.iot.shcs.security.service;


import com.iot.shcs.security.constant.ArmModeEnum;
import com.iot.shcs.security.domain.Security;
import com.iot.shcs.security.vo.rsp.SecurityResp;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/9 17:59
 * 修改人:
 * 修改时间：
 */
public interface SecurityService {

    /**
     *  根据 spaceId 获取 Security
     *
     * @param spaceId
     * @return
     */
    Security getBySpaceId(Long spaceId,Long tenantId);

    SecurityResp getSecurityRespBySpaceId(Long spaceId,Long tenantId);

    /**
     * 更新 安防模式
     *
     * @param spaceId
     * @param updateBy
     * @param armModeEnum
     */
    void updateArmModeBySpaceId(Long spaceId,Long tenantId, Long updateBy, ArmModeEnum armModeEnum);

    /**
     * 修改 安防密码
     *
     * @param security
     * @param updateBy
     * @param password
     */
    void updatePasswordById(Security security, Long updateBy, String password);

    Long securityResetFactory(String deviceUuid,Long tenantId);

    /**
     *  创建一个 Security
     *
     * @param userId
     * @param spaceId
     * @return
     */
    Security createSecurity(Long tenantId, Long userId, Long spaceId, String password);

    //设置安防状态到安防rule表中
    public void setSecurityStatus(Integer type, Long spaceId,Long tenantId);

    Security getSecurityById(Long id);

}
