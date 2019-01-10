package com.iot.user.mapper;

import com.iot.user.entity.FeedbackEntity;
import com.iot.user.entity.Userfeedback;
import com.iot.user.vo.FeedbackFileVo;
import com.iot.user.vo.FeedbackReq;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface UserFeedbackMapper {

    @Insert({
          "insert into user_feedback " +
          "(user_id,feedback_content,create_time)" +
          "values (#{userId,jdbcType=BIGINT},#{feedbackContent,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP})"})
    public int insertFeedbackContent(FeedbackEntity feedbackEntity);

    @Insert({
            "insert into user_feedback_fileId " +
                    "(user_id,tenant_id,file_id,create_time,update_time)" +
                    "values (#{userId,jdbcType=BIGINT},#{tenantId,jdbcType=BIGINT},#{fileId,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"})
    public int insertFeedbackFileId(FeedbackFileVo feedbackFileVo);


    @Insert(
            {  "<script>",
                    "insert into user_feedback_fileId " ,
                    "(user_id,tenant_id,file_id,create_time,update_time) values" ,
                    " <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">" ,
                    "(#{item.userId,jdbcType=BIGINT},#{item.tenantId,jdbcType=BIGINT},#{item.fileId,jdbcType=VARCHAR},",
                    "#{item.createTime,jdbcType=TIMESTAMP},#{item.updateTime,jdbcType=TIMESTAMP})",
                    "</foreach>",
                   "</script>"
            }
    )
    public int insertFeedbackFileIdByList(List<FeedbackFileVo> feedbackFileVo);

}
