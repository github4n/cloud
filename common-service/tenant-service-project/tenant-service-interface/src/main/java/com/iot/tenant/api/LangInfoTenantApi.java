package com.iot.tenant.api;

import com.iot.common.helper.Page;
import com.iot.tenant.api.fallback.LangInfoTenantFallbackFactory;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantPageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoTenantReq;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：租户文案管理api(portal使用)
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 14:15
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 14:15
 * 修改描述：
 */
@Api(tags = "租户文案管理api(portal使用)")
@FeignClient(value = "tenant-service", fallback = LangInfoTenantFallbackFactory.class)
@RequestMapping(value = "/api/langInfoTenant")
public interface LangInfoTenantApi {

    /**
      * @despriction：复制一份基础文案至对应的租户
      * @author  yeshiyuan
      * @created 2018/9/30 14:29
      * @return
      */
    @ApiOperation(value = "复制一份基础文案至对应的租户", notes = "复制一份基础文案至对应的租户")
    @RequestMapping(value = "/copyLangInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void copyLangInfo(@RequestBody CopyLangInfoReq copyLangInfoReq);

    /**
     * @despriction：复制某对象的文案至对应的对象
     * @author  yeshiyuan
     * @created 2018/10/26 9:15
     * @return
     */
    @ApiOperation(value = "复制某对象的文案至对应的对象", notes = "复制某对象的文案至对应的对象")
    @RequestMapping(value = "/copyLangInfoTenant", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int copyLangInfoTenant(@RequestBody CopyLangInfoReq copyLangInfoReq);

    /**
      * @despriction：查询租户下的某个对象文案
      * @author  yeshiyuan
      * @created 2018/9/30 15:01
      * @return
      */
    @ApiOperation(value = "查询租户下的某个对象文案", notes = "查询租户下的某个对象文案")
    @RequestMapping(value = "/queryLangInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    LangInfoTenantResp queryLangInfo(@RequestBody QueryLangInfoTenantReq queryLangInfoTenantReq);

    /**
      * @despriction：修改文案信息
      * @author  yeshiyuan
      * @created 2018/9/30 16:02
      * @return
      */
    @ApiOperation(value = "修改文案信息", notes = "修改文案信息")
    @RequestMapping(value = "/saveLangInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveLangInfo(@RequestBody SaveLangInfoTenantReq req);

    /**
     * @return
     * @despriction：添加文案信息
     * @author yeshiyuan
     * @created 2018/9/30 16:02
     */
    @ApiOperation(value = "添加文案信息", notes = "添加文案信息")
    @RequestMapping(value = "/addLangInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int addLangInfo(@RequestBody SaveLangInfoTenantReq req);

    /**
      * @despriction：删除文案信息
      * @author  yeshiyuan
      * @created 2018/10/8 11:50
      * @return
      */
    @ApiOperation(value = "删除文案信息", notes = "删除文案信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectType", value = "对象类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "objectId", value = "对象id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/deleteLangInfo", method = RequestMethod.POST)
    int deleteLangInfo(@RequestParam("objectType") String objectType, @RequestParam("objectId") String objectId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：分页查询文案
      * @author  yeshiyuan
      * @created 2018/10/12 15:44
      * @return
      */
    @ApiOperation(value = "分页查询文案", notes = "分页查询文案")
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<LangInfoTenantResp> pageQuery(@RequestBody QueryLangInfoTenantPageReq pageReq);

    /**
      * @despriction：加载app文案
      * @author  yeshiyuan
      * @created 2018/10/17 14:33
      * @return key为语言类型，value为文案
      */
    @ApiOperation(value = "加载app文案", notes = "加载app文案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getAppLangInfo", method = RequestMethod.GET)
    Map<String, Map<String, String>> getAppLangInfo(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：加载app添加的关联产品基本文案
      * @author  yeshiyuan
      * @created 2018/10/17 15:32
      * @return
      */
    @ApiOperation(value = "加载app添加的关联产品基本文案", notes = "加载app添加的关联产品基本文案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "productIds", value = "产品ids", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getLangByProductIds", method = RequestMethod.GET)
    Map<String, Map<String,String>> getLangByProductIds(@RequestParam("tenantId") Long tenantId, @RequestParam("productIds")List<Long> productIds);

    /**
     * @despriction：获取产品基本文案
     * @author  chq
     * @created 2019/1/4 15:32
     * @return
     */
    @ApiOperation(value = "获取产品基本文案", notes = "获取产品基本文案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "productIds", value = "产品ids", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getLangInfoByProductIds", method = RequestMethod.GET)
    List<LangInfoTenant> getLangInfoByProductIds(@RequestParam("tenantId") Long tenantId, @RequestParam("productIds") List<Long> productIds);
}
