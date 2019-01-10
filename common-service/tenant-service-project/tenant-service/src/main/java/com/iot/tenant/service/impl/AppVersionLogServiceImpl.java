package com.iot.tenant.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.iot.common.util.CommonUtil;
import com.iot.tenant.domain.AppVersionLog;
import com.iot.tenant.mapper.AppVersionLogMapper;
import com.iot.tenant.service.IAppVersionLogService;
import com.iot.tenant.util.CheckVersionUtil;
import com.iot.tenant.vo.req.AppVersionLogReq;
import com.iot.tenant.vo.resp.AppVersionLogResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AppVersionLogServiceImpl extends ServiceImpl<AppVersionLogMapper,AppVersionLog> implements IAppVersionLogService {

    @Autowired
    private AppVersionLogMapper appVersionLogMapper;

    @Override
    public Long insertOrUpdate(AppVersionLogReq appVersionLogReq) {
        AppVersionLog appVersionLog = new AppVersionLog();
        if (appVersionLogReq.getId()==null){
            appVersionLog.setCreateTime(new Date());
            appVersionLog.setUpdateTime(new Date());
            appVersionLog.setCreateBy(appVersionLogReq.getCreateBy());
            appVersionLog.setUpdateBy(appVersionLogReq.getUpdateBy());
        } else {
            appVersionLog.setId(appVersionLogReq.getId());
            appVersionLog.setUpdateTime(new Date());
            appVersionLog.setUpdateBy(appVersionLogReq.getUpdateBy());
        }
        appVersionLog.setSystemInfo(appVersionLogReq.getSystemInfo());
        appVersionLog.setAppPackage(appVersionLogReq.getAppPackage());
        appVersionLog.setKey(appVersionLogReq.getSystemInfo()+"-"+appVersionLogReq.getAppPackage());
        appVersionLog.setAppName(appVersionLogReq.getAppName());
        appVersionLog.setVersion(appVersionLogReq.getVersion());
        appVersionLog.setDiscribes(appVersionLogReq.getDiscribes());
        appVersionLog.setDownLocation(appVersionLogReq.getDownLocation());
        appVersionLog.setTenantId(appVersionLogReq.getTenantId());
        super.insertOrUpdate(appVersionLog);
        return appVersionLog.getId();
    }

    @Override
    public AppVersionLogResp versionLogByKey(String systemInfo, String appPackage,String version) {
        String key = systemInfo + "-" + appPackage;
        AppVersionLogResp appVersionLogResp = new AppVersionLogResp();
        AppVersionLog appVersionLog = appVersionLogMapper.versionLogByKey(key);
        if (appVersionLog != null){
            if (CheckVersionUtil.compareVersion(version, appVersionLog.getVersion())==0){
                appVersionLogResp = null;
            } else {
                appVersionLogResp.setId(appVersionLog.getId());
                appVersionLogResp.setSystemInfo(appVersionLog.getSystemInfo());
                appVersionLogResp.setAppPackage(appVersionLog.getAppPackage());
                appVersionLogResp.setKey(appVersionLog.getSystemInfo()+"-"+appVersionLog.getAppPackage());
                appVersionLogResp.setAppName(appVersionLog.getAppName());
                appVersionLogResp.setVersion(appVersionLog.getVersion());
                appVersionLogResp.setDiscribes(appVersionLog.getDiscribes());
                appVersionLogResp.setDownLocation(appVersionLog.getDownLocation());
                appVersionLogResp.setCreateTime(appVersionLog.getCreateTime());
                appVersionLogResp.setUpdateTime(appVersionLog.getUpdateTime());
                appVersionLogResp.setCreateBy(appVersionLog.getCreateBy());
                appVersionLogResp.setUpdateBy(appVersionLog.getUpdateBy());
                appVersionLogResp.setTenantId(appVersionLog.getTenantId());
            }

        } else {
            appVersionLogResp = null;
        }
        return appVersionLogResp;
    }

    @Override
    public PageInfo page(AppVersionLogReq appVersionLogReq) {
        Page<AppVersionLog> page = new Page<>(CommonUtil.getPageNum(appVersionLogReq),CommonUtil.getPageSize(appVersionLogReq));
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.orderDesc(Arrays.asList("create_time"));
        wrapper.eq("tenant_id",appVersionLogReq.getTenantId());
        List list = appVersionLogMapper.selectPage(page,wrapper);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(list);
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNum(appVersionLogReq.getPageNum());
        return pageInfo;
    }

    @Override
    public void delete(List<Long> ids) {
        super.deleteBatchIds(ids);
    }
}
