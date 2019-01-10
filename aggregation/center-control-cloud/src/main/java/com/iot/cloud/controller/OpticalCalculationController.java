package com.iot.cloud.controller;

import com.iot.cloud.service.impl.OpticalCalculationService;
import com.iot.cloud.util.ConstantUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 光学计算
 *
 * @author fenglijian
 */
@Api(tags = {"光学计算接口"})
@Controller
@RequestMapping(ConstantUtil.commonPath+"/optical")
public class OpticalCalculationController {

    @Autowired
    private OpticalCalculationService opticalCalculationService;

    /**
     * 计算空间坐标照度值
     *
     * @param length       长
     * @param width        宽
     * @param high         高
     * @param unitLength   网格长
     * @param unitWidth    网格宽
     * @param refractivity 折射率
     * @param fileId       文件Id
     */
    @ApiOperation("计算空间坐标的照度值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "length", value = "空间长度", required = true, paramType = "query", dataType = "Float"),
            @ApiImplicitParam(name = "width", value = "空间宽度", required = true, paramType = "query", dataType = "Float"),
            @ApiImplicitParam(name = "high", value = "空间高度", required = true, paramType = "query", dataType = "Float"),
            @ApiImplicitParam(name = "unitLength", value = "网格长度", required = true, paramType = "query", dataType = "Float"),
            @ApiImplicitParam(name = "unitWidth", value = "网格宽度", required = true, paramType = "query", dataType = "Float"),
            @ApiImplicitParam(name = "refractivity", value = "折射率", required = true, paramType = "query", dataType = "Float"),
            @ApiImplicitParam(name = "fileId", value = "文件Id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/calculator", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<Map<String, Float>>> calculator(@RequestParam("length") Float length,
                                                               @RequestParam("width") Float width, @RequestParam("high") Float high,
                                                               @RequestParam("unitLength") Float unitLength, @RequestParam("unitWidth") Float unitWidth,
                                                               @RequestParam("refractivity") Float refractivity, @RequestParam("fileId") String fileId,
                                                               @RequestParam Float[] lightSource) {
        try {
            List<Map<String, Float>> result = opticalCalculationService.calculator(length, width, high,
                    unitLength, unitWidth, refractivity, fileId, lightSource);//计算方法
            return CommonResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 计算空间坐标照度值（有桌子的情况）
     *
     * @param length       长
     * @param width        宽
     * @param high         高
     * @param unitLength   网格长
     * @param unitWidth    网格宽
     * @param refractivity 折射率
     * @param fileId       文件Id
     * @return [{"[0.1,0.1,0,0,0,1]":55.964905},{"[0.1,0.70000005,0,0,0,1]":86.67297}....] 可能存在精度问题
     */
    @ApiOperation("计算空间坐标照度值（有桌子的情况）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "length", value = "空间长度", required = true, paramType = "query", dataType = "Float", defaultValue = "5"),
            @ApiImplicitParam(name = "width", value = "空间宽度", required = true, paramType = "query", dataType = "Float", defaultValue = "5"),
            @ApiImplicitParam(name = "high", value = "空间高度", required = true, paramType = "query", dataType = "Float", defaultValue = "3"),
            @ApiImplicitParam(name = "unitLength", value = "网格长度", required = true, paramType = "query", dataType = "Float", defaultValue = "0.2"),
            @ApiImplicitParam(name = "unitWidth", value = "网格宽度", required = true, paramType = "query", dataType = "Float", defaultValue = "0.2"),
            //@ApiImplicitParam(name = "lightSource", value = "光源", required = true, paramType = "lightSource",defaultValue = "2.5,2.5,3,0,0,-1"),
            @ApiImplicitParam(name = "refractivity", value = "折射率", required = true, paramType = "query", dataType = "Float", defaultValue = "0.5"),
            @ApiImplicitParam(name = "fileId", value = "文件Id", required = true, paramType = "query", dataType = "String", defaultValue = "bf7b6fd235704f43b0e328de26769936"),
            @ApiImplicitParam(name = "tableFlag", value = "桌子的标志", required = true, paramType = "query", dataType = "String", defaultValue = "TABLE"),
            @ApiImplicitParam(name = "tableLength", value = "桌子的长度", required = true, paramType = "query", dataType = "Float", defaultValue = "1"),
            @ApiImplicitParam(name = "tableWidth", value = "桌子的宽度", required = true, paramType = "query", dataType = "Float", defaultValue = "1"),
            @ApiImplicitParam(name = "tableHeight", value = "桌子的高度", required = true, paramType = "query", dataType = "Float", defaultValue = "1"),
            @ApiImplicitParam(name = "tbaleCenterX", value = "桌子中点的X坐标", required = true, paramType = "query", dataType = "Float", defaultValue = "2.5"),
            @ApiImplicitParam(name = "tbaleCenterY", value = "桌子中点的Y坐标", required = true, paramType = "query", dataType = "Float", defaultValue = "2.5"),
            @ApiImplicitParam(name = "tbaleCenterZ", value = "桌子中点的Z坐标", required = true, paramType = "query", dataType = "Float", defaultValue = "1")
    })
    @RequestMapping(value = "/calculatorHaveTable", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<Map<String, Float>>> calculatorHaveTable(@RequestParam("length") Float length, @RequestParam("width") Float width, @RequestParam("high") Float high,
                                                                        @RequestParam("unitLength") Float unitLength, @RequestParam("unitWidth") Float unitWidth, @RequestParam("refractivity") Float refractivity,
                                                                        @RequestParam("fileId") String fileId, @RequestParam Float[] lightSource,
                                                                        @RequestParam("tableLength") Float tableLength, @RequestParam("tableWidth") Float tableWidth, @RequestParam("tableHeight") Float tableHeight,
                                                                        @RequestParam("tbaleCenterX") Float tbaleCenterX, @RequestParam("tbaleCenterY") Float tbaleCenterY, @RequestParam("tbaleCenterZ") Float tbaleCenterZ,
                                                                        @RequestParam("tableFlag") String tableFlag
    ) {
        try {
            List<Map<String, Float>> result = opticalCalculationService.calculatorHaveTable(length, width, high,
                    unitLength, unitWidth, refractivity, fileId, lightSource, tableLength, tableWidth, tableHeight, tbaleCenterX, tbaleCenterY, tbaleCenterZ, tableFlag);//计算方法
            return CommonResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 眩光值
     *
     * @param testPoint   人眼坐标  (x,y,z,vx,xy,xz)
     * @param lightPoint  光源坐标 (x,y,z,vx,xy,xz)
     * @param lightParams 光源的长宽 x y
     * @param ies         ies文件id
     * @return
     */
    @ApiOperation("眩光值")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "testPoint", value = "人眼坐标", required = true, paramType = "query", dataType = "Float",defaultValue = "5"),
//			@ApiImplicitParam(name = "lightPoint", value = "光源坐标", required = true, paramType = "query", dataType = "Float",defaultValue = "5"),
//			@ApiImplicitParam(name = "lightParams", value = "光源的长宽", required = true, paramType = "query", dataType = "Float",defaultValue = "5"),
//			@ApiImplicitParam(name = "ies", value = "ies文件", required = true, paramType = "query", dataType = "Float",defaultValue = "5")
//	})
    @RequestMapping(value = "/glareCalculator", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<Map<String, Float>>> glareCalculator(
            @ApiParam(name = "testPoint", required = true, defaultValue = "1,1,1,0,1,0") @RequestParam("testPoint") Float[] testPoint,// 1,1,1.2,0,0,1
            @ApiParam(name = "lightPoint", required = true, defaultValue = "2.5,2.5,3,0,0,-1") @RequestParam("lightPoint") List<Float[]> lightPoint,// 2.5,2.5,3,0,0,-1
            @ApiParam(name = "lightParams", required = true, defaultValue = "0.5,0.5") @RequestParam("lightParams") Float[] lightParams, // 0.5,0.5
            @ApiParam(name = "ies", required = true, defaultValue = "bf7b6fd235704f43b0e328de26769936") @RequestParam("ies") String ies,
            @ApiParam(name = "coordinateList", required = false) @RequestParam(name = "coordinateList", required = false) List<Map<String, Object>> coordinateList

    ) {
        try {
            lightPoint = new ArrayList<>();
            lightPoint.add(new Float[]{2.5f, 2.5f, 3f, 0f, 0f, -1f});
            opticalCalculationService.glareCalculator(testPoint, lightPoint, lightParams, ies, coordinateList);
            return CommonResponse.success(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件上传
     *
     * @return
     * @throws BusinessException
     */
    @ApiOperation("文件上传")
//	@ApiImplicitParams({
//        @ApiImplicitParam(name = "multipartRequest", value = "文件", required = false, paramType = "query", dataType = "String"),
//        @ApiImplicitParam(name = "tenantId", value = "租户Id", required = true, paramType = "query", dataType = "Long")
//	})
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<String> fileUpload(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("tenantId") Long tenantId)
            throws BusinessException {
        String fileId = null;
        try {
            fileId = opticalCalculationService.fileUpload(multipartFile, tenantId);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
        return CommonResponse.success(fileId);
    }

    /**
     * 添加位置指数到redis
     *
     * @return
     * @throws BusinessException
     */
    @ApiOperation("添加位置指数到redis")
    @RequestMapping(value = "/addWZZS2Redis", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<String> addWZZS2Redis(MultipartFile multipartFile)
            throws BusinessException {
        try {
            opticalCalculationService.addWZZS2Redis(multipartFile);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
        return CommonResponse.success();
    }
}
