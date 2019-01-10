package com.iot.file.util.excel.vo.style;


/**
  * @despriction：excel字体样式
  * @author  yeshiyuan
  * @created 2018/11/27 20:45
  */
public class ExcelFontStyle {

    /**
     * 字体是否加粗(true:是;false:否)
     */
    private boolean isBold;

    /**
     * 字体名字
     */
    private String fontName;

    /**
     * 字体大小
     */
    private short fontSize;

    public ExcelFontStyle() {
    }

    public ExcelFontStyle(boolean isBold, String fontName, short fontSize) {
        this.isBold = isBold;
        this.fontName = fontName;
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public short getFontSize() {
        return fontSize;
    }

    public void setFontSize(short fontSize) {
        this.fontSize = fontSize;
    }
}
