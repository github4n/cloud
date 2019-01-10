package com.iot.center.controller;

import java.util.List;

import com.iot.center.annotation.PermissionAnnotation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.building.shortcut.vo.ShortcutVo;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.service.ShortcutService;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.user.vo.LoginResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "快捷方式管理接口" })
@Controller
@RequestMapping("/shortcut")
public class ShortcutController {
	
	@Autowired
	private ShortcutService shortcutService;

	@ApiOperation("list")
    @RequestMapping("/list")
    @ResponseBody
    public CommonResponse<List<ShortcutVo>> list(Long spaceId){
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        ShortcutVo vo =new ShortcutVo(); vo.setSpaceId(spaceId);
        vo.setTenantId(user.getTenantId());vo.setLocationId(user.getLocationId());
        List<ShortcutVo> voList=shortcutService.findShortcutList(vo);
        return CommonResponse.success(voList);
    }

    @PermissionAnnotation(value = "SHORTCUT")
	@SystemLogAnnotation(value = "执行快捷方式")
	@ApiOperation("excute")
    @RequestMapping("/excute")
    @ResponseBody
    public CommonResponse<List<ShortcutVo>> excute(Long id,String type,Integer flag){
       LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
       shortcutService.excute(id, type, flag,user.getTenantId(),user.getLocationId());
       return CommonResponse.success();
    }
}
