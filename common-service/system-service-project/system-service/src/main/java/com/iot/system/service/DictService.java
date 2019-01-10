package com.iot.system.service;

import com.iot.system.vo.DictItemResp;

import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：字典表接口
 * 创建人： maochengyuan
 * 创建时间：2018/6/29 11:17
 * 修改人： maochengyuan
 * 修改时间：2018/6/29 11:17
 * 修改描述：
 */
public interface DictService {

    /**
     * 描述：依据字典表类别获取字典表明细
     * @author maochengyuan
     * @created 2018/7/5 9:55
     * @param typeId
     * @return java.util.Map<java.lang.Short,com.iot.user.vo.DictItemResp>
     */
    Map<String, DictItemResp> getDictItem(Short typeId);

    /** 
     * 描述：依据字典表类别获取字典表名称
     * @author maochengyuan
     * @created 2018/7/5 9:53
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    Map<String, String> getDictItemNames(Short typeId);

    /** 
     * 描述：依据字典表类别获取字典表描述
     * @author maochengyuan
     * @created 2018/7/5 9:53
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    Map<String, String> getDictItemDescs(Short typeId);

}
