package com.iot.center.utils;


import com.alibaba.fastjson.JSON;
import com.iot.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {
	private static final Logger log = Logger.getLogger(ExcelUtils.class);
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
	public static final String PROCESSING = "Processing...";
	
	/**
	 * read the Excel file
	 * @param path the path of the Excel file
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> readExcel(String path) throws IOException {
	    if (path == null || EMPTY.equals(path)) {
	        return null;
	    } else {
	        String postfix = getPostfix(path);
	        return readExcelResult(postfix, path, "");
	    }
	}
	
	public static Map<String, Object> readExcel(String path, String target) throws IOException {
	    if (path == null || EMPTY.equals(path)) {
	        return null;
	    } else {
        	String postfix = getPostfix(path, target);
        	return readExcelResult(postfix, path, target);
	    }
	}
	
	public static Map<String, Object> readExcelResult(String postfix, String path, String target) throws IOException {
		if (!EMPTY.equals(postfix)) {
            if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                return readXls(path, target);
            } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                return readXlsx(path, target);
            }
        } else {
            System.out.println(path + NOT_EXCEL_FILE);
        }
		return null;
	}
	
	public static String getPostfix(String path, String target){
		String postfix = "";
		try{
			BufferedInputStream bis = new BufferedInputStream(readUrlFile(path));  
			if (POIFSFileSystem.hasPOIFSHeader(bis)) {  
				return OFFICE_EXCEL_2003_POSTFIX;
			} else {
				return OFFICE_EXCEL_2010_POSTFIX;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read the Excel 2010
	 * @param path the path of the excel file
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> readXlsx(String path, String target) throws IOException {
	    System.out.println(PROCESSING + path);
	    InputStream is = StringUtil.isNotBlank(target)?readUrlFile(path):new FileInputStream(path);
	    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
	    
	    Map<String, Object> result = new HashMap<String, Object>();
	    // Read the Sheet
	    for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
	        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
	        if (xssfSheet == null) {
	            continue;
	        }
	        List<Map<String, Object>> sheetList = new ArrayList<Map<String, Object>>();
	        Map<String, Object> cellData = null;
	        // Read the Row
	        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
	            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
	            if (xssfRow != null) {
	            	cellData = new HashMap<String, Object>();
	                XSSFCell build = xssfRow.getCell(0);
	                XSSFCell floor = xssfRow.getCell(1);
	                XSSFCell room = xssfRow.getCell(2);
	                cellData.put("build", getValue(build));
	                cellData.put("floor", getValue(floor));
	                cellData.put("room", getValue(room));
	                sheetList.add(cellData);
	            }
	        }
	        result.put("sheet_"+numSheet, sheetList);
	    }
	    return result;
	}

	/**
	 * Read the Excel 2003-2007
	 * @param path the path of the Excel
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> readXls(String path, String target) throws IOException {
	    System.out.println(PROCESSING + path);
	    InputStream is = StringUtil.isNotBlank(target)?readUrlFile(path):new FileInputStream(path);
	    HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
	    Map<String, Object> result = new HashMap<String, Object>();
	    // Read the Sheet
	    for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	        if (hssfSheet == null) {
	            continue;
	        }
	        List<Map<String, Object>> sheetList = new ArrayList<Map<String, Object>>();
	        Map<String, Object> cellData = null;
	        // Read the Row
	        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	            if (hssfRow != null) {
	            	cellData = new HashMap<String, Object>();
	                HSSFCell businessType = hssfRow.getCell(0);
	                HSSFCell description = hssfRow.getCell(1);
	                HSSFCell productId = hssfRow.getCell(2);
	                HSSFCell isHomeShow = hssfRow.getCell(3);
	                cellData.put("businessType", getValue(businessType));
	                cellData.put("description", getValue(description));
	                cellData.put("productId", getValue(productId));
	                cellData.put("isHomeShow", getValue(isHomeShow));
	                sheetList.add(cellData);
	            }
	        }
	        result.put("sheet_"+numSheet, sheetList);
	    }
	    return result;
	}

	@SuppressWarnings("static-access")
	private static String getValue(XSSFCell xssfRow) {
	    if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
	        return String.valueOf(xssfRow.getBooleanCellValue());
	    } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
	        return String.valueOf(xssfRow.getNumericCellValue());
	    } else {
	        return String.valueOf(xssfRow.getStringCellValue());
	    }
	}

	@SuppressWarnings("static-access")
	private static String getValue(HSSFCell hssfCell) {
	    if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
	        return String.valueOf(hssfCell.getBooleanCellValue());
	    } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
	        return String.valueOf(hssfCell.getNumericCellValue());
	    } else {
	        return String.valueOf(hssfCell.getStringCellValue());
	    }
	}
	
	/**
	 * get postfix of the path
	 * @param path
	 * @return
	 */
	public static String getPostfix(String path) {
	    if (path == null || EMPTY.equals(path.trim())) {
	        return EMPTY;
	    }
	    if (path.contains(POINT)) {
	        return path.substring(path.lastIndexOf(POINT) + 1, path.length());
	    }
	    return EMPTY;
	}
	
	/**
	 * 读取网络文件
	 * @param path
	 */
	public static InputStream readUrlFile(String path) {  
        URL url = null;  
        InputStream is =null;  
        try {  
            url = new URL(path);  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        }  
        try {  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.  
            conn.setDoInput(true);  
            conn.connect();  
            is = conn.getInputStream(); //得到网络返回的输入流  
              
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return is;  
    }
	
	public static void main(String[] args) {
		Map<String, Object> result;
        // 对读取Excel表格标题测试
        try {
        	result = ExcelUtils.readExcel("https://leedarson-bucket-00001.s3.cn-north-1.amazonaws.com.cn/1/xlsx/d1160bc673854d0098b958d32a3ade5b.xlsx?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180604T084814Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Credential=AKIAPHLS3Q5HE2XCECOQ%2F20180604%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Signature=c66015ce3bffcb90854283bd18893151f0a9b3f53e899768a9602a092d259d86", "S3");
        	//result = ExcelUtils.readExcel("C:\\Users\\fenglijian\\Desktop\\space.xlsx");
        	List<Map<String, Object>> dataList = (List<Map<String, Object>>) result.get("sheet_0");
			for(Map<String,Object> theMap:dataList){
				System.out.println(JSON.toJSONString(theMap));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object>  readExcelNew(MultipartFile file) throws IOException {
		//获得Workbook工作薄对象
		Workbook hssfWorkbook = getWorkBook(file);
		Map<String, Object> result = new HashMap<String, Object>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = (HSSFSheet) hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			List<Map<String, Object>> sheetList = new ArrayList<Map<String, Object>>();
			Map<String, Object> cellData = null;
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					cellData = new HashMap<String, Object>();
					HSSFCell name = hssfRow.getCell(0);
					HSSFCell type = hssfRow.getCell(1);
					HSSFCell parent = hssfRow.getCell(2);
					HSSFCell templateName = hssfRow.getCell(3);
					
					cellData.put("name", getCellValue(name));
					cellData.put("type", getCellValue(type));
					cellData.put("parent", getCellValue(parent));
					cellData.put("template_name", getCellValue(templateName));
					sheetList.add(cellData);
				}
			}
			result.put("sheet_"+numSheet, sheetList);
		}
		return result;
	}

	/**
	 * 解析excel
	 * @param file
	 * @return
	 */
	public static List<Map<String, Object>> resolveExcelStr(MultipartFile file) throws IOException{
		//获得Workbook工作薄对象
		Workbook workbook = getWorkBook(file);
		//创建返回对象，把每行中的值作为一个数组，所有行作为一个map返回
		Map<String, Object> result = new HashMap<String, Object>();
		//List<String[]> list = new ArrayList<String[]>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(workbook != null){
			for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
				//获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if(sheet == null){
					continue;
				}
				//获得当前sheet的开始行
				int firstRowNum  = sheet.getFirstRowNum();
				//获得当前sheet的结束行
				int lastRowNum = sheet.getPhysicalNumberOfRows();
				//循环除了第一行的所有行
				for(int rowNum = firstRowNum+1;rowNum < lastRowNum;rowNum++){
					Map<String, Object> cellData =  new HashMap<String, Object>();
					//获得当前行
					Row row = sheet.getRow(rowNum);
					if(row == null){
						continue;
					}
					//获得当前行的开始列
					int firstCellNum = row.getFirstCellNum();
					//获得当前行的列数
					int lastCellNum = row.getLastCellNum();
					String[] cells = new String[row.getLastCellNum()];
					//循环当前行
					for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
						int aa = cellNum;
						Cell cell = row.getCell(cellNum);
						String a = getCellValue(cell);
						cellData.put(cellNum+"", a);
						//cells[cellNum] = getCellValue(cell);
					}
					list.add(cellData);
					//list.add(cells);
					//result.put("result", list);
				}
			}
		}
		return list;
	}

	public static List<String[]> resolveExcel(MultipartFile file) throws IOException{
		//获得Workbook工作薄对象
		Workbook workbook = getWorkBook(file);
		//创建返回对象，把每行中的值作为一个数组，所有行作为一个map返回
		Map<String, Object> result = new HashMap<String, Object>();
		List<String[]> list = new ArrayList<String[]>();
		//List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(workbook != null){
			for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
				//获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if(sheet == null){
					continue;
				}
				//获得当前sheet的开始行
				int firstRowNum  = sheet.getFirstRowNum();
				//获得当前sheet的结束行
				int lastRowNum = sheet.getPhysicalNumberOfRows();
				Map<String, Object> cellData =  new HashMap<String, Object>();
				//循环除了第一行的所有行
				for(int rowNum = firstRowNum+1;rowNum < lastRowNum;rowNum++){
					//获得当前行
					Row row = sheet.getRow(rowNum);
					if(row == null){
						continue;
					}
					//获得当前行的开始列
					int firstCellNum = row.getFirstCellNum();
					//获得当前行的列数
					int lastCellNum = row.getLastCellNum();
					String[] cells = new String[row.getLastCellNum()];
					//循环当前行
					for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
						int aa = cellNum;
						Cell cell = row.getCell(cellNum);
						String a = getCellValue(cell);
						//cellData.put(cellNum+"", a);
						cells[cellNum] = getCellValue(cell);
					}
					//list.add(cellData);
					list.add(cells);
					//result.put("result", list);
				}
			}
		}
		return list;
	}


	public static  Workbook getWorkBook(MultipartFile file) {
		//获得文件名
		String fileName = file.getOriginalFilename();
		//创建Workbook工作薄对象，表示整个excel
		Workbook workbook = null;
		try {
			//获取excel文件的io流
			InputStream is = file.getInputStream();
			//根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
			if(fileName.endsWith("xls")){
				//2003
				workbook = new HSSFWorkbook(is);
			}else if(fileName.endsWith("xlsx")){
				//2007 及2007以上
				workbook = new XSSFWorkbook(is);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return workbook;
	}

	public static String getCellValue(Cell cell){
		String cellValue = "";
		if(cell == null){
			return cellValue;
		}
		//判断数据的类型
		switch (cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC: //数字
				cellValue = stringDateProcess(cell);
				break;
			case Cell.CELL_TYPE_STRING: //字符串
				cellValue = String.valueOf(cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN: //Boolean
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA: //公式
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK: //空值
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR: //故障
				cellValue = "非法字符";
				break;
			default:
				cellValue = "未知类型";
				break;
		}
		return cellValue;
	}
	/**
	 * 时间格式处理
	 * @return
	 * @author Liu Xin Nan
	 * @data 2017年11月27日
	 */
	public static String stringDateProcess(Cell cell){
		String result = new String();
		if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
			SimpleDateFormat sdf = null;
			if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
				sdf = new SimpleDateFormat("HH:mm");
			} else {// 日期
				sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			}
			Date date = cell.getDateCellValue();
			result = sdf.format(date);
		} else if (cell.getCellStyle().getDataFormat() == 58) {
			// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			double value = cell.getNumericCellValue();
			Date date = DateUtil
					.getJavaDate(value);
			result = sdf.format(date);
		} else {
			double value = cell.getNumericCellValue();
			CellStyle style = cell.getCellStyle();
			DecimalFormat format = new DecimalFormat();
			String temp = style.getDataFormatString();
			// 单元格设置成常规
			if (temp.equals("General")) {
				format.applyPattern("#");
			}
			result = format.format(value);
		}

		return result;
	}
}
