package com.iot.shcs.helper;

import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;

public class PageUtils<T> {

    public Page<T> buildPage(PageInfo<T> pageInfo) {
        Page<T> page = new Page<>();
        if (pageInfo != null) {
            page.setStartRow(pageInfo.getStartRow());
            page.setEndRow(pageInfo.getEndRow());
            page.setPageNum(pageInfo.getPageNum());
            page.setPages(pageInfo.getPages());
            page.setPageSize(pageInfo.getPageSize());
            page.setTotal(pageInfo.getTotal());
            page.setResult(pageInfo.getList());
        }
        
        return page;
    }
}
