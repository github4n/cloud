package com.iot.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.iot.system.vo.LangInfoResp;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：国际化
 * 创建人： maochengyuan
 * 创建时间：2018/7/12 9:57
 * 修改人： maochengyuan
 * 修改时间：2018/7/12 9:57
 * 修改描述：
 */
public interface LangMapper {

    /** 
     * 描述：获取有效的国际化信息
     * @author maochengyuan
     * @created 2018/7/12 10:07
     * @param 
     * @return java.util.List<com.iot.user.vo.LangInfoResp>
     */
    @Select({ "select lang_type as langType," +
            " lang_key as langKey," +
            " lang_value as langValue" +
            " from lang_info" +
            " where is_deleted = 'valid'" })
    List<LangInfoResp> getLangInfoList();

}