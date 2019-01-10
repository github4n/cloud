package com.iot.boss.service.network;

import com.iot.boss.vo.network.BossSaveNetworkStepReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepBaseResp;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤管理service
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 10:10
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 10:10
 * 修改描述：
 */
public interface IBossDeviceNetworkStepService {

    /**
      * @despriction：保存
      * @author  yeshiyuan
      * @created 2018/10/9 10:11
      * @return
      */
    void save(BossSaveNetworkStepReq req);

    /**
      * @despriction：查询设备配网步骤信息
      * @author  yeshiyuan
      * @created 2018/10/9 10:21
      * @return
      */
    DeviceNetworkStepBaseResp queryNetworkStepInfo(Long deviceTypeId, Long networkTypeId);
}
