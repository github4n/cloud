package com.iot.device.service;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.NetworkTypePageReq;
import com.iot.device.vo.rsp.NetworkTypeResp;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：配网类型service
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 16:24
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 16:24
 * 修改描述：
 */
public interface INetworkTypeService {

    /**
      * @despriction：查询配网类型信息
      * @author  yeshiyuan
      * @created 2018/10/15 16:25
      * @param null
      * @return
      */
    NetworkTypeResp getNetworkType(Long networkTypeId);

    /**
     * @despriction：查询配网类型信息
     * @author  yeshiyuan
     * @created 2018/10/15 16:15
     * @return
     */
    Map<Long, NetworkTypeResp> getNetworkTypes(List<Long> networkTypeIds);

    /**
      * @despriction：分页
      * @author  yeshiyuan
      * @created 2018/10/15 17:22
      * @return
      */
    Page<NetworkTypeResp> page(NetworkTypePageReq pageReq);

    /**
     * @despriction：通过配网编码查找配网方式
     * @author  yeshiyuan
     * @created 2018/12/11 16:34
     */
    List<NetworkTypeResp> findByTypeCode(List<String> typeCodes);
}
