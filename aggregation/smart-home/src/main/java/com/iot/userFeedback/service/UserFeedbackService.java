package com.iot.userFeedback.service;

import com.alibaba.fastjson.JSON;
import com.iot.BusinessExceptionEnum;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.StringUtil;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.vo.FileInfoResp;
import com.iot.message.api.MessageApi;
import com.iot.user.api.FeedbackApi;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FeedbackFileVo;
import com.iot.user.vo.FeedbackReq;
import com.iot.user.vo.FeedbackVo;
import com.iot.user.vo.FetchUserResp;
import com.iot.userFeedback.controller.UserFeedbackController;
import com.iot.userFeedback.vo.FeedbackContentVo;
import com.iot.userFeedback.vo.UploadFeedbackVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserFeedbackService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserFeedbackService.class);

    @Autowired
    private FeedbackApi feedbackApi;

    @Autowired
    private FileUploadApi fileUploadApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private MessageApi messageApi;
    /**
     * 线程池
     */
    private static ExecutorService excutor = Executors.newCachedThreadPool();

    //接受的图片类型
    @Value("${upload.img.str}")
    private String uploadImgStr;

    //接受的音频类型
    @Value("${upload.video.str}")
    private String uploadVideoStr;

    public CommonResponse saveFeedback(Long userId, String feedbackContent) {

        if (feedbackContent.isEmpty() || feedbackContent == null || userId == null) {
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
        FeedbackReq feedbackReq = new FeedbackReq();
        feedbackReq.setUserId(userId);
        feedbackReq.setFeedbackContent(feedbackContent);
        int result = feedbackApi.saveFeedback(feedbackReq);
        if (result == 1) {
            return CommonResponse.success(true).setDesc("Save feedback Ok");
        }
        return CommonResponse.success(false).setDesc("Save feedback false");
    }


    public  CommonResponse saveFeedbackFile(MultipartHttpServletRequest multipartRequest, Long currentTenantId, Long userId){

        //判空
        if (multipartRequest == null || !multipartRequest.getFileNames().hasNext()) {
            return new CommonResponse(ResultMsg.FAIL, "Fail.", "File is null");
        }
        if(currentTenantId==null){
            return new CommonResponse(ResultMsg.FAIL, "Fail.", "tenantId is null");
        }

        //判断图片类型
        MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        String fileSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        String[] imgStr = uploadImgStr.split(",");
        String[] videoStr=uploadVideoStr.split(",");
        Boolean isImg = Arrays.asList(imgStr).contains(fileSuffix);
        Boolean isVideo=Arrays.asList(videoStr).contains(fileSuffix);
        LOGGER.info("filetype is {},isImg is {}, is Video is{}",fileSuffix,isImg,isVideo);
        if(isImg||isVideo){
            //将文件上传
            FileInfoResp fileInfoResp = fileUploadApi.upLoadFileAndGetUrl(multipartFile, currentTenantId);
            String fileId=fileInfoResp.getFileId();
            String fileUrl=fileInfoResp.getUrl();
            FeedbackFileVo feedbackFileVo=new FeedbackFileVo();
            feedbackFileVo.setFileId(fileId);
            feedbackFileVo.setUserId(userId);
            feedbackFileVo.setCreateTime(new Date());
            feedbackFileVo.setTenantId(currentTenantId);
            feedbackFileVo.setUpdateTime(new Date());
            LOGGER.info("saveFeedbackFile fileId{},fileUrl{}",fileId,fileUrl);
            feedbackApi.saveFeedbackFile(feedbackFileVo);
        }else {
            return new CommonResponse(ResultMsg.FAIL, "Fail.", "File type is not allowed");
        }
        return new CommonResponse(ResultMsg.SUCCESS, "Success.");
    }

    //上传用户反馈
    public  CommonResponse uploadFeedback(MultipartHttpServletRequest multipartRequest, Long currentTenantId, Long userId,String content){

        LOGGER.info("currentTenantId{}, userId{},content{}",currentTenantId,userId,content);
        //必须保证有tenantId，文件上传时需要,存储时需要userId
        if(currentTenantId==null||userId==null){
            return new CommonResponse(ResultMsg.FAIL, "Fail.", "userId or tenantId is null");
        }

        //反馈中必须要有内容但是可以不用传文件
        if(StringUtil.isEmpty(content)){
            return new CommonResponse(ResultMsg.FAIL, "Fail.", "feedback content is null");
        }

        //调用user-service中的feedbackApi将数据存储到数据库中

        FeedbackVo feedbackVo=new FeedbackVo();
        feedbackVo.setFeedbackContent(content);
        feedbackVo.setTenantId(currentTenantId);
        feedbackVo.setUserId(userId);
        List<MultipartFile> files=new ArrayList<>();
        FetchUserResp fetchUserResp=userApi.getUser(userId);
        String userName=fetchUserResp.getUserName();
        String message=userName+"(tenantId"+currentTenantId+")"+":  "+content;
        Long appId = -1L;
        //当只有内容没有文件时调用
        if(multipartRequest==null){
            LOGGER.info("multipartRequest is null");
            feedbackApi.save(feedbackVo);
        }else {
            //存储说所有的文件id
            List<String> fileIdList=new ArrayList<>();
            Iterator<String> iterator=multipartRequest.getFileNames();
            LOGGER.info("multipartRequest.getFileNames{}",JSON.toJSONString(multipartRequest.getFileNames()));
            //批量上传文件循环调用
            while (iterator.hasNext()){
                MultipartFile multipartFile=multipartRequest.getFile(iterator.next());
                LOGGER.info("multipartFile size{}",multipartFile.getSize());
                //判断上传的文件是否都符合格式要求
                String fileSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
                String[] imgStr = uploadImgStr.split(",");
                String[] videoStr=uploadVideoStr.split(",");
                Boolean isImg = Arrays.asList(imgStr).contains(fileSuffix);
                Boolean isVideo=Arrays.asList(videoStr).contains(fileSuffix);
                LOGGER.info("fileSuffix{},isImg{} 和 isVideo{}",fileSuffix,isImg,isVideo);
                if(isImg||isVideo){
                    //将数据临时存储到redis中，只有当所有的文件都成功时才存入数据库
                    FileInfoResp fileInfoResp = fileUploadApi.uploadFileButNoSaveToDb(multipartFile, currentTenantId);
                    String fileId=fileInfoResp.getFileId();
                    fileIdList.add(fileId);
                    files.add(multipartFile);
                    LOGGER.info("saveFeedbackFile fileId{}",fileId);
                }else{
                    LOGGER.info("Feedback file type is not allowed");
                    return new CommonResponse(ResultMsg.FAIL, "Fail.", "Feedback file type is not allowed");
                }
            }

            feedbackVo.setFileId(fileIdList);
            //表明所有的文件都符合格式要求上传成功，此时保存到文件数据库中
            LOGGER.info("**********saveFeedbackFileList"+ JSON.toJSONString(fileIdList));
            if(CollectionUtils.isNotEmpty(fileIdList)){
                fileApi.saveFileInfosToDb(fileIdList);
            }
            feedbackApi.save(feedbackVo);
        }
        //异步发送邮件
        excutor.submit(new Runnable() {
            @Override
            public void run() {
                messageApi.annexMailSinglePush((MultipartFile[])files.toArray(),appId,message,3,"");
            }
        });

        return new CommonResponse(ResultMsg.SUCCESS,"Success.");
    }
}
