package com.iot.system.service.impl;

import com.iot.common.util.CommonUtil;
import com.iot.system.cache.DictCacheApi;
import com.iot.system.dao.DictMapper;
import com.iot.system.service.DictService;
import com.iot.system.vo.DictItemResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：字典表接口实现
 * 创建人： maochengyuan
 * 创建时间：2018/6/29 11:17
 * 修改人： maochengyuan
 * 修改时间：2018/6/29 11:17
 * 修改描述：
 */
@Service("dictService")
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Resource
    private DictCacheApi dictCacheApi;

    /** 
     * 描述：获取字典表明细
     * @author maochengyuan
     * @created 2018/6/29 11:34
     * @param typeId 字典表类别
     * @return java.util.List<com.iot.user.entity.DictItemResp>
     */
    @Override
    public Map<String, DictItemResp> getDictItem(Short typeId) {
        List<DictItemResp> list = this.dictCacheApi.getDictItemCache(typeId);
        if(CommonUtil.isEmpty(list)){
            list = this.dictMapper.getDictItem();
            list = this.dictCacheApi.setDictItemCache(list, typeId);
        }
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(DictItemResp::getItemId, DictItemResp -> DictItemResp));
    }

    /**
     * 描述：获取字典表名称
     * @author maochengyuan
     * @created 2018/6/29 11:34
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    @Override
    public Map<String, String> getDictItemNames(Short typeId) {
        List<DictItemResp> list = this.dictCacheApi.getDictItemCache(typeId);
        if(CommonUtil.isEmpty(list)){
            list = this.dictMapper.getDictItem();
            list = this.dictCacheApi.setDictItemCache(list, typeId);
        }
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(DictItemResp::getItemId, DictItemResp::getItemName));
    }

    /**
     * 描述：获取字典表描述
     * @author maochengyuan
     * @created 2018/6/29 11:34
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    @Override
    public Map<String, String> getDictItemDescs(Short typeId) {
        List<DictItemResp> list = this.dictCacheApi.getDictItemCache(typeId);
        if(CommonUtil.isEmpty(list)){
            list = this.dictMapper.getDictItem();
            list = this.dictCacheApi.setDictItemCache(list, typeId);
        }
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(DictItemResp::getItemId, DictItemResp::getItemDesc));
    }

}
