package com.iot.system.vo;
/**
 * 项目名称：立达信IOT云平台
 * 模块名称：公共模块
 * 功能描述：字典信息
 * 创建人： 490485964@qq.com
 * 创建时间：2018年06月28日 16:58
 * 修改人： 490485964@qq.com
 * 修改时间：2018年06月28日 16:58
 */
public class DictItemResp {

    /**
     * 大类型id
     */
    private Short typeId;

    /**
     * 小类型id
     */
    private String itemId;

    /**
     * 小类型名称
     */
    private String itemName;

    /**
     * 小类型描述
     */
    private String itemDesc;

    /**
     * 排序字段
     */
    private Integer itemSort;

    public Short getTypeId() {
        return typeId;
    }

    public void setTypeId(Short typeId) {
        this.typeId = typeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Integer getItemSort() {
        return itemSort;
    }

    public void setItemSort(Integer itemSort) {
        this.itemSort = itemSort;
    }

}