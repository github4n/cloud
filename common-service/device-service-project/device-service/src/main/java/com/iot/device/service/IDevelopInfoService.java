package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.DevelopInfo;
import com.iot.device.vo.req.AddDevelopInfoReq;
import com.iot.device.vo.req.DevelopInfoListResp;
import com.iot.device.vo.req.DevelopInfoPageReq;

import java.util.List;

/**
 * <p>
 * 开发信息表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-29
 */
public interface IDevelopInfoService extends IService<DevelopInfo> {


    /**
     * 添加或修改开发者信息
     *
     * @param infoReq
     * @return
     * @author lucky
     * @date 2018/6/29 10:04
     */
    Long addOrUpdateDevelopInfo(AddDevelopInfoReq infoReq);


    /**
     * 获取所有的开发者信息
     *
     * @param
     * @return
     * @author lucky
     * @date 2018/6/29 10:06
     */
    List<DevelopInfoListResp> findDevelopInfoListAll();


    /**
     * 分页获取开发者信息
     *
     * @param pageReq
     * @return
     * @author lucky
     * @date 2018/6/29 10:16
     */
    Page<DevelopInfoListResp> findDevelopInfoPage(DevelopInfoPageReq pageReq);
}
