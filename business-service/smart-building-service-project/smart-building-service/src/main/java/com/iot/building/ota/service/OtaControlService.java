package com.iot.building.ota.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;

public interface OtaControlService {

    public void updateOtaVersion(Long orgId, String deviceId, Long tenantId, Long locationId);

    public void downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq);
    
    /**
     * 描述：获取OTA文件列表
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param
     * @return
     */
    Page<OtaFileInfoResp> getOtaFileList(@RequestBody OtaPageReq pageReq);
    
    /**
     * 描述：添加OTA文件信息
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param otaFileInfoResp
     * @return
     */
	int saveOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq);
	/**
     * 描述：更新OTA文件信息
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param otaFileInfoResp
     * @return
     */
	int updateOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq);
	/**
     * 描述：根据productId获取OTA文件信息
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param otaFileInfoResp
     * @return
     */
	OtaFileInfoResp findOtaFileInfoByProductId(@RequestBody OtaFileInfoReq otaFileInfoReq);
}
