package com.iot.boss.restful.malf;

import com.github.pagehelper.PageInfo;
import com.iot.boss.api.UserMalfApi;
import com.iot.boss.dto.*;
import com.iot.boss.manager.malf.UserMalfServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：cloud
 * 功能描述：用户报障管理
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:41
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:41
 * 修改描述：
 */
@RestController
public class UserMalfRestful implements UserMalfApi{

    @Autowired
    private UserMalfServiceManager userMalfServiceManager;

    /**
     * 超级管理员确认问题已修复
     * @param adminId
     * @param malfId
     * @param remark
     */
    @Override
    public void confirmFixProblem(@RequestParam("adminId") Long adminId,@RequestParam("malfId") Long malfId, @RequestParam("remark") String remark) {
        userMalfServiceManager.confirmFixProblem(adminId, malfId, remark);
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
    public boolean submitMalf(@RequestParam("userId")String userId, @RequestParam("malfDesc")String malfDesc,@RequestParam("tenantId")Long tenantId) {
        return userMalfServiceManager.submitMalf(userId,  malfDesc,tenantId);
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
    public boolean adminSubmitHandleMalf(@RequestParam("handleMsg")String handleMsg, @RequestParam("malfId")Long malfId,
                                         @RequestParam("malfRank")Integer malfRank, @RequestParam(value = "handleAdminId",required = false)Long handleAdminId) {
        return userMalfServiceManager.adminSubmitHandleMalf(handleMsg,malfId,malfRank,handleAdminId);
    }

    /**
     * 超级管理员确认是否是问题
     * @param adminId 操作人id
     * @param malfId 报障单id
     * @param remark 备注
     * @param rollBackFlag 是否是问题
     */
    @Override
    public void confirmProblem(@RequestParam("adminId") Long adminId,@RequestParam("malfId") Long malfId,@RequestParam("remark") String remark,@RequestParam("rollBackFlag")Integer rollBackFlag) {
        userMalfServiceManager.confirmProblem(adminId, malfId, remark, rollBackFlag);
    }

    /**
     * 描述：运维人员提交报障处理结果
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param adminId 当前处理人id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    @Override
    public void mainteProcessMalf(@RequestParam("adminId") Long adminId, @RequestParam("malfId")Long malfId, @RequestParam("handleMsg")String handleMsg) {
        this.userMalfServiceManager.mainteProcessMalf(adminId, malfId, handleMsg);
    }

    /**
     * 描述：超级管理人员退回报障处理
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param adminId 当前处理人id
     * @param malfId 报障单id
     * @param handleMsg 备注
     * @return void
     */
    @Override
    public void returnMalfToMainte(@RequestParam("adminId") Long adminId, @RequestParam("malfId")Long malfId, @RequestParam("handleMsg")String handleMsg) {
        this.userMalfServiceManager.returnMalfToMainte(adminId, malfId, handleMsg);
    }

    @Override
    public MalfDetailDto getUserMalfDetail(Long malfId) {
        return this.userMalfServiceManager.getUserMalfDetail(malfId);
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
    public PageInfo<UserMalfDto> getUserMalfList(@RequestBody UserMalfListSearch searchParam) {
        return userMalfServiceManager.getUserMalfList(searchParam);
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
    @Override
    public PageInfo<MalfSysUserDto> getAdminSelectList(@RequestBody UserAdminSearch searchParam) {
        return userMalfServiceManager.getAdminSelectList(searchParam);
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
    @Override
    public PageInfo<UserMalfHistoryDto> getUserMalfHistoryList(@RequestBody UserMalfHistorySearch searchParam){
        return userMalfServiceManager.getUserMalfHistoryList(searchParam);
    }

}
