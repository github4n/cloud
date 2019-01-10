package com.iot.portal.corpuser.service.impl;

import com.github.pagehelper.PageInfo;
import com.iot.common.beans.SearchParam;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.*;
import com.iot.file.api.FileApi;
import com.iot.file.entity.FileBean;
import com.iot.permission.api.PermissionApi;
import com.iot.permission.constants.ModuleConstants;
import com.iot.permission.entity.UserRoleRelate;
import com.iot.permission.enums.RoleTypeEnum;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.RoleDto;
import com.iot.permission.vo.UserToRolesDto;
import com.iot.portal.common.service.CommonServiceImpl;
import com.iot.portal.corpuser.service.UserBusinessService;
import com.iot.portal.corpuser.vo.*;
import com.iot.portal.corpuser.vo.UserResp;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.LangApi;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.enums.AuditStatusEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.enums.SystemTypeEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.*;
import com.iot.util.ToolUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户接口实现
 * 创建人： nongchongwei
 * 创建时间：2018年07月04日 10:09
 * 修改人： nongchongwei
 * 修改时间：2018年07月04日 10:09
 */
@Service("userBusiness")
public class UserBusinessServiceImpl implements UserBusinessService {

    /**
     * 日志
     */
    private static final Log logger = LogFactory.getLog(UserBusinessServiceImpl.class);

    /**
     * 用户服务
     */
    @Autowired
    private UserApi userApi;

    /**
     * 权限服务
     */
    @Autowired
    protected PermissionApi permissionApi;

    /**
     * 文件服务
     */
    @Autowired
    private FileApi fileApi;

    @Autowired
    protected TenantApi tenantApi;

    @Autowired
    private LangApi langApi;

    /**
     * 描述：企业用户注册
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/2 20:28
     */
    @Override
	public Long userRegister(UserRegisterReq user) {
		// 参数校验
		String language = LocaleContextHolder.getLocale().toString();
		ValidateUtil.validateEmail(user.getUserName());
		if(user.getTel() != null) {
			ValidateUtil.isNumeric(user.getTel());
		}
		if ("zh_CN".equals(language)) {
			CommonServiceImpl.checkStringParam(user.getNickname(), 0, 25, language);
			CommonServiceImpl.checkStringParam(user.getAddress(), 0, 50, language);
			CommonServiceImpl.checkStringParam(user.getCompany(), 0, 25, language);
			CommonServiceImpl.checkStringParam(user.getPassWord(), 0, 50, "en_us");
		} else {
			CommonServiceImpl.checkStringParam(user.getNickname(), 0, 50, language);
			CommonServiceImpl.checkStringParam(user.getAddress(), 0, 100, language);
			CommonServiceImpl.checkStringParam(user.getCompany(), 0, 50, language);
			CommonServiceImpl.checkStringParam(user.getPassWord(), 0, 50, "en_us");
		}
		RegisterReq req = new RegisterReq();
		BeanUtils.copyProperties(user, req);
		// 给用户一个默认头像
		req.setHeadImg("img_20");
		return this.userApi.register2B(req);
	}

    /**
     * 描述：企业用户登录
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/2 20:28
     */
    @Override
	public UserLoginResp userLogin(UserLoginReq ulpVO) {

		ValidateUtil.validateEmail(ulpVO.getUserName());
		CommonServiceImpl.checkStringParam(ulpVO.getPassWord(), 0, 50, "en_us");

		LoginReq loginReq = new LoginReq();
		BeanUtils.copyProperties(ulpVO, loginReq);
		loginReq.setPwd(StringUtil.isNotEmpty(ulpVO.getPassWord()) ? ulpVO.getPassWord() : ulpVO.getPwd());
		LoginResp loginResp = this.userApi.login2B(loginReq);
		UserLoginResp userToken = new UserLoginResp();
		BeanUtils.copyProperties(loginResp, userToken);
		userToken.setUserUuId(loginResp.getUserUuid());
		List<PermissionDto> permissionDtoList = permissionApi.getUserPermissionTree(loginResp.getUserId());
		userToken.setPermissionDtoList(permissionDtoList);
		return userToken;
	}

