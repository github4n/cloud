package com.iot.design.dict.service;

import com.iot.design.dict.dto.SystemDict;
import com.iot.design.dict.mapper.DictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/21
 */
@Service
public class DictService {
    @Autowired
    private DictMapper dictMapper;

    public List<SystemDict> getDict(Long tenantId, String dict) {
        return dictMapper.listDict(dict,tenantId);
    }
}
