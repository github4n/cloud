package com.iot.tenant.service;

import com.iot.tenant.vo.req.network.CopyNetworkStepReq;
import com.iot.tenant.vo.req.network.SaveNetworkStepTenantReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepTenantResp;
import com.iot.tenant.vo.resp.network.NetworkFileFormat;

import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤service(portal租户)
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 11:47
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 11:47
 * 修改描述：
 */
public interface IDeviceNetworkStepTenantService {
    /**
     * @despriction：拷贝配网步骤文案
     * @author  yeshiyuan
     * @created 2018/10/9 11:31
     * @return
     */
    void copyNetworkStep(CopyNetworkStepReq copyNetworkStepReq);

    /**
      * @despriction：拷贝配网步骤
      * @author  yeshiyuan
      * @created 2018/10/26 9:30
      * @return
      */
    void copyNetworkStepTenant(CopyNetworkStepReq copyNetworkStepReq, Long oldAppId);

    /**
     * @despriction：保存配网步骤文案（先删后插）
     * @author  yeshiyuan
     * @created 2018/10/9 14:02
     * @return
     */
    void save(SaveNetworkStepTenantReq req);

    /**
      * @despriction：查询配网步骤详情
      * @author  yeshiyuan
      * @created 2018/10/9 16:17
      * @return
      */
    DeviceNetworkStepTenantResp queryNetworkStep(Long tenantId, Long appId, Long productId);

    /**
      * @despriction：删除配网步骤及对应文案
      * @author  yeshiyuan
      * @created 2018/10/10 13:50
      * @return
      */
    void deleteNetworkStep(Long tenantId, Long appId, Long productId);

    /**
      * @despriction：获取配网步骤文件格式
      * @author  yeshiyuan
      * @created 2018/10/18 14:57
      * @return
      */
    Map<String, NetworkFileFormat> getNetworkFileFormat(Long tenantId, Long appId, Long productId);

    /**
      * @despriction：校验是否存在配网步骤
      * @author  yeshiyuan
      * @created 2018/11/29 13:46
      */
    boolean checkExist(Long appId, Long productId, Long tenantId);

    /**
     * @despriction：删除app下的产品配网
     * @author  yeshiyuan
     * @created 2018/10/10 13:50
     * @return
     */
    void deleteAppNetwork(Long appId, Long tenantId);
}
