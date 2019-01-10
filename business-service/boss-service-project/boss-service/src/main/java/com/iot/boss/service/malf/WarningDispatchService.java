package com.iot.boss.service.malf;


import java.util.Date;

import com.iot.boss.entity.MalfAttendanceTimer;

/**
 *
 * 项目名称：立达信IOT云平台
 * 模块名称：视频云后台管理
 * 功能描述：报障指派管理
 * 创建人： xiangyitao
 * 创建时间：2017年10月17日
 */
public interface WarningDispatchService {

    /**
     * 
     * 描述：保障派单添加流程
     * @author 李帅
     * @created 2018年5月16日 上午10:31:58
     * @since 
     * @param malfRecordId
     * @param createDateTime
     */
    void addWarning(Long malfRecordId, Date createDateTime);

    /**
     * 
     * 描述：更新redis保障信息
     * @author 李帅
     * @created 2018年5月16日 上午10:32:17
     * @since 
     * @param malfAttendanceTimer
     * @param type 1 ： 修改  2 ： 删除
     */
    void updateOrDelete(MalfAttendanceTimer malfAttendanceTimer,String type);

}
