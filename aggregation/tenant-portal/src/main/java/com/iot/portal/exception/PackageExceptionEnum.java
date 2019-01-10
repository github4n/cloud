package com.iot.portal.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称：cloud
 * 功能描述：套包异常
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum PackageExceptionEnum implements IBusinessException {
    /**套包参数是空的*/
    PACKAGE_PARAM_NULL("package.param.null"),
    /**套包不存在*/
    PACKAGE_NOT_EXIST("package.not.exists"),
    /**套包未选择关联产品*/
    PACKAGE_PRODUCT_NULL("package.product.null"),
    /**产品未审核通过*/
    PACKAGE_PRODUCT_NOT_AUDITPASS("package.product.notAuditPass"),
    /**套包不能使用*/
    PACKAGE_NOT_USED("package.can't.use"),
    /**套包设备类型未满足*/
    PACKAGE_DEVICE_TYPE_NOT_ENOUGH("package.deviceType.notEnough"),
    /**套包必须选择一个网关*/
    PACKAGE_MUST_CHOOSE_HUB("package.no.hub"),
    /**套包只能选择一个网关*/
    PACKAGE_ONLY_ONE_HUB("package.one.hub"),
    /**套包产品不支持ifttt*/
    PACKAGE_PRODUCT_NOT_IFTTT("package.product.noIfttt"),
    /**套包产品已添加*/
    PACKAGE_PRODUCT_HAD_ADD("package.product.hadAdd"),
    /**套包产品属性不存在*/
    PACKAGE_PROPERTY_PROPERTY_NOTEXIST("package.product.property.notExist"),
    /**套包产品方法不存在*/
    PACKAGE_PROPERTY_ACTION_NOTEXIST("package.product.action.notExist"),
    /**套包产品事件不存在*/
    PACKAGE_PROPERTY_EVENT_NOTEXIST("package.product.event.notExist"),
    /**安防套包设备类型不满足*/
    PACKAGE_SECURITY_DEVICE_NOT_ENOUGH("package.security.deviceType.notEnough"),
    /**套包不属于你（租户越权） */
    PACKAGE_TENANTID_ERROR("package.tenantId.error"),
    /**套包场景配置为空 */
    PACKAGE_SCENE_CONFIG_NULL("package.scene.configNull"),
    /**套包场景配置错误*/
    PACKAGE_SCENE_CONFIG_ERROR("package.scene.configError"),
    /**套包场景不存在*/
    PACKAGE_SCENE_NOT_EXIST("package.scene.notExist"),
    /**套包场景数量错误（超过最大值）*/
    PACKAGE_SCENE_NUM_ERROR("package.scene.numError"),
    /**套包策略数量错误（超过最大值）*/
    PACKAGE_RULE_NUM_ERROR("package.rule.numError"),
    /**套包策略不存在*/
    PACKAGE_RULE_NOT_EXIST("package.rule.notExist"),
    /**套包策略已存在*/
    PACKAGE_RULE_TYPE_EXIST("package.rule.exist");

    private String messageKey;

    PackageExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }
    
    @Override
    public String getMessageKey() {
        return messageKey;
    }

}