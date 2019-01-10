package com.iot.boss.service.malf.impl;

import com.iot.boss.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.iot.boss.api.MalfAttendanceApi;
import com.iot.boss.api.UserMalfApi;
import com.iot.boss.service.malf.MalfService;
import com.iot.saas.SaaSContextHolder;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：BOSS聚合层
 * 功能描述：报障业务
 * 创建人： mao2080@sina.com
 * 创建时间：2018/5/16 9:22
 * 修改人： mao2080@sina.com
 * 修改时间：2018/5/16 9:22
 * 修改描述：
 */
@Service("malfService")
public class MalfServiceImpl implements MalfService{

    @Autowired
    private UserMalfApi userMalfApi;

    @Autowired
    private MalfAttendanceApi malfAttendanceApi;
    
    /**
     * 描述：运维人员提交报障处理结果
     * @author mao2080@sina.com
     * @created 2018/5/16 9:29
     * @param adminId 当前处理人id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    public void mainteProcessMalf(Long adminId, Long malfId, String handleMsg){
        this.userMalfApi.mainteProcessMalf(adminId, malfId, handleMsg);
    }

    /**
     * 描述：超级管理人员退回报障处理
     * @author mao2080@sina.com
     * @created 2018/5/16 9:29
     * @param adminId 当前处理人id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    public void returnMalfToMainte(Long adminId, Long malfId, String handleMsg){
        this.userMalfApi.returnMalfToMainte(adminId, malfId, handleMsg);
    }

    /**
     * 描述：提交报账单
     * @author 490485964@qq.com
     * @date 2018/5/16 10:28
     * @param userId 用户ID
     * @param malfDesc 故障描述
     * @param tenantId 租户Id
     * @return
     */
    @Override
    public boolean submitMalf(String userId, String malfDesc, Long tenantId) {
        return userMalfApi.submitMalf(userId,malfDesc,tenantId);
    }
    /**
     * 描述：提交故障处理
     * @author 490485964@qq.com
     * @date 2018/5/16 10:29
     * @param handleMsg  留言
     * @param malfId 故障ID
     * @param malfRank 故障等级
     * @param handleAdminId 处理人ID
     * @return
     */
    @Override
    public boolean adminSubmitHandleMalf(String handleMsg, Long malfId, Integer malfRank, Long handleAdminId) {
        return userMalfApi.adminSubmitHandleMalf(handleMsg,malfId,malfRank,handleAdminId);
    }

    /**
     * @despriction：确认是否是问题
     * @author  yeshiyuan
     * @created 2018/5/16 10:42
     * @param adminId 当前处理人
     * @param malfId 报障单id
     * @param remark 备注
     * @param rollBackFlag 回滚标志
     * @return
     */
    @Override
    public void confirmProblem(Long malfId, String remark, Integer rollBackFlag) {
        this.userMalfApi.confirmProblem(SaaSContextHolder.getCurrentUserId(), malfId, remark, rollBackFlag);
    }

    /**
     * @despriction：超级管理员确认问题已修复
     * @author  yeshiyuan
     * @created 2018/5/15 19:28
     * @param null
     * @return
     */
    @Override
    public void confirmFixProblem(Long malfId, String remark) {
        this.userMalfApi.confirmFixProblem(SaaSContextHolder.getCurrentUserId(), malfId, remark);
    }

    /**
     * @despriction：查询用户报障处理详情
     * @author  yeshiyuan
     * @created 2018/5/16 11:33
     * @param malfId 报障单id
     * @return
     */
    @Override
    public MalfDetailDto getUserMalfDetail(Long malfId) {
        return this.userMalfApi.getUserMalfDetail(malfId);
    }
    
    /**
     * 
     * 描述：查询管理员值班
     * @author 李帅
     * @created 2018年5月16日 下午4:56:23
     * @since 
     * @param malfParam
     * @return
     */
    @Override
    public PageInfo<MalfAttendanceTimerDto> getMalfAttendance(MalfParam malfParam) {
        return this.malfAttendanceApi.getMalfAttendance(malfParam);
    }
    
    /**
     * 
     * 描述：设置管理员值班
     * @author 李帅
     * @created 2018年5月16日 下午4:57:01
     * @since 
     * @param malfAttendanceParam
     */
    @Override
    public void setMalfAttendance(MalfAttendanceParam malfAttendanceParam) {
        this.malfAttendanceApi.setMalfAttendance(malfAttendanceParam);
    }
    
    /**
     * 
     * 描述：删除管理员值班
     * @author 李帅
     * @created 2018年5月16日 下午4:57:35
     * @since 
     * @param malfAttendanceId
     */
    @Override
    public void deleteMalfAttendance(String malfAttendanceId) {
        this.malfAttendanceApi.deleteMalfAttendance(malfAttendanceId);
    }

    /**
     *
     * 描述：查询用户报障记录
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    @Override
    public PageInfo<UserMalfDto> getUserMalfList(UserMalfListSearch searchParam){
        return this.userMalfApi.getUserMalfList(searchParam);
    }

    /**
     *
     * 描述：管理(运维)人员列表查询
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    public PageInfo<MalfSysUserDto> getAdminSelectList(UserAdminSearch searchParam){
        return this.userMalfApi.getAdminSelectList(searchParam);
    }

    /**
     *
     * 描述：用户报障历史记录查询
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    public PageInfo<UserMalfHistoryDto> getUserMalfHistoryList(UserMalfHistorySearch searchParam){
        return this.userMalfApi.getUserMalfHistoryList(searchParam);
    }
}