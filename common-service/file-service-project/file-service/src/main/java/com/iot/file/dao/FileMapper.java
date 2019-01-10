package com.iot.file.dao;

import com.iot.file.entity.FileBean;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

@Mapper
public interface FileMapper {


    /**
     * 
     * 描述：添加文件信息
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:54:04
     * @since 
     * @param fileBean
     * @throws Exception
     */
    @Insert({
            "insert into file_info (file_id, file_type,tenant_id,create_time,file_path,update_time,file_name)",
            "values (#{fileBean.fileId,jdbcType=VARCHAR}, #{fileBean.fileType,jdbcType=VARCHAR},",
            "#{fileBean.tenantId,jdbcType=BIGINT},#{fileBean.createTime,jdbcType=TIMESTAMP},#{fileBean.filePath,jdbcType=VARCHAR},"
            + "#{fileBean.updateTime,jdbcType=TIMESTAMP},#{fileBean.fileName,jdbcType=VARCHAR})"
    })
    void insertFileInfo(@Param("fileBean")FileBean fileBean);

    @Insert({"<script>",
            "insert into file_info (file_id, file_type,tenant_id,create_time,file_path,update_time,file_name)",
            "values " +
                    "<foreach collection='fileBeans' item='fileBean' separator=','>" +
                    "(" +
                    " #{fileBean.fileId,jdbcType=VARCHAR}, #{fileBean.fileType,jdbcType=VARCHAR},",
                     "#{fileBean.tenantId,jdbcType=BIGINT},#{fileBean.createTime,jdbcType=TIMESTAMP},#{fileBean.filePath,jdbcType=VARCHAR},"
                     + "#{fileBean.updateTime,jdbcType=TIMESTAMP},#{fileBean.fileName,jdbcType=VARCHAR}" +
                             ")" +
                    "</foreach>",
            "</script>"
    })
    void insertFileInfos(@Param("fileBeans")List<FileBean> fileBeans);

    /**
     * 
     * 描述：根据文件id获取文件信息
     * @author zhouzongwei
     * @created 2018年3月17日 下午3:21:21
     * @since 
     * @param fileId
     * @return
     * @throws Exception
     */
    @Select({
            "SELECT file_path as filePath FROM file_info where file_id =#{fileId,jdbcType=VARCHAR}"
    })
    String getFilePath(@Param("fileId")String fileId);
    
    /**
     * 
     * 描述：获取批量文件信息
     * @author zhouzongwei
     * @created 2018年3月17日 下午3:22:41
     * @since 
     * @param fileIds
     * @return
     * @throws Exception
     */
    @Select({
            "<script>",
            "SELECT id,file_path as filePath,file_id as fileId,file_name as fileName FROM file_info",
            "where file_id in",
            "<foreach item='fileId' index='index' collection='fileIds' open='(' separator=',' close=')'>",
            "#{fileId}",
            "</foreach>",
            "</script>"
    })
    @Results({
            @Result(column = "fileId", property = "fileId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "filePath", property = "filePath", jdbcType = JdbcType.VARCHAR),
            @Result(column = "fileName", property = "fileName", jdbcType = JdbcType.VARCHAR)
    })
    List<FileBean> getFileInfoList(@Param("fileIds")List<String> fileIds);


    /**
      * @despriction：删除文件信息
      * @author  yeshiyuan
      * @created 2018/4/23 16:21
      * @param fileId 文件uuid
      * @return
      */
    @Delete("delete from file_info where file_id = #{fileId}")
    int delete(@Param("fileId") String fileId);

    /**
     * @despriction：批量删除文件信息
     * @author  yeshiyuan
     * @created 2018/4/23 16:21
     * @param fileIds 文件uuid集合
     * @return
     */
    @Delete("<script>"+
            "delete from file_info where file_id in"+
            "<foreach item='fileId' index='index' collection='fileIds' open='(' separator=',' close=')'>" +
            "#{fileId}"+
            "</foreach>"+
            "</script>"
    )
    int batchDelete(@Param("fileIds") List<String> fileIds);

    /**
      * @despriction：通过文件uuid获取文件信息
      * @author  yeshiyuan
      * @created 2018/4/24 10:54
      * @param fileId 文件uuid
      * @return
      */
    @Select("select * from file_info where file_id = #{fileId}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT,id = true),
            @Result(column = "file_id", property = "fileId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "file_type", property = "fileType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "file_path", property = "filePath", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "file_name", property = "fileName", jdbcType = JdbcType.VARCHAR)
    })
    FileBean getFileInfoByFileId(@Param("fileId")String fileId);

    /**
      * @despriction：修改文件的修改时间字段
      * @author  yeshiyuan
      * @created 2018/4/24 11:29
      * @param fileId 文件uuid
      * @param updateTime 修改时间
      * @return
      */
    @Update("update file_info set update_time = #{updateTime}, file_name=#{fileName} where file_id = #{fileId}")
    int updateFileUpdateTime(@Param("fileId") String fileId,@Param("updateTime") Date updateTime,@Param("fileName") String fileName);


    /**
     * @despriction：批量删除文件信息
     * @author  yeshiyuan
     * @created 2018/4/23 16:21
     * @param fileIds 文件id集合
     * @return
     */
    @Delete("<script>"+
            "delete from file_info where id in"+
            "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}"+
            "</foreach>"+
            "</script>"
    )
    int batchDeleteByIds(@Param("ids") List<Long> ids);
}