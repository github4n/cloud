package com.iot.device.exception;


import com.iot.common.exception.IBusinessException;

public enum DeviceExceptionEnum implements IBusinessException {

	/***********************************设备相关 begin**************************************/

	DEVICE_ERROR("device.error"),
	DEVICE_ADD_ERROR("device.add.error"),
	DEVICE_NOT_EXIST("device.not.exist"),
	DEVICE_HAS_CHLID("device.haschild"),
	DEVICE_EXIST("device.exist"),
	DEVICE_BINDED("device.binded"),

	/***********************************设备相关 end**************************************/


	/*************** 设备对接 10700-10999  ***************************************/
	/**查询直连设备列表失败*/
	FIND_DIRECTDEVICE_LIST_FAILED("FIND.DIRECTDEVICE.LIST.FAILED"),
	/**查询非直连设备列表失败*/
	FIND_UNDIRECTDEVICE_LIST_FAILED(10701, "findUnDirectDeviceList failed"),
	/**根据id删除device失败*/
	DELETE_BYPRIMARYKEY_FAILED(10702, "deleteByPrimaryKey failed"),
	/**查询所有直连设备列表失败*/
	FIND_AllUNDIRECTDEVICE_LIST_FAILED(10704, "findAllUnDirectDeviceList failed"),
	/**更新设备类型失败*/
	UPDATE_DEVICE_TYPE_FAILED(10705, "updateDeviceType failed"),
	/** 参数deviceIp为空*/
	EMPTY_OF_DEVICE_IP(10707, "param deviceIp is empty"),
	/** 参数deviceIp为空*/
	EMPTY_OF_DEVICE_ERROR(10714, "param deviceIp is error"),
	/**添加设备失败*/
	ADD_DEVICE_FAILED(10708, "addDevice failed"),
	/**添加告警失败*/
	INSERT_WARNING_FAILED(10709, "insertWarning failed"),
	/**查询历史告警数据失败*/
	FIND_HISTORY_WARNINGLIST_FAILED(10710, "findHistoryWarningList failed"),

	/**查询设备业务类型列表失败*/
	FIND_DEVICECATEGORYLIST_FAILED(10712, "findDeviceCategoryList failed"),
	/**查询设备信息失败*/
	FIND_DEVICEBYIP_FAILED(10713, "findDeviceByIp failed"),
	/** 参数deviceIp为空*/
	EMPTY_OF_DEVICE_ISEXIST(10715, "device is exist"),
	/** 查询未读告警数据失败*/
	FIND_UNREAD_WARNINGLIST_FAILED(10716, "findUnreadWarningList failed"),
	/** 更新告警消息状态*/
	UPDATE_WARNING_STATUS_FAILED(10717, "updateWarningStatus failed"),
	/** 查询告警ID失败*/
	FIND_WARNING_ID_FAILED(10718, "findWarningId failed"),
	/** 有关联关系 删除失败*/
	DELETE_RELATION_DELETE_FAILED(10719, "have relation not delete"),
	/** 同步失败*/
	SYNCHRONIZATION_FAILED(10720, "synchronization failed"),
	/** 添加传感器条件失败*/
	ADD_SENSOR_CONDITION_FAILED(10721, "synchronization failed"),
	/** 设备已被用户绑定 */
	USER_DEVICE_BINDED(10722, "device binded"),

	/** 移除直连设备的子设备错误 */
	REMOVE_SUB_DEVICE_ERROR(10723, "remove sub device error."),

	/************************************（22001~23000）************************************/

	/**设备id为空**/
	EMPTY_OF_DEVICE_ID(22001, "deviceid is empty."),

	/**秘钥为空**/
	EMPTY_OF_SECURITY_KEY(22002, "securitykey is empty."),

	/**设备不存在**/
	NOT_EXISTS_DEVICE(22003, "device is not exits."),

	/**用户id为空**/
	EMPTY_OF_USER_ID(22004, "userid is empty."),

	/**绑定类型为空**/
	EMPTY_OF_BOUND_TYPE(22005, "boundtype is empty."),

	/**绑定类型有误**/
	ERROR_OF_BOUND_TYPE(22006, "boundtype is error."),

