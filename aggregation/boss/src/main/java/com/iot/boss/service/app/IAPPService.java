package com.iot.boss.service.app;

import com.iot.boss.vo.review.req.AppReviewListSearchReq;
import com.iot.boss.vo.review.resp.AppInfoReviewResp;
import com.iot.common.helper.Page;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：boss聚合层
 * 功能描述：审核功能接口定义
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 15:57
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 15:57
 * 修改描述：
 */
public interface IAPPService {

    /**
     * 描述：获取APP审核列表
     * @author maochengyuan
     * @created 2018/10/26 9:29
     * @param req 请求参数
     * @return java.util.List<com.iot.boss.vo.review.resp.AppInfoReviewResp>
     */
    Page<AppInfoReviewResp> getAppListByAuditStatus(AppReviewListSearchReq req);

    /**
     * @return
     * @despriction：重开可编辑
     * @author yeshiyuan
     * @created 2018/10/25 17:06
     */
    void reOpen(Long appId);

    /**
     * 修改app打包状态
     * @param id
     */
    public void updateAppStatus(Long id);

}
