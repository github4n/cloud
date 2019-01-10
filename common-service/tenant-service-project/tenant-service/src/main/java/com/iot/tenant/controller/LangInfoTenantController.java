package com.iot.tenant.controller;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.ILangInfoTenantService;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantPageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoTenantReq;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：租户文案管理controller
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 14:20
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 14:20
 * 修改描述：
 */
@RestController
public class LangInfoTenantController implements LangInfoTenantApi{

    @Autowired
    private ILangInfoTenantService langInfoTenantService;

    /**
     * @despriction：复制一份基础文案至对应的租户
     * @author  yeshiyuan
     * @created 2018/9/30 14:29
     * @return
     */
    @Override
    public void copyLangInfo(@RequestBody CopyLangInfoReq copyLangInfoReq) {
        CopyLangInfoReq.checkParam(copyLangInfoReq);
        langInfoTenantService.copyLangInfo(copyLangInfoReq);
    }

    /**
     * @despriction：复制某对象的文案至对应的对象
     * @author  yeshiyuan
     * @created 2018/10/26 9:15
     * @return
     */
    @Override
    public int copyLangInfoTenant(@RequestBody CopyLangInfoReq copyLangInfoReq) {
        CopyLangInfoReq.checkParam(copyLangInfoReq);
        return langInfoTenantService.copyLangInfoTenant(copyLangInfoReq);
    }

    /**
     * @despriction：查询租户下的某个对象文案
     * @author  yeshiyuan
     * @created 2018/9/30 15:01
     * @return
     */
    @Override
    public LangInfoTenantResp queryLangInfo(@RequestBody QueryLangInfoTenantReq req) {
        if (!LangInfoObjectTypeEnum.checkObjectType(req.getObjectType())) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
        }
        if (req.getTenantId() == null) {
            throw new BusinessException(TenantExceptionEnum.TENANTID_IS_NULL);
        }
        if (req.getObjectId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "objectId is null");
        }
        return langInfoTenantService.queryLangInfo(req);
    }


    /**
     * @despriction：修改文案信息
     * @author  yeshiyuan
     * @created 2018/9/30 16:02
     * @return
     */
    @Override
    public void saveLangInfo(@RequestBody SaveLangInfoTenantReq req) {
        SaveLangInfoTenantReq.checkParam(req);
        langInfoTenantService.saveLangInfo(req);
    }

    @Override
    public int addLangInfo(@RequestBody SaveLangInfoTenantReq req) {
        SaveLangInfoTenantReq.checkParam(req);
        return langInfoTenantService.addLangInfo(req);
    }

    /**
     * @despriction：删除文案信息
     * @author  yeshiyuan
     * @created 2018/10/8 11:50
     * @return
     */
    @Override
    public int deleteLangInfo(@RequestParam("objectType") String objectType,
                               @RequestParam("objectId") String objectId, @RequestParam("tenantId") Long tenantId) {
        return langInfoTenantService.deleteByObjectTypeAndObjectIdAndTenantId(objectType, objectId, tenantId);
    }

    /**
     * @despriction：分页查询文案
     * @author  yeshiyuan
     * @created 2018/10/12 15:44
     * @return
     */
    @Override
    public Page<LangInfoTenantResp> pageQuery(@RequestBody QueryLangInfoTenantPageReq pageReq) {
        if (!LangInfoObjectTypeEnum.checkObjectType(pageReq.getObjectType())) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
        }
        return langInfoTenantService.pageQuery(pageReq);
    }

    /**
     * @despriction：加载app文案
     * @author  yeshiyuan
     * @created 2018/10/17 14:33
     * @return key为语言类型，value为文案
     */
    @Override
    public Map<String,  Map<String, String>> getAppLangInfo(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId) {
        return langInfoTenantService.getAppLangInfo(appId, tenantId);
    }

    /**
     * @despriction：加载app添加的关联产品基本文案
     * @author  yeshiyuan
     * @created 2018/10/17 15:32
     * @return
     */
    @Override
    public Map<String, Map<String, String>> getLangByProductIds(@RequestParam("tenantId") Long tenantId, @RequestParam("productIds")List<Long> productIds) {
        return langInfoTenantService.getProductLangInfo(tenantId, productIds);
    }

    @Override
    public List<LangInfoTenant> getLangInfoByProductIds(@RequestParam("tenantId") Long tenantId, @RequestParam("productIds") List<Long> productIds){
        return langInfoTenantService.getProductLangInfoTenants(tenantId, productIds);
    }
}
