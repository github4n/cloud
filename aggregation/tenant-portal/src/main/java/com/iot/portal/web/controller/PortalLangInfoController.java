package com.iot.portal.web.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.StringUtil;
import com.iot.common.util.ToolUtil;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.file.api.FileApi;
import com.iot.file.util.FileUtil;
import com.iot.file.util.excel.ExcelUtil;
import com.iot.file.util.excel.vo.ExcelColumnVo;
import com.iot.file.util.excel.vo.ExcelFileVo;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.web.vo.req.LangInfoExportReq;
import com.iot.portal.web.vo.req.PortalSaveLangInfoTenantReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantPageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoTenantReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.lang.LangInfoTenantResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 项目名称：cloud
 * 功能描述：portal文案管理
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 17:57
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 17:57
 * 修改描述：
 */
@Api(tags = "portal文案管理")
@RestController
@RequestMapping(value = "/langInfo")
public class PortalLangInfoController {

    @Autowired
    private LangInfoTenantApi langInfoTenantApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    protected FileApi fileApi;

    /**
      * @despriction：查询某对象对应文案
      * @author  yeshiyuan
      * @created 2018/9/30 18:04
      * @return
      */
    @ApiOperation(value = "查询某对象对应文案", notes = "查询某对象对应文案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectId", value = "文案对象id", dataType = "Long", paramType = "query", required = true),
            @ApiImplicitParam(name = "objectType", value = "文案类型", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "belongModule", value = "所属模块", dataType = "String", paramType = "query", required = false)
    })
    @RequestMapping(value = "/queryLangInfo", method = RequestMethod.POST)
    public CommonResponse queryLangInfo(@RequestParam("objectId")Long objectId, @RequestParam("objectType") String objectType,
                                        @RequestParam(value = "belongModule",required = false) String belongModule) {
        QueryLangInfoTenantReq req = new QueryLangInfoTenantReq(objectId.toString(), objectType,SaaSContextHolder.currentTenantId(), belongModule);
        return ResultMsg.SUCCESS.info(langInfoTenantApi.queryLangInfo(req));
    }

    /**
      * @despriction：保存文案信息
      * @author  yeshiyuan
      * @created 2018/9/30 18:07
      * @param null
      * @return
      */
    @ApiOperation(value = "保存文案信息", notes = "保存文案信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse update(@RequestBody PortalSaveLangInfoTenantReq req) {
        if (req == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The request parameter is empty.");
        }
        //校验操作对象是否存在
        Long obTenantId;
        if (LangInfoObjectTypeEnum.appConfig.name().equals(req.getObjectType())) {
            AppInfoResp appInfoResp = appApi.getAppById(req.getObjectId());
            if (appInfoResp==null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The APP ID does not exist.");
            }
            obTenantId = appInfoResp.getTenantId();
        } else if (LangInfoObjectTypeEnum.checkIsDeviceType(req.getObjectType())) {
            ProductResp resp = productApi.getProductById(req.getObjectId());
            if (resp==null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
            }
            obTenantId = resp.getTenantId();
        } else {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "Object type error.");
        }
        //是否属于当前租户
        Long currentTenantId = SaaSContextHolder.currentTenantId();
        if (!currentTenantId.equals(obTenantId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The object doesn't belong to you.");
        }
        SaveLangInfoTenantReq langInfoTenantReq = new SaveLangInfoTenantReq();
        BeanUtil.copyProperties(req, langInfoTenantReq);
        langInfoTenantReq.setUserId(SaaSContextHolder.getCurrentUserId());
        langInfoTenantReq.setTenantId(SaaSContextHolder.currentTenantId());
        langInfoTenantReq.setObjectId(req.getObjectId().toString());
        langInfoTenantApi.saveLangInfo(langInfoTenantReq);
        return ResultMsg.SUCCESS.info();
    }

    /**
      * @despriction：分页查询
      * @author  yeshiyuan
      * @created 2018/10/12 16:15
      * @return
      */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public CommonResponse pageQuery(@RequestBody QueryLangInfoTenantPageReq pageReq) {
        pageReq.setTenantId(SaaSContextHolder.currentTenantId());
        return ResultMsg.SUCCESS.info(langInfoTenantApi.pageQuery(pageReq));
    }

    /**
      * @despriction：文案导出并生成excel文件
      * @author  yeshiyuan
      * @created 2018/11/27 11:43
      */
    @LoginRequired(Action.Skip)
    @ApiOperation(value = "文案导出并生成excel文件", notes = "文案导出并生成excel文件")
    @RequestMapping(value = "/exportToExcel", method = RequestMethod.GET)
    public void exportToExcel(LangInfoExportReq req, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtil.isBlank(req.getCode())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "Please login first.");
        }
        if (!fileApi.checkDownloadCode(req.getCode())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "Please login first.");
        }
        Long tenantId;
        String fileName;
        if (LangInfoObjectTypeEnum.appConfig.name().equals(req.getObjectType())) {
            AppInfoResp appInfoResp = appApi.getAppById(Long.valueOf(req.getObjectId()));
            if (appInfoResp==null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The APP ID does not exist.");
            }
            tenantId = appInfoResp.getTenantId();
            fileName = appInfoResp.getAppName();
        } else if (LangInfoObjectTypeEnum.checkIsDeviceType(req.getObjectType())) {
            ProductResp resp = productApi.getProductById(Long.valueOf(req.getObjectId()));
            if (resp==null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
            }
            fileName = resp.getProductName();
            tenantId = resp.getTenantId();
        } else {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "Object type error.");
        }
        QueryLangInfoTenantReq queryReq = new QueryLangInfoTenantReq(req.getObjectId(), req.getObjectType(), tenantId, null);
        LangInfoTenantResp langInfoTenantResp = langInfoTenantApi.queryLangInfo(queryReq);
        if (langInfoTenantResp == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The language information does not exist.");
        }
        //excel信息
        String saveFilePath = request.getSession().getServletContext().getRealPath("/") + ToolUtil.getUUID();
        ExcelFileVo excelFileVo = new ExcelFileVo(saveFilePath,fileName+".xlsx","文案",null);
        //列名格式
        List<ExcelColumnVo> excelColumnVos = new ArrayList<>();
        excelColumnVos.add(new ExcelColumnVo("key", "key", 25*256));
        langInfoTenantResp.getLangTypes().forEach(langType -> {
            excelColumnVos.add(new ExcelColumnVo(langType, langType, 25*256));
        });
        //组装成excel数据格式
        List<Map<String,Object>> dataList = new ArrayList<>();
        langInfoTenantResp.getLangInfos().forEach(langInfo -> {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("key", langInfo.getKey());
            dataMap.putAll(langInfo.getVal());
            dataList.add(dataMap);
        });
        try {
            ExcelUtil.downloadExcel(excelFileVo, excelColumnVos, dataList, response);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.FILE_DOWNLOAD_ERROR);
        }
        /*//FileUtil.downloadFile(response, excelFile);

        ;

        FileInputStream fi = null;
        BufferedInputStream br = null;
        OutputStream os = null;
        try {
            File excelFile = ExcelUtil.createExcelFile(excelFileVo, excelColumnVos, dataList);
            //File excelFile = new File("C:\\打包.xlsx");
            Properties properties = System.getProperties();
            String encodingStr = properties.getProperty("file.encoding");

            fi = new FileInputStream(excelFile);




            br = new BufferedInputStream (fi);

            byte []buffer = new byte[1024];
            //ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            //下载文件
            os = response.getOutputStream();
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //response.setHeader("Content-Type", "text/html;charset=utf-8");
            response.setCharacterEncoding(encodingStr);
            response.setHeader("content-length", excelFile.length() + "");
            response.setHeader("content-disposition", "attachment; filename=" + new String(excelFile.getName().getBytes(), "ISO8859-1") );
            int i;
            while ((i=br.read(buffer))!=-1){
                os.write(buffer,0, i);
            }
            *//*Integer contentLength = tmp.size();
            response.setHeader("content-length", contentLength + "");
            os.write(tmp.toByteArray());*//*

            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fi!=null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os!=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
       // FileUtil.deleteByFilePath(saveFilePath);
    }



}
