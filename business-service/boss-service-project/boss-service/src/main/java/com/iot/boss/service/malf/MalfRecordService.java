package com.iot.boss.service.malf;

import com.iot.boss.dto.*;
import com.iot.boss.entity.MalfRecord;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:41
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:41
 * 修改描述：
 */
public interface MalfRecordService {
    /**
     * 描述：提交报账单
     * @author 490485964@qq.com
     * @date 2018/5/16 9:12
     * @param
     * @return
     */
    int submitMalf(MalfRecord malfRecord);
    /**
     * 描述：提交故障处理
     * @author 490485964@qq.com
     * @date 2018/5/16 10:49
     * @param
     * @return
     */
    int adminSubmitHandleMalf(MalfRecord malfRecord);

    /**
     * @despriction：校验当前处理人、报障单
     * @author  yeshiyuan
     * @created 2018/5/15 20:18
     * @param malfId 报障单id
     * @param curHandleAdminId 当前处理
     * @param handleStatus 处理状态
     * @return
     */
    public void checkCurDealAdmin(Long malfId,Long curHandleAdminId,Integer handleStatus);


    /**
      * @despriction：确认报障单修复完毕
      * @author  yeshiyuan
      * @created 2018/5/15 20:47
      * @param malfId 报障单id
      * @param handleStatus 处理状态
      * @param renovatTime 修复时间
      * @return
      */
    public int confirmFixProblem(Long malfId,Integer handleStatus,Date renovatTime);

    /**
      * @despriction：超级管理员确认是问题
      * @author  yeshiyuan
      * @created 2018/5/16 9:50
      * @param malfId 报障单id
      * @param preHandleAdminId 当前处理人
      * @param isRollBack 是否回退
      * @return
      */
    public int confirmIsProblem(Long malfId,Long preHandleAdminId,Integer isRollBack);

    /**
     * 描述：运维人员提交报障处理结果
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfRecord 报障单
     * @return void
     */
    void mainteProcessMalf(MalfRecord malfRecord);

    /**
     * 描述：超级管理人员退回报障处理
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfRecord 报障单
     * @return void
     */
    void returnMalfToMainte(MalfRecord malfRecord);

    /**
     * 描述：依据报障单id获取报障单信息
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfId 报障单id
     * @return void
     */
    MalfRecord getMalfRecordById(Long malfId);

    /**
     * 描述：获取用户报障列表
     * @author ouyangjie
     * @created 2018/5/15 20:02
     * @param search 查询条件
     * @return void
     */
    public List<UserMalfDto> getUserMalfList(UserMalfListSearch search);

    /**
     * 描述：获取管理人员列表
     * @author ouyangjie
     * @created 2018/5/15 20:02
     * @param type 查询条件
     * @return void
     */
    public List<MalfSysUserDto> getAdminSelectList(int type);

    /**
     * 描述：获取管理人员列表
     * @author ouyangjie
     * @created 2018/5/15 20:02
     * @param searchParam 查询条件
     * @return void
     */
    public List<UserMalfHistoryDto> getUserMalfHistoryList(UserMalfHistorySearch searchParam);

    /**
     * 描述：获取用户报障详情
     * @author ouyangjie
     * @created 2018/5/15 20:02
     * @param id 查询条件
     * @return void
     */
    public List<UserMalfProcessInfoDto> getUserMalfProcessDetails(long id);

    /**
     * 
     * 描述：获取未分配报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:25:40
     * @since 
     * @return
     */
    List<MalfRecord> getUnassignedMalfRecord();
    
    /**
     * 
     * 描述：获取未定级报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:26:29
     * @since 
     * @return
     */
    List<MalfRecord> getUnclassifiedMalfRecord();
    
    /**
     * 
     * 描述：获取处理中报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:27:04
     * @since 
     * @return
     */
    List<MalfRecord> getProcessingMalfRecord();
    
    /**
     * 
     * 描述：更新报障记录报障处理进度
     * @author 李帅
     * @created 2018年5月17日 下午6:17:50
     * @since 
     * @param malfRecordId
     * @param malfStatus
     */
    void updateRecordMalfStatus(Long malfRecordId, int malfStatus);
}
