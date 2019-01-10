package com.iot.system.api.fallback;

import com.iot.system.api.DictApi;
import com.iot.system.vo.DictItemResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：字典表Fallback
 * 创建人： maochengyuan
 * 创建时间：2018/6/29 14:03
 * 修改人： maochengyuan
 * 修改时间：2018/6/29 14:03
 * 修改描述：
 */
@Component
public class DictApiFallbackFactory implements FallbackFactory<DictApi> {

    @Override
    public DictApi create(Throwable cause) {

        return new DictApi() {
            @Override
            public Map<String, DictItemResp> getDictItem(Short typeId) {
                return null;
            }

            @Override
            public Map<String, String> getDictItemNames(Short typeId) {
                return null;
            }

            @Override
            public Map<String, String> getDictItemDescs(Short typeId) {
                return null;
            }

            @Override
            public Map<Short, Map<String, String>> getDictItemNamesBatch(Collection<Short> typeIds) {
                return null;
            }
        };

    }
}
