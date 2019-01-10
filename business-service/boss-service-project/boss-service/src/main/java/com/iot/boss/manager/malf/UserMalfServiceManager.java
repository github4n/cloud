package com.iot.boss.manager.malf;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.MalfDetailDto;
import com.iot.boss.dto.MalfProcesLogDto;
import com.iot.boss.dto.MalfSysUserDto;
import com.iot.boss.dto.UserAdminSearch;
import com.iot.boss.dto.UserMalfDto;
import com.iot.boss.dto.UserMalfHistoryDto;
import com.iot.boss.dto.UserMalfHistorySearch;
import com.iot.boss.dto.UserMalfListSearch;
import com.iot.boss.entity.MalfProcesLog;
import com.iot.boss.entity.MalfRecord;
import com.iot.boss.enums.AdminTypeEnum;
import com.iot.boss.enums.AllocatedEnum;
import com.iot.boss.enums.MalfHandleStatusEnum;
import com.iot.boss.enums.MalfHandleTypeEnum;
import com.iot.boss.enums.MalfRankEnum;
import com.iot.boss.enums.RollBackEnum;
import com.iot.boss.exception.BusinessExceptionEnum;
import com.iot.boss.service.malf.MalfProcesLogService;
import com.iot.boss.service.malf.MalfRecordService;
import com.iot.boss.service.malf.SystemUserService;
import com.iot.boss.service.malf.WarningDispatchService;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:49
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:49
 * 修改描述：
 */
@Component
public class UserMalfServiceManager {

    @Autowired
    private MalfRecordService malfRecordService;

    @Autowired
    private MalfProcesLogService malfProcesLogService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private WarningDispatchService warningDispatchService;


    /**线程池*/
    private ExecutorService excutor = Executors.newCachedThreadPool();

    private static final Logger logger = LoggerFactory.getLogger(UserMalfServiceManager.class);

    /**
      * @despriction：超级管理员确认问题已修复
      * @author  yeshiyuan
      * @created 2018/5/15 19:28
      * @param null
      * @return
      */
    public void confirmFixProblem(Long adminId,Long malfId,String remark){
        //校验当前处理人是否是超级管理员
        systemUserService.checkAdminAuth(adminId,AdminTypeEnum.SUPER_ADMIN.getValue());
        //校验订单是否当前人处理(包括校验状态）
        malfRecordService.checkCurDealAdmin(malfId,adminId, MalfHandleStatusEnum.DEAL_COMPLETE.getValue());
        //修改报障单处理状态
        malfRecordService.confirmFixProblem(malfId,MalfHandleStatusEnum.REPAIR_COMPLETE.getValue(),new Date());
        //记录处理日志(确认已修复)
        this.saveMalfProcesLog(adminId, malfId, remark, MalfHandleTypeEnum.REPAIR_COMPLETE);
    }
    /**
     * 描述：提交报账单
     * @author 490485964@qq.com
     * @date 2018/5/16 15:51
     * @param userId 用户Id
     * @param malfDesc 留言
     * @param tenantId 租户ID
     * @return
     */
    public boolean submitMalf(String userId, String malfDesc,Long tenantId){
        if(StringUtil.isBlank(userId)){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_USERID);
        }
        if(StringUtil.isBlank(malfDesc)){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_DESC);
        }
        if(null == tenantId){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_TENANTID);
        }
        boolean success = false;
        //新增报账单
        MalfRecord malfRecord = new MalfRecord();
        //用户ID
        malfRecord.setUserId(userId);
        //故障描述
        malfRecord.setMalfDesc(malfDesc);
        //租户ID
        malfRecord.setTenantId(tenantId);
        //创建状态
        malfRecord.setHandleStatus(MalfHandleStatusEnum.CREATE.getValue());
        //没有被超级管理员回退
        malfRecord.setIsRollback(RollBackEnum.ROLLBACK_N.getValue());
        //已分配给普通管理员
        malfRecord.setIsAllocated(AllocatedEnum.ALLOCATEY.getValue());
        //自增Id
