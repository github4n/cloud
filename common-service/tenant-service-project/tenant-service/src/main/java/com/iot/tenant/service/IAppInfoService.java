package com.iot.tenant.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.tenant.domain.AppInfo;
import com.iot.tenant.vo.req.review.AppReviewSearchReq;

import java.util.List;

/**
 * <p>
 * App应用 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-05
 */
public interface IAppInfoService extends IService<AppInfo> {

    /**
     * 描述：依据AppId获取App审核状态
     * @author maochengyuan
     * @created 2018/10/23 14:59
     * @param id appId
     * @return java.lang.Byte
     */
    AppInfo getAppAuditStatus(Long id);

    /**
     * 描述：依据AppId更新App审核状态
     * @author maochengyuan
     * @created 2018/10/23 14:59
     * @param id appId
     * @param status 状态
     * @return void
     */
    void setAppAuditStatus(Long id, Byte status);

    /**
     * 描述：依据审核状态查询APP信息
     * @author maochengyuan
     * @created 2018/10/25 14:28
     * @param req 查询条件
     * @return java.util.List<com.iot.tenant.domain.AppInfo>
     */
    Page<AppInfo> getAppInfoList(AppReviewSearchReq req);

    /**
     * 更新app状态
     *
     */
    void updateAppStatusByTime();

    /**
     * @despriction：通过打包状态获取appId
     * @author  yeshiyuan
     * @created 2018/11/14 16:38
     * @return
     */
    List<Long> getAppIdByPackStatus(Integer packStatus);

    /**
     * @despriction：修改app审核状态为空
     * @author  yeshiyuan
     * @created 2018/11/16 9:49
     * @return
     */
    void updateAuditStatusToNull(Long appId);
}
