package com.iot.boss.dao.malf;

import com.iot.boss.dto.MalfProcesLogDto;
import com.iot.boss.entity.MalfProcesLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：报障处理日志记录
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:45
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:45
 * 修改描述：
 */
@Mapper
public interface MalfProcesLogMapper {

    /**
      * @despriction：新增报障处理日志记录
      * @author  yeshiyuan
      * @created 2018/5/16 11:41
      * @param malfProcesLog 报障处理日志
      * @return
      */
    @Insert("INSERT into malf_proces_log("
//    		+ "id,"
    		+ "malf_id,handle_type,handle_time,handle_msg,handle_admin_id) " +
            "VALUES("
//            + "#{malfProcesLog.id},"
            + "#{malfProcesLog.malfId},#{malfProcesLog.handleType},#{malfProcesLog.handleTime},#{malfProcesLog.handleMsg},#{malfProcesLog.handleAdminId})")
    int insert(@Param("malfProcesLog") MalfProcesLog malfProcesLog);

    /**
      * @despriction：查找报障处理日志记录
      * @author  yeshiyuan
      * @created 2018/5/16 11:41
      * @param malfId 报障单id
      * @return
      */
    @Select("select l.id,l.malf_id as malfId," +
            "l.handle_type as handleType," +
            "l.handle_time as handleTime," +
            "l.handle_msg as handleMsg," +
            "u.nick_name as nickName from malf_proces_log l" +
            " left join system_user u on u.id=l.handle_admin_id" +
            " where l.malf_id = #{malfId} order by l.handle_time desc")
    List<MalfProcesLogDto> findByMalfId(@Param("malfId")Long malfId);
}
