package com.iot.boss.service.network.impl;

import com.iot.boss.service.network.IBossDeviceNetworkStepService;
import com.iot.boss.vo.network.BossSaveNetworkStepReq;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.IBusinessException;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.NetworkTypeApi;
import com.iot.device.api.TechnicalRelateApi;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.file.api.FileApi;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.DeviceNetworkStepBaseApi;
import com.iot.tenant.vo.req.network.NetworkStepHelpReq;
import com.iot.tenant.vo.req.network.SaveNetworkStepBaseReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepBaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤管理service
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 10:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 10:12
 * 修改描述：
 */
@Service
public class BossDeviceNetworkStepServiceImpl implements IBossDeviceNetworkStepService{

    @Autowired
    private DeviceNetworkStepBaseApi deviceNetworkStepBaseApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private NetworkTypeApi networkTypeApi;

    @Autowired
    private TechnicalRelateApi technicalRelateApi;

    /**
     * @despriction：保存
     * @author  yeshiyuan
     * @created 2018/10/9 10:11
     * @return
     */
    @Override
    public void save(BossSaveNetworkStepReq req) {
        DeviceTypeResp resp = deviceTypeApi.getDeviceTypeById(req.getDeviceTypeId());
        if (resp == null) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 400;
                }
                @Override
                public String getMessageKey() {
                    return "device type isn't exists";
                }
            });
        }
        List<NetworkTypeResp> networkTypeResps = technicalRelateApi.deviceSupportNetworkType(req.getDeviceTypeId());
        if (networkTypeResps.isEmpty()) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 400;
                }
                @Override
                public String getMessageKey() {
                    return "this device type haven't config network type";
                }
            });
        }
        Map<Long,NetworkTypeResp> map= networkTypeResps.stream().collect(Collectors.toMap(NetworkTypeResp::getId, a->a));
        req.getNetworkInfos().forEach(networkInfoReq -> {
            if (!map.containsKey(networkInfoReq.getNetworkTypeId())) {
                throw new BusinessException(new IBusinessException() {
                    @Override
                    public int getCode() {
                        return 400;
                    }
                    @Override
                    public String getMessageKey() {
                        return "this device type no support network type";
                    }
                });
            }
        });
        if (req.getNewFileList()!=null && !req.getNewFileList().isEmpty()) {
            fileApi.saveFileInfosToDb(req.getNewFileList());
        }
        SaveNetworkStepBaseReq networkStepBaseReq = new SaveNetworkStepBaseReq();
        BeanUtil.copyProperties(req, networkStepBaseReq);
        networkStepBaseReq.setUserId(SaaSContextHolder.getCurrentUserId());
        deviceNetworkStepBaseApi.save(networkStepBaseReq);
    }

    /**
     * @despriction：查询设备配网步骤信息
     * @author  yeshiyuan
     * @created 2018/10/9 10:21
     * @return
     */
    @Override
    public DeviceNetworkStepBaseResp queryNetworkStepInfo(Long deviceTypeId, Long networkTypeId) {
        DeviceNetworkStepBaseResp networkStepBaseResp = deviceNetworkStepBaseApi.queryNetworkStep(deviceTypeId, networkTypeId);
        List<Long> networkTypeIds = new ArrayList<>();
        if (networkStepBaseResp.getNetworkInfos()!=null) {
            networkStepBaseResp.getNetworkInfos().forEach(networkInfoResp -> {
                networkTypeIds.add(networkInfoResp.getNetworkTypeId());
                networkInfoResp.getSteps().forEach(step -> {
                    if (!StringUtil.isBlank(step.getIcon())) {
                        step.setIconUrl(fileApi.getGetUrl(step.getIcon()).getPresignedUrl());
                    }
                    NetworkStepHelpReq helpResp = step.getHelp();
                    if (helpResp != null) {
                        if (!StringUtil.isBlank(helpResp.getIcon())) {
                            helpResp.setIconUrl(fileApi.getGetUrl(helpResp.getIcon()).getPresignedUrl());
                        }
                    }
                });
            });
            Map<Long, NetworkTypeResp> map = networkTypeApi.getNetworkTypes(networkTypeIds);
            networkStepBaseResp.getNetworkInfos().forEach(networkInfoResp -> {
                networkInfoResp.setNetworkType(map.get(networkInfoResp.getNetworkTypeId()).getNetworkName());
            });
        }
        return networkStepBaseResp;
    }
}
