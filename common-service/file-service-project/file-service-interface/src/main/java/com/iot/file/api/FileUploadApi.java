package com.iot.file.api;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.IBusinessException;
import com.iot.file.vo.FileInfoResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * 项目名称：cloud
 * 功能描述：文件上传api（不会超时）
 * 创建人： yeshiyuan
 * 创建时间：2018/9/4 20:13
 * 修改人： yeshiyuan
 * 修改时间：2018/9/4 20:13
 * 修改描述：
 */
@Service
public class FileUploadApi {

    private final static String serviceName = "http://file-service";

    private static Logger logger = LoggerFactory.getLogger(FileUploadApi.class);

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        return new RestTemplate(requestFactory);
    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @despriction：基于RestTemplate的文件上传实现
     * @author  yeshiyuan
     * @created 2018/9/6 10:59
     * @param null
     * @return
     */
    private <T> T upload(MultipartFile file, Long tenantId, String url, Class<T> valueClass){
        Long startTime = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType( MediaType.parseMediaType("multipart/form-data; charset=UTF-8") );
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<> ();
        try {
            ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()){
                @Override
                public String getFilename(){
                    return file.getOriginalFilename();
                }
            };
            form.add("file", contentsAsResource);
            form.add("tenantId",tenantId);
            HttpEntity< MultiValueMap<String, Object> > formEntity = new HttpEntity<>(form, headers);
            ResponseEntity<T> result = restTemplate.postForEntity(url ,formEntity , valueClass);
            logger.debug("上传文件({},文件大小：{})总共耗时：{}ms", file.getOriginalFilename(), file.getSize(),(System.currentTimeMillis()-startTime));
            return result.getBody();
        } catch (Throwable e) {
            logger.error("FileUploadApi upload fail", e);
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 400;
                }

                @Override
                public String getMessageKey() {
                    return "fileservice.upLoadFile.error";
                }
            });
        }
    }

    /**
      * @despriction：上传文件并获取文件uuid、访问url
      * @author  yeshiyuan
      * @created 2018/9/5 16:57
      * @return
      */
    public FileInfoResp upLoadFileAndGetUrl(MultipartFile file, Long tenantId){
        String url = serviceName + "/api/file/service/upLoadFileAndGetUrl";
        return this.upload(file, tenantId, url, FileInfoResp.class);
    }

    /**
     * @despriction：上传文件并获取文件的uuid、MD5
     * @author  yeshiyuan
     * @created 2018/9/5 16:57
     * @return
     */
    public FileInfoResp upLoadFileAndGetMd5(MultipartFile file, Long tenantId){
        String url = serviceName + "/api/file/service/upLoadFileAndGetMd5";
        return this.upload(file, tenantId, url, FileInfoResp.class);
    }

    /**
      * @despriction：上传文件并返回文件uuid
      * @author  yeshiyuan
      * @created 2018/9/5 17:05
      * @param null
      * @return
      */
    public String uploadFile(MultipartFile file, Long tenantId) {
        String url = serviceName + "/api/file/service/upLoadFile";
        return this.upload(file, tenantId, url, String.class);
    }

    /**
      * @despriction：上传文件但不保存文件信息至数据库（）
      * @author  yeshiyuan
      * @created 2018/10/9 17:41
      * @return
      */
    public FileInfoResp uploadFileButNoSaveToDb(MultipartFile file, Long tenantId) {
        String url = serviceName + "/api/file/service/uploadFileButNoSaveToDb";
        return this.upload(file, tenantId, url, FileInfoResp.class);
    }

}
