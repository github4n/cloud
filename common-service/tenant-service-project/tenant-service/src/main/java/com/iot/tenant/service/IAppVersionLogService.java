package com.iot.tenant.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.iot.tenant.domain.AppVersionLog;
import com.iot.tenant.vo.req.AppVersionLogReq;
import com.iot.tenant.vo.resp.AppVersionLogResp;

import java.util.List;

public interface IAppVersionLogService extends IService<AppVersionLog> {

    /**
     * 增加记录
     * @param appVersionLogReq
     * @return
     */
    Long insertOrUpdate(AppVersionLogReq appVersionLogReq);

    /**
     * 获取版本信息
     * @param systemInfo
     * @param appPackage
     * @return
     */
    AppVersionLogResp versionLogByKey(String systemInfo, String appPackage,String version);

    /**
     * 分页获取版本记录
     * @return
     */
    PageInfo page(AppVersionLogReq appVersionLogReq);

    /**
     * 删除
     * @param ids
     */
    void delete(List<Long> ids);

}
