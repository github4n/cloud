package com.iot.system.service;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：国际化
 * 创建人： 李帅
 * 创建时间：2018年7月11日 下午4:25:31
 * 修改人：李帅
 * 修改时间：2018年7月11日 下午4:25:31
 */
public interface LangService {

    /** 
     * 
     * 描述：通过key获取对应语言信息
     * @author 李帅
     * @created 2018年7月11日 下午4:24:17
     * @since 
     * @param keys
     * @param lang
     * @return
     */
	Map<String, String> getLangValueByKey(Collection<String> keys, String lang);

}
