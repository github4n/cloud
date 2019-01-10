package com.iot.portal.web.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ProductApi;
import com.iot.device.api.TechnicalRelateApi;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.portal.service.LangInfoService;
import com.iot.portal.web.utils.CompactAlgorithm;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.DeviceNetworkStepTenantApi;
import com.iot.tenant.api.LangInfoTenantApi;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.network.CopyNetworkStepReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.AppProductResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/10/17 17:46
 * 修改人： yeshiyuan
 * 修改时间：2018/10/17 17:46
 * 修改描述：
 */
@Api(description = "测试",tags = "测试")
@RestController
public class TestCtrl {

    @Autowired
    private LangInfoService langInfoService;


    @Autowired
    private AppApi appApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private LangInfoTenantApi langInfoTenantApi;


    @Autowired
    private DeviceNetworkStepTenantApi deviceNetworkStepTenantApi;

    @Autowired
    private TechnicalRelateApi technicalRelateApi;

    @LoginRequired(Action.Skip)
    @ApiOperation(value = "下载app打包文件", notes = "下载app打包文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/downAppPackageFile",method = RequestMethod.GET)
    public void downAppPackageFile(@RequestParam("appId") Long appId, HttpServletResponse response){
        AppInfoResp resp = appApi.getAppById(appId);
        SaaSContextHolder.setCurrentTenantId(resp.getTenantId());
        List<AppProductResp> list = appApi.getAppProduct(appId);
        List<Long> productList = list.stream().map(AppProductResp::getProductId).collect(Collectors.toList());
        String saveFilePath = langInfoService.createAppLangFile(appId,productList,resp.getTenantId());
        //压缩文件
        File file = new File(saveFilePath);
        String zipFilePath = file.getParent() + File.separator + file.getName() + ".zip";
        File zipFile = new File(zipFilePath);
        new CompactAlgorithm(zipFile).zipFiles(file);


        FileInputStream fi = null;
        BufferedInputStream  br = null;
        OutputStream os = null;
        try {
            fi = new FileInputStream(zipFilePath);
            br = new BufferedInputStream (fi);
            byte []buffer = new byte[1024];
            os = new BufferedOutputStream(response.getOutputStream());
            //下载文件
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename="+ file.getName() + ".zip");
            int i;
            while ((i=br.read(buffer))!=-1){
                os.write(buffer,0, i);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        }
        delDir(saveFilePath);
        zipFile.delete();
    }

    public void delDir(String path) {
        File dir=new File(path);
        if(dir.exists()) {
            File[] tmp=dir.listFiles();
            for(int i=0;i<tmp.length;i++) {
                if(tmp[i].isDirectory()) {
                    delDir(path+"/"+tmp[i].getName());
                }
                 else {
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }

    @LoginRequired(Action.Normal)
    @ApiOperation(value = "拷贝文案及配网引导", notes = "拷贝文案及配网引导")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productIds", value = "产品id" ,dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "appId", value = "appId" ,dataType = "appId", paramType = "query")
    })
    @RequestMapping(value = "/copyLangAndNetwork", method = RequestMethod.POST)
    public CommonResponse copyLangAndNetwork(@RequestParam("productIds") List<Long> productIds, @RequestParam("appId") Long appId) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Map<Long, Long> productDeviceTypeMap = new HashMap<>();
        if (productIds != null && !productIds.isEmpty()) {
            //先拷贝产品文案
            for (Long productId : productIds) {
                ProductResp productResp = productApi.getProductById(productId);
                if (productResp!=null) {
                    productDeviceTypeMap.put(productId, productResp.getDeviceTypeId()) ;
                    CopyLangInfoReq copyLangInfoReq = new CopyLangInfoReq(productResp.getTenantId(), productId, productResp.getDeviceTypeId(), LangInfoObjectTypeEnum.deviceType.name(), userId);
                    langInfoTenantApi.copyLangInfo(copyLangInfoReq);
                }
            }
        }
        if (appId != null) {
            AppInfoResp appInfoResp = appApi.getAppById(appId);
            if (appInfoResp != null){
                List<AppProductResp> appProducts = appApi.getAppProduct(appId);
                if (appProducts!=null && !appProducts.isEmpty()) {
                    List<Long> pIds = appProducts.stream().map(AppProductResp::getProductId).filter(productId -> productIds.contains(productId)).collect(Collectors.toList());
                    if (!pIds.isEmpty()){
                        for (Long productId : pIds) {
                            Long deviceTypeId = productDeviceTypeMap.get(productId);
                            List<NetworkTypeResp> networkTypes = technicalRelateApi.deviceSupportNetworkType(deviceTypeId);
                            List<Long> networkTypeIds = networkTypes.stream().map(NetworkTypeResp::getId).collect(Collectors.toList());
                            if (networkTypeIds != null && !networkTypeIds.isEmpty()) {
                                CopyNetworkStepReq copyNetworkStepReq = new CopyNetworkStepReq(appInfoResp.getTenantId(),userId, appId, productId, deviceTypeId, networkTypeIds);
                                deviceNetworkStepTenantApi.copyNetworkStep(copyNetworkStepReq);
                            }
                        }
                    }
                 }
             }
        }
        return CommonResponse.success();
    }
}
