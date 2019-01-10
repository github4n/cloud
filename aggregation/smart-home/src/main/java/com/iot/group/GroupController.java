package com.iot.group;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.BusinessExceptionEnum;
import com.iot.cloud.helper.SpaceEnum;
import com.iot.cloud.vo.EditSpaceVo;
import com.iot.cloud.vo.SingleSpaceVo;
import com.iot.cloud.vo.Space;
import com.iot.cloud.vo.SpaceVo;
import com.iot.common.Service.CommonStringUtil;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.api.GroupApi;
import com.iot.device.exception.GroupExceptionEnum;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.entity.FileBean;
import com.iot.file.vo.FileInfoResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.security.api.SecurityApi;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.space.exception.SpaceExceptionEnum;
import com.iot.shcs.space.vo.SpacePageResp;
import com.iot.shcs.space.vo.SpaceReq;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.space.vo.SpaceRespVo;
import com.iot.user.api.UserApi;
import com.iot.util.AssertUtils;
import com.iot.vo.BgImgVO;
import com.iot.vo.HomePageVo;
import com.iot.vo.RoomPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api("组管理接口")
@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupApi groupApi;

    @Autowired
    private SmarthomeSpaceApi spaceService;

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "组创建")
    @RequestMapping(value = "/addGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addGroup(@RequestBody UpdateGroupReq req) {
        AssertUtils.notNull(req, "group.notnull");
        log.debug("addGroup req is {}", JSON.toJSONString(req));
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long homeId = req.getHomeId();
        //检查用户家庭
        this.checkUserHome(tenantId, userId, homeId);
        //TODO 增加名称重复检验
        req.setTenantId(tenantId);
        req.setCreateBy(userId);
        Long groupId = groupApi.save(req);
        req.setGroupId(groupId);
        return new CommonResponse(ResultMsg.SUCCESS, req);
    }

    //检查当前用户家庭与传值是否一致
    private void checkUserHome(Long tenantId, Long userId, Long homeId){
        SpaceResp space = spaceService.findUserDefaultSpace(userId, tenantId);
        if(space == null){
            throw new BusinessException(SpaceExceptionEnum.USER_NOT_DEFAULT_SPACE);
        }
        if(space.getId().compareTo(homeId) != 0){
            throw new BusinessException(GroupExceptionEnum.USER_HOMEID_IS_ERROR);
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "组删除")
    @RequestMapping(value = "/delGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<ResultMsg> delGroup(@RequestBody UpdateGroupReq req) {
        AssertUtils.notNull(req.getGroupId(), "group.notnull");
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        req.setTenantId(tenantId);
        req.setCreateBy(userId);
        req.setId(req.getGroupId());
        boolean result = groupApi.delGroupById(req);
        if(result){
            return new CommonResponse(ResultMsg.SUCCESS);
        }else {
            return new CommonResponse(ResultMsg.FAIL);
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "组修改")
    @RequestMapping(value = "/editGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<ResultMsg> editGroup(@RequestBody UpdateGroupReq req) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long homeId = req.getHomeId();
        //检查用户家庭
        this.checkUserHome(tenantId, userId, homeId);
        req.setId(req.getGroupId());
        req.setTenantId(tenantId);
        req.setUpdateBy(userId);
        //TODO 增加名称重复检验
        boolean result = groupApi.update(req);
        return new CommonResponse(ResultMsg.SUCCESS);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询组列表")
    @RequestMapping(value = "/getGroupList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getGroupList(@RequestBody UpdateGroupReq req) {
        try {
            Long tenantId = SaaSContextHolder.currentTenantId();
            Long userId = SaaSContextHolder.getCurrentUserId();
            req.setTenantId(tenantId);
            req.setCreateBy(userId);
            return new CommonResponse(ResultMsg.SUCCESS, groupApi.getGroupByUser(req));
        } catch (Exception e) {
            log.error("getGroupList error.{}", e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION, e);
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "添加组成员")
    @RequestMapping(value = "/addGroupMember", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addGroupMember(@RequestBody UpdateGroupDetailReq req) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        req.setTenantId(tenantId);
        req.setCreateBy(userId);
        //TODO 是否需要进行用户设备检查？
        boolean result = groupApi.saveGroupDetialBatch(req);
        if(result){
            return new CommonResponse(ResultMsg.SUCCESS);
        }else {
            return new CommonResponse(ResultMsg.FAIL);
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "删除组成员")
    @RequestMapping(value = "/delGroupMember", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<ResultMsg> delGroupMember(@RequestBody UpdateGroupDetailReq req) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        req.setTenantId(tenantId);
        req.setCreateBy(userId);
        boolean result = groupApi.delGroupDetial(req);
        if(result){
            return new CommonResponse(ResultMsg.SUCCESS);
        }else {
            return new CommonResponse(ResultMsg.FAIL);
        }
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询组成员")
    @RequestMapping(value = "/getGroupMember", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getGroupMember(@RequestBody UpdateGroupDetailReq req) {
        try {
            Long tenantId = SaaSContextHolder.currentTenantId();
            Long userId = SaaSContextHolder.getCurrentUserId();
            req.setTenantId(tenantId);
            req.setCreateBy(userId);
            List<GetGroupDetailResp> resps = groupApi.getGroupDetailByGroupId(req);
            GetGroupDetailResp result = new GetGroupDetailResp();
            List<String> members = new ArrayList<>();
            if(!CollectionUtils.isEmpty(resps)){
                resps.forEach(resp->{
                    members.add(resp.getDeviceId());
                });
                result.setGroupId(req.getGroupId());
                result.setMembers(members);
            }

            return new CommonResponse(ResultMsg.SUCCESS, result);
        } catch (Exception e) {
            log.error("getGroupMember error.{}", e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION, e);
        }
    }

}