	/**设备密码为空**/
	EMPTY_OF_DEVICE_VCODE(22007, "device vcode is empty."),

	/**设备验证出错，设备密码有误**/
	ERROR_OF_DEVICE_VERTIFY(22008, "device vertify is fail, device vcode is error."),

	/**app token 为空**/
	EMPTY_OF_APP_TOKEN(22009, "app token is empty."),

	/**设备状态信息 为空**/
	EMPTY_OF_DEVICE_STATE(22010, "device state is empty."),

	/**设备记录时间为空**/
	EMPTY_OF_DEVICE_LOGDATE(22011, "device logdate is empty."),

	/**设备类型有误**/
	ERROR_OF_DEVICE_TYPE(22012, "device type is error."),

	/**房间关系为空**/
	EMPTY_OF_ROOMRALATIONSHIP(22013, "roomralationship is empty."),

	/**用户id不存在**/
	NOT_EXISTS_USER_ID(22014, "userid is not exits."),

	/**场景关系为空**/
	EMPTY_OF_SENCERALATIONSHIP(22015, "senceralationship is empty."),

	/**联动规则为空**/
	EMPTY_OF_RULE(22016, "rule is empty."),

	/**联动规则id为空**/
	EMPTY_OF_RULEID(22017, "ruleid is empty."),

	/**联动规则id不存在**/
	NOT_EXISTS_RULEID(22018, "ruleid is not exits."),

	/**设备名称为空**/
	EMPTY_OF_DEVICE_NAME(22019, "deviceName is empty."),

	/**用户与设备关系不存在*/
	COGNITO_USER_DEVICE_RELATIONSHIP_NOT_EXIST(22019, "User and device relationship does not exist."),

	/**获取设备token失败*/
	COGNITO_GET_DEVICE_TOKEN_FAILED(22020, "get device token failed."),

	/**获取credential失败*/
	COGNITO_GET_CREDENTIALS_FAILED(22021, "get credential failed."),

	/**厂商代码为空*/
	EMPTY_OF_VENDER_CODE(22022, "Vender code is empty."),

	/**app上报关系为空*/
	EMPTY_OF_APP_RELATIONSHIPINFO(22023, "app relationshipinfo is empty."),

	/**房间名称为空*/
	EMPTY_OF_ROOM_NAME(22024, "roomName is empty."),

	/**场景名称为空*/
	EMPTY_OF_SENCE_NAME(22024, "SenceName is empty."),

	/**客户id为空**/
	EMPTY_OF_CUST_ID(22025, "custId is empty."),

	/**客户id不存在**/
	NOT_EXISTS_CUST_ID(22026, "custId is not exits."),

	/**是否直连设备**/
	EMPTY_OF_ISDIRECTDEVICE(22027, "isDirectDevice is empty."),

	/**版本号**/
	EMPTY_OF_VERSIONCODE(22028, "versionCode is empty."),

	/**设备在线状态**/
	EMPTY_OF_ONLINESTATUS(22029, "onlineStatus is empty."),

	/**厂商简称**/
	EMPTY_OF_VENDERSHORT(22030, "venderShort is empty."),

	/**固件id**/
	EMPTY_OF_FWID(22031, "fwId is empty."),

	/**属性名称**/
	EMPTY_OF_PROPERTY_NAME(22032, "propertyName is empty."),

	/**属性描述**/
	EMPTY_OF_PROPERTY_DESC(22033, "propertyDesc is empty."),

	/**数据类型**/
	EMPTY_OF_DATA_TYPE(22034, "dataType is empty."),

	/**常量**/
	EMPTY_OF_REPORT_CONSTANT(22035, "reportConstant is empty."),

	/**流水记录**/
	EMPTY_OF_FLOWLOGSLIST(22036, "flowLogsJsonArray is empty."),

	/**报表名称**/
	EMPTY_OF_REPORT_NAME(22037, "reportName is empty."),

	/**已被另一个添加**/
	DEVICE_HAS_ADDED(22038, "device has added by someone else."),

