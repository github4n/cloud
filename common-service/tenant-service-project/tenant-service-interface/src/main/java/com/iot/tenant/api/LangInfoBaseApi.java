package com.iot.tenant.api;

import com.iot.common.helper.Page;
import com.iot.tenant.api.fallback.LangInfoBaseApiFallbackFactory;
import com.iot.tenant.vo.req.lang.DelLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBasePageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoBaseReq;
import com.iot.tenant.vo.resp.lang.LangInfoBaseResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：文案信息模板接口（BOSS使用）
 * 功能描述：文案信息模板接口（BOSS使用）
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:31:47
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:31:47
 */
@Api(tags = "文案信息模板接口（BOSS使用）")
@FeignClient(value = "tenant-service", fallback = LangInfoBaseApiFallbackFactory.class)
@RequestMapping("/api/langInfoBase")
public interface LangInfoBaseApi {

    /**
      * @despriction：保存基础文案信息(全删全插)
      * @author  yeshiyuan
      * @created 2018/9/29 16:44
      * @return
      */
    @ApiOperation(value = "保存基础文案信息(全删全插)", notes = "保存基础文案信息(全删全插)")
    @RequestMapping(value = "/saveLangInfoBase", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveLangInfoBase(@RequestBody SaveLangInfoBaseReq req);

    /**
      * @despriction：删除基础文案信息
      * @author  yeshiyuan
      * @created 2018/9/30 9:41
      * @return
      */
    @ApiOperation(value = "删除基础文案信息", notes = "删除基础文案信息")
    @RequestMapping(value = "/delLangInfoBase", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delLangInfoBase(@RequestBody DelLangInfoBaseReq req);

    /**
      * @despriction：添加基础文案信息（有重复的key会报错）
      * @author  yeshiyuan
      * @created 2018/9/30 9:54
      * @return
      */
    @ApiOperation(value = "添加基础文案信息（有重复的key会报错）", notes = "添加基础文案信息（有重复的key会报错）")
    @RequestMapping(value = "/addLangInfoBase", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addLangInfoBase(@RequestBody SaveLangInfoBaseReq req);

    /**
      * @despriction：修改基础文案信息
      * @author  yeshiyuan
      * @created 2018/9/30 10:00
      * @return
      */
    @ApiOperation(value = "修改基础文案信息", notes = "修改基础文案信息")
    @RequestMapping(value = "/updateLangInfoBase", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateLangInfoBase(@RequestBody SaveLangInfoBaseReq req);

    /**
      * @despriction：查询对应的基础文案
      * @author  yeshiyuan
      * @created 2018/9/30 11:55
      * @return
      */
    @ApiOperation(value = "查询对应的基础文案", notes = "查询对应的基础文案")
    @RequestMapping(value = "/queryLangInfoBase", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    LangInfoBaseResp queryLangInfoBase(@RequestBody QueryLangInfoBaseReq queryLangInfoBaseReq);

    /**
      * @despriction：分页查询对应的基础文案
      * @author  yeshiyuan
      * @created 2018/10/12 13:52
      * @return
      */
    @ApiOperation(value = "分页查询对应的基础文案", notes = "分页查询对应的基础文案")
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<LangInfoBaseResp> pageQuery(@RequestBody QueryLangInfoBasePageReq pageReq);
}
