package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.iot.device.model.GenerateModule;
import com.iot.device.model.ServiceModule;
import com.iot.device.vo.req.AddServiceModuleReq;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.ServiceModuleResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组表 服务类
 * </p>
 *
 * @author zhangyue
 * @since 2018-06-27
 */
public interface IGenerateModuleService extends IService<GenerateModule> {

    String generateModuleId(GenerateModuleReq generateModuleReq);

    PageInfo generateModuleList(GenerateModuleReq generateModuleReq);

}
