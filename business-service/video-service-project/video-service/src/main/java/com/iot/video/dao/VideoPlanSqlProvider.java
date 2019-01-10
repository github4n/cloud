package com.iot.video.dao;

import com.iot.video.dto.VideoFileParamDto;
import com.iot.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/26 9:41
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/26 9:41
 * 修改描述：
 */
public class VideoPlanSqlProvider {

    public String getVideoFileList(VideoFileParamDto vfpDto){
        return new SQL(){
            {
                SELECT("video_file_id,video_length");
                FROM("video_file");
                if(StringUtil.isNotEmpty(vfpDto.getPlanId())){
                    WHERE("plan_id=#{planId}");
                }
                if(StringUtil.isNotEmpty(vfpDto.getDeviceId())){
                    WHERE("device_id=#{deviceId}");
                }
            }
        }.toString();
    }

}
