package com.iot.space;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.BusinessExceptionEnum;
import com.iot.cloud.helper.SpaceEnum;
import com.iot.cloud.vo.EditSpaceVo;
import com.iot.cloud.vo.SingleSpaceVo;
import com.iot.cloud.vo.Space;
import com.iot.cloud.vo.SpaceVo;
import com.iot.common.Service.CommonStringUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.share.api.ShareSpaceApi;
import com.iot.control.share.vo.resp.ShareSpaceResp;
import com.iot.control.space.api.SpaceApi;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.entity.FileBean;
import com.iot.file.vo.FileInfoResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.security.api.SecurityApi;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.space.vo.SpacePageResp;
import com.iot.shcs.space.vo.SpaceReq;
import com.iot.shcs.space.vo.SpaceRespVo;
import com.iot.user.api.UserApi;
import com.iot.vo.BgImgVO;
import com.iot.vo.HomePageVo;
import com.iot.vo.RoomPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Api("空间接口")
@RestController
@RequestMapping("/home")
public class SpaceController {

    @Autowired
    private SpaceApi spaceApi;

    @Autowired
    private SmarthomeSpaceApi spaceService;

    @Autowired
    private UserApi userApiService;

    @Autowired
    private SecurityApi securityApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private FileUploadApi fileUploadApi;


    @Autowired
    private ShareSpaceApi shareSpaceApi;

    //接受的图片类型
    @Value("${upload.img.str}")
    private String uploadImgStr;

    @ApiOperation(value = "家庭创建", response = Space.class)
    @RequestMapping(value = "/addHome", method = RequestMethod.POST)
    public CommonResponse<SpaceRespVo> addHome(@RequestBody SpaceVo spaceVo) {
        //判断房间名称长度
        String language = LocaleContextHolder.getLocale().toString();
        String name=spaceVo.getName();
        if ("zh_CN".equals(language)) {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }else {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }
        CommonResponse<SpaceRespVo> result = addSpace(spaceVo, SpaceEnum.HOME.getCode());
        return result;
    }

