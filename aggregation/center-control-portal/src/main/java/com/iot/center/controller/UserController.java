package com.iot.center.controller;

import com.iot.center.annotation.PermissionAnnotation;
import com.iot.center.service.UserService;
import com.iot.center.vo.UserInfoVo;
import com.iot.common.beans.CommonResponse;
import com.iot.common.helper.Page;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.RoleDto;
import com.iot.permission.vo.RoleReqDto;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.UserReq;
import com.iot.user.vo.UserResp;
import com.iot.user.vo.UserSearchReq;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/9/5
 * @Description: *
 */
@Api(tags = "用户权限分配")
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PermissionAnnotation(value = "USER_LIST")
    @RequestMapping("getUserList")
    @ResponseBody
    public CommonResponse<Page<UserResp>> getUserList(UserSearchReq req) {
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        req.setTenantId(user.getTenantId());
        req.setLocationId(user.getLocationId());
        return userService.getUserList(req);
    }

    @PermissionAnnotation(value = "USER_LIST")
    @RequestMapping("addUser")
    @ResponseBody
    public CommonResponse addUser(UserReq req) {
        return userService.addUser(req);
    }

    @PermissionAnnotation(value = "USER_LIST")
    @RequestMapping("editUser")
    @ResponseBody
    public CommonResponse editUser(UserReq req) {
        return userService.editUser(req);
    }

    @PermissionAnnotation(value = "USER_LIST")
    @RequestMapping("deleteUser")
    @ResponseBody
    public CommonResponse deleteUser(Long id) {
        return userService.deleteUser(id);
    }

    @PermissionAnnotation(value = "USER_LIST")
    @RequestMapping("deleteUserBatch")
    @ResponseBody
    public CommonResponse deleteUserBatch(String ids) {
    	if(StringUtils.isNotBlank(ids)) {
    		String[] idsArry=ids.split(",");
    		for(String idStr:idsArry) {
    			Long id=Long.valueOf(idStr);
    			userService.deleteUser(id);
    		}
    	}
        return CommonResponse.success();
    }

    @PermissionAnnotation(value = "ROLE_PERMISSION")
    @RequestMapping("getRoleList")
    @ResponseBody
    public CommonResponse<List<RoleDto>> getRoleList(RoleReqDto req) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	req.setTenantId(user.getTenantId());
    	req.setOrgId(user.getOrgId());
        return userService.getRoleList(req);
    }

    @PermissionAnnotation(value = "ROLE_PERMISSION")
    @RequestMapping("addRole")
    @ResponseBody
    public CommonResponse addRole(RoleDto role) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        return userService.addRole(role,user.getUserId());
    }

    @PermissionAnnotation(value = "ROLE_PERMISSION")
    @RequestMapping("editRole")
    @ResponseBody
    public CommonResponse editRole(RoleDto role) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	role.setUpdateBy(user.getUserId());
    	role.setUpdateTime(new Date());
        return userService.editRole(role);
    }

    @PermissionAnnotation(value = "ROLE_PERMISSION")
    @RequestMapping("deleteRole")
    @ResponseBody
    public CommonResponse deleteRole(Long id) {
        return userService.deleteRole(id);
    }

    @RequestMapping("assignPermissionToRole")
    @ResponseBody
    public CommonResponse assignPermissionToRole(Long roleId, @RequestParam(value = "permissionIds") Long[] permissionIds) {
        return userService.assignPermissionToRole(roleId, permissionIds);
    }

    @RequestMapping("assignPermissionToUser")
    @ResponseBody
    public CommonResponse assignPermissionToUser(Long userId, Long roleId, @RequestParam(value = "spaceIds") Long[] spaceIds) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	return userService.assignPermissionToUser(user.getTenantId(),userId, roleId, spaceIds);
    }

    @RequestMapping("getUserPermission")
    @ResponseBody
    public CommonResponse<UserInfoVo> getUserPermission(Long userId) {
        return userService.getUserPermission(userId);
    }

    @RequestMapping("getPermissionList")
    @ResponseBody
    public CommonResponse<List<PermissionDto>> getPermissionList() {
        return userService.getPermissionList();
    }

    @RequestMapping("getPermissionByRoleId")
    @ResponseBody
    public CommonResponse<List<PermissionDto>> getPermissionByRoleId(Long roleId) {
        return userService.getPermissionByRoleId(roleId);
    }

    @RequestMapping("batchImportUser")
    @ResponseBody
    public CommonResponse batchImportUser(@RequestParam("file") MultipartFile file) {
        return userService.batchImportUser(file);
    }
    
    /**
     * 导入excel，并解析excel
     */
//    @SystemLogAnnotation(value = "用户上传并添加角色")
//    @RequestMapping(value = "/addUserFromExcel", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResponse<Map<String,Object>> addUserFromExcel(MultipartHttpServletRequest multipartRequest) {
//        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
//        try {
//            MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
//            List<String[]> list = Lists.newArrayList();
//            try {
//                list = ExcelUtils.resolveExcel(multipartFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Map<String,Object> map=userService.saveUserAndRoleByExcel(user, list);
//            return CommonResponse.success(map);
//        } catch (BusinessException e) {
//            throw e;
//        }
//    }

	
}
