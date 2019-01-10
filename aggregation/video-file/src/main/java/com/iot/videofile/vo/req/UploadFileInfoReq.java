package com.iot.videofile.vo.req;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.videofile.exception.BusinessExceptionEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @program: cloud
 * @description: 上报文件信息入参
 * @author: yeshiyuan
 * @create: 2018-12-19 10:26
 **/
@ApiModel(description = "上报文件信息入参")
@Data
public class UploadFileInfoReq {

    private String planId;

    private VideoFileInfoReq file;

    private String timestamp;

    /**
      * @despriction：校验参数
      * @author  yeshiyuan
      * @created 2018/12/19 10:58
      * @params [req]
      * @return void
      */
    public static void checkParam(UploadFileInfoReq req) {
        if (StringUtil.isBlank(req.getPlanId())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null");
        }
        VideoFileInfoReq fileInfo = req.getFile();
        if (fileInfo == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "file info is null");
        }
        if (StringUtil.isBlank(fileInfo.getFn())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "file name is null");
        }
        if (StringUtil.isBlank(fileInfo.getFileType())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "file type is null");
        }
        if (fileInfo.getStartTime() == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "file start time is null");
        }
    }
}
