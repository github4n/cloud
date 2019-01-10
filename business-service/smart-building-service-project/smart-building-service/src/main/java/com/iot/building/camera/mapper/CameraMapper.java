package com.iot.building.camera.mapper;

import com.iot.building.camera.vo.CameraPropertyVo;
import com.iot.building.camera.vo.CameraVo;
import com.iot.building.camera.vo.req.CameraPropertyReq;
import com.iot.building.camera.vo.req.CameraReq;
import com.iot.building.camera.vo.resp.CameraPropertyResp;
import com.iot.building.camera.vo.resp.CameraResp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface CameraMapper {

	/**
	 * 保存人脸的camera_record
	 */
	@Insert({
			"insert into camera_record (",
			"id, 			    ",
			"ip, 			    ",
			"name,		    ",
			"title, 				    ",
			"time, 			    ",
			"register,				    ",
			"url,			    ",
			"create_time			    ",
			")				    ",
			"values				    ",
			"(",
			"#{id},		    ",
			"#{ip},		    ",
			"#{name},		    ",
			"#{title}, 	    ",
			"#{time},	    ",
			"#{register},	    ",
			"#{url},	    ",
			"#{createTime}	    ",
			")                                  "
	})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int save(CameraReq cameraReq);



	/**
	 *查询camera_record的列表,通过ip
	 * @return
	 */
	@Select({
			"SELECT " +
					"		id, " +
					"		ip, " +
					"		name, " +
					"		title, " +
					"		register, " +
					"		time, " +
					"		url, " +
					"      create_time as createTime"+
					"		FROM " +
					"			camera_record " +
					"		where ip = #{ip} order by id desc limt 1"
	})
	CameraResp findLastOneByIp(CameraReq cameraReq);


    /**
     * 保存region_record
     */
    @Insert({
            "insert into region_record (",
            "id, 			    ",
            "region, 			    ",
            "value,  	    ",
            "camera_record_id  	    ",
            ")				    ",
            "values				    ",
            "(             "      ,
            "#{id},		    ",
            "#{region},		    ",
            "#{value},		    ",
            "#{cameraRecordId}		    ",
            ")                                  "
    })
    void savePropertyDetail(CameraPropertyVo cameraPropertyVo);


	@Select({
			"SELECT " +
					"		region , value , camera_record_id as cameraRecordId" +
					"		FROM " +
					"		region_record   " +
					"		where camera_record_id = #{cameraRecordId}"
	})
    List<CameraPropertyVo> findPropertyVoList(CameraPropertyVo cameraPropertyVo);



	@Select({
			"<script>							  " +
			"SELECT " +
					"		a.id, " +
					"		a.ip, " +
					"		a.name, " +
					"		a.title, " +
					"		a.register, " +
					"		a.time, " +
					"		a.url " +
					"		FROM " +
					"			camera_record a" +
                    "      where NOT EXISTS " +
					"    ( " +
					"      select * from camera_record b where b.time = a.time  and b.id &lt; a.id  " +
					"  ) " +
					"	<if test=\"name != null\">			  " +
					"		and a.name like CONCAT(CONCAT('%',#{name}),'%')  " +
					"	</if>	"  +
					"		ORDER BY a.time desc "+
					"</script>							  "
	})
    List<CameraResp> findCameraList(CameraReq cameraReq);


	@Select({
			"SELECT " +
					"		sum(t.ch1) as ch1, " +
					"		sum(t.ch2) as ch2, " +
					"		sum(t.ch3) as ch3, " +
					"		sum(t.ch4) as ch4, " +
					"		sum(t.ch1+t.ch2+t.ch3+t.ch4) as sum " +
					"		FROM " +
					"			region_hour_record t" +
					"		where SUBSTR(t.time,1,8) = #{time}  "
	})
	CameraPropertyResp count(CameraPropertyReq cameraPropertyReq);

	@Select({
			"SELECT " +
					"		t.ch1 as ch1, " +
					"		t.ch2 as ch2, " +
					"		t.ch3 as ch3, " +
					"		t.ch4 as ch4, " +
					"		sum(t.ch1+t.ch2+t.ch3+t.ch4) as sum, " +
					"		t.time_flag as timeFlag " +
					"		FROM " +
					"      region_hour_record t " +
					"      where SUBSTR(t.time,1,8) = #{time} " +
					"      GROUP BY t.time_flag,t.ch1,t.ch2,t.ch3,t.ch4 "
	})
	/*@Select({
			"SELECT " +
					"		sum(t2.ch1+t2.ch2+t2.ch3+t2.ch4) as maxSum, " +
					"		SUBSTR(t2.time,1,2) as time " +
					"		FROM " +
					"  (SELECT" +
					"    max(t.ch1) as ch1, " +
					"    max(t.ch2) as ch2, " +
					"    max(t.ch3) as ch3, " +
					"    max(t.ch4) as ch4, " +
					"    SUBSTR(t.time,9,2) as time " +
					"    FROM " +
					"    region_record t " +
					"    where SUBSTR(t.time,1,8) = #{time} " +
					"    GROUP BY SUBSTR(t.time,9,2) " +
					"    ) t2 "+
					"    GROUP BY SUBSTR(t2.time,1,2)  "
	})*/
	List<CameraPropertyResp> countEveryHour(CameraPropertyReq cameraPropertyReq);

	@Insert({
			"insert into region_record (",
			"id, 			    ",
			"ch1, 			    ",
			"ch2,  	    ",
			"ch3,  	    ",
			"ch4,  	    ",
			"time,  	    ",
			"create_time  	    ",
			")				    ",
			"values				    ",
			"(             "      ,
			"#{id},		    ",
			"#{ch1},		    ",
			"#{ch2},		    ",
			"#{ch3},		    ",
			"#{ch4},		    ",
			"#{time},		    ",
			"#{createTime}		    ",
			")                                  "
	})
	void saveDensity(CameraPropertyReq cameraPropertyReq);


	@Insert({
			"insert into region_hour_record (",
			"id, 			    ",
			"ch1, 			    ",
			"ch2,  	    ",
			"ch3,  	    ",
			"ch4,  	    ",
			"time_flag,  	    ",
			"time,    ",
			"create_time  	    ",
			")				    ",
			"values				    ",
			"(             "      ,
			"#{id},		    ",
			"#{ch1},		    ",
			"#{ch2},		    ",
			"#{ch3},		    ",
			"#{ch4},		    ",
			"#{timeFlag},		    ",
			"#{time},		    ",
			"#{createTime}		    ",
			")                                  "
	})
	void saveRegionHourRecord(CameraPropertyReq cameraPropertyReq);


	@Select({
			"SELECT " +
					"		t.id as id, " +
					"		t.ch1 as ch1, " +
					"		t.ch2 as ch2, " +
					"		t.ch3 as ch3, " +
					"		t.ch4 as ch4, " +
					"		t.time_flag as timeFlag, " +
					"		t.time as time, " +
					"		t.create_time as createTime " +
					"		 " +
					"		FROM " +
					"			region_hour_record t" +
					"		where t.time_flag = #{timeFlag}  "
	})
	CameraPropertyResp findOneHourRecord(CameraPropertyReq cameraPropertyReq);


	@Update(
			"<script>							  " +
					"update region_hour_record							  " +
					"<set>								  " +
					"	<if test=\"ch1 != null\">			  " +
					"		ch1 = #{ch1},	  " +
					"	</if>							  " +
					"	<if test=\"ch2 != null\">				  " +
					"		ch2 = #{ch2},		  " +
					"	</if>							  " +
					"	<if test=\"ch3 != null\">				  " +
					"		ch3 = #{ch3},	  " +
					"	</if>							  " +
					"	<if test=\"ch4 != null\">				  " +
					"		ch4 = #{ch4},		  " +
					"	</if>							  " +
					"	<if test=\"time != null\">				  " +
					"		time = #{time},	  " +
					"	</if>							  " +
					"	<if test=\"createTime != null\">				  " +
					"		create_time = #{createTime},		  " +
					"	</if>							  " +
					"</set>								  " +
					"where time_flag = #{timeFlag}				  " +
					"</script>                                                        "
	)
	void updateRegionHourRecord(CameraPropertyReq cameraPropertyReq);

	@Select({
			"SELECT " +
					"		t.ch1 as ch1, " +
					"		t.ch2 as ch2, " +
					"		t.ch3 as ch3, " +
					"		t.ch4 as ch4, " +
					"		t.time as time " +
					"		FROM " +
					"			region_record t " +
					"		order by t.create_time desc limit 1  "
	})
	CameraPropertyResp newCount();


	@Select({
			"SELECT " +
					"		id, " +
					"		ip, " +
					"		name, " +
					"		title, " +
					"		register, " +
					"		time, " +
					"		url, " +
					"      create_time as createTime"+
					"		FROM " +
					"			camera_record " +
					"		order by create_time DESC limit 3"
	})
	List<CameraVo> newFace();
}
