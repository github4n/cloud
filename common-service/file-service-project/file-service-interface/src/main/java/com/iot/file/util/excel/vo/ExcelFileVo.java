package com.iot.file.util.excel.vo;

/**
  * @despriction：excel文件信息
  * @author  yeshiyuan
  * @created 2018/11/27 16:06
  */
public class ExcelFileVo {

    /** 文件保存路径*/
    private String preFilePath;

    /**保存的文件名称*/
    private String fileName;

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * excel表格title
     */
    private String title;

    public ExcelFileVo() {
    }

    public ExcelFileVo(String preFilePath, String fileName, String sheetName, String title) {
        this.preFilePath = preFilePath;
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.title = title;
    }

    public String getPreFilePath() {
        return preFilePath;
    }

    public void setPreFilePath(String preFilePath) {
        this.preFilePath = preFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
