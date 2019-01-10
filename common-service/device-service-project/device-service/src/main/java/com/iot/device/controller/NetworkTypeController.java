package com.iot.device.controller;

import com.iot.common.helper.Page;
import com.iot.device.api.NetworkTypeApi;
import com.iot.device.service.INetworkTypeService;
import com.iot.device.vo.req.NetworkTypePageReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：配网类型api
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 16:29
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 16:29
 * 修改描述：
 */
@RestController
public class NetworkTypeController implements NetworkTypeApi{
    @Autowired
    private INetworkTypeService networkTypeService;

    @Override
    public NetworkTypeResp getNetworkType(@RequestParam("networkTypeId") Long networkTypeId) {
        return networkTypeService.getNetworkType(networkTypeId);
    }

    @Override
    public Map<Long, NetworkTypeResp> getNetworkTypes(@RequestParam("networkTypeIds")List<Long> networkTypeIds) {
        return networkTypeService.getNetworkTypes(networkTypeIds);
    }

    /**
     * @despriction：分页查询配网类型
     * @author  yeshiyuan
     * @created 2018/10/15 17:16
     * @return
     */
    @Override
    public Page<NetworkTypeResp> page(@RequestBody NetworkTypePageReq pageReq) {
        return networkTypeService.page(pageReq);
    }

    /**
     * @despriction：通过配网编码查找配网方式
     * @author  yeshiyuan
     * @created 2018/12/11 16:34
     */
    @Override
    public List<NetworkTypeResp> findByTypeCode(@RequestParam("typeCodes") List<String> typeCodes) {
        return networkTypeService.findByTypeCode(typeCodes);
    }
}
