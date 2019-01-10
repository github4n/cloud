package com.iot.design.dict.controller;

import com.iot.design.dict.service.DictService;
import com.iot.design.dict.vo.resp.DictResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/21
 */
@RestController
public class DictController implements DictApi {
    @Autowired
    private DictService dictService;

    @Override
    public List<DictResp> getDict(Long tenantId, String dict) {
        return dictService.getDict(tenantId, dict).stream().map(one -> new DictResp(one.getDictCode(), one.getDictName())).collect(Collectors.toList());
    }
}
