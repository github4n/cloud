package com.iot.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.iot.system.vo.DictItemResp;

/**
 * 项目名称：IOT云平台
 * 模块名称：权限模块
 * 功能描述：资源信息
 * 创建人： 490485964@qq.com
 * 创建时间：2018年06月28日 16:58
 * 修改人： 490485964@qq.com
 * 修改时间：2018年06月28日 16:58
 */
public interface DictMapper {

    /** 
     * 描述：获取字典表明细
     * @author mao2080@sina.com
     * @created 2018/6/29 11:27
     * @param  
     * @return java.util.List<com.iot.user.entity.DictItemResp>
     */
    @Select({ "select type_id as typeId," +
            " item_id as itemId," +
            " item_name as itemName," +
            " item_desc as itemDesc," +
            " item_sort as itemSort from sys_dict_item" +
            " order by type_id asc, item_sort asc" })
    List<DictItemResp> getDictItem();
}