	/*************************OTA Begin************************/
	/**升级版本号**/
	EMPTY_OF_UPGRADE_VERSION_CODE(22038, "upgradeVersionCode is empty."),
	/**替换版本号**/
	EMPTY_OF_REPLACED_VERSION_CODE(22039, "replacedVersionCode is empty."),
	/**设备型号**/
	EMPTY_OF_DEVICE_MODEL(22040, "deviceModel is empty."),
	/**升级方式**/
	EMPTY_OF_UPGRADE_TYPE(22041, "upgradeType is empty."),
	/**版本有效开始时间**/
	EMPTY_OF_BEGIN_TIME(22042, "beginTime is empty."),
	/**版本有效结束时间***/
	EMPTY_OF_END_TIME(22043, "endTime is empty."),
	/**版本升级计划id***/
	EMPTY_OF_UPGRADE_PLANID(22044, "upgradePlanId is empty."),
	/**版本升级文件url***/
	EMPTY_OF_FILE_URL(22045, "fileUrl is empty."),
	/**版本升级文件md5值***/
	EMPTY_OF_MD5CHECK(22046, "MD5Check is empty."),
	/**版本升级包类型***/
	EMPTY_OF_PACK_TYPE(22047, "PackType is empty."),
	/**升级结果代码***/
	EMPTY_OF_UPGRADE_RESULT_CODE(22048, "UpgradeResultcode is empty."),
	/**版本升级结果描述***/
	EMPTY_OF_UPGRADE_RESULT_ERRMSG(22049, "UpgradeResultErrMsg is empty."),
	/*************************OTA End***************************/

	/**用户类型***/
	EMPTY_OF_USER_TYPE(22050, "userType is empty."),
	/**校验用户是否该设备的住账户***/
	ERROR_OF_USER_NOT_ROOT(22051, "user is not root."),
	/**用户类型有误***/
	ERROR_OF_USER_TYPE(22052, "userType is error."),
	/**设备已经被绑定过***/
	DEVICE_HAS_BEEN_BINDED(22053, "device has been binded."),
	/**分享临时密码***/
	EMPTY_OF_SHARE_TEM_PASS(22054, "tempPassword is empty."),
	/**分享临时密码已经过期***/
	SHARE_TEM_PASS_HAS_BEEN_EXPIRED(22055, "tempPassword has been expired."),
	/**分享临时密码有误***/
	ERROR_OF_SHARE_TEM_PASS(22056, "tempPassword is error."),
	/**解绑设备id***/
	EMPTY_OF_UNBIND_DEVICEID(22057, "unbindDevId is empty."),
	/**解绑用户id***/
	EMPTY_OF_UNBIND_USERID(22058, "unbindUserId is empty."),
	/**设备解绑失败***/
	FAILURE_OF_DEVICE_UNBIND(22059, "device UnBind fail."),
	/**获取设备基本信息列表失败***/
	FAILURE_OF_GET_DEVICE_BASEINFOLIST(22060, "get Device BaseInfoList fail."),
	/**获取设备基本信息失败***/
	FAILURE_OF_GET_DEVICE_BASEINFO(22061, "get Device BaseInfo fail."),
	/**获取设备基本配置信息失败***/
	FAILURE_OF_GET_DEVICE_BASECONFIGURE(22062, "get Device BaseConfigure fail."),
	/**设备绑定失败***/
	FAILURE_OF_DEVICE_BIND(22063, "device Bind fail."),
	/**上报设备基本信息失败***/
	FAILURE_OF_UPDATE_DEVICE_BASEINFO(22064, "update Device BaseInfo fail."),
	/**设备分享失败***/
	FAILURE_OF_DEVICE_SHARE(22065, "device Share fail."),
	/**设备解绑确认失败***/
	FAILURE_OF_DEVICE_UNBIND_CONFIRM(22066, "device UnBind Confirm fail."),
	/**通过设备id获取子用户失败***/
	FAILURE_OF_QUERY_SUBUSER_BY_DEVIID(22067, "query SubUser fail By DeviId."),
	/**查询是否root用户失败***/
	FAILURE_OF_QUERY_IS_ROOTUSER(22068, "query isRootUser fail."),
	/**设备类型***/
	EMPTY_OF_DEVICE_TYPE(22069, "devicetype is empty."),
	/**查询未绑定计划的设备列表***/
	FAILURE_OF_GET_UNBINDPLAN_DEVICELIST(22070, "get UnBindPlan DeviceList fail."),
	/**计划id***/
	EMPTY_OF_PLAN_ID(22071, "planId is empty."),
	/**事件id***/
	EMPTY_OF_EVENT_ID(22072, "eventId is empty."),
	/**事件触发时间***/
	EMPTY_OF_EVENT_ODDUR_TIME(22073, "eventOddurTime is empty."),
	/**事件上报失败***/
	FAILURE_OF_ADD_EVENT(22074, "add Event fail."),
	/**文件类型***/
	EMPTY_OF_FILE_TYPE(22075, "fileType is empty."),
	/**获取FS预签名URL失败***/
	FAILURE_OF_GET_FSURL(22076, "getfsurl fail."),
	/**增加视频文件信息***/
	FAILURE_OF_ADD_VIDEO_FILE(22077, "addVideoFile fail."),
	/**文件名称***/
	EMPTY_OF_FILE_NAME(22078, "fileName is empty."),
	/**视频开始时间***/
	EMPTY_OF_VEDIO_START_TIME(22079, "VedioStartTime is empty."),

