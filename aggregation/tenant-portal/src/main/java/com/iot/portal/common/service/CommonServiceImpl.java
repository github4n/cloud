package com.iot.portal.common.service;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;
import com.iot.common.util.*;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.portal.constant.TenantConstant;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：公共服务
 * 创建人： maochengyuan
 * 创建时间：2018/7/26 10:15
 * 修改人： maochengyuan
 * 修改时间：2018/7/26 10:15
 * 修改描述：
 */
public class CommonServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    /**设置日期字段*/
    private final static String DATA_DATE = "setDataDate";

    @Autowired
    private FileApi fileApi;

    @Autowired
    private FileUploadApi fileUploadApi;

    /**
     * 描述：加密主键
     * @author maochengyuan
     * @created 2018/7/26 10:30
     * @param primaryKey 主键
     * @return java.lang.String
     */
    protected String encryptKey(Long primaryKey) {
        if(primaryKey == null){
            return "";
        }
        return SecurityUtil.EncryptByAES(primaryKey.toString(), TenantConstant.SECURITY_KEY);
    }

    /**
     * 描述：解密主键
     * @author maochengyuan
     * @created 2018/7/26 10:30
     * @param primaryKey 主键
     * @param filedName 字段名
     * @return java.lang.Long
     */
    protected Long decryptKey(String primaryKey, String filedName) {
        if(StringUtil.isEmpty(primaryKey)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, filedName+" is null");
        }
        try {
            String key = SecurityUtil.DecryptAES(primaryKey, TenantConstant.SECURITY_KEY);
            return NumberUtil.toLong(key);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, filedName+" incorrect format");
        }
    }

    /**
     * 描述：多个文件上传
     * @author maochengyuan
     * @created 2018/7/27 14:12
     * @param multipartRequest 文件信息
     * @param allowTypes 允许的文件类型
     * @param maxSize 最大上传大小
     * @return java.util.Map<java.lang.String,java.lang.String> {fileName1:fileId1, fileName2:fileId2...}
     */
    protected Map<String, String> upLoadMultFile(MultipartHttpServletRequest multipartRequest, List<String> allowTypes, int maxSize) {
        return (Map<String, String>) this.upLoadFile(multipartRequest, allowTypes, maxSize, false);
    }

    /**
     * 描述：单个文件上传
     * @author maochengyuan
     * @created 2018/7/27 14:12
     * @param multipartRequest 文件信息
     * @param allowTypes 允许的文件类型
     * @param maxSize 最大上传大小
     * @return java.lang.String
     */
    protected String upLoadSoleFile(MultipartHttpServletRequest multipartRequest, List<String> allowTypes, int maxSize) {
        return (String) this.upLoadFile(multipartRequest, allowTypes, maxSize, true);
    }

    /**
     * 描述：多个文件上传
     * @author maochengyuan
     * @created 2018/7/27 14:12
     * @param multipartRequest 文件信息
     * @param allowTypes 允许的文件类型
     * @param maxSize 最大上传大小
     * @param isSole 是否是单个文件
     * @return java.util.Map<java.lang.String,java.lang.String> {fileName1:fileId1, fileName2:fileId2...}
     */
    protected Object upLoadFile(MultipartHttpServletRequest multipartRequest, List<String> allowTypes, int maxSize, boolean isSole) {
        /**检查是否有文件*/
        if(multipartRequest == null || !multipartRequest.getFileNames().hasNext()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, " file is null");
        }
        /**检查允许上传的文件类型*/
        if(CommonUtil.isEmpty(allowTypes)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, " allow types is null");
        }
        if(isSole){
            MultipartFile file = multipartRequest.getFile(multipartRequest.getFileNames().next());
            this.checkFileLegality(file, allowTypes, maxSize);
            return this.fileUploadApi.uploadFile(file, SaaSContextHolder.currentTenantId());
        }else{
            /**检查文件大小*/
            multipartRequest.getMultiFileMap().forEach((k, v) ->{
                this.checkFileLegality(v.get(0), allowTypes, maxSize);
            });
            /**定义返回结果*/
            Map<String, String> resultMap = new HashMap<>();
            /**依次上传文件*/
            multipartRequest.getMultiFileMap().forEach((k, v) ->{
                try {
                    resultMap.put(k, this.fileApi.upLoadFile(v.get(0), SaaSContextHolder.currentTenantId()));
                } catch (Exception e) {
                    this.deleteFile(resultMap.values());
                    throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_UPLOAD_FAILED, e);
                }
            });
            return resultMap;
        }
    }

    /**
     * 描述：回滚删除上传部分成功数据
     * @author maochengyuan
     * @created 2018/7/26 15:40
     * @param fileIds 删除文件
     * @return void
     */
    private void deleteFile(Collection<String> fileIds){
        if(!CommonUtil.isEmpty(fileIds)){
            try {
                this.fileApi.deleteObject(new ArrayList<>(fileIds));
            } catch (Exception e) {
                logger.error("delete file error：", e);
            }
        }
    }

    /**
     * 描述：获取文件后缀
     * @author maochengyuan
     * @created 2018/7/27 11:21
     * @param file 文件
     * @return java.lang.String
     */
    private String getFileType(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if(StringUtil.isEmpty(fileName)){
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /** 
     * 描述：检查文件合法性
     * @author maochengyuan
     * @created 2018/7/27 14:48
     * @param file 文件信息
     * @param allowTypes 允许的文件类型
     * @param maxSize 最大上传大小
     * @return void
     */
    private void checkFileLegality(MultipartFile file, List<String> allowTypes, int maxSize){
        String fileType = this.getFileType(file);
        if(file.getSize() > maxSize){
            throw new BusinessException(BusinessExceptionEnum.EXCEED_MAX_FILE_SIZE, maxSize+TenantConstant.FILE_SIZE_UNIT);
        }
        if(!allowTypes.contains(fileType)){
            throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_TYPE_N0T_SUPPORT, fileType);
        }
    }
    
    /**
     * 
     * 描述：检查字符串入参
     * @author 李帅
     * @created 2018年8月3日 上午10:32:39
     * @since 
     * @param param
     * @param min
     * @param max
     * @param language
     */
	public static void checkStringParam(String param, int min, int max, String language) {
		if (min > 0 && param.length() < min) {
			throw new BusinessException(ExceptionEnum.STRING_TOO_SHORT);
		}
		if (param != null && "zh_CN".equals(language)) {
			String regEx = "[\\u4e00-\\u9fa5]";
			String term = param.replaceAll(regEx, "aa");
			if (term.length() - param.length() > max) {
				throw new BusinessException(ExceptionEnum.STRING_TOO_LONG);
			}
		} else {
			if (param != null && param.length() > max) {
				throw new BusinessException(ExceptionEnum.STRING_TOO_LONG);
			}
		}
	}

    /**
     * 描述：获取查询周期
     * @author maochengyuan
     * @created 2019/1/7 17:33
     * @param allowList 运行的周期
     * @param days 天数
     * @return java.util.Date[]
     */
    protected Date[] getDateArray(List<String> allowList, String days){
        if(allowList.contains(days)){
            Calendar sta = CalendarUtil.getNowCalendar();
            sta.add(Calendar.DAY_OF_MONTH, Integer.parseInt(days));
            return new Date[]{new Date(sta.getTimeInMillis()), new Date(CalendarUtil.getNowCalendar().getTimeInMillis())};
        }
        throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR);
    }

    /**
     * 描述：构建基础数据
     * @author maochengyuan
     * @created 2019/1/9 16:54
     * @param cycle 周期
     * @param classz
     * @return java.util.LinkedHashMap<java.lang.String,T>
     */
    protected static <T> LinkedHashMap<String, T> bulidBaseBean(String cycle, Class<T> classz){
        Calendar sta = CalendarUtil.getNowCalendar();
        Calendar end = CalendarUtil.getNowCalendar();
        LinkedHashMap<String, T> restMap = new LinkedHashMap<>();
        sta.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(cycle));
        while(sta.before(end)){
            String dataDate = CalendarUtil.format(sta);
            restMap.put(dataDate, newInstance(classz, dataDate));
            sta.add(Calendar.DAY_OF_MONTH, 1);
        }
        return restMap;
    }

    /**
     * 描述：实例化对象并设置日期
     * @author maochengyuan
     * @created 2019/1/9 17:07
     * @param classz 对象
     * @param dataDate 日期
     * @return T
     */
    protected static <T> T newInstance(Class<T> classz, String dataDate){
        T t = null;
        try {
            t = classz.newInstance();
            ClassUtil.invokeMethodByClass(t, DATA_DATE, new Class[]{String.class}, new String[]{dataDate});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

}
