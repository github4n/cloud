package com.iot.center.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.building.allocation.api.ActivityRecordApi;
import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.center.annotation.PermissionAnnotation;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.user.vo.LoginResp;

@Controller
@RequestMapping("/activeRecord")
public class ActiveRecordController {

	@Autowired
	private ActivityRecordApi activityRecordApi;

	@PermissionAnnotation(value = "USER_LOG")
	@RequestMapping("/page")
	@ResponseBody
	public CommonResponse<Page<ActivityRecordResp>> findSapcePage(@RequestParam(value = "name", required = false) String name, int pageNumber, int pageSize)
			throws BusinessException {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		ActivityRecordReq activityRecordReq=new ActivityRecordReq();
		activityRecordReq.setUserName(name);
		activityRecordReq.setPageNum(pageNumber);
		activityRecordReq.setPageSize(pageSize);
		Page<ActivityRecordResp> page = activityRecordApi.queryActivityRecordToB(activityRecordReq);
		return CommonResponse.success(page);
	}
}