	/**设备绑定秘钥失败***/
	FAILURE_OF_DEVICE_BOUND_SECURITYKEY(22080, "deviceBoundSecurityKey fail."),
	/**设备秘钥加密***/
	FAILURE_OF_DEVICE_ENCODE_AES(22081, "deviceEncodeAES fail."),
	/**设备秘钥解密***/
	FAILURE_OF_DEVICE_DECODE_AES(22082, "deviceDecodeAES fail."),
	/**设备上报自身状态或网关上报子设备状态***/
	FAILURE_OF_DEVICE_UPLOAD_STATE(22083, "deviceUploadState fail."),
	/**直连设备解除绑定关系***/
	FAILURE_OF_UNBOUND_RELATIONSHIP(22084, "unBoundRelationship fail."),
	/**设备激活***/
	FAILURE_OF_ACTIVE_DEVICE(22085, "activeDevice fail."),
	/**设备验证***/
	FAILURE_OF_VERTIFY_DEVICE(22086, "vertifyDevice fail."),
	/**APP绑定秘钥***/
	FAILURE_OF_APP_BOUND_SECURITYKEY(22087, "appBoundSecurityKey fail."),
	/**app秘钥加密***/
	FAILURE_OF_APP_ENCODE_AES(22088, "appEncodeAES fail."),
	/**APP秘钥解密***/
	FAILURE_OF_APP_DECODE_AES(22089, "appDecodeAES fail."),
	/**根据用户id查找设备信息***/
	FAILURE_OF_QUERY_DEVICEINFO_BYUSERID(22090, "queryDeviceInfoByUserId fail."),
	/**通过设备id查找设备属性***/
	FAILURE_OF_QUERY_DEVICEPROPERTY_BYDEVICEID(22091, "queryDevicePropertyByDeviceId fail."),
	/**编辑设备信息***/
	FAILURE_OF_UPDATE_DEVICE(22092, "updateDevice fail."),
	/**查询设备信息***/
	FAILURE_OF_QUERY_DEVICE(22093, "queryDevice fail."),
	/**通过locationId查询设备列表***/
	FAILURE_OF_QUERY_DEVICE_BYLOCATIONID(22094, "queryDeviceByLocationId fail."),
	/**app上报绑定关系***/
	FAILURE_OF_UPLOAD_BOUND_RELATIONSHIP(22095, "uploadBoundRelationship fail."),
	/**根据设备id获取设备信息***/
	FAILURE_OF_QUERY_DEVICEINFO_BYDEVIID(22096, "queryDeviceInfoByDeviId fail."),
	/**通过用户id获取设备最新版本信息列表***/
	FAILURE_OF_GET_DEVICEVERSIONLIST_BYUSERID(22097, "getDeviceVersionListByUserId fail."),
	/**获取某个设备的某个版本信息***/
	FAILURE_OF_GET_DEVICEVERSIONINFO_BYDEVICEID(22098, "getDeviceVersionInfoByDeviceId fail."),
	/**更新设备在线状态***/
	FAILURE_OF_UPDATE_DEVICE_ONLINE_STATUS(22099, "updateDeviceOnlineStatus fail."),
	/**通过厂商简称获取厂商代码***/
	FAILURE_OF_GET_VENDERCODE_BYVENDERSHORT(22100, "getVenderCodeByVenderShort fail."),
	/**根据固件id获取固件版本***/
	FAILURE_OF_GET_FIRMWARE_VERSION(22101, "getFirmWareVersion fail."),
	/**更新ota文件url***/
	FAILURE_OF_UPLOAD_OTA_FILE(22102, "uploadOtaFile fail."),
	/**添加设备自定义属性***/
	FAILURE_OF_ADD_DEVICE_REPORT_PROPERTY(22103, "addDeviceReportProperty fail."),
	/**上报设备报表流水***/
	FAILURE_OF_UPLOAD_DEVICE_REPORT_FLOW(22104, "uploadDeviceReportFlow fail."),
	/**按自定义时间查询报表流水***/
	FAILURE_OF_QUERY_REPORT_FLOW(22105, "queryReportFlow fail."),

