package com.iot.system.api.fallback;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.iot.system.api.LangApi;

import feign.hystrix.FallbackFactory;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：国际化Fallback
 * 创建人： 李帅
 * 创建时间：2018年7月11日 下午4:24:58
 * 修改人：李帅
 * 修改时间：2018年7月11日 下午4:24:58
 */
@Component
public class LangApiFallbackFactory implements FallbackFactory<LangApi> {

    @Override
    public LangApi create(Throwable cause) {

        return new LangApi() {
            @Override
            public Map<String, String> getLangValueByKey(Collection<String> keys, String lang ) {
                return null;
            }
        };

    }
}
