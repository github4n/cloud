package com.iot.boss.dao.malf;

import com.iot.boss.entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：管理员信息查询
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 19:33
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 19:33
 * 修改描述：
 */
@Mapper
public interface SystemUserMapper {

    /**
      * @despriction：查询管理员类型
      * @author  yeshiyuan
      * @created 2018/5/15 19:35
      * @param id
      * @return
      */
    @Select("select id," +
            "admin_name as adminName," +
            "email," +
            "nick_name as nickName," +
            "phone," +
            "admin_no as adminNo," +
            "admin_desc as adminDesc," +
            "password," +
            "create_time as createTime," +
            "cancel_time as cancelTime," +
            "admin_state as adminState," +
            "admin_type as adminType " +
            " from system_user where id = #{id} and admin_state in (2,3,4) limit 1")
    SystemUser getAdminById(@Param("id") Long id);
    /**
     * 描述：根据类型查管理员ID
     * @author 490485964@qq.com
     * @date 2018/5/16 11:30
     * @param
     * @return
     */
    @Select("select id from system_user where admin_type = #{adminType} and admin_state in (2,3,4)")
    List<Long> getAdminIdByType(@Param("adminType")Integer adminType);

    @Select("select count(1) from system_user where id = #{id} and admin_state in (2,3,4)")
    int checkUserExist(@Param("id") Long id);
}
