package com.iot.building.helper;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import org.springframework.web.multipart.MultipartFile;

public class ExcelUtils {
	
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
	public static final String PROCESSING = "Processing...";

	private static final Logger log = Logger.getLogger(ExcelUtils.class);
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
	    InputStream is = StringUtils.isNoneBlank(target)?readUrlFile(path):new FileInputStream(path);
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
	                XSSFCell name = xssfRow.getCell(0);
	                XSSFCell type = xssfRow.getCell(1);
	                XSSFCell parent = xssfRow.getCell(2);

	                cellData.put("name", getValue(name));
	                cellData.put("type", getValue(type));
	                cellData.put("parent", getValue(parent));

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
	    InputStream is = StringUtils.isNoneBlank(target)?readUrlFile(path):new FileInputStream(path);
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
	                HSSFCell name = hssfRow.getCell(0);
	                HSSFCell type = hssfRow.getCell(1);
	                HSSFCell parent = hssfRow.getCell(2);

	                cellData.put("name", getValue(name));
	                cellData.put("type", getValue(type));
	                cellData.put("parent", getValue(parent));

	                sheetList.add(cellData);
	            }
	        }
	        result.put("sheet_"+numSheet, sheetList);
	    }
	    return result;
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

					cellData.put("name", getValue(name));
					cellData.put("type", getValue(type));
					cellData.put("parent", getValue(parent));

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
		if (hssfCell != null) {
			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
				return String.valueOf(hssfCell.getNumericCellValue());
			} else {
				return String.valueOf(hssfCell.getStringCellValue());
			}
		} else {
			return String.valueOf("");
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

}
