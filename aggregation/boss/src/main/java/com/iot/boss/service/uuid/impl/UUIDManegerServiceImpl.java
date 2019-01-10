package com.iot.boss.service.uuid.impl;

import com.iot.boss.service.uuid.UUIDManegerService;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.api.UUIDManegerApi;
import com.iot.device.api.UuidApplyRecordApi;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.exception.UuidExceptionEnum;
import com.iot.device.vo.req.LicenseUsageReq;
import com.iot.device.vo.req.UserLoginReq;
import com.iot.device.vo.req.uuid.GenerateUUID;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.UuidApplyForBoss;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.enums.AdminStatusEnum;
import com.iot.user.enums.UserLevelEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：租户管理实现类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/22 17:04
 */
@Service("uuidManegerService")
public class UUIDManegerServiceImpl implements UUIDManegerService {

    @Autowired
    private UUIDManegerApi uuidManegerApi;
    
    @Autowired
    private UuidApplyRecordApi uuidApplyRecordApi;

    @Autowired
    private UserApi userApi;
    
    /**
     * 
     * 描述：申请生成uuid
     * @author 李帅
     * @created 2018年5月25日 下午5:48:42
     * @since 
     * @param uuidApplyForBoss
     */
    @Override
    public Long generateUUID(UuidApplyForBoss uuidApplyForBoss) {
        //在Header中获取数据
        Long userId = SaaSContextHolder.getCurrentUserId();
//        Long tenantId = SaaSContextHolder.currentTenantId();
        //校验用户是否有权限申请uuid
        checkUserHadRight(userId);
//        uuidApplyForBoss.setTenantId(tenantId);
        uuidApplyForBoss.setUserId(userId);
        return uuidApplyRecordApi.uuidApplyForBoss(uuidApplyForBoss);
    }
    
    /**
     * 
     * 描述：UUID生成列表
     * @author 李帅
     * @created 2018年5月25日 下午5:48:36
     * @since 
     * @param uuidOrderReq
     * @return
     */
    @Override
    public Page<UUIDOrderDto> getUUIDGenerateList(GetUUIDOrderReq uuidOrderReq) {
        return uuidManegerApi.getUUIDOrder(uuidOrderReq);
    }
    
    /**
     * 
     * 描述：通过批次号获取证书下载URL
     * @author 李帅
     * @created 2018年6月8日 下午1:51:26
     * @since 
     * @param batchNum
     * @return
     */
    @Override
    public String getDownloadUUID(Long batchNum) {
    	//在Header中获取数据
        String userUuid = SaaSContextHolder.getCurrentUserUuid();
//        Long tenantId = SaaSContextHolder.currentTenantId();
        //校验用户是否有权限申请uuid
        Long userId = SaaSContextHolder.getCurrentUserId();
        checkUserHadRight(userId);
        GenerateUUID uuidInfo = uuidManegerApi.getGenerateUUIDInfo(batchNum);
        if(uuidInfo == null){
            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_NOT_EXIST);
        }
        Long tenantId = uuidInfo.getTenantId();
        return uuidManegerApi.getDownloadUUID(tenantId, userUuid, batchNum);
    }

    /**
     *
     * 描述：管理员登陆
     * @author 李帅
     * @created 2018年6月8日 下午2:37:22
     * @since
     * @param userLoginReq
     * @throws Exception
     */
    @Override
    public void userLogin(UserLoginReq userLoginReq) {
    	if(userLoginReq != null && userLoginReq.getTenantId() != 0
    			&& "root".equals(userLoginReq.getPassWord()) && "root".equals(userLoginReq.getUserName())) {

        } else {
    		try {
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }

    /**
     * 描述：上报总装测试数据
     *
     * @param licenseUsageReq
     * @author chq
     * @created 2018年6月28日 下午2:37:31
     * @since
     */
    @Override
    public void licenseUsage(LicenseUsageReq licenseUsageReq) throws BusinessException {
        uuidManegerApi.licenseUsage(licenseUsageReq);
    }

    public void checkUserHadRight(Long userId){
        //校验用户是否有权限
        FetchUserResp user = userApi.getUser(userId);
        if (user == null || UserLevelEnum.BOSS.getCode() != user.getUserLevel()) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        if(AdminStatusEnum.SUPER.getCode() != user.getAdminStatus()){
            throw new BusinessException(UserExceptionEnum.USER_DO_NOT_HAVE_PERMISSION);
        }
    }
}
