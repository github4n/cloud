package com.iot.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.cloud.helper.BusinessExceptionEnum;
import com.iot.cloud.util.ExcelUtils;
import com.iot.common.exception.BusinessException;
import com.iot.design.optical.CalculatorApi;
import com.iot.design.optical.OpticalApi;
import com.iot.design.optical.req.GlareCalculatorReq;
import com.iot.design.optical.vo.LightSource;
import com.iot.design.optical.vo.Rectangle;
import com.iot.design.optical.vo.Table;
import com.iot.file.api.FileApi;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OpticalCalculationService {

    @Autowired
    private CalculatorApi calculatorApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private OpticalApi opticalApi;

    public List<Map<String, Float>> calculator(Float length, Float width, Float high,
                                               Float unitLength, Float unitWidth, Float refractivity, String fileId, Float[] lightSource) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHigh(high);
        rectangle.setLength(length);
        rectangle.setWidth(width);
        rectangle.setUnitLength(unitLength);
        rectangle.setUnitWidth(unitWidth);
        rectangle.setRefractivity(refractivity);
        rectangle.setFileId(fileId);

        LightSource lightSourceObj = new LightSource(lightSource);
        LightSource lightSourceObj1 = new LightSource(lightSource);
        rectangle.getLightSources().add(lightSourceObj);
        rectangle.getLightSources().add(lightSourceObj1);

        return calculatorApi.calculator(rectangle);
    }

    public List<Map<String, Float>> calculatorHaveTable(Float length, Float width, Float high, Float unitLength, Float unitWidth, Float refractivity, String fileId,
                                                        Float[] lightSource, Float tableLength, Float tableWidth, Float tableHeight, Float tbaleCenterX, Float tbaleCenterY,
                                                        Float tbaleCenterZ, String tableFlag) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHigh(high);
        rectangle.setLength(length);
        rectangle.setWidth(width);
        rectangle.setUnitLength(unitLength);
        rectangle.setUnitWidth(unitWidth);
        rectangle.setRefractivity(refractivity);
        rectangle.setFileId(fileId);

        Table table1 = new Table(tableLength, tableWidth, tableHeight, tbaleCenterX, tbaleCenterY, tbaleCenterZ);
        Table table2 = new Table(tableLength, tableWidth, tableHeight, tbaleCenterX + 0.5f, tbaleCenterY + 0.5f, tbaleCenterZ);
        Table table3 = new Table(tableLength, tableWidth, tableHeight, tbaleCenterX + 0.5f, tbaleCenterY + 1f, tbaleCenterZ);
        Table table4 = new Table(tableLength, tableWidth, tableHeight, tbaleCenterX + 1f, tbaleCenterY + 0.5f, tbaleCenterZ);
        Table table5 = new Table(tableLength, tableWidth, tableHeight, tbaleCenterX + 1f, tbaleCenterY + 1f, tbaleCenterZ);
        rectangle.getTables().add(table1);
//		rectangle.getTables().add(table2);
//		rectangle.getTables().add(table3);
//		rectangle.getTables().add(table4);
//		rectangle.getTables().add(table5);


        LightSource lightSourceObj1 = new LightSource(new Float[]{1f, 1f, 2.5f, 0f, 0f, -1f});
        rectangle.getLightSources().add(lightSourceObj1);
//		LightSource lightSourceObj2=new LightSource(new Float[]{1f,2f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj2);
//		LightSource lightSourceObj3=new LightSource(new Float[]{1f,3f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj3);
//
//		LightSource lightSourceObj4=new LightSource(new Float[]{2f,1f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj4);
//		LightSource lightSourceObj5=new LightSource(new Float[]{2f,2f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj5);
//		LightSource lightSourceObj6=new LightSource(new Float[]{2f,3f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj6);
//
//
//		LightSource lightSourceObj7=new LightSource(new Float[]{3f,1f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj7);
//		LightSource lightSourceObj8=new LightSource(new Float[]{3f,2f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj8);
//		LightSource lightSourceObj9=new LightSource(new Float[]{3f,3f,2.5f,0f,0f,-1f});
//		rectangle.getLightSources().add(lightSourceObj9);

        rectangle.setTableFlag(tableFlag);
        return calculatorApi.calculator(rectangle);
    }

    public String fileUpload(MultipartFile multipartFile, Long tenantId) {
//        if ((multipartRequest != null && !multipartRequest.getFileNames().hasNext())) {
//            throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_EMPTY);
//        }
//        String fileId = null;
//        try {
//            MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
//            fileId = this.fileApi.upLoadFile(multipartFile, tenantId);
//            opticalApi.readFile(fileId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (multipartFile == null) {
            throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_EMPTY);
        }
        String fileId = null;
        try {
            fileId = this.fileApi.upLoadFile(multipartFile, tenantId);
            opticalApi.readFile(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileId;
    }

    /**
     * 眩光值计算
     *
     * @param testPoint
     * @param lightPoint
     * @param lightParams
     * @param ies
     */
    public void glareCalculator(Float[] testPoint, List<Float[]> lightPoint, Float[] lightParams, String ies, List<Map<String, Object>> coordinateList) {
        calculatorApi.glareCalculator(new GlareCalculatorReq(testPoint, lightPoint, lightParams, ies, coordinateList));
    }

    /**
     * 眩光值添加到redis中
     *
     * @param multipartFile
     */
    public void addWZZS2Redis(MultipartFile multipartFile) {
        try {
            //从excel中得到位置指数图 然后拼凑 key值 x-y  value 存到redis中
            List<Float[]> excels = resolveExcel(multipartFile);
            Map<String, Float> maps = new HashMap<>();
            for (int y = 1; y < excels.size(); y++) {
                Float[] xArray = excels.get(y);
                for (int x = 1; x < xArray.length; x++) {
                    //拼凑 key值 x-y
                    String key = new Float(x - 1) / 10 + "-" + new Float(y - 1) / 10;
                    maps.put(key, xArray[x]);
                }
            }
            opticalApi.addWZZS2Redis(JSON.toJSONString(maps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Float[]> resolveExcel(MultipartFile file) throws IOException {
        //获得Workbook工作薄对象
        Workbook workbook = ExcelUtils.getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个map返回
        List<Float[]> list = new ArrayList<Float[]>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getLastCellNum();
                    Float[] cells = new Float[row.getLastCellNum()];
                    //循环当前行
                    for (int cellNum = firstCellNum + 1; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = etCellValue(cell);
                    }
                    list.add(cells);
                }
            }
        }
        workbook.close();
        return list;
    }

    private static Float etCellValue(Cell cell) {
        try {
            return new Float(cell.getNumericCellValue());
        } catch (Exception e) {
            return 0f;
        }
    }

}
