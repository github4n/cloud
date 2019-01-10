package com.iot.file.storage;

import com.iot.file.entity.FileBean;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：存储接口
 * 创建人： zhouzongwei
 * 创建时间：2018年3月12日 下午3:20:47
 * 修改人： zhouzongwei
 * 修改时间：2018年3月12日 下午3:20:47
 */
public interface IStorage {
	
	/**
	 * 
	 * 描述：获取单个预签名Put 类型的Url
	 * @author zhouzongwei
	 * @created 2018年3月9日 下午3:07:27
	 * @since 
	 * @param tenantId:租户id
	 * @param fileType：文件类型
	 * @return
	 */
    FileBean getPutUrl(Long tenantId,String fileType) throws Exception;
    
    /**
     * 
     * 描述：批量获取预签名Put 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:07:58
     * @since 
     * @param tenantId:租户id
	 * @param fileType：文件类型
     * @param urlNum：批量个数
     * @return
     */
    List<FileBean> getPutUrl(Long tenantId,String fileType,Integer urlNum) throws Exception;
    
    /**
     * 
     * 描述：获取单个预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:04
     * @since 
     * @param filePath:文件路径
     * @return
     */
    FileBean getGetUrl(String filePath) throws Exception;
    
    /**
     * 
     * 描述：批量获取预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:59
     * @since 
     * @param filePaths：文件路径集合
     * @return
     */
    List<FileBean> getGetUrl(List<FileBean> filePaths) throws Exception;
    
    /**
     * 
     * 描述：删除单个对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:11:09
     * @since 
     * @param filePath：文件路径
     */
    void deleteObject(String filePath) throws Exception;

    /**
     * 
     * 描述：批量删除对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:16:38
     * @since 
     * @param filePaths：文件路径集合
     */
    void deleteObject(List<String> filePaths) throws Exception;

    /**
	 * 
	 * 描述：单个上传文件到s3
	 * @author mao2080@sina.com
	 * @created 2017年4月11日 下午5:13:43
	 * @since 
	 * @param filePath 文件路径
	 * @param file 文件对象
	 * @return 文件存储路径
	 * @throws Exception
	 */
	String putObject(String filePath, File file, int expiration) throws Exception;
	/**
	 * 描述：单个上传文件到s3
	 * @author 490485964@qq.com
	 * @date 2018/4/11 11:35
	 * @param preFilePath 文件存储路径前缀
	 * @param file 待上传文件
	 * @param fileType 文件类型
	 * @return
	 */
	String putObject(String preFilePath, File file, String fileType) throws Exception;

	/**
	 * 描述：单个上传文件到s3
	 * @author 490485964@qq.com
	 * @date 2018/4/11 11:35
	 * @param absolutePath 文件完整路径，包含文件名（包含文件名 eg: 0/png/ac410b63761e4a8baceb7f6c5f0e9780.png）
	 * @param file 文件
	 * @return
	 */
	String putObject(String absolutePath, File file) throws Exception;

	/**
	  * @despriction：获取上传url
	  * @author  yeshiyuan
	  * @created 2018/8/2 11:19
	  * @param preFilePath 文件路径前缀
	  * @param fileType 文件类型
	  * @return
	  */
	FileBean getUploadUrl(String preFilePath, String fileType) throws Exception;

	/**
	  * @despriction：直接用流上传文件
	  * @author  yeshiyuan
	  * @created 2018/10/19 16:29
	  * @param preFilePath 文件路径前缀
	  * @param fileType 文件类型
	  * @return
	  */
	String putObject(String preFilePath, String fileType, InputStream inputStream) throws Exception;

	/**
	  * @despriction：直接用流上传文件
	  * @author  yeshiyuan
	  * @created 2018/10/19 16:11
	  * @param absolutePath 文件完整路径，包含文件名（包含文件名 eg: 0/png/ac410b63761e4a8baceb7f6c5f0e9780.png）
	  * @param inputStream 文件读取流
	  * @return
	  */
	String putObject(String absolutePath, InputStream inputStream) throws Exception ;

	/**
	 * 描述：获取对象md5
	 * @author nongchongwei
	 * @date 2018/11/5 16:50
	 * @param
	 * @return
	 */
	String getObjectContentMD5(String path) throws Exception ;
}