	/**新增设备升级版本信息***/
	FAILURE_OF_ADD_DEVICE_UPGRADE_VERSION(22106, "addDeviceUpgradeVersion fail."),
	/**创建版本升级计划***/
	FAILURE_OF_ADD_DEVICE_UPGRADE_PLAN(22107, "addDeviceUpgradePlan fail."),
	/**编辑设备升级计划***/
	FAILURE_OF_UPDATE_DEVICE_UPGRADE_PLAN(22108, "updateDeviceUpgradePlan fail."),
	/**Device 发起版本升级通知***/
	FAILURE_OF_UPGRADE_VERSION_NOTICE(22109, "upgradeVersionNotice fail."),
	/**获取设备升级详情***/
	FAILURE_OF_GET_DEVICE_UPGRADE_DETAIL(22110, "getDeviceUpgradeDetail fail."),
	/**记录升级结果日志***/
	FAILURE_OF_UPLOAD_UPGRADE_RESULT(22111, "uploadUpgradeResult fail."),
	/**获取新版本信息***/
	FAILURE_OF_GET_NEW_UPGRADE_VERSIONINFO(22112, "getNewUpgradeVersionInfo fail."),

	/**根据产品id获取用户信息***/
	FAILURE_OF_GET_APPUSERLIST_BYPROID(22113, "getAppUserListByProId fail."),
	/**根据客户id查找所有产品信息***/
	FAILURE_OF_GET_PRODUCT_INFO(22114, "getProductInfo fail."),
	/**获取产品属性***/
	FAILURE_OF_GET_PRODUCTPROPERTY_BYDEVICEID(22115, "getProductPropertyByDeviceId fail."),

	/**deviceId**/
	DEVICE_IS_EMPTY(22116, "device is empty."),
	/**getfsurlHttps**/
	GETFSURLHTTPS_IS_EMPTY(22117, "getfsurlHttps is empty."),

	GET_DEVICECATEGORYLIST_FAILED("get.device.category.list.failed"),

	//*************** DEVICE DATA POINT 20000-20099  ********************************************
	EMPTY_OF_DEVICE_DATA_POINT_OBJ(20000, "device data point object is empty"),
	//** 参数deviceName为空
	EMPTY_OF_DEVICE_DATA_POINT_NAME(20001, "device data point name is empty"),
	//**catalog name 重复
	DEVICE_PROPERTY_CODE_REPETITION(20002, "device property code is repetition"),
	//** 参数mode为空
	EMPTY_OF_DEVICE_DATA_POINT_MODE(20003, "device data point mode is empty"),
	//** 参数data type为空
	EMPTY_OF_DEVICE_DATA_TYPE(20004, "device data type is empty"),
	//** 参数data type为空
	EMPTY_OF_DEVICE_DATA_POINT_PROPERTY(20005, "device data point property is empty"),
	//** 参数id为空
	EMPTY_OF_DEVICE_DATA_POINT_ID(20006, "device data point id is empty"),
	//** primary id 不存在
	DEVICE_DATA_POINT_ID_IS_INEXISTENCE(20007, "device data point id is inexistence"),
	//**增加device data point 失败
	ADD_DEVICE_DATA_POINT_FAILED(20008, "add device data point failed"),
	//**删除device data point 失败
	DELETE_DEVICE_DATA_POINT_FAILED(20009, "delete device data point failed"),
	//**发现device data point 失败
	FIND_DEVICE_DATA_POINT_FAILED(20010, "find device data point failed"),
	//**device data point 更新失败
	UPDATE_DEVICE_DATA_POINT__FAILED(20011,"update Device data point failed"),

