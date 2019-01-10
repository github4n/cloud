package com.iot.user.mapper;

import com.iot.user.entity.SmartToken;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SmartTokenMapper {

    @Insert({
            "insert into smart_token (user_id, ",
            "smart, access_token, ",
            "refresh_token, update_time, third_party_info_id) ",
            "values (#{userId,jdbcType=BIGINT}, ",
            "#{smart,jdbcType=TINYINT}, #{accessToken,jdbcType=VARCHAR}, ",
            "#{refreshToken,jdbcType=VARCHAR},",
            "#{updateTime,jdbcType=TIMESTAMP},",
            "#{thirdPartyInfoId,jdbcType=BIGINT})"})
    int insert(SmartToken record);

    @Select({
            "select",
            "id, user_id as userId, access_token as accessToken, refresh_token as refreshToken, smart, update_time as updateTime,",
            "local_access_token as localAccessToken, local_refresh_token as localRefreshToken, third_party_info_id as thirdPartyInfoId",
            "from smart_token",
            "where user_id = #{userId,jdbcType=BIGINT} AND smart = #{smart,jdbcType=TINYINT}",
            " order by update_time desc limit 0,1"})
    SmartToken getByUserIdAndSmart(@Param("userId") Long userId, @Param("smart") Integer smart);

    @Select({
            "select",
            "id, user_id as userId, access_token as accessToken, refresh_token as refreshToken, smart, update_time as updateTime,",
            "local_access_token as localAccessToken, local_refresh_token as localRefreshToken, third_party_info_id as thirdPartyInfoId",
            "from smart_token",
            "where user_id = #{userId,jdbcType=BIGINT} AND third_party_info_id = #{thirdPartyInfoId,jdbcType=BIGINT}",
            " order by update_time desc limit 0,1"})
    SmartToken getByUserIdAndThirdPartyInfoId(@Param("userId") Long userId, @Param("thirdPartyInfoId") Long thirdPartyInfoId);

    @Select({
            "select",
            "id, user_id as userId, access_token as accessToken, refresh_token as refreshToken, smart, update_time as updateTime,",
            "local_access_token as localAccessToken, local_refresh_token as localRefreshToken, third_party_info_id as thirdPartyInfoId",
            "from smart_token",
            "where user_id = #{userId,jdbcType=BIGINT} AND third_party_info_id IS NOT NULL"})
    public List<SmartToken> findThirdPartyInfoIdNotNull(@Param("userId") Long userId);

    @Select({
            "select",
            "id, user_id as userId, access_token as accessToken, refresh_token as refreshToken, smart, update_time as updateTime,",
            "local_access_token as localAccessToken, local_refresh_token as localRefreshToken, third_party_info_id as thirdPartyInfoId",
            "from smart_token",
            "where local_access_token = #{localAccessToken,jdbcType=VARCHAR}",
            "limit 0,1"})
    SmartToken getByLocalAccessToken(@Param("localAccessToken") String localAccessToken);

    @Update({
            "update smart_token",
            "set access_token = #{accessToken,jdbcType=VARCHAR},",
            "refresh_token = #{refreshToken,jdbcType=VARCHAR},",
            "update_time = #{updateTime,jdbcType=TIMESTAMP}",
            "where id = #{id,jdbcType=BIGINT}"})
    int updateByPrimaryKey(SmartToken token);

    @Update({
            "update smart_token",
            "set local_access_token = #{localAccessToken,jdbcType=VARCHAR},",
            "local_refresh_token = #{localRefreshToken,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=BIGINT}"})
    int updateLocalTokenById(@Param("id") Long id, @Param("localAccessToken") String localAccessToken, @Param("localRefreshToken") String localRefreshToken);


    @Delete({
            "delete from smart_token",
            "where user_id = #{userId} AND smart = #{smart}"
    })
    public void deleteSmartTokenByUserIdAndSmart(@Param("userId") Long userId, @Param("smart")  int smart);

    @Delete({
            "delete from smart_token",
            "where user_id = #{userId} AND local_access_token = #{localAccessToken}"
    })
    public void deleteByUserIdAndLocalAccessToken(@Param("userId") Long userId, @Param("localAccessToken") String localAccessToken);
}
