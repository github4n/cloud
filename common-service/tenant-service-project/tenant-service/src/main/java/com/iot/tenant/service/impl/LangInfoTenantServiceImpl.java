package com.iot.tenant.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.enums.LangTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.mapper.LangInfoTenantMapper;
import com.iot.tenant.service.ILangInfoTenantService;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.lang.LangInfoReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantPageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoTenantReq;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;
import com.iot.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：租户文案service
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 14:08
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 14:08
 * 修改描述：
 */
@Service
public class LangInfoTenantServiceImpl implements ILangInfoTenantService{

    private static Logger logger = LoggerFactory.getLogger(LangInfoTenantServiceImpl.class);

    @Autowired
    private LangInfoTenantMapper langInfoTenantMapper;

    /**
     * @despriction：复制一份基础文案至对应的租户
     * @author  yeshiyuan
     * @created 2018/9/30 14:29
     * @return
     */
    @Override
    public int copyLangInfo(CopyLangInfoReq copyLangInfoReq) {
        langInfoTenantMapper.deleteByObjectTypeAndObjectIdAndTenantId(copyLangInfoReq.getObjectType(),copyLangInfoReq.getObjectId().toString(),copyLangInfoReq.getTenantId());
        return langInfoTenantMapper.copyLangInfo(copyLangInfoReq, new Date());
    }

    @Override
    public LangInfoTenantResp queryLangInfo(QueryLangInfoTenantReq req) {
        LangInfoTenantResp resp = new LangInfoTenantResp();
        resp.setObjectId(req.getObjectId());
        resp.setObjectType(req.getObjectType());
        resp.setBelongModule(req.getBelongModule());
        List<LangInfoTenant> list = langInfoTenantMapper.queryLangInfos(req.getTenantId(), req.getObjectId(), req.getObjectType(), req.getBelongModule());
        Map<String,LangInfoReq> map = toLangInfoReqMap(list);
        resp.setLangInfos(map.values().stream().collect(Collectors.toList()));
        for (LangInfoReq langInfoReq : map.values()) {
            resp.setLangTypes(new ArrayList<>(langInfoReq.getVal().keySet()));
            break;
        }
        return resp;
    }

