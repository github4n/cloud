package com.iot.boss.service.malf;

import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public interface MalfService {

    /**
     * 描述：运维人员提交报障处理结果
     * @author mao2080@sina.com
     * @created 2018/5/16 9:29
     * @param adminId 当前处理人id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    void mainteProcessMalf(Long adminId, Long malfId, String handleMsg);

    /** 
     * 描述：超级管理人员退回报障处理
     * @author mao2080@sina.com
     * @created 2018/5/16 9:29
     * @param adminId 当前处理人id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    void returnMalfToMainte(Long adminId, Long malfId, String handleMsg);

    /**
     * 描述：提交报账单
     * @author 490485964@qq.com
     * @date 2018/5/16 10:28
     * @param userId 用户ID
     * @param malfDesc 故障描述
     * @param tenantId 租户Id
     * @return
     */
    boolean submitMalf(String userId, String malfDesc,  Long tenantId);
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
    boolean adminSubmitHandleMalf(String handleMsg,Long malfId, Integer malfRank,Long handleAdminId);

    /**
     * @despriction：确认是否是问题
     * @author  yeshiyuan
     * @created 2018/5/16 10:42
     * @param malfId 报障单id
     * @param remark 备注
     * @param rollBackFlag 回滚标志
     * @return
     */
    void confirmProblem(Long malfId, String remark, Integer rollBackFlag) ;

    /**
     * @despriction：超级管理员确认问题已修复
     * @author  yeshiyuan
     * @created 2018/5/15 19:28
     * @param null
     * @return
     */
    void confirmFixProblem(Long malfId,String remark);

    /**
     * @despriction：查询用户报障处理详情
     * @author  yeshiyuan
     * @created 2018/5/16 11:33
     * @param malfId 报障单id
     * @return
     */
    public MalfDetailDto getUserMalfDetail(Long malfId);

    /**
     * 
     * 描述：查询管理员值班
     * @author 李帅
     * @created 2018年5月16日 下午4:56:13
     * @since 
     * @param malfParam
     * @return
     */
    public PageInfo<MalfAttendanceTimerDto> getMalfAttendance(MalfParam malfParam);
    
    /**
     * 
     * 描述：设置管理员值班
     * @author 李帅
     * @created 2018年5月16日 下午4:56:53
     * @since 
     * @param malfAttendanceParam
     */
    public void setMalfAttendance(MalfAttendanceParam malfAttendanceParam);
    
    /**
     * 
     * 描述：删除管理员值班
     * @author 李帅
     * @created 2018年5月16日 下午4:57:26
     * @since 
     * @param malfAttendanceId
     */
    public void deleteMalfAttendance(String malfAttendanceId);

    /**
     *
     * 描述：查询用户报障记录
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    public PageInfo<UserMalfDto> getUserMalfList(UserMalfListSearch searchParam);

    /**
     *
     * 描述：管理(运维)人员列表查询
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    public PageInfo<MalfSysUserDto> getAdminSelectList(UserAdminSearch searchParam);

    /**
     *
     * 描述：用户报障历史记录查询
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    public PageInfo<UserMalfHistoryDto> getUserMalfHistoryList(UserMalfHistorySearch searchParam);
}
