package com.iot.tenant.service;

import com.iot.common.helper.Page;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantPageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoTenantReq;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：租户文案service
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 14:07
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 14:07
 * 修改描述：
 */
public interface ILangInfoTenantService {

    /**
     * @despriction：复制一份基础文案至对应的租户
     * @author  yeshiyuan
     * @created 2018/9/30 14:29
     * @return
     */
    int copyLangInfo(CopyLangInfoReq copyLangInfoReq);

    /**
     * @despriction：查询租户下的某个对象文案
     * @author  yeshiyuan
     * @created 2018/9/30 15:01
     * @return
     */
     LangInfoTenantResp queryLangInfo(QueryLangInfoTenantReq req);

     /**
       * @despriction：保存租户文案
       * @author  yeshiyuan
       * @created 2018/9/30 16:52
       * @return
       */
     int saveLangInfo(SaveLangInfoTenantReq req);

    /**
     * @return
     * @despriction：纯添加租户文案
     * @author yeshiyuan
     * @created 2018/9/30 16:52
     */
    int addLangInfo(SaveLangInfoTenantReq req);

     /**
       * @despriction：删除对应的文案
       * @author  yeshiyuan
       * @created 2018/10/8 11:16
       * @return
       */
     int deleteByObjectTypeAndObjectIdAndTenantId(String objectType, String objectId, Long tenantId);

    /**
     * @despriction：分页查询文案
     * @author  yeshiyuan
     * @created 2018/10/12 15:44
     * @return
     */
    Page<LangInfoTenantResp> pageQuery(QueryLangInfoTenantPageReq pageReq);

    /**
     * @despriction：加载app文案
     * @author  yeshiyuan
     * @created 2018/10/17 14:33
     * @return key为语言类型，value为文案
     */
    Map<String,  Map<String, String>> getAppLangInfo(Long appId, Long tenantId);

    /**
     * @despriction：加载app添加的关联产品基本文案
     * @author  yeshiyuan
     * @created 2018/10/17 15:32
     * @return
     */
    Map<String, Map<String, String>> getProductLangInfo(Long tenantId, List<Long> productIds);

    /**
     * @despriction：查找产品基本文案
     * @author  chq
     * @created 2019/1/4 15:32
     * @return
     */
    List<LangInfoTenant> getProductLangInfoTenants(Long tenantId, List<Long> productIds);

    /**
      * @despriction：拷贝文案
      * @author  yeshiyuan
      * @created 2018/10/26 9:15
      * @return
      */
    int copyLangInfoTenant(CopyLangInfoReq copyLangInfoReq);
}
