package com.iot.system.service.impl;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.system.cache.LangCacheApi;
import com.iot.system.dao.LangMapper;
import com.iot.system.service.LangService;
import com.iot.system.vo.LangInfoResp;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：国际化
 * 创建人： 李帅
 * 创建时间：2018年7月11日 下午4:25:39
 * 修改人：李帅
 * 修改时间：2018年7月11日 下午4:25:39
 */
@Service("langService")
public class LangServiceImpl implements LangService {

    @Autowired
    private LangMapper langMapper;

    @Autowired
    private LangCacheApi langCacheApi;

    /**
     * 
     * 描述：通过key获取对应语言信息
     * @author 李帅
     * @created 2018年7月11日 下午4:24:27
     * @since 
     * @param keys
     * @param lang
     * @return
     */
	@Override
    public Map<String, String> getLangValueByKey(Collection<String> keys, String lang) {
        if(CommonUtil.isEmpty(keys) || StringUtil.isEmpty(lang)){
            return Collections.emptyMap();
        }
        Map<String, String> map = this.langCacheApi.getLangInfoCache(keys, lang);
        if(map.isEmpty()){
            List<LangInfoResp> list = this.langMapper.getLangInfoList();
            this.langCacheApi.setLangInfoCache(list);
            if(!CommonUtil.isEmpty(list)){
                list.forEach(o ->{
                    if(lang.equals(o.getLangType()) && keys.contains(o.getLangKey())){
                        map.put(o.getLangKey(), o.getLangValue());
                    }
                });
            }
        }
        keys.forEach(o ->{
            String key = StringUtil.toString(o);
            if(!map.containsKey(key)){
                map.put(key, key);
            }
        });
        
        return map;
    }

	// 判断文件是否存在
		public boolean judeFileExists(File file) {
			if (file.exists()) {
				return true;
			} else {
				return false;
			}

		}
}
