package com.iot.boss.controller;

import com.iot.boss.vo.lang.BossSaveLangInfoBaseReq;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.LangInfoBaseApi;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.vo.req.lang.DelLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBasePageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoBaseReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 项目名称：cloud
 * 功能描述：文案基础数据管理
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 11:14
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 11:14
 * 修改描述：
 */
@Api(tags = "文案基础数据管理")
@RestController
@RequestMapping(value = "/api/langInfoBase")
public class LangInfoBaseController {

    @Autowired
    private LangInfoBaseApi langInfoBaseApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;

    /**
     * @despriction：保存基础文案信息(全删全插)
     * @author  yeshiyuan
     * @created 2018/9/29 16:44
     * @return
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "保存基础文案信息(全删全插)", notes = "保存基础文案信息(全删全插)")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse save(@RequestBody BossSaveLangInfoBaseReq req) {
        //校验操作对象是否存在
        if (LangInfoObjectTypeEnum.appConfig.name().equals(req.getObjectType())) {
            req.setObjectId(-1L);
        } else if (LangInfoObjectTypeEnum.checkIsDeviceType(req.getObjectType())) {
            DeviceTypeResp resp = deviceTypeApi.getDeviceTypeById(req.getObjectId());
            if (resp==null) {
                return ResultMsg.EXCEPTION.info("deviceType isn't exists");
            }
        } else {
            return ResultMsg.EXCEPTION.info("Object type error.");
        }
        SaveLangInfoBaseReq langInfoBaseReq = new SaveLangInfoBaseReq();
        BeanUtil.copyProperties(req,langInfoBaseReq);
        langInfoBaseReq.setUserId(SaaSContextHolder.getCurrentUserId());
        langInfoBaseApi.saveLangInfoBase(langInfoBaseReq);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * @despriction：删除基础文案信息
     * @author  yeshiyuan
     * @created 2018/9/30 9:41
     * @return
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "删除基础文案信息", notes = "删除基础文案信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResponse delete(@RequestBody DelLangInfoBaseReq req) {
        langInfoBaseApi.delLangInfoBase(req);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * @despriction：添加基础文案信息（有重复的key会报错）
     * @author  yeshiyuan
     * @created 2018/9/30 9:54
     * @return
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "添加基础文案信息（有重复的key会报错）", notes = "添加基础文案信息（有重复的key会报错）")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse add(@RequestBody BossSaveLangInfoBaseReq req) {
        SaveLangInfoBaseReq langInfoBaseReq = new SaveLangInfoBaseReq();
        BeanUtil.copyProperties(req,langInfoBaseReq);
        langInfoBaseReq.setUserId(SaaSContextHolder.getCurrentUserId());
        langInfoBaseApi.addLangInfoBase(langInfoBaseReq);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * @despriction：修改基础文案信息
     * @author  yeshiyuan
     * @created 2018/9/30 10:00
     * @return
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "修改基础文案信息", notes = "修改基础文案信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse update(@RequestBody BossSaveLangInfoBaseReq req) {
        SaveLangInfoBaseReq langInfoBaseReq = new SaveLangInfoBaseReq();
        BeanUtil.copyProperties(req,langInfoBaseReq);
        langInfoBaseReq.setUserId(SaaSContextHolder.getCurrentUserId());
        langInfoBaseApi.updateLangInfoBase(langInfoBaseReq);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * @despriction：查询对应的基础文案
     * @author  yeshiyuan
     * @created 2018/9/30 11:55
     * @return
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询对应的基础文案", notes = "查询对应的基础文案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectId", value = "对象id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "objectType", value = "文案类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "belongModule", value = "所属模块（app配网文案需要）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/queryLangInfoBase", method = RequestMethod.GET)
    public CommonResponse queryLangInfoBase(@RequestParam("objectId") Long objectId, @RequestParam("objectType") String objectType,
                                            @RequestParam(value = "belongModule", required = false) String belongModule) {
        QueryLangInfoBaseReq req = new QueryLangInfoBaseReq();
        req.setObjectId(objectId);
        req.setObjectType(objectType);
        req.setBelongModule(belongModule);
        return ResultMsg.SUCCESS.info(langInfoBaseApi.queryLangInfoBase(req));
    }

    /**
      * @despriction：分页查询对应的基础文案
      * @author  yeshiyuan
      * @created 2018/10/12 15:36
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "分页查询对应的基础文案", notes = "分页查询对应的基础文案")
    @RequestMapping(value = "/pageQuery",method = RequestMethod.POST)
    public CommonResponse pageQuery(@RequestBody QueryLangInfoBasePageReq pageReq) {
        return ResultMsg.SUCCESS.info(langInfoBaseApi.pageQuery(pageReq));
    }
}
