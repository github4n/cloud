package com.iot.system.cache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.iot.common.util.CommonUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.system.vo.DictItemResp;

/**
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：字典表缓存
 * 创建人： maochengyuan
 * 创建时间：2018/6/28 20:17
 * 修改人： maochengyuan
 * 修改时间：2018/6/28 20:17
 * 修改描述：
 */
@Component
public class DictCacheApi {

    /**字典小类缓存Key*/
    public static final String DICT_SERVICE_ITEM = "dict:service-item:";

    /**
     * 描述：设置字典表明细，同时返回当前typeId的明细
     * @author maochengyuan
     * @created 2018/6/29 11:01
     * @param list 字典表明细
     * @return void
     */
    public List<DictItemResp> setDictItemCache(List<DictItemResp> list, Short typeId){
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        Map<Short, List<DictItemResp>> dictMap = list.stream().collect(Collectors.groupingBy(DictItemResp::getTypeId));
        dictMap.forEach((k, v) ->{
            String key = DICT_SERVICE_ITEM+k;
            RedisCacheUtil.delete(key);
            RedisCacheUtil.listSet(key, v);
        });
        return dictMap.get(typeId);
    }

    /** 
     * 描述：获取字典表明细
     * @author maochengyuan
     * @created 2018/6/29 11:01
     * @param typeId 字典表类别ID
     * @return java.util.List<com.iot.user.entity.DictItemResp>
     */
    public List<DictItemResp> getDictItemCache(Short typeId){
        if(typeId == null){
            return Collections.emptyList();
        }
        String key = DICT_SERVICE_ITEM+typeId;
        return RedisCacheUtil.listGetAll(key, DictItemResp.class);
    }

}
