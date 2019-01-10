package com.iot.space;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.cloud.helper.SpaceEnum;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.control.share.api.ShareSpaceApi;
import com.iot.control.share.vo.req.AddShareSpaceReq;
import com.iot.control.share.vo.resp.ShareSpaceResp;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.SpaceReq;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.api.MessageApi;
import com.iot.message.dto.MailBatchDto;
import com.iot.mqttploy.api.MqttPloyApi;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.SmartHomeDeviceCoreApi;
import com.iot.space.vo.AddShareUserReq;
import com.iot.space.vo.resp.ListHomeInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author lucky
 * @ClassName ShareSpaceController
 * @Description 分享家接口业务
 * @date 2019/1/2 14:12
 * @Version 1.0
 */
@Slf4j
@Api("分享家接口业务")
@Controller
@RequestMapping("/share/home")
public class ShareSpaceController {

    private static final long SHARE_EXPIRE_TIME = 60 * 60 * 24;

    private static final int MAX_CREATE_SHARE_SIZE = 5;

    private static final String SHARE_UUID_KEY = "share_space:uuid:%s";

    private static final String SHARE_HOME_USER_KEY = "%s:share_space:homeId:%s:userId:%s";

    private static final long SHARE_COMMIT_TIMEOUT = 60 * 60; //邮件发送限定一小时发送一次

    private static final String SHARE_COMMIT_TIMEOUT_KEY = "%s:share_commit_timeout_user:%s"; //邮件发送给指定人 key

    @Value("${service.domain.url}")
    private String serviceUrl;

    @Autowired
    private UserApi userApi;

    @Autowired
    private SpaceApi spaceApi;

    @Autowired
    private ShareSpaceApi shareSpaceApi;

    @Autowired
    private MessageApi messageApi;

    @Autowired
    private MqttPloyApi mqttPloyApi;

    @Autowired
    private SmartHomeDeviceCoreApi smartHomeDeviceCoreApi;

    @ResponseBody
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "创建分享信息")
    @RequestMapping(value = "/v1/create", method = RequestMethod.POST)
    public CommonResponse<ShareSpaceResp> createShare(@RequestBody AddShareUserReq params) {

        FetchUserResp currentUserInfo = userApi.getUser(SaaSContextHolder.getCurrentUserId());

        //1.检查用户是否存在
        FetchUserResp shareUserInfo = userApi.getUserByName(SaaSContextHolder.currentTenantId(), params.getToAccount());
        if (shareUserInfo == null) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_ACCOUNT_ERROR);
        }
        if (!shareUserInfo.getNickname().equals(params.getUserName())) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_ACCOUNT_ERROR);
        }
        if (shareUserInfo.getId().compareTo(currentUserInfo.getId()) == 0) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_NOT_INVITE_MYSELF_ERROR);
        }
        String commitTimeout = RedisCacheUtil.valueGet(String.format(SHARE_COMMIT_TIMEOUT_KEY, SaaSContextHolder.currentTenantId(), shareUserInfo.getId()));
        if (StringUtils.isEmpty(commitTimeout)) {
            RedisCacheUtil.valueSet(String.format(SHARE_COMMIT_TIMEOUT_KEY, SaaSContextHolder.currentTenantId(), shareUserInfo.getId()), String.valueOf(shareUserInfo.getId()), SHARE_COMMIT_TIMEOUT);
        } else {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_COMMIT_TIMEOUT_ERROR);
        }

        //2.检查是否已经有邀请了
        int countWhetherUpperMaxSize = shareSpaceApi.countByFromUserId(SaaSContextHolder.currentTenantId(), currentUserInfo.getId());
        if (MAX_CREATE_SHARE_SIZE <= countWhetherUpperMaxSize) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_UPPER_INVITE_MAX_SIZE_ERROR);
        }
        AddShareSpaceReq createShareSpace;
        String shareUUID = UUID.randomUUID().toString().replace("-", "");
        ShareSpaceResp shareSpaceResp = shareSpaceApi.getByToUserId(SaaSContextHolder.currentTenantId(), params.getHomeId(), shareUserInfo.getId());
        if (shareSpaceResp == null) {
            createShareSpace = AddShareSpaceReq.builder()
                    .tenantId(SaaSContextHolder.currentTenantId())
                    .fromUserId(currentUserInfo.getId())
                    .fromUserUuid(currentUserInfo.getUuid())
                    .toUserId(shareUserInfo.getId())
                    .toUserUuid(shareUserInfo.getUuid())
                    .spaceId(params.getHomeId())
                    .toUserEmail(shareUserInfo.getUserName())
                    .alias(StringUtils.isEmpty(shareUserInfo.getNickname()) ? shareUserInfo.getUserName() : shareUserInfo.getNickname())
                    .shareUuid(shareUUID)
                    .status(0)
                    .remark("inviting " + shareUserInfo.getNickname() + "now.")
                    .expireTime(SHARE_EXPIRE_TIME)
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
        } else {
            if (shareSpaceResp.getStatus() == ShareSpaceResp.INVITE_SUCCESS) {
                throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_NOT_REPEAT_INVITE_ERROR);
            }
            createShareSpace = AddShareSpaceReq.builder()
                    .id(shareSpaceResp.getId())
                    .tenantId(shareSpaceResp.getTenantId())
                    .shareUuid(shareUUID)
                    .alias(StringUtils.isEmpty(shareUserInfo.getNickname()) ? shareUserInfo.getUserName() : shareUserInfo.getNickname())
                    .status(0)
                    .expireTime(SHARE_EXPIRE_TIME)
                    .build();
        }
        ShareSpaceResp returnData = shareSpaceApi.saveOrUpdate(createShareSpace);


        //3.发送邮箱
        Map<String, String> noticeMap = Maps.newHashMap();
        noticeMap.put("subject", "share");
