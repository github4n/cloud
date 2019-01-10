package com.iot.file.contants;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：文件常量
 * 创建人： zhouzongwei
 * 创建时间：2018年3月13日 下午3:07:57
 * 修改人： zhouzongwei
 * 修改时间：2018年3月13日 下午3:07:57
 */
public class ModuleConstants {

	/** 斜杠 */
	public static final String SPRIT = "/";

	/** 点 */
	public static final String POINT = ".";

	/** 成功代码 */
	public static final int SUCCESS = 200;

	/** 过期时间 */
	public static int expiration = 24;

	/** 分布式Id redis Key */
	public static final String DB_TABLE_FILE_INFO = "max-row-id:file_info";

	/** 租户ID加密  AES KEY */
	public static final String AES_KEY = "F6us32872gs7fr#Bt@89hnkG==|[nu8hgvV76v7fC%^$$*^&%436gwGVC==";

	/** 批量删除数量 */
	public static final int batchDelNum = 500;

}
