package com.iot.design.dict.vo.resp;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/21
 */
public class DictResp {
    private String dictCode;
    private String dictName;

    public DictResp() {
    }

    public DictResp(String dictCode, String dictName) {
        this.dictCode = dictCode;
        this.dictName = dictName;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
}
