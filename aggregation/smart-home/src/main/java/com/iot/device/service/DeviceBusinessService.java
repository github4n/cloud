package com.iot.device.service;

import com.iot.device.vo.DeviceVo;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人：
 * 创建时间：2018/3/20 17:26
 * 修改人：
 * 修改时间：2018/3/20 17:26
 * 修改描述：
 */
public interface DeviceBusinessService {

    /**
     * 描述：计划绑定设备
     *
     * @param planId   计划id
     * @param deviceId 设备Id
     * @return
     * @author 490485964@qq.com
     * @date 2018/4/8 15:01
     */
    void planBandingDevice(String planId, String deviceId);

    /**
     * 
     * 描述：获取文件服务put预签名url
     * @author 李帅
     * @created 2018年7月17日 上午9:33:15
     * @since 
     * @param deviceId
     * @param planId
     * @param fileType
     * @return
     */
    Map<String, Object> getFilePreSignUrls(@RequestHeader(value="ssl_client_s_dn") String sslClientSDn,
    		@RequestParam("planId") String planId,@RequestParam("fileType") String fileType);
    
    /**
     * @param
     * @return
     * @despriction：获取设备列表
     * @author yeshiyuan
     * @created 2018/4/8 14:41
     */
    List<DeviceVo> getDeviceList();


    Map<String, Object> getDevList(Long tenantId, Long userId, Long homeId);

    /**
     * 设备排序
     * @param deviceIds
     * @return
     */
    Boolean sortDev(List<String> deviceIds);

    /**
     * 描述：通过设备Id 查询p2pId
     *
     * @return
     * @author 李帅
     * @created 2018年5月11日 下午1:58:31
     * @since
     */
    String getP2pId(String deviceId);

    /**
     * @param
     * @return
     * @despriction：加载未绑定计划的设备列表
     * @author yeshiyuan
     * @created 2018/4/8 15:42
     */
    List<DeviceVo> getUnBindPlanDeviceList();


    List<DeviceResp> getDeviceListByDirectDevice(String deviceId);
}
