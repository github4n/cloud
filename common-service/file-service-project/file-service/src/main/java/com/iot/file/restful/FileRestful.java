package com.iot.file.restful;

import com.iot.common.exception.BusinessException;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.file.entity.FileBean;
import com.iot.file.service.FileService;
import com.iot.file.vo.FileInfoResp;
import com.iot.file.vo.NeedReturnFileInfoReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
public class FileRestful implements FileApi {

    @Autowired
    private FileService fileService;

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
	@Override
	public FileDto getPutUrl(@RequestParam("tenantId")Long tenantId,@RequestParam("fileType") String fileType) throws BusinessException{
		return fileService.getPutUrl(tenantId, fileType);
	}

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
	@Override
	public List<FileDto> getPutUrl(@RequestParam("tenantId")Long tenantId,@RequestParam("fileType") String fileType, @RequestParam("urlNum")Integer urlNum) throws BusinessException{
		return fileService.getPutUrl(tenantId, fileType, urlNum);
	}

	 /**
     * 
     * 描述：获取单个预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:04
     * @since 
     * @param fileId:文件id
     * @return
     */
	@Override
	public FileDto getGetUrl(@RequestParam("fileId")String fileId) throws BusinessException{
		return fileService.getGetUrl(fileId);
	}

	  /**
     * 
     * 描述：批量获取预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:59
     * @since 
     * @param fileIds：文件id集合
     * @return
     */
	@Override
	public Map<String, String> getGetUrl(@RequestParam("fileIds")List<String> fileIds) throws BusinessException{
		return fileService.getGetUrl(fileIds);
	}

	 /**
     * 
     * 描述：删除单个对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:11:09
     * @since 
     * @param fileId：文件id
     */
	@Override
	public int deleteObject(@RequestParam("fileId")String fileId) throws BusinessException{
		return fileService.deleteObject(fileId);
	}

	/**
     * 
     * 描述：批量删除对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:16:38
     * @since 
     * @param fileIds：文件id集合
     */
	@Override
	public int deleteObject(@RequestParam("fileIds")List<String> fileIds) throws BusinessException{
		return fileService.deleteObject(fileIds);
	}

	/**
	 * 描述：根据路径删除文件 用于删除数据库没有存储数据的的文件 不删除数据库数据
	 * @author 490485964@qq.com
	 * @date 2018/5/25 14:01
	 * @param  path 文件在云上的路径
	 * @return
	 */
	@Override
	public int deleteObjectByPath(@RequestParam("path")String path) {
		return fileService.deleteObjectByPath(path);
	}

	/**
	 * 描述：批量根据路径删除文件 用于删除数据库没有存储数据的的文件 不删除数据库数据
	 * @author 490485964@qq.com
	 * @date 2018/5/25 14:01
	 * @param pathList 文件在云上的路径集合
	 * @return
	 */
	@Override
	public int deleteObjectByPath(@RequestParam("pathList")List<String> pathList) {
		return fileService.deleteObjectByPath(pathList);
	}


	@Override
	public String upLoadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam("tenantId")Long tenantId) {
		return  fileService.upLoadFile(file,tenantId);
	}

	@Override
	public void updateFile(@RequestPart(value = "file") MultipartFile file,@RequestParam("fileId") String fileId,@RequestParam("filePath") String filePath) {
		fileService.updateFile(file,fileId,filePath);
	}

	@Override
	public FileBean getFileInfoByFileId(@RequestParam("fileId") String fileId) {
		return fileService.getFileInfoByFileId(fileId);
	}

	/**
	 * @despriction：从redis中获取fileId进而删除s3和数据库的数据
	 * @author  yeshiyuan
	 * @created 2018/6/5 14:40
	 * @param redisTaskId
	 * @return
	 */
	@Override
	public void deleteFileByRedisTaskId(@RequestParam("redisTaskId") String redisTaskId) {
		fileService.deleteFileByRedisTaskId(redisTaskId);
	}

	/**
	  * @despriction：文件上传并获取到文件的MD5
	  * @author  yeshiyuan
	  * @created 2018/8/3 20:03
	  * @param null
	  * @return
	  */
	@Override
	public FileInfoResp upLoadFileAndGetMd5(@RequestPart(value = "file") MultipartFile file, @RequestParam("tenantId")Long tenantId) {
		return fileService.upLoadFileAndGetMd5(file, tenantId);
	}

	/**
	 * @despriction：获取文件路径
	 * @author  yeshiyuan
	 * @created 2018/8/21 14:38
	 * @param null
	 * @return
	 */
	@Override
	public Map<String, String> batchGetFilePath(@RequestParam("fileIds") List<String> fileIds) {
		return fileService.batchGetFilePath(fileIds);
	}

	/**
	 * @despriction：文件上传并返回文件id、访问url
	 * @author  yeshiyuan
	 * @created 2018/8/30 15:32
	 * @param null
	 * @return
	 */
	@Override
	public FileInfoResp upLoadFileAndGetUrl(@RequestPart(value = "file") MultipartFile file,@RequestParam(value = "tenantId") Long tenantId) {
		NeedReturnFileInfoReq req = new NeedReturnFileInfoReq(false,true);
		return fileService.uploadFile(file, tenantId, req);
	}

	/**
	  * @despriction：上传文件但不保存文件信息至数据库（会临时保存至redis，需手动调用saveInfoToDb接口才会入库，否则过段时间文件将被删除）
	  * @author  yeshiyuan
	  * @created 2018/10/9 17:44
	  * @return
	  */
	@ApiOperation(value = "上传文件但不保存文件信息至数据库", notes = "文件信息会临时保存至redis，需手动调用saveInfoToDb接口才会入库，否则过段时间文件将被删除")
	@RequestMapping(value = "/api/file/service/uploadFileButNoSaveToDb", method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FileInfoResp uploadFileButNoSaveToDb(@RequestPart(value = "file") MultipartFile file,@RequestParam(value = "tenantId") Long tenantId) {
		NeedReturnFileInfoReq req = new NeedReturnFileInfoReq(false,true);
		return fileService.uploadFile(file, tenantId, req, false);
	}

	/**
	 * @despriction：把redis中的文件信息保存至数据库
	 * @author  yeshiyuan
	 * @created 2018/10/9 19:10
	 * @return
	 */
	@Override
	public void saveFileInfoToDb(@RequestParam("fileId") String fileId) {
		fileService.saveFileInfoToDb(fileId);
	}

	/**
	 * @despriction：批量把redis中的文件信息保存至数据库
	 * @author  yeshiyuan
	 * @created 2018/10/9 19:10
	 * @return
	 */
	@Override
	public void saveFileInfosToDb(@RequestParam("fileIds") List<String> fileIds) {
		fileService.saveFileInfosToDb(fileIds);
	}

	/**
	 * 描述：获取md5值
	 * @author nongchongwei
	 * @date 2018/11/5 17:55
	 * @param
	 * @return
	 */
	@Override
	public String getObjectContentMD5(@RequestParam("fileId") String fileId) {
		return fileService.getObjectContentMD5(fileId);
	}

	/**
	 * @despriction：获取下载验证码（有效期单位秒）
	 * @author  yeshiyuan
	 * @created 2018/11/28 18:01
	 */
	@Override
	public String getDownloadCode(@RequestParam("second") Integer second) {
		return fileService.getDownloadCode(second);
	}


	/**
	 * @despriction：检验下载验证码是否过期
	 * @author  yeshiyuan
	 * @created 2018/11/28 19:11
	 */
	@Override
	public boolean checkDownloadCode(String uuid) {
		return fileService.checkDownloadCode(uuid);
	}
}
