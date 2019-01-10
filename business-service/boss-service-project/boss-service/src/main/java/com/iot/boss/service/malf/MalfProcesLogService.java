package com.iot.boss.service.malf;

import com.iot.boss.dto.MalfProcesLogDto;
import com.iot.boss.entity.MalfProcesLog;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：报障处理日志记录
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:53
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:53
 * 修改描述：
 */
public interface MalfProcesLogService {

    /**
      * @despriction：报障操作日志记录
      * @author  yeshiyuan
      * @created 2018/5/15 19:19
      * @param malfProcesLog
      * @return
      */
    int insert(MalfProcesLog malfProcesLog);

    /**
     * @despriction：查找报障处理日志记录
     * @author  yeshiyuan
     * @created 2018/5/16 11:41
     * @param malfId 报障单id
     * @return
     */
    List<MalfProcesLogDto> findByMalfId(Long malfId);
}
