package com.iot.boss.service.malf.impl;

import com.iot.boss.dao.malf.MalfRecordMapper;
import com.iot.boss.dto.*;
import com.iot.boss.entity.MalfRecord;
import com.iot.boss.enums.MalfHandleStatusEnum;
import com.iot.boss.exception.BusinessExceptionEnum;
import com.iot.boss.service.malf.MalfRecordService;
import com.iot.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:42
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:42
 * 修改描述：
 */
@Service
public class MalfRecordServiceImpl implements MalfRecordService {

    @Autowired
    private MalfRecordMapper malfRecordMapper;

    @Override
    public int submitMalf(MalfRecord malfRecord) {
        return malfRecordMapper.submitMalf(malfRecord);
    }

    @Override
    public int adminSubmitHandleMalf(MalfRecord malfRecord) {
        return malfRecordMapper.adminSubmitHandleMalf(malfRecord);
    }

    @Override
    public void checkCurDealAdmin(Long malfId, Long curHandleAdminId, Integer handleStatus) {
        int i = malfRecordMapper.checkCurDealAdmin(malfId,curHandleAdminId,handleStatus);
        if (i==0){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_PASS);
        }
    }

    @Override
    public int confirmFixProblem(Long malfId, Integer handleStatus, Date renovatTime) {
        return malfRecordMapper.confirmFixProblem(malfId,handleStatus,renovatTime);
    }

    /**
     * 描述：运维人员提交报障处理结果
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfRecord 报障单
     * @return void
     */
    public void mainteProcessMalf(MalfRecord malfRecord) {
        this.malfRecordMapper.mainteProcessMalf(malfRecord);
    }

    /**
     * 描述：超级管理人员退回报障处理
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfRecord 报障单
     * @return void
     */
    public void returnMalfToMainte(MalfRecord malfRecord) {
        this.malfRecordMapper.returnMalfToMainte(malfRecord);
    }

    /**
     * 描述：查询保障单
     * @author mao2080@sina.com
     * @created 2018/5/15 20:38
     * @param malfId 报障单id
     * @return com.iot.boss.entity.MalfRecord
     */
    public MalfRecord getMalfRecordById(Long malfId){
        return this.malfRecordMapper.getMalfRecordById(malfId);
    }

    /**
     * @despriction：超级管理员确认是问题
     * @author  yeshiyuan
     * @created 2018/5/16 9:50
     * @param malfId 报障单id
     * @param preHandleAdminId 当前处理人
     * @param isRollBack 是否回退
     * @return
     */
    @Override
    public int confirmIsProblem(Long malfId,Long preHandleAdminId,Integer isRollBack) {
        MalfRecord malfRecord = new MalfRecord();
        malfRecord.setId(malfId);
        malfRecord.setPreHandleAdminId(preHandleAdminId);
        malfRecord.setIsRollback(isRollBack);
        malfRecord.setHandleStatus(MalfHandleStatusEnum.CREATE.getValue());
        return this.malfRecordMapper.confirmIsProblem(malfRecord);
    }

    @Override
    public List<UserMalfDto> getUserMalfList(UserMalfListSearch search) {
        return malfRecordMapper.getUserMalfList(search);
    }

    @Override
    public List<MalfSysUserDto> getAdminSelectList(int type){
        return malfRecordMapper.getAdminSelectList(type);
    }

    @Override
    public List<UserMalfHistoryDto> getUserMalfHistoryList(UserMalfHistorySearch searchParam){
        return malfRecordMapper.getUserMalfHistoryList(searchParam);
    }

    @Override
    public List<UserMalfProcessInfoDto> getUserMalfProcessDetails(long id){
        return malfRecordMapper.getUserMalfProcessDetails(id);
    }
    
    /**
     * 
     * 描述：获取未分配报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:25:50
     * @since 
     * @return
     */
    @Override
    public List<MalfRecord> getUnassignedMalfRecord(){
    	return malfRecordMapper.getUnassignedMalfRecord();
    }
    
    /**
     * 
     * 描述：获取未定级报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:26:36
     * @since 
     * @return
     */
    @Override
    public List<MalfRecord> getUnclassifiedMalfRecord(){
    	return malfRecordMapper.getUnclassifiedMalfRecord();
    }
    
    /**
     * 
     * 描述：获取处理中报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:27:11
     * @since 
     * @return
     */
    @Override
    public List<MalfRecord> getProcessingMalfRecord(){
    	return malfRecordMapper.getProcessingMalfRecord();
    }
    
    /**
     * 
     * 描述：更新报障记录报障处理进度
     * @author 李帅
     * @created 2018年5月17日 下午6:17:58
     * @since 
     * @param malfRecordId
     * @param malfStatus
     */
    public void updateRecordMalfStatus(Long malfRecordId, int malfStatus) {
        this.malfRecordMapper.updateRecordMalfStatus(malfRecordId, malfStatus);
    }
}
