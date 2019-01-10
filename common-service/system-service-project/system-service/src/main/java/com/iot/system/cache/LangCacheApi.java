package com.iot.system.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.system.vo.LangInfoResp;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：国际化缓存
 * 创建人： maochengyuan
 * 创建时间：2018/7/12 10:15
 * 修改人： maochengyuan
 * 修改时间：2018/7/12 10:15
 * 修改描述：
 */
@Component
public class LangCacheApi {

    /**字典小类缓存Key*/
    public static final String LANG_SERVICE_ITEM = "lang:service-item:";

    /**
     * 描述：设置国际化明细
     * @author maochengyuan
     * @created 2018/6/29 11:01
     * @param list 国际化明细
     * @return void
     */
    public void setLangInfoCache(List<LangInfoResp> list){
        if(CommonUtil.isEmpty(list)){
            return;
        }
        Map<String, Map<String, String>> langMap = new HashMap<>();
        list.forEach(o ->{
            String key = LANG_SERVICE_ITEM.concat(o.getLangType());
            if(langMap.get(key) == null){
                langMap.put(key, new HashMap<>());
            }
            langMap.get(key).put(o.getLangKey(), o.getLangValue());
        });
        RedisCacheUtil.hashPutBatch(langMap);
    }

    /**
     * 描述：获取国际化明细
     * @author maochengyuan
     * @created 2018/7/12 10:59
     * @param keys keys
     * @param lang 语言类型
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public Map<String, String> getLangInfoCache(Collection<String> keys, String lang){
        if(StringUtil.isEmpty(lang) || CommonUtil.isEmpty(keys)){
            return new HashMap<>();
        }
        String key = LANG_SERVICE_ITEM.concat(lang);
        return RedisCacheUtil.hashMultiGet(key, keys);
    }

}