//        noticeMap.put("message", inviteHtmlContent(currentUserInfo.getNickname(), SaaSContextHolder.currentTenantId(), shareUserInfo.getId(), params.getHomeId(), shareUUID));
        noticeMap.put("templateId", "EN00013");
        noticeMap.put("userName", shareUserInfo.getUserName());
        noticeMap.put("href", serviceUrl + "/share/home/v1/html/confirm?tenantId=" + SaaSContextHolder.currentTenantId() + "&toUserId=" + shareUserInfo.getId() + "&shareUUID=" + shareUUID);
//        noticeMap.put("subject", "invite join " + currentUserInfo.getNickname() + "home");
        MailBatchDto mailBatchPush = new MailBatchDto();
        mailBatchPush.setNoticeMap(noticeMap);
        mailBatchPush.setRetryNum(1);
        mailBatchPush.setAppId(-1L);//LDS SaaSContextHolder.currentTenantId()
        mailBatchPush.setTos(Lists.newArrayList(params.getToAccount()));
        mailBatchPush.setLangage("en_US");

        messageApi.mailBatchPush(mailBatchPush);

        RedisCacheUtil.valueSet(String.format(SHARE_HOME_USER_KEY, SaaSContextHolder.currentTenantId(), params.getHomeId(), shareUserInfo.getId())
                , shareUUID, SHARE_EXPIRE_TIME);
        RedisCacheUtil.valueSet(String.format(SHARE_UUID_KEY, shareUUID), JSON.toJSONString(returnData), SHARE_EXPIRE_TIME);
        return CommonResponse.success(returnData);
    }

    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "受邀分享人明细【受邀分享人明细】")
    @RequestMapping(value = "/v1/info", method = RequestMethod.GET)
    public CommonResponse<ShareSpaceResp> info(@RequestParam("shareId") Long shareId) {
        ShareSpaceResp resultData = shareSpaceApi.getById(SaaSContextHolder.currentTenantId(), shareId);
        if (resultData == null) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_NOT_EXIST);
        }
        long expireTimes = DateUtil.addSecond(resultData.getCreateTime(), resultData.getExpireTime().intValue()).getTime();
        if (expireTimes - new Date().getTime() <= 0) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }
        return CommonResponse.success(resultData);
    }

    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "未接受邀请,删除邀请")
    @RequestMapping(value = "/v1/remove", method = RequestMethod.POST)
    public CommonResponse remove(@RequestParam("shareUUID") String shareUUID) {
        //1.检测失效、更新邀请状态
        ShareSpaceResp shareSpaceResp = shareSpaceApi.getByShareUUID(SaaSContextHolder.currentTenantId(), shareUUID);
        if (shareSpaceResp == null) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }
        if (shareSpaceResp.getStatus() == 1) {
            //前端操作移除受邀请业务 有误，非解绑操作方能调用
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_BUSINESS_ERROR);
        }
        shareSpaceApi.delById(SaaSContextHolder.currentTenantId(), shareSpaceResp.getId());
        return CommonResponse.success();
    }


    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "[单个]受邀人解绑邀请【app解绑邀请】")
    @RequestMapping(value = "/v1/unbind", method = RequestMethod.POST)
    public CommonResponse unbind(@RequestParam("shareUUID") String shareUUID) {
        //1.检测失效、更新邀请状态
        ShareSpaceResp shareSpaceResp = shareSpaceApi.getByShareUUID(SaaSContextHolder.currentTenantId(), shareUUID);
        if (shareSpaceResp == null) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }

        long expireTimes = DateUtil.addSecond(shareSpaceResp.getCreateTime(), shareSpaceResp.getExpireTime().intValue()).getTime();
        if (expireTimes - new Date().getTime() <= 0) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }

        //2.解绑用户和所有直连设备的策略
        List<String> directDeviceIds = getDirectDeviceIdList(shareSpaceResp.getTenantId(), shareSpaceResp.getFromUserId(), shareSpaceResp.getSpaceId());
        this.unbindAcls(shareSpaceResp.getToUserUuid(), directDeviceIds);

        RedisCacheUtil.delete(String.format(SHARE_COMMIT_TIMEOUT_KEY, shareSpaceResp.getTenantId(), shareSpaceResp.getToUserId()));
        RedisCacheUtil.delete(String.format(SHARE_UUID_KEY, shareSpaceResp.getShareUuid()));
        RedisCacheUtil.delete(String.format(SHARE_HOME_USER_KEY, shareSpaceResp.getTenantId(), shareSpaceResp.getSpaceId(), shareSpaceResp.getToUserId()));
        shareSpaceApi.delById(SaaSContextHolder.currentTenantId(), shareSpaceResp.getId());
        return CommonResponse.success();
    }

    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "[整个家]解绑家下所有的邀请【app解绑】")
    @RequestMapping(value = "/v1/unbindHome", method = RequestMethod.POST)
    public CommonResponse unbindHome(@RequestParam("homeId") Long homeId) {

        return CommonResponse.success();
    }


    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "受邀人接收邀请【app接收邀请】")
    @RequestMapping(value = "/v1/confirm", method = RequestMethod.POST)
    public CommonResponse<ShareSpaceResp> confirm(@RequestParam("shareUUID") String shareUUID) {
        //1.检测失效、更新邀请状态
        ShareSpaceResp shareSpaceResp = shareSpaceApi.getByShareUUID(SaaSContextHolder.currentTenantId(), shareUUID);
        if (shareSpaceResp == null) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }
        if (SaaSContextHolder.getCurrentUserId().compareTo(shareSpaceResp.getToUserId()) != 0) {//非当前用户的shareUUID不给于通过
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }

        long expireTimes = DateUtil.addSecond(shareSpaceResp.getCreateTime(), shareSpaceResp.getExpireTime().intValue()).getTime();
        if (expireTimes - new Date().getTime() <= 0) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }

        AddShareSpaceReq updateShareSpace = AddShareSpaceReq.builder()
                .tenantId(SaaSContextHolder.currentTenantId())
                .id(shareSpaceResp.getId())
                .status(1)//0邀请中 1邀请成功、2邀请失败
                .updateTime(new Date())
                .build();
        ShareSpaceResp resultData = shareSpaceApi.saveOrUpdate(updateShareSpace);

        //2.添加用户和所有直连设备的策略
        List<String> directDeviceIds = getDirectDeviceIdList(shareSpaceResp.getTenantId(), shareSpaceResp.getFromUserId(), shareSpaceResp.getSpaceId());
        this.addAcls(shareSpaceResp.getToUserUuid(), directDeviceIds);

        RedisCacheUtil.delete(String.format(SHARE_COMMIT_TIMEOUT_KEY, shareSpaceResp.getTenantId(), shareSpaceResp.getToUserId()));
        RedisCacheUtil.delete(String.format(SHARE_UUID_KEY, shareSpaceResp.getShareUuid()));
        RedisCacheUtil.delete(String.format(SHARE_HOME_USER_KEY, shareSpaceResp.getTenantId(), shareSpaceResp.getSpaceId(), shareSpaceResp.getToUserId()));
        return CommonResponse.success(resultData);
    }


    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "受邀人拒绝邀请【app拒绝邀请】")
    @RequestMapping(value = "/v1/refuse", method = RequestMethod.POST)
    public CommonResponse<ShareSpaceResp> refuse(@RequestParam("shareUUID") String shareUUID) {
        ShareSpaceResp shareSpaceResp = shareSpaceApi.getByShareUUID(SaaSContextHolder.currentTenantId(), shareUUID);
        if (shareSpaceResp == null) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }
        if (SaaSContextHolder.getCurrentUserId().compareTo(shareSpaceResp.getToUserId()) != 0) {//非当前用户的shareUUID不给于通过
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }

        long expireTimes = DateUtil.addSecond(shareSpaceResp.getCreateTime(), shareSpaceResp.getExpireTime().intValue()).getTime();
        if (expireTimes - new Date().getTime() <= 0) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }

        AddShareSpaceReq updateShareSpace = AddShareSpaceReq.builder()
                .tenantId(SaaSContextHolder.currentTenantId())
                .id(shareSpaceResp.getId())
                .status(2)//0邀请中 1邀请成功、2邀请失败
                .remark("refuse invited")
                .updateTime(new Date())
                .build();
        ShareSpaceResp resultData = shareSpaceApi.saveOrUpdate(updateShareSpace);
        RedisCacheUtil.delete(String.format(SHARE_UUID_KEY, shareSpaceResp.getShareUuid()));
        RedisCacheUtil.delete(String.format(SHARE_HOME_USER_KEY, shareSpaceResp.getTenantId(), shareSpaceResp.getSpaceId(), shareSpaceResp.getToUserId()));
        return CommonResponse.success(resultData);
    }

    @ResponseBody
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "用户接收邀请【邮箱中邀请确认】")
    @RequestMapping(value = "/v1/html/confirm", method = RequestMethod.GET)
    public ModelAndView htmlConfirm(@RequestParam("tenantId") Long tenantId, @RequestParam("toUserId") Long toUserId, @RequestParam("shareUUID") String shareUUID) {
        ModelAndView resultView = new ModelAndView("share/home/success");
        Map<String, Object> resultDataMap = Maps.newHashMap();
        resultDataMap.put("shareUUID", shareUUID);
        resultDataMap.put("msg", "confirm success");
        try {
            //1.检测失效、更新邀请状态
            ShareSpaceResp shareSpaceResp = getValidateExpireCacheShareUUID(tenantId, toUserId, shareUUID);
            if (shareSpaceResp.getStatus() == 0) {

                AddShareSpaceReq updateShareSpace = AddShareSpaceReq.builder()
                        .tenantId(tenantId)
                        .id(shareSpaceResp.getId())
                        .status(1)//0邀请中 1邀请成功、2邀请失败
                        .updateTime(new Date())
                        .build();
                shareSpaceApi.saveOrUpdate(updateShareSpace);

                //2.添加用户和所有直连设备的策略
                List<String> directDeviceIds = getDirectDeviceIdList(shareSpaceResp.getTenantId(), shareSpaceResp.getFromUserId(), shareSpaceResp.getSpaceId());
                this.addAcls(shareSpaceResp.getToUserUuid(), directDeviceIds);

                resultView.setViewName("share/home/success");

                RedisCacheUtil.delete(String.format(SHARE_COMMIT_TIMEOUT_KEY, shareSpaceResp.getTenantId(), shareSpaceResp.getToUserId()));
            } else if (shareSpaceResp.getStatus() == 1) {
                resultView.setViewName("share/home/success");
            } else {
                resultDataMap.put("msg", "confirm error. please contact administrator.");
                resultView.setViewName("share/home/error");
            }
            RedisCacheUtil.delete(String.format(SHARE_UUID_KEY, shareSpaceResp.getShareUuid()));
            RedisCacheUtil.delete(String.format(SHARE_HOME_USER_KEY, shareSpaceResp.getTenantId(), shareSpaceResp.getSpaceId(), shareSpaceResp.getToUserId()));
        } catch (BusinessException e) {
            resultDataMap.put("msg", e.getMessage());
            if (BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR.getCode() == e.getCode()) {
                //失效的 跳转到失效页面
                resultView.setViewName("share/home/expire");
            } else {
                resultView.setViewName("share/home/error");
            }
        } catch (Exception e) {
            resultDataMap.put("msg", "confirm error. please contact administrator.");
            resultView.setViewName("share/home/error");
        }

        resultView.addAllObjects(resultDataMap);
        return resultView;
    }

    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "分享家-用户列表")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public CommonResponse<List<ShareSpaceResp>> users(@RequestParam(value = "homeId", required = true) Long homeId) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        List<ShareSpaceResp> resultDataList = shareSpaceApi.listBySpaceId(tenantId, homeId);
        return CommonResponse.success(resultDataList);
    }

    @ResponseBody
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "分享家-【受邀分享人】家列表")
    @RequestMapping(value = "/homes", method = RequestMethod.GET)
    public CommonResponse<List<ListHomeInfoResp>> homes() {
        List<ListHomeInfoResp> resultDataList = Lists.newArrayList();
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        //包含当前人的家
        FetchUserResp currentUserInfo = userApi.getUser(userId);
        SpaceReq space = new SpaceReq();
        space.setDefaultSpace(1);
        space.setUserId(userId);
        space.setTenantId(tenantId);
        space.setType(SpaceEnum.HOME.getCode());
        com.iot.control.space.vo.SpaceResp spaceResp = null;
        List<com.iot.control.space.vo.SpaceResp> spaceList = spaceApi.findSpaceByCondition(space);
        if (!CollectionUtils.isEmpty(spaceList)) {
            spaceResp = spaceList.get(0);
        }
        if (spaceResp != null) {
            ListHomeInfoResp resultData = ListHomeInfoResp.builder()
                    .tenanId(tenantId)
                    .homeId(spaceResp.getId())
                    .homeName(spaceResp.getName())
                    .currentUserId(userId)
                    .currentUserUuid(currentUserInfo.getUuid())
                    .belongUserId(userId)
                    .belongUserUuid(currentUserInfo.getUuid())
                    .build();
            resultDataList.add(resultData);
        }
        List<ShareSpaceResp> shareSpaceRespList = shareSpaceApi.listByToUserId(tenantId, userId);//必须接受邀请后的列表
        if (!CollectionUtils.isEmpty(shareSpaceRespList)) {
            Map<Long, ShareSpaceResp> shareSpaceRespMap = Maps.newHashMap();
            shareSpaceRespList.forEach(shareSpaceResp -> {
                shareSpaceRespMap.put(shareSpaceResp.getSpaceId(), shareSpaceResp);
            });
            shareSpaceRespMap.keySet().forEach(spaceId -> {//后期优化 批量获取房间列表信息by ids
                com.iot.control.space.vo.SpaceResp targetSpaceInfo = spaceApi.findSpaceInfoBySpaceId(tenantId, spaceId);
                if (targetSpaceInfo != null) {
                    ShareSpaceResp targetShareSpace = shareSpaceRespMap.get(spaceId);
                    ListHomeInfoResp resultData = ListHomeInfoResp.builder()
                            .tenanId(tenantId)
                            .homeId(spaceId)
                            .homeName(targetSpaceInfo.getName())
                            .currentUserId(userId)
                            .currentUserUuid(currentUserInfo.getUuid())
                            .belongUserId(targetShareSpace.getFromUserId())
                            .belongUserUuid(targetShareSpace.getFromUserUuid())
                            .build();
                    resultDataList.add(resultData);
                }
            });
        }
        return CommonResponse.success(resultDataList);
    }


    private ShareSpaceResp getValidateExpireCacheShareUUID(Long tenantId, Long toUserId, String shareUUID) {
        String redisShareValue = RedisCacheUtil.valueGet(String.format(SHARE_UUID_KEY, shareUUID));
        if (StringUtils.isEmpty(redisShareValue)) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }
        ShareSpaceResp resultData = JSON.parseObject(redisShareValue, ShareSpaceResp.class);
        long expireTimes = DateUtil.addSecond(resultData.getCreateTime(), resultData.getExpireTime().intValue()).getTime();
        if (expireTimes - new Date().getTime() <= 0) {
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }
        Long homeId = resultData.getSpaceId();
        String targetUserShareUUID = RedisCacheUtil.valueGet(String.format(SHARE_HOME_USER_KEY, tenantId, homeId, toUserId));
        if (!shareUUID.equals(targetUserShareUUID)) {//请求的uuid 跟 原受邀分享用户的shareUUID 不一致【防止被爆破获取】
            throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_EXPIRE_INVITE_ERROR);
        }
        return resultData;
    }

    private List<String> getDirectDeviceIdList(Long tenantId, Long userId, Long homeId) {
        List<String> directDeviceIds = Lists.newArrayList();
        Map<String, Object> resultDataMap = smartHomeDeviceCoreApi.getDevList(tenantId, userId, homeId);
        if (resultDataMap.containsKey("dev")) {
            List<Map<String, Object>> deviceMapList = (List<Map<String, Object>>) resultDataMap.get("dev");
            if (CollectionUtils.isEmpty(deviceMapList)) {
                return directDeviceIds;
            }
            deviceMapList.forEach(deviceInfoMap -> {
                if (deviceInfoMap.containsKey("parentId")) {
                    if (StringUtils.isEmpty(deviceInfoMap.get("parentId"))) {
                        directDeviceIds.add(String.valueOf(deviceInfoMap.get("devId")));
                    }
                }
            });
        }
        return directDeviceIds;
    }

    /**
     * 为受邀分享人 添加所有直连设备的策略
     *
     * @param toUserUUID
     * @param directDeviceIds
     */
    private void addAcls(String toUserUUID, List<String> directDeviceIds) {


    }

    /**
     * 为受邀分享人 解除所有直连设备的策略
     *
     * @param toUserUUID
     * @param directDeviceIds
     */
    private void unbindAcls(String toUserUUID, List<String> directDeviceIds) {

    }
}
