package com.iot.file.util.excel;

import com.iot.common.util.StringUtil;
import com.iot.file.util.excel.vo.ExcelColumnVo;
import com.iot.file.util.excel.vo.ExcelFileVo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
  * @despriction：excel工具
  * @author  yeshiyuan
  * @created 2018/11/27 14:01
  */
public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    //字体名称
   // private String fontName = "Courier New";


    public static void main(String agrs[]) {
        String filePath = "C:\\";
        String fileName = String.valueOf(Math.random())+".xlsx";
        List<ExcelColumnVo> columnVos = new ArrayList<>();
        columnVos.add(new ExcelColumnVo("key","key",256*10));
        columnVos.add(new ExcelColumnVo("en_US","en_US",256*10));
        columnVos.add(new ExcelColumnVo("zh_CN（中文）","zh_CN",256*10));
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key", "user.aaa");
        map1.put("en_US","aaa");
        map1.put("zh_CN","啊啊啊啊");
        dataList.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("key", 223.23432342);
        map2.put("en_US", new Date());
        map2.put("zh_CN",12.322F);
        dataList.add(map2);
        ExcelFileVo excelFileVo = new ExcelFileVo(filePath, fileName, "文案", null);
        try {
            createExcelFile(excelFileVo, columnVos, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
      * @despriction：初始化表格
      * @author  yeshiyuan
      * @created 2018/11/28 19:43
      */
    public static Workbook init(ExcelFileVo excelFileInfo, List<ExcelColumnVo> columnVos, List<Map<String,Object>> dataList) throws Exception {
        // 创建工作簿对象
        Workbook workbook = null;
        // 判断文件类型如果是xlsx，则使用XSSFWorkbook 对象进行实例化
        // 判断文件类型如果是xls，则使用HSSFWorkbook 对象进行实例化
        if(excelFileInfo.getFileName().contains(".xlsx")){
            //如果是2007的，也就是.xlsx
            workbook = new XSSFWorkbook();
        }else if (excelFileInfo.getFileName().contains(".xls")){
            //如果是2003的，也就是.xls，
            workbook = new HSSFWorkbook();
        }else{
            throw new Exception("file name not written file type");
        }
        // 创建工作表
        Sheet sheet = workbook.createSheet(excelFileInfo.getSheetName() == null ? "sheet" : excelFileInfo.getSheetName());
        int columnNumber = columnVos.size();
        for (int i = 0; i < columnNumber; i++) {
            sheet.setColumnWidth(i, columnVos.get(i).getColumnWith()); // 单独设置每列的宽
        }
        //获取列头样式对象
        CellStyle titleStyle = getTitleStyle(workbook);
        // 1、产生表格标题行
        int rowNum = 0;
        if (excelFileInfo.getTitle() != null || "".equals(excelFileInfo.getTitle())) {
            Row titleRow = sheet.createRow(rowNum++);
            titleRow.setHeight((short) (25 * 35)); //设置高度
            Cell cell1 = titleRow.createCell(0);// 创建标题第一列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNumber - 1)); // 合并第0到第17列
            cell1.setCellValue(excelFileInfo.getTitle()); // 设置值标题

            cell1.setCellStyle(titleStyle); // 设置标题样式
        }
        //2.1、创建表头行
        Row row = sheet.createRow(rowNum++);
        CellStyle textStyle = getTextStyle(workbook);                    //单元格样式对象
        //2.2 创建表头的列
        String[] columsKey = new String[columnNumber];
        for (int i = 0; i < columnNumber; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnVos.get(i).getColumnName());
            columsKey[i] = columnVos.get(i).getColumnKey();
            cell.setCellStyle(textStyle);
        }
        //3、遍历数据，创建单元格，并设置值
        for (int i = 0; i < dataList.size(); i++) {
            row = sheet.createRow(rowNum++);
            Cell datacell = null;
            Map<String, Object> dataMap = dataList.get(i);
            for (int j = 0; j < columnNumber; j++) {
                datacell = row.createCell(j);
                Object value = dataMap.get(columsKey[j]);
                if (value instanceof String) {
                    datacell.setCellValue(value.toString());
                } else if (value instanceof Integer) {
                    datacell.setCellValue((Integer) value);
                } else if (value instanceof Long) {
                    datacell.setCellValue((Long) value);
                } else if (value instanceof Double) {
                    datacell.setCellValue((Double) value);
                } else if (value instanceof Float) {
                    datacell.setCellValue((Float) value);
                } else if (value instanceof Date) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    datacell.setCellValue(simpleDateFormat.format(value));
                }
                datacell.setCellStyle(textStyle);
            }
        }
        return workbook;
    }

    /**
      * @despriction：生成excel文件
      * @author  yeshiyuan
      * @created 2018/11/27 14:03
      */
    public static File createExcelFile(ExcelFileVo excelFileInfo, List<ExcelColumnVo> columnVos, List<Map<String,Object>> dataList) throws Exception {
        if (StringUtil.isBlank(excelFileInfo.getPreFilePath())) {
            throw new Exception("file save path is null");
        }
        FileOutputStream fos = null;
        File excelFile = null;
        Workbook workbook = null;
        try{
            workbook = init(excelFileInfo, columnVos, dataList);
            // 4.生成excel文件
            String filename = excelFileInfo.getFileName();
            logger.info("excel save path:{}", excelFileInfo.getPreFilePath());
            File filePathFile = new File(excelFileInfo.getPreFilePath());
            if (!filePathFile.exists()) {
                filePathFile.mkdirs();
            }
            excelFile = new File(excelFileInfo.getPreFilePath() + File.separator + filename);
            if (!excelFile.exists()) {
                excelFile.createNewFile();
            }
            fos = new FileOutputStream(excelFile);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            logger.error("ExcelUtil -> createExcelFile error", e);
        } catch (IOException e) {
            logger.error("ExcelUtil -> createExcelFile error", e);
        } finally {
            try {
                if (workbook!=null){
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return excelFile;
    }

    /**
      * @despriction：下载excel
      * @author  yeshiyuan
      * @created 2018/11/28 19:51
      */
    public static void downloadExcel(ExcelFileVo excelFileInfo, List<ExcelColumnVo> columnVos, List<Map<String,Object>> dataList, HttpServletResponse response) throws Exception {
        // 创建工作簿对象
        Workbook workbook = null;
        try {
            workbook = init(excelFileInfo, columnVos, dataList);
            //下载文件
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(excelFileInfo.getFileName().getBytes(), "ISO8859-1") );
            workbook.write(response.getOutputStream());
        } catch (FileNotFoundException e) {
            logger.error("ExcelUtil -> downloadExcel error", e);
        } catch (IOException e) {
            logger.error("ExcelUtil -> downloadExcel error", e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
      * @despriction：标题文本样式
      * @author  yeshiyuan
      * @created 2018/11/27 14:08
      */
    public static CellStyle getTitleStyle(Workbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)11);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        CellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(IndexedColors.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置单元格背景颜色
        style.setFillForegroundColor(IndexedColors.WHITE.index);
        //style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        return style;

    }

    /*
   * 列数据信息单元格样式
   */
    public static CellStyle getTextStyle(Workbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        CellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(IndexedColors.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }



}