	//*************** DEVICE TYPE DATA POINT 20100-20199  ********************************************
	//**对象为空
	EMPTY_OF_DEVICE_TYPE_DATA_POINT_OBJ(20100, "device type data point object is empty"),
	//**device type data point 重复
	DEVICE_TYPE_DATA_POINT_REPETITION(20101, "device type data point is repetition"),
	//**primary id 不存在
	DEVICE_TYPE_DATA_POINT_ID_IS_INEXISTENCE(20102, "device type data point id is inexistence"),
	//** 参数id为空
	EMPTY_OF_DEVICE_TYPE_DATA_POINT_ID(20103, "device type data point id is empty"),
	//**增加device type data point 失败
	ADD_DEVICE_TYPE_DATA_POINT_FAILED(20104, "add device data type point failed"),
	//**删除device type data point失败
	DELETE_DEVICE_TYPE_DATA_POINT_FAILED(20105, "delete device type data point failed"),
	//**发现device type data point 失败
	FIND_DEVICE_TYPE_DATA_POINT_FAILED(20106, "find device type data point failed"),
	//**device type data point 更新失败
	UPDATE_DEVICE_TYPE_DATA_POINT_FAILED(20107, "update Device type data point failed"),

	//*************** PRODUCT DATA POINT 20200-20299  ********************************************
	//**对象为空
	EMPTY_OF_PRODUCT_DATA_POINT_OBJ(20200, "product data point object is empty"),
	//**product data point 重复*//*
	PRODUCT_DATA_POINT_REPETITION(20201, "product data point is repetition"),
	//**primary id 不存在  *//*
	PRODUCT_DATA_POINT_ID_IS_INEXISTENCE(20202, "product data point id is inexistence"),
	//** 参数id为空*//*
	EMPTY_OF_PRODUCT_DATA_POINT_ID(20203, "device type data point id is empty"),
	//**增加device type data point 失败  *//*
	ADD_PRODUCT_DATA_POINT_FAILED(20204, "add product data point failed"),
	//**删除device type data point失败  *//*
	DELETE_PRODUCT_DATA_POINT_FAILED(20205, "delete product data point failed"),
	//**发现device type data point 失败  *//*
	FIND_PRODUCT_DATA_POINT_FAILED(20206, "find product data point failed"),
	//**device type data point 更新失败*//*
	UPDATE_PRODUCT_DATA_POINT_FAILED(20207,"update product data point failed"),

	//*************** DEVICE CATALOG/TYPE 11800-11899  ********************************************//*
	EMPTY_OF_DEVICE_CATALOG_OBJ(11800, "device catalog object is empty"),
	//** 参数deviceName为空*//*
	EMPTY_OF_DEVICE_CATALOG_NAME(11801, "device name is empty"),
	//** 参数tenant_id为空*//*
	EMPTY_OF_DEVICE_CATALOG_TENANT_ID(11802, "device catalog tenant_id is empty"),
	//** 参数id为空*//*
	EMPTY_OF_DEVICE_CATALOG_ID(11803, "device catalog id is empty"),
	//**catalog name 重复*//*
	CATALOG_NAME_REPETITION(11804, "catalog name is repetition"),
	//**catalog tenant_id 不能改变*//*
	CATALOG_TENANT_ID_CHANGE(11805, "device catalog tenant_id cant change"),
	//**primary id 不存在  *//*
	DEVICE_CATALOG_ID_IS_INEXISTENCE(11806, "device catalog id is inexistence"),
	//**增加device catalog 失败  *//*
	ADD_DEVICE_CATALOG_FAILED(11807, "add device catalog failed"),
	//**删除device catalog 失败  *//*
	DELETE_DEVICE_CATALOG_FAILED(11808, "delete device catalog failed"),
	//**发现device catalog 失败  *//*
	FIND_DEVICE_CATALOG_FAILED(11809, "find device catalog failed"),
	//**device catalog 更新失败*//*
	UPDATE_DEVICE_CATALOG__FAILED(118010,"updateDeviceCatalog failed"),