    @ApiOperation(value = "家庭删除", response = Void.class)
    @RequestMapping(value = "/delHome", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> delHome(@RequestBody SingleSpaceVo deleteVo) {
        CommonResponse<ResultMsg> result = delSpace(deleteVo, SpaceEnum.HOME.getCode());
        return result;
    }

    @ApiOperation(value = "家庭修改", response = Void.class)
    @RequestMapping(value = "/editHome", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> editHome(@RequestBody EditSpaceVo editVo) {
        //判断房间名称长度
        String language = LocaleContextHolder.getLocale().toString();
        String name=editVo.getName();
        if ("zh_CN".equals(language)) {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }else {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }
        editVo.setId(editVo.getHomeId());
        CommonResponse<ResultMsg> result = editSpace(editVo, SpaceEnum.HOME.getCode());
        return result;
    }

    @ApiOperation(value = "房间创建", response = Space.class)
    @RequestMapping(value = "/addRoom", method = RequestMethod.POST)
    public CommonResponse<SpaceRespVo> addRoom(@RequestBody SpaceVo spaceVo) {
        //判断房间名称长度
        String language = LocaleContextHolder.getLocale().toString();
        String name=spaceVo.getName();
        if ("zh_CN".equals(language)) {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }else {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }
        spaceVo.setParentId(spaceVo.getHomeId());
        CommonResponse<SpaceRespVo> result = addSpace(spaceVo, SpaceEnum.ROOM.getCode());
        return result;
    }

    @ApiOperation(value = "房间删除", response = Void.class)
    @RequestMapping(value = "/delRoom", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> delRoom(@RequestBody SingleSpaceVo deleteVo) {
        CommonResponse<ResultMsg> result = delSpace(deleteVo, SpaceEnum.ROOM.getCode());
        return result;
    }

    @ApiOperation(value = "房间修改", response = Void.class)
    @RequestMapping(value = "/editRoom", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> editRoom(@RequestBody EditSpaceVo editVo) {
        //判断房间名称长度
        String language = LocaleContextHolder.getLocale().toString();
        String name=editVo.getName();
        if ("zh_CN".equals(language)) {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }else {
            CommonStringUtil.checkStringParam(name,0,20,language);
        }
        editVo.setId(editVo.getRoomId());
        editVo.setParentId(editVo.getHomeId());
        CommonResponse<ResultMsg> result = editSpace(editVo, SpaceEnum.ROOM.getCode());
        return result;
    }

    @ApiOperation(value = "用户家庭获取", response = Space.class)
    @RequestMapping(value = "/getHomeList", method = RequestMethod.POST)
    public CommonResponse<HomePageVo> getHome(@RequestBody SpaceVo space) {

        //1.获取当前用户的房间列表
        space.setUserId(SaaSContextHolder.getCurrentUserId());
        SpaceReq req = spaceVoToSpaceReq(space);
        req.setType(SpaceEnum.HOME.getCode());
        req.setDefaultSpace(1);
        req.setTenantId(SaaSContextHolder.currentTenantId());
        Page<SpacePageResp> pageResult = spaceService.getHomePage(req);

        List<SpacePageResp> resultDataList = Lists.newArrayList();
        pageResult.getResult().forEach(currentUserSpace -> {
            currentUserSpace.setBelongCurrentUser(true);
            currentUserSpace.setBelongUserId(SaaSContextHolder.getCurrentUserId());
            currentUserSpace.setBelongUserUuid(SaaSContextHolder.getCurrentUserUuid());
            currentUserSpace.setCurrentUserId(SaaSContextHolder.getCurrentUserId());
            currentUserSpace.setCurrentUserUuid(SaaSContextHolder.getCurrentUserUuid());
            resultDataList.add(currentUserSpace);
        });
        //2.获取对应当前用户受邀分享家列表[必须接受邀请后的列表]
        List<ShareSpaceResp> shareSpaceRespList = shareSpaceApi.listByToUserId(SaaSContextHolder.currentTenantId(), SaaSContextHolder.getCurrentUserId());
        if (!CollectionUtils.isEmpty(shareSpaceRespList)) {
            Map<Long, ShareSpaceResp> shareSpaceRespMap = Maps.newHashMap();
            shareSpaceRespList.forEach(shareSpaceResp -> {
                if (shareSpaceResp.getStatus() == 1) {
                    shareSpaceRespMap.put(shareSpaceResp.getSpaceId(), shareSpaceResp);
                }
            });
            List<Long> spaceIds = Lists.newArrayList(shareSpaceRespMap.keySet());
            Page<SpacePageResp> shareResultDataPage = spaceService.getHomePage(SpaceReq.builder()
                    .tenantId(SaaSContextHolder.currentTenantId())
                    .type(SpaceEnum.HOME.getCode())
                    .defaultSpace(1)
                    .spaceIds(spaceIds)
                    .build());
            shareResultDataPage.getResult().forEach(shareUserSpace -> {
                ShareSpaceResp targetShareSpace = shareSpaceRespMap.get(Long.parseLong(shareUserSpace.getHomeId()));
                if (targetShareSpace != null) {
                    shareUserSpace.setBelongCurrentUser(false);
                    shareUserSpace.setBelongUserId(targetShareSpace.getFromUserId());
                    shareUserSpace.setBelongUserUuid(targetShareSpace.getFromUserUuid());
                    shareUserSpace.setCurrentUserId(SaaSContextHolder.getCurrentUserId());
                    shareUserSpace.setCurrentUserUuid(SaaSContextHolder.getCurrentUserUuid());
                    resultDataList.add(shareUserSpace);
                }
            });
        }
        HomePageVo homePageVo = new HomePageVo();
        homePageVo.setHomes(resultDataList);
        homePageVo.setTotalCount(pageResult.getTotal());

        return CommonResponse.success(homePageVo);

    }

    @ApiOperation(value = "用户家庭所属房间列表", response = Space.class)
    @RequestMapping(value = "/getRoomList", method = RequestMethod.POST)
    public CommonResponse<RoomPageVo> getRoom(@RequestBody SpaceVo space) {
        try {
            space.setUserId(SaaSContextHolder.getCurrentUserId());
            SpaceReq req = spaceVoToSpaceReq(space);
            req.setTenantId(SaaSContextHolder.currentTenantId());
            Page<SpacePageResp> pageResult = spaceService.getRoomPage(req);
            RoomPageVo roomPageVo = new RoomPageVo();
            roomPageVo.setRooms(pageResult.getResult());
            roomPageVo.setTotalCount(pageResult.getTotal());
            return new CommonResponse<>(ResultMsg.SUCCESS, roomPageVo);
        } catch (Exception e) {
            log.error("get roomlist error.{}", e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION, e);
        }
    }

    public CommonResponse<SpaceRespVo> addSpace(SpaceVo spaceVo, String type) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long orgId = SaaSContextHolder.getCurrentOrgId();
        spaceVo.setType(type);
        spaceVo.setUserId(userId);
        spaceVo.setTenantId(tenantId);
        spaceVo.setOrgId(orgId);
        SpaceReq req = new SpaceReq();

        BeanUtils.copyProperties(spaceVo, req);
        SpaceRespVo spaceRespVo = spaceService.addSpace(req);

        return new CommonResponse<SpaceRespVo>(ResultMsg.SUCCESS, spaceRespVo);
    }

    public CommonResponse<ResultMsg> delSpace(SingleSpaceVo deleteVo, String type) {
        Long spaceId;
        if (SpaceEnum.HOME.getCode().equals(type)) {
            spaceId = deleteVo.getHomeId();
        } else if (SpaceEnum.ROOM.getCode().equals(type)) {
            spaceId = deleteVo.getRoomId();
        } else {
            spaceId = deleteVo.getSpaceId();
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        spaceService.deleteSpaceBySpaceIdAndUserId(spaceId, userId, tenantId);
        return new CommonResponse<>(ResultMsg.SUCCESS);

    }

    public CommonResponse<ResultMsg> editSpace(EditSpaceVo editVo, String type) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long orgId = SaaSContextHolder.getCurrentOrgId();

        SpaceReq space = new SpaceReq();

        BeanUtils.copyProperties(editVo, space);

        space.setUserId(userId);
        space.setTenantId(tenantId);
        space.setOrgId(orgId);
        space.setUpdateBy(userId);
        space.setUpdateTime(new Date());
        space.setType(type);
        spaceService.editSpace(space);
        return new CommonResponse().success();

    }


    private SpaceReq spaceVoToSpaceReq(SpaceVo vo) {
        SpaceReq req = new SpaceReq();
        req.setUserId(vo.getUserId() == null ? SaaSContextHolder.getCurrentUserId() : vo.getUserId());
//        req.setUserId(SaaSContextHolder.getCurrentUserId());
        req.setIcon(vo.getIcon());
        req.setHomeId(vo.getHomeId());
        if (StringUtils.isNotBlank(vo.getId())) {
            req.setId(Long.parseLong(vo.getId()));
        }
        if (StringUtils.isNotBlank(vo.getLocationId())) {
            req.setLocationId(Long.parseLong(vo.getLocationId()));
        }
        req.setName(vo.getName());
        req.setPosition(vo.getPosition());
        req.setType(vo.getType());
        req.setSort(vo.getSort());
        req.setDefaultSpace(vo.getDefaultSpace());
        req.setPageSize(vo.getPageSize());
        req.setOffset(vo.getOffset());
        req.setParentId(vo.getHomeId() != null ? vo.getHomeId() : vo.getParentId());

        return req;
    }


    @ApiOperation("上传图片")
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public CommonResponse uploadImg(MultipartHttpServletRequest multipartRequest, @RequestParam("fileId") String fileId) {

        //判空
        if (multipartRequest == null || !multipartRequest.getFileNames().hasNext()) {
            return new CommonResponse(ResultMsg.FAIL, "Fail.", "File is null");
        }

        try {
            if(StringUtil.isNotEmpty(fileId)&&!"null".equals(fileId)){
                FileBean fileBean=fileApi.getFileInfoByFileId(fileId);
                if(fileBean!=null){
                    fileApi.deleteObject(fileId);
                }
            }
        }catch (Exception E){
            log.error("delete fileId error");
        }

        //判断图片类型
        MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        String ImgSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        String[] result = uploadImgStr.split(",");
        Boolean isOk = Arrays.asList(result).contains(ImgSuffix);

        if (isOk) {

            //获取租户Id
            Long currentTenantId = SaaSContextHolder.currentTenantId();
//            String newfileId = this.fileApi.upLoadFile(multipartFile, currentTenantId);
//            String fildUrl=fileApi.getGetUrl(newfileId).getPresignedUrl();
            FileInfoResp fileInfoResp = fileUploadApi.upLoadFileAndGetUrl(multipartFile, currentTenantId);
            String newfileId=fileInfoResp.getFileId();
            String fildUrl=fileInfoResp.getUrl();
            BgImgVO bgImgVO=new BgImgVO();
            bgImgVO.setFileId(newfileId);
            bgImgVO.setFileUrl(fildUrl);
            log.info("/home/uploadImg tenantId is{},newfileId is{},fileUrl is{}", currentTenantId, newfileId, fildUrl);
            return new CommonResponse<BgImgVO>(ResultMsg.SUCCESS, "Success.", bgImgVO);
        } else {
            return new CommonResponse(ResultMsg.FAIL, "Fail.", "Wrong image type");
        }
    }

    @ApiOperation("取消上传的图片")
    @RequestMapping(value = "/cancleUploadFile", method = RequestMethod.POST)
    public  CommonResponse cancleUploadFile(@RequestParam("fileId") String fileId){
        fileApi.deleteObject(fileId);
        return CommonResponse.success().msg("delete ok!");
    }

}
