package com.iot.common.controller;

import com.github.pagehelper.PageInfo;
import com.iot.BusinessExceptionEnum;
import com.iot.cloud.helper.Constants;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.common.vo.ActivityRecordVO;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.activity.vo.rsp.ActivityRecordResp;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Api(description = "日志活动接口")
@RestController
public class ActivityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityRecordApi activityRecordApi;

    @ApiOperation(value = "获得全部设备活动记录", notes = "获得全部设备活动记录")
    @RequestMapping(value = "/activityController/getAllDevActivity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Page<ActivityRecordResp>> getAllDevActivity(@RequestBody ActivityRecordVO activityRecordVO) {
        activityRecordVO.setType(null);
        return list(activityRecordVO);
    }

    @ApiOperation(value = "获得单个设备活动记录", notes = "获得单个设备活动记录")
    @RequestMapping(value = "/activityController/getDevActivityById", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Page<ActivityRecordResp>> getDevActivityById(@RequestBody ActivityRecordVO activityRecordVO) {
        activityRecordVO.setType(null);
        return list(activityRecordVO);
    }

    @ApiOperation(value = "获得安防活动记录", notes = "获得安防活动记录")
    @RequestMapping(value = "/activityController/getSecurityActivity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Page<ActivityRecordResp>> getSecurityActivity(@RequestBody ActivityRecordVO activityRecordVO) {
        activityRecordVO.setType(Constants.ACTIVITY_RECORD_SECURITY);
        return list(activityRecordVO);
    }

    @ApiOperation(value = "删除全部设备活动记录", notes = "删除全部设备活动记录")
    @RequestMapping(value = "/activityController/delAllDevActivity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Integer> delAllDevActivity(@RequestBody ActivityRecordVO activityRecordVO) {
        activityRecordVO.setType(null);
        int result = delete(activityRecordVO);
        return new CommonResponse<Integer>(ResultMsg.SUCCESS, result);
    }

    @ApiOperation(value = "删除单个设备活动记录", notes = "删除单个设备活动记录")
    @RequestMapping(value = "/activityController/delDevActivity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Integer> delDevActivity(@RequestBody ActivityRecordVO activityRecordVO) {
        activityRecordVO.setType(null);
        int result = delete(activityRecordVO);
        return new CommonResponse<Integer>(ResultMsg.SUCCESS, result);
    }

    @ApiOperation(value = "删除安防活动记录", notes = "删除安防活动记录")
    @RequestMapping(value = "/activityController/delSecurityActivity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Integer> delSecurityActivity(@RequestBody ActivityRecordVO activityRecordVO) {
        activityRecordVO.setType(Constants.ACTIVITY_RECORD_SECURITY);
        int result = delete(activityRecordVO);
        return new CommonResponse<Integer>(ResultMsg.SUCCESS, result);
    }

    public CommonResponse<Page<ActivityRecordResp>> list(ActivityRecordVO activityRecordVO) {
        CommonResponse<Page<ActivityRecordResp>> result = null;
        try {
            int pageSize = activityRecordVO.getPageSize();
            int pageNum = activityRecordVO.getOffset();
            Long userId = SaaSContextHolder.getCurrentUserId();
            String time = activityRecordVO.getTimestamp();

            ActivityRecordReq activityRecordReq = new ActivityRecordReq();
            activityRecordReq.setPageSize(pageSize);
            activityRecordReq.setPageNum(pageNum);

            if (StringUtils.isNotBlank(time)) {
                time = time.split(" ")[0];
                try {
                    activityRecordReq.setTime(DateUtils.parseDate(time, "yyyy-MM-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            activityRecordReq.setCreateBy(userId);
            activityRecordReq.setType(activityRecordVO.getType());

            if (StringUtil.isNotBlank(activityRecordVO.getDevId())) {
                activityRecordReq.setForeignId(activityRecordVO.getDevId());
            }

            PageInfo<ActivityRecordResp> page = activityRecordApi.queryActivityRecord(activityRecordReq);
            Page<ActivityRecordResp> currentPage = new Page<ActivityRecordResp>(page.getPageNum(), page.getPageSize());
            currentPage.setTotal(page.getTotal());
            currentPage.setResult(page.getList());
            currentPage.setPages(page.getPages());
            result = new CommonResponse<Page<ActivityRecordResp>>(ResultMsg.SUCCESS, currentPage);
        } catch (BusinessException e) {
            LOGGER.error("get activityResponselist failed", e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION, e);
        }
        return result;
    }

    public Integer delete(ActivityRecordVO activityRecordVO) {
        Integer result = null;
        try {
            Long userId = SaaSContextHolder.getCurrentUserId();

            ActivityRecordReq activityRecordReq = new ActivityRecordReq();
            activityRecordReq.setCreateBy(userId);
            activityRecordReq.setType(activityRecordVO.getType());

            if (StringUtil.isNotBlank(activityRecordVO.getDevId())) {
                activityRecordReq.setForeignId(activityRecordVO.getDevId());
            }

            result = activityRecordApi.delActivityRecord(activityRecordReq);
        } catch (BusinessException e) {
            LOGGER.error("delete activityResponselist failed", e);
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION, e);
        }
        return result;
    }

}