    /**
     * 描述：完善企业信息
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/2 20:31
     */
    @Override
	public Long improveTenantInfo(TenantUpdateReq req) {
		Long userId = SaaSContextHolder.getCurrentUserId();
		Long tenantId = SaaSContextHolder.currentTenantId();
		req.setUserId(userId);
		req.setId(tenantId);
		String language = LocaleContextHolder.getLocale().toString();
		if ("zh_CN".equals(language)) {
			CommonServiceImpl.checkStringParam(req.getName(), 0, 10, language);
			CommonServiceImpl.checkStringParam(req.getBusiness(), 0, 30, language);
			CommonServiceImpl.checkStringParam(req.getContacts(), 0, 30, language);
			CommonServiceImpl.checkStringParam(req.getJob(), 0, 10, language);
			CommonServiceImpl.checkStringParam(req.getCountry(), 0, 25, language);
			CommonServiceImpl.checkStringParam(req.getProvince(), 0, 25, language);
			CommonServiceImpl.checkStringParam(req.getCity(), 0, 25, language);
			CommonServiceImpl.checkStringParam(req.getAddress(), 0, 50, language);
		} else {
			CommonServiceImpl.checkStringParam(req.getName(), 0, 20, language);
			CommonServiceImpl.checkStringParam(req.getBusiness(), 0, 100, language);
			CommonServiceImpl.checkStringParam(req.getContacts(), 0, 100, language);
			CommonServiceImpl.checkStringParam(req.getJob(), 0, 20, language);
			CommonServiceImpl.checkStringParam(req.getCountry(), 0, 50, language);
			CommonServiceImpl.checkStringParam(req.getProvince(), 0, 50, language);
			CommonServiceImpl.checkStringParam(req.getCity(), 0, 50, language);
			CommonServiceImpl.checkStringParam(req.getAddress(), 0, 100, language);
		}
		// 验证邮箱
		if(req.getEmail() != null) {
			ValidateUtil.validateEmail(req.getEmail());
		}
		TenantReq tenantReq = new TenantReq();
		BeanUtils.copyProperties(req, tenantReq);
		return this.userApi.improveTenantInfo(tenantReq);
	}

