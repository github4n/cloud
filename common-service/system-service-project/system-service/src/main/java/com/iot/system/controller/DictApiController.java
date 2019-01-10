package com.iot.system.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.util.CommonUtil;
import com.iot.system.api.DictApi;
import com.iot.system.service.DictService;
import com.iot.system.vo.DictItemResp;

/**
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：字典表服务
 * 创建人： maochengyuan
 * 创建时间：2018/6/29 11:35
 * 修改人： maochengyuan
 * 修改时间：2018/6/29 11:35
 * 修改描述：
 */
@RestController
public class DictApiController implements DictApi {

    @Autowired
    private DictService dictService;

    /**
     * 描述：依据字典表类别获取字典表明细
     * @author maochengyuan
     * @created 2018/6/29 11:34
     * @param typeId 字典表类别
     * @return java.util.List<com.iot.user.entity.DictItemResp>
     */
    @Override
    public Map<String, DictItemResp> getDictItem(@RequestParam("typeId")Short typeId){
        return this.dictService.getDictItem(typeId);
    }

    /**
     * 描述：依据字典表类别获取字典表名称
     * @author maochengyuan
     * @created 2018/7/5 9:53
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    public Map<String, String> getDictItemNames(@RequestParam("typeId") Short typeId){
        return this.dictService.getDictItemNames(typeId);
    }

    /**
     * 描述：依据字典表类别获取字典表描述
     * @author maochengyuan
     * @created 2018/7/5 9:53
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    public Map<String, String> getDictItemDescs(@RequestParam("typeId") Short typeId){
        return this.dictService.getDictItemDescs(typeId);
    }

    /**
     * 描述：依据字典表类别获取字典表名称（批量）
     * @author maochengyuan
     * @created 2018/7/6 11:22
     * @param typeIds 字典表类别
     * @return java.util.Map<java.lang.Short,java.util.Map<java.lang.Short,java.lang.String>>
     */
    @Override
    public Map<Short, Map<String, String>> getDictItemNamesBatch(@RequestParam("typeIds") Collection<Short> typeIds) {
        if(CommonUtil.isEmpty(typeIds)){
            return Collections.emptyMap();
        }
        Map<Short, Map<String, String>> map = new HashMap<>();
        typeIds.forEach(typeId ->{
            if(typeId != null){
                map.put(typeId, this.getDictItemNames(typeId));
            }
        });
        return map;
    }

}
