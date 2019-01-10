package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iot.boss.service.file.FileService;
import com.iot.boss.vo.FileResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.StyleTemplateResp;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api(description = "样式模板",value = "样式模板")
@RequestMapping("/api/style/template")
public class StyleTemplateController {

    private Logger log = LoggerFactory.getLogger(StyleTemplateController.class);

    @Autowired
    private StyleTemplateApi styleTemplateApi;

    @Autowired
    private FileService fileService;

    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody StyleTemplateReq styleTemplateReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(styleTemplateReq));
        styleTemplateReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        styleTemplateReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        styleTemplateReq.setTenantId(SaaSContextHolder.currentTenantId());
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,styleTemplateApi.saveOrUpdate(styleTemplateReq));
        return result;
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("删除",ids);
        styleTemplateApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("删除",ids);
        styleTemplateApi.delete(ids);
        return CommonResponse.success();
    }


    @ApiOperation("获取全部")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list(@RequestBody StyleTemplateReq styleTemplateReq) {
        PageInfo<StyleTemplateResp> result = styleTemplateApi.list(styleTemplateReq);
        List<String> imgId = new ArrayList<>();
        result.getList().forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                imgId.add(m.getImg());
            }
            if (StringUtils.isNotEmpty(m.getResourceLink())){
                imgId.add(m.getResourceLink());
            }
        });
        Map map = fileService.getUrl(imgId);
        if (map!=null){
            result.getList().forEach(n->{
                if (map.get(n.getImg())!=null){
                    n.setImg(map.get(n.getImg()).toString());
                }
                if (map.get(n.getResourceLink())!=null){
                    n.setResourceLink(map.get(n.getResourceLink()).toString());
                }
            });
        }

        return CommonResponse.success(result);
    }


    @ApiOperation("根据deviceTypeId获取全部")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByDeviceTypeId/{deviceTypeId}", method = RequestMethod.GET)
    public CommonResponse listByDeviceTypeId(@PathVariable(value = "deviceTypeId" , required = true) Long deviceTypeId) {
        log.debug("根据deviceTypeId获取全部",deviceTypeId);
        List<StyleTemplateResp> list = styleTemplateApi.listByDeviceTypeId(deviceTypeId);
        List<String> imgId = new ArrayList<>();
        list.forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                imgId.add(m.getImg());
            }
            if (StringUtils.isNotEmpty(m.getResourceLink())){
                imgId.add(m.getResourceLink());
            }
        });
        Map map = fileService.getUrl(imgId);
        if (map!=null){
            list.forEach(n->{
                if (map.get(n.getImg())!=null){
                    n.setImg(map.get(n.getImg()).toString());
                }
                if (map.get(n.getResourceLink())!=null){
                    n.setResourceLink(map.get(n.getResourceLink()).toString());
                }
            });
        }

        return CommonResponse.success(list);
    }

    @ApiOperation("根据moduleStyleId获取全部")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByModuleStyleId/{moduleStyleId}", method = RequestMethod.GET)
    public CommonResponse listByModuleStyleId(@PathVariable(value = "moduleStyleId" , required = true) Long moduleStyleId) {
        log.debug("根据moduleStyleId获取全部",moduleStyleId);
        List<StyleTemplateResp> list = styleTemplateApi.listByModuleStyleId(moduleStyleId);
        List<String> imgId = new ArrayList<>();
        list.forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                imgId.add(m.getImg());
            }
            if (StringUtils.isNotEmpty(m.getResourceLink())){
                imgId.add(m.getResourceLink());
            }
        });
        Map map = fileService.getUrl(imgId);
        if (map!=null){
            list.forEach(n->{
                if (map.get(n.getImg())!=null){
                    n.setImg(map.get(n.getImg()).toString());
                }
                if (map.get(n.getResourceLink())!=null){
                    n.setResourceLink(map.get(n.getResourceLink()).toString());
                }
            });
        }

        return CommonResponse.success(list);
    }

    @ApiOperation("根据productId获取全部")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByProductId/{productId}", method = RequestMethod.GET)
    public CommonResponse listByProductId(@PathVariable(value = "productId" , required = true) Long productId) {
        log.debug("根据moduleStyleId获取全部",productId);
        List<StyleTemplateResp> list = styleTemplateApi.listByProductId(productId);
        List<String> imgId = new ArrayList<>();
        list.forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                imgId.add(m.getImg());
            }
            if (StringUtils.isNotEmpty(m.getResourceLink())){
                imgId.add(m.getResourceLink());
            }
        });
        Map map = fileService.getUrl(imgId);
        if (map!=null){
            list.forEach(n->{
                if (map.get(n.getImg())!=null){
                    n.setImg(map.get(n.getImg()).toString());
                }
                if (map.get(n.getResourceLink())!=null){
                    n.setResourceLink(map.get(n.getResourceLink()).toString());
                }
            });
        }

        return CommonResponse.success(list);
    }



}
