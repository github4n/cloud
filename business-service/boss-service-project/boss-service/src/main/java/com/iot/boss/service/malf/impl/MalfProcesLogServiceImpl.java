package com.iot.boss.service.malf.impl;

import com.iot.boss.dao.malf.MalfProcesLogMapper;
import com.iot.boss.dto.MalfProcesLogDto;
import com.iot.boss.entity.MalfProcesLog;
import com.iot.boss.service.malf.MalfProcesLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class MalfProcesLogServiceImpl implements MalfProcesLogService{

    @Autowired
    private MalfProcesLogMapper malfProcesLogMapper;

    @Override
    public int insert(MalfProcesLog malfProcesLog) {
        return malfProcesLogMapper.insert(malfProcesLog);
    }

    @Override
    public List<MalfProcesLogDto> findByMalfId(Long malfId) {
        return malfProcesLogMapper.findByMalfId(malfId);
    }
}
