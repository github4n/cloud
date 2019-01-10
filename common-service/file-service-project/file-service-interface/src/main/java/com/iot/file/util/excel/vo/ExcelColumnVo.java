package com.iot.file.util.excel.vo;

/**
  * @despriction：key与列名
  * @author  yeshiyuan
  * @created 2018/11/27 15:24
  */
public class ExcelColumnVo {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列key
     */
    private String columnKey;

    /**
     * 列宽
     */
    private Integer columnWith;

    public ExcelColumnVo(String columnName, String columnKey) {
        this.columnName = columnName;
        this.columnKey = columnKey;
        this.columnWith = 256;
    }

    public ExcelColumnVo(String columnName, String columnKey, Integer columnWith) {
        this.columnName = columnName;
        this.columnKey = columnKey;
        this.columnWith = columnWith;
    }

    public Integer getColumnWith() {
        return columnWith;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnKey() {
        return columnKey;
    }
}
