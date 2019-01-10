package com.iot.report.service;

import com.iot.common.helper.Page;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.req.DevActivateOrActiveReq;
import com.iot.report.dto.req.DevDistributionReq;
import com.iot.report.dto.resp.DevActivateResp;
import com.iot.report.dto.resp.DevDistributionResp;
import com.iot.report.dto.resp.DevPageResp;
import com.iot.report.dto.resp.DistributionResp;
import com.iot.report.entity.DeviceActivatedInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description:    立达信IOT云平台 设备报表
 * @Author:         nongchongwei
 * @CreateDate:     2019/1/4 15:12
 * @UpdateUser:     nongchongwei
 * @UpdateDate:     2019/1/4 15:12
 * @UpdateRemark:
 * @Version:        1.0
 */
public interface DeviceActivateService {

    /**
     * 描述: 定时保存设备激活信息
     * @author  nongchongwei
     * @param
     * @return  void
     * @exception
     * @date     2019/1/5 16:48
     */
    void saveDeviceActiveInfo();
    /**
     * 描述: 查询激活信息
     * @author  nongchongwei
     * @param uuid
     * @return  com.iot.report.entity.DeviceActivatedInfo
     * @exception
     * @date     2019/1/5 16:49
     */
    DeviceActivatedInfo getDeviceActiveInfoByUuid(String uuid);


    /**
     * 描述: 查询地区激活、活跃信息
     * @author  nongchongwei
     * @param devDistributionReq
     * @return  Map<String, DistributionResp>
     * @exception
     * @date     2019/1/5 16:49
     */
    List<DevDistributionResp> getDistributionAmount(DevDistributionReq devDistributionReq);

    /**
     * 描述:激活设备分页查询
     * @author  nongchongwei
     * @param devActivateReq
     * @return  void
     * @exception
     * @date     2019/1/4 15:09
     */
    Page<DevPageResp> getPageDeviceActivateOrActive(DevActivateOrActiveReq devActivateReq);

    /** 
     * 描述：获取设备活跃页面数据
     * @author maochengyuan
     * @created 2019/1/8 11:40
     * @param req
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    DevActivateResp getDeviceActive(ActivateBaseReq req);

    /** 
     * 描述：获取设备激活页面数据
     * @author maochengyuan
     * @created 2019/1/8 11:41
     * @param req
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    DevActivateResp getDeviceActivated(ActivateBaseReq req);

    /** 
     * 描述：获取设备激活/活跃数据
     * @author maochengyuan
     * @created 2019/1/8 11:41
     * @param req
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    DevActivateResp getDeviceActiveAndActivated(ActivateBaseReq req);

    /**
     *@description 获取设备激活总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/9 10:40
     *@return java.lang.Long
     */
    Long getDeviceActivatedCount(Long tenantId);
}
