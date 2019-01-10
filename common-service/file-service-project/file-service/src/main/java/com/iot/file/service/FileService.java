package com.iot.file.service;

import com.iot.common.exception.BusinessException;
import com.iot.file.dto.FileDto;
import com.iot.file.entity.FileBean;
import com.iot.file.vo.FileInfoResp;
import com.iot.file.vo.NeedReturnFileInfoReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface FileService {

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
	FileDto getPutUrl(Long tenantId,String fileType) throws BusinessException;
    
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
    List<FileDto> getPutUrl(Long tenantId,String fileType,Integer urlNum) throws BusinessException;
    
    /**
     * 
     * 描述：获取单个预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:04
     * @since 
     * @param fileId:文件id
     * @return
     */
    FileDto getGetUrl(String fileId) throws BusinessException;
    
    /**
     * 
     * 描述：批量获取预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:59
     * @since 
     * @param fileIds：文件id集合
     * @return
     */
	Map<String, String> getGetUrl(List<String> fileIds) throws BusinessException;
    
    /**
     * 
     * 描述：删除单个对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:11:09
     * @since 
     * @param fileId：文件id
     */
    int deleteObject(String fileId) throws BusinessException;

	/**
	 * 描述：获取文件md5
	 * @author nongchongwei
	 * @date 2018/11/5 16:46
	 * @param
	 * @return
	 */
	String getObjectContentMD5(String fileId) throws BusinessException;
    /**
     * 
     * 描述：批量删除对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:16:38
     * @since 
     * @param fileIds：文件id集合
     */
    int deleteObject(List<String> fileIds) throws BusinessException;

	/**
	 * 描述：根据路径删除文件 用于删除数据库没有存储数据的的文件 不删除数据库数据
	 * @author 490485964@qq.com
	 * @date 2018/5/25 14:01
	 * @param  path 文件在云上的路径
	 * @return
	 */
	int deleteObjectByPath(String path) throws BusinessException;

	/**
	 * 描述：批量根据路径删除文件 用于删除数据库没有存储数据的的文件 不删除数据库数据
	 * @author 490485964@qq.com
	 * @date 2018/5/25 14:01
	 * @param pathList 文件在云上的路径集合
	 * @return
	 */
	int deleteObjectByPath(List<String> pathList) throws BusinessException;

    


	/**
	 * 描述：文件上传
	 * @author 490485964@qq.com
	 * @date 2018/4/11 10:17
	 * @param
	 * @return
	 */
	String upLoadFile(MultipartFile file, Long tenantId) throws BusinessException;

	/**
	  * @despriction：修改文件
	  * @author  yeshiyuan
	  * @created 2018/4/24 10:37
	  * @param file 文件
	  * @param fileId 文件uuid
	  * @param filePath 文件路径
	  * @return
	  */
	void updateFile(MultipartFile file,String fileId,String filePath) throws BusinessException;

	/**
	 * @despriction：通过文件uuid获取文件信息
	 * @author  yeshiyuan
	 * @created 2018/4/24 10:54
	 * @param fileId 文件uuid
	 * @return
	 */
	FileBean getFileInfoByFileId(String fileId);

	/**
	 * @despriction：从redis中获取fileId进而删除s3和数据库的数据
	 * @author  yeshiyuan
	 * @created 2018/6/5 14:40
	 * @param redisTaskId
	 * @return
	 */
	void deleteFileByRedisTaskId(String redisTaskId);


	/**
	 * @despriction：文件上传并获取到文件的MD5
	 * @author  yeshiyuan
	 * @created 2018/8/3 20:03
	 * @param null
	 * @return
	 */
	FileInfoResp upLoadFileAndGetMd5(MultipartFile file, Long tenantId);

	/**
	  * @despriction：查找文件路径
	  * @author  yeshiyuan
	  * @created 2018/8/21 14:47
	  * @param null
	  * @return
	  */
	Map<String, String> batchGetFilePath(List<String> fileIds);

	/**
	 * @despriction：上传文件，并根据参数返回相关字段
	 * @author  yeshiyuan
	 * @created 2018/8/3 20:15
	 * @param null
	 * @return
	 */
	FileInfoResp uploadFile(MultipartFile multipartFile, Long tenantId, NeedReturnFileInfoReq needReq);

	/**
	 * @despriction：上传文件，并根据参数返回相关字段(文件信息是否需要保存至数据库)
	 * @author  yeshiyuan
	 * @created 2018/8/3 20:15
	 * @param null
	 * @return
	 */
	FileInfoResp uploadFile(MultipartFile multipartFile, Long tenantId, NeedReturnFileInfoReq needReq, boolean saveInfoToDb);

	/**
	 * @despriction：把redis中的文件信息保存至数据库
	 * @author  yeshiyuan
	 * @created 2018/10/9 19:10
	 * @return
	 */
	void saveFileInfoToDb(String fileId);

	/**
	 * @despriction：批量把redis中的文件信息保存至数据库
	 * @author  yeshiyuan
	 * @created 2018/10/9 19:10
	 * @return
	 */
	void saveFileInfosToDb(List<String> fileIds);

	/**
	 * @despriction：获取下载验证码（有效期单位秒）
	 * @author  yeshiyuan
	 * @created 2018/11/28 18:01
	 */
	 String getDownloadCode(Integer second);

	/**
	 * @despriction：检验下载验证码是否过期
	 * @author  yeshiyuan
	 * @created 2018/11/28 19:11
	 */
	 boolean checkDownloadCode(String uuid);
}