    /**
     * @despriction：保存租户文案（更新，有新语言类型则插入）
     * @author  yeshiyuan
     * @created 2018/9/30 16:52
     * @return
     */
    @Transactional
    @Override
    public int saveLangInfo(SaveLangInfoTenantReq req) {
        int delNum=0, addNum=0, updateNum = 0;
        if (req.getDelLangType() != null && !req.getDelLangType().isEmpty()) {
            delNum = langInfoTenantMapper.deleteByLangTypes(req.getObjectType(), req.getObjectId(), req.getTenantId(), req.getDelLangType());
        }
        if (req.getAddLangType() != null && !req.getAddLangType().isEmpty()) {
            req.getAddLangType().forEach(type -> {
                int existNum = langInfoTenantMapper.checkLangTypeIsExist(req.getObjectType(), req.getObjectId(), req.getTenantId(), type);
                if (existNum>0) {
                    throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "this lang type(" + type + ") existx");
                }
            });
            List<LangInfoTenant> existKeys = langInfoTenantMapper.queryLangKeys(req.getObjectType(), req.getObjectId(), req.getTenantId());
            List<LangInfoTenant> list = new ArrayList<>();
            existKeys.forEach(existKey -> {
                req.getAddLangType().forEach(addLangType -> {
                    LangInfoTenant langInfoTenant = new LangInfoTenant(req.getTenantId(),req.getObjectType(),req.getObjectId()
                            ,addLangType , existKey.getLangKey(),"",req.getUserId(),new Date(), existKey.getBelongModule());
                    list.add(langInfoTenant);
                });
            });
            if (!list.isEmpty()) {
                addNum = langInfoTenantMapper.batchInsert(list);
            }
        }
        req.getLangInfos().forEach(langInfoReq -> {
            for (Map.Entry<String,String> entry: langInfoReq.getVal().entrySet()) {
                if (!LangTypeEnum.checkLangType(entry.getKey())){
                    throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "(key: "+ langInfoReq.getKey()+")");
                }
                LangInfoTenant langInfoTenant = new LangInfoTenant(req.getTenantId(),req.getObjectType(),req.getObjectId()
                        ,entry.getKey(),langInfoReq.getKey(),entry.getValue(),req.getUserId(),new Date());
                langInfoTenantMapper.update(langInfoTenant);
            }
        });
        logger.debug("saveLangInfoTenant: delNum:{}, addNum:{}, updateNum:{}", delNum, addNum, updateNum);
        return updateNum;
    }

    @Transactional
    @Override
    public int addLangInfo(SaveLangInfoTenantReq req) {
        logger.debug("addLangInfo SaveLangInfoTenantReq={}", JSON.toJSONString(req));
        if (req == null || CollectionUtils.isEmpty(req.getLangInfos())) {
            return 0;
        }
        List<LangInfoTenant> langInfoTenants = Lists.newArrayList();
        req.getLangInfos().forEach(langInfoReq -> {
            for (Map.Entry<String, String> entry : langInfoReq.getVal().entrySet()) {
                if (!LangTypeEnum.checkLangType(entry.getKey())) {
                    throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "(key: " + langInfoReq.getKey() + ")");
                }
                LangInfoTenant langInfoTenant = new LangInfoTenant(req.getTenantId(), req.getObjectType(), req.getObjectId()
                        , entry.getKey(), langInfoReq.getKey(), entry.getValue(), req.getUserId(), new Date());
                langInfoTenants.add(langInfoTenant);
            }
        });
        return langInfoTenantMapper.batchInsert(langInfoTenants);
    }

    /**
     * @despriction：删除对应的文案
     * @author  yeshiyuan
     * @created 2018/10/8 11:16
     * @return
     */
    @Override
    public int deleteByObjectTypeAndObjectIdAndTenantId(String objectType, String objectId, Long tenantId) {
        return langInfoTenantMapper.deleteByObjectTypeAndObjectIdAndTenantId(objectType, objectId, tenantId);
    }

    /**
     * @despriction：分页查询文案
     * @author  yeshiyuan
     * @created 2018/10/12 15:44
     * @return
     */
    @Override
    public Page<LangInfoTenantResp> pageQuery(QueryLangInfoTenantPageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<LangInfoTenant> page =new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<LangInfoTenant> list = langInfoTenantMapper.queryLangValueGroupByLangKey(page, pageReq);
        List<LangInfoTenant> langInfoList = new ArrayList<>();
        List<String> langTypes = new ArrayList<>();  //用于给前端控制语言显示顺序
        boolean isFirst = true;
        for (LangInfoTenant langInfoBase:list) {
            String value = langInfoBase.getLangValue();
            String langValues[] = value.split("@@");
            for (String langValue : langValues) {
                int index = langValue.indexOf(":");
                LangInfoTenant base = new LangInfoTenant(langInfoBase.getLangKey(), langValue.substring(0,index), langValue.substring(index+1));
                langInfoList.add(base);
            }
            if (isFirst) {
                isFirst = false;
                for (String langValue : langValues) {
                    int index = langValue.indexOf(":");
                    langTypes.add(langValue.substring(0,index));
                }
            }
        }
        LangInfoTenantResp resp = new LangInfoTenantResp(pageReq.getObjectId(), pageReq.getObjectType(), pageReq.getBelongModule());
        resp.setLangTypes(langTypes);
        Map<String,LangInfoReq> map = toLangInfoReqMap(langInfoList);
        resp.setLangInfos(map.values().stream().collect(Collectors.toList()));

        com.iot.common.helper.Page<LangInfoTenantResp> myPage=new com.iot.common.helper.Page<>();
        myPage.setTotal(page.getTotal());
        myPage.setPages(page.getPages());
        myPage.setPageNum(page.getCurrent());
        myPage.setPageSize(page.getSize());
        myPage.setStartRow((page.getCurrent() - 1) * page.getSize());
        myPage.setEndRow(myPage.getStartRow()+page.getSize());
        List<LangInfoTenantResp> resps = new ArrayList<>();
        resps.add(resp);
        myPage.setResult(resps);
        return myPage;
    }

    /**
     * @despriction：加载app文案
     * @author  yeshiyuan
     * @created 2018/10/17 14:33
     * @return key为语言类型，value为文案
     */
    @Override
    public Map<String,Map<String, String>> getAppLangInfo(Long appId, Long tenantId) {
        List<LangInfoTenant> list = langInfoTenantMapper.queryLangInfos(tenantId, appId.toString(), LangInfoObjectTypeEnum.appConfig.toString(), null);
        return toLangKeyValueRespMap(list);
    }

    /**
     * @despriction：加载app添加的关联产品基本文案
     * @author  yeshiyuan
     * @created 2018/10/17 15:32
     * @return
     */
    @Override
    public Map<String, Map<String, String>> getProductLangInfo(Long tenantId, List<Long> productIds) {
        List<String> objectIds = productIds.stream().map(id ->{return id.toString();}).collect(Collectors.toList());
        List<LangInfoTenant> list = langInfoTenantMapper.findByObjectIds(tenantId, objectIds, LangInfoObjectTypeEnum.deviceType.toString());
        return toLangKeyValueRespMap(list);
    }

    @Override
    public List<LangInfoTenant> getProductLangInfoTenants(Long tenantId, List<Long> productIds){
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(productIds, "productIds.notnull");
        List<String> objectIds = productIds.stream().map(id ->{return id.toString();}).collect(Collectors.toList());
        return langInfoTenantMapper.findByObjectIds(tenantId, objectIds, LangInfoObjectTypeEnum.deviceType.toString());
    }

    /**
      * @despriction：转为LangInfoReq格式（key为文案key，value为LangInfoReq）
      * @author  yeshiyuan
      * @created 2018/10/17 14:38
      * @param 
      * @return
      */
    public static Map<String,LangInfoReq> toLangInfoReqMap(List<LangInfoTenant> langInfoTenants) {
        Map<String,LangInfoReq> map = new HashMap<>();
        langInfoTenants.forEach(langInfoBase -> {
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
      * @despriction：转为输出文案文件格式（key：语言类型，value为文案信息[key:value]）
      * @author  yeshiyuan
      * @created 2018/10/17 14:56
      * @return
      */
    public static Map<String, Map<String, String>> toLangKeyValueRespMap(List<LangInfoTenant> list) {
        Map<String, Map<String, String>> map = new HashMap<>();
        list.forEach(langInfo -> {
            Map<String, String> langInfoMap = map.get(langInfo.getLangType());
            if (langInfoMap == null) {
                langInfoMap = new HashMap<>();
                langInfoMap.put(langInfo.getLangKey(), langInfo.getLangValue());
                map.put(langInfo.getLangType(), langInfoMap);
            } else {
                langInfoMap.put(langInfo.getLangKey(), langInfo.getLangValue());
            }
        });
        return map;
    }

    /**
     * @despriction：拷贝文案
     * @author  yeshiyuan
     * @created 2018/10/26 9:15
     * @return
     */
    @Override
    public int copyLangInfoTenant(CopyLangInfoReq copyLangInfoReq) {
        List<LangInfoTenant> list = langInfoTenantMapper.queryLangInfos(copyLangInfoReq.getTenantId(),copyLangInfoReq.getCopyObjectId().toString(),copyLangInfoReq.getObjectType(),null);
        List<LangInfoTenant> addList = new ArrayList<>();
        list.forEach(lang -> {
            LangInfoTenant langInfoTenant = new LangInfoTenant();
            BeanUtil.copyProperties(lang, langInfoTenant);
            langInfoTenant.setTenantId(copyLangInfoReq.getTenantId());
            langInfoTenant.setCreateBy(copyLangInfoReq.getUserId());
            langInfoTenant.setObjectId(copyLangInfoReq.getObjectId().toString());
            langInfoTenant.setObjectType(copyLangInfoReq.getObjectType());
            langInfoTenant.setCreateTime(new Date());
            addList.add(langInfoTenant);
        });
        int i = 0;
        if (!addList.isEmpty()) {
            i = langInfoTenantMapper.batchInsert(addList);
        }
        return i;
    }
}