	//*************** DEVICE TYPE 11900-11999  ********************************************//*
	EMPTY_OF_DEVICE_TYPE_OBJ(11900, "device type object is empty"),
	//** 参数deviceName为空*//*
	EMPTY_OF_DEVICE_TYPE_NAME(11901, "device name is empty"),
	//** 参数tenant_id为空*//*
	EMPTY_OF_DEVICE_TYPE_TENANT_ID(11902, "device type tenant_id is empty"),
	//**catalog name 重复*//*
	TYPE_NAME_REPETITION(11903, "type name is repetition"),
	//**catalog tenant_id 不能改变*//*
	TYPE_TENANT_ID_CHANGE(11904, "device type tenant_id cant change"),
	//**primary id 不存在  *//*
	DEVICE_TYPE_ID_IS_INEXISTENCE(11905, "device type id is inexistence"),
	//** 参数id为空*//*
	EMPTY_OF_DEVICE_TYPE_ID(11906, "device type id is empty"),
	//**增加device catalog 失败  *//*
	ADD_DEVICE_TYPE_FAILED(11907, "add device type failed"),
	//**删除device catalog 失败  *//*
	DELETE_DEVICE_TYPE_FAILED(11908, "delete device type failed"),
	//**发现device catalog 失败  *//*
	FIND_DEVICE_TYPE_FAILED(11909, "find device type failed"),
	//**device catalog 更新失败*//*
	UPDATE_DEVICE_TYPE__FAILED(119010,"updateDevicetype failed"),

	
	CUDTCODE_IS_NULL("custCode is null."),
	CUDTNAME_IS_NULL("custName is null."),
	UUIDTYPE_IS_NULL("UuidType is null."),
	PRODUCTID_IS_NULL("productId is null."),
	UUIDAMOUNT_EQUAL_ZERO("UuidAmount less than or equal to zero."),
	UUIDVALIDITYDAYS_EQUAL_ZERO("UuidValidityDays less than zero."),
	CA_MD5_ABNORMAL("Abnormal MD5 value of CA certificate."),
	GENERATEUUID_FAILED("generateUUID failed."),
	NOT_ENOUGH_P2PID("We don't have enough P2PID."),
	UUIDMARK_IS_NULL("uuidMark is null."),
	TENANTID_IS_NULL("tenantId is null."),
	USERID_IS_NULL("userId is null."),
	BATCHNUM_IS_ERROR("batchNum is error."),
	CERT_NOT_EXIST("The batch certificate does not exist."),
	NO_PERMISSION("No download permission."),
	CERT_BEING("Certificate is being generated."),
	GENERATION_FAILED("Certificate generation failed."),
	FILEID_NOT_EXIST("The fileID does not exist."),
	GETDOWNLOADUUID_FAILED("getDownloadUUID failed."),

	ORDER_NOT_PAID("The order has not been paid."),
	GETUUIDINFO_FAILED("getUUIDInfo failed."),
	GETUUIDOrder_FAILED("getUUIDOrder failed."),
	FAILED_DEC_CA_FILE("Failed to decrypt CA file."),
	
	UUIDREFUNDREQ_IS_NULL("uuidRefundReq is null."),
	BEFOREUUIDREFUND_IS_ERROR("beforeUUIDRefund is error."),
	REFUNDSUCCESS_IS_ERROR("refundSuccess is error."),
	REFUNDFAIL_IS_ERROR("refundFail is error."),
	ORDER_NOT_EXIST("The order does not exist."),
	ORDER_CANNOT_REFUND("This order cannot be refunded."),
	ORDER_NOTIN_REFUNDLIST("The order is not on the refund list."),
	BATCHNUM_IS_NULL("batchNum is null."),
	ORDERID_IS_NULL("orderId is null.");

	private int code;

	private String messageKey;

	DeviceExceptionEnum(String messageKey) {
		code = 0;
		this.messageKey = messageKey;
	}

	DeviceExceptionEnum(int code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

}
