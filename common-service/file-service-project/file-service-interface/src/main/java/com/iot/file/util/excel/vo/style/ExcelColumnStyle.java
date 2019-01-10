package com.iot.file.util.excel.vo.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
  * @despriction：excel列样式
  * @author  yeshiyuan
  * @created 2018/11/27 20:42
  */
public class ExcelColumnStyle {

    /**
     * 字体样式
     */
    private ExcelFontStyle fontStyle;

    /**
     * 边框样式
     */
    private BorderStyle borderStyle;

    /**
     * 边框颜色
     */
    private short borderColor;

    /**
     * 是否自动换行(true:是；false:否)
     */
    private boolean isWrapText;

    /**
     * 水平对齐的样式
     */
    private HorizontalAlignment horizontalAlignment;

    /**
     * 垂直对齐的样式
     */
    private VerticalAlignment verticalAlignment;

    /**
     * 背景颜色
     */
    private short backgroundColor;
}
