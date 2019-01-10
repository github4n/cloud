package com.iot.tenant.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.tenant.api.AppVersionApi;
import com.iot.tenant.domain.AppVersion;
import com.iot.tenant.service.IAppVersionService;
import com.iot.tenant.util.CheckVersionUtil;
import com.iot.tenant.vo.req.AppVersionListReq;
import com.iot.tenant.vo.req.CheckVersionRequest;
import com.iot.tenant.vo.req.SaveAppVersionReq;
import com.iot.tenant.vo.resp.AppVersionResp;
import com.iot.tenant.vo.resp.CheckVersionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 应用版本记录 前端控制器
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-12
 */
@RestController
public class AppVersionController implements AppVersionApi {
    private final Logger logger = LoggerFactory.getLogger(AppVersionController.class);
    @Autowired
    private IAppVersionService appVersionService;

    @Override
    public Page<AppVersionResp> list(@RequestBody AppVersionListReq req) {
        logger.debug("get app page req:" + req.toString());
        Integer pageNum = req.getPageNum();
        Integer pageSize = req.getPageSize();
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 1000;
        }
        com.baomidou.mybatisplus.plugins.Page datePage = new com.baomidou.mybatisplus.plugins.Page(pageNum, pageSize);
        Page<AppVersionResp> page = new Page<AppVersionResp>();
        EntityWrapper<AppVersion> wrapper = new EntityWrapper<>();
        if (req.getTenantId() != null) {
            wrapper.eq("tenant_id", req.getTenantId());
        }
        if (req.getAppId() != null) {
            wrapper.eq("app_id", req.getAppId());
        }
        wrapper.like("version", req.getVersion());
        wrapper.orderBy("create_time", false);
        datePage = appVersionService.selectPage(datePage, wrapper);
        List<AppVersion> list = datePage.getRecords();
        List<AppVersionResp> resList = Lists.newArrayList();
        for (AppVersion entity : list) {
            AppVersionResp appVersionResp = new AppVersionResp();
            BeanUtils.copyProperties(entity, appVersionResp);
            resList.add(appVersionResp);
        }
        page.setResult(resList);
        page.setTotal(datePage.getTotal());
        return page;
    }

    @Override
    public Long save(@RequestBody SaveAppVersionReq req) {
        logger.info("save app version req:" + req.toString());
        AppVersion appVersion = new AppVersion();
        BeanUtils.copyProperties(req, appVersion);
        Long id = req.getId();
        if (id != null && id != 0) {
            //修改
            appVersionService.updateById(appVersion);
        } else {
            appVersion.setCreateTime(new Date());
            appVersionService.insert(appVersion);
        }
        return appVersion.getId();
    }

    @Override
    public AppVersionResp getLastVersion(Long appId) {
        EntityWrapper<AppVersion> wrapper = new EntityWrapper<>();
        wrapper.eq("app_id", appId);
        wrapper.orderBy("create_time", false);
        AppVersion appVersion = appVersionService.selectOne(wrapper);
        if (appVersion != null) {
            AppVersionResp appVersionResp = new AppVersionResp();
            BeanUtils.copyProperties(appVersion, appVersionResp);
            return appVersionResp;
        } else {
            return null;
        }
    }

    @Override
    public CheckVersionResponse checkVersion(@RequestBody CheckVersionRequest req) {
        CheckVersionResponse resp = new CheckVersionResponse();
        resp.setResult(CheckVersionUtil.compareVersion(req.getOldVer(), req.getNewVer()));
        return resp;
    }
}

