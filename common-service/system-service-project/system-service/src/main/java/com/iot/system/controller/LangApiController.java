package com.iot.system.controller;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.system.api.LangApi;
import com.iot.system.service.LangService;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：国际化
 * 创建人： 李帅
 * 创建时间：2018年7月11日 下午4:25:19
 * 修改人：李帅
 * 修改时间：2018年7月11日 下午4:25:19
 */
@RestController
public class LangApiController implements LangApi {

    @Autowired
    private LangService langService;

    /**
     * 
     * 描述：通过key获取对应语言信息
     * @author 李帅
     * @created 2018年7月11日 下午4:24:10
     * @since 
     * @param keys
     * @param lang
     * @return
     */
    @Override
    public Map<String, String> getLangValueByKey(@RequestParam("keys") Collection<String> keys, @RequestParam("lang") String lang ) {
    	return langService.getLangValueByKey(keys, lang);
    }

}