    @Override
    public Long saveTenantInfo(TenantUpdateReq req) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        req.setUserId(userId);
        req.setId(tenantId);
        String language = LocaleContextHolder.getLocale().toString();
        if ("zh_CN".equals(language)) {
            CommonServiceImpl.checkStringParam(req.getName(), 0, 10, language);
            CommonServiceImpl.checkStringParam(req.getBusiness(), 0, 30, language);
            CommonServiceImpl.checkStringParam(req.getContacts(), 0, 30, language);
            CommonServiceImpl.checkStringParam(req.getJob(), 0, 10, language);
            CommonServiceImpl.checkStringParam(req.getCountry(), 0, 25, language);
            CommonServiceImpl.checkStringParam(req.getProvince(), 0, 25, language);
            CommonServiceImpl.checkStringParam(req.getCity(), 0, 25, language);
            CommonServiceImpl.checkStringParam(req.getAddress(), 0, 50, language);
        } else {
            CommonServiceImpl.checkStringParam(req.getName(), 0, 20, language);
            CommonServiceImpl.checkStringParam(req.getBusiness(), 0, 100, language);
            CommonServiceImpl.checkStringParam(req.getContacts(), 0, 100, language);
            CommonServiceImpl.checkStringParam(req.getJob(), 0, 20, language);
            CommonServiceImpl.checkStringParam(req.getCountry(), 0, 50, language);
            CommonServiceImpl.checkStringParam(req.getProvince(), 0, 50, language);
            CommonServiceImpl.checkStringParam(req.getCity(), 0, 50, language);
            CommonServiceImpl.checkStringParam(req.getAddress(), 0, 100, language);
        }
        // 验证邮箱
        if(req.getEmail() != null) {
            ValidateUtil.validateEmail(req.getEmail());
        }
        TenantReq tenantReq = new TenantReq();
        BeanUtils.copyProperties(req, tenantReq);
        return this.userApi.saveTenantInfo(tenantReq);
    }

    @Override
	public TenantResp getTenantInfo() {
		Long tenantId = SaaSContextHolder.currentTenantId();
		TenantInfoResp tenantInfoResp = tenantApi.getTenantById(tenantId);
		if (null == tenantInfoResp) {
			throw new BusinessException(BusinessExceptionEnum.TENANT_INFO_IS_NULL);
		}
		TenantResp tenantResp = new TenantResp();
		BeanUtils.copyProperties(tenantInfoResp, tenantResp);
        tenantResp.setCellphone(ToolUtils.idEncrypt(tenantInfoResp.getCellphone()));
		return tenantResp;
	}

    /**
     * 描述：用户退出
     *
     * @param terminalMark 终端类型（app|web）
     * @return
     * @author nongchongwei
     * @date 2018/7/4 15:01
     */
    @Override
    public void userLogout(String terminalMark) {
        this.userApi.logout(SaaSContextHolder.getCurrentUserId(), terminalMark);
    }

    /**
     * 描述：查询用户详细信息
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/4 15:02
     */
    @Override
	public UserResp getUserInfo() {
		Long id = SaaSContextHolder.getCurrentUserId();
		if (null == id) {
			throw new BusinessException(BusinessExceptionEnum.USER_NEED_LOGIN);
		}
		FetchUserResp user = this.userApi.getUser(id);
		UserResp userResp = new UserResp();
		BeanUtils.copyProperties(user, userResp);
		return userResp;
	}

    /**
     * 描述：修改用户详细信息
     *
     * @param userVO 用户对象
     * @return
     * @author nongchongwei
     * @date 2018/7/4 15:02
     */
    @Override
	public void editUserInfo(UserUpdateReq userVO) {
		String language = LocaleContextHolder.getLocale().toString();
		ValidateUtil.validateEmail(userVO.getUserName());
		if(userVO.getEmail() != null) {
			ValidateUtil.validateEmail(userVO.getEmail());
		}
		if(userVO.getTel() != null) {
			ValidateUtil.isNumeric(userVO.getTel());
		}
		if ("zh_CN".equals(language)) {
			CommonServiceImpl.checkStringParam(userVO.getNickname(), 0, 25, language);
			CommonServiceImpl.checkStringParam(userVO.getAddress(), 0, 50, language);
			CommonServiceImpl.checkStringParam(userVO.getCompany(), 0, 25, language);
		} else {
			CommonServiceImpl.checkStringParam(userVO.getNickname(), 0, 50, language);
			CommonServiceImpl.checkStringParam(userVO.getAddress(), 0, 100, language);
			CommonServiceImpl.checkStringParam(userVO.getCompany(), 0, 50, language);
		}
		userVO.setId(SaaSContextHolder.getCurrentUserId());
		UpdateUserReq user = new UpdateUserReq();
		BeanUtils.copyProperties(userVO, user);
		this.userApi.updateUser(user);
	}

    /**
     * 描述：
     *
     * @param multipartRequest 文件参数
     * @param newHeadImg       系统头像编号
     * @return
     * @author nongchongwei
     * @date 2018/7/4 14:33
     */
    @Override
    public String uploadPhoto(MultipartHttpServletRequest multipartRequest, String newHeadImg) {
        if ((multipartRequest != null && !multipartRequest.getFileNames().hasNext()) || (StringUtil.isEmpty(newHeadImg) && multipartRequest == null)) {
            throw new BusinessException(BusinessExceptionEnum.USER_PHOTO_IS_EMPTY);
        }
        //fileId
        String oldHeadImg = this.userApi.getUserHeadImg(SaaSContextHolder.getCurrentUserId());
        if (StringUtil.isEmpty(newHeadImg)) {
            return chargeHeadImg(multipartRequest, oldHeadImg);
        } else {
            this.userApi.setUserHeadImg(SaaSContextHolder.getCurrentUserId(), newHeadImg);
            if (!oldHeadImg.contains("img_")) {   //不是系统头像才去删除
                this.deleteOldHeadImg(oldHeadImg);
            }
            return newHeadImg;
        }
    }

    private String chargeHeadImg(MultipartHttpServletRequest multipartRequest, String oldHeadImg) {
        try {
            MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
            String fileName = multipartFile.getOriginalFilename();
            String newFileType = fileName.substring(fileName.lastIndexOf('.') + 1);
            String fileId;
            //获取旧文件类型
            String oldFileType = "";
            if (StringUtil.isNotEmpty(oldHeadImg) && !oldHeadImg.contains("img_")) {
                FileBean fileBean = fileApi.getFileInfoByFileId(oldHeadImg);
                if (fileBean != null) {
                    oldFileType = fileBean.getFileType();
                }
            }
            if (StringUtil.isNotEmpty(oldHeadImg) && oldFileType.equals(newFileType) && !oldHeadImg.contains("img_")) {
                //使用自定义头像且文件类型与上一次使用的文件类型一致的话则直接覆盖之前的文件
                fileId = oldHeadImg;
                fileApi.updateFile(multipartFile, fileId, "");
            } else {
                //上传文件
                fileId = this.fileApi.upLoadFile(multipartFile, SaaSContextHolder.currentTenantId());
                this.userApi.setUserHeadImg(SaaSContextHolder.getCurrentUserId(), fileId);
                if (!oldHeadImg.contains("img_")) {   //不是系统头像才去删除
                    this.deleteOldHeadImg(oldHeadImg);
                }
            }
            return this.fileApi.getGetUrl(fileId).getPresignedUrl();
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException(BusinessExceptionEnum.USER_PHOTO_UPLOAD_FAILED, e);
        }
    }

    /**
     * 描述：删除旧头像
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/4 15:03
     */
    private void deleteOldHeadImg(String oldHeadImg) {
        try {
            if (StringUtil.isNotBlank(oldHeadImg) && oldHeadImg.length() > 2) {
                this.fileApi.deleteObject(oldHeadImg);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * 描述：修改密码
     *
     * @param oldPwd 旧密码（经md5加密）
     * @param newPwd 新密码（经md5加密）
     * @return
     * @author nongchongwei
     * @date 2018/7/4 15:03
     */
    @Override
    public void changePassword(String oldPwd, String newPwd) {
    	CommonServiceImpl.checkStringParam(newPwd, 0, 50, "en_us");
        ModifyPwdReq o = new ModifyPwdReq();
        o.setNewPwd(newPwd);
        o.setOldPwd(oldPwd);
        o.setUserId(SaaSContextHolder.getCurrentUserId());
        this.userApi.modifyPwd(o);
    }

    /**
     * 描述：重置密码
     *
     * @param userName   email或手机号
     * @param verifyCode 验证码
     * @param newPwd     新密码（经md5加密）
     * @return void
     * @author nongchongwei
     * @created 2018/4/8 16:30
     */
    @Override
    public void resetPassword(String userName, String verifyCode, String newPwd) {
    	CommonServiceImpl.checkStringParam(newPwd, 0, 50, "en_us");
        ResetPwdReq o = new ResetPwdReq();
        o.setNewPwd(newPwd);
        o.setUserName(userName);
        o.setVerifyCode(verifyCode);
        this.userApi.resetPwd2B(o);
    }

    /**
     * 描述：创建子账户
     *
     * @param user 用户对象
     * @return void
     * @author nongchongwei
     * @created 2018/4/8 16:30
     */
    @Override
    public void createSubUser(UserRegisterReq user) {
    	
    	//参数校验
    	String language = LocaleContextHolder.getLocale().toString();
    	ValidateUtil.validateEmail(user.getUserName());
    	if(user.getTel() != null) {
    		ValidateUtil.isNumeric(user.getTel());
    	}
    	if ("zh_CN".equals(language)) {
        	CommonServiceImpl.checkStringParam(user.getNickname(), 0, 25, language);
            CommonServiceImpl.checkStringParam(user.getAddress(), 0, 50, language);
            CommonServiceImpl.checkStringParam(user.getCompany(), 0, 25, language);
            CommonServiceImpl.checkStringParam(user.getPassWord(), 0, 50, "en_us");
    	}else {
        	CommonServiceImpl.checkStringParam(user.getNickname(), 0, 50, language);
            CommonServiceImpl.checkStringParam(user.getAddress(), 0, 100, language);
            CommonServiceImpl.checkStringParam(user.getCompany(), 0, 50, language);
            CommonServiceImpl.checkStringParam(user.getPassWord(), 0, 50, "en_us");
    	}

        RegisterReq req = new RegisterReq();
        BeanUtils.copyProperties(user, req);
        //给用户一个默认头像
        req.setHeadImg("img_20");
        req.setTenantId(SaaSContextHolder.currentTenantId());
        TenantInfoResp tenantInfoResp = tenantApi.getTenantById(SaaSContextHolder.currentTenantId());
        logger.info("tenantInfoResp->"+tenantInfoResp);
        logger.info("!AuditStatusEnum.Passed.equals(tenantInfoResp.getAuditStatus())->"+!AuditStatusEnum.Passed.equals(tenantInfoResp.getAuditStatus()));
        if(null == tenantInfoResp || null == tenantInfoResp.getAuditStatus() || AuditStatusEnum.Passed.getAuditStatus().intValue()!=tenantInfoResp.getAuditStatus()){
            throw new BusinessException(TenantExceptionEnum.AUDITSTATUS_NO_PASS);
        }
        req.setOperatorId(SaaSContextHolder.getCurrentUserId());
        Long userId = this.userApi.addSubCorpUser(req);
        UserToRolesDto userToRoleVo = new UserToRolesDto(SaaSContextHolder.getCurrentUserId(),userId,user.getRoleIds());
        permissionApi.addUserToRoles(userToRoleVo);
    }

    @Override
    public PageInfo<FetchUserResp> querySubUserList(SearchParam searchParam) {
        UserSearchReq req = new UserSearchReq();
        req.setPageNum(searchParam.getPageNum());
        req.setPageSize(searchParam.getPageSize());
        req.setTenantId(SaaSContextHolder.currentTenantId());
        PageInfo<FetchUserResp> pageInfo = userApi.querySubUserList(req);
        if(null == pageInfo.getList() || pageInfo.getList().size() == 0){
            return pageInfo;
        }
        List<FetchUserResp> fetchUserRespList = pageInfo.getList();
        List<Long> userList = new ArrayList<>();
        for(FetchUserResp fetchUserResp : fetchUserRespList){
            userList.add(fetchUserResp.getId());
        }
        Map<Long,List<RoleDto>> roleDtoMap = permissionApi.getBathUserRoles(userList);
        for(FetchUserResp fetchUserResp : fetchUserRespList) {
            List<RoleDto> roleDtoList = roleDtoMap.get(fetchUserResp.getId());
            // 角色名称字符串
            StringBuilder roleName = new StringBuilder();
            if (roleDtoList != null && roleDtoList.size() > 0) {
                Set<String> sets = roleDtoList.stream().map(RoleDto::getRoleName).collect(Collectors.toSet());
                if (sets != null && sets.size() > 0) {
                    Map<String, String> map = langApi.getLangValueByKey(sets, LocaleContextHolder.getLocale().toString());
                    for (RoleDto roleDto : roleDtoList) {
                        roleName.append(map.get(roleDto.getRoleName())).append(",");
                    }
                }
                if (roleName.length() > 0) {
                    roleName.deleteCharAt(roleName.length() - 1);
                }
            }
            fetchUserResp.setRoleName(roleName.toString());
        }
        return pageInfo;
    }

    /**
     * 描述：刷新凭证
     *
     * @param refreshToken 刷新令牌
     * @param terminalMark 终端类型
     * @return com.lds.iot.vo.corpuser.UserToken
     * @author nongchongwei
     * @created 2018/4/11 14:32
     */
    @Override
    public UserToken refreshUserToken(String refreshToken, String terminalMark) {
        UserTokenResp userToken = this.userApi.refreshUserToken(refreshToken, terminalMark, SystemTypeEnum.USER_PORTAL.getCode());
        UserToken userTokenResp = new UserToken();
        BeanUtils.copyProperties(userToken, userTokenResp);
        return userTokenResp;
    }

    /**
     * 描述：发送注册验证码
     *
     * @param email 账号
     * @return void
     * @author nongchongwei
     * @created 2018/4/8 16:30
     */
    @Override
    public void sendVerifyCode(String email, Byte type) {
    	String language = LocaleContextHolder.getLocale().toString();
        this.userApi.sendCorpVerifyCode(email, type, language);
    }

    /**
     * 描述：发送密码重置验证码
     *
     * @param email 账号
     * @return void
     * @author nongchongwei
     * @created 2018/4/8 16:30
     */
    @Override
    public void sendResetPasswordVerifyCode(String email) {
    	String language = LocaleContextHolder.getLocale().toString();
        this.userApi.sendVerifyCode(email, (byte) 2, language, null);
    }

    /**
     * 描述：密码错误3次，发送验证码
     *
     * @param email 账号
     * @return void
     * @author nongchongwei
     * @created 2018/4/8 16:30
     */
    @Override
    public void sendPasswordErrorVerifyCode(String email) {
    	String language = LocaleContextHolder.getLocale().toString();
        this.userApi.sendVerifyCode(email, (byte) 3, language, null);
    }

    /**
     * 描述：校验用户账号是否已注册
     *
     * @param
     * @return
     * @author 490485964@qq.com
     * @date 2018/7/4 16:06
     */
    @Override
    public Integer checkUserName(String userName) {
    	//参数校验
    	ValidateUtil.validateEmail(userName);
    	
        return userApi.checkUserName2B(userName);
    }


    @Override
    public String getVerifyCodeImage(String userName) {
        String verifyCode = userApi.getVerifyCodeImage(userName);
        String base64Image = "";
        // 生成图片
        int w = 100, h = 30;
        try {
            base64Image = VerifyCodeUtils.outputImage(w, h, verifyCode);
        } catch (Exception e) {
            logger.error("", e);
        }
        return base64Image;
    }

    /**
     * 描述：依据userId删除用户
     * @author maochengyuan
     * @created 2018/7/13 18:26
     * @param userId 用户id
     * @return int
     */
    @Override
    public int deleteUserByUserId(Long userId) {
        FetchUserResp user = this.userApi.getUser(userId);
        if(user == null){
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_EXIST);
        }
        if(user.getTenantId() == null || !user.getTenantId().equals(SaaSContextHolder.currentTenantId())){
            throw new BusinessException(BusinessExceptionEnum.USER_DOES_NOT_BELONG_THIS_TENANT);
        }
        return this.userApi.deleteUserByUserId(userId);
    }

    /**
     * 描述：给单个用户分配角色
     * @author maochengyuan
     * @created 2018/7/14 11:24
     * @param roleIds 角色id集合
     * @param userId 用户id
     * @return void
     */
    @Override
    public void AssignRolesToSingleUser(List<Long> roleIds, Long userId) {
        if(CommonUtil.isEmpty(roleIds)){
            throw new BusinessException(BusinessExceptionEnum.CHOOSE_AT_LEAST_ONE_ROLE);
        }
        /**找出用户并判断是否属于这个租户的2B用户*/
        FetchUserResp user = this.userApi.getUser(userId);
        if(user == null){
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_EXIST);
        }
        Long currentTenantId = SaaSContextHolder.currentTenantId();
        if(user.getTenantId() == null || !user.getTenantId().equals(currentTenantId)){
            throw new BusinessException(BusinessExceptionEnum.USER_DOES_NOT_BELONG_THIS_TENANT);
        }
        /**查询出2B所有角色*/
        List<RoleDto> list = this.permissionApi.getRoleByRoleType(RoleTypeEnum.TOB.getCode());
        if(CommonUtil.isEmpty(list)){
            return;
        }
        /**角色id取交集，防止篡改*/
        Set<Long> id = list.stream().collect(Collectors.toMap(RoleDto::getId, RoleDto::getId, (k1, k2) ->k1)).keySet();
        roleIds.retainAll(id);
        if(CommonUtil.isEmpty(list)){
            return;
        }
        UserToRolesDto vo = new UserToRolesDto(currentTenantId, userId, roleIds);
        this.permissionApi.addUserToRoles(vo);
    }

    /**
     * 描述：初始化用户角色信息
     * @author maochengyuan
     * @created 2018/7/14 14:37
     * @param userId 用户id
     * @return java.util.List<com.iot.portal.corpuser.vo.RoleInitResp>
     */
    @Override
    public RoleInitResp initUserRoleInfo(Long userId) {
        FetchUserResp user = this.userApi.getUser(userId);
        if(user == null){
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_EXIST);
        }
        if(user.getTenantId() == null || !user.getTenantId().equals(SaaSContextHolder.currentTenantId())){
            throw new BusinessException(BusinessExceptionEnum.USER_DOES_NOT_BELONG_THIS_TENANT);
        }
        /**查询出2B所有角色*/
        List<RoleDto> list = this.permissionApi.getRoleByRoleType(RoleTypeEnum.TOB.getCode());
        if(!CommonUtil.isEmpty(list)){
            Set<String> roleNames = list.stream().collect(Collectors.toMap(RoleDto::getRoleName, RoleDto::getRoleName, (k1, k2) ->k1)).keySet();
            Map<String, String> langMap = this.langApi.getLangValueByKey(roleNames, LocaleContextHolder.getLocale().toString());
            List<RoleResp> roleList = new ArrayList<>();
            list.forEach(o ->{
                roleList.add(new RoleResp(o.getId(), langMap.get(o.getRoleName())));
            });
            /**查询出用户拥有的角色id*/
            List<RoleDto> temp = this.permissionApi.getUserRoles(userId);
            Set<Long> roleIds = null;
            if(!CommonUtil.isEmpty(temp)){
                roleIds = temp.stream().collect(Collectors.toMap(RoleDto::getId, RoleDto::getId, (k1, k2) ->k1)).keySet();
            }
            return new RoleInitResp(roleList, roleIds);
        }
        return new RoleInitResp();
    }

    @Override
    public PageInfo<FetchUserResp> getUserPageList(UserSearchReq searchReq) {
        logger.info("==============getUserPageList==============");
        logger.info(searchReq.toString());
        return userApi.getUserPageList(searchReq);
    }

    @Override
    public PageInfo<FetchUserResp> getCorpUserPageList(UserSearchReq searchReq) {
        logger.info("==============getCorpUserPageList==============");
        logger.info(searchReq.toString());
        searchReq.setTenantId(SaaSContextHolder.currentTenantId());
        return userApi.getCorpUserPageList(searchReq);
    }

    public List<TenantReviewRecordInfoResp> getTenantReviewRecordByTenantId(){
    	Long tenantId = SaaSContextHolder.currentTenantId();
    	return tenantApi.getTenantReviewRecordByTenantId(tenantId);
    }
    
    /**
     * 
     * 描述：获取租户角色列表
     * @author 李帅
     * @created 2018年11月5日 下午3:57:49
     * @since 
     * @return
     */
    public List<RoleDto> getPortalRole(){
        List<RoleDto> roleDtoList = permissionApi.getRoleByRoleType(RoleTypeEnum.TOB.getCode());
        Set<String> keys=new HashSet<>();
        roleDtoList.forEach(e->{
            if (e.getRoleName()!=null)
                keys.add(e.getRoleName());
            if (e.getRoleDesc()!=null)
                keys.add(e.getRoleDesc());
        });
        Map<String,String> langMap=langApi.getLangValueByKey(keys,LocaleContextHolder.getLocale().toString());
        roleDtoList.forEach(e->{
            if (e.getRoleName()!=null)
                e.setRoleName(langMap.get(e.getRoleName()));
            if (e.getRoleDesc()!=null)
                e.setRoleDesc(langMap.get(e.getRoleDesc()));
        });
    	return roleDtoList;
    }

    /**
     * 描述：删除子账户
     * @author wucheng
     * @date 2018-11-06 11:15:13
     * @param userId 用户id
     * @return
     */
    @Override
    public int deleteSubUser(Long userId) {
        // 1.先删除该用户
        // 2.再删除该用户绑定的角色
        int result = 0;
        try {
            int deleteUser = this.userApi.deleteUserByUserId(userId);
            if (deleteUser > 0) {
                // 获取当前账号绑定的角色信息
                List<UserRoleRelate> results = this.permissionApi.getUserRoleRelateByUserId(userId);
                Long[] ids = new Long[results.size()];
                if (results != null && results.size() > 0) {
                    for (int i = 0; i < results.size(); i++) {
                        ids[i] = results.get(i).getId();
                    }
                }
                // 删除当前用户绑定角色信息
                if (ids != null && ids.length > 0) {
                    this.permissionApi.deleteUserRoleRelateById(ids);
                }
                result = 1;
            }
        } catch(Exception e) {
            logger.error("错误：" + e.getMessage());
            result = 0;
        }
        return result;
    }
    /**
     * 描述：修改子账户信息
     * @author wucheng
     * @param userId
     * @param roleIds 角色id,多个角色使用,隔开
     * @return
     */
    @Override
    public int editSubUser(String roleIds, Long userId) {
        try {
            if (userId == null) {
                throw new BusinessException(UserExceptionEnum.USERID_IS_NULL);
            }
            // 获取当前账号绑定的角色信息
            List<UserRoleRelate> results = this.permissionApi.getUserRoleRelateByUserId(userId);
            Long[] ids =  new Long[results.size()];
            if (results != null && results.size() > 0) {
                for (int i = 0; i < results.size();i++) {
                    ids[i] = results.get(i).getId();
                }
            }
            // 删除当前用户绑定角色信息
            if (ids != null && ids.length > 0) {
                this.permissionApi.deleteUserRoleRelateById(ids);
            }
            UserToRolesDto userToRoleVo = new UserToRolesDto();
            List<Long> roleLists = new ArrayList<>();
            String[] newRoleIds = roleIds.split(",");
            for (int i = 0; i < newRoleIds.length; i++) {
                roleLists.add(Long.parseLong(newRoleIds[i]));
            }
            userToRoleVo.setRoleIds(roleLists);
            userToRoleVo.setUserId(userId);
            userToRoleVo.setCreateId(SaaSContextHolder.getCurrentUserId());
            this.permissionApi.addUserToRoles(userToRoleVo);
            RedisCacheUtil.deleteBlurry(ModuleConstants.ROLE_INFO_USERID + userId);
            return 1;
        } catch (Exception e) {
            logger.error("错误信息：" + e.getMessage());
        }
        return 0;
    }

    @Override
    public int updatePasswordByUserId(Long userId, String newPassword, String oldPassword) {
        CommonServiceImpl.checkStringParam(newPassword, 0, 50, "en_us");
        Long  currentLoginTenantId =  SaaSContextHolder.currentTenantId();
        FetchUserResp user = this.userApi.getUser(userId);
        if (user != null) {
            if (currentLoginTenantId == user.getTenantId()) { // 表明是同一个租户
                if (MD5SaltUtil.verify(oldPassword, user.getPassword())) {
                    int result = this.userApi.updatePasswordByUserId(userId, MD5SaltUtil.generate(newPassword));
                    if (result > 0) {
                        return 1;
                    }
                } else {
                    throw new BusinessException(BusinessExceptionEnum.PASSWORD_IS_NOT_THE_SAME);
                }
            }
        }
        return 0;
    }
}
