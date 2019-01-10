package com.iot.device.service.impl;

import com.iot.common.helper.Page;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.mapper.NetworkTypeMapper;
import com.iot.device.service.INetworkTypeService;
import com.iot.device.vo.req.NetworkTypePageReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：配网模式service
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 16:25
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 16:25
 * 修改描述：
 */
@Service
public class NetworkTypeServiceImpl implements INetworkTypeService {
    @Autowired
    private NetworkTypeMapper networkTypeMapper;

    @Override
    public NetworkTypeResp getNetworkType(Long networkTypeId) {
        return networkTypeMapper.findById(networkTypeId);
    }

    /**
     * @despriction：查询配网类型信息
     * @author  yeshiyuan
     * @created 2018/10/15 16:15
     * @return
     */
    @Override
    public Map<Long, NetworkTypeResp> getNetworkTypes(List<Long> networkTypeIds) {
        List<NetworkTypeResp> list = networkTypeMapper.findByIds(networkTypeIds);
        return list.stream().collect(Collectors.toMap(NetworkTypeResp::getId, a->a));
    }

    /**
     * @despriction：分页
     * @author  yeshiyuan
     * @created 2018/10/15 17:22
     * @return
     */
    @Override
    public Page<NetworkTypeResp> page(NetworkTypePageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<NetworkTypeResp> page =new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<NetworkTypeResp> list = networkTypeMapper.page(page);
        page.setRecords(list);
        Page<NetworkTypeResp> myPage=new com.iot.common.helper.Page<>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page,myPage);
        return myPage;
    }

    /**
     * @despriction：通过配网编码查找配网方式
     * @author  yeshiyuan
     * @created 2018/12/11 16:34
     */
    @Override
    public List<NetworkTypeResp> findByTypeCode(List<String> typeCodes) {
        if (typeCodes == null || typeCodes.isEmpty()) {
            return null;
        }
        return networkTypeMapper.findByTypeCode(typeCodes);
    }
}
