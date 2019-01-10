package com.iot.boss.service.malf;

import com.iot.boss.entity.SystemUser;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：管理员信息
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 19:39
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 19:39
 * 修改描述：
 */
public interface SystemUserService {

    /**
     * @despriction：校验管理员权限
     * @author  yeshiyuan
     * @created 2018/5/15 19:35
     * @param id
     * @Param compareType 对比的管理员类型
     * @return
     */
    SystemUser checkAdminAuth(Long id, Integer compareType);
    /**
     * 描述：根据类型获取管理员id
     * @author 490485964@qq.com
     * @date 2018/5/16 10:50
     * @param
     * @return
     */
    List<Long> getAdminIdByType(Integer adminType);
    /**
     * 描述：根据ID校验管理员是否存在
     * @author 490485964@qq.com
     * @date 2018/5/21 17:27
     * @param
     * @return
     */
    int checkUserExist(Long id);
}
