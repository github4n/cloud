package com.iot.tenant.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.tenant.domain.AppInfo;
import com.iot.tenant.mapper.AppInfoMapper;
import com.iot.tenant.service.IAppInfoService;
import com.iot.tenant.vo.req.review.AppReviewSearchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * App应用 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-05
 */
@Service
public class AppInfoServiceImpl extends ServiceImpl<AppInfoMapper, AppInfo> implements IAppInfoService {

    @Autowired
    private AppInfoMapper appInfoMapper;

    /**
     * 描述：依据appId获取APP信息
     * @author maochengyuan
     * @created 2018/10/24 14:35
     * @param appId appId
     * @return com.iot.tenant.domain.AppInfo
     */
    @Override
    public AppInfo getAppAuditStatus(Long appId) {
        return this.appInfoMapper.getAppAuditStatus(appId);
    }

    /**
     * 描述：依据appId获取更新APP审核状态
     * @author maochengyuan
     * @created 2018/10/24 14:36
     * @param appId appId
     * @param auditStatus 审核状态
     * @return void
     */
    @Override
    public void setAppAuditStatus(Long appId, Byte auditStatus) {
        this.appInfoMapper.setAppAuditStatus(appId, auditStatus);
    }

    /**
     * 描述：依据审核状态查询APP信息
     * @author maochengyuan
     * @created 2018/10/25 14:28
     * @param req 查询参数
     * @return java.util.List<com.iot.tenant.domain.AppInfo>
     */
    @Override
    public com.iot.common.helper.Page<AppInfo> getAppInfoList(AppReviewSearchReq req) {
        Page page = new Page<>(req.getPageNum(), req.getPageSize());
        List<AppInfo> list = this.appInfoMapper.getAppInfoList(page, req);
        com.iot.common.helper.Page<AppInfo> myPage = new com.iot.common.helper.Page<>();
        myPage.setTotal(page.getTotal());
        myPage.setPages(page.getPages());
        myPage.setPageNum(page.getCurrent());
        myPage.setPageSize(page.getSize());
        myPage.setStartRow((page.getCurrent() - 1) * page.getSize());
        myPage.setEndRow(myPage.getStartRow()+page.getSize());
        myPage.setResult(list);
        return myPage;
    }

    @Override
    public void updateAppStatusByTime() {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("confirm_status",0);
        List<AppInfo> appInfo = super.selectList(wrapper);
        Date date = new Date();
        List<AppInfo> updateResult = new ArrayList<>();
        appInfo.forEach(m->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(m.getConfirmTime());
            calendar.add(Calendar.WEEK_OF_MONTH,2);
            Long result = calendar.getTimeInMillis();
            if ( date.getTime() > result ){
                m.setConfirmStatus(1);
                updateResult.add(m);
            }
        });
        super.updateBatchById(updateResult);
    }
    /**
     * @despriction：通过打包状态获取appId
     * @author  yeshiyuan
     * @created 2018/11/14 16:38
     * @return
     */
    @Override
    public List<Long> getAppIdByPackStatus(Integer packStatus) {
        return appInfoMapper.getAppIdByPackStatus(packStatus);
    }

    /**
     * @despriction：修改app审核状态为空
     * @author  yeshiyuan
     * @created 2018/11/16 9:49
     * @return
     */
    @Override
    public void updateAuditStatusToNull(Long appId) {
        appInfoMapper.updateAuditStatusToNull(appId);
    }
}
