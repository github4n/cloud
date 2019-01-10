package com.iot.tenant.service;

import com.iot.tenant.vo.req.network.SaveNetworkStepBaseReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepBaseResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤模板（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:40
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:40
 * 修改描述：
 */
public interface IDeviceNetworkStepBaseService {

    /**
      * @despriction：保存步骤并保存步骤对应的文案至lang_info_base
      * @author  yeshiyuan
      * @created 2018/10/8 16:50
      * @return
      */
    void saveStepAndInsertLangInfo(SaveNetworkStepBaseReq req);

    /**
     * @despriction：查询某设备类型对应的配网步骤
     * @author  yeshiyuan
     * @created 2018/10/8 17:48
     * @return
     */
    DeviceNetworkStepBaseResp queryNetworkStep(Long deviceTypeId, Long networkTypeId);

    /**
     * @despriction：查询某设备类型支持的配网类型
     * @author  yeshiyuan
     * @created 2018/12/4 13:53
     */
    List<Long> supportNetworkType(Long deviceTypeId);

    /**
     * @despriction：删除设备类型的某种配网类型
     * @author  yeshiyuan
     * @created 2018/12/4 14:04
     */
    void deleteByNetworkTypes(Long deviceTypeId, List<Long> networkTypeIds);
}
