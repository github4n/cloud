package com.iot.tenant.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.tenant.entity.LangInfoBase;
import com.iot.tenant.enums.LangTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.mapper.LangInfoBaseMapper;
import com.iot.tenant.service.ILangInfoBaseService;
import com.iot.tenant.vo.req.lang.*;
import com.iot.tenant.vo.resp.lang.LangInfoBaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：文案基础信息service
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 16:50
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 16:50
 * 修改描述：
 */
@Service
public class LangInfoBaseServiceImpl implements ILangInfoBaseService{

    @Autowired
    private LangInfoBaseMapper langInfoBaseMapper;

    /**
     * @despriction：保存文案基础信息(全删全插)
     * @author  yeshiyuan
     * @created 2018/9/29 16:49
     * @return
     */
    @Transactional
    @Override
    public void saveLangInfoBase(SaveLangInfoBaseReq req) {
        langInfoBaseMapper.deleteByObjectTypeAndObjectIdAndModule(req.getObjectType(), req.getObjectId(), req.getBelongModule());
        List<LangInfoBase> list = new ArrayList<>();
        req.getLangInfos().forEach(langInfoReq -> {
            for (Map.Entry<String,String> entry: langInfoReq.getVal().entrySet()) {
                if (!LangTypeEnum.checkDefaultLang(entry.getKey())){
                    throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "(key: "+ langInfoReq.getKey()+")");
                }
                LangInfoBase langInfoBase = new LangInfoBase(req.getTenantId(),req.getObjectType(),req.getObjectId()
                        ,entry.getKey(),langInfoReq.getKey(),entry.getValue(),req.getUserId(),new Date(), req.getBelongModule());
                list.add(langInfoBase);
            }
        });
        langInfoBaseMapper.batchInsert(list);
    }

    /**
     * @despriction：删除文案通过主键集合
     * @author  yeshiyuan
     * @created 2018/9/30 9:45
     * @return
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        return langInfoBaseMapper.deleteByIds(ids);
    }

    /**
     * @despriction：添加基础文案信息（重复key则报错）
     * @author  yeshiyuan
     * @created 2018/9/30 9:54
     * @return
     */
    @Transactional
    @Override
    public void addLangInfoBase(SaveLangInfoBaseReq req) {
        List<LangInfoBase> newList = new ArrayList<>();
        req.getLangInfos().forEach(langInfoReq -> {
            List<LangInfoBase> langInfoBases = langInfoBaseMapper.findByObjectIdAndObjectTypeAndLangKey(req.getObjectType(), req.getObjectId(), langInfoReq.getKey());
            if (langInfoBases.isEmpty()) {
                for (Map.Entry<String,String> entry: langInfoReq.getVal().entrySet()) {
                    if (!LangTypeEnum.checkDefaultLang(entry.getKey())){
                        throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "(key: "+ langInfoReq.getKey()+")");
                    }
                    LangInfoBase langInfoBase = new LangInfoBase(req.getTenantId(),req.getObjectType(),req.getObjectId()
                            ,entry.getKey(),langInfoReq.getKey(),entry.getValue(),req.getUserId(),new Date(), req.getBelongModule());
                    newList.add(langInfoBase);
                }
            } else {
                throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGKEY_EXISTS,  "(key: "+ langInfoReq.getKey()+")");
            }
        });
        langInfoBaseMapper.batchInsert(newList);
    }

    /**
     * @despriction：修改基础文案信息
     * @author  yeshiyuan
     * @created 2018/9/30 10:37
     * @return
     */
    @Override
    public void updateLangInfoBase(SaveLangInfoBaseReq req) {
        req.getLangInfos().forEach(langInfoReq -> {
            for (Map.Entry<String,String> entry: langInfoReq.getVal().entrySet()) {
                if (!LangTypeEnum.checkDefaultLang(entry.getKey())){
                    throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "(key: "+ langInfoReq.getKey()+")");
                }
                LangInfoBase langInfoBase = new LangInfoBase(req.getTenantId(),req.getObjectType(),req.getObjectId()
                        ,entry.getKey(),langInfoReq.getKey(),entry.getValue());
                langInfoBase.setUpdateBy(req.getUserId());
                langInfoBase.setUpdateTime(new Date());
                langInfoBaseMapper.updateLangInfoBase(langInfoBase);
            }
        });
    }

    /**
     * @despriction：描述
     * @author  yeshiyuan
     * @created 2018/9/30 13:38
     * @return
     */
    @Override
    public LangInfoBaseResp queryLangInfoBase(QueryLangInfoBaseReq req) {
        List<LangInfoBase> list = langInfoBaseMapper.findByObjectIdAndObjectTypeAndModule(req.getObjectType(), req.getObjectId(), req.getBelongModule());
        LangInfoBaseResp resp = new LangInfoBaseResp();
        resp.setObjectId(req.getObjectId());
        resp.setObjectType(req.getObjectType());
        resp.setBelongModule(req.getBelongModule());
        Map<String,LangInfoReq> map = toLangInfoReqMap(list);
        resp.setLangInfos(map.values().stream().collect(Collectors.toList()));
        return resp;
    }

    /**
      * @despriction：把文案基础信息转换为map格式（key为文案key，val为中英文对应的值）
      * @author  yeshiyuan
      * @created 2018/10/8 20:24
      * @return
      */
    public static Map<String,LangInfoReq> toLangInfoReqMap(List<LangInfoBase> list){
        Map<String,LangInfoReq> map = new HashMap<>();
        list.forEach(langInfoBase -> {
            String langKey = langInfoBase.getLangKey();
            if (map.containsKey(langKey)) {
                LangInfoReq langInfoReq = map.get(langKey);
                langInfoReq.getVal().put(langInfoBase.getLangType(), langInfoBase.getLangValue());
            }else {
                LangInfoReq langInfoReq = new LangInfoReq();
                langInfoReq.setKey(langKey);
                Map<String, String> val = new HashMap<>();
                val.put(langInfoBase.getLangType(), langInfoBase.getLangValue());
                langInfoReq.setVal(val);
                map.put(langKey, langInfoReq);
            }
        });
        return map;
    }

    /**
     * @despriction：删除基础文案
     * @author  yeshiyuan
     * @created 2018/10/10 15:35
     * @return
     */
    @Override
    public int deleteByObjectIdAndTypeAndKeys(DelLangInfoBaseReq delLangInfoBaseReq) {
        return langInfoBaseMapper.deleteByObjectIdAndTypeAndKeys(delLangInfoBaseReq);
    }

    /**
     * @despriction：分页查询对应的基础文案
     * @author  yeshiyuan
     * @created 2018/10/12 13:52
     * @return
     */
    @Override
    public Page<LangInfoBaseResp> pageQuery(QueryLangInfoBasePageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<LangInfoBase> page =new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<LangInfoBase> list = langInfoBaseMapper.queryLangValueGroupByLangKey(page, pageReq);
        List<LangInfoBase> langInfoList = new ArrayList<>();
        list.forEach(langInfoBase -> {
            String value = langInfoBase.getLangValue();
            String langValues[] = value.split("@@");
            for (String langValue : langValues) {
                int index = langValue.indexOf(":");
                LangInfoBase base = new LangInfoBase(langInfoBase.getLangKey(), langValue.substring(0,index), langValue.substring(index+1));
                langInfoList.add(base);
            }
        });
        LangInfoBaseResp resp = new LangInfoBaseResp(pageReq.getObjectId(), pageReq.getObjectType(), pageReq.getBelongModule());

        Map<String,LangInfoReq> map = toLangInfoReqMap(langInfoList);
        resp.setLangInfos(map.values().stream().collect(Collectors.toList()));

        com.iot.common.helper.Page<LangInfoBaseResp> myPage=new com.iot.common.helper.Page<>();
        myPage.setTotal(page.getTotal());
        myPage.setPages(page.getPages());
        myPage.setPageNum(page.getCurrent());
        myPage.setPageSize(page.getSize());
        myPage.setStartRow((page.getCurrent() - 1) * page.getSize());
        myPage.setEndRow(myPage.getStartRow()+page.getSize());
        List<LangInfoBaseResp> resps = new ArrayList<>();
        resps.add(resp);
        myPage.setResult(resps);
        return myPage;
    }
}
