package com.iot.building.space.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.building.space.vo.SpaceBackgroundImgReq;
import com.iot.building.space.vo.SpaceBackgroundImgResp;

public interface SpaceBackgroundImgMapper {

    @Insert({
            "insert into space_background_img (",
            "id, 			    ",
            "create_by, 			    ",
            "update_by, 			    ",
            "create_time, 			    ",
            "update_time,		    ",
            "space_id, 				    ",
            "bg_img, 			    ",
            "thumbnail_img,				    ",
            "view_img, 			    ",
            "thumbnail_pos,			    ",
            "view_pos			    ",
            ")				    ",
            "values				    ",
            "(",
            "#{id},		    ",
            "#{createBy}, 	    ",
            "#{updateBy},	    ",
            "#{createTime},		    ",
            "#{updateTime},		    ",
            "#{spaceId}, 	    ",
            "#{bgImg},	    ",
            "#{thumbnailImg},	    ",
            "#{viewImg},	    ",
            "#{thumbnailPos},	    ",
            "#{viewPos}    ",
            ")                                  "
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public Integer save(SpaceBackgroundImgReq req);

    @Update(
            "<script>							  " +
            "update space_background_img							  " +
            "<set>								  " +
            "	<if test=\"updateTime != null\">			  " +
            "		update_time = #{updateTime},	  " +
            "	</if>							  " +
            "	<if test=\"updateBy != null\">				  " +
            "		update_by = #{updateBy},	  " +
            "	</if>							  " +
            "	<if test=\"spaceId != null\">				  " +
            "		space_id = #{spaceId},		  " +
            "	</if>							  " +
            "	<if test=\"bgImg != null\">				  " +
            "		bg_img = #{bgImg},	  " +
            "	</if>							  " +
            "	<if test=\"thumbnailImg != null\">				  " +
            "		thumbnail_img = #{thumbnailImg},		  " +
            "	</if>							  " +
            "	<if test=\"viewImg != null\">				  " +
            "		view_img = #{viewImg},	  " +
            "	</if>							  " +
            "	<if test=\"thumbnailPos != null\">				  " +
            "		thumbnail_pos = #{thumbnailPos},		  " +
            "	</if>							  " +
            "	<if test=\"viewPos != null\">			  " +
            "		view_pos = #{viewPos},	  " +
            "	</if>							  " +
            "</set>								  " +
            "where id = #{id}				  " +
            "</script>                              "
    )
    public Integer update(SpaceBackgroundImgReq req);

    @Delete({
    	"delete from space_background_img where id=#{id}     "
    })
    public Integer delete(Long id);


    /**
     * 根据条件获取背景图片
     *
     * @param params userId
     * @return
     */
    @Select(
        "<script>                                                 " +
        "select                                                   " +
        "id,							  " +
        "create_by as createBy ,				  " +
        "update_by as updateBy ,				  " +
        "create_time as createTime ,				  " +
        "update_time as updateTime ,			  " +
        "space_id as spaceId ,							  " +
        "bg_img as bgImg ,						  " +
        "thumbnail_img as thumbnailImg ,							  " +
        "view_img as viewImg ,					  " +
        "thumbnail_pos as thumbnailPos ,					  " +
        "view_pos as viewPos				  " +
        "from space_background_img 						  " +
        "where 1=1 						  " +
        "<if test=\"id != null\">				  " +
        "and id=#{id}				  " +
        "</if>							  " +
        "<if test=\"createTime != null\">			  " +
        "and create_time=#{createTime}		  " +
        "</if>							  " +
        "<if test=\"updateTime != null\">			  " +
        "and update_time=#{updateTime}	  " +
        "</if>							  " +
        "<if test=\"spaceId != null\">				  " +
        "and space_id=#{spaceId}			  " +
        "</if>							  " +
        "</script>                                        "
    )
    public List<SpaceBackgroundImgResp> getSpaceBackgroundImgByCondition(SpaceBackgroundImgReq req);
    
    
    /**
     * 根id获取背景图片
     *
     * @param params userId
     * @return
     */
    @Select(
        "select                                                   " +
        "id,							  " +
        "create_by as createBy ,				  " +
        "update_by as updateBy ,				  " +
        "create_time as createTime ,				  " +
        "update_time as updateTime ,			  " +
        "space_id as spaceId ,							  " +
        "bg_img as bgImg ,						  " +
        "thumbnail_img as thumbnailImg ,							  " +
        "view_img as viewImg ,					  " +
        "thumbnail_pos as thumbnailPos ,					  " +
        "view_pos as viewPos				  " +
        "from space_background_img 						  " +
        "where 1=1 						  " +
        "and id=#{id}				  " 
    )
    public SpaceBackgroundImgResp getSpaceBackgroundImgById(Long id);

}