//        Long malfRecordId = RedisCacheUtil.incr(ModuleConstants.DB_TABLE_MALF_RECORD,0L);
//        malfRecord.setId(malfRecordId);
        int count  = malfRecordService.submitMalf(malfRecord);
        if(1==count) success = true;
        //定时器分发
        dispatchWarningOrder(malfRecord.getId(), CalendarUtil.format(CalendarUtil.getNowTime(),"yyyy-MM-dd HH:mm:ss").getTime());

        return success;
    }
    private void dispatchWarningOrder(final Long malfRecordId,final Date date){
        excutor.submit(new Runnable(){
            @Override
            public void run() {
                try {
                    warningDispatchService.addWarning(malfRecordId, date);
                } catch (BusinessException e) {
                    logger.error("dispatch warning order error.",e);
                }
            }

        });
    }
    /**
     * 描述：管理员提交处理
     * @author 490485964@qq.com
     * @date 2018/5/16 10:04
     * @param handleMsg 操作留言
     * @param malfId 故障单Id
     * @param malfRank 故障等级
     * @param handleAdminId 操作人Id
     * @return
     */
    public boolean adminSubmitHandleMalf( String handleMsg,Long malfId,Integer malfRank,Long handleAdminId){
        if (malfId == null || malfId == 0){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "malfId is null");
        }
        if(null == malfRank){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_MALFRANK);
        }
        if(MalfRankEnum.NOTPROBLEM.getValue() != malfRank && null == handleAdminId){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_HANDLEADMINID);
        }
        MalfRecord curMalfRecord = this.malfRecordService.getMalfRecordById(malfId);
        if(curMalfRecord == null){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_NOT_EXISTS,"people not operatMan");
        }

        if(null != handleAdminId && 0 != handleAdminId){
            List<Long> idList = systemUserService.getAdminIdByType(AdminTypeEnum.OPERAT_ADMIN.getValue());
            if(null != idList && !idList.contains(handleAdminId)){
                throw new BusinessException(BusinessExceptionEnum.PEOPLE_NOT_OPERATMAN);
            }
        }

        if(MalfRankEnum.NOTPROBLEM.getValue()!=malfRank && MalfRankEnum.MILD.getValue()!=malfRank
                && MalfRankEnum.GENERAL.getValue()!=malfRank && MalfRankEnum.URGENT.getValue()!=malfRank){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }

        boolean success = false;
        MalfRecord malfRecord = new MalfRecord();
        //报账单ID
        malfRecord.setId(malfId);
        //上一个处理人
        malfRecord.setPreHandleAdminId(curMalfRecord.getCurHandleAdminId());
        //处理状态为处理中
        malfRecord.setHandleStatus(MalfHandleStatusEnum.HANDLE.getValue());
        //问题等级
        malfRecord.setMalfRank(malfRank);
        if(MalfRankEnum.NOTPROBLEM.getValue() == malfRank){
            //由超级管理员处理
            malfRecord.setCurHandleAdminId(this.getSuperAdminId());
        }else {
            //由运维人员处理
            malfRecord.setCurHandleAdminId(handleAdminId);
        }
        int count  =  malfRecordService.adminSubmitHandleMalf(malfRecord);
        if(1==count) success = true;
        this.saveMalfProcesLog(malfRecord.getPreHandleAdminId(), malfId, handleMsg, MalfHandleTypeEnum.CONFIRM_MALF);
        return success;
    }

    /**
     * @despriction：描述
     * @author  yeshiyuan
     * @created 2018/5/16 10:42
     * @param adminId 当前处理人
     * @param malfId 报障单id
     * @param remark 备注
     * @param rollBackFlag 回滚标志
     * @return
     */
    public void confirmProblem(Long adminId, Long malfId, String remark, Integer rollBackFlag) {
        //校验当前处理人是否是超级管理员
        systemUserService.checkAdminAuth(adminId,AdminTypeEnum.SUPER_ADMIN.getValue());
        //校验订单是否当前人处理(包括校验状态-》报障单创建）
        malfRecordService.checkCurDealAdmin(malfId,adminId, MalfHandleStatusEnum.HANDLE.getValue());
        if (rollBackFlag==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"rollBackFlag is null");
        }
        MalfHandleTypeEnum handleType = null;
        if (RollBackEnum.ROLLBACK_Y.getValue().intValue()==rollBackFlag.intValue()){
            //是问题 -> 回滚至普通管理员
            malfRecordService.confirmIsProblem(malfId,adminId,rollBackFlag);
            handleType = MalfHandleTypeEnum.CONFIRM_IS_PROPLEM;
        }else if (RollBackEnum.ROLLBACK_N.getValue().intValue()==rollBackFlag.intValue()){
            //非问题 -> 修改报障单处理状态为修复完毕
            malfRecordService.confirmFixProblem(malfId,MalfHandleTypeEnum.REPAIR_COMPLETE.getValue(),new Date());
            handleType = MalfHandleTypeEnum.REPAIR_COMPLETE;
        }
        this.saveMalfProcesLog(adminId, malfId, remark, handleType);
    }

    /**
     * 描述：运维人员提交报障处理结果
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param adminId 当前操作员id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    public void mainteProcessMalf(Long adminId, Long malfId, String handleMsg) {
        if(adminId == null){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_HANDLEADMINID);
        }
        if (malfId == null || malfId == 0){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "malfId is null");
        }
        MalfRecord malfRecord = this.malfRecordService.getMalfRecordById(malfId);
        if(malfRecord == null){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_NOT_EXISTS);
        }
        if(!adminId.equals(malfRecord.getCurHandleAdminId())){
            throw new BusinessException(BusinessExceptionEnum.ADMIN_AUTH_NOT_ENOUGH);
        }
        if(!MalfHandleStatusEnum.HANDLE.getValue().equals(malfRecord.getHandleStatus())){
            throw new BusinessException(BusinessExceptionEnum.PROCESSING_STATE_NOT_MATCH);
        }
        MalfRecord record = new MalfRecord(malfId);
        record.setCurHandleAdminId(this.getSuperAdminId());
        record.setPreHandleAdminId(malfRecord.getCurHandleAdminId());
        record.setHandleStatus(MalfHandleStatusEnum.DEAL_COMPLETE.getValue());
        record.setHandleEndTime(new Date());
        this.malfRecordService.mainteProcessMalf(record);
        this.saveMalfProcesLog(adminId, malfId, handleMsg, MalfHandleTypeEnum.HANDLE_COMPLETE);
    }

    /**
     * 描述：超级管理人员退回报障处理
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfId 当前操作员id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    public void returnMalfToMainte(Long adminId, Long malfId, String handleMsg) {
        if(adminId == null){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_CHECK_NO_HANDLEADMINID);
        }
        if (malfId == null || malfId == 0){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "malfId is null");
        }
        MalfRecord malfRecord = this.malfRecordService.getMalfRecordById(malfId);
        if(malfRecord == null){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_NOT_EXISTS);
        }
        if(!adminId.equals(malfRecord.getCurHandleAdminId())){
            throw new BusinessException(BusinessExceptionEnum.ADMIN_AUTH_NOT_ENOUGH);
        }
        if(!MalfHandleStatusEnum.DEAL_COMPLETE.getValue().equals(malfRecord.getHandleStatus())){
            throw new BusinessException(BusinessExceptionEnum.PROCESSING_STATE_NOT_MATCH);
        }
        MalfRecord record = new MalfRecord(malfId);
        record.setCurHandleAdminId(malfRecord.getPreHandleAdminId());
        record.setPreHandleAdminId(malfRecord.getCurHandleAdminId());
        record.setHandleStatus(MalfHandleStatusEnum.HANDLE.getValue());
        this.malfRecordService.returnMalfToMainte(record);
        this.saveMalfProcesLog(adminId, malfId, handleMsg, MalfHandleTypeEnum.REPAIR_NO_COMPLETE);
    }

    /**
     * 描述：构建日志
     * @author mao2080@sina.com
     * @created 2018/5/15 20:47
     * @param malfId 当前操作员id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @param type 处理类别
     * @return com.iot.boss.entity.MalfProcesLog
     */
    private void saveMalfProcesLog(Long adminId, Long malfId, String handleMsg, MalfHandleTypeEnum type){
//        Long logId = RedisCacheUtil.incr(ModuleConstants.DB_TABLE_MALF_PROCES_LOG,0L);
        MalfProcesLog log = new MalfProcesLog(null, malfId);
        log.setHandleAdminId(adminId);
        log.setHandleMsg(handleMsg);
        log.setHandleTime(new Date());
        log.setHandleType(type.getValue());
        this.malfProcesLogService.insert(log);
    }

    /**
     * 描述：获取超级管理员ID
     * @author mao2080@sina.com
     * @created 2018/5/16 10:02
     * @param
     * @return java.lang.Long
     */
    private Long getSuperAdminId(){
        List<Long> list = this.systemUserService.getAdminIdByType(AdminTypeEnum.SUPER_ADMIN.getValue());
        if(list.isEmpty()){
            throw new BusinessException(BusinessExceptionEnum.SUPERADMIN_NOT_EXISTS);
        }else {
            return list.get(0);
        }
    }
    
    /**
      * @despriction：查询用户报障处理详情
      * @author  yeshiyuan
      * @created 2018/5/16 11:33
      * @param malfId 报障单id
      * @return 
      */
    public MalfDetailDto getUserMalfDetail(Long malfId){
        if (malfId==null || malfId==0){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"malfId is null");
        }
        MalfDetailDto malfDetailDto = new MalfDetailDto();
        MalfRecord malfRecord = malfRecordService.getMalfRecordById(malfId);
        if (malfRecord==null){
            throw new BusinessException(BusinessExceptionEnum.MALF_RECORD_NOT_EXISTS);
        }
        malfDetailDto.setCreateTime(malfRecord.getCreateTime());
        malfDetailDto.setConfirmTime(malfRecord.getConfirmTime());
        malfDetailDto.setHandleTime(malfRecord.getHandleStartTime());
        malfDetailDto.setRenovateTime(malfRecord.getRenovateTime());
        malfDetailDto.setHandleStatus(malfRecord.getHandleStatus());
        malfDetailDto.setMalfId(malfId);
        //查询报障处理日志
        List<MalfProcesLogDto> logDtos = malfProcesLogService.findByMalfId(malfId);
        malfDetailDto.setList(logDtos);
        return malfDetailDto;
    }

    /**
     * @despriction：查询用户报障日志
     * @author  ouyangjie
     * @created 2018/5/15 15:28
     * @param searchParam
     * @return null
     */
    public PageInfo<UserMalfDto> getUserMalfList(UserMalfListSearch searchParam) {
        if(null == searchParam){
            throw new BusinessException(BusinessExceptionEnum.OBJECT_IS_NULL, "search param is null");
        }
        if(null == searchParam.getDateStart()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param dateStart is null or black");
        }
        if(null == searchParam.getDateEnd()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param dateEnd is null or black");
        }
        if(!searchParam.checkHandleStatusValid()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param handleStatus is not 0 or 1 or 2");
        }
        if(!searchParam.checkMalfStatusValid()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param malfStatus should be 0 1 2 3");
        }
        if(!searchParam.checkMalRankValid()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param malfRank should be 0 1 2 3");
        }
        if(searchParam.getUserId() != null && searchParam.getUserId().isEmpty()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param userId is blank");
        }
        if(!searchParam.checkDateTypeValid()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param dateType should be 0 1 2 3");
        }
        PageHelper.startPage(CommonUtil.getPageNum(searchParam), CommonUtil.getPageSize(searchParam),true);
        try {
            return new PageInfo<>(malfRecordService.getUserMalfList(searchParam));
        }catch (Exception e){
            throw new BusinessException(BusinessExceptionEnum.INTERNAL_ERROR, "internal error");
        }
    }

    /**
     * @despriction：管理人员查询
     * @author  ouyangjie
     * @created 2018/5/15 15:28
     * @param search
     * @return null
     */
    public PageInfo<MalfSysUserDto> getAdminSelectList(UserAdminSearch search){
        if(search == null){
            throw new BusinessException(BusinessExceptionEnum.OBJECT_IS_NULL, "search param is null");
        }
        if(!AdminTypeEnum.checkIsValid(search.getType())){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param type should be 0 1 2");
        }
        PageHelper.startPage(CommonUtil.getPageNum(search), CommonUtil.getPageSize(search),true);
        try {
            return new PageInfo<>(malfRecordService.getAdminSelectList(search.getType()));
        }catch (Exception e){
            throw new BusinessException(BusinessExceptionEnum.INTERNAL_ERROR, "internal error");
        }
    }

    /**
     * @despriction：报障历史记录查询
     * @author  ouyangjie
     * @created 2018/5/15 15:28
     * @param searchParam
     * @return null
     */
    public PageInfo<UserMalfHistoryDto> getUserMalfHistoryList(UserMalfHistorySearch searchParam) {
        if(null == searchParam){
            throw new BusinessException(BusinessExceptionEnum.OBJECT_IS_NULL, "search param is null");
        }
        if(!searchParam.checkMalfStatusValid()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param malfStatus should be 0 1 2 3");
        }
        if(!searchParam.checkMalRankValid()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param malfRank should be 0 1 2 3");
        }
        if(searchParam.getUserId() != null && searchParam.getUserId().isEmpty()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param userId is blank");
        }
        if(null == searchParam.getSearchDate()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param searchDate is null");
        }
        if(!searchParam.checkDateTypeValid()){
            throw new BusinessException(BusinessExceptionEnum.MALF_DATE_TYPE_ERROR, "search param dateType should be 0 1 2 3");
        }
        PageHelper.startPage(CommonUtil.getPageNum(searchParam), CommonUtil.getPageSize(searchParam),true);
        try {
            List<UserMalfHistoryDto> list = malfRecordService.getUserMalfHistoryList(searchParam);
            for(UserMalfHistoryDto tmp : list){
                tmp.setDetails(malfRecordService.getUserMalfProcessDetails(tmp.getMalfId()));
            }
            return new PageInfo<>(list);
        }catch (Exception e){
            throw new BusinessException(BusinessExceptionEnum.INTERNAL_ERROR, "internal error");
        }
    }

}


