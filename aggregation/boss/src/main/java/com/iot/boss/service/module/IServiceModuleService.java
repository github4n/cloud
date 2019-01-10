package com.iot.boss.service.module;

import com.iot.boss.vo.module.BossServiceModuleResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/10/26 15:40
 * 修改人： yeshiyuan
 * 修改时间：2018/10/26 15:40
 * 修改描述：
 */
public interface IServiceModuleService {

    /**
      * @despriction：查询产品对应的模组信息
      * @author  yeshiyuan
      * @created 2018/10/26 15:42
      * @return
      */
    List<BossServiceModuleResp> queryProductModule(Long productId);

    /**
    * @Description: 获取产品对应的功能组定义列表【事件方法属性【包括是否选中问题】】
    * 如果产品未定义功能组，则从设备类型上获取，返回显示
    * @param productId
    * @return:
    * @author: chq
    * @date: 2018/11/8 10:24
    **/
    List<BossServiceModuleResp> findServiceModuleListByProductId(Long productId);

}
